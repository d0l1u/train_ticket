package com.l9e.util;

import java.io.UnsupportedEncodingException;

public class CommStr {
	/**
	 * url地址转换工具类
	 */
	public static java.lang.String strReplace(java.lang.String destStr,
			java.lang.String oldStr, java.lang.String newStr) {
		if (destStr == null)
			return "";
		String tmpStr = destStr;
		int foundPos = tmpStr.indexOf(oldStr);
		while (foundPos >= 0) {
			tmpStr = tmpStr.substring(0, foundPos)
					+ newStr
					+ tmpStr.substring(foundPos + oldStr.length(), tmpStr
							.length());
			foundPos = tmpStr.indexOf(oldStr, foundPos + newStr.length());
		}
		return tmpStr;
	}

	/**
	 *Encode for HTML.
	 */
	public static String htmlEncoder(String str) {
		if (str == null || str.equals(""))
			return "";
		String res_str;
		res_str = strReplace(str, "&", "&amp;");
		res_str = strReplace(res_str, " ", "&nbsp;");
		res_str = strReplace(res_str, "<", "&lt;");
		res_str = strReplace(res_str, ">", "&rt;");
		res_str = strReplace(res_str, "\"", "&quot;");
		res_str = strReplace(res_str, "'", "&#039;");
		return res_str;
	}

	/**
	 *Encode for HTML-Text.
	 */
	public static String htmlTextEncoder(String str) {
		if (str == null || str.equals(""))
			return "";
		String res_str;
		res_str = strReplace(str, "&", "&amp;");
		res_str = strReplace(res_str, "<", "&lt;");
		res_str = strReplace(res_str, ">", "&rt;");
		res_str = strReplace(res_str, "\"", "&quot;");
		res_str = strReplace(res_str, "'", "&#039;");
		res_str = strReplace(res_str, " ", "&nbsp;");
		res_str = strReplace(res_str, "\r\n", "<br>");
		res_str = strReplace(res_str, "\r", "<br>");
		res_str = strReplace(res_str, "\n", "<br>");
		return res_str;
	}

	/**
	 *Encode for URL.
	 */
	public static String urlEncoder(String str) {
		try {
			str = java.net.URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 *Encode for XML.
	 */
	public static String xmlEncoder(String str) {
		if (str == null || str.equals(""))
			return "";
		String res_str;
		res_str = strReplace(str, "&", "&amp;");
		res_str = strReplace(res_str, "<", "&lt;");
		res_str = strReplace(res_str, ">", "&gt;");
		res_str = strReplace(res_str, "\"", "&quot;");
		res_str = strReplace(res_str, "\'", "&acute;");
		return res_str;
	}

	/**
	 *Encode for SQL.
	 */
	public static String sqlEncoder(String str) {
		if (str == null || str.equals(""))
			return "";
		String res_str;
		res_str = strReplace(str, "'", "''");
		return res_str;
	}

	/**
	 *Encode for Javascript.
	 */
	public static String jsEncoder(String str) {
		if (str == null || str.equals(""))
			return "";
		String res_str;
		res_str = strReplace(str, "\\", "\\\\"); // 将\替换成\\
		res_str = strReplace(res_str, "'", "\\'"); // 将'替换成\'
		res_str = strReplace(res_str, "\"", "\\\"");// 将"替换成\"
		res_str = strReplace(res_str, "\r\n", "\\\n");// 将\r\n替换成\\n
		res_str = strReplace(res_str, "\n", "\\\n");// 将\n替换成\\n
		res_str = strReplace(res_str, "\r", "\\\n");// 将\r替换成\\n
		return res_str;
	}

	/**
	 *Encode for Javascript.
	 */
	public static String javaEncoder(String str) {//返回去
		if (str == null || str.equals(""))
			return "";
		String res_str;
		res_str = strReplace(str, "\\\\", "\\"); // 将\替换成\\
		res_str = strReplace(res_str, "\\'", "'"); // 将'替换成\'
		res_str = strReplace(res_str, "\\\"", "\"");// 将"替换成\"
		res_str = strReplace(res_str, "\\\n" , "\r\n");// 将\r\n替换成\\n
		res_str = strReplace(res_str, "\\\n" , "\n");// 将\n替换成\\n
		res_str = strReplace(res_str, "\\\n" , "\r");// 将\r替换成\\n
		return res_str;
	}
	
}
