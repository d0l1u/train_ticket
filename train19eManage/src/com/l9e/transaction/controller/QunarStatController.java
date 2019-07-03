package com.l9e.transaction.controller;

import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.QunarStatService;
import com.l9e.transaction.vo.PageVo;
import com.l9e.util.DateUtil;
import com.l9e.util.PageUtil;

@Controller
@RequestMapping("/qunarstat")
public class QunarStatController extends BaseController {

	@Resource
	QunarStatService qunarStatService;

	@RequestMapping("/queryAllStat.do")
	public String getStatInfo(HttpServletRequest request,
			HttpServletResponse response) {

		return "qunarstat/qunarstat";
	}

	@RequestMapping("/queryStat.do")
	public String queryStatInfo(HttpServletRequest request,
			HttpServletResponse response) {
		String beginTime = this.getParam(request, "begin_time");
		String endTime = this.getParam(request, "end_time");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("begin_time", beginTime);
		map.put("end_time", endTime);
		int totalCount = qunarStatService.getStatInfoCount(map);

		PageVo page = PageUtil.getInstance().paging(request, 15, totalCount);
		map.put("everyPagefrom", page.getFirstResultIndex());
		map.put("pageSize", page.getEveryPageRecordCount());
		List<HashMap<String, String>> statList = qunarStatService
				.getDaysStatInfo(map);
		request.setAttribute("statList", statList);
		request.setAttribute("isShowList", 1);
		request.setAttribute("begin_time",beginTime);
		request.setAttribute("end_time", endTime);
		return "qunarstat/qunarstat";
	}

	/**
	 * 15日内的销售统计图
	 * 
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/showPictureLine.do")
	public String showPictureLine(HttpServletResponse response,
			HttpServletRequest request) {
		String status = "15day";
		String prePath = request.getSession().getServletContext().getRealPath(
				"/jchart");
		String date = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
		String url = prePath + "/jfreePicture" + date + ".jpg";
		File file = new File(url);
		if (file.exists()) {
			request.setAttribute("date15", date);
			request.setAttribute("status", status);
			return "qunarstat/jfreePicture";
		} else {

			DefaultCategoryDataset dataset = new DefaultCategoryDataset();
			List<Map<String, String>> hashMap = qunarStatService
					.queryPictureLineParam();
			for(Map<String, String> map : hashMap) {
				System.out.println(map);
			}
			for (Map map : hashMap) {
				int num = (Integer) map.get("out_ticket_succeed");
				String date1 = map.get("order_time").toString();
				String dateSb = date1.substring(8, date1.length()) + "日";
				dataset.addValue(num, "销售业绩", dateSb);
			}

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

			JFreeChart chart = ChartFactory.createLineChart("15日内的销售统计图", // 图表标题
					"日期", // 目录轴的显示标签
					"成功订单数", // 数值轴的显示标签
					dataset, // 数据集
					PlotOrientation.VERTICAL, // 图表方向：水平、垂直
					true, // 是否显示图例(对于简单的柱状图必须是false)
					false, // 是否生成工具
					false // 是否生成URL链接
					);

			// 获得线图的Plot对象
			CategoryPlot plot = chart.getCategoryPlot();
			NumberAxis numberaxis = (NumberAxis) plot.getRangeAxis();
			numberaxis
					.setStandardTickUnits(NumberAxis.createIntegerTickUnits());// 设置Y轴显示数值为整数
			numberaxis.setAutoRange(true);
			Font font = new Font("华文行楷", 10, 20);
			// 设置标题文字，并将其字体设置 此处为图片正上方文字
			chart.getTitle().setFont(font);
			// 设置底部说明字体
			chart.getLegend(0).setItemFont(font);

			FileOutputStream fos_jpg = null;

			try {
				fos_jpg = new FileOutputStream(prePath + "/jfreePicture" + date
						+ ".jpg");// 这个路径自然是可以随意设置的
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

			request.setAttribute("date15", date);
			request.setAttribute("status", status);
			return "qunarstat/jfreePicture";
		}
	}

	/**
	 * 全部销售小时统计图
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/showAllHourPicture.do")
	public String showAllHourPicture(HttpServletRequest request,
			HttpServletResponse response) {
		String status = "allDay";
		String date = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
		String prePath = request.getSession().getServletContext().getRealPath(
				"/jchart"); // 保存文件的文件夹
		String url = prePath + "/jfreeAllTime" + date + ".jpg";
		File file = new File(url);
		if (file.exists()) {
			request.setAttribute("date", date);
			request.setAttribute("status", status);
			return "qunarstat/jfreePicture";
		} else {
			DefaultCategoryDataset dataset = new DefaultCategoryDataset();// 创建数据集容器
			List<Map<String, String>> hashMap = qunarStatService.queryAllHour();
		
			for (Map map : hashMap) { // 循环添加数据集
				long num = (Long) map.get("order_count");
				String dateHour = map.get("h_time").toString();
				dataset.addValue(num, "销售业绩", dateHour);

			}
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

			// 获得线图的Plot对象
			CategoryPlot plot = chart.getCategoryPlot();
			NumberAxis numberaxis = (NumberAxis) plot.getRangeAxis();
			numberaxis
					.setStandardTickUnits(NumberAxis.createIntegerTickUnits());// 设置Y轴显示数值为整数
			numberaxis.setAutoRange(true);
			chart.getTitle().setFont(font);
			// 设置底部说明字体
			chart.getLegend(0).setItemFont(font);

			FileOutputStream fos_jpg = null;
			try {
				fos_jpg = new FileOutputStream(prePath + "/jfreeAllTime" + date
						+ ".jpg");// 这个路径自然是可以随意设置的
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
			request.setAttribute("date", date);
			request.setAttribute("status", status);
			return "qunarstat/jfreePicture";

		}
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
		String prePath = request.getSession().getServletContext().getRealPath(
				"/jchart/hour"); // 保存文件的文件夹
		String create_time = this.getParam(request, "order_time");
		String url = prePath + "/jfreeThisDay" + create_time + ".jpg";// 文件的位置
		File file = new File(url);
		if (file.exists()) {
			request.setAttribute("status", status);
			request.setAttribute("create_time", create_time);
			return "qunarstat/jfreePicture";
		} else {
			DefaultCategoryDataset dataset = new DefaultCategoryDataset();// 创建数据集容器
			List<Map<String, String>> hashMap = qunarStatService.queryThisDayHour(create_time);
		
			for (Map map : hashMap) { // 循环添加数据集
				long num = (Long) map.get("order_count");
				String date = map.get("h_time").toString();
				dataset.addValue(num, "销售业绩", date);

			}
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
			// 获得线图的Plot对象
			CategoryPlot plot = chart.getCategoryPlot();
			NumberAxis numberaxis = (NumberAxis) plot.getRangeAxis();
			numberaxis
					.setStandardTickUnits(NumberAxis.createIntegerTickUnits());// 设置Y轴显示数值为整数
			numberaxis.setAutoRange(true);
			FileOutputStream fos_jpg = null;

			try {
				fos_jpg = new FileOutputStream(prePath + "/jfreeThisDay"
						+ create_time + ".jpg");// 这个路径自然是可以随意设置的
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

			request.setAttribute("status", status);
			request.setAttribute("create_time", create_time);
			request.setAttribute("prePath", prePath);
			return "qunarstat/jfreePicture";
		}
	}

}
