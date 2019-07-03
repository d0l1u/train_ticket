package com.l9e.transaction.controller;

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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.CompeteService;
import com.l9e.transaction.vo.AccountVo;
import com.l9e.transaction.vo.AllRefundVo;
import com.l9e.transaction.vo.CompeteVo;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.util.ExcelUtil;
import com.l9e.util.PageUtil;
/**
 * 竞价
 * @author zhangjc
 *
 */
@Controller
@RequestMapping("/compete")
public class CompeteController extends BaseController{
	@Resource
	private CompeteService competeService;
	/**
	 * 进入竞价页面
	 */
	@RequestMapping("/queryCompetePage.do")
	public String queryCompetePage(HttpServletRequest request,
			HttpServletResponse response){
		return "redirect:/compete/queryCompeteList.do";
	}
	/**
	 * 查询列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryCompeteList.do")
	public String queryCompeteList(HttpServletRequest request,
			HttpServletResponse response){
		
		/******************查询条件********************/
		String begin_info_time = this.getParam(request, "begin_info_time");//开始时间
		String end_info_time = this.getParam(request, "end_info_time");//结束时间
		String opt_ren = this.getParam(request, "opt_ren");
		List<String>compete_channel = this.getParamToList(request, "compete_channel");
		List<String> compete_goods = this.getParamToList(request,"compete_goods");
		
		/******************查询Map********************/
		Map<String,Object>paramMap = new HashMap<String,Object>();
		paramMap.put("begin_info_time", begin_info_time);//开始时间
		paramMap.put("end_info_time", end_info_time);//结束时间
		paramMap.put("opt_ren", opt_ren);
		paramMap.put("compete_channel", compete_channel);
		paramMap.put("compete_goods", compete_goods);
		
		/******************分页条件开始********************/
		int totalCount = competeService.queryCompeteCounts(paramMap);
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		
		/******************查询开始********************/
		List<Map<String,String>> competeList = competeService.queryCompeteList(paramMap);
		Calendar theCa = Calendar.getInstance(); 
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat dftime = new SimpleDateFormat("HH");
		theCa.setTime(new Date()); 
		Date date = theCa.getTime();
		String querydate=df.format(date);
		String querytime=dftime.format(date);
		System.out.println("@@@@@!$$$$$$$"+date);
		String compete_time="";
		if("7".equals(querytime)||"07".equals(querytime))compete_time="07:00-07:59";
		if("8".equals(querytime)||"08".equals(querytime))compete_time="08:00-08:59";
		if("9".equals(querytime)||"09".equals(querytime))compete_time="09:00-09:59";
		if("10".equals(querytime))compete_time="10:00-10:59";
		if("11".equals(querytime))compete_time="11:00-11:59";
		if("12".equals(querytime))compete_time="12:00-12:59";
		if("13".equals(querytime))compete_time="13:00-13:59";
		if("14".equals(querytime))compete_time="14:00-14:59";
		if("15".equals(querytime))compete_time="15:00-15:59";
		if("16".equals(querytime))compete_time="16:00-16:59";
		if("17".equals(querytime))compete_time="17:00-17:59";
		if("18".equals(querytime))compete_time="18:00-18:59";
		if("19".equals(querytime))compete_time="19:00-19:59";
		if("20".equals(querytime))compete_time="20:00-20:59";
		if("21".equals(querytime))compete_time="21:00-21:59";
		if("22".equals(querytime))compete_time="22:00-22:59";
		
		Map<String,Object>nowMap = new HashMap<String,Object>();
		List<Map<String,String>> now_compete1 = competeService.querynow_compete(nowMap);
		nowMap.put("compete_date", querydate);
		nowMap.put("compete_time", compete_time);
		System.out.println("@@@@@!$$$$$$$"+querydate+"######"+compete_time);
		List<Map<String,String>> now_compete2 = competeService.querynow_compete(nowMap);
		if(now_compete2.isEmpty())request.setAttribute("now_compete", now_compete1);
		else request.setAttribute("now_compete", now_compete2);
		Map<String,Object>paramMap2 = new HashMap<String,Object>();
		paramMap2.put("everyPagefrom", 0);
		paramMap2.put("pageSize", 20);
		List<Map<String,String>> history=competeService.queryCompeteHistory(paramMap2);
		/******************Request绑定开始********************/
		request.setAttribute("competeList", competeList);
		request.setAttribute("begin_info_time", begin_info_time);
		request.setAttribute("end_info_time", end_info_time);
		request.setAttribute("opt_ren", opt_ren);
		request.setAttribute("competeChannel", CompeteVo.getCompeteChannel());
		request.setAttribute("competeGoods", CompeteVo.getCompeteGoods());
		request.setAttribute("compete_channel", compete_channel.toString());
		request.setAttribute("compete_goods", compete_goods.toString());
		request.setAttribute("isShowList", 1);
		request.setAttribute("history", history);
		return "compete/competeList";
	}

	/**
	 * 跳转新增页面
	 * 
	 */
	@RequestMapping("/addCompetePage.do")
	public String addCompetePage(HttpServletRequest request,
			HttpServletResponse response){
		request.setAttribute("competeChannel", CompeteVo.getCompeteChannel());
		request.setAttribute("competeGoods", CompeteVo.getCompeteGoods());
		return "compete/addCompete";
	}
	
	/**
	 * 增加
	 */
	@RequestMapping("addCompete.do")
	public String addCompete(HttpServletRequest request, HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo)request.getSession().getAttribute("loginUserVo");
		String opt_ren = loginUserVo.getReal_name();//获得操作人姓名
		String compete_time = this.getParam(request, "compete_time");
		String compete_channel = this.getParam(request, "compete_channel");
		String compete_money_1 = this.getParam(request, "compete_money_1");
		String compete_money_un_1 = this.getParam(request, "compete_money_un_1");
		String compete_ranking_1 = this.getParam(request, "compete_ranking_1");
		String compete_ranking_un_1 = this.getParam(request, "compete_ranking_un_1");
		String compete_money_2 = this.getParam(request, "compete_money_2");
		String compete_money_un_2 = this.getParam(request, "compete_money_un_2");
		String compete_ranking_2 = this.getParam(request, "compete_ranking_2");
		String compete_ranking_un_2 = this.getParam(request, "compete_ranking_un_2");
		List<String> compete_top = this.getParamToList(request,"compete_top");
		List<String> compete_top_un = this.getParamToList(request,"compete_top_un");
		Map<String,Object>paramMap = new HashMap<String,Object>();
		paramMap.put("opt_ren", opt_ren);
		paramMap.put("compete_time", compete_time);
		paramMap.put("compete_channel", compete_channel);
		paramMap.put("compete_goods_1", "qunar1");
		paramMap.put("compete_money_1", compete_money_1);
		paramMap.put("compete_money_un_1", compete_money_un_1);
		paramMap.put("compete_ranking_1", compete_ranking_1);
		paramMap.put("compete_ranking_un_1", compete_ranking_un_1);
		paramMap.put("compete_goods_2", "qunar2");
		paramMap.put("compete_money_2", compete_money_2);
		paramMap.put("compete_money_un_2", compete_money_un_2);
		paramMap.put("compete_ranking_2", compete_ranking_2);
		paramMap.put("compete_ranking_un_2", compete_ranking_un_2);
		paramMap.put("compete_top", compete_top.toString());
		paramMap.put("compete_top_un", compete_top_un.toString());
		paramMap.put("compete_date", 1);
		paramMap.put("create_time", 1);
	     String time ="";
	        if("07:00-07:59".equals(compete_time)){time=" 07";}
			if("08:00-08:59".equals(compete_time)){time=" 08";}
			if("09:00-09:59".equals(compete_time)){time=" 09";}
			if("10:00-10:59".equals(compete_time)){time=" 10";}
			if("11:00-11:59".equals(compete_time)){time=" 11";}
			if("12:00-12:59".equals(compete_time)){time=" 12";}
			if("13:00-13:59".equals(compete_time)){time=" 13";}
			if("14:00-14:59".equals(compete_time)){time=" 14";}
			if("15:00-15:59".equals(compete_time)){time=" 15";}
			if("16:00-16:59".equals(compete_time)){time=" 16";}
			if("17:00-17:59".equals(compete_time)){time=" 17";}
			if("18:00-18:59".equals(compete_time)){time=" 18";}
			if("19:00-19:59".equals(compete_time)){time=" 19";}
			if("20:00-20:59".equals(compete_time)){time=" 20";}
			if("21:00-21:59".equals(compete_time)){time=" 21";}
			if("22:00-22:59".equals(compete_time)){time=" 22";}
	      Map<String, Object> paramMap2 =new HashMap<String, Object>();
	      paramMap2.put("hour",time);
	      Calendar theCa = Calendar.getInstance(); 
			theCa.setTime(new Date());  
			Date date = theCa.getTime();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String querydate=df.format(date);
	      paramMap2.put("create_time",querydate);
	      paramMap2.put("order_source", "qunar1");
	      int count_1=competeService.qunar_ticket_count(paramMap2);
	      paramMap2.put("order_source", "qunar2");
	      int count_2=competeService.qunar_ticket_count(paramMap2);
	  	paramMap.put("count_1", count_1);
		paramMap.put("count_2", count_2);
		competeService.addCompete(paramMap);
		
		return "redirect:/compete/queryCompeteList.do";
	}
	
	/**
	 * 跳转修改页面
	 * 
	 */
	@RequestMapping("/updateCompetePage.do")
	public String updateCompetePage(HttpServletRequest request,
			HttpServletResponse response){
		String compete_id = this.getParam(request, "compete_id");
		Map<String,Object>paramMap = new HashMap<String,Object>();
		paramMap.put("compete_id", compete_id);
		List<Map<String,String>> competeInfo=competeService.queryCompeteInfo(paramMap);
		request.setAttribute("compete", competeInfo);
		request.setAttribute("compete_id", compete_id);
		request.setAttribute("competeChannel", CompeteVo.getCompeteChannel());
		request.setAttribute("competeGoods", CompeteVo.getCompeteGoods());
		return "compete/competeInfo";
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("addhistory.do")
	public String updateCompete(HttpServletRequest request, HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo)request.getSession().getAttribute("loginUserVo");
		String opt_ren = loginUserVo.getReal_name();//获得操作人姓名
		String next_compete_money_1 = this.getParam(request, "next_compete_money_1");
		String next_compete_money_un_1 = this.getParam(request, "next_compete_money_un_1");
		String next_compete_money_2 = this.getParam(request, "next_compete_money_2");
		String next_compete_money_un_2 = this.getParam(request, "next_compete_money_un_2");
		Map<String,Object>paramMap = new HashMap<String,Object>();
		paramMap.put("compete_money_1", next_compete_money_1);
		paramMap.put("compete_money_un_1", next_compete_money_un_1);
		paramMap.put("compete_money_2", next_compete_money_2);
		paramMap.put("compete_money_un_2", next_compete_money_un_2);
		paramMap.put("opt_person", opt_ren);
		String content=opt_ren+"更新了下一小时竞价[19旅行:CDG  "+next_compete_money_1+"非CDG  "+next_compete_money_un_1+"]" +
				"[九九商旅:CDG  "+next_compete_money_2+"非CDG  "+next_compete_money_un_2+"]";
		paramMap.put("content", content);
		paramMap.put("create_time", "now()");
		competeService.addhistory(paramMap);
		
		return "redirect:/compete/queryCompeteList.do";
	}
	
	/**
	 * 操作日志页面
	 * 
	 */
	@RequestMapping("/queryHistory.do")
	public String queryHistory(HttpServletRequest request,
			HttpServletResponse response){
		int totalCount = competeService.queryCompeteHistoryCounts();
		PageVo page = PageUtil.getInstance().paging(request, 10, totalCount);
		Map<String,Object>paramMap = new HashMap<String,Object>();
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		List<Map<String,String>> history=competeService.queryCompeteHistory(paramMap);
		request.setAttribute("history", history);
		request.setAttribute("isShowList", 1);
		return "compete/competeHistory";
	}
	
	/**
	 * 当天竞价页面
	 * 
	 */
	@RequestMapping("/todayCompete.do")
	public String todayCompete(HttpServletRequest request,
			HttpServletResponse response){
		Calendar theCa = Calendar.getInstance(); 
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		theCa.setTime(new Date()); 
		Date date = theCa.getTime();
		String querydate=df.format(date);
		Map<String,Object>paramMap = new HashMap<String,Object>();
		paramMap.put("begin_info_time", querydate);//开始时间
		paramMap.put("end_info_time", querydate);//结束时间
		List<Map<String,String>> competeList = competeService.queryCompeteExcel(paramMap);
		
		List<Map<String,String>> competeList8=new ArrayList<Map<String,String>>();
		List<Map<String,String>> competeList9=new ArrayList<Map<String,String>>();
		List<Map<String,String>> competeList10=new ArrayList<Map<String,String>>();
		List<Map<String,String>> competeList11=new ArrayList<Map<String,String>>();
		List<Map<String,String>> competeList12=new ArrayList<Map<String,String>>();
		List<Map<String,String>> competeList13=new ArrayList<Map<String,String>>();
		List<Map<String,String>> competeList14=new ArrayList<Map<String,String>>();
		List<Map<String,String>> competeList15=new ArrayList<Map<String,String>>();
		List<Map<String,String>> competeList16=new ArrayList<Map<String,String>>();
		List<Map<String,String>> competeList17=new ArrayList<Map<String,String>>();
		List<Map<String,String>> competeList18=new ArrayList<Map<String,String>>();
		List<Map<String,String>> competeList19=new ArrayList<Map<String,String>>();
		List<Map<String,String>> competeList20=new ArrayList<Map<String,String>>();
		List<Map<String,String>> competeList21=new ArrayList<Map<String,String>>();
		List<Map<String,String>> competeList22=new ArrayList<Map<String,String>>();
		for(int i=0;i<competeList.size();i++){
			String compete_time=competeList.get(i).get("compete_time");
			if("08:00-08:59".equals(compete_time))competeList8.add(competeList.get(i));
			if("09:00-09:59".equals(compete_time))competeList9.add(competeList.get(i));
			if("10:00-10:59".equals(compete_time))competeList10.add(competeList.get(i));
			if("11:00-11:59".equals(compete_time))competeList11.add(competeList.get(i));
			if("12:00-12:59".equals(compete_time))competeList12.add(competeList.get(i));
			if("13:00-13:59".equals(compete_time))competeList13.add(competeList.get(i));
			if("14:00-14:59".equals(compete_time))competeList14.add(competeList.get(i));
			if("15:00-15:59".equals(compete_time))competeList15.add(competeList.get(i));
			if("16:00-16:59".equals(compete_time))competeList16.add(competeList.get(i));
			if("17:00-17:59".equals(compete_time))competeList17.add(competeList.get(i));
			if("18:00-18:59".equals(compete_time))competeList18.add(competeList.get(i));
			if("19:00-19:59".equals(compete_time))competeList19.add(competeList.get(i));
			if("20:00-20:59".equals(compete_time))competeList20.add(competeList.get(i));
			if("21:00-21:59".equals(compete_time))competeList21.add(competeList.get(i));
			if("22:00-22:59".equals(compete_time))competeList22.add(competeList.get(i));
		}
		request.setAttribute("competeList8", competeList8);
		request.setAttribute("competeList9", competeList9);
		request.setAttribute("competeList10", competeList10);
		request.setAttribute("competeList11", competeList11);
		request.setAttribute("competeList12", competeList12);
		request.setAttribute("competeList13", competeList13);
		request.setAttribute("competeList14", competeList14);
		request.setAttribute("competeList15", competeList15);
		request.setAttribute("competeList16", competeList16);
		request.setAttribute("competeList17", competeList17);
		request.setAttribute("competeList18", competeList18);
		request.setAttribute("competeList19", competeList19);
		request.setAttribute("competeList20", competeList20);
		request.setAttribute("competeList21", competeList21);
		request.setAttribute("competeList22", competeList22);
		return "compete/competeToday";
	}
	/*
	 * 导出
	 */
	@RequestMapping("exportExcel.do")
	public String exportExcel(HttpServletRequest request, HttpServletResponse response){
		/******************查询条件********************/
		String begin_info_time = this.getParam(request, "begin_info_time");//开始时间
		String end_info_time = this.getParam(request, "end_info_time");//结束时间
		String opt_ren = this.getParam(request, "opt_ren");
		List<String>compete_channel = this.getParamToList(request, "compete_channel");
		
		/******************查询Map********************/
		Map<String,Object>paramMap = new HashMap<String,Object>();
		paramMap.put("begin_info_time", begin_info_time);//开始时间
		paramMap.put("end_info_time", end_info_time);//结束时间
		paramMap.put("opt_ren", opt_ren);
		paramMap.put("compete_channel", compete_channel);
		
		/******************查询开始********************/
		List<Map<String,String>> competeList = competeService.queryCompeteExcel(paramMap);
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		for (Map<String, String> m : competeList) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(String.valueOf(m.get("compete_date")));
			linkedList.add(m.get("compete_time"));
			linkedList.add(m.get("compete_channel"));
			linkedList.add(m.get("compete_goods_1"));
			linkedList.add(String.valueOf(m.get("compete_money_1")));
			linkedList.add(String.valueOf(m.get("compete_money_un_1")));
		    linkedList.add(m.get("compete_ranking_1"));
			linkedList.add(m.get("compete_ranking_un_1"));
			linkedList.add(String.valueOf(m.get("count_1")));
			linkedList.add(m.get("compete_goods_2"));
			linkedList.add(String.valueOf(m.get("compete_money_2")));
			linkedList.add(String.valueOf(m.get("compete_money_un_2")));
		    linkedList.add(m.get("compete_ranking_2"));
			linkedList.add(m.get("compete_ranking_un_2"));
			linkedList.add(String.valueOf(m.get("count_2")));
			linkedList.add(String.valueOf(m.get("create_time")));
			linkedList.add(m.get("opt_ren"));
			list.add(linkedList);
		}
		String title = "竞价明细";
		String date = createDate(begin_info_time, end_info_time);
		String filename = "火车票竞价表.xls";
		String[] secondTitles = { "序号", "竞价日期", "竞价时段", "渠道","产品", "CDG竞价",
				"非CDG竞价", "CDG排名",  "非CDG排名" ,"票数","产品", "CDG竞价",
				"非CDG竞价", "CDG排名",  "非CDG排名" ,"票数","创建时间","操作人"};
		HSSFWorkbook book = ExcelUtil.createExcel(filename, title, date,
				secondTitles, list, request, response);
		
		
		return null;
	}
	private String createDate(String begin_create_time, String end_create_time) {
		String date = "日期：";
		SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd");
		if (begin_create_time.equals(end_create_time)
				|| begin_create_time == end_create_time) {
			if (begin_create_time == null || "".equals(begin_create_time)) {
				date += ss.format(new Date());
			} else {
				date += begin_create_time;
			}
		} else {
			if (begin_create_time == null || "".equals(begin_create_time)) {
				if (end_create_time == null || "".equals(end_create_time)) {
					date += ss.format(new Date()) + "之前";
				} else {
					date += end_create_time + "之前";
				}
			} else {
				if (end_create_time == null || "".equals(end_create_time)) {
					date += begin_create_time + "-------" + ss.format(new Date());
				} else {
					date += begin_create_time + "-------" + end_create_time;
				}
			}
		}
		return date;
	}
	
}
