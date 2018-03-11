package com.l9e.transaction.vo;

import java.util.LinkedHashMap;
import java.util.Map;

public class RegisterVo {

	private static final String REGISTER_WAIT = "00";
	private static final String REGISTER_ING = "11";
	private static final String REGISTER_SUCCESS = "22";
	private static final String REGISTER_FAIL = "33";
	private static final String REGISTER_MANMADE = "44";
	private static final String REGISTER_CHECK = "55";
	private static final String REGISTER_MAIL = "88";
	
	private static final String DEFAULT = "1";
	private static final String BULK_IMPORT = "2";
	private static final String MAN_MADE = "3";
	private static final String OTHER = "4";
	
	private static final String UNEXPORT = "0";
	private static final String EXPORTED = "1";


	private static Map<String, String> REGIST_STATUS = new LinkedHashMap<String, String>();
	public static Map<String, String> getRegist_Status(){
		if(REGIST_STATUS.isEmpty()){
			REGIST_STATUS.put(REGISTER_WAIT, "等待注册");
			REGIST_STATUS.put(REGISTER_ING, "注册中");
			REGIST_STATUS.put(REGISTER_SUCCESS, "注册成功");
			REGIST_STATUS.put(REGISTER_FAIL, "注册失败");
			REGIST_STATUS.put(REGISTER_MANMADE, "人工注册");
			REGIST_STATUS.put(REGISTER_CHECK, "需要核验");
			REGIST_STATUS.put(REGISTER_MAIL, "邮箱激活");
		}
		
		return REGIST_STATUS;
	}
	
	
	private static Map<String, String> ACCOUNT_SOURCE = new LinkedHashMap<String, String>();
	public static Map<String, String> getAccount_Source(){
		if(ACCOUNT_SOURCE.isEmpty()){
			ACCOUNT_SOURCE.put(DEFAULT, "19e");
			ACCOUNT_SOURCE.put(BULK_IMPORT, "批量导入");
			ACCOUNT_SOURCE.put(MAN_MADE, "人工添加");
			ACCOUNT_SOURCE.put(OTHER, "其他");
		}
		return ACCOUNT_SOURCE;
	}
	
	private static Map<String, String> IS_OUTPUT = new LinkedHashMap<String, String>();
	public static Map<String, String> getIs_Output(){
		if(IS_OUTPUT.isEmpty()){
			IS_OUTPUT.put(UNEXPORT, "未使用");
			IS_OUTPUT.put(EXPORTED, "已使用");
		}
		return IS_OUTPUT;
	}
	
	
}
