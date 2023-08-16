package com.enhui.mppdb;

import java.nio.ByteBuffer;

public class DefaultMessageDecoder {

  /**
   * BEGIN<br>
   * {"table_name":"public.test0426","op_type":"INSERT","columns_name":["col1","col2","col3"],"columns_type":["integer","integer","integer"],"columns_val":["1","2","3"],"old_keys_name":[],"old_keys_type":[],"old_keys_val":[]}
   * COMMIT (at 2023-05-05 10:58:21.798275+08) CSN 536576070<br>
   * BEGIN<br>
   * {"table_name":"public.test0426","op_type":"UPDATE","columns_name":["col1","col2","col3"],"columns_type":["integer","integer","integer"],"columns_val":["1","1","1"],"old_keys_name":["col1","col2","col3"],"old_keys_type":["integer","integer","integer"],"old_keys_val":["1","2","1"]}
   * COMMIT (at 2023-05-05 14:33:21.790813+08) CSN 536576095<br>
   * BEGIN<br>
   * {"table_name":"public.test0426","op_type":"DELETE","columns_name":[],"columns_type":[],"columns_val":[],"old_keys_name":["col1","col2","col3"],"old_keys_type":["integer","integer","integer"],"old_keys_val":["1","1","1"]}
   * COMMIT (at 2023-05-05 14:33:55.930686+08) CSN 536576096 <br>
   */
  public NonRecursiveHelper processMessage(long lastReceiveLsn, ByteBuffer byteBuffer) {
    try {
      if (!byteBuffer.hasArray()) {
        throw new IllegalStateException(
            "Invalid buffer received from Gbase8c server during streaming replication");
      }
      int offset = byteBuffer.arrayOffset();
      byte[] source = byteBuffer.array();
      int length = source.length - offset;
      final String content = new String(source, offset, length);
      System.out.println("mppdb handle content : " + lastReceiveLsn + " " + content);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return null;
  }
}
