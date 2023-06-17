package com.shaoqin.fruit.utils;

/**
 * ClassName: StringUtil
 * Package: com.shaoqin.utils
 * Description:
 * Author Shaoqin
 * Create 6/15/23 2:49 PM
 * Version 1.0
 */
public class StringUtil {

    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

}
