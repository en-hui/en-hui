package com.enhui.util.excel.upload;

import lombok.Data;

import java.util.Date;

/**
 * 根据测试，导入规则：
 * excel的列从前往后-->对应本类字段从上到下
 *
 * @Author 胡恩会
 * @Date 2021/8/6 21:18
 **/
@Data
public class BetaProblemUploadData {
    /**
     * 产品名称
     **/
    private String productName;
    /**
     * 产品版本
     **/
    private String productVersion;
    /**
     * 测试时间
     **/
    private Date testTime;
    /**
     * 平均电流
     **/
    private Double avgCurrent;
    /**
     * 测试案例--相比导出，多加了一个字段
     **/
    private String testCase;
}
