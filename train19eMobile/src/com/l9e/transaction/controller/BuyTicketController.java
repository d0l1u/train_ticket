package com.l9e.transaction.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jiexun.iface.util.StringUtil;
import com.l9e.common.ExternalBase;
import com.l9e.transaction.service.QueryTicketService;
import com.l9e.transaction.vo.TrainStationVo;

/**
 * 车票预订
 * @author zhangjun
 *
 */
@Controller
@RequestMapping("/buyTicket")
public class BuyTicketController extends ExternalBase {
	
	private static final Logger logger = Logger.getLogger(BuyTicketController.class);
	
	@Resource
	protected NewBuyTicketController newBuyTicketController;
	@Resource
	private SoukdBuyTicketController soukd;
	@Resource
	private QueryTicketService ticketService;
	
	/**
	 * 根据车站查询
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/queryByStation.jhtml")
	public void queryByStation(HttpServletRequest request, 
			HttpServletResponse response){
		
		logger.info("调用对外车票查询接口操作！");
		String from_station = this.getParam(request, "from_station");
		String arrive_station = this.getParam(request, "arrive_station");
		String travel_time = this.getParam(request, "travel_time");
		String terminal = this.getParam(request, "terminal");
		String timestamp = this.getParam(request, "timestamp");
		String version = this.getParam(request, "version");
		JSONObject errReturn = new JSONObject();
		
		if(StringUtil.isEmpty(from_station)||StringUtil.isEmpty(arrive_station)||StringUtil.isEmpty(travel_time)
				||StringUtil.isEmpty(terminal)||StringUtil.isEmpty(timestamp)
				||StringUtil.isEmpty(version)){
			logger.info("输入参数错误或为空。");
			errReturn.put("return_code","PARAM_EMPTY");
			errReturn.put("message","请求的必填参数不能为空！");
			printJson(response,errReturn.toString());
			return;
		}
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("from_station", from_station);
		paramMap.put("arrive_station", arrive_station);
		paramMap.put("travel_time", travel_time);
		paramMap.put("method", "DGTrain");//调用方法
		/**3:12306新接口 2:SOUKD接口**/
		String channel = getSysInterfaceChannel("INTERFACE_CHANNEL");
		String weather_book = getSysInterfaceChannel("sys_weather_book");
		if("0".equals(weather_book)){
			newBuyTicketController.unableBookTicketsQuery(paramMap,request,response);
		}
		if(StringUtils.isEmpty(channel) || "3".equals(channel)){
			newBuyTicketController.newQueryData(paramMap, request, response);
		}else if("2".equals(channel)){
			soukd.soukdQueryData(paramMap,request,response);
		}
	}
	
	
	/**
	 * 
	 * 查询途经站
	 * @param request
	 * @param response
	 */
	@RequestMapping("/queryStopStations.jhtml")
	public void queryStopStations(HttpServletRequest request, HttpServletResponse response){
		logger.info("【途经站】调用途径车站接口操作！");
		String terminal = this.getParam(request, "terminal");
		String timestamp = this.getParam(request, "timestamp");
		String version = this.getParam(request, "version");
		String train_code = this.getParam(request, "train_code");
		
		logger.info("【途经站】terminal="+terminal+",timestamp="+timestamp+",version="+version+",train_code="+train_code);
		
		JSONObject returnJson = new JSONObject();
		if(StringUtil.isEmpty(terminal)||StringUtil.isEmpty(timestamp)
				||StringUtil.isEmpty(version)||StringUtil.isEmpty(train_code)){
			logger.info("【途经站】输入参数错误或为空。");
			returnJson.put("return_code", "PARAM_EMPTY");
			returnJson.put("message", "请求的必填参数不能为空！");
			printJson(response, returnJson.toString());
			return;
		}
		train_code = ticketService.queryTheCheciForStation(train_code);
		List<TrainStationVo> list = ticketService.queryWayStationInfo(train_code);
		
		if(list==null||list.size()==0){
			logger.info("【途经站】暂时没有该途经站信息。");
			returnJson.put("return_code", "FAIL");
			returnJson.put("message", "很抱歉，没有查询到该车次的途经站信息！");
		}else{
			for(TrainStationVo tnv: list){
				tnv.reSetInterval();
			}
			//返回正确结果值
			returnJson.put("return_code", "SUCCESS");
			returnJson.put("message", "查询途经站成功！");
			JSONArray jsonArray = JSONArray.fromObject(list);  
			returnJson.put("stop_stations", jsonArray);
		}
		printJson(response, returnJson.toString());
	}
	
	public static void main(String[] args) {
		try {
			System.out.println(URLDecoder.decode("URL:http://10.12.12.151:8099/RunScript?ScriptPath=12306new.lua&SessionID=1395635973834&Timeout=160000&ParamCount=2&Param1=hcp_730%7C123456a%7Cjjslw1403241236599dd%7C%E7%9B%98%E9%94%A6%7C%E5%A4%A7%E8%BF%9E%E5%8C%97%7C2014-04-07%7CD45%2FD44%7C0&Param2=CP201403241239009341%7C%E5%BE%90%E6%BB%9F%E7%8F%85%7C0%7C2%7C211102198205101062%7C2","utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
