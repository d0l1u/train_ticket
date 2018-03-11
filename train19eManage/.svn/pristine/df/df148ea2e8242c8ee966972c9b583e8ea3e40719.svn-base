package com.l9e.transaction.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.RemindService;
import com.l9e.transaction.service.TrainSystemSettingService;
import com.l9e.util.MemcachedUtil;

/**
 * 页面提醒
 * @author zhangjc
 *
 */
@Controller
@RequestMapping("/remind")
public class RemindController extends BaseController {
	
	private static final Logger logger = Logger.getLogger(RemindController.class);
	@Resource
	private RemindService remindService;
	
	@Resource
	private TrainSystemSettingService trainSystemSettingService;
	
	@RequestMapping("/queryRemindList.do")
	public String queryRemindList(HttpServletRequest request,HttpServletResponse response){
		
		return "remind/remindList";
	}
	/**
	 * 查询未退款，通知失败，保险，机器人等
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryCount.do")
	public void queryCount(HttpServletRequest request,HttpServletResponse response){
		/* 查询数据*/
		Map<String, String> map = this.getCountsValue("count");
		String l9eCount = map.get("l9eCount");//19e未退款
		String qunarCount =  map.get("qunarCount");//去哪未退款
		String extCount = map.get("extCount");//对外商户未退款
		String B2CCount = map.get("B2CCount");//B2C未退款
		String innerCount= map.get("innerCount");//内嵌未退款
		String elongCount= map.get("elongCount");//艺龙未退款
		String tcCount= map.get("tcCount");//同程未退款
		String complain= map.get("complain");//投诉
		String bxCount = map.get("bxCount");//未发送保险
		String bookExtCount =  map.get("bookExtCount");//商户预订通知失败
		String bookQunarCount =  map.get("bookQunarCount");//去哪预订通知失败
		String bookElongCount =  map.get("bookElongCount");//艺龙预订通知失败
		String bookTcCount =  map.get("bookTcCount");//同程预订通知失败
		
		String bookGtCount =  map.get("bookGtCount");//高铁预订通知失败
		String bookMtCount =  map.get("bookMtCount");//美团预订通知失败
		String bookTuniuCount =  map.get("bookTuniuCount");//途牛预订通知失败
		
		String refundExtCount =  map.get("refundExtCount");//商户退款通知失败
		String refundQunarCount =  map.get("refundQunarCount");//去哪退款通知失败
		String refundElongCount =  map.get("refundElongCount");//艺龙退款通知失败
		String refundTcCount =  map.get("refundTcCount");//同程退款通知失败
		
		String alterCount = map.get("alterCount");//未改签
		/* 创建JSON对象*/
		JSONObject jsonObj = new JSONObject();  
		
		/* 给JSON赋值 */
		//未退款数,保险和投诉
		jsonObj.put("l9eCount", l9eCount);
		jsonObj.put("qunarCount", qunarCount);
		jsonObj.put("extCount", extCount);
		jsonObj.put("B2CCount", B2CCount);
		jsonObj.put("innerCount", innerCount);
		jsonObj.put("elongCount", elongCount);
		jsonObj.put("tcCount", tcCount);
		jsonObj.put("complain", complain);
		jsonObj.put("bxCount", bxCount);
		
		jsonObj.put("alterCount", alterCount);
		//机器人启动数
		Map<String, String>  robotMap = this.getCountsValue("robotcount");//查询当前运行的机器人数
		
		jsonObj.put("payCount",(robotMap.get("3")!=null)?robotMap.get("3"):"0");
		jsonObj.put("bookCount",(robotMap.get("1")!=null)?robotMap.get("1"):"0");
		jsonObj.put("refundCount",(robotMap.get("8")!=null)?robotMap.get("8"):"0");
		jsonObj.put("endorseCount",(robotMap.get("7")!=null)?robotMap.get("7"):"0");
		jsonObj.put("cancelCount",(robotMap.get("6")!=null)?robotMap.get("6"):"0");
		jsonObj.put("checkCount",(robotMap.get("5")!=null)?robotMap.get("5"):"0");
		jsonObj.put("registerCount",(robotMap.get("10")!=null)?robotMap.get("10"):"0");
		jsonObj.put("queryCount",(robotMap.get("9")!=null)?robotMap.get("9"):"0");
		jsonObj.put("moneyCount",(robotMap.get("11")!=null)?robotMap.get("11"):"0");
		jsonObj.put("deleteCount",(robotMap.get("13")!=null)?robotMap.get("13"):"0");
		jsonObj.put("enrollCount",(robotMap.get("14")!=null)?robotMap.get("14"):"0");
		jsonObj.put("activateCount",(robotMap.get("15")!=null)?robotMap.get("15"):"0");
		//通知失败
		jsonObj.put("bookExtCount",bookExtCount); 
		jsonObj.put("bookQunarCount",bookQunarCount);  
		jsonObj.put("bookElongCount",bookElongCount);  
		jsonObj.put("bookTcCount",bookTcCount);
		
		jsonObj.put("bookGtCount",bookGtCount);
		jsonObj.put("bookMtCount",bookMtCount);
		jsonObj.put("bookTuniuCount",bookTuniuCount);
		
		jsonObj.put("refundExtCount",refundExtCount);  
		jsonObj.put("refundQunarCount",refundQunarCount);  
		jsonObj.put("refundElongCount",refundElongCount);  
		jsonObj.put("refundTcCount",refundTcCount);  
		try {
			response.getWriter().write(jsonObj.toString());
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 查询打码情况
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryCodeCount.do")
	public void queryCodeCount(HttpServletRequest request,HttpServletResponse response){
		//当前打码人数
		int totalPerson = Integer.valueOf(this.getCodeCountsValue("totalPerson", "totalPerson"));
		//当天打码总数
		int codeCountToday = Integer.valueOf(this.getCodeCountsValue("codeCountToday", "codeCountToday"));
		//当天打码正确总数
		int codeSuccessToday = Integer.valueOf(this.getCodeCountsValue("codeSuccessToday", "codeSuccessToday"));
		//我们未打码数
		int uncodeCount = Integer.valueOf(this.getCodeCountsValue("uncodeCount", "uncodeCount"));
		//去哪未打码数
		int uncodeQunarCount =Integer.valueOf(this.getCodeCountsValue("uncodeQunarCount", "uncodeQunarCount"));
		//同程未打码数
//		int uncodeTcCount = Integer.valueOf(this.getCodeCountsValue("uncodeTcCount", "uncodeTcCount"));
		//去哪打码方式
		String codeQunarType = this.getCodeCountsValue("codeQunarType", "codeQunarType");
		
		/****###################new ###################***/
		//打码部门：11打码团队1、22打码团队2、33打码团队3、99打打码团队其他
		int waitCodeCount01 = this.getCodeCountsValueNew("waitCodeCount01", "waitCodeCount01");
		// loginService.queryWaitCodeQueenCount("5");//当前打码团队01未打码总数
		int waitCodeCount02 = this.getCodeCountsValueNew("waitCodeCount02", "waitCodeCount02");
		//loginService.queryWaitCodeQueenCount("00");//当前打码团队02未打码总数
		int waitCodeCount03 = this.getCodeCountsValueNew("waitCodeCount03", "waitCodeCount03");
		//oginService.queryWaitCodeQueenCount("4");//当前打码团队03未打码总数
		int waitCodeCount04 = this.getCodeCountsValueNew("waitCodeCount04", "waitCodeCount04");
		//loginService.queryWaitCodeQueenCount("99");//当前打码团队04未打码总数
		
		int totalCount01=0, totalCount02=0, totalCount03=0, totalCount04=0;
		Map<String,Object>paramMap1 = new HashMap<String,Object>();
		//1：客服部、2：运营部、3：研发部、4：其他部门、5：对外部门、00：对外部门02、6：同程部门、7：机票部门、8：代理商部门、9：艺龙部门
		paramMap1.put("department", "5");
		totalCount01 = this.getCodeCountsValueNew("totalCount01", "totalCount01");//loginService.queryAdminCurrentNameCount(paramMap1);
		paramMap1.put("department", "00");
		totalCount02 = this.getCodeCountsValueNew("totalCount02", "totalCount02");//loginService.queryAdminCurrentNameCount(paramMap1);
		paramMap1.put("department", "4");
		totalCount03 = this.getCodeCountsValueNew("totalCount03", "totalCount03");//loginService.queryAdminCurrentNameCount(paramMap1);
		paramMap1.put("department", "99");
		totalCount04 = this.getCodeCountsValueNew("totalCount04", "totalCount04");//loginService.queryAdminCurrentOtherCount(paramMap1);
		
		/****######################################***/
		
		JSONObject jsonObj = new JSONObject();    
		 jsonObj.put("totalPerson", totalPerson); //当前打码人数   
		 jsonObj.put("codeCountToday", codeCountToday);  //当天打  码总数  
		 jsonObj.put("codeSuccessToday", codeSuccessToday); //当天打码正确总数
		 jsonObj.put("uncodeCount", uncodeCount);//当前我们未打码数
		 jsonObj.put("uncodeQunarCount", uncodeQunarCount);//当前去哪未打码数
//		 jsonObj.put("uncodeTcCount", uncodeTcCount);//当前同事未打码数
		 jsonObj.put("codeQunarType", codeQunarType.trim());//去哪打码方式
		 
		 jsonObj.put("waitCodeCount01", waitCodeCount01);
		 jsonObj.put("waitCodeCount02", waitCodeCount02);
		 jsonObj.put("waitCodeCount03", waitCodeCount03);
		 jsonObj.put("waitCodeCount04", waitCodeCount04);
		 jsonObj.put("totalCount01", totalCount01);
		 jsonObj.put("totalCount02", totalCount02);
		 jsonObj.put("totalCount03", totalCount03);
		 jsonObj.put("totalCount04", totalCount04);
		 
		try {
			response.getWriter().write(jsonObj.toString());
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * 查询账号核验状态
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryAccountCount.do")
	public void queryAccountCount(HttpServletRequest request,HttpServletResponse response){
		List<Map<String, String>> accountlist ;
		if(null == MemcachedUtil.getInstance().getAttribute("accountlist")){
			accountlist = remindService.queryAccountList();
			MemcachedUtil.getInstance().setAttribute("accountlist", accountlist, 5*60*1000);
		}else{
			accountlist = (List<Map<String, String>>) MemcachedUtil.getInstance().getAttribute("accountlist");
		}
		Map<String, String>  accountMap = new HashMap<String, String>();
		for(int i=0;i<accountlist.size();i++){
			accountMap.put(String.valueOf(accountlist.get(i).get("status")), String.valueOf(accountlist.get(i).get("num")));
		}
//		System.out.println("accountlist"+accountlist.toString());
//		System.out.println("accountMap"+accountMap.toString());
		JSONObject jsonObj = new JSONObject();    
		 jsonObj.put("status_00",  accountMap.get("00")!=null?accountMap.get("00"):"0");
		 jsonObj.put("status_01",  accountMap.get("01")!=null?accountMap.get("01"):"0");
		 jsonObj.put("status_02",  accountMap.get("02")!=null?accountMap.get("02"):"0");
		 jsonObj.put("status_22",  accountMap.get("22")!=null?accountMap.get("22"):"0");
		 jsonObj.put("status_33",  accountMap.get("33")!=null?accountMap.get("33"):"0");
		 jsonObj.put("status_34",  accountMap.get("34")!=null?accountMap.get("34"):"0");
		 try {
				response.getWriter().write(jsonObj.toString());
				response.getWriter().flush();
				response.getWriter().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	/**
	 * @param name
	 * @param key
	 * @return  Map<String, String> 
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getCountsValue(String key){
		Map<String, String> map= new HashMap<String, String>();
		if(null == MemcachedUtil.getInstance().getAttribute(key)){
			if("count".equals(key)){
				map = remindService.queryCounts();
			}else if("robotcount".equals(key)){
				map = remindService.queryRobotCounts();
			}
			MemcachedUtil.getInstance().setAttribute(key, map, 60*1000);
		}else{
			map = (Map<String, String>)MemcachedUtil.getInstance().getAttribute(key);
		}
		return map;
	}
	
	/**
	 * @param name
	 * @param key
	 * @return String
	 */
	public String getCodeCountsValue(String name, String key){
		String value = null;
		if(null == MemcachedUtil.getInstance().getAttribute(key)){
			
			int count=0;String 	channel="";
			if(name.equals("codeCountToday")){
				count=remindService.queryCodeCountToday();
				value = String.valueOf(count).trim();
				MemcachedUtil.getInstance().setAttribute(key, value, 5*60*1000);
			}else if(name.equals("codeSuccessToday")){
				count=remindService.queryCodeToday();
				value = String.valueOf(count).trim();
				MemcachedUtil.getInstance().setAttribute(key, value, 5*60*1000);
			}else if(name.equals("totalPerson")){
				count=remindService.queryAdminCurrentNameCount();
				value = String.valueOf(count).trim();
				MemcachedUtil.getInstance().setAttribute(key, value, 60*1000);
			}else if(name.equals("uncodeCount")){
				channel="19e";
				count=remindService.queryUncode(channel);
				value = String.valueOf(count).trim();
				MemcachedUtil.getInstance().setAttribute(key, value, 60*1000);
			}else if(name.equals("uncodeQunarCount")){
				channel="qunar";
				count=remindService.queryUncode(channel);
				value = String.valueOf(count).trim();
				MemcachedUtil.getInstance().setAttribute(key, value, 60*1000);
			}else if(name.equals("uncodeTcCount")){
				channel="tongcheng";
				count=remindService.queryUncode(channel);
				value = String.valueOf(count).trim();
				MemcachedUtil.getInstance().setAttribute(key, value, 60*1000);
			}else if(name.equals("codeQunarType")){
				value = remindService.codeQunarType();
				MemcachedUtil.getInstance().setAttribute(key, value, 60*1000);
			}
			

		}else{
			value = (String) MemcachedUtil.getInstance().getAttribute(key);
		}
		return value;
	}
	
	
	/**
	 * @param name
	 * @param key
	 * @return String
	 */
	public int getCodeCountsValueNew(String name, String key){
		int count=0;String department="";
		Map<String,Object>paramMap1 = new HashMap<String,Object>();
		if(null == MemcachedUtil.getInstance().getAttribute(key)){
			if("waitCodeCount01".equals(name)){
				department ="5";
				count=remindService.queryWaitCodeQueenCount(department);
				MemcachedUtil.getInstance().setAttribute(key, count, 60*1000);
			}else if("waitCodeCount02".equals(name)){
				department ="00";
				count=remindService.queryWaitCodeQueenCount(department);
				MemcachedUtil.getInstance().setAttribute(key, count, 60*1000);
			}else if("waitCodeCount03".equals(name)){
				department ="4";
				count=remindService.queryWaitCodeQueenCount(department);
				MemcachedUtil.getInstance().setAttribute(key, count, 60*1000);
			}else if("waitCodeCount04".equals(name)){
				department ="99";
				count=remindService.queryWaitCodeQueenCount(department);
				MemcachedUtil.getInstance().setAttribute(key, count, 60*1000);
			}else if("totalCount01".equals(name)){
				paramMap1.put("department", "5");
				count=remindService.queryAdminCurrentNameCount2(paramMap1);
				MemcachedUtil.getInstance().setAttribute(key, count, 60*1000);
			}else if("totalCount02".equals(name)){
				paramMap1.put("department", "00");
				count=remindService.queryAdminCurrentNameCount2(paramMap1);
				MemcachedUtil.getInstance().setAttribute(key, count, 60*1000);
			}else if("totalCount03".equals(name)){
				paramMap1.put("department", "4");
				count=remindService.queryAdminCurrentNameCount2(paramMap1);
				MemcachedUtil.getInstance().setAttribute(key, count, 60*1000);
			}else if("totalCount04".equals(name)){
				paramMap1.put("department", "99");
				count=remindService.queryAdminCurrentNameCount2(paramMap1);
				MemcachedUtil.getInstance().setAttribute(key, count, 60*1000);
			}

		}else{
			count = (Integer) MemcachedUtil.getInstance().getAttribute(key);
		}
		return count;
	}
	
	
	/**
	 * 查询账号余量
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryAccountMarginCount.do")
	public void queryAccountMarginCount(HttpServletRequest request,HttpServletResponse response){
		
		Map<String, String>  accountMap = new HashMap<String, String>();
		if(null == MemcachedUtil.getInstance().getAttribute("accountMap")){
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			String book_num = String.valueOf(trainSystemSettingService.querySystemRefundAndAlert("robot_app_book_num"));
			String contact_num = String.valueOf(trainSystemSettingService.querySystemRefundAndAlert("contact_num"));
			map.put("book_num", Integer.parseInt(book_num));
			map.put("contact_num", Integer.parseInt(contact_num));
			List<Map<String, String>> accountMargin = remindService.queryAccountMarginList(map);
//			logger.info("accountMargin=="+accountMargin.toString());
			int qitaNumber = 0;
			for(int i=0;i<accountMargin.size();i++){
				if(String.valueOf(accountMargin.get(i).get("channel")).equals("19e")
						|| String.valueOf(accountMargin.get(i).get("channel")).equals("elong")
						|| String.valueOf(accountMargin.get(i).get("channel")).equals("tongcheng")
						|| String.valueOf(accountMargin.get(i).get("channel")).equals("qunar")){
					accountMap.put(String.valueOf(accountMargin.get(i).get("channel")), String.valueOf(accountMargin.get(i).get("number")));
				}else{
					qitaNumber+=Integer.parseInt(String.valueOf(accountMargin.get(i).get("number")));
				}
			}
			accountMap.put("qita",String.valueOf(qitaNumber) );
			MemcachedUtil.getInstance().setAttribute("accountMap", accountMap,60*1000);
		}else{
			accountMap = (Map<String, String>) MemcachedUtil.getInstance().getAttribute("accountMap");
		}
//		logger.info("accountMap=="+accountMap.toString()+"accountMap.get('19e')="+accountMap.get("19e"));
		JSONObject jsonObj = new JSONObject();    
		
		 jsonObj.put("l9eAccount",  accountMap.get("19e")!=null?accountMap.get("19e"):"0");
		 jsonObj.put("elongAccount",  accountMap.get("elong")!=null?accountMap.get("elong"):"0");
		 jsonObj.put("tcAccount",  accountMap.get("tongcheng")!=null?accountMap.get("tongcheng"):"0");
		 jsonObj.put("qunarAccount",  accountMap.get("qunar")!=null?accountMap.get("qunar"):"0");
		 jsonObj.put("qitaAccount",  accountMap.get("qita")!=null?accountMap.get("qita"):"0");
		 try {
				response.getWriter().write(jsonObj.toString());
				response.getWriter().flush();
				response.getWriter().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	/**
	 * 查询账号联系人明细
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryAccountContactCount.do")
	public void queryAccountContactCount(HttpServletRequest request,HttpServletResponse response){
		
		Map<String, String>  contactMap = new HashMap<String, String>();
		if(null == MemcachedUtil.getInstance().getAttribute("contactMap")){
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			String book_num = String.valueOf(trainSystemSettingService.querySystemRefundAndAlert("robot_app_book_num"));
			String contact_num = String.valueOf(trainSystemSettingService.querySystemRefundAndAlert("contact_num"));
			map.put("book_num", Integer.parseInt(book_num));
			map.put("contact_num", Integer.parseInt(contact_num));
			List<Map<String, String>> accountMargin = remindService.queryAccountContactList(map);
//			logger.info("accountMargin=="+accountMargin.toString());
			int qitaNumber1 = 0,qitaNumber2 = 0;
			for(int i=0;i<accountMargin.size();i++){
				if(String.valueOf(accountMargin.get(i).get("channel")).equals("19e")
						|| String.valueOf(accountMargin.get(i).get("channel")).equals("elong")
						|| String.valueOf(accountMargin.get(i).get("channel")).equals("tongcheng")
						|| String.valueOf(accountMargin.get(i).get("channel")).equals("qunar")){
					contactMap.put(accountMargin.get(i).get("channel"), String.valueOf(accountMargin.get(i).get("number1"))+" / "+String.valueOf(accountMargin.get(i).get("number2")));
				}else{
					qitaNumber1+=Integer.parseInt(String.valueOf(accountMargin.get(i).get("number1")));
					qitaNumber2+=Integer.parseInt(String.valueOf(accountMargin.get(i).get("number2")));
				}
			}
			contactMap.put("qita",qitaNumber1+" / "+qitaNumber2 );
			MemcachedUtil.getInstance().setAttribute("contactMap", contactMap,60*1000);
		}else{
			contactMap = (Map<String, String>) MemcachedUtil.getInstance().getAttribute("contactMap");
		}
//		logger.info("contactMap=="+contactMap.toString()+"contactMap.get('19e')="+contactMap.get("19e"));
		JSONObject jsonObj = new JSONObject();    
		
		 jsonObj.put("l9eContact",  contactMap.get("19e")!=null?contactMap.get("19e"):"0");
		 jsonObj.put("elongContact",  contactMap.get("elong")!=null?contactMap.get("elong"):"0");
		 jsonObj.put("tcContact",  contactMap.get("tongcheng")!=null?contactMap.get("tongcheng"):"0");
		 jsonObj.put("qunarContact",  contactMap.get("qunar")!=null?contactMap.get("qunar"):"0");
		 jsonObj.put("qitaContact",  contactMap.get("qita")!=null?contactMap.get("qita"):"0");
		 try {
				response.getWriter().write(jsonObj.toString());
				response.getWriter().flush();
				response.getWriter().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	
	/**
	 * 查询机器人与分单量 预警
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryRobotAndOrderCount.do")
	public void queryRobotAndOrderCount(HttpServletRequest request,HttpServletResponse response){
		
		Map<String, String>  robotAndorder = new HashMap<String, String>();
		if(null == MemcachedUtil.getInstance().getAttribute("robotAndorder")){
			String robot_order_remind = String.valueOf(trainSystemSettingService.querySystemRefundAndAlert("robot_order_remind"));
			robotAndorder.put("robot_order_remind", robot_order_remind);
			int orderNumber = remindService.queryOrderNumber();
			int robotNumber_00 = remindService.queryRobotNumber("00");
			int robotNumber_01 = remindService.queryRobotNumber("01");
			int robotNumber_11 = remindService.queryRobotNumber("11");
			robotAndorder.put("order_number", String.valueOf(orderNumber));
			robotAndorder.put("robotNumber_00", String.valueOf(robotNumber_00));
			robotAndorder.put("robotNumber_01", String.valueOf(robotNumber_01));
			robotAndorder.put("robotNumber_11", String.valueOf(robotNumber_11));
			MemcachedUtil.getInstance().setAttribute("robotAndorder", robotAndorder,60*1000);
		}else{
			robotAndorder = (Map<String, String>) MemcachedUtil.getInstance().getAttribute("robotAndorder");
		}
//		logger.info("contactMap=="+contactMap.toString()+"contactMap.get('19e')="+contactMap.get("19e"));
		JSONObject jsonObj = new JSONObject();    
		 jsonObj.put("robotOrderRemind",  robotAndorder.get("robot_order_remind")!=null?robotAndorder.get("robot_order_remind"):"0");
		 jsonObj.put("orderNumber",  robotAndorder.get("order_number")!=null?robotAndorder.get("order_number"):"0");
		 jsonObj.put("robotNumber_00",  robotAndorder.get("robotNumber_00")!=null?robotAndorder.get("robotNumber_00"):"0");
		 jsonObj.put("robotNumber_01",  robotAndorder.get("robotNumber_01")!=null?robotAndorder.get("robotNumber_01"):"0");
		 jsonObj.put("robotNumber_11",  robotAndorder.get("robotNumber_11")!=null?robotAndorder.get("robotNumber_11"):"0");
		 try {
				response.getWriter().write(jsonObj.toString());
				response.getWriter().flush();
				response.getWriter().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	
	
	/**
	 * 查询支付宝余额
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryZhifubaoMoney.do")
	public void queryZhifubaoMoney(HttpServletRequest request,HttpServletResponse response){
		String radioValue = request.getParameter("zhifubaoRadio");
		//System.out.println(radioValue);
		List<Map<String, String>> alipayList = new ArrayList<Map<String,String>>();
		if(null == MemcachedUtil.getInstance().getAttribute("alipayList"+radioValue)){
			alipayList = remindService.queryZhifubaoMoney(radioValue);
			MemcachedUtil.getInstance().setAttribute("alipayList"+radioValue, alipayList,60*1000);
		}else{
			alipayList = (List<Map<String, String>>) MemcachedUtil.getInstance().getAttribute("alipayList"+radioValue);
		}
		 JSONObject jsonObj = new JSONObject();    
//		 logger.info("alipayList:"+alipayList.toString());
		 jsonObj.put("alipayList",  alipayList);
//		 logger.info("jsonObj:"+jsonObj.toString());
		 try {
				response.getWriter().write(jsonObj.toString());
				response.getWriter().flush();
				response.getWriter().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}
