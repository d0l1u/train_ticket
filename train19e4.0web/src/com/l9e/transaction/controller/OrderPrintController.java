package com.l9e.transaction.controller;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.JoinUsService;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.vo.AgentVo;
import com.l9e.transaction.vo.OrderInfo;

@Controller
@RequestMapping("/print")
public class OrderPrintController extends BaseController {
	
	Logger logger = Logger.getLogger(OrderPrintController.class);
	
	@Resource
	private OrderService orderService;
	
	@Resource
	private JoinUsService joinUsService;
	
	/**
	 * ajax初始化打印发票方式
	 * @throws Exception 
	 */
	@RequestMapping(value="/findPrintSetup_no.jhtml")
	@ResponseBody
	public  String	findPrintSetup(HttpServletRequest request,HttpServletResponse response) throws Exception{
		//登录用户
		//LoginUserInfo userInfo = (LoginUserInfo) request.getSession().getAttribute(TrainConsts.INF_LOGIN_USER);
		String order_id = request.getParameter("order_id");
		Map<String, String> paramMap1 = new HashMap<String, String>();
		paramMap1.put("order_id", order_id);
		paramMap1.put("refund_before_time",  this.getSysSettingValue("before_refundTicket_time", "before_refundTicket_time"));
		paramMap1.put("refund_time",  this.getSysSettingValue("stop_refundTicket_time", "stop_refundTicket_time"));
		OrderInfo orderInfo = orderService.queryOrderInfo2(paramMap1);
		String dealerid = orderInfo.getDealer_id();//代理商ID
		response.setCharacterEncoding("utf-8");
		PrintWriter out;
		logger.info("存在打印发票设置！ ，代理商ID：" + dealerid);
		out = response.getWriter();
		out.write(String.valueOf(0));
		return null;
	}
	
	/**
	 * 获取要打印的数据
	 * @author zuoyx 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/orderView_no.jhtml")
	public String viewOrder(HttpServletRequest request,HttpServletResponse response) throws Exception {
		checkAndPrintOrderInfo(request,response);
		return "/query/orderview";
	}
	
	public void checkAndPrintOrderInfo(HttpServletRequest request,HttpServletResponse response){
		//登录用户
		//LoginUserInfo loginUserInfo = (LoginUserInfo) request.getSession().getAttribute(TrainConsts.INF_LOGIN_USER);
		
		String orderId = request.getParameter("orderId") == null ? "" : request.getParameter("orderId");
		Map<String, String> paramMap1 = new HashMap<String, String>();
		paramMap1.put("order_id", orderId);
		paramMap1.put("refund_before_time",  this.getSysSettingValue("before_refundTicket_time", "before_refundTicket_time"));
		paramMap1.put("refund_time",  this.getSysSettingValue("stop_refundTicket_time", "stop_refundTicket_time"));
		OrderInfo orderInfo = orderService.queryOrderInfo2(paramMap1);
		AgentVo agentInfo = joinUsService.queryAgentInfo(orderInfo.getDealer_id());
		List<Map<String, String>> detailList = orderService.queryOrderDetailList(orderId);
		if (null == orderId || "".equals(orderId)) {
			request.setAttribute("msg", "获取打印数据失败，请重试!");
			logger.info("小票打印,获取订单号参数为空");
			//return "print";
		}
		
		if (null == orderInfo) {
			request.setAttribute("error", "系统暂时忙，请重试!");
			logger.info("打印,根据订单号查询订单信息失败,订单号:" + orderId);
			//return ERROR;
		}
		request.setAttribute("orderInfo", orderInfo);//订单信息
		request.setAttribute("agentInfo", agentInfo);//代理商信息
		request.setAttribute("detailList", detailList);//车票信息
		SimpleDateFormat sdfFirst = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		request.setAttribute("printTime",sdfFirst.format(new Date()));
	}
}
