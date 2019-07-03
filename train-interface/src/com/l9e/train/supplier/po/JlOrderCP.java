package com.l9e.train.supplier.po;

/**
 * 抢票订单车票信息实体类
 * @author wangsf01
 *
 */
public class JlOrderCP {
	
	public String cpId;//车票ID
	public String orderId;//关联的订单ID
	public String userName;//车票中客户姓名
	public Integer ticketType;//车票类型
	public Integer certType;//证件类型
	public String certNo;//证件号码
	public String telephone;//电话号码
	public String createTime;//创建时间
	public String payMoney;//支付价格
	public String buyMoney;//成本价格
	public String modifyTime;//修改时间
	public String seatType;//座席类型
	public String trainNo;//车次
	public String trainBox;//车厢
	public String seatNo;//座位号
	public String checkStatus;//12306身份核验状态
	public String payMoneyExt;//咱们把抢票订单传给携程出票时，携程收取咱们的单人抢票服务费（咱们给携程的服务费，单人的）
	public String buyMoneyExt;//抢票时咱们收取客户的单人抢票服务费（客户给咱们的服务费，单人的）
	
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
	public Integer getTicketType() {
		return ticketType;
	}
	public void setTicketType(Integer ticketType) {
		this.ticketType = ticketType;
	}
	public Integer getCertType() {
		return certType;
	}
	public void setCertType(Integer certType) {
		this.certType = certType;
	}
	public String getCertNo() {
		return certNo;
	}
	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(String payMoney) {
		this.payMoney = payMoney;
	}
	public String getBuyMoney() {
		return buyMoney;
	}
	public void setBuyMoney(String buyMoney) {
		this.buyMoney = buyMoney;
	}
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	public String getSeatType() {
		return seatType;
	}
	public void setSeatType(String seatType) {
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
	public String getPayMoneyExt() {
		return payMoneyExt;
	}
	public void setPayMoneyExt(String payMoneyExt) {
		this.payMoneyExt = payMoneyExt;
	}
	public String getBuyMoneyExt() {
		return buyMoneyExt;
	}
	public void setBuyMoneyExt(String buyMoneyExt) {
		this.buyMoneyExt = buyMoneyExt;
	}
	public String getTrainNo() {
		return trainNo;
	}
	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}

	
	
	

}
