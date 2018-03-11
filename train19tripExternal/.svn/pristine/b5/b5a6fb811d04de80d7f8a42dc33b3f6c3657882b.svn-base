package com.l9e.common;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class ExceptionHandler extends ExternalBase implements HandlerExceptionResolver {
	
	private static final Logger logger = Logger.getLogger(ExceptionHandler.class);

	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
//		logger.error("系统全局错误拦截，错误信息", ex);
		JSONObject errReturn = new JSONObject();
		logger.info("系统全局错误拦截，错误信息" + ex);
		ex.printStackTrace();
		Map<String,Object> map = new HashMap<String,Object>(); 
		StringPrintWriter strintPrintWriter = new StringPrintWriter();
		ex.printStackTrace(strintPrintWriter);
		map.put("showErr", strintPrintWriter.getString());
		errReturn.put("return_code", "001");
		errReturn.put("message", "系统错误，未知服务异常。");
		printJson(response, errReturn.toString());
		return null;
	}

}
