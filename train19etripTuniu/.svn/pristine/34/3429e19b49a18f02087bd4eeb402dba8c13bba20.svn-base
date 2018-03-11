package com.l9e.transaction.vo;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.l9e.util.DateUtil;

/**
 * 途牛订单
 * 
 * @author licheng
 * 
 */
public class TuniuOrder {

	/**
	 * 订单号
	 */
	private String orderId;
	/**
	 * 订单名称
	 */
	@JsonIgnore
	private String orderName;
	/**
	 * 支付金额
	 */
	@JsonIgnore
	private Double payMoney;
	/**
	 * 成本价(输出时为订单总金额)
	 */
	@JsonProperty("orderAmount")
	private Double buyMoney;
	/**
	 * 订单状态
	 */
	@JsonIgnore
	private String orderStatus;
	/**
	 * 通知状态
	 */
	@JsonIgnore
	private String notifyStatus;
	/**
	 * 创建时间
	 */
	@JsonIgnore
	private Date createTime;
	/**
	 * 出票时间
	 */
	@JsonIgnore
	private Date outTicketTime;
	
	
	/**
	 * 12306取票单号
	 */
	@JsonProperty("orderNumber")
	private String outTicketBillno;
	/**
	 * 失败原因code
	 */
	@JsonIgnore
	private String outFailReason;
	/**
	 * 车次
	 */
	@JsonProperty("cheCi")
	private String trainNo;
	/**
	 * 出发城市简码
	 */
	@JsonProperty("fromStationCode")
	private String fromCityCode;
	/**
	 * 出发城市
	 */
	@JsonProperty("fromStationName")
	private String fromCity;
	/**
	 * 到达城市简码
	 */
	@JsonProperty("toStationCode")
	private String toCityCode;
	/**
	 * 到达城市
	 */
	@JsonProperty("toStationName")
	private String toCity;
	/**
	 * 出发时间
	 */
	@JsonIgnore
	private Date fromTime;
	/**
	 * 到达时间
	 */
	@JsonIgnore
	private Date toTime;
	
	/**
	 * 支付限制时间(途牛清位时间)
	 */
	@JsonIgnore
	private Date payLimitTime;
	
	/**
	 * 乘车日期
	 */
	@JsonIgnore
	private Date travelDate;
	/**
	 * 是否无座 1、不能无座 0、可以无座
	 */
	@JsonIgnore
	private Integer hasSeat;
	
	/**
	 * 是否选座, 1:选, 0:非选
	 */
	@JsonIgnore
	private Integer isChooseSeats;
		
	/**
	 * 选座信息(选座个数要和乘客数量一致),1A1D2B2C2F
	 */
	@JsonIgnore
	private String  chooseSeats;
	
	/**
	 * 订单类型 1、先预定后支付 2、先支付后预订
	 */
	@JsonIgnore
	private String orderType;
	/**
	 * 途牛联系人
	 */
	@JsonIgnore
	private String contact;
	/**
	 * 途牛联系人电话
	 */
	@JsonIgnore
	private String phone;
	/**
	 * 途牛12306账号
	 */
	@JsonIgnore
	private String userName;
	/**
	 * 途牛12306密码
	 */
	@JsonIgnore
	private String userPassword;
	/**
	 * 保险单号
	 */
	@JsonIgnore
	private String insureCode;
	/**
	 * 订单内车票数量
	 */
	@JsonIgnore
	private Integer ticketNumber;

	/* 输入输出参数 */
	/**
	 * 合作伙伴方订单号（目前和途牛订单号一样）
	 */
	private String vendorOrderId;
	/**
	 * 订单是否成功
	 */
	private Boolean orderSuccess;
	/**
	 * 实际输出出发时间
	 */
	@JsonProperty("startTime")
	private String _fromTime;
	/**
	 * 实际输出到达时间
	 */
	@JsonProperty("arriveTime")
	private String _toTime;
	/**
	 * 实际输出乘车时间
	 */
	@JsonProperty("trainDate")
	private String _travelDate;
	
	/**
	 * 支付限制时间(途牛清位时间)
	 */
	@JsonProperty("clearTime")
	private String _payLimitTime;
	
	
	/* 关联 */
	/**
	 * 乘客
	 */
	private List<TuniuPassenger> passengers;

	/* 非业务字段 */
	/**
	 * 操作人
	 */
	@JsonIgnore
	private String optPerson;
	/**是否是重复订单*/
	private Integer isRepeat;
	
	private String optTime;

	public TuniuOrder() {
		super();
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public Double getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(Double payMoney) {
		this.payMoney = payMoney;
	}

	public Double getBuyMoney() {
		return buyMoney;
	}

	public void setBuyMoney(Double buyMoney) {
		this.buyMoney = buyMoney;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getNotifyStatus() {
		return notifyStatus;
	}

	public void setNotifyStatus(String notifyStatus) {
		this.notifyStatus = notifyStatus;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getOutTicketTime() {
		return outTicketTime;
	}

	public void setOutTicketTime(Date outTicketTime) {
		this.outTicketTime = outTicketTime;
	}

	public String getOutTicketBillno() {
		return outTicketBillno;
	}

	public void setOutTicketBillno(String outTicketBillno) {
		this.outTicketBillno = outTicketBillno;
	}

	public String getOutFailReason() {
		return outFailReason;
	}

	public void setOutFailReason(String outFailReason) {
		this.outFailReason = outFailReason;
	}

	public String getTrainNo() {
		return trainNo;
	}

	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}

	public String getFromCityCode() {
		return fromCityCode;
	}

	public void setFromCityCode(String fromCityCode) {
		this.fromCityCode = fromCityCode;
	}

	public String getFromCity() {
		return fromCity;
	}

	public void setFromCity(String fromCity) {
		this.fromCity = fromCity;
	}

	public String getToCityCode() {
		return toCityCode;
	}

	public void setToCityCode(String toCityCode) {
		this.toCityCode = toCityCode;
	}

	public String getToCity() {
		return toCity;
	}

	public void setToCity(String toCity) {
		this.toCity = toCity;
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

	public Date getTravelDate() {
		return travelDate;
	}

	public void setTravelDate(Date travelDate) {
		this.travelDate = travelDate;
	}

	public Integer getHasSeat() {
		return hasSeat;
	}

	public void setHasSeat(Integer hasSeat) {
		this.hasSeat = hasSeat;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getInsureCode() {
		return insureCode;
	}

	public void setInsureCode(String insureCode) {
		this.insureCode = insureCode;
	}

	public Integer getTicketNumber() {
		return ticketNumber;
	}

	public void setTicketNumber(Integer ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public String getVendorOrderId() {
		return vendorOrderId;
	}

	public void setVendorOrderId(String vendorOrderId) {
		this.vendorOrderId = vendorOrderId;
	}

	public Boolean getOrderSuccess() {
		return orderSuccess;
	}

	public void setOrderSuccess(Boolean orderSuccess) {
		this.orderSuccess = orderSuccess;
	}

	public List<TuniuPassenger> getPassengers() {
		return passengers;
	}

	public void setPassengers(List<TuniuPassenger> passengers) {
		this.passengers = passengers;
	}

	public String getOptPerson() {
		return optPerson;
	}

	public void setOptPerson(String optPerson) {
		this.optPerson = optPerson;
	}

	public String get_fromTime() {
		if(fromTime == null) 
			return "";
		_fromTime = DateUtil.dateToString(fromTime, "HH:mm:ss");
		return _fromTime;
	}

	public void set_fromTime(String fromTime) {
		_fromTime = fromTime;
	}

	public String get_toTime() {
		if(toTime == null) 
			return "";
		_toTime = DateUtil.dateToString(toTime, "HH:mm:ss");
		return _toTime;
	}

	public void set_toTime(String toTime) {
		_toTime = toTime;
	}

	public String get_travelDate() {
		if(travelDate == null)
			return "";
		_travelDate = DateUtil.dateToString(travelDate, DateUtil.DATE_FMT1);
		return _travelDate;
	}

	public void set_travelDate(String travelDate) {
		_travelDate = travelDate;
	}

	public Integer getIsRepeat() {
		return isRepeat;
	}

	public void setIsRepeat(Integer isRepeat) {
		this.isRepeat = isRepeat;
	}

	public String getOptTime() {
		return optTime;
	}

	public void setOptTime(String optTime) {
		this.optTime = optTime;
	}

	public Integer getIsChooseSeats() {
		return isChooseSeats;
	}

	public void setIsChooseSeats(Integer isChooseSeats) {
		this.isChooseSeats = isChooseSeats;
	}

	public String getChooseSeats() {
		return chooseSeats;
	}

	public void setChooseSeats(String chooseSeats) {
		this.chooseSeats = chooseSeats;
	}

	public Date getPayLimitTime() {
		return payLimitTime;
	}

	public void setPayLimitTime(Date payLimitTime) {
		this.payLimitTime = payLimitTime;
	}

	public String get_payLimitTime() {
		if(payLimitTime == null)
			return "";
		_payLimitTime = DateUtil.dateToString(payLimitTime, DateUtil.DATE_FMT3);
		return _payLimitTime;
	}

	public void set_payLimitTime(String payLimitTime) {
		_payLimitTime = payLimitTime;
	}

}
