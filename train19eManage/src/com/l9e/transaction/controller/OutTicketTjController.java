package com.l9e.transaction.controller;

import java.text.DateFormat;
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

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.OutTicketTjService;
import com.l9e.transaction.service.ExtRefundService;
import com.l9e.transaction.vo.AccountVo;

/**
 * 渠道统计
 * @author zhangjc
 *
 */
@Controller
@RequestMapping("/outTicketTj")
public class OutTicketTjController extends BaseController{
	private static final Logger logger = Logger.getLogger(OutTicketTjController.class);
	@Resource
	private ExtRefundService extRefundService;
	@Resource
	private OutTicketTjService outTicketTjService;
	/**
	 * 查询页面
	
	 */
	@RequestMapping("/queryOutTicketTjPage.do")
	public String queryOutTicketTjPage(HttpServletResponse response ,HttpServletRequest request){
		Calendar theCa = Calendar.getInstance(); 
		theCa.setTime(new Date());  
		theCa.add(theCa.DATE, 0); 
		Date date = theCa.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String querydate=df.format(date);
		String device_type = request.getParameter("type");//识别pc和app参数
		String create_time = request.getParameter("create_time");
//		return "redirect:/outTicketTj/queryOutTicketTjList.do?create_time="+querydate+"&type="+device_type+"&create_times="+create_time;
		return "redirect:/outTicketTj/queryOutTicketTjList.do?create_time="+create_time+"&type="+device_type;
	
	}

	/**
	 * 查询列表
	 * @param request 
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryOutTicketTjList.do")
	public String queryOutTicketTjList(HttpServletRequest request,
			HttpServletResponse response){
		
		/******************查询条件********************/
//		String begin_info_time = this.getParam(request, "begin_info_time");//开始时间
//		String end_info_time = this.getParam(request, "end_info_time");//结束时间
		String create_time = this.getParam(request, "create_time");//时间
		
//		System.out.println(request.getParameter("create_times")+"-------------"+create_time);
		String device_type = request.getParameter("type");//识别pc和app参数
		List<String> channelList = this.getParamToList(request, "channel");//渠道
		List<String> channel = new ArrayList<String>(channelList);
		List<Map<String,String>> merchantList = extRefundService.queryExtMerchantinfo();//查询合作商户的id及名称
		Map<String, String> merchantMap = AccountVo.getChannels();
		for(int i = 0 ; i < merchantList.size() ; i++){
			merchantMap.put(merchantList.get(i).get("merchant_id"), merchantList.get(i).get("merchant_name"));
		}
		request.setAttribute("merchantList", merchantList);
		request.setAttribute("merchantMap", merchantMap);
		/******************查询Map********************/
		Map<String,Object>paramMap = new HashMap<String,Object>();
//		paramMap.put("begin_info_time", begin_info_time);//开始时间
//		paramMap.put("end_info_time", end_info_time);//结束时间
		paramMap.put("create_time", create_time);
		paramMap.put("channel", channel);
		paramMap.put("device_type", device_type);
		List<Map<String,String>> outTicketTjList = outTicketTjService.queryOutTicketTjList(paramMap);//总的出票效率
		request.setAttribute("outTicketTjList", outTicketTjList);
		int hourSize=outTicketTjList.size()/2;
		Calendar theCa = Calendar.getInstance(); 
		theCa.setTime(new Date());  
		theCa.add(theCa.DATE, 0); 
		Date date = theCa.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat df2 = new SimpleDateFormat("HH");
		String now=df.format(date);
		String hour=df2.format(date);
		/******************Request绑定开始********************/
		request.setAttribute("hour", hour);
		request.setAttribute("now", now);
//		request.setAttribute("begin_info_time", begin_info_time);
//		request.setAttribute("end_info_time", end_info_time);
		request.setAttribute("hourSize", hourSize);
		request.setAttribute("create_time", create_time);
		request.setAttribute("channelStr", channel.toString());
		request.setAttribute("isShowList", 1);
		request.setAttribute("device_type", device_type);
		
		return  "outTicketTj/outTicketTjList";
	}

}
