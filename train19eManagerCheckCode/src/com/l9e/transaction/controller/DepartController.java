package com.l9e.transaction.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.DepartService;
import com.l9e.transaction.service.LoginService;
import com.l9e.transaction.service.UserService;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.util.PageUtil;
/**
 * 部门管理员
 */
@Controller
@RequestMapping("/depart")
public class DepartController extends BaseController {
	private static final Logger logger = Logger.getLogger(DepartController.class);
	@Resource
	private DepartService departService ;
	@Resource
	private UserService userService ;
	@Resource
	private LoginService loginService ;
	
	/**
	 * 打码总况
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryDepartAdminPage.do")
	public String queryDepartAdminPage(HttpServletRequest request,HttpServletResponse response){
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String newDate = sdf.format(now); 
		//System.out.println(newDate);//获得当前系统时间
		SimpleDateFormat df1 = new java.text.SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(c.getTime().getTime() - 1000 * 60 * 60 * 24));
		String time = df1.format(c.getTime()); //获取前一天时间
		/******************************获取参数***********************************/
		String department = this.getParam(request, "department");
		//当天打码总数
		int codeCountToday = departService.queryDepartCodeCountToday(department);
		//当天打码正确总数
		int codeSuccessToday = departService.queryDepartCodeToday(department);
		//当天打码错误总数
		int codeFailToday = departService.queryDepartCodeFailToday(department);
		//当天打码超时总数
		int codeOvertimeToday = departService.queryDepartCodeOvertimeToday(department);
		//本周打码情况
		Map<String,Object> weekMap = departService.queryDepartCodeCountWeek(department);
		int adminCodeWeekSuccess = ((BigDecimal) weekMap.get("pic_success")).intValue();
		//本月打码情况
		Map<String,Object> monthMap = departService.queryDepartCodeCountMonth(department);
		int adminCodeMonthSuccess = ((BigDecimal) monthMap.get("pic_success")).intValue();
		//打码总情况
		Map<String,Object> totalMap = departService.queryDepartCodeCount(department);
		int adminCodeTotalSuccess = ((BigDecimal) totalMap.get("pic_success")).intValue();
		//昨日打码情况
		Map<String,Object> yesterdayMap = departService.queryDepartCodeCountYesterday(department);
		int adminCodeYesterdaySuccess = ((BigDecimal) yesterdayMap.get("pic_success")).intValue();
		
		request.setAttribute("newDate", newDate);
		request.setAttribute("time", time);
		request.setAttribute("department", department);
		request.setAttribute("codeCountToday", codeCountToday);
		request.setAttribute("codeSuccessToday", codeSuccessToday);
		request.setAttribute("codeFailToday", codeFailToday);
		request.setAttribute("codeOvertimeToday", codeOvertimeToday);
		request.setAttribute("yesterdayMap", yesterdayMap);
		request.setAttribute("weekMap", weekMap);
		request.setAttribute("monthMap", monthMap);
		request.setAttribute("totalMap", totalMap);
		request.setAttribute("adminCodeWeekSuccess", adminCodeWeekSuccess);
		request.setAttribute("adminCodeMonthSuccess", adminCodeMonthSuccess);
		request.setAttribute("adminCodeTotalSuccess", adminCodeTotalSuccess);
		request.setAttribute("adminCodeYesterdaySuccess", adminCodeYesterdaySuccess);
		request.setAttribute("userDepartment", LoginUserVo.getUser_department());
		return "depart/departAdminPage";
	}
	
	/**
	 * 当前打码
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryDepartCurrentPage.do")
	public String queryDepartCurrentPage(HttpServletRequest request, HttpServletResponse response){
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String newDate = sdf.format(now); //获得当前系统时间
		String department = this.getParam(request, "department");//所在部门
		/******************查询Map********************/
		Map<String,Object>paramMap = new HashMap<String,Object>();
		paramMap.put("department", department);
		/******************分页条件开始********************/
		int totalCount = departService.queryDepartCurrentNameCount(paramMap);//查询本部门当前有几个人在打码
		PageVo page = PageUtil.getInstance().paging(request, 15, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		
		List<Map<String, Object>> nameList = departService.queryAdminCurrentName(paramMap);
		List<Map<String,Object>> codeList = new ArrayList<Map<String,Object>>();
		Map<String, Object> nameMap = new HashMap<String, Object>();
		for(int i=0;i<nameList.size();i++){
			nameMap = nameList.get(i);
			String opt_ren = nameMap.get("username").toString();
			String opt_name = nameMap.get("real_name").toString();
			paramMap.put("opt_ren", opt_ren);
			//该用户当天打码情况
			int adminCodeTodayCount = departService.queryAdminCodeTodayCount(opt_ren);
			int adminCodeTodaySuccess = departService.queryAdminCodeTodaySuccess(opt_ren);
			int adminCodeTodayFail = departService.queryAdminCodeTodayFail(opt_ren);
			int adminCodeTodayOvertime = departService.queryAdminCodeTodayOvertime(opt_ren);
			if(adminCodeTodayCount>0){
				nameMap.put("adminCodeTodayCount", adminCodeTodayCount);
				nameMap.put("adminCodeTodaySuccess", adminCodeTodaySuccess);
				nameMap.put("adminCodeTodayFail", adminCodeTodayFail);
				nameMap.put("adminCodeTodayOvertime", adminCodeTodayOvertime);
				nameMap.put("department", department);
				codeList.add(nameMap);
			}
			
		}
		//对codeList按照adminCodeTodayCount进行排序
		Map currentMap1;  
		for (int k = 0; k < codeList.size() - 1; k++) {
			for (int j = 0; j < codeList.size() - k - 1; j++) {
				int m = (Integer) codeList.get(j).get("adminCodeTodayCount");
				int n = (Integer) codeList.get(j + 1).get("adminCodeTodayCount");
				if (m < n) {
					currentMap1 = codeList.get(j + 1);
					codeList.set(j + 1, codeList.get(j));
					codeList.set(j, currentMap1);
				}
			}
		}
		//int waitCodeCount = loginService.queryWaitCodeCount();//当前未打码总数
		int waitCodeCount = 0;
		//1：客服部、2：运营部、3：研发部、4：其他部门、5：对外部门(打码团队1)、00：对外部门02、6：同程部门、7：机票部门、8：代理商部门、9：艺龙部门
		if("5".equals(department)){
			waitCodeCount = loginService.queryWaitCodeQueenCount("11");//当前打码团队01未打码总数
		}else if("4".equals(department)){
			waitCodeCount = loginService.queryWaitCodeQueenCount("33");//当前打码团队03未打码总数
		}else if("00".equals(department)){
			waitCodeCount = loginService.queryWaitCodeQueenCount("22");//当前打码团队02未打码总数
		}else{
			waitCodeCount = loginService.queryWaitCodeQueenCount("99");//当前打码团队04未打码总数
		}
		
		request.setAttribute("waitCodeCount", waitCodeCount);
		request.setAttribute("isShowList", 1);
		request.setAttribute("codeList", codeList);
		request.setAttribute("newDate", newDate);
		Map<String,String> userDepartment = LoginUserVo.getUser_department();
		request.setAttribute("userDepartment", userDepartment);
		request.setAttribute("department", department);
		request.setAttribute("totalCount", totalCount);
		return "depart/departAdminCurrent";
	}
	
	/**
	 * 打码统计
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryDepartPicturePageList.do")
	public String queryDepartPicturePageList(HttpServletRequest request, HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user_department = loginUserVo.getDepartment();//获得当前的部门
		String user_level = loginUserVo.getUser_level();//获得当前的用户等级
		/******************查询条件********************/
		String begin_time =this.getParam(request, "begin_time");//开始时间
		String end_time = this.getParam(request, "end_time");//结束时间
		String department = this.getParam(request, "department");
		//String channel = this.getParam(request, "channel");//渠道
		/******************查询Map********************/
		Map<String,Object>paramMap = new HashMap<String,Object>();
		paramMap.put("begin_time", begin_time);
		paramMap.put("end_time", end_time);
		paramMap.put("department", department);
		//paramMap.put("channel", channel);
		/******************分页条件开始********************/
		int totalCount = departService.queryPictureCount(paramMap);
		PageVo page = PageUtil.getInstance().paging(request, 15, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		
		/******************查询开始********************/
		List<Map<String,String>> pictureList = departService.queryPictureList(paramMap);
		
		/******************Request绑定开始********************/
		request.setAttribute("pictureList", pictureList);
		request.setAttribute("begin_time", begin_time);
		request.setAttribute("end_time", end_time);
		request.setAttribute("department", department);
		request.setAttribute("userDepartment", LoginUserVo.getUser_department());
		//request.setAttribute("channel", channel);
		request.setAttribute("user_department", user_department);
		request.setAttribute("user_level", user_level);
		request.setAttribute("isShowList", 1);
		return "depart/departPictureList";
	}
	
	/**
	 * 按天查询
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryDepartByDay.do")
	public String queryDepartByDay(HttpServletRequest request, HttpServletResponse response){
		/******************查询条件********************/
		String begin_time =this.getParam(request, "begin_time");//开始时间
		String end_time = this.getParam(request, "end_time");//结束时间
		String department = this.getParam(request, "department");
		//List<String> date_List = loginService.queryDate_List();
		/******************查询Map********************/
		Map<String,Object>paramMap = new HashMap<String,Object>();
		paramMap.put("begin_time", begin_time);
		paramMap.put("end_time", end_time);
		paramMap.put("department", department);
		/******************分页条件开始********************/
		int totalCount = departService.queryDayPageCount(paramMap);
		PageVo page = PageUtil.getInstance().paging(request, 15, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		/******************查询开始********************/
		List<Map<String,String>> codeList = departService.queryDayPageList(paramMap);
		request.setAttribute("department", department);
		request.setAttribute("begin_time", begin_time);
		request.setAttribute("end_time", end_time);
		request.setAttribute("codeList", codeList);
		request.setAttribute("isShowList", 1);
		request.setAttribute("userDepartment", LoginUserVo.getUser_department());
		return "depart/departAdminDayList";
	}
	
	/**
	 * 打码用户
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryDepartByAdmin.do")
	public String queryDepartByAdmin(HttpServletRequest request, HttpServletResponse response){
		SimpleDateFormat df1 = new java.text.SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(c.getTime().getTime() - 1000 * 60 * 60 * 24));
		String time = df1.format(c.getTime()); // 前一天时间
		/******************查询条件********************/
		String username =this.getParam(request, "username");//用户名或者真实姓名
		String real_name = this.getParam(request, "real_name");
		String department = this.getParam(request, "department");
		/******************查询Map********************/
		Map<String,Object>paramMap = new HashMap<String,Object>();
		paramMap.put("opt_ren", username);
		paramMap.put("opt_name", real_name);
		paramMap.put("department", department);
		/******************分页条件开始********************/
		int totalCount = departService.queryByAdminPageCount(paramMap);
		PageVo page = PageUtil.getInstance().paging(request, 15, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		/******************查询开始********************/
		List<Map<String,String>> adminCodeList = departService.queryAdminCodeList(paramMap);
		request.setAttribute("real_name", real_name);
		request.setAttribute("username", username);
		request.setAttribute("adminCodeList", adminCodeList);
		request.setAttribute("isShowList", 1);
		request.setAttribute("time", time);
		request.setAttribute("department", department);
		request.setAttribute("userDepartment", LoginUserVo.getUser_department());
		return "depart/departAdminInfo";
	}
	
	
	/**
	 * 进入添加管理员页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/toAddDepartAdminPage.do")
	public String toAddDepartAdminPage(HttpServletRequest request ,HttpServletResponse response){
		String department = this.getParam(request, "department");
		request.setAttribute("department", department);
		request.setAttribute("userType", LoginUserVo.getUser_type());
		request.setAttribute("userDepartment", LoginUserVo.getUser_department());
		return "depart/addDepartAdminPage";
	}
	
	/**
	 * 添加管理员
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/addDepartAdminPage.do")
	public String addDepartAdminPage(HttpServletRequest request ,HttpServletResponse response){
		//String user_id = CreateIDUtil.createID("US");
		String user_name = this.getParam(request, "user_name");
		String real_name = this.getParam(request, "real_name");
		String password = this.getParam(request, "password");
		String department = this.getParam(request, "department");
		String email = this.getParam(request, "email");
		String user_phone =this.getParam(request, "user_phone");
		String user_level = this.getParam(request, "user_level");
		/***************************Map存储并执行操作*****************************/
		Map<String,String>add_Map = new HashMap<String,String>();
		//add_Map.put("user_id", user_id);
		add_Map.put("username", user_name);
		add_Map.put("real_name", real_name);
		add_Map.put("pwd", password);
		add_Map.put("department", department);
		add_Map.put("email", email);
		add_Map.put("telephone", user_phone);
		add_Map.put("user_level", user_level);
		departService.addAdmin(add_Map);
		request.setAttribute("department", department);
		request.setAttribute("userDepartment", LoginUserVo.getUser_department());
		return "redirect:/depart/queryDepartAdminUserList.do?department="+department;
	}
	
	/**
	 * 删除部门用户
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/deleteDepartAdmin.do")
	public String deleteDepartAdmin(HttpServletRequest request,HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String real_name = loginUserVo.getReal_name();//获得当前的操作人的真实姓名
		String opt_ren = this.getParam(request, "opt_ren");
		String department = this.getParam(request, "department");
		departService.deleteDepartAdmin(opt_ren);
		logger.info(department+"‘部门管理员:’"+real_name+"点击了【删除】部门用户信息，删除了用户："+opt_ren);
		request.setAttribute("department", department);
		return "redirect:/depart/queryDepartAdminUserList.do?department="+department;
	}
	
	
	
	/**
	 * 用户查询
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryDepartAdminUserList.do")
	public String queryDepartAdminUserList(HttpServletRequest request,HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String opt_ren = loginUserVo.getUsername();
		/******************查询条件********************/
		String username = this.getParam(request, "username");//用户名或者真实姓名
		String real_name = this.getParam(request, "real_name");
		String department = this.getParam(request, "department");
		//String department = departService.queryDepartment(opt_ren);
		/******************查询Map********************/
		Map<String,Object>paramMap = new HashMap<String,Object>();
		paramMap.put("username", username);
		paramMap.put("real_name", real_name);
		paramMap.put("department", department);
		/******************分页条件开始********************/
		int totalCount = departService.queryDepartAdminUserListPageCount(paramMap);
		PageVo page = PageUtil.getInstance().paging(request, 15, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		/******************查询开始********************/
		List<Map<String,Object>> adminList = departService.queryDepartAdminUserList(paramMap);
		request.setAttribute("real_name", real_name);
		request.setAttribute("username", username);
		request.setAttribute("adminList", adminList);
		request.setAttribute("isShowList", 1);
		request.setAttribute("department", department);
		request.setAttribute("userDepartment", LoginUserVo.getUser_department());
		request.setAttribute("userLoginStatus", LoginUserVo.getLoginStatus());
		request.setAttribute("userLevel", LoginUserVo.getUser_type());
		return "depart/departAdminUser";
	}
	
	/**
	 * 查询部门用户的明细
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryDepartAdminUserInfo.do")
	public String queryDepartAdminUserInfo(HttpServletRequest request, HttpServletResponse response){
		String opt_ren = this.getParam(request, "opt_ren");
		String department = this.getParam(request, "department");
		Map<String, String> userMap = userService.queryUserInfo(opt_ren);
		request.setAttribute("userMap", userMap);
		request.setAttribute("department", department);
		request.setAttribute("userDepartment", LoginUserVo.getUser_department());
		return "depart/departAdminUpdate";
	}
	
	/**
	 * 修改用户信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/updateDepartAdminUser.do")
	public String updateDepartAdminUser(HttpServletRequest request, HttpServletResponse response){
		String user_id = this.getParam(request, "user_id");
		String password = this.getParam(request, "password");
		String department = this.getParam(request, "department");
		String email = this.getParam(request, "email");
		String user_phone =this.getParam(request, "user_phone");
		String username = this.getParam(request, "username");
		String get_code_time_limit = this.getParam(request, "get_code_time_limit");
		/***************************Map存储并执行操作*****************************/
		Map<String,String> map = new HashMap<String,String>();
		map.put("user_id", user_id);
		map.put("pwd", password);
		map.put("department", department);
		map.put("email", email);
		map.put("telephone", user_phone);
		map.put("get_code_time_limit", get_code_time_limit);
		userService.updateUser(map);
		request.setAttribute("department", department);
		return "redirect:/depart/queryDepartAdminUserList.do?department="+department;
	}
	
}
