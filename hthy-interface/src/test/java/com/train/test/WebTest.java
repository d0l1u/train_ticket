package com.train.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.train.commons.util.HttpUtil;
import com.train.robot.em.CardType;
import com.train.robot.em.SeatType;
import com.train.robot.em.TicketType;

public class WebTest {

	// @Test
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

		String param = "jsonStr={\"partnerid\":\"19eceshi\",\"method\":\"train_order\",\"reqtime\":\"20180711081853\",\"sign\":\"5d962e1f37815bf19a90fb2efde5bfc5\",\"checi\":\"K1674\",\"from_station_name\":\"包头\",\"to_station_name\":\"包头东\",\"train_date\":\"2018-08-08\",\"callbackurl\":\"http://219.238.151.236/hthy/callback/orderCallback\",\"is_choose_seats\":false,\"choose_seats\":\"\",\"is_accept_standing\":true,\"passengers\":[{\"passengersename\":\"范凡\",\"passportseno\":\"130682198912230048\",\"passporttypeseidname\":\"二代身份证\",\"passporttypeseid\":\"1\",\"zwname\":\"硬座\",\"zwcode\":\"1\",\"price\":0,\"piaotype\":\"1\",\"cxin\":\"\"}]}";

		String result = http.doHttpPost("http://116.196.87.111:8080/k618/order", requestJson.toJSONString(), 10 * 1000,
				true, "UTF-8");
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
