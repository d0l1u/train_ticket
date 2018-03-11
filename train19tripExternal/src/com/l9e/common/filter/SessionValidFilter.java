package com.l9e.common.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;


/**
 * session验证以及用户轨迹
 */
public class SessionValidFilter implements Filter {
	
	protected static final Logger logger = Logger.getLogger(SessionValidFilter.class);
	
	private List<String> homeList = new ArrayList<String>();

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		String explorerInfo = "user-agent:" + req.getHeader("user-agent");
		logger.info(explorerInfo);
		
		res.setHeader("P3P", "CP=CAO PSA OUR");
		
		chain.doFilter(req, res);
	}

	public void destroy() {
		// TODO Auto-generated method stub
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

	public List<String> getHomeList() {
		return homeList;
	}

	public void setHomeList(List<String> homeList) {
		this.homeList = homeList;
	}
}
