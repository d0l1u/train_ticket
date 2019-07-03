package com.l9e.transaction.job;

import org.springframework.stereotype.Component;

import com.l9e.common.IpInfoBase;
import com.l9e.transaction.vo.IpInfo;


@Component("setBookIp")
public class SetBookIp extends IpInfoBase {
	
	@Override
	public Integer type() {
		return IpInfo.TYPE_BOOKIP;
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
