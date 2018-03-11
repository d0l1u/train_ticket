package com.l9e.transaction.vo;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
public class QunarRefundVo {

	//拒绝退款原因
	public static final String TICKETOUT = "1";//已取票
	public static final String TIMEOUT = "2";//已过时间
	public static final String PHONECALL = "3"; //电话取消
	public static final String FENGHAO = "4"; //账号被封  
	//public static final String OTHER = "4";//其他
	public static final String MONEYFAIL = "6"; //退款金额有损失	高诚信用户极速退款订单退款手续费有误	 
	
	//通知状态
	public static final String NOTIFY_00 = "00";//准备通知
	public static final String NOTIFY_11 = "11";//开始通知
	public static final String NOTIFY_22 = "22";//通知完成
	public static final String NOTIFY_33 = "33";//通知失败
	
	private static Map<String, String> REFUSEREASON = new LinkedHashMap<String, String>();
	private static Map<String, String> REFUNDSTATUS = new LinkedHashMap<String, String>();
	private static Map<String, String> REFUNDORIGSTATUS = new LinkedHashMap<String, String>();
	private static Map<String, String> NOTIFYSTATUS = new LinkedHashMap<String, String>();

	public static Map<String, String> getNotifyStatus() {
		if(NOTIFYSTATUS.isEmpty()) {
			NOTIFYSTATUS.put(NOTIFY_00, "准备通知");
			NOTIFYSTATUS.put(NOTIFY_11, "开始通知");
			NOTIFYSTATUS.put(NOTIFY_22, "完成通知");
			NOTIFYSTATUS.put(NOTIFY_33, "通知失败");
		}
		return NOTIFYSTATUS;
	}
	//退款状态 
	//00：等待机器改签   01：重新机器改签   02：开始机器改签   03：机器改签失败   04：等待机器退票   05：重新机器退票
	//06：开始机器退票   07：机器退票失败   11：退票完成   22：拒绝退票   33：审核退款  44：搁置订单
	public static final String WAITREFUND = "00";//等待退票
	public static final String AGREEREFUND = "11";//同意退票
	public static final String REFUSEREFUND = "22"; //拒绝退票
	public static final String SUCCESS = "33";//退款成功
	public static final String REROBOT = "01";//重新机器改签
	public static final String STARTROBOT = "02";//开始机器改签
	public static final String ROBOTGAIQIAN = "03";//机器改签失败 
	public static final String WAITROBOT = "04";//等待机器退票
	public static final String REROBOTTUI = "05";//重新机器退票
	public static final String STARTROBOTTUI = "06";//开始机器退票
	public static final String FAILROBOTTUI = "07";//机器退票失败
	public static final String GEZHI = "44";//搁置订单
	//00：等待机器改签   01：重新机器改签   02：开始机器改签   03：机器改签失败   04：等待机器退票   05：重新机器退票
	//06：开始机器退票   07：机器退票失败   11：退票完成   22：拒绝退票   33：审核退款  44：搁置订单
	public static Map<String, String> getRefundStatus() {
		if(REFUNDSTATUS.isEmpty()) {
			REFUNDSTATUS.put(WAITREFUND, "正在改签");
			REFUNDSTATUS.put(REROBOT, "正在改签");
			REFUNDSTATUS.put(STARTROBOT, "正在改签");
			REFUNDSTATUS.put(ROBOTGAIQIAN, "人工改签");
			REFUNDSTATUS.put(WAITROBOT, "正在退票");
			REFUNDSTATUS.put(REROBOTTUI, "正在退票");
			REFUNDSTATUS.put(STARTROBOTTUI, "正在退票");
			REFUNDSTATUS.put(FAILROBOTTUI, "人工退票");
			REFUNDSTATUS.put(AGREEREFUND, "退票完成");
			REFUNDSTATUS.put(REFUSEREFUND, "拒绝退票");
			REFUNDSTATUS.put(SUCCESS, "审核退款");
			REFUNDSTATUS.put(GEZHI, "搁置订单");
		}
		return REFUNDSTATUS;
	}

	public static Map<String, String> getRefuseReason(){
		if(REFUSEREASON.isEmpty()) {
			REFUSEREASON.put(TICKETOUT, "已取票");
			REFUSEREASON.put(TIMEOUT, "已过时间");
			REFUSEREASON.put(PHONECALL, "来电取消");
			REFUSEREASON.put(FENGHAO, "账号被封");
			//REFUSEREASON.put(OTHER, "其他");
			REFUSEREASON.put(MONEYFAIL, "退款金额有损失");
		}
		return REFUSEREASON;
	}
	//退款状态：11、同意退票 22、拒绝退票 33、退款成功
	public static Map<String, String> getRefundOrigStatus() {
		if(REFUNDORIGSTATUS.isEmpty()) {
			REFUNDORIGSTATUS.put(AGREEREFUND, "同意退款");
			REFUNDORIGSTATUS.put(SUCCESS, "退款成功");
			REFUNDORIGSTATUS.put(REFUSEREFUND, "拒绝退款");
		}
		return REFUNDORIGSTATUS;
	}
	//用户退款原因：11、时间/车次错误  22、行程有变  33、其他
	public static final String REASON_TIME = "11";//时间/车次错误
	public static final String REASON_TRIP = "22";//行程有变
	public static final String REASON_QITA = "33";//其他
	private static Map<String, String> REFUNDREASON = new HashMap<String, String>();
	public static Map<String, String> getRefundOrigReason() {
		if(REFUNDREASON.isEmpty()) {
			REFUNDREASON.put(REASON_TIME, "时间/车次错误");
			REFUNDREASON.put(REASON_TRIP, "行程有变");
			REFUNDREASON.put(REASON_QITA, "其他");
		}
		return REFUNDREASON;
	}
}
