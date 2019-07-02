package com.kuyou.train.job.producer;

import com.google.common.collect.Lists;
import com.kuyou.train.common.log.MDCLog;
import com.kuyou.train.common.status.OrderStatus;
import com.kuyou.train.entity.po.OrderPo;
import com.kuyou.train.service.HistoryService;
import com.kuyou.train.service.OrderService;
import com.kuyou.train.thread.producer.BookProducerThread;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * BookConsumer：预订占位消费
 *
 * @author taokai3
 * @date 2018/10/26
 */
@Slf4j
@Component("bookProducer")
public class BookProducer {

	@Value("${producer.bookMax}")
	private int bookMax;

	@Resource
	private OrderService orderService;

	@Resource
	protected ThreadPoolTaskExecutor taskExecutor;

	@Resource
	private HistoryService historyService;

	@MDCLog
	@Scheduled(cron = "* * 5-23 * * ?")
	public void consumer() {
		log.info("********* BookProducer Start *********");

		//获取状态为 的改签订单
		List<OrderPo> orderPos = orderService.selectByStatus(Lists.newArrayList(OrderStatus.BOOK_INIT, OrderStatus.BOOK_RESEND), bookMax);
		if (CollectionUtils.isEmpty(orderPos)) {
			log.info("预订占位 orderPos 为空，不做处理");
			return;
		}
		for (OrderPo orderPo : orderPos) {
			String orderId = orderPo.getOrderId();
			String orderStatus = orderPo.getOrderStatus();
			try {
				log.info("修改预订单状态为[11:预订中]orderId:{}", orderId);
				int result = orderService.updateStatusPre(orderId, orderStatus, OrderStatus.BOOK_ING);
				if (result > 0) {
					historyService.insertBookLog(orderId, "开始预订占位");
					//将需要处理的处理的预订单通过线程池处理
					taskExecutor.execute(new BookProducerThread(orderPo));
				}
			} catch (Exception e) {
				log.info("BookProducer.consumer异常:{}", e.getClass().getSimpleName(), e);
			}
		}
	}
}
