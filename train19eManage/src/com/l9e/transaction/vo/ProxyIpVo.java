package com.l9e.transaction.vo;

import java.util.Date;
import java.util.List;

/**   
 * @ClassName: ProxyIpVo   
 * @Description: TODO  
 * @author: taokai
 * @date: 2017年6月27日 下午2:53:45
 * @Copyright: 2017 www.19e.cn Inc. All rights reserved. 
 */
public class ProxyIpVo {

	private Integer autoId;
	private String orderId;
	private String ip;
	private String username;
	private String password;
	private Integer port;
	private Date createTime;
	private Date endTime;
	private List<String> ipList;
	private Integer status;
	private String reason;
	public Integer getAutoId() {
		return autoId;
	}
	public void setAutoId(Integer autoId) {
		this.autoId = autoId;
	}
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
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public List<String> getIpList() {
		return ipList;
	}
	public void setIpList(List<String> ipList) {
		this.ipList = ipList;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
}
