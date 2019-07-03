package com.l9e.transaction.job;

import org.springframework.stereotype.Service;

import com.l9e.common.MqOrderBase;

@Service("gaoTieTime")
public class GaoTietime extends MqOrderBase {

	// @Override
	public String channel() {
		return "301030";
	}

	// @Override
	public Integer sec() {
		return 50;
	}

	// @Override
	public Integer waitsec() {
		return 500;
	}

	// @Override
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
