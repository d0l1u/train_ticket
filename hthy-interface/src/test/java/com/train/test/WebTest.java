package com.train.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.train.commons.util.HttpUtil;
import com.train.robot.em.CardType;
import com.train.robot.em.SeatType;
import com.train.robot.em.TicketType;

public class WebTest {

	// @org.junit.Test
	public void order() {

		HttpUtil http = new HttpUtil();
		JSONObject requestJson = new JSONObject(true);
		requestJson.put("myOrderId", "test201807090755");

		JSONObject accountJson = new JSONObject(true);
		accountJson.put("username", "qkq686");
		accountJson.put("password", "qae060");
		requestJson.put("account", accountJson);

		JSONObject ticketJson = new JSONObject(true);
		ticketJson.put("trainCode", "K7906");
		ticketJson.put("fromStationName", "包头");
		ticketJson.put("toStationName", "包头东");
		ticketJson.put("departureDate", "2018-08-04");
		ticketJson.put("chooseSeat", "");

		JSONArray passengerArray = new JSONArray();
		JSONObject passenger1 = new JSONObject(true);
		passenger1.put("name", "李少辉");
		passenger1.put("cpId", "cp_0001");
		passenger1.put("cardNo", "610426199906220019");
		passenger1.put("cardType", CardType.SHEN_FEN.getKy());
		passenger1.put("seatType", SeatType.HARD_SEAT.getKy());
		passenger1.put("ticketType", TicketType.CHE_NGREN.getKy());
		passengerArray.add(passenger1);

		ticketJson.put("passengers", passengerArray);
		requestJson.put("data", ticketJson);

		System.err.println(requestJson.toJSONString());

		String result = http.doHttpPost("http://116.196.87.111:18000/hthy/k618/order",
				"{\"data\":{\"trainCode\":\"K1674\",\"departureDate\":\"2018-08-08\",\"fromStationName\":\"包头\",\"toStationName\":\"包头东\",\"passengers\":[{\"passengerNo\":\"CP1807110915192773\",\"name\":\"范凡\",\"cardType\":\"2\",\"cardNo\":\"130682198912230048\",\"ticketType\":\"0\",\"seatType\":\"8\"}]},\"account\":{},\"publicIp\":\"127.0.0.1\",\"orderId\":\"ky18071154592481\"}",
				10 * 1000, true, "UTF-8");
		System.out.println("result:" + result);
	}

	// @Test
	public void cancel() {
		HttpUtil http = new HttpUtil();
		String myOrderId = "test201807090755";
		String supplierId = "T18070992AE99740C1ED04C510AD6E00AC617B41FFF";

		String result = http.doHttpGet("http://116.196.87.111:8080/k618/cancel/" + myOrderId + "/" + supplierId,
				60 * 1000, "UTF-8");
		System.out.println("result:" + result);
	}

	// @Test
	public void confirm() {
		HttpUtil http = new HttpUtil();
		String myOrderId = "test201807090755";
		String supplierId = "T18070992AE99740C1ED04C510AD6E00AC617B41FFF";

		String result = http.doHttpGet("http://116.196.87.111:8080/k618/confirm/" + myOrderId + "/" + supplierId,
				60 * 1000, "UTF-8");
		System.out.println("result:" + result);
	}

	// @Test
	public void refund() {
		HttpUtil http = new HttpUtil();
		JSONObject requestJson = new JSONObject(true);
		requestJson.put("myOrderId", "test201807090755");
		requestJson.put("supplierId", "T18070992AE99740C1ED04C510AD6E00AC617B41FFF");

		JSONObject accountJson = new JSONObject(true);
		accountJson.put("username", "qkq686");
		accountJson.put("password", "qae060");
		requestJson.put("account", accountJson);

		JSONObject ticketJson = new JSONObject(true);
		ticketJson.put("sequence", "EE57582455");

		JSONArray passengerArray = new JSONArray();
		JSONObject passenger1 = new JSONObject(true);
		passenger1.put("name", "李少辉");
		passenger1.put("subSequence", "EE575824551150003");
		passenger1.put("cardNo", "610426199906220019");
		passenger1.put("cardType", CardType.SHEN_FEN.getKy());
		passengerArray.add(passenger1);

		ticketJson.put("passengers", passengerArray);
		requestJson.put("data", ticketJson);

		System.err.println(requestJson.toJSONString());

		String result = http.doHttpPost("http://116.196.87.111:8080/k618/refund", requestJson.toJSONString(), 5 * 1000,
				true, "UTF-8");
		System.out.println("result:" + result);
	}
}
