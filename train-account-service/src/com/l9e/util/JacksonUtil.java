package com.l9e.util;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

/**
 * jackson工具
 * 
 * @author licheng
 *
 */
public class JacksonUtil {

	private static final ObjectMapper mapper = new ObjectMapper();

	static {
		mapper.setSerializationInclusion(Include.NON_NULL);
	}

	/**
	 * 对象转json串
	 * 
	 * @param obj
	 * @return
	 * @throws IOException
	 */
	public static String generateJson(Object obj) throws IOException {
		return mapper.writeValueAsString(obj);
	}

	/**
	 * json串转对象
	 * 
	 * @param <T>
	 * @param jsonStr
	 * @param clazz
	 * @return
	 * @throws IOException
	 */
	public static <T> T readJson(String jsonStr, Class<T> clazz) throws IOException {
		if (jsonStr == null || jsonStr.equals(""))
			return null;
		return mapper.readValue(jsonStr, clazz);
	}

	/**
	 * json数组串转list
	 * 
	 * @param <T>
	 * @param arrayStr
	 * @param clazz
	 * @return
	 * @throws IOException
	 */
	public static <T> List<T> readList(String arrayStr, Class<T> clazz) throws IOException {
		// JsonNode arrayNode = mapper.readTree(arrayStr);
		// if(arrayNode.isMissingNode() || !arrayNode.isArray())
		// return null;
		CollectionType type = mapper.getTypeFactory().constructCollectionType(List.class, clazz);
		return mapper.readValue(arrayStr, type);
	}

	public static <T> T getValue(String json, String field, Class<T> clazz)
			throws JsonProcessingException, IOException {

		JsonNode root = mapper.readTree(json);

		if (!root.isMissingNode()) {
			JsonNode fieldNode = root.get(field);
			if (!fieldNode.isMissingNode()) {
				return mapper.readValue(fieldNode.toString(), clazz);
			}
		}
		return null;
	}

	public static void main(String[] args) throws JsonProcessingException, IOException {
		String json = "{\"strfield\":\"strfield\",\"intfield\":1234,\"success\":true}";
		boolean value = getValue(json, "success", Boolean.class);
		System.out.println(value);
	}
}
