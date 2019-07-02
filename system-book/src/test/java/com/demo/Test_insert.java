package com.demo;

import com.train.system.booking.entity.Order;
import com.train.system.booking.service.LogService;
import com.train.system.booking.service.OrderService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Test
 *
 * @author taokai3
 * @date 2018/6/18
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:context/spring-*.xml"})
public class Test_insert {

    @Autowired
    private LogService logService;

    @Autowired
    private OrderService orderService;



    @org.junit.Test
    public void log() throws InterruptedException {
        Thread.sleep(1000 * 1200);
        Order order =  new Order();
        order.setOrderStatus("55");
        order.setTotalPrice(new BigDecimal(20));
        order.setDepartureTime(new Date());
        order.setArrivalTime(new Date());
        System.out.println(logService.insertBookingLog("20150415234269206","测试","tk"));

    }

//    @org.junit.Test
    public void updateOrder(){
        System.out.println(orderService.updateOrderStatus("20150415234269206","01","99"));
    }
}
