package com.enhui.connector.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * kafka源码直接粘贴的
 * 加注释及打印，方便理解
 */
public class FileSourceTask extends SourceTask {
  private static final Logger log = LoggerFactory.getLogger(FileSourceTask.class);
  public static final String FILENAME_FIELD = "filename";
  public  static final String POSITION_FIELD = "position";
  private static final Schema VALUE_SCHEMA = Schema.STRING_SCHEMA;

  private String filename;
  private InputStream stream;
  private BufferedReader reader = null;
  private char[] buffer = new char[1024];
  private int offset = 0;
  private String topic = null;
  private int batchSize = FileSourceConnector.DEFAULT_TASK_BATCH_SIZE;

  private Long streamOffset;

  @Override
  public String version() {
    return new FileSourceConnector().version();
  }

  @Override
  public void start(Map<String, String> props) {
    System.out.println("FileSourceTask::start——得到的 connector 中的参数配置：" + props);
    filename = props.get(FileSourceConnector.FILE_CONFIG);
    if (filename == null || filename.isEmpty()) {
      stream = System.in;
      // Tracking offset for stdin doesn't make sense
      streamOffset = null;
      reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
    }
    // Missing topic or parsing error is not possible because we've parsed the config in the
    // Connector
    topic = props.get(FileSourceConnector.TOPIC_CONFIG);
    batchSize = Integer.parseInt(props.get(FileSourceConnector.TASK_BATCH_SIZE_CONFIG));
  }

  @Override
  public List<SourceRecord> poll() throws InterruptedException {
    if (stream == null) {
      try {
        stream = Files.newInputStream(Paths.get(filename));
        // 获取文件读取到了什么偏移位置
        Map<String, Object> offset = context.offsetStorageReader().offset(Collections.singletonMap(FILENAME_FIELD, filename));
        if (offset != null) {
          Object lastRecordedOffset = offset.get(POSITION_FIELD);
          if (lastRecordedOffset != null && !(lastRecordedOffset instanceof Long))
            throw new org.apache.kafka.connect.errors.ConnectException("Offset position is the incorrect type");
          if (lastRecordedOffset != null) {
            log.debug("Found previous offset, trying to skip to file offset {}", lastRecordedOffset);
            long skipLeft = (Long) lastRecordedOffset;
            while (skipLeft > 0) {
              try {
                long skipped = stream.skip(skipLeft);
                skipLeft -= skipped;
              } catch (IOException e) {
                log.error("Error while trying to seek to previous offset in file {}: ", filename, e);
                throw new org.apache.kafka.connect.errors.ConnectException(e);
              }
            }
            log.debug("Skipped to offset {}", lastRecordedOffset);
          }
          streamOffset = (lastRecordedOffset != null) ? (Long) lastRecordedOffset : 0L;
        } else {
          // 没获取到已读取的偏移量，偏移量从头开始
          streamOffset = 0L;
        }
        reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
        log.debug("Opened {} for reading", logFilename());
      } catch (NoSuchFileException e) {
        log.warn("Couldn't find file {} for FileStreamSourceTask, sleeping to wait for it to be created", logFilename());
        synchronized (this) {
          this.wait(1000);
        }
        return null;
      } catch (IOException e) {
        log.error("Error while trying to open file {}: ", filename, e);
        throw new ConnectException(e);
      }
    }

    // Unfortunately we can't just use readLine() because it blocks in an uninterruptible way.
    // Instead we have to manage splitting lines ourselves, using simple backoff when no new data
    // is available.
    try {
      final BufferedReader readerCopy;
      synchronized (this) {
        readerCopy = reader;
      }
      if (readerCopy == null)
        return null;

      ArrayList<SourceRecord> records = null;

      int nread = 0;
      while (readerCopy.ready()) {
        nread = readerCopy.read(buffer, offset, buffer.length - offset);
        log.trace("Read {} bytes from {}", nread, logFilename());

        if (nread > 0) {
          offset += nread;
          if (offset == buffer.length) {
            char[] newbuf = new char[buffer.length * 2];
            System.arraycopy(buffer, 0, newbuf, 0, buffer.length);
            buffer = newbuf;
          }

          String line;
          do {
            line = extractLine();
            if (line != null) {
              log.trace("Read a line from {}", logFilename());
              if (records == null)
                records = new ArrayList<>();
              // 将读取到的文件偏移量也重置到最新位置了
              records.add(new SourceRecord(offsetKey(filename), offsetValue(streamOffset), topic, null,
                      null, null, VALUE_SCHEMA, line, System.currentTimeMillis()));

              if (records.size() >= batchSize) {
                return records;
              }
            }
          } while (line != null);
        }
      }

      if (nread <= 0)
        synchronized (this) {
          this.wait(1000);
        }

      return records;
    } catch (IOException e) {
      // Underlying stream was killed, probably as a result of calling stop. Allow to return
      // null, and driving thread will handle any shutdown if necessary.
    }
    return null;
  }

  private String extractLine() {
    int until = -1, newStart = -1;
    for (int i = 0; i < offset; i++) {
      if (buffer[i] == '\n') {
        until = i;
        newStart = i + 1;
        break;
      } else if (buffer[i] == '\r') {
        // We need to check for \r\n, so we must skip this if we can't check the next char
        if (i + 1 >= offset)
          return null;

        until = i;
        newStart = (buffer[i + 1] == '\n') ? i + 2 : i + 1;
        break;
      }
    }

    if (until != -1) {
      String result = new String(buffer, 0, until);
      System.arraycopy(buffer, newStart, buffer, 0, buffer.length - newStart);
      offset = offset - newStart;
      if (streamOffset != null)
        streamOffset += newStart;
      return result;
    } else {
      return null;
    }
  }

  @Override
  public void stop() {
    log.info("停止任务...");
    synchronized (this) {
      try {
        if (stream != null && stream != System.in) {
          stream.close();
          log.info("关闭文件输入流...");
        }
      } catch (IOException e) {
        log.error("Failed to close FileStreamSourceTask stream: ", e);
      }
      this.notify();
    }
  }

  private Map<String, String> offsetKey(String filename) {
    return Collections.singletonMap(FILENAME_FIELD, filename);
  }

  private Map<String, Long> offsetValue(Long pos) {
    return Collections.singletonMap(POSITION_FIELD, pos);
  }

  private String logFilename() {
    return filename == null ? "stdin" : filename;
  }
}
