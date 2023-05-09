package com.enhui;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EntityWeight implements Serializable {

  private static final long serialVersionUID = 4011092846683793930L;

  private Table table;
  private double weight;
}
