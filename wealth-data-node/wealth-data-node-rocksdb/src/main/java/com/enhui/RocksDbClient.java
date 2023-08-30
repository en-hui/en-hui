package com.enhui;

import java.io.File;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

/** rocksdb java客户端使用方式：http://rocksdb.org.cn/doc/RocksJava-Basics.html */
public class RocksDbClient {

  RocksDB rocksDb;
  String rocksDir = "./rocksDB/data";

  @Test
  public void testAll() throws RocksDBException {
    String key = "test_key1";

    String writeValue = "test_value1";
    rocksDb.put(key.getBytes(), writeValue.getBytes());
    byte[] readValue = rocksDb.get(key.getBytes());
    System.out.println(new String(readValue));

    writeValue = "test_value2";
    rocksDb.put(key.getBytes(), writeValue.getBytes());
    readValue = rocksDb.get(key.getBytes());
    System.out.println(new String(readValue));

    rocksDb.delete(key.getBytes());
    readValue = rocksDb.get(key.getBytes());
    System.out.println(readValue == null ? "没有即为null" : new String(readValue));
  }

  @Test
  public void testWrite() throws RocksDBException {}

  @Test
  public void testRead() throws RocksDBException {
    String key = "test_key1";
    final byte[] value = rocksDb.get(key.getBytes());
    System.out.println(new String(value));
  }

  @Test
  public void testDel() throws RocksDBException {
    String key = "test_key1";
    rocksDb.delete(key.getBytes());
  }

  @BeforeEach
  public void init() {
    RocksDB.loadLibrary();
    File file = new File(rocksDir);
    boolean success = false;
    if (!file.exists()) {
      success = file.mkdirs();
    }
    if (success) {
      System.out.println("make a new dir : " + rocksDir);
    }
    try (final Options options = new Options().setCreateIfMissing(true)) {
      rocksDb = RocksDB.open(options, rocksDir);
    } catch (RocksDBException e) {
      // do some error handling
      e.printStackTrace();
    }
  }

  @AfterEach
  public void destory() {
    if (rocksDb != null) {
      rocksDb.close();
    }
  }
}
