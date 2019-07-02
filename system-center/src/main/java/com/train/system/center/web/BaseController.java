package com.train.system.center.web;

import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerMapping;

/**
 * BaseController
 *
 * @author taokai3
 * @date 2018/7/9
 */
public class BaseController {

	private Logger logger = LoggerFactory.getLogger(BaseController.class);

	/**
	 * 异常返回值
	 * 
	 * @param request
	 * @param response
	 * @param e
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public String exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {
		logger.info("【 接口异常】:{}", e.getClass().getSimpleName(), e);
		Set<MediaType> mediaTypeSet = new HashSet<>();
		mediaTypeSet.add(new MediaType(MediaType.TEXT_HTML, Charset.forName("UTF-8")));
		request.setAttribute(HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE, mediaTypeSet);

		// JSONObject json = new JSONObject(true);
		// json.put("success", false);
		// json.put("message", "系统异常");
		// json.put("exception", e.getClass().getSimpleName());
		// return json.toJSONString();

		return "false";
	}

	String createLogid() {
		String logid = String.valueOf(System.nanoTime());
		logid = "[" + logid.substring(logid.length() - 6) + "] ";
		return logid;
	}
}
