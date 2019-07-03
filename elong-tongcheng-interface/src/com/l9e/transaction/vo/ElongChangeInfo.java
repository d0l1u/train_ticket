package com.l9e.transaction.vo;

import java.util.List;

/**
 * 改签记录
 * 
 * @author licheng
 * 
 */
public class ElongChangeInfo {


	/**
	 * 改签ID
	 */
	private Integer change_id;
	/**
	 * 订单ID
	 */
	private String order_id;
	/**
	 * 创建时间
	 */
	private String create_time;
	/**
	 * 退还原票票款记录的同程资金变动流水号(改签新票款大于原票款)
	 */
	private String old_ticket_change_serial;
	/**
	 * 收取新票票款记录的同程资金变动流水号(改签新票款大于原票款)
	 */
	private String new_ticket_change_serial;
	/**
	 * 退还票款差价记录的同程资金变动流水号(改签新票款小于原票款)
	 */
	private String ticket_price_diff_change_serial;
	/**
	 * 改签总票款差额
	 */
	private String change_diff_money;
	/**
	 * 退还原票款金额(新票款大于旧票款)
	 */
	private String change_refund_money;
	/**
	 * 收取新票款金额(新票款大于旧票款)
	 */
	private String change_receive_money;
	/**
	 * 改签车票预订时间
	 */
	private String book_ticket_time;
	/**
	 * 失败原因
	 */
	private String fail_reason;
	/**
	 * 失败原因
	 */
	private String fail_msg;
	/**
	 * 原车次
	 */
	private String train_no;
	/**
	 * 改签后车次
	 */
	private String change_train_no;
	/**
	 * 原发车时间
	 */
	private String from_time;
	/**
	 * 改签后发车时间
	 */
	private String change_from_time;
	/**
	 * 改签后到达时间
	 */
	private String change_to_time;
	/**
	 * 乘车日期
	 */
	private String travel_time;
	/**
	 * 改签后乘车日期
	 */
	private String change_travel_time;
	/**
	 * 出发城市
	 */
	private String from_city;
	/**
	 * 到达城市
	 */
	private String to_city;
	/**
	 * 12306单号
	 */
	private String out_ticket_billno;
	/**
	 * 出票账号id
	 */
	private String account_id;
	/**
	 * 改签状态，定义于TongChengChangeService接口中
	 */
	private String change_status;
	/**
	 * 是否异步
	 */
	private String isasync;
	/**
	 * 异步回调地址
	 */
	private String callbackurl;
	/**
	 * 请求特征值
	 */
	private String reqtoken;
	/**
	 * 回调状态
	 */
	private String change_notify_status;
	/**
	 * 回调次数
	 */
	private Integer change_notify_count;
	/**
	 * 回调开始时间
	 */
	private String change_notify_time;
	/**
	 * 回调结束时间
	 */
	private String change_notify_finish_time;
	               	    	        	    
	private String to_station_code;
	private String from_station_code;
	private String pay_limit_time;
	
	//0：改签 1：变更到站
	private Integer ischangeto;
	
	/**
	 * 是否选座  ,1 、选  0 、不选
	 */
	private Integer isChooseSeats;
	
	/**
	 * 选座信息  
	 */
	private String  chooseSeats;
	
	private String merchant_id;
	/**
	 * 改签记录所改签的车票
	 */
	private List<ElongChangePassengerInfo> cPassengers;
	
	
	private String fee;
	private String totalpricediff;
	private String diffrate;
	/**
	 * 回调结束时间
	 */

	public Integer getChange_id() {
		return change_id;
	}

	public String getTo_station_code() {
		return to_station_code;
	}

	public void setTo_station_code(String toStationCode) {
		to_station_code = toStationCode;
	}

	public String getFrom_station_code() {
		return from_station_code;
	}

	public void setFrom_station_code(String fromStationCode) {
		from_station_code = fromStationCode;
	}

	public String getFail_msg() {
		return fail_msg;
	}

	public void setFail_msg(String failMsg) {
		fail_msg = failMsg;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getTotalpricediff() {
		return totalpricediff;
	}

	public void setTotalpricediff(String totalpricediff) {
		this.totalpricediff = totalpricediff;
	}

	public String getDiffrate() {
		return diffrate;
	}

	public void setDiffrate(String diffrate) {
		this.diffrate = diffrate;
	}

	public void setChange_id(Integer changeId) {
		change_id = changeId;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String orderId) {
		order_id = orderId;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String createTime) {
		create_time = createTime;
	}

	public String getIsasync() {
		return isasync;
	}

	public void setIsasync(String isasync) {
		this.isasync = isasync;
	}

	public String getCallbackurl() {
		return callbackurl;
	}

	public void setCallbackurl(String callbackurl) {
		this.callbackurl = callbackurl;
	}

	public String getReqtoken() {
		return reqtoken;
	}

	public void setReqtoken(String reqtoken) {
		this.reqtoken = reqtoken;
	}

	public String getOld_ticket_change_serial() {
		return old_ticket_change_serial;
	}

	public void setOld_ticket_change_serial(String oldTicketChangeSerial) {
		old_ticket_change_serial = oldTicketChangeSerial;
	}

	public String getNew_ticket_change_serial() {
		return new_ticket_change_serial;
	}

	public void setNew_ticket_change_serial(String newTicketChangeSerial) {
		new_ticket_change_serial = newTicketChangeSerial;
	}

	public String getTicket_price_diff_change_serial() {
		return ticket_price_diff_change_serial;
	}

	public void setTicket_price_diff_change_serial(
			String ticketPriceDiffChangeSerial) {
		ticket_price_diff_change_serial = ticketPriceDiffChangeSerial;
	}

	public String getChange_diff_money() {
		return change_diff_money;
	}

	public void setChange_diff_money(String changeDiffMoney) {
		change_diff_money = changeDiffMoney;
	}

	public String getChange_refund_money() {
		return change_refund_money;
	}

	public void setChange_refund_money(String changeRefundMoney) {
		change_refund_money = changeRefundMoney;
	}

	public String getChange_receive_money() {
		return change_receive_money;
	}

	public void setChange_receive_money(String changeReceiveMoney) {
		change_receive_money = changeReceiveMoney;
	}

	public String getChange_notify_status() {
		return change_notify_status;
	}

	public void setChange_notify_status(String changeNotifyStatus) {
		change_notify_status = changeNotifyStatus;
	}

	public Integer getChange_notify_count() {
		return change_notify_count;
	}

	public void setChange_notify_count(Integer changeNotifyCount) {
		change_notify_count = changeNotifyCount;
	}

	public String getChange_notify_time() {
		return change_notify_time;
	}

	public void setChange_notify_time(String changeNotifyTime) {
		change_notify_time = changeNotifyTime;
	}

	public String getChange_notify_finish_time() {
		return change_notify_finish_time;
	}

	public void setChange_notify_finish_time(String changeNotifyFinishTime) {
		change_notify_finish_time = changeNotifyFinishTime;
	}

	public String getBook_ticket_time() {
		return book_ticket_time;
	}

	public void setBook_ticket_time(String bookTicketTime) {
		book_ticket_time = bookTicketTime;
	}

	public String getFail_reason() {
		return fail_reason;
	}

	public void setFail_reason(String failReason) {
		fail_reason = failReason;
	}

	public String getTrain_no() {
		return train_no;
	}

	public void setTrain_no(String trainNo) {
		train_no = trainNo;
	}

	public String getChange_train_no() {
		return change_train_no;
	}

	public void setChange_train_no(String changeTrainNo) {
		change_train_no = changeTrainNo;
	}

	public String getFrom_time() {
		return from_time;
	}

	public void setFrom_time(String fromTime) {
		from_time = fromTime;
	}

	public String getChange_from_time() {
		return change_from_time;
	}

	public void setChange_from_time(String changeFromTime) {
		change_from_time = changeFromTime;
	}

	public String getTravel_time() {
		return travel_time;
	}

	public void setTravel_time(String travelTime) {
		travel_time = travelTime;
	}

	public String getChange_travel_time() {
		return change_travel_time;
	}

	public void setChange_travel_time(String changeTravelTime) {
		change_travel_time = changeTravelTime;
	}

	public String getFrom_city() {
		return from_city;
	}

	public void setFrom_city(String fromCity) {
		from_city = fromCity;
	}

	public String getTo_city() {
		return to_city;
	}

	public void setTo_city(String toCity) {
		to_city = toCity;
	}

	public String getOut_ticket_billno() {
		return out_ticket_billno;
	}

	public void setOut_ticket_billno(String outTicketBillno) {
		out_ticket_billno = outTicketBillno;
	}

	public String getAccount_id() {
		return account_id;
	}

	public void setAccount_id(String accountId) {
		account_id = accountId;
	}

	public String getChange_status() {
		return change_status;
	}

	public void setChange_status(String changeStatus) {
		change_status = changeStatus;
	}

	public List<ElongChangePassengerInfo> getcPassengers() {
		return cPassengers;
	}

	public void setcPassengers(List<ElongChangePassengerInfo> cPassengers) {
		this.cPassengers = cPassengers;
	}

	public Integer getIschangeto() {
		return ischangeto;
	}

	public void setIschangeto(Integer ischangeto) {
		this.ischangeto = ischangeto;
	}

	public String getMerchant_id() {
		return merchant_id;
	}

	public void setMerchant_id(String merchant_id) {
		this.merchant_id = merchant_id;
	}

	public String getChange_to_time() {
		return change_to_time;
	}

	public void setChange_to_time(String changeToTime) {
		change_to_time = changeToTime;
	}

	public String getPay_limit_time() {
		return pay_limit_time;
	}

	public void setPay_limit_time(String payLimitTime) {
		pay_limit_time = payLimitTime;
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



}
