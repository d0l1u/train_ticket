package com.l9e.transaction.job;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.l9e.transaction.thread.OrderResultThread;
import com.l9e.transaction.vo.TicketEntrance;
import com.l9e.util.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.jiexun.iface.util.StringUtil;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.CommonService;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.service.ReceiveNotifyService;
import com.l9e.transaction.vo.ExternalLogsVo;
import com.l9e.transaction.vo.OrderInfo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 向合作商户发送订单处理完成通知job
 * 
 * @author zhangjc02
 *
 */
@Component("orderResultJob")
public class OrderResultJob {
	private static final Logger logger = Logger.getLogger(OrderResultJob.class);
	@Resource
	ReceiveNotifyService receiveService;

	@Resource
	private OrderService orderService;

	@Resource
	private CommonService commonService;

	@Resource
	private ThreadPoolTaskExecutor taskExecutorOrderResult;//出票结果回调线程池

	@Resource
	private ApplicationContext applicationContext;


	public void sendMerchantResultData() {

		List<Map<String, String>> waitNotifyList = receiveService.findOrderResultNotify();
		logger.info("出票结果回调："+waitNotifyList);
		if (waitNotifyList != null && waitNotifyList.size() > 0) {
			logger.info("waitNotifyList-size:" + waitNotifyList.size());
			for (Map<String, String> waitNotify : waitNotifyList) {
				logger.info("json-waitNotify:" + JSONObject.fromObject(waitNotify).toString());
				OrderResultThread orderResultThread= ContextUtil.getBean("orderResultThread",OrderResultThread.class);
				//OrderResultThread orderResultThread= applicationContext.getBean("orderResultThread",OrderResultThread.class);
				orderResultThread.setParamMap(waitNotify);
				taskExecutorOrderResult.submit(orderResultThread);//订单异步处理
			}
		}
	}


}
