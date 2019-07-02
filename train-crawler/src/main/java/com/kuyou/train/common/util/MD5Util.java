package com.kuyou.train.common.util;

import java.security.MessageDigest;

/**
 * MD5Util
 *
 * @author taokai3
 * @date 2018/11/8
 */
public class MD5Util {

    /**
     * @param source：数据源
     * @param charset：编码
     * @return String
     * @Title: md5
     * @Description: 加密
     * @author: taokai
     * @date: 2017年8月7日 下午2:02:00
     */
    public static final String md5(String source, String charset) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = source.getBytes(charset);

            MessageDigest mdInst = MessageDigest.getInstance("MD5");

            mdInst.update(btInput);

            byte[] md = mdInst.digest();

            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[(k++)] = hexDigits[(byte0 >>> 4 & 0xF)];
                str[(k++)] = hexDigits[(byte0 & 0xF)];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static final String md5(String source) {
        return md5(source, "UTF-8");
    }
}
