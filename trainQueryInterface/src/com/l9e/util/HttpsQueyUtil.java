package com.l9e.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;

public class HttpsQueyUtil {


    public static SSLContext sc = null;
    private static Logger logger = Logger.getLogger(HttpsQueyUtil.class);

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

    /**
     * 返回值包含result,cookie
     *
     * @param urlStr
     * @return
     */
    public static Map<String, String> sendHttpsGET(String urlStr, String cookieStr) {

        long start = System.currentTimeMillis();
        String str_return = "";
        URL console;
        Map<String, String> map = new HashMap<String, String>(2);
      //  Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("121.40.79.162", 8888));
        try {
            console = new URL(urlStr);
      /*      HttpsURLConnection conn = (HttpsURLConnection) console
                    .openConnection(proxy);*/
            HttpsURLConnection conn = (HttpsURLConnection) console
                    .openConnection();
            conn.setSSLSocketFactory(sc.getSocketFactory());
            conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
            conn.setRequestMethod("GET");
            if (StrUtil.isEmpty(cookieStr)) {  //true
                logger.info("cookie is empty");
            } else {
                conn.setRequestProperty("Cookie", cookieStr);
            }

         /*   String encoded = Base64Util.getBASE64("mzs:mzs");
            conn.setRequestProperty("Proxy-Authorization", "Basic " + encoded);*/
    
            conn.setRequestProperty("Host","kyfw.12306.cn");
            conn.setRequestProperty("Connection","keep-alive");
            conn.setRequestProperty("Cache-Control","no-cache");
            conn.setRequestProperty("Accept","*/*");
            conn.setRequestProperty("X-Requested-With","XMLHttpRequest");
            conn.setRequestProperty("If-Modified-Since","0");
            conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.221 Safari/537.36 SE 2.X MetaSr 1.0");
            conn.setRequestProperty("Referer","https://kyfw.12306.cn/otn/leftTicket/init");
            conn.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch");
            conn.setRequestProperty("Accept-Language","zh-CN,zh;q=0.8");
            			
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
            StringBuffer sb = new StringBuffer();
            BufferedInputStream bis = new BufferedInputStream(is);
            String ret = "";
            ret = br.readLine();
            while (ret != null) {
                sb.append(ret);
                ret = br.readLine();
            }
            if (br != null) {
                br.close();
            }
            if (is != null) {
                is.close();
            }

            Map<String, List<String>> responseHeader = conn.getHeaderFields();
            Set<Map.Entry<String, List<String>>> entry = responseHeader.entrySet();
            for (Map.Entry<String, List<String>> entryOne : entry) {
                logger.info(entryOne.getKey() + "," + "****>" + entryOne.getValue());
            }

            List<String> setCookieList = responseHeader.get("Set-Cookie");
            logger.info("*****" + conn.getResponseCode());
            logger.info("$$$$$$" + setCookieList);
            String cookie = "";

            if (null != setCookieList && !setCookieList.isEmpty()) {
                cookie = handlerCookie(setCookieList);
            }

            conn.disconnect();

            str_return = sb.toString();

            if (conn.getResponseCode() == 200) {
                map.put("cookie", cookie);
                map.put("result", str_return);
            } else {
                map.clear();
            }

        } catch (MalformedURLException e) {
            map.clear();
            logger.info(e.getMessage(), e);
        } catch (IOException e) {
            map.clear();
            logger.info(e.getMessage(), e);
        } catch (Exception e) {
            map.clear();
            logger.info(e.getMessage(), e);
        }
        logger.info((System.currentTimeMillis() - start) + "ms耗时");

        return map;
    }


    public static String handlerCookie(List<String> setCookie) {

        Iterator<String> it = setCookie.iterator();
        StringBuffer sbu = new StringBuffer();
        while (it.hasNext()) {
            String dot = it.next();
            String[] dot1 = dot.split(";");
            sbu.append(dot1[0]).append("; ");
        }
        logger.info("******:" + sbu.toString());
        String result = sbu.substring(0, sbu.lastIndexOf("; "));
        return result;
    }


    public static Map<String, String> sendHttpsPOST(String urlStr, String params, String cookieStr) {
        long start = System.currentTimeMillis();
        String str_return = "";
        URL console;
        Map<String, String> map = new HashMap<String, String>(2);
        try {
            console = new URL(urlStr);
            HttpsURLConnection conn = (HttpsURLConnection) console
                    .openConnection();

            conn.setSSLSocketFactory(sc.getSocketFactory());
            conn.setHostnameVerifier(new TrustAnyHostnameVerifier());

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            if (StrUtil.isEmpty(cookieStr)) {  //true
                logger.info("cookie is empty");
            } else {
                conn.setRequestProperty("Cookie", cookieStr);
            }

            conn.connect();
            OutputStreamWriter reqOut = new OutputStreamWriter(conn.getOutputStream());
            reqOut.write(params);
            reqOut.flush();

            InputStream is = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuffer sb = new StringBuffer();
            BufferedInputStream bis = new BufferedInputStream(is);
            String ret = "";
            ret = br.readLine();
            while (ret != null) {
                sb.append(ret);
                ret = br.readLine();
            }
            if (br != null) {
                br.close();
            }
            if (is != null) {
                is.close();
            }

            Map<String, List<String>> responseHeader = conn.getHeaderFields();
            Set<Map.Entry<String, List<String>>> entry = responseHeader.entrySet();
            for (Map.Entry<String, List<String>> entryOne : entry) {
                logger.info(entryOne.getKey() + "," + "****>" + entryOne.getValue());
            }

            List<String> setCookieList = responseHeader.get("Set-Cookie");
            logger.info("*****" + conn.getResponseCode());
            logger.info("$$$$$$" + setCookieList);
            String cookie = "";

            if (null != setCookieList && !setCookieList.isEmpty()) {
                cookie = handlerCookie(setCookieList);
            }

            if (conn.getResponseCode() == 200) {
                map.put("cookie", cookie);
                map.put("result", str_return);
            } else {
                map.clear();
            }

            conn.disconnect();
            str_return = sb.toString();

        } catch (MalformedURLException e) {
            map.clear();
            logger.info(e.getMessage(), e);
        } catch (IOException e) {
            map.clear();
            logger.info(e.getMessage(), e);
        } catch (Exception e) {
            map.clear();
            logger.info(e.getMessage(), e);
        }


        logger.info((System.currentTimeMillis() - start) + "ms耗时");
        return map;
    }


    private static class TrustAnyTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

}
