package com.enhui.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AllTypeTableColumn {
  public static String ORACLE_DATABASE = "oracle_base";
  public static String MYSQL_DATABASE = "mysql_base";
  public static String TERADATA_DATABASE = "teradata_base";
  public static String PG_DATABASE = "pg_base";

  public enum NotSupportedType {
    ORACLE,
    MYSQL,
    TERADATA,
    PG
  }

  String name;
  String type;
  Object value;
  List<NotSupportedType> notSupportedTypes;

  /** 全类型表 */
  public static List<AllTypeTableColumn> ALL_COLUMN_TYPE = new ArrayList<>();

  static {
    // https://docs.vastdata.com.cn/zh/docs/VastbaseG100Ver2.2.10/doc/%E5%BC%80%E5%8F%91%E8%80%85%E6%8C%87%E5%8D%97/%E6%95%B0%E6%8D%AE%E7%B1%BB%E5%9E%8B.html
    // 日期时间类型
    ALL_COLUMN_TYPE.add(AllTypeTableColumn.builder().name("col").type("DATE").value("date '10-10-2010'").build());
    ALL_COLUMN_TYPE.add(AllTypeTableColumn.builder().name("col").type("TIME").value("'21:21:21'").build());
    ALL_COLUMN_TYPE.add(AllTypeTableColumn.builder().name("col").type("TIMETZ").value("'21:21:21 pst'").build());
    ALL_COLUMN_TYPE.add(
        AllTypeTableColumn.builder().name("col").type("TIMESTAMP").value("'2010-12-12'").build());
    ALL_COLUMN_TYPE.add(
        AllTypeTableColumn.builder().name("col").type("TIMESTAMPTZ").value("'2013-12-11 pst'").build());
    ALL_COLUMN_TYPE.add(
        AllTypeTableColumn.builder().name("col").type("SMALLDATETIME").value("'2003-04-12 04:05:06'").build());
    ALL_COLUMN_TYPE.add(
        AllTypeTableColumn.builder()
            .name("col")
            .type("INTERVAL DAY (6) TO SECOND (6)")
            .value("INTERVAL '3' DAY")
            .build());
    ALL_COLUMN_TYPE.add(
        AllTypeTableColumn.builder().name("col").type("INTERVAL YEAR").value("interval '2' year").build());
    ALL_COLUMN_TYPE.add(AllTypeTableColumn.builder().name("col").type("reltime").value("'90'").build());
    ALL_COLUMN_TYPE.add(AllTypeTableColumn.builder().name("col").type("abstime").value("'1901-12-13 20:45:53 GMT'").build());
    ALL_COLUMN_TYPE.add(AllTypeTableColumn.builder().name("col").type("oradate").value("date '10-10-2010'").build());

    // 数值类型
    ALL_COLUMN_TYPE.add(AllTypeTableColumn.builder().name("col").type("TINYINT").value("").build());
    ALL_COLUMN_TYPE.add(
        AllTypeTableColumn.builder().name("col").type("SMALLINT").value("").build());
    ALL_COLUMN_TYPE.add(AllTypeTableColumn.builder().name("col").type("INTEGER").value("").build());
    ALL_COLUMN_TYPE.add(
        AllTypeTableColumn.builder().name("col").type("BINARY_INTEGER").value("").build());
    ALL_COLUMN_TYPE.add(AllTypeTableColumn.builder().name("col").type("BIGINT").value("").build());
    ALL_COLUMN_TYPE.add(
        AllTypeTableColumn.builder()
            .name("col")
            .type("int16")
            .value("")
            .notSupportedTypes(Arrays.asList(NotSupportedType.ORACLE, NotSupportedType.MYSQL, NotSupportedType.TERADATA, NotSupportedType.PG))
            .build());
    ALL_COLUMN_TYPE.add(
        AllTypeTableColumn.builder().name("col").type("NUMERIC(10,5)").value("").build());
    ALL_COLUMN_TYPE.add(
        AllTypeTableColumn.builder().name("col").type("DECIMAL(10,5)").value("").build());
    ALL_COLUMN_TYPE.add(
        AllTypeTableColumn.builder().name("col").type("NUMBER(10,5)").value("").build());

    // mysql 模式下，只能有一个递增列
    ALL_COLUMN_TYPE.add(
        AllTypeTableColumn.builder()
            .name("col")
            .type("SMALLSERIAL")
            .value("")
            .notSupportedTypes(Arrays.asList(NotSupportedType.MYSQL))
            .build());
    ALL_COLUMN_TYPE.add(
        AllTypeTableColumn.builder()
            .name("col")
            .type("SERIAL")
            .value("")
            .notSupportedTypes(Arrays.asList(NotSupportedType.MYSQL))
            .build());
    ALL_COLUMN_TYPE.add(
        AllTypeTableColumn.builder()
            .name("col")
            .type("BIGSERIAL")
            .value("")
            .notSupportedTypes(Arrays.asList(NotSupportedType.MYSQL))
            .build());
    ALL_COLUMN_TYPE.add(
        AllTypeTableColumn.builder().name("col").type("LARGESERIAL").value("").build());

    ALL_COLUMN_TYPE.add(AllTypeTableColumn.builder().name("col").type("REAL").value("").build());
    ALL_COLUMN_TYPE.add(AllTypeTableColumn.builder().name("col").type("FLOAT4").value("").build());
    ALL_COLUMN_TYPE.add(
        AllTypeTableColumn.builder().name("col").type("DOUBLE PRECISION").value("").build());
    ALL_COLUMN_TYPE.add(
        AllTypeTableColumn.builder().name("col").type("BINARY_DOUBLE").value("").build());
    ALL_COLUMN_TYPE.add(AllTypeTableColumn.builder().name("col").type("FLOAT8").value("").build());
    ALL_COLUMN_TYPE.add(
        AllTypeTableColumn.builder().name("col").type("FLOAT(53)").value("").build());
    ALL_COLUMN_TYPE.add(
        AllTypeTableColumn.builder().name("col").type("DEC(100,30)").value("").build());
    ALL_COLUMN_TYPE.add(
        AllTypeTableColumn.builder().name("col").type("INTEGER(100,30)").value("").build());

    // 货币类型
    ALL_COLUMN_TYPE.add(AllTypeTableColumn.builder().name("col").type("money").value("").build());
    // 布尔类型
    ALL_COLUMN_TYPE.add(AllTypeTableColumn.builder().name("col").type("BOOLEAN").value("true").build());
    // 字符类型
    ALL_COLUMN_TYPE.add(
        AllTypeTableColumn.builder().name("col").type("CHAR(255)").value("").build());
    ALL_COLUMN_TYPE.add(
        AllTypeTableColumn.builder().name("col").type("CHARACTER(255)").value("").build());
    ALL_COLUMN_TYPE.add(
        AllTypeTableColumn.builder().name("col").type("NCHAR(255)").value("").build());
    ALL_COLUMN_TYPE.add(
        AllTypeTableColumn.builder().name("col").type("VARCHAR(255)").value("").build());
    ALL_COLUMN_TYPE.add(
        AllTypeTableColumn.builder().name("col").type("CHARACTER VARYING(255)").value("").build());
    ALL_COLUMN_TYPE.add(
        AllTypeTableColumn.builder().name("col").type("VARCHAR2(255)").value("").build());
    ALL_COLUMN_TYPE.add(
        AllTypeTableColumn.builder().name("col").type("NVARCHAR2(255)").value("").build());
    ALL_COLUMN_TYPE.add(
        AllTypeTableColumn.builder().name("col").type("NVARCHAR(255)").value("").build());
    ALL_COLUMN_TYPE.add(AllTypeTableColumn.builder().name("col").type("TEXT").value("").build());
    ALL_COLUMN_TYPE.add(AllTypeTableColumn.builder().name("col").type("CLOB").value("").build());
    ALL_COLUMN_TYPE.add(AllTypeTableColumn.builder().name("col").type("bpchar").value("").build());
    ALL_COLUMN_TYPE.add(AllTypeTableColumn.builder().name("col").type("name").value("").build());
    ALL_COLUMN_TYPE.add(AllTypeTableColumn.builder().name("col").type("char").value("").build());

    // 二进制类型
    ALL_COLUMN_TYPE.add(AllTypeTableColumn.builder().name("col").type("BLOB").value("").build());
    ALL_COLUMN_TYPE.add(AllTypeTableColumn.builder().name("col").type("RAW").value("").build());
    ALL_COLUMN_TYPE.add(AllTypeTableColumn.builder().name("col").type("BYTEA").value("").build());
    ALL_COLUMN_TYPE.add(
        AllTypeTableColumn.builder()
            .name("col")
            .type("BYTEAWITHOUTORDERWITHEQUALCOL")
            .value("")
            .notSupportedTypes(Arrays.asList(NotSupportedType.ORACLE, NotSupportedType.MYSQL, NotSupportedType.TERADATA, NotSupportedType.PG))
            .build());
    ALL_COLUMN_TYPE.add(
        AllTypeTableColumn.builder()
            .name("col")
            .type("BYTEAWITHOUTORDERCOL")
            .value("")
            .notSupportedTypes(Arrays.asList(NotSupportedType.ORACLE, NotSupportedType.MYSQL, NotSupportedType.TERADATA, NotSupportedType.PG))
            .build());
    ALL_COLUMN_TYPE.add(
        AllTypeTableColumn.builder()
            .name("col")
            .type("_BYTEAWITHOUTORDERWITHEQUALCOL")
            .value("")
            .notSupportedTypes(Arrays.asList(NotSupportedType.ORACLE, NotSupportedType.MYSQL, NotSupportedType.TERADATA, NotSupportedType.PG))
            .build());
    ALL_COLUMN_TYPE.add(
        AllTypeTableColumn.builder()
            .name("col")
            .type("_BYTEAWITHOUTORDERCOL")
            .value("")
            .notSupportedTypes(Arrays.asList(NotSupportedType.ORACLE, NotSupportedType.MYSQL, NotSupportedType.TERADATA, NotSupportedType.PG))
            .build());

    // 几何类型
    ALL_COLUMN_TYPE.add(AllTypeTableColumn.builder().name("col").type("point").value("point '(2.0,0)'").build());
    ALL_COLUMN_TYPE.add(
        AllTypeTableColumn.builder()
            .name("col")
            .type("line")
            .value("")
            .notSupportedTypes(Arrays.asList(NotSupportedType.ORACLE, NotSupportedType.MYSQL, NotSupportedType.TERADATA, NotSupportedType.PG))
            .build());
    ALL_COLUMN_TYPE.add(AllTypeTableColumn.builder().name("col").type("lseg").value("lseg(point '(-1,0)', point '(1,0)')").build());
    ALL_COLUMN_TYPE.add(AllTypeTableColumn.builder().name("col").type("box").value("box(circle '((0,0),2.0)')").build());
    ALL_COLUMN_TYPE.add(AllTypeTableColumn.builder().name("col").type("path").value("path(polygon '((0,0),(1,1),(2,0))')").build());
    ALL_COLUMN_TYPE.add(AllTypeTableColumn.builder().name("col").type("polygon").value("polygon(box '((0,0),(1,1))')").build());
    ALL_COLUMN_TYPE.add(AllTypeTableColumn.builder().name("col").type("circle").value("circle(box '((0,0),(1,1))')").build());
    // 网络地址类型
    ALL_COLUMN_TYPE.add(AllTypeTableColumn.builder().name("col").type("cidr").value("").build());
    ALL_COLUMN_TYPE.add(AllTypeTableColumn.builder().name("col").type("inet").value("inet '192.168.1.5'").build());
    ALL_COLUMN_TYPE.add(AllTypeTableColumn.builder().name("col").type("macaddr").value("macaddr '12:34:56:78:90:ab'").build());
    // 位串类型
    ALL_COLUMN_TYPE.add(AllTypeTableColumn.builder().name("col").type("bit(3)").value("B'101'").build());
    ALL_COLUMN_TYPE.add(
        AllTypeTableColumn.builder().name("col").type("bit varying(5)").value("B'00'").build());
    // 文本搜索类型
    ALL_COLUMN_TYPE.add(
        AllTypeTableColumn.builder().name("col").type("tsvector").value("").build());
    ALL_COLUMN_TYPE.add(AllTypeTableColumn.builder().name("col").type("tsquery").value("").build());
    // UUID类型
    ALL_COLUMN_TYPE.add(AllTypeTableColumn.builder().name("col").type("UUID").value("'A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A11'").build());
    // HLL数据类型
    ALL_COLUMN_TYPE.add(AllTypeTableColumn.builder().name("col").type("hll").value("hll_empty()").build());
    // 范围类型
    ALL_COLUMN_TYPE.add(
        AllTypeTableColumn.builder().name("col").type("int4range").value("int4range(15, 25)").build());
    ALL_COLUMN_TYPE.add(
        AllTypeTableColumn.builder().name("col").type("int8range").value("int8range(15, 25)").build());
    ALL_COLUMN_TYPE.add(
        AllTypeTableColumn.builder().name("col").type("numrange").value("numrange(20.0, 30.0)").build());
    ALL_COLUMN_TYPE.add(AllTypeTableColumn.builder().name("col").type("tsrange").value("'[2010-01-01 14:30, 2010-01-01 15:30)'").build());
    ALL_COLUMN_TYPE.add(
        AllTypeTableColumn.builder().name("col").type("tstzrange").value("'[2010-01-01 14:30 +08:00, 2010-01-01 15:30 +08:00)'").build());
    ALL_COLUMN_TYPE.add(
        AllTypeTableColumn.builder().name("col").type("daterange").value("'[2010-01-01, 2020-01-01]'").build());
    // 对象标识符类型
    ALL_COLUMN_TYPE.add(AllTypeTableColumn.builder().name("col").type("OID").value("").build());
    ALL_COLUMN_TYPE.add(AllTypeTableColumn.builder().name("col").type("CID").value("").build());
    ALL_COLUMN_TYPE.add(AllTypeTableColumn.builder().name("col").type("XID").value("").build());
//    ALL_COLUMN_TYPE.add(AllTypeTableColumn.builder().name("col").type("TID").value("(1,11)").build());
    ALL_COLUMN_TYPE.add(
        AllTypeTableColumn.builder().name("col").type("REGCONFIG").value("").build());
    ALL_COLUMN_TYPE.add(
        AllTypeTableColumn.builder().name("col").type("REGDICTIONARY").value("").build());
    ALL_COLUMN_TYPE.add(AllTypeTableColumn.builder().name("col").type("REGOPER").value("").build());
    ALL_COLUMN_TYPE.add(
        AllTypeTableColumn.builder().name("col").type("REGOPERATOR").value("").build());
    ALL_COLUMN_TYPE.add(AllTypeTableColumn.builder().name("col").type("REGPROC").value("").build());
    ALL_COLUMN_TYPE.add(
        AllTypeTableColumn.builder().name("col").type("REGPROCEDURE").value("").build());
    ALL_COLUMN_TYPE.add(
        AllTypeTableColumn.builder().name("col").type("REGCLASS").value("").build());
    ALL_COLUMN_TYPE.add(AllTypeTableColumn.builder().name("col").type("REGTYPE").value("").build());

    ALL_COLUMN_TYPE.add(AllTypeTableColumn.builder().name("col").type("xml").value("").build());
    ALL_COLUMN_TYPE.add(AllTypeTableColumn.builder().name("col").type("hash16").value("").build());
    ALL_COLUMN_TYPE.add(AllTypeTableColumn.builder().name("col").type("hash32").value("'685847ed1fe38e18f6b0e2b18c00edee'").build());
    ALL_COLUMN_TYPE.add(AllTypeTableColumn.builder().name("col").type("JSON").value("").build());
    ALL_COLUMN_TYPE.add(AllTypeTableColumn.builder().name("col").type("JSONB").value("").build());
//    ALL_COLUMN_TYPE.add(AllTypeTableColumn.builder().name("col").type("bfile").value("bfilename('d_bfile','bfile.data')").build());

    // mysql兼容类型
    ALL_COLUMN_TYPE.add(
        AllTypeTableColumn.builder()
            .name("col")
            .type("binary(255)")
            .value("")
            .notSupportedTypes(Arrays.asList(NotSupportedType.ORACLE, NotSupportedType.TERADATA, NotSupportedType.PG))
            .build());
    ALL_COLUMN_TYPE.add(
        AllTypeTableColumn.builder().name("col").type("longtext").value("").build());
    ALL_COLUMN_TYPE.add(
        AllTypeTableColumn.builder()
            .name("col")
            .type("datetime")
            .value("'2010-12-12'")
            .notSupportedTypes(Arrays.asList(NotSupportedType.ORACLE, NotSupportedType.TERADATA, NotSupportedType.PG))
            .build());
    ALL_COLUMN_TYPE.add(
        AllTypeTableColumn.builder().name("col").type("longblob").value("").build());
    ALL_COLUMN_TYPE.add(
        AllTypeTableColumn.builder()
            .name("col")
            .type("int")
            .value("")
            .notSupportedTypes(Arrays.asList(NotSupportedType.ORACLE, NotSupportedType.TERADATA))
            .build());
    ALL_COLUMN_TYPE.add(
        AllTypeTableColumn.builder()
            .name("col")
            .type("tinyint")
            .value("")
            .notSupportedTypes(Arrays.asList(NotSupportedType.ORACLE, NotSupportedType.TERADATA))
            .build());
    ALL_COLUMN_TYPE.add(
        AllTypeTableColumn.builder()
            .name("col")
            .type("bigint")
            .value("")
            .notSupportedTypes(Arrays.asList(NotSupportedType.ORACLE, NotSupportedType.TERADATA))
            .build());
  }
}
