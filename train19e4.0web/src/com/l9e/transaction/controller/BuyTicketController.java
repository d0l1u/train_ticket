package com.l9e.transaction.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.l9e.common.BaseController;
import com.l9e.common.LoginUserInfo;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.CommonService;
import com.l9e.transaction.service.JoinUsService;
import com.l9e.transaction.service.UserInfoService;
import com.l9e.transaction.vo.OuterSoukdData;
import com.l9e.transaction.vo.ProductVo;
import com.l9e.transaction.vo.RobTicketVo;
import com.l9e.transaction.vo.RobTicket_Notify;
import com.l9e.transaction.vo.SuitVo;
import com.l9e.transaction.vo.TrainData;
import com.l9e.util.DateUtil;
import com.l9e.util.MemcachedUtil;
import com.l9e.util.RobTicketUtils;
 
/**
 * 车票预订
 * @author zhangjun
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
	private JoinUsService  joinUsService;

	@Resource
	private UserInfoService userInfoService;
	
	@Resource
	private CommonService commonService;
	
	/**
	 * 进入预定首页
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/bookIndex.jhtml")
	public String bookIndex(HttpServletRequest request, 
			HttpServletResponse response) {
		//公告
		LoginUserInfo loginUser = this.getLoginUser(request);
		logger.info("@yw-logingUserInfo:=================>>>>>>>"+JSON.toJSONString(loginUser));
		String provinceId = loginUser.getProvinceId();
		String provinceName = commonService.queryProvinceName(provinceId);
		List<Map<String, String>> noticeList = commonService.queryNoticeList(provinceName);
		//是否投诉
		String agentId=loginUser.getAgentId();
		SuitVo suit=commonService.querySuit(agentId);
		request.setAttribute("suitTotalCount",0);//弹窗标记 
		if(suit!=null){
			suit.setGent_id(agentId);
			String suitDate=suit.getCreate_time();//被投诉的最新时间
			//当前时间与最新投诉时间 间隔
			int diff=DateUtil.dateDiff(new Date(), DateUtil.stringToDate(suitDate, DateUtil.DATE_FMT3));
			//数据库投诉信息解析
			int days=suit.getSuit_count()/10;//第几天
			int count=suit.getSuit_count()%10;//第几次
			if(diff<=SuitVo.SUITDAYS&&count<SuitVo.SUITCOUNTS){//在规定投诉天数内  在规定的投诉次数内
				if(diff>days){//不在当天 就更新天数和次数
					count=1;
					suit.setSuit_count(diff*10+count);
				}
				if(diff==days){//当天叠加投诉次数
					suit.setSuit_count(days*10+count+1);
				}
				//更新数据库 投诉天数 次数信息
				commonService.updateSuit(suit);
				request.setAttribute("suitTotalCount",1); 
			}
		}
		
		Map<String, String> paramMap = new HashMap<String, String>();
		String travel_time = DateUtil.dateToString(new Date(), DateUtil.DATE_FMT1);
		//默认乘车日期的匹配（当天或3天）
		String ticketing_time_limit = this.getSysSettingValue("ticketing_time_limit","ticketing_time_limit");
		travel_time = DateUtil.dateAddDays(travel_time,ticketing_time_limit);
		paramMap.put("travel_time", travel_time);//日期设置默认值
		
		/**
		 * 查询是否是第一次进入引导页面start
		 */
		List<Map<String,String>> agentLoginList=userInfoService.queryAgentLogin(loginUser.getAgentId());
		if(agentLoginList!=null && agentLoginList.size()>0){
			//不是第一次登陆
			request.setAttribute("isAgentLogin", "1");
			//是否第一次登录最新改版后的页面：0第一次 1不是第一次
			String is_newLogin = agentLoginList.get(0).get("is_newLogin");
			request.setAttribute("is_newLogin", is_newLogin);
			Map<String,String> map=new HashMap<String,String>();
			map.put("agent_id", loginUser.getAgentId());
			map.put("is_newLogin", "1");
			userInfoService.updateAgentLoginInfo(map);
		}else{
			//第一次登陆
			request.setAttribute("isAgentLogin", "0");
			request.setAttribute("is_newLogin", "1");
			Map<String,String> map=new HashMap<String,String>();
			map.put("agent_id", loginUser.getAgentId());
			map.put("is_verify", "0");
			map.put("is_newLogin", "1");
			userInfoService.addAgentLoginInfo(map);
		}
		
		/**end*/
		
		/**
		 * 开始处理代理商联系人实名数据以及金牌金牌铜牌数据,弹出窗口数据
		 */
		String userId=agentId;
		List<Map<String,String>> userInfoList=joinUsService.queryAgentRegisterInfo(userId);
		Map<String,String> numMap=this.getAgentPersonData(request, userInfoList);
		request.getSession().setAttribute("numMap", numMap);
		/*********end********/
		//预购天数查询
		String book_day_num=commonService.querySysSettingByKey("book_day_num");
		request.setAttribute("book_day_num", book_day_num);
		//查看可不可以预订
		String sys_weather_book=commonService.querySysSettingByKey("sys_weather_book");
		request.setAttribute("sys_weather_book", sys_weather_book);
		String alert_insurance=commonService.queryTrainSysSettingByKey("alert_insurance");
		request.setAttribute("alert_insurance", alert_insurance);
		request.setAttribute("paramMap", paramMap);
		request.setAttribute("noticeList", noticeList); //公告
		return "book/trainHome";
	}
	
	/**
	 * 车票查询入口
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
		//从系统配置中查询最大预购天
		String book_day_num=commonService.querySysSettingByKey("book_day_num");
		//取消 加10天 预约购票服务
		//int book_day_number=Integer.parseInt(book_day_num)+10;
		int book_day_number=Integer.parseInt(book_day_num);
		request.setAttribute("book_day_num", book_day_number);
		List<Map<String, String>>  dayAndWeek =new  ArrayList<Map<String, String>>();
		for(int i=0; i<book_day_number; i++){	
			Map<String, String> map = new HashMap<String, String>();
			String day = DateUtil.getOneMonthDayAndWeek().get(i).get("day");
			String newday = DateUtil.getOneMonthDayAndWeek().get(i).get("newday");
			String week = DateUtil.getOneMonthDayAndWeek().get(i).get("week");
			map.put("day", day);
			map.put("newday", newday);
			map.put("week", week);
			dayAndWeek.add(map);
		}
		
		List<Map<String, String>>  beforeAndAfter4Days =new  ArrayList<Map<String, String>>();
		Date date = DateUtil.stringToDate(travel_time, "yyyy-MM-dd");
		int subDay = DateUtil.dateDiff(date, new Date());//选择的日期和今天相差几天
		if(subDay<4){//选择的日期为今天
			for(int i=0;i<9;i++){
				String someday = DateUtil.dateToString(DateUtil.dateAddDays(new Date(), i), "MM-dd");
				Map<String, String> map = new HashMap<String, String>();
				map.put("days", someday);
				beforeAndAfter4Days.add(map);
			}
		}else if(subDay>=4 && subDay<book_day_number-4){
			for(int i=-4;i<5;i++){
				String someday = DateUtil.dateToString(DateUtil.dateAddDays(date, i), "MM-dd");
				Map<String, String> map = new HashMap<String, String>();
				map.put("days", someday);
				beforeAndAfter4Days.add(map);
			}
		}else if(subDay>=book_day_number-4){
			int n = book_day_number-1-subDay;
			for(int i=n-8;i<=n;i++){
				String someday = DateUtil.dateToString(DateUtil.dateAddDays(date, i), "MM-dd");
//				System.out.println(i+"  "+someday);
				Map<String, String> map = new HashMap<String, String>();
				map.put("days", someday);
				beforeAndAfter4Days.add(map);
			}
		}
//		System.out.println("dayAndWeek:"+dayAndWeek);
//		System.out.println("beforeAndAfter4Days:"+beforeAndAfter4Days);
		request.setAttribute("dayAndWeek", dayAndWeek);
		request.setAttribute("beforeAndAfter4Days", beforeAndAfter4Days);
//		request.setAttribute("dayAndWeek", DateUtil.getOneMonthDayAndWeek());
//		request.setAttribute("beforeAndAfter4Days", DateUtil.getBeforeAndAfter4Days(travel_time));
		request.setAttribute("travelDay", travel_time.substring(5));
		List<String> nodays = new ArrayList<String>();
//		for(int i=0; i<DateUtil.getOneMonthDayAndWeek().size(); i++){
		for(int i=0; i<book_day_number; i++){	
			String day = DateUtil.getOneMonthDayAndWeek().get(i).get("day");
			nodays.add(day);
		}
		for(Map<String, String> dayMap : DateUtil.getBeforeAndAfter4Days(travel_time)){
			String days = dayMap.get("days");
			nodays.remove(days);
		}
		request.setAttribute("nodays", nodays);
//		System.out.println("nodays:"+nodays);
		//设置缓存区余票阀值数
		MemcachedUtil.spareTicket = Integer.valueOf(this.getSysSettingValue("spare_ticket_amount","spare_ticket_amount"));
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("from_city", from_city);
		paramMap.put("to_city", to_city);
		paramMap.put("travel_time", travel_time);
		paramMap.put("gaotie", checkGcd);
		paramMap.put("method", "DGTrain");//调用方法
		logger.info("学生票---begin--");
		String t_type = getParam(request, "t_type");
		request.setAttribute("t_type", t_type);
		if (t_type.equals("0X00")) {
			paramMap.put("purpose_codes", "0X00");
		}
		logger.info("学生票---end--查询参数为"+paramMap);
		/**1:12306接口 2:SOUKD接口**/
		String channel = getSysInterfaceChannel("INTERFACE_CHANNEL");
		
		
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
			if(stopTime>=2 && stopTime<=10){
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
		
		LoginUserInfo loginUser = this.getLoginUser(request);
		if (loginUser==null) {
			loginUser = RobTicketUtils.getTestUser(loginUser);
		}
		String provinceId = loginUser.getProvinceId();
		String cityId = loginUser.getCityId();
		
		//查询保险产品信息
		ProductVo product = new ProductVo();
		product.setProvince_id(provinceId);
		product.setCity_id(cityId);
		product.setType(TrainConsts.PRODUCT_TYPE_1);//保险
		product.setStatus(TrainConsts.PRODUCT_STATUS_1);//上架
		//排队人数以及等待时间
		int amount =Integer.valueOf(this.getSysSettingValue("queuing_tickets_amount", "queuing_tickets_amount"));
		Random random=new Random();
		int wait_amount = amount+random.nextInt(10);
		String hours =this.getSysSettingValue("queuing_tickets_time", "queuing_tickets_time");
		
		List<ProductVo> productList = null;
		//12306允许的售票日：20天内  
		//12306允许的售票日book_day_num天内
		String book_day_num=commonService.querySysSettingByKey("book_day_num");
		int i=Integer.parseInt(book_day_num);
		int j=i-1;
		Date canBookDate = DateUtil.dateAddDays(new Date(),j);
		Date travel_time = DateUtil.stringToDate(travelTime, DateUtil.DATE_FMT1);//出发日期
		if(canBookDate.getTime() >= travel_time.getTime()){//出发日期在20天之内，符合购票规定
			productList = commonService.queryProductInfoList(product);
		}else{//超过book_day_num(20)天的购票日期，属于预约购票
			productList = commonService.queryProductInfoYuyueList(product);
		}
		request.setAttribute("agentId", loginUser.getAgentId());
		request.setAttribute("travelTime", travelTime);
		request.setAttribute("trainCode", trainCode);
		request.setAttribute("trainTypeCn", this.getTrainTypeCn(trainCode));
		request.setAttribute("startCity", startCity);
		request.setAttribute("endCity", endCity);
		request.setAttribute("startTime", startTime);
		request.setAttribute("endTime", endTime);
		request.setAttribute("seatInfoList", seatInfoList);
		request.setAttribute("seatPrizeMapper", JSONObject.fromObject(seatPrizeMap).toString());
		request.setAttribute("productList", productList);
		request.setAttribute("wait_amount", wait_amount);
		request.setAttribute("wait_time", hours);
		
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("user_id", loginUser.getAgentId());
		paramMap.put("link_status", "33");
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
//		System.out.println(jsonList.toString());
		request.setAttribute("jsonLinkList", jsonLimitList);
		request.setAttribute("jsonList", jsonList);
		String ps_order_status = this.getTrainSysSettingValue("ps_order_status", "ps_order_status");
		request.setAttribute("ps_order_status", ps_order_status);
		logger.info("bookInfoInput页面存入 票类型开始");
		String t_type = getParam(request, "t_type");
		request.setAttribute("t_type", t_type);
		logger.info("bookInfoInput页面存入 票类型成功"+t_type);
		return "book/bookInfoInput";
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
				
				//余票小于等于10张则过滤该坐席
				if(StringUtils.isEmpty(yp)){
					continue;
//				}else if(Double.parseDouble(yp)<=10){
//					continue;
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
	 * AJAX异步验证票源是否充足
	 * @param request
	 * @param response
	 */
	@RequestMapping("/checkTicketEnoughAjax.jhtml")
	@ResponseBody
	public void checkTicketEnoughAjax(HttpServletRequest request, 
			HttpServletResponse response){
		try {
/*			String train_no = this.getParam(request, "train_no");
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("from_city", this.getParam(request, "from_city"));
			paramMap.put("to_city", this.getParam(request, "to_city"));
			paramMap.put("travel_time", this.getParam(request, "travel_time"));
			paramMap.put("train_no", train_no);
			
			OuterSoukdData otVo = getDataFromInterface(paramMap, "12306", this.getSysInterface12306Url());
			String yp_key = TrainConsts.getSeatIdYpMap().get(this.getParam(request, "seat_type"));
			
			String result = "no";
			if(otVo != null && otVo.getDataList() != null
					&& otVo.getDataList().size() > 0){
				for(TrainData trainData : otVo.getDataList()){//遍历查询list获取本车次信息
					if(!StringUtils.isEmpty(trainData.getTrainCode())
							&& train_no.equals(trainData.getTrainCode())){
						
						JSONObject jo = JSONObject.fromObject(trainData);
						String yp = (String) jo.get(yp_key);
						
						//余票小于等于10张则过滤该坐席
						if(StringUtils.isEmpty(yp) || "-".equals(yp)){
							result = "no";
						}else if(Double.parseDouble(yp)<=10){
							result = "no";
						}else{
							result = "yes";
						}
						break;
					}
				}
				
			}*/
			String result = "yes";
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html");
			response.getWriter().write(result);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	
	/**
	 * 根据代理商的联系人情况，返回其铜牌金牌银牌数量的map，
	 * 并且request.setAttribute("isCheck","0or1")用于验证弹出来
	 * @param request
	 * @param userInfoList
	 * @return
	 */

	private Map<String,String> getAgentPersonData(HttpServletRequest request,List<Map<String,String>> userInfoList){
		//已通过数量
		int passNum =0;
		//审核中数量
		int waitNum=0;
		//未通过数量
		int noNum=0;
		String passName="";
		String waitName="";
		String noName="";
		if(userInfoList!=null && userInfoList.size()>0){
			//弹窗实名标记
			boolean check=false;
			for(int i=0;i<userInfoList.size();i++){
				String registStatus=userInfoList.get(i).get("regist_status").trim();
				if("22".equals(registStatus)){
					if(passName.length()==0){
						passName=userInfoList.get(i).get("user_name").trim();
					}
					passNum=passNum+1;
					check=true;
				}else if("00".equals(registStatus)|| "11".equals(registStatus)||"55".equals(registStatus)||"44".equals(registStatus)){
					if(waitName.length()==0){
						waitName=userInfoList.get(i).get("user_name").trim();
					}
					waitNum=waitNum+1;
					Date nowDate=new Date();
					Calendar calendar = Calendar.getInstance();	
					calendar.setTime(nowDate);		
					calendar.add(Calendar.DAY_OF_MONTH, -1);
					//得到昨天
					nowDate = calendar.getTime();
					if(nowDate.before(DateUtil.stringToDate(userInfoList.get(i).get("create_time"), "yyyy-MM-dd HH:mm:ss"))){
						check=true;
					}
				}else if("33".equals(registStatus)){
					if(noName.length()==0){
						noName=userInfoList.get(i).get("user_name").trim();
					}
					noNum=noNum+1;
				}
			}
			//已经实名
			if(check==true){
				request.setAttribute("isCheck", "0");
			}else{
				request.setAttribute("isCheck", "1");
			}
		}else{
			//未实名
			request.setAttribute("isCheck", "1");
		}
		
		/**用来判断引导页面。 */
		request.setAttribute("agentPassNum", passNum);
		request.setAttribute("agentWaitNum", waitNum);
		request.setAttribute("agentNoNum", noNum);
		/**end**/
		
		Map<String,String> numMap=new HashMap<String,String>();
		numMap.put("passNum", passNum+"");
		numMap.put("waitNum", waitNum+"");
		numMap.put("noNum", noNum+"");
		numMap.put("passName", passName);
		numMap.put("waitName", waitName);
		numMap.put("noName", noName);
		return numMap;
	}
	
	
	public static void main(String[] args) {
		RobTicket_Notify notify = new RobTicketVo().getNotify();
		notify.setCreateTime(new Date());
		System.out.println(JSON.toJSONString(notify));
		
	}
	
}
