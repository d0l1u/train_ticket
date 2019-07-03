package com.l9ea.train.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.l9e.train.po.OrderVo;
import com.l9e.train.po.Passenger;
import com.l9e.train.util.Consts;
import com.l9e.train.util.JsonlibUtil;
import com.l9ea.train.service.BaseHttpService;
import com.l9ea.train.service.OrderService;

import net.sf.json.JSONObject;

@Service("orderService")
public class OrderServiceImpl extends BaseHttpService implements OrderService{

	private Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
	
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
