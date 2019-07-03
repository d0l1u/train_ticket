package com.l9e.transaction.vo;

import java.io.Serializable;

/**
 * 渠道唤醒账号
 * @author zhangjun
 *
 */
public class ChannelAlive implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String channel;
	
	private String alive_num;

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getAlive_num() {
		return alive_num;
	}

	public void setAlive_num(String alive_num) {
		this.alive_num = alive_num;
	}
	

}
