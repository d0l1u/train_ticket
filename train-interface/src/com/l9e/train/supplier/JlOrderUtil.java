package com.l9e.train.supplier;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.l9e.train.supplier.po.JlOrder;
import com.l9e.train.util.MemcachedUtil;


public class JlOrderUtil {

	private Logger logger=Logger.getLogger(this.getClass());
	private String verifycode;
	private Map<String, String> key;
	
	
	public String verifyCode(){
		
		return verifycode;
	}
	
	public JlOrderUtil() {
		// TODO Auto-generated constructor stub
		key = new HashMap<String, String>();

		key.put("orderId", "0011");
		key.put("payMoney", "0013");
		key.put("trainNo", "0014");
		key.put("fromCity", "0015");
		key.put("toCity", "0016");
		key.put("fromTime", "0017");
		key.put("toTime", "0018");
		key.put("travelTime", "0019");
		key.put("seatType", "0021");
		key.put("seatTrains", "0020");
		key.put("outTicketType", "0022");
		key.put("channel", "0023");
		key.put("seatTypeAccept", "0024");
		key.put("level", "0027");
		key.put("ispay", "0028");
		key.put("manualOrder", "0029");
		key.put("waitForOrder", "0030");
		verifycode = "0000";
	}

	public JlOrder verifyRequest(HttpServletRequest request){
		// TODO Auto-generated method stub
		Enumeration<String> names =  request.getParameterNames();
		String robJson = request.getParameter("json");
		JSONObject obj = JSON.parseObject(robJson);
		String string = obj.getString("orderId");
		logger.info("接收抢票订单JSON-"+string+"-存入memecache,key 为单号");
		MemcachedUtil.getInstance().setAttribute(string, obj,0);
		JlOrder parseObject = null;
		try {
			parseObject = JSON.parseObject(robJson, JlOrder.class);
			logger.info("接收抢票订单JSON-"+string+"JSON 解析 JlOrder 成功");
		} catch (Exception e) {
			logger.error("19e前台-抢票模块-传入JSON解析 JlOrder 错误");
			verifycode = "1111";
			e.printStackTrace();
		}
		return parseObject;
	}
}
