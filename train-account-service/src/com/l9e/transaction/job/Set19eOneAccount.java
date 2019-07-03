package com.l9e.transaction.job;


import org.springframework.stereotype.Component;

import com.l9e.common.AccountBase;

@Component("set19eOneAccount")
public class Set19eOneAccount extends AccountBase {

	@Override
	public Integer accountlownum() {
		return 50;
	}

	@Override
	public String channel() {
		return "19e";
	}

	@Override
	public Integer sec() {
		return 1000;
	}

	@Override
	public Integer waitsec() {
		return 2000;
	}

	@Override
	public Integer passengerSize() {
		// TODO Auto-generated method stub
		return 1;
	}

}
