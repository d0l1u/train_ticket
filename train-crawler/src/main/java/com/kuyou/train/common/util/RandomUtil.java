package com.kuyou.train.common.util;

import java.util.concurrent.ThreadLocalRandom;

/**
 * RandomUtil
 *
 * @author taokai3
 * @date 2018/11/15
 */
public class RandomUtil {


    public static String getLogId() {
        StringBuffer sb = new StringBuffer();
        while (sb.length() < 6) {
            sb.append(ThreadLocalRandom.current().nextInt(10));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.err.println(getLogId());
    }
}
