package com.l9e.train.supplier;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.l9e.train.mq.MqService;
import com.l9e.train.supplier.service.impl.OrderService;
import com.l9e.train.util.Contes;
import com.l9e.train.util.StrUtil;

/**
 * Servlet implementation class PayServlet
 */
public class PayServlet extends HttpServlet {
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
		logger.info("----start insert pay----receive url:"+request.getQueryString());
	
		OrderService orderService = new OrderService();
		
		String order_id = request.getParameter("order_id");
		String pay_money = request.getParameter("pay_money");
		logger.info("order_id:"+order_id+";pay_money:"+pay_money);
		try {
			long step1=System.currentTimeMillis();
			orderService.payReturnInfo(order_id,pay_money);
			long step2=System.currentTimeMillis();
			write(response,"success");
			long step3=System.currentTimeMillis();
			/**插入消息队列*/
			String channel=orderService.getChannel();
			if("tongcheng".equals(channel)||"elong".equals(channel)){
				try {
					logger.info("pay to mq start:"+order_id);
					MqService  helloService = Contes.mqUtil.get("mq");
					helloService.sendMqMsg(StrUtil.getPayQueue(channel),order_id);
					logger.info("pay to mq end:"+order_id);
					//long step4=System.currentTimeMillis();
					
				} catch (Exception e) {
					logger.info(order_id+"pay to mq Exception:"+e);
					logger.info("pay resend to mq start:"+order_id);
					MqService  helloService = Contes.mqUtil.get("mq");
					helloService.sendMqMsg(StrUtil.getPayQueue(channel),order_id);
					logger.info("pay resend to mq end:"+order_id);
					e.printStackTrace();
				}
				long step4=System.currentTimeMillis();
				logger.info("order_id:"+order_id+" request for the pay step1:"+(step2-step1)+",step2:"+(step3-step2)+",step3:"+(step4-step3));
			}
		} catch (Exception e) {
			e.printStackTrace();
			write(response,"failure");
		} 
		logger.info("----end insert pay----");
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
