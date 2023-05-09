package com.enhui;

import java.io.Serializable;
import java.util.List;
import java.util.StringJoiner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@Data
@EqualsAndHashCode
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Table implements Serializable {

  private static final long serialVersionUID = -7815849818556667000L;
  public enum EntityType {
    TABLE,
    VIEW,
  }
  private String database;
  private String schema;
  private String name;
  private String comment;
  private EntityType type = EntityType.TABLE;

  public static Table ofSchema(String schema, String name) {
    return new Table(null, schema, name);
  }

  private Table(String database, String schema, String name) {
    this.database = StringUtils.isBlank(database) ? null : database;
    this.schema = StringUtils.isBlank(schema) ? null : schema;
    this.name = name;
  }

  public String getFullName() {
    StringJoiner joiner = new StringJoiner(", ", "[", "]");
    if (database != null) {
      joiner.add(String.format("database='%s'", database));
    }
    if (schema != null) {
      joiner.add(String.format("schema='%s'", schema));
    }
    return joiner.add(String.format("name='%s'", name)).toString();
  }
}
