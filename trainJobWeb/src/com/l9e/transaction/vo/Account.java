package com.l9e.transaction.vo;

public class Account {

	private String accId;
	private String accUsername;
	private String accPassword;
	private String accStatus;
	private String createTime;
	private String optionTime;
	private String atProvinceId;
	private String atCityId;
	private String orderId;
	private String stopTime;
	private String priority;
	private String channel;
	private String optPerson;
	private String accMail;
	private String stopReason;
	private String realName;
	private String idCard;
	private String contactNum;
	private String accountSource;
	private String isAlive;
	private String liveTime;
	private String verifyTime;
	private String deleteRemark;
	private String activeStatus;
	private String activeTime;
	private String deleteStatus;
	private String modifyStatus;
	private String oldPass;
	private String modifyTime;
	private int bookNum;
	

	public static String DOWNING = "00"; // 正在下单
	public static String FREE = "33"; // 账号空闲
	public static String VERIFY = "55"; // 正在核验
	public static final String ACC_STATUS_SUSPENDED = "44";
	public static final String ACC_STATUS_FREE = "33";
	public static final String STOP_REASON_SUSPENDED = "5";

	public int getBookNum() {
		return bookNum;
	}

	public void setBookNum(int bookNum) {
		this.bookNum = bookNum;
	}

	public String getAccStatus() {
		return accStatus;
	}

	public void setAccStatus(String accStatus) {
		this.accStatus = accStatus;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getOptionTime() {
		return optionTime;
	}

	public void setOptionTime(String optionTime) {
		this.optionTime = optionTime;
	}

	public String getAtProvinceId() {
		return atProvinceId;
	}

	public void setAtProvinceId(String atProvinceId) {
		this.atProvinceId = atProvinceId;
	}

	public String getAtCityId() {
		return atCityId;
	}

	public void setAtCityId(String atCityId) {
		this.atCityId = atCityId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getStopTime() {
		return stopTime;
	}

	public void setStopTime(String stopTime) {
		this.stopTime = stopTime;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getOptPerson() {
		return optPerson;
	}

	public void setOptPerson(String optPerson) {
		this.optPerson = optPerson;
	}

	public String getAccMail() {
		return accMail;
	}

	public void setAccMail(String accMail) {
		this.accMail = accMail;
	}

	public String getStopReason() {
		return stopReason;
	}

	public void setStopReason(String stopReason) {
		this.stopReason = stopReason;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getContactNum() {
		return contactNum;
	}

	public void setContactNum(String contactNum) {
		this.contactNum = contactNum;
	}

	public String getAccountSource() {
		return accountSource;
	}

	public void setAccountSource(String accountSource) {
		this.accountSource = accountSource;
	}

	public String getIsAlive() {
		return isAlive;
	}

	public void setIsAlive(String isAlive) {
		this.isAlive = isAlive;
	}

	public String getLiveTime() {
		return liveTime;
	}

	public void setLiveTime(String liveTime) {
		this.liveTime = liveTime;
	}

	public String getVerifyTime() {
		return verifyTime;
	}

	public void setVerifyTime(String verifyTime) {
		this.verifyTime = verifyTime;
	}

	public String getDeleteRemark() {
		return deleteRemark;
	}

	public void setDeleteRemark(String deleteRemark) {
		this.deleteRemark = deleteRemark;
	}

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getActiveTime() {
		return activeTime;
	}

	public void setActiveTime(String activeTime) {
		this.activeTime = activeTime;
	}

	public String getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(String deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

	public String getAccId() {
		return accId;
	}

	public void setAccId(String accId) {
		this.accId = accId;
	}

	public String getAccUsername() {
		return accUsername;
	}

	public void setAccUsername(String accUsername) {
		this.accUsername = accUsername;
	}

	public String getAccPassword() {
		return accPassword;
	}

	public void setAccPassword(String accPassword) {
		this.accPassword = accPassword;
	}

	public String getModifyStatus() {
		return modifyStatus;
	}

	public void setModifyStatus(String modifyStatus) {
		this.modifyStatus = modifyStatus;
	}

	public String getOldPass() {
		return oldPass;
	}

	public void setOldPass(String oldPass) {
		this.oldPass = oldPass;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

}
