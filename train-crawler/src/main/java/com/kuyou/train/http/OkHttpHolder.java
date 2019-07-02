package com.kuyou.train.http;

import com.kuyou.train.http.cert.TrustAllCerts;
import com.kuyou.train.http.cookie.CacheCookieJar;
import com.kuyou.train.http.factory.OkHttpClientFactoryBean;
import com.kuyou.train.http.interceptor.HttpEventListener;
import com.kuyou.train.http.interceptor.RetryInterceptor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

/**
 * OkHttpHolder
 *
 * @author liujia33
 * @date 2018/9/27
 */
@Slf4j
public class OkHttpHolder extends OkHttpClientFactoryBean {

    @Override
    public OkHttpClient getObject() {
        X509TrustManager x509TrustManager = new TrustAllCerts();
        return new OkHttpClient.Builder()
                //禁制OkHttp的重定向自动跳转操作
                .followRedirects(false)
                //禁制OkHttp的重定向自动跳转操作
                .followSslRedirects(false)
                //连接超时：与服务器建立连接的时间
                .connectTimeout(15, TimeUnit.SECONDS)
                //读取超时：设置有意义,在服务器处理能力差，但最终会响应的情况下，可以将客户端的等待响应时间设长一些。如果太长的话，由于客户端使用的是BIO的方式，线程会一直阻塞在IO而导致挂起。
                .readTimeout(30, TimeUnit.SECONDS)
                //写超时：一般不象connection timeout和read timeout可以在客户端显示调值，TCP有写重传的概念，一般8m内会重试，否则，直接断开连接
                .writeTimeout(30, TimeUnit.SECONDS)
                //cookie管理
                .cookieJar(new CacheCookieJar())
                //链接超时重试异常
                .addInterceptor(new RetryInterceptor())
                //ssl，信任所有证书
                .sslSocketFactory(createSSLSocketFactory(x509TrustManager), x509TrustManager)
                //事件监听
                .eventListenerFactory(HttpEventListener.FACTORY)
                // 代理，用于本地测试
                //.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 8888)))
                .build();
    }

    private static SSLSocketFactory createSSLSocketFactory(X509TrustManager x509TrustManager) {
        SSLSocketFactory sslSocketFactory = null;
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{x509TrustManager}, new SecureRandom());
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (Exception e) {
            log.info("Create SSLSocketFactory error.", e);
        }
        return sslSocketFactory;
    }
}
