package com.l9e.transaction.controller;

import java.io.IOException;
import java.io.PrintWriter;
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
import com.l9e.transaction.service.LoginService;
import com.l9e.transaction.service.UserService;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.util.PageUtil;
/**
 * 超级管理员
 */
@Controller
@RequestMapping("/login")
public class LoginController extends BaseController{
	private static final Logger logger = 
		Logger.getLogger(LoginController.class);

	@Resource
	private LoginService loginService ;
	@Resource
	private UserService userService ;
	
	@RequestMapping("login.do")
	@ResponseBody
	public String login(HttpServletRequest request,HttpServletResponse response){
		/***************************获取用户名和密码*****************************/
		String username = this.getParam(request, "user_name");
		String pwd = this.getParam(request, "pwd");
		//String checkCode=this.getParam(request, "checkCode");   //验证码
		/***************************Map存储并查询*****************************/
		Map<String,String>query_Map = new HashMap<String,String>();
		query_Map.put("username", username);
		query_Map.put("pwd", pwd);
		Map<String,Object>userInfo_Map = loginService.queryLogin_UserInfo(query_Map);  //查询用户名和密码是否正确
		Integer monthMoney = loginService.queryAdminCodeMonthMoney(username);//查询用户的本月的打码金额
		String logined = null;
		if(userInfo_Map!=null){
			logined = userInfo_Map.get("logined").toString();//判断用户是否登陆过打码系统，是否改过密码 
		}
		/***************************Session存储AJAX返回*****************************/
		//String sessionCheckCode = request.getSession().getAttribute(RandomValidateCode.RANDOMCODEKEY).toString();
		String result = null;
		
		if(userInfo_Map==null){
			result="no";
//		}else if (sessionCheckCode==null || !sessionCheckCode.equalsIgnoreCase(checkCode)){
//			result="checkCodeNo";
		//}else if(userInfo_Map!=null && sessionCheckCode.equalsIgnoreCase(checkCode) ){ 
		}else if(logined.equals("0")){ //第一次登录系统，还未修改过密码
			LoginUserVo loginUserVo = new LoginUserVo();
			loginUserVo.setUser_id((Integer)userInfo_Map.get("user_id"));
			loginUserVo.setUsername((String)userInfo_Map.get("username"));
			loginUserVo.setPwd(query_Map.get("password"));
			loginUserVo.setReal_name((String)userInfo_Map.get("real_name"));
			loginUserVo.setLogin_status((Integer)userInfo_Map.get("login_status"));//登录状态
			loginUserVo.setCode_total_number((Integer)userInfo_Map.get("code_total_number"));
			loginUserVo.setCode_error_number((Integer)userInfo_Map.get("code_error_number"));
			loginUserVo.setLast_login_time((Date)userInfo_Map.get("last_login_time"));
			loginUserVo.setDepartment((String)userInfo_Map.get("department"));
			loginUserVo.setTelephone((String)userInfo_Map.get("telephone"));
			loginUserVo.setEmail((String)userInfo_Map.get("email"));
			loginUserVo.setUser_level((String)userInfo_Map.get("user_level"));
			loginUserVo.setMonthMoney(monthMoney.toString());
			loginUserVo.setLogined((String)userInfo_Map.get("logined"));
			request.getSession().setAttribute("loginUserVo",loginUserVo);
			/**********************若修改过密码，则logined置为1**************************/
			Map<String,String> add_Map = new HashMap<String,String>();
			add_Map.put("user_id", (String) userInfo_Map.get("user_id").toString());
			add_Map.put("user_name", (String) userInfo_Map.get("user_name"));
			add_Map.put("logined", (String) userInfo_Map.get("logined"));
			//add_Map.put("ip", request.getRemoteAddr());
			//////loginService.updateLoginTimeAndIp(add_Map);
			//request.getSession().removeAttribute("loginUserVo");
			result="yes";
		}else{//已经改过密码
			LoginUserVo loginUserVo = new LoginUserVo();
			loginUserVo.setUser_id((Integer)userInfo_Map.get("user_id"));
			loginUserVo.setUsername((String)userInfo_Map.get("username"));
			loginUserVo.setPwd(query_Map.get("password"));
			loginUserVo.setReal_name((String)userInfo_Map.get("real_name"));
			loginUserVo.setLogin_status((Integer)userInfo_Map.get("login_status"));//登录状�1�7�1�7
			loginUserVo.setCode_total_number((Integer)userInfo_Map.get("code_total_number"));
			loginUserVo.setCode_error_number((Integer)userInfo_Map.get("code_error_number"));
			loginUserVo.setLast_login_time((Date)userInfo_Map.get("last_login_time"));
			loginUserVo.setDepartment((String)userInfo_Map.get("department"));
			loginUserVo.setTelephone((String)userInfo_Map.get("telephone"));
			loginUserVo.setEmail((String)userInfo_Map.get("email"));
			loginUserVo.setUser_level((String)userInfo_Map.get("user_level"));
			loginUserVo.setLogined((String)userInfo_Map.get("logined"));
			loginUserVo.setMonthMoney(monthMoney.toString());
			request.getSession().setAttribute("loginUserVo",loginUserVo);
//			/**********************若修改过密码，则logined置为1**************************/
//			Map<String,String> add_Map = new HashMap<String,String>();
//			add_Map.put("user_id", (String) userInfo_Map.get("user_id").toString());
//			add_Map.put("user_name", (String) userInfo_Map.get("user_name"));
//			add_Map.put("logined", (String) userInfo_Map.get("logined"));
//			//add_Map.put("ip", request.getRemoteAddr());
//			loginService.updateLoginTimeAndIp(add_Map);
			result="yes";
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
	
	
	
	/**
	 * 进入用户管理页面
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/loginUserPage.do")
	public String queryLoginUserPage(HttpServletResponse response ,HttpServletRequest request){
		/***************************获取Session*****************************/
		LoginUserVo loginUserVo = (LoginUserVo)request.getSession().getAttribute("loginUserVo");
		String user_level = loginUserVo.getUser_level();
		if(user_level.equals("1")){//管理员
			return "redirect:/login/queryAdminPage.do";
		}else{
			return "redirect:/login/loginUserList.do"; //普通用户
		}
	}
	
	/**
	 * 管理员页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryAdminPage.do")
	public String queryAdminPage(HttpServletRequest request,HttpServletResponse response){
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String newDate = sdf.format(now); 
		//System.out.println(newDate);//获得当前系统时间
		SimpleDateFormat df1 = new java.text.SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(c.getTime().getTime() - 1000 * 60 * 60 * 24));
		String time = df1.format(c.getTime()); //获取前一天时间
		/**********************获取查询条件，并存储到Map****************************/
		String department = this.getParam(request, "department");//查询条件
		Map<String,Object>paramMap = new HashMap<String,Object>();
		paramMap.put("department", department);
		
		//当天打码总数
		int codeCountToday = loginService.queryCodeCountToday(paramMap);
		//当天打码正确总数
		int codeSuccessToday = loginService.queryCodeToday(paramMap);
		//当天打码错误总数
		int codeFailToday = loginService.queryCodeFailToday(paramMap);
		//当天打码超时总数
		int codeOvertimeToday = loginService.queryCodeOvertimeToday(paramMap);
		
		//本周打码情况
		Map<String,Object> weekMap = loginService.queryCodeCountWeek(paramMap);
		int adminCodeWeekSuccess = ((BigDecimal) weekMap.get("pic_success")).intValue();
		//本月打码情况
		Map<String,Object> monthMap = loginService.queryCodeCountMonth(paramMap);
		int adminCodeMonthSuccess = ((BigDecimal) monthMap.get("pic_success")).intValue();
		//打码总情况
		Map<String,Object> totalMap = loginService.queryCodeCount(paramMap);
		int adminCodeTotalSuccess = ((BigDecimal) totalMap.get("pic_success")).intValue();
		//昨日打码情况
		Map<String,Object> yesterdayMap = loginService.queryCodeCountYesterday(paramMap);
		int adminCodeYesterdaySuccess = ((BigDecimal) yesterdayMap.get("pic_success")).intValue();
		
		request.setAttribute("newDate", newDate);
		request.setAttribute("time", time);
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
		request.setAttribute("department", department);
		return "login/adminPage";
	}
	/**
	 * 进入添加管理员页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/toAddAdminPage.do")
	public String toAddAdminPage(HttpServletRequest request ,HttpServletResponse response){
		request.setAttribute("userType", LoginUserVo.getUser_type());
		request.setAttribute("userDepartment", LoginUserVo.getUser_department());
		return "login/addAdminPage";
	}
	
	/**
	 * 添加管理员
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/addAdminPage.do")
	public String addAdminPage(HttpServletRequest request ,HttpServletResponse response){
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
		loginService.addAdmin(add_Map);
		return "redirect:/login/queryAdminUserList.do";
	}
	@RequestMapping("/queryUsername.do")
	@ResponseBody
	public String queryUsername(HttpServletRequest request ,HttpServletResponse response){
		String user_name=this.getParam(request, "user_name");
		String result = null;
		if(StringUtils.isEmpty(loginService.queryUsername(user_name))){
			result="yes";
		}else{
			result="no";
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
	
	@RequestMapping("/queryRealname.do")
	@ResponseBody
	public String queryRealname(HttpServletRequest request ,HttpServletResponse response){
		String real_name=this.getParam(request, "real_name");
		String result = null;
		int res = loginService.queryRealname(real_name);
		if(res!=0){
			result="no";
		}else{
			result="yes";
		}
		try{
			response.getWriter().write(result);
			response.getWriter().flush();
			response.getWriter().close();
		}catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 用户登出系统
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/logOutUser.do")
	public void logOutUser(HttpServletResponse response,HttpServletRequest request){
		request.getSession().removeAttribute("loginUserVo");
		PrintWriter out;
		try {
			out = response.getWriter();
			StringBuilder builder = new StringBuilder();
			builder.append("<script type=\"text/javascript\" charset=\"UTF-8\">");
			builder.append("window.top.location.href=\"");
			//builder.append("/pages/login/login.jsp\";</script>");
			builder.append("/login.jsp\";</script>");
			out.print(builder.toString());
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 按天查询
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryByDay.do")
	public String queryByDay(HttpServletRequest request,HttpServletResponse response){
		/******************查询条件********************/
		String begin_time =this.getParam(request, "begin_time");//开始时间
		String end_time = this.getParam(request, "end_time");//结束时间
		//List<String> date_List = loginService.queryDate_List();
		/******************查询Map********************/
		Map<String,Object>paramMap = new HashMap<String,Object>();
		paramMap.put("begin_time", begin_time);
		paramMap.put("end_time", end_time);
		/******************分页条件开始********************/
		int totalCount = loginService.queryDayPageCount(paramMap);
		PageVo page = PageUtil.getInstance().paging(request, 15, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		/******************查询开始********************/
		List<Map<String,String>> codeList = loginService.queryDayPageList(paramMap);
		request.setAttribute("begin_time", begin_time);
		request.setAttribute("end_time", end_time);
		request.setAttribute("codeList", codeList);
		request.setAttribute("isShowList", 1);
		return "login/dayAdminList";
	}
	
	/**
	 * 按用户查询
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryByAdmin.do")
	public String queryByAdmin(HttpServletRequest request,HttpServletResponse response){
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
		int totalCount = loginService.queryByAdminPageCount(paramMap);
		PageVo page = PageUtil.getInstance().paging(request, 15, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		/******************查询开始********************/
		List<Map<String,String>> adminCodeList = loginService.queryAdminCodeList(paramMap);
		request.setAttribute("real_name", real_name);
		request.setAttribute("username", username);
		request.setAttribute("adminCodeList", adminCodeList);
		request.setAttribute("isShowList", 1);
		request.setAttribute("time", time);
		request.setAttribute("department", department);
		request.setAttribute("userDepartment", LoginUserVo.getUser_department());
		return "login/adminInfo";
	}
	@RequestMapping("/queryByAdminInfo.do")
	public String queryByAdminInfo(HttpServletRequest request,HttpServletResponse response){
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String newDate = sdf.format(now); //获得当前系统时间
		SimpleDateFormat df1 = new java.text.SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(c.getTime().getTime() - 1000 * 60 * 60 * 24));
		String yesterday = df1.format(c.getTime()); // 前一天时间
		/******************查询条件********************/
		String opt_ren =this.getParam(request, "opt_ren");//用户名 
		//String opt_name =this.getParam(request, "opt_name");//真实姓名
		String opt_name = loginService.queryOptname(opt_ren);
		/******************查询Map********************/
		Map<String,Object>paramMap = new HashMap<String,Object>();
		paramMap.put("opt_ren", opt_ren);
		paramMap.put("newDate", newDate);
		/******************查询开始********************/
		//用户上月打码收入
		int successPreCount = loginService.querySuccessPreCount(opt_ren);
		//该用户当天打码情况
		int adminCodeTodayCount = loginService.queryAdminCodeTodayCount(opt_ren);
		int adminCodeTodaySuccess = loginService.queryAdminCodeTodaySuccess(opt_ren);
		int adminCodeTodayFail = loginService.queryAdminCodeTodayFail(opt_ren);
		int adminCodeTodayOvertime = loginService.queryAdminCodeTodayOvertime(opt_ren);
		//该用户昨天打码情况
		Map<String,Object> yesterdayMap = loginService.queryAdminCodeYesterday(opt_ren);
		int adminCodeYesterdaySuccess = ((BigDecimal) yesterdayMap.get("pic_success")).intValue();
		//该用户本周打码情况
		Map<String,Object> weekMap = loginService.queryAdminCodeWeek(opt_ren);
		int adminCodeWeekSuccess = ((BigDecimal) weekMap.get("pic_success")).intValue();
		//该用户本月打码情况
		Map<String,Object> monthMap = loginService.queryAdminCodeMonth(opt_ren);
		int adminCodeMonthSuccess = ((BigDecimal) monthMap.get("pic_success")).intValue();
		//该用户总打码情况
		Map<String,Object> countMap = loginService.queryAdminCodeInfoSum(opt_ren);
		int adminCodeTotalSuccess = ((BigDecimal) countMap.get("pic_success")).intValue();
		
		request.setAttribute("successPreCount", successPreCount);
		
		request.setAttribute("adminCodeTodayCount", adminCodeTodayCount);
		request.setAttribute("adminCodeTodaySuccess", adminCodeTodaySuccess);
		request.setAttribute("adminCodeTodayFail", adminCodeTodayFail);
		request.setAttribute("adminCodeTodayOvertime", adminCodeTodayOvertime);
		request.setAttribute("yesterdayMap", yesterdayMap);
		request.setAttribute("adminCodeYesterdaySuccess", adminCodeYesterdaySuccess);
		request.setAttribute("weekMap", weekMap);
		request.setAttribute("adminCodeWeekSuccess", adminCodeWeekSuccess);
		request.setAttribute("monthMap", monthMap);
		request.setAttribute("adminCodeMonthSuccess", adminCodeMonthSuccess);
		request.setAttribute("countMap", countMap);
		request.setAttribute("adminCodeTotalSuccess", adminCodeTotalSuccess);
		request.setAttribute("yesterday", yesterday);
		request.setAttribute("opt_ren", opt_ren);
		request.setAttribute("opt_name", opt_name);
		request.setAttribute("time", newDate);
		return "login/adminInfoList";
	}
	
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryCurrentPage.do")
	public String queryCurrentPage(HttpServletRequest request, HttpServletResponse response){
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String newDate = sdf.format(now); //获得当前系统时间
		String department = this.getParam(request, "department");//所在部门
		/******************查询Map********************/
		Map<String,Object>paramMap = new HashMap<String,Object>();
		paramMap.put("department", department);
		/******************分页条件开始********************/
		int totalCount = loginService.queryAdminCurrentNameCount(paramMap);
		PageVo page = PageUtil.getInstance().paging(request, 15, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		
		List<Map<String, Object>> nameList = loginService.queryAdminCurrentName(paramMap);
		List<Map<String,Object>> codeList = new ArrayList<Map<String,Object>>();
		Map<String, Object> nameMap = new HashMap<String, Object>();
		for(int i=0;i<nameList.size();i++){
			nameMap = nameList.get(i);
			String opt_ren = nameMap.get("username").toString();
			String opt_name = nameMap.get("real_name").toString();
			paramMap.put("opt_ren", opt_ren);
			String department1 = loginService.queryAdminDepartment(opt_ren);
			//该用户当天打码情况
			int adminCodeTodayCount = loginService.queryAdminCodeTodayCount(opt_ren);
			int adminCodeTodaySuccess = loginService.queryAdminCodeTodaySuccess(opt_ren);
			int adminCodeTodayFail = loginService.queryAdminCodeTodayFail(opt_ren);
			int adminCodeTodayOvertime = loginService.queryAdminCodeTodayOvertime(opt_ren);
			if(adminCodeTodayCount>0){
				nameMap.put("adminCodeTodayCount", adminCodeTodayCount);
				nameMap.put("adminCodeTodaySuccess", adminCodeTodaySuccess);
				nameMap.put("adminCodeTodayFail", adminCodeTodayFail);
				nameMap.put("adminCodeTodayOvertime", adminCodeTodayOvertime);
				nameMap.put("department", department1);
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

		request.setAttribute("isShowList", 1);
		request.setAttribute("codeList", codeList);
		request.setAttribute("newDate", newDate);
		Map<String,String> userDepartment = LoginUserVo.getUser_department();
		request.setAttribute("userDepartment", userDepartment);
		request.setAttribute("department", department);
		request.setAttribute("totalCount", totalCount);
		return "login/adminCurrent";
	}
	
	@RequestMapping("/deleteAdmin.do")
	public String deleteAdmin(HttpServletRequest request,HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String real_name = loginUserVo.getReal_name();//获得当前的操作人的真实姓名
		
		String opt_ren = this.getParam(request, "opt_ren");
		logger.info(real_name+"‘超级管理员’点击了【删除】"+opt_ren);
		loginService.deleteAdmin(opt_ren);
		return "redirect:/login/queryAdminUserList.do";
	}
	
	
	
	/**
	 * 用户查询
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryAdminUserList.do")
	public String queryAdminUserList(HttpServletRequest request,HttpServletResponse response){
		/******************查询条件********************/
		String username =this.getParam(request, "username");//用户名或者真实姓名
		String real_name = this.getParam(request, "real_name");
		String department = this.getParam(request, "department");
		/******************查询Map********************/
		Map<String,Object>paramMap = new HashMap<String,Object>();
		paramMap.put("username", username);
		paramMap.put("real_name", real_name);
		paramMap.put("department", department);
		/******************分页条件开始********************/
		int totalCount = loginService.queryByAdminUserListPageCount(paramMap);
		PageVo page = PageUtil.getInstance().paging(request, 15, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		/******************查询开始********************/
		List<Map<String,Object>> adminList = loginService.queryAdminUserList(paramMap);
		request.setAttribute("real_name", real_name);
		request.setAttribute("username", username);
		request.setAttribute("adminList", adminList);
		request.setAttribute("isShowList", 1);
		request.setAttribute("department", department);
		request.setAttribute("userDepartment", LoginUserVo.getUser_department());
		request.setAttribute("userLoginStatus", LoginUserVo.getLoginStatus());
		return "login/adminUserList";
	}
	
	@RequestMapping("/queryAdminUserInfo.do")
	public String queryAdminUserInfo(HttpServletRequest request, HttpServletResponse response){
		String opt_ren = this.getParam(request, "opt_ren");
		Map<String, String> userMap = userService.queryUserInfo(opt_ren);
		request.setAttribute("userMap", userMap);
		return "login/adminUpdate";
	}
	
	@RequestMapping("/updateAdminUser.do")
	public String updateAdminUser(HttpServletRequest request, HttpServletResponse response){
		String user_id = this.getParam(request, "user_id");
		String password = this.getParam(request, "password");
		String department = this.getParam(request, "department");
		String email = this.getParam(request, "email");
		String user_phone =this.getParam(request, "user_phone");
		String username = this.getParam(request, "username");
		/***************************Map存储并执行操作*****************************/
		Map<String,String> map = new HashMap<String,String>();
		map.put("user_id", user_id);
		map.put("pwd", password);
		map.put("department", department);
		map.put("email", email);
		map.put("telephone", user_phone);
		userService.updateUser(map);
		return "redirect:/login/queryAdminUserList.do";
	}
	
	
	
	
	/**
	 * 给火车票提供查看当前打码人数的接口
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryCurrentCoding.do")
	public String queryCurrentCoding(HttpServletRequest request, HttpServletResponse response){
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String newDate = sdf.format(now); //获得当前系统时间
		/******************查询Map********************/
		Map<String,Object>paramMap = new HashMap<String,Object>();
		/******************分页条件开始********************/
		int totalCount = loginService.queryAdminCurrentNameCount(paramMap);
		PageVo page = PageUtil.getInstance().paging(request, 15, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		
		List<Map<String, Object>> nameList = loginService.queryAdminCurrentName(paramMap);
		List<Map<String,Object>> codeList = new ArrayList<Map<String,Object>>();
		Map<String, Object> nameMap = new HashMap<String, Object>();
		for(int i=0;i<nameList.size();i++){
			nameMap = nameList.get(i);
			String opt_ren = nameMap.get("username").toString();
			String opt_name = nameMap.get("real_name").toString();
			paramMap.put("opt_ren", opt_ren);
			String department1 = loginService.queryAdminDepartment(opt_ren);
			//该用户当天打码情况
			int adminCodeTodayCount = loginService.queryAdminCodeTodayCount(opt_ren);
			int adminCodeTodaySuccess = loginService.queryAdminCodeTodaySuccess(opt_ren);
			int adminCodeTodayFail = loginService.queryAdminCodeTodayFail(opt_ren);
			int adminCodeTodayOvertime = loginService.queryAdminCodeTodayOvertime(opt_ren);
			if(adminCodeTodayCount>0){
				nameMap.put("adminCodeTodayCount", adminCodeTodayCount);
				nameMap.put("adminCodeTodaySuccess", adminCodeTodaySuccess);
				nameMap.put("adminCodeTodayFail", adminCodeTodayFail);
				nameMap.put("adminCodeTodayOvertime", adminCodeTodayOvertime);
				nameMap.put("department", department1);
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
		//当天打码总数
		int codeCountToday = loginService.queryCodeCountToday(paramMap);
		//当天打码正确总数
		int codeSuccessToday = loginService.queryCodeToday(paramMap);
		
		request.setAttribute("isShowList", 1);
		request.setAttribute("codeList", codeList);
		request.setAttribute("newDate", newDate);
		Map<String,String> userDepartment = LoginUserVo.getUser_department();
		request.setAttribute("userDepartment", userDepartment);
		request.setAttribute("totalCount", totalCount);
		request.setAttribute("codeCountToday", codeCountToday);
		request.setAttribute("codeSuccessToday", codeSuccessToday);
		return "login/codingCurrent";
	}
	
}
