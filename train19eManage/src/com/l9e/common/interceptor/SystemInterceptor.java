package com.l9e.common.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Repository
public class SystemInterceptor extends HandlerInterceptorAdapter {

    protected static final Logger logger = Logger.getLogger(SystemInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        // 后台session控制
        String[] noFilters = new String[]{"login.jsp", "login.do", "loginRobot.do", "receiveOrderInfo.do",
                "queryCurrentCoding.do", "queryCMpayOrder.do", "queryRobotCancleOrder.do"};
        String uri = request.getRequestURI();

        if (uri.indexOf(".jsp") != -1 || uri.indexOf(".do") != -1) {
            boolean beFilter = true;
            for (String s : noFilters) {
                if (uri.indexOf(s) != -1) {
                    beFilter = false;
                    break;
                }
            }
            if (beFilter) {

                Object obj = request.getSession().getAttribute("loginUserVo");
                if (null == obj) {

                    // 未登录
                    PrintWriter out = response.getWriter();
                    StringBuilder builder = new StringBuilder();
                    builder.append("<script type=\"text/javascript\" charset=\"UTF-8\">");
                    builder.append("alert(\"页面过期，请重新登录\");");
                    builder.append("window.top.location.href=\"");
                    builder.append("/pages/login/login.jsp\";</script>");
                    out.print(builder.toString());
                    out.close();
                    return false;
                }
            }
        }

        logger.info(request.getRequestURL() + "|" + request.getQueryString() + "|" + request.getHeader("user-agent"));

        return super.preHandle(request, response, handler);
    }

}
