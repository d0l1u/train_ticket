package com.train.system.center.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Order
 *
 * @author taokai3
 * @date 2018/6/25
 */
public class Order {

	/**
	 * 订单号，自有 ky + yyMMddHH + 6位随机
	 */
	private String myOrderId;
	private String supplierOrderId;

	/**
	 * 订单号，分销商
	 */
	private String orderId;

	/**
	 * 订单状态：00:开始占位, 01:占位重发, 02:队列中, 11:正在占位, 44:占位人工
	 */
	private String orderStatus;

	/**
	 * 分销商支付金额
	 */
	private BigDecimal payMoney;

	/**
	 * 机器人支付总金额
	 */
	private BigDecimal totalPrice;

	/**
	 * 分配账号ID
	 */
	private Integer accountId;

	/**
	 * 账号来源：0:公司，1:乘客自有
	 */
	private String accountFromWay;

	/**
	 * 订单渠道，分销商简码
	 */
	private String channel;

	/**
	 * 分销商支付状态，00：已支付；11：未支付
	 */
	private String isPay;

	/**
	 * 12306 订单号
	 */
	private String sequence;

	private String trainCode;
	private String fromStationName;
	private String fromStationCode;
	private String toStationName;
	private String toStationCode;
	private Date departureDate;

	@JSONField(format = "yyyy-MM-dd HH:mm")
	private Date departureTime;
	private Date arrivalDate;
	@JSONField(format = "yyyy-MM-dd HH:mm")
	private Date arrivalTime;

	/**
	 * 交易关闭时间
	 */
	private Date payLimitTime;

	/**
	 * 占位完成时间
	 */
	private Date finishTime;

	private String seatType;

	/**
	 * 备选坐席
	 */
	private String extraSeatType;

	/**
	 * 选铺
	 */
	private String chooseBed;

	/**
	 * 选座
	 */
	private String chooseSeat;

	/**
	 * 错误简码
	 */
	private String errorCode;

	/**
	 * 重发次数
	 */
	private Integer resendNum;

	/**
	 * 重发类型
	 */
	private String resendType;

	/**
	 * 延迟处理
	 */
	private String delayOrder;

	/**
	 * 日志显示错误信息简码
	 */
	private String returnLog;

	/**
	 * 供货商标识。00:12306， 01:航天华有
	 */
	private String supplierType;

	/**
	 * 切换供货商标识 0:不可以切换, 1:可以切换
	 */
	private Boolean canSwitchSupplier;

	/**
	 * 操作时间
	 */
	private Date modifyTime;
	private Date payTime;

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public String getSeatType() {
		return seatType;
	}

	public void setSeatType(String seatType) {
		this.seatType = seatType;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Boolean getCanSwitchSupplier() {
		return canSwitchSupplier;
	}

	public void setCanSwitchSupplier(Boolean canSwitchSupplier) {
		this.canSwitchSupplier = canSwitchSupplier;
	}

	public String getSupplierOrderId() {
		return supplierOrderId;
	}

	public void setSupplierOrderId(String supplierOrderId) {
		this.supplierOrderId = supplierOrderId;
	}

	public String getMyOrderId() {
		return myOrderId;
	}

	public void setMyOrderId(String myOrderId) {
		this.myOrderId = myOrderId;
	}

	public String getSupplierType() {
		return supplierType;
	}

	public void setSupplierType(String supplierType) {
		this.supplierType = supplierType;
	}

	public String getResendType() {
		return resendType;
	}

	public String getReturnLog() {
		return returnLog;
	}

	public void setReturnLog(String returnLog) {
		this.returnLog = returnLog;
	}

	public void setResendType(String resendType) {
		this.resendType = resendType;
	}

	public Integer getResendNum() {
		return resendNum;
	}

	public void setResendNum(Integer resendNum) {
		this.resendNum = resendNum;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	private List<Passenger> passengers;

	private String failReason;

	public String getFailReason() {
		return failReason;
	}

	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}

	public List<Passenger> getPassengers() {
		return passengers;
	}

	public void setPassengers(List<Passenger> passengers) {
		this.passengers = passengers;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Date getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(Date departureTime) {
		this.departureTime = departureTime;
	}

	public Date getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(Date arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	public Date getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(Date arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public BigDecimal getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(BigDecimal payMoney) {
		this.payMoney = payMoney;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public String getAccountFromWay() {
		return accountFromWay;
	}

	public void setAccountFromWay(String accountFromWay) {
		this.accountFromWay = accountFromWay;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getIsPay() {
		return isPay;
	}

	public void setIsPay(String isPay) {
		this.isPay = isPay;
	}

	public String getTrainCode() {
		return trainCode;
	}

	public void setTrainCode(String trainCode) {
		this.trainCode = trainCode;
	}

	public String getFromStationName() {
		return fromStationName;
	}

	public void setFromStationName(String fromStationName) {
		this.fromStationName = fromStationName;
	}

	public String getFromStationCode() {
		return fromStationCode;
	}

	public void setFromStationCode(String fromStationCode) {
		this.fromStationCode = fromStationCode;
	}

	public String getToStationName() {
		return toStationName;
	}

	public void setToStationName(String toStationName) {
		this.toStationName = toStationName;
	}

	public String getToStationCode() {
		return toStationCode;
	}

	public void setToStationCode(String toStationCode) {
		this.toStationCode = toStationCode;
	}

	public Date getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(Date departureDate) {
		this.departureDate = departureDate;
	}

	public String getExtraSeatType() {
		return extraSeatType;
	}

	public void setExtraSeatType(String extraSeatType) {
		this.extraSeatType = extraSeatType;
	}

	public String getChooseBed() {
		return chooseBed;
	}

	public void setChooseBed(String chooseBed) {
		this.chooseBed = chooseBed;
	}

	public String getChooseSeat() {
		return chooseSeat;
	}

	public void setChooseSeat(String chooseSeat) {
		this.chooseSeat = chooseSeat;
	}

	public Date getPayLimitTime() {
		return payLimitTime;
	}

	public void setPayLimitTime(Date payLimitTime) {
		this.payLimitTime = payLimitTime;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getDelayOrder() {
		return delayOrder;
	}

	public void setDelayOrder(String delayOrder) {
		this.delayOrder = delayOrder;
	}
}
