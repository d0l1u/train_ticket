package com.l9e.util;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import com.l9e.transaction.service.CommonService;

/**
 * 短信接口
 * @author zhangjun
 *
 */
public class MobileMsgUtil {
	
	private static final Logger logger = Logger.getLogger(MobileMsgUtil.class);
	
	@Value("#{propertiesReader[msg_url]}")
	private String msg_url;
	
	@Resource
	protected CommonService commonService;
	
	public void send(String phoneNo, String content){
		//查询短信渠道  1、19e 2、鼎鑫亿动 3、企信通
		String channel = commonService.querySysSettingByKey("mobile_msg_channel");
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("telephone", phoneNo);
		if("2".equals(channel)){
			paramMap.put("content", content.replace(" ", "")+"【19易】");
		}else if("3".equals(channel)){
			paramMap.put("content", "【19旅行】"+content.replace(" ", ""));
		}else{
			paramMap.put("content", content.replace(" ", ""));
		}
		paramMap.put("phone_channel", channel);//发送短信的渠道
		paramMap.put("source_channel", "cmpay");//短信来源的渠道
		
		String params = "";
		try {
			params = UrlFormatUtil.CreateUrl("", paramMap, "", "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("调用短信平台："+msg_url+params);
		HttpUtil.sendByPost(msg_url, params, "utf-8");
	}
	
}
