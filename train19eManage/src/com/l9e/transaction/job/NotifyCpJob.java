package com.l9e.transaction.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.JobService;
import com.l9e.util.HttpUtil;
import com.l9e.util.UrlFormatUtil;
/**
 * 通知自动流程
 * @author liht
 *
 */
@Component("notifyCpJob")
public class NotifyCpJob {
	
	private static final Logger logger = Logger.getLogger(NotifyCpJob.class);
	@Resource
	private JobService jobService;
	
	private String notify_cp_interface_url;
	
	private String notify_cp_back_url;
	
	@Value("#{propertiesReader[notify_cp_interface_url]}")
	public void setNotify_cp_interface_url(String notify_cp_interface_url) {
		this.notify_cp_interface_url = notify_cp_interface_url;
	}
	
	@Value("#{propertiesReader[notify_cp_back_url]}")
	public void setNotify_cp_back_url(String notify_cp_back_url) {
		this.notify_cp_back_url = notify_cp_back_url;
	}

	public void notifyCp() throws Exception {
		List<Map<String, String>> scanList = jobService.queryScanedOrderList();		
		List<Map<String, String>> cpInfoList = null;
		
		Map<String, String> paramMap = null;

		for (Map<String, String> orderMap : scanList) {
			cpInfoList = jobService.queryCpInfoList(orderMap.get("order_id"));
			
			paramMap = new HashMap<String, String>();
			paramMap.put("orderid", orderMap.get("order_id"));
			paramMap.put("ordername", orderMap.get("order_name"));
			paramMap.put("paymoney", orderMap.get("ticket_pay_money"));//车票总价
			paramMap.put("trainno", orderMap.get("train_no"));
			paramMap.put("fromcity", orderMap.get("from_city"));
			paramMap.put("tocity", orderMap.get("to_city"));
			paramMap.put("fromtime", orderMap.get("from_time"));
			paramMap.put("totime", orderMap.get("to_time"));
			paramMap.put("traveltime", orderMap.get("travel_time"));
			paramMap.put("seattype", orderMap.get("seat_type"));
			paramMap.put("outtickettype", orderMap.get("out_ticket_type"));
			
			StringBuffer sb = new StringBuffer();
			for (Map<String, String> cpInfo : cpInfoList) {
				if(sb.length()>0) {
					sb.append("#");
				}
				sb.append(cpInfo.get("cp_id")).append("|").append(cpInfo.get("user_name")).append("|")
				  .append(cpInfo.get("ticket_type")).append("|").append(cpInfo.get("ids_type")).append("|")
				  .append(cpInfo.get("user_ids")).append("|").append(cpInfo.get("seat_type")).append("|")
				  .append(cpInfo.get("pay_money"));
			}
			paramMap.put("seattrains", sb.toString());
			paramMap.put("backurl", notify_cp_back_url);

			String params = UrlFormatUtil.CreateUrl("", paramMap, "", "UTF-8");
			
			String result = HttpUtil.sendByPost(notify_cp_interface_url, params, "UTF-8");
			logger.info("请求通知出票接口返回：" + result);
			
			if(!StringUtils.isEmpty(result)){
				String[] results = result.trim().split("\\|");
				
				if("success".equalsIgnoreCase(results[0]) && results.length == 2
						&& orderMap.get("order_id").equals(results[1])){//通知成功
					Map<String, String> reMap = new HashMap<String, String>();
					reMap.put("timeout", "2");
					reMap.put("order_id", results[1]);
					
					jobService.updateScanInfoById(reMap);
				}
			}

		}

	}

}
