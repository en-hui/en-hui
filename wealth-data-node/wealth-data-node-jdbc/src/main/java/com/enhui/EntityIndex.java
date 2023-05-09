package com.enhui;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntityIndex implements Serializable {

  private static final long serialVersionUID = 8648796941417407071L;

  private String indexName;
  private String columnName;
  private Integer columnPosition;
  private String collation;
  private boolean unique;
  private boolean primaryKey;
  private String indexType;
}
