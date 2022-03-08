package com.enhui.util;

import com.alibaba.fastjson.JSON;
import java.io.IOException;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/** 基于poi从excel读取接口测试用例 */
@Slf4j
public class ExcelUtil {

  // 属性
  private String excelPath;

  // 构造方法
  public ExcelUtil(String excelPath) {
    this.excelPath = excelPath;
    log.info("创建excelUtil对象，并使用excel文件：【{}】", excelPath);
  }

  // 普通方法-返回值是二维数组；二维数组里面是有多个1维数组组成；返回值有多个数据时要考虑一维数组，若返回值是多行多列时要考虑二维数组
  public Object[][] getCellValue(int sheetIndex) throws IOException {
    // 据指定excel得到sheet
    XSSFWorkbook wb = new XSSFWorkbook(excelPath);
    XSSFSheet sheet2 = wb.getSheetAt(sheetIndex);

    // 定义二维数组存放接口测试用例
    Object[][] testcase = new Object[sheet2.getLastRowNum()][];

    // 遍历行
    for (int rowIndex = 1; rowIndex <= sheet2.getLastRowNum(); rowIndex++) {
      XSSFRow row = sheet2.getRow(rowIndex);

      // 二维数组中每个1维数组中的数据个数和基于行遍历列
      testcase[rowIndex - 1] = new Object[row.getLastCellNum()];
      for (int cellIndex = rowIndex - 1; cellIndex < row.getLastCellNum(); cellIndex++) {
        // 得到单元格
        XSSFCell cell = row.getCell(cellIndex);
        testcase[rowIndex - 1][cellIndex] = cellTypeCase(cell);
      }
      // System.out.println("-------------------------");
    }
    log.info("ExcelUtil Object[][] testcase =" + JSON.toJSON(testcase).toString());
    // 释放资源
    wb.close();
    return testcase;
  }

  // 据cellType取值
  // 总结：当方法有多个返回值时，需定义返回值类型为Object
  public Object cellTypeCase(XSSFCell cell) {
    CellType type = cell.getCellType();
    log.info("ExcelUtil cell type=" + type);
    // switch (cell.getCellType()){//为了加log,修改为以下代码
    switch (type) {
      case NUMERIC:
        return cell.getNumericCellValue();
      case STRING:
        return cell.getStringCellValue();
      case FORMULA:
        return cell.getCellFormula();
      case BOOLEAN:
        return cell.getBooleanCellValue();
      case ERROR:
        return cell.getErrorCellString();
      case _NONE:
        return null;
      case BLANK:
        return null;
      default:
        return null;
    }
  }

}
