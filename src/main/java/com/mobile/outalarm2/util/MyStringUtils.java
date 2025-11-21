package com.mobile.outalarm2.util;

public class MyStringUtils {
    /**
     * 移除字符串中的0x00空字节
     */
    public static String removeNullByte(String str) {
        if (str == null) {
            return null;
        }
        // 替换0x00为空字符串
        return str.replace("\u0000", "");
    }
}
