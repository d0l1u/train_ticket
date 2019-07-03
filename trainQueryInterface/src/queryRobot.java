import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;

import org.apache.log4j.Logger;

import com.l9e.util.DateUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.HttpsQueyUtil;


public class queryRobot {
	
	private static Logger logger = Logger.getLogger(queryRobot.class);
	
	public static void main(String[] args) {
		while (true) {
			String url="http://43.241.226.93:18101/queryTicket?query_type=02&url=Z";
			String travel_time = "2018-02-28";
			String from_station = "VBP";
			String arrive_station = "JMP";
			
			StringBuffer params = new StringBuffer();
			params.append("travel_time=").append(travel_time)
					.append("&from_station=").append(from_station)
					.append("&arrive_station=").append(arrive_station);
			
			logger.info(params.toString());
			
			try {
				logger.info("java program query :"+URLDecoder.decode(params.toString(), "utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			String jsonStr = HttpUtil.sendByPost(url, params.toString(), "UTF-8");
			logger.info(jsonStr.toString());
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	
	}

}
