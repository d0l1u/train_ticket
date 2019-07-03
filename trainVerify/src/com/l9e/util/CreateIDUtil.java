package com.l9e.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateIDUtil {
	public static int no = 1;
	public static Integer selectNum = 1; 
	
	public static Integer HEYAN = 10; 
	public static final Object LOCK = new Object();

	/**
	 * 用此方法创建各表的ID
	 * @param prex 前缀名称
	 * @return
	 */
	public static String createID(String prex) {

		String FORMAT1 = "yyyyMMddHHmmss";
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT1);
		String tranDate = sdf.format(new Date());
		int current = 1000;
		synchronized (LOCK) {
			if (no > 9999) {
				no = 1000;
			}
			current += no++;
		}

		StringBuffer sb = new StringBuffer(prex);
		sb.append(tranDate).append(current);

		return sb.toString();
	}
	
	public static int getNextNum(int count) {
		synchronized (selectNum){//对指定渠道的分配selectNum进行加锁
			int num=selectNum;
			if (num >= count) {
				selectNum = 1;
			} else {
				selectNum = 1+num;
			}
			return selectNum;
		}
		
	}
}
