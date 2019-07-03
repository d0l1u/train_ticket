package com.l9e.common;

import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

/**
 * 车票预订
 * 
 * @author zhangjun
 *
 */
public class ExternalBase extends BaseController {

	protected static final Logger logger = Logger.getLogger(ExternalBase.class);

	protected static Random random = new Random();

	/**
	 * 获取所有请求参数
	 * 
	 * @param request
	 * @return
	 */
	protected String getFullURL(HttpServletRequest request) {
		StringBuffer params = new StringBuffer();
		@SuppressWarnings("unchecked")
		Map<String, String[]> requestMap = (Map<String, String[]>) request.getParameterMap();
		for (String name : requestMap.keySet()) {
			String[] values = requestMap.get(name);
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < values.length; i++) {
				sb.append(values[i]).append("&");
			}
			params.append(name).append("=").append(sb.toString());
		}

		StringBuffer url = request.getRequestURL();

		if (request.getQueryString() != null) {
			url.append('?');
			url.append(request.getQueryString());
		}
		url.append("##").append(params.toString());

		return url.toString();
	}

	protected String createLogUUID() {
		// 创建日志唯一编号
		String uuid = "[";
		for (int i = 0; i < 6; i++) {
			uuid = uuid + random.nextInt(9);
		}
		uuid = uuid + "] ";
		return uuid;
	}

}
