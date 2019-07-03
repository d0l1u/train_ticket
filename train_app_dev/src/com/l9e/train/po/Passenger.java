package com.l9e.train.po;

/**
 * 乘客信息实体
 * 
 * @author licheng
 * 
 */
public class Passenger {

	/**
	 * 车票类型：儿童票
	 */
	public static final Integer TICKET_TYPE_CHILDREN = 1;
	/**
	 * 车票类型：成人票
	 */
	public static final Integer TICKET_TYPE_ADULT = 0;

	/**
	 * 证件类型：一代身份证
	 */
	public static final Integer PASSPORT_TYPE_ID_CARD = 1;
	/**
	 * 证件类型：二代身份证
	 */
	public static final Integer PASSPORT_TYPE_SECOND_ID_CARD = 2;
	/**
	 * 证件类型：港澳通行证
	 */
	public static final Integer PASSPORT_TYPE_HONGKONG_MACAO_PASS = 3;
	/**
	 * 证件类型：台湾通行证
	 */
	public static final Integer PASSPORT_TYPE_TAIWAN_PASS = 4;
	/**
	 * 证件类型：护照
	 */
	public static final Integer PASSPORT_TYPE_PASSPORT = 5;

	/**
	 * 核验信息：已通过
	 */
	public static final String CHECK_STATUS_HAVE_PASSED = "0";
	/**
	 * 核验信息：待核验
	 */
	public static final String CHECK_STATUS_VERIFY = "1";
	/**
	 * 核验信息：未通过
	 */
	public static final String CHECK_STATUS_NOT_PASS = "2";

	/**
	 * 车票id(主键)
	 */
	private String id;
	/**
	 * 乘客姓名
	 */
	private String name;
	/**
	 * 车票类型
	 */
	private Integer ticketType;
	/**
	 * 证件类型
	 */
	private Integer passportType;
	/**
	 * 证件号
	 */
	private String passportNo;
	/**
	 * 联系电话
	 */
	private String telephone;
	/**
	 * 支付价格
	 */
	private Double payMoney;
	/**
	 * 成本价格
	 */
	private Double buyMoney;
	/**
	 * 坐席类型
	 */
	private Integer seatType;
	/**
	 * 车厢号
	 */
	private String trainBox;
	/**
	 * 坐席号
	 */
	private String seatNo;
	/**
	 * 身份验证结果
	 */
	private String checkStatus;

	public Passenger() {
		super();
	}

	public Passenger(String id, String name, Integer ticketType,
			Integer passportType, String passportNo, String telephone,
			Double payMoney, Double buyMoney, Integer seatType,
			String trainBox, String seatNo, String checkStatus) {
		super();
		this.id = id;
		this.name = name;
		this.ticketType = ticketType;
		this.passportType = passportType;
		this.passportNo = passportNo;
		this.telephone = telephone;
		this.payMoney = payMoney;
		this.buyMoney = buyMoney;
		this.seatType = seatType;
		this.trainBox = trainBox;
		this.seatNo = seatNo;
		this.checkStatus = checkStatus;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getTicketType() {
		return ticketType;
	}

	public void setTicketType(Integer ticketType) {
		this.ticketType = ticketType;
	}

	public Integer getPassportType() {
		return passportType;
	}

	public void setPassportType(Integer passportType) {
		this.passportType = passportType;
	}

	public String getPassportNo() {
		return passportNo;
	}

	public void setPassportNo(String passportNo) {
		this.passportNo = passportNo;
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

	public Integer getSeatType() {
		return seatType;
	}

	public void setSeatType(Integer seatType) {
		this.seatType = seatType;
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

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	@Override
	public String toString() {
		return "Passenger [buyMoney=" + buyMoney + ", checkStatus="
				+ checkStatus + ", id=" + id + ", name=" + name
				+ ", passportNo=" + passportNo + ", passportType="
				+ passportType + ", payMoney=" + payMoney + ", seatNo="
				+ seatNo + ", seatType=" + seatType + ", telephone="
				+ telephone + ", ticketType=" + ticketType + ", trainBox="
				+ trainBox + "]";
	}

}
