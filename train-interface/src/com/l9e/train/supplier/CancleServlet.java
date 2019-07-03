package com.l9e.train.supplier;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.l9e.train.supplier.service.impl.OrderService;

/**
 * Servlet implementation class PayServlet
 */
public class CancleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 private Logger logger=Logger.getLogger(this.getClass());   
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("----cancle order----receive url:"+request.getQueryString());
	
		OrderService orderService = new OrderService();
		
		String order_id = request.getParameter("order_id");
		logger.info("----start cancle order----:"+order_id);
		try {
			int result = orderService.canaleOrderInfo(order_id);
			logger.info("----cancel order " + order_id + " , result " + result + " ------");
			if(result==1){
				logger.info("----cancle success----");
				write(response,"success");
			}else{
				logger.info("----cancle failure----");
				write(response,"failure");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("----cancle Exception----"+e);
			write(response,"failure");
		}
		logger.info("----end cancle order----");
	}

	private void write(HttpServletResponse response,String returnMsg){
		try {
			response.getWriter().write(returnMsg);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			logger.error("response write failure:"+e);
			
		}finally{
			
		}
	}
}
