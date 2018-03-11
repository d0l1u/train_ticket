package com.l9e.transaction.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.ElongSettingService;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.util.PageUtil;

@Controller
@RequestMapping("/elongSetting")
public class ElongSettingController extends BaseController {
	@Resource 
	private ElongSettingService elongSettingService;
	
	@RequestMapping("/getElongSetting.do")
	public String getSystemSetting(HttpServletRequest request,
			HttpServletResponse response) {
		List<Map<String, String>> list = elongSettingService.querySetting();
		//Map<String, String> settingMap = list.get(0);
 		request.setAttribute("elongsetting", list);
		return "elongSetting/elongSettingList";
	}
	
	//更改艺龙通知   0、出票成功通知 1、预定成功通知
	@RequestMapping("/updateSetting.do")
	public String updateInterfaceChannel(HttpServletRequest request,
			HttpServletResponse response) {
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
		String opt_person = loginUserVo.getReal_name();//当前登录人 
		Map<String, String> log = new HashMap<String, String>();
		String setting_id = this.getParam(request, "setting_id");
		String channel =this.getParam(request, "channel");
		String setting_value="";
		if("elong".equals(channel)){
			setting_value =this.getParam(request, "out_notify_elong");
			channel="艺龙";
		}
		if("tongcheng".equals(channel)){
			setting_value =this.getParam(request, "out_notify_tongcheng");
			channel="同程";
		}
		log.put("setting_id", setting_id);
		log.put("setting_value", setting_value);
		elongSettingService.updateQunarSetting(log);
			log.put("opt_person", opt_person);
			log.put("content", opt_person+"更改了"+channel+"通知状态");
			elongSettingService.addSystemLog(log);
		return "redirect:/elongSetting/getElongSetting.do";
	}
	
	//更改艺龙核验  0、关 1、开
	@RequestMapping("/updateSettingCheck.do")
	public String updateSettingCheck(HttpServletRequest request,
			HttpServletResponse response) {
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
		String opt_person = loginUserVo.getReal_name();//当前登录人 
		Map<String, String> log = new HashMap<String, String>();
		String setting_id = this.getParam(request, "setting_id");
		String channel =this.getParam(request, "channel");
		String setting_value="";
		if("elong".equals(channel)){
			setting_value =this.getParam(request, "elong_check");
			channel="艺龙";}
		if("tongcheng".equals(channel)){
			setting_value =this.getParam(request, "tongcheng_check");
			channel="同程";}
		log.put("setting_id", setting_id);
		log.put("setting_value", setting_value);
		elongSettingService.updateQunarSetting(log);
			log.put("opt_person", opt_person);
			log.put("content", opt_person+"更改了"+channel+"核验开关状态");
			elongSettingService.addSystemLog(log);
		return "redirect:/elongSetting/getElongSetting.do";
	}
	
	//更改同程收单  0、关 1、开
	@RequestMapping("/changeTcGetOrder.do")
	public String changeTcGetOrder(HttpServletRequest request,
			HttpServletResponse response) {
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
		String opt_person = loginUserVo.getReal_name();//当前登录人 
		Map<String, String> log = new HashMap<String, String>();
		String setting_id = this.getParam(request, "setting_id");
		String setting_value=this.getParam(request, "tcgo_value");
		log.put("setting_id", setting_id);
		log.put("setting_value", setting_value);
		elongSettingService.updateQunarSetting(log);
		log.put("opt_person", opt_person);
		String str="";
		if("1".equals(setting_value)){
			str="收单";
		}else if("0".equals(setting_value)){
			str="停止收单";
		}
		log.put("content", opt_person+"更改同程收单状态为:"+str);
		elongSettingService.addSystemLog(log);
		return "redirect:/elongSetting/getElongSetting.do";
	}
	@RequestMapping("/querySystemSetList.do")
	public String querySystemSettingList(HttpServletRequest request, HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
		String opt_person = loginUserVo.getReal_name();//当前登录人 
		Map<String,Object>paramMap = new HashMap<String,Object>();
		/******************分页条件开始********************/
		int totalCount = elongSettingService.querySystemSetListCount();
		PageVo page = PageUtil.getInstance().paging(request, 10, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		/******************操作********************/
		List<Map<String,Object>> systemList = elongSettingService.querySystemSetList(paramMap);
		request.setAttribute("systemList", systemList);
		request.setAttribute("isShowList", 1);
		return "elongSetting/elongSystemSetList";
	}
	
	
	//更改艺龙核验  0、关 1、开
	@RequestMapping("/updateSettingCheckAuto.do")
	public String updateSettingCheckAuto(HttpServletRequest request,
			HttpServletResponse response) {
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
		String opt_person = loginUserVo.getReal_name();//当前登录人 
		Map<String, String> log = new HashMap<String, String>();
		String setting_id = this.getParam(request, "setting_id");
		String setting_value=this.getParam(request, "tongcheng_check_auto");
		log.put("setting_id", setting_id);
		log.put("setting_value", setting_value);
		elongSettingService.updateQunarSetting(log);
		log.put("opt_person", opt_person);
		String str = "";
		if("1".equals(setting_value))str = "【开启】";
		if("0".equals(setting_value))str = "【关闭】";
		log.put("content", opt_person+str+"了自动开启/关闭 【核验开关】状态");
		elongSettingService.addSystemLog(log);
		return "redirect:/elongSetting/getElongSetting.do";
	}
}
