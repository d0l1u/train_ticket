package com.l9e.transaction.job;


import com.l9e.common.AccountBase;

//@Component("setQunarAccount")
public class SetQunarAccount extends AccountBase {

	@Override
	public Integer accountlownum() {
		return 100;
	}

	@Override
	public String channel() {
		return "qunar";
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
		return 0;
	}

}