package com.l9e.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.l9e.transaction.vo.Order;


/**
 * jackson工具
 * @author licheng
 *
 */
public class JacksonUtil {

	private static final ObjectMapper MAPPER = new ObjectMapper();
	private static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	static {
		MAPPER.setSerializationInclusion(Include.NON_NULL);
		MAPPER.setDateFormat(DEFAULT_DATE_FORMAT);
	}
	
	/**
	 * 对象转json串
	 * @param obj
	 * @return
	 * @throws IOException
	 */
	public static String generateJson(Object obj) throws IOException {
		return MAPPER.writeValueAsString(obj);
	}
	
	/**
	 * json串转对象
	 * @param <T>
	 * @param jsonStr
	 * @param clazz
	 * @return
	 * @throws IOException
	 */
	public static <T> T readJson(String jsonStr, Class<T> clazz) throws IOException {
		if (jsonStr == null || jsonStr.equals(""))
			return null;
		return MAPPER.readValue(jsonStr, clazz);
	}
	
	/**
	 * json串转对象，自定义日期格式
	 * @param <T>
	 * @param jsonStr
	 * @param clazz
	 * @param dateFormatPattern
	 * @return
	 * @throws IOException
	 */
	public static <T> T readJson(String jsonStr, Class<T> clazz, String dateFormatPattern) throws IOException {
		if (jsonStr == null || jsonStr.equals(""))
			return null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormatPattern);
			MAPPER.setDateFormat(sdf);
			return MAPPER.readValue(jsonStr, clazz);
		} finally {
			MAPPER.setDateFormat(DEFAULT_DATE_FORMAT);
		}
	}
	
	/**
	 * json数组串转list
	 * @param <T>
	 * @param arrayStr
	 * @param clazz
	 * @return
	 * @throws IOException
	 */
	public static <T> List<T> readList(String arrayStr, Class<T> clazz) throws IOException {
//		JsonNode arrayNode = mapper.readTree(arrayStr);
//		if(arrayNode.isMissingNode() || !arrayNode.isArray())
//			return null;
		CollectionType type = MAPPER.getTypeFactory().constructCollectionType(List.class, clazz);
		return MAPPER.readValue(arrayStr, type);
	}
	
	public static <T> T getValue(String json, String field, Class<T> clazz) throws JsonProcessingException, IOException {
		
		JsonNode root = MAPPER.readTree(json);
		
		if(!root.isMissingNode()) {
			JsonNode fieldNode = root.get(field);
			if(!fieldNode.isMissingNode()) {
				return MAPPER.readValue(fieldNode.toString(), clazz);
			}
		}
		return null;
	}
	
	public static void main(String[] args) throws JsonProcessingException, IOException {
		Date testDate = new Date();
		Order order = new Order();
		order.setId("TGT_IOIEHMT23");
		order.setOutTicketTime(testDate);
		String json = generateJson(order);
		System.out.println(json);
		System.out.println(readJson(json, Order.class));
		json = json.replaceAll("2016-01-22", "2016~01~22");
		System.out.println(json);
		System.out.println(readJson(json, Order.class, "yyyy~MM~dd HH:mm:ss"));
		json = json.replaceAll("2016~01~22", "2016-01-22");
		System.out.println(json);
		System.out.println(readJson(json, Order.class));
	}
}
