package com.l9e.transaction.controller;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleEdge;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.JfreeService;
import com.l9e.util.DateUtil;
/**
 * 统计图
 * @author liht
 *
 */
@Controller
@RequestMapping("/jfree")
public class JfreeController extends BaseController {
	private static final Logger logger = Logger.getLogger(JfreeController.class);
	@Resource
	private JfreeService jfreeService;

	/**
	 * 查看该图片是否存在
	 * 
	 * @param url
	 * @return
	 */
	public boolean queryPic(String url) {
		File file = new File(url);
		if (file.exists()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 设置图标样式
	 * 
	 * @param dataset
	 * @param title
	 * @param contentsTitle
	 * @param dataTitle
	 * @return
	 */
	public JFreeChart getPicStyle(DefaultCategoryDataset dataset, String title,
			String contentsTitle, String dataTitle) {
		// 创建主题样式
		StandardChartTheme standardChartTheme = new StandardChartTheme("CN");
		// 设置标题字体
		standardChartTheme.setExtraLargeFont(new Font("隶书", Font.BOLD, 20));
		// 设置图例的字体
		standardChartTheme.setRegularFont(new Font("宋书", Font.PLAIN, 15));
		// 设置轴向的字体
		standardChartTheme.setLargeFont(new Font("宋书", Font.PLAIN, 15));
		// 应用主题样式
		ChartFactory.setChartTheme(standardChartTheme);
		JFreeChart chart = ChartFactory.createLineChart(title, // 图表标题
				contentsTitle, // 目录轴的显示标签
				dataTitle, // 数值轴的显示标签
				dataset, // 数据集
				PlotOrientation.VERTICAL, // 图表方向：水平、垂直
				true, // 是否显示图例(对于简单的柱状图必须是false)
				false, // 是否生成工具
				false // 是否生成URL链接
				);
		// 获得线图的Plot对象
		CategoryPlot plot = chart.getCategoryPlot();
		NumberAxis numberaxis = (NumberAxis) plot.getRangeAxis();
		numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());// 设置Y轴显示数值为整数
		numberaxis.setAutoRange(true);
		Font font = new Font("华文行楷", 10, 20);
		// 设置标题文字，并将其字体设置 此处为图片正上方文字
		chart.getTitle().setFont(font);
		// 设置底部说明字体
		chart.getLegend(0).setItemFont(font);
		return chart;
	}

	/**
	 * 生成图片保存到指定路径
	 * 
	 * @param path
	 * @param chart
	 */
	public void makePic(String path, JFreeChart chart) {
		FileOutputStream fos_jpg = null;
		try {
			fos_jpg = new FileOutputStream(path);// 这个路径自然是可以随意设置的
			ChartUtilities.writeChartAsJPEG(fos_jpg, 1.0f, chart, 960, 590,
					null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fos_jpg.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 15日内的销售统计图19e渠道
	 * 
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/showPictureLine.do")
	public String showPictureLine(HttpServletResponse response,
			HttpServletRequest request) {
		String status = "15day";
		String date = DateUtil.dateToString(new Date(), "yyyy-MM-dd");

		// 19e渠道15日内销售统计图的数据xml
		StringBuilder strXML1 = new StringBuilder("");   
	    strXML1.append("<?xml version='1.0' encoding='UTF-8'?>" +
	     		"<chart palette='2' maxColWidth='70' rotateYAxisName='0' caption='19e渠道15日内销售统计图' baseFontSize='12' "  
		        + "startingAngle='125'  formatNumberScale='0' shownames='1' showvalues='0' PYAxisName='成功数' SYAxisName='活跃数'" +
		        "exportEnabled='1' exportAtClient='1' exportHandler='fcExporter1' exportFormats='PDF=导出为PDF|PNG=导出为PNG图片|JPG=导出JPG图片'" +
		        "exportDialogMessage='加载数据中:'>");   
	     List<Map<String,String>>hashMap = jfreeService.queryPictureLineParam();
			int active =0;    
			strXML1.append("<categories> ");
				for(Map map :hashMap){
					String date1 = map.get("order_time").toString();
					String dateSb = date1.substring(8, date1.length())+"日";
					strXML1.append("<category  name='" + dateSb + "'/>");   
				}
				strXML1.append("</categories> ");
				strXML1.append("<dataset renderAs='Line' parentYAxis='P' seriesName='销售业绩'> ");
				for(Map map :hashMap){
					int num = (Integer) map.get("out_ticket_succeed");
					strXML1.append("<set value='"+ num + "' /> ");
				}
				strXML1.append("</dataset> ");
				strXML1.append("<dataset lineThickness='1' parentYAxis='S' seriesName='活跃用户'> ");
				for(Map map :hashMap){
					active = (Integer) map.get("active");
					strXML1.append("<set value='"+ active + "' /> ");
				}
				strXML1.append("</dataset> ");
		        strXML1.append("</chart>");   
		request.setAttribute("strXML1", strXML1.toString());
		System.out.println("~~~~~~~~~~~~"+strXML1.toString());
		request.setAttribute("date15", date);
		request.setAttribute("status", status);
		return "jfree/show_picture";
	}

	/**
	 * 日期小时销售统计图表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/showDayHourDetail.do")
	public String showDayHourDetail(HttpServletRequest request,
			HttpServletResponse response) {
		String status = "dayHourDetail";
		SimpleDateFormat df1 = new java.text.SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(c.getTime().getTime() ));
		String create_time3 = df1.format(c.getTime()); // 前一天时间

		c = Calendar.getInstance();
		c.setTime(new Date(c.getTime().getTime() - 1000 * 60 * 60 * 24 * 1));
		String create_time2 = df1.format(c.getTime()); // 前两天时间

		c = Calendar.getInstance();
		c.setTime(new Date(c.getTime().getTime() - 1000 * 60 * 60 * 24 * 2));
		String create_time = df1.format(c.getTime()); // 前三天时间
//		String create_time3 = "2013-07-25";
//		String create_time2 = "2013-07-24";
//		String create_time = "2013-07-23";

		// 19e渠道分时销售统计图的数据xml
		StringBuilder strXML2 = new StringBuilder("");
		strXML2.append("<?xml version='1.0' encoding='UTF-8'?>"
						+ "<chart caption='19e渠道分时销售统计图' labelDisplay='Stagger' lineThickness='2' showValues='0' anchorRadius='3' anchorBgAlpha='50' "
						+ "showAlternateVGridColor='1' numVisiblePlot='12' animation='0' baseFontSize='12' yAxisName='成功订单数'>");
		Map mapHour = new HashMap();
		strXML2.append("<categories>");
		for (int i = 0; i < 24; i++) {
			strXML2.append("<category  label='" + i + ":00'/>");
			strXML2.append("<category  label='" + i + ":30'/>");
			if(i<10){
				mapHour.put("0"+i + ":00", "0"+i + ":00");
				mapHour.put("0"+i + ":30", "0"+i + ":30");
			}else{
				mapHour.put(i + ":00", i + ":00");
				mapHour.put(i + ":30", i + ":30");
			}
		} 
		strXML2.append("</categories>");

		List<Map<String, String>> before_map1 = jfreeService.queryDayTimeBefore(create_time); // 每个前半小时
		List<Map> list1 = new ArrayList<Map>();
		int num1 = 0;
		String hour1 = null, day1 = null;
		for (Map map_before1 : before_map1) { // 遍历每个前半小时的数据
			num1 = (Integer) map_before1.get("order_count");
			hour1 = map_before1.get("hour_stat").toString();
			day1 = map_before1.get("day_stat").toString();
			list1.add(map_before1);// 将每个前半小时的数据加入到list里面
		}
		strXML2.append("<dataset seriesName='"
						+ create_time
						+ "' color='DBDC25' anchorBorderColor='DBDC25' anchorBgColor='DBDC25'>");
		Map map_hour1 = new HashMap();
		for (int i = 0; i < list1.size(); i++) {// 循环添加数据集
			num1 = (Integer) list1.get(i).get("order_count");
			hour1 = list1.get(i).get("hour_stat").toString();
			day1 = list1.get(i).get("day_stat").toString();
			map_hour1.put(hour1, num1);
		}
		String[] keySet1 = (String[]) mapHour.keySet().toArray(new String[48]);
		Arrays.sort(keySet1);
		for (Object keyHour : keySet1) {
			if (map_hour1.containsKey(keyHour)) {
				strXML2.append("<set value='" + map_hour1.get(keyHour) + "' /> ");
			} else {
				strXML2.append("<set value='0' /> ");
			}
		}
		strXML2.append("</dataset> ");

		List<Map<String, String>> before_map2 = jfreeService.queryDayTimeBefore(create_time2); // 每个前半小时
		List<Map> list2 = new ArrayList<Map>();
		int num2 = 0;
		String hour2 = null, day2 = null;
		for (Map map_before2 : before_map2) { // 遍历每个前半小时的数据
			num2 = (Integer) map_before2.get("order_count");
			hour2 = map_before2.get("hour_stat").toString();
			day2 = map_before2.get("day_stat").toString();
			list2.add(map_before2);// 将每个前半小时的数据加入到list里面
		}
		strXML2.append("<dataset seriesName='"
						+ create_time2
						+ "' color='B1D1DC' anchorBorderColor='B1D1DC' anchorBgColor='B1D1DC'>");
		Map map_hour2 = new HashMap();
		for (int i = 0; i < list2.size(); i++) {// 循环添加数据集
			num2 = (Integer) list2.get(i).get("order_count");
			hour2 = list2.get(i).get("hour_stat").toString();
			day2 = list2.get(i).get("day_stat").toString();
			map_hour2.put(hour2, num2);
		}
		String[] keySet2 = (String[]) mapHour.keySet().toArray(new String[48]);
		Arrays.sort(keySet2);
		for (Object keyHour : keySet2) {
			if (map_hour2.containsKey(keyHour)) {
				strXML2.append("<set value='" + map_hour2.get(keyHour) + "' /> ");
			} else {
				strXML2.append("<set value='0' /> ");
			}
		}
		strXML2.append("</dataset> ");

		List<Map<String, String>> before_map = jfreeService.queryDayTimeBefore(create_time3); // 每个前半小时
		List<Map> list = new ArrayList<Map>();
		int num = 0;
		String hour = null, day = null;
		for (Map map_before : before_map) { // 遍历每个前半小时的数据
			num = (Integer) map_before.get("order_count");
			hour = map_before.get("hour_stat").toString();
			day = map_before.get("day_stat").toString();
			list.add(map_before);// 将每个前半小时的数据加入到list里面
		}
		strXML2.append("<dataset seriesName='"
						+ create_time3
						+ "' lineThickness='5' color='F1683C' anchorBorderColor='F1683C' anchorBgColor='F1683C'>");
		Map map_hour = new HashMap();
		for (int i = 0; i < list.size(); i++) {// 循环添加数据集
			num = (Integer) list.get(i).get("order_count");
			hour = list.get(i).get("hour_stat").toString();
			day = list.get(i).get("day_stat").toString();
			map_hour.put(hour, num);
		}
		String[] keySet = (String[]) mapHour.keySet().toArray(new String[48]);
		Arrays.sort(keySet);
		for (Object keyHour : keySet) {
			if (map_hour.containsKey(keyHour)) {
				strXML2.append("<set value='" + map_hour.get(keyHour) + "' /> ");
			} else {
				strXML2.append("<set value='0' /> ");
			}
		}
		strXML2.append("</dataset> ");

		strXML2.append("</chart>");
		request.setAttribute("strXML2", strXML2.toString());
		//System.out.println("strXML2===="+strXML2.toString());
		request.setAttribute("status", status);
		return "jfree/show_picture";
	}

	/**
	 * 15日内的销售统计图
	 * 
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/showPictureLineQunar.do")
	public String showPictureLineQunar(HttpServletResponse response,
			HttpServletRequest request) {
		String status = "15dayQunar";
		String date = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
		StringBuilder strXML1 = new StringBuilder(""); // 用于15日活跃用户数图表所需的xml
		strXML1.append("<?xml version='1.0' encoding='UTF-8'?>"
						+ "<chart rotateYAxisName='0' caption='qunar15日内销售趋势' baseFontSize='12' formatNumberScale='0' "
						+ "shownames='1' showvalues='0' yAxisName='成功订单数' rotateYAxisName='1' lineColor='FCB541'>");
			List<Map<String, String>> hashMap = jfreeService.queryQunar15DayPic();
			for (Map map : hashMap) {
				int num = (Integer) map.get("out_ticket_succeed");
				String date1 = map.get("order_time").toString();
				String dateSb = date1.substring(8, date1.length()) + "日";
				strXML1.append("<set name='" + dateSb + "' value='" + num + "' />");
			}
			strXML1.append("</chart>");
			request.setAttribute("strXML1", strXML1.toString());
			//System.out.println(strXML1.toString());
			request.setAttribute("date15", date);
			request.setAttribute("status", status);
			return "jfree/show_picture";
	}

	/**
	 * 当日销售小时统计图
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/showHourPicture.do")
	public String showHourPicture(HttpServletRequest request,
			HttpServletResponse response) {
		String status = "thisDay";
		String create_time = this.getParam(request, "order_time");
		
//		SimpleDateFormat df1 = new java.text.SimpleDateFormat("yyyy-MM-dd");
//		if(create_time.equals("yesterday")){
//			Calendar c = Calendar.getInstance();
//			c.setTime(new Date(c.getTime().getTime() - 1000 * 60 * 60 * 24));
//			String create_time3 = df1.format(c.getTime()); // 前一天时间
//		}
//		
		String channel = this.getParam(request, "channel");
		StringBuilder strXML1 = new StringBuilder(""); // 用于15日活跃用户数图表所需的xml
		strXML1.append("<?xml version='1.0' encoding='UTF-8'?>"
				+ "<chart rotateYAxisName='0' caption='");
		strXML1.append(channel);
		strXML1.append("当日销售趋势' baseFontSize='12' formatNumberScale='0' "
						+ "shownames='1' showvalues='0' yAxisName='成功订单数' rotateYAxisName='1' lineColor='FCB541'>");
		List<Map<String, String>> hashMap = jfreeService.queryThisDayHour(create_time, channel);
		if(hashMap.isEmpty() || hashMap.size()==0){
			strXML1.append("<set name='0时' value='0' />");
		}else{
			for (Map map : hashMap) { // 循环添加数据集
				long num = (Long) map.get("order_count");
				String date = map.get("h_time").toString() + "时";
				strXML1.append("<set name='" + date + "' value='" + num + "' />");
			}
		}
		
		strXML1.append("</chart>");
		request.setAttribute("strXML1", strXML1.toString());
		//System.out.println(strXML1.toString());
		request.setAttribute("create_time", create_time);
		request.setAttribute("channel", channel);
		request.setAttribute("status", status);
		return "jfree/show_picture";
		// }
	}

	/**
	 * 各省当天销售统计
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("showProvinceSellChart.do")
	public String showProvinceSellChart(HttpServletRequest request,
			HttpServletResponse response) {
		String status = "thisDayEachProvince";
		String province_id = this.getParam(request, "province_id");
		String area_no = this.getParam(request, "area_no");
		String create_time = this.getParam(request, "create_time");
		StringBuilder strXML2 = new StringBuilder(""); // 用于15日活跃用户数图表所需的xml
		strXML2.append("<?xml version='1.0' encoding='UTF-8'?>"
						+ "<chart rotateYAxisName='0' caption='本省销售图表' baseFontSize='12' formatNumberScale='0' "
						+ "shownames='1' showvalues='0' yAxisName='成功订单数' rotateYAxisName='1' lineColor='FCB541'>");
		Map<String, Object> query_Map = new HashMap<String, Object>();
		query_Map.put("province_id", province_id);
		query_Map.put("create_time", create_time);
		List<Map<String, String>> hashMap1 = jfreeService.showProvinceSellChart(query_Map);
		strXML2.append("<set name='0时' value='0' />");
		for (Map map : hashMap1) { // 循环添加数据集
			long num = (Long) map.get("order_count");
			String date = map.get("h_time").toString() + "时";
			strXML2.append("<set name='" + date + "' value='" + num + "' />");
		}
		strXML2.append("</chart>");
		request.setAttribute("strXML2", strXML2.toString());
		request.setAttribute("status", status);
		request.setAttribute("create_time", create_time);
		return "jfree/show_picture";
	}

	/**
	 * 本省十五日活跃用户数
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("query15DaysActive.do")
	public String query15DaysActive(HttpServletRequest request,
			HttpServletResponse response) {
//		String status = "query15DaysActiveProvince";
//		String province_id = this.getParam(request, "province_id");
//		String area_no = this.getParam(request, "area_no");
//		String create_time = this.getParam(request, "create_time");
//		StringBuilder strXML1 = new StringBuilder(""); // 用于15日活跃用户数图表所需的xml
//		strXML1.append("<?xml version='1.0' encoding='UTF-8'?>"
//						+ "<chart rotateYAxisName='0' caption='本省15日活跃用户数趋势' baseFontSize='12' formatNumberScale='0' "
//						+ "shownames='1' showvalues='0' yAxisName='活跃用户数' rotateYAxisName='1' lineColor='FCB541'>");
//		List<Map<String, String>> hashMap = jfreeService.query15DaysActive(province_id);
//		for (Map map : hashMap) { // 循环添加数据集
//			long num = Long.parseLong(map.get("activeAgent").toString());
//			String date = map.get("order_time").toString();
//			String date1 = date.substring(3, date.length()) + "日";
//			strXML1.append("<set name='" + date1 + "' value='" + num + "' />");
//		}
//		strXML1.append("</chart>");
//
//		request.setAttribute("status", status);
//		request.setAttribute("create_time", create_time);
//		request.setAttribute("strXML1", strXML1.toString());
//		System.out.println(strXML1.toString());
//		return "jfree/show_picture";
		
		String status = "query15DaysActiveProvince";
		String province_id = this.getParam(request, "province_id");
//		String area_no = this.getParam(request, "area_no");
		String create_time = this.getParam(request, "create_time");
		String date = DateUtil.dateToString(new Date(), "yyyy-MM-dd");

		
		StringBuilder strXML1 = new StringBuilder("");   
		List<Map<String,String>>hashMap = jfreeService.query15DaysActiveInfo(province_id);
//		for(Map map :hashMap){
//		String province_name = map.get("province_name").toString();
	    strXML1.append("<?xml version='1.0' encoding='UTF-8'?>" +
	     		"<chart palette='2' maxColWidth='70' rotateYAxisName='0' caption='本省15日内销售统计图' baseFontSize='12' "  
		        + "startingAngle='125'  formatNumberScale='0' shownames='1' showvalues='0' PYAxisName='成功订单数' SYAxisName='活跃数'" +
		        "exportEnabled='1' exportAtClient='1' exportHandler='fcExporter1' exportFormats='PDF=导出为PDF|PNG=导出为PNG图片|JPG=导出JPG图片'" +
		        "exportDialogMessage='加载数据中:'>");   
//	     		}
			int activeAgent =0;    
			strXML1.append("<categories> ");
				for(Map map :hashMap){
					String date1 = map.get("order_time").toString();
					String dateSb = date1.substring(3, date1.length())+"日";
					strXML1.append("<category  name='" + dateSb + "'/>");   
				}
				strXML1.append("</categories> ");
				strXML1.append("<dataset renderAs='Line' parentYAxis='P' seriesName='销售业绩'> ");
				for(Map map :hashMap){
					int num = (Integer) map.get("succeed_count");
					strXML1.append("<set value='"+ num + "' /> ");
				}
				strXML1.append("</dataset> ");
				strXML1.append("<dataset lineThickness='1' parentYAxis='S' seriesName='活跃用户'> ");
				for(Map map :hashMap){
					activeAgent = (Integer) map.get("activeAgent");
					strXML1.append("<set value='"+ activeAgent + "' /> ");
				}
				strXML1.append("</dataset> ");
		        strXML1.append("</chart>");   
		request.setAttribute("strXML1", strXML1.toString());
		System.out.println("111111111:"+strXML1.toString());
		request.setAttribute("create_time", create_time);
		request.setAttribute("date15", date);
		request.setAttribute("status", status);
		return "jfree/show_picture";
	}
	
	
	/**
	 * 15日内的销售统计图qunar渠道
	 * 
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/showPictureLine15dayQunar.do")
	public String showPictureLine15dayQunar(HttpServletResponse response,
			HttpServletRequest request) {
		
		String status = "15day";
		String date = DateUtil.dateToString(new Date(), "yyyy-MM-dd");

		// 19e渠道15日内销售统计图的数据xml
		StringBuilder strXML1 = new StringBuilder("");   
	    strXML1.append("<?xml version='1.0' encoding='UTF-8'?>" +
	     		"<chart palette='2' maxColWidth='70' rotateYAxisName='0' caption='19e渠道15日内销售统计图' baseFontSize='12' "  
		        + "startingAngle='125'  formatNumberScale='0' shownames='1' showvalues='0' PYAxisName='成功数' SYAxisName='活跃数'" +
		        "exportEnabled='1' exportAtClient='1' exportHandler='fcExporter1' exportFormats='PDF=导出为PDF|PNG=导出为PNG图片|JPG=导出JPG图片'" +
		        "exportDialogMessage='加载数据中:'>");   
	     List<Map<String,String>>hashMap = jfreeService.queryPictureLineParam();
			int active =0;    
			strXML1.append("<categories> ");
				for(Map map :hashMap){
					String date1 = map.get("order_time").toString();
					String dateSb = date1.substring(8, date1.length())+"日";
					strXML1.append("<category  name='" + dateSb + "'/>");   
				}
				strXML1.append("</categories> ");
				strXML1.append("<dataset renderAs='Line' parentYAxis='P' seriesName='销售业绩'> ");
				for(Map map :hashMap){
					int num = (Integer) map.get("out_ticket_succeed");
					strXML1.append("<set value='"+ num + "' /> ");
				}
				strXML1.append("</dataset> ");
				strXML1.append("<dataset lineThickness='1' parentYAxis='S' seriesName='活跃用户'> ");
				for(Map map :hashMap){
					active = (Integer) map.get("active");
					strXML1.append("<set value='"+ active + "' /> ");
				}
				strXML1.append("</dataset> ");
		        strXML1.append("</chart>");   
		request.setAttribute("strXML1", strXML1.toString());

		request.setAttribute("date15", date);
		request.setAttribute("status", status);
		
		
		
		
		
//		StringBuilder strXML1 = new StringBuilder(""); // 用于15日活跃用户数图表所需的xml
//		strXML1.append("<?xml version='1.0' encoding='UTF-8'?>"
//						+ "<chart rotateYAxisName='0' caption='qunar15日内销售趋势' baseFontSize='12' formatNumberScale='0' "
//						+ "shownames='1' showvalues='0' yAxisName='成功订单数' rotateYAxisName='1' lineColor='FCB541'>");
//			List<Map<String, String>> hashMap = jfreeService.queryQunar15DayPic();
//			for (Map map : hashMap) {
//				int num = (Integer) map.get("out_ticket_succeed");
//				String date1 = map.get("order_time").toString();
//				String dateSb = date1.substring(8, date1.length()) + "日";
//				strXML1.append("<set name='" + dateSb + "' value='" + num + "' />");
//			}
//			strXML1.append("</chart>");
//			request.setAttribute("strXML1", strXML1.toString());
//			//System.out.println(strXML1.toString());
//			request.setAttribute("date15", date);
//			request.setAttribute("status", status);
			return "jfree/show_picture";
	}

}
