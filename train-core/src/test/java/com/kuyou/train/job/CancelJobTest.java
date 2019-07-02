package com.kuyou.train.job;

import com.kuyou.train.MvcBaseTest;
import com.kuyou.train.common.status.ChangeStatus;
import com.kuyou.train.common.status.OrderStatus;
import com.kuyou.train.common.util.DateUtil;
import com.kuyou.train.entity.po.ServicePo;
import com.kuyou.train.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * CancelJobTest
 *
 * @author taokai3
 * @date 2018/11/26
 */
@Slf4j
public class CancelJobTest extends MvcBaseTest {

    @Resource
    private OrderService orderService;


    @Test
    public void selectStuckService() {
        List<ServicePo> servicePos = orderService
                .selectStuckService(OrderStatus.CANCEL_ING, ChangeStatus.CANCEL_ING, DateUtil.add(-2, TimeUnit.DAYS));
        log.info("selectStuckService.servicePos:{}", servicePos);
    }


    @Test
    public void selectWaitService() {
        List<ServicePo> servicePos = orderService
                .selectWaitService(OrderStatus.CANCEL_INIT, ChangeStatus.CANCEL_INIT, 100);
        log.info("selectWaitService.servicePos:{}", servicePos);
    }
}
