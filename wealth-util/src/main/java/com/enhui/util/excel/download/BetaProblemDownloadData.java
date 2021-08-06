package com.enhui.util.excel.download;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author 胡恩会
 * @Date 2021/8/6 21:19
 **/
@Data
public class BetaProblemDownloadData {

    @ExcelProperty("产品名称")
    private String productName;
    @ExcelProperty("产品版本")
    private String productVersion;
    @ExcelProperty("测试时间")
    private Date testTime;
    @ExcelProperty("平均电流")
    private Double avgCurrent;
}
