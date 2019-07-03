package test.l9e.tuniu.test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.l9e.util.HttpUtil;

public class UrlBuilder {

	public static void main(String[] args) throws UnsupportedEncodingException {
		
//		System.getProperties().setProperty("http.proxyHost","192.168.65.126");
//		System.getProperties().setProperty("http.proxyPort", "3128" );
		String url = "https://kyfw.12306.cn/otn/lcxxcx/query?purpose_codes=ADULT";
		
		String travel_time = "2015-11-30";
		String from_station = "西安北";
		from_station = URLEncoder.encode(from_station, "GBK");
		String to_station = "北京西";
		to_station = URLEncoder.encode(to_station, "GBK");
		StringBuilder builder = new StringBuilder();
		
		builder.append(url).append("&queryDate=").append(travel_time).append("&from_station=").append(from_station).append("&to_station=").append(to_station);

		String requestUrl = builder.toString();
		System.out.println(requestUrl);
		String result = HttpUtil.sendByGet(requestUrl, "UTF-8", "30000", "30000");
		
		System.out.println(result);
	}
}
