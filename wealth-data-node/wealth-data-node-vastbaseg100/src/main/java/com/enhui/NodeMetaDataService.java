package com.enhui;

import static com.enhui.model.AllTypeTableColumn.ALL_COLUMN_TYPE;

import com.enhui.model.AllTypeTableColumn;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.postgresql.jdbc.PgConnection;

public class NodeMetaDataService {

  @Test
  public void createAllTypeTable() throws SQLException, ClassNotFoundException {
    initDatabase();

    createAllTypeTable(
        AllTypeTableColumn.ORACLE_DATABASE,
        AllTypeTableColumn.NotSupportedType.ORACLE,
        "heh_oracle_all_type1");

    createAllTypeTable(
        AllTypeTableColumn.MYSQL_DATABASE,
        AllTypeTableColumn.NotSupportedType.MYSQL,
        "heh_mysql_all_type1");

    createAllTypeTable(
        AllTypeTableColumn.TERADATA_DATABASE,
        AllTypeTableColumn.NotSupportedType.TERADATA,
        "heh_teradata_all_type1");

    createAllTypeTable(
        AllTypeTableColumn.PG_DATABASE, AllTypeTableColumn.NotSupportedType.PG, "heh_pg_all_type1");
  }

  private void createAllTypeTable(
      String database, AllTypeTableColumn.NotSupportedType notSupportedType, String tableName)
      throws SQLException, ClassNotFoundException {
    try (PgConnection conn = NodeService.getSlaveConn(database);
        Statement statement = conn.createStatement()) {
      AtomicInteger nameI = new AtomicInteger(1);
      final List<AllTypeTableColumn> supportedList =
          ALL_COLUMN_TYPE.stream()
              .filter(
                  t ->
                      t.getNotSupportedTypes() == null
                          || !t.getNotSupportedTypes().contains(notSupportedType))
              .collect(Collectors.toList());

      final List<String> notSupportedList =
          ALL_COLUMN_TYPE.stream()
              .filter(
                  t ->
                      t.getNotSupportedTypes() != null
                          && t.getNotSupportedTypes().contains(notSupportedType))
              .map(AllTypeTableColumn::getType)
              .collect(Collectors.toList());
      System.out.println("vastbase g100的" + notSupportedType + "兼容模式 不支持的类型：" + notSupportedList);
      final AtomicInteger finalNameI = nameI;
      String columnSql =
          supportedList.stream()
              .map(
                  colu ->
                      String.format(
                          "%s %s", colu.getName() + finalNameI.getAndIncrement(), colu.getType()))
              .collect(Collectors.joining(",\n"));

      String createSql = String.format("CREATE TABLE public." + tableName + "(%s);", columnSql);
      //      System.out.println("vastbase g100的" + notSupportedType + "兼容模式 全类型建表语句：" + createSql);
      try {
        statement.execute("drop table public." + tableName);
      } catch (Exception ignore) {
      }
      statement.execute(createSql);

      // 插入数据
      nameI = new AtomicInteger(1);
      AtomicInteger finalNameI1 = nameI;
      final String names =
          supportedList.stream()
              .map(colu -> colu.getName() + finalNameI1.getAndIncrement())
              .collect(Collectors.joining(","));

      final AtomicInteger valueI = new AtomicInteger(1);
      final String values =
          supportedList.stream()
              .map(
                  o -> "".equals(o.getValue()) ? "'" + valueI.get() + "'" : o.getValue().toString())
              .collect(Collectors.joining(","));
      String insertSql =
          String.format("INSERT INTO public.%s (%s) VALUES (%s)", tableName, names, values);
      //      System.out.println(insertSql);
      statement.execute(insertSql);
    }
  }

  /**
   * 创建不同兼容模式的数据库，已存在则忽略
   *
   * @throws SQLException
   * @throws ClassNotFoundException
   */
  private void initDatabase() throws SQLException, ClassNotFoundException {
    try (PgConnection slaveConn = NodeService.getSlaveConn(null);
        Statement statement = slaveConn.createStatement()) {
      List<String> databases = new ArrayList<>();
      ResultSet resultSet = statement.executeQuery("select * from pg_database");
      while (resultSet.next()) {
        final String s = resultSet.getString(1) + "--" + resultSet.getString(12);
        //                System.out.println(s);
        databases.add(s);
      }
      // DBCOMPATIBILITY [ = ] compatibility_type
      // 指定兼容的数据库的类型，默认兼容O。
      // 取值范围：A、B、C、PG。分别表示兼容Oracle、MySQL、Teradata和POSTGRES。
      if (!databases.contains(AllTypeTableColumn.ORACLE_DATABASE + "--A")) {
        System.out.println("创建数据库：" + AllTypeTableColumn.ORACLE_DATABASE);
        statement.execute(
            "CREATE DATABASE "
                + AllTypeTableColumn.ORACLE_DATABASE
                + " WITH DBCOMPATIBILITY = 'A'");
      }
      if (!databases.contains(AllTypeTableColumn.MYSQL_DATABASE + "--B")) {
        System.out.println("创建数据库：" + AllTypeTableColumn.MYSQL_DATABASE);
        statement.execute(
            "CREATE DATABASE " + AllTypeTableColumn.MYSQL_DATABASE + " WITH DBCOMPATIBILITY = 'B'");
      }
      if (!databases.contains(AllTypeTableColumn.TERADATA_DATABASE + "--C")) {
        System.out.println("创建数据库：" + AllTypeTableColumn.TERADATA_DATABASE);
        statement.execute(
            "CREATE DATABASE "
                + AllTypeTableColumn.TERADATA_DATABASE
                + " WITH DBCOMPATIBILITY = 'C'");
      }
      if (!databases.contains(AllTypeTableColumn.PG_DATABASE + "--PG")) {
        System.out.println("创建数据库：" + AllTypeTableColumn.PG_DATABASE);
        statement.execute(
            "CREATE DATABASE " + AllTypeTableColumn.PG_DATABASE + " WITH DBCOMPATIBILITY = 'PG'");
      }
    }
  }

  @Test
  public void testDesc() throws SQLException, ClassNotFoundException {
    String database = "mysql_base";
    String schema = "public";
    String table = "heh_mysql_all_type1";
    final String sql = descTableSql(true, database, schema, table);
    AllTypeTableColumn.NotSupportedType notSupportedType =
        AllTypeTableColumn.NotSupportedType.MYSQL;

    List<AllTypeTableColumn> supportedList =
        ALL_COLUMN_TYPE.stream()
            .filter(
                t ->
                    t.getNotSupportedTypes() == null
                        || !t.getNotSupportedTypes().contains(notSupportedType))
            .collect(Collectors.toList());

    int i = 0;
    Map<String, String> map = new LinkedHashMap<>();
    try (PgConnection conn = NodeService.getSlaveConn(database);
        Statement statement = conn.createStatement()) {
      final ResultSet resultSet = statement.executeQuery(sql);
      while (resultSet.next()) {
        final String columnName = resultSet.getString("column_name");
        final String columnType = resultSet.getString("raw_type");
        map.put(columnName, columnType);
        System.out.println(columnName + " " + supportedList.get(i++).getType() + " " + columnType);
      }
    }
  }

  public String descTableSql(boolean useNew, String database, String schemaName, String tableName) {
    String descSql = useNew ? NEW_SHOW_TABLE_DESC_SQL : SHOW_TABLE_DESC_SQL;
    final String schemaAndTable = String.format("('%s','%s')", schemaName, tableName);
    return String.format(descSql, database, schemaAndTable);
  }

  protected static final String SHOW_TABLE_DESC_SQL =
      "select distinct isc.table_catalog, isc.table_schema, isc.table_name, isc.ordinal_position,"
          + " isc.column_name, isc.is_nullable, isc.character_maximum_length,"
          + " isc.numeric_precision, isc.numeric_scale, isc.datetime_precision, isc.column_default,"
          + " isc.udt_name as raw_type, kcu.column_name as primary_key, des.comment\n"
          + "from pg_catalog.pg_statio_all_tables st right join INFORMATION_SCHEMA.COLUMNS isc on"
          + " (isc.table_schema = st.schemaname and isc.table_name = st.relname)\n"
          + "left join INFORMATION_SCHEMA.KEY_COLUMN_USAGE kcu on (kcu.table_catalog ="
          + " isc.table_catalog and kcu.table_schema = isc.table_schema and kcu.table_name ="
          + " isc.table_name and kcu.column_name = isc.column_name)\n"
          + "left join ( select col_description(a.attrelid, a.attnum) as comment, a.attname as"
          + " name, c.relname from pg_class as c, pg_attribute as a where a.attrelid = c.oid and"
          + " a.attnum>0) des on des.relname = isc.table_name and des.name = isc.column_name where"
          + " isc.table_catalog = '%s' and (isc.table_schema, isc.table_name) in (%s)\n"
          + "order by isc.ordinal_position";

  private static final String NEW_SHOW_TABLE_DESC_SQL =
      "SELECT current_database() AS table_catalog,pgn.nspname as table_schema, pgc.relname as"
          + " table_name, pga.attnum as ordinal_position, pga.attname as column_name,  CASE\n"
          + "    WHEN pga.attnotnull OR t.typtype = 'd'::\"char\" AND t.typnotnull THEN 'NO'::text"
          + " ELSE 'YES'::text END::information_schema.yes_or_no                                   "
          + "                                                        AS is_nullable,   "
          + " information_schema._pg_char_max_length(information_schema._pg_truetypid(pga.*, t.*), "
          + "                                          information_schema._pg_truetypmod(pga.*,"
          + " t.*))::information_schema.cardinal_number         AS character_maximum_length,    "
          + " information_schema._pg_numeric_precision(information_schema._pg_truetypid(pga.*,"
          + " t.*),\n"
          + "                                             information_schema._pg_truetypmod(pga.*,"
          + " t.*))::information_schema.cardinal_number       AS numeric_precision,\n"
          + "    information_schema._pg_numeric_scale(information_schema._pg_truetypid(pga.*,"
          + " t.*),\n"
          + "                                         information_schema._pg_truetypmod(pga.*,"
          + " t.*))::information_schema.cardinal_number           AS numeric_scale,\n"
          + "    information_schema._pg_datetime_precision(information_schema._pg_truetypid(pga.*,"
          + " t.*),\n"
          + "                                              information_schema._pg_truetypmod(pga.*,"
          + " t.*))::information_schema.cardinal_number      AS datetime_precision,\n"
          + "    pg_get_expr(def.adbin, def.adrelid)::information_schema.character_data            "
          + "                                                AS column_default,\n"
          + "    t.typname::information_schema.sql_identifier                                      "
          + "                        AS raw_type,\n"
          + "    col_description(pga.attrelid, pga.attnum) as comment\n"
          + "FROM\n"
          + "    pg_namespace pgn\n"
          + "        JOIN\n"
          + "    pg_class pgc on pgn.oid = pgc.relnamespace\n"
          + "        JOIN\n"
          + "    pg_attribute pga on pgc.oid = pga.attrelid\n"
          + "        JOIN\n"
          + "    pg_type t on pga.atttypid = t.oid\n"
          + "        LEFT JOIN\n"
          + "    pg_attrdef def on pga.attnum = def.adnum and pga.attrelid = def.adrelid\n"
          + "WHERE\n"
          + "        pgc.relkind IN ('r', 'v') \n"
          + "  AND current_database() = '%s'         AND (pgn.nspname,pgc.relname) IN (%s)\n"
          + "        AND pga.attnum > 0\n"
          + "        ORDER BY table_name,ordinal_position;";
}
