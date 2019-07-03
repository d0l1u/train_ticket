package com.l9e.transaction.vo;

public class BookPassenger {

	private static final long serialVersionUID = 1L;
	
	private String name;//乘客姓名
	private String certNo;//乘客证件号码
	private String certType;//证件类型:1:二代身份证，2:一代身份证，C:港澳通行证，G:台湾通行证，B:护照
	private String certName;//证件类型名称
	private String ticketType;//票种:1:成人票，2:儿童票，3:学生票，4:残军票
	private String ticketPrice;//票价，暂不支持联程
	private String ticketName;//票种名称
	private String seatCode;//与座位名称对应关系：9:商务座，P:特等座，M:一等座，O:二等座，6:高级软卧，4:软卧，3:硬卧，2:软座，1:硬座
	private String seatName;//座位名称
	private String status;//身份核验状态: 0：正常 1：待审核 2：未通过     目前没有身份核验，传的是null
	private PassengerStudentExt passengerStudentExt;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCertNo() {
		return certNo;
	}
	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}
	public String getCertType() {
		return certType;
	}
	public void setCertType(String certType) {
		this.certType = certType;
	}
	public String getCertName() {
		return certName;
	}
	public void setCertName(String certName) {
		this.certName = certName;
	}
	public String getTicketType() {
		return ticketType;
	}
	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}
	public String getTicketPrice() {
		return ticketPrice;
	}
	public void setTicketPrice(String ticketPrice) {
		this.ticketPrice = ticketPrice;
	}
	public String getTicketName() {
		return ticketName;
	}
	public void setTicketName(String ticketName) {
		this.ticketName = ticketName;
	}
	public String getSeatCode() {
		return seatCode;
	}
	public void setSeatCode(String seatCode) {
		this.seatCode = seatCode;
	}
	public String getSeatName() {
		return seatName;
	}
	public void setSeatName(String seatName) {
		this.seatName = seatName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public PassengerStudentExt getPassengerStudentExt() {
		return passengerStudentExt;
	}
	public void setPassengerStudentExt(PassengerStudentExt passengerStudentExt) {
		this.passengerStudentExt = passengerStudentExt;
	}
	
}
