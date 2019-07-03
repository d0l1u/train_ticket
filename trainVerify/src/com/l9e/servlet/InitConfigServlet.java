package com.l9e.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component(value="initConfigServlet")
public class InitConfigServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger.getLogger(InitConfigServlet.class);
	
	@Override
	public void init() throws ServletException {
		logger.info("===============init system config begin=====================");
		super.init();
		logger.info("===============init system config end=======================");
	}

	@Override
	public void destroy() {
		super.destroy();
	}
	

}
