package com.train.system.center.job;

import com.train.system.center.entity.Order;
import com.train.system.center.service.OrderService;
import com.train.system.center.thread.PayThread;
import com.train.system.center.util.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 支付订单生产者 PayProcuderJob
 *
 * @author taokai3
 * @date 2018/6/25
 */
@Component
public class BookingPayJob {

	private Logger logger = LoggerFactory.getLogger(BookingPayJob.class);

	@Resource
	private OrderService orderService;

	@Resource
	private ThreadPoolTaskExecutor taskExecutor;

	@Scheduled(cron = "0/3 * 5-23 * * ?")
	public void producer() {
		long begin = System.currentTimeMillis();
		logger.info("【支付】***** Pay Procuder Start *****");

		// 获取指定数量的占位订单，状态为开始占位，占位重发和长时间占位中的订单(操作时间 + 3分钟 < 当前时间);
		List<Order> orderList = orderService.queryListWaitPay(20);
		if (orderList == null || orderList.isEmpty()) {
			logger.info("【支付】获取等待处理的支付订单结果为空,等待下一次任务执行");
			return;
		}

		int size = orderList.size();
		logger.info("【支付】Order List Size:{}", size);

		// 每个线程处理五条数据
		int index = 0;
		while (index < size) {
			int temp = index;
			index += 5;
			if (index > size) {
				index = size;
			}
			List<Order> subList = orderList.subList(temp, index);

			logger.info("【支付】提交 index[{}, {}]", temp, index);
			PayThread thread = SpringContextUtil.getBean("payThread", PayThread.class);
			thread.setOrderList(subList);
			taskExecutor.submit(thread);
		}
		logger.info("【支付】任务执行完毕,耗时:{}ms,等待下一次任务执行", System.currentTimeMillis() - begin);
	}

}
