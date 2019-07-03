package com.l9e.transaction.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.TjInsuranceService;
import com.l9e.util.HttpUtil;
import com.l9e.util.Md5Encrypt;

/**
 * TjInsuranceController
 */
@Controller
@RequestMapping("/tjInsurance")
public class TjInsuranceController extends BaseController{

protected static final Logger logger = Logger.getLogger(TjInsuranceController.class);
	
	@Resource
	private TjInsuranceService tjInsuranceService;
	
	@RequestMapping("/clickNumAdd.jhtml")
	public void clickNumAdd(HttpServletResponse response,HttpServletRequest request){
		logger.info("#start ## clickNumAdd.jhtml ####");
		Calendar theCa = Calendar.getInstance(); 
		theCa.setTime(new Date());  
		Date date = theCa.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String querydate=df.format(date);
		String result = "failure";
		String type = this.getParam(request,"type");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("type", type);
		map.put("date_time", querydate);
	//	System.out.println("type="+type +"#date="+querydate);
		int queryCount = tjInsuranceService.queryclickNum(map);
		if(queryCount == 0){
			tjInsuranceService.clickNumAdd(map);
			result = "success";
		}else{
			int updateCount = tjInsuranceService.clickNumUpdate(map);
			if(updateCount > 0 )result = "success";
		}
		try {
			PrintWriter out = response.getWriter();
			out.write(result);
			out.flush();
			out.close();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/tjInsuranceCode.jhtml")
	public void tjInsuranceCode(HttpServletResponse response,HttpServletRequest request){
		logger.info("#start ## 获取验证码 ####");
		String phone = this.getParam(request,"phone");
		StringBuffer params = new StringBuffer();
		String city="北京";
		params.append("phone=").append(phone).append("&city=").append(city).append("&pageId=").append("3")
		.append("&advPositionId=").append("213");
		String result = "";
		String url = "http://www.dainar.com/hkout/sendValidCode";
		logger.info(url+"?"+params);
		
//		System.setProperty("http.proxySet", "true");   
//		System.setProperty("http.proxyHost", "192.168.65.126");   
//		System.setProperty("http.proxyPort", "3128");  

		String str = url+"?"+params.toString();
		try {
				result = HttpUtil.sendByGet(str,"UTF-8", null, null);
				logger.info("获取验证码 result="+result);
		} catch (Exception e) {
			logger.error("获取验证码异常1",e);
		}
		JSONObject object = JSONObject.fromObject(result);
		try {
			response.getWriter().write(object.toString());
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e){
			logger.error("获取验证码异常2",e);
		}
	}
	
	
	@RequestMapping("/submitInsurance.jhtml")
	public void submitInsurance(HttpServletResponse response,HttpServletRequest request){
		logger.info("#start ## 提交保险信息 ####");
		String reason ="failure";
		String name = this.getParam(request,"name");
		String phone = this.getParam(request,"phone");
		String idCard = this.getParam(request,"idCard");
		StringBuffer params = new StringBuffer();
//		String name ="张俊驰";
//		String phone = "18892257687";
//		String idCard = "120225199004262398";
		String sign = "jipiaogongying" +"fb3d7883a00e7c09c55a99d9162382e6" +phone;
		
		String hmac = Md5Encrypt.getKeyedDigestFor19Pay(sign,"","utf-8");
		
		String url = "http://www.ilovepingan.com/newtank/thirdparty/interface/insure.do";
		params.append(url)
			.append("?channel=jipiaogongying")
			.append("&productCode=pingan_wycx")
			.append("&mobile=").append(phone)
			.append("&name=").append(name)
			.append("&idCard=").append(idCard)
			.append("&birth=&sex=&flightNo=")
			.append("&sign=").append(hmac);
		String result = "";
		logger.info("投保："+params);
		
		String add ="0";
		try {
				result = HttpUtil.sendByGet(params.toString(),"UTF-8", "30000","30000");
				logger.info("提交保险信息  result="+result);
				reason="success";
		} catch (Exception e) {
			logger.error("提交保险异常");
		}
		
//		失败结果  	result={"message":"身份证号与出生日期/性别不得同时为空","status":"1"};
//		        	result={"message":"该手机号在一年之内已领取免费险","status":"2"}；
//		成功结果		result={"policyNo":"","message":"投保成功","status":"0"};
		
		JSONObject object = new JSONObject();
		if(result!= ""){
			object = JSONObject.fromObject(result);
			if("0".equals(String.valueOf(object.get("status"))))add="1";
		}
		try {
			response.getWriter().write(reason);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e){
			logger.error("解析保险参数异常");
		}
		logger.info("add=="+add);
		if(add=="1"){
			Calendar theCa = Calendar.getInstance(); 
			theCa.setTime(new Date());  
			Date date = theCa.getTime();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String querydate=df.format(date);
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("type", "22");
			map.put("date_time", querydate);
			map.put("isSuccess", "11");
			int updateCount = tjInsuranceService.clickNumUpdate(map);
			logger.info("成功记一次："+updateCount);
		}
	}
	

}
