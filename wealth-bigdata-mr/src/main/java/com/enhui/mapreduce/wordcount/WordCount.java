package com.enhui.mapreduce.wordcount;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WordCount {

  public static void main(String[] args) throws Exception {
    // 默认加载配置文件
    Configuration conf = new Configuration(true);

    // 获取 mr 的客户端
    Job job = Job.getInstance(conf, "word count task 001");
    job.setJarByClass(WordCount.class);

    Path inPath = new Path("/user/root/wordCount.txt");
    FileInputFormat.addInputPath(job, inPath);

    Path outPath = new Path("/user/root/mrOut/wordCount");
    if (outPath.getFileSystem(conf).exists(outPath)) {
      outPath.getFileSystem(conf).delete(outPath, true);
    }
    FileOutputFormat.setOutputPath(job, outPath);

    job.setMapperClass(WordCountMapper.class);
    job.setMapOutputKeyClass(Text.class);
    job.setMapOutputValueClass(IntWritable.class);
    job.setReducerClass(WordCountReducer.class);

    job.waitForCompletion(true);
  }
}
