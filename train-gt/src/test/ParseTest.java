package test;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import com.l9e.transaction.vo.BookInfo;
import com.l9e.util.ParamCheckUtil;

import net.sf.json.JSONArray;

public class ParseTest {

	@Test
	public void test() throws JsonParseException, JsonMappingException, IOException {
		BookInfo bookInfo = null;
		JSONArray users = new JSONArray();
		String jsonParam = "{\r\n" + "	\"wz_ext\": \"0\",\r\n" + "	\"order_pro1\": \"\",\r\n"
				+ "	\"merchant_order_id\": \"D30709102447541010\",\r\n" + "	\"link_phone\": \"\",\r\n"
				+ "	\"link_address\": \"\",\r\n" + "	\"order_pro2\": \"\",\r\n"
				+ "	\"chooseSeats\": \"1A1B1C1D\",\r\n" + "	\"link_mail\": \"\",\r\n" + "	\"sum_amount\": 388,\r\n"
				+ "	\"travel_time\": \"2018-07-16\",\r\n" + "	\"bx_invoice\": \"0\",\r\n"
				+ "	\"book_detail_list\": [{\r\n" + "		\"cp_id\": \"D307091024475410100\",\r\n"
				+ "		\"user_name\": \"徐虎\",\r\n" + "		\"ticket_type\": \"1\",\r\n"
				+ "		\"ids_type\": \"2\",\r\n" + "		\"bx\": \"0\",\r\n"
				+ "		\"user_ids\": \"612321198611271118\"\r\n" + "	}, {\r\n"
				+ "		\"cp_id\": \"D307091024475410101\",\r\n" + "		\"user_name\": \"徐虎\",\r\n"
				+ "		\"ticket_type\": \"2\",\r\n" + "		\"ids_type\": \"2\",\r\n" + "		\"bx\": \"0\",\r\n"
				+ "		\"user_ids\": \"612321198611271118\"\r\n" + "	}, {\r\n"
				+ "		\"cp_id\": \"D307091024475410102\",\r\n" + "		\"user_name\": \"黄凤清\",\r\n"
				+ "		\"ticket_type\": \"0\",\r\n" + "		\"ids_type\": \"2\",\r\n" + "		\"bx\": \"0\",\r\n"
				+ "		\"user_ids\": \"612321194308091626\"\r\n" + "	}, {\r\n"
				+ "		\"cp_id\": \"D307091024475410103\",\r\n" + "		\"user_name\": \"徐朝新\",\r\n"
				+ "		\"ticket_type\": \"0\",\r\n" + "		\"ids_type\": \"2\",\r\n" + "		\"bx\": \"0\",\r\n"
				+ "		\"user_ids\": \"612321196404011613\"\r\n" + "	}],\r\n" + "	\"from_time\": \"10:24\",\r\n"
				+ "	\"from_station\": \"汉中\",\r\n" + "	\"bx_invoice_receiver\": \"\",\r\n"
				+ "	\"bx_invoice_phone\": \"\",\r\n"
				+ "	\"order_result_url\": \"https://jt.rsscc.com/trainwap/platform/tasks/buyCallblack419e.action\",\r\n"
				+ "	\"train_code\": \"D6862\",\r\n" + "	\"book_result_url\": \"\",\r\n" + "	\"link_name\": \"\",\r\n"
				+ "	\"seat_type\": \"3\",\r\n" + "	\"bx_invoice_address\": \"\",\r\n"
				+ "	\"isChooseSeats\": true,\r\n" + "	\"arrive_time\": \"11:55\",\r\n"
				+ "	\"ticket_price\": 97,\r\n" + "	\"sms_notify\": \"0\",\r\n" + "	\"bx_invoice_zipcode\": \"\",\r\n"
				+ "	\"arrive_station\": \"西安北\",\r\n" + "	\"order_level\": \"0\"\r\n" + "}";

		bookInfo = new ObjectMapper().readValue(jsonParam, BookInfo.class);

		boolean infoCheck = ParamCheckUtil.bookDetailInfoCheck(bookInfo.getBook_detail_list(),
				bookInfo.getOrder_level());
		System.err.println("infoCheck:" + infoCheck);
	}
}
