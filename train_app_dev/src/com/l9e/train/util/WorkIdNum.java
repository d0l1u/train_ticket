package com.l9e.train.util;


public class WorkIdNum {
	public static Integer selectNum = 1; 
	
	public static Integer limit_day = 0;
	
	public static Integer no_order_day = 0;
	
	public static Integer book_num = 10;
	
	public static Integer contact_num = 25;
	
	public static Integer l9e_order_channel=0;
	
	public static Integer ctripAccSelectNum = 1; 
	
	public static Integer ctripWorkerSelectNum=1; 
	
	public static Integer jdAccSelectNum = 0;
	
	public static Integer jdWorkerSelectNum=1;
	
	public static Integer jdPreCardSelectNum=1;
	
	
	public static int getNextJdAccNum(int count) {
		synchronized (jdAccSelectNum){//对指定渠道的分配selectNum进行加锁
			int num=jdAccSelectNum;
			if (num >= count) {
				jdAccSelectNum = 0;
			} else {
				jdAccSelectNum = 1+num;
			}
			return jdAccSelectNum;
		}
		
	}
	
	public static int getNextJdWorkerNum(int count) {
		synchronized (jdWorkerSelectNum){//对指定渠道的分配selectNum进行加锁
			int num=jdWorkerSelectNum;
			if (num >= count) {
				jdWorkerSelectNum = 1;
			} else {
				jdWorkerSelectNum = 1+num;
			}
			return jdWorkerSelectNum;
		}
		
	}
	
	public static int getNextJdPreCardNum(int count) {
		synchronized (jdPreCardSelectNum){//对指定渠道的分配selectNum进行加锁
			int num=jdPreCardSelectNum;
			if (num >= count) {
				jdPreCardSelectNum = 1;
			} else {
				jdPreCardSelectNum = 1+num;
			}
			return jdPreCardSelectNum;
		}
		
	}
	
	public static int getctripWorkerSelectNum(int count) {
		synchronized (ctripWorkerSelectNum){//对指定渠道的分配selectNum进行加锁
			int num=ctripWorkerSelectNum;
			if (num >= count) {
				ctripWorkerSelectNum = 1;
			} else {
				ctripWorkerSelectNum = 1+num;
			}
			return ctripWorkerSelectNum;
		}
		
	}
	
	
	public static int getNextCtripNum(int count) {
		synchronized (ctripAccSelectNum){//对指定渠道的分配selectNum进行加锁
			int num=ctripAccSelectNum;
			if (num >= count) {
				ctripAccSelectNum = 1;
			} else {
				ctripAccSelectNum = 1+num;
			}
			return ctripAccSelectNum;
		}
		
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
