package com.l9e.common;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.l9e.transaction.dao.RedisDao;
import com.l9e.transaction.mq.MqService;
import com.l9e.transaction.service.impl.SysSettingServiceImpl;
import com.l9e.util.HttpPostUtil;
import com.l9e.util.MemcachedUtil;
import com.l9e.util.StrUtil;
import com.l9e.util.WorkIdNum;

public abstract class MqOrderBase {

	static Logger logger = Logger.getLogger(MqOrderBase.class.getClass());

	public abstract String channel();// 设置渠道

	public abstract Integer sec();// 设置执行时间

	public abstract Integer waitsec();// 设置等待时间

	public abstract Integer getNum();// 获取消息的数量

	public abstract Integer goNum();// 机器人预订的数量

	public abstract Integer sqltime();// 执行时间

	private Timer timer;

	@Resource
	private RedisDao redisDao;

	@Autowired
	private MqService mqService;

	@PostConstruct
	public void init() {
		logger.info("### MqOrderBase Init...");
		this.setCoustomer(sec(), channel(), waitsec(), getNum(), goNum(), sqltime());
	}

	/**
	 * 开始消费
	 * 
	 * @author: taoka
	 * @date: 2018年1月5日 下午2:17:59
	 * @param period
	 *            定时
	 * @param channel
	 * @param waitsec
	 * @param getnum
	 * @param gonum
	 * @param unlockTime
	 *            竞争锁存活时间
	 */
	public void setCoustomer(int period, final String channel, final int waitsec, final int getnum, final int gonum, final int unlockTime) {
		long delay = 3 * 1000;
		logger.info("### channel:" + channel + ", period:" + period + ", delay:" + delay + ", waitsec:" + waitsec + ", getnum:" + getnum + ", gonum:" + gonum
				+ ", unlockTime:" + unlockTime);
		final String queueName = StrUtil.getOrderQueue(channel);
		logger.info("### queueName:" + queueName);
		redisDao.delVal(queueName);

		logger.info("### 启动time程序...");
		timer = new Timer();

		// 轮询
		logger.info("### 进行轮询执行任务...");
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				// 创建日志前缀
				String uuid = "[";
				Random random = new Random();
				for (int i = 0; i < 5; i++) {
					uuid = uuid + random.nextInt(9);
				}
				uuid = uuid + "] ";

				logger.info(uuid + "========== TimerTask Begin ==========");
				String lockKey = StrUtil.getOrderRedis(channel);
				logger.info(uuid + "竞争锁lockKey:" + lockKey);

				// 设置竞争锁
				boolean flag = redisDao.setNXVal(lockKey, "1", 0);
				logger.info(uuid + "设置竞争锁结果:" + flag);

				long ttl = redisDao.getTTL(lockKey);
				logger.info(uuid + "竞争锁存活时间:" + ttl);

				if (!flag) {
					logger.info(uuid + "未获取到竞争锁,判断锁是否已经解除");
					if (ttl != -2) {
						logger.info(uuid + "释放竞争锁");
						redisDao.delVal(lockKey);
					}
					return;
				} else {
					if (ttl == -1) {
						logger.info(uuid + "设置自动解锁时间:" + unlockTime);
						redisDao.setExpire(lockKey, unlockTime);
					}
				}

				// 获取记录数量
				Integer count = (int) redisDao.getINDEVal(queueName);
				logger.info(uuid + channel + "-记录存入执行数量:" + count);

				SysSettingServiceImpl sysImpl = new SysSettingServiceImpl();
				// ========== 消费开关限制
				// 默认开启状态
				String trainFhStatus = "1";
				MemcachedUtil memcached = MemcachedUtil.getInstance();
				// 先从Memcache获取，Memcache为空再查询数据库获取
				Object object = memcached.getAttribute("train_fh_status");
				if (null != object) {
					trainFhStatus = String.valueOf(object);
					logger.info(uuid + "Memcache trainFhStatus:" + trainFhStatus);
				} else {
					try {
						sysImpl.querySysVal("train_fh_status");
					} catch (Exception e) {
						e.printStackTrace();
					}
					trainFhStatus = sysImpl.getSysVal();
					logger.info(uuid + "DB trainFhStatus:" + trainFhStatus);
					// 设置memcached缓存
					memcached.setAttribute("train_fh_status", trainFhStatus, 30 * 1000);
				}

				if ("0".equals(trainFhStatus)) {
					logger.info(uuid + "train_fh 预订分发功能已关闭,不再消费mq订单");
					return;
				}

				// =========== 读取处理订单数的配置
				String orderPoolNumKey = "order_pool_num_" + channel;
				String orderPoolNum = "";
				object = memcached.getAttribute(orderPoolNumKey);
				if (null != object) {
					orderPoolNum = String.valueOf(object);
					logger.info(uuid + "Memcache orderPoolNum:" + orderPoolNum);
				} else {
					try {
						sysImpl.querySysVal(orderPoolNumKey);
					} catch (Exception e) {
						e.printStackTrace();
					}
					orderPoolNum = sysImpl.getSysVal();
					logger.info(uuid + "DB orderPoolNum:" + orderPoolNum);
					memcached.setAttribute(orderPoolNumKey, orderPoolNum, 60 * 1000);
				}

				int poolNum = 0;
				try {
					poolNum = Integer.parseInt(orderPoolNum);
				} catch (Exception e1) {
					poolNum = gonum;
				}
				logger.info(uuid + "poolNum(处理订单数):" + poolNum);

				// =========== 读取打码方式的配置
				object = memcached.getAttribute("rand_code_type");
				String randCodeType = "";
				if (null != object) {
					randCodeType = String.valueOf(object);
					logger.info(uuid + "Memcached randCodeType:" + randCodeType);
				} else {
					try {
						sysImpl.querySysVal("rand_code_type");
					} catch (Exception e) {
						e.printStackTrace();
					}
					randCodeType = sysImpl.getSysVal();
					logger.info(uuid + "DB randCodeType:" + randCodeType);
					memcached.setAttribute("rand_code_type", randCodeType, 60 * 1000);
				}
				WorkIdNum.randCodeType = randCodeType;
				logger.info(uuid + "randCodeType(打码方式):" + randCodeType);

				if (count < poolNum) {
					for (int i = 0; i < getnum; i++) {
						try {
							logger.info(uuid + "开始获取 MQ 消息...");
							TextMessage textMessage = (TextMessage) mqService.receive(queueName);
							if (textMessage != null) {
								String param = textMessage.getText();
								logger.info(uuid + "发送HTTP请求,调用本地Servlet, URL:" + Consts.ORDERURL + ", PARAM:" + param);
								// 这里发送请求。走本地train_fh服务OrderServlet.java
								String tempuuid = uuid.substring(0, uuid.length() - 2);
								tempuuid = tempuuid + "-" + random.nextInt(9) + random.nextInt(9) + random.nextInt(9) + "] ";
								String strParam = "param=" + param + "&channel=" + channel + "&uuid=" + tempuuid;
								HttpPostUtil.sendAndRecive(Consts.ORDERURL, strParam, 200, 1000);
							} else {
								logger.info(uuid + "获取消息为空，不做处理...");
								break;
							}
						} catch (Exception e) {
							logger.info(uuid + "【HttpPostUtil exception】", e);
						}
					}
					logger.info(uuid + "消息获取结束...");
				} else {
					logger.info(uuid + "处理中订单个数已满等待 " + waitsec + " 秒");
					try {
						Thread.sleep(waitsec);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		}, delay, period);
	}

}
