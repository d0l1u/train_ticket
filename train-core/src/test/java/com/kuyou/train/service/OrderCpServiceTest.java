package com.kuyou.train.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.kuyou.train.MvcBaseTest;
import com.kuyou.train.common.status.OrderStatus;
import com.kuyou.train.common.util.DateUtil;
import com.kuyou.train.entity.po.OrderCpPo;
import com.kuyou.train.entity.po.OrderPo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * OrderCpServiceTest
 *
 * @author taokai3
 * @date 2018/11/5
 */
@Slf4j
public class OrderCpServiceTest extends MvcBaseTest {

    @Resource
    private OrderCpService orderCpService;

    @Resource
    private OrderService orderService;

    @Test
    public void selectByStatus() {
        OrderCpPo orderCpPo = orderCpService.selectByCpId("EXCP1708121626261073");
        log.info("selectByStatus结果:{}", JSON.toJSONString(orderCpPo));
    }

    @Test
    public void selectStuck(){
        List<OrderPo> orderPoList = orderService.selectStuck(Lists.newArrayList(OrderStatus.BOOK_RESEND, OrderStatus.BOOK_ING),
                DateUtil.add(-3, TimeUnit.MINUTES));
        log.info("orderPoList:{}", orderPoList);

    }

}
