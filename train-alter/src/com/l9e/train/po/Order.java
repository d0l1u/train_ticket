package com.l9e.train.po;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

//退款订单的实体类
public class Order {
	
	@JsonProperty("retValue")  
	public String retValue;//返回值
	
	@JsonProperty("retInfo")  
	private String retInfo;//返回信息
	
	private String changeId;
	private String orderId;
	private String createTime;
	private String orderstr;
	private String outTicketBillno;//12306单号
	private String trainNo;
	private String fromCity;
	private String toCity;
	private String changeStatus;
	private String fromTime;
	private String travelTime;
	private String seatType;
	private String changeTrainNo;
	private String changeTravelTime;
	private String changeSeatType;
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
	private Integer isChangeTo; //0:改签, 1:变更到站
	public String fromCity_3c; //订单中的出发城市三字码	
	public String toCity_3c; //订单中的到达城市三字码
	public Integer hasSeat;//改签是否接受无座票  1、不改到无座票 0、允许改到无座票
	public Integer alterPayType; //改签支付类型    1：平改   2：高改低   3：低改高
	public Double changeReceiveMoney; //待支付的总金额
	
	private String chooseSeat;
	
	public static final String WAITING_RESIGN="11"; //等待改签
	
	/** 正在改签 */
	public static final String RESIGNING = "12";  
	
	/** 人工改签 */
	public static final String ARTIFICIAL_RESIGN = "13";
	
	/** 改签待确认 */
	public static final String WAITING_CONFIRM = "14";
	
	/** 改签失败 */
	public static final String RESIGN_FAILURE = "15";	
	public static final String WAITING_CANCEL = "21";	//等待取消
	public static final String CANCELING = "22";		//正在取消
	public static final String CANCEL_FINISH = "23";	//取消成功
	public static final String CANCEL_FAILURE = "24";	//取消失败
	public static final String WAITING_PAY = "31";		//等待支付
	public static final String PAYING = "32";			//正在支付
	public static final String ARTIFICIAL_PAY = "33";	//人工支付
	public static final String PAY_FINISH = "34";		//支付成功
	public static final String PAY_FAILURE = "35";		//支付失败
	public static final String FILL_PAY = "36";			//补价支付
	
	public String getChangeId() {
		return changeId;
	}
	public void setChangeId(String changeId) {
		this.changeId = changeId;
	}
	public String getFromCity() {
		return fromCity;
	}
	public void setFromCity(String fromCity) {
		this.fromCity = fromCity;
	}
	public String getToCity() {
		return toCity;
	}
	public void setToCity(String toCity) {
		this.toCity = toCity;
	}
	public String getChangeTrainNo() {
		return changeTrainNo;
	}
	public void setChangeTrainNo(String changeTrainNo) {
		this.changeTrainNo = changeTrainNo;
	}
	public String getChangeTravelTime() {
		return changeTravelTime;
	}
	public void setChangeTravelTime(String changeTravelTime) {
		this.changeTravelTime = changeTravelTime;
	}
	
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

	
	public String getChangeSeatType() {
		return changeSeatType;
	}
	public void setChangeSeatType(String changeSeatType) {
		this.changeSeatType = changeSeatType;
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
	public String getChangeStatus() {
		return changeStatus;
	}
	public void setChangeStatus(String changeStatus) {
		this.changeStatus = changeStatus;
	}
	public String getAlterFromTime() {
		return alterFromTime;
	}
	public void setAlterFromTime(String alterFromTime) {
		this.alterFromTime = alterFromTime;
	}
	public Integer getIsChangeTo() {
		return isChangeTo;
	}
	public void setIsChangeTo(Integer isChangeTo) {
		this.isChangeTo = isChangeTo;
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
	public Integer getHasSeat() {
		return hasSeat;
	}
	public void setHasSeat(Integer hasSeat) {
		this.hasSeat = hasSeat;
	}
	public Integer getAlterPayType() {
		return alterPayType;
	}
	public void setAlterPayType(Integer alterPayType) {
		this.alterPayType = alterPayType;
	}
	public Double getChangeReceiveMoney() {
		return changeReceiveMoney;
	}
	public void setChangeReceiveMoney(Double changeReceiveMoney) {
		this.changeReceiveMoney = changeReceiveMoney;
	}
	public String getChooseSeat() {
		return chooseSeat;
	}
	public void setChooseSeat(String chooseSeat) {
		this.chooseSeat = chooseSeat;
	}
}
