package com.l9e.common;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class ExceptionHandler implements HandlerExceptionResolver {
	
	private static final Logger logger = Logger.getLogger(ExceptionHandler.class);

	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
//		logger.error("系统全局错误拦截，错误信息", ex);
		logger.info("系统全局错误拦截，错误信息" + ex);
		ex.printStackTrace();
		Map<String,Object> map = new HashMap<String,Object>(); 
		StringPrintWriter strintPrintWriter = new StringPrintWriter();
		ex.printStackTrace(strintPrintWriter);
		map.put("showErr", strintPrintWriter.getString());
		if(ex instanceof MaxUploadSizeExceededException) {
			//request.setAttribute("errMsg", "上传小票大小不能超过2M，请重新上传！");
			map.put("errMsg", "上传小票大小不能超过2M，请重新上传！");
		}
		return new ModelAndView("common/error", map);
		
	}

}
