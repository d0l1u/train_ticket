package com.l9e.transaction.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.transaction.dao.CommonDao;
import com.l9e.transaction.dao.PhonePlatDao;
import com.l9e.transaction.vo.Phone;
import com.l9e.util.HttpUtil;
import com.l9e.util.MobileMsgUtil;

/**
 * 短信发送
 * 
 * @author zyx
 * 
 */
@Component("phonePlatJob")
public class PhonePlatJob {

	private static final Logger logger = Logger.getLogger(PhonePlatJob.class);

	@Resource
	private PhonePlatDao phonePlatDao;
	
	@Resource
	private CommonDao commonDao;
	private String COUNT_URL;
	
	public void phone() throws Exception {
		List<Phone> phone_list = phonePlatDao.queryWaitPhoneList();
		MobileMsgUtil msgUtil = new MobileMsgUtil();
		Map<String, Object> countMap = new HashMap<String, Object>();
		for(Phone phone : phone_list){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("old_status", "00");
			map.put("phone_status", "11");
			map.put("phone_id", phone.getPhone_id());
			//更新开始发送
			phonePlatDao.updatePhoneNum(map);
			logger.info("start msg send："+phone.getTelephone());
			String result = msgUtil.send(phone);
			/*
			 * 统计一次发送短信请求
			 */
			countMap.clear();
			countMap.put("channel", "msg");
			countMap.put("source", "phonePlatJob"); // 请求来源，项目名
			countMap.put("type", "05"); // 发送短信
			countMap.put("code", "50"); // 一次请求
			countMap.put("message", "发送一次短信请求");
			requestCountServer(countMap);
			
			
			if("SUCCESS".equals(result)){
				map.put("old_status", "11");
				map.put("phone_status", "22");
				map.put("phone_id", phone.getPhone_id());
				logger.info("msg send success："+phone.getTelephone());
				phonePlatDao.updatePhoneNum(map);
				
				countMap.put("code", "01"); // 一次请求
				countMap.put("message", "向" + phone.getPhone_id() + "成功发送一条短信");
				requestCountServer(countMap);
			}else{
				map.put("fail_reason", result);
				map.put("old_status", "11");
				map.put("phone_status", "33");
				map.put("phone_id", phone.getPhone_id());
				phonePlatDao.updatePhoneNum(map);
				logger.info("msg send fail："+phone.getTelephone());
				countMap.put("code", "02"); // 一次请求
				countMap.put("message", "向" + phone.getPhone_id() + "发送一条短信失败");
				requestCountServer(countMap);
			}
		}
		
	}
	
	private void requestCountServer(Map<String, Object> map) {
		COUNT_URL = commonDao.querySysSettingByKey("count_url");
		JSONObject countJson = new JSONObject();
		countJson.putAll(map);
		String param = "command=count&result=" + countJson.toString();
		HttpUtil.sendByPost(COUNT_URL, param, "UTF-8");
	}

}
