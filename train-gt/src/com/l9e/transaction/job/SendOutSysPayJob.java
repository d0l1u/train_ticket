package com.l9e.transaction.job;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.service.ReceiveNotifyService;
import com.l9e.transaction.thread.SendSystemThread;
import com.l9e.util.ContextUtil;

/**
 * 向EOP请求发货完成通知job
 * 
 * @author zhangjun
 *
 */
@Component("sendOutSysPayJob")
public class SendOutSysPayJob extends BaseController {

	private static final Logger logger = Logger.getLogger(SendOutSysPayJob.class);

	@Resource
	private OrderService orderService;

	@Resource
	private ReceiveNotifyService receiveService;

	@Resource
	private ThreadPoolTaskExecutor taskExecutor;

	/**
	 * 发货
	 */
	public void send() {
		logger.info("============ 通知出票系统支付 BEGIN =============");
		// 查询等待通知的数据
		List<Map<String, String>> sendList = receiveService.queryEopAndPayNotify();
		if (sendList != null && !sendList.isEmpty()) {
			logger.info("本次待通知数据共 " + sendList.size() + " 条");
			for (Map<String, String> sendMap : sendList) {
				SendSystemThread thread = ContextUtil.getBean("sendSystemThread", SendSystemThread.class);
				thread.setSendMap(sendMap);
				taskExecutor.submit(thread);
			}
		} else {
			logger.info("本次待通知数据为空,等待下一次任务执行...");
		}
	}
}
