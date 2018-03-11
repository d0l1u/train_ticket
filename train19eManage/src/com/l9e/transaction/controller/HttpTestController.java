package com.l9e.transaction.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;


@Controller
@RequestMapping("/httpTest")
public class HttpTestController extends BaseController{
	protected static final Logger logger = Logger.getLogger(HttpTestController.class);

	@RequestMapping("/get.do")
	public void getParams(HttpServletRequest request,
			HttpServletResponse response,@RequestBody String json){
		
		logger.info(json);
			
		logger.info(getFullURL(request));
		
		logger.info("str:"+request.getParameter("str"));
		logger.info("sss:"+request.getParameter("sss"));
		/*try {
			logger.info("answer:"+URLDecoder.decode(request.getParameter("str"), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		*/
		try {
			logger.info("json2:"+URLDecoder.decode(json, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		writeN2Response(response, "SUCCESS");
			
	}
	
	 /**
	  * 获取所有请求参数
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public  String getFullURL(HttpServletRequest request) {
	    	
	    	StringBuffer params = new StringBuffer();
	    	 Map<String,String[]> map1 = (Map<String,String[]>)request.getParameterMap();  
	         for(String name:map1.keySet()){  
	             String[] values = map1.get(name);
	             StringBuffer sBuffer = new StringBuffer();
	             for (int i = 0; i < values.length; i++) {
	            	 sBuffer.append(values[i]).append("&");
				 }
	             params.append(name).append("=").append(sBuffer.toString());
	         }

	    	 StringBuffer url = request.getRequestURL();
	    	 
	    	 if (request.getQueryString() != null) {
	    	     url.append('?');
	    	     url.append(request.getQueryString());
	    	 }
	    	 url.append("##").append(params.toString());
	    	 
	    	 return url.toString();
	    }
	
	
}
