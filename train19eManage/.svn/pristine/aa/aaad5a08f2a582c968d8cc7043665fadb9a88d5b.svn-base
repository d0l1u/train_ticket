package com.l9e.transaction.controller;

import java.net.URLDecoder;
import java.text.DateFormat;
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
import com.l9e.transaction.service.ChanneltjService;
import com.l9e.transaction.service.ExtRefundService;
import com.l9e.transaction.vo.AccountVo;
import com.l9e.transaction.vo.AllRefundVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.util.ExcelUtil;
import com.l9e.util.PageUtil;

/**
 * 渠道统计
 * @author zhangjc
 *
 */
@Controller
@RequestMapping("/channeltj")
public class ChannelTjController extends BaseController{
	private static final Logger logger = Logger.getLogger(ChannelTjController.class);
	@Resource
	private ExtRefundService extRefundService;
	@Resource
	private ChanneltjService channeltjService;
	/**
	 * 查询页面
	
	 */
	@RequestMapping("/queryChanneltjPage.do")
	public String queryChanneltjPage(HttpServletResponse response ,HttpServletRequest request){
		Calendar theCa = Calendar.getInstance(); 
		theCa.setTime(new Date());  
		theCa.add(theCa.DATE, -2); 
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
		    return "redirect:/channeltj/queryChanneltjList.do?begin_info_time="+querydate+str;
		}else{
			return "redirect:/channeltj/queryChanneltjList.do?begin_info_time="+querydate;
		}  
	
	}

	/**
	 * 查询列表
	 * @param request 
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryChanneltjList.do")
	public String queryChanneltjList(HttpServletRequest request,
			HttpServletResponse response){
		
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
		int totalCount = channeltjService.queryChanneltjCounts(paramMap);
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		
		/******************查询开始********************/
		List<Map<String,String>> channeltjList = channeltjService.queryChanneltjList(paramMap);
		
		/******************Request绑定开始********************/
		request.setAttribute("channeltjList", channeltjList);
		request.setAttribute("begin_info_time", begin_info_time);
		request.setAttribute("end_info_time", end_info_time);
		request.setAttribute("channelStr", channel.toString());
		request.setAttribute("isShowList", 1);
		return  "channeltj/channeltjList";
	}


	
	//导出excel
	@RequestMapping("/exportChanneltjexcel.do")
	public String exportChanneltjexcel(HttpServletRequest request,
			HttpServletResponse response) {
		/******************查询条件********************/
		String begin_info_time = this.getParam(request, "begin_info_time");//开始时间
		String end_info_time = this.getParam(request, "end_info_time");//结束时间
		List<String> channelList = this.getParamToList(request, "channel");//渠道
		List<String> channel = new ArrayList<String>(channelList);
		/******************查询Map********************/
		Map<String,Object>paramMap = new HashMap<String,Object>();
		paramMap.put("begin_info_time", begin_info_time);//开始时间
		paramMap.put("end_info_time", end_info_time);//结束时间
		paramMap.put("channel", channel);
		
		List<Map<String, String>> reslist = channeltjService.queryChanneltjExcel(paramMap);
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		for (Map<String, String> m : reslist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(m.get("channel"));
			linkedList.add(String.valueOf(m.get("create_time")));
			linkedList.add(String.valueOf(m.get("search_count")));
			linkedList.add(String.valueOf(m.get("search_success")));
			linkedList.add(String.valueOf(m.get("search_fail")));
			linkedList.add(String.valueOf(m.get("msg_count")));
			linkedList.add(String.valueOf(m.get("alter_count")));
			linkedList.add(String.valueOf(m.get("alter_success")));
			linkedList.add(String.valueOf(m.get("alter_fail")));
			linkedList.add(String.valueOf(m.get("refund_count")));
			linkedList.add(String.valueOf(m.get("refund_success")));
			linkedList.add(String.valueOf(m.get("refund_fail")));
			linkedList.add(m.get("alter_success_cgl"));
			linkedList.add(m.get("refund_success_cgl"));
			list.add(linkedList);
		}

		String title = "火车票渠道统计明细";
		String date = createDate(begin_info_time, end_info_time);
		String filename = "火车票渠道统计.xls";
		String[] secondTitles = { "序号", "渠道", "日期", "查询总数","查询成功", "查询失败",
				"下发短信", "改签总数", "改签成功", "改签失败",  "退款总数" ,"退款成功","退款失败","改签成功率","退款成功率"};
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
