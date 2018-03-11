package test.l9e.tuniu.test;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpRequestTest {

	public static void main(String[] args) throws IOException {
		String result = null;
//		String url = "http://115.28.72.120:18027/queryTicket";
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("travel_time", "2015-12-30");
//		params.put("from_station", "BJP");
//		params.put("arrive_station", "SHH");
////		String url = "https://kyfw.12306.cn/otn/resources/js/framework/station_name.js";
//		System.getProperties().setProperty("http.proxyHost","192.168.65.126");
//		System.getProperties().setProperty("http.proxyPort", "3128" );
////		String result = SSLUtil.httpsGet(url, "UTF-8", 30000, 30000);
//		String result = HttpUtil.sendByPost(url, UrlFormatUtil.createUrl("", params), "utf-8");
////		SocketAddress address = new InetSocketAddress("192.168.65.126", 3128);
////		Proxy proxy = new Proxy(Type.HTTP, address);
////		String result = SSLUtil1.httpsGet(url, "utf-8", proxy, 30000, 30000);
		parse(result);
	}
	
	public static void parse(String json) throws JsonProcessingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
//		JsonNode datas = mapper.readTree(json);
		JsonNode root = mapper.readTree(new File("test/info"));
		System.out.println(root.toString());
		JsonNode datas = root.path("datas");
		if(!datas.isMissingNode() && datas.isArray()) {
			for(int i = 0; i < datas.size(); i++) {
				JsonNode train = datas.get(i);
				System.out.println("-------------------->>>" + i);
				if(!train.isMissingNode()) {
					Iterator<Entry<String, JsonNode>> fieldsIter = train.fields();
					while(fieldsIter.hasNext()) {
						Entry<String, JsonNode> field = fieldsIter.next();
						System.out.println(field.getKey() + " : " + field.getValue().toString());
					}
				}
			}
		}
	}
}
