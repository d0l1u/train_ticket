package com.l9e.train.supplier.po;


import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 * 退款的实体类
 * 
 * @author guona
 * 
 */
public class OrderRefund {
	private Logger logger = Logger.getLogger(this.getClass());
	public static String REFUND_ERROR = "1001"; 
	public static String REFUND_SUCCESS = "0000"; 
	
	public static String ORDER_STATUS00 = "00";
	public static String ORDER_STATUS07 = "07";
	public static String ORDER_STATUS11 = "11";
	public static String ORDER_STATUS22 = "22";
	public static String ORDER_STATUS04 = "04";
	
	private String refundseq;	//退票流水
	private String orderid; // 订单号
	private String cpid; // 车票号
	private String accountname; // 帐号
	private String accountpwd;// 密码
	private String trainno; // 车次
	private String fromstation;// 出发站
	private String arrivestation;// 到达站
	private String fromtime; // 发车时间
	private String alterfromtime; // 改签后发车时间
	private String traveltime; // 出发日期
	private String buymoney; // 票价
	private String refundmoney; // 退款金额
	private String username; // 乘客姓名
	private String tickettype; // 车票类型 成人票 儿童票
	private String idstype; // 证件类型
	private String userids; // 证件号
	private String seattype; // 坐席
	private String trainbox; // 车厢号
	private String seatno; // 座位号
	private String outticketbillno; // 12306订单号
	private String outtickettime;//出票时间
	private String altertrainno; // 要改签的车次
	private String alterseattype; // 要改签的席位
	private String altertraveltime; // 要改签的发车日期
	private String channel; // 渠道
	private String backurl; // 回调地址
	private String refund12306seq;	//退票流水号
	private String userremark;	//用户备注
	private String refundpercent;	//手续费百分比
	private String refundtype;	//退票类型
	
	public String getRefundtype() {
		return refundtype;
	}

	public void setRefundtype(String refundtype) {
		this.refundtype = refundtype;
	}

	public String getRefundseq() {
		return refundseq;
	}

	public void setRefundseq(String refundseq) {
		this.refundseq = refundseq;
	}

	public String getUserremark() {
		return userremark;
	}

	public void setUserremark(String userremark) {
		this.userremark = userremark;
	}

	public String getRefundpercent() {
		return refundpercent;
	}

	public void setRefundpercent(String refundpercent) {
		this.refundpercent = refundpercent;
	}

	public String getAccountname() {
		return accountname;
	}

	public void setAccountname(String accountname) {
		this.accountname = accountname;
	}

	public String getAccountpwd() {
		return accountpwd;
	}

	public void setAccountpwd(String accountpwd) {
		this.accountpwd = accountpwd;
	}

	public String getAlterfromtime() {
		return alterfromtime;
	}

	public void setAlterfromtime(String alterfromtime) {
		this.alterfromtime = alterfromtime;
	}

	public String getRefund12306seq() {
		return refund12306seq;
	}

	public void setRefund12306seq(String refund12306seq) {
		this.refund12306seq = refund12306seq;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getCpid() {
		return cpid;
	}

	public void setCpid(String cpid) {
		this.cpid = cpid;
	}

	public String getTrainno() {
		return trainno;
	}

	public void setTrainno(String trainno) {
		this.trainno = trainno;
	}

	public String getFromstation() {
		return fromstation;
	}

	public void setFromstation(String fromstation) {
		this.fromstation = fromstation;
	}

	public String getArrivestation() {
		return arrivestation;
	}

	public void setArrivestation(String arrivestation) {
		this.arrivestation = arrivestation;
	}

	public String getFromtime() {
		return fromtime;
	}

	public void setFromtime(String fromtime) {
		this.fromtime = fromtime;
	}

	public String getTraveltime() {
		return traveltime;
	}

	public void setTraveltime(String traveltime) {
		this.traveltime = traveltime;
	}

	public String getBuymoney() {
		return buymoney;
	}

	public void setBuymoney(String buymoney) {
		this.buymoney = buymoney;
	}

	public String getRefundmoney() {
		return refundmoney;
	}

	public void setRefundmoney(String refundmoney) {
		this.refundmoney = refundmoney;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTickettype() {
		return tickettype;
	}

	public void setTickettype(String tickettype) {
		this.tickettype = tickettype;
	}

	public String getIdstype() {
		return idstype;
	}

	public void setIdstype(String idstype) {
		this.idstype = idstype;
	}

	public String getUserids() {
		return userids;
	}

	public void setUserids(String userids) {
		this.userids = userids;
	}

	public String getSeattype() {
		return seattype;
	}

	public void setSeattype(String seattype) {
		this.seattype = seattype;
	}

	public String getTrainbox() {
		return trainbox;
	}

	public void setTrainbox(String trainbox) {
		this.trainbox = trainbox;
	}

	public String getSeatno() {
		return seatno;
	}

	public void setSeatno(String seatno) {
		this.seatno = seatno;
	}

	public String getOutticketbillno() {
		return outticketbillno;
	}

	public void setOutticketbillno(String outticketbillno) {
		this.outticketbillno = outticketbillno;
	}

	public String getAltertrainno() {
		return altertrainno;
	}

	public void setAltertrainno(String altertrainno) {
		this.altertrainno = altertrainno;
	}

	public String getAlterseattype() {
		return alterseattype;
	}

	public void setAlterseattype(String alterseattype) {
		this.alterseattype = alterseattype;
	}

	public String getAltertraveltime() {
		return altertraveltime;
	}

	public void setAltertraveltime(String altertraveltime) {
		this.altertraveltime = altertraveltime;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getBackurl() {
		return backurl;
	}

	public void setBackurl(String backurl) {
		this.backurl = backurl;
	}
	public String getOuttickettime() {
		return outtickettime;
	}

	public void setOuttickettime(String outtickettime) {
		this.outtickettime = outtickettime;
	}

	//下单返回结果
	public String responstValue(OrderRefund refund, String codeNo) {
		if (StringUtils.equals(codeNo, REFUND_SUCCESS)) {
			return "success|" + this.orderid;

		} else if (StringUtils.equals(codeNo, REFUND_ERROR)) {
			return "failure|" + this.orderid;
		} else {
			return "exception|" + this.orderid;
		}
	}

}
