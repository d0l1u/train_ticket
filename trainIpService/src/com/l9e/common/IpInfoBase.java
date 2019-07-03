package com.l9e.common;

import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.l9e.transaction.dao.RedisDao;
import com.l9e.transaction.service.IpInfoService;
import com.l9e.util.StrUtil;


public abstract class IpInfoBase {

	static Logger logger = Logger.getLogger(IpInfoBase.class.getClass());
	/**
	 * IP类型
	 * @return
	 */
	public abstract Integer type();
	/**
	 * 执行时间
	 * @return
	 */
	public abstract Integer sec();
	/**
	 * 等待时间
	 * @return
	 */
	public abstract Integer waitsec();
	/**
	 * ip存储最小数量
	 * @return
	 */
	public abstract Integer iplownum();
	
	private Timer timer;

	@Resource
	private IpInfoService ipInfoService;
	
	@Resource
	private RedisDao redisDao;
	
//	@Resource
//	private SystemService systemService;

	@PostConstruct
	public void init() {
		this.prepareIpInfo(type(), sec(), waitsec(), iplownum());
	}

	/**
	 * 准备IP
	 * @param sec
	 * @param waitsec
	 * @param iplownum
	 */
	public void prepareIpInfo(final Integer type, int sec, final int waitsec, final Integer iplownum) {

		logger.info("启动代理IP准备程序... ...");
		
		if(timer != null) {
			timer.cancel();
			timer = null;
		}
		
		timer = new Timer();
		
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
//				
//				/*读取系统设置机器人池最小值*/
//				String poolSizeKey = "worker_pool_size_" + type;
//				Object workerPoolSize = MemcachedUtil.getInstance().getAttribute(poolSizeKey);
//				if(workerPoolSize == null){
//					workerPoolSize = systemService.getSystemSettingValue(poolSizeKey);
//					MemcachedUtil.getInstance().setAttribute(poolSizeKey, workerPoolSize, 60 * 1000);
//				}
					
//				try {
//					size = Integer.parseInt((String)workerPoolSize);
//				} catch (Exception e) {
//					logger.info("读取机器人存储池大小值异常，e:" + e.getMessage());
//					size = workerlownum;
//				}
//				Integer count = (int) redisService.getINDEVal(StrUtil.getWorkerQueue(type));
//				logger.info(type+"类型记录机器人数据：【"+count+"】 ,pool size " + count);
//				if (count <= size) {
//					logger.info("开始准备机器人，类型：" + type);
//				} else {
//				}
				try {
					int size = iplownum;
					long lLen = redisDao.LLEN(StrUtil.getIpInfoQueue(type));
					logger.info(type + " IP类型，设置队列长度 : " + size + ",实际队列长度 : " + lLen);
					
					if(lLen < size) {
						ipInfoService.setIpInfo(type);
					}
					Thread.sleep(waitsec);
				} catch (Exception e) {
					e.printStackTrace();
				}
				logger.info(type + " 类型IP池存入的代理IP个数已满等待时间："+waitsec);
			}
		}, 0, sec);
	}

}
