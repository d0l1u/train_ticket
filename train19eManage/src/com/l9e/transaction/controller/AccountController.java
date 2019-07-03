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
import com.l9e.transaction.service.AccountService;
import com.l9e.transaction.service.ExtRefundService;
import com.l9e.transaction.vo.AccountVo;
import com.l9e.transaction.vo.AreaVo;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.util.ExcelUtil;
import com.l9e.util.PageUtil;
import com.l9e.util.StringUtil;

/**
 * 账号管理
 * 
 * @author liht
 *
 */
@Controller
@RequestMapping("/account")
public class AccountController extends BaseController {
	
	private static final Logger logger = Logger.getLogger(AccountController.class);
	
	@Resource
	private AccountService accountService;
	@Resource
	private ExtRefundService extRefundService;
	
	
	/**
	 * 进入查询页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryAccountPage.do")
	public String queryAccountPage(HttpServletRequest request,
			HttpServletResponse response){
		request.setAttribute("channel_types", AccountVo.getChannels());
		request.setAttribute("province", accountService.getProvince());
		request.setAttribute("accountStatus", AccountVo.getAccoutStatus());
		request.setAttribute("stopReason", AccountVo.getAccountStopreason());
		return "redirect:/account/queryAccountList.do?acc_status=00";
	}
	
	/**
	 * 查询列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryAccountList.do")
	public String queryAccountList(HttpServletRequest request,HttpServletResponse response){
		/*************************查询条件***************************/
		String at_province_id = this.getParam(request, "at_province_id");
		String at_city_id = this.getParam(request, "at_city_id");
		List<String> statusList = this.getParamToList(request, "acc_status");
		String accUserName = this.getParam(request, "acc_username");
		List<String> channel = this.getParamToList(request, "channel");
		List<String> stopReasonStr = this.getParamToList(request,"stopReason");
		List<String> stop_reasonStr = this.getParamToList(request,"stop_reason");
		List<String> SourceList = this.getParamToList(request, "account_source");
		String begin_info_time = this.getParam(request, "begin_info_time");
		String end_info_time = this.getParam(request, "end_info_time");
		String active_time = this.getParam(request, "active_time");
		String is_alive_0 = this.getParam(request, "is_alive_0");
		String is_alive_1 = this.getParam(request, "is_alive_1");
		String mail_163 = this.getParam(request, "mail_163");
		String mail_19trip = this.getParam(request, "mail_19trip");
		String mail_qita = this.getParam(request, "mail_qita");
		String acc_mail = this.getParam(request, "acc_mail");
		List<String> contact_numList= this.getParamToList(request, "contact_num");
		List<String> contact_numNew=new ArrayList<String>();
		for(int j=0;j<contact_numList.size();j++){
			String contact_num=contact_numList.get(j);	
			int num=Integer.valueOf(contact_num)-9;
//			if(num==1)contact_numNew.add("0");
				if(num==-9){
					contact_numNew.add("0");
				}else{for(int m=num;m<=Integer.valueOf(contact_num);m++){
					contact_numNew.add(String.valueOf(m));
				}
			}
			
		}

		/*************************创建Map***************************/
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("acc_username", accUserName);
		paramMap.put("acc_status", statusList);
//		paramMap.put("acc_status", statusList1);
		paramMap.put("at_province_id", at_province_id);
		paramMap.put("at_city_id", at_city_id);
		paramMap.put("channel", channel);
		paramMap.put("account_source", SourceList);
		paramMap.put("begin_info_time", begin_info_time);
		paramMap.put("end_info_time", end_info_time);
		paramMap.put("is_alive_0", is_alive_0);
		paramMap.put("is_alive_1", is_alive_1);
		paramMap.put("active_time", active_time);
		paramMap.put("mail_163", mail_163);
		paramMap.put("mail_19trip", mail_19trip);
		paramMap.put("mail_qita", mail_qita);
		paramMap.put("acc_mail", acc_mail);
		paramMap.put("contact_num", contact_numNew);
		paramMap.put("mail_set", 2);
		if(!"".equals(mail_163) || !"".equals(mail_19trip) || !"".equals(mail_qita)){
			paramMap.put("mail_set", -1);
		}


		List<String> stopListr=new ArrayList<String>(stop_reasonStr);
		String reason = "";
		if(stopListr!=null && stopListr.size()>0){
			for(int i=0;i<stopListr.size();i++){
				reason = stopListr.get(i);
				if(reason.equals("000") || reason=="000"){
					stopListr.remove(reason);
					i--;
				}
			}
		}
		paramMap.put("stop_reason", stopListr);
//		paramMap.put("stop_reason", reasonSt1);
		

		/*************************分页条件***************************/
		int totalCount = accountService.queryAccountListCount(paramMap);//总条数	
		//分页
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		
		/*************************执行查询***************************/
		List<Map<String, String>> accountList = accountService.queryAccountList(paramMap);
		
		List<Map<String,String>> merchantList = extRefundService.queryExtMerchantinfo();//查询合作商户的id及名称
		Map<String, String> merchantMap = AccountVo.getChannels();
		for(int i = 0 ; i < merchantList.size() ; i++){
			merchantMap.put(merchantList.get(i).get("merchant_id"), merchantList.get(i).get("merchant_name"));
		}
		request.setAttribute("channel_types", merchantMap);
		/*************************request绑定***************************/
		request.setAttribute("accountStatus", AccountVo.getAccoutStatus());
		request.setAttribute("accountList", accountList);
		request.setAttribute("isShowList", 1);
		request.setAttribute("channel_types", AccountVo.getChannels());
		request.setAttribute("channelStr", channel.toString());
		request.setAttribute("statusStr", statusList.toString()); 
		request.setAttribute("sourceSrt", SourceList.toString());
		request.setAttribute("stop_reasonStr", stop_reasonStr.toString());
		request.setAttribute("stopReasonStr", stopReasonStr.toString());
		request.setAttribute("province", accountService.getProvince()); 
		request.setAttribute("at_province_id", at_province_id);
		request.setAttribute("at_city_id", at_city_id);
//		request.setAttribute("city", accountService.getCity(at_province_id));
		request.setAttribute("acc_username", accUserName);
		request.setAttribute("channel", channel);
		request.setAttribute("begin_info_time", begin_info_time);
		request.setAttribute("end_info_time", end_info_time);
		request.setAttribute("stopReason", AccountVo.getAccountStopreason());
		request.setAttribute("account_source", AccountVo.getAccount_Source());
		request.setAttribute("active_time", active_time);
		request.setAttribute("is_alive_0", is_alive_0);
		request.setAttribute("is_alive_1", is_alive_1);
		request.setAttribute("mail_163", mail_163);
		request.setAttribute("mail_19trip", mail_19trip);
		request.setAttribute("mail_qita", mail_qita);
		request.setAttribute("acc_mail", acc_mail);
		request.setAttribute("contact_num", contact_numList);
		return "account/accountList";
	}
	
	/**
	 * 查询明细
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryAccount.do")
	public String queryAccount(HttpServletRequest request,
			HttpServletResponse response){
		String order_id = this.getParam(request, "order_id");
		
		Map<String, String> account = accountService.queryAccount(order_id);
	
		
		
		//BookService.queryBookOrderInfoPs();
		request.setAttribute("account", account);
		
		request.setAttribute("accountStatus", AccountVo.getAccoutStatus());
		//request.setAttribute("bxList", bxList);
	
		
		return "account/accountInfo";
	}
	
	
	/**
	 * 进入更新页面
	 * @param account
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/updatePreAccount.do")
	public String preUpdateAccount(AccountVo account, HttpServletRequest request,HttpServletResponse response){
		
		Map<String, String> acc = accountService.queryAccount(account.getAcc_id());
		
		 
		request.setAttribute("account", acc);
		request.setAttribute("channel_types", AccountVo.getChannels());
		return "account/accountModify";
	}

	
	/**
	 * 更新账号信息
	 * @param account
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/updateAccount.do")
	public String updateAccount(AccountVo account, HttpServletRequest request,HttpServletResponse response){
		String result = "yes";
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
			String user =loginUserVo.getReal_name();//获取当前登录的人
			account.setOpt_person(user);
			accountService.updateAccount(account);
			request.setAttribute("channel_types", AccountVo.getChannels());
			result="no";
		response.setCharacterEncoding("utf-8");
		try {
			response.getWriter().write(result);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e){
			e.printStackTrace();
		}
		//return "redirect:/account/queryAccountList.do";
		return "account/accountModify";
	}
	
	/**
	 * 进入增加页面
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/addPreAccount.do")
	public String preAddAccount(Map<String, Object> params, HttpServletRequest request,HttpServletResponse response){
		request.setAttribute("province", accountService.getProvince());
		request.setAttribute("channel_types", AccountVo.getChannels());
		return "account/accountAdd";
	}
	
	
	/**
	 * 删除账号信息
	 * @param account
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/deleteAccount.do")
	public String deleteAccount(AccountVo account, HttpServletRequest request,HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user =loginUserVo.getReal_name();//获取当前登录的人
		account.setOpt_person(user);
		account.setAcc_status("99");
		accountService.deleteAccount(account);
		
		return "redirect:/account/queryAccountList.do";
	}
	
	/**
	 * 添加账号信息
	 * @param account
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/addAccount.do")
	public String addAccount(AccountVo account,HttpServletRequest request,HttpServletResponse response){
		
		account.setAcc_status(AccountVo.FREE);
		String acc_username = this.getParam(request, "acc_username") ;
		//String acc_mail = this.getParam(request, "acc_mail");
		String username = accountService.queryAcc_username(acc_username);
		if(StringUtil.isNotEmpty(username)){
			return "redirect:/account/queryAccountList.do";
		}
		accountService.insertAccount(account);
		
		return "redirect:/account/queryAccountList.do";
	}
	
	@RequestMapping("/queryAcc_username.do")
	@ResponseBody
	public String queryAcc_username(HttpServletRequest request ,HttpServletResponse response){
		String acc_username = this.getParam(request, "acc_username") ;
		String username = accountService.queryAcc_username(acc_username);
		//System.out.println("username="+username);
		String result = null;
		if(StringUtils.isEmpty(accountService.queryAcc_username(acc_username))){
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
	
	@RequestMapping("/queryGetCity.do")
	@ResponseBody
	public String getCity(String provinceid,HttpServletRequest request,HttpServletResponse response){
	
		
		List<AreaVo> list = accountService.getCity(provinceid);
		
		
		ObjectMapper map = new ObjectMapper();
		try {
			map.writeValue(response.getOutputStream(), list);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping("/queryGetArea.do")
	@ResponseBody
	public String getArea(String cityid,HttpServletRequest request,HttpServletResponse response){
	
		List<AreaVo> list = accountService.getArea(cityid);

		ObjectMapper map = new ObjectMapper();
		try {
			map.writeValue(response.getOutputStream(), list);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	@RequestMapping("/updateShifangAccount.do")
	@ResponseBody
	public void updateShifangAccount(AccountVo account, HttpServletRequest request,HttpServletResponse response){
		String result="yes";
		try {
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
			String user =loginUserVo.getReal_name();//获取当前登录的人
			account.setOpt_person(user);
			logger.info(user+"点击了释放账号！");
			account.setOpt_logs(user+"点击了释放账号");
			accountService.updateAccount(account);
			request.setAttribute("channel_types", AccountVo.getChannels());
		} catch (Exception e) {
			result="no";
		}
		response.setCharacterEncoding("utf-8");
		try {
			response.getWriter().write(result);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/updateQiYongAccount.do")
	@ResponseBody
	public void updateQiYongAccount(AccountVo account, HttpServletRequest request,HttpServletResponse response){
		String result="yes";
		try {
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
			String user =loginUserVo.getReal_name();//获取当前登录的人
			account.setOpt_person(user);
			logger.info(user+"点击了启用账号！");
			account.setOpt_logs(user+"点击了启用账号");
			accountService.updateAccount(account);
			request.setAttribute("channel_types", AccountVo.getChannels());
		} catch (Exception e) {
			result="no";
		}
		response.setCharacterEncoding("utf-8");
		try {
			response.getWriter().write(result);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	@RequestMapping("/updateStopAccount.do")
	@ResponseBody
	public void updateStopAccount(AccountVo account, HttpServletRequest request,HttpServletResponse response){
		String result="yes";
		try {
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
			String user =loginUserVo.getReal_name();//获取当前登录的人
			account.setOpt_person(user);
			logger.info(user+"点击了账号停用！");
			account.setOpt_logs(user+"点击了账号停用！");
			String stop_reason = account.getStop_reason();
			accountService.updateAccount(account);
			request.setAttribute("channel_types", AccountVo.getChannels());
		} catch (Exception e) {
			result="no";
		}
		response.setCharacterEncoding("utf-8");
		try {
			response.getWriter().write(result);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 假删除
	 * @param account
	 * @param request
	 * @param response
	 */
	@RequestMapping("/updateDeleteAccount.do")
	@ResponseBody
	public void updateDeleteAccount(AccountVo account, HttpServletRequest request,HttpServletResponse response){
		String result="yes";
		try {
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
			String user =loginUserVo.getReal_name();//获取当前登录的人
			account.setOpt_person(user);
			logger.info(user+"点击了删除账号！");
			account.setAcc_status("99");
			account.setOpt_logs(user+"点击了删除账号！");
			accountService.deleteAccount(account);
		} catch (Exception e) {
			result="no";
		}
		response.setCharacterEncoding("utf-8");
		try {
			response.getWriter().write(result);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	@RequestMapping("/queryRegister.do")
	public String queryRegister(AccountVo account, HttpServletRequest request,HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String opt_person =loginUserVo.getReal_name();//获取当前登录的人
		logger.info(opt_person+"进行账号插入操作");
		int register_num = Integer.parseInt(this.getParam(request, "register_num"));
		String channel = this.getParam(request,"choose_channel");
		String account_source = this.getParam(request,"choose_source");
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("register_num", register_num);
		paramMap.put("channel", channel);
		paramMap.put("account_source", account_source);
		paramMap.put("opt_person", opt_person);
		int numbers = accountService.addRegistersBatch(paramMap);
		logger.info("成功插入数量:" +numbers);
		
		PrintWriter out;
		try {
			out = response.getWriter();
		
		StringBuilder builder = new StringBuilder();
		builder.append("<script type=\"text/javascript\" charset=\"UTF-8\">");
		builder.append("alert('成功插入"+numbers+"条数据'); window.location.href='/account/queryAccountList.do'"+"</script>");
		out.print(builder.toString());
		out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		return null;
	}
	
	
	@RequestMapping("/startQueryRegister.do")
	public String startQueryRegister(AccountVo account, HttpServletRequest request,HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String opt_person =loginUserVo.getReal_name();//获取当前登录的人
		
		int register_num = Integer.parseInt(this.getParam(request, "register_num"));
		String channel = this.getParam(request,"choose_channel");
		String account_source = this.getParam(request,"choose_source");
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("register_num", register_num);
		paramMap.put("channel", channel);
		paramMap.put("account_source", account_source);
		paramMap.put("opt_person", opt_person);
		
		int numbers = accountService.startRegistersBatch(paramMap);
		System.out.println("成功启用数量:" +numbers);
		
		PrintWriter out;
		try {
			out = response.getWriter();
		
		StringBuilder builder = new StringBuilder();
		builder.append("<script type=\"text/javascript\" charset=\"UTF-8\">");
		builder.append("alert('成功启用"+numbers+"条数据'); window.location.href='/account/queryAccountList.do'"+"</script>");
		out.print(builder.toString());
		out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		return null;
	}
	
	
	//批量停用
	@RequestMapping("/updateAbatch.do")
	public String updateAbatch(HttpServletRequest request,HttpServletResponse response){
		String stop_reason = this.getParam(request,"batchStopReason");
		String[] acc_id = request.getParameterValues("acc_id");
		String account_source = this.getParam(request, "account_source");
		
		
		for (int i = 0; i < acc_id.length; i++) {
			String acc_ids = acc_id[i];
			AccountVo account = new AccountVo();
			account.setAcc_id(acc_ids);
			account.setStop_reason(stop_reason);
			accountService.updateAbatchStop(account);
		}
		//return "redirect:/account/queryAccountList.do?acc_status=33&account_source="+account_source+"&pageIndex="+pageIndex;
		return "redirect:/account/queryAccountList.do?acc_status=33&account_source="+account_source;
	}
	
	//导出excel
	@RequestMapping("/exportexcel.do")
	public String exportExcel(HttpServletRequest request,
			HttpServletResponse response) {
		/*************************查询条件***************************/
		String at_province_id = this.getParam(request, "at_province_id");
		String at_city_id = this.getParam(request, "at_city_id");
		List<String> statusList = this.getParamToList(request, "acc_status");
		String accUserName = this.getParam(request, "acc_username");
		List<String> channel = this.getParamToList(request, "channel");
		List<String> stopReasonStr = this.getParamToList(request,"stopReason");
		List<String> stop_reasonStr = this.getParamToList(request,"stop_reason");
		List<String> SourceList = this.getParamToList(request, "account_source");
		String begin_info_time = this.getParam(request, "begin_info_time");
		String end_info_time = this.getParam(request, "end_info_time");
		String active_time = this.getParam(request, "active_time");
		String is_alive_0 = this.getParam(request, "is_alive_0");
		String is_alive_1 = this.getParam(request, "is_alive_1");
		String mail_163 = this.getParam(request, "mail_163");
		String mail_19trip = this.getParam(request, "mail_19trip");
		String mail_qita = this.getParam(request, "mail_qita");
		String acc_mail = this.getParam(request, "acc_mail");
//		List<String> statusList1 = new ArrayList<String>();
//		if(statusList.size()==1){
//			String statusSta[] = statusList.get(0).split(",");
//			for(int i=0;i<statusSta.length;i++){
//				statusList1.add(statusSta[i]);
//			}
//		}
//		List<String> reasonSt1 = new ArrayList<String>();
//		if(stop_reasonStr.size()==1){
//			String reasonStr[] = stop_reasonStr.get(0).split(",");
//			for(int i=0;i<reasonStr.length;i++){
//				reasonSt1.add(reasonStr[i]);
//			}
//		}
		/*************************创建Map***************************/
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("acc_username", accUserName);
		paramMap.put("acc_status", statusList);
//		paramMap.put("acc_status", statusList1);
		paramMap.put("at_province_id", at_province_id);
		paramMap.put("at_city_id", at_city_id);
		paramMap.put("channel", channel);
		paramMap.put("account_source", SourceList);
		paramMap.put("begin_info_time", begin_info_time);
		paramMap.put("end_info_time", end_info_time);
		paramMap.put("is_alive_0", is_alive_0);
		paramMap.put("is_alive_1", is_alive_1);
		paramMap.put("active_time", active_time);
		paramMap.put("mail_163", mail_163);
		paramMap.put("mail_19trip", mail_19trip);
		paramMap.put("mail_qita", mail_qita);
		paramMap.put("acc_mail", acc_mail);
		
		
		paramMap.put("mail_set", 2);
		if(!"".equals(mail_163) || !"".equals(mail_19trip) || !"".equals(mail_qita)){
			paramMap.put("mail_set", -1);
		}


		List<String> stopListr=new ArrayList<String>(stop_reasonStr);
		String reason = "";
		if(stopListr!=null && stopListr.size()>0){
			for(int i=0;i<stopListr.size();i++){
				reason = stopListr.get(i);
				if(reason.equals("000") || reason=="000"){
					stopListr.remove(reason);
					i--;
				}
			}
		}
		paramMap.put("stop_reason", stopListr);
		List<Map<String, String>> reslist = accountService.queryAccountExcel(paramMap);
		
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		for (Map<String, String> m : reslist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(m.get("province_name"));
			linkedList.add( AccountVo.getAccount_Source().get(m.get("account_source")));
			linkedList.add(m.get("acc_username"));
			linkedList.add(m.get("acc_password"));
			linkedList.add(m.get("acc_mail"));
			linkedList.add(m.get("pwd"));
			linkedList.add(AccountVo.getAccoutStatus().get(m.get("acc_status")));
			linkedList.add(m.get("create_time"));
			linkedList.add(m.get("option_time"));
			if("22".equals(m.get("acc_status")) && "3".equals("stop_reason"))linkedList.add(String.valueOf(m.get("offdays")));
			else linkedList.add(" ");
			linkedList.add(String.valueOf(m.get("contact_num")));
			linkedList.add(m.get("order_id"));
			linkedList.add(m.get("channel"));
			String stop_reason="";
		 	if("1".equals(m.get("stop_reason")))stop_reason="账号被封";
		 	if("2".equals(m.get("stop_reason")))stop_reason="取消订单过多";
		 	if("3".equals(m.get("stop_reason")))stop_reason="联系人达上限";
		 	if("4".equals(m.get("stop_reason")))stop_reason="未实名制";
		 	if("5".equals(m.get("stop_reason")))stop_reason="已达订购上限";
		 	if("6".equals(m.get("stop_reason")))stop_reason="用户取回";
		 	linkedList.add(stop_reason);
			linkedList.add(m.get("opt_person"));
			list.add(linkedList);
		}
		String title = "火车票账号管理明细";

		String date = createDate(begin_info_time, end_info_time);
		String filename = "火车票账号管理.xls";
		String[] secondTitles = { "序号", "所在省份","账号来源", "登陆名", "登陆密码",
				 "邮箱", "邮箱密码", "账号状态 ", "创建时间", "操作时间", "停用天数","联系人","订单号","渠道","停用原因","操作人"};
		HSSFWorkbook book = ExcelUtil.createExcel(filename, title, date,
				secondTitles, list, request, response);

		return null;
	}
	private String createDate(String begin_info_time, String end_info_time) {
		String date = "日期：";
		SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd");
		if (begin_info_time.equals(end_info_time)
				|| begin_info_time == end_info_time) {
			if (begin_info_time == null || "".equals(begin_info_time)) {
				date += ss.format(new Date());
			} else {
				date += begin_info_time;
			}
		} else {
			if (begin_info_time == null || "".equals(begin_info_time)) {
				if (end_info_time == null || "".equals(end_info_time)) {
					date += ss.format(new Date()) + "之前";
				} else {
					date += end_info_time + "之前";
				}
			} else {
				if (end_info_time == null || "".equals(end_info_time)) {
					date += begin_info_time + "-------" + ss.format(new Date());
				} else {
					date += begin_info_time + "-------" + end_info_time;
				}
			}
		}
		return date;
	}
	
}
