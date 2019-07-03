package com.l9e.transaction.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.UserInfoService;
/**
 * 处理代理商以及联系人信息
 * @author yangchao
 *
 */

@Controller
@RequestMapping("/userIdsCardInfo")
public class UserInfoController extends BaseController{
	
	private static final Logger logger = Logger.getLogger(UserInfoController.class);
	
	@Resource
	private UserInfoService userInfoService;
	
	@RequestMapping("/checkUserIdsCardInfo.jhtml")
	public void checkUserIdsCard(HttpServletRequest request, 
			HttpServletResponse response) {
			logger.info("开始验证身份证数据！");
			String data = this.getParam(request, "data");
			if(StringUtils.isEmpty(data)){
				logger.error("校验数据data为空！");
				 write2Response(response, "FAIL");
				 return;
			}
			try {
				JSONArray jarray = JSONArray.fromObject(data.trim());
				List<Map<String,Object>> idCard = JSONArray.toList(jarray, new HashMap<String, Object>(), new JsonConfig());
				Map<String,Object> map=new HashMap<String,Object>();
				List<Map<String,String>> resultJsonList=new ArrayList<Map<String,String>>();
				if(idCard!=null && idCard.size()>0){
					for(int i=0;i<idCard.size();i++){
						if(idCard.get(i).get("userIds")!=null && idCard.get(i).get("userName")!=null){
							String idsCard=idCard.get(i).get("userIds").toString().trim();
							String userName=idCard.get(i).get("userName").toString().trim();
							logger.info("姓名："+userName +"  身份证："+idsCard);
							Map<String,String> queryMap=new HashMap<String,String>();
							queryMap.put("user_name", userName);
							queryMap.put("ids_card", idsCard);
							List<Map<String,String>> resultList=userInfoService.getUserIdsCardInfo(queryMap);
							if(resultList!=null && resultList.size()>0){
								String status=resultList.get(0).get("status");
								if("2".equals(status)){
									logger.info("身份证数据姓名:"+userName+" 身份证号:"+idsCard +"--未通过!");
									Map<String,String> resultMap=new HashMap<String,String>();
									resultMap.put("ids_card", idsCard);
									resultMap.put("userName", userName);
									resultMap.put("failInfo", "未通过");
									resultMap.put("status", "2");
									resultJsonList.add(resultMap);
								}else if("1".equals(status)){
									logger.info("身份证数据姓名:"+userName+" 身份证号:"+idsCard +"--待审核!");
									Map<String,String> resultMap=new HashMap<String,String>();
									resultMap.put("ids_card", idsCard);
									resultMap.put("userName", userName);
									resultMap.put("failInfo", "待审核");
									resultMap.put("status", "1");
									resultJsonList.add(resultMap);
								}else{
									logger.info("身份证数据姓名:"+userName+" 身份证号:"+idsCard +"--验证通过!");
								}
							}
						}else{
							logger.error("从页面拿到的身份数据为空!");
							write2Response(response, "FAIL");
							return;
						}
					}
					if(resultJsonList!=null && resultJsonList.size()>0){
						map.put("errorData", resultJsonList);
						JSONObject jsonStr=JSONObject.fromObject(map);
						write2Response(response, jsonStr.toString());
					}else{
						write2Response(response, "SUCCESS");
					}
			 }else{
				 logger.error("从页面拿到的身份数据为空!");
				 write2Response(response, "FAIL");
				 return;
			 }
		} catch (Exception e) {
			logger.info("校验数据时出现异常！");
			 write2Response(response, "FAIL");
			 return;
		}
		
	}
}
