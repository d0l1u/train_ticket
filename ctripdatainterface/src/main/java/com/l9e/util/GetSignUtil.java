package com.l9e.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class GetSignUtil {

	/**
	 * 将map的key排序，然后按照规则生成md5Str
	 * @param map
	 * @param secretStr
	 * @return
	 */
	public static String getSign(Map<String, String> map,String secretStr) {
		LinkedList<String> llist = new LinkedList<String>();
		llist.addAll(map.keySet());
		Collections.sort(llist);// 正向排序
		StringBuffer buf=new StringBuffer();
		for (Iterator<String> iter = llist.iterator(); iter.hasNext();) {
			 String key=iter.next();
			 String value=map.get(key);
			 buf.append(value).append(",");
		}
		buf.append(secretStr);
		String md5Str=Md5Encrypt.getKeyedDigestFor19Pay(secretStr, "", "utf-8");
		return md5Str;
	}

	public static void main(String[] args) {
		
		HashMap<String, String> mapTest = new HashMap<String, String>();
		mapTest.put("version", "10");
		mapTest.put("user", "");
		mapTest.put("to", "9");
		mapTest.put("sign", "8");
		mapTest.put("reqtime", "7");
		mapTest.put("is_mobile", "6");
		mapTest.put("from", "5");
		mapTest.put("format", "4");
		mapTest.put("encode", "3");
		mapTest.put("date", "2");
		mapTest.put("action", "1");
		System.out.println(GetSignUtil.getSign(mapTest,"cbbb99987777"));

	}

}
