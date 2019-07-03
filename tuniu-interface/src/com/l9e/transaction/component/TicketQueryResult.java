package com.l9e.transaction.component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 处理查询接口返回数据
 * @author licheng
 *
 */
public class TicketQueryResult {
	
	private static final ObjectMapper mapper = new ObjectMapper();

	public List<Map<String, Object>> ticketQueryJson(String result) {
		
		List<Map<String, Object>> array = new ArrayList<Map<String,Object>>();
		try {
			JsonNode root = mapper.readTree(result);
			JsonNode trainArray = path("datas", root);
			if(!trainArray.isArray() || trainArray.size() == 0)
				return null;
			
			
			JSONObject resultJson = JSONObject.parseObject(result);
			JSONArray dataArray = resultJson.getJSONArray("datajson");
			if(dataArray == null || dataArray.isEmpty()){
				return null;
			}
			
			
			
			for(int i = 0;i < dataArray.size();i++) {

				JSONObject dataJson = dataArray.getJSONObject(i);
				Map<String, Object> train = new HashMap<String, Object>();
				
				train.put("saleDateTime", dataJson.getString("sale_time"));
				train.put("canBuyNow", dataJson.getString("canWebBuy"));
				train.put("arriveDays", Integer.valueOf(dataJson.getString("day_difference")));
				train.put("trainStartDate", dataJson.getString("start_train_date"));
				train.put("trainCode", dataJson.getString("station_train_code"));
				train.put("accessByIdcard", dataJson.getString("is_support_card"));
				train.put("trainNo", dataJson.getString("train_no"));
				train.put("trainType", trainType(dataJson.getString("station_train_code")));
				train.put("fromStationName", dataJson.getString("from_station_name"));
				train.put("fromStationCode", dataJson.getString("from_station_telecode"));
				train.put("toStationName", dataJson.getString("to_station_name"));
				train.put("toStationCode", dataJson.getString("to_station_telecode"));
				train.put("startStationName", dataJson.getString("start_station_name"));
				train.put("endStationName", dataJson.getString("end_station_name"));
				train.put("startTime", dataJson.getString("start_time"));
				train.put("arriveTime", dataJson.getString("arrive_time"));
				train.put("runTime", dataJson.getString("lishi"));
				train.put("runTimeMinute", dataJson.getString("lishiValue"));
				train.put("gjrwNum", dataJson.getString("gjrw_num"));
				train.put("gjrwPrice", dataJson.getString("gjrw"));
				train.put("qtxbNum", dataJson.getString("qtxb_num"));
				train.put("qtxbPrice", dataJson.getString("qtxb"));
				train.put("rwNum", dataJson.getString("rw_num"));
				train.put("rwPrice", dataJson.getString("rws"));
				train.put("rzNum", dataJson.getString("rz_num"));
				train.put("rzPrice", dataJson.getString("rz"));
				train.put("swzNum", dataJson.getString("swz_num"));
				train.put("swzPrice", dataJson.getString("swz"));
				train.put("tdzNum", dataJson.getString("tdz_num"));
				train.put("tdzPrice", dataJson.getString("tdz"));
				train.put("wzNum", dataJson.getString("wz_num"));
				train.put("wzPrice", dataJson.getString("yz"));
				train.put("ywNum", dataJson.getString("yw_num"));
				train.put("ywPrice", dataJson.getString("yws"));
				train.put("yzNum", dataJson.getString("yz_num"));
				train.put("yzPrice", dataJson.getString("yz"));
				train.put("edzNum", dataJson.getString("edz_num"));
				train.put("edzPrice", dataJson.getString("edz"));
				train.put("ydzNum", dataJson.getString("ydz_num"));
				train.put("ydzPrice", dataJson.getString("ydz"));
				
				array.add(train);
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return array;
	}
	
	private JsonNode path(String fieldName, JsonNode parent) {
		if(parent == null)
			return null;
		JsonNode node = parent.path(fieldName);
		if(node == null || node.isMissingNode())
			return null;
		return node;
	}
	
	private String getString(String name, JsonNode node) {
		JsonNode valueNode = path(name, node);
		if(valueNode == null) 
			return "";
		return valueNode.asText();
	}
	
	private Integer trainType(String trainCode) {
		if(trainCode.indexOf("G") >= 0) {
			return 0;
		} else if(trainCode.indexOf("C") >= 0){
			return 1;
		} else if(trainCode.indexOf("D") >= 0){
			return 2;
		} else if(trainCode.indexOf("Z") >= 0){
			return 3;
		} else if(trainCode.indexOf("T") >= 0){
			return 4;
		} else if(trainCode.indexOf("K") >= 0){
			return 5;
		}else {
			return 6;
		}
	}		
}
