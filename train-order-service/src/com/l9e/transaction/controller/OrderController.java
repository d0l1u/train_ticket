package com.l9e.transaction.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.transaction.exception.OrderException;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.vo.Order;
import com.l9e.transaction.vo.Result;
import com.l9e.util.JacksonUtil;

/**
 * 订单服务接口
 * 
 * @author licheng
 * 
 */
@Controller("orderController")
@RequestMapping("/order")
public class OrderController extends BaseController {

	private static final Logger logger = Logger
			.getLogger(OrderController.class);
	
	@Resource
	private OrderService orderService;

	/**
	 * 修改订单
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/modifyOrder")
	public void updateOrder(HttpServletRequest request,HttpServletResponse response) {
		/*
		 * order json 
		 * {
		 * 		"id" : "TGT_SF95536AC6E0B158E",
		 * 		"payMoney" : 105.00,
		 * 		"buyMoney" : 105.00,
		 * 		"status" : "99",
		 * 		"payTime" : "2016-01-21 15:02:09",
		 * 		"outTicketTime" : "2016-01-21 14:58:24",
		 * 		"billno" : "EA39575512",
		 * 		"fromTime" : "2016-01-28 13:12:00",
		 * 		"toTime" : "2016-01-28 21:39:00",
		 * 		"accountId" : 980869,
		 * 		"workerId" : 768,
		 * 		"payAccount" : "huochepiao19e18@163.com",
		 * 		"paySeq" : "W2016012164788942",
		 * 		"errorInfo" : "6"
		 * }
		 */
		
		String remoteAddr = request.getRemoteAddr();
		logger.info("访问IP:"+remoteAddr);
		
		String orderJson = getParam(request, "order");
		
		Result result = new Result();

		try {
			if(StringUtils.isEmpty(orderJson)) {
				logger.info("修改订单json参数为空");
				throw new OrderException("订单参数为空");
			}
			
			Order order = null;
			try {
				order = JacksonUtil.readJson(orderJson, Order.class);
			} catch (Exception e) {
				logger.info("订单json串解析异常，e : " + e.getMessage());
				throw new OrderException("订单参数解析异常");
			}
			if(order != null) {
				if(orderService.updateOrder(order)) {
					logger.info("修改订单成功, order : " + order);
				} else {
					logger.info("修改订单失败，订单可能不存在，order : " + order);
					throw new OrderException("订单可能不存在");
				}
			} else {
				logger.info("订单信息有误，不作修改。orderJson : " + orderJson);
				throw new OrderException("订单信息有误，不作修改");
			}
		} catch (OrderException e) {
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			logger.info("修改订单信息异常，e : " + e.getMessage());
			result.setMsg("系统异常");
			result.setSuccess(false);
			e.printStackTrace();
		}

		try {
			printJson(response, JacksonUtil.generateJson(result));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
