package com.l9e.transaction.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.CtripAccountService;
import com.l9e.transaction.service.ExtRefundService;
import com.l9e.transaction.vo.AreaVo;
import com.l9e.transaction.vo.CtripVo;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.util.ExcelUtil;
import com.l9e.util.PageUtil;
import com.l9e.util.StringUtil;

/**
 * 携程账号管理
 * 
 * @author zhangjc
 *
 */
@Controller
@RequestMapping("/ctripAccount")
public class CtripAccountController extends BaseController {
	
	private static final Logger logger = Logger.getLogger(CtripAccountController.class);
	
	@Resource
	private CtripAccountService ctripAccountService;
	
	/**
	 * 进入查询页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryCtripAccountPage.do")
	public String queryCtripAccountPage(HttpServletRequest request,
			HttpServletResponse response){
		return "redirect:/ctripAccount/queryCtripAccountList.do";
	}
	
	/**
	 * 查询列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryCtripAccountList.do")
	public String queryCtripAccountList(HttpServletRequest request,HttpServletResponse response){
		/*************************查询条件***************************/
		String ctrip_name = this.getParam(request, "ctrip_name");
		List<String> ctrip_statusList = this.getParamToList(request, "ctrip_status");
		String ctrip_username = this.getParam(request, "ctrip_username");
		String ctrip_phone = this.getParam(request, "ctrip_phone");
		String begin_info_time = this.getParam(request, "begin_info_time");
		String end_info_time = this.getParam(request, "end_info_time");
		String beginBalance = this.getParam(request, "beginBalance");
		String endBalance = this.getParam(request, "endBalance");
		String acc_degree = this.getParam(request, "acc_degree");

		/*************************创建Map***************************/
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ctrip_name", ctrip_name);
		paramMap.put("begin_info_time", begin_info_time);
		paramMap.put("end_info_time", end_info_time);
		paramMap.put("ctrip_statusList", ctrip_statusList);
		paramMap.put("ctrip_username", ctrip_username);
		paramMap.put("ctrip_phone", ctrip_phone);
		paramMap.put("beginBalance", beginBalance==""?null:new Integer(beginBalance));
		paramMap.put("endBalance", endBalance==""?null:new Integer(endBalance));
		paramMap.put("acc_degree", acc_degree==""?null:new Integer(acc_degree));

		/*************************分页条件***************************/
		int totalCount = ctripAccountService.queryCtripAccountListCount(paramMap);//总条数	
		//分页
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		/*************************执行查询***************************/
		List<Map<String, String>> ctripAccountList = ctripAccountService.queryCtripAccountList(paramMap);
		/*************************request绑定***************************/
		request.setAttribute("isShowList", 1);
		request.setAttribute("ctripAccountList", ctripAccountList);
		request.setAttribute("statusStr", ctrip_statusList.toString()); 
		request.setAttribute("ctripStatus", CtripVo.getCtripStatus()); 
		request.setAttribute("ctripDegree", CtripVo.getCtripDegree()); 
		request.setAttribute("ctrip_name", ctrip_name);
		request.setAttribute("begin_info_time", begin_info_time);
		request.setAttribute("end_info_time", end_info_time);
		request.setAttribute("ctrip_username", ctrip_username);
		request.setAttribute("ctrip_phone", ctrip_phone);
		request.setAttribute("beginBalance", beginBalance);
		request.setAttribute("endBalance", endBalance);
		request.setAttribute("acc_degree", acc_degree);
		return "ctripAccount/ctripAccountList";
	}
	
	/**
	 * 查询明细/修改账号
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryCtripAccountInfo.do")
	public String queryCtripAccountInfo(HttpServletRequest request,
			HttpServletResponse response){
		String ctrip_id = this.getParam(request, "ctrip_id");
		Map<String, String> ctripAccount = ctripAccountService.queryCtripAccount(ctrip_id);
		request.setAttribute("ctripAccount", ctripAccount);
		return "ctripAccount/ctripAccountModify";
	}
	
	/**
	 * 修改账号信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/updateCtripAccountInfo.do")
	public void updateCtripAccountInfo(HttpServletRequest request,HttpServletResponse response){
		String result = "yes";
		String ctrip_id = this.getParam(request, "ctrip_id");
		String ctrip_name = this.getParam(request, "ctrip_name");
		String ctrip_password = this.getParam(request, "ctrip_password");
		String pay_password = this.getParam(request, "pay_password");
		String ctrip_username = this.getParam(request, "ctrip_username");
		String ctrip_phone = this.getParam(request, "ctrip_phone");
		String cookie = this.getParam(request, "cookie");
		String cid = this.getParam(request, "cid");
		String auth = this.getParam(request, "auth");
		String sauth = this.getParam(request, "sauth");
		
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String opt_person =loginUserVo.getReal_name();//获取当前登录的人
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("ctrip_name", ctrip_name);
		paramMap.put("ctrip_password", ctrip_password);
		paramMap.put("pay_password", pay_password);
		paramMap.put("ctrip_username", ctrip_username);
		paramMap.put("ctrip_phone", ctrip_phone);
		paramMap.put("opt_person", opt_person);
		paramMap.put("ctrip_id", ctrip_id);
		paramMap.put("cid", cid);
		paramMap.put("cookie", cookie);
		paramMap.put("auth", auth);
		paramMap.put("sauth", sauth);
		
		System.out.println(ctrip_name+ctrip_password+pay_password+ctrip_username+ctrip_phone+opt_person+ctrip_id);
		try {
			ctripAccountService.updateCtripAccount(paramMap);
			Map<String, String> logs = new HashMap<String, String>();
			logs.put("opt_person", opt_person);
			logs.put("content", opt_person+"修改了账号"+ctrip_name+"信息id="+ctrip_id);
			ctripAccountService.insertCtriplogs(logs);
		} catch (Exception e) {
			result="no";
			logger.error(e);
		}
		try {
			response.getWriter().write(result);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 进入增加页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/addCtripAccountPage.do")
	public String addCtripAccountPage( HttpServletRequest request,HttpServletResponse response){
		return "ctripAccount/ctripAccountAdd";
	}
	
	/**
	 * 添加账号信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/addCtripAccount.do")
	public String addCtripAccount(HttpServletRequest request,HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String opt_person =loginUserVo.getReal_name();//获取当前登录的人
		String ctrip_name = this.getParam(request, "ctrip_name");
		String ctrip_password = this.getParam(request, "ctrip_password");
		String pay_password = this.getParam(request, "pay_password");
		String ctrip_username = this.getParam(request, "ctrip_username");
		String ctrip_phone = this.getParam(request, "ctrip_phone");
		
		String cookie = this.getParam(request, "cookie");
		String cid = this.getParam(request, "cid");
		String auth = this.getParam(request, "auth");
		String sauth = this.getParam(request, "sauth");
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("ctrip_name", ctrip_name);
		paramMap.put("ctrip_password", ctrip_password);
		paramMap.put("pay_password", pay_password);
		paramMap.put("ctrip_username", ctrip_username);
		paramMap.put("ctrip_phone", ctrip_phone);
		
		paramMap.put("cookie", cookie);
		paramMap.put("cid", cid);
		paramMap.put("auth", auth);
		paramMap.put("sauth", sauth);
		
		paramMap.put("opt_person", opt_person);
		
		ctripAccountService.insertCtripAccount(paramMap);
		Map<String, String> logs = new HashMap<String, String>();
		logs.put("opt_person", opt_person);
		logs.put("content", opt_person+"添加了账号：" + ctrip_name);
		ctripAccountService.insertCtriplogs(logs);
		return "redirect:/ctripAccount/queryCtripAccountPage.do";
	}
	
	/**
	 * 修改账号状态
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/updateCtripAccountStatus.do")
	public void updateCtripAccountStatus(HttpServletRequest request,HttpServletResponse response){
		String result="yes";
		String ctrip_id = this.getParam(request, "ctrip_id");
		String ctrip_name = this.getParam(request, "ctrip_name");
		String ctrip_status = this.getParam(request, "ctrip_status");
		try {
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
			String opt_person =loginUserVo.getReal_name();//获取当前登录的人
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("opt_person",opt_person);
			paramMap.put("ctrip_id",ctrip_id);
			paramMap.put("ctrip_status",ctrip_status);
			ctripAccountService.updateCtripAccount(paramMap);
			Map<String, String> logs = new HashMap<String, String>();
			logs.put("opt_person", opt_person);
			String content = "";
			if("11".equals(ctrip_status))content = opt_person + "停用了账号：" + ctrip_name;
			if("00".equals(ctrip_status))content = opt_person + "启用了账号：" + ctrip_name;
			logs.put("content", content);
			ctripAccountService.insertCtriplogs(logs);
		} catch (Exception e) {
			result="no";
			logger.error(e);
		}
		try {
			response.getWriter().write(result);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询登陆名是否被占用
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryCtrip_name.do")
	@ResponseBody
	public void queryCtrip_name(HttpServletRequest request ,HttpServletResponse response){
		String ctrip_name = this.getParam(request, "ctrip_name");
		String result = null;
		if(StringUtils.isEmpty(ctripAccountService.queryCtrip_name(ctrip_name))){
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
	}
	

	/**
	 * 查询日志
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryCtripHistory.do")
	public String queryCtripHistory(HttpServletRequest request ,HttpServletResponse response){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		int totalCount = ctripAccountService.queryCtripAccountLogCount();//总条数	
		//分页
		PageVo page = PageUtil.getInstance().paging(request, 10, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		/*************************执行查询***************************/
		List<Map<String, String>> history = ctripAccountService.queryCtripAccountLogList(paramMap);
		request.setAttribute("history", history);
		request.setAttribute("isShowList", 1);
		return "ctripAccount/ctripAccountHistory";
	}
	/**
	 * 查询出票金额设置
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryAmountArea.do")
	public String queryAmountArea(HttpServletRequest request ,HttpServletResponse response){
		List<Map<String, String>> amountAreaList = ctripAccountService.queryAmountArea();
		request.setAttribute("amountAreaList",amountAreaList);
		return "ctripAccount/ctripAccountAmountArea";
	}
	/**
	 * 修改出票金额设置
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/editAmountArea.do")
	@ResponseBody
	public String editAmountArea(HttpServletRequest request ,HttpServletResponse response){
		int id = emptyToZero(getParam(request, "id"));
		Double limit_begin = emptyToZeroDouble(getParam(request, "limit_begin"));
		Double limit_end =emptyToZeroDouble(getParam(request, "limit_end"));
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id",id);
		param.put("limit_begin",limit_begin);
		param.put("limit_end",limit_end);

		int result = ctripAccountService.updateAmountArea(param);
		return result>0?"success":"error";
	}
	
	
	/**
	 * 查询异常账号
	 * @param request
	 * @param response
	 * @return 
	 * @author daiqh
	 */
	@RequestMapping("/queryOddAccountCtrip.do")
	public String queryOddAccountCtrip(HttpServletRequest request ,HttpServletResponse response){
		/*************************查询条件***************************/
		String ctrip_name = this.getParam(request, "ctrip_name");
		List<String> ctrip_statusList = this.getParamToList(request, "ctrip_status");
		String ctrip_username = this.getParam(request, "ctrip_username");
		String ctrip_phone = this.getParam(request, "ctrip_phone");
		String begin_info_time = this.getParam(request, "begin_info_time");
		String end_info_time = this.getParam(request, "end_info_time");
		String beginBalance = this.getParam(request, "beginBalance");
		String endBalance = this.getParam(request, "endBalance");
		String acc_degree = this.getParam(request, "acc_degree");

		/*************************创建Map***************************/
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ctrip_name", ctrip_name);
		paramMap.put("begin_info_time", begin_info_time);
		paramMap.put("end_info_time", end_info_time);
		paramMap.put("ctrip_statusList", ctrip_statusList);
		paramMap.put("ctrip_username", ctrip_username);
		paramMap.put("ctrip_phone", ctrip_phone);
		paramMap.put("beginBalance", beginBalance==""?null:new Integer(beginBalance));
		paramMap.put("endBalance", endBalance==""?null:new Integer(endBalance));
		paramMap.put("acc_degree", acc_degree==""?null:new Integer(acc_degree));
		paramMap.put("result_type", 0);

		/*************************分页条件***************************/
		int totalCount = ctripAccountService.queryCtripAccountListCount(paramMap);//总条数	
		//分页
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		/*************************执行查询***************************/
		List<Map<String, String>> ctripAccountList = ctripAccountService.queryCtripAccountList(paramMap);
		/*************************request绑定***************************/
		request.setAttribute("isShowList", 1);
		request.setAttribute("ctripAccountList", ctripAccountList);
		request.setAttribute("statusStr", ctrip_statusList.toString()); 
		request.setAttribute("ctripStatus", CtripVo.getCtripStatus()); 
		request.setAttribute("ctripDegree", CtripVo.getCtripDegree()); 
		request.setAttribute("ctrip_name", ctrip_name);
		request.setAttribute("begin_info_time", begin_info_time);
		request.setAttribute("end_info_time", end_info_time);
		request.setAttribute("ctrip_username", ctrip_username);
		request.setAttribute("ctrip_phone", ctrip_phone);
		request.setAttribute("beginBalance", beginBalance);
		request.setAttribute("endBalance", endBalance);
		request.setAttribute("acc_degree", acc_degree);
		return "ctripAccount/ctripOddAccountList";
	}
	
	
	/**
	 * 移除异常账号
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/removeOddAccount.do")
	public void RemoveOddAccount(HttpServletRequest request, HttpServletResponse response){
		String result="yes";
		String ctrip_id = this.getParam(request, "ctrip_id");
		String ctrip_name = this.getParam(request, "ctrip_name");
		try {
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
			String opt_person =loginUserVo.getReal_name();//获取当前登录的人
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("opt_person",opt_person);
			paramMap.put("ctrip_id",ctrip_id);
			paramMap.put("ctrip_status","11");
			paramMap.put("result_type","9");
			ctripAccountService.updateCtripAccount(paramMap);
			Map<String, String> logs = new HashMap<String, String>();
			logs.put("opt_person", opt_person);
			String content = "";
			content = opt_person + "停用了账号：" + ctrip_name; 
			logs.put("content", content);
			ctripAccountService.insertCtriplogs(logs);
		} catch (Exception e) {
			result="no";
			logger.error(e);
		}
		try {
			response.getWriter().write(result);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
}
