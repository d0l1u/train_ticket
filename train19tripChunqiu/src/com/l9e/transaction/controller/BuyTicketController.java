package com.l9e.transaction.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.l9e.common.BuyTicketBase;
import com.l9e.common.LoginUserInfo;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.QueryTicketService;
import com.l9e.transaction.service.UserInfoService;
import com.l9e.transaction.vo.OuterSoukdData;
import com.l9e.transaction.vo.ProductVo;
import com.l9e.transaction.vo.TrainData;
import com.l9e.util.DateUtil;
import com.l9e.util.MemcachedUtil;

/**
 * 车票预订
 * @author zhangjun
 *
 */
@Controller
@RequestMapping("/chunqiu/buyTicket")
public class BuyTicketController extends BuyTicketBase {
	
	@Resource
	protected NewBuyTicketController newBuyTicketController;
	@Resource
	private SoukdBuyTicketController soukd;
	@Resource
	private QueryTicketService queryTicketService;
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
		//公告
		List<Map<String, String>> noticeList = commonService.queryNoticeList();
		
		Map<String, String> paramMap = new HashMap<String, String>();
		String travel_time = DateUtil.dateToString(new Date(), DateUtil.DATE_FMT1);
		//默认乘车日期的匹配（当天或3天）
		String ticketing_time_limit = this.getSysSettingValue("ticketing_time_limit","ticketing_time_limit");
		travel_time = DateUtil.dateAddDays(travel_time,ticketing_time_limit);
		paramMap.put("travel_time", travel_time);//日期设置默认值
		request.setAttribute("paramMap", paramMap);
		if(noticeList.size()!=0){
			request.setAttribute("noticeList", noticeList); //公告
		}
		
		Map<String,List<Map<String,String>>> highTrainList = new HashMap<String,List<Map<String,String>>>();
		List<String> list = new ArrayList<String>();
		list = queryTicketService.queryOftenStation();
		List<Map<String,String>> stationList = new ArrayList<Map<String,String>>();
		for(String station:list){
			stationList = queryTicketService.queryOftenStationInfo(station);
			highTrainList.put(station, stationList);
		}
		request.setAttribute("highTrainList", highTrainList);
		//预购天数查询
		String book_day_num=commonService.querySysSettingByKey("inner_book_day_num");
		int i=Integer.parseInt(book_day_num);
		int j=i-1;
		request.setAttribute("book_day_num", j);

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

		String from_station = this.getParam(request, "from_station");
		String arrive_station = this.getParam(request, "arrive_station");
		String travel_time = this.getParam(request, "travel_time");
		
		LoginUserInfo loginUser = this.getLoginUser(request);
		String user_id = loginUser.getUserId();
		logger.info("【当前登录春秋用户--预定】user_id:"+user_id);
		if(user_id.equals("f513b9dea8a856572e690b0552bbc431")){
			request.setAttribute("isLogin", "false");
		}else{
			request.setAttribute("isLogin", "true");
		}
		//设置缓存区余票阀值数
		MemcachedUtil.spareTicket = Integer.valueOf(this.getSysSettingValue("spare_ticket_amount","spare_ticket_amount"));
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("from_station", from_station);
		paramMap.put("arrive_station", arrive_station);
		paramMap.put("travel_time", travel_time);
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
		
		//预购天数查询
		String book_day_num=commonService.querySysSettingByKey("inner_book_day_num");
		int i=Integer.parseInt(book_day_num);
		int j=i-1;
		request.setAttribute("book_day_num", j);

		return "book/bookIndexNew";
	}
	
	
	
	//处理otVo对象在travel_time时间之内的票不能订购
	public OuterSoukdData unableBookTicket(OuterSoukdData otVo,String travle_time) {
		if(travle_time.equals(DateUtil.dateToString(new Date(), DateUtil.DATE_FMT1))){
			int currentTime = Integer.parseInt(DateUtil.dateToString(new Date(), "HHmm"));
			if(otVo != null && otVo.getDataList() != null
					&& otVo.getDataList().size()>0){
				for (TrainData trainData : otVo.getDataList()) {
					int beginTime = Integer.parseInt(trainData.getStartTime().replaceAll(":", ""));
					String yp_show = "无";
					if(beginTime-currentTime < 600){
						trainData.setWz_yp_show(yp_show);
						trainData.setYz_yp_show(yp_show);
						trainData.setRz1_yp_show(yp_show);
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
	 * 异步登陆
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/putSession.jhtml")
	public void putSession(HttpServletRequest request, 
			HttpServletResponse response) throws Exception{
		LoginUserInfo loginUser = this.getLoginUser(request);
		String userId = loginUser.getUserId();
		if(userId.equals("f513b9dea8a856572e690b0552bbc431") || userId.equals("null")){
			String user_id = this.getParam(request, "user_id");
			if(StringUtils.isNotEmpty(user_id) && !"null".equals(user_id)){
				loginUser.setUserId(user_id);
				logger.info("【游客登录春秋--异步】成功user_id:"+user_id);
				//用户信息放入session
				request.getSession().setAttribute(TrainConsts.INF_LOGIN_USER, loginUser);
			}else{
				request.getSession().setAttribute(TrainConsts.INF_LOGIN_USER, null);
			}
		}
		userId = loginUser.getUserId();
		logger.info("【当前登录春秋用户--异步】user_id:"+userId);
		if("f513b9dea8a856572e690b0552bbc431".equals(userId) || "null".equals(userId)){
			request.setAttribute("isLogin", "false");//未登录
		}else{
			request.setAttribute("isLogin", "true");//已登录
		}
		write2Response(response,"true");
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
		
		LoginUserInfo loginUser = this.getLoginUser(request);
		String userId = loginUser.getUserId();
		logger.info("【游客登录春秋--预定】user_id:"+userId);
		if(userId.equals("f513b9dea8a856572e690b0552bbc431") || userId.equals("null")){
			String user_id = this.getParam(request, "user_id");
			if(StringUtils.isNotEmpty(user_id) && !"null".equals(user_id)){
				loginUser.setUserId(user_id);
				logger.info("【游客登录春秋--预定】成功user_id:"+user_id);
				//用户信息放入session
				request.getSession().setAttribute(TrainConsts.INF_LOGIN_USER, loginUser);
			}else{
				request.getSession().setAttribute(TrainConsts.INF_LOGIN_USER, null);
			}
		}
		
		
		LinkedList<Map<String, String>> seatInfoList = new LinkedList<Map<String, String>>();
		Map<String, String> seatPrizeMap = new HashMap<String, String>();//座席与价格映射
		//解析座位信息
		this.deSeatMsg(seatInfoList, seatMsg, seatPrizeMap, defaultSelect);
		
		//查询保险产品信息
		ProductVo product = new ProductVo();
		product.setType(TrainConsts.PRODUCT_TYPE_1);//保险
		product.setStatus(TrainConsts.PRODUCT_STATUS_1);//上架
		//排队人数以及等待时间
		int amount =Integer.valueOf(this.getSysSettingValue("queuing_tickets_amount", "queuing_tickets_amount"));
		Random random=new Random();
		int wait_amount = amount+random.nextInt(10);
		String hours =this.getSysSettingValue("queuing_tickets_time", "queuing_tickets_time");
		
		List<ProductVo> productList = commonService.queryProductInfoList(product);
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
		paramMap.put("user_id", loginUser.getUserId());
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
		System.out.println(jsonList.toString());
		request.setAttribute("jsonLinkList", jsonLimitList);
		request.setAttribute("jsonList", jsonList);
		
		return "book/bookInfoInput";
	}
	
	/**
	 * 解析座位信息
	 * @param seatInfoList
	 * @param seatMsg
	 */
	private void deSeatMsg(LinkedList<Map<String, String>> seatInfoList, String seatMsg, 
			Map<String, String> seatPrizeMap, String defaultSelect){
		/********yangchao 修改成默认选中的放在第一位，解决ie6 option无法选中的问题*******/
		String[] seats = seatMsg.split(",");
		for (String seat : seats) {
			String[] element = seat.split("_");
			String price = element[1].trim();
			String yp = element[2].trim();
			if(!StringUtils.isEmpty(price) && !"-".equals(price) && !"-".equals(yp)){//有该类别坐席
				
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
				map.put("price", element[1]);
				map.put("yp", element[2]);
				if(defaultSelect.equals(element[0])){
					map.put("seatSelect", "select");
					seatInfoList.addFirst(map);
				}else{
					map.put("seatSelect", "unSelect");
					seatInfoList.add(map);
				}
				seatPrizeMap.put("seatType"+seatType, element[1]);//坐席价格映射
			}
		}
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
}
