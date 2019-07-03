package com.l9e.transaction.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.annotation.JSONField;
import com.l9e.util.DateUtil;

/**
 * 抢票订单信息
 * 
 * @author yangwei01
 */
public class RobTicket_OI implements Serializable {

	private static final long serialVersionUID = 6581673261338530429L;
	private String orderId;

	private String ctripOrderId;
	
	@JSONField(name="fromToZh")
	private String fromtoZh;

	private BigDecimal payMoney;

	private BigDecimal buyMoney;

	private String orderStatus;

	@JSONField(format = DateUtil.DATE_FMT3)
	private Date createTime;
	@JSONField(format = DateUtil.DATE_FMT3)
	private Date payTime;
	@JSONField(format = DateUtil.DATE_FMT3)
	private Date outTicketTime;

	private Integer outTicketType;

	private String optRen;

	private String outTicketBillno;

	private String trainNo;

	private String trainNoAccept;

	private String fromCity;

	private String toCity;
	@JSONField(format = DateUtil.DATE_FMT3)
	private Date fromTime;
	@JSONField(format = DateUtil.DATE_FMT3)
	private Date toTime;

	private String travelTime;

	private String seatType;

	private String seatTypeAccept;

	private String accountId;

	private Integer workerId;

	private String outTicketAccount;

	private String bankPaySeq;

	private String errorInfo;
	@JSONField(format = DateUtil.DATE_FMT3)
	private Date optionTime;

	private String channel;

	private String level;

	private String payType;

	private String isPay;

	private String returnOptlog;

	private String proBak2;
	@JSONField(format = DateUtil.DATE_FMT3)
	private Date payLimitTime;

	private String manualOrder;

	private String waitForOrder;

	private Byte deviceType;
	@JSONField(name = "fromCity3c")
	private String from3c;
	@JSONField(name = "toCity3c")
	private String to3c;

	private Integer accountFromWay;

	@JSONField(name = "payMoneyExt")
	private BigDecimal payMoneyExtSum;

	@JSONField(name = "buyMoneyExt")
	private BigDecimal buyMoneyExtSum;

	private String finalTrainNo;

	private String paySerialNumber;

	private String finalSeatType;

	private Integer leakCutOfftime; // 捡漏截止时间 携程 小时标准

	private String contactPerson;

	private String contactPhone;
	
	private String leakCutStr; // 捡漏截止时间
	
	private Date cancelTime; // 取消时间
	
	private Date refundTime; // 退款时间
	
	public String getLeakCutStr() {
		return leakCutStr;
	}

	public void setLeakCutStr(String leakCutStr) {
		this.leakCutStr = leakCutStr;
	}

	public Date getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(Date cancelTime) {
		this.cancelTime = cancelTime;
	}

	public Date getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(Date refundTime) {
		this.refundTime = refundTime;
	}

	private List<RobTicket_CP> orderCps;

	public List<RobTicket_CP> getOrderCps() {
		return orderCps;
	}

	public void setOrderCps(List<RobTicket_CP> orderCps) {
		this.orderCps = orderCps;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId == null ? null : orderId.trim();
	}

	public String getCtripOrderId() {
		return ctripOrderId;
	}

	public void setCtripOrderId(String ctripOrderId) {
		this.ctripOrderId = ctripOrderId == null ? null : ctripOrderId.trim();
	}

	public String getFromtoZh() {
		return fromtoZh;
	}

	public void setFromtoZh(String fromtoZh) {
		this.fromtoZh = fromtoZh == null ? null : fromtoZh.trim();
	}

	public BigDecimal getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(BigDecimal payMoney) {
		this.payMoney = payMoney;
	}

	public BigDecimal getBuyMoney() {
		return buyMoney;
	}

	public void setBuyMoney(BigDecimal buyMoney) {
		this.buyMoney = buyMoney;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus == null ? null : orderStatus.trim();
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public Date getOutTicketTime() {
		return outTicketTime;
	}

	public void setOutTicketTime(Date outTicketTime) {
		this.outTicketTime = outTicketTime;
	}

	public Integer getOutTicketType() {
		return outTicketType;
	}

	public void setOutTicketType(Integer outTicketType) {
		this.outTicketType = outTicketType;
	}

	public String getOptRen() {
		return optRen;
	}

	public void setOptRen(String optRen) {
		this.optRen = optRen == null ? null : optRen.trim();
	}

	public String getOutTicketBillno() {
		return outTicketBillno;
	}

	public void setOutTicketBillno(String outTicketBillno) {
		this.outTicketBillno = outTicketBillno == null ? null : outTicketBillno
				.trim();
	}

	public String getTrainNo() {
		return trainNo;
	}

	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo == null ? null : trainNo.trim();
	}

	public String getTrainNoAccept() {
		return trainNoAccept;
	}

	public void setTrainNoAccept(String trainNoAccept) {
		this.trainNoAccept = trainNoAccept == null ? null : trainNoAccept
				.trim();
	}

	public String getFromCity() {
		return fromCity;
	}

	public void setFromCity(String fromCity) {
		this.fromCity = fromCity == null ? null : fromCity.trim();
	}

	public String getToCity() {
		return toCity;
	}

	public void setToCity(String toCity) {
		this.toCity = toCity == null ? null : toCity.trim();
	}

	public Date getFromTime() {
		return fromTime;
	}

	public void setFromTime(Date fromTime) {
		this.fromTime = fromTime;
	}

	public Date getToTime() {
		return toTime;
	}

	public void setToTime(Date toTime) {
		this.toTime = toTime;
	}

	public String getTravelTime() {
		return travelTime;
	}

	public void setTravelTime(String travelTime) {
		this.travelTime = travelTime;
	}

	public String getSeatType() {
		return seatType;
	}

	public void setSeatType(String seatType) {
		this.seatType = seatType == null ? null : seatType.trim();
	}

	public String getSeatTypeAccept() {
		return seatTypeAccept;
	}

	public void setSeatTypeAccept(String seatTypeAccept) {
		this.seatTypeAccept = seatTypeAccept == null ? null : seatTypeAccept
				.trim();
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public Integer getWorkerId() {
		return workerId;
	}

	public void setWorkerId(Integer workerId) {
		this.workerId = workerId;
	}

	public String getOutTicketAccount() {
		return outTicketAccount;
	}

	public void setOutTicketAccount(String outTicketAccount) {
		this.outTicketAccount = outTicketAccount == null ? null
				: outTicketAccount.trim();
	}

	public String getBankPaySeq() {
		return bankPaySeq;
	}

	public void setBankPaySeq(String bankPaySeq) {
		this.bankPaySeq = bankPaySeq == null ? null : bankPaySeq.trim();
	}

	public String getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo == null ? null : errorInfo.trim();
	}

	public Date getOptionTime() {
		return optionTime;
	}

	public void setOptionTime(Date optionTime) {
		this.optionTime = optionTime;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel == null ? null : channel.trim();
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level == null ? null : level.trim();
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType == null ? null : payType.trim();
	}

	public String getIsPay() {
		return isPay;
	}

	public void setIsPay(String isPay) {
		this.isPay = isPay == null ? null : isPay.trim();
	}

	public String getReturnOptlog() {
		return returnOptlog;
	}

	public void setReturnOptlog(String returnOptlog) {
		this.returnOptlog = returnOptlog == null ? null : returnOptlog.trim();
	}

	public String getProBak2() {
		return proBak2;
	}

	public void setProBak2(String proBak2) {
		this.proBak2 = proBak2 == null ? null : proBak2.trim();
	}

	public Date getPayLimitTime() {
		return payLimitTime;
	}

	public void setPayLimitTime(Date payLimitTime) {
		this.payLimitTime = payLimitTime;
	}

	public String getManualOrder() {
		return manualOrder;
	}

	public void setManualOrder(String manualOrder) {
		this.manualOrder = manualOrder == null ? null : manualOrder.trim();
	}

	public String getWaitForOrder() {
		return waitForOrder;
	}

	public void setWaitForOrder(String waitForOrder) {
		this.waitForOrder = waitForOrder == null ? null : waitForOrder.trim();
	}

	public Byte getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(Byte deviceType) {
		this.deviceType = deviceType;
	}

	public String getFrom3c() {
		return from3c;
	}

	public void setFrom3c(String from3c) {
		this.from3c = from3c == null ? null : from3c.trim();
	}

	public String getTo3c() {
		return to3c;
	}

	public void setTo3c(String to3c) {
		this.to3c = to3c == null ? null : to3c.trim();
	}

	public Integer getAccountFromWay() {
		return accountFromWay;
	}

	public void setAccountFromWay(Integer accountFromWay) {
		this.accountFromWay = accountFromWay;
	}

	public BigDecimal getPayMoneyExtSum() {
		return payMoneyExtSum;
	}

	public void setPayMoneyExtSum(BigDecimal payMoneyExtSum) {
		this.payMoneyExtSum = payMoneyExtSum;
	}

	public BigDecimal getBuyMoneyExtSum() {
		return buyMoneyExtSum;
	}

	public void setBuyMoneyExtSum(BigDecimal buyMoneyExtSum) {
		this.buyMoneyExtSum = buyMoneyExtSum;
	}

	public String getFinalTrainNo() {
		return finalTrainNo;
	}

	public void setFinalTrainNo(String finalTrainNo) {
		this.finalTrainNo = finalTrainNo == null ? null : finalTrainNo.trim();
	}

	public String getPaySerialNumber() {
		return paySerialNumber;
	}

	public void setPaySerialNumber(String paySerialNumber) {
		this.paySerialNumber = paySerialNumber == null ? null : paySerialNumber
				.trim();
	}

	public String getFinalSeatType() {
		return finalSeatType;
	}

	public void setFinalSeatType(String finalSeatType) {
		this.finalSeatType = finalSeatType == null ? null : finalSeatType
				.trim();
	}

	public Integer getLeakCutOfftime() {
		return leakCutOfftime;
	}

	public void setLeakCutOfftime(Integer leakCutOfftime) {
		this.leakCutOfftime = leakCutOfftime;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson == null ? null : contactPerson
				.trim();
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone == null ? null : contactPhone.trim();
	}


}