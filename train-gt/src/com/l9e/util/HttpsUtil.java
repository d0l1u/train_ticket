package com.l9e.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;

public class HttpsUtil {
    public static SSLContext sc = null;
    private static Logger logger = Logger.getLogger(HttpsUtil.class);

    static {

        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[]{new TrustAnyTrustManager()},
                    new java.security.SecureRandom());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String sendHttps(String urlStr) {
        long start = System.currentTimeMillis();
        String str_return = "";
        URL console;
        HttpsURLConnection conn = null;
        InputStream is = null;
        BufferedReader br = null;
        try {
            console = new URL(urlStr);
            conn = (HttpsURLConnection) console.openConnection();
            conn.setSSLSocketFactory(sc.getSocketFactory());
            conn.setReadTimeout(60000);//1min
            conn.setConnectTimeout(60000);//1min
            conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
            conn.connect();
            is = conn.getInputStream();
            br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String ret = "";
            ret = br.readLine();
            while (ret != null) {
                sb.append(ret);
                ret = br.readLine();
            }
            str_return = sb.toString();
        } catch (MalformedURLException e) {
            logger.info("MalformedURLException", e);
        } catch (IOException e) {
            logger.info("IOException", e);
        } catch (Exception e) {
            logger.info("Exception", e);
        } finally {

            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    logger.info("Exception", e);
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    logger.info("Exception", e);
                }
            }
            if (null != conn) {
                conn.disconnect();
            }

        }
        logger.info((System.currentTimeMillis() - start) + "ms耗时");
        return str_return;
    }

    private static class TrustAnyTrustManager implements X509TrustManager {
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    public static void main(String[] args) {
        System.out.println(HttpsUtil.sendHttps("https://jt.rsscc.com/trainwap/platform/tasks/afterSupplierGetSeat.action"));
    }

}
