package com.enhui.work.kafka;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.enhui.util.CsvUtil;
import com.enhui.util.ParamUtil;
import com.enhui.util.RequestUtil;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Iterator;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Slf4j
public class KafkaConsumerTest {
  @DataProvider(name = "kafkaConsumerDataProvider")
  public Object[][] dataProvider() throws IOException {
    String filePath =
        "/Users/huenhui/IdeaProjects/wealth/wealth-auto-test/src/main/resources/work/kafka/KafkaConsumerTest.csv";
    Object[][] data = CsvUtil.listData(filePath);
    log.info("加载{}文件成功，用例条数为{}", filePath, data.length);
    return data;
  }

  @Test(dataProvider = "kafkaConsumerDataProvider")
  public void consumerKafka(
      String id,
      String topicName,
      String transmissionMode,
      String kafkaConsumerWay,
      String consumerStartTime,
      String consumerStartOffset,
      String consumerCount,
      String consumerFilter,
      String serializer,
      String desc)
      throws Exception {
    String host = InetAddress.getByName("dev_huenhui").getHostAddress();
    String url = "http://" + host + "/v3/system/kafka/topic/consumer";

    // 请求参数
    JSONObject paramJson = new JSONObject();
    paramJson.put("topicName", topicName);
    paramJson.put("kafkaConsumerWay", kafkaConsumerWay);
    paramJson.put("consumerCount", consumerCount);
    paramJson.put("serializer", serializer);
    paramJson.put("transmissionMode", transmissionMode);
    paramJson.put("consumerStartTime", consumerStartTime);
    paramJson.put("consumerStartOffset", consumerStartOffset);
    paramJson.put("consumerFilter", consumerFilter);
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
