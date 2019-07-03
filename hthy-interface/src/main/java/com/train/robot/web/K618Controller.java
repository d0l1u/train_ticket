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
		logger.info("{}占位下单,参数:{}", logid, paramJson);
		HthyRequestService hthyRequest = new HthyRequestService(logid);
		return hthyRequest.trainOrder(paramJson);
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
		logger.info("{}取消占位:{}-{}", logid, orderId, supplierId);
		HthyRequestService hthyRequest = new HthyRequestService(logid);
		return hthyRequest.cancel(orderId, supplierId);
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
		logger.info("{}确认占位:{}-{}", logid, orderId, supplierId);
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
		logger.info("{}线上退票,参数:{}", logid, paramJson);
		HthyRequestService hthyRequest = new HthyRequestService(logid);
		return hthyRequest.refund(paramJson);
	}

	@RequestMapping(value = "/change", method = RequestMethod.POST)
	@ResponseBody
	public String change(@RequestParam JSONObject paramJson) {
		String logid = createLogid();
		logger.info("{}改签占位,参数:{}", logid, paramJson);
		HthyRequestService hthyRequest = new HthyRequestService(logid);
		return hthyRequest.refund(paramJson);
	}

}
