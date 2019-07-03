package com.l9e.common;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * 基础控制器
 * @author licheng
 *
 */
public abstract class BaseController {
	
	private static final Logger logger = Logger.getLogger(BaseController.class);
	
	/**
	 * 从request中获取参数，未获取到则返回空字符串
	 * 获取到的参数经过trim处理
	 * @param param
	 * @return
	 */
	public String getParam(HttpServletRequest request, String param){
//		try {
//			request.setCharacterEncoding("utf-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
		return request.getParameter(param) == null ? "" : request.getParameter(param).toString().trim();
	}
	
	/**
	 * 响应输出
	 * @param response
	 * @param respStr
	 */
	public void write2Response(HttpServletResponse response, String respStr){
		try {
			response.getWriter().write(respStr);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * 响应json字符串
	 * @param response
	 * @param jsonStr
	 */
	public void printJson(HttpServletResponse response,String jsonStr){
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(jsonStr);
		} catch (IOException e) {
			logger.error("输出异常！",e);
		}finally{
				out.flush();
				out.close();
		}
	}
	
	/**
	 * 判断字符串是否为 null或者空串
	 * @param input
	 * @return
	 */
	public boolean isEmpty(String input) {
		return input == null || input.equals("");
	}
	
	/**
	 * 接收参数
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public String recieveParameterUTF8(HttpServletRequest request, HttpServletResponse response) throws IOException {
		InputStream in = null;
		BufferedInputStream bin = null;
		
		try{
			in = request.getInputStream();
			bin = new BufferedInputStream(in);
			
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			
			byte[] buff = new byte[1024 << 3];
			int len = -1;
			while((len = bin.read(buff)) > -1) {
				bao.write(buff, 0, len);
			}
			
			return new String(bao.toByteArray(), "UTF-8");
		} finally {
			if(bin != null) {
				in.close();
			}
		}
		
	}
}
