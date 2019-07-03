package com.l9e.util.hcpjar.bean;
/**
 * @author liuyi02
 * */
public class TicketDataBean {
	TrainBean[] datas;
	String flag;
	String searchDate;
	String message;
	
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public TrainBean[] getDatas() {
		return datas;
	}
	public void setDatas(TrainBean[] datas) {
		this.datas = datas;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getSearchDate() {
		return searchDate;
	}
	public void setSearchDate(String searchDate) {
		this.searchDate = searchDate;
	}
	
	//"flag":true,"searchDate":"2014年11月10号  周一
}
