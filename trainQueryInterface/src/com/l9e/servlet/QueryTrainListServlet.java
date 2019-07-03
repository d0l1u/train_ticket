package com.l9e.servlet;

import java.io.IOException;

import javax.net.ssl.SSLContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.l9e.entity.TrainConsts;
import com.l9e.util.HttpsUtil;

public class QueryTrainListServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7047004354427782913L;
	private static Logger logger=Logger.getLogger(QueryTrainListServlet.class);
	SSLContext sc=null;
	String url="https://kyfw.12306.cn/otn/resources/js/query/train_list.js";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req,resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		
		try{
			logger.info(url.toString());
			String jsonStr = HttpsUtil.sendHttps(url);
			logger.info("jsonStr:"+jsonStr);
			write2Response(response,jsonStr);
		}catch(Exception e){
			logger.error("java查询车次列表 ！",e);
			write2Response(response,TrainConsts.ERROR);
		}
	}
	
	/**
	 * 值写入response
	 * @param response
	 * @param StatusStr
	 */
	public void write2Response(HttpServletResponse response, String StatusStr){
		try {
			response.getWriter().write(StatusStr);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
