package com.l9e.transaction.vo;

import java.io.Serializable;

public class SuitVo implements Serializable{

	/**
	 *  @author liuyi
	 */
	private static final long serialVersionUID = 1L;
	public static final int SUITDAYS=3;//投诉天数
	public static final int SUITCOUNTS=3;//每天投诉次数
	private	String gent_id;
	private String create_time;
	private int suit_id; 
	private int suit_count;
	public int getSuit_count() {
		return suit_count;
	}

	public void setSuit_count(int suitCount) {
		suit_count = suitCount;
	}

	public int getSuit_id() {
		return suit_id;
	}

	public void setSuit_id(int suitId) {
		suit_id = suitId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String createTime) {
		create_time = createTime;
	}


	public String getGent_id() {
		return gent_id;
	}

	public void setGent_id(String gentId) {
		gent_id = gentId;
	}
	
	
}
