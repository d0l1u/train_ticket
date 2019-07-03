package com.l9e.transaction.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
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
import com.l9e.transaction.service.JfreeService;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.util.DateUtil;

@Controller
@RequestMapping("/jfree")
public class JfreeController extends BaseController {
	private static final Logger logger = Logger.getLogger(JfreeController.class);
	@Resource
	private JfreeService jfreeService;

	/**
	 * 15日以内某用户打码统计图表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/showPeruserCodePicture.do")
	public String showPeruserCodePicture(HttpServletRequest request, HttpServletResponse response) {
		String status = "peruserCode";
		SimpleDateFormat df1 = new java.text.SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(c.getTime().getTime() - 1000 * 60 * 60 * 24));
		String yesterday = df1.format(c.getTime()); // 前一天时间
		//获取查询用户名
		String opt_ren = this.getParam(request, "opt_ren");
		//查询某用户15日之日有几天打码
		int count = jfreeService.query15dayUserCodeCount(opt_ren);
		//某用户15日之内打码记录
		List<Map<String,Object>> codeList = jfreeService.query15dayUserCode(opt_ren);
		String opt_name = "";
		for (Map map : codeList) {
			opt_name = map.get("opt_name").toString();
		}
		//15日以内某用户打码统计图表的数据xml
		StringBuilder strXML2 = new StringBuilder("");
		strXML2.append("<?xml version='1.0' encoding='UTF-8'?>"
						+ "<chart caption='"+opt_name+"15日以内打码统计图' lineThickness='2' showValues='0' anchorRadius='2' anchorBgAlpha='50' "
						+ "showAlternateVGridColor='1' numVisiblePlot='12' animation='0' baseFontSize='12' formatNumberScale='0' yAxisName='打码数'>");
		long pic_count,pic_success,pic_fail,pic_unkonwn=0;
		if( count==0 ){
			strXML2.append("<categories>");
			strXML2.append("<category  label='" + yesterday.substring(8, yesterday.length()) + "日'/>");
			strXML2.append("</categories>");
			strXML2.append("<dataset seriesName='打码总数' color='DBDC25' anchorBorderColor='DBDC25' anchorBgColor='DBDC25'>");
			strXML2.append("<set value='0' /> ");
			strXML2.append("</dataset> ");
		}else{
			Map mapHour = new HashMap();
			strXML2.append("<categories>");
			for (Map map : codeList) {
				String date = map.get("opt_time").toString();
				String dateSb = date.substring(8, date.length()) + "日";
				pic_count = (Long) map.get("pic_count");
				pic_success = (Long) map.get("pic_success");
				pic_fail = (Long) map.get("pic_fail");
				pic_unkonwn = (Long) map.get("pic_unkonwn");
				strXML2.append("<category  label='" + dateSb + "'/>");
			}
			strXML2.append("</categories>");
			strXML2.append("<dataset seriesName='打码总数' color='DBDC25' anchorBorderColor='DBDC25' anchorBgColor='DBDC25'>");
			for (Map map : codeList) {
				pic_count = (Long) map.get("pic_count");
				strXML2.append("<set value='" + pic_count + "' /> ");
			}
			strXML2.append("</dataset> ");
			strXML2.append("<dataset seriesName='打码成功' color='F1683C' anchorBorderColor='F1683C' anchorBgColor='F1683C' lineThickness='3'>");
			for (Map map : codeList) {
				pic_success = (Long) map.get("pic_success");
				strXML2.append("<set value='" + pic_success + "' /> ");
			}
			strXML2.append("</dataset> ");
			strXML2.append("<dataset seriesName='打码失败' color='B1D1DC' anchorBorderColor='B1D1DC' anchorBgColor='B1D1DC'>");
			for (Map map : codeList) {
				pic_fail = (Long) map.get("pic_fail");
				strXML2.append("<set value='" + pic_fail + "' /> ");
			}
			strXML2.append("</dataset> ");
			strXML2.append("<dataset seriesName='打码超时' color='BC8F8F' anchorBorderColor='BC8F8F' anchorBgColor='BC8F8F'>");
			for (Map map : codeList) {
				pic_unkonwn = (Long) map.get("pic_unkonwn");
				strXML2.append("<set value='" + pic_unkonwn + "' /> ");
			}
			strXML2.append("</dataset> ");
		}
		strXML2.append("</chart>");
		request.setAttribute("strXML2", strXML2.toString());
		logger.info(strXML2.toString());
		//System.out.println(strXML2.toString());
		request.setAttribute("status", status);
		request.setAttribute("userDepartment", LoginUserVo.getUser_department());
		return "jfree/show_picture";
	}

	/**
	 * 15日内的打码统计图
	 * 
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/show15dayCodePicture.do")
	public String show15dayCodePicture(HttpServletResponse response, HttpServletRequest request) {
		String status = "15dayCode";
		String date = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
		List<Map<String, String>> codeList = jfreeService.query15dayCodePicture();
		StringBuilder strXML2 = new StringBuilder(""); // 用于15日内的打码统计图所需的xml
		strXML2.append("<?xml version='1.0' encoding='UTF-8'?>"
						+ "<chart caption='15日以内打码统计图' lineThickness='2' showValues='0' anchorRadius='2' anchorBgAlpha='50' "
						+ "showAlternateVGridColor='1' numVisiblePlot='12' animation='0' baseFontSize='12' formatNumberScale='0' yAxisName='打码数'>");
		
		long pic_count,pic_success,pic_fail,pic_unkonwn=0;
		
			Map mapHour = new HashMap();
			strXML2.append("<categories>");
			for (Map map : codeList) {
				String date1 = map.get("opt_time").toString();//2014-01-02
				String dateSb = date1.substring(8, date1.length()) + "日";
				pic_count = ((BigDecimal) map.get("pic_count")).longValue();
				pic_success = ((BigDecimal) map.get("pic_success")).longValue();
				pic_fail = ((BigDecimal) map.get("pic_fail")).longValue();
				pic_unkonwn = ((BigDecimal) map.get("pic_unkonwn")).longValue();
				strXML2.append("<category  label='" + dateSb + "'/>");
			}
			strXML2.append("</categories>");

			strXML2.append("<dataset seriesName='打码总数' color='DBDC25' anchorBorderColor='DBDC25' anchorBgColor='DBDC25'>");
			for (Map map : codeList) {
				pic_count = ((BigDecimal) map.get("pic_count")).longValue();
				strXML2.append("<set value='" + pic_count + "' /> ");
			}
			strXML2.append("</dataset> ");
			
			strXML2.append("<dataset seriesName='打码成功' color='F1683C' anchorBorderColor='F1683C' anchorBgColor='F1683C' lineThickness='3'>");
			for (Map map : codeList) {
				pic_success = ((BigDecimal) map.get("pic_success")).longValue();
				strXML2.append("<set value='" + pic_success + "' /> ");
			}
			strXML2.append("</dataset> ");
			
			strXML2.append("<dataset seriesName='打码失败' color='B1D1DC' anchorBorderColor='B1D1DC' anchorBgColor='B1D1DC'>");
			for (Map map : codeList) {
				pic_fail = ((BigDecimal) map.get("pic_fail")).longValue();
				strXML2.append("<set value='" + pic_fail + "' /> ");
			}
			strXML2.append("</dataset> ");
			
			strXML2.append("<dataset seriesName='打码超时' color='BC8F8F' anchorBorderColor='BC8F8F' anchorBgColor='BC8F8F'>");
			for (Map map : codeList) {
				pic_unkonwn = ((BigDecimal) map.get("pic_unkonwn")).longValue();
				strXML2.append("<set value='" + pic_unkonwn + "' /> ");
			}
			strXML2.append("</dataset> ");
			strXML2.append("</chart>");
			request.setAttribute("strXML2", strXML2.toString());
			logger.info(strXML2.toString());
			request.setAttribute("date15", date);
			request.setAttribute("status", status);
			return "jfree/show_picture";
	}
	
	
	/**
	 * 3日以内用户打码成功统计图表
	 * 今天，昨天和前天
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/showCodeSuccessPicture.do")
	public String showCodeSuccessPicture(HttpServletRequest request, HttpServletResponse response) {
		String status = "codeSuccess";
		String today = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
		SimpleDateFormat df1 = new java.text.SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(c.getTime().getTime() - 1000 * 60 * 60 * 24));
		String date1 = df1.format(c.getTime()); // 前一天时间
		c = Calendar.getInstance();
		c.setTime(new Date(c.getTime().getTime() - 1000 * 60 * 60 * 24 * 2));
		String date2 = df1.format(c.getTime()); // 前两天时间
		
		//3日以内打码成功统计图表的数据xml
		StringBuilder strXML2 = new StringBuilder("");
		strXML2.append("<?xml version='1.0' encoding='UTF-8'?>"
						+ "<chart caption='3日内小时打码成功统计图' lineThickness='2' showValues='0' anchorRadius='2' anchorBgAlpha='50' "
						+ "showAlternateVGridColor='1' numVisiblePlot='12' animation='0' baseFontSize='12' formatNumberScale='0' yAxisName='打码数'>");
		
		long pic_count,pic_success,pic_fail,pic_unkonwn=0;
			Map mapHour = new HashMap();
			strXML2.append("<categories>");
			for(int i=0;i<24;i++){
				strXML2.append("<category  label='" + i + "时'/>");
				if(i<10){
					mapHour.put("0"+i, "0"+i);
				}else{
					mapHour.put(i+"", i+"");
				}
			}
			strXML2.append("</categories>");

			//前2天时间打码记录
			strXML2.append("<dataset seriesName='"+ date2 +"' color='DBDC25' anchorBorderColor='DBDC25' anchorBgColor='DBDC25'>");
			List<Map<String,Object>> codeList2 = jfreeService.query3dayCodeSuccess(date2);
			Map map_hour2 = new HashMap();
			for (Map map : codeList2) {
				String hour_stat = map.get("hour_stat").toString();
				long code_success = Integer.valueOf(map.get("code_success").toString()).longValue();
				map_hour2.put(hour_stat, code_success);
			}
			String[] keySet2 = (String[]) mapHour.keySet().toArray(new String[17]);
			Arrays.sort(keySet2);
			for (Object keyHour : keySet2) {
				if (map_hour2.containsKey(keyHour)) {
					strXML2.append("<set value='" + map_hour2.get(keyHour) + "' /> ");
				} else {
					strXML2.append("<set value='0' /> ");
				}
			}
			strXML2.append("</dataset> ");
			
			//前1天时间打码记录
			strXML2.append("<dataset seriesName='"+ date1 +"' color='B1D1DC' anchorBorderColor='B1D1DC' anchorBgColor='B1D1DC'>");
			List<Map<String,Object>> codeList1 = jfreeService.query3dayCodeSuccess(date1);
			Map map_hour1 = new HashMap();
			for (Map map : codeList1) {
				String hour_stat = map.get("hour_stat").toString();
				long code_success = Integer.valueOf(map.get("code_success").toString()).longValue();
				map_hour1.put(hour_stat, code_success);
			}
			String[] keySet1 = (String[]) mapHour.keySet().toArray(new String[17]);
			Arrays.sort(keySet1);
			for (Object keyHour : keySet1) {
				if (map_hour1.containsKey(keyHour)) {
					strXML2.append("<set value='" + map_hour1.get(keyHour) + "' /> ");
				} else {
					strXML2.append("<set value='0' /> ");
				}
			}
			strXML2.append("</dataset> ");
			
			//今天小时打码成功数据
			strXML2.append("<dataset seriesName='"+ today +"' color='F1683C' anchorBorderColor='F1683C' anchorBgColor='F1683C'>");
			List<Map<String,Object>> codeList = jfreeService.queryTodayCodeSuccess(today);
			Map map_hour = new HashMap();
			for (Map map : codeList) {
				String hour_stat = map.get("hour_stat").toString();
				long code_success = Integer.valueOf(map.get("code_success").toString()).longValue();
				map_hour.put(hour_stat, code_success);
			}
			String[] keySet = (String[]) mapHour.keySet().toArray(new String[17]);
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
		logger.info(strXML2.toString());
		//request.setAttribute("date15", date);
		request.setAttribute("status", status);
		return "jfree/show_picture";
	}
	
	/**
	 * 用户每小时打码情况统计
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/showUserCodeSuccess.do")
	public String showUserCodeSuccess(HttpServletRequest request, HttpServletResponse response){
		String status = "showUserCodeSuccess";
		SimpleDateFormat df1 = new java.text.SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(c.getTime().getTime() - 1000 * 60 * 60 * 24));
		String yesterday = df1.format(c.getTime()); // 前一天时间
		//获取查询用户名
		String opt_ren = this.getParam(request, "opt_ren");
		//查询某用户昨日是否打码
		//int count = jfreeService.queryUserCodeSuccessCount(opt_ren);
		//某用户昨日每小时的打码记录
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("create_time", yesterday);
		paramMap.put("opt_ren", opt_ren);
			List<Map<String,Object>> hashMap = jfreeService.queryUserCodeSuccessList(paramMap);
			StringBuilder strXML1 = new StringBuilder(""); // 用于15日活跃用户数图表所需的xml
			strXML1.append("<?xml version='1.0' encoding='UTF-8'?>"
					+ "<chart rotateYAxisName='0' caption='"+ yesterday +"小时打码统计图' baseFontSize='12' formatNumberScale='0' "
							+ "shownames='1' showvalues='0' yAxisName='打码成功数' rotateYAxisName='1' lineColor='FCB541'>");
			strXML1.append("<set name='0时' value='0' />");
			for (Map map : hashMap) { //循环添加数据集
				long num = (Long) map.get("code_count");
				String date = map.get("hour_stat").toString() + "时";
				strXML1.append("<set name='" + date + "' value='" + num + "' />");
			}
		strXML1.append("</chart>");
		request.setAttribute("strXML1", strXML1.toString());
		logger.info(strXML1.toString());		
		request.setAttribute("status", status);
		
		return "jfree/show_picture"; 
	}
	
	
	/**
	 * 15日内的部门管理员打码统计图
	 * 
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/show15dayDepartCodePicture.do")
	public String show15dayDepartCodePicture(HttpServletResponse response, HttpServletRequest request) {
		String status = "15dayDepartCode";
		String date = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
		String department = this.getParam(request, "department");
		List<Map<String, String>> codeList = jfreeService.query15dayDepartCodePicture(department);
		StringBuilder strXML2 = new StringBuilder(""); // 用于15日内的打码统计图所需的xml
		strXML2.append("<?xml version='1.0' encoding='UTF-8'?>"
						+ "<chart caption='15日以内打码统计图' lineThickness='2' showValues='0' anchorRadius='2' anchorBgAlpha='50' "
						+ "showAlternateVGridColor='1' numVisiblePlot='12' animation='0' baseFontSize='12' formatNumberScale='0' yAxisName='打码数'>");
		
		long pic_count,pic_success,pic_fail,pic_unkonwn=0;
		
			Map mapHour = new HashMap();
			strXML2.append("<categories>");
			for (Map map : codeList) {
				String date1 = map.get("opt_time").toString();//2014-01-02
				String dateSb = date1.substring(8, date1.length()) + "日";
				pic_count = ((BigDecimal) map.get("pic_count")).longValue();
				pic_success = ((BigDecimal) map.get("pic_success")).longValue();
				pic_fail = ((BigDecimal) map.get("pic_fail")).longValue();
				pic_unkonwn = ((BigDecimal) map.get("pic_unkonwn")).longValue();
				strXML2.append("<category  label='" + dateSb + "'/>");
			}
			strXML2.append("</categories>");

			strXML2.append("<dataset seriesName='打码总数' color='DBDC25' anchorBorderColor='DBDC25' anchorBgColor='DBDC25'>");
			for (Map map : codeList) {
				pic_count = ((BigDecimal) map.get("pic_count")).longValue();
				strXML2.append("<set value='" + pic_count + "' /> ");
			}
			strXML2.append("</dataset> ");
			
			strXML2.append("<dataset seriesName='打码成功' color='F1683C' anchorBorderColor='F1683C' anchorBgColor='F1683C' lineThickness='3'>");
			for (Map map : codeList) {
				pic_success = ((BigDecimal) map.get("pic_success")).longValue();
				strXML2.append("<set value='" + pic_success + "' /> ");
			}
			strXML2.append("</dataset> ");
			
			strXML2.append("<dataset seriesName='打码失败' color='B1D1DC' anchorBorderColor='B1D1DC' anchorBgColor='B1D1DC'>");
			for (Map map : codeList) {
				pic_fail = ((BigDecimal) map.get("pic_fail")).longValue();
				strXML2.append("<set value='" + pic_fail + "' /> ");
			}
			strXML2.append("</dataset> ");
			
			strXML2.append("<dataset seriesName='打码超时' color='BC8F8F' anchorBorderColor='BC8F8F' anchorBgColor='BC8F8F'>");
			for (Map map : codeList) {
				pic_unkonwn = ((BigDecimal) map.get("pic_unkonwn")).longValue();
				strXML2.append("<set value='" + pic_unkonwn + "' /> ");
			}
			strXML2.append("</dataset> ");
			strXML2.append("</chart>");
			request.setAttribute("strXML2", strXML2.toString());
			logger.info(strXML2.toString());
			request.setAttribute("date15", date);
			request.setAttribute("status", status);
			request.setAttribute("department", department);
			return "jfree/show_picture";
	}
	
	/**
	 * 某部门每小时打码情况统计
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/showDepartCodeSuccessPicture.do")
	public String showDepartCodeSuccessPicture(HttpServletRequest request, HttpServletResponse response){
		String status = "showDepartCodeSuccess";
		String department = this.getParam(request, "department");
		SimpleDateFormat df1 = new java.text.SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(c.getTime().getTime() - 1000 * 60 * 60 * 24));
		String yesterday = df1.format(c.getTime()); // 前一天时间
		//某用户昨日每小时的打码记录
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("create_time", yesterday);
		paramMap.put("department", department);
		
			List<Map<String,Object>> hashMap = jfreeService.queryDepartCodeSuccessPicture(paramMap);
			StringBuilder strXML1 = new StringBuilder(""); // 用于15日活跃用户数图表所需的xml
			strXML1.append("<?xml version='1.0' encoding='UTF-8'?>"
					+ "<chart rotateYAxisName='0' caption='"+ yesterday +"小时打码成功统计图' baseFontSize='12' formatNumberScale='0' "
							+ "shownames='1' showvalues='0' yAxisName='打码数' rotateYAxisName='1' lineColor='FCB541'>");
			strXML1.append("<set name='0时' value='0' />");
			for (Map map : hashMap) { //循环添加数据集
				long num = (Long) map.get("code_count");
				String date = map.get("hour_stat").toString() + "时";
				strXML1.append("<set name='" + date + "' value='" + num + "' />");
			}
		strXML1.append("</chart>");
		request.setAttribute("strXML1", strXML1.toString());
		logger.info(strXML1.toString());		
		request.setAttribute("status", status);
		request.setAttribute("department", department);
		return "jfree/show_picture"; 
	}
	
}
