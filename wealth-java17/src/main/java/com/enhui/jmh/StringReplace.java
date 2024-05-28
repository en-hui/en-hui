package com.enhui.jmh;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Slf4j
public class StringReplace {

    public static Object removeNullChar(Object value, boolean isRemove) {
        if (isRemove) {
            if (value != null) {
                if (value instanceof byte[]) {
                    byte[] byteValue = (byte[]) value;
                    byte[] replaceBytes = byteValue;
                    String str = new String(byteValue);
                    String replace = str.replace("\u0000", "");
                    if (!str.equals(replace)) {
                        replaceBytes = replace.getBytes();
                        log.info("removeNullChar:: before-「{}」,after-「{}」", str, replace);
                        log.info(
                                "removeNullChar:: before byte[]-「{}」,after byte[]-「{}」", value, replaceBytes);
                    }
                    return replaceBytes;
                } else {
                    String str = value.toString();
                    String replace = str.replace("\u0000", "");
                    if (!str.equals(replace)) {
                        log.info("removeNullChar:: before-「{}」,after-「{}」", str, replace);
                    }
                    return replace;
                }
            }
        }
        return value;
    }


    public static Object removeNullCharNew(Object value, boolean isRemove) {
        if (isRemove && value != null) {
            String originalString;
            String replacedString;

            if (value instanceof byte[]) {
                byte[] byteValue = (byte[]) value;
                originalString = new String(byteValue, StandardCharsets.UTF_8);
            } else {
                originalString = value.toString();
            }

            replacedString = originalString.replace("\u0000", "");

            if (!originalString.equals(replacedString)) {
                log.info("removeNullChar:: before-「{}」,after-「{}」", originalString, replacedString);

                if (value instanceof byte[]) {
                    return replacedString.getBytes(StandardCharsets.UTF_8);
                } else {
                    return replacedString;
                }
            }
        }
        return value;
    }
}
