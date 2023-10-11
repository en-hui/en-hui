package com.enhui.util.crypt;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {
  public static void main(String[] args) {
      AESUtil aesUtil = new AESUtil();
      String before = "HelloWorld";
      String after = "gGhNjPqeFf3kaL2AIXEAMg==";
      // 加密
      System.out.println("例子：加密[" + before + "] 得到密文：" + aesUtil.encrypt(before));
      // 解密
      System.out.println("例子：解密[" + after + "] 得到原文：" + aesUtil.decrypt(after));
      Scanner scanner = new Scanner(System.in);
      while (true) {
          System.out.println("输入要解密的内容，多个可以用英文逗号,隔开");
          String str = scanner.nextLine();
          String[] split = str.split(",");
          for (String s : split) {
              System.out.println("解密前：" + s + ", 解密后: " + aesUtil.decrypt(s));
          }
      }
  }

  private static final String DEFAULT_SECRET = "datapipelinescret123";
  private static SecretKeySpec secretKey;

  private static void setKey(String myKey) throws NoSuchAlgorithmException {
    byte[] key = myKey.getBytes(StandardCharsets.UTF_8);
    MessageDigest sha = MessageDigest.getInstance("SHA-1");
    key = sha.digest(key);
    key = Arrays.copyOf(key, 16);
    secretKey = new SecretKeySpec(key, "AES");
  }

  private static String encrypt(String strToEncrypt, String secret) {
    if (strToEncrypt == null) {
      return null;
    }
    try {
      setKey(secret);
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      cipher.init(Cipher.ENCRYPT_MODE, secretKey);
      return Base64.getEncoder()
          .encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

  private static String decrypt(String strToDecrypt, String secret) {
    if (strToDecrypt == null) {
      return null;
    }
    try {
      setKey(secret);
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
      cipher.init(Cipher.DECRYPT_MODE, secretKey);
      return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

  public String encrypt(String strToEncrypt) {
    return encrypt(strToEncrypt, DEFAULT_SECRET);
  }

  public String decrypt(String strToDecrypt) {
    return decrypt(strToDecrypt, DEFAULT_SECRET);
  }
}
