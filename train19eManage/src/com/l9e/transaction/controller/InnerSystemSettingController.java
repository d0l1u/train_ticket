package com.l9e.transaction.controller;

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
import com.l9e.transaction.service.InnerSystemSettingService;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.transaction.vo.SystemSettingVo;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.PageUtil;

@Controller
@RequestMapping("/innerSystemSetting")
public class InnerSystemSettingController extends BaseController {

		@Resource
		private InnerSystemSettingService innerSystemSettingService;
		
		@RequestMapping("/getSystemSetting.do")
		public String getSystemSetting(HttpServletRequest request,
				HttpServletResponse response) {
			request.setAttribute("systemSetting", innerSystemSettingService.getSystemSetting());
			return "innerSystemSetting/innerSettingList";
		}
		
	
		//查看操作日志
		@RequestMapping("/querySystemSetList.do")
		public String querySystemSettingList(HttpServletRequest request, HttpServletResponse response){
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			Map<String,Object>paramMap = new HashMap<String,Object>();
			/******************分页条件开始********************/
			int totalCount = innerSystemSettingService.querySystemSetListCount();
			PageVo page = PageUtil.getInstance().paging(request, 10, totalCount);
			paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
			paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
			/******************操作********************/
			List<Map<String,Object>> systemList = innerSystemSettingService.querySystemSetList(paramMap);
			request.setAttribute("systemList", systemList);
			request.setAttribute("isShowList", 1);
			return "innerSystemSetting/innerSystemSetList";
		}
		
		@RequestMapping("/updateInterfaceChannel.do")
		public String updateInterfaceChannel(HttpServletRequest request,
				HttpServletResponse response) {
			SystemSettingVo systemSettingVo = innerSystemSettingService.getSystemSettingById(request.getParameter("channelId"));
			systemSettingVo.setSetting_value(request.getParameter("channel"));
			Map<String, String> log = new HashMap<String, String>();
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			log.put("content", "切换接口value="+request.getParameter("channel"));
			log.put("opt_person", opt_person);
			innerSystemSettingService.updateInterfaceChannel(systemSettingVo, log);

			return "redirect:/innerSystemSetting/getSystemSetting.do";
		}
		
		@RequestMapping("/updateConTimeout.do")
		public String updateConTimeout(HttpServletRequest request,
				HttpServletResponse response) {
			String setting_id = request.getParameter("conTimeoutId");
			SystemSettingVo systemSettingVo = innerSystemSettingService.getSystemSettingById(setting_id);
			systemSettingVo.setSetting_value(request.getParameter("con_timeout"));
			Map<String, String> log = new HashMap<String, String>();
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			log.put("content", "修改连接超时时间"+request.getParameter("con_timeout"));
			log.put("opt_person", opt_person);
			innerSystemSettingService.updateInterfaceConTimeout(systemSettingVo, log);

			return "redirect:/innerSystemSetting/getSystemSetting.do";
		}
		
		@RequestMapping("/updateReadTimeout.do")
		public String updateReadTimeout(HttpServletRequest request, HttpServletResponse response) {
			SystemSettingVo systemSettingVo = innerSystemSettingService.getSystemSettingById(request.getParameter("readTimeoutId"));
			systemSettingVo.setSetting_value(request.getParameter("read_timeout"));
			Map<String, String> log = new HashMap<String, String>();
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			log.put("content", "修改读超时时间"+request.getParameter("read_timeout"));
			log.put("opt_person", opt_person);
			innerSystemSettingService.updateInterfaceReadTimeout(systemSettingVo, log);

			return "redirect:/innerSystemSetting/getSystemSetting.do";
		}
		
		@RequestMapping("/updateInterface12306RUL.do")
		public String updateInterface12306RUL(HttpServletRequest request, HttpServletResponse response) {
			String urlId = request.getParameter("urlId");
			SystemSettingVo systemSettingVo = innerSystemSettingService.getSystemSettingById(urlId);
			String urlstr = request.getParameter(urlId);
			systemSettingVo.setSetting_value(urlstr);
			
			Map<String, String> log = new HashMap<String, String>();
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			log.put("content", "修改12306接口地址,setting_id="+request.getParameter("urlId")+",value="+urlstr);
			log.put("opt_person", opt_person);
			innerSystemSettingService.updateInterface12306URL(systemSettingVo, log);

			return "redirect:/innerSystemSetting/getSystemSetting.do";
		}
		
		@RequestMapping("/changeURLStatus.do")
		public String changURLStatus(HttpServletRequest request, HttpServletResponse response){
			SystemSettingVo systemSettingVo = innerSystemSettingService.getSystemSettingById(request.getParameter("urlId"));
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
			innerSystemSettingService.updateURLStatus(systemSettingVo, log);

			return "redirect:/innerSystemSetting/getSystemSetting.do";
		}
		
		@RequestMapping("/deleteURL.do")
		public String deleteURL(HttpServletRequest request, HttpServletResponse response){
			SystemSettingVo systemSettingVo = innerSystemSettingService.getSystemSettingById(request.getParameter("urlId"));
			
			Map<String, String> log = new HashMap<String, String>();
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			log.put("content", "删除12306接口地址,setting_id="+request.getParameter("urlId")+",url="+systemSettingVo.getSetting_value());
			log.put("opt_person", opt_person);
			innerSystemSettingService.deleteInterface12306URL(systemSettingVo, log);

			return "redirect:/innerSystemSetting/getSystemSetting.do";
		}
		
		@RequestMapping("/turnToAddURLPage.do")
		public String turnToAddURL(HttpServletRequest request, HttpServletResponse response) {
			return "innerSystemSetting/addURL";
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
			innerSystemSettingService.addSystemLog(log);
			innerSystemSettingService.addInterface12306URL(systemSettingVo, log);

			return "redirect:/innerSystemSetting/getSystemSetting.do";
		}
		
		@RequestMapping("/updateInterface12306NewRUL.do")
		public String updateInterface12306NewRUL(HttpServletRequest request, HttpServletResponse response) {
			String urlId = request.getParameter("urlId");
			SystemSettingVo systemSettingVo = innerSystemSettingService.getSystemSettingById(urlId);
			String urlstr = request.getParameter(urlId);
			systemSettingVo.setSetting_value(urlstr);
			
			Map<String, String> log = new HashMap<String, String>();
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			log.put("content", "修改12306新接口地址,setting_id="+request.getParameter("urlId")+",value="+urlstr);
			log.put("opt_person", opt_person);
			innerSystemSettingService.updateInterface12306URL(systemSettingVo, log);

			return "redirect:/innerSystemSetting/getSystemSetting.do";
		}
		
		@RequestMapping("/changeNewURLStatus.do")
		public String changNewURLStatus(HttpServletRequest request, HttpServletResponse response){
			SystemSettingVo systemSettingVo = innerSystemSettingService.getSystemSettingById(request.getParameter("urlId"));
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
			innerSystemSettingService.updateURLStatus(systemSettingVo, log);

			return "redirect:/innerSystemSetting/getSystemSetting.do";
		}
		
		@RequestMapping("/deleteNewURL.do")
		public String deleteNewURL(HttpServletRequest request, HttpServletResponse response){
			SystemSettingVo systemSettingVo = innerSystemSettingService.getSystemSettingById(request.getParameter("urlId"));
			
			Map<String, String> log = new HashMap<String, String>();
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			log.put("content", "删除12306新接口地址,setting_id="+request.getParameter("urlId")+",url="+systemSettingVo.getSetting_value());
			log.put("opt_person", opt_person);
			innerSystemSettingService.deleteInterface12306URL(systemSettingVo, log);

			return "redirect:/innerSystemSetting/getSystemSetting.do";
		}
		
		@RequestMapping("/turnToAddNewURLPage.do")
		public String turnToAddNewURL(HttpServletRequest request, HttpServletResponse response) {
			return "innerSystemSetting/addNewURL";
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
			innerSystemSettingService.addSystemLog(log);
			innerSystemSettingService.addInterface12306URL(systemSettingVo, log);

			return "redirect:/innerSystemSetting/getSystemSetting.do";
		}
		
		@RequestMapping("/updatePayCtrl.do")
		public String updatePayCtrl(HttpServletRequest request,
				HttpServletResponse response) {
			String setting_id = this.getParam(request, "setting_id");
			String setting_value = this.getParam(request, "robot_pay_ctrl");
			if(StringUtils.isEmpty(setting_id) || StringUtils.isEmpty(setting_value)){
				return "redirect:/innerSystemSetting/getSystemSetting.do";
			}
			SystemSettingVo systemSettingVo = new SystemSettingVo();
			systemSettingVo.setSetting_id(setting_id);
			systemSettingVo.setSetting_value(setting_value);
			
			Map<String, String> log = new HashMap<String, String>();
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			log.put("content", "切换机器人支付控制,setting_id="+setting_id+",value="+setting_value);
			log.put("opt_person", opt_person);
			innerSystemSettingService.updatePayCtrl(systemSettingVo, log);

			return "redirect:/innerSystemSetting/getSystemSetting.do";
		}
		
		//切换保险渠道
		@RequestMapping("/updateBxChannel.do")
		public String updateBxChannel(HttpServletRequest request,
				HttpServletResponse response){
			String setting_id = this.getParam(request, "setting_id");
			String bx_value = this.getParam(request, "bx_value");
			SystemSettingVo bxChannel = innerSystemSettingService.getSystemSettingById(setting_id);
			
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			Map<String, String> log = new HashMap<String, String>();
			log.put("content", opt_person + "更改了保险渠道，更改前的值为" + bxChannel.getSetting_value() + ",更改后的值为" + bx_value);
			log.put("opt_person", opt_person);
			innerSystemSettingService.addSystemLog(log);
			bxChannel.setSetting_value(bx_value);
			innerSystemSettingService.updateChannel(bxChannel);
			return "redirect:/innerSystemSetting/getSystemSetting.do";
		}
		
		//切换短信渠道
		@RequestMapping("/updateMsgChannel.do")
		public String updateMsgChannel(HttpServletRequest request, HttpServletResponse response){
			String setting_id = this.getParam(request, "setting_id");
			String msg_value = this.getParam(request, "msg_value");
			SystemSettingVo msgChannel = innerSystemSettingService.getSystemSettingById(setting_id);
			
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			Map<String, String> log = new HashMap<String, String>();
			log.put("content", opt_person + "更改了短信渠道，更改前的值为" + msgChannel.getSetting_value() + ",更改后的值为" + msg_value);
			log.put("opt_person", opt_person);
			innerSystemSettingService.addSystemLog(log);
			msgChannel.setSetting_value(msg_value);
			innerSystemSettingService.updateChannel(msgChannel);
			return "redirect:/innerSystemSetting/getSystemSetting.do";
		}
		
		//切换默认乘车日期
		@RequestMapping("/updateTtlChannel.do")
		public String updateTtlChannel(HttpServletRequest request, HttpServletResponse response){
			String setting_id = this.getParam(request, "setting_id");
			String ttl_value = this.getParam(request, "ttl_value");
			SystemSettingVo ttlChannel = innerSystemSettingService.getSystemSettingById(setting_id);
			
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			Map<String, String> log = new HashMap<String, String>();
			log.put("content", opt_person + "更改了默认乘车日期，更改前的值为" + ttlChannel.getSetting_value() + ",更改后的值为" + ttl_value);
			log.put("opt_person", opt_person);
			innerSystemSettingService.addSystemLog(log);
			ttlChannel.setSetting_value(ttl_value);
			innerSystemSettingService.updateChannel(ttlChannel);
			return "redirect:/innerSystemSetting/getSystemSetting.do";
		}
		
		//切换余票阀值
		@RequestMapping("/updateStaChannel.do")
		public String updateStaChannel(HttpServletRequest request, HttpServletResponse response){
			String setting_id = this.getParam(request, "setting_id");
			String sta_value = this.getParam(request, "sta_value");
			SystemSettingVo staChannel = innerSystemSettingService.getSystemSettingById(setting_id);
			
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			Map<String, String> log = new HashMap<String, String>();
			log.put("content", opt_person + "更改了余票阀值数，更改前的值为" + staChannel.getSetting_value() + ",更改后的值为" + sta_value);
			log.put("opt_person", opt_person);
			innerSystemSettingService.addSystemLog(log);
			staChannel.setSetting_value(sta_value);
			innerSystemSettingService.updateChannel(staChannel);
			return "redirect:/innerSystemSetting/getSystemSetting.do";
		}
		
		//切换默认排队人数
		@RequestMapping("/updateQtaChannel.do")
		public String updateQtaChannel(HttpServletRequest request, HttpServletResponse response){
			String setting_id = this.getParam(request, "setting_id");
			String qta_value = this.getParam(request, "qta_value");
			SystemSettingVo qtaChannel = innerSystemSettingService.getSystemSettingById(setting_id);
			
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			Map<String, String> log = new HashMap<String, String>();
			log.put("content", opt_person + "更改了默认排队人数，更改前的值为" + qtaChannel.getSetting_value() + ",更改后的值为" + qta_value);
			log.put("opt_person", opt_person);
			innerSystemSettingService.addSystemLog(log);
			qtaChannel.setSetting_value(qta_value);
			innerSystemSettingService.updateChannel(qtaChannel);
			return "redirect:/innerSystemSetting/getSystemSetting.do";
		}
		
		//切换默认排队时间
		@RequestMapping("/updateQttChannel.do")
		public String updateQttChannel(HttpServletRequest request, HttpServletResponse response){
			String setting_id = this.getParam(request, "setting_id");
			String qtt_value = this.getParam(request, "qtt_value");
			SystemSettingVo qttChannel = innerSystemSettingService.getSystemSettingById(setting_id);
			
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			Map<String, String> log = new HashMap<String, String>();
			log.put("content", opt_person + "更改了排队等候时间，更改前的值为" + qttChannel.getSetting_value() + ",更改后的值为" + qtt_value);
			log.put("opt_person", opt_person);
			innerSystemSettingService.addSystemLog(log);
			qttChannel.setSetting_value(qtt_value);
			innerSystemSettingService.updateChannel(qttChannel);
			return "redirect:/innerSystemSetting/getSystemSetting.do";
		}
		//切换预定机器人版本
		@RequestMapping("/updateBookRobotChannel.do")
		public String updateBookRobotChannel(HttpServletRequest request, HttpServletResponse response){
			String setting_id = this.getParam(request, "setting_id");
			String book_robot_version = this.getParam(request, "book_robot_version");
			SystemSettingVo bookRobotChannel = innerSystemSettingService.getSystemSettingById(setting_id);
			
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			Map<String, String> log = new HashMap<String, String>();
			log.put("content", opt_person + "切换了预定机器人版本，更改前的值为" + bookRobotChannel.getSetting_value() + ",更改后的值为" + book_robot_version);
			log.put("opt_person", opt_person);
			innerSystemSettingService.addSystemLog(log);
			bookRobotChannel.setSetting_value(book_robot_version);
			innerSystemSettingService.updateChannel(bookRobotChannel);
			return "redirect:/innerSystemSetting/getSystemSetting.do";
		}
		
		//切换前台是否可以预订功能
		@RequestMapping("/changeSysBook.do")
		public String changeSysBook(HttpServletRequest request, HttpServletResponse response){
			String setting_id = this.getParam(request, "setting_id");
			String weather_book = this.getParam(request, "weather_book");
			SystemSettingVo weatherBookChannel = innerSystemSettingService.getSystemSettingById(setting_id);
			
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			Map<String, String> log = new HashMap<String, String>();
			log.put("content", opt_person + "切换前台是否可预订功能，更改前的值为" + weatherBookChannel.getSetting_value() + ",更改后的值为" + weather_book);
			log.put("opt_person", opt_person);
			innerSystemSettingService.addSystemLog(log);
			weatherBookChannel.setSetting_value(weather_book);
			innerSystemSettingService.updateChannel(weatherBookChannel);
			return "redirect:/innerSystemSetting/getSystemSetting.do";
		}
		
		//切换打码方式
		@RequestMapping("/changeCodeType.do")
		public String changeCodeType(HttpServletRequest request, HttpServletResponse response){
			String setting_id = this.getParam(request, "setting_id");
			String code_type = this.getParam(request, "code_type");
			SystemSettingVo codeTypeChannel = innerSystemSettingService.getSystemSettingById(setting_id);
			
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			Map<String, String> log = new HashMap<String, String>();
			log.put("content", opt_person + "切换打码方式功能，更改前的值为" + codeTypeChannel.getSetting_value() + ",更改后的值为" + code_type);
			log.put("opt_person", opt_person);
			innerSystemSettingService.addSystemLog(log);
			codeTypeChannel.setSetting_value(code_type);
			innerSystemSettingService.updateChannel(codeTypeChannel);
			return "redirect:/innerSystemSetting/getSystemSetting.do";
		}
		
		//切换改退方式
		@RequestMapping("/changeRefundAndAlert.do")
		public String changeRefundAndAlert(HttpServletRequest request, HttpServletResponse response){
			String setting_id = this.getParam(request, "setting_id");
			String refund_and_alert = this.getParam(request, "refund_and_alert");
			SystemSettingVo SystemSetting = innerSystemSettingService.getSystemSettingById(setting_id);
			
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			Map<String, String> log = new HashMap<String, String>();
			log.put("content", opt_person + "切换改退方式，更改前的值为" + SystemSetting.getSetting_value() + ",更改后的值为" + refund_and_alert+"  (0、人工改退 1、机器改退)");
			log.put("opt_person", opt_person);
			innerSystemSettingService.addSystemLog(log);
			SystemSetting.setSetting_value(refund_and_alert);
			innerSystemSettingService.updateChannel(SystemSetting);
			return "redirect:/innerSystemSetting/getSystemSetting.do";
		}
		
		
		//切换默认排队时间
		@RequestMapping("/updateBuytime.do")
		public String updateBuytime(HttpServletRequest request, HttpServletResponse response){
			String setting_id = this.getParam(request, "setting_id");
			String btt_value = this.getParam(request, "btt_value");
			SystemSettingVo bttChannel = innerSystemSettingService.getSystemSettingById(setting_id);
			
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			Map<String, String> log = new HashMap<String, String>();
			log.put("content", opt_person + "开车前购票时间，更改前的值为" + bttChannel.getSetting_value() + ",更改后的值为" + btt_value);
			log.put("opt_person", opt_person);
			innerSystemSettingService.addSystemLog(log);
			bttChannel.setSetting_value(btt_value);
			innerSystemSettingService.updateChannel(bttChannel);
			return "redirect:/innerSystemSetting/getSystemSetting.do";
		}
		
		//切换发车前退票时间
		@RequestMapping("/updateBeforeRefundtime.do")
		public String updateBeforeRefundtime(HttpServletRequest request, HttpServletResponse response){
			String setting_id = this.getParam(request, "setting_id");
			String btt_value = this.getParam(request, "brtt_value");
			SystemSettingVo bttChannel = innerSystemSettingService.getSystemSettingById(setting_id);
			
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			Map<String, String> log = new HashMap<String, String>();
			log.put("content", opt_person + "开车前退票时间，更改前的值为" + bttChannel.getSetting_value() + ",更改后的值为" + btt_value);
			log.put("opt_person", opt_person);
			innerSystemSettingService.addSystemLog(log);
			bttChannel.setSetting_value(btt_value);
			innerSystemSettingService.updateChannel(bttChannel);
			return "redirect:/innerSystemSetting/getSystemSetting.do";
		}
		//切换发车后退票时间
		@RequestMapping("/updateRefundtime.do")
		public String updateRefundtime(HttpServletRequest request, HttpServletResponse response){
			String setting_id = this.getParam(request, "setting_id");
			String btt_value = this.getParam(request, "srtt_value");
			SystemSettingVo bttChannel = innerSystemSettingService.getSystemSettingById(setting_id);
			
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			Map<String, String> log = new HashMap<String, String>();
			log.put("content", opt_person + "开车后退票时间，更改前的值为" + bttChannel.getSetting_value() + ",更改后的值为" + btt_value);
			log.put("opt_person", opt_person);
			innerSystemSettingService.addSystemLog(log);
			bttChannel.setSetting_value(btt_value);
			innerSystemSettingService.updateChannel(bttChannel);
			return "redirect:/innerSystemSetting/getSystemSetting.do";
		}
		
		//修改预订机器人一次处理订单量
		@RequestMapping("/updateRapnChannel.do")
		public String updateRapnChannel(HttpServletRequest request, HttpServletResponse response){
			String setting_id = this.getParam(request, "setting_id");
			String rapn_value = this.getParam(request, "rapn_value");
			SystemSettingVo rapnChannel = innerSystemSettingService.getSystemSettingById(setting_id);
			
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			Map<String, String> log = new HashMap<String, String>();
			log.put("content", opt_person + "更改了预订机器人一次处理订单量，更改前的值为" + rapnChannel.getSetting_value() + ",更改后的值为" + rapn_value);
			log.put("opt_person", opt_person);
			innerSystemSettingService.addSystemLog(log);
			rapnChannel.setSetting_value(rapn_value);
			innerSystemSettingService.updateChannel(rapnChannel);
			return "redirect:/innerSystemSetting/getSystemSetting.do";
		}
		
		//修改预约购票天数
		@RequestMapping("/updateBdnChannel.do")
		public String updateBdnChannel(HttpServletRequest request, HttpServletResponse response){
			String setting_id = this.getParam(request, "setting_id");
			String bdn_value = this.getParam(request, "bdn_value");
			SystemSettingVo bdnChannel = innerSystemSettingService.getSystemSettingById(setting_id);
			
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			Map<String, String> log = new HashMap<String, String>();
			log.put("content", opt_person + "更改了预约购票天数，更改前的值为" + bdnChannel.getSetting_value() + ",更改后的值为" + bdn_value);
			log.put("opt_person", opt_person);
			innerSystemSettingService.addSystemLog(log);
			bdnChannel.setSetting_value(bdn_value);
			innerSystemSettingService.updateChannel(bdnChannel);
			return "redirect:/innerSystemSetting/getSystemSetting.do";
		}
		
	}
