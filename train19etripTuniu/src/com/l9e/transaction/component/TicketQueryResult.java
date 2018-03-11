package com.l9e.transaction.component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
			for(int i = 0;i < trainArray.size();i++) {
				JsonNode trainNode = trainArray.get(i);
				Map<String, Object> train = new HashMap<String, Object>();
				
				train.put("saleDateTime", getString("sale_time", trainNode));
				train.put("canBuyNow", getString("canWebBuy", trainNode));
				train.put("arriveDays", Integer.valueOf(getString("day_difference", trainNode)));
				train.put("trainStartDate", getString("start_train_date", trainNode));
				train.put("trainCode", getString("station_train_code", trainNode));
				train.put("accessByIdcard", getString("is_support_card", trainNode));
				train.put("trainNo", getString("train_no", trainNode));
				train.put("trainType", trainType(getString("station_train_code", trainNode)));
				train.put("fromStationName", getString("from_station_name", trainNode));
				train.put("fromStationCode", getString("from_station_telecode", trainNode));
				train.put("toStationName", getString("to_station_name", trainNode));
				train.put("toStationCode", getString("to_station_telecode", trainNode));
				train.put("startStationName", getString("start_station_name", trainNode));
				train.put("endStationName", getString("end_station_name", trainNode));
				train.put("startTime", getString("start_time", trainNode));
				train.put("arriveTime", getString("arrive_time", trainNode));
				train.put("runTime", getString("lishi", trainNode));
				train.put("runTimeMinute", getString("lishiValue", trainNode));
				train.put("gjrwNum", getString("gjrw_num", trainNode));
				train.put("gjrwPrice", getString("gjrw", trainNode));
				train.put("qtxbNum", getString("qtxb_num", trainNode));
				train.put("qtxbPrice", getString("qtxb", trainNode));
				train.put("rwNum", getString("rw_num", trainNode));
				train.put("rwPrice", getString("rws", trainNode));
				train.put("rzNum", getString("rz_num", trainNode));
				train.put("rzPrice", getString("rz", trainNode));
				train.put("swzNum", getString("swz_num", trainNode));
				train.put("swzPrice", getString("swz", trainNode));
				train.put("tdzNum", getString("tdz_num", trainNode));
				train.put("tdzPrice", getString("tdz", trainNode));
				train.put("wzNum", getString("wz_num", trainNode));
				train.put("wzPrice", getString("yz", trainNode));
				train.put("ywNum", getString("yw_num", trainNode));
				train.put("ywPrice", getString("yws", trainNode));
				train.put("yzNum", getString("yz_num", trainNode));
				train.put("yzPrice", getString("yz", trainNode));
				train.put("edzNum", getString("edz_num", trainNode));
				train.put("edzPrice", getString("edz", trainNode));
				train.put("ydzNum", getString("ydz_num", trainNode));
				train.put("ydzPrice", getString("ydz", trainNode));
				
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
