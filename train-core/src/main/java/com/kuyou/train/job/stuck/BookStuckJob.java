package com.kuyou.train.job.stuck;

import com.google.common.collect.Lists;
import com.kuyou.train.common.constant.SupplierCode;
import com.kuyou.train.common.log.MDCLog;
import com.kuyou.train.common.status.ChangeStatus;
import com.kuyou.train.common.status.OrderStatus;
import com.kuyou.train.common.util.DateUtil;
import com.kuyou.train.entity.po.ChangePo;
import com.kuyou.train.entity.po.OrderPo;
import com.kuyou.train.service.HistoryService;
import com.kuyou.train.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * BookStatusJob
 *
 * @author taokai3
 * @date 2018/10/26
 */
@Slf4j
@Component("bookStuckJob")
public class BookStuckJob  {

	@Resource
	private OrderService orderService;

	@Resource
	private HistoryService historyService;

	@Value("${stuckMinutes}")
	private Integer stuckMinutes;

	@MDCLog
	@Scheduled(cron = "0/10 * 5-23 * * ?")
	public void update() {
		log.info("********* 重置【预订】卡单 *********");
		List<OrderPo> orderPoList = orderService.selectStuck(Lists.newArrayList(OrderStatus.BOOK_RESEND, OrderStatus.BOOK_ING),
				DateUtil.add(-stuckMinutes, TimeUnit.MINUTES));

		if (orderPoList.isEmpty()) {
			return;
		}
		for (OrderPo orderPo : orderPoList) {
			String orderId = orderPo.getOrderId();
			String orderStatus = orderPo.getOrderStatus();
			String supplierType = orderPo.getSupplierType();
            Integer resendNum = orderPo.getResendNum();

            //航天华有不进行重试
            if(SupplierCode.HTHY.equals(supplierType)){
                continue;
            }
            if(resendNum == null){
                resendNum = 1;
            }
            resendNum = resendNum + 1;
            try {
                if(resendNum > 3){
                    log.info("orderId:{}超过{}分钟未响应结果，转人工处理", orderId, stuckMinutes);
                    OrderPo updatePo = new OrderPo();
                    updatePo.setOrderStatus(OrderStatus.BOOK_MANUAL);
                    updatePo.setResendNum(0);
                    updatePo.setReturnOptlog("57");
                    orderService.updateByOrderId(updatePo, orderId, orderStatus);
                    historyService.insertBookLog(orderId, String.format("超过%s分钟未响应结果，转人工处理", stuckMinutes));
                } else {
                    log.info("orderId:{}超过{}分钟未响应结果，重试", orderId, stuckMinutes);
                    OrderPo updatePo = new OrderPo();
                    updatePo.setOrderStatus(OrderStatus.BOOK_RESEND);
                    updatePo.setResendNum(resendNum);
                    orderService.updateByOrderId(updatePo, orderId, orderStatus);
                    historyService.insertBookLog(orderId, String.format("超过%s分钟未响应结果，重试", stuckMinutes));
                }
			} catch (Exception e) {
				log.info("BookStuckJob.resetStuckStatus 异常:{}", e.getClass().getSimpleName(), e);
			}
		}
	}
}
