package com.l9e.transaction.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import java.util.Map;

import javax.annotation.Resource;
import javax.mail.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.l9e.checkCode.RandomValidateCode;
import com.l9e.common.BaseController;
import com.l9e.transaction.service.LoginService;
import com.l9e.transaction.service.NoticeService;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.PageUtil;
/**
 * 用户登录
 * @author liht
 *
 */
@Controller
@RequestMapping("/login")
public class LoginController extends BaseController{
	private static final Logger logger = 
		Logger.getLogger(LoginController.class);

	@Resource
	private LoginService loginService ;
		
	@RequestMapping("login.do")
	@ResponseBody
	public String login(HttpServletRequest request,HttpServletResponse response){
		/***************************获取用户名和密码*****************************/
		String user_name = this.getParam(request, "user_name");
		String password = this.getParam(request, "password");
		String checkCode=this.getParam(request, "checkCode");   //验证码
		/***************************Map存储并查询*****************************/
		Map<String,String>query_Map = new HashMap<String,String>();
		query_Map.put("user_name", user_name);
		query_Map.put("password", password);
		Map<String,String>userInfo_Map = loginService.queryLogin_UserInfo(query_Map);  //查询用户名和密码是否正确
		/***************************Session存储AJAX返回*****************************/
		String sessionCheckCode = null;
		String loginType = request.getParameter("loginType");  //当其值为robot时，不用验证checkCode;默认为user(非robot用户)
		if(loginType==null){
			loginType="user";
			sessionCheckCode = //获取session中的验证码
				request.getSession().getAttribute(RandomValidateCode.RANDOMCODEKEY).toString();
		}
		String result = null;
		
		if(userInfo_Map==null){
			result="no";
		}else if ((userInfo_Map.get("user_status").toString()).equals("0")){
			result="wait";
		}else if ((userInfo_Map.get("user_status").toString()).equals("2")){
			result="notthrough";
		}else if ((userInfo_Map.get("user_IsOpen").toString()).equals("1")){
			result="isClose";
		}else if(loginType!=null&&"robot".equals(loginType)){
			LoginUserVo loginUserVo = new LoginUserVo();
			loginUserVo.setUser_id(userInfo_Map.get("user_id"));
			loginUserVo.setUser_name(userInfo_Map.get("user_name"));
			loginUserVo.setReal_name(userInfo_Map.get("real_name"));
			loginUserVo.setPassword(query_Map.get("password"));
			loginUserVo.setUser_IsOpen(userInfo_Map.get("user_IsOpen"));
			loginUserVo.setUser_level(userInfo_Map.get("user_level"));
			loginUserVo.setSupervise_name(userInfo_Map.get("supervise_name"));
			request.getSession().setAttribute("loginUserVo",loginUserVo);
			
			Map<String,String> add_Map = new HashMap<String,String>();
			add_Map.put("user_id", userInfo_Map.get("user_id"));
			add_Map.put("user_name", userInfo_Map.get("user_name")+"_robot");
			add_Map.put("real_name", userInfo_Map.get("real_name"));
			add_Map.put("login_ip", request.getRemoteAddr());
			loginService.updateLoginTimeAndIp(add_Map);
			loginService.addLogin_Log(add_Map);
			
			result="yes";
		}else if(loginType!=null&&"user".equals(loginType)){
			if(sessionCheckCode==null || !sessionCheckCode.equalsIgnoreCase(checkCode)){
				result="checkCodeNo";
			}else{
				LoginUserVo loginUserVo = new LoginUserVo();
				loginUserVo.setUser_id(userInfo_Map.get("user_id"));
				loginUserVo.setUser_name(userInfo_Map.get("user_name"));
				loginUserVo.setReal_name(userInfo_Map.get("real_name"));
				loginUserVo.setPassword(query_Map.get("password"));
				loginUserVo.setUser_IsOpen(userInfo_Map.get("user_IsOpen"));
				loginUserVo.setUser_level(userInfo_Map.get("user_level"));
				loginUserVo.setSupervise_name(userInfo_Map.get("supervise_name"));
				request.getSession().setAttribute("loginUserVo",loginUserVo);
				
				Map<String,String> add_Map = new HashMap<String,String>();
				add_Map.put("user_id", userInfo_Map.get("user_id"));
				add_Map.put("user_name", userInfo_Map.get("user_name"));
				add_Map.put("real_name", userInfo_Map.get("real_name"));
				add_Map.put("login_ip", request.getRemoteAddr());
				loginService.updateLoginTimeAndIp(add_Map);
				loginService.addLogin_Log(add_Map);
				
				result="yes";
			}
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
		if(user_level.equals("2")){
			return "login/loginUserList";
		}else{
			return "redirect:/login/loginUserList.do";
		}
		
	}
	
	/**
	 * 用户管理页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/loginUserList.do")
	public String loginUserList(HttpServletRequest request ,HttpServletResponse response){
		/***************************获取Session*****************************/
		LoginUserVo loginUserVo = (LoginUserVo)request.getSession().getAttribute("loginUserVo");
		String user_level = loginUserVo.getUser_level();
		String user_Session_id = loginUserVo.getUser_id();
		String user_name = this.getParam(request, "user_name");
		List<Map<String,Object>>loginUser_List;
		
		/***************************Map存储并查询*****************************/
		Map<String,Object>query_Map = new HashMap<String,Object>();
		Map<String,Object>query_MapOnly = new HashMap<String,Object>();
		query_Map.put("user_name", user_name);
		query_MapOnly.put("user_id", user_Session_id);
		int totalCount = loginService.queryLoginUserListCount(query_Map);//总条数	
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		query_Map.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		query_Map.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		
		if(user_level.endsWith("2")){
			loginUser_List = loginService.queryLoginUserList(query_Map);
		}else{
			loginUser_List = loginService.queryLoginUserListOnly(query_MapOnly);
		}
		for(Map<String,Object>map:loginUser_List){
			String user_id=map.get("user_id").toString();
			int loginTotal = loginService.queryUserLoginTotal(user_id);
			map.put("loginTotal", loginTotal);
		}
		/***************************request绑定*****************************/
		request.setAttribute("loginUser_List", loginUser_List);
		request.setAttribute("user_Types", LoginUserVo.getUser_Types());
		request.setAttribute("user_Status", LoginUserVo.getUser_Status());
		request.setAttribute("user_Isopen", LoginUserVo.getUser_ISOPENS());
		request.setAttribute("user_name", user_name);
		request.setAttribute("isShowList", 1);
		return "login/loginUserList";
	}
	
	/**
	 * 删除账号
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/deleteUserAccount.do")
	public String deleteUserAccount(HttpServletRequest request ,HttpServletResponse response){
		String user_id = this.getParam(request, "user_id");
		loginService.deleteUserAccount(user_id);
		return "redirect:/login/loginUserList.do";
	}
	
	/**
	 * 账号的停用和启用
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/updateUserIsOpen.do")
	public String updateUserIsOpen(HttpServletRequest request ,HttpServletResponse response){
		String user_id = this.getParam(request, "user_id");
		String user_IsOpen = this.getParam(request, "user_IsOpen");
		/***************************Map存储并修改*****************************/
		Map<String,Object>update_Map = new HashMap<String,Object>();
		update_Map.put("user_id", user_id);
		update_Map.put("user_IsOpen", user_IsOpen);
		loginService.updateUserIsOpen(update_Map);
		return "redirect:/login/loginUserList.do";
	}
	/**
	 * 查询修改用户明细
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryUpdateUserInfo.do")
	public String queryUpdateUserInfo(HttpServletRequest request ,HttpServletResponse response){
		/***************************获取Session*****************************/
		LoginUserVo loginUserVo = (LoginUserVo)request.getSession().getAttribute("loginUserVo");
		String user_Session_level = loginUserVo.getUser_level();
		
		String user_id = this.getParam(request, "user_id");
		/***************************Map存储并查询*****************************/
		Map<String,Object>query_Map = new HashMap<String,Object>();
		query_Map.put("user_id", user_id);
		Map<String,String>userInfo_Map = loginService.queryUpdateUserInfo(query_Map);
		request.setAttribute("userInfo_Map", userInfo_Map);
		request.setAttribute("userType", LoginUserVo.getUser_Types());
		request.setAttribute("userStatus", LoginUserVo.getUser_Status());
		request.setAttribute("user_Session_level", user_Session_level);
		request.setAttribute("userType", LoginUserVo.getUser_Types());
		request.setAttribute("province", loginService.getProvince());
		return "login/loginUpdateInfo";
	}
	/**
	 * 执行修改
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/updateUserInfo.do")
	public String updateUserInfo(HttpServletRequest request ,HttpServletResponse response){
		String user_id = this.getParam(request, "user_id");
		String password = this.getParam(request, "password");
		String email = this.getParam(request, "email");
		String user_phone =this.getParam(request, "user_phone");
		String user_level = this.getParam(request, "user_level");
		String user_status = this.getParam(request, "user_status");
		String remark = this.getParam(request, "remark");
		List<String>supervise_name = this.getParamToList(request, "supervise_name");
		/***************************获取Session*****************************/
		LoginUserVo loginUserVo = (LoginUserVo)request.getSession().getAttribute("loginUserVo");
		String user_Session_level = loginUserVo.getUser_level();
		
		/***************************Map存储并修改*****************************/
		Map<String,String>update_Map = new HashMap<String,String>();
		if(user_Session_level.equals("2")){
			update_Map.put("user_id", user_id);
			update_Map.put("password", password);
			update_Map.put("email", email);
			update_Map.put("user_phone", user_phone);
			update_Map.put("user_level", user_level);
			update_Map.put("user_status", user_status);
			update_Map.put("remark", remark);
			update_Map.put("supervise_name", supervise_name.toString());
			loginService.updateUserInfo(update_Map);
		}else{
			update_Map.put("user_id", user_id);
			update_Map.put("password", password);
			update_Map.put("email", email);
			update_Map.put("user_phone", user_phone);
			loginService.updateUserInfo(update_Map);
		}
		
		return "redirect:/login/loginUserList.do";
	}
	
	/**
	 * 进入添加用户页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/addUserInfo.do")
	public String addUserInfo(HttpServletRequest request ,HttpServletResponse response){
		request.setAttribute("userType", LoginUserVo.getUser_Types());
		request.setAttribute("province", loginService.getProvince());
		return "login/loginAddInfo";
	}
	
	/**
	 * 添加用户
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/addUser.do")
	public String addUser(HttpServletRequest request ,HttpServletResponse response){
		String user_id = CreateIDUtil.createID("US");
		String user_name = this.getParam(request, "user_name");
		String real_name = this.getParam(request, "real_name");
		String password = this.getParam(request, "password");
		String email = this.getParam(request, "email");
		String user_phone =this.getParam(request, "user_phone");
		String user_level = this.getParam(request, "user_level");
		String user_IsOpen = LoginUserVo.ISOPEN_YES;
		String user_status = LoginUserVo.STATUS_PASS;
		String remark = this.getParam(request, "remark");
		List<String>supervise_name = this.getParamToList(request, "supervise_name");
		/***************************Map存储并修改*****************************/
		Map<String,String>add_Map = new HashMap<String,String>();
		add_Map.put("user_id", user_id);
		add_Map.put("user_name", user_name);
		add_Map.put("real_name", real_name);
		add_Map.put("password", password);
		add_Map.put("email", email);
		add_Map.put("user_phone", user_phone);
		add_Map.put("user_level", user_level);
		add_Map.put("user_IsOpen", user_IsOpen);
		add_Map.put("user_status", user_status);
		add_Map.put("remark", remark);
		add_Map.put("supervise_name", supervise_name.toString());
		loginService.addUser(add_Map);
		
		return "redirect:/login/loginUserList.do";
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
		builder
				.append("<script type=\"text/javascript\" charset=\"UTF-8\">");
		builder.append("window.top.location.href=\"");
		builder.append("/pages/login/login.jsp\";</script>");
		out.print(builder.toString());
		out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 进入登录日志查询页面
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("loginLogPage.do")
	public String loginLogPage(HttpServletResponse response,HttpServletRequest request){
		
		return "login/loginLogList";
	}
	
	/**
	 * 登录日志列表
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("loginLog.do")
	public String loginLog(HttpServletResponse response,HttpServletRequest request){
		String begin_time = this.getParam(request, "begin_time");
		String end_time = this.getParam(request, "end_time");
		String user_name = this.getParam(request, "user_name");
		Map<String,Object>query_Map = new HashMap<String,Object>();
		query_Map.put("begin_time", begin_time);
		query_Map.put("end_time", end_time);
		query_Map.put("user_name", user_name);
		int totalCount = loginService.queryLoginLogsListCount(query_Map);//总条数	
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		query_Map.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		query_Map.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		List<Map<String,String>> loginLogs_List = loginService.queryLoginLogsList(query_Map);
		request.setAttribute("loginLogs_List", loginLogs_List);
		request.setAttribute("begin_time", begin_time);
		request.setAttribute("end_time",end_time);
		request.setAttribute("user_name", user_name);
		request.setAttribute("isShowList", 1);
		return "login/loginLogList";
	}
}
