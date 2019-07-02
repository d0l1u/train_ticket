package com.train.robot.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.train.robot.service.HthyRequestService;

import java.util.Random;

/**
 * K618Controller
 *
 * @author taokai3
 * @date 2018/7/4
 */
@Controller
@RequestMapping("/k618")
public class K618Controller extends BaseController {

	private Logger logger = LoggerFactory.getLogger(K618Controller.class);

	/**
	 * 占位下单
	 *
	 * @param paramJson
	 * @return
	 */
	@RequestMapping(value = "/order", method = RequestMethod.POST)
	@ResponseBody
	public String order(@RequestBody JSONObject paramJson) {
		String logid = createLogid();
		logger.info("{}-------------【请求占位】-------------", logid);
		logger.info("{}system参数:{}", logid, paramJson);
		HthyRequestService hthyRequest = new HthyRequestService(logid);
		String result = hthyRequest.order(paramJson);
		return result;
	}

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	@ResponseBody
	public String test() {
		String logid = createLogid();
		logger.info("{}-------------【请求占位】-------------", logid);
		try {
			Thread.sleep(2000 + new Random().nextInt(3000));
		}catch (Exception e){
		}
		logger.info("{}测试李凯昊",logid);
		return "success->kaihao-test";
	}

	/**
	 * 取消占位下单
	 *
	 * @param orderId
	 * @param supplierId
	 * @return
	 */
	@RequestMapping(value = "/cancel/{orderId}/{supplierId}", method = RequestMethod.GET)
	@ResponseBody
	public String cancel(@PathVariable("orderId") String orderId, @PathVariable("supplierId") String supplierId) {
		String logid = createLogid();
		logger.info("{}-------------【请求取消】-------------", logid);
		logger.info("{}system参数:{}-{}", logid, orderId, supplierId);
		HthyRequestService hthyRequest = new HthyRequestService(logid);
		String result = hthyRequest.cancel(orderId, supplierId);
		return result;
	}

	/**
	 * 确认占位
	 *
	 * @param orderId
	 * @param supplierId
	 * @return
	 */
	@RequestMapping(value = "/confirm/{orderId}/{supplierId}", method = RequestMethod.GET)
	@ResponseBody
	public String confirm(@PathVariable("orderId") String orderId, @PathVariable("supplierId") String supplierId) {
		String logid = createLogid();
		logger.info("{}-------------【请求出票】-------------", logid);
		logger.info("{}system参数:{}-{}", logid, orderId, supplierId);
		HthyRequestService hthyRequest = new HthyRequestService(logid);
		return hthyRequest.confirm(orderId, supplierId);
	}

	/**
	 * 退票
	 *
	 * @param paramJson
	 * @return
	 */
	@RequestMapping(value = "/refund", method = RequestMethod.POST)
	@ResponseBody
	public String refund(@RequestBody JSONObject paramJson) {
		String logid = createLogid();
		logger.info("{}-------------【请求退票】-------------", logid);
		logger.info("{}system参数:{}", logid, paramJson);
		HthyRequestService hthyRequest = new HthyRequestService(logid);
		return hthyRequest.refund(paramJson);
	}

	@RequestMapping(value = "/changeOrder", method = RequestMethod.POST)
	@ResponseBody
	public String changeOrder(@RequestBody JSONObject paramJson) {
		String logid = createLogid();
		logger.info("{}-------------【请求改签占位】-------------", logid);
		logger.info("{}system参数:{}", logid, paramJson);
		HthyRequestService hthyRequest = new HthyRequestService(logid);
		return hthyRequest.changeOrder(paramJson);
	}

	@RequestMapping(value = "/changeCancel/{orderId}/{supplierId}", method = RequestMethod.GET)
	@ResponseBody
	public String changeCancel(@PathVariable("orderId") String orderId, @PathVariable("supplierId") String supplierId) {
		String logid = createLogid();
		logger.info("{}-------------【请求改签取消】-------------", logid);
		logger.info("{}system参数-orderId:{},supplierId:{}", logid, orderId, supplierId);
		HthyRequestService hthyRequest = new HthyRequestService(logid);
		return hthyRequest.changeCancel(orderId, supplierId);
	}

	@RequestMapping(value = "/changeConfirm/{orderId}/{supplierId}/{changeId}", method = RequestMethod.GET)
	@ResponseBody
	public String changeConfirm(@PathVariable("orderId") String orderId, @PathVariable("supplierId") String supplierId, @PathVariable("changeId") Integer changeId) {
		String logid = createLogid();
		logger.info("{}-------------【请求改签出票】-------------", logid);
		logger.info("{}system参数-orderId:{},supplierId:{},changeId:{}", logid, orderId, supplierId,changeId);
		HthyRequestService hthyRequest = new HthyRequestService(logid);
		return hthyRequest.changeConfirm(orderId, supplierId,changeId);
	}

}
