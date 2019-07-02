package com.l9e.transaction.job;

import org.springframework.stereotype.Service;

import com.l9e.common.MqOrderBase;

@Service("qunartime")
public class Qunartime   extends MqOrderBase {


	@Override
	public Integer sec() {
		return 100;
	}

	@Override
	public Integer waitsec() {
		return 800;
	}

	@Override
	public String channel() {
		return "qunar";
	}

	@Override
	public Integer getNum() {
		return 20;
	}

	@Override
	public Integer goNum() {
		return 70;
	}

	@Override
	public Integer sqltime() {
		return 100;
	}

}
