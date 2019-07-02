package com.kuyou.train.http.interceptor;

import com.kuyou.train.common.enums.KeyEnum;
import com.kuyou.train.common.jedis.JedisClient;
import com.kuyou.train.common.util.IpUtil;
import com.kuyou.train.common.util.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * HttpEventListener：网络耗时监控
 *
 * @author taokai3
 * @date 2018/9/27
 */
@Slf4j
public class HttpEventListener extends EventListener {

    private static final long MAX_TIME = 1000 * 10;

    /**
     * 自定义EventListener工厂
     */
    public static final Factory FACTORY = call -> new HttpEventListener(call.request().url(), System.currentTimeMillis());

    /**
     * 每次请求的开始时间，单位毫秒
     */
    private final long callStart;
    private StringBuilder logBuilder;

    public HttpEventListener( HttpUrl url, long callStart) {
        this.callStart = callStart;
        this.logBuilder = new StringBuilder("[").append(url.toString()).append("]{");
    }

    private void recordEventLog(String name) {
        long elapseMilliseconds = System.currentTimeMillis() - callStart;
        logBuilder.append("\"").append(name).append("\":").append(elapseMilliseconds).append(",");

        if (name.equalsIgnoreCase("callEnd") || name.equalsIgnoreCase("callFailed")) {
            //打印出每个步骤的时间点
            log.debug(logBuilder.delete(logBuilder.length() - 1, logBuilder.length()).append("}").toString());
            /*
            try {
                JedisClient jedisClient = SpringContextUtil.getBean("ipJedisClient", JedisClient.class);
                //获取内网IP
                String innetIp = IpUtil.getInnetIp();
                String key = KeyEnum.IP_CHECK.getValue();
                //判断耗时是否超过指定毫秒值，如果是，则修改redis排序
                if(elapseMilliseconds > MAX_TIME){
                    Double zincrby = jedisClient.zincrby(key, 1, innetIp);
                    log.info("ip:{}-zincrby:{}", innetIp, zincrby);
                } else {
                    //判断是否存在redis中
                    Double zscore = jedisClient.zscore(key, innetIp);
                    if(zscore == null){
                        zscore = Double.valueOf(0);
                        zscore = jedisClient.zincrby(key, 0, innetIp);
                    }
                    log.info("ip:{}-zscore:{}", innetIp, zscore);
                }
            }catch (Exception e){
                log.info("统计IP效率异常");
            }
            */
        }
    }

    @Override
    public void callStart(Call call) {
        super.callStart(call);
        recordEventLog("callStart");
    }

    @Override
    public void dnsStart(Call call, String domainName) {
        super.dnsStart(call, domainName);
        recordEventLog("dnsStart");
    }

    @Override
    public void dnsEnd(Call call, String domainName, List<InetAddress> inetAddressList) {
        super.dnsEnd(call, domainName, inetAddressList);
        recordEventLog("dnsEnd");
    }

    @Override
    public void connectStart(Call call, InetSocketAddress inetSocketAddress, Proxy proxy) {
        super.connectStart(call, inetSocketAddress, proxy);
        recordEventLog("connectStart");
    }

    @Override
    public void secureConnectStart(Call call) {
        super.secureConnectStart(call);
        recordEventLog("secureConnectStart");
    }

    @Override
    public void secureConnectEnd(Call call, Handshake handshake) {
        super.secureConnectEnd(call, handshake);
        recordEventLog("secureConnectEnd");
    }

    @Override
    public void connectEnd(Call call, InetSocketAddress inetSocketAddress, Proxy proxy, Protocol protocol) {
        super.connectEnd(call, inetSocketAddress, proxy, protocol);
        recordEventLog("connectEnd");
    }

    @Override
    public void connectFailed(Call call, InetSocketAddress inetSocketAddress, Proxy proxy, Protocol protocol,
            IOException ioe) {
        super.connectFailed(call, inetSocketAddress, proxy, protocol, ioe);
        recordEventLog("connectFailed");
    }

    @Override
    public void connectionAcquired(Call call, Connection connection) {
        super.connectionAcquired(call, connection);
        recordEventLog("connectionAcquired");
    }

    @Override
    public void connectionReleased(Call call, Connection connection) {
        super.connectionReleased(call, connection);
        recordEventLog("connectionReleased");
    }

    @Override
    public void requestHeadersStart(Call call) {
        super.requestHeadersStart(call);
        recordEventLog("requestHeadersStart");
    }

    @Override
    public void requestHeadersEnd(Call call, Request request) {
        super.requestHeadersEnd(call, request);
        recordEventLog("requestHeadersEnd");
    }

    @Override
    public void requestBodyStart(Call call) {
        super.requestBodyStart(call);
        recordEventLog("requestBodyStart");
    }

    @Override
    public void requestBodyEnd(Call call, long byteCount) {
        super.requestBodyEnd(call, byteCount);
        recordEventLog("requestBodyEnd");
    }

    @Override
    public void responseHeadersStart(Call call) {
        super.responseHeadersStart(call);
        recordEventLog("responseHeadersStart");
    }

    @Override
    public void responseHeadersEnd(Call call, Response response) {
        super.responseHeadersEnd(call, response);
        recordEventLog("responseHeadersEnd");
    }

    @Override
    public void responseBodyStart(Call call) {
        super.responseBodyStart(call);
        recordEventLog("responseBodyStart");
    }

    @Override
    public void responseBodyEnd(Call call, long byteCount) {
        super.responseBodyEnd(call, byteCount);
        recordEventLog("responseBodyEnd");
    }

    @Override
    public void callEnd(Call call) {
        super.callEnd(call);
        recordEventLog("callEnd");
    }

    @Override
    public void callFailed(Call call, IOException ioe) {
        super.callFailed(call, ioe);
        recordEventLog("callFailed");
    }
}

