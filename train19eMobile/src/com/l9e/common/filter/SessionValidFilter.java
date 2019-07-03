package com.l9e.common.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.l9e.util.MemcachedUtil;

/**
 * session验证以及用户轨迹
 */
public class SessionValidFilter implements Filter {

	protected static final Logger logger = Logger
			.getLogger(SessionValidFilter.class);

	private List<String> homeList = new ArrayList<String>();

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		res.setHeader("P3P", "CP=CAO PSA OUR");

		HttpSession session = req.getSession();

		String explorerInfo = "user-agent:" + req.getHeader("user-agent");
		logger.info(explorerInfo);

		String requestpath = req.getServletPath();
		String esid = session.getId();

		logger.info("+++++++++++++++++++++esid++++++++++++++++++++" + esid);
		Cookie[] cookies = req.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie c : cookies) {
				logger.info("cookie:" + c.getName() + "=========="
						+ c.getValue());
			}
		}

		/**
		 * checkIsHomeList EOP访问系统URL checkurl 系统免登陆URL
		 */
		if (checkIsHomeList(req)) {
			logger.info("【sid=" + esid + "】用户开始登录本系统，请求为：" + requestpath);

		} else if (isNoLoginUrl(requestpath)) {
			logger.info("【sid=" + esid + "】免验证URL，请求为：" + requestpath);

		} else {
			String lvcode = this.getParam(req, "lvcode");
			logger.info("【filter】用户lvcode=" + lvcode);
			if(StringUtils.isEmpty(lvcode)){
				logger.info("【sid=" + esid + "】用户登录信息过期，需要重新登录，请求为："
						+ requestpath);
				JSONObject returnJson = new JSONObject();
				returnJson.put("return_code", "LOGIN_OUT");
				returnJson.put("message", "代理商登录失效，请重新登录！");
				printJson(res, returnJson.toString());
				return;
			}
			
			Object loginObj = MemcachedUtil.getInstance().getAttribute(lvcode);
			
			if (null == loginObj) {
				logger.info("【sid=" + esid + "】用户登录信息过期，需要重新登录，请求为："
						+ requestpath);
				JSONObject returnJson = new JSONObject();
				returnJson.put("return_code", "LOGIN_OUT");
				returnJson.put("message", "代理商登录失效，请重新登录！");
				printJson(res, returnJson.toString());
				return;
			}
			// 延长用户登录时间，防止过期
			MemcachedUtil.getInstance().setAttribute(lvcode, loginObj,
					1000 * 60 * 30);

		}
		chain.doFilter(req, res);
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

	// 输出jsonObject
	public void printJson(HttpServletResponse response, String str) {
		logger.info(str);
		//response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		//response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.write(str);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.flush();
			out.close();
		}
	}

	public String getParam(HttpServletRequest request, String param) {
		return request.getParameter(param) == null ? "" : request.getParameter(
				param).toString().trim();
	}
}
