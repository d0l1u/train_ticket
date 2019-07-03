package com.l9e.train.util;

public class WorkIdNum {
	public static Integer selectNum = 1;
	public static Integer selectPayNum = 0;

	public static int getNextNum(int count) {
		synchronized (selectNum) {// 对指定渠道的分配selectNum进行加锁
			int num = selectNum;
			if (num >= count) {
				selectNum = 1;
			} else {
				selectNum = 1 + num;
			}
			return selectNum;
		}
	}

	public static int getPayNum(int count, int index) {
		synchronized (selectPayNum) {// 对指定渠道的分配selectNum进行加锁
			int num = selectPayNum;
			if (num >= count) {
				selectPayNum = 1;
			} else {
				selectPayNum = index + num;
			}
			return selectNum;
		}
	}
}
