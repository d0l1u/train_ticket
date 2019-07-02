package com.train.system.center.job;

import com.train.system.center.entity.Change;
import com.train.system.center.service.ChangeService;
import com.train.system.center.thread.ChangePayThread;
import com.train.system.center.util.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class ChangePayJob {

	private Logger logger = LoggerFactory.getLogger(ChangePayJob.class);

	@Resource
	private ChangeService changeService;

	@Resource
	private ThreadPoolTaskExecutor taskExecutor;

	@Scheduled(cron = "0/5 * 5-23 * * ?")
	public void producer() {

		long begin = System.currentTimeMillis();
		logger.info("【改签支付】***** Change Order Procuder Start *****");

		// 获取指定数量的占位订单，状态为开始占位，占位重发和长时间占位中的订单(操作时间 + 3分钟 < 当前时间);
		List<Change> orderList = changeService.queryWaitPay(20);
		if (orderList == null || orderList.isEmpty()) {
			logger.info("【改签支付】获取等待处理的订单结果为空,等待下一次任务执行");
			return;
		}

		int size = orderList.size();
		logger.info("【改签支付】Order List Size:{}", size);

		// 每个线程处理五条数据
		int index = 0;
		while (index < size) {
			int temp = index;
			index += 5;
			if (index > size) {
				index = size;
			}
			List<Change> subList = orderList.subList(temp, index);

			logger.info("【改签支付】提交 index[{}, {}]", temp, index);
			ChangePayThread thread = SpringContextUtil.getBean("changePayThread", ChangePayThread.class);
			thread.setOrderList(subList);
			taskExecutor.submit(thread);
		}
		logger.info("【改签支付】任务执行完毕,耗时:{}ms,等待下一次任务执行", System.currentTimeMillis() - begin);

	}
}
