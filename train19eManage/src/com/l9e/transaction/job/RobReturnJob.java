package com.l9e.transaction.job;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.l9e.transaction.service.RobTicketService;
import com.l9e.transaction.vo.RobTicketVo.RobTicket_CP;
import com.l9e.util.JedisUtil;
import com.l9e.util.RobTicketUtils;

import redis.clients.jedis.Jedis;

//@Component
public class RobReturnJob {
	private static Logger logger = Logger.getLogger(RobReturnJob.class);
	@Autowired
	RobTicketService service;
	HashMap<String, String> paramMap = new HashMap<String, String>();
	private static String log_pattern = "抢票退票定时任务[%s],退票order_id[%s],退票cp_id[%s]";
	private static final String ROB_KEY_REDIS = "ROB_TICKET_RETURN";
	private static final String ROB_SEP = "##";

	// @Scheduled(cron="0 0/5 * * * ?")
	public void excute() {
		logger.info(String.format(log_pattern, "进入定时任务", "", ""));
		Jedis jedis = JedisUtil.getJedis();
		String value = jedis.lpop(ROB_KEY_REDIS);
		if (StringUtils.isEmpty(value)) {
			logger.info(String.format(log_pattern, "当前队列空", "", ""));
			return;
		}
		String[] split = value.split(ROB_SEP);
		String result = "fail";
		String cp_id = "";
		String order_id = "";
		try {
			cp_id = split[0];
			order_id = split[1];
			result = RobTicketUtils.refundManul(service, cp_id);
			String start = String.format(log_pattern, "开始", order_id, cp_id);
			logger.info(start);
			if (result.indexOf("SUCCESS") != -1) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("order_id", order_id);
				map.put("order_optlog", "携程退票申请HTTP请求成功,系统等待携程退票通知");
				map.put("create_time", new Date());
				map.put("opter", "ctrip-http");
				service.insertJLHistory(map);
				HashMap<String, String> refund = new HashMap<String, String>();
				refund.put("cp_id", cp_id);
				refund.put("status", RobTicket_CP.REFUND_REQ);
				service.updateFrontBackCP_Refund(refund);
				String succ = String.format(log_pattern, "成功", order_id, cp_id);
				logger.info(succ);
			}
		} catch (Exception e) {
			// 将未成功的放入到队列的末尾;
			if (StringUtils.isNotEmpty(cp_id) && StringUtils.isNotEmpty(order_id)) {
				jedis.rpush(ROB_KEY_REDIS, cp_id + ROB_SEP + order_id);
				String re = String.format(log_pattern, "重新放入队列", order_id, cp_id);
				logger.info(re);
			}
			String error = String.format(log_pattern, "失败", order_id, cp_id);
			logger.error(error + "--" + e.toString());
		} finally {
			JedisUtil.returnJedis(jedis);
		}
	}
}
