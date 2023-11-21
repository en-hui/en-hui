package com.enhui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式的测试
 */
public class RegexPatternTest {
    public static final Pattern TRUNCATE_PATTERN =
            Pattern.compile("TRUNCATE.+", Pattern.CASE_INSENSITIVE);

    public static void main(String[] args) {
        Matcher matcher = TRUNCATE_PATTERN.matcher("truncate  a");
        System.out.println(matcher.find());
        matcher = TRUNCATE_PATTERN.matcher("truncate  table a.a");
        System.out.println(matcher.find());

    }
}
