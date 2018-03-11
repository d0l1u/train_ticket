package com.l9e.transaction.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.TrainSystemSettingService;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.transaction.vo.SystemSettingVo;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.PageUtil;

@Controller
@RequestMapping("/trainSystemSetting")
public class TrainSystemSettingController extends BaseController {

		@Resource
		private TrainSystemSettingService trainSystemSettingService;
		
		@RequestMapping("/getSystemSetting.do")
		public String getSystemSetting(HttpServletRequest request,
				HttpServletResponse response) {
			List<SystemSettingVo> systemSetting =trainSystemSettingService.getSystemSetting();
			List<SystemSettingVo> book_auto_fail =new ArrayList<SystemSettingVo>();
			List<SystemSettingVo> pay_auto_fail =new ArrayList<SystemSettingVo>();
			for(int i=0;i<systemSetting.size();i++){
				if(systemSetting.get(i).getSetting_name().equals("book_auto_fail"))
					book_auto_fail.add(systemSetting.get(i));
				if(systemSetting.get(i).getSetting_name().equals("pay_auto_fail"))
					pay_auto_fail.add(systemSetting.get(i));
			}
			
			String cc=book_auto_fail.get(0).getSetting_value();
			Map<Integer,String> book_auto_fail_value =new HashMap<Integer,String>();
			String[] arr3 = cc.split("@");
			for(int i=0;i<arr3.length;i++)
				book_auto_fail_value.put(10*(i+1)+(i+1), arr3[i]);
			
			String dd=pay_auto_fail.get(0).getSetting_value();
			Map<Integer,String> pay_auto_fail_value =new HashMap<Integer,String>();
			String[] arr4 = dd.split("@");
			for(int i=0;i<arr4.length;i++)
				pay_auto_fail_value.put(10*(i+1)+(i+1), arr4[i]);
			
			
			
			request.setAttribute("systemSetting", systemSetting);
			request.setAttribute("book_auto_fail_value", book_auto_fail_value);
			request.setAttribute("book_auto_fail", book_auto_fail);
			request.setAttribute("pay_auto_fail_value", pay_auto_fail_value);
			request.setAttribute("pay_auto_fail", pay_auto_fail);
			return "trainSystemSetting/trainSettingList";
		}
		
	
		//查看操作日志
		@RequestMapping("/querySystemSetList.do")
		public String querySystemSettingList(HttpServletRequest request, HttpServletResponse response){
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			Map<String,Object>paramMap = new HashMap<String,Object>();
			/******************分页条件开始********************/
			int totalCount = trainSystemSettingService.querySystemSetListCount();
			PageVo page = PageUtil.getInstance().paging(request, 10, totalCount);
			paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
			paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
			/******************操作********************/
			List<Map<String,Object>> systemList = trainSystemSettingService.querySystemSetList(paramMap);
			request.setAttribute("systemList", systemList);
			request.setAttribute("isShowList", 1);
			return "trainSystemSetting/trainSystemSetList";
		}
		
		@RequestMapping("/updateInterfaceChannel.do")
		public String updateInterfaceChannel(HttpServletRequest request,
				HttpServletResponse response) {
			SystemSettingVo systemSettingVo = trainSystemSettingService.getSystemSettingById(request.getParameter("channelId"));
			systemSettingVo.setSetting_value(request.getParameter("channel"));
			Map<String, String> log = new HashMap<String, String>();
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			log.put("content", "切换接口value="+request.getParameter("channel"));
			log.put("opt_person", opt_person);
			trainSystemSettingService.updateInterfaceChannel(systemSettingVo, log);

			return "redirect:/trainSystemSetting/getSystemSetting.do";
		}
		
		@RequestMapping("/updateConTimeout.do")
		public String updateConTimeout(HttpServletRequest request,
				HttpServletResponse response) {
			String setting_id = request.getParameter("conTimeoutId");
			SystemSettingVo systemSettingVo = trainSystemSettingService.getSystemSettingById(setting_id);
			systemSettingVo.setSetting_value(request.getParameter("con_timeout"));
			Map<String, String> log = new HashMap<String, String>();
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			log.put("content", "修改连接超时时间"+request.getParameter("con_timeout"));
			log.put("opt_person", opt_person);
			trainSystemSettingService.updateInterfaceConTimeout(systemSettingVo, log);

			return "redirect:/trainSystemSetting/getSystemSetting.do";
		}
		
		@RequestMapping("/updateReadTimeout.do")
		public String updateReadTimeout(HttpServletRequest request, HttpServletResponse response) {
			SystemSettingVo systemSettingVo = trainSystemSettingService.getSystemSettingById(request.getParameter("readTimeoutId"));
			systemSettingVo.setSetting_value(request.getParameter("read_timeout"));
			Map<String, String> log = new HashMap<String, String>();
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			log.put("content", "修改读超时时间"+request.getParameter("read_timeout"));
			log.put("opt_person", opt_person);
			trainSystemSettingService.updateInterfaceReadTimeout(systemSettingVo, log);

			return "redirect:/trainSystemSetting/getSystemSetting.do";
		}
		
		@RequestMapping("/updateInterface12306RUL.do")
		public String updateInterface12306RUL(HttpServletRequest request, HttpServletResponse response) {
			String urlId = request.getParameter("urlId");
			SystemSettingVo systemSettingVo = trainSystemSettingService.getSystemSettingById(urlId);
			String urlstr = request.getParameter(urlId);
			systemSettingVo.setSetting_value(urlstr);
			
			Map<String, String> log = new HashMap<String, String>();
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			log.put("content", "修改12306接口地址,setting_id="+request.getParameter("urlId")+",value="+urlstr);
			log.put("opt_person", opt_person);
			trainSystemSettingService.updateInterface12306URL(systemSettingVo, log);

			return "redirect:/trainSystemSetting/getSystemSetting.do";
		}
		
		@RequestMapping("/changeURLStatus.do")
		public String changURLStatus(HttpServletRequest request, HttpServletResponse response){
			SystemSettingVo systemSettingVo = trainSystemSettingService.getSystemSettingById(request.getParameter("urlId"));
			if(systemSettingVo.getSetting_status().equals("1")){
				systemSettingVo.setSetting_status("0");
			}else if(systemSettingVo.getSetting_status().equals("0")){
				systemSettingVo.setSetting_status("1");
			}
			
			Map<String, String> log = new HashMap<String, String>();
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			log.put("content", "12306接口地址状态"+systemSettingVo.getSetting_status()+",setting_id="+request.getParameter("urlId"));
			log.put("opt_person", opt_person);
			trainSystemSettingService.updateURLStatus(systemSettingVo, log);

			return "redirect:/trainSystemSetting/getSystemSetting.do";
		}
		
		@RequestMapping("/deleteURL.do")
		public String deleteURL(HttpServletRequest request, HttpServletResponse response){
			SystemSettingVo systemSettingVo = trainSystemSettingService.getSystemSettingById(request.getParameter("urlId"));
			
			Map<String, String> log = new HashMap<String, String>();
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			log.put("content", "删除12306接口地址,setting_id="+request.getParameter("urlId")+",url="+systemSettingVo.getSetting_value());
			log.put("opt_person", opt_person);
			trainSystemSettingService.deleteInterface12306URL(systemSettingVo, log);

			return "redirect:/trainSystemSetting/getSystemSetting.do";
		}
		
		@RequestMapping("/turnToAddURLPage.do")
		public String turnToAddURL(HttpServletRequest request, HttpServletResponse response) {
			return "trainSystemSetting/addURL";
		}
		
		@RequestMapping("/addURL.do")
		public String addURL(HttpServletRequest request, HttpServletResponse response) {
			SystemSettingVo systemSettingVo = new SystemSettingVo();
			systemSettingVo.setSetting_id(CreateIDUtil.createID("SS"));
			systemSettingVo.setSetting_name("INTERFACE_12306_URL");
			systemSettingVo.setSetting_desc("12306接口地址");
			systemSettingVo.setSetting_value(request.getParameter("url"));
			systemSettingVo.setSetting_status("1");
			Map<String, String> log = new HashMap<String, String>();
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			log.put("content", "添加12306接口地址,setting_id="+systemSettingVo.getSetting_id()+",url="+systemSettingVo.getSetting_value());
			log.put("opt_person", opt_person);
			trainSystemSettingService.addSystemLog(log);
			trainSystemSettingService.addInterface12306URL(systemSettingVo, log);

			return "redirect:/trainSystemSetting/getSystemSetting.do";
		}
		
		@RequestMapping("/updateInterface12306NewRUL.do")
		public String updateInterface12306NewRUL(HttpServletRequest request, HttpServletResponse response) {
			String urlId = request.getParameter("urlId");
			SystemSettingVo systemSettingVo = trainSystemSettingService.getSystemSettingById(urlId);
			String urlstr = request.getParameter(urlId);
			systemSettingVo.setSetting_value(urlstr);
			
			Map<String, String> log = new HashMap<String, String>();
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			log.put("content", "修改12306新接口地址,setting_id="+request.getParameter("urlId")+",value="+urlstr);
			log.put("opt_person", opt_person);
			trainSystemSettingService.updateInterface12306URL(systemSettingVo, log);

			return "redirect:/trainSystemSetting/getSystemSetting.do";
		}
		
		@RequestMapping("/changeNewURLStatus.do")
		public String changNewURLStatus(HttpServletRequest request, HttpServletResponse response){
			SystemSettingVo systemSettingVo = trainSystemSettingService.getSystemSettingById(request.getParameter("urlId"));
			if(systemSettingVo.getSetting_status().equals("1")){
				systemSettingVo.setSetting_status("0");
			}else if(systemSettingVo.getSetting_status().equals("0")){
				systemSettingVo.setSetting_status("1");
			}
			
			Map<String, String> log = new HashMap<String, String>();
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			log.put("content", "12306新接口地址状态"+systemSettingVo.getSetting_status()+",setting_id="+request.getParameter("urlId"));
			log.put("opt_person", opt_person);
			trainSystemSettingService.updateURLStatus(systemSettingVo, log);

			return "redirect:/trainSystemSetting/getSystemSetting.do";
		}
		
		@RequestMapping("/deleteNewURL.do")
		public String deleteNewURL(HttpServletRequest request, HttpServletResponse response){
			SystemSettingVo systemSettingVo = trainSystemSettingService.getSystemSettingById(request.getParameter("urlId"));
			
			Map<String, String> log = new HashMap<String, String>();
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			log.put("content", "删除12306新接口地址,setting_id="+request.getParameter("urlId")+",url="+systemSettingVo.getSetting_value());
			log.put("opt_person", opt_person);
			trainSystemSettingService.deleteInterface12306URL(systemSettingVo, log);

			return "redirect:/trainSystemSetting/getSystemSetting.do";
		}
		
		@RequestMapping("/turnToAddNewURLPage.do")
		public String turnToAddNewURL(HttpServletRequest request, HttpServletResponse response) {
			return "trainSystemSetting/addNewURL";
		}
		
		@RequestMapping("/addNewURL.do")
		public String addNewURL(HttpServletRequest request, HttpServletResponse response) {
			SystemSettingVo systemSettingVo = new SystemSettingVo();
			systemSettingVo.setSetting_id(CreateIDUtil.createID("SS"));
			systemSettingVo.setSetting_name("INTERFACE_12306_NEW_URL");
			systemSettingVo.setSetting_desc("12306新接口地址");
			systemSettingVo.setSetting_value(request.getParameter("url"));
			systemSettingVo.setSetting_status("1");
			Map<String, String> log = new HashMap<String, String>();
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			log.put("content", "添加12306新接口地址,setting_id="+systemSettingVo.getSetting_id()+",url="+systemSettingVo.getSetting_value());
			log.put("opt_person", opt_person);
			trainSystemSettingService.addSystemLog(log);
			trainSystemSettingService.addInterface12306URL(systemSettingVo, log);

			return "redirect:/trainSystemSetting/getSystemSetting.do";
		}
		
		@RequestMapping("/turnToAddRiZhiPage.do")
		public String turnToAddRiZhiPage(HttpServletRequest request, HttpServletResponse response) {
			String type = this.getParam(request, "type");
			request.setAttribute("type", type);
			return "trainSystemSetting/addRiZhi";
		}
		@RequestMapping("/addRiZhi.do")
		public String addRiZhi(HttpServletRequest request, HttpServletResponse response) {
			String type = this.getParam(request, "type");
			SystemSettingVo systemSettingVo = new SystemSettingVo();
			systemSettingVo.setSetting_id(CreateIDUtil.createID("SS"));
			String typeName="";
			if(type.equals("1")){
				systemSettingVo.setSetting_name("book_again");
				systemSettingVo.setSetting_desc("预订重发");
				typeName="预订重发";
			}else if(type.equals("2")){
				systemSettingVo.setSetting_name("pay_again");
				systemSettingVo.setSetting_desc("支付重发");
				typeName="支付重发";
			}else if(type.equals("3")){
				systemSettingVo.setSetting_name("book_auto_fail");
				systemSettingVo.setSetting_desc("预订自动失败");
				typeName="预订自动失败";
			}else if(type.equals("4")){
				systemSettingVo.setSetting_name("pay_auto_fail");
				systemSettingVo.setSetting_desc("支付自动失败");
				typeName="支付自动失败";
			}
			
			
			systemSettingVo.setSetting_value(request.getParameter("url"));
			systemSettingVo.setSetting_status("1");
			Map<String, String> log = new HashMap<String, String>();
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			log.put("content", "添加"+typeName+"新日志,setting_id="+systemSettingVo.getSetting_id()+",日志="+systemSettingVo.getSetting_value());
			log.put("opt_person", opt_person);
			trainSystemSettingService.addInterface12306URL(systemSettingVo, log);

			return "redirect:/trainSystemSetting/getSystemSetting.do";
		}
		
		@RequestMapping("/updateNewRiZhi.do")
		public String updateNewRiZhi(HttpServletRequest request, HttpServletResponse response) {
			String urlId = request.getParameter("urlId");
			SystemSettingVo systemSettingVo = trainSystemSettingService.getSystemSettingById(urlId);
			String urlstr= this.getParam(request, "urlStr");
			String type = this.getParam(request, "type");
			String typeName="";
				if(type.equals("1"))
				typeName="预订重发";
			else if(type.equals("2"))
				typeName="支付重发";
			else if(type.equals("3"))
				typeName="预订自动失败";
			else if(type.equals("4"))
				typeName="支付自动失败";
			
			Map<String, String> log = new HashMap<String, String>();
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			log.put("content", "修改【"+typeName+"】日志"+urlstr+",修改前为："+systemSettingVo.getSetting_value());
			
			log.put("opt_person", opt_person);
			systemSettingVo.setSetting_value(urlstr);
			trainSystemSettingService.updateInterface12306URL(systemSettingVo, log);

			return "redirect:/trainSystemSetting/getSystemSetting.do";
		}
		
		@RequestMapping("/changeNewRiZhiStatus.do")
		public String changeNewRiZhiStatus(HttpServletRequest request, HttpServletResponse response){
			SystemSettingVo systemSettingVo = trainSystemSettingService.getSystemSettingById(request.getParameter("urlId"));
			String status="";
			if(systemSettingVo.getSetting_status().equals("1")){
				systemSettingVo.setSetting_status("0");
				status="启用改禁用";
			}else if(systemSettingVo.getSetting_status().equals("0")){
				systemSettingVo.setSetting_status("1");
				status="禁用改启用";
			}
			String type = this.getParam(request, "type");
			String typeName="";
			if(type.equals("1")){
				typeName="预订重发";
			}else if(type.equals("2")){
				typeName="支付重发";
			}else if(type.equals("3")){
				typeName="自动失败";
			}
			String urlstr = request.getParameter(request.getParameter("urlId"));
			Map<String, String> log = new HashMap<String, String>();
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			log.put("content", "【"+typeName+"】："+urlstr+" 的状态，"+status);
			log.put("opt_person", opt_person);
			trainSystemSettingService.updateURLStatus(systemSettingVo, log);

			return "redirect:/trainSystemSetting/getSystemSetting.do";
		}
		
		@RequestMapping("/updatePayCtrl.do")
		public String updatePayCtrl(HttpServletRequest request,
				HttpServletResponse response) {
			String setting_id = this.getParam(request, "setting_id");
			String setting_value = this.getParam(request, "robot_pay_ctrl");
			if(StringUtils.isEmpty(setting_id) || StringUtils.isEmpty(setting_value)){
				return "redirect:/trainSystemSetting/getSystemSetting.do";
			}
			SystemSettingVo systemSettingVo = new SystemSettingVo();
			systemSettingVo.setSetting_id(setting_id);
			systemSettingVo.setSetting_value(setting_value);
			
			Map<String, String> log = new HashMap<String, String>();
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			log.put("content", "切换机器人支付控制,setting_id="+setting_id+",value="+setting_value);
			log.put("opt_person", opt_person);
			trainSystemSettingService.updatePayCtrl(systemSettingVo, log);

			return "redirect:/trainSystemSetting/getSystemSetting.do";
		}
		
		//切换保险渠道
		@RequestMapping("/updateBxChannel.do")
		public String updateBxChannel(HttpServletRequest request,
				HttpServletResponse response){
			String setting_id = this.getParam(request, "setting_id");
			String bx_value = this.getParam(request, "bx_value");
			SystemSettingVo bxChannel = trainSystemSettingService.getSystemSettingById(setting_id);
			
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			Map<String, String> log = new HashMap<String, String>();
			log.put("content", opt_person + "更改了保险渠道，更改前的值为" + bxChannel.getSetting_value() + ",更改后的值为" + bx_value);
			log.put("opt_person", opt_person);
			trainSystemSettingService.addSystemLog(log);
			bxChannel.setSetting_value(bx_value);
			trainSystemSettingService.updateChannel(bxChannel);
			return "redirect:/trainSystemSetting/getSystemSetting.do";
		}
		
		//切换短信渠道
		@RequestMapping("/updateMsgChannel.do")
		public String updateMsgChannel(HttpServletRequest request, HttpServletResponse response){
			String setting_id = this.getParam(request, "setting_id");
			String msg_value = this.getParam(request, "msg_value");
			SystemSettingVo msgChannel = trainSystemSettingService.getSystemSettingById(setting_id);
			
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			Map<String, String> log = new HashMap<String, String>();
			log.put("content", opt_person + "更改了短信渠道，更改前的值为" + msgChannel.getSetting_value() + ",更改后的值为" + msg_value);
			log.put("opt_person", opt_person);
			trainSystemSettingService.addSystemLog(log);
			msgChannel.setSetting_value(msg_value);
			trainSystemSettingService.updateChannel(msgChannel);
			return "redirect:/trainSystemSetting/getSystemSetting.do";
		}
		
		//切换默认乘车日期
		@RequestMapping("/updateTtlChannel.do")
		public String updateTtlChannel(HttpServletRequest request, HttpServletResponse response){
			String setting_id = this.getParam(request, "setting_id");
			String ttl_value = this.getParam(request, "ttl_value");
			SystemSettingVo ttlChannel = trainSystemSettingService.getSystemSettingById(setting_id);
			
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			Map<String, String> log = new HashMap<String, String>();
			log.put("content", opt_person + "更改了默认乘车日期，更改前的值为" + ttlChannel.getSetting_value() + ",更改后的值为" + ttl_value);
			log.put("opt_person", opt_person);
			trainSystemSettingService.addSystemLog(log);
			ttlChannel.setSetting_value(ttl_value);
			trainSystemSettingService.updateChannel(ttlChannel);
			return "redirect:/trainSystemSetting/getSystemSetting.do";
		}
		
		//切换余票阀值
		@RequestMapping("/updateStaChannel.do")
		public String updateStaChannel(HttpServletRequest request, HttpServletResponse response){
			String setting_id = this.getParam(request, "setting_id");
			String sta_value = this.getParam(request, "sta_value");
			SystemSettingVo staChannel = trainSystemSettingService.getSystemSettingById(setting_id);
			
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			Map<String, String> log = new HashMap<String, String>();
			log.put("content", opt_person + "更改了余票阀值数，更改前的值为" + staChannel.getSetting_value() + ",更改后的值为" + sta_value);
			log.put("opt_person", opt_person);
			trainSystemSettingService.addSystemLog(log);
			staChannel.setSetting_value(sta_value);
			trainSystemSettingService.updateChannel(staChannel);
			return "redirect:/trainSystemSetting/getSystemSetting.do";
		}
		
		//切换默认排队人数
		@RequestMapping("/updateQtaChannel.do")
		public String updateQtaChannel(HttpServletRequest request, HttpServletResponse response){
			String setting_id = this.getParam(request, "setting_id");
			String qta_value = this.getParam(request, "qta_value");
			SystemSettingVo qtaChannel = trainSystemSettingService.getSystemSettingById(setting_id);
			
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			Map<String, String> log = new HashMap<String, String>();
			log.put("content", opt_person + "更改了默认排队人数，更改前的值为" + qtaChannel.getSetting_value() + ",更改后的值为" + qta_value);
			log.put("opt_person", opt_person);
			trainSystemSettingService.addSystemLog(log);
			qtaChannel.setSetting_value(qta_value);
			trainSystemSettingService.updateChannel(qtaChannel);
			return "redirect:/trainSystemSetting/getSystemSetting.do";
		}
		
		//切换默认排队时间
		@RequestMapping("/updateQttChannel.do")
		public String updateQttChannel(HttpServletRequest request, HttpServletResponse response){
			String setting_id = this.getParam(request, "setting_id");
			String qtt_value = this.getParam(request, "qtt_value");
			SystemSettingVo qttChannel = trainSystemSettingService.getSystemSettingById(setting_id);
			
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			Map<String, String> log = new HashMap<String, String>();
			log.put("content", opt_person + "更改了排队等候时间，更改前的值为" + qttChannel.getSetting_value() + ",更改后的值为" + qtt_value);
			log.put("opt_person", opt_person);
			trainSystemSettingService.addSystemLog(log);
			qttChannel.setSetting_value(qtt_value);
			trainSystemSettingService.updateChannel(qttChannel);
			return "redirect:/trainSystemSetting/getSystemSetting.do";
		}
		//切换预定机器人版本
		@RequestMapping("/updateBookRobotChannel.do")
		public String updateBookRobotChannel(HttpServletRequest request, HttpServletResponse response){
			String setting_id = this.getParam(request, "setting_id");
			String book_robot_version = this.getParam(request, "book_robot_version");
			SystemSettingVo bookRobotChannel = trainSystemSettingService.getSystemSettingById(setting_id);
			
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			Map<String, String> log = new HashMap<String, String>();
			log.put("content", opt_person + "切换了预定机器人版本，更改前的值为" + bookRobotChannel.getSetting_value() + ",更改后的值为" + book_robot_version);
			log.put("opt_person", opt_person);
			trainSystemSettingService.addSystemLog(log);
			bookRobotChannel.setSetting_value(book_robot_version);
			trainSystemSettingService.updateChannel(bookRobotChannel);
			return "redirect:/trainSystemSetting/getSystemSetting.do";
		}
		
		//切换前台是否可以预订功能
		@RequestMapping("/changeSysBook.do")
		public String changeSysBook(HttpServletRequest request, HttpServletResponse response){
			String setting_id = this.getParam(request, "setting_id");
			String weather_book = this.getParam(request, "weather_book");
			SystemSettingVo weatherBookChannel = trainSystemSettingService.getSystemSettingById(setting_id);
			
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			Map<String, String> log = new HashMap<String, String>();
			log.put("content", opt_person + "切换前台是否可预订功能，更改前的值为" + weatherBookChannel.getSetting_value() + ",更改后的值为" + weather_book);
			log.put("opt_person", opt_person);
			trainSystemSettingService.addSystemLog(log);
			weatherBookChannel.setSetting_value(weather_book);
			trainSystemSettingService.updateChannel(weatherBookChannel);
			return "redirect:/trainSystemSetting/getSystemSetting.do";
		}
		
		//切换打码方式
		@RequestMapping("/changeCodeType.do")
		public String changeCodeType(HttpServletRequest request, HttpServletResponse response){
			String setting_id = this.getParam(request, "setting_id");
			String code_type = this.getParam(request, "code_type");
			SystemSettingVo codeTypeChannel = trainSystemSettingService.getSystemSettingById(setting_id);
			
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			Map<String, String> log = new HashMap<String, String>();
			log.put("content", opt_person + "切换打码方式功能，更改前的值为" + codeTypeChannel.getSetting_value() + ",更改后的值为" + code_type);
			log.put("opt_person", opt_person);
			trainSystemSettingService.addSystemLog(log);
			codeTypeChannel.setSetting_value(code_type);
			trainSystemSettingService.updateChannel(codeTypeChannel);
			return "redirect:/trainSystemSetting/getSystemSetting.do";
		}
		
		//切换手动打码渠道
		@RequestMapping("/changeCodeTypeChannel.do")
		public String changeCodeTypeChannel(HttpServletRequest request, HttpServletResponse response){
			String setting_id = this.getParam(request, "setting_id");
			String code_type = this.getParam(request, "code_type_channel");
			SystemSettingVo codeTypeChannel = trainSystemSettingService.getSystemSettingById(setting_id);
			
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			Map<String, String> log = new HashMap<String, String>();
			log.put("content", opt_person + "切换打码渠道功能，更改前的值为" + codeTypeChannel.getSetting_value() + ",更改后的值为" + code_type);
			log.put("opt_person", opt_person);
			trainSystemSettingService.addSystemLog(log);
			codeTypeChannel.setSetting_value(code_type);
			trainSystemSettingService.updateChannel(codeTypeChannel);
			trainSystemSettingService.updateChannelRh(codeTypeChannel);
			return "redirect:/trainSystemSetting/getSystemSetting.do";
		}
		
		//切换改退方式
		@RequestMapping("/changeRefundAndAlert.do")
		public String changeRefundAndAlert(HttpServletRequest request, HttpServletResponse response){
			String setting_id = this.getParam(request, "setting_id");
			String refund_and_alert = this.getParam(request, "refund_and_alert");
			SystemSettingVo SystemSetting = trainSystemSettingService.getSystemSettingById(setting_id);
			
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			Map<String, String> log = new HashMap<String, String>();
			log.put("content", opt_person + "切换改退方式，更改前的值为" + SystemSetting.getSetting_value() + ",更改后的值为" + refund_and_alert+"  (0、人工改退 1、机器改退)");
			log.put("opt_person", opt_person);
			trainSystemSettingService.addSystemLog(log);
			SystemSetting.setSetting_value(refund_and_alert);
			trainSystemSettingService.updateChannel(SystemSetting);
			return "redirect:/trainSystemSetting/getSystemSetting.do";
		}
		
		
		//切换默认排队时间
		@RequestMapping("/updateBuytime.do")
		public String updateBuytime(HttpServletRequest request, HttpServletResponse response){
			String setting_id = this.getParam(request, "setting_id");
			String btt_value = this.getParam(request, "btt_value");
			SystemSettingVo bttChannel = trainSystemSettingService.getSystemSettingById(setting_id);
			
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			Map<String, String> log = new HashMap<String, String>();
			log.put("content", opt_person + "开车前购票时间，更改前的值为" + bttChannel.getSetting_value() + ",更改后的值为" + btt_value);
			log.put("opt_person", opt_person);
			trainSystemSettingService.addSystemLog(log);
			bttChannel.setSetting_value(btt_value);
			trainSystemSettingService.updateChannel(bttChannel);
			return "redirect:/trainSystemSetting/getSystemSetting.do";
		}
		
		//切换发车后退票时间
		@RequestMapping("/updateRefundtime.do")
		public String updateRefundtime(HttpServletRequest request, HttpServletResponse response){
			String setting_id = this.getParam(request, "setting_id");
			String btt_value = this.getParam(request, "srtt_value");
			SystemSettingVo bttChannel = trainSystemSettingService.getSystemSettingById(setting_id);
			
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			Map<String, String> log = new HashMap<String, String>();
			log.put("content", opt_person + "开车后退票时间，更改前的值为" + bttChannel.getSetting_value() + ",更改后的值为" + btt_value);
			log.put("opt_person", opt_person);
			trainSystemSettingService.addSystemLog(log);
			bttChannel.setSetting_value(btt_value);
			trainSystemSettingService.updateChannel(bttChannel);
			return "redirect:/trainSystemSetting/getSystemSetting.do";
		}
		
		//修改预订机器人一次处理订单量
		@RequestMapping("/updateRapnChannel.do")
		public String updateRapnChannel(HttpServletRequest request, HttpServletResponse response){
			String setting_id = this.getParam(request, "setting_id");
			String rapn_value = this.getParam(request, "rapn_value");
			SystemSettingVo rapnChannel = trainSystemSettingService.getSystemSettingById(setting_id);
			
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			Map<String, String> log = new HashMap<String, String>();
			log.put("content", opt_person + "更改了预订机器人一次处理订单量，更改前的值为" + rapnChannel.getSetting_value() + ",更改后的值为" + rapn_value);
			log.put("opt_person", opt_person);
			trainSystemSettingService.addSystemLog(log);
			rapnChannel.setSetting_value(rapn_value);
			trainSystemSettingService.updateChannel(rapnChannel);
			return "redirect:/trainSystemSetting/getSystemSetting.do";
		}
		
		//修改预约购票天数
		@RequestMapping("/updateBdnChannel.do")
		public String updateBdnChannel(HttpServletRequest request, HttpServletResponse response){
			String setting_id = this.getParam(request, "setting_id");
			String bdn_value = this.getParam(request, "bdn_value");
			SystemSettingVo bdnChannel = trainSystemSettingService.getSystemSettingById(setting_id);
			
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			Map<String, String> log = new HashMap<String, String>();
			log.put("content", opt_person + "更改了预约购票天数，更改前的值为" + bdnChannel.getSetting_value() + ",更改后的值为" + bdn_value);
			log.put("opt_person", opt_person);
			trainSystemSettingService.addSystemLog(log);
			bdnChannel.setSetting_value(bdn_value);
			trainSystemSettingService.updateChannel(bdnChannel);
			return "redirect:/trainSystemSetting/getSystemSetting.do";
		}
		
		//修改去哪打码方式
		@RequestMapping("/changequnarPlayCode.do")
		public String changequnarPlayCode(HttpServletRequest request, HttpServletResponse response){
			String setting_id = this.getParam(request, "setting_id");
			String bdn_value = this.getParam(request, "qunar_play_code");
			SystemSettingVo bdnChannel = trainSystemSettingService.getSystemSettingById(setting_id);
			
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			Map<String, String> log = new HashMap<String, String>();
			String str1="",str2="";
			if("00".equals(bdnChannel.getSetting_value()))str1="去哪打码";
			if("11".equals(bdnChannel.getSetting_value()))str1="我们打码";
			if("22".equals(bdnChannel.getSetting_value()))str1="去哪+我们打码";
			if("33".equals(bdnChannel.getSetting_value()))str1="打码兔";
			if("00".equals(bdn_value))str2="去哪打码";
			if("11".equals(bdn_value))str2="我们打码";
			if("22".equals(bdn_value))str2="去哪+我们打码";
			if("33".equals(bdn_value))str2="打码兔";
			log.put("content", opt_person + "更改了去哪打码方式，更改前的值为【" + str1 + "】,更改后的值为【" + str2+"】");
			log.put("opt_person", opt_person);
			trainSystemSettingService.addSystemLog(log);
			bdnChannel.setSetting_value(bdn_value);
			trainSystemSettingService.updateChannel(bdnChannel);
			return "redirect:/trainSystemSetting/getSystemSetting.do";
		}
		
		//修改同程打码方式
		@RequestMapping("/changetcPlayCode.do")
		public String changetcPlayCode(HttpServletRequest request, HttpServletResponse response){
			String setting_id = this.getParam(request, "setting_id");
			String bdn_value = this.getParam(request, "tc_play_code");
			SystemSettingVo bdnChannel = trainSystemSettingService.getSystemSettingById(setting_id);
			
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			Map<String, String> log = new HashMap<String, String>();
			String str1="",str2="";
			if("00".equals(bdnChannel.getSetting_value()))str1="同程打码";
			if("11".equals(bdnChannel.getSetting_value()))str1="同程+我们打码";
			if("22".equals(bdnChannel.getSetting_value()))str1="同程打码（包括我们码）";
			if("00".equals(bdn_value))str2="同程打码";
			if("11".equals(bdn_value))str2="同程+我们打码";
			if("22".equals(bdn_value))str2="同程打码（包括我们码）";
			log.put("content", opt_person + "更改了同程打码方式，更改前的值为【" + str1 + "】,更改后的值为【" + str2+"】");
			log.put("opt_person", opt_person);
			trainSystemSettingService.addSystemLog(log);
			bdnChannel.setSetting_value(bdn_value);
			trainSystemSettingService.updateChannel(bdnChannel);
			return "redirect:/trainSystemSetting/getSystemSetting.do";
		}
		
		//修改同程打码方式
		@RequestMapping("/changeElongPlayCode.do")
		public String changeElongPlayCode(HttpServletRequest request, HttpServletResponse response){
			String setting_id = this.getParam(request, "setting_id");
			String bdn_value = this.getParam(request, "elong_play_code");
			SystemSettingVo bdnChannel = trainSystemSettingService.getSystemSettingById(setting_id);
			
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			Map<String, String> log = new HashMap<String, String>();
			String str1="",str2="";
			if("00".equals(bdnChannel.getSetting_value()))str1="艺龙打自己码";
			if("11".equals(bdnChannel.getSetting_value()))str1="我们+艺龙打码";
			if("22".equals(bdnChannel.getSetting_value()))str1="艺龙给我们打码";
			if("33".equals(bdnChannel.getSetting_value()))str1="打码兔打码";
			if("44".equals(bdnChannel.getSetting_value()))str1="我们+打码兔打码";
			if("00".equals(bdn_value))str2="艺龙打自己码";
			if("11".equals(bdn_value))str2="我们+艺龙打码";
			if("22".equals(bdn_value))str2="艺龙给我们打码";
			if("33".equals(bdn_value))str2="打码兔打码";
			if("44".equals(bdn_value))str2="我们+打码兔打码";
			log.put("content", opt_person + "更改了艺龙打码方式，更改前的值为【" + str1 + "】,更改后的值为【" + str2+"】");
			log.put("opt_person", opt_person);
			trainSystemSettingService.addSystemLog(log);
			bdnChannel.setSetting_value(bdn_value);
			trainSystemSettingService.updateChannel(bdnChannel);
			return "redirect:/trainSystemSetting/getSystemSetting.do";
		}
		
		//查询返回日志页面
		@RequestMapping("/gototrain_return_optlog.do")
		public String gototrain_return_optlog(HttpServletRequest request, HttpServletResponse response){
			/*************************创建Map***************************/
			Map<String, Object> paramMap = new HashMap<String, Object>();
			/*************************分页条件***************************/
			int totalCount = trainSystemSettingService.querytrain_return_optlogCount(paramMap);//总条数	
			//分页
			PageVo page = PageUtil.getInstance().paging(request, 10, totalCount);
			paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
			paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
			
			List<Map<String,Object>> train_return_optlogList=trainSystemSettingService.querytrain_return_optlogList(paramMap);
			request.setAttribute("train_return_optlogList", train_return_optlogList);
			request.setAttribute("isShowList", 1);
			return "trainSystemSetting/trainReturnOptlog";
		}
		//增加返回日志页面
		@RequestMapping("/addtrain_return_optlog.do")
		public String addtrain_return_optlog(HttpServletRequest request, HttpServletResponse response){
			String pageIndex=this.getParam(request, "pageIndex");
			String return_id=this.getParam(request, "return_id_add");
			String return_name=this.getParam(request, "return_name_add");
			String return_value=this.getParam(request, "return_value_add");
			String return_type=this.getParam(request, "return_type_add");
			String return_status=this.getParam(request, "return_status_add");
			String return_join=this.getParam(request, "return_join_add");
			String return_ticket=this.getParam(request, "return_ticket_add");
			String return_active=this.getParam(request, "return_active_add");
			String return_fail_reason=this.getParam(request, "return_fail_reason_add");
			Map<String, Object> paramMap=new HashMap<String, Object>();
			paramMap.put("return_id", return_id);
			paramMap.put("return_name", return_name);
			paramMap.put("return_value", return_value);
			paramMap.put("return_type", return_type);
			paramMap.put("return_status", return_status);
			paramMap.put("return_join", return_join);
			paramMap.put("return_ticket", return_ticket);
			paramMap.put("return_active", return_active);
			paramMap.put("return_fail_reason", return_fail_reason);
			trainSystemSettingService.addtrain_return_optlog(paramMap);
			Map<String, String> log = new HashMap<String, String>();
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			log.put("content", "添加新的返回日志,return_id="+return_id+",return_name="+return_name+",return_value="+return_value+",return_type="+return_type+",return_status="+return_status+",return_join="+return_join+",return_ticket="+return_ticket+",return_active="+return_active);
			log.put("opt_person", opt_person);
			trainSystemSettingService.addSystemLog(log);
			return "redirect:/trainSystemSetting/gototrain_return_optlog.do?pageIndex="+pageIndex;
		}
		//验证输入编号是否存在
		@RequestMapping("/queryreturn_optlog_id.do")
		public String queryreturn_optlog_id(HttpServletRequest request ,HttpServletResponse response){
			String return_id = this.getParam(request, "return_id") ;
			System.out.println("return_id="+return_id);
			String result = null;
			if(StringUtils.isEmpty( trainSystemSettingService.queryreturn_optlogById(return_id) )){
				result = "yes";
			}else{
				result = "no";
			}
			try {
				response.getWriter().write(result);
				response.getWriter().flush();
				response.getWriter().close() ;
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		//修改返回日志页面
		@RequestMapping("/updatetrain_return_optlog.do")
		public String updatetrain_return_optlog(HttpServletRequest request, HttpServletResponse response){
			String pageIndex=this.getParam(request, "pageIndex");
			String return_id=this.getParam(request, "return_id");
			String return_name=this.getParam(request, "return_name_"+return_id);
			String return_value=this.getParam(request, "return_value_"+return_id);
			String return_type=this.getParam(request, "return_type_"+return_id);
			String return_status=this.getParam(request, "return_status_"+return_id);
			String return_join=this.getParam(request, "return_join_"+return_id);
			String return_ticket=this.getParam(request, "return_ticket_"+return_id);
			String return_active=this.getParam(request, "return_active_"+return_id);
			String return_fail_reason=this.getParam(request, "return_fail_reason_"+return_id);
			Map<String, Object> paramMap=new HashMap<String, Object>();
			paramMap.put("return_id", return_id);
			paramMap.put("return_name", return_name);
			paramMap.put("return_value", return_value);
			paramMap.put("return_type", return_type);
			paramMap.put("return_status", return_status);
			paramMap.put("return_join", return_join);
			paramMap.put("return_ticket", return_ticket);
			paramMap.put("return_active", return_active);
			paramMap.put("return_fail_reason", return_fail_reason);
			trainSystemSettingService.updatetrain_return_optlog(paramMap);
			Map<String, String> log = new HashMap<String, String>();
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			log.put("content", "添加新的返回日志,return_id="+return_id+",return_name="+return_name+",return_value="+return_value+",return_type="+return_type+",return_status="+return_status+",return_join="+return_join+",return_ticket="+return_ticket+",return_active="+return_active);
			log.put("opt_person", opt_person);
			trainSystemSettingService.addSystemLog(log);
			return "redirect:/trainSystemSetting/gototrain_return_optlog.do?pageIndex="+pageIndex;
		}
		//删除返回日志页面
		@RequestMapping("/deletetrain_return_optlog.do")
		public String deletetrain_return_optlog(HttpServletRequest request, HttpServletResponse response){
			String return_id=this.getParam(request, "return_id");
			String return_name=this.getParam(request, "return_name_"+return_id);
			String pageIndex=this.getParam(request, "pageIndex");
			Map<String, Object> paramMap=new HashMap<String, Object>();
			paramMap.put("return_id", return_id);
			trainSystemSettingService.deletetrain_return_optlog(paramMap);
			Map<String, String> log = new HashMap<String, String>();
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			log.put("content", "删除返回日志,return_id="+return_id+",return_name="+return_name);
			log.put("opt_person", opt_person);
			trainSystemSettingService.addSystemLog(log);
			return "redirect:/trainSystemSetting/gototrain_return_optlog.do?pageIndex="+pageIndex;
		}
		
		//修改短信通知号码
		@RequestMapping("/changephoneMsgList.do")
		public String changephoneMsgList(HttpServletRequest request, HttpServletResponse response){
			String setting_id = this.getParam(request, "setting_id");
			List<String> bdn_valueList = this.getParamToList(request, "phone_msg_list");
			SystemSettingVo bdnChannel = trainSystemSettingService.getSystemSettingById(setting_id);
			
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			Map<String, String> log = new HashMap<String, String>();
			String bdn_value="";
			for(int i=0;i<bdn_valueList.size();i++){
				bdn_value += bdn_valueList.get(i)+"@@";
			}
			log.put("content", opt_person + "更改了短信通知列表");
			log.put("opt_person", opt_person);
			trainSystemSettingService.addSystemLog(log);
			bdnChannel.setSetting_value(bdn_value);
			trainSystemSettingService.updateChannel(bdnChannel);
			return "redirect:/trainSystemSetting/getSystemSetting.do";
		}

		
		@RequestMapping("/changeManualZb.do")
		public String changeManualZb(HttpServletRequest request, HttpServletResponse response){
			String setting_id = this.getParam(request, "setting_id");
			String bdn_value = this.getParam(request, "manual_order_zb");
			SystemSettingVo bdnChannel = trainSystemSettingService.getSystemSettingById(setting_id);
			
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			Map<String, String> log = new HashMap<String, String>();
			log.put("content", opt_person + "更改了人工出票占比，更改前的值为【" + bdnChannel.getSetting_value() + "】,更改后的值为【" + bdn_value+"】");
			log.put("opt_person", opt_person);
			trainSystemSettingService.addSystemLog(log);
			bdnChannel.setSetting_value(bdn_value);
			trainSystemSettingService.updateChannel(bdnChannel);
			return "redirect:/trainSystemSetting/getSystemSetting.do";
		}
		@RequestMapping("/changeManualNum.do")
		public String changeManualNum(HttpServletRequest request, HttpServletResponse response){
			String setting_id = this.getParam(request, "setting_id");
			String bdn_value = this.getParam(request, "manual_order_num");
			SystemSettingVo bdnChannel = trainSystemSettingService.getSystemSettingById(setting_id);
			
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			Map<String, String> log = new HashMap<String, String>();
			log.put("content", opt_person + "更改了人工出票总数，更改前的值为【" + bdnChannel.getSetting_value() + "】,更改后的值为【" + bdn_value+"】");
			log.put("opt_person", opt_person);
			trainSystemSettingService.addSystemLog(log);
			bdnChannel.setSetting_value(bdn_value);
			trainSystemSettingService.updateChannel(bdnChannel);
			return "redirect:/trainSystemSetting/getSystemSetting.do";
		}
		
		@RequestMapping("/changeCodeSetStatus.do")
		public String changeCodeSetStatus(HttpServletRequest request, HttpServletResponse response){
			SystemSettingVo systemSettingVo = trainSystemSettingService.getSystemSettingById(request.getParameter("urlId"));
			String content="";
			if(systemSettingVo.getSetting_status().equals("1")){
				systemSettingVo.setSetting_status("0");
				content+="【禁用】打码器版本";
			}else if(systemSettingVo.getSetting_status().equals("0")){
				systemSettingVo.setSetting_status("1");
				content+="【启用】打码器版本";
			}
			
			Map<String, String> log = new HashMap<String, String>();
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			log.put("content", content);
			log.put("opt_person", opt_person);
			trainSystemSettingService.updateURLStatus(systemSettingVo, log);

			return "redirect:/trainSystemSetting/getSystemSetting.do";
		}
		
		@RequestMapping("/changeCodeSet.do")
		public String changeCodeSet(HttpServletRequest request, HttpServletResponse response) {
			String urlId = request.getParameter("urlId");
			SystemSettingVo systemSettingVo = trainSystemSettingService.getSystemSettingById(urlId);
			String urlstr = request.getParameter(urlId);
			String content="修改"+systemSettingVo.getSetting_desc()+",之前="+systemSettingVo.getSetting_value()+",value="+urlstr;
			systemSettingVo.setSetting_value(urlstr);
			
			Map<String, String> log = new HashMap<String, String>();
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			log.put("content", content);
			log.put("opt_person", opt_person);
			trainSystemSettingService.updateInterface12306URL(systemSettingVo, log);

			return "redirect:/trainSystemSetting/getSystemSetting.do";
		}
	}
