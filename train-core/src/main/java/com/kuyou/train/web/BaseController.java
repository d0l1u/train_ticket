package com.kuyou.train.web;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

/**
 * BaseController
 *
 * @author taokai3
 * @date 2018/11/5
 */
@Slf4j
public class BaseController {

    /**
     * 异常返回值
     *
     * @param request
     * @param response
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public String exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {
        log.info("【回调接口异常】:{}", e.getClass().getSimpleName(), e);
        Set<MediaType> mediaTypeSet = new HashSet<>();
        mediaTypeSet.add(new MediaType(MediaType.TEXT_HTML, Charset.forName("UTF-8")));
        request.setAttribute(HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE, mediaTypeSet);
        return "false";
    }

    /**
     * 响应json字符串
     *
     * @param response
     */
    public void writerResponse(HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();
        out.print("success");
    }

    /**
     * 获取post请求参数
     *
     * @param request
     * @return
     */
    public String getPost(HttpServletRequest request) throws IOException {
        String result = getParameter(request);
        if (StringUtils.isBlank(result)) {
            throw new IOException("参数为空，返回false");
        }
        try {
            result = URLDecoder.decode(result, "utf-8");
        } catch (Exception e) {
            log.info("URLDecoder.decode Exception");
        }
        log.info("getPost参数:{}", result);
        return result;
    }

    /**
     * data=xxxxxxxxx
     *
     * @param request
     * @return
     * @throws IOException
     */
    public String getData(HttpServletRequest request) throws IOException {
        String result = request.getParameter("data");
        if (StringUtils.isBlank(result)) {
            throw new IOException("参数为空，返回false");
        }
        try {
            result = URLDecoder.decode(result, "utf-8");
        } catch (Exception e) {
            log.info("URLDecoder.decode Exception");
        }
        log.info("getData参数:{}", result);
        return result;
    }

    /**
     * backjson=xxxxxxxxx
     *
     * @param request
     * @return
     * @throws IOException
     */
    public String getBackJson(HttpServletRequest request) throws IOException {
        String result = getParameter(request);
        if (StringUtils.isBlank(result)) {
            throw new IOException("参数为空，返回false");
        }
        result = result.split("=")[1];
        if (StringUtils.isBlank(result)) {
            throw new IOException("2-参数为空，返回false");
        }
        try {
            result = URLDecoder.decode(result, "utf-8");
        } catch (Exception e) {
            log.info("URLDecoder.decode Exception");
        }
        log.info("getBackJson参数:{}", result);
        return result;
    }

    /**
     * 从request获取参数
     *
     * @param request
     * @return
     * @throws IOException
     */
    private String getParameter(HttpServletRequest request) throws IOException {
        int contentLength = request.getContentLength();
        if (contentLength < 0) {
            return null;
        }
        byte[] buffer = new byte[contentLength];
        for (int i = 0; i < contentLength; ) {
            int readLength = request.getInputStream().read(buffer, i, contentLength - i);
            if (readLength == -1) {
                break;
            }
            i += readLength;
        }
        String charEncoding = request.getCharacterEncoding();
        if (StringUtils.isBlank(charEncoding)) {
            charEncoding = "UTF-8";
        }
        String result = new String(buffer, charEncoding);
        log.info("Request参数:{}", result);
        return result;
    }

    public String keyValue2Json(String data) {
        String[] dataArr = data.split("&");
        JSONObject json = new JSONObject();
        for (String keyValue : dataArr) {
            String[] keyValueArr = keyValue.split("=");
            json.put(keyValueArr[0], keyValueArr[1]);
        }
        log.info("keyValue2Json参数:{}", json);
        return json.toJSONString();
    }
}
