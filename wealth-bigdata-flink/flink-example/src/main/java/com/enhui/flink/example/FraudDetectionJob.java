/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.enhui.flink.example;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.walkthrough.common.entity.Alert;
import org.apache.flink.walkthrough.common.entity.Transaction;
import org.apache.flink.walkthrough.common.sink.AlertSink;
import org.apache.flink.walkthrough.common.source.TransactionSource;

/** Skeleton code for the datastream walkthrough */
public class FraudDetectionJob {
  public static void main(String[] args) throws Exception {
    /**
     * 执行环境<br>
     * 第一行的 StreamExecutionEnvironment 用于设置你的执行环境。 任务执行环境用于定义任务的属性、创建数据源以及最终启动任务的执行。
     */
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

    /**
     * 创建数据源<br>
     * 数据源从外部系统例如 Apache Kafka、Rabbit MQ 或者 Apache Pulsar 接收数据， 然后将数据送到 Flink 程序中。
     * 这个代码练习使用的是一个能够无限循环生成信用卡模拟交易数据的数据源。 每条交易数据包括了信用卡 ID （accountId），交易发生的时间 （timestamp）
     * 以及交易的金额（amount）。 绑定到数据源上的 name 属性是为了调试方便，如果发生一些异常，我们能够通过它快速定位问题发生在哪里。
     */
    DataStream<Transaction> transactions =
        env.addSource(new TransactionSource()).name("transactions");

    /**
     * 对事件分区 & 欺诈检测<br>
     * transactions这个数据流包含了大量的用户交易数据，需要被划分到多个并发上进行欺诈检测处理。
     * 由于欺诈行为的发生是基于某一个账户的，所以，必须要要保证同一个账户的所有交易行为数据要被同一个并发的 task 进行处理。
     *
     * <p>为了保证同一个 task 处理同一个 key 的所有数据，你可以使用 DataStream#keyBy 对流进行分区。 process()
     * 函数对流绑定了一个操作，这个操作将会对流上的每一个消息调用所定义好的函数。 通常，一个操作会紧跟着 keyBy 被调用，
     * 在这个例子中，这个操作是FraudDetector，该操作是在一个 keyed context 上执行的。
     */
    DataStream<Alert> alerts =
        transactions
            .keyBy(Transaction::getAccountId)
            .process(new FraudDetector())
            .name("fraud-detector");

    /**
     * 输出结果<br>
     * sink 会将 DataStream 写出到外部系统，例如 Apache Kafka、Cassandra 或者 AWS Kinesis 等。 AlertSink 使用 INFO
     * 的日志级别打印每一个 Alert 的数据记录，而不是将其写入持久存储，以便你可以方便地查看结果。
     */
    alerts.addSink(new AlertSink()).name("send-alerts");

    /**
     * 运行作业<br>
     * Flink 程序是懒加载的，并且只有在完全搭建好之后，才能够发布到集群上执行。 调用 StreamExecutionEnvironment#execute
     * 时给任务传递一个任务名参数，就可以开始运行任务。
     */
    env.execute("Fraud Detection");
  }
}
