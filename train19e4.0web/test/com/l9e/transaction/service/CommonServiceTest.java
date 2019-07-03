package com.l9e.transaction.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.Versioned;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.dao.RobTicketDao;
import com.l9e.transaction.vo.RobTicketFormVo;
import com.l9e.transaction.vo.RobTicketVo;
import com.l9e.transaction.vo.RobTicket_CP;
import com.l9e.transaction.vo.RobTicket_OI;
import com.l9e.transaction.vo.RobTicket_Refund;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.HttpPostUtil;
import com.l9e.util.JedisUtil;
import com.l9e.util.MemcachedUtil;
import com.l9e.util.RobTicketUtils;
import com.sun.org.apache.bcel.internal.generic.NEW;

import redis.clients.jedis.Jedis;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:context/applicationContext.xml")
public class CommonServiceTest {

	@Resource
	private CommonService commonService;
	
	@Resource
	private RobTicketService robTicketService;
	
	@Resource
	private RobTicketDao robTicketDao;
	
	/**
	 * 抢票订单 携程预订接口
	 */
	@Test
	public void testBookCtrip(){
		RobTicket_OI oi = new RobTicket_OI();
		oi.setOrderId("HC_ROB1612071000141001");
		try {
			RobTicketUtils.pushRobTicket(robTicketService, oi);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCancelCtrip(){
		RobTicket_OI oi = new RobTicket_OI();
		oi.setOrderId("HC_ROB1612071000141001");
		String cancelRefund = RobTicketUtils.cancelRefund("HC_ROB1612071000141001", RobTicketVo.CANCEL_TYPE);
		System.out.println(cancelRefund);
	}
	
	@Test
	public void test002(){
		Map<String, String> queryEOPByEopId = null;
		try {
			queryEOPByEopId = robTicketService.queryEOPByEopId("1307221549221009151376012893");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(queryEOPByEopId);
	}
	
	@Test
	public void test03(){
		InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("testPush.json");
		String inputStream2String = RobTicketUtils.inputStream2String(resourceAsStream);
		String robVo2JSON = inputStream2String;
		try {
			HttpPostUtil.sendAndRecive("http://localhost:8080/train_interface_dev/jlOrder", "type=push&json="+robVo2JSON);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void test06(){
		RobTicket_OI oi = new RobTicket_OI();
		oi.setOrderId("HC_ROB1612221643551849");
		oi.setOrderStatus(RobTicketVo.OI_ORDER_STATUS_EXT_SUCC);
		oi.setOptionTime(new Date());
		String json = RobTicketUtils.inputStream2String(this.getClass().getClassLoader().getResourceAsStream("robsucc.json"));
		try {
			robTicketService.updateFrontAndBackWithSucc(oi, json);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void test07(){
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("agent_id", "AA0ae2b2013100815533885265");
		map.put("page", 0);
		map.put("pageSize", 10);
		try {
			robTicketService.selectOrderInfoByConditions(map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void test08() throws Exception{
		String[] cpids = {"HC_ROB_CP1612121654231083","HC_ROB_CP1612121654231084"};
		robTicketService.updateFrontBackCP_Refund(cpids , RobTicket_CP.REFUNDING);
	}
	
	@Test
	public void test09(){
		Map<String, String> seatMap = new HashMap<String, String>();
		seatMap .put("cc", "G411");
		seatMap.put("fz", "北京南");
		seatMap.put("dz", "上海虹桥");
		LinkedList<Map<String, String>> seatInfoList = new LinkedList<Map<String, String>>();
		Map<String, String> seatPrizeMap = new HashMap<String, String>();// 座席与价格映射
		Map<String, String> seatMoneyMap = commonService.querySeatMap(seatMap);
		String seatMsg = "";
		String defaultSelect = "";
		String[] seats = seatMsg .split(",");
		for (String seat : seats) {
			String[] element = seat.split("_");
			String price = element[1].trim();
			String yp = element[2].trim();
			if (!StringUtils.isEmpty(price) && !"-".equals(price)) {// 有该类别坐席

				// 余票小于等于10张则过滤该坐席
				if (StringUtils.isEmpty(yp)) {
					continue;
				} else {
					if (yp.equals("_")) {
						yp = "0";
					}
				}
				Map<String, String> map = new HashMap<String, String>(3);

				String seatType = null;
				// 根据坐席名称取得坐席ID
				for (Map.Entry<String, String> entry : TrainConsts
						.getSeatType().entrySet()) {
					if (!StringUtils.isEmpty(element[0])
							&& entry.getValue().equals(element[0])) {
						seatType = entry.getKey();
					}
				}
				map.put("seatName", element[0]);
				map.put("seatType", seatType);
				if (seatType != null) {
					if (seatType.equals("10")) {
					} else {
						String priceDb = seatMoneyMap.get("seat" + seatType);
						if (!price.equals(priceDb)) {
							if (Float.parseFloat(priceDb) >= Float
									.parseFloat(price)) {
								price = priceDb;
							} else {
							}
						}
					}
				}
				map.put("price", price);
				map.put("yp", yp);
				
				if (defaultSelect .equals(element[0])) {
					map.put("seatSelect", "select");
					seatInfoList.addFirst(map);
				} else {
					map.put("seatSelect", "unSelect");
					seatInfoList.add(map);
				}
				seatPrizeMap.put("seatType" + seatType, price);// 坐席价格映射
			}
		}
		
	}
	
	@Test
	public void test(){
		List<String> hmget = JedisUtil.getJedis().hmget("12306UserInfo", "1234");
		if (!hmget.isEmpty()) {
			String string = hmget.get(0);
			String[] split = string.split("##");
			for (String string2 : split) {
				System.out.println(string2);
			}
		}
	}
	@Test
	public void test0(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("1234", "zhangyang5178"+"##"+"543218");
		JedisUtil.getJedis().hmset("12306UserInfo", map);
	}
	@Test
	public void test1() throws Exception{
		Map<String, String> queryOrderIdByCtripId = robTicketService.queryOrderIdByCtripId("19e201612221644171006");
		RobTicket_OI oi = new RobTicket_OI();
		oi.setOrderId("HC_ROB1701061045253804");
		oi.setCtripOrderId("19e201612221644171006");
		oi.setContactPhone("15321761517");
		robTicketService.sendSMSAfterRobSucc(oi);
	}
	
	@Test
	public void test02() throws IOException{
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("school.json");
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8")));
		StringBuffer buffer = new StringBuffer();
		while(reader.ready()){
			buffer.append(reader.readLine());
		}
		reader.close();
		JSONArray jsonArray = JSON.parseArray(buffer.toString());
		Iterator<Object> iterator = jsonArray.iterator();
		JSONArray array = new JSONArray();
		while (iterator.hasNext()) {
			JSONObject object = (JSONObject) iterator.next();
			String string = object.getString("name");
			array.add(string);
		}
		System.out.println(array);
	}
	
	@Test
	public void testrefund(){
		String json ="{'Authentication':{'MessageIdentity':'B3166687BF44D27C23BCCE6DE8FBC6FF','PartnerName':'tieyou','ServiceName':'web.order.returnTicketNotice','TimeStamp':'2017-5-11 12:48:32'},'TrainOrderService':{'OrderNumber':'19e201705091307261008','contactMobile':'15011414015','contactName':'å·¦','refundTicket':{'childBillId':'','eOrderNumber':'E945349956','eOrderType':'1','orderId':'3654510382','passport':'350681198108170026','passportName':'郑小兰','realName':'郑小兰','reason':'退票成功，退款金额:162.50元','seatNumber':'05车厢13A号','status':'1'}}}";
		JSONObject obj = JSON.parseObject(json);
		JSONObject TrainOrderService = obj.getJSONObject("TrainOrderService");
		JSONObject refundTicket = TrainOrderService.getJSONObject("refundTicket");
		String passport = refundTicket.getString("passport");
		String passportName = refundTicket.getString("passportName");
		String status = refundTicket.getString("status");
		String seatNumber = refundTicket.getString("seatNumber");
		String eOrderType = refundTicket.getString("eOrderType");
		String reason = refundTicket.getString("reason");
		HashMap<String, Object> map = new HashMap<String, Object>();
		/*map.put("order_id", orderId);
		map.put("cert_no", passport);
		if (eOrderType.equals("1")) {
			map.put("ticket_type", 0);
		}else if (eOrderType.equals("2")) {
			map.put("ticket_type", 1);
		}
		map.put("user_name", passportName);
		map.put("refund_status", RobTicket_CP.REFUND_REQ);
		map.put("seat_no", seatNumber);
		logger.info("queryRefundCp输入数据-->"+map);
		RobTicket_CP cp = queryRefundCp(map);
		String cpid = cp.getCpId();*/
	}
	
	@Test
	public void testRedis(){
		Jedis jedis = JedisUtil.getJedis();
		jedis.set("db6", "db6");
	}
	
	

}
