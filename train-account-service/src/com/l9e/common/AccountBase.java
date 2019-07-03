package com.l9e.common;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.l9e.transaction.dao.RedisDao;
import com.l9e.transaction.service.AccountService;
import com.l9e.transaction.service.SystemService;
import com.l9e.util.MemcachedUtil;
import com.l9e.util.StrUtil;

public abstract class AccountBase {

	static Logger logger = Logger.getLogger(AccountBase.class.getClass());

	public abstract String channel();// 设置渠道

	public abstract Integer passengerSize();// 订单中的联系人个数

	public abstract Integer sec();// 设置执行时间

	public abstract Integer waitsec();// 设置等待时间

	public abstract Integer accountlownum();// 设置队列存储账号下限数量 少于下限数量开始增加账号

	private Timer timer;

	@Resource
	private AccountService accountService;

	@Resource
	private RedisDao redisDao;

	@Resource
	private SystemService systemService;

	@PostConstruct
	public void init() {
		this.prepareAccount(sec(), channel(), waitsec(), accountlownum(), passengerSize());

	}

	public void prepareAccount(int sec, final String channel, final int waitsec, final Integer accountlownum,
			final Integer passengerSize) {

		if (timer != null) {
			timer.cancel();
			timer = null;
		}

		timer = new Timer();

		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				String uuid = "[" ;
				Random random = new Random();
				for (int i = 0; i < 6; i++) {
					uuid = uuid + random.nextInt(9);
				}
				uuid = uuid + "] ";
				logger.info(uuid + "-------------------------------------");
				
				logger.info(uuid + "channel:" + channel);
				logger.info(uuid + "passengerSize:" + passengerSize);
				logger.info(uuid + "accountlownum:" + accountlownum);
				String accountQueue = StrUtil.getAccountQueue(channel, passengerSize);
				String accountLock = StrUtil.getAccountLock(channel, passengerSize);
				logger.info(uuid + "accountQueue:" + accountQueue);
				logger.info(uuid + "accountLock:" + accountLock);
				
				boolean flag = redisDao.setNXVal(accountLock, "1", 20);
				logger.info(uuid + "设置执行锁结果:" + flag);
				
				if (!flag) {
					logger.info(uuid + "未获得执行锁,不做任何处理。。。");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
					return;
				}
				
				long ttl = redisDao.getTTL(accountLock);
				logger.info(uuid + "执行锁:" + accountLock + " 生命周期:" + ttl + " 秒");

				try {
					MemcachedUtil memcache = MemcachedUtil.getInstance();
					int size = accountlownum;// 初始化

					String poolSizeKey = "acc_pool_size_" + channel;
					logger.info(uuid + "poolSizeKey:" + poolSizeKey);
					/* 读取数据库配置信息，设置账号池大小 (依据订单量和登入session保持时间) */
					Object accountPoolSize = memcache.getAttribute(poolSizeKey);
					if (accountPoolSize == null) {
						accountPoolSize = systemService.getSystemSettingValue(poolSizeKey);
						memcache.setAttribute(poolSizeKey, accountPoolSize, 60 * 1000);
					}
					logger.info(uuid + "accountPoolSize:" + accountPoolSize);

					try {
						size = Integer.parseInt(accountPoolSize.toString());
					} catch (Exception e) {
						logger.info(uuid + "读取账号池大小配置异常", e);
						size = accountlownum;
					}
					logger.info(uuid + "size:" + size);
					
					try {
						long lLen = redisDao.LLEN(accountQueue);
						int lenInt = Long.valueOf(lLen).intValue();
						logger.info(uuid + "队列:" + accountQueue + " 长度:" + lLen);
						if (lenInt < size) {
							int limit = size - lenInt;
							logger.info(uuid + "填充账号...");
							accountService.setAccount(channel, passengerSize, uuid, limit);
						}
						Thread.sleep(waitsec);
					} catch (Exception e) {
						logger.info(uuid + "setAccount异常", e);
					}
					logger.info(uuid + "账号池存入的个数已满等待时间：" + waitsec);
				} catch (Exception e) {
					logger.info(uuid + "账号填充异常", e);
				} finally {
					redisDao.delVal(accountLock);
				}
			}

		}, 0, sec);
	}

}
