package com.kuyou.train.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * RegexUtil
 *
 * @author taokai3
 * @date 2018/11/12
 */
public class RegexUtil {


    public static String matcher(String source, Pattern pattern) {
        Matcher mat = pattern.matcher(source);
        if (mat.find()) {
            return mat.group(1);
        }
        return "";
    }
}
