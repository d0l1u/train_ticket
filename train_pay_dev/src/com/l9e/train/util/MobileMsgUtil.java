package com.l9e.train.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * 短信接口
 * @author zhangjun
 *
 */
public class MobileMsgUtil {
	
	private static final Logger logger = Logger.getLogger(MobileMsgUtil.class);
	
	private String msg_url = "http://10.3.12.95:18033/main/insertPhone.do";
	
	public void send(String phoneNo, String content){
		String channel = "2";
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("telephone", phoneNo);
		if("2".equals(channel)){
			paramMap.put("content", content.replace(" ", "")+"【19易】");
		}else if("3".equals(channel)){
			paramMap.put("content", "【19旅行】"+content.replace(" ", ""));
		}else{
			paramMap.put("content", content.replace(" ", ""));
		}
		paramMap.put("phone_name", "pay_dev");//发送短信电话简称
		paramMap.put("phone_channel", channel);//发送短信的渠道
		paramMap.put("source_channel", "pay_dev");//短信来源的渠道
		paramMap.put("msg_type", "22");//一小时最多通知一次
		
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
