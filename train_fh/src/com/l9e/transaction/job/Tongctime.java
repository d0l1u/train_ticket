package com.l9e.transaction.job;

import org.springframework.stereotype.Service;

import com.l9e.common.MqOrderBase;

@Service("tongctime")
public class Tongctime extends MqOrderBase {

	//@Override
	public String channel() {
		return "tongcheng";
	}

	//@Override
	public Integer sec() {
		return 50;
	}

	//@Override
	public Integer waitsec() {
		return 500;
	}

	//@Override
	public Integer getNum() {
		return 20;
	}

	@Override
	public Integer goNum() {
		return 90;
	}

	@Override
	public Integer sqltime() {
		return 50;
	}

}
