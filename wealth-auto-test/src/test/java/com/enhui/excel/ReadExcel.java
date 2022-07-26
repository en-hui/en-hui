package com.enhui.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

@Slf4j
public class ReadExcel {
    String filePath1 = "/Users/liuhe/develop/IdeaPro/wealth/docs/认证笔记/总结/题目0624.xlsx";
    String filePath2 = "/Users/liuhe/develop/IdeaPro/wealth/docs/认证笔记/总结/题目0726.xlsx";

    String filePath = filePath2;


    StringBuilder result = new StringBuilder();
    String cacheTitle1 = "";
    String cacheTitle2 = "";
    int count = 1;

    @Test
    public void readSheet0() {
        EasyExcel.read(filePath, new AnalysisEventListener<Map<String, String>>() {
            @Override
            public void invoke(Map<String, String> o, AnalysisContext analysisContext) {
                if (!"题型".equals(o.get(0))) {
                    boolean cont = true;
                    if (!cacheTitle1.equals(o.get(0))) {
                        cacheTitle1 = o.get(0);
                        result.append("## ").append(o.get(0)).append("  \n");
                    }
                    if (!cacheTitle2.equals(o.get(1))) {
                        cacheTitle2 = o.get(1);
                        result.append("### ").append(o.get(1)).append("  \n");
                        count = 1;
                    }
                    // .append(String.format("(%s)",o.get(10)))
                    result.append("- ").append(count++).append("、").append(o.get(2)).append("  \n");


                    // 选择
                    if (cont && StringUtils.isBlank(o.get(3))) {
                        result.append("\n");
                        cont = false;
                    } else if (cont) {
                        result.append("A、").append(o.get(3)).append("   ");
                    }
                    if (cont && StringUtils.isBlank(o.get(4))) {
                        result.append("\n");
                        cont = false;
                    } else if (cont) {
                        result.append("B、").append(o.get(4)).append("   ");
                    }
                    if (cont && StringUtils.isBlank(o.get(5))) {
                        result.append("\n");
                        cont = false;
                    } else if (cont) {
                        result.append("C、").append(o.get(5)).append("   ");
                    }
                    if (cont && StringUtils.isBlank(o.get(6))) {
                        result.append("\n");
                        cont = false;
                    } else if (cont) {
                        result.append("D、").append(o.get(6)).append("   ");
                    }
                    if (cont && StringUtils.isBlank(o.get(7))) {
                        result.append("\n");
                        cont = false;
                    } else if (cont) {
                        result.append("E、").append(o.get(7)).append("   ");
                    }
                    if (cont && StringUtils.isBlank(o.get(8))) {
                        result.append("\n");
                        cont = false;
                    } else if (cont) {
                        result.append("F、").append(o.get(8)).append("   ");
                    }
                    if (cont && StringUtils.isBlank(o.get(9))) {
                        result.append("\n");
                        cont = false;
                    } else if (cont) {
                        result.append("G、").append(o.get(9)).append("   ");
                    }
                }

            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                System.out.println(result);
                FileWriter writer = null;
                try {
                    writer = new FileWriter("/Users/liuhe/develop/IdeaPro/wealth/wealth-auto-test/src/test/resources/temp.md");
                    writer.write(result.toString());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void invokeHeadMap(Map headMap, AnalysisContext context) {
            }
        }).sheet(4).doRead();


    }


}
