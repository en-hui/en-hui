package com.enhui;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.jdbc.JdbcIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;

public class MysqlToMysqlPipeline {
  public static void main(String[] args) {
    PipelineOptions options = PipelineOptionsFactory.create();
    Pipeline pipeline = Pipeline.create(options);

    final JdbcIO.DataSourceConfiguration dataSourceConfiguration =
        JdbcIO.DataSourceConfiguration.create(
                "com.mysql.cj.jdbc.Driver", "jdbc:mysql://heh-mysql:3306/test")
            .withUsername("root")
            .withPassword("Datapipeline123");

    final PCollection<KV<Integer, String>> source =
        pipeline.apply(
            JdbcIO.<KV<Integer, String>>read()
                .withDataSourceConfiguration(dataSourceConfiguration)
                .withQuery("SELECT col1, col2 FROM source_table")
                .withRowMapper(
                    new JdbcIO.RowMapper<KV<Integer, String>>() {
                      public KV<Integer, String> mapRow(ResultSet resultSet) throws Exception {
                        return KV.of(resultSet.getInt(1), resultSet.getString(2));
                      }
                    }));

    source.apply(
        JdbcIO.<KV<Integer, String>>write()
            .withDataSourceConfiguration(dataSourceConfiguration)
            .withStatement("insert into sink_table values(?, ?)")
            .withPreparedStatementSetter(
                new JdbcIO.PreparedStatementSetter<KV<Integer, String>>() {
                  public void setParameters(KV<Integer, String> element, PreparedStatement query)
                      throws SQLException {
                    query.setInt(1, element.getKey());
                    query.setString(2, element.getValue());
                  }
                }));

    pipeline.run();
  }
}
