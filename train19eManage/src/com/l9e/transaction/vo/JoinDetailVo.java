package com.l9e.transaction.vo;

import java.util.LinkedHashMap;
import java.util.Map;

public class JoinDetailVo {
	private String user_id ;
	private String order_id;
	private String jp_id; 
	private String product_id ;
	private String name;
	private String user_name;
	private String create_time;
	private String pay_money;
	private String dealer_id ;
	private String sumNow;
	private String sumPre;
	private String order_status;
	private String sale_price ;
	
	
	private static String BEFORE_ODD ="00" ;//预下单
	private static String PAY_SUCCEED ="11"; //支付成功
	private static String PAY_BAULK = "22" ;//支付失败
	private static String WAIT_REFUNDMENT = "33" ;//等待退款
	private static String REFUNDMENT_SUCCEED = "44" ;//退款成功
	private static String REFUNDMENT_BAULK = "55" ; //退款失败
	
	private static Map<String,String> ORDER_STATU = new LinkedHashMap<String,String>() ;
	
	public static Map<String,String>getStatus (){
		if(ORDER_STATU.isEmpty()){
			ORDER_STATU.put(BEFORE_ODD, "预下单");
			ORDER_STATU.put(PAY_SUCCEED, "支付成功") ;
			ORDER_STATU.put(PAY_BAULK, "支付失败") ;
			ORDER_STATU.put(WAIT_REFUNDMENT, "等待退款") ;
			ORDER_STATU.put(REFUNDMENT_SUCCEED, "退款成功") ;
			ORDER_STATU.put(REFUNDMENT_BAULK, "退款失败") ;
		}
		return ORDER_STATU ;
	}
	
	private static String YUAN_JIAN = "0" ; //  元/件
	private static String YUAN_MOUTH = "1" ; // 元/月
	private static String YUAN_YEAR = "2" ;  // 元/年
	
	private static Map<String,String> TYPE = new LinkedHashMap<String,String>() ;
	
	public static Map<String,String>getTypes(){
		if(TYPE.isEmpty()){
			TYPE.put(YUAN_JIAN, "元/件") ;
			TYPE.put(YUAN_MOUTH, "元/月") ;
			TYPE.put(YUAN_YEAR, "元/年") ;
		}
		return TYPE ;
	}
	
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getJp_id() {
		return jp_id;
	}
	public void setJp_id(String jp_id) {
		this.jp_id = jp_id;
	}
	public String getProduct_id() {
		return product_id;
	}
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOrder_status() {
		return order_status;
	}
	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}
	public String getSumNow() {
		return sumNow;
	}
	public void setSumNow(String sumNow) {
		this.sumNow = sumNow;
	}
	public String getSumPre() {
		return sumPre;
	}
	public void setSumPre(String sumPre) {
		this.sumPre = sumPre;
	}
	public String getDealer_id() {
		return dealer_id;
	}
	public void setDealer_id(String dealer_id) {
		this.dealer_id = dealer_id;
	}

	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getPay_money() {
		return pay_money;
	}
	public void setPay_money(String pay_money) {
		this.pay_money = pay_money;
	}

	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	

	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}


	public static String getPAY_SUCCEED() {
		return PAY_SUCCEED;
	}


	public static void setPAY_SUCCEED(String pay_succeed) {
		PAY_SUCCEED = pay_succeed;
	}


	public static String getPAY_BAULK() {
		return PAY_BAULK;
	}


	public static void setPAY_BAULK(String pay_baulk) {
		PAY_BAULK = pay_baulk;
	}


	public static String getWAIT_REFUNDMENT() {
		return WAIT_REFUNDMENT;
	}


	public static void setWAIT_REFUNDMENT(String wait_refundment) {
		WAIT_REFUNDMENT = wait_refundment;
	}


	public static String getREFUNDMENT_SUCCEED() {
		return REFUNDMENT_SUCCEED;
	}


	public static void setREFUNDMENT_SUCCEED(String refundment_succeed) {
		REFUNDMENT_SUCCEED = refundment_succeed;
	}


	public static String getREFUNDMENT_BAULK() {
		return REFUNDMENT_BAULK;
	}


	public static void setREFUNDMENT_BAULK(String refundment_baulk) {
		REFUNDMENT_BAULK = refundment_baulk;
	}


	public static Map<String, String> getORDER_STATU() {
		return ORDER_STATU;
	}


	public static void setORDER_STATU(Map<String, String> order_statu) {
		ORDER_STATU = order_statu;
	}

	public static String getYUAN_JIAN() {
		return YUAN_JIAN;
	}

	public static void setYUAN_JIAN(String yuan_jian) {
		YUAN_JIAN = yuan_jian;
	}

	public static String getYUAN_MOUTH() {
		return YUAN_MOUTH;
	}

	public static void setYUAN_MOUTH(String yuan_mouth) {
		YUAN_MOUTH = yuan_mouth;
	}

	public static String getYUAN_YEAR() {
		return YUAN_YEAR;
	}

	public static void setYUAN_YEAR(String yuan_year) {
		YUAN_YEAR = yuan_year;
	}

	public static Map<String, String> getTYPE() {
		return TYPE;
	}

	public static void setTYPE(Map<String, String> type) {
		TYPE = type;
	}

	public String getSale_price() {
		return sale_price;
	}

	public void setSale_price(String sale_price) {
		this.sale_price = sale_price;
	}

	public static String getBEFORE_ODD() {
		return BEFORE_ODD;
	}

	public static void setBEFORE_ODD(String before_odd) {
		BEFORE_ODD = before_odd;
	}
}
