package com.l9e.transaction.vo;

import java.io.Serializable;

public class TradeVo implements Serializable {

	private String order_id;
	private String trade_no;	//支付平台生成的流水号
	private String trade_seq;	//平台生成的流水号
	private String batch_no; // 支付宝支出时的批次号
	private String create_time; // 信息创建时间
	private String trade_time; // 交易时间
	private String trade_id;
	private String trade_type; // 支出或收入
	private String trade_status; // 交易状态
	private String buyer_id; // 支出账号
	private String buyer_name; // 支出账号名
	private String seller_id; // 收款账号
	private String trade_fee; // 交易金额
	private String channel; // 渠道，如qunar，elong, chunqiu
	private String trade_channel; // 交易渠道,如alipay，cbc
	private String seller_name;
	private String operate_time; // 操作时间
	private String operate_person; // 最后造作人
	private String remark;		//备注
	private String fail_reason;	//失败原因

	public final static String TRADE_SUCCESS = "02"; // 交易成功，且可对该交易做操作，如：多级分润、退款等。
	public final static String TRADE_CLOSED = "04"; // 交易关闭
	public final static String WAIT_BUYER_PAY = "00"; // 等待买家付款
	public final static String TRADE_PENDING = "01"; // 等待卖家收款
	public final static String TRADE_FINISHED = "03"; // 交易成功且结束，即不可再做任何操作
	
	
	//10、等待退款，11、正在退款， 12退款成功， 13， 退款失败
	public static final String WAIT_REFUND = "10";		//等待退款
	public static final String REFUNDING = "11";		//正在退款
	public static final String START_REFUND_SUCCESS="12";	//发起退款成功
	public static final String REFUND_SUCCESS = "13";	//退款成功
	public static final String REFUND_FAILURE = "14";	//退款失败
	public static final String ALIPAY_REFUNDING = "15"; //处理中或银行卡充退

	public final static String ALIPAY_TRADE_CHANNEL = "alipay";
	public final static String CBC_TRADE_CHANNEL = "cbc";

	public final static String INCOME = "0"; // 收入
	public final static String EXPENSES = "1"; // 支出

	public TradeVo() {
		super();
	}

	public TradeVo(String trade_id, String order_id, String trade_no, String trade_seq,
			String batch_no, String create_time, String trade_time,
			String trade_type, String buyer_name, String trade_status,
			String buyer_id, String seller_id, String trade_fee,
			String channel, String trade_channel, String operate_time,
			String operate_person, String remark, String fail_reason) {
		super();
		this.trade_id = trade_id;
		this.order_id = order_id;
		this.trade_no = trade_no;
		this.batch_no = batch_no;
		this.trade_seq = trade_seq;
		this.create_time = create_time;
		this.trade_time = trade_time;
		this.trade_type = trade_type;
		this.trade_status = trade_status;
		this.buyer_id = buyer_id;
		this.buyer_name = buyer_name;
		this.seller_id = seller_id;
		this.trade_fee = trade_fee;
		this.channel = channel;
		this.operate_person = operate_person;
		this.operate_time = operate_time;
		this.trade_channel = trade_channel;
		this.remark = remark;
		this.fail_reason = fail_reason;
	}

	public String getTrade_seq() {
		return trade_seq;
	}

	public void setTrade_seq(String trade_seq) {
		this.trade_seq = trade_seq;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getFail_reason() {
		return fail_reason;
	}

	public void setFail_reason(String fail_reason) {
		this.fail_reason = fail_reason;
	}

	public String getBatch_no() {
		return batch_no;
	}

	public void setBatch_no(String batch_no) {
		this.batch_no = batch_no;
	}

	public String getBuyer_name() {
		return buyer_name;
	}

	public void setBuyer_name(String buyer_name) {
		this.buyer_name = buyer_name;
	}

	public String getTrade_id() {
		return trade_id;
	}

	public void setTrade_id(String trade_id) {
		this.trade_id = trade_id;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getTrade_no() {
		return trade_no;
	}

	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getTrade_time() {
		return trade_time;
	}

	public void setTrade_time(String trade_time) {
		this.trade_time = trade_time;
	}

	public String getTrade_type() {
		return trade_type;
	}

	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}

	public String getTrade_status() {
		return trade_status;
	}

	public void setTrade_status(String trade_status) {
		this.trade_status = trade_status;
	}

	public String getBuyer_id() {
		return buyer_id;
	}

	public void setBuyer_id(String buyer_id) {
		this.buyer_id = buyer_id;
	}

	public String getSeller_id() {
		return seller_id;
	}

	public void setSeller_id(String seller_id) {
		this.seller_id = seller_id;
	}

	public String getSeller_name() {
		return seller_name;
	}

	public void setSeller_name(String seller_name) {
		this.seller_name = seller_name;
	}

	public String getTrade_fee() {
		return trade_fee;
	}

	public void setTrade_fee(String trade_fee) {
		this.trade_fee = trade_fee;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getTrade_channel() {
		return trade_channel;
	}

	public void setTrade_channel(String trade_channel) {
		this.trade_channel = trade_channel;
	}

	public String getOperate_time() {
		return operate_time;
	}

	public void setOperate_time(String operate_time) {
		this.operate_time = operate_time;
	}

	public String getOperate_person() {
		return operate_person;
	}

	public void setOperate_person(String operate_person) {
		this.operate_person = operate_person;
	}

	public String toString() {
		StringBuffer element = new StringBuffer();
		element.append("[trade_id:").append(trade_id).append(",order_id:")
				.append(order_id).append(", trade_no:").append(trade_no)
				.append(", trade_type:").append(trade_type)
				.append(",trade_time:").append(trade_time)
				.append(", trade_status:").append(trade_status)
				.append(", channel:").append(channel).append(", buyer_id:")
				.append(buyer_id).append(", seller_id:").append(seller_id)
				.append(",trade_fee:").append(trade_fee)
				.append(", trade_channel:").append(trade_channel).append("]");
		return element.toString();
	}
}
