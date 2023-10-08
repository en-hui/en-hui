//
// Created by 胡恩会 on 2023/10/8.
//

#include <iostream>
#include <librdkafka/rdkafkacpp.h>

using std::string;

int main() {
    string err_str;
    // 创建Kafka配置
    RdKafka::Conf *conf = RdKafka::Conf::create(RdKafka::Conf::CONF_GLOBAL);
    conf->set("bootstrap.servers", "kafka1:9092", err_str);

    std::cout << "init conf" << std::endl;

    // 创建Kafka生产者实例
    RdKafka::Producer *producer = RdKafka::Producer::create(conf, err_str);

    std::cout << "create producer" << std::endl;

    // 创建Topic
    RdKafka::Topic *topic = RdKafka::Topic::create(producer, "cpp_topic", conf, err_str);

    std::cout << "create topic" << std::endl;

    // 生产消息
    std::string message = "Hello, Kafka!";
    RdKafka::ErrorCode resp = producer->produce(topic, RdKafka::Topic::PARTITION_UA,
                                                RdKafka::Producer::RK_MSG_COPY, (void *) message.c_str(),
                                                message.size(), NULL, NULL);

    if (resp != RdKafka::ERR_NO_ERROR) {
        std::cerr << "Failed to produce message: " << RdKafka::err2str(resp) << std::endl;
    } else {
        std::cout << "Message sent successfully." << std::endl;
    }

    // 清理资源
    delete topic;
    delete producer;
    delete conf;

    return 0;
}



