package com.l9e.transaction.controller;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleEdge;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.OrderStatService;
import com.l9e.transaction.vo.BookVo;
import com.l9e.transaction.vo.JoinUsVo;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.util.DateUtil;
import com.l9e.util.PageUtil;
import com.l9e.util.SwitchUtils;
/**
 * 订单统计
 * @author liht
 *
 */
@Controller
@RequestMapping("/orderStat")
public class OrderStatController  extends BaseController{
	private static final Logger logger = Logger.getLogger(OrderStatController.class);
	@Resource
	private OrderStatService orderStatService ;
	
	 /**
	  * 进入查询页面
	  * @param request
	  * @param response
	  * @return
	  */
	@RequestMapping("/queryOrderStatPage.do")
	public String queryOrderStatPage(HttpServletRequest request,HttpServletResponse response){
		/*****************************省级代理省份方法*****************************/
		LoginUserVo loginUserVo = (LoginUserVo)request.getSession().getAttribute("loginUserVo");
		if(loginUserVo.getUser_level().equals("1.1")&& loginUserVo.getSupervise_name()!=null){
			String str = loginUserVo.getSupervise_name();
			List<String>Supervise_name_List =  SwitchUtils.strToList(str);
			List<Map<String,String>>area_no_list  = new ArrayList<Map<String,String>>();
			Map<String,String>area_Map = new HashMap<String,String>();
			for(int i=0;i<Supervise_name_List.size();i++){
				area_Map=orderStatService.querySupervise_nameToArea_no(Supervise_name_List.get(i).toString());
				area_no_list.add(area_Map);
			}
			request.setAttribute("province",area_no_list); 
			return "orderStat/superviseAreaList";
		}else{
			return "orderStat/orderStatList";
		}
	}
	/**
	 * 查询列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryOrderStatList.do")
	public String queryOrderStatList(HttpServletRequest request,HttpServletResponse response){
		
	       
		String begin_time = this.getParam(request, "begin_time");
		String end_time = this.getParam(request, "end_time");
		Map<String,Object>paramMap = new HashMap<String,Object>();
		paramMap.put("begin_time", begin_time);
		paramMap.put("end_time", end_time);
		
		int totalCount = orderStatService.queryOrderStatListCount(paramMap);//总条数	
		PageVo page = PageUtil.getInstance().paging(request, 15, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		List<Map<String,Object>>orderStatList =orderStatService.queryOrderStatList(paramMap);
		for(int i=0;i<orderStatList.size();i++){
			Map<String,Object> map = orderStatList.get(i);
			 int active=orderStatService.queryActiveUser(map.get("order_time").toString());
			map.put("active", active);
		}
		request.setAttribute("orderStatList", orderStatList);
		request.setAttribute("begin_time", begin_time);
		request.setAttribute("end_time", end_time);
		request.setAttribute("isShowList", 1);
		return "orderStat/orderStatList";
	}
	/**
	 * 省级代理显示页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("querySuperviseAreaList.do")
	public String querySuperviseAreaList(HttpServletRequest request,HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo)request.getSession().getAttribute("loginUserVo");
		String str = loginUserVo.getSupervise_name();
		List<String>supervise_name_List =  SwitchUtils.strToList(str);
		
		String province_id = this.getParam(request, "province_id");
		String begin_time = this.getParam(request, "begin_time");
		String end_time = this.getParam(request, "end_time");
		Map<String,Object>paramMap = new HashMap<String,Object>();
		paramMap.put("begin_time", begin_time);
		paramMap.put("end_time", end_time);
		paramMap.put("province_id", province_id);
		paramMap.put("supervise_name_List", supervise_name_List);
		int totalCount = orderStatService.querySuperviseAreaCount(paramMap);//总条数	
		PageVo page = PageUtil.getInstance().paging(request, 15, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		List<Map<String,Object>>superviseAreaList =orderStatService.querySuperviseAreaList(paramMap);
		
		for(int j=0;j<superviseAreaList.size();j++){
			Map<String,Object> map = superviseAreaList.get(j);
			java.text.DecimalFormat df=new   java.text.DecimalFormat("#.##");   
			int cg = (Integer)map.get("succeed_count");
			int sb = (Integer)map.get("defeated_count"); 
			int zh = (Integer)map.get("pay_fall"); 
			String cgl =df.format((double)cg/(double)(cg+sb)*100)+"%".toString();
			String sbl =df.format((double)sb/(double)(cg+sb)*100)+"%".toString();
			String zhl =df.format((double)cg/(double)(cg+sb+zh)*100)+"%".toString();
			map.put("cgl", cgl);
			map.put("sbl", sbl);
			map.put("zhl", zhl);
		}
			List<Map<String,String>>area_no_list  = new ArrayList<Map<String,String>>();
			Map<String,String>area_Map = new HashMap<String,String>();
			for(int i=0;i<supervise_name_List.size();i++){
				area_Map=orderStatService.querySupervise_nameToArea_no(supervise_name_List.get(i).toString());
				area_no_list.add(area_Map);
			}
		request.setAttribute("province",area_no_list); 
		request.setAttribute("superviseAreaList", superviseAreaList);
		request.setAttribute("begin_time", begin_time);
		request.setAttribute("end_time", end_time);
		request.setAttribute("province_id", province_id);
		request.setAttribute("isShowList", 1);
		
		return "orderStat/superviseAreaList";
	}
	
	/**
	 * 15日内的销售统计图
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/showPictureLine.do")
	public String showPictureLine(HttpServletResponse response,HttpServletRequest request){
		String status = "15day";
		String prePath = request.getSession().getServletContext().getRealPath("/jchart"); 
		String date = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
		String url = prePath+"/jfreePicture"+date+".jpg";
		File file = new File(url);
		if(file.exists()){
			request.setAttribute("date15", date);
			request.setAttribute("status", status);
			return "orderStat/jfreePicture";
		}else{
		
		
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		List<Map<String,String>>hashMap = orderStatService.queryPictureLineParam();
		int active =0; 
			for(Map map :hashMap){
				int num = (Integer) map.get("out_ticket_succeed");
				String date1 = map.get("order_time").toString();
				active = orderStatService.queryActiveUser(date1);
				String dateSb = date1.substring(8, date1.length())+"日";
				dataset.addValue(num,"销售业绩",dateSb);
				dataset.addValue(active, "活跃用户数", dateSb);
			}
		
		//创建主题样式  
		   StandardChartTheme standardChartTheme=new StandardChartTheme("CN");  
		   //设置标题字体  
		   standardChartTheme.setExtraLargeFont(new Font("隶书",Font.BOLD,20));  
		   //设置图例的字体  
		   standardChartTheme.setRegularFont(new Font("宋书",Font.PLAIN,15));  
		   //设置轴向的字体  
		   standardChartTheme.setLargeFont(new Font("宋书",Font.PLAIN,15));  
		   //应用主题样式  
		   ChartFactory.setChartTheme(standardChartTheme); 
		
		JFreeChart chart = ChartFactory.createLineChart("15日内的销售统计图", // 图表标题
				"日期", // 目录轴的显示标签
				"成功订单数", // 数值轴的显示标签
				dataset, // 数据集
				PlotOrientation.VERTICAL, // 图表方向：水平、垂直
				true, // 是否显示图例(对于简单的柱状图必须是false)
				false, // 是否生成工具
				false // 是否生成URL链接
				);

		//获得线图的Plot对象
		CategoryPlot plot =  chart.getCategoryPlot();
		NumberAxis numberaxis = (NumberAxis)plot.getRangeAxis();
		numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());//设置Y轴显示数值为整数
		numberaxis.setAutoRange(true);
		Font font = new Font("华文行楷", 10, 20);
		// 设置标题文字，并将其字体设置 此处为图片正上方文字
		chart.getTitle().setFont(font);
		// 设置底部说明字体
		chart.getLegend(0).setItemFont(font);

		FileOutputStream fos_jpg = null;

		try {
			fos_jpg = new FileOutputStream(
					prePath+"/jfreePicture"+date+".jpg");// 这个路径自然是可以随意设置的
			ChartUtilities.writeChartAsJPEG(fos_jpg, 1.0f, chart, 960, 590,null);
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

		request.setAttribute("date15", date);
		request.setAttribute("status", status);
		return "orderStat/jfreePicture";
		}
	}
	/**
	 * 全部销售小时统计图
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/showAllHourPicture.do")
	public String showAllHourPicture(HttpServletRequest request,HttpServletResponse response){
		String status = "allDay";
		String date = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
		String prePath = request.getSession().getServletContext().getRealPath("/jchart"); //保存文件的文件夹
		String url = prePath+"/jfreeAllTime"+date+".jpg";
		File file = new File(url);
		if(file.exists()){
			request.setAttribute("date", date);
			request.setAttribute("status", status);
			return "orderStat/jfreePicture";
		}else{
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();//创建数据集容器
		List<Map<String,String>>hashMap = orderStatService.queryAllHour();
		for(Map map :hashMap){  //循环添加数据集
			long num = (Long) map.get("order_count");
			String dateHour = map.get("h_time").toString();
			dataset.addValue(num,"销售业绩",dateHour);
			
		}
		//创建主题样式  
		   StandardChartTheme standardChartTheme=new StandardChartTheme("CN");  
		   //设置标题字体  
		   standardChartTheme.setExtraLargeFont(new Font("隶书",Font.BOLD,20));  
		   //设置图例的字体  
		   standardChartTheme.setRegularFont(new Font("宋书",Font.PLAIN,15));  
		   //设置轴向的字体  
		   standardChartTheme.setLargeFont(new Font("宋书",Font.PLAIN,15));  
		   //应用主题样式  
		   ChartFactory.setChartTheme(standardChartTheme); 
		
		JFreeChart chart = ChartFactory.createLineChart("全部销售小时统计图", // 图表标题
				"小时", // 目录轴的显示标签
				"成功订单数", // 数值轴的显示标签
				dataset, // 数据集
				PlotOrientation.VERTICAL, // 图表方向：水平、垂直
				true, // 是否显示图例(对于简单的柱状图必须是false)
				false, // 是否生成工具
				false // 是否生成URL链接
				);

		Font font = new Font("华文行楷", 10, 20);
		// 设置标题文字，并将其字体设置 此处为图片正上方文字
		
		//获得线图的Plot对象
		CategoryPlot plot =  chart.getCategoryPlot();
		NumberAxis numberaxis = (NumberAxis)plot.getRangeAxis();
		numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());//设置Y轴显示数值为整数
		numberaxis.setAutoRange(true);
		chart.getTitle().setFont(font);
		// 设置底部说明字体
		chart.getLegend(0).setItemFont(font);

		FileOutputStream fos_jpg = null;
		try {
			fos_jpg = new FileOutputStream(
					prePath+"/jfreeAllTime"+date+".jpg");// 这个路径自然是可以随意设置的
			ChartUtilities.writeChartAsJPEG(fos_jpg, 1.0f, chart, 960, 590,null);
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
		request.setAttribute("date", date);
		request.setAttribute("status", status);
		return "orderStat/jfreePicture";
		
		}
	}
	/**
	 * 当日销售小时统计图
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/showHourPicture.do")
	public String showHourPicture(HttpServletRequest request,HttpServletResponse response){
		String status = "thisDay";
		String prePath = request.getSession().getServletContext().getRealPath("/jchart/hour");  //保存文件的文件夹
		String create_time = this.getParam(request, "order_time");
		String url = prePath+"/jfreeThisDay"+create_time+".jpg";//文件的位置
		File file = new File(url);
		if(file.exists()){
			request.setAttribute("status", status);
			request.setAttribute("create_time", create_time);
			return "orderStat/jfreePicture";
		}else{
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();//创建数据集容器
		List<Map<String,String>>hashMap = orderStatService.queryThisDayHour(create_time);
		for(Map map :hashMap){  //循环添加数据集
			long num = (Long) map.get("order_count");
			String date = map.get("h_time").toString();
			dataset.addValue(num,"销售业绩",date);
			
		}
		//创建主题样式  
		   StandardChartTheme standardChartTheme=new StandardChartTheme("CN");  
		   //设置标题字体  
		   standardChartTheme.setExtraLargeFont(new Font("隶书",Font.BOLD,20));  
		   //设置图例的字体  
		   standardChartTheme.setRegularFont(new Font("宋书",Font.PLAIN,15));  
		   //设置轴向的字体  
		   standardChartTheme.setLargeFont(new Font("宋书",Font.PLAIN,15));  
		   //应用主题样式  
		   ChartFactory.setChartTheme(standardChartTheme); 
		
		JFreeChart chart = ChartFactory.createLineChart("当日销售小时统计图", // 图表标题
				"小时", // 目录轴的显示标签
				"成功订单数", // 数值轴的显示标签
				dataset, // 数据集
				PlotOrientation.VERTICAL, // 图表方向：水平、垂直
				true, // 是否显示图例(对于简单的柱状图必须是false)
				false, // 是否生成工具
				false // 是否生成URL链接
				);

		Font font = new Font("华文行楷", 10, 20);
		// 设置标题文字，并将其字体设置 此处为图片正上方文字
		chart.getTitle().setFont(font);
		// 设置底部说明字体
		chart.getLegend(0).setItemFont(font);
		//获得线图的Plot对象
		CategoryPlot plot =  chart.getCategoryPlot();
		NumberAxis numberaxis = (NumberAxis)plot.getRangeAxis();
		numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());//设置Y轴显示数值为整数
		numberaxis.setAutoRange(true);
		FileOutputStream fos_jpg = null;
	
		try {
			fos_jpg = new FileOutputStream(
					prePath+"/jfreeThisDay"+create_time+".jpg");// 这个路径自然是可以随意设置的
			ChartUtilities.writeChartAsJPEG(fos_jpg, 1.0f, chart, 960, 590,null);
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

		request.setAttribute("status", status);
		request.setAttribute("create_time", create_time);
		request.setAttribute("prePath", prePath);
		return "orderStat/jfreePicture";
		}
	}
	
	/**
	 * 查询各省销售总计
	 * @param response
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("queryThisDayInfo.do")
	public String queryProvinceCount(HttpServletResponse response ,HttpServletRequest request){
		String create_time = this.getParam(request, "create_time");
		List<Map<String,Object>>statInfo_provinceList = orderStatService.queryHc_statInfo_provinceList(create_time);
		for(int j=0;j<statInfo_provinceList.size();j++){
			Map<String,Object> map = statInfo_provinceList.get(j);
			java.text.DecimalFormat df=new   java.text.DecimalFormat("#.##");   
			int cg = (Integer)map.get("succeed_count");
			int sb = (Integer)map.get("defeated_count"); 
			int zh = (Integer)map.get("pay_fall"); 
			String cgl =df.format((double)cg/(double)(cg+sb)*100)+"%".toString();
			String sbl =df.format((double)sb/(double)(cg+sb)*100)+"%".toString();
			String zhl =df.format((double)cg/(double)(cg+sb+zh)*100)+"%".toString();
			map.put("cgl", cgl);
			map.put("sbl", sbl);
			map.put("zhl", zhl);
		}
		request.setAttribute("statInfo_provinceList", statInfo_provinceList);
		return "orderStat/provinceCountInfo";
	}
			
	/**
	 * 日期小时销售统计图表 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/showDayHourDetail.do")
	public String showDayHourDetail(HttpServletRequest request,HttpServletResponse response){
		String status = "dayHourDetail";
		String prePath = request.getSession().getServletContext().getRealPath("/jchart"); //保存文件的文件夹
		String dateNow = DateUtil.dateToString(new Date(), "yyyy-MM-dd"); 
		//String dateNow = "2013-08-15";
		String url = prePath+"/jfreeDayHourDetail"+dateNow+".jpg";//文件位置
		File file = new File(url); //查找文件是否存在
		if(file.exists()){ //如果存在则直接跳转 
			request.setAttribute("status", status);
			request.setAttribute("dateNow", dateNow);
			return "orderStat/jfreePicture";
		}else{ //不存在则创建
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();//创建数据集容器
		//因为SQL语句用到between and 所以传过去一个今天的时间 
		List<Map<String,String>>hashMap = orderStatService.queryDateTimeDetail(dateNow);
		for(Map map :hashMap){  //循环添加数据集
			long num = (Long) map.get("order_count");
			String hour = map.get("hour_stat").toString();
			String time = map.get("day_stat").toString();
			dataset.addValue(num,time,hour);
			
		}
		//创建主题样式  
		   StandardChartTheme standardChartTheme=new StandardChartTheme("CN");  
		   //设置标题字体  
		   standardChartTheme.setExtraLargeFont(new Font("隶书",Font.BOLD,20));  
		   //设置图例的字体  
		   standardChartTheme.setRegularFont(new Font("宋书",Font.PLAIN,15));  
		   //设置轴向的字体  
		   standardChartTheme.setLargeFont(new Font("宋书",Font.PLAIN,15));  
		   //应用主题样式  
		   ChartFactory.setChartTheme(standardChartTheme); 
	
		   JFreeChart chart = ChartFactory.createLineChart("各天每小时销售统计图", // 图表标题
				"小时", // 目录轴的显示标签
				"成功订单数", // 数值轴的显示标签
				dataset, // 数据集
				PlotOrientation.VERTICAL, // 图表方向：水平、垂直
				true, // 是否显示图例(对于简单的柱状图必须是false)
				false, // 是否生成工具
				false // 是否生成URL链接
				);
		
		Font font = new Font("华文行楷", 10, 20);
		
		//获得线图的Plot对象
		CategoryPlot plot =  chart.getCategoryPlot();
		NumberAxis numberaxis = (NumberAxis)plot.getRangeAxis();
		numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());//设置Y轴显示数值为整数
		numberaxis.setAutoRange(true);
		
		BasicStroke realLine = new BasicStroke(2.5f);
		float dashes[] = { 8.0f };     // 定义虚线数组
		BasicStroke brokenLine = new BasicStroke(1.0f,     // 线条粗细
			      BasicStroke.CAP_ROUND,     // 端点风格
			      BasicStroke.JOIN_ROUND,     // 折点风格
			      8.f,     // 折点处理办法
			      dashes,     // 虚线数组
			      0.0f);     // 虚线偏移量
		
		LineAndShapeRenderer renderer = (LineAndShapeRenderer)plot.getRenderer();
		renderer.setSeriesPaint(0, new Color(183,6,204));
		renderer.setSeriesStroke(0, brokenLine);
		renderer.setSeriesPaint(1, new Color(0,0,255));
		renderer.setSeriesStroke(1, brokenLine);
		renderer.setSeriesPaint(2, new Color(255,0,0));
		renderer.setSeriesStroke(2, realLine);
		
		
		// 设置标题文字，并将其字体设置 此处为图片正上方文字
		chart.getTitle().setFont(font);
		chart.getLegend(0).setPosition(RectangleEdge.RIGHT);//设置图例说明在右侧
		// 设置底部说明字体
		chart.getLegend(0).setItemFont(font);

		FileOutputStream fos_jpg = null;
		
		try {
			fos_jpg = new FileOutputStream(
					prePath+"/jfreeDayHourDetail"+dateNow+".jpg");// 这个路径自然是可以随意设置的
			ChartUtilities.writeChartAsJPEG(fos_jpg, 1.0f, chart, 960, 590,null);
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
		
		request.setAttribute("status", status);
		request.setAttribute("dateNow", dateNow);
		return "orderStat/jfreePicture";
		}
	}
	
	/**
	 * 各省当天销售统计
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("showProvinceSellChart.do")
	public String showProvinceSellChart(HttpServletRequest request,HttpServletResponse response){
		String status = "thisDayEachProvince";
		String prePath = request.getSession().getServletContext().getRealPath("/jchart/provinceHour"); 
		String province_id = this.getParam(request, "province_id");
		String area_no = this.getParam(request, "area_no");
		String create_time = this.getParam(request, "create_time");
		String area_noCreate_time = area_no+create_time;
		logger.info("area_noCreate_time="+area_noCreate_time);
		String url = prePath+"/jfreeThisDayEachProvince"+area_noCreate_time+".jpg";//文件的位置
		File file = new File(url);
		if(file.exists()){
			request.setAttribute("status", status);
			request.setAttribute("area_noCreate_time", area_noCreate_time);
			request.setAttribute("create_time", create_time);
			return "orderStat/jfreePicture";
		}else{
			logger.info("进入else创建图片。。");

			DefaultCategoryDataset dataset = new DefaultCategoryDataset();//创建数据集容器
			Map<String,Object>query_Map = new HashMap<String,Object>();
			query_Map.put("province_id", province_id);
			query_Map.put("create_time", create_time);
			List<Map<String,String>>hashMap = orderStatService.showProvinceSellChart(query_Map);
			for(Map map :hashMap){  //循环添加数据集
				long num = (Long) map.get("order_count");
				String date = map.get("h_time").toString();
				dataset.addValue(num,"销售业绩",date);
				logger.info("Map:num="+num+",date="+date);
			}
			//创建主题样式  
			   StandardChartTheme standardChartTheme=new StandardChartTheme("CN");  
			   //设置标题字体  
			   standardChartTheme.setExtraLargeFont(new Font("隶书",Font.BOLD,20));  
			   //设置图例的字体  
			   standardChartTheme.setRegularFont(new Font("宋书",Font.PLAIN,15));  
			   //设置轴向的字体  
			   standardChartTheme.setLargeFont(new Font("宋书",Font.PLAIN,15));  
			   //应用主题样式  
			   ChartFactory.setChartTheme(standardChartTheme); 
			
			JFreeChart chart = ChartFactory.createLineChart("当日销售小时统计图", // 图表标题
					"小时", // 目录轴的显示标签
					"成功订单数", // 数值轴的显示标签
					dataset, // 数据集
					PlotOrientation.VERTICAL, // 图表方向：水平、垂直
					true, // 是否显示图例(对于简单的柱状图必须是false)
					false, // 是否生成工具
					false // 是否生成URL链接
					);

			Font font = new Font("华文行楷", 10, 20);
			// 设置标题文字，并将其字体设置 此处为图片正上方文字
			chart.getTitle().setFont(font);
			// 设置底部说明字体
			chart.getLegend(0).setItemFont(font);
			//获得线图的Plot对象
			CategoryPlot plot =  chart.getCategoryPlot();
			NumberAxis numberaxis = (NumberAxis)plot.getRangeAxis();
			numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());//设置Y轴显示数值为整数
			numberaxis.setAutoRange(true);
			FileOutputStream fos_jpg = null;
		
			try {
				fos_jpg = new FileOutputStream(
						prePath+"/jfreeThisDayEachProvince"+area_noCreate_time+".jpg");// 这个路径自然是可以随意设置的
				ChartUtilities.writeChartAsJPEG(fos_jpg, 1.0f, chart, 960, 590,null);
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
		request.setAttribute("status", status);
		request.setAttribute("area_noCreate_time", area_noCreate_time);
		request.setAttribute("create_time", create_time);
		return "orderStat/jfreePicture";
	}
	
	public static void main(String []args){
		java.text.DecimalFormat   df=new   java.text.DecimalFormat("#.##");   
	
	}
	
}
