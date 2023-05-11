package com.enhui;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MysqlClient extends JdbcClient {

  public MysqlClient(String ip, int port, String userName, String password) {
    super(ip, port, userName, password);
  }

  @Override
  protected String getJdbcUrlTemplate() {
    return "jdbc:mysql://%s:%s?userunicode=true";
  }

  @Override
  protected String getDriverClassName() {
    return "com.mysql.cj.jdbc.Driver";
  }

  protected Properties getJdbcProperties() {
    Properties properties = new Properties();
    properties.setProperty("useSSL", "false");
    properties.setProperty("autoReconnect", "true");

    properties.setProperty("allowMultiQueries", "true");
    properties.setProperty("rewriteBatchedStatements", "true");
    return properties;
  }

  protected static final String CHECK_TABLE_SQL =
      "SELECT * FROM information_schema.tables WHERE table_schema = '%s' AND table_name = '%s'"
          + " LIMIT 1;";

  protected static final String GET_TABLE_SCHEMA_SQL =
      "SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE (TABLE_SCHEMA,TABLE_NAME) in (%s) order by"
          + " ORDINAL_POSITION";

  protected static final List<String> SYSTEM_DBS =
      Arrays.asList("information_schema", "performance_schema", "mysql", "sys", "sysdb");

  protected static final String GET_TABLE_INDEX_SQL =
      "select * from INFORMATION_SCHEMA.STATISTICS WHERE (TABLE_SCHEMA,TABLE_NAME) in (%s)";

  protected static final String GET_TABLE_DATA_LENGTH_SQL =
      "SELECT TABLE_SCHEMA,TABLE_NAME, round(((data_length + index_length) / 1024 / 1024), 2)"
          + " `表大小(MB)`\n"
          + " FROM information_schema.TABLES WHERE table_schema = '%s' AND table_name = '%s';";

  protected static final String SHOW_ALL_SCHEMA_SQL =
      "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA";

  protected static final String GET_ALL_TABLES_SQL =
      "SELECT * FROM information_schema.TABLES t1 left join"
          + " information_schema.COLLATION_CHARACTER_SET_APPLICABILITY t2 on t1.TABLE_COLLATION ="
          + " t2.COLLATION_NAME ";

  protected static final String GET_ALL_TABLES_SQL_CONDITION =
      "WHERE TABLE_SCHEMA not in ('mysql','information_schema','performance_schema','sys');";

  protected static final String GET_ALL_TABLES_SQL_SINGLE_SCHEMA_CONDITION =
      "WHERE TABLE_SCHEMA = '%s';";

  @Override
  public void insert(Table table, List<TestData> list) throws SQLException {
    final TestData testData = list.get(0);
    String sql =
        String.format(
            "insert into %s (%s) VALUES (%s)",
            table.getSchema() + "." + table.getName(),
            String.join(",", testData.getColNames()),
            IntStream.range(0, testData.getColNames().size())
                .mapToObj(index -> "?")
                .collect(Collectors.joining(",")));
    try (final Connection connection = dataSource.getConnection();
        final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      for (final TestData data : list) {
        for (int j = 0; j < data.getColNames().size(); j++) {
          if ("INT".equals(data.getColTypes().get(j))) {
            preparedStatement.setInt(j + 1, Integer.parseInt(data.getColVals().get(j)));
          } else {
            preparedStatement.setString(j + 1, data.getColVals().get(j));
          }
        }
        preparedStatement.addBatch();
      }
      preparedStatement.executeBatch();
    }
  }

  public boolean tableExists(String dbName, String tableName) throws SQLException {
    boolean result = false;
    try (Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement()) {
      final ResultSet resultSet =
          statement.executeQuery(String.format(CHECK_TABLE_SQL, dbName, tableName));
      if (resultSet.next()) {
        result = true;
      }
    }
    return result;
  }

  @Override
  public void truncateTable(Table table) throws SQLException {
    try (final Connection connection = dataSource.getConnection();
        final Statement statement = connection.createStatement()) {
      statement.execute(String.format("truncate table %s.%s", table.getSchema(), table.getName()));
    }
  }

  public Map<String, List<EntityField>> getEntityFieldsMap(Collection<Table> tables)
      throws SQLException {
    Map<String, List<EntityField>> tableFieldsMap = new HashMap<>(tables.size());
    Map<String, Table> statementTableMap =
        tables.stream()
            .collect(
                Collectors.toMap(
                    table -> String.format("('%s', '%s')", table.getSchema(), table.getName()),
                    Function.identity()));
    try (Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement()) {
      //      statement.execute("USE INFORMATION_SCHEMA");
      ResultSet rs =
          statement.executeQuery(
              String.format(GET_TABLE_SCHEMA_SQL, String.join(",", statementTableMap.keySet())));
      while (rs.next()) {
        String tableSchema = rs.getString("TABLE_SCHEMA");
        String tableName = rs.getString("TABLE_NAME");
        Table table = statementTableMap.get(String.format("('%s', '%s')", tableSchema, tableName));
        if (table == null) {
          table = Table.ofSchema(tableSchema, tableName);
        }
        String fieldName = rs.getString("COLUMN_NAME");
        boolean nullable = rs.getString("IS_NULLABLE").equals("YES");
        String dataType = rs.getString("DATA_TYPE");
        String defaultValue = rs.getString("COLUMN_DEFAULT");
        if (defaultValue != null) {
          if (dataType.equalsIgnoreCase("datetime") || dataType.equalsIgnoreCase("timestamp")) {
            if (!defaultValue.toUpperCase().contains("CURRENT_TIMESTAMP")) {
              defaultValue = String.format("'%s'", defaultValue);
            }
          } else {
            defaultValue = String.format("'%s'", defaultValue);
          }
        }
        String comment = rs.getString("COLUMN_COMMENT");

        EntityField.EntityFieldBuilder tableFieldBuilder =
            new EntityField.EntityFieldBuilder()
                .name(fieldName)
                .type(dataType)
                .optional(nullable)
                .defaultValue(defaultValue)
                .comment(comment == null ? "" : comment);
        if (rs.getObject("CHARACTER_MAXIMUM_LENGTH") != null) {
          tableFieldBuilder.precision(rs.getLong("CHARACTER_MAXIMUM_LENGTH"));
        }
        if (rs.getObject("NUMERIC_PRECISION") != null) {
          tableFieldBuilder.precision(rs.getLong("NUMERIC_PRECISION"));
        }
        if (rs.getObject("NUMERIC_SCALE") != null) {
          tableFieldBuilder.scale(rs.getLong("NUMERIC_SCALE"));
        }
        if (rs.getObject("DATETIME_PRECISION") != null) {
          tableFieldBuilder.precision(rs.getLong("DATETIME_PRECISION"));
        }
        String colType = rs.getString("COLUMN_TYPE");
        if (colType.toLowerCase().contains("unsigned")) {
          tableFieldBuilder.type(dataType + " unsigned");
        }
        String charsetAlias = rs.getString("CHARACTER_SET_NAME");
        tableFieldBuilder.charset(charsetAlias);

        tableFieldsMap
            .computeIfAbsent(table.getFullName(), k -> new ArrayList<>())
            .add(tableFieldBuilder.build());
      }
    }
    return tableFieldsMap;
  }

  public List<String> getAllDatabases() {
    try {
      List<String> schemas = new ArrayList<>(100);
      try (Connection connection = dataSource.getConnection();
          Statement statement = connection.createStatement()) {
        final ResultSet resultSet = statement.executeQuery(SHOW_ALL_SCHEMA_SQL);
        while (resultSet.next()) {
          schemas.add(resultSet.getString("SCHEMA_NAME"));
        }
      }
      schemas.removeAll(SYSTEM_DBS);
      return schemas;
    } catch (Exception e) {
      throw new RuntimeException("查询schema失败", e);
    }
  }

  /**
   * SELECT table_name AS `表名`, round(((data_length + index_length) / 1024 / 1024), 2) `表大小(MB)`
   * FROM information_schema.TABLES WHERE table_schema = '[数据库名]' AND table_name = '[表名]';
   *
   * @param table
   * @return
   */
  public EntityWeight entityWeights(Table table) {
    EntityWeight entityWeight = new EntityWeight();
    try {
      try (Connection connection = dataSource.getConnection();
          Statement statement = connection.createStatement()) {
        final ResultSet rs =
            statement.executeQuery(
                String.format(GET_TABLE_DATA_LENGTH_SQL, table.getSchema(), table.getName()));
        while (rs.next()) {
          double tableSize = rs.getDouble("表大小(MB)");
          entityWeight.setTable(table);
          entityWeight.setWeight(tableSize);
        }
      }
      return entityWeight;
    } catch (Exception e) {
      throw new RuntimeException("获取表权重信息查询失败", e);
    }
  }

  public List<Table> getAllTables(String database) throws SQLException {
    try {
      List<Table> tables = new ArrayList<>(100);
      try (final Connection connection = dataSource.getConnection();
          final Statement statement = connection.createStatement()) {
        final ResultSet rst =
            statement.executeQuery(
                GET_ALL_TABLES_SQL
                    + (isBlank(database)
                        ? GET_ALL_TABLES_SQL_CONDITION
                        : String.format(GET_ALL_TABLES_SQL_SINGLE_SCHEMA_CONDITION, database)));
        while (rst.next()) {
          String comment = rst.getString("TABLE_COMMENT");
          Table table = Table.ofSchema(rst.getString("TABLE_SCHEMA"), rst.getString("TABLE_NAME"));
          table.setComment(comment == null ? "" : comment);
          tables.add(table);
          if (Objects.equals(rst.getString("TABLE_TYPE"), "VIEW")) {
            table.setType(Table.EntityType.VIEW);
          }
        }
      }
      return tables;
    } catch (Exception e) {
      throw new RuntimeException("获取表名信息查询失败", e);
    }
  }
}
