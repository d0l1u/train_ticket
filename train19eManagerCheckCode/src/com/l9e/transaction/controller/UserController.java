package com.l9e.transaction.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.UserService;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.util.PageUtil;
/**
 * 普通用户
 *
 */
@Controller
@RequestMapping("/user")
public class UserController  extends BaseController {
	private static final Logger logger = Logger.getLogger(UserController.class);

	@Resource
	private UserService userService ;
	
	@RequestMapping("/queryUserInfo.do")
	public String queryUserInfo(HttpServletRequest request, HttpServletResponse response){
		String opt_ren = this.getParam(request, "opt_ren");
		Map<String, String> userMap = userService.queryUserInfo(opt_ren);
		request.setAttribute("userDepartment", LoginUserVo.getUser_department());
		request.setAttribute("userMap", userMap);
		return "user/userInfo";
	}
	
	@RequestMapping("/updateUser.do")
	public String updateUser(HttpServletRequest request, HttpServletResponse response){
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
		map.put("user_name", username);
		userService.updateUser(map);
		/**********************若修改过密码，则logined置为1**************************/
		userService.updateLogined(map);
		return "redirect:/loginManager/logOutUser.do";
	}
	
	@RequestMapping("/dayUser.do")
	public String dayUser(HttpServletRequest request, HttpServletResponse response){
		/******************查询条件********************/
		String opt_ren = this.getParam(request, "opt_ren");
		String begin_time =this.getParam(request, "begin_time");//开始时间
		String end_time = this.getParam(request, "end_time");//结束时间
		/******************查询Map********************/
		Map<String,Object>paramMap = new HashMap<String,Object>();
		paramMap.put("begin_time", begin_time);
		paramMap.put("end_time", end_time);
		paramMap.put("opt_ren", opt_ren);
		/******************分页条件开始********************/
		int totalCount = userService.queryDayUserCount(paramMap);
		PageVo page = PageUtil.getInstance().paging(request, 15, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		/******************查询开始********************/
		List<Map<String,String>> codeList = userService.queryDayUserList(paramMap);
		request.setAttribute("begin_time", begin_time);
		request.setAttribute("end_time", end_time);
		request.setAttribute("codeList", codeList);
		request.setAttribute("opt_ren", opt_ren);
		request.setAttribute("isShowList", 1);
		return "user/dayUser";
		
	}
	
	
}
