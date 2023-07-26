package com.enhui.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AllTypeTableColumn {

    String name;
    String type;
    Object value;
}
