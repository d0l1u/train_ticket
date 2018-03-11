package com.l9e.transaction.controller;

import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.AcquireService;
import com.l9e.transaction.service.ExtRefundService;
import com.l9e.transaction.service.FailtjService;
import com.l9e.transaction.vo.AccountVo;
import com.l9e.transaction.vo.AcquireVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.util.ExcelUtil;
import com.l9e.util.PageUtil;

/**
 * 失败订单统计
 * @author zhangjc
 *
 */
@Controller
@RequestMapping("/failtj")
public class FailOrderTjController extends BaseController{
	private static final Logger logger = Logger.getLogger(FailOrderTjController.class);
	@Resource
	private FailtjService failtjService;
	@Resource
	private ExtRefundService extRefundService;
	@Resource
	private AcquireService AcquireService;
	/**
	 * 查询页面
	 */
	@RequestMapping("/queryFailtjPage.do")
	public String queryFailtjPage(HttpServletResponse response ,HttpServletRequest request){
		Calendar theCa = Calendar.getInstance(); 
		theCa.setTime(new Date());  
		theCa.add(theCa.DATE, -7); 
		Date date = theCa.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String querydate=df.format(date);
		String str = "";
		Cookie[] cookies = request.getCookies();  
		if (cookies != null && cookies.length > 0) { //如果没有设置过Cookie会返回null  
		    for (Cookie cookie : cookies) {
		    	if(cookie.getName().equals("channel")){
		    		String channelStr = URLDecoder.decode(cookie.getValue());
		    		System.out.println(channelStr);
		    		String[] channel; //定义一数组
		    		channel = channelStr.split(",");
		    		for (int i=0;i<channel.length ;i++ ){
				    		str += "&channel="+channel[i];
		    		} 
		    	}
		    } 
		    Cookie c = new Cookie("one","yes");
		    c.setMaxAge(7*24*60*60);
		    response.addCookie(c);
		    return "redirect:/failtj/queryFailtjList.do?begin_info_time="+querydate+str;
		}else{
			return "redirect:/failtj/queryFailtjList.do?begin_info_time="+querydate;
		}  
	
		
	}

	/**
	 * 查询列表
	 */
	@RequestMapping("/queryFailtjList.do")
	public String queryFailtjList(HttpServletRequest request,
			HttpServletResponse response){
		logger.info("【进入出票失败统计页面】queryFailtjList.do");
		/******************查询条件********************/
		String begin_info_time = this.getParam(request, "begin_info_time");//开始时间
		String end_info_time = this.getParam(request, "end_info_time");//结束时间
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
		paramMap.put("begin_info_time", begin_info_time);//开始时间
		paramMap.put("end_info_time", end_info_time);//结束时间
		paramMap.put("channel", channel);
		/******************分页条件开始********************/
		int totalCount = failtjService.queryFailtjCounts(paramMap);
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		
		/******************查询开始********************/
		List<Map<String,String>> failtjList = failtjService.queryFailtjList(paramMap);
		
		/******************Request绑定开始********************/
		request.setAttribute("failtjList", failtjList);
		request.setAttribute("begin_info_time", begin_info_time);
		request.setAttribute("end_info_time", end_info_time);
		request.setAttribute("isShowList", 1);
		request.setAttribute("channelStr", channel.toString());
		return  "failtj/failtjList";
	}

	/**
	 * 查询列表
	 */
	@RequestMapping("/queryFailtjInfo.do")
	public String queryFailtjInfo(HttpServletRequest request,HttpServletResponse response){
		logger.info("【进入出票失败统计明细】queryFailtjInfo.do");
		/******************查询条件********************/
		String begin_info_time = this.getParam(request, "begin_info_time");//开始时间
		String order_id = this.getParam(request, "order_id");//订单号
		String level = this.getParam(request, "level");//订单级别
		String opt_ren = this.getParam(request, "opt_ren");//操作人
		String channelList = this.getParam(request, "channel");//渠道
		logger.info("【进入出票失败统计明细】queryFailtjInfo.do"+"时间："+begin_info_time+"渠道："+channelList);
		List<String> channel = new ArrayList<String>();
				channel.add(channelList);
			if(channel.contains("30101612")){
				channel.add("301016");
				channel.add("30101601");
				channel.add("30101602");
		    }
			
		List<String> errorInfoList = new ArrayList<String>();//失败原因
		String ERROR_INFO_1 = this.getParam(request, "ERROR_INFO_1");
		String ERROR_INFO_2 = this.getParam(request, "ERROR_INFO_2");
		String ERROR_INFO_3 = this.getParam(request, "ERROR_INFO_3");
		String ERROR_INFO_4 = this.getParam(request, "ERROR_INFO_4");
		String ERROR_INFO_5 = this.getParam(request, "ERROR_INFO_5");
		String ERROR_INFO_6 = this.getParam(request, "ERROR_INFO_6");
		String ERROR_INFO_7 = this.getParam(request, "ERROR_INFO_7");
		String ERROR_INFO_8 = this.getParam(request, "ERROR_INFO_8");
		String ERROR_INFO_11 = this.getParam(request, "ERROR_INFO_11");
		String ERROR_INFO_12 = this.getParam(request, "ERROR_INFO_12");
		String ERROR_INFO_9 = this.getParam(request, "ERROR_INFO_9");
		if(!"".equals(ERROR_INFO_1))errorInfoList.add("1");
		if(!"".equals(ERROR_INFO_2))errorInfoList.add("2");
		if(!"".equals(ERROR_INFO_3))errorInfoList.add("3");
		if(!"".equals(ERROR_INFO_4))errorInfoList.add("4");
		if(!"".equals(ERROR_INFO_5))errorInfoList.add("5");
		if(!"".equals(ERROR_INFO_6))errorInfoList.add("6");
		if(!"".equals(ERROR_INFO_7))errorInfoList.add("7");
		if(!"".equals(ERROR_INFO_8))errorInfoList.add("8");
		if(!"".equals(ERROR_INFO_11))errorInfoList.add("11");
		if(!"".equals(ERROR_INFO_12))errorInfoList.add("12");
		if(!"".equals(ERROR_INFO_9))errorInfoList.add("9");
		request.setAttribute("ERROR_INFO_1", ERROR_INFO_1);
		request.setAttribute("ERROR_INFO_2", ERROR_INFO_2);
		request.setAttribute("ERROR_INFO_3", ERROR_INFO_3);
		request.setAttribute("ERROR_INFO_4", ERROR_INFO_4);
		request.setAttribute("ERROR_INFO_5", ERROR_INFO_5);
		request.setAttribute("ERROR_INFO_6", ERROR_INFO_6);
		request.setAttribute("ERROR_INFO_7", ERROR_INFO_7);
		request.setAttribute("ERROR_INFO_8", ERROR_INFO_8);
		request.setAttribute("ERROR_INFO_11", ERROR_INFO_11);
		request.setAttribute("ERROR_INFO_12", ERROR_INFO_12);
		request.setAttribute("ERROR_INFO_9", ERROR_INFO_9);
		
		List<String> errorInfoQunarList = this.getParamToList(request, "error_qunar_info");//去哪儿失败原因
		Map<String, Object> paramMap2 = new HashMap<String, Object>();
		paramMap2.put("begin_info_time", begin_info_time);
		paramMap2.put("end_info_time", begin_info_time);
		paramMap2.put("channel", channel);
		List<String> orderStatus=new ArrayList<String>();
		orderStatus.add("10");
		int totalAllCount = AcquireService.queryAcquireFailListCount(paramMap2);
		paramMap2.put("order_status", orderStatus);
		int totalCount2 = AcquireService.queryAcquireFailListCount(paramMap2);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("order_id", order_id);
		paramMap.put("level", level);
		paramMap.put("opt_ren", opt_ren);
		paramMap.put("begin_info_time", begin_info_time);
		paramMap.put("end_info_time", begin_info_time);
		paramMap.put("order_status", orderStatus);
		paramMap.put("channel", channel);
		if(channel.indexOf("qunar")>=0) {paramMap.put("error_info", errorInfoQunarList);	}	
		else {paramMap.put("error_info", errorInfoList);	}
					
		int totalCount = AcquireService.queryAcquireFailListCount(paramMap);//总条数	
		//分页
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		List<Map<String, String>> acquireFailList = AcquireService.queryAcquireFailList(paramMap);
		List<Map<String,String>> merchantList = extRefundService.queryExtMerchantinfo();//查询合作商户的id及名称
		Map<String, String> channelTypes = AccountVo.getChannels();
		Map<String, String> channel_types = AccountVo.getErrorInfoChannels();
		for(int i = 0 ; i < merchantList.size() ; i++){
			channelTypes.put(merchantList.get(i).get("merchant_id"), merchantList.get(i).get("merchant_name"));
			channel_types.put(merchantList.get(i).get("merchant_id"), merchantList.get(i).get("merchant_name"));
		}
		
		request.setAttribute("merchantList", merchantList);
		request.setAttribute("acquireList", acquireFailList);
		request.setAttribute("isShowList", 1);
		request.setAttribute("acquireStatus", AcquireVo.getAcquireStatus());
		request.setAttribute("order_id", order_id);
		request.setAttribute("level", level);
		request.setAttribute("opt_ren", opt_ren);
		request.setAttribute("begin_info_time", begin_info_time);
		request.setAttribute("seat_Types", AcquireVo.getSEAT_TYPES());
		request.setAttribute("channel", channelList);
		request.setAttribute("channel_types", channel_types);
		request.setAttribute("channelTypes", channelTypes);
		request.setAttribute("errorInfoStr", errorInfoList.toString());
		request.setAttribute("errorInfoQunarStr", errorInfoQunarList.toString());
		request.setAttribute("error_infos", AccountVo.getErrorInfos());
		request.setAttribute("error_info_qunars", AccountVo.getErrorInfoQunars());
		request.setAttribute("error_info_elongs", AccountVo.getErrorInfoElongs());
		request.setAttribute("totalCount", totalCount);
		DecimalFormat decimal = new DecimalFormat("0.00");
		double zhanbi=(double)totalCount*100/(double)totalCount2;
		request.setAttribute("totalCount2", totalCount2);
		if(totalCount2==0)request.setAttribute("zhanbi", "0.00");
		else request.setAttribute("zhanbi",decimal.format(zhanbi));	
		double zhanbi_all=(double)totalCount*100/(double)totalAllCount;
		if(totalAllCount==0)request.setAttribute("zhanbi_all", "0.00");
		else request.setAttribute("zhanbi_all",decimal.format(zhanbi_all));	
		return  "failtj/failtjInfo";
	}
}
