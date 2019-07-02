package com.l9e.train.po;

public class OrderBill {

	//00：等待机器改签 01：重新机器改签 02：开始机器改签 03：机器改签失败 
	//04：等待机器退票 05：重新机器退票06：开始机器退票 07：机器退票失败 11：退票完成 22：拒绝退票 33：审核退款
	public static String WAIT_ROBOT_ALTER= "00";//等待机器改签
	public static String REPEAT_ROBOT_ALTER= "01";//重新机器改签
	public static String BEGIN_ROBOT_ALTER= "02";//开始机器改签
	public static String FAIL_ROBOT_ALTER= "03";//机器改签失败 
	public static String WAIT_ROBOT_REFUND= "04";//等待机器退票
	public static String REPEAT_ROBOT_REFUND= "05";//重新机器退票
	public static String BEGIN_ROBOT_REFUND= "06";//开始机器退票 
	public static String FAIL_ROBOT_REFUND= "07";//机器退票失败
	public static String FINISH_REFUND= "11";//退票完成
	public static String REFUSE_REFUND= "22";//拒绝退票
	public static String VERIFY_REFUND= "33";//审核退款
	
	private String refundSeq;
	private String orderId;
	private String cpId;
	private String buyMoney;
	private String refundMoney;
	private String refund12306Money;
	private String alterDiffMoney;
	private String alterBuyMoney;
	private String alterTravelTime;
	private String alterSeatType;
	private String alterTrainBox;
	private String alterSeatNo;
	private String notifyUrl;
	private String createTime;
	private String notifyType;
	private String refund12306Seq;
	private String orderStatus;
	private String ourRemark;
	private String refuseReason;
	
	public String getRefundMoney() {
		return refundMoney;
	}
	public void setRefundMoney(String refundMoney) {
		this.refundMoney = refundMoney;
	}
	public String getAlterDiffMoney() {
		return alterDiffMoney;
	}
	public void setAlterDiffMoney(String alterDiffMoney) {
		this.alterDiffMoney = alterDiffMoney;
	}
	public String getRefundSeq() {
		return refundSeq;
	}
	public void setRefundSeq(String refundSeq) {
		this.refundSeq = refundSeq;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getOurRemark() {
		return ourRemark;
	}
	public void setOurRemark(String ourRemark) {
		this.ourRemark = ourRemark;
	}
	public String getRefuseReason() {
		return refuseReason;
	}
	public void setRefuseReason(String refuseReason) {
		this.refuseReason = refuseReason;
	}
	public String getRefund12306Seq() {
		return refund12306Seq;
	}
	public void setRefund12306Seq(String refund12306Seq) {
		this.refund12306Seq = refund12306Seq;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getCpId() {
		return cpId;
	}
	public void setCpId(String cpId) {
		this.cpId = cpId;
	}
	public String getBuyMoney() {
		return buyMoney;
	}
	public void setBuyMoney(String buyMoney) {
		this.buyMoney = buyMoney;
	}
	public String getRefund12306Money() {
		return refund12306Money;
	}
	public void setRefund12306Money(String refund12306Money) {
		this.refund12306Money = refund12306Money;
	}
	public String getAlterBuyMoney() {
		return alterBuyMoney;
	}
	public void setAlterBuyMoney(String alterBuyMoney) {
		this.alterBuyMoney = alterBuyMoney;
	}
	public String getAlterTravelTime() {
		return alterTravelTime;
	}
	public void setAlterTravelTime(String alterTravelTime) {
		this.alterTravelTime = alterTravelTime;
	}
	public String getAlterSeatType() {
		return alterSeatType;
	}
	public void setAlterSeatType(String alterSeatType) {
		this.alterSeatType = alterSeatType;
	}
	public String getAlterTrainBox() {
		return alterTrainBox;
	}
	public void setAlterTrainBox(String alterTrainBox) {
		this.alterTrainBox = alterTrainBox;
	}
	public String getAlterSeatNo() {
		return alterSeatNo;
	}
	public void setAlterSeatNo(String alterSeatNo) {
		this.alterSeatNo = alterSeatNo;
	}
	public String getNotifyUrl() {
		return notifyUrl;
	}
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getNotifyType() {
		return notifyType;
	}
	public void setNotifyType(String notifyType) {
		this.notifyType = notifyType;
	}
	
}
