package com.l9e.transaction.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 抢单历史记录
 * 
 * @author yangwei01
 */
public class RobTicket_History implements Serializable {
	private static final long serialVersionUID = 5216518842239652550L;

	private Integer historyId;

	private String orderId;

	private String orderOptlog;

	private Date createTime;

	private String opter;

	public Integer getHistoryId() {
		return historyId;
	}

	public void setHistoryId(Integer historyId) {
		this.historyId = historyId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId == null ? null : orderId.trim();
	}

	public String getOrderOptlog() {
		return orderOptlog;
	}

	public void setOrderOptlog(String orderOptlog) {
		this.orderOptlog = orderOptlog == null ? null : orderOptlog.trim();
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getOpter() {
		return opter;
	}

	public void setOpter(String opter) {
		this.opter = opter == null ? null : opter.trim();
	}

}
