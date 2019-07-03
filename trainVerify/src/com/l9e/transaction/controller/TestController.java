package com.l9e.transaction.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;

@Controller
@RequestMapping("/test")
public class TestController extends BaseController{
	
	private static final Logger logger = Logger.getLogger(TestController.class);
	
	
	@RequestMapping("/dl_visit.do")
	public void query(HttpServletRequest request,
			HttpServletResponse response){
		
		String line = null;
		try {
			Process proc = Runtime.getRuntime().exec("sh /root/dl_visit.sh");  
			InputStream stderr = proc.getInputStream();
			InputStreamReader isr = new InputStreamReader(stderr);
			BufferedReader br = new BufferedReader(isr);
			line = br.readLine();
			logger.info("visist num="+ line);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.write2Response(response, "visist num="+ line);
	}
	
}
