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
import org.apache.log4j.Logger;
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
@RequestMapping("/trainSetting")
public class TrainSettingNewController extends BaseController {

	private static final Logger logger = Logger.getLogger(TrainSettingNewController.class);
		@Resource
		private TrainSystemSettingService trainSystemSettingService;
		
		//跳转页面
		@RequestMapping("/trainSettingPage.do")
		public String trainSettingPage(HttpServletRequest request,
				HttpServletResponse response) {
			return "redirect:/trainSetting/trainSettingList.do";
		}
		
		//查询
		@RequestMapping("/trainSettingList.do")
		public String trainSettingList(HttpServletRequest request,
				HttpServletResponse response) {
			List<SystemSettingVo> trainSetList =trainSystemSettingService.getSystemSetting();
			List<Map<String, Object>> newList = new ArrayList<Map<String,Object>>();
			for(int i=0;i<trainSetList.size();i++){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("setting_id", trainSetList.get(i).getSetting_id());
				map.put("setting_name", trainSetList.get(i).getSetting_name());
				map.put("setting_status", trainSetList.get(i).getSetting_status().equals("1")?"禁用":"启用");
				map.put("show_name", trainSetList.get(i).getShow_name());
				map.put("setting_desc", trainSetList.get(i).getSetting_desc());
				map.put("show_priority", trainSetList.get(i).getShow_priority());
				map.put("show_type", trainSetList.get(i).getShow_type());
				map.put("setting_value" , trainSetList.get(i).getSetting_value());
				if(StringUtils.isNotEmpty(trainSetList.get(i).getShow_list())){
					Map<String ,String> strmap = new HashMap<String, String>();
					for(String str : trainSetList.get(i).getShow_list().split(",")){
						strmap.put(str.split("=")[1], str.split("=")[0]);
					}
					map.put("showList", strmap);
				}else{
					map.put("showList", "");
				}
				newList.add(map);
			}
			
			request.setAttribute("trainSetList", newList);
			Map<String, String> codeInfo = trainSystemSettingService.queryCodeInfo();
			request.setAttribute("codeInfo", codeInfo);
			return "trainSettingNew/trainSetListNew";
		}
	
		//查看操作日志
		@RequestMapping("/queryHistory.do")
		public String queryHistory(HttpServletRequest request, HttpServletResponse response){
			Map<String,Object>paramMap = new HashMap<String,Object>();
			/******************分页条件开始********************/
			int totalCount = trainSystemSettingService.querySystemSetListCount();
			PageVo page = PageUtil.getInstance().paging(request, 10, totalCount);
			paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
			paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
			/******************操作********************/
			List<Map<String,Object>> history = trainSystemSettingService.querySystemSetList(paramMap);
			request.setAttribute("systemList", history);
			request.setAttribute("isShowList", 1);
			return "trainSettingNew/trainHistoryList";
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
			/*************************分页结束***************************/
			
			//获取表单参数
			String return_name=this.getParam(request, "return_name_find");
			String return_value=this.getParam(request, "return_value_find");
			String return_type=this.getParam(request, "return_type_find");
			String return_status=this.getParam(request, "return_status_find");
			String return_join=this.getParam(request, "return_join_find");
			String return_ticket=this.getParam(request, "return_ticket_find");
			String return_active=this.getParam(request, "return_active_find");
			String return_fail_reason=this.getParam(request, "return_fail_reason_find");
			
			
			paramMap.put("return_name", return_name);
			paramMap.put("return_value", return_value);
			paramMap.put("return_type", return_type);
			paramMap.put("return_status", return_status);
			paramMap.put("return_join", return_join);
			paramMap.put("return_ticket", return_ticket);
			paramMap.put("return_active", return_active);
			paramMap.put("return_fail_reason", return_fail_reason);
			
			
			List<Map<String,Object>> train_return_optlogList=trainSystemSettingService.querytrain_return_optlogList(paramMap);
			request.setAttribute("train_return_optlogList", train_return_optlogList);
			request.setAttribute("isShowList", 1);
			
			//显示上次填写的表单值
			request.setAttribute("list", paramMap);
			
			
			return "trainSettingNew/trainReturnOptlog";
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
			return "redirect:/trainSetting/gototrain_return_optlog.do?pageIndex="+pageIndex;
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
			return "redirect:/trainSetting/gototrain_return_optlog.do?pageIndex="+pageIndex;
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
			return "redirect:/trainSetting/gototrain_return_optlog.do?pageIndex="+pageIndex;
		}
		
		//状态修改（启用、禁用）
		@RequestMapping("/changeSettingStatus.do")
		public String changeCodeSetStatus(HttpServletRequest request, HttpServletResponse response){
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			SystemSettingVo systemSettingVo = trainSystemSettingService.getSystemSettingById(request.getParameter("setting_id"));
			String content="";
			if(systemSettingVo.getSetting_status().equals("1")){
				systemSettingVo.setSetting_status("0");
				content+=opt_person+"点击了【禁用】"+systemSettingVo.getShow_name();
			}else if(systemSettingVo.getSetting_status().equals("0")){
				systemSettingVo.setSetting_status("1");
				content+=opt_person+"点击了【启用】"+systemSettingVo.getShow_name();
			}
			Map<String, String> log = new HashMap<String, String>();
			log.put("content", content);
			log.put("opt_person", opt_person);
			trainSystemSettingService.updateURLStatus(systemSettingVo, log);
			return "redirect:/trainSetting/trainSettingPage.do";
		}
		
		//修改值（setting_value）
		@RequestMapping("/changeSettingValue.do")
		public String changeSettingValue(HttpServletRequest request, HttpServletResponse response){
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			SystemSettingVo systemSettingVo = trainSystemSettingService.getSystemSettingById(this.getParam(request,"setting_id"));
			String setting_value = this.getParam(request, "setting_value");
			String 	content = opt_person+"点击了【切换】"+systemSettingVo.getShow_name()+
			"切换前："+systemSettingVo.getSetting_value()+"切换后："+setting_value;
			systemSettingVo.setSetting_value(setting_value);
			Map<String, String> log = new HashMap<String, String>();
			log.put("content", content);
			log.put("opt_person", opt_person);
			trainSystemSettingService.updateInterface12306URL(systemSettingVo, log);
			if("code_type_channel".equals(systemSettingVo.getSetting_name())){//个别功能附加条件
				trainSystemSettingService.updateChannelRh(systemSettingVo);
			}
			return "redirect:/trainSetting/trainSettingPage.do";
		}
		
		
		//跳转到增加系统设置参数
		@RequestMapping("/turnToAddsettingPage.do")
		public String turnToAddsettingPage(HttpServletRequest request, HttpServletResponse response){
		return "trainSettingNew/trainAddSetting";
		}
		//增加系统设置
		@RequestMapping("/addsetting.do")
		public String addsetting(HttpServletRequest request, HttpServletResponse response){
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			SystemSettingVo systemSettingVo = new SystemSettingVo();
			String show_name = this.getParam(request, "show_name");
			String show_type = this.getParam(request, "show_type");
			String setting_name = this.getParam(request, "setting_name");
			String setting_value = this.getParam(request, "setting_value");
			String setting_desc = this.getParam(request, "setting_desc");
			List<String> show_list_name = this.getParamToList(request, "show_list_name");
			List<String> show_list_value = this.getParamToList(request, "show_list_value");
		    String show_list = "";
			    for(int i=0;i<show_list_name.size();i++){
			    	if(!"".equals(show_list_name.get(i))&& !"".equals(show_list_value.get(i))){
			    		show_list += show_list_name.get(i)+"="+show_list_value.get(i)+",";
			    	}
			    }
            systemSettingVo.setSetting_id(CreateIDUtil.createID("SD"));
		    systemSettingVo.setSetting_name(setting_name);
		    systemSettingVo.setSetting_value(setting_value);
		    systemSettingVo.setSetting_desc(setting_desc);
		    systemSettingVo.setShow_name(show_name);
		    systemSettingVo.setShow_type(show_type);
		    systemSettingVo.setShow_list(show_list);
		    Map<String, String> log = new HashMap<String, String>();
		    log.put("content", opt_person+"增加了新的系统设置");
		    log.put("opt_person", opt_person);
		    
		    trainSystemSettingService.addSetting(systemSettingVo,log);
		    
		    logger.info("show_name="+show_name+"show_type="+show_type+"setting_name="+setting_name+"setting_value="+setting_value+"setting_desc="+setting_desc+
						"show_list_name="+show_list_name.toString()+"show_list_value="+show_list_value.toString()+"show_list="+show_list);
		return "redirect:/trainSetting/trainSettingPage.do";
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
		
		//修改权重值
		@RequestMapping("/updateCodeWeight.do")
		public String updateCode01Weight(HttpServletRequest request,HttpServletResponse response){
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
			String real_name = loginUserVo.getReal_name();//获得当前的操作人的真实姓名
			String code01_weight = this.getParam(request, "code01_weight");
			String code02_weight = this.getParam(request, "code02_weight");
			String code03_weight = this.getParam(request, "code03_weight");
			String code04_weight = this.getParam(request, "code04_weight");
			Map<String, String> codeInfo = trainSystemSettingService.queryCodeInfo();
			Map<String,Object>paramMap = new HashMap<String,Object>();
			paramMap.put("code01_weight", code01_weight);
			paramMap.put("code02_weight", code02_weight);
			paramMap.put("code03_weight", code03_weight);
			paramMap.put("code04_weight", code04_weight);
			String content = real_name + 
			"修改了打码权重值，修改前的数据【"+codeInfo.get("code01_weight")
			+"/"+codeInfo.get("code02_weight")
			+"/"+codeInfo.get("code03_weight")
			+"/"+codeInfo.get("code04_weight")+"】，" +
			"修改后的数据【"+code01_weight+"/"+code02_weight+"/"+code03_weight+"/"+code04_weight+"】";
			logger.info(content);
			Map<String, String> log = new HashMap<String, String>();
			log.put("content", content);
			log.put("opt_person", real_name);
			trainSystemSettingService.updateCodeInfo(paramMap,log);
			return "redirect:/trainSetting/trainSettingPage.do";
		}
}
