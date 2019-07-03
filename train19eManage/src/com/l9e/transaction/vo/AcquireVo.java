package com.l9e.transaction.vo;

import java.util.LinkedHashMap;
import java.util.Map;

public class AcquireVo {
	
	
	private String order_id;
	private String order_status;
	private String out_ticket_account;
	private String out_ticket_billno;
	private String bank_pay_seq;
	private String buy_money;
	private String account_id;
	private String opt_ren;
	private String error_info;
	private String acc_username;
	private String pay_type;
	
	private String ps_fail_reason;
	
	public static String ORDER_START = "00";
	public static String ORDER_RESEND = "01";
	public static String ORDER_PAIDUI = "15";//12306排队
	public static String ORDER_ING = "11";
	public static String ORDER_FAILURE = "22"; 
	public static String ORDER_SUCCESS = "33";
	public static String ORDER_MANUAL = "44";
	
	public static String PAY_START = "55";
	public static String PAY_AGAIN = "56";
	public static String PAY_ING="66";
	
	public static String PAY_SUCCESS= "88";	
	public static String BILL_SUCCESS= "99";
	public static String BILL_FAILURE= "10";
	
	public static String PAY_PERSON = "61";//人工支付
	public static String FINDING= "81";//正在查询
	public static String FIND_MANUAL= "82";//查询人工
	
	public static String WAIT_PAY="45";//等待支付
	public static String CHEXIAO="51";//正在撤销
	public static String CHEXIAOFAIL="52";//撤销失败
	
	public static String START_CANCLE = "85";//开始取消
	public static String CANCELING = "83";//正在取消
	public static String PAY_FAILURE= "77";//取消失败
	
	public static String WAITBUDAN="AA";//等待补单
	
	public static String PAIDUI="05";//排队处理
	
	public static String PAY_PAIDUI="46";//排队处理
	
	public static Integer ORDER_Robot = new Integer(1);//订购机器人
	public static Integer ORDER_Manual = new Integer(2);//订购人工
	
	public static Integer PAY_Robot = new Integer(3);//订购机器人
	public static Integer PAY_Manual = new Integer(4);//订购人工
	
	public static String CHINA_BANK = "ZhongGuoYinHang";//中国银行
	public static String BUILD_BANK = "JianSheYinHang";//建设银行
	
	/*
	 * 0、商务座 1、特等座 2、一等座 3、二等座 4、高级软卧 5、软卧 6、硬卧 7、 软座 8、硬座 9、无座 10、其他 
	 * 11、一人软包 12、观光座  20动卧  22高级动卧  23一等软座  24二等软座

	 */
	public static String SEAT_commerce ="0";
	public static String SEAT_special ="1";
	public static String SEAT_one ="2";
	public static String SEAT_two ="3";
	public static String SOFT_VIP ="4";
	public static String SOFT_VIP_berth_top ="41";
	public static String SOFT_VIP_berth_down ="42";
	public static String SOFT__berth ="5";
	public static String SOFT__berth_top ="51";
	public static String SOFT__berth_down ="52";
	public static String RIGIDITY_berth ="6";
	public static String RIGIDITY_berth_top ="61";
	public static String RIGIDITY_berth_middle="62";
	public static String RIGIDITY_berth_down ="63";
	public static String SEAT_soft ="7";
	public static String SEAT_rigidity ="8";
	public static String SEAT_no ="9";
	public static String OTHER_seat ="10";
	
	public static String SOFT_ONE="11";
	public static String SEAT_LOOK ="12";
	public static String D_BERTH ="20";//动卧
	public static String D_BERTH_VIP ="22";
	public static String SEAT_SOFT_ONE ="23";
	public static String SEAT_SOFT_TWO ="24";
	
	private static Map<String, String> ACQUIRETATUS = new LinkedHashMap<String, String>();
	
	private static Map<String, String> ACQUIREPAYTYPE = new LinkedHashMap<String, String>();
	
	private static Map<Integer, String> WORKERTYPE = new LinkedHashMap<Integer, String>();
	
	private static Map<String,String> BANK = new LinkedHashMap<String,String>();
	
	private static Map<String,String> SEAT_TYPE = new LinkedHashMap<String,String>();
	
	
	
	public static Map<String,String> getSEAT_TYPES(){
		if(SEAT_TYPE.isEmpty()){
			SEAT_TYPE.put(SEAT_commerce, "商务座");
			SEAT_TYPE.put(SEAT_special, "特等座");
			SEAT_TYPE.put(SEAT_one, "一等座");
			SEAT_TYPE.put(SEAT_two, "二等座");
			SEAT_TYPE.put(SOFT_VIP, "高级软卧");
			SEAT_TYPE.put(SOFT_VIP_berth_top, "高级软卧上");
			SEAT_TYPE.put(SOFT_VIP_berth_down, "高级软卧下");
			SEAT_TYPE.put(SOFT__berth, "软卧");
			SEAT_TYPE.put(SOFT__berth_top, "软卧上");
			SEAT_TYPE.put(SOFT__berth_down, "软卧下");
			SEAT_TYPE.put(RIGIDITY_berth, "硬卧");
			SEAT_TYPE.put(RIGIDITY_berth_top, "硬卧上");
			SEAT_TYPE.put(RIGIDITY_berth_middle, "硬卧中");
			SEAT_TYPE.put(RIGIDITY_berth_down, "硬卧下");
			SEAT_TYPE.put(SEAT_soft, "软座");
			SEAT_TYPE.put(SEAT_rigidity, "硬座");
			SEAT_TYPE.put(SEAT_no, "无座");
			SEAT_TYPE.put(OTHER_seat, "其他");

			SEAT_TYPE.put(SOFT_ONE, "一人软包");
			SEAT_TYPE.put(SEAT_LOOK, "观光座");
			SEAT_TYPE.put(D_BERTH, "动卧");
			SEAT_TYPE.put(D_BERTH_VIP, "高级动卧");
			SEAT_TYPE.put(SEAT_SOFT_ONE, "一等软座");
			SEAT_TYPE.put(SEAT_SOFT_TWO, "二等软座");
		}
		return SEAT_TYPE;
	}
	
	public static Map<String, String> getAcquireStatus(){
		//00、开始出票 11、正在预定 22、预定失败 33、预定成功 44、预定人工 55、开始支付 56、重新支付 66、正在支付 77、支付失败 88、支付成功
		if(ACQUIRETATUS.isEmpty()){
			ACQUIRETATUS.put(ORDER_START, "开始出票 ");
			ACQUIRETATUS.put(ORDER_RESEND, "预定重发 ");
			ACQUIRETATUS.put(ORDER_PAIDUI, "12306排队");
			ACQUIRETATUS.put(ORDER_ING, "正在预定");
			ACQUIRETATUS.put(ORDER_FAILURE, "预定失败  ");
			ACQUIRETATUS.put(ORDER_SUCCESS, "预定成功 ");
			ACQUIRETATUS.put(ORDER_MANUAL, "人工预订 ");
			ACQUIRETATUS.put(PAY_START, "开始支付 ");
			ACQUIRETATUS.put(PAY_AGAIN, "重新支付 ");
			ACQUIRETATUS.put(PAY_ING, "正在支付 ");
			ACQUIRETATUS.put(PAY_FAILURE, "取消失败");
			ACQUIRETATUS.put(PAY_PERSON, "人工支付");
			ACQUIRETATUS.put(PAY_SUCCESS, "支付成功 ");
			ACQUIRETATUS.put(FINDING, "正在查询 ");
			ACQUIRETATUS.put(FIND_MANUAL, "人工查询");
			ACQUIRETATUS.put(CANCELING, "正在取消");
			ACQUIRETATUS.put(BILL_SUCCESS, "订单成功 ");
			ACQUIRETATUS.put(BILL_FAILURE, "订单失败 ");
			ACQUIRETATUS.put(START_CANCLE, "开始取消 ");
			ACQUIRETATUS.put(WAIT_PAY, "等待支付");
			ACQUIRETATUS.put(CHEXIAO, "正在撤销");
			ACQUIRETATUS.put(CHEXIAOFAIL, "撤销失败");
			ACQUIRETATUS.put(PAIDUI, "排队处理");
			ACQUIRETATUS.put(PAY_PAIDUI, "支付排队");
			ACQUIRETATUS.put(WAITBUDAN, "等待补单");
		}
		return ACQUIRETATUS;
	}
	
	public static String ORDER_MANUALING = "MM";
	private static Map<String, String> MANUALSTATUS = new LinkedHashMap<String, String>();
	public static Map<String, String> getManualStatus(){
		if(MANUALSTATUS.isEmpty()){
			MANUALSTATUS.put(ORDER_MANUALING, "人工出票 ");
			MANUALSTATUS.put(PAY_SUCCESS, "正在查询 ");
			MANUALSTATUS.put(FIND_MANUAL, "人工查询");
			MANUALSTATUS.put(BILL_SUCCESS, "订单成功 ");
			MANUALSTATUS.put(BILL_FAILURE, "订单失败 ");

		}
		return MANUALSTATUS;
	}
	
//	public static String JIQIPAY = "0";
	public static String SHOUDONGPAY = "1";
	public static Map<String, String> getAcquirePayType(){
		//0、机器支付  1、手动支付
		if(ACQUIREPAYTYPE.isEmpty()){
		//	ACQUIREPAYTYPE.put(JIQIPAY, "机器支付 ");
			ACQUIREPAYTYPE.put(SHOUDONGPAY, "手动支付 ");
			
			
		}
		return ACQUIREPAYTYPE;
	}
	
	public static Map<String, String> getAcquireStatusKefu(){
		//00、开始出票 11、正在预定 22、预定失败 33、预定成功 44、预定人工 55、开始支付 66、正在支付 77、支付失败 88、支付成功
		if(ACQUIRETATUS.isEmpty()){
			ACQUIRETATUS.put(ORDER_MANUAL, "人工预订 ");
			ACQUIRETATUS.put(PAY_PERSON, "人工支付");
			ACQUIRETATUS.put(FIND_MANUAL, "人工查询");
		}
		return ACQUIRETATUS;
	}
	
	
	public static Map<Integer, String> getWorkerType(){
		//00、开始出票 11、正在预定 22、预定失败 33、预定成功 44、预定人工 55、开始支付 66、正在支付 77、支付失败 88、支付成功
		
		
		if(WORKERTYPE.isEmpty()){
			WORKERTYPE.put(ORDER_Robot, "机器人订购 ");
			WORKERTYPE.put(ORDER_Manual, "人工订购");
			WORKERTYPE.put(PAY_Robot, "机器人支付  ");
			WORKERTYPE.put(PAY_Manual, "人工支付 ");
		}
		
		return WORKERTYPE;
	}
	
	public static Map<String,String> getBank(){
		//ZhongGuoYinHang 中国银行   JianSheYinHang 建设银行
		if(BANK.isEmpty()){
			BANK.put("CHINA_BANK", "中国银行");
			BANK.put("BUILD_BANK", "建设银行");
		}
		return BANK;
	}


	public String getAccount_id() {
		return account_id;
	}


	public void setAccount_id(String account_id) {
		this.account_id = account_id;
	}


	public String getOrder_id() {
		return order_id;
	}


	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}


	public String getOrder_status() {
		return order_status;
	}


	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}


	public String getOut_ticket_account() {
		return out_ticket_account;
	}


	public void setOut_ticket_account(String out_ticket_account) {
		this.out_ticket_account = out_ticket_account;
	}


	public String getOut_ticket_billno() {
		return out_ticket_billno;
	}


	public void setOut_ticket_billno(String out_ticket_billno) {
		this.out_ticket_billno = out_ticket_billno;
	}


	public String getBuy_money() {
		return buy_money;
	}


	public void setBuy_money(String buy_money) {
		this.buy_money = buy_money;
	}


	public String getOpt_ren() {
		return opt_ren;
	}


	public void setOpt_ren(String opt_ren) {
		this.opt_ren = opt_ren;
	}


	public String getBank_pay_seq() {
		return bank_pay_seq;
	}


	public void setBank_pay_seq(String bank_pay_seq) {
		this.bank_pay_seq = bank_pay_seq;
	}


	public String getError_info() {
		return error_info;
	}


	public void setError_info(String error_info) {
		this.error_info = error_info;
	}

	public String getAcc_username() {
		return acc_username;
	}

	public void setAcc_username(String acc_username) {
		this.acc_username = acc_username;
	}

	public String getPay_type() {
		return pay_type;
	}

	public void setPay_type(String payType) {
		pay_type = payType;
	}

	public String getPs_fail_reason() {
		return ps_fail_reason;
	}

	public void setPs_fail_reason(String psFailReason) {
		ps_fail_reason = psFailReason;
	}
	
	
}
