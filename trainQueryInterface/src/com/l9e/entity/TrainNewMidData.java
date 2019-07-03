package com.l9e.entity;

import java.util.List;

public class TrainNewMidData {
	private String flag;
	
	private String searchDate;
	
	private List<TrainNewData> datas;
	
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
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

	public List<TrainNewData> getDatas() {
		return datas;
	}

	public void setDatas(List<TrainNewData> datas) {
		this.datas = datas;
	}

}
