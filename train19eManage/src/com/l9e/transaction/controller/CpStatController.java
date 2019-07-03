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
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.CpStatService;
import com.l9e.transaction.vo.CpStatVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.util.DateUtil;
import com.l9e.util.PageUtil;
/**
 * cp表统计
 * @author Liht
 *
 */
@Controller
@RequestMapping("/cpStat")
public class CpStatController extends BaseController{
	private static final Logger logger = Logger.getLogger(CpStatController.class);
	
	@Resource
	private CpStatService cpStatService;
	
	/**
	 * 进入查询页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("queryCpStatListPage.do")
	public String queryCpStatListPage(HttpServletRequest request,HttpServletResponse response){
		request.setAttribute("channel_Type", CpStatVo.getCHANNELS());
		return "cpStat/cpStatList";
	}
	/**
	 * 查询列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("queryCpStatList.do")
	public String queryCpStatList(HttpServletRequest request,HttpServletResponse response){
		List<String> channel = this.getParamToList(request, "channel");
		String begin_time = this.getParam(request, "begin_time");
		String end_time = this.getParam(request, "end_time");
		Map<String,Object>paramMap = new HashMap<String,Object>();
		paramMap.put("begin_time", begin_time);
		paramMap.put("end_time", end_time);
		paramMap.put("channel", channel);
		
		int totalCount = cpStatService.querycpStatServiceListCount(paramMap);//总条数	
		PageVo page = PageUtil.getInstance().paging(request, 15, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		List<Map<String,Object>>cpStatList = cpStatService.querycpStatServiceList(paramMap);
		List<Map<String,Object>>cpStatListCount = new ArrayList<Map<String,Object>>();
		String cgl;String sbl;double order_succeed;double order_defeated;double order_count;
		for(int i=0;i<cpStatList.size();i++){
			Map<String,Object>map = cpStatList.get(i);
			if((Integer)map.get("order_count")==0){
				cgl="0.00%";
				sbl="0.00%";
			}else{
				order_succeed=(Integer)map.get("order_succeed");
				order_defeated=(Integer)map.get("order_defeated");
				order_count = (Integer)map.get("order_count");
				BigDecimal   b   =   new   BigDecimal((order_succeed/order_count)*100);
				double   f1   =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
				BigDecimal   b1   =   new   BigDecimal((order_defeated/order_count)*100);
				double   f2   =   b1.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
				cgl=f1+"%";
				sbl=f2+"%";
			}
			map.put("cgl", cgl);
			map.put("sbl", sbl);
			cpStatListCount.add(map);
		}
		request.setAttribute("cpStatListCount", cpStatListCount);
		request.setAttribute("channel_Type", CpStatVo.getCHANNELS());
		request.setAttribute("channelStr", channel.toString());
		request.setAttribute("isShowList", 1);
		return "cpStat/cpStatList";
	}
	/**
	 * 查询19e与去哪网的15日内销售情况统计图
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("showPictureCpLine.do")
	public String showPictureLine(HttpServletResponse response,HttpServletRequest request){
		String status = "15dayCpStat";
		String prePath = request.getSession().getServletContext().getRealPath("/jchart"); 
		String date = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
		String url = prePath+"/jfreePictureCpStat"+date+".jpg";
		File file = new File(url);
		if(file.exists()){
			request.setAttribute("date15", date);
			request.setAttribute("status", status);
			return "cpStat/cpStatPicture";
		}else{
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		List<Map<String,String>>hashMap = cpStatService.queryPictureLineParam();
			for(Map map :hashMap){
				int num_19e = (Integer) map.get("order_succeed_19e");
				int num_qunar = (Integer) map.get("order_succeed_qunar");
				String date1 = map.get("order_time_19e").toString();
				String dateSb = date1.substring(8, date1.length())+"日";
				dataset.addValue(num_19e,"19e销售量",dateSb);
				dataset.addValue(num_qunar, "qunar销售量", dateSb);
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
		
		JFreeChart chart = ChartFactory.createLineChart("15日内的销售对比统计图", // 图表标题
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
					prePath+"/jfreePictureCpStat"+date+".jpg");// 这个路径自然是可以随意设置的
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
		return "cpStat/cpStatPicture";
		}
	}

}
