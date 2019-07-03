package com.l9e.train.supplier;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.l9e.train.supplier.po.OrderRefund;
import com.l9e.train.supplier.service.impl.OrderService;


public class RefundServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(this.getClass());

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		logger.info("----start insert refund----receive url:"+request.getQueryString());
		String returnMsg=null;
		RefundUtil util = new RefundUtil();
		
		OrderRefund orderRefund=util.verifyRequest(request);
		
		logger.info("orderid:"+orderRefund.getOrderid());
		
		OrderService orderService = new OrderService();
		try {
			if(StringUtils.isEmpty(orderRefund.getChannel())){
				orderRefund.setChannel("19e");
			}
			//订单入库
			orderService.refundOrderInfo(orderRefund);
			
			returnMsg =orderRefund.responstValue(orderRefund, OrderRefund.REFUND_SUCCESS);
			write(response,returnMsg);
		} catch (Exception e) {
			//订单重复
			logger.warn("refund repateException"+orderRefund.getOrderid()+" exception:"+e);
			returnMsg =orderRefund.responstValue(orderRefund, OrderRefund.REFUND_SUCCESS);
			write(response,returnMsg);
		}
		
		logger.info("----end insert refund----");
	}

	private void write(HttpServletResponse response, String returnMsg) {
		try {
			response.getWriter().write(returnMsg);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			logger.error("response write failure:" + e);

		} finally {

		}
	}

}
