package com.l9e.transaction.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
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

import com.jiexun.iface.util.StringUtil;
import com.l9e.common.BaseController;
import com.l9e.common.LoginUserInfo;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.UserInfoService;
import com.l9e.util.HttpUtil;
import com.l9e.util.UrlFormatUtil;
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
	
	@Value("#{propertiesReader[real_name_verify_url]}")
	private String real_name_verify_url;
	
	
	
	/**
	 * 身份证核验，在自己的数据库表核验
	 * 
	 */
//	@RequestMapping("/checkUserIdsCardInfo.jhtml")
//	public void checkUserIdsCard(HttpServletRequest request, 
//			HttpServletResponse response) {
//		logger.info("开始验证身份证数据！");
//		String data = this.getParam(request, "data");
//		if(StringUtils.isEmpty(data)){
//			logger.error("校验数据data为空！");
//			 write2Response(response, "FAIL");
//			 return;
//		}
//		try {
//			JSONArray jarray = JSONArray.fromObject(data.trim());
//			List<Map<String,Object>> idCard = JSONArray.toList(jarray, new HashMap<String, Object>(), new JsonConfig());
//			Map<String,Object> map=new HashMap<String,Object>();
//			List<Map<String,String>> resultJsonList=new ArrayList<Map<String,String>>();
//			if(idCard!=null && idCard.size()>0){
//				for(int i=0;i<idCard.size();i++){
//					if(idCard.get(i).get("userIds")!=null && idCard.get(i).get("userName")!=null){
//						String idsCard=idCard.get(i).get("userIds").toString().trim();
//						String userName=idCard.get(i).get("userName").toString().trim();
//						logger.info("姓名："+userName +"  身份证："+idsCard);
//						Map<String,String> queryMap=new HashMap<String,String>();
//						queryMap.put("user_name", userName);
//						queryMap.put("ids_card", idsCard);
//						List<Map<String,String>> resultList=userInfoService.getUserIdsCardInfo(queryMap);
//						if(resultList!=null && resultList.size()>0){
//							String status=resultList.get(0).get("status");
//							if("2".equals(status)){
//								logger.info("身份证数据姓名:"+userName+" 身份证号:"+idsCard +"--未通过!");
//								Map<String,String> resultMap=new HashMap<String,String>();
//								resultMap.put("ids_card", idsCard);
//								resultMap.put("userName", userName);
//								resultMap.put("failInfo", "未通过");
//								resultMap.put("status", "2");
//								resultJsonList.add(resultMap);
//							}else if("1".equals(status)){
//							
//								//判断待核验是否是一天之内，如果为一天之内，则拒绝购票，一天之前，则可以正常购票
//								Date nowDate=new Date();
//								Calendar calendar = Calendar.getInstance();	
//								calendar.setTime(nowDate);		
//								calendar.add(Calendar.DAY_OF_MONTH, -1);
//								//得到昨天
//								nowDate = calendar.getTime();
//								if(nowDate.before(DateUtil.stringToDate(resultList.get(0).get("update_time"), "yyyy-MM-dd HH:mm:ss"))){
//									logger.info("身份证数据姓名:"+userName+" 身份证号:"+idsCard +"--待审核!");
//									Map<String,String> resultMap=new HashMap<String,String>();
//									resultMap.put("ids_card", idsCard);
//									resultMap.put("userName", userName);
//									resultMap.put("failInfo", "待审核");
//									resultMap.put("status", "1");
//									resultJsonList.add(resultMap);
//								}else{
//									logger.info("用户的待审核数据为一天之前的数据，可以正常购票！");
//								}
//							}else{
//								logger.info("身份证数据姓名:"+userName+" 身份证号:"+idsCard +"--验证通过!");
//							}
//						}
//					}else{
//						logger.error("从页面拿到的身份数据为空1！");
//						write2Response(response, "FAIL");
//						return;
//					}
//				}
//				if(resultJsonList!=null && resultJsonList.size()>0){
//					map.put("errorData", resultJsonList);
//					JSONObject jsonStr=JSONObject.fromObject(map);
//					write2Response(response, jsonStr.toString());
//				}else{
//					write2Response(response, "SUCCESS");
//				}
//		 }else{
//			 logger.error("从页面拿到的身份数据为空2！");
//			 write2Response(response, "FAIL");
//			 return;
//		 }
//	  } catch (Exception e) {
//		logger.error("校验数据时出现异常！");
//		 write2Response(response, "FAIL");
//		 e.printStackTrace();
//		 return;
//	  }	
//	}

	
	/**
	 * 身份证核验，直接去12306添加联系人核验
	 * 
	 */
	@RequestMapping("/checkUserIdsCardInfo.jhtml")
	public void checkUserIdsCard(HttpServletRequest request, 
			HttpServletResponse response) {
		logger.info("开始验证身份证数据！");
		String data = this.getParam(request, "data");
		if(StringUtils.isEmpty(data)){
			logger.error("校验数据data为空！");
			 write2Response(response, "SUCCESS");
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
		       String result = HttpUtil.sendRealNameByPost(real_name_verify_url, reqParams, "UTF-8");
		       logger.info("实名制核验接口返回result=" + result);
		       if(result=="" || result.length()==0){
		    	   write2Response(response, "SUCCESS");
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
								resultMap.put("failInfo", "待审核");
								resultMap.put("status", "1");
								resultJsonList.add(resultMap);
							}else{
								logger.info("身份证数据姓名:"+user_name+" 身份证号:"+cert_no +"--验证通过!");
							}
							
						}else{
							write2Response(response, "SUCCESS");
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
					logger.info("解析返回的json,得到的数据为空！直接返回成功");
					 write2Response(response, "SUCCESS");
					return;
				}
		} catch (Exception e) {
		logger.error("校验数据时出现异常！直接成功");
		 write2Response(response, "SUCCESS");
		 e.printStackTrace();
		 return;
	  }	
	}
	
	/**
	 * 代理商点击我已实名认证
	 * @param request
	 * @param response
	 */
	@RequestMapping("/alreadyAuthenticate.jhtml")
	public void alreadyAuthenticate(HttpServletRequest request, 
			HttpServletResponse response,String data) {
			logger.info("代理商点击我已窗口认证！");
			if(StringUtils.isNotEmpty(data)){
				LoginUserInfo loginUser=(LoginUserInfo) request.getSession().getAttribute(TrainConsts.INF_LOGIN_USER);
				String opt_person=loginUser.getUserId();
				String param[]=data.split("\\&");
				if(param.length==2){
					String user_name=param[0].toString().trim();
					String ids_card=param[1].toString().trim();
					Map<String,String> map=new HashMap<String,String>();
					map.put("ids_card", ids_card);
					map.put("user_name", user_name);
					map.put("regist_status", "44");
					map.put("opt_person", opt_person);
					int update=0;
					update=userInfoService.updateHcUserRegistStatus(map);
					if(update==1){
						logger.info("我已窗口认证成功，更新数据成功!");
						write2Response(response, "SUCCESS");
					}else{
						logger.info("我已实名认证失败，更新数据失败!");
						write2Response(response, "FAIL");
					}
				}else{
					logger.info("我已实名认证失败，姓名或者身份证号为空!");
					write2Response(response, "FAIL");
				}
		   }else{
				logger.info("我已实名认证失败，页面传递过来的数据为空!");
				write2Response(response, "FAIL");
		   }
	
	}
	
	/**
	 * 查询最近中奖的所有代理商信息
	 */
	@RequestMapping("/allAgentWinningInfo.jhtml")
	public String queryAgentWinning(HttpServletRequest request ,HttpServletResponse response){
		List<Map<String,Object>> agentWinningList=userInfoService.queryAgentWinningInfo();
		if(agentWinningList!=null && agentWinningList.size()>0){
			for(int i=0;i<agentWinningList.size();i++){
				if(agentWinningList.get(i).get("dealer_name")!=null ){
					String dealerName=agentWinningList.get(i).get("dealer_name").toString().trim();
					String newDealerName="";
					//判断是否是纯数字
					Pattern pattern = Pattern.compile("[0-9]*");
				    Matcher isNum = pattern.matcher(dealerName);
				    if(!isNum.matches()){
				    	//不是纯数字
				    	if(dealerName.length()>=5){
				    		newDealerName=dealerName.substring(0, 4);
				    		newDealerName=newDealerName+"*";
				    	 }else{
							newDealerName=dealerName.substring(0, dealerName.length()-1);
							newDealerName=newDealerName+"*";
				    	 }
				    }else{
				    	//纯数、 一般手机号
						if(dealerName.length()==11){
							String endName="";
							String startName="";
							startName=dealerName.substring(0, dealerName.length()-8);
							endName=dealerName.substring(7,dealerName.length());
							newDealerName=startName+"****"+endName;
						}else{
							newDealerName=dealerName.substring(0, dealerName.length()-1);
							newDealerName=newDealerName+"*";
						}
					}
					agentWinningList.get(i).put("dealer_name", newDealerName);
			}else{
				continue;
			}
		  }
	   }
		request.setAttribute("agentWinningList", agentWinningList);
	
		return "common/agentWinning";
		
	}
	
	
	/**
	 * 保存为常用乘车人
	 * 
	 */
	@RequestMapping("/saveUserIdsCardInfo.jhtml")
	public void saveUserIdsCardInfo(HttpServletRequest request, 
			HttpServletResponse response) {
		logger.info("开始保存常用乘客信息！");
		String data = this.getParam(request, "data");
		if(StringUtils.isEmpty(data)){
			logger.error("校验数据data为空！");
			return;
		}
		try {
			LoginUserInfo loginUser=this.getLoginUser(request);
			
			String userId = loginUser.getUserId();
			if("D63D1A292A70CA8E".equals(userId)){
				return;
			}
			
			logger.info("passengers=" + data.toString());
			JSONArray pasArr = JSONArray.fromObject(data);
			//查询该代理商目前的常用乘客总数
			Integer sum = userInfoService.queryAgentPassTotalNum(loginUser.getUserId());
			//判断要添加的乘客数，若大于100，则删除多余数量
			int size = pasArr.size();
			if(size+sum>100){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("user_id", loginUser.getUserId());
				map.put("num", size+sum-100);
				userInfoService.deleteAgentPass(map);
			};
			Map<String,String> param = new HashMap<String,String>();
			for(int i=0; i<size;i++){
				param.put("user_id", loginUser.getUserId());
				param.put("user_ids", (String) pasArr.getJSONObject(i).get("card_num"));
				param.put("ids_type", (String) pasArr.getJSONObject(i).get("card_type"));
				param.put("link_name", (String) pasArr.getJSONObject(i).get("user_name"));
				Integer num = userInfoService.queryPassNumByCard(param);
				if(num == 0){
					userInfoService.addAgentPassInfo(param);
				}
			}
		} catch (Exception e) {
			logger.error("校验数据时出现异常！直接成功");
			e.printStackTrace();
	  }	
	}
	
	
	/**
	 * 删除常用乘车人
	 * 
	 */
	@RequestMapping("/deleteUserIdsCardInfo.jhtml")
	public void deleteUserIdsCardInfo(HttpServletRequest request, 
			HttpServletResponse response) {
		logger.info("开始删除常用乘客信息！");
		String data = this.getParam(request, "data");
		if(StringUtils.isEmpty(data)){
			logger.error("校验数据data为空！");
			return;
		}
		try {
			LoginUserInfo loginUser=this.getLoginUser(request);
			System.out.println("---------------------------"+data);
			if(data.contains("& #39;")){
				data.replaceAll("& #39;", "/'");
			}
			logger.info("passengers=" + data.toString());
//			JSONArray pasArr = JSONArray.fromObject(data);
			JSONObject jsonObject = JSONObject.fromObject(data); 
	        Map map = new HashMap(); 
	        for(Iterator iter = jsonObject.keys(); iter.hasNext();){ 
	            String key = (String)iter.next(); 
	            map.put(key, jsonObject.get(key)); 
	        } 
	        System.out.println("要删除的常用乘客信息："+map);
			//查询该代理商目前的常用乘客总数
			Map<String, String> param = new HashMap<String, String>();
			param.put("user_id", loginUser.getUserId());
			param.put("user_ids", map.get("user_ids").toString());
			param.put("ids_type", map.get("ids_type").toString());
			param.put("link_name", map.get("link_name").toString());
			Integer num = userInfoService.queryPassNumByCard(param);
			if(num>0){
				userInfoService.deleteAgentPassInfo(param);
			}
		} catch (Exception e) {
			logger.error("校验数据时出现异常！直接成功");
			e.printStackTrace();
	  }	
	}
	
}
