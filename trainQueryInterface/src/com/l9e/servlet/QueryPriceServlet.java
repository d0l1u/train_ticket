package com.l9e.servlet;

import java.io.IOException;

import javax.net.ssl.SSLContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.l9e.entity.TrainConsts;
import com.l9e.util.HttpsUtil;


public class QueryPriceServlet extends HttpServlet{
	private static Logger logger=Logger.getLogger(QueryPriceServlet.class);
	SSLContext sc=null;
	String url="https://kyfw.12306.cn/otn/leftTicket/queryTicketPrice";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String train_no=req.getParameter("train_no");
		String from_station_no=req.getParameter("from_station_no");
		String to_station_no=req.getParameter("to_station_no");
		String seat_types=req.getParameter("seat_types");
		String train_date=req.getParameter("train_date");
		String url_ = req.getParameter("url");
		if(url_!=null){
			this.url = url_;
		}
		
		try{
			StringBuffer query_url = new StringBuffer();
			query_url.append(url).append("?train_no=").append(train_no).append("&from_station_no=")
			.append(from_station_no).append("&to_station_no=").append(to_station_no)
			.append("&seat_types=").append(seat_types).append("&train_date=").append(train_date);
			logger.info(query_url.toString());
			String jsonStr = HttpsUtil.sendHttps(query_url.toString());
			JSONObject json = JSONObject.fromObject(jsonStr.toString());
			Object status = json.get("status");
			if("true".equals(status.toString())){
				Object data = json.get("data");
				JSONObject data_json = JSONObject.fromObject(data.toString());
				logger.info(data_json.toString());
				write2Response(response,data_json.toString());
			}else{
				write2Response(response,TrainConsts.ERROR); 
			}
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
