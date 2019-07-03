package com.l9e.common;

import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class BaseController {
    private static final Logger logger = Logger.getLogger(BaseController.class);

    // 输出jsonObject
    public static void printJson(HttpServletResponse response, String str) {
        logger.info("响应结果：" + str);
        response.setCharacterEncoding("utf-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.write(str);
        } catch (IOException e) {
            logger.info("输出异常！", e);
        } finally {
            out.flush();
            out.close();
        }
    }

    /**
     * 空转0
     *
     * @param str
     * @return
     */
    public int emptyToZero(String str) {
        if (str == null || "".equals(str.trim()) || "null".equals(str.trim())) {
            return 0;
        } else {
            return Integer.parseInt(str.trim());
        }
    }

    /**
     * 值写入response
     *
     * @param response
     * @param StatusStr
     */
    public void write2Response(HttpServletResponse response, String StatusStr) {
        try {
            response.getWriter().write(StatusStr);
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * request.getParameter
     *
     * @param param
     * @return
     */
    public String getParam(HttpServletRequest request, String param) {
        return request.getParameter(param) == null ? "" : request.getParameter(param).toString().trim();
    }

    // 根据状态码获取输出jsonObject对象
    public JSONObject getJson(String code) {
        JSONObject json = new JSONObject();
        json.put("return_code", code);
        json.put("message", TrainConsts.getReturnCode().get(code));
        return json;

    }

    /**
     * 获取所有请求参数
     *
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    public String getFullURL(HttpServletRequest request) {

        StringBuffer params = new StringBuffer();
        Map<String, String[]> map1 = (Map<String, String[]>) request.getParameterMap();
        for (String name : map1.keySet()) {
            String[] values = map1.get(name);
            StringBuffer sBuffer = new StringBuffer();
            for (int i = 0; i < values.length; i++) {
                sBuffer.append(values[i]).append("&");
            }
            params.append(name).append("=").append(sBuffer.toString());
        }

        StringBuffer url = request.getRequestURL();

        if (request.getQueryString() != null) {
            url.append('?');
            url.append(request.getQueryString());
        }
        url.append("##").append(params.toString());

        logger.info("GET请求参数：" + request.getQueryString());
        String postStr = params.substring(0, params.lastIndexOf("&"));//去掉末尾&
        logger.info("POST请求参数：" + postStr);

        return url.toString();
    }

}
