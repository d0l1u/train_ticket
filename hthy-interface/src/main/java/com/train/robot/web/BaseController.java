package com.train.robot.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;

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
	 * @param request
	 * @return String
	 * @Title: getPostParameter
	 * @Description: 获取Post请求体数据(
	 * @author: taokai
	 * @date: 2017年7月19日 下午6:34:06
	 */
	protected String getPostParameter(HttpServletRequest request, String uuid) {
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
				logger.info("{} 【系统异常】:获取Post请求参数发生异常", uuid, e);
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
			logger.info("{} 【系统异常】:Post请求参数转成字符串发生异常", uuid, e);
		}
		return result;
	}

	protected String writeValueAsString(Object object, final String... filterNames) {
		PropertyFilter profilter = new PropertyFilter() {
			@Override
			public boolean apply(Object object, String name, Object value) {
				if (filterNames != null) {
					List<String> nameList = Arrays.asList(filterNames);
					if (nameList.contains(name)) {
						// false表示last字段将被排除在外
						return false;
					}
				}
				return true;
			}
		};
		String parameter = JSONObject.toJSONString(object, profilter, SerializerFeature.SortField);
		return parameter;
	}

	protected String writeValueAsString(Object object, final List<String> filterNames) {
		PropertyFilter profilter = new PropertyFilter() {
			@Override
			public boolean apply(Object object, String name, Object value) {
				if (filterNames != null && !filterNames.isEmpty()) {
					if (filterNames.contains(name)) {
						// false表示last字段将被排除在外
						return false;
					}
				}
				return true;
			}
		};
		String parameter = JSONObject.toJSONString(object, profilter, SerializerFeature.SortField);
		return parameter;
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
	 * 判断字符串是否为 null或者空串
	 *
	 * @param input
	 * @return
	 */
	public boolean isEmpty(String input) {
		return input == null || input.equals("");
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
