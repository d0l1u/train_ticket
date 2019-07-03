package com.l9e.transaction.job;

import org.springframework.stereotype.Component;

import com.l9e.common.IpInfoBase;
import com.l9e.transaction.vo.IpInfo;


@Component("setCtripIp")
public class SetCtripIp extends IpInfoBase {
	
	@Override
	public Integer type() {
		return IpInfo.TYPE_CTRIP;
	}

	@Override
	public Integer iplownum() {
		return 10;
	}

	@Override
	public Integer sec() {
		return 1000;
	}

	@Override
	public Integer waitsec() {
		return 6;
	}

}
