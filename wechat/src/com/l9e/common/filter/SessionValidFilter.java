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
		HttpSession session = req.getSession();

		String explorerInfo = "user-agent:" + req.getHeader("user-agent");
		logger.info(explorerInfo);

		String requestpath = req.getServletPath();
		String esid = session.getId();

		// res.setHeader("P3P", "CP=CAO PSA OUR");

		if (requestpath.indexOf("interface") > 0
				|| (requestpath.indexOf("receiveUpayNofity") > 0)
				|| (requestpath.indexOf("orderResult_no") > 0)
				|| (requestpath.indexOf("receviceNotification") > 0)
				|| (requestpath.indexOf("openID") > 0)) {
			chain.doFilter(req, res);
			return;
		}

		/**
		 * checkIsHomeList EOP访问系统URL checkurl 系统免登陆URL
		 */
		if (checkIsHomeList(req)) {
			logger.info("【sid=" + esid + "】用户从Cmpay左侧菜单开始访问本系统，请求为："
					+ requestpath);

		} else if (isNoLoginUrl(requestpath)) {
			logger.info("【sid=" + esid + "】免登录用户开始访问本系统，请求为：" + requestpath);

		} else if (null == session.getAttribute("weixinOpenID")
				&& requestpath.indexOf("/register/check.jhtml") > 0) {
			logger.info("【sid=" + esid + "】用户登录信息过期，需要重新登录，请求为：" + requestpath);

			// session.setAttribute("errMsg", "请重新登录后再试！");

			RequestDispatcher rd = request
					.getRequestDispatcher("/pages/common/sessionTimeOut.jsp");
			rd.forward(request, response);
			return;

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
	}

	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public List<String> getHomeList() {
		return homeList;
	}

	public void setHomeList(List<String> homeList) {
		this.homeList = homeList;
	}
}
