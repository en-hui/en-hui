package com.enhui.test;

import java.util.Set;
import lombok.Data;

@Data
public class ValueModel {
  public ValueModel(Set<Integer> sets) {
    this.sets = sets;
  }

  Set<Integer> sets;
}
