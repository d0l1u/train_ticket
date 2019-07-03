package com.l9e.transaction.controller;

import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.ui.TextAnchor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.LoginService;
import com.l9e.transaction.service.UserStatService;
import com.l9e.transaction.vo.PageVo;
import com.l9e.util.DateUtil;
import com.l9e.util.PageUtil;
import com.l9e.util.SwitchUtils;
/**
 * 用户统计
 * @author liht
 *
 */
@Controller
@RequestMapping("/userStat")
public class UserStatController extends BaseController {
	private static final Logger logger = Logger.getLogger(UserStatController.class);
	@Resource
	private UserStatService userStatService;

	/**
	 * 进入查询页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryUserStatListPage.do")
	public String queryUserStatListPage(HttpServletRequest request,
			HttpServletResponse response) {
		request.setAttribute("province", userStatService.getProvince());

		return "userStat/userStatList";
	}

	/**
	 * 查询列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryUserStatList.do")
	public String queryUserStatList(HttpServletRequest request,
			HttpServletResponse response) {
		// 获得系统当前时间
		// String now = DateUtil.nowDate();
		String begin_time = this.getParam(request, "begin_time");
		String end_time = this.getParam(request, "end_time");
		String area_no = this.getParam(request, "province_id");
		Map<String, Object> query_Map = new HashMap<String, Object>();
		query_Map.put("begin_time", begin_time);
		query_Map.put("end_time", end_time);
		query_Map.put("area_no", area_no);
		int totalCount = userStatService.queryOrderStatListCount(query_Map);// 总条数
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		query_Map.put("everyPagefrom", page.getFirstResultIndex());// 每页开始的序号
		query_Map.put("pageSize", page.getEveryPageRecordCount());// 每页显示的条数

		List<Map<String, String>> queryUserStat_List = userStatService.queryUserStatList(query_Map);
		List<Map<String, String>> userStat_List = new ArrayList<Map<String, String>>();
		for (int i = 0; i < queryUserStat_List.size(); i++) {
			Map<String, String> add_Map = queryUserStat_List.get(i);
			BigDecimal b = new BigDecimal(Double.parseDouble(add_Map.get("user_rate_increase")) * 100);
			BigDecimal b1 = new BigDecimal(Double.parseDouble(add_Map.get("active_rate_increase")) * 100);
			String user_rate_increase = (b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()) + "%";
			String active_rate_increase = (b1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()) + "%";
			add_Map.put("user_rate_increase", user_rate_increase);
			add_Map.put("active_rate_increase", active_rate_increase);
			userStat_List.add(add_Map);
		}
		request.setAttribute("userStat_List", userStat_List);
		request.setAttribute("begin_time", begin_time);
		request.setAttribute("end_time", end_time);
		// 如果begin_time为空，则显示当前系统时间
		// if(begin_time.equals("")){
		// request.setAttribute("begin_time", now);
		// }else{
		// request.setAttribute("begin_time", begin_time);
		// }
		// //如果end_time为空，则显示当前系统时间
		// if(end_time.equals("")){
		// request.setAttribute("end_time", now);
		// }else{
		// request.setAttribute("end_time", end_time);
		// }

		request.setAttribute("province", userStatService.getProvince());
		request.setAttribute("provinceStr", area_no);
		request.setAttribute("isShowList", 1);

		return "userStat/userStatList";
	}

	/**
	 * 用户增长趋势图
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("showUserStatPictureLine.do")
	public String showUserStatPictureLine(HttpServletRequest request,
			HttpServletResponse response) {
		String status = "15dayUserStat";
		String date = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
		StringBuilder strXML1 = new StringBuilder(""); // 用于图表所需的xml
		strXML1.append("<?xml version='1.0' encoding='UTF-8'?>"
						+ "<chart rotateYAxisName='0' caption='15日内的用户增长趋势' baseFontSize='12' formatNumberScale='0' "
						+ "shownames='1' showvalues='0' yAxisName='增长趋势' rotateYAxisName='1' lineColor='FCB541'>");
		List<Map<String, String>> hashMap = userStatService
				.queryUserStatPictureLine();
		for (Map map : hashMap) {
			int user_increase = (Integer) map.get("user_increase");
			String date1 = map.get("tj_time").toString();
			String dateSb = date1.substring(8, date1.length()) + "日";
			strXML1.append("<set name='" + dateSb + "' value='" + user_increase + "' />");
		}
		strXML1.append("</chart>");

		request.setAttribute("strXML1", strXML1.toString());
		request.setAttribute("date15", date);
		request.setAttribute("status", status);
		return "userStat/userStatPicture";
	}

	/**
	 * 查询本省本天查询并统计趋势图
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("showUserStatPictureInfo.do")
	public String showUserStatPictureInfo(HttpServletRequest request,
			HttpServletResponse response) {
		String area_no = this.getParam(request, "area_no");
		String tj_time = this.getParam(request, "tj_time");
		String status = "15dayUserStatInfo";
		StringBuilder strXML1 = new StringBuilder(""); // 用于图表所需的xml
		strXML1.append("<?xml version='1.0' encoding='UTF-8'?>"
						+ "<chart rotateYAxisName='0' caption='15日内用户增长趋势' baseFontSize='12' formatNumberScale='0' "
						+ "shownames='1' showvalues='0' yAxisName='增长趋势' rotateYAxisName='1' lineColor='FCB541'>");
		Map<String, String> query_Map = new HashMap<String, String>();
		query_Map.put("area_no", area_no);
		query_Map.put("tj_time", tj_time);
		List<Map<String, String>> pic_List = userStatService
				.queryThisDayUserStat(query_Map);
		for (Map map : pic_List) {
			int user_increase = (Integer) map.get("user_increase");
			String date1 = map.get("tj_time").toString();
			String dateSb = date1.substring(8, date1.length()) + "日";
			strXML1.append("<set name='" + dateSb + "' value='" + user_increase + "' />");
		}
		strXML1.append("</chart>");

		request.setAttribute("strXML1", strXML1.toString());
		request.setAttribute("date15", tj_time + area_no);
		request.setAttribute("status", status);
		return "userStat/userStatPicture";
	}

	/**
	 * 各省活跃用户15天趋势
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("showUserStatPictureActive.do")
	public String showUserStatPictureActive(HttpServletRequest request,
			HttpServletResponse response) {
		String area_no = this.getParam(request, "area_no");
		String tj_time = this.getParam(request, "tj_time");
		String status = "15dayUserStatActive";
		StringBuilder strXML1 = new StringBuilder(""); // 用于图表所需的xml
		strXML1.append("<?xml version='1.0' encoding='UTF-8'?>"
						+ "<chart rotateYAxisName='0' caption='15日内用户增长趋势' baseFontSize='12' formatNumberScale='0' "
						+ "shownames='1' showvalues='0' yAxisName='增长趋势' rotateYAxisName='1' lineColor='FCB541'>");

		Map<String, String> query_Map = new HashMap<String, String>();
		query_Map.put("area_no", area_no);
		query_Map.put("tj_time", tj_time);
		List<Map<String, String>> pic_List = userStatService
				.queryThisDayUserStatActive(query_Map);
		for (Map map : pic_List) {
			int active_user = (Integer) map.get("active_user");
			String date1 = map.get("tj_time").toString();
			String dateSb = date1.substring(8, date1.length()) + "日";
			strXML1.append("<set name='" + dateSb + "' value='" + active_user + "' />");
		}
		strXML1.append("</chart>");

		request.setAttribute("strXML1", strXML1.toString());
		request.setAttribute("date15", tj_time + area_no);
		request.setAttribute("status", status);
		return "userStat/userStatPicture";
	}

	/**
	 * 各省用户总量对比柱状图
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("showAllPrivateUserStatBar.do")
	public String showAllPrivateUserStatBar(HttpServletRequest request,
			HttpServletResponse response) {
		String status = "userStatBar";
		String date = SwitchUtils.getPreDate();
		StringBuilder strXML1 = new StringBuilder(""); // 用于图表所需的xml
		strXML1.append("<?xml version='1.0' encoding='UTF-8'?>"
						+ "<chart palette='2' caption='各省用户量对比图' shownames='1' showvalues='0' useRoundEdges='1' legendBorderAlpha='0' "
						+ "rotateYAxisName='0'  baseFontSize='12' formatNumberScale='0' shownames='1' showvalues='1' >");
		List<Map<String, String>> hashMap = userStatService
				.queryAllPrivateUserBar(date);
		for (Map map : hashMap) {
			String thisDay = map.get("tj_time").toString();
			String area_no = map.get("area_no").toString();
			Map<String, String> queryMap = new HashMap<String, String>();
			queryMap.put("thisDay", thisDay);
			queryMap.put("area_no", area_no);
			int thisdayCount = userStatService.queryThisProvinceCount(queryMap);
			int predayCount = userStatService.queryPreProvinceCount(queryMap);
			int aWeekAgoCount = userStatService.queryAWeekAgoCount(queryMap);
			map.put("thisdayCount", thisdayCount);
			map.put("predayCount", predayCount);
			map.put("aWeekAgoCount", aWeekAgoCount);
		}
		strXML1.append("<categories>");
		for (Map map : hashMap) {
			String area_no = map.get("area_no").toString();
			String area_name = null;
			if ("黑龙江省".equals(map.get("area_name").toString())) {
				area_name = (map.get("area_name").toString()).substring(0, 3);
			} else {
				area_name = (map.get("area_name").toString()).substring(0, 2);
			}
			strXML1.append("<category label='" + area_name + "' /> ");
		}
		strXML1.append("</categories>");

		strXML1.append("<dataset seriesName='用户数' color='AFD8F8' showValues='0'>");
		for (Map map : hashMap) {
			int user_total = (Integer) map.get("user_total");
			strXML1.append("<set value='" + user_total + "' /> ");
		}
		strXML1.append("</dataset>");

		strXML1
				.append("<dataset seriesName='昨天销量' color='F6BD0F' showValues='0'>");
		for (Map map : hashMap) {
			int thisdayCount = (Integer) map.get("thisdayCount");
			strXML1.append("<set value='" + thisdayCount + "' /> ");
		}
		strXML1.append("</dataset>");

		strXML1.append("<dataset seriesName='前天销量' color='8BBA00' showValues='0'>");
		for (Map map : hashMap) {
			int predayCount = (Integer) map.get("predayCount");
			strXML1.append("<set value='" + predayCount + "' /> ");
		}
		strXML1.append("</dataset>");

		strXML1.append("<dataset seriesName='一周前的今天' showValues='0'>");
		for (Map map : hashMap) {
			int aWeekAgoCount = (Integer) map.get("aWeekAgoCount");
			strXML1.append("<set value='" + aWeekAgoCount + "' /> ");
		}
		strXML1.append("</dataset>");
		strXML1.append("</chart>");

		request.setAttribute("strXML1", strXML1.toString());
		// System.out.println(strXML1.toString());
		request.setAttribute("date15", date);
		request.setAttribute("status", status);
		return "userStat/userStatPicture";
	}

}
