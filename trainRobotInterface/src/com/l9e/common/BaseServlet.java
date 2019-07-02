package com.l9e.common;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class BaseServlet{
	public  static String SOUKD_CHARSET = "GBK";
	public  static String SOUKD_METHOD_DGTRAIN = "DGTrain.Asp";
	public  static String SOUKD_USERID = "19e";
	public  static String SOUKD_CHECKCODE = "57342AA7715832648A5DD9CC18F2DA96";
	public  static String SOUKD_URL = "57342AA7715832648A5DD9CC18F2DA96";
	
	public  static String _url_12306 = "https://kyfw.12306.cn/otn/lcxxcx/query?purpose_codes=ADULT";
	
	public static Map<String,String> stationName = new HashMap<String,String>();
	
	
	
	public static Map<String,String> backStationName = new HashMap<String,String>();
	
	public static String getSoukdUrl(Map<String, String> map, String interfaceUrl){
		String fromCity = "";
		String toCity = "";
		
		try {
			fromCity = URLEncoder.encode(map.get("from_station"), SOUKD_CHARSET);
			toCity = URLEncoder.encode(map.get("arrive_station"), SOUKD_CHARSET);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
			
		StringBuffer sb = new StringBuffer();
		if("DGTrain".equals(map.get("method"))){
			sb.append(interfaceUrl)
			  .append(SOUKD_METHOD_DGTRAIN)
			  .append("?FromCity=")
			  .append(fromCity)
			  .append("&ToCity=")
			  .append(toCity)
			  .append("&sDate=")
			  .append(map.get("travel_time"))
			  .append("&UserID=")
			  .append(SOUKD_USERID)
			  .append("&CheckCode=")
			  .append(SOUKD_CHECKCODE);
		}
		return sb.toString();
	}

	public Map<String, String> getStationName() {
		return stationName;
	}

}
