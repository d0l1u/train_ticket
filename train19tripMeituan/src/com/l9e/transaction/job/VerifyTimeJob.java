package com.l9e.transaction.job;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.CommonService;
import com.l9e.transaction.service.OrderService;
import com.l9e.util.HttpUtil;
import com.l9e.util.UrlFormatUtil;

/**
 * 每隔10分钟统计一次  超过15秒的比率超过3%发短信给18611339439和13522404324
 **/
@Component("verifyTimeJob")
public class VerifyTimeJob {
	
	private static Logger logger=Logger.getLogger(VerifyTimeJob.class);
	
	@Resource
	protected CommonService commonService;
	
	@Resource
	private OrderService orderService;
	
	/**
	 * 10分钟内核验超过15秒的比率
	 * */
	public void queryVerifyTime(){
		String logPre ="[美团核验时间]";
		
		//10分钟内核验超过15秒的比率
		String ratio = orderService.queryVerfiyTimeRatio();
		if("no".equals(ratio)){
			logger.info(logPre+"10分钟内没有乘客核验");
			return;
		}else{
			logger.info(logPre+"10分钟内核验超过15秒的比率ratio="+ratio);
			
			Double ratiod = Double.parseDouble(ratio);
			if(ratiod>3.0){
				String content = "【美团】10分钟内核验超过15秒的比率为"+ratio+"%，请尽快处理！";
				//发短信
//				sendServer("18611339439", content);
				sendServer("13522404324", content);
				
				logger.info(logPre+"超过15秒的比率超过3%，发短信成功");
			}
		}
	}
	
	//入库
	public void send(String phoneNo, String content){
		//查询短信渠道  1、19e 2、鼎鑫亿动 3、企信通
		String channel = commonService.querySysSettingByKey("mobile_msg_channel");
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("telephone", phoneNo);
		paramMap.put("content", content.replace(" ", ""));
		paramMap.put("phone_channel", channel);//发送短信的渠道
		paramMap.put("source_channel", "meituan");//短信来源的渠道
		
		orderService.addPhone(paramMap);
	}
	
	
	//发送短信服务入库
	public void sendServer(String phoneNo, String content){
		String msg_url = "http://10.3.12.95:18033/main/insertPhone.do";
//		String msg_url = "http://localhost:8090/main/insertPhone.do";
		//查询短信渠道  1、19e 2、鼎鑫亿动
		//查询短信渠道  1、19e 2、鼎鑫亿动 3、企信通
		String channel = commonService.querySysSettingByKey("mobile_msg_channel");
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("telephone", phoneNo);
		paramMap.put("content", content.replace(" ", ""));
		paramMap.put("phone_channel", channel);//发送短信的渠道
		paramMap.put("source_channel", "19e");//短信来源的渠道
		String params = "";
		try {
			params = UrlFormatUtil.CreateUrl("", paramMap, "", "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("[美团核验时间]调用短信平台："+msg_url+params);
		HttpUtil.sendByPost(msg_url, params, "utf-8");
	}
	
}
