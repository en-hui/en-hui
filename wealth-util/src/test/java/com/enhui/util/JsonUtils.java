package com.enhui.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/** json处理 */
public enum JsonUtils {
  /** 实例 */
  INSTANCE;

  private final ObjectMapper objectMapper;

  JsonUtils() {
    this.objectMapper = new ObjectMapper();
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
    objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
  }

  public ObjectMapper getObjectMapper() {
    return objectMapper;
  }

  public <T> T getObject(Object obj, Class<T> clazz) {
    try {
      if (obj instanceof String) {
        return objectMapper.readValue((String) obj, clazz);
      }
    } catch (Exception e) {
      throw convertObjectException(obj, clazz.getName(), e);
    }
    return objectMapper.convertValue(obj, clazz);
  }

  public <T> T getObject(Object obj, TypeReference<T> typeReference) {
    try {
      if (obj instanceof String) {
        return objectMapper.readValue((String) obj, typeReference);
      }
    } catch (Exception e) {
      throw convertObjectException(obj, typeReference.getType().getTypeName(), e);
    }
    return objectMapper.convertValue(obj, typeReference);
  }

  public JsonNode readTree(byte[] obj) {
    try {
      return objectMapper.readTree(obj);
    } catch (Exception e) {
      throw convertJsonException(obj, e);
    }
  }

  public JsonNode readTree(String obj) {
    try {
      return objectMapper.readTree(obj);
    } catch (Exception e) {
      throw convertJsonException(obj, e);
    }
  }

  public byte[] getJsonBytes(Object obj) {
    try {
      return objectMapper.writeValueAsBytes(obj);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public String getJsonString(Object obj) {
    try {
      return objectMapper.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  private IllegalArgumentException convertObjectException(Object obj, String type, Throwable e) {
    return new IllegalArgumentException(
        obj + " cannot convert to object: " + type + " reason:" + e.getMessage());
  }

  private IllegalArgumentException convertJsonException(Object obj, Throwable e) {
    return new IllegalArgumentException(
        obj + " cannot convert to json node, reason:" + e.getMessage());
  }
}
