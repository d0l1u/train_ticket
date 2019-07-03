package com.l9e.transaction.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 途牛乘客
 * 
 * @author licheng
 * 
 */
public class TuniuPassenger {

	/* 一般参数 */
	/**
	 * 车票id
	 */
	@JsonProperty("ticketNo")
	private String cpId;
	/**
	 * 订单id
	 */
	@JsonIgnore
	private String orderId;
	/**
	 * 乘客姓名
	 */
	@JsonProperty("passengerName")
	private String userName;
	/**
	 * 保险单号
	 */
	private String insureNumber;
	/**
	 * 保险价格
	 */
	private Double insurePrice;
	/**
	 * 途牛坐席类型
	 */
	@JsonProperty("zwCode")
	private String tuniuSeatType;
	/**
	 * 系统坐席类型
	 */
	@JsonIgnore
	private String seatType;
	/**
	 * 途牛车票类型
	 */
	@JsonProperty("piaoType")
	private String tuniuTicketType;
	/**
	 * 系统车票类型
	 */
	@JsonIgnore
	private String ticketType;
	/**
	 * 途牛证件类型
	 */
	@JsonProperty("passportTypeId")
	private String tuniuIdsType;
	/**
	 * 系统证件类型
	 */
	@JsonIgnore
	private String idsType;
	/**
	 * 证件号码
	 */
	@JsonProperty("passportNo")
	private String userIds;
	/**
	 * 联系电话
	 */
	@JsonIgnore
	private String telephone;
	/**
	 * 支付金额
	 */
	@JsonProperty("price")
	private Double payMoney;
	/**
	 * 成本价
	 */
	@JsonIgnore
	private Double buyMoney;
	/**
	 * 车厢号
	 */
	@JsonIgnore
	private String trainBox;
	/**
	 * 座位号
	 */
	@JsonIgnore
	private String seatNo;
	/**
	 * 12306出票单号
	 */
	@JsonIgnore
	private String outTicketBillno;
	/**
	 * 途牛乘客序号
	 */
	private Integer passengerId;
	/**
	 * 身份核验信息 0、正常 1、待审核 2、未通过
	 */
	private Integer reason;

	/* 学生票参数 */
	/**
	 * 省份编码
	 */
	private String provinceCode;
	/**
	 * 省份名称
	 */
	private String provinceName;
	/**
	 * 学校代号
	 */
	private String schoolCode;
	/**
	 * 学校名称
	 */
	private String schoolName;
	/**
	 * 学号
	 */
	private String studentNo;
	/**
	 * 学制
	 */
	private String schoolSystem;
	/**
	 * 入学年份
	 */
	private String enterYear;
	/**
	 * 优惠区间起始地名称
	 */
	private String preferenceFromStationName;
	/**
	 * 优惠区间起始地代号
	 */
	private String preferenceFromStationCode;
	/**
	 * 优惠区间到达地名称
	 */
	private String preferenceToStationName;
	/**
	 * 优惠区间到达地代号
	 */
	private String preferenceToStationCode;

	/* 输入输出参数 */
	/**
	 * 证件类型名称
	 */
	private String passportTypeName;
	/**
	 * 车票类型名称
	 */
	private String piaoTypeName;
	/**
	 * 座位编码名称
	 */
	private String zwName;
	/**
	 * 几车厢几座
	 */
	private String cxin;

	public TuniuPassenger() {
		super();
	}

	public String getCpId() {
		return cpId;
	}

	public void setCpId(String cpId) {
		this.cpId = cpId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getInsureNumber() {
		return insureNumber;
	}

	public void setInsureNumber(String insureNumber) {
		this.insureNumber = insureNumber;
	}

	public Double getInsurePrice() {
		return insurePrice;
	}

	public void setInsurePrice(Double insurePrice) {
		this.insurePrice = insurePrice;
	}

	public String getTuniuSeatType() {
		return tuniuSeatType;
	}

	public void setTuniuSeatType(String tuniuSeatType) {
		this.tuniuSeatType = tuniuSeatType;
	}

	public String getSeatType() {
		return seatType;
	}

	public void setSeatType(String seatType) {
		this.seatType = seatType;
	}

	public String getTuniuTicketType() {
		return tuniuTicketType;
	}

	public void setTuniuTicketType(String tuniuTicketType) {
		this.tuniuTicketType = tuniuTicketType;
	}

	public String getTicketType() {
		return ticketType;
	}

	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}

	public String getTuniuIdsType() {
		return tuniuIdsType;
	}

	public void setTuniuIdsType(String tuniuIdsType) {
		this.tuniuIdsType = tuniuIdsType;
	}

	public String getIdsType() {
		return idsType;
	}

	public void setIdsType(String idsType) {
		this.idsType = idsType;
	}

	public String getUserIds() {
		return userIds;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
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

	public String getTrainBox() {
		return trainBox;
	}

	public void setTrainBox(String trainBox) {
		this.trainBox = trainBox;
	}

	public String getSeatNo() {
		return seatNo;
	}

	public void setSeatNo(String seatNo) {
		this.seatNo = seatNo;
	}

	public String getOutTicketBillno() {
		return outTicketBillno;
	}

	public void setOutTicketBillno(String outTicketBillno) {
		this.outTicketBillno = outTicketBillno;
	}

	public Integer getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(Integer passengerId) {
		this.passengerId = passengerId;
	}

	public Integer getReason() {
		return reason;
	}

	public void setReason(Integer reason) {
		this.reason = reason;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getSchoolCode() {
		return schoolCode;
	}

	public void setSchoolCode(String schoolCode) {
		this.schoolCode = schoolCode;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getStudentNo() {
		return studentNo;
	}

	public void setStudentNo(String studentNo) {
		this.studentNo = studentNo;
	}

	public String getSchoolSystem() {
		return schoolSystem;
	}

	public void setSchoolSystem(String schoolSystem) {
		this.schoolSystem = schoolSystem;
	}

	public String getEnterYear() {
		return enterYear;
	}

	public void setEnterYear(String enterYear) {
		this.enterYear = enterYear;
	}

	public String getPreferenceFromStationName() {
		return preferenceFromStationName;
	}

	public void setPreferenceFromStationName(String preferenceFromStationName) {
		this.preferenceFromStationName = preferenceFromStationName;
	}

	public String getPreferenceFromStationCode() {
		return preferenceFromStationCode;
	}

	public void setPreferenceFromStationCode(String preferenceFromStationCode) {
		this.preferenceFromStationCode = preferenceFromStationCode;
	}

	public String getPreferenceToStationName() {
		return preferenceToStationName;
	}

	public void setPreferenceToStationName(String preferenceToStationName) {
		this.preferenceToStationName = preferenceToStationName;
	}

	public String getPreferenceToStationCode() {
		return preferenceToStationCode;
	}

	public void setPreferenceToStationCode(String preferenceToStationCode) {
		this.preferenceToStationCode = preferenceToStationCode;
	}

	public String getPassportTypeName() {
		return passportTypeName;
	}

	public void setPassportTypeName(String passportTypeName) {
		this.passportTypeName = passportTypeName;
	}

	public String getPiaoTypeName() {
		return piaoTypeName;
	}

	public void setPiaoTypeName(String piaoTypeName) {
		this.piaoTypeName = piaoTypeName;
	}

	public String getZwName() {
		return zwName;
	}

	public void setZwName(String zwName) {
		this.zwName = zwName;
	}

	public String getCxin() {
		return cxin;
	}

	public void setCxin(String cxin) {
		this.cxin = cxin;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

}
