package com.l9e.transaction.vo;

import java.util.LinkedHashMap;
import java.util.Map;

public class AccountVo {
	
	private static final String DEFAULT = "1"; //19e
	private static final String BULK_IMPORT = "2"; //批量导入
	private static final String MAN_MADE = "3"; //人工添加
	private static final String OTHER = "4"; //其他
	
	public static String DOWNING = "00";//正在下单
	public static String STOP = "22";//账号停用
	public static String FREE = "33"; //账号空闲
	public static String  MANUAL = "55"; //人工启用
	public static String  OCCUPY = "66"; //账号占座
	
	//账号状态：00、等待注册 11、注册中 22、注册/认证失败 33、等待认证 44、认证中 55、认证通过 66、认证未通过
//	public static String WAIT_REGISTER = "00"; //等待注册
//	public static String REGISTERING = "11"; //注册中 
//	public static String FAIL_REGISTER = "22"; //注册/认证失败 
//	public static String WAIT_ATTEST = "33"; //等待认证
//	public static String ATTESTING = "44"; //认证中
//	public static String PASS_ATTEST = "55"; //认证通过
//	public static String FAIL_ATTEST = "66"; //认证未通过
	//实名认证：1已通过 2未通过 3未认证
	public static String PASS = "1"; //已通过
	public static String NOPASS = "2"; //未通过
	public static String NOREGISTER = "3"; //未认证
	
	private static Map<String, String> ACCOUNTSTATUS = new LinkedHashMap<String, String>();
	private static Map<String, String> ACCOUNTSTOPREASON = new LinkedHashMap<String, String>();
	public static Map<String, String> CHANEL_TYPE = new LinkedHashMap<String, String>();
	public static Map<String, String> CHANEL_TJ_TYPE = new LinkedHashMap<String, String>();
	public static Map<String, String> REGISTER_STATUS = new LinkedHashMap<String, String>();
	
	static{
//		REGISTER_STATUS.put(WAIT_REGISTER, "等待注册 ");
//		REGISTER_STATUS.put(REGISTERING, "注册中 ");
//		REGISTER_STATUS.put(FAIL_REGISTER, "注册/认证失败 ");
//		REGISTER_STATUS.put(WAIT_ATTEST, "等待认证");
//		REGISTER_STATUS.put(ATTESTING, "认证中");
//		REGISTER_STATUS.put(PASS_ATTEST, "认证通过");
//		REGISTER_STATUS.put(FAIL_ATTEST, "认证未通过");
		REGISTER_STATUS.put(PASS, "已通过");
		REGISTER_STATUS.put(NOPASS, "未通过");
		REGISTER_STATUS.put(NOREGISTER, "未认证");
	}
	public static Map<String, String> getRegisterStatus(){
		//账号状态：00、等待注册 11、注册中 22、注册/认证失败 33、等待认证 44、认证中 55、认证通过 66、认证未通过
		return REGISTER_STATUS;
	}
	
	private String acc_id;
	private String acc_username;
	private String acc_password;
	private String province_name;
	private String city_name;
	private String acc_status;
	private String at_province_id;
	private String at_city_id ;
	private String channel;
	private String opt_person;
	

	private String opt_logs;
	private String order_id;
	private String acc_mail;
	private String stop_reason;
	private String real_name;
	private String id_card;
	
	
	public String getReal_name() {
		return real_name;
	}



	public void setReal_name(String realName) {
		real_name = realName;
	}



	public String getId_card() {
		return id_card;
	}



	public void setId_card(String idCard) {
		id_card = idCard;
	}



	public String getAcc_mail() {
		return acc_mail;
	}



	public void setAcc_mail(String accMail) {
		acc_mail = accMail;
	}



	public String getOrder_id() {
		return order_id;
	}



	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}



	public String getOpt_logs() {
		return opt_logs;
	}



	public void setOpt_logs(String opt_logs) {
		this.opt_logs = opt_logs;
	}


	public static String CHANNEL_19E = "19e";
	public static String CHANNEL_qunar = "qunar";
	public static String CHANNEL_qunar1 = "qunar1";
	public static String CHANNEL_qunar2 = "qunar2";
	public static String CHANNEL_inner = "inner";
	public static String CHANNEL_cmpay = "cmpay";
	public static String CHANNEL_cmwap = "cmwap";
	public static String CHANNEL_19PAY = "19pay";
	public static String CHANNEL_19TRIP = "web";
	public static String CHANNEL_APP = "app";
	public static String CHANNEL_CBCPAY = "ccb";
	public static String CHANNEL_WEIXIN = "weixin";
	public static String CHANNEL_ELONG = "elong";
	public static String CHANNEL_TONGCHENG = "tongcheng";
	public static String CHANNEL_MEITUAN = "meituan";
	public static String CHANNEL_TUNIU = "tuniu";
	public static String CHANNEL_CHQ = "chq";
	public static Map<String,String>getChannels(){
		if(CHANEL_TYPE.isEmpty()){
			CHANEL_TYPE.put(CHANNEL_19E, "19e");
			CHANEL_TYPE.put(CHANNEL_qunar, "去哪");
			CHANEL_TYPE.put(CHANNEL_cmpay, "cmpay");
			CHANEL_TYPE.put(CHANNEL_cmwap, "cmwap");
			CHANEL_TYPE.put(CHANNEL_19PAY, "19pay");
			CHANEL_TYPE.put(CHANNEL_19TRIP, "19trip");
			CHANEL_TYPE.put(CHANNEL_APP, "app");
			CHANEL_TYPE.put(CHANNEL_CBCPAY, "建行");
			CHANEL_TYPE.put(CHANNEL_CHQ, "春秋");
			CHANEL_TYPE.put(CHANNEL_WEIXIN, "微信");
			CHANEL_TYPE.put(CHANNEL_ELONG, "艺龙");
			CHANEL_TYPE.put(CHANNEL_TONGCHENG, "同程");
			CHANEL_TYPE.put(CHANNEL_MEITUAN, "美团");
			CHANEL_TYPE.put(CHANNEL_TUNIU, "途牛");
			
		}
		return CHANEL_TYPE;
	}
	public static Map<String,String>getTjChannels(){//出票统计处的map
		if(CHANEL_TJ_TYPE.isEmpty()){
			CHANEL_TJ_TYPE.put(CHANNEL_19E, "19e");
			CHANEL_TJ_TYPE.put(CHANNEL_qunar, "去哪");
			CHANEL_TJ_TYPE.put(CHANNEL_qunar1, "19旅行");
			CHANEL_TJ_TYPE.put(CHANNEL_qunar2, "久久");
			CHANEL_TJ_TYPE.put(CHANNEL_inner, "内嵌");
			CHANEL_TJ_TYPE.put(CHANNEL_cmpay, "cmpay");
			CHANEL_TJ_TYPE.put(CHANNEL_cmwap, "cmwap");
			CHANEL_TJ_TYPE.put(CHANNEL_CHQ, "春秋");
			CHANEL_TJ_TYPE.put(CHANNEL_19PAY, "19pay");
			CHANEL_TJ_TYPE.put(CHANNEL_CBCPAY, "建行");
			CHANEL_TJ_TYPE.put(CHANNEL_19TRIP, "19trip");
			CHANEL_TJ_TYPE.put(CHANNEL_APP, "app");
			CHANEL_TJ_TYPE.put(CHANNEL_WEIXIN, "微信");
			CHANEL_TJ_TYPE.put(CHANNEL_ELONG, "艺龙");
			CHANEL_TJ_TYPE.put(CHANNEL_TONGCHENG, "同程");
			CHANEL_TJ_TYPE.put(CHANNEL_MEITUAN, "美团");
			CHANEL_TJ_TYPE.put(CHANNEL_TUNIU, "途牛");
			
		}
		return CHANEL_TJ_TYPE;
	}
	
	//停用原因：1账号被封 2取消订单过多 3联系人达上限 4未实名制
	public static String FENG = "1";
	public static String CANCEL = "2";
	public static String LINK = "3";
	public static String NOREALNAME = "4";
	public static String DINGGOU = "5";
	public static String GOBACK = "6";
	public static String PHONECHECK = "7";
	static{
		ACCOUNTSTATUS.put(DOWNING, "正在下单 ");
		ACCOUNTSTATUS.put(STOP, "账号停用 ");
		ACCOUNTSTATUS.put(FREE, "账号空闲 ");
		ACCOUNTSTATUS.put(MANUAL, "人工启用 ");
		ACCOUNTSTATUS.put(OCCUPY, "账号占座 ");
		ACCOUNTSTOPREASON.put(FENG, "账号被封");
		ACCOUNTSTOPREASON.put(CANCEL, "取消订单过多");
		ACCOUNTSTOPREASON.put(LINK, "联系人达上限");
		ACCOUNTSTOPREASON.put(NOREALNAME, "未实名制");
		ACCOUNTSTOPREASON.put(DINGGOU, "已达订购上限");
		ACCOUNTSTOPREASON.put(GOBACK, "用户取回");
		ACCOUNTSTOPREASON.put(PHONECHECK, "手机核验");
	}
	public static Map<String, String> getAccoutStatus(){
		//00、正在下单；22、账号停用；33、账号空闲	
		return ACCOUNTSTATUS;
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
	
	
	public static Map<String,String> getAccountStopreason(){
		return ACCOUNTSTOPREASON;
	}
	
	public String getAcc_id() {
		return acc_id;
	}



	public void setAcc_id(String acc_id) {
		this.acc_id = acc_id;
	}



	public String getAcc_status() {
		return acc_status;
	}


	public void setAcc_status(String acc_status) {
		this.acc_status = acc_status;
	}


	public String getAcc_username() {
		return acc_username;
	}


	public void setAcc_username(String acc_username) {
		this.acc_username = acc_username;
	}


	public String getAcc_password() {
		return acc_password;
	}


	public void setAcc_password(String acc_password) {
		this.acc_password = acc_password;
	}



	public String getProvince_name() {
		return province_name;
	}



	public void setProvince_name(String province_name) {
		this.province_name = province_name;
	}



	public String getCity_name() {
		return city_name;
	}



	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}



	public String getAt_province_id() {
		return at_province_id;
	}



	public void setAt_province_id(String at_province_id) {
		this.at_province_id = at_province_id;
	}



	public String getAt_city_id() {
		return at_city_id;
	}



	public void setAt_city_id(String at_city_id) {
		this.at_city_id = at_city_id;
	}



	public String getChannel() {
		return channel;
	}



	public void setChannel(String channel) {
		this.channel = channel;
	}



	public String getOpt_person() {
		return opt_person;
	}


	public void setOpt_person(String opt_person) {
		this.opt_person = opt_person;
	}

	public String getStop_reason() {
		return stop_reason;
	}

	public void setStop_reason(String stopReason) {
		stop_reason = stopReason;
	}
	
	//错误信息：1所购买的车次坐席已无票 2身份证件已经实名制购票 3票价和12306不符 4乘车时间异常 
	//5证件错误 6用户要求取消订单 7未通过12306实名认证 8乘客身份信息待核验 
	private static Map<String, String> ERROR_INFO = new LinkedHashMap<String, String>();
	public static String ERROR_INFO_1 = "1";
	public static String ERROR_INFO_2 = "2";
	public static String ERROR_INFO_3 = "3";
	public static String ERROR_INFO_4 = "4";
	public static String ERROR_INFO_5 = "5";
	public static String ERROR_INFO_6 = "6";
	public static String ERROR_INFO_7 = "7";
	public static String ERROR_INFO_8 = "8";
	public static String ERROR_INFO_11 = "11";
	public static String ERROR_INFO_9 = "9";
	public static String ERROR_INFO_10 = "10";
	public static String ERROR_INFO_12 = "12";
	public static Map<String,String>getErrorInfos(){
		if(ERROR_INFO.isEmpty()){
			ERROR_INFO.put(ERROR_INFO_1, "所购买的车次坐席已无票");
			ERROR_INFO.put(ERROR_INFO_2, "身份证件已经实名制购票");
			ERROR_INFO.put(ERROR_INFO_3, "票价和12306不符");
			ERROR_INFO.put(ERROR_INFO_4, "乘车时间异常");
			ERROR_INFO.put(ERROR_INFO_5, "证件错误");
			ERROR_INFO.put(ERROR_INFO_6, "用户要求取消订单");
			ERROR_INFO.put(ERROR_INFO_7, "未通过12306实名认证");
			ERROR_INFO.put(ERROR_INFO_8, "乘客身份信息待核验");
			ERROR_INFO.put(ERROR_INFO_11, "乘客超时未支付");
			ERROR_INFO.put(ERROR_INFO_9, "系统异常");
			ERROR_INFO.put(ERROR_INFO_10, "高消费限制失败");
			ERROR_INFO.put(ERROR_INFO_12, "信息冒用");
		}
		return ERROR_INFO;
	}
	//【qunar】错误信息：0、其他 1、所购买的车次坐席已无票 2、身份证件已经实名制购票，不能再次购买同日期同车次的车票
	//3、qunar票价和12306不符 4、车次数据与12306不一致 5、乘客信息错误 6、12306乘客身份信息核验失败
	private static Map<String, String> ERROR_INFO_QUNAR = new LinkedHashMap<String, String>();
	public static String ERROR_INFO_QUNAR_0 = "0";
	public static String ERROR_INFO_QUNAR_1 = "1";
	public static String ERROR_INFO_QUNAR_2 = "2";
	public static String ERROR_INFO_QUNAR_3 = "3";
	public static String ERROR_INFO_QUNAR_4 = "4";
	public static String ERROR_INFO_QUNAR_5 = "5";
	public static String ERROR_INFO_QUNAR_6 = "6";
	public static String ERROR_INFO_QUNAR_7 = "7";
	public static Map<String,String>getErrorInfoQunars(){
		if(ERROR_INFO_QUNAR.isEmpty()){
			ERROR_INFO_QUNAR.put(ERROR_INFO_QUNAR_0, "其他");
			ERROR_INFO_QUNAR.put(ERROR_INFO_QUNAR_1, "所购买的车次坐席已无票");
			ERROR_INFO_QUNAR.put(ERROR_INFO_QUNAR_2, "身份证件已经实名制购票");
			ERROR_INFO_QUNAR.put(ERROR_INFO_QUNAR_3, "票价和12306不符");
			ERROR_INFO_QUNAR.put(ERROR_INFO_QUNAR_4, "车次数据与12306不一致");
			ERROR_INFO_QUNAR.put(ERROR_INFO_QUNAR_5, "乘客信息错误");
			ERROR_INFO_QUNAR.put(ERROR_INFO_QUNAR_6, "12306乘客身份信息核验失败");
			//在此新增失败原因：7：信息冒用
			ERROR_INFO_QUNAR.put(ERROR_INFO_QUNAR_7, "信息冒用");
		}
		return ERROR_INFO_QUNAR;
	}
	
	//【elong】错误信息：0、其他 1、所购买的车次坐席已无票 2、身份证件已经实名制购票，不能再次购买同日期同车次的车票
	//3、elong票价和12306不符 4、车次数据与12306不一致 5、乘客信息错误 6、12306乘客身份信息核验失败
	private static Map<String, String> ERROR_INFO_ELONG = new LinkedHashMap<String, String>();
//	public static String ERROR_INFO_ELONG_0 = "0";
//	public static String ERROR_INFO_ELONG_1 = "1";
//	public static String ERROR_INFO_ELONG_2 = "2";
//	public static String ERROR_INFO_ELONG_3 = "3";
//	public static String ERROR_INFO_ELONG_4 = "4";
//	public static String ERROR_INFO_ELONG_5 = "5";
//	public static String ERROR_INFO_ELONG_6 = "6";
	public static Map<String,String>getErrorInfoElongs(){
		if(ERROR_INFO_ELONG.isEmpty()){
			ERROR_INFO_ELONG.put(ERROR_INFO_1, "所购买的车次坐席已无票");
			ERROR_INFO_ELONG.put(ERROR_INFO_2, "身份证件已经实名制购票");
			ERROR_INFO_ELONG.put(ERROR_INFO_3, "票价和12306不符");
			ERROR_INFO_ELONG.put(ERROR_INFO_4, "乘车时间异常");
			ERROR_INFO_ELONG.put(ERROR_INFO_5, "证件错误");
			ERROR_INFO_ELONG.put(ERROR_INFO_8, "用户要求取消订单");
			ERROR_INFO_ELONG.put(ERROR_INFO_7, "未通过12306实名认证");
			ERROR_INFO_ELONG.put(ERROR_INFO_6, "乘客身份信息待核验");
			ERROR_INFO_ELONG.put(ERROR_INFO_11, "乘客超时未支付");
			ERROR_INFO_ELONG.put(ERROR_INFO_9, "系统异常");
			ERROR_INFO_ELONG.put(ERROR_INFO_10, "高消费限制失败");
			//在此新增失败原因：12：信息冒用
			ERROR_INFO_ELONG.put(ERROR_INFO_12, "信息冒用");
		}
		return ERROR_INFO_ELONG;
	}
	//渠道
	private static Map<String, String> ERROR_INFO_CHANNEL = new LinkedHashMap<String, String>();
	
	public static String ERROR_INFO_CHANNEL_19E = "19e";
	public static String ERROR_INFO_CHANNEL_cmpay = "cmpay";
	public static String ERROR_INFO_CHANNEL_cmwap = "cmwap";
	public static String ERROR_INFO_CHANNEL_19PAY = "19pay";
	public static String ERROR_INFO_CHANNEL_19TRIP = "web";
	public static String ERROR_INFO_CHANNEL_APP = "app";
	public static String ERROR_INFO_CHANNEL_CCB = "ccb";
	public static String ERROR_INFO_CHANNEL_CHQ = "chq";
	public static String ERROR_INFO_CHANNEL_WEIXIN = "weixin";
	public static String ERROR_INFO_CHANNEL_ELONG = "elong";
	public static String ERROR_INFO_CHANNEL_TONGCHENG = "tongcheng";
	public static String ERROR_INFO_CHANNEL_MEITUAN = "meituan";
	public static String ERROR_INFO_CHANNEL_TUNIU = "tuniu";
	public static Map<String,String>getErrorInfoChannels(){
		if(ERROR_INFO_CHANNEL.isEmpty()){
			ERROR_INFO_CHANNEL.put(ERROR_INFO_CHANNEL_19E, "19e");
			ERROR_INFO_CHANNEL.put(ERROR_INFO_CHANNEL_cmpay, "cmpay");
			ERROR_INFO_CHANNEL.put(ERROR_INFO_CHANNEL_cmwap, "cmwap");
			ERROR_INFO_CHANNEL.put(ERROR_INFO_CHANNEL_19PAY, "19pay");
			ERROR_INFO_CHANNEL.put(ERROR_INFO_CHANNEL_19TRIP, "19trip");
			ERROR_INFO_CHANNEL.put(ERROR_INFO_CHANNEL_APP, "app");
			ERROR_INFO_CHANNEL.put(ERROR_INFO_CHANNEL_CCB, "建行");
			ERROR_INFO_CHANNEL.put(ERROR_INFO_CHANNEL_CHQ, "春秋");
			ERROR_INFO_CHANNEL.put(ERROR_INFO_CHANNEL_WEIXIN, "微信");
			ERROR_INFO_CHANNEL.put(ERROR_INFO_CHANNEL_ELONG, "艺龙");
			ERROR_INFO_CHANNEL.put(ERROR_INFO_CHANNEL_TONGCHENG, "同程");
			ERROR_INFO_CHANNEL.put(ERROR_INFO_CHANNEL_MEITUAN, "美团");
			ERROR_INFO_CHANNEL.put(ERROR_INFO_CHANNEL_TUNIU, "途牛");
		}
		return ERROR_INFO_CHANNEL;
	}
	
	//类别：11公司、22商户、33内嵌、44B2C、55代理
	private static Map<String, String> SORT_CHANNEL = new LinkedHashMap<String, String>();
	public static String SORT_CHANNEL_COMPANY = "11";
	public static String SORT_CHANNEL_MERCHANT = "22";
	public static String SORT_CHANNEL_INNER = "33";
	public static String SORT_CHANNEL_B2C = "44";
	public static String SORT_CHANNEL_AGENCY = "55";
	public static Map<String,String>getSortChannels(){
		if(SORT_CHANNEL.isEmpty()){
			SORT_CHANNEL.put(SORT_CHANNEL_COMPANY, "公司");
			SORT_CHANNEL.put(SORT_CHANNEL_MERCHANT, "商户");
			SORT_CHANNEL.put(SORT_CHANNEL_INNER, "内嵌");
			SORT_CHANNEL.put(SORT_CHANNEL_B2C, "B2C");
			SORT_CHANNEL.put(SORT_CHANNEL_AGENCY, "代理");
		}
		return SORT_CHANNEL;
	}
	private static Map<String, String> SORT_CHANNEL_BX = new LinkedHashMap<String, String>();
	public static Map<String,String>getBxSortChannels(){
		if(SORT_CHANNEL_BX.isEmpty()){
			SORT_CHANNEL_BX.put(SORT_CHANNEL_COMPANY, "公司");
			SORT_CHANNEL_BX.put(SORT_CHANNEL_MERCHANT, "商户");
			SORT_CHANNEL_BX.put(SORT_CHANNEL_INNER, "内嵌");
			SORT_CHANNEL_BX.put(SORT_CHANNEL_B2C, "B2C");
		}
		return SORT_CHANNEL_BX;
	}
	
	//类别：11公司  (19e、19pay)
	private static Map<String, String> COMPANY_CHANNEL = new LinkedHashMap<String, String>();
	public static String COMPANY_CHANNEL_19E_ = "19e";
	public static String COMPANY_CHANNEL_19PAY = "19pay";
	public static Map<String,String>getCompanyChannels(){
		if(COMPANY_CHANNEL.isEmpty()){
			COMPANY_CHANNEL.put(COMPANY_CHANNEL_19E_, "19e");
			COMPANY_CHANNEL.put(COMPANY_CHANNEL_19PAY, "19pay");
		}
		return COMPANY_CHANNEL;
	}
	
	//类别：33内嵌(cmpay、ccb)
	private static Map<String, String> INNER_CHANNEL = new LinkedHashMap<String, String>();
	public static String INNER_CHANNEL_CMPAY = "cmpay";
	public static String INNER_CHANNEL_CMWAP = "cmwap";
	public static String INNER_CHANNEL_CCB = "ccb";
	public static String INNER_CHANNEL_CHQ = "chq";
	public static Map<String,String>getInnerChannels(){
		if(INNER_CHANNEL.isEmpty()){
			INNER_CHANNEL.put(INNER_CHANNEL_CMPAY, "cmpay");
			INNER_CHANNEL.put(INNER_CHANNEL_CMWAP, "cmwap");
			INNER_CHANNEL.put(INNER_CHANNEL_CCB, "建行");
			INNER_CHANNEL.put(INNER_CHANNEL_CHQ, "春秋");
		}
		return INNER_CHANNEL;
	}
	
	//类别：44B2C(app、weixin)
	private static Map<String, String> B2C_CHANNEL = new LinkedHashMap<String, String>();
	public static String B2C_CHANNEL_19TRIP = "web";
	public static String B2C_CHANNEL_APP = "app";
	public static String B2C_CHANNEL_WEIXIN = "weixin";
	public static Map<String,String>getB2cChannels(){
		if(B2C_CHANNEL.isEmpty()){
			B2C_CHANNEL.put(B2C_CHANNEL_19TRIP, "19trip");
			B2C_CHANNEL.put(B2C_CHANNEL_APP, "app");
			B2C_CHANNEL.put(B2C_CHANNEL_WEIXIN, "微信");
		}
		return B2C_CHANNEL;
	}
	
	//类别：55代理 (去哪)
	private static Map<String, String> AGENCY_CHANNEL = new LinkedHashMap<String, String>();
	public static String AGENCY_CHANNEL_QUNAR = "qunar";
	public static String AGENCY_CHANNEL_QUNAR1 = "qunar1";
	public static String AGENCY_CHANNEL_QUNAR2 = "qunar2";
	public static Map<String,String>getAgencyChannels(){
		if(AGENCY_CHANNEL.isEmpty()){
			AGENCY_CHANNEL.put(AGENCY_CHANNEL_QUNAR, "去哪");
			AGENCY_CHANNEL.put(AGENCY_CHANNEL_QUNAR1, "19旅行");
			AGENCY_CHANNEL.put(AGENCY_CHANNEL_QUNAR2, "久久");
		}
		return AGENCY_CHANNEL;
	}
	
	
}
