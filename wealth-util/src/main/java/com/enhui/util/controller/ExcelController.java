package com.enhui.util.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.enhui.util.excel.download.BetaProblemDownloadData;
import com.enhui.util.excel.upload.BetaProblemUploadData;
import com.enhui.util.excel.upload.BetaProblemUploadDataListener;
import com.enhui.util.service.BetaProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

/**
 * easyExcel官网例子
 *
 * @Author 胡恩会
 * @Date 2021/8/6 21:07
 **/
@RestController
public class ExcelController {

    @Autowired
    private BetaProblemService betaProblemService;

    @PostMapping("/upload/excel")
    public String uploadExcel(MultipartFile file) throws IOException {
        EasyExcel.read(file.getInputStream(), BetaProblemUploadData.class, new BetaProblemUploadDataListener(betaProblemService)).sheet().doRead();
        return "success";
    }

    /**
     * 文件下载并且失败的时候返回json（默认失败了会返回一个有部分数据的Excel）
     *
     * @param request:
     * @param response:
     * @Author: 胡恩会
     * @Date: 2021/8/6 21:35
     * @return: void
     **/
    @GetMapping("/download/excel")
    public void downloadExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 数据准备
        List<BetaProblemDownloadData> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            BetaProblemDownloadData data = new BetaProblemDownloadData();
            data.setProductName("产品" + i);
            data.setProductVersion("版本" + i);
            data.setTestTime(new Date());
            data.setAvgCurrent(0.56);
            list.add(data);
        }

        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("测试", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), BetaProblemDownloadData.class)
                    .autoCloseStream(Boolean.FALSE).sheet("模板")
                    .doWrite(list);
        } catch (Exception e) {
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = new HashMap<String, String>();
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            response.getWriter().println(JSON.toJSONString(map));
        }
    }

}
