package com.l9e.transaction.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.l9e.common.Consts;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.vo.VerifySuccPassenger;
import com.l9e.util.HttpUtil;

public class VerifyResultThread implements Runnable {
	private static final Logger logger = Logger.getLogger(VerifyResultThread.class);
	
	long start;
	String result;
	List<VerifySuccPassenger> vsps;
	Map<String, Object> countMap;
	OrderService orderService;
	
	public VerifyResultThread(long start, String result, List<VerifySuccPassenger> vsps, Map<String, Object> countMap, OrderService orderService){
		this.start = start;
		this.result = result;
		this.vsps = vsps;
		this.countMap = countMap;
		this.orderService = orderService;
	}
	
	@Override
	public void run() {
		long verify_time = System.currentTimeMillis() - start;
		if(result!=null && result.indexOf("身份信息涉嫌冒用")>-1){
			verify_time = 25000;
			logger.info("核验超时设置verify_time为25000");
		}
		logger.info("verity passengers total take " + verify_time + "ms");
		
		boolean verify_flag = true;
		String message = "";
		for (VerifySuccPassenger vsp : vsps) {
			if (!"1".equalsIgnoreCase(vsp.getVerification_status())) {
				verify_flag = false;
				message = vsp.getPassenger_id_no() + "未通过12306核验";
				break;
			}
		}
		countMap.remove("code");
		countMap.remove("message");
		if (verify_flag) {
			countMap.put("code", "01");
			countMap.put("message", System.currentTimeMillis() - start);
		} else {
			countMap.put("code", "02");
			countMap.put("message", message);
		}
		requestCountServer(countMap);
		
		//插入核验时间统计
		Map<String, String> timeMap = new HashMap<String, String>();
		timeMap.put("verify_time", verify_time+"");
		timeMap.put("cert_no", vsps.get(0).getPassenger_id_no());
		orderService.addVerifyTime(timeMap);
		logger.info("add mt_verify_timeinfo success----" + vsps.get(0).getPassenger_id_no());
			
	}
	
	
	//统计核验数据
	private void requestCountServer(Map<String, Object> map) {
		try {
			JSONObject countJson = new JSONObject();
			countJson.putAll(map);
			String param = "command=count&result=" + countJson.toString();
			//超时时间设置下
			HttpUtil.sendByPost(Consts.VERIFY_COUNT_URL, param, "UTF-8");
		} catch (Exception e) {
			logger.info("核验统计异常"+e);
			e.printStackTrace();
		}
	}
}
