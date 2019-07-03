package com.l9e.train.po;

import java.util.ArrayList;
import java.util.List;

/**
 * 请求机器人时的参数封装类
 * @author wangsf01
 *
 */
public class RequestParam {
	private String timeOut; //超时时间
	private String trainCode;//车次
	private String trainDate;//乘车日期
	private String fromCode;//出发站三字码
	private String fromName;//出发站名
	private String toCode;//到达站三字码
	private String toName;//到达站名
	private String payMoney;//支付金额

	private String railwayName;//12306账号
	private String railwayPwd;//12306密码
	
	private String couponNo;//优惠券编号
	private String channalName;//渠道账号
	private String channalPwd;//渠道密码
	private String channalPayPwd;//渠道支付密码
	private String contacts;//京东订票成功后接收短信的联系人姓名
	private String phone;//京东订票成功后接收短信的手机号
	
	
	private List<JDOrderCP> passengers = new ArrayList<JDOrderCP>(); //车票乘客信息
	private List<JdPrePayCard> cards = new ArrayList<JdPrePayCard>();//京东预付卡信息
	
	
	public String getTimeOut() {
		return timeOut;
	}
	public void setTimeOut(String timeOut) {
		this.timeOut = timeOut;
	}
	public String getTrainCode() {
		return trainCode;
	}
	public void setTrainCode(String trainCode) {
		this.trainCode = trainCode;
	}
	public String getTrainDate() {
		return trainDate;
	}
	public void setTrainDate(String trainDate) {
		this.trainDate = trainDate;
	}
	public String getFromCode() {
		return fromCode;
	}
	public void setFromCode(String fromCode) {
		this.fromCode = fromCode;
	}
	public String getFromName() {
		return fromName;
	}
	public void setFromName(String fromName) {
		this.fromName = fromName;
	}
	public String getToCode() {
		return toCode;
	}
	public void setToCode(String toCode) {
		this.toCode = toCode;
	}
	public String getToName() {
		return toName;
	}
	public void setToName(String toName) {
		this.toName = toName;
	}
	public String getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(String payMoney) {
		this.payMoney = payMoney;
	}
	public String getChannalName() {
		return channalName;
	}
	public void setChannalName(String channalName) {
		this.channalName = channalName;
	}
	public String getChannalPwd() {
		return channalPwd;
	}
	public void setChannalPwd(String channalPwd) {
		this.channalPwd = channalPwd;
	}
	public String getChannalPayPwd() {
		return channalPayPwd;
	}
	public void setChannalPayPwd(String channalPayPwd) {
		this.channalPayPwd = channalPayPwd;
	}

	public String getRailwayName() {
		return railwayName;
	}
	public void setRailwayName(String railwayName) {
		this.railwayName = railwayName;
	}
	public String getRailwayPwd() {
		return railwayPwd;
	}
	public void setRailwayPwd(String railwayPwd) {
		this.railwayPwd = railwayPwd;
	}
	public String getContacts() {
		return contacts;
	}
	public void setContacts(String contacts) {
		this.contacts = contacts;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public List<JdPrePayCard> getCards() {
		return cards;
	}
	public void setCards(List<JdPrePayCard> cards) {
		this.cards = cards;
	}
	public List<JDOrderCP> getPassengers() {
		return passengers;
	}
	public void setPassengers(List<JDOrderCP> passengers) {
		this.passengers = passengers;
	}
	public String getCouponNo() {
		return couponNo;
	}
	public void setCouponNo(String couponNo) {
		this.couponNo = couponNo;
	}
	

}
