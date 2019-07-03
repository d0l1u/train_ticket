package com.l9e.common;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class ExceptionHandler implements HandlerExceptionResolver {
	
	private static final Logger logger = Logger.getLogger(ExceptionHandler.class);

	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		logger.error("系统全局错误拦截，错误信息", ex);
		
		JSONObject returnJson = new JSONObject();
		if(ex instanceof MaxUploadSizeExceededException) {
			returnJson.put("return_code", "FILE_OUTSIZE");
			returnJson.put("message", "上传文件超过2M！");
		}else{
			returnJson.put("return_code", "SYS_ERROR");
			returnJson.put("message", "系统错误，未知服务异常！");
		}
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			pw.write(returnJson.toString());
			pw.flush();
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}

}
