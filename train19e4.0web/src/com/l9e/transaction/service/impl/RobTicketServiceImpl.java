package com.l9e.transaction.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.l9e.transaction.dao.CommonDao;
import com.l9e.transaction.dao.RobTicketDao;
import com.l9e.transaction.pubsub.JLTimer;
import com.l9e.transaction.pubsub.RobRefund;
import com.l9e.transaction.service.RobTicketService;
import com.l9e.transaction.vo.RobTicketVo;
import com.l9e.transaction.vo.RobTicket_CP;
import com.l9e.transaction.vo.RobTicket_History;
import com.l9e.transaction.vo.RobTicket_Notify;
import com.l9e.transaction.vo.RobTicket_OI;
import com.l9e.transaction.vo.RobTicket_Refund;
import com.l9e.util.DateUtil;
import com.l9e.util.JedisUtil;
import com.l9e.util.MobileMsgUtil;
import com.l9e.util.RobTicketUtils;

import redis.clients.jedis.Jedis;

/**
 * 19e后台-抢票service
 * 
 * @author yangwei01
 * 
 */
@Service("robTicketService")
public class RobTicketServiceImpl implements RobTicketService {

	String[] channels = { "ROB_CANCEL_SUCC", "ROB_DIFF_REFUND", "ROB_EXC_REFUND", "ROB_LOCK", "ROB_NO_TICKET",
			"ROB_SUCC", "ROB_REFUND_TICKET" };

	private static final Logger logger = Logger.getLogger(RobTicketServiceImpl.class);
	@Resource
	private RobTicketDao robTicketDao;
	@Resource
	private CommonDao commonDao;
	@Resource
	private RobRefund robRefund;
	@Resource
	private MobileMsgUtil mobileMsgUtil;

	@Override
	public void deleteCP(RobTicket_CP cp) throws Exception {

		robTicketDao.deleteCP(cp);
	}

	@Override
	public void deleteHistory(RobTicket_History history) throws Exception {

		robTicketDao.deleteHistory(history);
	}

	@Override
	public void deleteNotify(RobTicket_Notify notify) throws Exception {

		robTicketDao.deleteNotify(notify);
	}

	@Override
	public void deleteOrderInfo(RobTicket_OI oi) throws Exception {

		robTicketDao.deleteOrderInfo(oi);
	}

	@Override
	public void insertCP(RobTicket_CP cp) throws Exception {

		robTicketDao.insertCP(cp);
	}

	@Override
	public void insertHistory(RobTicket_History history) throws Exception {

		robTicketDao.insertHistory(history);
	}

	@Override
	public void insertNotify(RobTicket_Notify notify) throws Exception {

		robTicketDao.insertNotify(notify);
	}

	@Override
	public void insertOrderInfo(RobTicket_OI oi) throws Exception {

		robTicketDao.insertOrderInfo(oi);
	}

	@Override
	public List<Map<String, String>> queryCP(Map<String, Object> paramMap) throws Exception {

		return robTicketDao.queryCP(paramMap);
	}

	@Override
	public List<Map<String, String>> queryHistory(Map<String, Object> paramMap) throws Exception {

		return robTicketDao.queryHistory(paramMap);
	}

	@Override
	public List<Map<String, String>> queryNotify(Map<String, Object> paramMap) throws Exception {

		return robTicketDao.queryNotify(paramMap);
	}

	@Override
	public List<Map<String, String>> queryOrderInfo(Map<String, Object> paramMap) throws Exception {

		return robTicketDao.queryOrderInfo(paramMap);
	}

	@Override
	public void updateCP(RobTicket_CP cp) throws Exception {
		robTicketDao.updateCP(cp);
	}

	@Override
	public void updateHistory(RobTicket_History history) throws Exception {
		robTicketDao.updateHistory(history);
	}

	@Override
	public void updateNotify(RobTicket_Notify notify) throws Exception {
		robTicketDao.updateNotify(notify);
	}

	@Override
	public void updateOrderInfo(RobTicket_OI oi) throws Exception {
		robTicketDao.updateOrderInfo(oi);

	}

	@Override
	public void insertOrderAndTickets(RobTicket_OI oi, List<RobTicket_CP> cps) throws Exception {

		robTicketDao.insertOrderInfo(oi);
		for (RobTicket_CP robTicketCP : cps) {
			robTicketDao.insertCP(robTicketCP);
		}

	}

	@Override
	public void updateAfterPay(RobTicket_OI oi, String status, Map<String, String> eopInfo) throws Exception {
		// 目前 只考虑 成功
		if (status.equalsIgnoreCase("SUCCESS")) {
			// eop 订单 更新
			commonDao.updateOrderEop(eopInfo);
			// orderinfo 更新
			RobTicket_OI toUpdate = robTicketDao.selectOrderInfoByPrimaryKey(oi);
			toUpdate.setOrderStatus(RobTicketVo.OI_ORDER_STATUS_PAY_SUCC);
			toUpdate.setPayTime(new Date());
			toUpdate.setOptionTime(new Date());
			robTicketDao.updateOrderInfo(toUpdate);
			// 推送给 携程订票 接口
			String pushRobTicketResult = "";

			logger.info("支付成功后抢票订单推送开始--单号--" + oi.getOrderId());
			pushRobTicketResult = RobTicketUtils.pushRobTicket(this, oi);
			logger.info("支付成功后抢票订单推送成功--单号--" + oi.getOrderId() + "-推送返回结果为-" + pushRobTicketResult);

		}
	}

	@Override
	public Map<String, Object> selectAndPushRob(RobTicket_OI oi) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		RobTicket_OI selectOrderInfoByPrimaryKey = robTicketDao.selectOrderInfoByPrimaryKey(oi);
		List<RobTicket_CP> selectCPsByorderId = robTicketDao.selectCPsByorderId(oi);
		map.put("oi", selectOrderInfoByPrimaryKey);
		map.put("cps", selectCPsByorderId);
		return map;
	}

	@Override
	public List<RobTicket_OI> selectOrderInfoByConditions(Map<String, Object> conditions) throws Exception {
		// selectType oneMonthOrder createTime orderId outTicketBillno
		Object object = conditions.get("selectType");
		String string = (object == null ? "" : object.toString());
		if (StringUtils.isEmpty(string) || string.equals("0")) {
			conditions.put("order_Status", "");
		} else if (string.equals("1")) {
			conditions.put("order_Status", "00");
		} else if (string.equals("2")) {
			conditions.put("order_Status", "88");
		} else if (string.equals("3")) {
			conditions.put("order_Status", "99");
		} else if (string.equals("4")) {
			conditions.put("order_Status", "10");
		} else if (string.equals("5")) {
			conditions.put("order_Status", "71");
		}
		return robTicketDao.selectOrderInfoByConditions(conditions);
	}

	@Override
	public RobTicket_CP selecTicketCP(RobTicket_CP cp) throws Exception {
		return robTicketDao.selectCPByPrimaryKey(cp);
	}

	@Override
	public List<RobTicket_CP> selectCps(RobTicket_OI oi) throws Exception {
		return robTicketDao.selectCPsByorderId(oi);
	}

	@Override
	public RobTicket_OI selectOrderInfo(RobTicket_OI oi) throws Exception {
		return robTicketDao.selectOrderInfoByPrimaryKey(oi);
	}

	@Override
	public void updateRobOrderbyApiResult(RobTicketVo vo) throws Exception {
		logger.info("抢票接口返回结果信息" + JSON.toJSONString(vo));

		robTicketDao.updateOrderInfo(vo.getOi());
		List<RobTicket_CP> cps = vo.getCps();
		for (RobTicket_CP robTicketCP : cps) {
			robTicketDao.updateCP(robTicketCP);
		}
		robTicketDao.updateHistory(vo.getHistory());
		robTicketDao.updateNotify(vo.getNotify());

	}

	@Override
	public HashMap<String, String> selectOrderStatusNum(HashMap<String, String> request2Map) throws Exception {
		return robTicketDao.selectOrderStatusNum(request2Map);
	}

	@Override
	public void deleteOiAndCps(RobTicket_OI oi) throws Exception {
		robTicketDao.deleteOrderInfo(oi);
		robTicketDao.deleteCPByOrderInfo(oi);
	}

	@Override
	public Map<String, String> queryEOPByEopId(String eopOrderId) throws Exception {

		return robTicketDao.queryEOPByEopId(eopOrderId);
	}

	@Override
	public Map<String, String> queryOrderIdByCtripId(String ctripId) throws Exception {
		return robTicketDao.queryOrderIdByCtripId(ctripId);
	}

	@Override
	public void insertRefund(RobTicket_Refund refund) throws Exception {
		robTicketDao.insertRefund(refund);
	}

	@Override
	public void updateRefund(RobTicket_Refund refund) throws Exception {
		robTicketDao.updateRefund(refund);
	}

	@Override
	public void updateFrontAndBack(RobTicket_OI oi) throws Exception {
		robTicketDao.updateOrderInfo(oi);
		robTicketDao.updateJLOrderInfo(oi);
	}

	@Override
	public void insertJLHistory(String orderId, String log, String person) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("order_id", orderId);
		map.put("order_optlog", log);
		map.put("create_time", new Date());
		map.put("opter", person);
		robTicketDao.insertJLHistory(map);

	}

	@Override
	public void updateFrontAndBackWithSucc(RobTicket_OI oi, String json) throws Exception {
		JSONObject obj = JSON.parseObject(json);
		JSONObject TicketInfoFinal = obj.getJSONObject("TrainOrderService").getJSONObject("OrderInfo")
				.getJSONObject("TicketInfoFinal");
		String eordernumber12306 = TicketInfoFinal.getString("ElectronicOrderNumber");
		String OrderTicketCheci = TicketInfoFinal.getString("OrderTicketCheci");
		oi.setOutTicketBillno(eordernumber12306);
		oi.setOutTicketAccount("携程抢票接口");
		oi.setOutTicketTime(new Date());
		Object tickets = TicketInfoFinal.getJSONObject("Tickets").get("Ticket");
		List<HashMap<String, String>> adultCPS = new ArrayList<HashMap<String, String>>();
		List<HashMap<String, String>> childCPS = new ArrayList<HashMap<String, String>>();
		Iterator<Object> iterator = null;
		if (tickets instanceof JSONArray) {
			JSONArray arr = (JSONArray) tickets;
			iterator = arr.iterator();
		}else{
			JSONObject tic = (JSONObject) tickets;
			String TicketType = tic.getString("TicketType");
			String OrderTicketPrice = tic.getString("OrderTicketPrice");
			String OrderTicketSeat = tic.getString("OrderTicketSeat");
			if (TicketType.equals("成人票")) {
				JSONObject DetailInfos = tic.getJSONObject("DetailInfos");// .getJSONArray("DetailInfo");
				Object jsonObject = DetailInfos.get("DetailInfo");
				JSONArray infos = null;
				if (jsonObject instanceof JSONArray) {
					infos = DetailInfos.getJSONArray("DetailInfo");
					Iterator<Object> infosIt = infos.iterator();
					while (infosIt.hasNext()) {
						Object object2 = (Object) infosIt.next();
						JSONObject info = (JSONObject) object2;
						String NumberID = info.getString("NumberID");
						String SeatNo = info.getString("SeatNo");
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("NumberID", NumberID);
						map.put("seat_no", SeatNo);
						map.put("OrderTicketPrice", OrderTicketPrice);
						map.put("OrderTicketSeat", OrderTicketSeat);
						map.put("train_no", OrderTicketCheci);
						adultCPS.add(map);
					}
				} else {
					JSONObject js = (JSONObject)jsonObject;
					String NumberID = js.getString("NumberID");
					String SeatNo = js.getString("SeatNo");
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("NumberID", NumberID);
					map.put("seat_no", SeatNo);
					map.put("OrderTicketPrice", OrderTicketPrice);
					map.put("OrderTicketSeat", OrderTicketSeat);
					map.put("train_no", OrderTicketCheci);
					adultCPS.add(map);

				}

			} else if (TicketType.equals("儿童票")) {
				JSONObject DetailInfos = tic.getJSONObject("DetailInfos");// .getJSONArray("DetailInfo");
				Object jsonObject = DetailInfos.get("DetailInfo");
				JSONArray infos = null;
				if (jsonObject instanceof JSONArray) {
					infos = DetailInfos.getJSONArray("DetailInfo");
					Iterator<Object> infosIt = infos.iterator();
					while (infosIt.hasNext()) {
						Object object2 = (Object) infosIt.next();
						JSONObject info = (JSONObject) object2;
						String NumberID = info.getString("NumberID");
						String SeatNo = info.getString("SeatNo");
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("seat_no", SeatNo);
						map.put("OrderTicketPrice", OrderTicketPrice);
						map.put("OrderTicketSeat", OrderTicketSeat);
						map.put("train_no", OrderTicketCheci);
						childCPS.add(map);
					}
				}else{
					JSONObject js = (JSONObject) jsonObject;
					String SeatNo = js.getString("SeatNo");
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("seat_no", SeatNo);
					map.put("OrderTicketPrice", OrderTicketPrice);
					map.put("OrderTicketSeat", OrderTicketSeat);
					map.put("train_no", OrderTicketCheci);
					childCPS.add(map);
					
				}
			}
			
			
		}
		
		
		
		while (iterator!=null && iterator.hasNext()) {
			Object object = (Object) iterator.next();
			JSONObject tic = (JSONObject) object;
			String TicketType = tic.getString("TicketType");
			String OrderTicketPrice = tic.getString("OrderTicketPrice");
			String OrderTicketSeat = tic.getString("OrderTicketSeat");
			if (TicketType.equals("成人票")) {
				JSONObject DetailInfos = tic.getJSONObject("DetailInfos");// .getJSONArray("DetailInfo");
				Object jsonObject = DetailInfos.get("DetailInfo");
				JSONArray infos = null;
				if (jsonObject instanceof JSONArray) {
					infos = DetailInfos.getJSONArray("DetailInfo");
					Iterator<Object> infosIt = infos.iterator();
					while (infosIt.hasNext()) {
						Object object2 = (Object) infosIt.next();
						JSONObject info = (JSONObject) object2;
						String NumberID = info.getString("NumberID");
						String SeatNo = info.getString("SeatNo");
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("NumberID", NumberID);
						map.put("seat_no", SeatNo);
						map.put("OrderTicketPrice", OrderTicketPrice);
						map.put("OrderTicketSeat", OrderTicketSeat);
						map.put("train_no", OrderTicketCheci);
						adultCPS.add(map);
					}
				} else {
					JSONObject js = (JSONObject)jsonObject;
					String NumberID = js.getString("NumberID");
					String SeatNo = js.getString("SeatNo");
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("NumberID", NumberID);
					map.put("seat_no", SeatNo);
					map.put("OrderTicketPrice", OrderTicketPrice);
					map.put("OrderTicketSeat", OrderTicketSeat);
					map.put("train_no", OrderTicketCheci);
					adultCPS.add(map);

				}

			} else if (TicketType.equals("儿童票")) {
				JSONObject DetailInfos = tic.getJSONObject("DetailInfos");// .getJSONArray("DetailInfo");
				Object jsonObject = DetailInfos.get("DetailInfo");
				JSONArray infos = null;
				if (jsonObject instanceof JSONArray) {
					infos = DetailInfos.getJSONArray("DetailInfo");
					Iterator<Object> infosIt = infos.iterator();
					while (infosIt.hasNext()) {
						Object object2 = (Object) infosIt.next();
						JSONObject info = (JSONObject) object2;
						String NumberID = info.getString("NumberID");
						String SeatNo = info.getString("SeatNo");
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("seat_no", SeatNo);
						map.put("OrderTicketPrice", OrderTicketPrice);
						map.put("OrderTicketSeat", OrderTicketSeat);
						map.put("train_no", OrderTicketCheci);
						childCPS.add(map);
					}
				}else{
					JSONObject js = (JSONObject) jsonObject;
					String SeatNo = js.getString("SeatNo");
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("seat_no", SeatNo);
					map.put("OrderTicketPrice", OrderTicketPrice);
					map.put("OrderTicketSeat", OrderTicketSeat);
					map.put("train_no", OrderTicketCheci);
					childCPS.add(map);
					
				}
			}
		}
		List<RobTicket_CP> selectCps = this.selectCps(oi);
		List<RobTicket_CP> adults = new ArrayList<RobTicket_CP>();
		List<RobTicket_CP> children = new ArrayList<RobTicket_CP>();
		for (RobTicket_CP cp : selectCps) {
			String certNo = cp.getCertNo();
			Integer ticketType = cp.getTicketType();
			// 成人票
			if (ticketType == 0) {
				adults.add(cp);
			} else if (ticketType == 1) {
				// 儿童票
				children.add(cp);
			}
		}
		for (int i = 0; i < children.size(); i++) {
			RobTicket_CP robTicket_CP = children.get(i);
			HashMap<String, String> hashMap = childCPS.get(i);
			hashMap.put("cp_id", robTicket_CP.getCpId());
		}
		for (RobTicket_CP ad : adults) {
			String certNo = ad.getCertNo();
			for (HashMap<String, String> adMap : adultCPS) {
				if (certNo.equals(adMap.get("NumberID"))) {
					adMap.put("cp_id", ad.getCpId());
				}
			}
		}

		adultCPS.addAll(childCPS);
		updateCPRobSucc(adultCPS);
		updateFrontAndBack(oi);
		insertJLHistory(oi.getOrderId(), "ROB_SUCC 携程返回有票,出票成功", "ctrip_callback");
		sendSMSAfterRobSucc(oi);
	}

	@Override
	public void sendSMSAfterRobSucc(RobTicket_OI oi) {
			//万达通讯提醒您，我们非常荣幸的帮助您成功订购编号为E872846519的01月16日20:40（常州-新乡）K1150次蒋海霞03车无座席位的车票，请您携身份证到车站售票点或者自助机领取纸质车票，祝您旅途愉快！【19易】
			Map<String, String> contact = robTicketDao.querySMSRobSucc(oi.getOrderId());
			List<RobTicket_CP> cps = robTicketDao.selectCPsByorderId(oi);
			StringBuffer content = new StringBuffer();
			if(StringUtils.isEmpty(contact.get("shop_short_name"))){
				content.append("19e加盟站");
			}else{
				content.append(contact.get("shop_short_name"));
			}
			content.append("提醒您，我们非常荣幸的帮助您成功【抢票】编号为")
				   .append(contact.get("out_ticket_billno"))
				   .append("的")
				   .append(contact.get("from_time"))
				   .append("（").append(contact.get("from_city")).append("-").append(contact.get("to_city")).append("）")
				   .append(contact.get("train_no")).append("次");
			for(int i=0; i<cps.size(); i++){
				RobTicket_CP robTicket_CP = cps.get(i);
				Integer ticketType = robTicket_CP.getTicketType();
				String ticType = RobTicketUtils.getTicketTypeChinese(ticketType);
				if(i > 0){
					content.append("、");
				}
				content.append("【"+ticType+"】");
				content.append(robTicket_CP.getUserName())
				       .append(robTicket_CP.getSeatNo()).append("的").append(robTicket_CP.getOrderTicketSeat());
			}
			content.append("席位的车票，请您携身份证到车站售票点或者自助机领取纸质车票，祝您旅途愉快！【19易】");
			//mobileMsgUtil.send(contact.get("contact_phone"), content.toString());
	}

	

	@Override
	public void updateCPRobSucc(List<HashMap<String, String>> adultCPS) throws Exception {
		robTicketDao.updateCPRobSucc(adultCPS);
	}

	@Override
	public void updateWithCtripCallback(String pattern, String channel, String message) throws Exception {
		Jedis jedis = null;
		for (int i = 0; i < channels.length; i++) {
			String c = channels[i];
			if (channel.equals(c)) {
				if (c.equalsIgnoreCase("ROB_CANCEL_SUCC")) {
					// TODO 携程取消成功,前台 出票失败 ,全额退款(票价,服务费)
					String orderId = message;
					logger.info("---携程取消成功,前台 出票失败 ,全额退款(票价,服务费)----" + orderId);
					try {
						RobTicket_OI oi = new RobTicket_OI();
						oi.setOrderId(orderId);
						oi.setOrderStatus(RobTicketVo.OI_ORDER_STATUS_EXT_FAIL);
						oi.setOptionTime(new Date());
						updateFrontAndBack(oi);
						logger.info("---携程取消成功,前台 出票失败 ,全额退款(票价,服务费)----数据库状态更改成功---" + orderId + "--状态--"
								+ RobTicketVo.OI_ORDER_STATUS_EXT_FAIL);
						robRefund.refundFromEOP(orderId, "", c);
						insertJLHistory(orderId, "ROB_CANCEL_SUCC,用户主动取消抢票成功,当前全额退款", "ctrip_callback");
						jedis = JedisUtil.getJedis();
						jedis.hset(JLTimer.SET_PREFIX, "ROB_CANCEL_SUCC" + orderId, JLTimer.YES);
						logger.info("--取消成功,退款成功--" + orderId);
					} catch (Exception e) {
						logger.error("---携程取消业务失败--单号("+orderId+")--错误信息-->" + e.toString());
						e.printStackTrace();
					} finally {
							jedis.close();
					}
				} else if (c.equalsIgnoreCase("ROB_DIFF_REFUND")) {
					// TODO 1.携程抢票成功,差额退款(最高票价 减去 实际 票价) 2.退票退款
					String[] split = message.split("@@##");
					String criptId = split[0];
					String json = split[1];
					try {
						JSONObject object = JSON.parseObject(json);
						JSONObject TrainOrderService = object.getJSONObject("TrainOrderService");
						String refundMoney = TrainOrderService.getString("TotalRefundAmount");
						Map<String, String> jl = queryOrderIdByCtripId(criptId);
						String orderId = jl.get("order_id");
						robRefund.refundFromEOP(orderId, refundMoney, c);
						insertRobRefundFromCtrip(orderId,refundMoney,object);
						insertJLHistory(orderId, "ROB_DIFF_REFUND,携程差额退款(或退票退款),本次退款金额为" + refundMoney, "ctrip_callback");
						jedis = JedisUtil.getJedis();
						jedis.hset(JLTimer.SET_PREFIX, JLTimer.DIFF_REFUND + criptId, JLTimer.YES);
						logger.info("携程差额退款成功--行程订单-" + criptId + "-19e订单号-" + orderId + "-退款金额-" + refundMoney);
					} catch (Exception e) {
						logger.info("携程差额退款失败了--->"+ e.toString());
						e.printStackTrace();
					} finally {
						jedis.close();
					}
				} else if (c.equalsIgnoreCase("ROB_EXC_REFUND")) {
					// TODO 携程异常 ,前台出票失败 全额退款
					String[] split = message.split("@@##");
					String criptId = split[0];
					try {
						Map<String, String> jl = queryOrderIdByCtripId(criptId);
						String orderId = jl.get("order_id");
						RobTicket_OI oi = new RobTicket_OI();
						oi.setOrderId(orderId);
						oi.setOrderStatus(RobTicketVo.OI_ORDER_STATUS_EXT_FAIL);
						oi.setOptionTime(new Date());
						updateFrontAndBack(oi);
						insertJLHistory(orderId, "ROB_EXC_REFUND,携程异常,携程发起异常取消 ,19e前台全额退款", "ctrip_callback");
						robRefund.refundFromEOP(orderId, "", c);
						jedis = JedisUtil.getJedis();
						jedis.hset(JLTimer.SET_PREFIX, JLTimer.EXC_CANCEL + criptId, JLTimer.YES);
						logger.info("携程异常退款成功--携程订单-" + criptId + "-19e订单号-" + orderId + "-退款金额-全款");
					} catch (Exception e) {
						logger.info("携程异常退款失败了-->"+ e.toString());
						e.printStackTrace();
					} finally {
						jedis.close();
					}
				} else if (c.equalsIgnoreCase("ROB_LOCK")) {
					// TODO 取消失败,订单锁定 (如果当前订单 为 出票中 状态 改为 80 订单锁定中 不允许取消)
					// 用户不允许 取消 -- 前台 操作 "已锁定"-- 用户点击 给个 弹窗 解释
					logger.info("携程锁定通知进入===>" + message);
					String[] split = message.split("@@##");
					String criptId = split[0];
					try {
						Map<String, String> jl = queryOrderIdByCtripId(criptId);
						String orderId = jl.get("order_id");
						RobTicket_OI oi = new RobTicket_OI();
						oi.setOrderId(orderId);
						oi.setOrderStatus(RobTicketVo.OI_ORDER_STATUS_LOCK);
						oi.setOptionTime(new Date());
						logger.info("携程锁定更改前后台状态为锁定开始-->" + orderId);
						updateFrontAndBack(oi);
						logger.info("携程锁定更改前后台状态为锁定成功-->" + orderId);
						insertJLHistory(orderId, "ROB_LOCK,携程锁定当前订单,不可取消", "ctrip_callback");
						logger.info("携程锁定插入历史操作表成功-->" + orderId);
						jedis = JedisUtil.getJedis();
						jedis.hset(JLTimer.SET_PREFIX, JLTimer.LOCK + criptId, JLTimer.YES);
						logger.info("携程锁定通知成功--携程订单-" + criptId + "-19e订单号-" + orderId);
					} catch (Exception e) {
						logger.info("携程锁定失败了--->"+ e.toString());
						e.printStackTrace();
					} finally {
						jedis.close();
					}
				} else if (c.equalsIgnoreCase("ROB_NO_TICKET")) {
					// TODO 携程无票,前台 出票失败,全额退款 (票价 服务费)
					String[] split = message.split("@@##");
					String criptId = split[0];
					try {
						Map<String, String> jl = queryOrderIdByCtripId(criptId);
						String orderId = jl.get("order_id");
						RobTicket_OI oi = new RobTicket_OI();
						oi.setOrderId(orderId);
						oi.setOrderStatus(RobTicketVo.OI_ORDER_STATUS_EXT_FAIL);
						oi.setOptionTime(new Date());
						updateFrontAndBack(oi);
						robRefund.refundFromEOP(orderId, "", c);
						insertJLHistory(orderId, "ROB_NO_TICKET,携程返回无票,前台全额退款", "ctrip_callback");
						jedis = JedisUtil.getJedis();
						jedis.hset(JLTimer.SET_PREFIX, JLTimer.NO_TICKET + criptId, JLTimer.YES);
						logger.info("携程无票退款成功--携程订单-" + criptId + "-19e订单号-" + orderId + "-退款金额-全款");
					} catch (Exception e) {
						logger.info("携程无票退款失败了-->" + e.toString());
						e.printStackTrace();
					} finally {
						jedis.close();
					}
				} else if (c.equalsIgnoreCase("ROB_SUCC")) {
					// TODO 抢票成功 实际票 信息 出票成功
					String[] split = message.split("@@##");
					String criptId = split[0];
					String json = split[1]; 
					try {
						Map<String, String> hc = queryOrderIdByCtripId(criptId);
						String orderId = hc.get("order_id");
						//String contact_phone = hc.get("contact_phone");
						RobTicket_OI oi = new RobTicket_OI();
						oi.setCtripOrderId(criptId);
						oi.setOrderId(orderId);
						oi.setOrderStatus(RobTicketVo.OI_ORDER_STATUS_EXT_SUCC);
						oi.setOptionTime(new Date());
						//oi.setContactPhone(contact_phone);
						updateFrontAndBackWithSucc(oi, json);
						jedis = JedisUtil.getJedis();
						jedis.hset(JLTimer.SET_PREFIX, JLTimer.ROB_SUCC + criptId, JLTimer.YES);
					} catch (Exception e) {
						logger.info("携程有票逻辑失败了--->" + e.toString());
						e.printStackTrace();
					} finally {
						jedis.close();
					}
				} else if (c.equalsIgnoreCase("ROB_REFUND_TICKET")) {
					// TODO 退票结果
					String[] split = message.split("@@##");
					String criptId = split[0];
					String json = split[1];
					try {
						jedis = JedisUtil.getJedis();
						Map<String, String> jl = queryOrderIdByCtripId(criptId);
						String orderId = jl.get("order_id");
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
						map.put("order_id", orderId);
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
						if(cp == null){
							logger.info("退票查不到数据--"+orderId);
							return;
						}
						String cpid = cp.getCpId();
						String cp_status = "";
						if (status.equals("1")) {
							cp_status = RobTicket_CP.REFUND_SUCC;
						}else{
							cp_status = RobTicket_CP.REFUND_FAIL;
						}
						updateFrontBackCP_Refund(new String[]{cpid},cp_status);
						if (status.equals("1")) {
							//robRefund.refundFromEOP(orderId, refundMoney, bizType);
							RobTicket_OI oi = new RobTicket_OI();
							oi.setOrderId(orderId);
							oi.setOrderStatus(RobTicketVo.OI_ORDER_STATUS_REFUND_SUCC);
							updateFrontAndBack(oi);
						}else{
							RobTicket_OI oi = new RobTicket_OI();
							oi.setOrderId(orderId);
							oi.setOrderStatus(RobTicketVo.OI_ORDER_STATUS_REFUND_FAIL);
							updateFrontAndBack(oi);
						}
						insertJLHistory(orderId, "携程退票回调接收成功,CP_ID("+cpid+"),退票结果("+reason+")", "ctrip_callback");
						jedis.hset(JLTimer.SET_PREFIX, JLTimer.TICKET_RETURN + criptId, JLTimer.YES);
					} catch (Exception e) {
						logger.error("携程退票通知逻辑处理失败-->单号为"+criptId+"原始JSON-->"+json+"--错误原因--->"+e.toString());
						e.printStackTrace();
					}finally {
						jedis.close();
					}
					
				}

			}

		}

	}

	@Override
	public void insertRobRefundFromCtrip(String orderId, String refundMoney, JSONObject object) {
		String TimeStamp = object.getJSONObject("Authentication").getString("TimeStamp");
		String OrderTradeNo = object.getJSONObject("TrainOrderService").getJSONObject("OrderInfo").getString("OrderTradeNo");
		Map<String, String> map = new HashMap<String, String>();
		map.put("order_id", orderId);
		map.put("refund_money", refundMoney);
		map.put("ctrip_ref_time", TimeStamp);
		map.put("ctrip_trade_no", OrderTradeNo);
		map.put("create_time", DateUtil.dateToString(new Date(), DateUtil.DATE_FMT3));
		robTicketDao.insertRobRefundFromCtrip(map);
		
	}

	@Override
	public RobTicket_CP queryRefundCp(HashMap<String, Object> map)throws Exception {
		
		return robTicketDao.queryRefundCp(map);
	}

	@Override
	public int selectOrderInfoByConditionsCount(Map<String, String> conditions) throws Exception {

		return robTicketDao.selectOrderInfoByConditionsCount(conditions);
	}

	@Override
	public void updateFrontBackCP_Refund(String[] cpids, String status) {
		logger.info("携程退款回调--票号"+JSON.toJSONString(cpids)+"状态["+status+"]");
		robTicketDao.updateFrontBackCP_Refund(cpids, status);
	}

	

}
