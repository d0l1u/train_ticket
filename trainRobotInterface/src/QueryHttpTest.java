import java.util.LinkedHashMap;
import java.util.Map;

import com.l9e.util.HttpUtil;


/**
 * @author meizs
 * 测试查询
 */
public class QueryHttpTest {

	public static void main(String[] args) {
		
		 StringBuffer buffer=new StringBuffer();
		 buffer.append("from_station=").append("上海").append("&");
		 buffer.append("arrive_station=").append("重庆北").append("&");
		 buffer.append("travel_time=").append("2018-02-27").append("&");
		 buffer.append("purpose_codes=").append("ADULT").append("&");
		 buffer.append("channel=").append("ext_meizs");
		 
		 String params=buffer.toString();
		 String result =HttpUtil.sendByPost("http://127.0.0.1:8080/queryTicket", params, "utf-8");
		 System.err.println(result);
		 
		//   curl http://127.0.0.1:8080/queryTicket
		//   上海虹桥   深圳北 
		//  curl -d "from_station=上海虹桥&arrive_station=深圳北&travel_time=2018-02-04&purpose_codes=ADULT&channel=19e" "http://10.3.12.95:18013/queryTicket"

		 
	}

}
