package com.l9e.transaction.qo;

import java.util.Date;
import java.util.List;

/**
 * @ClassName: ProxyIpQO
 * @Description: 代理IP条件查询对象
 * @author: taokai
 * @date: 2017年6月27日 下午6:46:19
 * @Copyright: 2017 www.19e.cn Inc. All rights reserved.
 */
public class ProxyIpQO {
	/** 代理IP所属订单 */
	private String orderId;
	/** IP */
	private String ip;
	
	/** 创建时间区间-begin*/
	private Date beginCreateTime;
	
	/** 创建时间区间-end */
	private Date endCreateTime;
	
	/** 过期时间区间-begin */
	private Date beginEndTime;
	
	/** 过期时间区间-end */
	private Date endEndTime;
	
	private Integer status;
	
	/** 状态集合 */
	private List<Integer> statusList;
	
	private int beginIndex;
	private int endIndex;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Date getBeginCreateTime() {
		return beginCreateTime;
	}

	public void setBeginCreateTime(Date beginCreateTime) {
		this.beginCreateTime = beginCreateTime;
	}

	public Date getEndCreateTime() {
		return endCreateTime;
	}

	public void setEndCreateTime(Date endCreateTime) {
		this.endCreateTime = endCreateTime;
	}

	public Date getBeginEndTime() {
		return beginEndTime;
	}

	public void setBeginEndTime(Date beginEndTime) {
		this.beginEndTime = beginEndTime;
	}

	public Date getEndEndTime() {
		return endEndTime;
	}

	public void setEndEndTime(Date endEndTime) {
		this.endEndTime = endEndTime;
	}

	public List<Integer> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<Integer> statusList) {
		this.statusList = statusList;
	}

	public int getBeginIndex() {
		return beginIndex;
	}

	public void setBeginIndex(int beginIndex) {
		this.beginIndex = beginIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
