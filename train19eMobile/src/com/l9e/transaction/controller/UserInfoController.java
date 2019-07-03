package com.l9e.transaction.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.util.HttpUtil;
import com.l9e.util.UrlFormatUtil;
/**
 * 处理代理商以及联系人信息
 * @author yangchao
 *
 */

@Controller
@RequestMapping("/userInfo")
public class UserInfoController extends BaseController{
	
	private static final Logger logger = Logger.getLogger(UserInfoController.class);
	
	
	@Value("#{propertiesReader[real_name_verify_url]}")
	private String real_name_verify_url;

	
	/**
	 * 身份证核验，直接去12306添加联系人核验
	 * 
	 */
	@RequestMapping("/verify_no.jhtml")
	public void checkUserIdsCard(HttpServletRequest request, 
			HttpServletResponse response) {
		logger.info("开始验证身份证数据！");
		String data = this.getParam(request, "data");
		if(StringUtils.isEmpty(data)){
			logger.error("校验数据data为空！");
			JSONObject returnJson = new JSONObject();
			returnJson.put("return_code","PARAM_EMPTY");
			returnJson.put("message","请求的必填参数不能为空！");
			printJson(response,returnJson.toString());
			return;
		}
		try {
			   logger.info("passengers=" + data.toString());
			   //passengers=[{"user_name":"杨超","cert_no":"522222199007112835","cert_type":"2"},{"user_name":"杨三","cert_no":"110101198001010117","cert_type":"2"}]
		       Map<String, String> maps = new HashMap<String,String>();
		       maps.put("command", "verify");//请求核验用户信息接口
		       maps.put("passengers", data.toString());
		       maps.put("channel", "19e");
		       String reqParams = UrlFormatUtil.CreateUrl("", maps, "", "UTF-8");
		       String result = HttpUtil.sendByPost(real_name_verify_url, reqParams, "UTF-8", 4000, 4000);
		       logger.info("实名制核验接口返回result=" + result);
		       if(result=="" || result.length()==0){
		    	    logger.info("实名制核验接口返回数据为空，以通过核验通过处理！直接成功");
					JSONObject returnJson = new JSONObject();
					returnJson.put("return_code","SUCCESS");
					returnJson.put("message","乘客信息通过核验");
					printJson(response,returnJson.toString());
		    	   return;
		       }
		       //实名制核验接口返回result=[{"cert_no":"522222199007112835","cert_type":"2","check_status":"0","user_name":"杨超","user_type":"0"},{"cert_no":"110101198001010117","cert_type":"2","check_status":"0","user_name":"杨三","user_type":"0"}]
		       JSONArray jarray = JSONArray.fromObject(result.trim());
				List<Map<String,String>> idCard = JSONArray.toList(jarray, new HashMap<String, String>(), new JsonConfig());
				Map<String,Object> map=new HashMap<String,Object>();
				List<Map<String,String>> resultJsonList=new ArrayList<Map<String,String>>();
				if(idCard!=null && idCard.size()>0){
					for(int i=0;i<idCard.size();i++){
						if(idCard.get(i).get("cert_no")!=null && idCard.get(i).get("user_name")!=null && idCard.get(i).get("check_status")!=null){
							String check_status=idCard.get(i).get("check_status").trim();
							String cert_no=idCard.get(i).get("cert_no").trim();
							String user_name=idCard.get(i).get("user_name").trim();
							if("2".equals(check_status)){
								//未通过
								logger.info("身份证数据姓名:"+user_name+" 身份证号:"+cert_no +"--未通过!");
								Map<String,String> resultMap=new HashMap<String,String>();
								resultMap.put("ids_card", cert_no);
								resultMap.put("userName", user_name);
								resultMap.put("failInfo", "未通过");
								resultMap.put("status", "2");
								resultJsonList.add(resultMap);
							}else if("1".equals(check_status)){
								logger.info("身份证数据姓名:"+user_name+" 身份证号:"+cert_no +"--待核验!");
								Map<String,String> resultMap=new HashMap<String,String>();
								resultMap.put("ids_card", cert_no);
								resultMap.put("userName", user_name);
								resultMap.put("failInfo", "待核验");
								resultMap.put("status", "1");
								resultJsonList.add(resultMap);
							}else{
								logger.info("身份证数据姓名:"+user_name+" 身份证号:"+cert_no +"--验证通过!");
							}
							
						}else{
				    	    logger.info("实名制核验接口返回数据数据异常，以通过核验通过处理！直接成功");
							JSONObject returnJson = new JSONObject();
							returnJson.put("return_code","SUCCESS");
							returnJson.put("message","乘客信息通过核验");
							printJson(response,returnJson.toString());
							return;
						}
					}
					if(resultJsonList!=null && resultJsonList.size()>0){
						JSONObject returnJson = new JSONObject();
						returnJson.put("return_code","FAIL");
						returnJson.put("message","乘客信息未通过核验");
						returnJson.put("errorData",resultJsonList);
						printJson(response,returnJson.toString());
						return;
					}else{
			    	    logger.info("乘客信息核验通过！");
						JSONObject returnJson = new JSONObject();
						returnJson.put("return_code","SUCCESS");
						returnJson.put("message","乘客信息通过核验");
						printJson(response,returnJson.toString());
						return;
					}
				}else{
					logger.info("解析返回的json,得到的数据为空，以通过核验通过处理！直接成功");
					JSONObject returnJson = new JSONObject();
					returnJson.put("return_code","SUCCESS");
					returnJson.put("message","乘客信息通过核验");
					printJson(response,returnJson.toString());
					return;
				}
		} catch (Exception e) {
			logger.error("校验数据时出现异常，以通过核验通过处理！！直接成功");
			JSONObject returnJson = new JSONObject();
			returnJson.put("return_code","SUCCESS");
			returnJson.put("message","乘客信息通过核验");
			printJson(response,returnJson.toString());
			e.printStackTrace();
			return;
	  }	
	}
}
