package test.l9e.tuniu.test;

import java.util.HashMap;
import java.util.Map;

import com.l9e.util.HttpUtil;
import com.l9e.util.UrlFormatUtil;

public class RequestTest {

	public static void main(String[] args) {
//		getPhone();//获取手机号
		send12306CheckCodeMsg();//发送12306短信
	}
	
	public static void getPhone() {
		String url = "http://www.19trip.com/train_phone/phone/getPhone";
		String params = "count=1";
		String result = HttpUtil.sendByPost(url, params, "utf-8");
		System.out.println(result);
	}
	
	public static void send12306CheckCodeMsg() {
		String url = "http://www.19trip.com/train_phone/phone/sendMsg";
		String number = "13309640041";
		String dest = "12306";
		String message = "999";
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("number", number);
		paramMap.put("dest", dest);
		paramMap.put("message", message);
		
		String params = UrlFormatUtil.createUrl("", paramMap);
		String result = HttpUtil.sendByPost(url, params, "utf-8");
		System.out.println(result);
	}
}
