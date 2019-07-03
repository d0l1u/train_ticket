package com.l9e.test;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;

import com.kuyou.train.commons.httpclient.client.HttpEngineClient;
import com.kuyou.train.commons.httpclient.client.HttpEngineResponse;

public class Demo5 {

	public static void main(String[] args) throws SocketTimeoutException, IOException {
		String url = "http://43.241.226.178:18080/robot/pcBook/doPost";
		String param = "{\r\n" + "    \"departureDate\": \"2017-10-28\",\r\n" + "    \"fromStationCode\": \"BJ\",\r\n"
				+ "    \"fromStationName\": \"北京\",\r\n" + "    \"ip\": \"110.211.31.24\",\r\n"
				+ "    \"orderId\": \"HC00001\",\r\n" + "    \"passengers\": [\r\n" + "        {\r\n"
				+ "            \"cardNo\": \"412727199410172612\",\r\n" + "            \"cardType\": \"1\",\r\n"
				+ "            \"name\": \"陶凯\",\r\n" + "            \"passengerNo\": \"P0001\",\r\n"
				+ "            \"seatType\": \"3\",\r\n" + "            \"ticketType\": \"1\"\r\n" + "        }\r\n"
				+ "    ],\r\n" + "    \"password\": \"taokai1017\",\r\n" + "    \"toStationCode\": \"SH\",\r\n"
				+ "    \"toStationName\": \"上海\",\r\n" + "    \"trainCode\": \"C008\",\r\n"
				+ "    \"username\": \"taokaivip@126.com\"\r\n" + "}";
		HttpEngineClient http = new HttpEngineClient().buildTimeout(1000 * 60 *5); 
		HashMap<String, String> header = new HashMap<>();
		header.put("Content-Type", "application/json ; charset=utf-8"); 
		HttpEngineResponse doPost = http.doPost(url, header , param);
		System.err.println("doPost:" + doPost.getResponseContent()); 
	}
}
