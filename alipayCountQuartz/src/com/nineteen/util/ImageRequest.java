package com.nineteen.util;

import java.io.ByteArrayOutputStream;  
import java.io.File;  
import java.io.FileOutputStream;  
import java.io.IOException;
import java.io.InputStream;  
import java.net.HttpURLConnection;  
import java.net.MalformedURLException;
import java.net.URL;  
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class ImageRequest {  
    /** 
     * @param args 
     */  
    public static void main(String[] args) throws Exception {  
    	String proxy = "192.168.65.126";
    	int port = 3128;
    	  System.setProperty("proxyType", "4");
    	  System.setProperty("proxyPort", Integer.toString(port));
    	  System.setProperty("proxyHost", proxy);
    	  System.setProperty("proxySet", "true");
    	  SSLContext sc = SSLContext.getInstance("SSL");
          sc.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new java.security.SecureRandom());
        //new一个URL对象  
        URL url = new URL("https://omeo.alipay.com/service/checkcode?sessionID=b2fa7b493eb1de4834ccf5fddc0d9ca8&amp;t=0.3439748131976038");  
        //打开链接  
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        // 设置通用的请求属性
        conn.setSSLSocketFactory(sc.getSocketFactory());
        conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
        //设置请求方式为"GET"  
        conn.setRequestMethod("GET");  
        //超时响应时间为3秒  
        conn.setConnectTimeout(3 * 1000);  
        //通过输入流获取图片数据  
        InputStream inStream = conn.getInputStream();  
        //得到图片的二进制数据，以二进制封装得到数据，具有通用性  
        byte[] data = readInputStream(inStream);  
        String currentTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        //new一个文件对象用来保存图片，  
        File imageFile = new File("d://image//"+currentTime+new Random().nextInt(99999)+".png");  
        //创建输出流  
        FileOutputStream outStream = new FileOutputStream(imageFile);  
        //写入数据  
        outStream.write(data);  
        //关闭输出流  
        outStream.close();  
    }  
    public static String Image(String uri,String dir) throws Exception{
    	String proxy = "192.168.65.126";
    	int port = 3128;
    	  System.setProperty("proxyType", "4");
    	  System.setProperty("proxyPort", Integer.toString(port));
    	  System.setProperty("proxyHost", proxy);
    	  System.setProperty("proxySet", "true");
    	  SSLContext sc = SSLContext.getInstance("SSL");
          sc.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new java.security.SecureRandom());
        //new一个URL对象  
        URL url = new URL(uri);  
        //打开链接  
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        // 设置通用的请求属性
        conn.setSSLSocketFactory(sc.getSocketFactory());
        conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
        //设置请求方式为"GET"  
        conn.setRequestMethod("GET");  
        //超时响应时间为3秒  
        conn.setConnectTimeout(3 * 1000);  
        //通过输入流获取图片数据  
        InputStream inStream = conn.getInputStream();  
        //得到图片的二进制数据，以二进制封装得到数据，具有通用性  
        byte[] data = readInputStream(inStream);  
        String currentTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        //new一个文件对象用来保存图片，  
        String img = dir+currentTime+new Random().nextInt(99999)+".png";
        File imageFile = new File(img);  
        //创建输出流  
        FileOutputStream outStream = new FileOutputStream(imageFile);  
        //写入数据  
        outStream.write(data);  
        //关闭输出流  
        outStream.close(); 
        return img;
    }
    public static byte[] readInputStream(InputStream inStream) throws Exception{  
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
        //创建一个Buffer字符串  
        byte[] buffer = new byte[1024];  
        //每次读取的字符串长度，如果为-1，代表全部读取完毕  
        int len = 0;  
        //使用一个输入流从buffer里把数据读取出来  
        while( (len=inStream.read(buffer)) != -1 ){  
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度  
            outStream.write(buffer, 0, len);  
        }  
        //关闭输入流  
        inStream.close();  
        //把outStream里的数据写入内存  
        return outStream.toByteArray();  
    } 
    private static class TrustAnyTrustManager implements X509TrustManager {
    	  
    	  public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    	  }
    	  public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
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
    	  private static TrustManager myX509TrustManager = new X509TrustManager() { 
        	  
    	      @Override 
    	      public X509Certificate[] getAcceptedIssuers() { 
    	          return null; 
    	      } 
    	  
    	      @Override 
    	      public void checkServerTrusted(X509Certificate[] chain, String authType) 
    	     throws CertificateException { 
    	    } 
    	 
    	     @Override 
    	     public void checkClientTrusted(X509Certificate[] chain, String authType) 
    	     throws CertificateException { 
    	     } 
    	 };    
     
}
