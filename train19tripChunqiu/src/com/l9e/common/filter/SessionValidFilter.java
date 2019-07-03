package com.l9e.common.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.l9e.common.TrainConsts;
import com.l9e.util.XssHttpRequest;


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
		HttpSession session = req.getSession();

		String explorerInfo = "user-agent:" + req.getHeader("user-agent");
		logger.info(explorerInfo);
		
		String requestpath = req.getServletPath();
		String esid = session.getId();
		
		res.setHeader("P3P", "CP=CAO PSA OUR");
		String remoteAddress = req.getRemoteAddr();
		logger.info("【sid=" + esid + "】访问本服务器的机器ip为：" + remoteAddress);
		
		/**
		 * checkIsHomeList EOP访问系统URL
		 * checkurl 系统免登陆URL
		 */
		if(isAlipayRequest(req)) {
			logger.info("【sid=" + esid + "】和支付宝相关的请求，请求为：" + requestpath);
		}else if(checkIsHomeList(req)){
			logger.info("【sid=" + esid + "】用户从春秋航空开始访问本系统，请求为：" + requestpath);
			
		}else if(isNoLoginUrl(requestpath)){
			logger.info("【sid=" + esid + "】免登录用户开始访问本系统，请求为：" + requestpath);
			
		}else if(null == session.getAttribute(TrainConsts.INF_LOGIN_USER)){
			logger.info("【sid=" + esid + "】用户登录信息过期，需要重新登录，请求为：" + requestpath);

			session.setAttribute("errMsg", "请重新登录后再试！");
			RequestDispatcher rd = request.getRequestDispatcher("/pages/common/sessionTimeOut.jsp");
			rd.forward(request, response);
			return;
		}
		chain.doFilter(new XssHttpRequest(
                (HttpServletRequest) request), response);
	}
	
	private boolean isAlipayRequest(HttpServletRequest request) {
		String url = request.getServletPath();
		return url.contains("alipay");
	}

	private boolean checkIsHomeList(HttpServletRequest request) {
		String uri = request.getServletPath();
		return homeList.contains(uri);
	}

	private boolean isNoLoginUrl(String url) {
		CharSequence cs = "_no.jhtml";
		CharSequence csother = ".jsp";
		boolean flag = false;
		if (url.contains(cs) || url.contains(csother)) {
			flag = true;
		}
		return flag;
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
