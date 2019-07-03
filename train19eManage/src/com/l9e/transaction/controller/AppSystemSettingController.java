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
import com.l9e.transaction.service.AppSystemSettingService;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.transaction.vo.SystemSettingVo;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.PageUtil;

@Controller
@RequestMapping("/appSystemSetting")
public class AppSystemSettingController extends BaseController {
	
	@Resource
	private AppSystemSettingService appSystemSettingService;
	
	@RequestMapping("/getSystemSetting.do")
	public String getSystemSetting(HttpServletRequest request,
			HttpServletResponse response) {
		request.setAttribute("systemSetting", appSystemSettingService.getSystemSetting());
		request.setAttribute("systemBooks", SystemSettingVo.getSystemBooks());
		return "appSystemSetting/appSystemSetting";
	}
	
	
	//切换保险渠道：快保，合众
	@RequestMapping("/updateBxChannel.do")
	public String updateBxChannel(HttpServletRequest request,
			HttpServletResponse response){
		String setting_id = this.getParam(request, "setting_id");
		String bx_value = this.getParam(request, "bx_value");
		SystemSettingVo bxChannel = appSystemSettingService.getSystemSettingById(setting_id);
		
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
		String opt_person = loginUserVo.getReal_name();//当前登录人 
		Map<String, String> log = new HashMap<String, String>();
		log.put("content", opt_person + "更改了保险渠道，更改前的值为" + bxChannel.getSetting_value() + ",更改后的值为" + bx_value);
		log.put("opt_person", opt_person);
		appSystemSettingService.addSystemLog(log);
		bxChannel.setSetting_value(bx_value);
		appSystemSettingService.updateChannel(bxChannel);
		return "redirect:/appSystemSetting/getSystemSetting.do";
	}
	
	//切换短信渠道：19e，鼎鑫亿动
	@RequestMapping("/updateMsgChannel.do")
	public String updateMsgChannel(HttpServletRequest request, HttpServletResponse response){
		String setting_id = this.getParam(request, "setting_id");
		String msg_value = this.getParam(request, "msg_value");
		SystemSettingVo msgChannel = appSystemSettingService.getSystemSettingById(setting_id);
		
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
		String opt_person = loginUserVo.getReal_name();//当前登录人 
		Map<String, String> log = new HashMap<String, String>();
		log.put("content", opt_person + "更改了短信渠道，更改前的值为" + msgChannel.getSetting_value() + ",更改后的值为" + msg_value);
		log.put("opt_person", opt_person);
		appSystemSettingService.addSystemLog(log);
		msgChannel.setSetting_value(msg_value);
		appSystemSettingService.updateChannel(msgChannel);
		return "redirect:/appSystemSetting/getSystemSetting.do";
	}
	
	//切换默认乘车日期：当天，1条，2天，3天 
	@RequestMapping("/updateTtlChannel.do")
	public String updateTtlChannel(HttpServletRequest request, HttpServletResponse response){
		String setting_id = this.getParam(request, "setting_id");
		String ttl_value = this.getParam(request, "ttl_value");
		SystemSettingVo ttlChannel = appSystemSettingService.getSystemSettingById(setting_id);
		
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
		String opt_person = loginUserVo.getReal_name();//当前登录人 
		Map<String, String> log = new HashMap<String, String>();
		log.put("content", opt_person + "更改了默认乘车日期，更改前的值为" + ttlChannel.getSetting_value() + ",更改后的值为" + ttl_value);
		log.put("opt_person", opt_person);
		appSystemSettingService.addSystemLog(log);
		ttlChannel.setSetting_value(ttl_value);
		appSystemSettingService.updateChannel(ttlChannel);
		return "redirect:/appSystemSetting/getSystemSetting.do";
	}
	
	//切换余票阀值
	@RequestMapping("/updateStaChannel.do")
	public String updateStaChannel(HttpServletRequest request, HttpServletResponse response){
		String setting_id = this.getParam(request, "setting_id");
		String sta_value = this.getParam(request, "sta_value");
		SystemSettingVo staChannel = appSystemSettingService.getSystemSettingById(setting_id);
		
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
		String opt_person = loginUserVo.getReal_name();//当前登录人 
		Map<String, String> log = new HashMap<String, String>();
		log.put("content", opt_person + "更改了余票阀值数，更改前的值为" + staChannel.getSetting_value() + ",更改后的值为" + sta_value);
		log.put("opt_person", opt_person);
		appSystemSettingService.addSystemLog(log);
		staChannel.setSetting_value(sta_value);
		appSystemSettingService.updateChannel(staChannel);
		return "redirect:/appSystemSetting/getSystemSetting.do";
	}
	
	//切换默认排队时间
	@RequestMapping("/updateBuytime.do")
	public String updateBuytime(HttpServletRequest request, HttpServletResponse response){
		String setting_id = this.getParam(request, "setting_id");
		String btt_value = this.getParam(request, "btt_value");
		SystemSettingVo bttChannel = appSystemSettingService.getSystemSettingById(setting_id);
		
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
		String opt_person = loginUserVo.getReal_name();//当前登录人 
		Map<String, String> log = new HashMap<String, String>();
		log.put("content", opt_person + "开车前购票时间，更改前的值为" + bttChannel.getSetting_value() + ",更改后的值为" + btt_value);
		log.put("opt_person", opt_person);
		appSystemSettingService.addSystemLog(log);
		bttChannel.setSetting_value(btt_value);
		appSystemSettingService.updateChannel(bttChannel);
		return "redirect:/appSystemSetting/getSystemSetting.do";
	}
	
	//切换系统是否可以预订功能
	@RequestMapping("/changeSysBook.do")
	public String changeSysBook(HttpServletRequest request, HttpServletResponse response){
		String setting_id = this.getParam(request, "setting_id");
		List<String> system_books = this.getParamToList(request, "system_book");//1、微信WAP 2、IOS 3、ANDROID
		String setting_status = this.getParam(request, "setting_status");//状态：00启用 11禁用
		SystemSettingVo weatherBookChannel = appSystemSettingService.getSystemSettingById(setting_id);
		
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
		String opt_person = loginUserVo.getReal_name();//当前登录人 
		Map<String, String> log = new HashMap<String, String>();
		log.put("content", opt_person + "切换系统预订功能，更改前的值为" + weatherBookChannel.getSetting_value()+",状态为"+weatherBookChannel.getSetting_status() + ",更改后的值为" + system_books+",状态为"+setting_status+"。(其中1、微信WAP 2、IOS 3、ANDROID 状态：00启用 11禁用)");
		log.put("opt_person", opt_person);
		appSystemSettingService.addSystemLog(log);
		weatherBookChannel.setSetting_value(system_books.toString());
		weatherBookChannel.setSetting_status(setting_status);
		appSystemSettingService.updateChannel(weatherBookChannel);
		return "redirect:/appSystemSetting/getSystemSetting.do";
	}
	
	//查看操作日志
	@RequestMapping("/querySystemSetList.do")
	public String querySystemSettingList(HttpServletRequest request, HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
		String opt_person = loginUserVo.getReal_name();//当前登录人 
		Map<String,Object>paramMap = new HashMap<String,Object>();
		/******************分页条件开始********************/
		int totalCount = appSystemSettingService.querySystemSetListCount();
		PageVo page = PageUtil.getInstance().paging(request, 10, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		/******************操作********************/
		List<Map<String,Object>> systemList = appSystemSettingService.querySystemSetList(paramMap);
		request.setAttribute("systemList", systemList);
		request.setAttribute("isShowList", 1);
		return "appSystemSetting/appSystemSetList";
	}
	//增加12306新接口地址
	@RequestMapping("/turnToAddNewURLPage.do")
	public String turnToAddNewURL(HttpServletRequest request, HttpServletResponse response) {
		return "appSystemSetting/appAddNewURL";
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
		appSystemSettingService.addSystemLog(log);
		appSystemSettingService.addInterface12306URL(systemSettingVo, log);

		return "redirect:/appSystemSetting/getSystemSetting.do";
	}
	//修改12306新接口地址
	@RequestMapping("/updateInterface12306NewRUL.do")
	public String updateInterface12306NewRUL(HttpServletRequest request, HttpServletResponse response) {
		String urlId = request.getParameter("urlId");
		SystemSettingVo systemSettingVo = appSystemSettingService.getSystemSettingById(urlId);
		String urlstr = request.getParameter(urlId);
		systemSettingVo.setSetting_value(urlstr);
		Map<String, String> log = new HashMap<String, String>();
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
		String opt_person = loginUserVo.getReal_name();//当前登录人 
		log.put("content", "修改12306新接口地址,setting_id="+request.getParameter("urlId")+",value="+urlstr);
		log.put("opt_person", opt_person);
		appSystemSettingService.updateInterface12306URL(systemSettingVo, log);
		return "redirect:/appSystemSetting/getSystemSetting.do";
	}
	//切换12306新接口地址
	@RequestMapping("/changeNewURLStatus.do")
	public String changNewURLStatus(HttpServletRequest request, HttpServletResponse response){
		SystemSettingVo systemSettingVo = appSystemSettingService.getSystemSettingById(request.getParameter("urlId"));
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
		appSystemSettingService.updateURLStatus(systemSettingVo, log);

		return "redirect:/appSystemSetting/getSystemSetting.do";
	}
	//删除12306新接口地址
	@RequestMapping("/deleteNewURL.do")
	public String deleteNewURL(HttpServletRequest request, HttpServletResponse response){
		SystemSettingVo systemSettingVo = appSystemSettingService.getSystemSettingById(request.getParameter("urlId"));
		
		Map<String, String> log = new HashMap<String, String>();
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
		String opt_person = loginUserVo.getReal_name();//当前登录人 
		log.put("content", "删除12306新接口地址,setting_id="+request.getParameter("urlId")+",url="+systemSettingVo.getSetting_value());
		log.put("opt_person", opt_person);
		appSystemSettingService.deleteInterface12306URL(systemSettingVo, log);

		return "redirect:/appSystemSetting/getSystemSetting.do";
	}
	
}
