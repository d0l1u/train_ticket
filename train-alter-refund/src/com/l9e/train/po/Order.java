package com.l9e.train.po;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;


//退款订单的实体类
public class Order {
	
	@JsonProperty("retValue")  
	public String retValue;//返回值
	
	@JsonProperty("retInfo")  
	private String retInfo;//返回信息
	
	private String orderId;
	private String createTime;
	private String orderstr;
	private String outTicketBillno;//12306单号
	private String trainNo;
	private String fromStation;
	private String arriveStation;
	private String fromTime;
	private String travelTime;
	private String seatType;
	private String alterTrainNo;
	private String alterTravelTime;
	private String alterSeatType;
	private String channel;
	private String accountName;
	private String accountPwd;
	private String inputCode;			//是否人工打码
	private String from_time_fifd;		//发车前15天
	private String from_time_tfh;		//发车前24小时
	private String from_time_feh;		//发车前48小时
	private String alterFromTime;
	private String buy_money;//	成人票价
	public Integer accountId;
	public List<OrderCP> cps;
	private String refund_percent;//手续费百分比
	private String account_from_way;//账号来源
	public String fromCity_3c; //订单中的出发城市三字码	
	public String toCity_3c; //订单中的到达城市三字码
	
	public String getBuy_money() {
		return buy_money;
	}
	public void setBuy_money(String buyMoney) {
		buy_money = buyMoney;
	}
	public String getRetValue() {
		return retValue;
	}
	public void setRetValue(String retValue) {
		this.retValue = retValue;
	}
	public String getRetInfo() {
		return retInfo;
	}
	public void setRetInfo(String retInfo) {
		this.retInfo = retInfo;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getOrderstr() {
		return orderstr;
	}
	public void setOrderstr(String orderstr) {
		this.orderstr = orderstr;
	}
	public String getOutTicketBillno() {
		return outTicketBillno;
	}
	public void setOutTicketBillno(String outTicketBillno) {
		this.outTicketBillno = outTicketBillno;
	}
	public String getTrainNo() {
		return trainNo;
	}
	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}
	public String getFromStation() {
		return fromStation;
	}
	public void setFromStation(String fromStation) {
		this.fromStation = fromStation;
	}
	public String getArriveStation() {
		return arriveStation;
	}
	public void setArriveStation(String arriveStation) {
		this.arriveStation = arriveStation;
	}
	public String getFromTime() {
		return fromTime;
	}
	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
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
		this.seatType = seatType;
	}
	public String getAlterTrainNo() {
		return alterTrainNo;
	}
	public void setAlterTrainNo(String alterTrainNo) {
		this.alterTrainNo = alterTrainNo;
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
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public Integer getAccountId() {
		return accountId;
	}
	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}
	public List<OrderCP> getCps() {
		return cps;
	}
	public void setCps(List<OrderCP> cps) {
		this.cps = cps;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getAccountPwd() {
		return accountPwd;
	}
	public void setAccountPwd(String accountPwd) {
		this.accountPwd = accountPwd;
	}
	public String getInputCode() {
		return inputCode;
	}
	public void setInputCode(String inputCode) {
		this.inputCode = inputCode;
	}
	public String getFrom_time_fifd() {
		return from_time_fifd;
	}
	public void setFrom_time_fifd(String fromTimeFifd) {
		from_time_fifd = fromTimeFifd;
	}
	public String getFrom_time_tfh() {
		return from_time_tfh;
	}
	public void setFrom_time_tfh(String fromTimeTfh) {
		from_time_tfh = fromTimeTfh;
	}
	public String getFrom_time_feh() {
		return from_time_feh;
	}
	public void setFrom_time_feh(String fromTimeFeh) {
		from_time_feh = fromTimeFeh;
	}
	public String getAlterFromTime() {
		return alterFromTime;
	}
	public void setAlterFromTime(String alterFromTime) {
		this.alterFromTime = alterFromTime;
	}
	public String getRefund_percent() {
		return refund_percent;
	}
	public void setRefund_percent(String refundPercent) {
		refund_percent = refundPercent;
	}
	public String getAccount_from_way() {
		return account_from_way;
	}
	public void setAccount_from_way(String accountFromWay) {
		account_from_way = accountFromWay;
	}
	public String getFromCity_3c() {
		return fromCity_3c;
	}
	public void setFromCity_3c(String fromCity_3c) {
		this.fromCity_3c = fromCity_3c;
	}
	public String getToCity_3c() {
		return toCity_3c;
	}
	public void setToCity_3c(String toCity_3c) {
		this.toCity_3c = toCity_3c;
	}
	
	
}
