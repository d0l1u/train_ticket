package com.train.robot.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerMapping;

/**
 * @ClassName: BaseController
 * @Description: TODO
 * @author: taokai
 * @date: 2017年7月19日 下午6:29:07
 * @Copyright: 2017 www.19e.cn Inc. All rights reserved.
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

	/**
	 * @param request
	 * @return String
	 * @Title: getPostParameter
	 * @Description: 获取Post请求体数据(
	 * @author: taokai
	 * @date: 2017年7月19日 下午6:34:06
	 */
	protected String getPostParameter(HttpServletRequest request, String logid) {
		int contentLength = request.getContentLength();
		if (contentLength < 0) {
			return null;
		}
		byte buffer[] = new byte[contentLength];
		for (int i = 0; i < contentLength;) {
			int readlen = -1;
			try {
				readlen = request.getInputStream().read(buffer, i, contentLength - i);
			} catch (IOException e) {
				logger.info("{} 【系统异常】:获取Post请求参数发生异常", logid, e);
				return null;
			}
			if (readlen == -1) {
				break;
			}
			i += readlen;
		}
		String charEncoding = request.getCharacterEncoding();
		if (StringUtils.isBlank(charEncoding)) {
			charEncoding = "UTF-8";
		}
		String result = null;
		try {
			result = new String(buffer, charEncoding);
		} catch (UnsupportedEncodingException e) {
			logger.info("{} 【系统异常】:Post请求参数转成字符串发生异常", logid, e);
		}
		return result;
	}

	/**
	 * 响应json字符串
	 *
	 * @param response
	 * @param jsonStr
	 */
	public void printJson(HttpServletResponse response, String jsonStr) {
		logger.info("返回数据：" + jsonStr);
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(jsonStr);
		} catch (IOException e) {
			logger.error("输出异常！", e);
		} finally {
			out.flush();
			out.close();
		}
	}

	/**
	 * 获取post请求参数
	 *
	 * @param request
	 * @return
	 */
	public String getPostParameter(HttpServletRequest request) {
		int contentLength = request.getContentLength();
		if (contentLength < 0) {
			return null;
		}
		byte[] buffer = new byte[contentLength];
		for (int i = 0; i < contentLength;) {
			int readlen = -1;
			try {
				readlen = request.getInputStream().read(buffer, i, contentLength - i);
			} catch (IOException e) {
				logger.info("{}【系统异常】：获取Post请求参数发生异常", e);
				return null;
			}
			if (readlen == -1) {
				break;
			}
			i += readlen;
		}
		String charEncoding = request.getCharacterEncoding();
		if (StringUtils.isBlank(charEncoding)) {
			charEncoding = "UTF-8";
		}
		String result = null;
		try {
			result = new String(buffer, charEncoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	String createLogid() {
		String logid = String.valueOf(System.nanoTime());
		logid = "[" + logid.substring(logid.length() - 6) + "] ";
		return logid;
	}
}
