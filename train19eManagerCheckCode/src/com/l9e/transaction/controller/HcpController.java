package com.l9e.transaction.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.l9e.transaction.service.HcpService;
import com.l9e.transaction.service.LoginService;
import com.l9e.util.MemcachedUtil;
@Controller
@RequestMapping("/hcp")
public class HcpController {
	private static final Logger logger = Logger.getLogger(HcpController.class);

	@Resource
	private HcpService hcpService ;
	
//	private String currentCodingInfo;
//	@Value("#{propertiesReader[currentCodingInfo]}")
//	public void setCurrentCodingInfo(String currentCodingInfo) {
//		this.currentCodingInfo = currentCodingInfo;
//	}
	/**
	 * 给火车票提供查看当前打码情况的接口
	 * @param request
	 * @param response
	 * @return
	 */
//	@SuppressWarnings("unchecked")
	@RequestMapping("/queryCurrentCoding.do")
	//@RequestMapping(value="/code")  
	public void queryCurrentCoding(HttpServletRequest request, HttpServletResponse response){
		
		//当前打码人数
		int totalPerson = hcpService.queryAdminCurrentNameCount();
		//当天打码总数
		int codeCountToday = Integer.valueOf(this.getCountsValue("codeCountToday", "codeCountToday"));
		//当天打码正确总数
		int codeSuccessToday = Integer.valueOf(this.getCountsValue("codeSuccessToday", "codeSuccessToday"));
		
		//获取当前日期距07:00:00的分钟数
//		int minutes = hcpService.queryMinutes();
		//前1天打码正确总数
//		int success1 = hcpService.querySuccess1();
		//前2天打码正确总数
//		int success2 = hcpService.querySuccess2();
		//我们未打码数
		String 	channel="19e";
		int uncodeCount = hcpService.queryUncode(channel);
		//去哪未打码数
		channel="qunar";
		int uncodeQunarCount = hcpService.queryUncode(channel);
		//去哪打码方式
		String codeQunarType = hcpService.codeQunarType();
		/*
		//平均每分钟打码成功数
		java.text.DecimalFormat df=new java.text.DecimalFormat("#.##");  
		String curcodePerMin = "";
		if(minutes != 0){
			curcodePerMin =df.format((double)codeSuccessToday/(double)minutes);
		}else{
			curcodePerMin = "0.00";
		}
		
		//历史同时期每分钟打码成功数
		String hiscodePerMin = "";
		if(minutes != 0){
			hiscodePerMin =df.format((double)(success1+success2)/(double)(minutes*2));
		}else{
			hiscodePerMin = "0.00";
		}
		*/
		
		JSONObject jsonObj = new JSONObject();    
		 jsonObj.put("totalPerson", totalPerson); //当前打码人数   
		 jsonObj.put("codeCountToday", codeCountToday);  //当天打  码总数  
		 jsonObj.put("codeSuccessToday", codeSuccessToday); //当天打码正确总数
//		 jsonObj.put("curcodePerMin", curcodePerMin);//平均每分钟打码成功数
//		 jsonObj.put("hiscodePerMin", hiscodePerMin);//历史同时期每分钟打码成功数
		 jsonObj.put("uncodeCount", uncodeCount);//当前我们未打码数
		 jsonObj.put("uncodeQunarCount", uncodeQunarCount);//当前去哪未打码数
		 jsonObj.put("codeQunarType", codeQunarType);//去哪打码方式
		 System.out.println(jsonObj.toString());
		 String result=jsonObj.toString();
		 result = "result=" + result;
//		return jsonObj.toString();    
		 request.setAttribute("result", result);
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		out.write(jsonObj.toString());
	}
	
	/**
	 * 获取刷新页面各参数
	 * @param name
	 * @param key
	 * @return String
	 */
	public String getCountsValue(String name, String key){
		String value = null;
		if(null == MemcachedUtil.getInstance().getAttribute(key)){
			
			int count=0;
			if(name.equals("codeCountToday")){
				count=hcpService.queryCodeCountToday();
			}else if(name.equals("codeSuccessToday")){
				count=hcpService.queryCodeToday();
			} 
			value = String.valueOf(count).trim();
			MemcachedUtil.getInstance().setAttribute(key, value, 5*60*1000);
		}else{
			value = (String) MemcachedUtil.getInstance().getAttribute(key);
		}
		return value;
	}
}
