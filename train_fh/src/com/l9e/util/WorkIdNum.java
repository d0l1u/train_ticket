package com.l9e.util;


public class WorkIdNum {
	public static Integer selectNum = 1; 
	
	public static Integer limit_day = 0;
	
	public static Integer no_order_day = 0;
	
	public static Integer book_num = 10;
	
	public static Integer contact_num = 50;
	
	
	public static String randCodeType="0";
	
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
	
	public static Integer accNum = 0; 
	
	public static int getAccNum(int count) {
		synchronized (accNum){//获取账号加锁
			int num=accNum;
			if (num >= count) {
				accNum = 0;
			} else {
				accNum = 1+num;
			}
			return accNum;
		}
	}
}
