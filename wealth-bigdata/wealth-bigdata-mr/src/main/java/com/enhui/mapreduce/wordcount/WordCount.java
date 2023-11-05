package com.enhui.mapreduce.wordcount;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class WordCount {

  /**
   * 参数——index 0 : 输入目录
   * 参数——index 1 : 输出目录
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    // 默认加载配置文件
    Configuration conf = new Configuration(true);

    // 获取 mr 的客户端
    Job job = Job.getInstance(conf, "word count");
    job.setJarByClass(WordCount.class);

    // 将命令行中 -D开头的参数放进conf中，留下剩余的参数，自己处理
    GenericOptionsParser parser = new GenericOptionsParser(conf, args);
    String[] otherArgs = parser.getRemainingArgs();

//    String inPathStr = "/data/input/wordCount.txt";
    String inPathStr = otherArgs[0];
//    String outPathStr = "/data/output/wordCountMR";
    String outPathStr = otherArgs[1];

    Path inPath = new Path(inPathStr);
    FileInputFormat.addInputPath(job, inPath);

    // 指定一个空目录
    Path outPath = new Path(outPathStr);
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
