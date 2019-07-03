package test.l9e.tuniu.test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import test.l9e.tuniu.test.util.HttpUtilNew;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.l9e.transaction.vo.TuniuPassenger;
import com.l9e.util.EncryptUtil;
import com.l9e.util.MD5Util;

public class TuniuRequestTest {
	
	public static String signKey = "f121bb2174d27f7b76682903cda672f0";

	public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException {
//		testSearch();//余票查询接口
//		testBook();//预订下单接口
//		testOut();//出票确认
//		testStations();//经停站查询
		testRefund();//线上退票
	}
	
	public static void testStations() throws JsonGenerationException, JsonMappingException, IOException {
		String url = "http://192.168.63.245:28354/tuniu/train/queryStations";
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("trainDate", "2015-11-30");
		param.put("trainCode", "4643");
		
		execMethod(url, param);
	}
	
	public static void testBook() throws JsonGenerationException, JsonMappingException, IOException {
		String url = "http://192.168.63.245:28354/tuniu/train/book";
//		url = "http://127.0.0.1:8805/tuniu/train/book";
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("orderId", "TN_9C42027A60F8E272");
		param.put("cheCi", "6056");
		param.put("fromStationCode", "BTC");
		param.put("fromStationName", "包头");
		param.put("toStationCode", "BDC");
		param.put("toStationName", "包头东");
		param.put("trainDate", "2015-12-30");
		param.put("callBackUrl", "http://192.168.12.38:8805/show");
		param.put("contact", "李成");
		param.put("phone", "18811528479");
		param.put("insureCode ", "");
		List<TuniuPassenger> passengers = new ArrayList<TuniuPassenger>();
		passengers.add(getPassenger());
		param.put("passengers", passengers);
		
		execMethod(url, param);
		
	}
	
	public static void testOut() throws JsonGenerationException, JsonMappingException, IOException {
		String url = "http://192.168.63.245:28354/tuniu/train/confirm";
		
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("vendorOrderId", "TN_9C42027A60F8E271");
		param.put("orderId", "TN_9C42027A60F8E271");
		param.put("callBackUrl", "http://192.168.12.38:8805/show");
		
		execMethod(url, param);
	}
	
	public static void testSearch() throws JsonGenerationException, JsonMappingException, IOException {
		String url = "http://www.19trip.com/train-tuniu/train/search";
		
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("trainDate", "2015-11-30");
		param.put("fromStation", "BJP");
		param.put("toStation", "SHH");
		param.put("trainCode", "");
		
		String str = execMethod(url, param);
		
		showQuery(str);
	}
	
	public static void testRefund() throws JsonGenerationException, JsonMappingException, IOException {
		String url = "http://www.19trip.com/train-tuniu/train/return";
		
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("orderId", "486100918");
		param.put("vendorOrderId", "486100918");
		param.put("orderNumber", "EC93386379");
		param.put("callBackUrl", "http://www.19trip.com/train-tuniu/outTicket/refundCallback");
		List<Map<String, Object>> tickets = new ArrayList<Map<String,Object>>();
		tickets.add(getTicket());
		param.put("tickets", tickets);
		
		execMethod(url, param);
	}
	
	public static Map<String, Object> getTicket() {
		Map<String, Object> ticket = new HashMap<String, Object>();
		ticket.put("ticketNo", "TN_5df91c9a4f814d14");
		ticket.put("passengerName", "李雁");
		ticket.put("passportTypeId", "1");
		ticket.put("passportNo", "441402197012292311");
		return ticket;
	}
	
	public static TuniuPassenger getStudent() {
		TuniuPassenger passenger = new TuniuPassenger();
		passenger.setPassengerId(13985573);
		passenger.setUserName("张三");
		passenger.setUserIds("610103197598242518");
		passenger.setTuniuIdsType("1");
		passenger.setPassportTypeName("二代身份证");
		passenger.setTuniuTicketType("3");
		passenger.setPiaoTypeName("学生票");
		passenger.setTuniuSeatType("1");
		passenger.setZwName("硬座");
		passenger.setPayMoney(156.0);
		
		passenger.setProvinceCode("610");
		passenger.setSchoolCode("3577");
		passenger.setSchoolName("哈佛");
		passenger.setStudentNo("ES00984452");
		passenger.setSchoolSystem("FULL");
		passenger.setEnterYear("2007");
		passenger.setPreferenceFromStationCode("BZH");
		passenger.setPreferenceFromStationName("亳州");
		passenger.setPreferenceToStationCode("RZH");
		passenger.setPreferenceToStationName("温州");
		return passenger;
	}
	
	public static TuniuPassenger getPassenger() {
		TuniuPassenger passenger = new TuniuPassenger();
		passenger.setPassengerId(13985573);
		passenger.setUserName("李成");
		passenger.setUserIds("610103198810232415");
		passenger.setTuniuIdsType("1");
		passenger.setPassportTypeName("二代身份证");
		passenger.setTuniuTicketType("1");
		passenger.setPiaoTypeName("成人票");
		passenger.setTuniuSeatType("1");
		passenger.setZwName("硬座");
		passenger.setPayMoney(1.0);
		return passenger;
	}
	
	public static String execMethod(String url, Map<String, Object> param) throws JsonGenerationException, JsonMappingException, IOException {
		String account = "tuniu19e";
		
		String timestamp = getTimestamp();
		System.out.println("timestamp : " + timestamp);
		String data = jsonStr(param);
		
		System.out.println("data : " + data);
		String dataEncrypt = encrypt(data);
		System.out.println("data_base64 : " + dataEncrypt);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("timestamp", timestamp);
		paramMap.put("account", account);
		paramMap.put("data", dataEncrypt);
		
		String sign = getSign(account, timestamp, dataEncrypt);
		System.out.println("sign : " + sign);
		paramMap.put("sign", sign);
		
		String params = jsonStr(paramMap);
		System.out.println("POST请求参数 : " + params);
		
		String result = HttpUtilNew.sendByPost(url, params, "UTF-8");
		System.out.println("请求返回 result : " + result);
		return result;
	}
	
	public static void showQuery(String jsonStr) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode root = mapper.readTree(jsonStr);
			JsonNode data = root.path("data");
			if(!data.isMissingNode() && data.isArray()) {
				for(int i = 0; i<data.size();i++) {
					JsonNode train = data.get(i);
					System.out.println(train.toString());
				}
			}
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	/**
	 * json字符串
	 * @param map
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static String jsonStr(Map<String, Object> map) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		return mapper.writeValueAsString(map);
	}
	
	/**
	 * 获取当前时间戳
	 * @return
	 */
	public static String getTimestamp() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
		Date current = new Date();
//		long diff = 60 * 1000 * 5;
		long diff = 0;
		long millis = diff + current.getTime();
		return format.format(new Date(millis));
	}
	
	/**
	 * 获取签名
	 * @param account
	 * @param timestamp
	 * @param dataBase64
	 * @return
	 */
	public static String getSign(String account, String timestamp, String dataBase64) {
		String sign = "";
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("account", account);
		params.put("timestamp", timestamp);
		params.put("data", dataBase64);
		
		List<String> sortList = new ArrayList<String>(params.keySet());
		Collections.sort(sortList);
		
		StringBuilder builder = new StringBuilder();
		String str4md5 = "";
		for(String key : sortList) {
			String value = params.get(key);
			if(value == null || value.equals(""))
				continue;
			builder.append(key)
				.append(value);
		}
		str4md5 = builder.toString();
		
		builder.setLength(0);
		builder.append(signKey)
			.append(str4md5)
			.append(signKey);
		str4md5 = builder.toString();
		
		sign = MD5Util.md5(str4md5 + "", "UTF-8").toUpperCase();
		return sign;
	}
	
	/**
	 * 字符串转加密
	 * @param data
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String encrypt(String data) throws UnsupportedEncodingException {
		return EncryptUtil.encode(data, signKey, "UTF-8");
	}
}
