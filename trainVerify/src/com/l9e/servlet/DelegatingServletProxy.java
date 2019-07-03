package com.l9e.servlet;

import java.io.IOException;

import javax.servlet.GenericServlet;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class DelegatingServletProxy extends GenericServlet {
	
	private static final long serialVersionUID = 1L;

	private String targetBean;

    private Servlet proxy;

	@Override
	public void service(ServletRequest req, ServletResponse res)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		proxy.service(req, res);
	}

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        this.targetBean = getServletName();
        this.proxy = (Servlet) wac.getBean(targetBean);

        proxy.init(getServletConfig());

	}
	
	

}
