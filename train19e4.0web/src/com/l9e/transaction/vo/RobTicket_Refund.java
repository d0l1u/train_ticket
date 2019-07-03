package com.l9e.transaction.vo;

import java.io.Serializable;
import java.util.Date;

public class RobTicket_Refund implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6689489283906958125L;
	/** 退款中 */
	public static final String ING = "00";
	/** 成功 */
	public static final String SUCC = "11";
	/** 失败 */
	public static final String FAIL = "22";

	public static final String DB_PREFIX = "ROB_REF";
	private String re_id;
	private String rob_order_id;
	private String eop_order_id;
	private String refund_money;
	private String eop_refund_seq;
	private String refund_status;
	private Date opt_time;
	private String opt_log;

	public String getRe_id() {
		return re_id;
	}

	public void setRe_id(String reId) {
		re_id = reId;
	}

	public String getEop_refund_seq() {
		return eop_refund_seq;
	}

	public void setEop_refund_seq(String eopRefundSeq) {
		eop_refund_seq = eopRefundSeq;
	}

	public String getRob_order_id() {
		return rob_order_id;
	}

	public void setRob_order_id(String robOrderId) {
		rob_order_id = robOrderId;
	}

	public String getEop_order_id() {
		return eop_order_id;
	}

	public void setEop_order_id(String eopOrderId) {
		eop_order_id = eopOrderId;
	}

	public String getRefund_money() {
		return refund_money;
	}

	public void setRefund_money(String refundMoney) {
		refund_money = refundMoney;
	}

	public String getRefund_status() {
		return refund_status;
	}

	public void setRefund_status(String refundStatus) {
		refund_status = refundStatus;
	}

	public Date getOpt_time() {
		return opt_time;
	}

	public void setOpt_time(Date optTime) {
		opt_time = optTime;
	}

	public String getOpt_log() {
		return opt_log;
	}

	public void setOpt_log(String optLog) {
		opt_log = optLog;
	}

}
