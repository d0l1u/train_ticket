package com.l9e.transaction.job;



import com.l9e.common.AccountBase;

//@Component("setTongchengAccount")
public class SetTongchengAccount extends AccountBase {

	@Override
	public Integer accountlownum() {
		return 3;
	}

	@Override
	public String channel() {
		return "tongcheng";
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
