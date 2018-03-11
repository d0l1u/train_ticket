package test.l9e.tuniu.test;

import java.util.HashMap;
import java.util.Map;

import com.l9e.util.HttpUtil;
import com.l9e.util.UrlFormatUtil;

public class RequestTest {

	public static void main(String[] args) {
		Map<String,String> paramMap=new HashMap<String, String>();
		String url= "";
		paramMap.put("channel", "tongcheng");
		paramMap.put("from_station", "NJH");
		paramMap.put("arrive_station", "SHH");
		paramMap.put("travel_time", "2015-12-11");
		paramMap.put("isNotZW", "no");//非中文查询
		String params;
		params = UrlFormatUtil.createUrl(url, paramMap);
		String result = HttpUtil.sendByPost(url, params, "UTF-8");
	}
}
