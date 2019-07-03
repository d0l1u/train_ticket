package com.l9e.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.l9e.entity.TrainConsts;
import com.l9e.util.HttpsUtil;

public class QueryMidStationServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 109037403000148161L;
	private static Logger logger=Logger.getLogger(QueryMidStationServlet.class);
	String url="https://kyfw.12306.cn/otn/czxx/queryByTrainNo?";
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
		String train_no=req.getParameter("train_no");
		String from_station_telecode=req.getParameter("from_station_telecode");
		String to_station_telecode=req.getParameter("to_station_telecode");
		String depart_date=req.getParameter("depart_date");
		String url_ = req.getParameter("url");
		if(url_!=null){
			this.url = url_;
		}
		
		try{
			StringBuffer query_url = new StringBuffer();
			query_url.append(url).append("train_no=").append(train_no).append("&from_station_telecode=")
			.append(from_station_telecode).append("&to_station_telecode=").append(to_station_telecode)
			.append("&depart_date=").append(depart_date);
			logger.info(query_url.toString());
			String jsonStr = HttpsUtil.sendHttps(query_url.toString());
			logger.info("jsonStr:"+jsonStr);
			write2Response(response,jsonStr);
		}catch(Exception e){
			logger.error("java查询票价异常",e);
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
