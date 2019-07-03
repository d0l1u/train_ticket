package com.soservice.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soservice.mina.factory.SessionManagerFactory;
import com.soservice.util.WebUtil;
import com.soservice.util.json.JsonUtil;

/**
 * 提供so调用服务
 * 
 * @author zhangyou
 * 
 */
@Controller
@RequestMapping("/so")
public class SoCallController {
	/**
	 * 日志
	 */
	private static Logger logger = Logger.getLogger(SoCallController.class);

	/**
	 * so调用服务
	 * 
	 * @param request
	 * @param response
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "/soCall.htm")
	@ResponseBody
	public String soCallService(HttpServletRequest request,
			HttpServletResponse response, String data) {
		String result = "";
		String uuid = "";
		Map<String, String> dataMap = null;
		//返回结果
		Map<String,String>  resultMap=new HashMap<String,String>();
		// 获取uuid作为唯一标识
		uuid = UUID.randomUUID().toString();
		logger.info("信息ID" + uuid + "开始调用app端so服务  请求信息      "+data);
		logger.info("请求来源ip+端口"+WebUtil.getRemoteAddr(request)+":"+WebUtil.getRemotePort(request));
		try {
			// 将json转换为Map
			dataMap = JsonUtil.JsonToMap(data);
			StringBuilder messeageBuilder = new StringBuilder(uuid);
			// 拼接发送app的端的信息
			messeageBuilder.append("@@").append(dataMap.get("type")).append(
					"@@").append(dataMap.get("data"));
			result = SessionManagerFactory.getSessionManager().writeMesseage(
					uuid, messeageBuilder.toString());
			logger.info("信息ID" + uuid + "调用app端so服务返回信息" + result);
			String[] results = result.split("@@");
			// 获取返回类型和返回内容
			resultMap.put("code", results[1]);
			resultMap.put("data", results[2]);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("信息ID" + uuid + "调用app端so服务异常" + e.getMessage());
	
			resultMap.put("code", "1");
			resultMap.put("data", e.getMessage());
		}
		result = JsonUtil.objectToJson(resultMap);
		return result;
	}
}
