package com.l9e.util.hcpjar.bean;

public class TicketDataBean {
	TrainBean[] datas;
	String flag;
	String searchDate;
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
