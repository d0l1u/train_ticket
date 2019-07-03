package com.l9e.transaction.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.transaction.exception.OrderException;
import com.l9e.transaction.service.PassengerService;
import com.l9e.transaction.vo.Passenger;
import com.l9e.transaction.vo.Result;
import com.l9e.util.JacksonUtil;

/**
 * 乘客服务接口
 * 
 * @author licheng
 * 
 */
@Controller("passengerController")
@RequestMapping("/passenger")
public class PassengerController extends BaseController {

	private static final Logger logger = Logger
			.getLogger(PassengerController.class);
	
	@Resource
	private PassengerService passengerService;

	/**
	 * 根据订单id获取乘客信息
	 * @param request
	 * @param response
	 */
	@RequestMapping("/orderPassengers")
	public void getPassengerByOrder(HttpServletRequest request,
			HttpServletResponse response) {

		String orderId = getParam(request, "orderId");
		
		Result result = new Result();
		
		try {
			if(StringUtils.isEmpty(orderId)) {
				logger.info("查询乘客信息，订单号为空");
				throw new OrderException("订单号为空");
			}
			
			List<Passenger> passengers = passengerService.findPassengerByOrder(orderId);
			
			result.setData(passengers);
		} catch (OrderException e) {
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			logger.info("查询乘客信息异常，e : " + e.getMessage());
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
