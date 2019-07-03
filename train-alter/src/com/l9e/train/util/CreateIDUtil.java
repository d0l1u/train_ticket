package com.l9e.train.util;

import java.text.SimpleDateFormat;
import java.util.Date;




public class CreateIDUtil {
	public static int no = 1;
	public static int user_no = 1;
	public static int day = 60;
	
	public static int wea_alter = 1; 
	public static int wea_alter_e = 1; 
	public static int wea_alter_19 = 1; 
	
	public static final byte[] LOCK = new byte[0];

	public static String HEAD_ORDER = "OR";
	public static String HEAD_PAY = "PY";
	public static String HEAD_HF = "HF";
	public static String HEAD_NOTICE = "NOTICE";
	
	public static String createID(String headname) {

		String FORMAT1 = "yyMMddHHmmss";
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT1);
		String tranDate = sdf.format(new Date());
		int current = 1000;
		synchronized (LOCK) {
			if (no > 9999) {
				no = 1000;
			}
			current += no++;
		}

		
		StringBuffer sb = new StringBuffer(headname);
		sb.append(tranDate).append(current);

		return sb.toString();
	}
	
	
	/**
	 * 用此方法创建各表的ID
	 * @param prex 前缀名称
	 * @return
	 */
	public static String createID() {

		String FORMAT1 = "HHmmss";
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT1);
		synchronized (LOCK) {
			if (day >= 21) {
				day = 1;
			}
			day++;
		}
		
		synchronized (LOCK) {
			if (no >= 99) {
				no = 10;
			}
			no++;
		}
		StringBuffer sb = new StringBuffer(day).append(sdf.format(new Date())).append(no);
		return sb.toString();
	}
	

}
