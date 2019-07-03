package com.soservice.util;

public class StringUtil {

	public static boolean isNotEmpty(String value) {
		if (value == null) {
			return false;
		}
		if (value.length() == 0) {
			return false;
		}
		return true;
	}

	/**
	 * null转空
	 * 
	 * @param str
	 * @return
	 */
	public static String nullToEmpty(String str) {
		if (str == null || "".equals(str.trim()) || "null".equals(str.trim())) {
			return "";
		} else {
			return str.trim();
		}
	}

	public static boolean isNotBlank(Double value) {
		if (value == null) {
			return false;
		}
		return true;
	}

	public static boolean isNotBlank(String value) {
		if (value == null) {
			return false;
		}
		return true;
	}
    /**
     * 字符串转unicode编码
     * @param string
     * @return
     */
	public static String string2Unicode(String string) {

		StringBuffer unicode = new StringBuffer();

		for (int i = 0; i < string.length(); i++) {

			// 取出每一个字符
			char c = string.charAt(i);

			// 转换为unicode
			unicode.append("\\u" + Integer.toHexString(c));
		}

		return unicode.toString();
	}
}
