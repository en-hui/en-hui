package com.enhui;

import com.enhui.fn.PrintTextFn;
import io.debezium.connector.mysql.MySqlConnector;
import org.apache.beam.io.debezium.DebeziumIO;
import org.apache.beam.io.debezium.KafkaSourceConsumerFn;
import org.apache.beam.io.debezium.SourceRecordJson;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.coders.StringUtf8Coder;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import org.checkerframework.checker.initialization.qual.Initialized;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.UnknownKeyFor;

public class DbzMysqlToMysqlPipeline {

  public static void main(String[] args) {
    DebeziumIO.ConnectorConfiguration mySqlConnectorConfig =
        DebeziumIO.ConnectorConfiguration.create()
            .withUsername("root")
            .withPassword("Datapipeline123")
            .withHostName("heh-mysql")
            .withPort("3306")
            .withConnectorClass(MySqlConnector.class)
            .withConnectionProperty("database.server.id", "184054")
            .withConnectionProperty("database.server.name", "serverid")
            .withConnectionProperty("database.include.list", "test")
            .withConnectionProperty("table.include.list", "test.source_table")
            .withConnectionProperty(
                "database.history",
                KafkaSourceConsumerFn.DebeziumSDFDatabaseHistory.class.getName())
            .withConnectionProperty("include.schema.changes", "false");

    PipelineOptions options = PipelineOptionsFactory.create();
    Pipeline p = Pipeline.create(options);
    final PCollection<@UnknownKeyFor @NonNull @Initialized String> source =
        p.apply(
                DebeziumIO.readAsJson()
                    .withConnectorConfiguration(mySqlConnectorConfig)
                    .withFormatFunction(new SourceRecordJson.SourceRecordJsonMapper()))
            .setCoder(StringUtf8Coder.of());

    // 使用 ParDo 将文本内容打印到标准输出
    source.apply(ParDo.of(new PrintTextFn()));

    p.run().waitUntilFinish();
  }
}
