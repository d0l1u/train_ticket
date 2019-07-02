package com.train.sytstem.center.test;

import com.train.system.center.util.HttpUtil;
import com.train.system.center.util.MD5Util;

public class CtripDemo {

	public static void main(String[] args) {
		String fromStationName = "上海";
		String toStationName = "重庆";
		String fromDateStr = "2018-11-03";
		String trainCode = "D952";
		// 从携程获取车次信息
		StringBuffer sb = new StringBuffer();
		String timeStamp = Long.toString(System.currentTimeMillis() / 1000);
		sb.append("From=").append(fromStationName)//
				.append("&To=").append(toStationName)//
				.append("&DepartDate=").append(fromDateStr)//
				.append("&TrainNo=").append(trainCode)//
				.append("&User=19e")//
				.append("&timeStamp=").append(timeStamp)//
				.append("&Sign=").append(MD5Util.md5(timeStamp + "7a692b08bb10a9c0681cc54697e8447d", "utf-8"));
		System.out.println(sb.toString());
		String result = new HttpUtil().doHttpPost("http://m.ctrip.com/restapi/soa2/12976/json/SearchS2S", sb.toString(),
				3000, false);
		System.out.println(result);
	}
}
