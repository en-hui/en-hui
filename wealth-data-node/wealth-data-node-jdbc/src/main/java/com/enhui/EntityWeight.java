package com.enhui;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntityWeight implements Serializable {

  private static final long serialVersionUID = 4011092846683793930L;

  private Table table;
  private double weight;

  @Override
  public String toString() {
    return "table=" + table.getFullName() + ", weight=" + weight;
  }
}
