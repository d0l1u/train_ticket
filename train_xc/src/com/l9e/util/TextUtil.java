package com.l9e.util;

import java.util.Random;

public class TextUtil {
	public static void main(String[] args) {
	}
	
	public  static String getBillno(){
		String str=String.valueOf(System.currentTimeMillis());
		return "E"+str.substring(str.length()-6);
	}
	
	public  static String getPaySeq(){
		String str=String.valueOf(System.currentTimeMillis());
		return "ZF"+str.substring(str.length()-8);
	}
	
	public  static int getTrainbox(){
		Random r=new Random();
		return r.nextInt(15)+1;
	}
	
	public  static int getSeatNo(){
		Random r=new Random();
		return r.nextInt(40)+1;
	}
	
	
	public  static String get12306tk(){
		String str=String.valueOf(System.currentTimeMillis());
		return "TK"+str.substring(str.length()-6);
	}
}
