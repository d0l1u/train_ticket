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
import com.l9e.transaction.service.ExtSystemSettingService;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.transaction.vo.SystemSettingVo;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.PageUtil;
@Controller
@RequestMapping("/extSystemSetting")
public class ExtSystemSettingController extends BaseController {
	
	@Resource
	private ExtSystemSettingService extSystemSettingService;
	
	@RequestMapping("/getSystemSetting.do")
	public String getSystemSetting(HttpServletRequest request,
			HttpServletResponse response) {
		request.setAttribute("systemSetting", extSystemSettingService.getSystemSetting());
		return "extSystemSetting/extSystemSetting";
	}
	
	@RequestMapping("/updateInterfaceChannel.do")
	public String updateInterfaceChannel(HttpServletRequest request,
			HttpServletResponse response) {
		SystemSettingVo systemSettingVo = extSystemSettingService.getSystemSettingById(request.getParameter("channelId"));
		systemSettingVo.setSetting_value(request.getParameter("channel"));
		Map<String, String> log = new HashMap<String, String>();
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
		String opt_person = loginUserVo.getReal_name();//当前登录人 
		log.put("content", "切换接口value="+request.getParameter("channel"));
		log.put("opt_person", opt_person);
		extSystemSettingService.updateInterfaceChannel(systemSettingVo, log);

		return "redirect:/extSystemSetting/getSystemSetting.do";
	}
	
	@RequestMapping("/updateConTimeout.do")
	public String updateConTimeout(HttpServletRequest request,
			HttpServletResponse response) {
		String setting_id = request.getParameter("conTimeoutId");
		SystemSettingVo systemSettingVo = extSystemSettingService.getSystemSettingById(setting_id);
		systemSettingVo.setSetting_value(request.getParameter("con_timeout"));
		Map<String, String> log = new HashMap<String, String>();
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
		String opt_person = loginUserVo.getReal_name();//当前登录人 
		log.put("content", "修改连接超时时间"+request.getParameter("con_timeout"));
		log.put("opt_person", opt_person);
		extSystemSettingService.updateInterfaceConTimeout(systemSettingVo, log);

		return "redirect:/extSystemSetting/getSystemSetting.do";
	}
	
	@RequestMapping("/updateReadTimeout.do")
	public String updateReadTimeout(HttpServletRequest request, HttpServletResponse response) {
		SystemSettingVo systemSettingVo = extSystemSettingService.getSystemSettingById(request.getParameter("readTimeoutId"));
		systemSettingVo.setSetting_value(request.getParameter("read_timeout"));
		Map<String, String> log = new HashMap<String, String>();
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
		String opt_person = loginUserVo.getReal_name();//当前登录人 
		log.put("content", "修改读超时时间"+request.getParameter("read_timeout"));
		log.put("opt_person", opt_person);
		extSystemSettingService.updateInterfaceReadTimeout(systemSettingVo, log);

		return "redirect:/extSystemSetting/getSystemSetting.do";
	}
	
	@RequestMapping("/updateInterface12306RUL.do")
	public String updateInterface12306RUL(HttpServletRequest request, HttpServletResponse response) {
		String urlId = request.getParameter("urlId");
		SystemSettingVo systemSettingVo = extSystemSettingService.getSystemSettingById(urlId);
		String urlstr = request.getParameter(urlId);
		systemSettingVo.setSetting_value(urlstr);
		
		Map<String, String> log = new HashMap<String, String>();
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
		String opt_person = loginUserVo.getReal_name();//当前登录人 
		log.put("content", "修改12306接口地址,setting_id="+request.getParameter("urlId")+",value="+urlstr);
		log.put("opt_person", opt_person);
		extSystemSettingService.updateInterface12306URL(systemSettingVo, log);

		return "redirect:/extSystemSetting/getSystemSetting.do";
	}
	
	@RequestMapping("/changeURLStatus.do")
	public String changURLStatus(HttpServletRequest request, HttpServletResponse response){
		SystemSettingVo systemSettingVo = extSystemSettingService.getSystemSettingById(request.getParameter("urlId"));
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
		extSystemSettingService.updateURLStatus(systemSettingVo, log);

		return "redirect:/extSystemSetting/getSystemSetting.do";
	}
	
	@RequestMapping("/deleteURL.do")
	public String deleteURL(HttpServletRequest request, HttpServletResponse response){
		SystemSettingVo systemSettingVo = extSystemSettingService.getSystemSettingById(request.getParameter("urlId"));
		
		Map<String, String> log = new HashMap<String, String>();
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
		String opt_person = loginUserVo.getReal_name();//当前登录人 
		log.put("content", "删除12306接口地址,setting_id="+request.getParameter("urlId")+",url="+systemSettingVo.getSetting_value());
		log.put("opt_person", opt_person);
		extSystemSettingService.deleteInterface12306URL(systemSettingVo, log);

		return "redirect:/extSystemSetting/getSystemSetting.do";
	}
	
	@RequestMapping("/turnToAddURLPage.do")
	public String turnToAddURL(HttpServletRequest request, HttpServletResponse response) {
		return "extSystemSetting/extAddURL";
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
		extSystemSettingService.addSystemLog(log);
		extSystemSettingService.addInterface12306URL(systemSettingVo, log);

		return "redirect:/extSystemSetting/getSystemSetting.do";
	}
	
	@RequestMapping("/updateInterface12306NewRUL.do")
	public String updateInterface12306NewRUL(HttpServletRequest request, HttpServletResponse response) {
		String urlId = request.getParameter("urlId");
		SystemSettingVo systemSettingVo = extSystemSettingService.getSystemSettingById(urlId);
		String urlstr = request.getParameter(urlId);
		systemSettingVo.setSetting_value(urlstr);
		
		Map<String, String> log = new HashMap<String, String>();
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
		String opt_person = loginUserVo.getReal_name();//当前登录人 
		log.put("content", "修改12306新接口地址,setting_id="+request.getParameter("urlId")+",value="+urlstr);
		log.put("opt_person", opt_person);
		extSystemSettingService.updateInterface12306URL(systemSettingVo, log);

		return "redirect:/extSystemSetting/getSystemSetting.do";
	}
	
	@RequestMapping("/changeNewURLStatus.do")
	public String changNewURLStatus(HttpServletRequest request, HttpServletResponse response){
		SystemSettingVo systemSettingVo = extSystemSettingService.getSystemSettingById(request.getParameter("urlId"));
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
		extSystemSettingService.updateURLStatus(systemSettingVo, log);

		return "redirect:/extSystemSetting/getSystemSetting.do";
	}
	
	@RequestMapping("/deleteNewURL.do")
	public String deleteNewURL(HttpServletRequest request, HttpServletResponse response){
		SystemSettingVo systemSettingVo = extSystemSettingService.getSystemSettingById(request.getParameter("urlId"));
		
		Map<String, String> log = new HashMap<String, String>();
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
		String opt_person = loginUserVo.getReal_name();//当前登录人 
		log.put("content", "删除12306新接口地址,setting_id="+request.getParameter("urlId")+",url="+systemSettingVo.getSetting_value());
		log.put("opt_person", opt_person);
		extSystemSettingService.deleteInterface12306URL(systemSettingVo, log);

		return "redirect:/extSystemSetting/getSystemSetting.do";
	}
	
	@RequestMapping("/turnToAddNewURLPage.do")
	public String turnToAddNewURL(HttpServletRequest request, HttpServletResponse response) {
		return "extSystemSetting/extAddNewURL";
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
		extSystemSettingService.addSystemLog(log);
		extSystemSettingService.addInterface12306URL(systemSettingVo, log);

		return "redirect:/extSystemSetting/getSystemSetting.do";
	}
	
	@RequestMapping("/updatePayCtrl.do")
	public String updatePayCtrl(HttpServletRequest request,
			HttpServletResponse response) {
		String setting_id = this.getParam(request, "setting_id");
		String setting_value = this.getParam(request, "robot_pay_ctrl");
		if(StringUtils.isEmpty(setting_id) || StringUtils.isEmpty(setting_value)){
			return "redirect:/extSystemSetting/getSystemSetting.do";
		}
		SystemSettingVo systemSettingVo = new SystemSettingVo();
		systemSettingVo.setSetting_id(setting_id);
		systemSettingVo.setSetting_value(setting_value);
		
		Map<String, String> log = new HashMap<String, String>();
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
		String opt_person = loginUserVo.getReal_name();//当前登录人 
		log.put("content", "切换机器人支付控制,setting_id="+setting_id+",value="+setting_value);
		log.put("opt_person", opt_person);
		extSystemSettingService.updatePayCtrl(systemSettingVo, log);

		return "redirect:/extSystemSetting/getSystemSetting.do";
	}
	
	//切换保险渠道
	@RequestMapping("/updateBxChannel.do")
	public String updateBxChannel(HttpServletRequest request,
			HttpServletResponse response){
		String setting_id = this.getParam(request, "setting_id");
		String bx_value = this.getParam(request, "bx_value");
		SystemSettingVo bxChannel = extSystemSettingService.getSystemSettingById(setting_id);
		
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
		String opt_person = loginUserVo.getReal_name();//当前登录人 
		Map<String, String> log = new HashMap<String, String>();
		log.put("content", opt_person + "更改了保险渠道，更改前的值为" + bxChannel.getSetting_value() + ",更改后的值为" + bx_value);
		log.put("opt_person", opt_person);
		extSystemSettingService.addSystemLog(log);
		bxChannel.setSetting_value(bx_value);
		extSystemSettingService.updateChannel(bxChannel);
		return "redirect:/extSystemSetting/getSystemSetting.do";
	}
	
	//切换短信渠道
	@RequestMapping("/updateMsgChannel.do")
	public String updateMsgChannel(HttpServletRequest request, HttpServletResponse response){
		String setting_id = this.getParam(request, "setting_id");
		String msg_value = this.getParam(request, "msg_value");
		SystemSettingVo msgChannel = extSystemSettingService.getSystemSettingById(setting_id);
		
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
		String opt_person = loginUserVo.getReal_name();//当前登录人 
		Map<String, String> log = new HashMap<String, String>();
		log.put("content", opt_person + "更改了短信渠道，更改前的值为" + msgChannel.getSetting_value() + ",更改后的值为" + msg_value);
		log.put("opt_person", opt_person);
		extSystemSettingService.addSystemLog(log);
		msgChannel.setSetting_value(msg_value);
		extSystemSettingService.updateChannel(msgChannel);
		return "redirect:/extSystemSetting/getSystemSetting.do";
	}
	
	//切换默认乘车日期
	@RequestMapping("/updateTtlChannel.do")
	public String updateTtlChannel(HttpServletRequest request, HttpServletResponse response){
		String setting_id = this.getParam(request, "setting_id");
		String ttl_value = this.getParam(request, "ttl_value");
		SystemSettingVo ttlChannel = extSystemSettingService.getSystemSettingById(setting_id);
		
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
		String opt_person = loginUserVo.getReal_name();//当前登录人 
		Map<String, String> log = new HashMap<String, String>();
		log.put("content", opt_person + "更改了默认乘车日期，更改前的值为" + ttlChannel.getSetting_value() + ",更改后的值为" + ttl_value);
		log.put("opt_person", opt_person);
		extSystemSettingService.addSystemLog(log);
		ttlChannel.setSetting_value(ttl_value);
		extSystemSettingService.updateChannel(ttlChannel);
		return "redirect:/extSystemSetting/getSystemSetting.do";
	}
	
	//切换余票阀值
	@RequestMapping("/updateStaChannel.do")
	public String updateStaChannel(HttpServletRequest request, HttpServletResponse response){
		String setting_id = this.getParam(request, "setting_id");
		String sta_value = this.getParam(request, "sta_value");
		SystemSettingVo staChannel = extSystemSettingService.getSystemSettingById(setting_id);
		
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
		String opt_person = loginUserVo.getReal_name();//当前登录人 
		Map<String, String> log = new HashMap<String, String>();
		log.put("content", opt_person + "更改了余票阀值数，更改前的值为" + staChannel.getSetting_value() + ",更改后的值为" + sta_value);
		log.put("opt_person", opt_person);
		extSystemSettingService.addSystemLog(log);
		staChannel.setSetting_value(sta_value);
		extSystemSettingService.updateChannel(staChannel);
		return "redirect:/extSystemSetting/getSystemSetting.do";
	}
	
	//切换默认排队人数
	@RequestMapping("/updateQtaChannel.do")
	public String updateQtaChannel(HttpServletRequest request, HttpServletResponse response){
		String setting_id = this.getParam(request, "setting_id");
		String qta_value = this.getParam(request, "qta_value");
		SystemSettingVo qtaChannel = extSystemSettingService.getSystemSettingById(setting_id);
		
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
		String opt_person = loginUserVo.getReal_name();//当前登录人 
		Map<String, String> log = new HashMap<String, String>();
		log.put("content", opt_person + "更改了默认排队人数，更改前的值为" + qtaChannel.getSetting_value() + ",更改后的值为" + qta_value);
		log.put("opt_person", opt_person);
		extSystemSettingService.addSystemLog(log);
		qtaChannel.setSetting_value(qta_value);
		extSystemSettingService.updateChannel(qtaChannel);
		return "redirect:/extSystemSetting/getSystemSetting.do";
	}
	
	//切换默认排队时间
	@RequestMapping("/updateQttChannel.do")
	public String updateQttChannel(HttpServletRequest request, HttpServletResponse response){
		String setting_id = this.getParam(request, "setting_id");
		String qtt_value = this.getParam(request, "qtt_value");
		SystemSettingVo qttChannel = extSystemSettingService.getSystemSettingById(setting_id);
		
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
		String opt_person = loginUserVo.getReal_name();//当前登录人 
		Map<String, String> log = new HashMap<String, String>();
		log.put("content", opt_person + "更改了排队等候时间，更改前的值为" + qttChannel.getSetting_value() + ",更改后的值为" + qtt_value);
		log.put("opt_person", opt_person);
		extSystemSettingService.addSystemLog(log);
		qttChannel.setSetting_value(qtt_value);
		extSystemSettingService.updateChannel(qttChannel);
		return "redirect:/extSystemSetting/getSystemSetting.do";
	}
	//切换预定机器人版本
	@RequestMapping("/updateBookRobotChannel.do")
	public String updateBookRobotChannel(HttpServletRequest request, HttpServletResponse response){
		String setting_id = this.getParam(request, "setting_id");
		String book_robot_version = this.getParam(request, "book_robot_version");
		SystemSettingVo bookRobotChannel = extSystemSettingService.getSystemSettingById(setting_id);
		
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
		String opt_person = loginUserVo.getReal_name();//当前登录人 
		Map<String, String> log = new HashMap<String, String>();
		log.put("content", opt_person + "切换了预定机器人版本，更改前的值为" + bookRobotChannel.getSetting_value() + ",更改后的值为" + book_robot_version);
		log.put("opt_person", opt_person);
		extSystemSettingService.addSystemLog(log);
		bookRobotChannel.setSetting_value(book_robot_version);
		extSystemSettingService.updateChannel(bookRobotChannel);
		return "redirect:/extSystemSetting/getSystemSetting.do";
	}
	
	//切换前台是否可以预订功能
	@RequestMapping("/changeSysBook.do")
	public String changeSysBook(HttpServletRequest request, HttpServletResponse response){
		String setting_id = this.getParam(request, "setting_id");
		String weather_book = this.getParam(request, "weather_book");
		SystemSettingVo weatherBookChannel = extSystemSettingService.getSystemSettingById(setting_id);
		
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
		String opt_person = loginUserVo.getReal_name();//当前登录人 
		Map<String, String> log = new HashMap<String, String>();
		log.put("content", opt_person + "切换前台是否可预订功能，更改前的值为" + weatherBookChannel.getSetting_value() + ",更改后的值为" + weather_book);
		log.put("opt_person", opt_person);
		extSystemSettingService.addSystemLog(log);
		weatherBookChannel.setSetting_value(weather_book);
		extSystemSettingService.updateChannel(weatherBookChannel);
		return "redirect:/extSystemSetting/getSystemSetting.do";
	}
	
	//切换打码方式
	@RequestMapping("/changeCodeType.do")
	public String changeCodeType(HttpServletRequest request, HttpServletResponse response){
		String setting_id = this.getParam(request, "setting_id");
		String code_type = this.getParam(request, "code_type");
		SystemSettingVo codeTypeChannel = extSystemSettingService.getSystemSettingById(setting_id);
		
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
		String opt_person = loginUserVo.getReal_name();//当前登录人 
		Map<String, String> log = new HashMap<String, String>();
		log.put("content", opt_person + "切换打码方式功能，更改前的值为" + codeTypeChannel.getSetting_value() + ",更改后的值为" + code_type);
		log.put("opt_person", opt_person);
		extSystemSettingService.addSystemLog(log);
		codeTypeChannel.setSetting_value(code_type);
		extSystemSettingService.updateChannel(codeTypeChannel);
		return "redirect:/extSystemSetting/getSystemSetting.do";
	}
	
	@RequestMapping("/querySystemSetList.do")
	public String querySystemSettingList(HttpServletRequest request, HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
		String opt_person = loginUserVo.getReal_name();//当前登录人 
		Map<String,Object>paramMap = new HashMap<String,Object>();
		/******************分页条件开始********************/
		int totalCount = extSystemSettingService.querySystemSetListCount();
		PageVo page = PageUtil.getInstance().paging(request, 10, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		/******************操作********************/
		List<Map<String,Object>> systemList = extSystemSettingService.querySystemSetList(paramMap);
		request.setAttribute("systemList", systemList);
		request.setAttribute("isShowList", 1);
		return "extSystemSetting/extSystemSetList";
	}
	
	//切换默认排队时间
	@RequestMapping("/updateBuytime.do")
	public String updateBuytime(HttpServletRequest request, HttpServletResponse response){
		String setting_id = this.getParam(request, "setting_id");
		String btt_value = this.getParam(request, "btt_value");
		SystemSettingVo bttChannel = extSystemSettingService.getSystemSettingById(setting_id);
		
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
		String opt_person = loginUserVo.getReal_name();//当前登录人 
		Map<String, String> log = new HashMap<String, String>();
		log.put("content", opt_person + "开车前购票时间，更改前的值为" + bttChannel.getSetting_value() + ",更改后的值为" + btt_value);
		log.put("opt_person", opt_person);
		extSystemSettingService.addSystemLog(log);
		bttChannel.setSetting_value(btt_value);
		extSystemSettingService.updateChannel(bttChannel);
		return "redirect:/extSystemSetting/getSystemSetting.do";
	}
	
	//切换发车后退票时间
	@RequestMapping("/updateRefundtime.do")
	public String updateRefundtime(HttpServletRequest request, HttpServletResponse response){
		String setting_id = this.getParam(request, "setting_id");
		String btt_value = this.getParam(request, "srtt_value");
		SystemSettingVo bttChannel = extSystemSettingService.getSystemSettingById(setting_id);
		
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
		String opt_person = loginUserVo.getReal_name();//当前登录人 
		Map<String, String> log = new HashMap<String, String>();
		log.put("content", opt_person + "开车后退票时间，更改前的值为" + bttChannel.getSetting_value() + ",更改后的值为" + btt_value);
		log.put("opt_person", opt_person);
		extSystemSettingService.addSystemLog(log);
		bttChannel.setSetting_value(btt_value);
		extSystemSettingService.updateChannel(bttChannel);
		return "redirect:/extSystemSetting/getSystemSetting.do";
	}
	
	@RequestMapping("/updateNoticeSetting.do")
	public String updateNoticeSetting(HttpServletRequest request,
			HttpServletResponse response) {
		String setting_id = this.getParam(request, "setting_id");
		String notice_value = this.getParam(request, "notice_value");
		SystemSettingVo notice = extSystemSettingService.getSystemSettingById(setting_id);
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
		String opt_person = loginUserVo.getReal_name();//当前登录人 
		Map<String, String> log = new HashMap<String, String>();
		String setting_value = notice.getSetting_value();
		StringBuilder content = new StringBuilder();
		if("0".equals(setting_value)){
			notice.setSetting_value("1");
			content.append("将【出票成功通知】切换到【预订成功通知】");
		}else if("1".equals(setting_value)){
			notice.setSetting_value("0");
			content.append("将【预订成功通知】切换到【出票成功通知】");
		}
		log.put("content", content.toString());
		log.put("opt_person", opt_person);
		extSystemSettingService.addSystemLog(log);
		extSystemSettingService.updateNoticeSetting(notice);
		return "redirect:/extSystemSetting/getSystemSetting.do";
	}
}
