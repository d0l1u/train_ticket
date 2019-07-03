package com.l9e.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class CreateIDUtil {
	public static int no = 1;

	public static final Object LOCK = new Object();

	/**
	 * 用此方法创建各表的ID
	 * @param prex 前缀名称
	 * @return
	 */
	public static String createID(String prex) {

		String FORMAT1 = "MMddHHmmss";
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT1);
		String tranDate = sdf.format(new Date());
		int current = 1;
		synchronized (LOCK) {
			if (no > 9) {
				no = 1;
			}
			current += no++;
		}

		StringBuffer sb = new StringBuffer(prex);
		sb.append(tranDate);
//		.append(current);

		return sb.toString();
	}
	
	public static void main(String[] args) {
		System.out.println(createID(""));
		System.out.println(UUID.randomUUID().toString());
	}
}
