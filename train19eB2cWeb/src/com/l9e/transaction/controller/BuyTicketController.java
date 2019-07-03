package com.l9e.transaction.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.l9e.common.BaseController;
import com.l9e.common.LoginUserInfo;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.CommonService;
import com.l9e.transaction.service.UserInfoService;
import com.l9e.transaction.vo.OuterSoukdData;
import com.l9e.transaction.vo.TrainData;
import com.l9e.util.DateUtil;
import com.l9e.util.MemcachedUtil;
 
/**
 * 车票预订
 *
 */
@Controller
@RequestMapping("/buyTicket")
public class BuyTicketController extends BaseController {
	
	private static final Logger logger = Logger.getLogger(BuyTicketController.class);
	
	@Resource
	private NewBuyTicketController newBuyTicketController;
	
	@Resource
	private SoukdBuyTicketController soukd;
	
	@Resource
	private CommonService commonService;
	
	@Resource
	private UserInfoService userInfoService;
	
	/**
	 * 进入预定首页
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/bookIndex.jhtml")
	public String bookIndex(HttpServletRequest request, 
			HttpServletResponse response) {
		
		Map<String, String> paramMap = new HashMap<String, String>();
		String travel_time = DateUtil.dateToString(new Date(), DateUtil.DATE_FMT1);
		//默认乘车日期的匹配（当天或3天）
		String ticketing_time_limit = this.getSysSettingValue("ticketing_time_limit","ticketing_time_limit");
		travel_time = DateUtil.dateAddDays(travel_time,ticketing_time_limit);
		paramMap.put("travel_time", travel_time);//日期设置默认值
		
		//热门高铁
		Map<String,List<Map<String,String>>> highTrainList = new HashMap<String,List<Map<String,String>>>();
		List<String> list = new ArrayList<String>();
		list = commonService.queryOftenStation();
		List<Map<String,String>> stationList = new ArrayList<Map<String,String>>();
		for(String station:list){
			stationList = commonService.queryOftenStationInfo(station);
			highTrainList.put(station, stationList);
		}
		request.setAttribute("highTrainList", highTrainList);
		
		request.setAttribute("paramMap", paramMap);
		return "book/trainHome";
	}
	
	/**
	 * 根据车站查询
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/queryByStation.jhtml")
	public String queryByStation(HttpServletRequest request, 
			HttpServletResponse response){
		
		String from_city = this.getParam(request, "from_city");
		String to_city = this.getParam(request, "to_city");
		String travel_time = this.getParam(request, "travel_time");
		String checkGcd = this.getParam(request, "checkGcd");//是否仅选择高铁
		List<String> list = new ArrayList<String>();
		list.add("G");
		list.add("D");
		list.add("C");
		if("on".equals(checkGcd)){
			request.setAttribute("trainType", list);
		}
		
		LoginUserInfo loginUser = this.getLoginUser(request);
		if(loginUser==null || StringUtils.isEmpty(loginUser.getUser_id())){
			request.setAttribute("isLogin", "no");
		}else{
			request.setAttribute("isLogin", "yes");
		}
		
		request.setAttribute("dayAndWeek", DateUtil.getOneMonthDayAndWeek());
		request.setAttribute("beforeAndAfter4Days", DateUtil.getBeforeAndAfter4Days(travel_time));
		request.setAttribute("travelDay", travel_time.substring(5));
		List<String> nodays = new ArrayList<String>();
		for(int i=0; i<DateUtil.getOneMonthDayAndWeek().size(); i++){
			String day = DateUtil.getOneMonthDayAndWeek().get(i).get("day");
			nodays.add(day);
		}
		for(Map<String, String> dayMap : DateUtil.getBeforeAndAfter4Days(travel_time)){
			String days = dayMap.get("days");
			nodays.remove(days);
		}
		request.setAttribute("nodays", nodays);
		//设置缓存区余票阀值数
		MemcachedUtil.spareTicket = Integer.valueOf(this.getSysSettingValue("spare_ticket_amount","spare_ticket_amount"));
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("from_city", from_city);
		paramMap.put("to_city", to_city);
		paramMap.put("travel_time", travel_time);
		paramMap.put("gaotie", checkGcd);
		paramMap.put("method", "DGTrain");//调用方法
		/**1:12306接口 2:SOUKD接口**/
		String channel = getSysInterfaceChannel("INTERFACE_CHANNEL");
		String weather_book = getSysInterfaceChannel("sys_weather_book");
		if("0".equals(weather_book)){
			newBuyTicketController.unableBookTicketsQuery(paramMap,request);
		}
		if(StringUtils.isEmpty(channel) || "3".equals(channel)){
			newBuyTicketController.newQueryData(paramMap, request,response);
		}else if("2".equals(channel)){
			soukd.soukdQueryData(paramMap,request,response);
		}
		return "book/bookIndexNew";
	}
	
	
	
	public OuterSoukdData unableBookTicket(OuterSoukdData otVo,String travle_time) {
		if(travle_time.equals(DateUtil.dateToString(new Date(), DateUtil.DATE_FMT1))){
			//查询系统配置的停止购票时间
			String stopBuyTicketTime=commonService.querySysStopTime();
			logger.info("停止购票时间为："+stopBuyTicketTime);
			int stopTime=0;
			int stopNum=600; //默认600 代表6个小时
			if(stopBuyTicketTime!=null && stopBuyTicketTime.length()>0){
				stopTime=Integer.valueOf(stopBuyTicketTime);
			}
			if(stopTime>=3 && stopTime<=10){
					stopNum=stopTime*100; //如果系统有配置，则使用系统配置
			}
			int currentTime = Integer.parseInt(DateUtil.dateToString(new Date(), "HHmm"));
			if(otVo != null && otVo.getDataList() != null
					&& otVo.getDataList().size()>0){
				for (TrainData trainData : otVo.getDataList()) {
					int beginTime = Integer.parseInt(trainData.getStartTime().replaceAll(":", ""));
					String yp_show = "0";
					if(beginTime-currentTime < stopNum){
						trainData.setWz_yp_show(yp_show);
						trainData.setYz_yp_show(yp_show);
						trainData.setRz_yp_show(yp_show);
						trainData.setRz1_yp_show(yp_show);
						trainData.setRz2_yp_show(yp_show);
						trainData.setYw_yp_show(yp_show);
						trainData.setRw_yp_show(yp_show);
						trainData.setGw_yp_show(yp_show);
						trainData.setTdz_yp_show(yp_show);
						trainData.setSwz_yp_show(yp_show);
						trainData.setCanBook("0");
					}
				}
			}
		}
		return otVo;
	}
	
	/**
	 * 进入预订下单页面
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/gotoBookOrder.jhtml")
	public String gotoBookOrder(HttpServletRequest request, 
			HttpServletResponse response) throws Exception{
/*		//9点-21点可以
		Date date = new Date();
		String datePre = DateUtil.dateToString(date, "yyyyMMdd");		
		Date begin = DateUtil.stringToDate(datePre + "090000", "yyyyMMddHHmmss");//8:00
		Date end = DateUtil.stringToDate(datePre + "210000", "yyyyMMddHHmmss");//22:00
		if(date.before(begin) || date.after(end)){
			String errMsg = "由于节前订票高峰，又遇火车票系统调整，临时订票时间修改为早9点-晚21点，带来的不便敬请谅解！";
			errMsg = URLEncoder.encode(errMsg, "UTF-8");
			return "redirect:/common/goToErrPage.jhtml?errMsg="+errMsg;
		}*/
		String travelTime = this.getParam(request, "travelTime");
		String trainCode = this.getParam(request, "trainCode");
		String startCity = URLDecoder.decode(this.getParam(request, "startCity"), "UTF-8");
		String endCity = URLDecoder.decode(this.getParam(request, "endCity"), "UTF-8");
		String startTime = this.getParam(request, "startTime");
		String endTime = this.getParam(request, "endTime");
		String seatMsg = URLDecoder.decode(this.getParam(request, "seatMsg"), "UTF-8");
		String defaultSelect = this.getParam(request, "defaultSelect");
		
		LinkedList<Map<String, String>> seatInfoList = new LinkedList<Map<String, String>>();
		Map<String, String> seatPrizeMap = new HashMap<String, String>();//座席与价格映射
		
		Map<String, String> seatMap = new HashMap<String, String>();
		seatMap.put("cc", trainCode);
		seatMap.put("fz", startCity);
		seatMap.put("dz", endCity);
		//解析座位信息
		this.deSeatMsg(seatInfoList, seatMsg, seatPrizeMap, defaultSelect, seatMap);
		
		//查询保险产品信息
//		ProductVo product = new ProductVo();
//		product.setType(TrainConsts.PRODUCT_TYPE_1);//保险
//		product.setStatus(TrainConsts.PRODUCT_STATUS_1);//上架
		//排队人数以及等待时间
//		int amount =Integer.valueOf(this.getSysSettingValue("queuing_tickets_amount", "queuing_tickets_amount"));
//		Random random=new Random();
//		int wait_amount = amount+random.nextInt(10);
//		String hours =this.getSysSettingValue("queuing_tickets_time", "queuing_tickets_time");
		
//		List<ProductVo> productList = null;
//		//12306允许的售票日：20天内  
//		Date canBookDate = DateUtil.dateAddDays(new Date(),19);
//		Date travel_time = DateUtil.stringToDate(travelTime, DateUtil.DATE_FMT1);//出发日期
//		if(canBookDate.getTime() >= travel_time.getTime()){//出发日期在20天之内，符合购票规定
//			productList = commonService.queryProductInfoList(product);
//		}else{//超过20天的购票日期，属于预约购票
//			productList = commonService.queryProductInfoYuyueList(product);
//		}
		request.setAttribute("travelTime", travelTime);
		request.setAttribute("trainCode", trainCode);
		request.setAttribute("trainTypeCn", this.getTrainTypeCn(trainCode));
		request.setAttribute("startCity", startCity);
		request.setAttribute("endCity", endCity);
		request.setAttribute("startTime", startTime);
		request.setAttribute("endTime", endTime);
		request.setAttribute("seatInfoList", seatInfoList);
		request.setAttribute("seatPrizeMapper", JSONObject.fromObject(seatPrizeMap).toString());
//		request.setAttribute("productList", productList);
//		request.setAttribute("wait_amount", wait_amount);
//		request.setAttribute("wait_time", hours);
		
		LoginUserInfo loginUser = this.getLoginUser(request);
		if(loginUser==null || StringUtils.isEmpty(loginUser.getUser_id())){//若用户没有登录
			request.setAttribute("jsonLinkList", "");
			request.setAttribute("jsonList", "");
		}else{//用户已经登录
			//用户的常用旅客信息
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("user_id", loginUser.getUser_id());
			paramMap.put("verify_status", "11");//核验已通过
			List<Map<String,String>> linkList = userInfoService.queryLinkInfoList(paramMap);
			paramMap.put("limit", 49);
			List<Map<String,String>> limitlinkList = userInfoService.queryLinkInfoList(paramMap);
			JSONArray jsonLimitList = new JSONArray();
			JSONArray jsonList = new JSONArray();
			for(Map<String,String> map:linkList){
				 JSONObject json = new JSONObject();
				 json.put("link_name", map.get("link_name"));
				 json.put("ids_type", map.get("ids_type"));
				 json.put("user_ids", map.get("user_ids"));
				 json.put("ticket_type", "0");
				 jsonList.add(json);
			}
			
			for(Map<String,String> map:limitlinkList){
				 JSONObject json = new JSONObject();
				 json.put("link_name", map.get("link_name"));
				 json.put("ids_type", map.get("ids_type"));
				 json.put("user_ids", map.get("user_ids"));
				 json.put("ticket_type", "0");
				 jsonLimitList.add(json);
			}
			System.out.println(jsonList.toString());
			request.setAttribute("jsonLinkList", jsonLimitList);
			request.setAttribute("jsonList", jsonList);
			//用户的常用收货地址信息
//			Map<String, String> param = new HashMap<String, String>();
//			param.put("user_id", loginUser.getUser_id());
//			param.put("limit", "1");
//			//param.put("addressee", addressee);//收件人姓名
//			List<Map<String, String>> addressList = userInfoService.queryAddressList(param);
//			request.setAttribute("addressList", addressList);
		}
		
		//省市区三级联动
		request.setAttribute("province", commonService.getProvince());
		return "book/bookInfoInput";
	}
	
	@RequestMapping("/queryGetCity.jhtml")
	@ResponseBody
	public String getCity(String provinceid,HttpServletRequest request,
			HttpServletResponse response){
		List<Map<String, String>> list = commonService.getCity(provinceid);
		ObjectMapper map = new ObjectMapper();
		try {
			map.writeValue(response.getOutputStream(), list);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping("/queryGetArea.jhtml")
	@ResponseBody
	public String getArea(String cityid,HttpServletRequest request,
			HttpServletResponse response){
		List<Map<String, String>> list = commonService.getArea(cityid);
		ObjectMapper map = new ObjectMapper();
		try {
			map.writeValue(response.getOutputStream(), list);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 解析座位信息
	 * @param seatInfoList
	 * @param seatMsg
	 */
	private void deSeatMsg(LinkedList<Map<String, String>> seatInfoList, String seatMsg, 
			Map<String, String> seatPrizeMap, String defaultSelect, Map<String, String> seatMap){
		Map<String, String> seatMoneyMap = commonService.querySeatMap(seatMap);//根据车次cc、发站fz、到站dz查询各种坐席的票价
		String[] seats = seatMsg.split(",");
		for (String seat : seats) {
			String[] element = seat.split("_");
			String price = element[1].trim();
			String yp = element[2].trim();
			if(!StringUtils.isEmpty(price) && !"-".equals(price) && !"-".equals(yp)){//有该类别坐席
				
				if(StringUtils.isEmpty(yp)){
					continue;
				}
				Map<String, String> map = new HashMap<String, String>(3);
				
				String seatType = null;
				//根据坐席名称取得坐席ID
				for(Map.Entry<String, String> entry : TrainConsts.getSeatType().entrySet()){
					if(!StringUtils.isEmpty(element[0]) && entry.getValue().equals(element[0])){
						seatType = entry.getKey();
					}
				}
				
				map.put("seatName", element[0]);
				map.put("seatType", seatType);
				if(seatType!=null){
					if(seatType.equals("10")){
						logger.info("其他坐席，数据库无数据，使用页面传值："+price);
					}else{
						String priceDb = seatMoneyMap.get("seat"+seatType);
						if(!price.equals(priceDb)){
							if(Float.parseFloat(priceDb)>=Float.parseFloat(price)){
								logger.info("【seatType"+seatType+"="+priceDb+"】数据库票价>=页面票价，数据库票价priceDb="+priceDb+",页面传票价price="+price);
								price = priceDb;
							}else{
								logger.info("【seatType"+seatType+"="+price+"】数据库票价<页面票价，数据库票价priceDb="+priceDb+",页面传票价price="+price);
							}
						}
					}
				}
				map.put("price", price);
				map.put("yp", element[2]);
				if(defaultSelect.equals(element[0])){
					map.put("seatSelect", "select");
					seatInfoList.addFirst(map);
				}else{
					map.put("seatSelect", "unSelect");
					seatInfoList.add(map);
				}
				seatPrizeMap.put("seatType"+seatType, price);//坐席价格映射
			}
		}
		logger.info("解析座位信息:"+seatPrizeMap);
		/*********end***********/
	}
	
	
	/**
	 * 获取系统接口频道
	 * @param provinceId
	 * @return
	 */
	private String getSysInterfaceChannel(String key){
		String channel = null;//1:12306接口 2:SOUKD接口,3:新接口
		if(null == MemcachedUtil.getInstance().getAttribute(key)){
			channel = commonService.querySysSettingByKey(key);
			MemcachedUtil.getInstance().setAttribute(key, channel, 60*1000);
			
		}else{
			channel = (String) MemcachedUtil.getInstance().getAttribute(key);
		}
		return channel;
	}
	
	
}
