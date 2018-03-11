package test.l9e.tuniu.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.l9e.util.HttpUtil;
import com.l9e.util.UrlFormatUtil;

public class QueryTicketTest {

	public static void main(String[] args) throws JsonProcessingException, IOException {
		String url = "http://192.168.63.245:37055/queryTicket";
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("travel_time", "2015-11-30");
		paramMap.put("from_station", "银川");
		paramMap.put("arrive_station", "成都");
		paramMap.put("channel", "ext");
		
		String params = UrlFormatUtil.createUrl("", paramMap);
		System.out.println(params);
		String result = HttpUtil.sendByPost(url, params, "UTF-8");
		
		System.out.println(result);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(result);
		Iterator<String> ite = root.fieldNames();
		while(ite.hasNext()) {
			String name = ite.next();
			JsonNode node = root.path(name);
			if(node.isMissingNode()) {
				
			} else {
				System.out.println(name + " : " + node.toString());
				if(node.isArray()) {
					System.out.println("array size = " + node.size());
					for(int i = 0; i < node.size();i++) {
						JsonNode n = node.get(i);
						Iterator<String> itee = n.fieldNames();
						while(itee.hasNext()) {
							String namee = itee.next();
							JsonNode valueNode = n.get(namee);
							System.out.println(namee + " : " + valueNode.toString());
//							System.out.println(valueNode.getNodeType());
						}
						System.out.println("-------------------------");
						System.out.println("---                  ----");
						System.out.println("-------------------------");
					}
//					Iterator<JsonNode> ele = node.iterator();
//					while(ele.hasNext()) {
//						JsonNode n = ele.next();
//						Iterator<String> itee = n.fieldNames();
//						while(itee.hasNext()) {
//							String namee = itee.next();
//							JsonNode valueNode = n.get(namee);
//							System.out.println(namee + " : " + valueNode.asText());
//						}
//						System.out.println("-------------------------");
//						System.out.println("---                  ----");
//						System.out.println("-------------------------");
//					}
				} else {
					
				}
			}
		}
	}
}
