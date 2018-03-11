package com.l9e.transaction.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.l9e.common.BaseController;
import com.l9e.transaction.service.ExtRefundService;
import com.l9e.transaction.service.RobTicketService;
import com.l9e.transaction.service.TrainSystemSettingService;
import com.l9e.transaction.vo.RobTicketVo.RobTicket_CP;
import com.l9e.transaction.vo.RobTicketVo.RobTicket_History;
import com.l9e.util.DateUtil;
import com.l9e.util.ExcelUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.JedisUtil;
import com.l9e.util.PageUtil;
import com.l9e.util.PropertyUtil;
import com.l9e.util.RobTicketUtils;

import redis.clients.jedis.Jedis;

/**
 * 19e后台-抢票controller
 * 
 * @author yangwei01
 * 
 */
@Controller
@SuppressWarnings("all")
@RequestMapping("/rob")
public class RobTicketController extends BaseController {
	
	
	private static final int PAGE_SIZE = 10;
	private static final Logger logger = Logger.getLogger(RobTicketController.class);
	private String folder_path = "robticket/";

	@Resource
	private RobTicketService robTicketService;

	@Resource
	private TrainSystemSettingService trainSystemSettingService;

	@Resource
	private ExtRefundService extRefundService;

	@RequestMapping("/queryRobPage.do")
	public String queryRobPage(String[] order_status, String[] channel,HttpServletRequest request, HttpServletResponse response) {
		String form = this.getParam(request, "form");
		String pageIndex = this.getParam(request, "pageIndex");
		if (StringUtils.isEmpty(pageIndex)) {
			pageIndex = "0";
		}
		int page = Integer.parseInt(pageIndex);
		if (page > 0) {
			page = (page - 1) * PAGE_SIZE;
		}
		String order_id = getParam(request, "order_id");
		String begin_info_time = getParam(request, "begin_info_time");
		String end_info_time = getParam(request, "end_info_time");
		String out_ticket_billno = getParam(request, "out_ticket_billno");
		String ctrip_order_id = getParam(request, "ctrip_order_id");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("order_id", order_id);
		map.put("begin_info_time", begin_info_time);
		map.put("end_info_time", end_info_time);
		map.put("out_ticket_billno", out_ticket_billno);
		map.put("orderStatusArr", order_status);
		map.put("channelArr", channel);
		map.put("ctrip_order_id", ctrip_order_id);
		int count = 0;
		map.put("page", page);
		map.put("pageSize", PAGE_SIZE);// 每页显示 10
		List<Map<String, String>> queryRobList = null;
		if (form.equals("1")) {
			count = robTicketService.queryRobListCount(map);
			queryRobList = robTicketService.queryRobList(map);
		}
		PageUtil.getInstance().paging(request, PAGE_SIZE, count);
		request.setAttribute("order_id", order_id);
		request.setAttribute("begin_info_time", begin_info_time);
		request.setAttribute("end_info_time", end_info_time);
		request.setAttribute("out_ticket_billno", out_ticket_billno);
		request.setAttribute("ctrip_order_id", ctrip_order_id);
		request.setAttribute("orders", queryRobList);
		HashMap<String, String> code_china = RobTicketUtils.getCtripSeatMapReverse();
		request.setAttribute("seatMap", code_china);
		HashMap<String, String> statusMap = RobTicketUtils.getOrderStatusMap();
		request.setAttribute("statusMap", statusMap);
		HashMap<String, String> channel_types = RobTicketUtils.getAllChannelMap();
		request.setAttribute("channelStr", Arrays.toString(channel));
		request.setAttribute("statusStr", Arrays.toString(order_status));
		request.setAttribute("channel_types", channel_types);
		return folder_path + "robList";
	}

	@RequestMapping("/ctrip_callback.do")
	public String getRedisData(HttpServletRequest request, HttpServletResponse response) {
		Jedis jedis = null;
		try {
			jedis = JedisUtil.getJedis();
			Map<String, String> hgetAll = jedis.hgetAll("ctrip_callback");
			request.setAttribute("ctrip_callback", hgetAll);
			request.setAttribute("key", "ctrip_callback");
		} catch (Exception e) {
			e.printStackTrace();  
		}finally {
			JedisUtil.returnJedis(jedis);
		}
		return folder_path + "ctrip_callback";
	}
	
	@RequestMapping("/ctrip_callback_yes.do")
	public String getRedisData_2(HttpServletRequest request, HttpServletResponse response) {
		Jedis jedis = null;
		try {
			jedis = JedisUtil.getJedis();
			Map<String, String> hgetAll = jedis.hgetAll("ctrip_callback_yes");
			request.setAttribute("ctrip_callback", hgetAll);
			request.setAttribute("key", "ctrip_callback_yes");
		} catch (Exception e) {
			e.printStackTrace();  
		}finally {
			JedisUtil.returnJedis(jedis);
		}
		return folder_path + "ctrip_callback";
	}

	@RequestMapping("/rediskey.do")
	public String getOrderRedisData(HttpServletRequest request, HttpServletResponse response) {
		Jedis jedis= null;
		try {
			jedis = JedisUtil.getJedis();
			String key = request.getParameter("key");
			Map<String, String> hgetAll2 = jedis.hgetAll(key);
			request.setAttribute("ctrip_callback", hgetAll2);
			request.setAttribute("key", key);
		} catch (Exception e) {
			e.printStackTrace();  
		}finally {
			JedisUtil.returnJedis(jedis);
		}
		return folder_path + "ctrip_callback";
	}

	@RequestMapping("/showHistory.do")
	public void getOrderHistory(HttpServletRequest request, HttpServletResponse response) {
		Jedis jedis = null;
		String orderId = "";
		String ctrip_id = "";
		try {
			orderId = getParam(request, "order_id");
			ctrip_id = getParam(request, "ctrip_id");
			jedis = JedisUtil.getJedis();
			String value = jedis.hget(ctrip_id, "rob_succ_");
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			if(!StringUtils.isEmpty(value)){
				RobTicketUtils.parseRubSuccJson(jsonMap, value);
			}
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("order_id", orderId);
			List<Map<String, String>> histories = robTicketService.queryHistory(paramMap);
			jsonMap.put("histories", histories);
			writeN2Response(response, JSON.toJSONString(jsonMap));
		} catch (Exception e) {
			logger.error("后台抢票显示日志异常,单号("+orderId+")--"+e.toString());
		}finally {
			JedisUtil.returnJedis(jedis);
		}
		
	}

	@RequestMapping("/refundManul.do")
	public void refundTicketManul(HttpServletRequest request, HttpServletResponse response) {
		String cp_id = getParam(request, "cp_id");
		String order_id = getParam(request, "order_id");
		String result = "fail";
		try {
			result = RobTicketUtils.refundManul(robTicketService, cp_id);
			logger.info("人工退票结果-->" + result);
			if (result.indexOf("SUCCESS") != -1) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("order_id", order_id);
				map.put("order_optlog", "携程退票申请HTTP请求成功,系统等待携程退票通知");
				map.put("create_time", new Date());
				map.put("opter", "ctrip-http");
				robTicketService.insertJLHistory(map);
				HashMap<String, String> refund = new HashMap<String, String>();
				refund.put("cp_id", cp_id);
				refund.put("status", RobTicket_CP.REFUND_REQ);
				robTicketService.updateFrontBackCP_Refund(refund);
			}
		} catch (Exception e) {
			result = "系统异常请稍后重试!";
			e.printStackTrace();
		}
		writeN2Response(response, result);
	}
	
	@RequestMapping("/exportexcel.do")
	public String exportExcel(HttpServletRequest request,
			HttpServletResponse response,String[] order_status, String[] channel) {
		return RobTicketUtils.excel(request, response,order_status, channel,robTicketService);
	}
	
	@RequestMapping("/manualDownOrder.do")
	public void manualDownOrder(HttpServletRequest request,HttpServletResponse response){
		String orderId = getParam(request, "orderId");
		String reqUrl  = PropertyUtil.getCtripValue("front_down_order_url")+"?orderId="+orderId;
		logger.info("前台手动下单地址:"+reqUrl);
		String sendByGet = HttpUtil.sendByGet(reqUrl, "UTF-8");
		writeN2Response(response, sendByGet);
	}
	@RequestMapping("/manualEOP.do")
	public void manualEOPRefund(HttpServletRequest request,HttpServletResponse response){
		//http://118.244.193.39:18050/rob/manualEOP.do?orderId=HC_ROB1705271122293130&refundMoney=266&bizType=manual
		String orderId = getParam(request, "orderId");
		String refundMoney = getParam(request, "refundMoney");
		String bizType = getParam(request, "bizType");
		String url = PropertyUtil.getCtripValue("front_down_order_url");
		url = url.replaceAll("manul.do", "eop_refund.do");
		String reqUrl = url + "?orderId="+orderId+"&refundMoney="+refundMoney+"&bizType="+bizType;
		logger.info("前台EOP退款地址地址:"+reqUrl);
		String sendByGet = HttpUtil.sendByGet(reqUrl, "UTF-8");
		writeN2Response(response, sendByGet);
	}
}
