package com.l9e.transaction.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.AccounttjService;
import com.l9e.transaction.vo.AccountStatistics;
import com.l9e.transaction.vo.PageVo;
import com.l9e.util.ExcelUtil;
import com.l9e.util.PageUtil;

/**
 * 渠道统计
 * 
 * @author zhangjc
 *
 */
@Controller
@RequestMapping("/accounttj")
public class AccountTjController extends BaseController {

	private Logger logger = Logger.getLogger(AccountTjController.class);
	@Resource
	private AccounttjService accounttjService;

	/**
	 * 查询页面
	 * 
	 */
	@RequestMapping("/queryAccounttjPage.do")
	public String queryAccounttjPage(HttpServletResponse response, HttpServletRequest request) {
		// Calendar theCa = Calendar.getInstance();
		// theCa.setTime(new Date());
		// theCa.add(theCa.DATE, -8);
		// Date date = theCa.getTime();
		// DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		// String querydate=df.format(date);
		return "redirect:/accounttj/queryAccounttjList.do";
	}

	/**
	 * 查询列表
	 * @param request 
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryAccounttjList.do")
	public String queryAccounttjList(HttpServletRequest request,
			HttpServletResponse response){
		
//		/******************查询条件********************/
//		String begin_info_time = this.getParam(request, "begin_info_time");//开始时间
//		String end_info_time = this.getParam(request, "end_info_time");//结束时间
//		
//		/******************查询Map********************/
//		Map<String,Object>paramMap = new HashMap<String,Object>();
//		paramMap.put("begin_info_time", begin_info_time);//开始时间
//		paramMap.put("end_info_time", end_info_time);//结束时间
//		
//		/******************分页条件开始********************/
//		int totalCount = accounttjService.queryAccounttjCounts(paramMap);
//		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
//		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
//		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
//		
//		/******************查询开始********************/
//		List<Map<String,String>> accounttjList = accounttjService.queryAccounttjList(paramMap);
//		
//		/******************Request绑定开始********************/
//		request.setAttribute("accounttjList", accounttjList);
//		request.setAttribute("begin_info_time", begin_info_time);
//		request.setAttribute("end_info_time", end_info_time);
//		request.setAttribute("isShowList", 1);
		logger.info("账号统计Controller"); 
		AccountStatistics as = new AccountStatistics();
		String begin_info_time = this.getParam(request, "begin_info_time");//开始时间
		String end_info_time = this.getParam(request, "end_info_time");//结束时间
		if(StringUtils.isNotBlank(begin_info_time)){
			Date beginInfoTime = null;
			try {
				beginInfoTime = new SimpleDateFormat("yyyy-MM-dd").parse(begin_info_time);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			as.setBeginInfoTime(beginInfoTime);
		}
		if(StringUtils.isNotBlank(end_info_time)){
			Date endInfoTime = null;
			try {
				endInfoTime = new SimpleDateFormat("yyyy-MM-dd").parse(end_info_time);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			as.setEndInfoTime(endInfoTime);
		}
		String queryType = this.getParam(request, "queryType");
		queryType = StringUtils.isBlank(queryType)?"1":queryType;
		logger.info("queryType:"+queryType); 
		logger.info("begin_info_time:"+begin_info_time); 
		logger.info("end_info_time:"+end_info_time); 
		as.setQueryType(queryType);
		
		Integer total = accounttjService.queryAccountStatisticsTotal(as);
		logger.info("total:"+total);
		PageVo page = PageUtil.getInstance().paging(request, 20, total);
		request.setAttribute("pageBean", page);	
		logger.info("查询统计列表");
		List<AccountStatistics> list = accounttjService.queryAccountStatistics(as);
		logger.info("查询账号信息"); 
		HashMap<String, String> accountMap = accounttjService.queryAccountTotals();
		request.setAttribute("account", accountMap);
		request.setAttribute("accounttjList", list);
		request.setAttribute("begin_info_time", begin_info_time);
		request.setAttribute("end_info_time", end_info_time);
		request.setAttribute("queryType", queryType);
		request.setAttribute("isShowList", 1);
		logger.info("账号统计结束"); 
		return  "accounttj/accounttjList";
	}

	/**
	 * 查询列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryAccounttjInfo.do")
	public String queryAccounttjInfo(HttpServletRequest request, HttpServletResponse response) {

		/****************** 查询条件 ********************/
		String begin_info_time = this.getParam(request, "begin_info_time");// 开始时间
		String end_info_time = this.getParam(request, "end_info_time");// 结束时间
		/****************** 查询Map ********************/
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("begin_info_time", begin_info_time);// 开始时间
		paramMap.put("end_info_time", end_info_time);// 结束时间

		/****************** 分页条件开始 ********************/
		// int totalCount = accounttjService.queryAccounttjCounts(paramMap);
		// PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		paramMap.put("everyPagefrom", 0);// 每页开始的序号
		paramMap.put("pageSize", 5);// 每页显示的条数

		/****************** 查询开始 ********************/
		List<Map<String, String>> accounttjList = accounttjService.queryAccounttjList(paramMap);

		/****************** Request绑定开始 ********************/
		request.setAttribute("accounttjList", accounttjList);
		request.setAttribute("begin_info_time", begin_info_time);
		request.setAttribute("end_info_time", end_info_time);
		return "accounttj/accounttjInfo";
	}

	// 导出excel
	@RequestMapping("/exportAccounttjexcel.do")
	public String exportAccounttjexcel(HttpServletRequest request, HttpServletResponse response) {
		/****************** 查询条件 ********************/
		String begin_info_time = this.getParam(request, "begin_info_time");// 开始时间
		String end_info_time = this.getParam(request, "end_info_time");// 结束时间
		/****************** 查询Map ********************/
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("begin_info_time", begin_info_time);// 开始时间
		paramMap.put("end_info_time", end_info_time);// 结束时间

		List<Map<String, String>> reslist = accounttjService.queryAccounttjExcel(paramMap);
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		for (Map<String, String> m : reslist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(String.valueOf(m.get("create_time")));
			linkedList.add(String.valueOf(m.get("add_regist")));
			linkedList.add(String.valueOf(m.get("regist_pass")));
			linkedList.add(String.valueOf(m.get("regist_wait")));
			linkedList.add(String.valueOf(m.get("regist_success")));
			linkedList.add(String.valueOf(m.get("regist_fail")));
			linkedList.add(String.valueOf(m.get("regist_other")));
			linkedList.add(String.valueOf(m.get("account")));
			linkedList.add(String.valueOf(m.get("account_wait")));
			linkedList.add(String.valueOf(m.get("account_stop")));
			linkedList.add(String.valueOf(m.get("regist_all_wait")));
			linkedList.add(String.valueOf(m.get("regist_all_hand")));
			linkedList.add(String.valueOf(m.get("account_land")));
			linkedList.add(String.valueOf(m.get("account_add")));
			linkedList.add(String.valueOf(m.get("qunar_num")));
			linkedList.add(String.valueOf(m.get("qunar_stop")));
			linkedList.add(String.valueOf(m.get("tongcheng_num")));
			linkedList.add(String.valueOf(m.get("tongcheng_stop")));
			list.add(linkedList);
		}

		String title = "火车票账号统计明细";
		String date = createDate(begin_info_time, end_info_time);
		String filename = "火车票账号统计.xls";
		String[] secondTitles = { "序号", "日期", "19e填写实名", "通过实名", "待核验", "已实名", "信息错误", "其他", "总账号", "目前空闲", "停用账号",
				"待核验", "人工注册", "已登录", "当天新增", "去哪总账号", "去哪停用", "同程总账号", "同程停用" };
		@SuppressWarnings("unused")
		HSSFWorkbook book = ExcelUtil.createExcel(filename, title, date, secondTitles, list, request, response);

		return null;
	}

	private String createDate(String begin_info_time, String end_info_time) {
		String date = "日期：";
		SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd");
		if (begin_info_time.equals(end_info_time) || begin_info_time == end_info_time) {
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
