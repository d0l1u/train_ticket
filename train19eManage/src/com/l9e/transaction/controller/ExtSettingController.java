package com.l9e.transaction.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.ExtSettingService;
import com.l9e.transaction.vo.ExtSettingVo;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.PageUtil;
@Controller
@RequestMapping("/extSetting")
public class ExtSettingController extends BaseController {
	@Resource
	private ExtSettingService extSettingService;
	
	//查询商户列表
	@RequestMapping("/queryExtSettingList.do")
	public String queryExtSettingList(HttpServletRequest request,
			HttpServletResponse response) {
		String merchant_id = this.getParam(request, "merchant_id"); // 合作商户编号
		String merchant_name = this.getParam(request, "merchant_name"); // 合作商户名称
		//查询条件
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("merchant_id", merchant_id);
		paramMap.put("merchant_name", merchant_name);
		
		int totalCount = extSettingService.queryExtSettingListCount(paramMap);// 总条数
		// 分页
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());// 每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());// 每页显示的条数
		
		List<ExtSettingVo> extSettingList = extSettingService.queryExtSettingList(paramMap);
		
		request.setAttribute("extSettingList", extSettingList);
		request.setAttribute("merchant_id", merchant_id);
		request.setAttribute("merchant_name", merchant_name);
		request.setAttribute("payTypes", ExtSettingVo.getPayTypes());//支付方式
		request.setAttribute("merchantFees", ExtSettingVo.getMerchantFees());//扣费方式
		request.setAttribute("bxCompanys", ExtSettingVo.getBxCompanys());//保险
		request.setAttribute("smsChannels", ExtSettingVo.getSmnChannels());//短信渠道
		//request.setAttribute("ticketTimeLimits", ExtSettingVo.getTicketTimeLimits());//默认乘车日期
		request.setAttribute("merchantStatuss", ExtSettingVo.getMerchantStatuss());//商户状态
		request.setAttribute("isShowList", 1);
		return "extSetting/extSettingList";
	}
	
	//修改商户状态（启用，停用）
	@RequestMapping("/updateMerchantStatus.do")
	public String updateMerchantStatus(HttpServletRequest request,
			HttpServletResponse response) {
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
		String opt_person = loginUserVo.getReal_name();//当前登录人 
		String merchant_id = this.getParam(request, "merchant_id"); // 合作商户编号
		String merchant_name = extSettingService.queryMerchantInfo(merchant_id).getMerchant_name();
		String merchant_status = this.getParam(request, "merchant_status"); // 合作商户状态（00、停用 11、启用）
		String merchant_stop_reason =this.getParam(request, "merchant_stop_reason");//商户停用原因
		//查询条件
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("merchant_id", merchant_id);
		paramMap.put("merchant_status", merchant_status);
		paramMap.put("merchant_stop_reason", merchant_stop_reason);
		paramMap.put("opt_person", opt_person);
		String content = "";
		if(merchant_status.equals("11")){
			content = "更改商户状态为：启用";
		}else{
			content = "更改商户状态为：停用，停用原因为："+merchant_stop_reason;
		}
		Map<String, String> logMap = new HashMap<String, String>();
		logMap.put("merchant_id", merchant_id);
		logMap.put("merchant_name", merchant_name);
		logMap.put("content", content);
		logMap.put("opt_person", opt_person);
		extSettingService.updateMerchantStatus(paramMap, logMap);//修改商户状态（启用，停用）
		return "redirect:/extSetting/queryExtSettingList.do";
	}
	
	//跳转到添加商户的页面
	@RequestMapping("toAddMarchantInfo.do")
	public String toAddMarchantInfo(HttpServletRequest request,HttpServletResponse response){
		request.setAttribute("merchantFees", ExtSettingVo.getMerchantFees());//扣费方式
		request.setAttribute("bxCompanys", ExtSettingVo.getBxCompanys());//保险
		request.setAttribute("smsChannels", ExtSettingVo.getSmnChannels());//短信渠道
		request.setAttribute("payTypes", ExtSettingVo.getPayTypes());//支付方式
		return "extSetting/addMarchantInfo";
	}
	//添加商户
	@RequestMapping("addMarchantInfo.do")
	public String addMarchantInfo(HttpServletRequest request, HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo)request.getSession().getAttribute("loginUserVo");
		String opt_person = loginUserVo.getReal_name();//获得操作人姓名opt_person
		/******************更改条件********************/
		String merchant_name = this.getParam(request, "merchant_name");						 
		String merchant_id = this.getParam(request, "merchant_id");							 
		String merchant_fee = this.getParam(request, "merchant_fee");
		String bx_company = this.getParam(request, "bx_company");	 
		String pay_type = this.getParam(request, "pay_type");	 
		String sms_channel = this.getParam(request, "sms_channel");	
		String spare_ticket_amount = this.getParam(request, "spare_ticket_amount");				 
		String stop_buyTicket_time = this.getParam(request, "stop_buyTicket_time");		
		String content = "添加新商户--扣费方式为【"+merchant_fee+"】保险为【"+bx_company+"】支付方式为【"+pay_type+
			"】短信为【"+sms_channel+"】余票阀值为【"+spare_ticket_amount+"】开车之前的停止购票时间为【"+stop_buyTicket_time+"】";
		/******************创建容器********************/
		Map<String, Object> map_add = new HashMap<String, Object>();
		map_add.put("merchant_name", merchant_name);
		map_add.put("merchant_id", merchant_id);
		map_add.put("merchant_fee", merchant_fee);
		map_add.put("pay_type", pay_type);
		map_add.put("bx_company", bx_company);
		map_add.put("sms_channel", sms_channel);
		map_add.put("stop_buyTicket_time",stop_buyTicket_time);
		map_add.put("spare_ticket_amount",spare_ticket_amount);
		map_add.put("opt_person", opt_person);
		map_add.put("merchant_status", "11");
		/****************将条件添加到容器******************/
		Map<String, String> logMap = new HashMap<String, String>();
		logMap.put("merchant_id", merchant_id);
		logMap.put("merchant_name", merchant_name);
		logMap.put("content", content);
		logMap.put("opt_person", opt_person);
		/****************执行操作******************/
		extSettingService.addMarchantInfo(map_add, logMap);
		return "redirect:/extSetting/queryExtSettingList.do";
	}
	
	//查看merchant_id是否存在
	@RequestMapping("/queryMarchantId.do")
	@ResponseBody
	public void queryMarchantId(HttpServletRequest request ,HttpServletResponse response){
		String merchant_id = this.getParam(request, "merchant_id");						
		String merchantId = extSettingService.queryMarchantId(merchant_id);
		String result = null;
		if(StringUtils.isEmpty(merchantId)){
			result = "no";
		}else{
			result = "yes";
		}
		try {
			response.getWriter().write(result);
			response.getWriter().flush();
			response.getWriter().close() ;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//跳转到修改商户信息的页面
	@RequestMapping("/toUpdateMerchantInfo.do")
	public String toUpdateMerchantInfo(HttpServletRequest request, HttpServletResponse response){
		String merchant_id = this.getParam(request, "merchant_id");
		ExtSettingVo merchantInfo = extSettingService.queryMerchantInfo(merchant_id);
		request.setAttribute("merchantInfo", merchantInfo);
		request.setAttribute("merchantFees", ExtSettingVo.getMerchantFees());//扣费方式
		request.setAttribute("bxCompanys", ExtSettingVo.getBxCompanys());//保险
		request.setAttribute("smsChannels", ExtSettingVo.getSmnChannels());//短信渠道
		request.setAttribute("payTypes", ExtSettingVo.getPayTypes());//支付方式
		return "extSetting/updateMarchantInfo";
	}
	
	//修改商户信息
	@RequestMapping("/updateMarchantInfo.do")
	public String updateMarchantInfo(HttpServletRequest request, HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo)request.getSession().getAttribute("loginUserVo");
		String opt_person = loginUserVo.getReal_name();//获得操作人姓名opt_person
		/******************更改条件********************/
		String merchant_id = this.getParam(request, "merchant_id");	
		String merchant_name = this.getParam(request, "merchant_name");
		String merchant_fee = this.getParam(request, "merchant_fee");
		String bx_company = this.getParam(request, "bx_company");	 
		String pay_type = this.getParam(request, "pay_type");	 
		String sms_channel = this.getParam(request, "sms_channel");	
		String spare_ticket_amount = this.getParam(request, "spare_ticket_amount");				 
		String stop_buyTicket_time = this.getParam(request, "stop_buyTicket_time");	
		String verify_status = this.getParam(request, "verify_status");	
		ExtSettingVo merchantInfo = extSettingService.queryMerchantInfo(merchant_id);
		//String content = "修改商户设置--扣费方式【"+merchant_fee+""+",保险为:"+bx_company+",支付方式为："+pay_type+
			//",短信为："+sms_channel+",余票阀值为："+spare_ticket_amount+",开车之前的停止购票时间为："+stop_buyTicket_time+"】";
		StringBuilder content = new StringBuilder("");   
		content.append("修改商户设置--");
		if(!merchantInfo.getMerchant_fee().equals(merchant_fee)){
			content.append("扣费方式【"+merchantInfo.getMerchant_fee()+"改为"+merchant_fee+"】");
		}
		if(!merchantInfo.getBx_company().equals(bx_company)){
			content.append("保险单位【"+merchantInfo.getBx_company()+"改为"+bx_company+"】");
		}
		if(!merchantInfo.getPay_type().equals(pay_type)){
			content.append("支付方式【"+merchantInfo.getPay_type()+"改为"+pay_type+"】");
		}
		if(!merchantInfo.getSms_channel().equals(sms_channel)){
			content.append("短信渠道【"+merchantInfo.getSms_channel()+"改为"+sms_channel+"】");
		}
		if(!merchantInfo.getSpare_ticket_amount().equals(spare_ticket_amount)){
			content.append("余票阀值【"+merchantInfo.getSpare_ticket_amount()+"改为"+spare_ticket_amount+"】");
		}
		if(!merchantInfo.getStop_buyTicket_time().equals(stop_buyTicket_time)){
			content.append("开车前订票时间【"+merchantInfo.getStop_buyTicket_time()+"改为"+stop_buyTicket_time+"】");
		}
		if(!merchantInfo.getVerify_status().equals(verify_status)){
			content.append("验证状态【"+merchantInfo.getVerify_status()+"改为"+verify_status+"】");
		}
		/******************创建容器********************/
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("merchant_id", merchant_id);
		paramMap.put("merchant_fee", merchant_fee);
		paramMap.put("pay_type", pay_type);
		paramMap.put("bx_company", bx_company);
		paramMap.put("sms_channel", sms_channel);
		paramMap.put("stop_buyTicket_time",stop_buyTicket_time);
		paramMap.put("spare_ticket_amount",spare_ticket_amount);
		paramMap.put("verify_status",verify_status);
		paramMap.put("opt_person", opt_person);
		/****************将条件添加到容器******************/
		Map<String, String> logMap = new HashMap<String, String>();
		logMap.put("merchant_id", merchant_id);
		logMap.put("merchant_name", merchant_name);
		logMap.put("content", content.toString());
		logMap.put("opt_person", opt_person);
		/****************执行操作******************/
		extSettingService.updateMarchantInfo(paramMap, logMap);
		return "redirect:/extSetting/queryExtSettingList.do";
	}
	
	//查看操作日志
	@RequestMapping("/querySystemSetList.do")
	public String querySystemSettingList(HttpServletRequest request, HttpServletResponse response){
		Map<String,Object>paramMap = new HashMap<String,Object>();
		/******************分页条件开始********************/
		int totalCount = extSettingService.queryMarchantLogListCount();
		PageVo page = PageUtil.getInstance().paging(request, 10, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		/******************操作********************/
		List<Map<String,Object>> systemList = extSettingService.queryMarchantLogList(paramMap);
		request.setAttribute("systemList", systemList);
		request.setAttribute("isShowList", 1);
		return "extSetting/marchantLogList";
	}
}
