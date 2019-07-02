package com.train.system.center.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.train.system.center.service.HthyService;

/**
 * HthyCallbackController
 *
 * @author taokai3
 * @date 2018/7/9
 */
@Controller
@RequestMapping("/hthy-callback")
public class HthyCallbackController extends BaseController {

	private Logger logger = LoggerFactory.getLogger(HthyCallbackController.class);

	@Resource
	private HthyService hthyService;

	@RequestMapping(value = "/order", method = RequestMethod.POST)
	@ResponseBody
	public String order(@RequestBody JSONObject paramJson) {
		long begin = System.currentTimeMillis();
		String logid = createLogid();
		logger.info("{}-------------【占位回调】-------------", logid);
		logger.info("{}占位结果:{}", logid, paramJson);
		String result = hthyService.order(paramJson, logid);
		logger.info("{}同步响应结果:{},处理耗时:{}", logid, result, (System.currentTimeMillis() - begin));
		return result;
	}

	@RequestMapping(value = "/confirm", method = RequestMethod.POST)
	@ResponseBody
	public String confirm(@RequestParam Map<String, Object> paramMap) {
		long begin = System.currentTimeMillis();
		String logid = createLogid();
		logger.info("{}-------------【出票回调】-------------", logid);
		logger.info("{}出票结果:{}", logid, paramMap);
		String result = hthyService.confirm(paramMap, logid);
		logger.info("{}同步响应结果:{},处理耗时:{}", logid, result, (System.currentTimeMillis() - begin));
		return result;
	}

	@RequestMapping(value = "/refund", method = RequestMethod.POST)
	@ResponseBody
	public String refund(@RequestBody JSONObject paramJson) {
		long begin = System.currentTimeMillis();
		String logid = createLogid();
		logger.info("{}-------------【退票回调】-------------", logid);
		logger.info("{}退票结果:{}", logid, paramJson);
		String result = hthyService.refund(paramJson, logid);
		logger.info("{}同步响应结果:{},处理耗时:{}", logid, result, (System.currentTimeMillis() - begin));
		return result;
	}

	@RequestMapping(value = "/changeOrder", method = RequestMethod.POST)
	@ResponseBody
	public String changeOrder(@RequestParam Map<String, Object> paramMap) {
		long begin = System.currentTimeMillis();
		String logid = createLogid();
		logger.info("{}-------------【改签占位回调】-------------", logid);
		String backjson = paramMap.get("backjson").toString();
		logger.info("{}改签占位结果-BEFORE:{}", logid, backjson);
		try {
			backjson = URLDecoder.decode(backjson, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("{}改签占位结果-AFTER:{}", logid, backjson);
		JSONObject requestJson = JSONObject.parseObject(backjson);
		String result = hthyService.changeOrder(requestJson, logid);
		logger.info("{}同步响应结果:{},处理耗时:{}", logid, result, (System.currentTimeMillis() - begin));
		return result;
	}

	@RequestMapping(value = "/changeCancel", method = RequestMethod.POST)
	@ResponseBody
	public String changeCancel(@RequestBody JSONObject paramJson) {
		long begin = System.currentTimeMillis();
		String logid = createLogid();
		logger.info("{}-------------【改签取消回调】-------------", logid);
		logger.info("{}改签取消结果:{}", logid, paramJson);
		String result = hthyService.refund(paramJson, logid);
		logger.info("{}同步响应结果:{},处理耗时:{}", logid, result, (System.currentTimeMillis() - begin));
		return result;
	}

	@RequestMapping(value = "/changeConfirm", method = RequestMethod.POST)
	@ResponseBody
	public String changeConfirm(@RequestParam Map<String, Object> paramMap) {
		long begin = System.currentTimeMillis();
		String logid = createLogid();
		logger.info("{}-------------【改签出票回调】-------------", logid);
		String backjson = paramMap.get("backjson").toString();
		logger.info("{}改签出票结果-BEFORE:{}", logid, backjson);
		try {
			backjson = URLDecoder.decode(backjson, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		logger.info("{}改签出票结果-AFTER:{}", logid, backjson);
		JSONObject requestJson = JSONObject.parseObject(backjson);
		String result = hthyService.changeConfirm(requestJson, logid);
		logger.info("{}同步响应结果:{},处理耗时:{}", logid, result, (System.currentTimeMillis() - begin));
		return result;
	}
}
