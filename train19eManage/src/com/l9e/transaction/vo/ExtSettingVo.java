package com.l9e.transaction.vo;

import java.util.LinkedHashMap;
import java.util.Map;

public class ExtSettingVo {
	private String merchant_id;//合作商户编号
	private String sign_key;//合作商户密钥
	private String merchant_name;//合作商户名称
	private String merchant_version;//合作商户使用接口的版本号
	private String merchant_terminal;//合作商户请求终端
	private String pay_order_fee;//平台支付手续费
	private String pay_type;//支付方式：11、自动代扣；22、商户自行扣费
	private String md5_type;//net、使用java自带加密； java、使用apache.common 的jar包md5加密
	private String merchant_fee;//扣费方式：00、我们保险；11、自用保险（每笔收取3元）；22、0.6元；33、0.7元；44、0.8元；55、0.9元；66、1.0元；77、1.1元；88、1.2元
	private String bx_company;//保险单位：11、快保；22、合众
	private String sms_channel;//短信渠道：00、19e；11、鼎鑫移动；22、企信通
	//private String ticket_time_limit;//默认乘车日期：00、当天；11、1天；22、2天；33、3天
	private String stop_buyTicket_time;//开车之前的停止购票时间:00、小时；11、4小时；22、5小时；33、6小时
	private String merchant_status;//商户状态：00、停用 11、启用
	private String create_time;//创建时间
	private String spare_ticket_amount;//余票阀值
	private String merchant_stop_reason;//商户停用原因
	
	
	private String verify_status;//验证状态：00、停用 11、启用
	private String verify_total_num;//验证用户信息总数
	
	public String getVerify_status() {
		return verify_status;
	}
	public void setVerify_status(String verifyStatus) {
		verify_status = verifyStatus;
	}
	public String getVerify_total_num() {
		return verify_total_num;
	}
	public void setVerify_total_num(String verifyTotalNum) {
		verify_total_num = verifyTotalNum;
	}
	public String getMerchant_status() {
		return merchant_status;
	}
	public void setMerchant_status(String merchantStatus) {
		merchant_status = merchantStatus;
	}
	public String getMerchant_id() {
		return merchant_id;
	}
	public void setMerchant_id(String merchantId) {
		merchant_id = merchantId;
	}
	public String getSign_key() {
		return sign_key;
	}
	public void setSign_key(String signKey) {
		sign_key = signKey;
	}
	public String getMerchant_name() {
		return merchant_name;
	}
	public void setMerchant_name(String merchantName) {
		merchant_name = merchantName;
	}
	public String getMerchant_version() {
		return merchant_version;
	}
	public void setMerchant_version(String merchantVersion) {
		merchant_version = merchantVersion;
	}
	public String getMerchant_terminal() {
		return merchant_terminal;
	}
	public void setMerchant_terminal(String merchantTerminal) {
		merchant_terminal = merchantTerminal;
	}
	public String getPay_order_fee() {
		return pay_order_fee;
	}
	public void setPay_order_fee(String payOrderFee) {
		pay_order_fee = payOrderFee;
	}
	public String getPay_type() {
		return pay_type;
	}
	public void setPay_type(String payType) {
		pay_type = payType;
	}
	public String getMd5_type() {
		return md5_type;
	}
	public void setMd5_type(String md5Type) {
		md5_type = md5Type;
	}
	public String getMerchant_fee() {
		return merchant_fee;
	}
	public void setMerchant_fee(String merchantFee) {
		merchant_fee = merchantFee;
	}
	public String getBx_company() {
		return bx_company;
	}
	public void setBx_company(String bxCompany) {
		bx_company = bxCompany;
	}
	public String getSms_channel() {
		return sms_channel;
	}
	public void setSms_channel(String smsChannel) {
		sms_channel = smsChannel;
	}
//	public String getTicket_time_limit() {
//		return ticket_time_limit;
//	}
//	public void setTicket_time_limit(String ticketTimeLimit) {
//		ticket_time_limit = ticketTimeLimit;
//	}
	public String getStop_buyTicket_time() {
		return stop_buyTicket_time;
	}
	public void setStop_buyTicket_time(String stopBuyTicketTime) {
		stop_buyTicket_time = stopBuyTicketTime;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String createTime) {
		create_time = createTime;
	}
	public String getSpare_ticket_amount() {
		return spare_ticket_amount;
	}
	public void setSpare_ticket_amount(String spareTicketAmount) {
		spare_ticket_amount = spareTicketAmount;
	}
	public String getMerchant_stop_reason() {
		return merchant_stop_reason;
	}
	public void setMerchant_stop_reason(String merchantStopReason) {
		merchant_stop_reason = merchantStopReason;
	}

	//pay_type支付方式：11、自动代扣；22、商户自行扣费
	private static Map<String,String> PAYTYPE = new LinkedHashMap<String,String>();
	public static String PAYTYPE_DAIKOU ="11";
	public static String PAYTYPE_EXT ="22";
	public static Map<String,String> getPayTypes(){
		if(PAYTYPE.isEmpty()){
			PAYTYPE.put(PAYTYPE_DAIKOU, "自动代扣");
			PAYTYPE.put(PAYTYPE_EXT, "商户自行扣费");
		}
		return PAYTYPE;
	}
	//扣费方式：19e_bx、提供保险方式；merchant_bx、商户自有保险方式（每笔收取3元）；手续费方式（0.2~~1.2元不等）
	private static Map<String,String> MARCHANTFEE = new LinkedHashMap<String,String>();
	public static String MARCHANTFEE_OUR ="19e_bx";
	public static String MARCHANTFEE_SELF ="merchant_bx";
	public static String MARCHANTFEE_02 = "0.2";
	public static String MARCHANTFEE_03 = "0.3";
	public static String MARCHANTFEE_04 = "0.4";
	public static String MARCHANTFEE_05 = "0.5";
	public static String MARCHANTFEE_06 = "0.6";
	public static String MARCHANTFEE_07 = "0.7";
	public static String MARCHANTFEE_08 = "0.8";
	public static String MARCHANTFEE_09 = "0.9";
	public static String MARCHANTFEE_10 = "1.0";
	public static String MARCHANTFEE_11 = "1.1";
	public static String MARCHANTFEE_12 = "1.2";
	public static Map<String,String> getMerchantFees(){
		if(MARCHANTFEE.isEmpty()){
			MARCHANTFEE.put(MARCHANTFEE_OUR, "我们保险");
			MARCHANTFEE.put(MARCHANTFEE_SELF, "自用保险");
			MARCHANTFEE.put(MARCHANTFEE_02, "0.2元");
			MARCHANTFEE.put(MARCHANTFEE_03, "0.3元");
			MARCHANTFEE.put(MARCHANTFEE_04, "0.4元");
			MARCHANTFEE.put(MARCHANTFEE_05, "0.5元");
			MARCHANTFEE.put(MARCHANTFEE_06, "0.6元");
			MARCHANTFEE.put(MARCHANTFEE_07, "0.7元");
			MARCHANTFEE.put(MARCHANTFEE_08, "0.8元");
			MARCHANTFEE.put(MARCHANTFEE_09, "0.9元");
			MARCHANTFEE.put(MARCHANTFEE_10, "1.0元");
			MARCHANTFEE.put(MARCHANTFEE_11, "1.1元");
			MARCHANTFEE.put(MARCHANTFEE_12, "1.2元");
		}
		return MARCHANTFEE;
	}
	//bx_company保险单位：11、快保；22、合众
	private static Map<String,String> BXCOMPANY = new LinkedHashMap<String,String>();
	public static String BXCOMPANY_KUAIBAO ="1";
	public static String BXCOMPANY_HEZHONG ="2";
	public static Map<String,String> getBxCompanys(){
		if(BXCOMPANY.isEmpty()){
			BXCOMPANY.put(BXCOMPANY_KUAIBAO, "快保");
			BXCOMPANY.put(BXCOMPANY_HEZHONG, "合众");
		}
		return BXCOMPANY;
	}
	//sms_channel短信渠道：00、19e；11、鼎鑫移动；22、企信通；88、商户自发短信
	private static Map<String,String> SMNCHANNEL = new LinkedHashMap<String,String>();
	public static String SMNCHANNEL_19E ="00";
	public static String SMNCHANNEL_DING ="11";
	public static String SMNCHANNEL_QI ="22";
	public static String SMNCHANNEL_ZIFA ="88";
	public static Map<String,String> getSmnChannels(){
		if(SMNCHANNEL.isEmpty()){
			SMNCHANNEL.put(SMNCHANNEL_19E, "19e");
			SMNCHANNEL.put(SMNCHANNEL_DING, "鼎鑫移动");
			SMNCHANNEL.put(SMNCHANNEL_QI, "企信通");
			SMNCHANNEL.put(SMNCHANNEL_ZIFA, "商户自发短信");
		}
		return SMNCHANNEL;
	}
	//ticket_time_limit 默认乘车日期：00、当天；11、1天；22、2天；33、3天
	/**
	private static Map<String,String> TICKETTIMELIMIT = new LinkedHashMap<String,String>();
	public static String TICKETTIMELIMIT_00 ="00";
	public static String TICKETTIMELIMIT_11 ="11";
	public static String TICKETTIMELIMIT_22 ="22";
	public static String TICKETTIMELIMIT_33 ="33";
	public static Map<String,String> getTicketTimeLimits(){
		if(TICKETTIMELIMIT.isEmpty()){
			TICKETTIMELIMIT.put(TICKETTIMELIMIT_00, "当天");
			TICKETTIMELIMIT.put(TICKETTIMELIMIT_11, "1天");
			TICKETTIMELIMIT.put(TICKETTIMELIMIT_22, "2天");
			TICKETTIMELIMIT.put(TICKETTIMELIMIT_33, "3天");
		}
		return TICKETTIMELIMIT;
	}
	*/
	//merchant_status 商户状态：00、停用 11、启用
	private static Map<String,String> MERCHANTSTATUS = new LinkedHashMap<String,String>();
	public static String MERCHANTSTATUS_00 ="00";
	public static String MERCHANTSTATUS_11 ="11";
	public static Map<String,String> getMerchantStatuss(){
		if(MERCHANTSTATUS.isEmpty()){
			MERCHANTSTATUS.put(MERCHANTSTATUS_00, "停用");
			MERCHANTSTATUS.put(MERCHANTSTATUS_11, "启用");
		}
		return MERCHANTSTATUS;
	}
}
