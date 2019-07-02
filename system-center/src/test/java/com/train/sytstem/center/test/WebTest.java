package com.train.sytstem.center.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.train.system.center.util.HttpUtil;

public class WebTest {

	// @Test
	public void order() {

		HttpUtil http = new HttpUtil();

		String param = "{\"partnerid\":\"19eceshi\",\"method\":\"train_order\",\"reqtime\":\"20180711081853\",\"sign\":\"5d962e1f37815bf19a90fb2efde5bfc5\",\"checi\":\"K1674\",\"from_station_name\":\"包头\",\"to_station_name\":\"包头东\",\"train_date\":\"2018-08-08\",\"callbackurl\":\"http://219.238.151.236/hthy/callback/orderCallback\",\"is_choose_seats\":false,\"choose_seats\":\"\",\"is_accept_standing\":true,\"passengers\":[{\"passengersename\":\"范凡\",\"passportseno\":\"130682198912230048\",\"passporttypeseidname\":\"二代身份证\",\"passporttypeseid\":\"1\",\"zwname\":\"硬座\",\"zwcode\":\"1\",\"price\":0,\"piaotype\":\"1\",\"cxin\":\"\"}]}";

		String result = http.doHttpPost("http://10.16.22.46:18000/hthy/k618/order", param, 20 * 1000, true);
		System.out.println("result:" + result);
	}

	// @Test
	public void cancel() {
		HttpUtil http = new HttpUtil();
		String myOrderId = "test201807090755";
		String supplierId = "T18070992AE99740C1ED04C510AD6E00AC617B41FFF";

		String result = http.doHttpGet("http://116.196.87.111:8080/k618/cancel/" + myOrderId + "/" + supplierId,
				60 * 1000);
		System.out.println("result:" + result);
	}

	// @Test
	public void confirm() {
		HttpUtil http = new HttpUtil();
		String myOrderId = "test201807090755";
		String supplierId = "T18070992AE99740C1ED04C510AD6E00AC617B41FFF";

		String result = http.doHttpGet("http://10.16.22.46:18000/hthy/k618/order/" + myOrderId + "/" + supplierId,
				60 * 1000);
		System.out.println("result:" + result);
	}

	// @Test
	public void refund() {
		HttpUtil http = new HttpUtil();
		JSONObject requestJson = new JSONObject(true);
		requestJson.put("myOrderId", "ky18071600103069");
		requestJson.put("supplierOrderId", "T1807166464AB5C0017B04E9709B0D0EB81EA2BAF56");

		JSONObject accountJson = new JSONObject(true);
		accountJson.put("username", "");
		accountJson.put("password", "");
		requestJson.put("account", accountJson);

		requestJson.put("sequence", "EE84367461");

		JSONArray passengerArray = new JSONArray();
		JSONObject passenger1 = new JSONObject(true);
		passenger1.put("name", "王园");
		passenger1.put("subSequence", "EE843674611160111");
		passenger1.put("cardNo", "430421199004208867");
		passenger1.put("cardType", "2");
		passengerArray.add(passenger1);

		requestJson.put("passengers", passengerArray);

		System.err.println(requestJson.toJSONString());

		String result = http.doHttpPost("http://10.16.22.46:18000/hthy/k618/refund", requestJson.toJSONString(),
				5 * 1000, true);
		System.out.println("result:" + result);
	}

	// @Test
	public void refundCallback() {
		String url = "http://10.16.22.41:18001/system-center/hthy-callback/refund";
		String param = "{\"timestamp\":\"1531829294\",\"sign\":\"fcef8c478cbe057d1e55c714ced62ff6\",\"reqtoken\":\"1531829294434\",\"returntickets\":[{\"returnsuccess\":true,\"returnfailid\":\"\",\"passengername\":\"鲍娅林\",\"ticket_no\":\"ED858513151130013\",\"returnmoney\":\"92\",\"returnfailmsg\":\"\",\"passportseno\":\"620202197208250422\",\"passporttypeseid\":\"1\",\"returntime\":\"2018-07-18 05:59:15\"}],\"apiorderid\":\"ky18071740480234\",\"returnmoney\":\"92\",\"trainorderid\":\"ED85851315\",\"returnmsg\":\"\",\"returnstate\":true,\"returntype\":\"0\"}";

		String result = new HttpUtil().doHttpPost(url, param, 60 * 1000, true);
		System.out.println("result:" + result);
	}
}
