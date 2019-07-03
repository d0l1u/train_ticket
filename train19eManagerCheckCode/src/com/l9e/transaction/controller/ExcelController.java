package com.l9e.transaction.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.l9e.common.BaseController;
import com.l9e.excel.ExcelViewAdmin;
import com.l9e.excel.ExcelViewCurrent;
import com.l9e.transaction.service.ExcelService;
import com.l9e.transaction.vo.ExcelAdminVo;
import com.l9e.transaction.vo.LoginUserVo;

@Controller
@RequestMapping("/excel")
public class ExcelController extends BaseController {
	private static final Logger logger = Logger.getLogger(ExcelController.class);
	@Resource
	private ExcelService excelService;
	
	/**
	 * 超级管理员--打码统计--导出Excel
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("excelAdminExport.do")
	public ModelAndView ExcelAdminExport(HttpServletRequest request,HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String opt_name = loginUserVo.getReal_name();//得到操作人的姓名
		logger.info("超级管理员："+opt_name+"点击了【导出“打码统计”Excel】");
		/******************查询条件********************/
		String begin_time =this.getParam(request, "begin_time");//开始时间
		String end_time = this.getParam(request, "end_time");//结束时间
		String department = this.getParam(request, "department");
		/******************查询Map********************/
		Map<String,Object>paramMap = new HashMap<String,Object>();
		paramMap.put("begin_time", begin_time);
		paramMap.put("end_time", end_time);
		paramMap.put("department", department);
		/******************查询开始********************/
		List<ExcelAdminVo> adminList = excelService.getExcelAdminList(paramMap);
		/******************Request绑定开始********************/
		request.setAttribute("adminList", adminList);
		request.setAttribute("begin_time", begin_time);
		request.setAttribute("end_time", end_time);
		request.setAttribute("department", department);
		request.setAttribute("userDepartment", LoginUserVo.getUser_department());
		request.setAttribute("isShowList", 1);
		
		
		Map map = new HashMap();
		map.put("list",adminList);
		map.put("begin_time", begin_time);
		map.put("end_time", end_time);
		map.put("department", department);
		map.put("userDepartment", LoginUserVo.getUser_department());
		ExcelViewAdmin excelView = new ExcelViewAdmin();
		return new ModelAndView(excelView, map);
	}
	
	/**
	 * 超级管理员--打码用户--导出Excel
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("excelCurrentExport.do")
	public ModelAndView excelCurrentExport(HttpServletRequest request,HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String opt_name = loginUserVo.getReal_name();//得到操作人的姓名
		logger.info("超级管理员："+opt_name+"点击了【导出“打码用户”Excel】");
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
		/******************查询开始********************/
		List<Map<String,String>> adminCodeList = excelService.queryAdminCodeList(paramMap);
		request.setAttribute("real_name", real_name);
		request.setAttribute("username", username);
		request.setAttribute("adminCodeList", adminCodeList);
		request.setAttribute("isShowList", 1);
		request.setAttribute("time", time);
		request.setAttribute("department", department);
		request.setAttribute("userDepartment", LoginUserVo.getUser_department());
		
		Map map = new HashMap();
		map.put("list",adminCodeList);
		map.put("real_name", real_name);
		map.put("username", username);
		map.put("department", department);
		map.put("time", time);
		map.put("userDepartment", LoginUserVo.getUser_department());
		ExcelViewCurrent excelView = new ExcelViewCurrent();
		return new ModelAndView(excelView, map);
	}
}
