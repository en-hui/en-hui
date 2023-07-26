package com.enhui;

import com.enhui.model.AllTypeTableColumn;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

public class NodeMetaDataService {
  /** 全类型表 */
  static List<AllTypeTableColumn> ALL = new ArrayList<>();

  static {
    // https://docs.vastdata.com.cn/zh/docs/VastbaseG100Ver2.2.10/doc/%E5%BC%80%E5%8F%91%E8%80%85%E6%8C%87%E5%8D%97/%E6%95%B0%E6%8D%AE%E7%B1%BB%E5%9E%8B.html
    // 日期时间类型
    ALL.add(AllTypeTableColumn.builder().name("col").type("DATE").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("TIME").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("TIMETZ").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("TIMESTAMP").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("TIMESTAMPTZ").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("SMALLDATETIME").value("").build());
    ALL.add(
        AllTypeTableColumn.builder()
            .name("col")
            .type("INTERVAL DAY (6) TO SECOND (6)")
            .value("")
            .build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("INTERVAL YEAR").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("reltime").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("abstime").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("oradate").value("").build());

    // 数值类型
    ALL.add(AllTypeTableColumn.builder().name("col").type("TINYINT").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("SMALLINT").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("INTEGER").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("BINARY_INTEGER").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("BIGINT").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("int16").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("NUMERIC(10,5)").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("DECIMAL(10,5)").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("NUMBER(10,5)").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("SMALLSERIAL").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("SERIAL").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("BIGSERIAL").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("LARGESERIAL").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("REAL").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("FLOAT4").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("DOUBLE PRECISION").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("BINARY_DOUBLE").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("FLOAT8").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("FLOAT(53)").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("DEC(100,30)").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("INTEGER(100,30)").value("").build());

    // 货币类型
    ALL.add(AllTypeTableColumn.builder().name("col").type("money").value("").build());
    // 布尔类型
    ALL.add(AllTypeTableColumn.builder().name("col").type("BOOLEAN").value("").build());
    // 字符类型
    ALL.add(AllTypeTableColumn.builder().name("col").type("CHAR(255)").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("CHARACTER(255)").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("NCHAR(255)").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("VARCHAR(255)").value("").build());
    ALL.add(
        AllTypeTableColumn.builder().name("col").type("CHARACTER VARYING(255)").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("VARCHAR2(255)").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("NVARCHAR2(255)").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("NVARCHAR(255)").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("TEXT").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("CLOB").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("bpchar").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("name").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("char").value("").build());

    // 二进制类型
    ALL.add(AllTypeTableColumn.builder().name("col").type("BLOB").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("RAW").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("BYTEA").value("").build());
    ALL.add(
        AllTypeTableColumn.builder()
            .name("col")
            .type("BYTEAWITHOUTORDERWITHEQUALCOL")
            .value("")
            .build());
    ALL.add(
        AllTypeTableColumn.builder().name("col").type("BYTEAWITHOUTORDERCOL").value("").build());
    ALL.add(
        AllTypeTableColumn.builder()
            .name("col")
            .type("_BYTEAWITHOUTORDERWITHEQUALCOL")
            .value("")
            .build());
    ALL.add(
        AllTypeTableColumn.builder().name("col").type("_BYTEAWITHOUTORDERCOL").value("").build());

    // 几何类型
    ALL.add(AllTypeTableColumn.builder().name("col").type("point").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("line").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("lseg").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("box").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("path").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("path").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("polygon").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("circle").value("").build());
    // 网络地址类型
    ALL.add(AllTypeTableColumn.builder().name("col").type("cidr").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("inet").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("macaddr").value("").build());
    // 位串类型
    ALL.add(AllTypeTableColumn.builder().name("col").type("bit(11)").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("bit varying(11)").value("").build());
    // 文本搜索类型
    ALL.add(AllTypeTableColumn.builder().name("col").type("tsvector").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("tsquery").value("").build());
    // UUID类型
    ALL.add(AllTypeTableColumn.builder().name("col").type("UUID").value("").build());
    // HLL数据类型
    ALL.add(AllTypeTableColumn.builder().name("col").type("hll").value("").build());
    // 范围类型
    ALL.add(AllTypeTableColumn.builder().name("col").type("int4range").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("int8range").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("numrange").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("tsrange").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("tstzrange").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("daterange").value("").build());
    // 对象标识符类型
    ALL.add(AllTypeTableColumn.builder().name("col").type("OID").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("CID").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("XID").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("TID").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("REGCONFIG").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("REGDICTIONARY").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("REGOPER").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("REGOPERATOR").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("REGPROC").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("REGPROCEDURE").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("REGCLASS").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("REGTYPE").value("").build());

    ALL.add(AllTypeTableColumn.builder().name("col").type("xml").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("hash16").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("hash32").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("JSON").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("JSONB").value("").build());
    ALL.add(AllTypeTableColumn.builder().name("col").type("bfile").value("").build());
  }

  @Test
  public void createAllTypeTable() {
    AtomicInteger i = new AtomicInteger(1);
    String columnSql =
        ALL.stream()
            .map(
                colu ->
                    String.format("%s %s", colu.getName() + i.getAndIncrement(), colu.getType()))
            .collect(Collectors.joining(",\n"));

    String createSql = String.format("CREATE TABLE heh_all_type1(%s);", columnSql);
    System.out.println(createSql);
  }
}
