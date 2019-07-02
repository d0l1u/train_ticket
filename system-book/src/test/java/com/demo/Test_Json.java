package com.demo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.train.system.booking.entity.Order;
import com.train.system.booking.entity.Passenger;
import com.train.system.booking.service.LogService;
import com.train.system.booking.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import sun.security.krb5.internal.Ticket;

import java.util.List;

/**
 * Test
 *
 * @author taokai3
 * @date 2018/6/18
 */
public class Test_Json {


    @org.junit.Test
    public void log() throws Exception {
        String str = "{\"orderId\":\"20150619236454251\",\"status\":\"0000\",\"message\":\"success\",\"startMillis\":1529410686859,\"endMillis\":1529410706335,\"body\":{\"sequence\":\"E046773063\",\"trainCode\":\"2636\",\"departureDate\":\"2018-07-17\",\"departureTime\":\"2018-07-17 04:25\",\"arrivalDate\":\"2018-07-17\",\"arrivalTime\":\"2018-07-17 04:40\",\"fromStationName\":\"包头\",\"fromStationCode\":\"BTC\",\"toStationName\":\"包头东\",\"toStationCode\":\"BDC\",\"passengerCount\":5,\"totalPrice\":\"6.0\",\"passengers\":[{\"passengerNo\":\"20150619236454252\",\"name\":\"张玉朋\",\"subSequence\":\"E0467730631030033\",\"cardType\":\"1\",\"cardNo\":\"370724198704210751\",\"ticketType\":\"1\",\"seatType\":\"1\",\"boxNo\":\"03\",\"boxName\":\"03\",\"seatNo\":\"0033\",\"seatName\":\"033号\",\"price\":6.0,\"loseTime\":\"2018-06-19 20:48:20\"}]}}";
        JSONObject responseJson = JSONObject.parseObject(str);
        String orderId = responseJson.getString("orderId");
        String status = responseJson.getString("status");
        String message = responseJson.getString("message");
        String data = responseJson.getString("body");

        Order responseOrder = JSONObject.parseObject(data, Order.class);
        responseOrder.setOrderId(orderId);


    }

}
