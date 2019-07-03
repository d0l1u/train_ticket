import java.util.Map;

import org.apache.log4j.Logger;

import com.l9e.util.HttpsQueyUtil;
import com.l9e.util.StrUtil;


public class queyTicket {
	

    private static final Logger logger = Logger.getLogger(queyTicket.class);

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		 Map<String, String> result_data = null;
	        int i = 0;
	        do {
	            result_data = HttpsQueyUtil.sendHttpsGET("https://kyfw.12306.cn/otn/leftTicket/queryZ?leftTicketDTO.train_date=2018-02-26&leftTicketDTO.from_station=HZH&leftTicketDTO.to_station=FZS&purpose_codes=ADULT", null);

	            i++;
	        } while (i < 3 && StrUtil.isEmpty(result_data.get("result")));

	        logger.info(result_data);
		
	        
	        try {
	        	
	        	throw new RuntimeException("车次列表为空");
	        }catch(Exception e) {
	        	System.out.println(e.getMessage());
	        }
		
		
	}

}
