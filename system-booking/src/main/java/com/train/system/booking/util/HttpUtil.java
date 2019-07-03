package com.train.system.booking.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * HttpUtil
 *
 * @author taokai3
 * @date 2018/6/18
 */

public class HttpUtil {

    private Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    public static final String EXCEPTION ="EXCEPTION";

    /**
     * HttpGet
     *
     * @param url
     * @param timeout
     * @author: taoka
     * @date: 2018年2月24日 上午9:21:10
     */
    public String doHttpGet(String url, int timeout) {
        logger.info("GET请求路径:【{}】", url);
        String result = EXCEPTION;
        CloseableHttpClient client = null;
        HttpGet get = null;
        CloseableHttpResponse response = null;
        BufferedReader br = null;
        try {
            client = HttpClients.createDefault();
            get = new HttpGet(url);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout)
                    .build();
            get.setConfig(requestConfig);
            get.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            response = client.execute(get);
            HttpEntity entity = response.getEntity();
            StringBuffer buffer = new StringBuffer();
            br = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
            String line = "";
            while ((line = br.readLine()) != null) {
                buffer.append(line);
            }
            result = buffer.toString();
        } catch (Exception e) {
            logger.info("GET请求异常", e);
        } finally {
            // 释放资源
            try {
                if (br != null) {
                    br.close();
                }
                if (get != null) {
                    get.releaseConnection();
                }
                if (client != null) {
                    client.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * HttpPost
     *
     * @param url
     * @param params
     * @param timeout
     * @param isJson
     * @throws Exception
     * @author: taoka
     * @date: 2018年2月24日 上午9:21:31
     */
    public String doHttpPost(String url, String params, int timeout, boolean isJson) {
        logger.info("POST请求路径:【{}】, 请求参数:【{}】", url, params);
        String result = EXCEPTION;
        CloseableHttpClient client = null;
        HttpPost post = null;
        CloseableHttpResponse response = null;
        InputStreamReader inputStreamReader = null;
        try {
            client = HttpClients.createDefault();
            post = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout)
                    .build();
            post.setConfig(requestConfig);
            if (isJson) {
                post.setHeader("Content-Type", "application/json; charset=UTF-8");
            } else {
                post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            }
            StringEntity strEntity = new StringEntity(params, "UTF-8");
            post.setEntity(strEntity);
            response = client.execute(post);

            HttpEntity entity = response.getEntity();
            BufferedReader br = null;
            StringBuffer buffer = new StringBuffer();
            inputStreamReader = new InputStreamReader(entity.getContent(), "UTF-8");
            br = new BufferedReader(inputStreamReader);
            String line = "";
            while ((line = br.readLine()) != null) {
                buffer.append(line);
            }
            result = buffer.toString();
        } catch (Exception e) {
            logger.info("POST请求异常", e);
        } finally {
            // 释放资源
            try {
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (post != null) {
                    post.releaseConnection();
                }
                if (client != null) {
                    client.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
