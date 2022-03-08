package com.enhui.work.kafka;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.enhui.util.CsvUtil;
import com.enhui.util.LocalCacheUtil;
import com.enhui.util.ParamUtil;
import com.enhui.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Iterator;

@Slf4j
public class KafkaListTaskTest {
    @DataProvider(name = "kafkaTaskTopicListDataProvider")
    public Object[][] dataProvider() throws IOException {
        String loginTestFilePath =
                "/Users/huenhui/IdeaProjects/wealth/wealth-auto-test/src/main/resources/work/kafka/KafkaListTaskTest.csv";
        Object[][] data = CsvUtil.listData(loginTestFilePath);
        log.info("加载{}文件成功，用例条数为{}", loginTestFilePath, data.length);
        return data;
    }

    @Test(dataProvider = "kafkaTaskTopicListDataProvider")
    public void listByTask(
            String id, String taskId, String desc)
            throws Exception {
        String host = InetAddress.getByName("dev_huenhui").getHostAddress();
        String url = "http://" + host + "/v3/system/kafka/task/topic/list";

        // 请求参数
        JSONObject paramJson = new JSONObject();
        paramJson.put("taskId", taskId);
        // 发起请求
        String result = RequestUtil.sendGet(url, ParamUtil.toSplicingStr(paramJson));

        Assert.assertNotNull(result, "result");
        // 返回结果
        JSONObject jsonObject = JSONObject.parseObject(result);
        JSONArray data = (JSONArray) jsonObject.get("data");
        Assert.assertNotNull(data, "data");
        Iterator<Object> iterator = data.stream().iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}
