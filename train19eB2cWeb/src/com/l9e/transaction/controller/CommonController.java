package com.l9e.transaction.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.util.MobileMsgUtil;

/**
 * Common
 *
 */
@Controller
@RequestMapping("/common")
public class CommonController extends BaseController{
	protected static final Logger logger = Logger.getLogger(CommonController.class);
	
	@Resource
	private MobileMsgUtil mobileMsgUtil;
	
	@RequestMapping("/goToErrPage.jhtml")
	public String goToErrPage(HttpServletRequest request,
			HttpServletResponse response){
		String errMsg = "";
		try {
			errMsg = URLDecoder.decode(this.getParam(request, "errMsg"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("URL解码异常", e);
		}
		request.setAttribute("errMsg", errMsg);
		return "common/error";
	}
	
	@RequestMapping("/sendPhoneMsg_no.jhtml")
	public String sendPhoneMsg(HttpServletRequest request,
			HttpServletResponse response){
		try {
			String key = this.getParam(request, "key");
			String phone = this.getParam(request, "phone");
			String content = URLDecoder.decode(this.getParam(request, "content"), "UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html");
			PrintWriter w = response.getWriter();
			if("19ehcp".equals(key)){
				mobileMsgUtil.send(phone, content, "22");
				w.write("1,正在发送短信");
			}else{
				w.write("-1,密钥错误");
			}
			w.flush();
			w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
