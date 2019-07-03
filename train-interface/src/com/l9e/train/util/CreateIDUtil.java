package com.l9e.train.util;

import java.text.SimpleDateFormat;
import java.util.Date;



public class CreateIDUtil {
	public static int no = 1;
	
	public static int user_no = 1;

	public static final byte[] LOCK = new byte[0];

	public static String createID(String headname) {

		String FORMAT1 = "yyMMddHHmmss";
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT1);
		String tranDate = sdf.format(new Date());
		int current = 100;
		synchronized (LOCK) {
			if (no > 999) {
				no = 100;
			}
			current += no++;
		}
		
		StringBuffer sb = new StringBuffer(headname);
		sb.append(tranDate).append(current);

		return sb.toString();
	}
	

}
