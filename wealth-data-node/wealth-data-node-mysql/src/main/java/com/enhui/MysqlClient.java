package com.enhui;

import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class MysqlClient extends JdbcClient {

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
      "SELECT TABLE_SCHEMA,TABLE_NAME,SUM(DATA_LENGTH) AS SIZE FROM INFORMATION_SCHEMA.TABLES WHERE"
          + " (TABLE_SCHEMA,TABLE_NAME) in (%s) GROUP BY TABLE_SCHEMA, TABLE_NAME";

  protected static final String SHOW_ALL_SCHEMA_SQL =
      "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA";

  protected static final String GET_ALL_TABLES_SQL =
      "SELECT * FROM information_schema.TABLES t1 left join"
          + " information_schema.COLLATION_CHARACTER_SET_APPLICABILITY t2 on t1.TABLE_COLLATION ="
          + " t2.COLLATION_NAME ";

  @Override
  protected String getJdbcUrl() {
    return null;
  }

  @Override
  protected String getDriverClassName() {
    return null;
  }

  @Override
  protected String getUserName() {
    return null;
  }

  @Override
  protected String getPassword() {
    return null;
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
      statement.execute("USE INFORMATION_SCHEMA");
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
        tableFieldBuilder.charset(Charset.forName(charsetAlias));

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

  protected Map<String, List<EntityIndex>> getIndices(String tableCondition) throws SQLException {
    Map<String, List<EntityIndex>> tableIndexMap = new HashMap<>(100, 1);
    try (Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement()) {
      final ResultSet rs =
          statement.executeQuery(String.format(GET_TABLE_INDEX_SQL, tableCondition));
      while (rs.next()) {
        String tableSchema = rs.getString("TABLE_SCHEMA");
        String tableName = rs.getString("TABLE_NAME");
        String fullTableName = Table.ofSchema(tableSchema, tableName).getFullName();
        String fieldName = rs.getString("COLUMN_NAME");
        try {
          // 5.7之前版本没这字段
          String expression = rs.getString("EXPRESSION");
          if (fieldName == null && expression != null) {
            continue;
          }
        } catch (Exception e) {
          log.warn("数据节点的版本无此字段：EXPRESSION", e);
        }
        boolean unique = rs.getInt("NON_UNIQUE") == 0;
        String indexName = rs.getString("INDEX_NAME");
        boolean primary = "PRIMARY".equals(indexName);
        String collation = rs.getString("COLLATION");
        boolean isDesc = "D".equals(collation);
        tableIndexMap
            .computeIfAbsent(fullTableName, k -> new ArrayList<>())
            .add(
                new EntityIndex(
                    indexName,
                    fieldName,
                    Integer.valueOf(rs.getString("SEQ_IN_INDEX")),
                    StringUtils.isBlank(collation) ? null : (isDesc ? "DESC" : "ASC"),
                    unique,
                    primary,
                    rs.getString("INDEX_TYPE")));
      }
    }
    return tableIndexMap;
  }
}
