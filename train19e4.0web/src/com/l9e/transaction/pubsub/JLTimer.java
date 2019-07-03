package com.l9e.transaction.pubsub;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.util.JedisUtil;

import redis.clients.jedis.Jedis;

@SuppressWarnings("all")
@Component
public class JLTimer {

	private static Logger logger = Logger.getLogger(JLTimer.class);

	// ===========================================//
	// 业务类型 key 前缀
	/** 携程方 异常 取消 订单 */
	public static final String EXC_CANCEL = "exc_cancel_";
	/** 携程锁定消息 */
	public static final String LOCK = "lock_";
	/** 携程无票 */
	public static final String NO_TICKET = "no_ticket_";
	/** 携程有票消息 */
	public static final String ROB_SUCC = "rob_succ_";
	/** 携程 差额 退款,(实际座位 儿童票 差额) */
	public static final String DIFF_REFUND = "diff_refund_";
	/** 携程退票结果 */
	public static final String TICKET_RETURN = "ticket_return_";

	public static final String SET_PREFIX = "ctrip_callback";
	public static final String NO = "no";
	public static final String YES = "yes";
	// ===========================================//

	private static ReentrantLock lock = new ReentrantLock();

	public static void writeToCache(String prefix, String ctrip_orderId, String value) {
		try {
			Jedis jedis = JedisUtil.getJedis();
			Map<String, String> map = new HashMap<String, String>();
			map.put(prefix + ctrip_orderId, value);
			jedis.hmset(SET_PREFIX, map);
		} catch (Exception e) {
			logger.error("携程回调信息写入   cache 错误" + prefix + ctrip_orderId);
		}

	}

	public static String getCacheResult(String prefix, String ctrip_orderId) {
		try {
			Jedis jedis = JedisUtil.getJedis();
			return jedis.hmget(SET_PREFIX, prefix + ctrip_orderId).get(0);
		} catch (Exception e) {
			logger.error("携程回调信息读取  cache 错误" + prefix + ctrip_orderId);
		}
		return "";
	}

	public static void main(String[] args) {
		String a[] = {"ROB_CANCEL_SUCCHC_ROB1701180957577845"};
		Jedis jedis = JedisUtil.getJedis();
		for (int i = 0; i < a.length; i++) {
			jedis.hset(JLTimer.SET_PREFIX, a[i], JLTimer.NO);
		}

		jedis.close();
	}

}
