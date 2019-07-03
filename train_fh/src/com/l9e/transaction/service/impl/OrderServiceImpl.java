package com.l9e.transaction.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.l9e.common.Consts;
import com.l9e.transaction.service.BaseHttpService;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.vo.Order;
import com.l9e.transaction.vo.OrderVo;
import com.l9e.transaction.vo.Passenger;
import com.l9e.util.JsonlibUtil;
import common.Logger;

@Service("orderService")
public class OrderServiceImpl extends BaseHttpService implements OrderService{
	
	private static final Logger logger = Logger.getLogger(OrderServiceImpl.class);

	
	@Override
	public List<Passenger> getPassengersByOrderId(String orderId) {
		List<Passenger> passengers = new ArrayList<Passenger>();
		
		try {
			String result = requestPost(Consts.GET_ORDER_PASSENGER, "orderId=" + orderId, "utf-8", 3, 500);
			logger.info("get order passengers , passenger result : " + result);
			if(!StringUtils.isEmpty(result)) {
				JSONObject resultJsonObject = JSONObject.fromObject(result);
				if(resultJsonObject.has("success") && resultJsonObject.getBoolean("success")) {
					passengers = JsonlibUtil.json2list(resultJsonObject.getJSONArray("data").toString(), Passenger.class);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return passengers;
	}

	@Override
	public void updateOrder(OrderVo order) {
		/*订单实体转json串*/
		String orderJson = "";
		
		try {
			orderJson = JsonlibUtil.bean2json(order);
		} catch (Exception ee) {
			logger.info("json order error ,order : " + order + ", exception : " + ee.getMessage());
			ee.printStackTrace();
		}
		
		try {
			String result = requestPost(Consts.UPDATE_ORDER, "order=" + orderJson, "utf-8", 0, 0);
			logger.info("update order result : " + result);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
