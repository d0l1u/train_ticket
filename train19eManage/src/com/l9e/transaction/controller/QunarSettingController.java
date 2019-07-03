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
import com.l9e.transaction.service.QunarSettingService;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.util.PageUtil;
@Controller
@RequestMapping("/qunarsetting")
public class QunarSettingController extends BaseController {
	
	@Resource 
	private QunarSettingService qunarSettingService;
	
	@RequestMapping("/getQunarSetting.do")
	public String getSystemSetting(HttpServletRequest request,
			HttpServletResponse response) {
		List<Map<String, String>> list = qunarSettingService.queryQunarSetting();
 		request.setAttribute("qunarsetting", list);
		return "setting/qunarsetting";
	}
	
	//更改去哪通知   0、出票成功通知 1、预定成功通知
	@RequestMapping("/updateSetting.do")
	public String updateInterfaceChannel(HttpServletRequest request,
			HttpServletResponse response) {
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
		String opt_person = loginUserVo.getReal_name();//当前登录人 
		Map<String, String> log = new HashMap<String, String>();
		String setting_id = this.getParam(request, "setting_id");
		String setting_value =this.getParam(request, "out_notify_qunar");
		log.put("setting_id", setting_id);
		log.put("setting_value", setting_value);
		qunarSettingService.updateQunarSetting(log);
		log.put("opt_person", opt_person);
		log.put("content", opt_person+"更改了去哪通知状态");
		if("1".equals(setting_value))log.put("content", opt_person+"通知状态更改为【预订成功通知】");
		else if("0".equals(setting_value))log.put("content", opt_person+"通知状态更改为【出票成功通知】");
		qunarSettingService.addSystemLog(log);
		return "redirect:/qunarsetting/getQunarSetting.do";
	}
	
	//更改去哪核验  0、关 1、开
	@RequestMapping("/updateSettingCheck.do")
	public String updateSettingCheck(HttpServletRequest request,
			HttpServletResponse response) {
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
		String opt_person = loginUserVo.getReal_name();//当前登录人 
		Map<String, String> log = new HashMap<String, String>();
		String setting_id = this.getParam(request, "setting_id");
		String setting_value =this.getParam(request, "qunar_check");
		log.put("setting_id", setting_id);
		log.put("setting_value", setting_value);
		qunarSettingService.updateQunarSetting(log);
		log.put("opt_person", opt_person);
		if("1".equals(setting_value))log.put("content", opt_person+"【开启】去哪核验开关");
		else if("0".equals(setting_value))log.put("content", opt_person+"【关闭】去哪核验开关");
		qunarSettingService.addSystemLog(log);
		return "redirect:/qunarsetting/getQunarSetting.do";
	}
	@RequestMapping("/querySystemSetList.do")
	public String querySystemSettingList(HttpServletRequest request, HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
		String opt_person = loginUserVo.getReal_name();//当前登录人 
		Map<String,Object>paramMap = new HashMap<String,Object>();
		/******************分页条件开始********************/
		int totalCount = qunarSettingService.querySystemSetListCount();
		PageVo page = PageUtil.getInstance().paging(request, 10, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		/******************操作********************/
		List<Map<String,Object>> systemList = qunarSettingService.querySystemSetList(paramMap);
		request.setAttribute("systemList", systemList);
		request.setAttribute("isShowList", 1);
		return "setting/qunarsettingHistory";
	}
}
