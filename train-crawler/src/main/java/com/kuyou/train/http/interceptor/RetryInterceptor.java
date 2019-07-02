package com.kuyou.train.http.interceptor;


import com.alibaba.fastjson.JSONObject;
import com.kuyou.train.common.code.Message;
import com.kuyou.train.common.exception.NotLoginException;
import com.kuyou.train.common.exception.TrainException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okio.Buffer;
import org.apache.commons.lang3.StringUtils;

import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

/**
 * RetryInterceptor：应用拦截器-重试（Timeout 重试、redirect 重试）
 *
 * @author taokai3
 * @date 2018/9/27
 */
@Slf4j
public class RetryInterceptor implements Interceptor {
    private static final int DEFAULT_MAX_RETRY_COUNT = 3;
    private static final long DEFAULT_RETRY_INTERVAL_MILLISECOND = 2 * 1000;
    /**
     * 最大重试次数
     */
    private int maxRetryCount;

    /**
     * 重试的间隔
     */
    private long retryIntervalMillisecond;

    public RetryInterceptor() {
        this.maxRetryCount = DEFAULT_MAX_RETRY_COUNT;
        this.retryIntervalMillisecond = DEFAULT_RETRY_INTERVAL_MILLISECOND;
    }


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        //统一设置请求头信息
        Headers headers = request.headers();
        if (StringUtils.isBlank(headers.get("Host"))) {
            request = request.newBuilder().addHeader("Host", "kyfw.12306.cn").build();
        }
        if (StringUtils.isBlank(headers.get("Connection"))) {
            request = request.newBuilder().addHeader("Connection", "keep-alive").build();
        }
        if (StringUtils.isBlank(headers.get("User-Agent"))) {
            request = request.newBuilder().addHeader("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36")
                    .build();
        }
        if (StringUtils.isBlank(headers.get("Accept-Encoding"))) {
            request = request.newBuilder().addHeader("Accept-Encoding", "deflate, br").build();
        }
        if (StringUtils.isBlank(headers.get("Accept-Language"))) {
            request = request.newBuilder().addHeader("Accept-Language", "zh-CN,zh;q=0.9").build();
        }

        //打印请求信息
        //printRequestMessage(request);

        int retryNum = 0;
        do {
            //重发睡眠
            if (retryNum != 0) {
                try {
                    Thread.sleep(retryIntervalMillisecond);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                //进行请求
                Response response = chain.proceed(request);

                //获取响应结果
                int code = response.code();
                boolean redirect = response.isRedirect();

                //判断是否成功
                if (code == 200) {
                    //打印结果
                    //response = printResponseMessage(response);
                    return response;
                }

                //其他状态
                if (!redirect) {
                    throw new TrainException(String.format(Message.RESPONSE_CODE_ERROR, request.url(), code));
                }

                //判断是否重定向
                if (location(request.url(), response.header("Location"))) {
                    return response;
                }
            } catch (SocketTimeoutException e) {
                String exName = e.getClass().getSimpleName();
                log.info("网络异常 SocketTimeoutException retryNum:{}, {}", retryNum, exName, e);
            } catch (SSLHandshakeException  e) {
                String exName = e.getClass().getSimpleName();
                log.info("网络异常 SSLHandshakeException retryNum:{}, {}", retryNum, exName, e);
            } catch (UnknownHostException e) {
                String exName = e.getClass().getSimpleName();
                log.info("网络异常 UnknownHostException retryNum:{}, {}", retryNum, exName, e);
            }
            log.info("retryNum:{}, URL:{}", retryNum, request.url());
        } while (++retryNum < maxRetryCount);

        //抛出异常,这里的都是重复超出次数
        throw new TrainException(String.format(Message.RETRY_TO_MORE, "okHttp"));
    }

    /**
     * 打印请求信息
     *
     * @param request
     */
    private void printRequestMessage(Request request) {
        if (request == null) {
            return;
        }
        JSONObject logJson = new JSONObject(true);
        logJson.put("Url", request.url().toString());
        logJson.put("Method", request.method());
        //logJson.put("Headers", request.headers().toString());

        RequestBody requestBody = request.body();
        if (requestBody == null) {
            return;
        }
        try {
            Buffer bufferedSink = new Buffer();
            requestBody.writeTo(bufferedSink);
            MediaType mediaType = requestBody.contentType();
            Charset charset = Charset.forName("utf-8");
            charset = mediaType != null && mediaType.charset() != null ? mediaType.charset() : charset;
            logJson.put("Param", bufferedSink.readString(charset));
            log.info("【Request:{}】", logJson.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印响应结果
     *
     * @param response
     */
    private Response printResponseMessage(Response response) throws IOException {

        if (response == null || !response.isSuccessful()) {
            return response;
        }
        ResponseBody responseBody = response.body();
        MediaType contentType = responseBody.contentType();
        long contentLength = responseBody.contentLength();
        JSONObject logJson = new JSONObject(true);
        String ct = contentType.toString();
        Charset charset = contentType.charset();
        logJson.put("ContentType", ct);
        logJson.put("Charset", charset);
        logJson.put("ContentLength", contentLength);
        logJson.put("Headers", response.headers().toString());

        byte[] bytes = response.body().bytes();
        if (charset == null) {
            charset = Charset.forName("UTF-8");
        }
        String result = "";
        if (contentLength != 0) {
            result = new String(bytes, charset);
        }
        if (!ct.contains("image/jpeg")) {
            logJson.put("Body", result);
        }

        log.info("【Response:{}】", logJson.toJSONString());
        return response.newBuilder().body(ResponseBody.create(contentType, bytes)).build();
    }

    private boolean location(HttpUrl httpUrl, String location) throws IOException {
        log.info("Redirect location:{}", location);
        //判断是否与本次请求路径一致
        String url = httpUrl.toString();
        if (location.equals(url) || location.endsWith("error.html")) {
            return false;
        } else if (location.contains("alipay")) {
            return true;
        } else if (location.startsWith("https://kyfw.12306.cn/otn/passport?redirect")) {
            //用户未登录
            throw new NotLoginException(String.format(Message.USER_NOT_LOGIN, "redirect->passport"));
        } else {
            throw new TrainException(String.format(Message.REDIRECT_EXCEPTION, location));
        }
    }
}
