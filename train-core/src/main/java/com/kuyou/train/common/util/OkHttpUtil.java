package com.kuyou.train.common.util;

import com.alibaba.fastjson.JSON;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * OKHttpUtil
 *
 * @author taokai3
 * @date 2018/10/30
 */
public class OkHttpUtil {

    private static final Integer TIMEOUT = 5 * 1000;
    private static final MediaType TEXT_HTML = MediaType.parse("text/html;charset=utf-8");
    private static final MediaType APPLICATION_JSON = MediaType.parse("application/json;charset=UTF-8");
    private static final MediaType APPLICATION_FORM = MediaType
            .parse("application/x-www-form-urlencoded;charset=UTF-8");

    /**
     * @param connectTimeout：连接超时 (毫秒)
     * @param readTimeout：读超时     (毫秒)
     * @param writeTimeout：写超时    (毫秒)
     * @return
     */
    private static OkHttpClient getOkHttpClient(int connectTimeout, int readTimeout, int writeTimeout) {
        return new OkHttpClient.Builder().connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(readTimeout, TimeUnit.MILLISECONDS).writeTimeout(writeTimeout, TimeUnit.MILLISECONDS)
                .build();
    }

    private static OkHttpClient getDefaultOkHttpClient() {
        return new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS).build();
    }


    private static String converterParameter2String(String url, Map<String, Object> parameter)
            throws UnsupportedEncodingException {
        StringBuffer queryString = new StringBuffer();
        int length = queryString.length();
        if (StringUtils.isNotBlank(url)) {
            queryString.append(url).append("?");
        }

        Iterator<Map.Entry<String, Object>> iterator = parameter.entrySet().iterator();
        while (iterator.hasNext()) {
            if (length != queryString.length()) {
                queryString.append("&");
            }
            Map.Entry<String, Object> next = iterator.next();
            String key = next.getKey();
            Object value = next.getValue();
            String valueStr = "";
            if (value instanceof String || value instanceof Number) {
                valueStr = value.toString();
            } else {
                valueStr = JSON.toJSONString(value);
            }
            queryString.append(URLDecoder.decode(key, "UTF-8")).append("=")
                    .append(URLDecoder.decode(valueStr, "UTF-8"));
        }
        return queryString.toString();
    }

    /**
     * @param url
     * @return
     */
    public static String httpGet(String url) {
        return httpGet(url, TIMEOUT);
    }

    public static String httpGet(String url, int timeout) {
        return httpGet(url, timeout, null);
    }

    public static String httpGet(String url, Map<String, Object> parameter) {
        return httpGet(url, TIMEOUT, parameter);
    }

    public static String httpGet(String url, int timeout, Map<String, Object> parameter) {
        String result = "";
        try {
            OkHttpClient client = getOkHttpClient(timeout, timeout, timeout);
            String queryString = url;
            if (parameter != null) {
                queryString = converterParameter2String(url, parameter);
            }
            Response response = client.newCall(new Request.Builder().url(queryString).build()).execute();
            result = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * post请求
     *
     * @param url
     * @param parameter
     */
    public static String httpPost(String url, Map<String, Object> parameter) {
        String result = "";
        try {
            OkHttpClient client = getDefaultOkHttpClient();
            RequestBody requestBody = null;
            if (parameter != null) {
                requestBody = RequestBody.create(TEXT_HTML, converterParameter2String(url, parameter));
            }
            Request request = new Request.Builder().url(url).post(requestBody).build();
            Response response = client.newCall(request).execute();
            result = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public static String httpPost(String url, String parameter) {
        String result = "";
        try {
            OkHttpClient client = getDefaultOkHttpClient();
            Request request = new Request.Builder().url(url).post(RequestBody.create(APPLICATION_FORM, parameter))
                    .build();
            Response response = client.newCall(request).execute();
            result = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}