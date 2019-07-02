package com.train.system.center.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * HttpUtil
 *
 * @author taokai3
 * @date 2018/11/8
 */
public class HttpUtil {
    private static Logger log = LoggerFactory.getLogger(HttpUtil.class);
    private static String charset = "UTF-8";

    /**
     * HttpGet
     *
     * @param url
     * @param timeout
     * @author: taoka
     * @date: 2018年2月24日 上午9:21:10
     */
    public String doHttpGet(String url, int timeout) {
        String result = "";
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
            get.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=" + charset);
            response = client.execute(get);
            int statusCode = response.getStatusLine().getStatusCode();
            log.info("响应状态码:{}", statusCode);
            HttpEntity entity = response.getEntity();
            StringBuffer buffer = new StringBuffer();
            br = new BufferedReader(new InputStreamReader(entity.getContent(), charset));
            String line = "";
            while ((line = br.readLine()) != null) {
                buffer.append(line);
            }
            result = buffer.toString();
            log.info("GET请求结果:{}", result);
        } catch (Exception e) {
            log.info("GET请求异常", e);
        } finally {
            // 释放资源
            close(client, get, response, br);
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
        String result = "";
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
                post.setHeader("Content-Type", "application/json; charset=" + charset);
            } else {
                post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=" + charset);
            }
            log.info("请求url:{},参数:{}",url, params);
            StringEntity strEntity = new StringEntity(params, charset);
            post.setEntity(strEntity);
            response = client.execute(post);
            int statusCode = response.getStatusLine().getStatusCode();
            log.info("响应状态码:{}", statusCode);

            HttpEntity entity = response.getEntity();
            BufferedReader br = null;
            StringBuffer buffer = new StringBuffer();
            inputStreamReader = new InputStreamReader(entity.getContent(), charset);
            br = new BufferedReader(inputStreamReader);
            String line = "";
            while ((line = br.readLine()) != null) {
                buffer.append(line);
            }
            result = buffer.toString();
            log.info("POST请求结果:{}", result);
        } catch (Exception e) {
            log.info("POST请求异常", e);
        } finally {
            close(client, post, response, inputStreamReader);

        }
        return result;
    }

    /**
     * 释放资源
     *
     * @param client
     * @param httpRequestBase
     * @param response
     * @param reader
     */
    private void close(CloseableHttpClient client, HttpRequestBase httpRequestBase, CloseableHttpResponse response,
            Reader reader) {
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (Exception e) {

        }
        try {
            if (httpRequestBase != null) {
                httpRequestBase.releaseConnection();
            }
        } catch (Exception e) {

        }
        try {
            if (client != null) {
                client.close();
            }
        } catch (Exception e) {

        }
        try {
            if (response != null) {
                response.close();
            }
        } catch (Exception e) {

        }
    }
}
