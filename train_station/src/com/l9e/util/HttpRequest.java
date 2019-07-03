package com.l9e.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.l9e.transaction.vo.ProxyEntity;
 
public class HttpRequest {
	
	public static Logger logger = Logger.getLogger(HttpRequest.class);
	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String sendGet(String url, String param ,int timeout,ProxyEntity entity) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url + "?" + param;
			URL realUrl = new URL(urlNameString);
			Proxy proxy = null;
			URLConnection connection=null;
			if (entity!=null) { 
				proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(entity.getHost(),entity.getPort()));   
				connection = realUrl.openConnection(proxy);
			}else{
				connection = realUrl.openConnection();
			}
			// 打开和URL之间的连接
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			if(entity!=null)
				connection.setRequestProperty("Proxy-Authorization","Basic " + Base64.getBase64(entity.getUserName()+":"+entity.getPassword()));
			connection.setReadTimeout(timeout);
			connection.setConnectTimeout(timeout);
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
//			for (String key : map.keySet()) {
//				System.out.println(key + "--->" + map.get(key));
//			}
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection
					.getInputStream(), "gbk"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			} 
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String param,int timeout,ProxyEntity entity) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			Proxy proxy = null;
			URLConnection conn=null;
			if (entity!=null) { 
				proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(entity.getHost(),entity.getPort()));   
				conn = realUrl.openConnection(proxy);
			}else{
				conn = realUrl.openConnection();
			}
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			if(entity!=null)
				conn.setRequestProperty("Proxy-Authorization","Basic " + Base64.getBase64(entity.getUserName()+":"+entity.getPassword()));
			conn.setReadTimeout(timeout);
			conn.setConnectTimeout(timeout);
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream(), "gbk"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			logger.info("代理IP:"+entity.getHost()+",发送 POST 请求出现异常！"+e.getMessage(),e);
			e.printStackTrace();	
			result="Exception";
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	public static void main(String[] args) {
		// 发送 GET 请求
//		String s = HttpRequest.sendGet(
//				"http://trains.ctrip.com/TrainBooking/Ajax/SearchListHandler.ashx", 
//				"Action=getSearchList&value=%7B%22IsAll%22:false,%22Filter%22:%220%22,%22Catalog%22:%22%22,%22IsGaoTie%22:false,%22IsDongChe%22:false,%22CatalogName%22:%22%22,%22DepartureCity%22:%22beijing%22,%22ArrivalCity%22:%22shanghai%22,%22HubCity%22:%22%22,%22DepartureCityName%22:%22%E5%8C%97%E4%BA%AC%22,%22ArrivalCityName%22:%22%E4%B8%8A%E6%B5%B7%22,%22DepartureDate%22:%222016-07-15%22,%22DepartureDateReturn%22:%222016-07-18%22,%22ArrivalDate%22:%22%22,%22TrainNumber%22:%22%22%7D%0A");
//		System.out.println("++++++"+s);
//http://trains.ctrip.com/TrainBooking/Ajax/SearchListHandler.ashx?Action=getSearchList&value=%7B%22IsAll%22:false,%22Filter%22:%220%22,%22Catalog%22:%22%22,%22IsGaoTie%22:false,%22IsDongChe%22:false,%22CatalogName%22:%22%22,%22DepartureCity%22:%22beijing%22,%22ArrivalCity%22:%22shanghai%22,%22HubCity%22:%22%22,%22DepartureCityName%22:%22%E5%8C%97%E4%BA%AC%22,%22ArrivalCityName%22:%22%E4%B8%8A%E6%B5%B7%22,%22DepartureDate%22:%222016-07-15%22,%22DepartureDateReturn%22:%222016-07-18%22,%22ArrivalDate%22:%22%22,%22TrainNumber%22:%22%22%7D%0A
		// 发送 POST 请求
		/*String sr = HttpRequest
				.sendPost("http://www.baidu.com",
						"");
		System.out.println(sr);*/
	}

}
