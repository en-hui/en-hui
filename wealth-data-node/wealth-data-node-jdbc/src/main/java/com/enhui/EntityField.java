package com.enhui;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@Builder
public class EntityField implements Serializable {

  private static final long serialVersionUID = 7640151947719108163L;

  //  @Schema(description = "名字")
  private String name;

  //  @Schema(description = "类型")
  private String type;

  //  @Schema(description = "jdbc类型")
  private Integer jdbcType;

  //  @Schema(description = "java类名")
  private String className;

  //  @Schema(description = "默认值")
  private String defaultValue;

  //  @Schema(description = "是否必填")
  private boolean optional;

  //  @Schema(description = "精度")
  private Long precision;

  //  @Schema(description = "标度")
  private Long scale;

  //  @Schema(description = "展示长度")
  private Long displaySize;

  //  @Schema(description = "注释")
  private String comment;

  //  @Schema(description = "是否主键")
  private boolean isPk;

  //  @Schema(description = "字符集")
  private String charset;
}
