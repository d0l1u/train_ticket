package com.l9e.train.supplier;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.l9e.train.supplier.po.JlOrder;
import com.l9e.train.supplier.service.impl.JlOrderService;
import com.unlun.commons.exception.DatabaseException;
import com.unlun.commons.exception.RepeatException;

/**
 * 抢票订单供货服务
 * @author wangsf01
 *
 */
public class JlOrderServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private Logger logger=Logger.getLogger(this.getClass());
	
	
	/**
     * @see HttpServlet#HttpServlet()
     */
	public JlOrderServlet() {
        super();
    }
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		
		logger.info("----start insert jl_order----receive url:"+request.getQueryString());
		String type = request.getParameter("type");
		if(type.equals("cancel")){
			cancel(request,response);
			return ;
		}else if (type.equals("refund")){
			refund(request,response);
			return ;
		}
		String returnMsg=null;
		JlOrderUtil util = new JlOrderUtil();
		
		JlOrder supJlOrder=util.verifyRequest(request);
		
		logger.info("抢票订单信息转换为实体JlOrder:"+supJlOrder);
		
		logger.info("抢票订单,JlOrderUtil.verifyCode()=="+util.verifyCode());
		if(util.verifyCode()!="0000"){
			//参数错误
			returnMsg =supJlOrder.responstValue(supJlOrder, util.verifyCode());
			write(response,returnMsg);
			return;
		}
		
		supJlOrder.setOrderStatus(JlOrder.STATUS_ORDER_START);
		logger.info("抢票订单信息实体JlOrder设置订单状态为开始出票:"+supJlOrder);
		//订单入库
		JlOrderService orderService = new JlOrderService();
		try {
			orderService.downBill(supJlOrder);
		} catch (RepeatException e) {
			//订单重复
			logger.info("jl_order repateException"+supJlOrder.getOrderId()+" exception:"+e);
			returnMsg =supJlOrder.responstValue(supJlOrder, JlOrder.ORDER_ERROR);
			write(response,returnMsg);
		} catch (DatabaseException e) {
			//数据库异常
			logger.info("jl_order DownBillFailure"+supJlOrder.getOrderId()+" exception:"+e);
			returnMsg =supJlOrder.responstValue(supJlOrder, JlOrder.ORDER_ERROR);
			write(response,returnMsg);
			return;
		} catch (Exception e) {
			//数据库异常
			logger.info("jl_order DownBill Exception"+supJlOrder.getOrderId()+" exception:"+e);
			returnMsg =supJlOrder.responstValue(supJlOrder, JlOrder.ORDER_ERROR);
			write(response,returnMsg);
			return;
		}
		
		logger.info("----end insert jl_order----");
	}
	
	private void refund(HttpServletRequest request, HttpServletResponse response) {
		logger.info("----start cancel jl_order----receive url:"+request.getQueryString());
		String orderId = request.getParameter("orderId");
		JlOrder order = new JlOrder();
		order.setOrderId(orderId);
		logger.info("----start cancel jl_order----receive order_Id:"+orderId);
		JlOrderService service = new JlOrderService();
		String returnMsg = "";
		try {
			int cancel = service.refund(orderId);
			if (cancel ==1) {
				returnMsg =order.responstValue(order, JlOrder.ORDER_SUCCESS);
				write(response,returnMsg);
			}else{
				returnMsg =order.responstValue(order, JlOrder.ORDER_ERROR);
				write(response,returnMsg);
			}
		} catch (RepeatException e) {
			logger.info("jl_order repateException:"+orderId+" exception:"+e);
			returnMsg =order.responstValue(order, JlOrder.ORDER_ERROR);
			write(response,returnMsg);
			e.printStackTrace();
		} catch (DatabaseException e) {
			logger.info("jl_order cancel fail"+order.getOrderId()+" exception:"+e);
			returnMsg =order.responstValue(order, JlOrder.ORDER_ERROR);
			write(response,returnMsg);
			e.printStackTrace();
		}
		
		
		
	}

	private void cancel(HttpServletRequest request, HttpServletResponse response) {
		logger.info("----start cancel jl_order----receive url:"+request.getQueryString());
		String orderId = request.getParameter("orderId");
		JlOrder order = new JlOrder();
		order.setOrderId(orderId);
		logger.info("----start cancel jl_order----receive order_Id:"+orderId);
		JlOrderService service = new JlOrderService();
		String returnMsg = "";
		try {
			int cancel = service.cancel(orderId);
			if (cancel ==1) {
				returnMsg =order.responstValue(order, JlOrder.ORDER_SUCCESS);
				write(response,returnMsg);
			}else{
				returnMsg =order.responstValue(order, JlOrder.ORDER_ERROR);
				write(response,returnMsg);
			}
		} catch (RepeatException e) {
			logger.info("jl_order repateException:"+orderId+" exception:"+e);
			returnMsg =order.responstValue(order, JlOrder.ORDER_ERROR);
			write(response,returnMsg);
			e.printStackTrace();
		} catch (DatabaseException e) {
			logger.info("jl_order cancel fail"+order.getOrderId()+" exception:"+e);
			returnMsg =order.responstValue(order, JlOrder.ORDER_ERROR);
			write(response,returnMsg);
			e.printStackTrace();
		}
		
	}

	private void write(HttpServletResponse response,String returnMsg){
		try {
			response.getWriter().write(returnMsg);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			logger.info("jl_order  response write failure:"+e);
			
		}finally{
			
		}
	}

}
