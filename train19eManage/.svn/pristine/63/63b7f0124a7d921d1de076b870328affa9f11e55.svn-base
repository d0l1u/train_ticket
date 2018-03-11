package com.l9e.transaction.controller;

import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import com.l9e.transaction.service.ExtRefundService;
import com.l9e.transaction.service.HcStatService;
import com.l9e.transaction.service.JfreeService;
import com.l9e.transaction.vo.AccountVo;
import com.l9e.transaction.vo.CpStatVo;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.util.DateUtil;
import com.l9e.util.ExcelUtil;
import com.l9e.util.MemcachedUtil;
import com.l9e.util.PageUtil;
import com.l9e.util.SwitchUtils;

/**
 * 统计Hc表其中包括去哪统计
 * @author liht
 *
 */
@Controller
@RequestMapping("/hcStat")
public class HcStatController extends BaseController{
	private static final Logger logger = Logger.getLogger(HcStatController.class);
	
	@Resource
	private HcStatService hcStatService;
	@Resource
	private JfreeService jfreeService ;
	@Resource
	private ExtRefundService extRefundService;
	
	/**
	 * 查询条数
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/queryHcStatPage.do")
	public String queryHcStatCount(HttpServletResponse response ,HttpServletRequest request){
		request.setAttribute("channel_Type", CpStatVo.getCHANNELS());
		
		/*****************************省级代理省份方法*****************************/
		LoginUserVo loginUserVo = (LoginUserVo)request.getSession().getAttribute("loginUserVo");
		Calendar theCa = Calendar.getInstance(); 
		theCa.setTime(new Date());  
		theCa.add(theCa.DATE, -7); 
		Date date = theCa.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String querydate=df.format(date);
		if(loginUserVo.getUser_level().equals("1.1")&& loginUserVo.getSupervise_name()!=null){
			String str = loginUserVo.getSupervise_name();
			List<String>Supervise_name_List =  SwitchUtils.strToList(str);
			List<Map<String,String>>area_no_list  = new ArrayList<Map<String,String>>();
			Map<String,String>area_Map = new HashMap<String,String>();
			for(int i=0;i<Supervise_name_List.size();i++){
				area_Map=hcStatService.querySupervise_nameToArea_no(Supervise_name_List.get(i).toString());
				area_no_list.add(area_Map);
			}
			request.setAttribute("province",area_no_list); 
			
			return "hcStat/superviseAreaList";
		}else{
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
			    return "redirect:queryHcStatList.do?begin_time="+querydate+str;
			}else{
				return "redirect:queryHcStatList.do&begin_time="+querydate;
			}  
		}
	}
	
	/**
	 * 出票统计页面
	 * @param response
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryHcStat.do")
	public String queryHcStat(HttpServletResponse response ,HttpServletRequest request){
		SimpleDateFormat df1 = new java.text.SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(c.getTime().getTime()));
		String create_time3 = df1.format(c.getTime());   //前一天时间--->今天

		c = Calendar.getInstance();
		c.setTime(new Date(c.getTime().getTime() - 1000 * 60 * 60 * 24 * 1));
		String create_time2 = df1.format(c.getTime());	//前两天时间--->前一天时间
		
		c = Calendar.getInstance();
		c.setTime(new Date(c.getTime().getTime() - 1000 * 60 * 60 * 24 * 2));
		String create_time = df1.format(c.getTime());	//前三天时间--->前两天时间
		
//		String create_time3 = "2014-06-25";
//		String create_time2 = "2014-06-24";
//		String create_time = "2014-06-23";
		
		Map<String,String> mapHistory = hcStatService.queryHistory();
		String out_ticket_succeed =  mapHistory.get("out_ticket_succeed");//历史最高出票量
		String succeed_money = mapHistory.get("succeed_money");//历史最高交易量
		String succeed_cgl = mapHistory.get("succeed_cgl");//平均成功率
		
		Map<String,Object> mapYesterday = hcStatService.queryYesterdayTicketMoney(create_time2);
		int ticket_succeed = Integer.parseInt((String) mapYesterday.get("ticket_succeed"));//昨日出票成功总票数
		int ticket_defeated = Integer.parseInt((String) mapYesterday.get("ticket_defeated"));
		int ticket_count = Integer.parseInt((String) mapYesterday.get("ticket_count"));   //票数总计
		//int ticket_defeated = (Integer)mapYesterday.get("ticket_defeated");//昨日出票失败总票数
		String buy_money = (String) mapYesterday.get("buy_money");//昨日出票成功金额
		
		java.text.DecimalFormat df=new   java.text.DecimalFormat("#.##");  
		String cgl = "";//昨日成功率
		if((ticket_defeated+ticket_succeed) != 0){
			cgl =df.format((double)ticket_succeed/(double)(ticket_defeated+ticket_succeed)*100)+"%".toString();
		}else{
			cgl = "0.00%";
		}
		
		request.setAttribute("ticket_count", ticket_count);
		request.setAttribute("out_ticket_succeed", out_ticket_succeed);
		request.setAttribute("succeed_money", succeed_money);
		request.setAttribute("succeed_cgl", succeed_cgl);
		request.setAttribute("ticket_succeed", ticket_succeed);
		request.setAttribute("buy_money", buy_money);
		request.setAttribute("cgl", cgl);
		
		//19e渠道15日内销售统计图的数据xml
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
		
		
		//19e渠道分时销售统计图的数据xml
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
		//System.out.println("strXML2---"+strXML2.toString());
		
		//19e渠道分时出票失败率统计图的数据xml
		//yAxisMaxValue='100' yAxisMinValue='0'  设置y轴的最大值和最小值
		StringBuilder strXML3 = new StringBuilder("");
		strXML3.append("<?xml version='1.0' encoding='UTF-8'?>"
						+ "<chart caption='19e渠道出票失败率统计图' labelDisplay='Stagger' lineThickness='2' showValues='0' anchorRadius='3' anchorBgAlpha='50' "
						+ "numberSuffix ='%25' showAlternateVGridColor='1' numVisiblePlot='12' animation='0' baseFontSize='12' yAxisName='出票失败率'>");
		Map mapHalfHour = new HashMap();
		strXML3.append("<categories>");
		for (int i = 0; i < 24; i++) {
			strXML3.append("<category  label='" + i + ":00'/>");
			strXML3.append("<category  label='" + i + ":30'/>");
			if(i<10){
				mapHalfHour.put("0"+i + ":00", "0"+i + ":00");
				mapHalfHour.put("0"+i + ":30", "0"+i + ":30");
			}else{
				mapHalfHour.put(i + ":00", i + ":00");
				mapHalfHour.put(i + ":30", i + ":30");
			}
		} 
		strXML3.append("</categories>");

		List<Map<String, String>> beforeMap1 = jfreeService.queryOutTicketSbl(create_time); // 每个前半小时
		List<Map> listMap1 = new ArrayList<Map>();
		String hourStat1 = null, dayStat1 = null;
		Double sbl1 = 0.0;
		for (Map map_before1 : beforeMap1) { // 遍历每个前半小时的数据
			sbl1 = Double.parseDouble(map_before1.get("sbl").toString());
			hourStat1 = map_before1.get("hour_stat").toString();
			dayStat1 = map_before1.get("day_stat").toString();
			listMap1.add(map_before1);// 将每个前半小时的数据加入到list里面
		}
		strXML3.append("<dataset seriesName='"
						+ create_time
						+ "' color='DBDC25' anchorBorderColor='DBDC25' anchorBgColor='DBDC25'>");
		Map mapHalfhour1 = new HashMap();
		for (int i = 0; i < listMap1.size(); i++) {// 循环添加数据集
			sbl1 = Double.parseDouble(listMap1.get(i).get("sbl").toString());
			hourStat1 = listMap1.get(i).get("hour_stat").toString();
			dayStat1 = listMap1.get(i).get("day_stat").toString();
			mapHalfhour1.put(hourStat1, sbl1);
		}
		String[] keyHalfSet1 = (String[]) mapHalfHour.keySet().toArray(new String[48]);
		Arrays.sort(keyHalfSet1);
		for (Object keyHour : keyHalfSet1) {
			if (mapHalfhour1.containsKey(keyHour)) {
				strXML3.append("<set value='" + mapHalfhour1.get(keyHour) + "' /> ");
			} else {
				strXML3.append("<set value='0.00' /> ");
			}
		}
		strXML3.append("</dataset> ");

		List<Map<String, String>> beforeMap2 = jfreeService.queryOutTicketSbl(create_time2); // 每个前半小时
		List<Map> listMap2 = new ArrayList<Map>();
		String hourStat2 = null, dayStat2 = null;
		Double sbl2 = 0.0000;
		for (Map map_before2 : beforeMap2) { // 遍历每个前半小时的数据
			sbl2 = Double.parseDouble(map_before2.get("sbl").toString());
			hourStat2 = map_before2.get("hour_stat").toString();
			dayStat2 = map_before2.get("day_stat").toString();
			listMap2.add(map_before2);// 将每个前半小时的数据加入到list里面
		}
		strXML3.append("<dataset seriesName='"
						+ create_time2
						+ "' color='B1D1DC' anchorBorderColor='B1D1DC' anchorBgColor='B1D1DC'>");
		Map mapHalfHour2 = new HashMap();
		for (int i = 0; i < listMap2.size(); i++) {// 循环添加数据集
			sbl2 = Double.parseDouble(listMap2.get(i).get("sbl").toString());
			hourStat2 = listMap2.get(i).get("hour_stat").toString();
			dayStat2 = listMap2.get(i).get("day_stat").toString();
			mapHalfHour2.put(hourStat2, sbl2);
		}
		String[] keyHalfSet2 = (String[]) mapHalfHour.keySet().toArray(new String[48]);
		Arrays.sort(keyHalfSet2);
		for (Object keyHour : keyHalfSet2) {
			if (mapHalfHour2.containsKey(keyHour)) {
				strXML3.append("<set value='" + mapHalfHour2.get(keyHour) + "' /> ");
			} else {
				strXML3.append("<set value='0.00' /> ");
			}
		}
		strXML3.append("</dataset> ");

		List<Map<String, String>> beforeMap = jfreeService.queryOutTicketSbl(create_time3); // 每个前半小时
		List<Map> listHour = new ArrayList<Map>();
		String hourStat = null, dayStat = null;
		Double sbl=0.0;
		for (Map map_before : beforeMap) { // 遍历每个前半小时的数据
			sbl = Double.parseDouble(map_before.get("sbl").toString());
			hourStat = map_before.get("hour_stat").toString();
			dayStat = map_before.get("day_stat").toString();
			listHour.add(map_before);// 将每个前半小时的数据加入到list里面
		}
		strXML3.append("<dataset seriesName='"
						+ create_time3
						+ "' lineThickness='5' color='F1683C' anchorBorderColor='F1683C' anchorBgColor='F1683C'>");
		Map map_hour_half = new HashMap();
		for (int i = 0; i < listHour.size(); i++) {// 循环添加数据集
			sbl = Double.parseDouble(listHour.get(i).get("sbl").toString());
			hourStat = listHour.get(i).get("hour_stat").toString();
			dayStat = listHour.get(i).get("day_stat").toString();
			map_hour_half.put(hourStat, sbl);
		}
		String[] keySetHalf = (String[]) mapHalfHour.keySet().toArray(new String[48]);
		Arrays.sort(keySetHalf);
		for (Object keyHour : keySetHalf) {
			if (map_hour_half.containsKey(keyHour)) {
				strXML3.append("<set value='" + map_hour_half.get(keyHour) + "' /> ");
			} else {
				strXML3.append("<set value='0.00' /> ");
			}
		}
		strXML3.append("</dataset> ");

		strXML3.append("</chart>");
		request.setAttribute("strXML3", strXML3.toString());
		//System.out.println("strXML3---"+strXML3.toString());
		
		
		return "hcStat/hcStat";
	}
	
	
	
	
	/**
	 * 查询列表
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/queryHcStatList.do")
	public String queryHcStatList(HttpServletResponse response ,HttpServletRequest request){
		String begin_time = this.getParam(request, "begin_time");
		String end_time = this.getParam(request, "end_time");
		List<String> channel = this.getParamToList(request, "channel");
		List<String> sort = this.getParamToList(request, "sort");
		String controlAllChannel = this.getParam(request, "controlAllChannel");//全选
		Map<String,Object>query_Map = new HashMap<String,Object>();
		query_Map.put("begin_time", begin_time);
		query_Map.put("end_time", end_time);
		query_Map.put("channel", channel);
//		if(controlAllChannel.length()==0){
//			if(channel.size()==0){
//				channel.add("19e");
//				channel.add("qunar");
//			}
//			query_Map.put("channel", channel);
//		}else{
//			query_Map.put("channel", "");
//		}
//		List<String> channelList = new ArrayList<String>(channel);
//		if(channel.size()>0 && !channel.equals("[]") && channel.get(0)!= ""){
//			
//			if(channelList.contains("30101612")){
//				channelList.add("301016");
//				channelList.add("30101601");
//				channelList.add("30101602");
//			}
//			query_Map.put("channel", channel);
//		}
	
		int totalCount = hcStatService.queryHcStatCount(query_Map);//总条数	
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		query_Map.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		query_Map.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		
		List<Map<String,Object>>hcStat_List = hcStatService.queryHcStatList(query_Map);
		request.setAttribute("hcStat_List", hcStat_List);
		request.setAttribute("begin_time", begin_time);
		request.setAttribute("end_time", end_time);
		List<Map<String,String>> merchantList = extRefundService.queryExtMerchantinfo();//查询合作商户的id及名称
		Map<String, String> merchantMap = AccountVo.getChannels();
		Map<String, String> merchant_map = new LinkedHashMap<String, String>();
		Map<String,String> channel_map = AccountVo.getTjChannels();
		channel_map.put("ext", "商户");
		merchant_map.put("ext", "商户");
		for(int i = 0 ; i < merchantList.size() ; i++){
			merchantMap.put(merchantList.get(i).get("merchant_id"), merchantList.get(i).get("merchant_name"));
			merchant_map.put(merchantList.get(i).get("merchant_id"), merchantList.get(i).get("merchant_name"));
			channel_map.put(merchantList.get(i).get("merchant_id"), merchantList.get(i).get("merchant_name"));
		}
		request.setAttribute("sort_channel", AccountVo.getSortChannels());//类别：11公司、22商户、33内嵌、44B2C、55代理
		request.setAttribute("company_channel", AccountVo.getCompanyChannels());//11公司  (19e、19pay)
		request.setAttribute("merchant_channel", merchant_map);//22商户
		request.setAttribute("inner_channel", AccountVo.getInnerChannels());//33内嵌(cmpay、ccb)
		request.setAttribute("b2c_channel", AccountVo.getB2cChannels());//44B2C(app、weixin)
		request.setAttribute("agency_channel", AccountVo.getAgencyChannels());//55代理 (去哪)
		
		request.setAttribute("channel_types", channel_map);
		//request.setAttribute("channel_Type", merchantMap);
		request.setAttribute("channelStr", channel.toString());
		request.setAttribute("isShowList", 1);
		request.setAttribute("sort", sort);
		request.setAttribute("controlAllChannel", controlAllChannel);
		
		return "hcStat/hcStatList";
	}
	
	/**
	 * 省级代理显示页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/querySuperviseAreaList.do")
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
		int totalCount = hcStatService.querySuperviseAreaCount(paramMap);//总条数	
		PageVo page = PageUtil.getInstance().paging(request, 15, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		List<Map<String,Object>>superviseAreaList =hcStatService.querySuperviseAreaList(paramMap);
		
		for(int j=0;j<superviseAreaList.size();j++){
			Map<String,Object> map = superviseAreaList.get(j);
			java.text.DecimalFormat df=new   java.text.DecimalFormat("#.##");   
			int cg = (Integer)map.get("succeed_count");
			int sb = (Integer)map.get("defeated_count"); 
			int zh = (Integer)map.get("pay_fall"); 
			String cgl,sbl,zhl = "";
			if((cg+sb) != 0){
				cgl =df.format((double)cg/(double)(cg+sb)*100)+"%".toString();
				sbl =df.format((double)sb/(double)(cg+sb)*100)+"%".toString();
			}else{
				cgl = "0.00%";
				sbl = "0.00%";
			}
			if((cg+sb+zh) != 0){
				zhl=df.format((double)cg/(double)(cg+sb+zh)*100)+"%".toString();
			}else{
				zhl = "0.00%";
			}
			map.put("cgl", cgl);
			map.put("sbl", sbl);
			map.put("zhl", zhl);
		}
			List<Map<String,String>>area_no_list  = new ArrayList<Map<String,String>>();
			Map<String,String>area_Map = new HashMap<String,String>();
			for(int i=0;i<supervise_name_List.size();i++){
				area_Map=hcStatService.querySupervise_nameToArea_no(supervise_name_List.get(i).toString());
				area_no_list.add(area_Map);
			}
		request.setAttribute("province",area_no_list); 
		request.setAttribute("superviseAreaList", superviseAreaList);
		request.setAttribute("begin_time", begin_time);
		request.setAttribute("end_time", end_time);
		request.setAttribute("province_id", province_id);
		request.setAttribute("isShowList", 1);
		
		return "hcStat/superviseAreaList";
	}
	
	/**
	 * 查询各省销售总计
	 * @param response
	 * @param request
	 * @return
	 */

	@RequestMapping("/queryThisDayInfo.do")
	public String queryProvinceCount(HttpServletResponse response ,HttpServletRequest request){
		String create_time = this.getParam(request, "create_time");
		List<Map<String,Object>>statInfo_provinceList = hcStatService.queryHc_statInfo_provinceList(create_time);
		for(int j=0;j<statInfo_provinceList.size();j++){
			Map<String,Object> map = statInfo_provinceList.get(j);
			java.text.DecimalFormat df=new   java.text.DecimalFormat("#.##");   
			int cg = (Integer)map.get("succeed_count");
			int sb = (Integer)map.get("defeated_count"); 
			int order_count = (Integer)map.get("order_count");
			String cgl,sbl,zhl = "";
			if((cg+sb) != 0){
				cgl =df.format((double)cg/(double)(cg+sb)*100)+"%".toString();
				sbl =df.format((double)sb/(double)(cg+sb)*100)+"%".toString();
			}else{
				cgl = "0.00%";
				sbl = "0.00%";
			}
			if(order_count != 0){
				zhl =df.format((double)cg/(double)order_count*100)+"%".toString();
			}else{
				zhl = "0.00%";
			}
			
			String province_id = map.get("province_id").toString();

			Map<String,String>map_Tic =  hcStatService.queryProvinceTotal_Ticket(province_id,create_time);
			map.put("cgl", cgl);
			map.put("sbl", sbl);
			map.put("zhl", zhl);
			map.put("total_Money", map_Tic.get("today_money").toString());
			map.put("total_Ticket", map_Tic.get("ticket_count").toString());
			
		}
		request.setAttribute("create_time", create_time);
		request.setAttribute("statInfo_provinceList", statInfo_provinceList);
		return "hcStat/provinceStat";
	}
	
	//出票统计--导出excel
	@RequestMapping("/exportexcel.do")
	public String exportExcel(HttpServletRequest request,
			HttpServletResponse response) {
		String begin_time = this.getParam(request, "begin_time");
		String end_time = this.getParam(request, "end_time");
		List<String> channel = this.getParamToList(request, "channel");
		List<String> sort = this.getParamToList(request, "sort");
		Map<String,Object>query_Map = new HashMap<String,Object>();
		query_Map.put("begin_time", begin_time);
		query_Map.put("end_time", end_time);
		if(channel.size()>0 && !channel.equals("[]") && channel.get(0)!= ""){
			query_Map.put("channel", channel);
		}
		List<Map<String, String>> reslist = hcStatService.queryHcStatExcel(query_Map);
		
		List<Map<String,String>> merchantList = extRefundService.queryExtMerchantinfo();//查询合作商户的id及名称
		Map<String,String> channel_map = AccountVo.getTjChannels();
		for(int i = 0 ; i < merchantList.size() ; i++){
			channel_map.put(merchantList.get(i).get("merchant_id"), merchantList.get(i).get("merchant_name"));
		}
		channel_map.put("30101612", "利安");
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		for (Map<String, String> m : reslist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			Object ob1 = m.get("active");
			Object ob2 = m.get("agent_login_num");
			Object ob3 = m.get("svip_num");
			Object ob4= m.get("ticket_count");
			Object ob5= m.get("channel");
			if(null!=ob1){
				linkedList.add(ob1.toString());
			}else{
				linkedList.add("");
			}
			if(null!=ob2){
				linkedList.add(ob2.toString());
			}else{
				linkedList.add("");
			}
			if(null!=ob3){
				linkedList.add(ob3.toString());
			}else{
				linkedList.add("");
			}
			if(null!=ob4){
				linkedList.add(ob4.toString());
			}else{
				linkedList.add("");
			}
			if(null!=ob5){
				linkedList.add(channel_map.get(ob5.toString()));
			}else{
				linkedList.add("");
			}
			
			
			list.add(linkedList);
		}
		String title = "火车票出票统计明细";

		String date = createDate(begin_time, end_time);
		String filename = "火车票出票统计.xls";
		String[] secondTitles = { "序号", "活跃数","登陆数", "SVIP数", "票总计", "渠道"};
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
	
	/**
	 * 进入统计页面
	 */
	@RequestMapping("/queryTongjiPage.do")
	public String queryTongjiPage(HttpServletRequest request,
			HttpServletResponse response){
		logger.info("【进入分时统计页面】queryTongjiPage.do");
		String begin_time = this.getParam(request, "begin_time");
		String end_time = this.getParam(request, "end_time");
		String channel = this.getParam(request, "channel");
		String days = this.getParam(request, "days");
		String type = this.getParam(request, "type");
		Map<String,Object>query_Map = new HashMap<String,Object>();
		query_Map.put("begin_time", begin_time);
		query_Map.put("end_time", end_time);
		List<String> channelList = new ArrayList<String>();
		channelList.add(channel);
		if("qunar1".equals(channel)){
			query_Map.put("order_source", "qunar1");
		}else if("qunar2".equals(channel)){
			query_Map.put("order_source", "qunar2");
		}else if("30101612".equals(channel)){
			channelList.add("301016");
			channelList.add("30101601");
			channelList.add("30101602");
			query_Map.put("channel", channelList);
		}else if("ext".equals(channel) ||"inner".equals(channel) ){
			//不赋值给channel
		}else{
		query_Map.put("channel", channelList);
		}
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		List<Map<String,Object>> timeList=new ArrayList<Map<String,Object>>();
		for(int j=0;j<=Integer.valueOf(days);j++){
			Calendar theCa = Calendar.getInstance(); 
			Date d;
			try {
				d = df.parse(end_time);
				theCa.setTime(d);  
			} catch (ParseException e) {
			}     
			theCa.add(theCa.DATE, -j); 
			Date date = theCa.getTime();
			String time=df.format(date);
		query_Map.put("create_time", time);
		List<Map<String,Object>> countList=new ArrayList<Map<String,Object>>();
		int amount=0;
		int nowAmount=0;
		Calendar nowTime = Calendar.getInstance(); 
		int ss = nowTime.get(Calendar.HOUR_OF_DAY); 
		
	if("1".equals(type)){
			for(int i=0;i<24;i++){
				if(i<10)
					query_Map.put("hour", " 0"+i);
				else
				query_Map.put("hour", " "+String.valueOf(i));
				int count=0;
				if("qunar1".equals(channel) ||"qunar2".equals(channel) )
					count = hcStatService.querytongjiListQunar(query_Map);
				else if("inner".equals(channel))
					count = hcStatService.querytongjiListInner(query_Map);
				else if("ext".equals(channel))
					count = hcStatService.querytongjiListExt(query_Map);
				else
					count = hcStatService.querytongjiList(query_Map);
				if(i==6){
					int count_a=0;
					int count_b=0;
					for(int m=0;m<6;m++){
					query_Map.put("hour", " 0"+m);
					if("qunar1".equals(channel) ||"qunar2".equals(channel) )
						count_a = hcStatService.querytongjiListQunar(query_Map);
					else if("inner".equals(channel))
						count_a = hcStatService.querytongjiListInner(query_Map);
					else if("ext".equals(channel))
						count_a = hcStatService.querytongjiListExt(query_Map);
					else
						count_a = hcStatService.querytongjiList(query_Map);
					}
					query_Map.put("hour", " "+String.valueOf(23));
					if("qunar1".equals(channel) ||"qunar2".equals(channel) )
						count_b = hcStatService.querytongjiListQunar(query_Map);
					else if("inner".equals(channel))
						count_b = hcStatService.querytongjiListInner(query_Map);
					else if("ext".equals(channel))
						count_b = hcStatService.querytongjiListExt(query_Map);
					else
						count_b = hcStatService.querytongjiList(query_Map);
					count=count+ count_a+count_b;
				}
				
				Map<String,Object> map=new HashMap<String, Object>();
				map.put("tongji", count);
				amount+=count;
				if(i<=ss)
					nowAmount+=count;
				countList.add(map);
			}
		}
		else if("2".equals(type)){
			for(int i=0;i<24;i++){
				if(i<10)
					query_Map.put("hour", " 0"+i);
				else
				query_Map.put("hour", " "+String.valueOf(i));
				int count=0;
				if("qunar1".equals(channel) ||"qunar2".equals(channel) )
					count = hcStatService.querytongjiListQunarOrder(query_Map);
				else if("inner".equals(channel))
					count = hcStatService.querytongjiListInnerOrder(query_Map);
				else if("ext".equals(channel))
					count = hcStatService.querytongjiListExtOrder(query_Map);
				else
					count = hcStatService.querytongjiListOrder(query_Map);
				if(i==6){
					int count_a=0;
					int count_b=0;
					for(int m=0;m<6;m++){
					query_Map.put("hour", " 0"+m);
					if("qunar1".equals(channel) ||"qunar2".equals(channel) )
						count_a = hcStatService.querytongjiListQunarOrder(query_Map);
					else if("inner".equals(channel))
						count_a = hcStatService.querytongjiListInnerOrder(query_Map);
					else if("ext".equals(channel))
						count_a = hcStatService.querytongjiListExtOrder(query_Map);
					else
						count_a = hcStatService.querytongjiListOrder(query_Map);
					}
					query_Map.put("hour", " "+String.valueOf(23));
					if("qunar1".equals(channel) ||"qunar2".equals(channel) )
						count_b = hcStatService.querytongjiListQunarOrder(query_Map);
					else if("inner".equals(channel))
						count_b = hcStatService.querytongjiListInnerOrder(query_Map);
					else if("ext".equals(channel))
						count_b = hcStatService.querytongjiListExtOrder(query_Map);
					else
						count_b = hcStatService.querytongjiListOrder(query_Map);
					count=count+ count_a+count_b;
				}
				
				Map<String,Object> map=new HashMap<String, Object>();
				map.put("tongji", count);
				amount+=count;
				if(i<=ss)
					nowAmount+=count;
				countList.add(map);
			}
			
		}
	
		Map<String,Object> mapTime=new HashMap<String, Object>();
		mapTime.put("time", time);
		mapTime.put("countList", countList);
		mapTime.put("amount", amount);
		mapTime.put("nowAmount", nowAmount);
		timeList.add(mapTime);
		}
		List<Map<String,String>> merchantList = extRefundService.queryExtMerchantinfo();//查询合作商户的id及名称
		Map<String,String> channel_map = AccountVo.getTjChannels();
		for(int i = 0 ; i < merchantList.size() ; i++){
			channel_map.put(merchantList.get(i).get("merchant_id"), merchantList.get(i).get("merchant_name"));
		}
		request.setAttribute("channel_types", channel_map);
		request.setAttribute("begin_time", begin_time);
		request.setAttribute("end_time", end_time);
		request.setAttribute("channel", channel);
		request.setAttribute("timeList", timeList);
		request.setAttribute("type", type);
		return "hcStat/tongjiStat";
		}
	
	/**
	 * 进入当天分时统计页面
	 */
	@RequestMapping("/queryTongjiTodayPage.do")
	public String queryTongjiTodayPage(HttpServletRequest request,
			HttpServletResponse response){
		logger.info("【进入当天分时统计页面】queryTongjiTodayPage.do");
		String begin_time = this.getParam(request, "begin_time");
		String end_time = this.getParam(request, "end_time");
		String channelStr = this.getParam(request, "channelList");
		String[] arr1 = channelStr.split(",");
		Map<String,Object>query_Map = new HashMap<String,Object>();
		query_Map.put("begin_time", begin_time);
		query_Map.put("end_time", end_time);
		
		query_Map.put("create_time", end_time);
		List<Map<String,Object>> timeList1=new ArrayList<Map<String,Object>>();
		String channel="";
		for(int j=0;j<arr1.length;j++){
			channel=arr1[j];
		List<String> channelList = new ArrayList<String>();
		channelList.add(channel);
		if("qunar1".equals(channel)){
			query_Map.put("order_source", "qunar1");
		}else if("qunar2".equals(channel)){
			query_Map.put("order_source", "qunar2");
		}else if("30101612".equals(channel)){
			channelList.add("301016");
			channelList.add("30101601");
			channelList.add("30101602");
			query_Map.put("channel", channelList);
		}else if("ext".equals(channel) ||"inner".equals(channel) ){
			//不赋值给channel
		}else{
		query_Map.put("channel", channelList);
		}
		List<Map<String,Object>> countList=new ArrayList<Map<String,Object>>();
		int amount=0;
		int nowAmount=0;
		Calendar nowTime = Calendar.getInstance(); 
		int ss = nowTime.get(Calendar.HOUR_OF_DAY); 
		System.out.println("#####"+ss);
		for(int i=0;i<24;i++){
			if(i<10)
				query_Map.put("hour", " 0"+i);
			else
			query_Map.put("hour", " "+String.valueOf(i));
			int count=0;
			if("qunar1".equals(channel) ||"qunar2".equals(channel) )
				count = hcStatService.querytongjiListQunar(query_Map);
			else if("inner".equals(channel))
				count = hcStatService.querytongjiListInner(query_Map);
			else if("ext".equals(channel))
				count = hcStatService.querytongjiListExt(query_Map);
			else
				count = hcStatService.querytongjiList(query_Map);
			
			if(i==6){
				int count_a=0;
				int count_b=0;
				for(int m=0;m<6;m++){
				query_Map.put("hour", " 0"+m);
				if("qunar1".equals(channel) ||"qunar2".equals(channel) )
					count_a = hcStatService.querytongjiListQunar(query_Map);
				else if("inner".equals(channel))
					count_a = hcStatService.querytongjiListInner(query_Map);
				else if("ext".equals(channel))
					count_a = hcStatService.querytongjiListExt(query_Map);
				else
					count_a = hcStatService.querytongjiList(query_Map);
				}
				query_Map.put("hour", " "+String.valueOf(23));
				if("qunar1".equals(channel) ||"qunar2".equals(channel) )
					count_b = hcStatService.querytongjiListQunar(query_Map);
				else if("inner".equals(channel))
					count_b = hcStatService.querytongjiListInner(query_Map);
				else if("ext".equals(channel))
					count_b = hcStatService.querytongjiListExt(query_Map);
				else
					count_b = hcStatService.querytongjiList(query_Map);
				count=count+ count_a+count_b;
			}
			
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("tongji", count);
			amount+=count;
			if(i<=ss)
				nowAmount+=count;
			countList.add(map);
		}
		Map<String,Object> mapTime=new HashMap<String, Object>();
		mapTime.put("countList", countList);
		mapTime.put("amount", amount);
		mapTime.put("nowAmount", nowAmount);
		mapTime.put("channel", channel);
		timeList1.add(mapTime);
		}
		
		query_Map.put("create_time", begin_time);
		List<Map<String,Object>> timeList2=new ArrayList<Map<String,Object>>();
		for(int j=0;j<arr1.length;j++){
			channel=arr1[j];
		List<String> channelList = new ArrayList<String>();
		channelList.add(channel);
		if("qunar1".equals(channel)){
			query_Map.put("channel", "qunar");
			query_Map.put("order_source", "qunar1");
		}else if("qunar2".equals(channel)){
			query_Map.put("channel", "qunar");
			query_Map.put("order_source", "qunar2");
		}else if("30101612".equals(channel)){
			channelList.add("301016");
			channelList.add("30101601");
			channelList.add("30101602");
			query_Map.put("channel", channelList);
		}else{
		query_Map.put("channel", channelList);
		}
		List<Map<String,Object>> countList=new ArrayList<Map<String,Object>>();
		int amount=0;
		int nowAmount=0;
		Calendar nowTime = Calendar.getInstance(); 
		int ss = nowTime.get(Calendar.HOUR_OF_DAY); 
		System.out.println("#####"+ss);
		for(int i=0;i<24;i++){
			if(i<10)
				query_Map.put("hour", " 0"+i);
			else
			query_Map.put("hour", " "+String.valueOf(i));
			int count=0;
			if("qunar1".equals(channel) ||"qunar2".equals(channel) )
				count = hcStatService.querytongjiListQunar(query_Map);
			else if("inner".equals(channel))
				count = hcStatService.querytongjiListInner(query_Map);
			else if("ext".equals(channel))
				count = hcStatService.querytongjiListExt(query_Map);
			else
				count = hcStatService.querytongjiList(query_Map);
			
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("tongji", count);
			amount+=count;
			if(i<=ss)nowAmount+=count;
			countList.add(map);
		}
		Map<String,Object> mapTime=new HashMap<String, Object>();
		mapTime.put("countList", countList);
		mapTime.put("amount", amount);
		mapTime.put("nowAmount", nowAmount);
		mapTime.put("channel", channel);
		timeList2.add(mapTime);
		}
		
		List<Map<String,String>> merchantList = extRefundService.queryExtMerchantinfo();//查询合作商户的id及名称
		Map<String,String> channel_map = AccountVo.getTjChannels();
		for(int i = 0 ; i < merchantList.size() ; i++){
			channel_map.put(merchantList.get(i).get("merchant_id"), merchantList.get(i).get("merchant_name"));
		}
		request.setAttribute("channel_types", channel_map);
		request.setAttribute("begin_time", begin_time);
		request.setAttribute("end_time", end_time);
		request.setAttribute("timeList1", timeList1);
		request.setAttribute("timeList2", timeList2);
		return "hcStat/tongjiTodayStat";
		}
	
	
	/**
	 * 查询核验
	 */
	@RequestMapping("/tongjiCheck.do")
	public String tongjiCheck(HttpServletResponse response ,HttpServletRequest request){
		String channel = this.getParam(request, "channel");
		String end_time = this.getParam(request, "end_time");
		String days = this.getParam(request, "days");
		Calendar theCa = Calendar.getInstance(); 
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		List<Map<String,Object>> countList=new ArrayList<Map<String,Object>>();
		if(null == MemcachedUtil.getInstance().getAttribute("countList"+channel)){
		Map<String,Object> map=new HashMap<String, Object>();
		int num1=3,num2=7;
		if(!days.isEmpty()){
			num1=Integer.valueOf(days)+1;
			num2=Integer.valueOf(days)+1;
		}
		if(channel.isEmpty()){
		for(int i=0;i<num1;i++){
				map.put("channel", "qunar");
					if(!end_time.isEmpty()){
						Date d = null;
						try {
							d = df.parse(end_time);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						theCa.setTime(d);  
					}else{
						theCa.setTime(new Date()); 
					}
					
				theCa.add(theCa.DATE, -i); 
				Date date = theCa.getTime();
				String querydate=df.format(date);
				map.put("create_time", querydate);
				map.put("code","00");
				int check_num1 = hcStatService.check_num(map);
				map.put("code","11");
				int check_num2 = hcStatService.check_num(map);
				int success_num = hcStatService.success_num(map);
				int fail_num = hcStatService.fail_num(map);
				int order_num = hcStatService.order_num_qunar(map);
				Map<String,Object> map_qunar=new HashMap<String, Object>();
				map_qunar.put("channel", "去哪");
				map_qunar.put("date", querydate);
				map_qunar.put("check_num", check_num1-check_num2);
				map_qunar.put("success_num", success_num);
				map_qunar.put("fail_num", fail_num);
				map_qunar.put("order_num", order_num);
				countList.add(map_qunar);
			}
		for(int i=0;i<num1;i++){
			map.put("channel", "elong");
			if(!end_time.isEmpty()){
				Date d = null;
				try {
					d = df.parse(end_time);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				theCa.setTime(d);  
			}else{
				theCa.setTime(new Date()); 
			}
			theCa.add(theCa.DATE, -i); 
			Date date = theCa.getTime();
			String querydate=df.format(date);
			map.put("create_time", querydate);
			map.put("code","00");
			int check_num1 = hcStatService.check_num(map);
			map.put("code","11");
			int check_num2 = hcStatService.check_num(map);
			int success_num = hcStatService.success_num(map);
			int fail_num = hcStatService.fail_num(map);
			int order_num = hcStatService.order_num_elong(map);
			Map<String,Object> map_elong=new HashMap<String, Object>();
			map_elong.put("channel", "艺龙");
			map_elong.put("date", querydate);
			map_elong.put("check_num", check_num1-check_num2);
			map_elong.put("success_num", success_num);
			map_elong.put("fail_num", fail_num);
			map_elong.put("order_num", order_num);
			countList.add(map_elong);
			}
		for(int i=0;i<num1;i++){
			map.put("channel", "tongcheng");
			if(!end_time.isEmpty()){
				Date d = null;
				try {
					d = df.parse(end_time);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				theCa.setTime(d);  
			}else{
				theCa.setTime(new Date()); 
			}
			theCa.add(theCa.DATE, -i); 
			Date date = theCa.getTime();
			String querydate=df.format(date);
			map.put("create_time", querydate);
			map.put("code","00");
			int check_num1 = hcStatService.check_num(map);
			map.put("code","11");
			int check_num2 = hcStatService.check_num(map);
			int success_num = hcStatService.success_num(map);
			int fail_num = hcStatService.fail_num(map);
			int order_num = hcStatService.order_num_tongcheng(map);
			Map<String,Object> map_tongcheng=new HashMap<String, Object>();
			map_tongcheng.put("channel", "同程");
			map_tongcheng.put("date", querydate);
			map_tongcheng.put("check_num", check_num1-check_num2);
			map_tongcheng.put("success_num", success_num);
			map_tongcheng.put("fail_num", fail_num);
			map_tongcheng.put("order_num", order_num);
			countList.add(map_tongcheng);
			}
		
		//在此新增美团查询接口 add by wangsf
		for(int i=0;i<num1;i++){
			map.put("channel", "meituan");
			if(!end_time.isEmpty()){
				Date d = null;
				try {
					d = df.parse(end_time);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				theCa.setTime(d);  
			}else{
				theCa.setTime(new Date()); 
			}
			theCa.add(theCa.DATE, -i); 
			Date date = theCa.getTime();
			String querydate=df.format(date);
			map.put("create_time", querydate);
			map.put("code","00");
			int check_num1 = hcStatService.check_num(map);
			map.put("code","11");
			int check_num2 = hcStatService.check_num(map);
			int success_num = hcStatService.success_num(map);
			int fail_num = hcStatService.fail_num(map);
			int order_num = hcStatService.order_num_meituan(map);
			Map<String,Object> map_meituan=new HashMap<String, Object>();
			map_meituan.put("channel", "美团");
			map_meituan.put("date", querydate);
			map_meituan.put("check_num", check_num1-check_num2);
			map_meituan.put("success_num", success_num);
			map_meituan.put("fail_num", fail_num);
			map_meituan.put("order_num", order_num);
			countList.add(map_meituan);
			}
		
		//在此新增高铁管家查询接口 add by wangsf
		for(int i=0;i<num1;i++){
			map.put("channel", "301030");
			if(!end_time.isEmpty()){
				Date d = null;
				try {
					d = df.parse(end_time);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				theCa.setTime(d);  
			}else{
				theCa.setTime(new Date()); 
			}
			theCa.add(theCa.DATE, -i); 
			Date date = theCa.getTime();
			String querydate=df.format(date);
			map.put("create_time", querydate);
			map.put("code","00");
			int check_num1 = hcStatService.check_num(map);
			map.put("code","11");
			int check_num2 = hcStatService.check_num(map);
			int success_num = hcStatService.success_num(map);
			int fail_num = hcStatService.fail_num(map);
			int order_num = hcStatService.order_num_gt(map);
			Map<String,Object> map_gt=new HashMap<String, Object>();
			map_gt.put("channel", "高铁管家");
			map_gt.put("date", querydate);
			map_gt.put("check_num", check_num1-check_num2);
			map_gt.put("success_num", success_num);
			map_gt.put("fail_num", fail_num);
			map_gt.put("order_num", order_num);
			countList.add(map_gt);
			}
		}else{
			for(int i=0;i<num2;i++){
				map.put("channel", channel);
				if(!end_time.isEmpty()){
					Date d = null;
					try {
						d = df.parse(end_time);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					theCa.setTime(d);  
				}else{
					theCa.setTime(new Date()); 
				}
				theCa.add(theCa.DATE, -i); 
				Date date = theCa.getTime();
				String querydate=df.format(date);
				map.put("create_time", querydate);
				map.put("code","00");
				int check_num1 = hcStatService.check_num(map);
				map.put("code","11");
				int check_num2 = hcStatService.check_num(map);
				int success_num = hcStatService.success_num(map);
				int fail_num = hcStatService.fail_num(map);
				int order_num = hcStatService.order_num_qunar(map);
				Map<String,Object> map_qunar=new HashMap<String, Object>();
				map_qunar.put("channel", AccountVo.getChannels().get(channel));
				map_qunar.put("date", querydate);
				map_qunar.put("check_num", check_num1-check_num2);
				map_qunar.put("success_num", success_num);
				map_qunar.put("fail_num", fail_num);
				map_qunar.put("order_num", order_num);
				countList.add(map_qunar);
			}
		}
				MemcachedUtil.getInstance().setAttribute("countList"+channel, countList, 60*1000);
		}else{
			countList = (List<Map<String, Object>>) MemcachedUtil.getInstance().getAttribute("countList"+channel);
		}
		request.setAttribute("countList", countList);
		return "hcStat/tongjiCheck";
	}
	
	
	/**
	 * 查询
	 */
	@RequestMapping("/tongji19e.do")
	public String tongji19e(HttpServletResponse response ,HttpServletRequest request){
		Calendar theCa = Calendar.getInstance(); 
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		List<Map<String,Object>> countList=new ArrayList<Map<String,Object>>();
		List<String> channel = new ArrayList<String>();
		channel.add("19e");
		String end_time = this.getParam(request, "end_time");
		if(null == MemcachedUtil.getInstance().getAttribute("19ecountList")){
		for(int i=0;i<8;i++){
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("channel", channel);
			if(!end_time.isEmpty()){
				Date d = null;
				try {
					d = df.parse(end_time);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				theCa.setTime(d);  
			}else{
				theCa.setTime(new Date()); 
			}
			theCa.add(theCa.DATE, -i); 
			Date date = theCa.getTime();
			String querydate=df.format(date);
			map.put("begin_time", querydate);
			map.put("end_time", querydate);
			List<Map<String, String>> reslist= hcStatService.queryHcStatExcel(map);
			System.out.println(" reslist="+reslist.toString());
			Map<String,Object> map_elong=new HashMap<String, Object>();
			map_elong.put("channel", "19e");
			map_elong.put("date", querydate);
			if(!reslist.isEmpty()){
			map_elong.put("active", reslist.get(0).get("active"));
			map_elong.put("agent_login_num", reslist.get(0).get("agent_login_num"));
			}else{
				map_elong.put("active", 0);
				map_elong.put("agent_login_num", 0);
			}
//			map.put("create_time", querydate);
//			int add_user_num =hcStatService.add_user_num(map);
//			int add_regist_num =hcStatService.regist_num(map);
//			map.put("regist_status", "55");
//			int regist_num_sh=hcStatService.regist_num(map);
//			map.put("regist_status", "22");
//			int regist_num=hcStatService.regist_num(map);
//			map.put("regist_status", "33");
//			int regist_num_fail=hcStatService.regist_num(map);
//			map.put("fail_reason", "2");
//			int regist_num_sm=hcStatService.regist_num(map);
//			map.put("fail_reason", "1");
//			int regist_num_error=hcStatService.regist_num(map);
//			
//			map_elong.put("add_user_num", add_user_num);
//			map_elong.put("regist_num_sh", regist_num_sh);
//			map_elong.put("add_regist_num", add_regist_num);
//			map_elong.put("regist_num", regist_num);
//			map_elong.put("regist_num_sm", regist_num_sm);
//			map_elong.put("regist_num_error", regist_num_error);
			
		//	map_elong.put("regist_num_opt", regist_num_opt);
		//	map_elong.put("regist_num_other", regist_num_fail-regist_num_sm-regist_num_error);
			countList.add(map_elong);
		}
		MemcachedUtil.getInstance().setAttribute("19ecountList", countList, 60*1000);
		}else{
			countList = (List<Map<String, Object>>) MemcachedUtil.getInstance().getAttribute("19ecountList");
		}
		request.setAttribute("countList", countList);
		return "hcStat/tongjil9e";
	}
	/**
	 * 验证码统计
	 */
	@RequestMapping("/tongjiCode.do")
	public String tongjiCode(HttpServletResponse response ,HttpServletRequest request){
		List<Map<String,Object>> codeList=new ArrayList<Map<String,Object>>();
		String date = this.getParam(request, "date");
		if(date==null||date==""){
			date = DateUtil.dateToString(new Date(), DateUtil.DATE_FMT1);
		}
		codeList = hcStatService.queryTjCode(date);
		
		
		Map<String ,Map<String ,Object>> resultMap = new HashMap<String ,Map<String ,Object>>();
		List <String> timeList = new ArrayList<String>();
		List <String> alipayList = new ArrayList<String>();
		//时刻
		for(int i=0;i<24;i++){
			String time = i<10?"0"+i:i+"";
			timeList.add(time);
		}
		//支付宝
		for(int i=2;i<32;i++){
			String alipay = i<10?"0"+i:i+"";
			alipayList.add(alipay);
		}
		
		//初始化，避免有的支付宝或某时刻没有记录
		for(int i=2;i<32;i++){
			Map<String,Object> timeValue= new HashMap<String,Object>();
			for(int j=0;j<23;j++){
				timeValue.put(j<10?"0"+j:j+"", 0);
			}
			resultMap.put(i<10?"0"+i:i+"", timeValue);
		}
		//处理数据
		if(codeList!=null && codeList.size()>0){
			for(int i=0;i<codeList.size();i++){
				String card_no = (String) codeList.get(i).get("card_no");
				card_no = card_no.substring(15, 17);
				resultMap.get(card_no).put((String)codeList.get(i).get("time"), codeList.get(i).get("num"));	
			}
		}
		request.setAttribute("date", date);
		request.setAttribute("timeList", timeList);
		request.setAttribute("alipayList", alipayList);
		request.setAttribute("resultMap", resultMap);
		
		return "hcStat/tongjiCode";
	}

}
