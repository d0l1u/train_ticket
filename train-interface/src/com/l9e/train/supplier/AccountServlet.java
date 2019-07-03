package com.l9e.train.supplier;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.l9e.train.supplier.service.impl.OrderService;
import com.unlun.commons.exception.DatabaseException;
import com.unlun.commons.exception.RepeatException;

/**
 * Servlet implementation class AccountServlet
 */
public class AccountServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(this.getClass());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AccountServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String remoteAddr = request.getRemoteAddr();
		logger.info("进行账号查询，remoteAddr:" + remoteAddr);
		String orderid = request.getParameter("orderid");

		if (StringUtils.isEmpty(orderid)) {
			write(response, "noorder");
			return;
		}

		OrderService orderService = new OrderService();

		try {

			orderService.accountByOrderid(orderid);

			write(response, orderService.getAccountStr().toString());

		} catch (RepeatException e) {
			// TODO Auto-generated catch block
			write(response, "noorder");
			logger.warn("order repateException exception:" + e);
			return;
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			write(response, "noorder");
			logger.warn("order repateException exception:" + e);
			return;
		}

	}

	private void write(HttpServletResponse response, String returnMsg) {
		try {
			response.getWriter().write(returnMsg);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("response write failure:" + e);

		} finally {

		}
	}
}
