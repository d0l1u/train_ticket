package com.l9e.transaction.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.l9e.common.BuyTicketBase;
import com.l9e.common.LoginUserInfo;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.QueryTicketService;
import com.l9e.transaction.vo.Outer12306Data;
import com.l9e.transaction.vo.OuterSoukdData;
import com.l9e.transaction.vo.ProductVo;
import com.l9e.transaction.vo.TrainData;
import com.l9e.util.DateUtil;
import com.l9e.util.FileUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.MemcachedUtil;

/**
 * 车票预订
 * @author zhangjun
 *
 */
@Controller
@RequestMapping("/buyTicket")
public class BuyTicketController extends BuyTicketBase {
	
	@Resource
	protected NewBuyTicketController newBuyTicketController;
	@Resource
	private SoukdBuyTicketController soukd;
	@Resource
	private QueryTicketService queryTicketService;
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
		request.setAttribute("noticeList", noticeList); //公告
		
		Map<String,List<Map<String,String>>> highTrainList = new HashMap<String,List<Map<String,String>>>();
		List<String> list = new ArrayList<String>();
		list = queryTicketService.queryOftenStation();
		List<Map<String,String>> stationList = new ArrayList<Map<String,String>>();
		for(String station:list){
			stationList = queryTicketService.queryOftenStationInfo(station);
			highTrainList.put(station, stationList);
		}
		request.setAttribute("highTrainList", highTrainList);
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
		String costTime = this.getParam(request, "costTime");
		String seatMsg = URLDecoder.decode(this.getParam(request, "seatMsg"), "UTF-8");
		
		List<Map<String, String>> seatInfoList = new ArrayList<Map<String, String>>();
		Map<String, String> seatPrizeMap = new HashMap<String, String>();//座席与价格映射
		//解析座位信息
		this.deSeatMsg(seatInfoList, seatMsg, seatPrizeMap);
		
		
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
		request.setAttribute("costTime", costTime);
		request.setAttribute("seatInfoList", seatInfoList);
		request.setAttribute("seatPrizeMapper", JSONObject.fromObject(seatPrizeMap).toString());
		request.setAttribute("productList", productList);
		request.setAttribute("wait_amount", wait_amount);
		request.setAttribute("wait_time", hours);
		
		return "book/bookInfoInput";
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