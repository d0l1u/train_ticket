package com.l9e.transaction.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.SystemDao;
import com.l9e.transaction.dao.WorkerDao;
import com.l9e.transaction.service.WorkerService;
import com.l9e.transaction.vo.Worker;
import com.l9e.util.ConfigUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.MemcachedUtil;
import com.l9e.util.StrUtil;
import com.train.commons.jedis.SingleJedisClient;

import net.sf.json.JSONObject;

@Service("workerService")
public class WorkerServiceImpl implements WorkerService {

	private static final Logger logger = Logger.getLogger(WorkerServiceImpl.class);

	@Resource
	private WorkerDao workerDao;

	@Resource
	private SingleJedisClient jedisClient;

	@Resource
	private SystemDao systemDao;

	@Override
	public void setWorker(Integer type, int limit, String logId) {
		if (type == Worker.TYPE_BOOK) {
			setBookWorker();
		} else if (type == Worker.TYPE_PAY) {
			setPayWorker(limit, logId);
		} else if (type == Worker.TYPE_VERIFY) {
			setVerifyWorker();
		} else if (type == Worker.TYPE_REGIST) {
			setRegistWorker();
		}

	}

	@Override
	public Worker getWorker(Integer type, String logid) {
		String key = StrUtil.getWorkerQueue(type);
		Worker worker = null;
		try {
			String rpop = jedisClient.rpop(key);
			logger.info(logid + "Redis Rpop Key:" + key + " Result:" + rpop);
			if (StringUtils.isNotBlank(rpop)) {
				Integer workerId = Integer.valueOf(rpop);
				worker = queryWorkerById(workerId);
				logger.info(logid + "DB Query Bean:" + worker);
			}
		} catch (Exception e) {
			logger.info(logid + "【getWorker() Exception】", e);
		}
		return worker;
	}

	@Override
	public void updateWorker(Worker worker) {
		workerDao.updateWorker(worker);
	}

	@Override
	public Worker queryWorkerById(Integer workerId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("workerId", workerId);
		return workerDao.selectOneWorker(params);
	}

	@Override
	public Worker startPreparedWorker(Integer type) {

		Worker preparedWorker = null;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("workerType", type);
		params.put("workerStatus", Worker.STATUS_PREPARED);
		params.put("limit", 1);

		preparedWorker = workerDao.selectOneWorker(params);
		if (preparedWorker != null) {
			preparedWorker.setWorkerStatus(Worker.STATUS_FREE);
			preparedWorker.setSingleOrder(0);

			int rows = workerDao.updateWorker(preparedWorker);
			if (rows == 0)
				preparedWorker = null;
		}
		return preparedWorker;
	}

	@Override
	public Worker stopWorker(Integer workerId, String stopReason) {

		Worker stopWorker = null;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("workerId", workerId);

		stopWorker = workerDao.selectOneWorker(params);
		if (stopWorker != null) {
			stopWorker.setWorkerStatus(Worker.STATUS_STOP);
			stopWorker.setStopReason(stopReason);
			stopWorker.setSingleOrder(0);

			logger.info("停用机器人 worker : " + stopWorker);
			int rows = workerDao.updateWorker(stopWorker);
			if (rows == 0) {
				stopWorker = null;
			} else {
				String key = StrUtil.getWorkerQueue(stopWorker.getWorkerType());
				jedisClient.lrem(key, 0, String.valueOf(workerId));
			}

		}

		return stopWorker;
	}

	/**
	 * 补充预订机器人
	 */
	private void setBookWorker() {

		/* 读取系统设置单线程开关 */
		String settingKey = "robot_book_single_order";
		Object singleOrderValue = MemcachedUtil.getInstance().getAttribute(settingKey);
		if (singleOrderValue == null) {
			singleOrderValue = systemDao.selectSettingValue(settingKey);
			MemcachedUtil.getInstance().setAttribute(settingKey, singleOrderValue, 60 * 1000);
		}

		/**
		 * 系统设置机器是否锁定开关 1：机器锁定开启 0：机器锁定关闭
		 */
		String workerLockSwitch = systemDao.selectSettingValue("worker_lock_switch");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("workerType", Worker.TYPE_BOOK);
		params.put("workerStatus", Worker.STATUS_FREE);
		params.put("limit", 30);
		if (new Integer(1).toString().equals(singleOrderValue)) {
			params.put("singleOrder", 0);
		}
		/**
		 * 如果workerLockSwitch为1，则表示机器入队列时需要锁定，防止其它订单也获取到这个机器 查找时只查找没有锁定的机器
		 * worker_lock 当前机器是否锁定 0、否 1、是
		 */
		if ("1".equals(workerLockSwitch)) {
			params.put("workerLock", Integer.valueOf(0));
		}
		logger.info("设置预定机器人 worker时的参数为 : " + params);
		List<Worker> workers = workerDao.selectWorkers(params);

		lpush(Worker.TYPE_BOOK, workers, "");
	}

	/**
	 * 补充支付机器人
	 * 
	 * @param limit
	 * 
	 * @param logId
	 */
	private void setPayWorker(int limit, String logid) {
		logger.info(logid + "补充支付机器人队列...");
		List<Worker> workers = workerDao.selectQueuePayWorkers(limit);
		lpush(Worker.TYPE_PAY, workers, logid);
	}

	/**
	 * 补充核验机器人
	 */
	private void setVerifyWorker() {
		List<Worker> workers = workerDao.selectQueueVerifyWorkers();
		lpush(Worker.TYPE_VERIFY, workers, "");
	}

	/**
	 * 补充注册机器人
	 */
	private void setRegistWorker() {
		List<Worker> workers = workerDao.selectQueueRegistWorkers();
		lpush(Worker.TYPE_REGIST, workers, "");
	}

	/**
	 * 机器人主键进入队列
	 * 
	 * @param type
	 * @param workers
	 * @param logid
	 */
	private void lpush(Integer type, List<Worker> workers, String logid) {
		try {

			if (workers == null || workers.size() == 0) {
				logger.info(logid + "Worker List为空，不做处理...");
				return;
			}

			logger.info(logid + "机器类型为 :" + type);

			/**
			 * 系统设置机器是否锁定开关 1：机器锁定开启 0：机器锁定关闭
			 */
			String workerLockSwitch = systemDao.selectSettingValue("worker_lock_switch");
			logger.info(logid + "锁定开关workerLockSwitch为 :" + workerLockSwitch);

			for (Worker worker : workers) {
				worker.setOptionTime("optionTime");
				/**
				 * 如果workerLockSwitch为1，则表示机器入队列时需要锁定，防止其它订单也获取到这个机器 worker_lock
				 * 要改成1，锁定 worker_lock：当前机器是否锁定 0、否 1、是 只锁定预定机器人
				 */
				if (type == Worker.TYPE_BOOK) {
					// 预订
					if ("1".equals(workerLockSwitch)) {
						worker.setWorkerLock(1);
					}
				}

				/**
				 * 判断是否是美团云机器，如果是，则判断机器使用次数是否达上限
				 * 如果次数达上限，则切换美团云IP，更新CP_workerinfo表中机器的IP地址和机器名字，并重置机器使用次数，
				 * 并且不能放入队列； 如果没有达到上限，则机器使用次数+1，进入队列
				 * 
				 */
				int workerMtyunIdentify = worker.getWorkerMtyunIdentify();// 是否美团云机器标识0：否，1：是
				logger.info(logid + "美团云机器的标识为 :" + workerMtyunIdentify + "[0:否, 1:是]");

				if (workerMtyunIdentify == 1) {
					// 后台配置的美团云切换IP开关 0：切IP机制关闭 1：切IP机制开启
					String workerChangeIpSwitch = systemDao.selectSettingValue("worker_changeIP_switch");
					logger.info(logid + "美团云机器切换IP开关为 :" + workerChangeIpSwitch);

					/**
					 * 如果美团云切换IP开关开启，则走是否切IP流程 如果没有开启，则机器人正常进入队列
					 */
					if ("1".equals(workerChangeIpSwitch)) {
						Integer workerMtyunUsenum = worker.getWorkerMtyunUsenum();// 美团云机器使用次数
						logger.info("机器人开始进入队列，当前美团云机器使用的次数为 : " + workerMtyunUsenum);

						// 后台配置的美团云机器使用最大次数
						String workerMtyunUseMaxNum = systemDao.selectSettingValue("worker_mtyun_useMaxNum");
						logger.info("机器人开始进入队列，后台配置的美团云机器使用最大次数为 : " + workerMtyunUseMaxNum);

						if (workerMtyunUsenum.intValue() > (Integer.valueOf(workerMtyunUseMaxNum).intValue())) {
							logger.info("开始切换美团云机器IP");
							// 当前美团云机器使用次数已达上限，切换IP
							String workerExt = worker.getWorkerExt();// 例子：http://115.28.138.199:8091/RunScript
							logger.info("美团云机器人不进入队列，当前美团云机器旧url为 : " + workerExt);

							String[] workerExtArr = workerExt.split("//");
							logger.info("美团云机器人不进入队列， url不带http为: " + workerExtArr[1]);
							String[] oldIpArr = workerExtArr[1].split("\\:");
							String oldIP = oldIpArr[0];
							logger.info("美团云机器人不进入队列， 当前美团云机器旧IP为: " + oldIP);

							// 进行美团云切换IP操作
							Date currentDate = new Date();
							String formatStr = "yyyyMMddHHmmss";
							String currentDateStr = DateUtil.dateToString(currentDate, formatStr);
							String ipName = "bookIP" + currentDateStr;// 浮动IP的自定义名称唯一

							String newIP = changeMtyunIp(ipName, oldIP);// 美团云切换IP后返回的新IP
							logger.info("[当前美团云机器切换IP后新IP]为: " + newIP + " 旧IP为：" + oldIP);
							logger.info("美团云机器切换IP结束");

							if (newIP != null && !("".equals(newIP))) {
								// 往cp_ipinfo_log表插入一条日志记录
								String msg = "trainWorkerService~当前美团云机器下单次数已达上限,新购美团云IP,执行新旧IP切换操作,并更新机器名字和url.";
								Map<String, Object> insertMap = new HashMap<String, Object>();
								insertMap.put("old_ip", oldIP);
								insertMap.put("new_ip", newIP);
								insertMap.put("content", msg);
								insertMap.put("create_time", "now()");
								workerDao.insertIpInfoLog(insertMap);
								logger.info("机器服务,当前美团云机器下单次数达上限时,新购IP,执行新旧IP切换操作，并往日志表中插入一条记录！");

								// 更新当前机器url,机器名字和机器使用次数
								logger.info("当前美团云机器的旧名字为：" + worker.getWorkerName());
								String[] newIpArr = newIP.split("\\.");
								String workerNameIp = newIpArr[2] + "." + newIpArr[3];
								String newWorkerName = "机器人" + workerNameIp;
								logger.info("当前美团云机器的新名字为：" + newWorkerName);
								String newWorkerExt = workerExt.replace(oldIP, newIP);
								logger.info("当前美团云机器新url为 : " + newWorkerExt);

								// 为机器人名字和url重新赋值
								worker.setWorkerExt(newWorkerExt);
								worker.setWorkerName(newWorkerName);
								worker.setWorkerMtyunUsenum(Integer.valueOf(0));// 切换IP后，机器使用次数重置为0
								int num = workerDao.updateWorker(worker);
								logger.info("美团云机器使用次数达上限，切换IP，不进入队列，更新语句执行次数为 : " + num);
							}
							continue;
						} else {
							// 美团云机器没有达到使用上限的情况下，使用次数+1，进入队列
							workerMtyunUsenum++;// 不切换IP的话，机器使用次数+1
							worker.setWorkerMtyunUsenum(workerMtyunUsenum);
							int num = workerDao.updateWorker(worker);
							logger.info("美团云机器使用次数还没达到上限，开始进入队列，更新语句执行次数为 : " + num);
							jedisClient.lpush(StrUtil.getWorkerQueue(type), String.valueOf(worker.getWorkerId()));
							continue;
						}
					}
				}

				logger.info(logid + "更新机器人信息...");
				int num = workerDao.updateWorker(worker);
				logger.info(logid + "机器人更新结果:" + num);

				String key = StrUtil.getWorkerQueue(type);
				Integer workerId = worker.getWorkerId();
				String workerName = worker.getWorkerName();

				logger.info(logid + "补充队列:" + key + ", workerId:" + workerId + ", workerName:" + workerName);
				Long lpush = jedisClient.lpush(key, String.valueOf(workerId));
				logger.info(logid + "队列长度:" + lpush);
			}

		} catch (Exception e) {
			logger.info(logid + "【lpush() Exception】", e);
		}
	}

	/**
	 * 随机查询一个java脚本机器人
	 * 
	 * @return Worker
	 */
	@Override
	public Worker queryOneJavaWorker() {
		// TODO Auto-generated method stub
		return workerDao.selectOneJavaWorker();
	}

	/**
	 * 切换美团云IP，把旧IP换成新购IP，并把旧IP存入待释放IP表中
	 * 
	 * @param ip_name
	 *            浮动IP的自定义名称 唯一(以字母开头，仅包含字母、数字或中划线的3-40个字符)
	 * @param oldIP
	 *            旧IP
	 * @return 新购IP
	 */
	@Override
	public String changeMtyunIp(String ipName, String oldIP) {
		// TODO Auto-generated method stub
		String newIP = "";// 新IP
		try {
			// 新购IP
			String buyNewIpResult = HttpUtil.sendByPost(ConfigUtil.getProperty("mtyunAllocateAddress"), "commond=" + "AllocateAddress" + "&ip_name=" + ipName,
					"utf-8");
			logger.info("预定机器人服务[新购浮动IP的JSON串为]" + buyNewIpResult);
			logger.info("预定机器人服务[旧IP地址为]" + oldIP);
			if (!StringUtils.isEmpty(buyNewIpResult)) {
				JSONObject buyNewIpObject = JSONObject.fromObject(buyNewIpResult);
				newIP = buyNewIpObject.getString("publicIp");
				logger.info("预定机器人服务[新购浮动IP地址为]" + newIP);
			}

			// 切换IP
			if (newIP != null && !"".equals(newIP)) {
				String changeIpResult = HttpUtil.sendByPost(ConfigUtil.getProperty("mtyunReplaceAddress"),
						"commond=" + "ReplaceAddress" + "&allocationId=" + oldIP + "&newAllocationId=" + newIP, "utf-8");
				logger.info("预定机器人服务[切换云产品上绑定的浮动IP]返回结果为: " + changeIpResult);
			}

			// 释放旧IP 中间间隔太短会报异常，另写一个定时任务来释放IP
			// String releaseIpResult = HttpUtil.sendByPost(ConfigUtil
			// .getProperty("mtyunReleaseAddress"), "commond="
			// + "ReleaseAddress" + "&allocationId=" + oldIP, "utf-8");
			// logger.info("[释放浮动IP]返回结果为: " + releaseIpResult);

			// 往cp_ipinfo_release表插入一条待释放IP(旧IP)的记录，用定时job定时释放该旧IP
			Map<String, Object> insertMap = new HashMap<String, Object>();
			insertMap.put("ip_ext", oldIP);
			insertMap.put("release_status", "0"); // 0：待释放 1：释放成功 2：释放失败 3：释放中
			insertMap.put("create_time", "now()");
			workerDao.insertIpInfoRelease(insertMap);
			logger.info("预定机器人服务，往cp_ipinfo_release表插入一条待释放IP的记录！");

		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("预定机器人服务，美团云切换后的新IP为: " + newIP);
		return newIP;
	}

	public static void main(String[] args) {

		String oldIP = "43.241.226.137";
		Date currentDate = new Date();
		String formatStr = "yyyyMMddHHmmss";
		String currentDateStr = DateUtil.dateToString(currentDate, formatStr);
		String ipName = "bookIP" + currentDateStr;
		String newIP = "";// 新IP
		logger.info("美团云服务地址: " + ConfigUtil.getProperty("mtyunAllocateAddress"));
		try {
			// 新购IP
			String buyNewIpResult = HttpUtil.sendByPost(ConfigUtil.getProperty("mtyunAllocateAddress"), "commond=" + "AllocateAddress" + "&ip_name=" + ipName,
					"utf-8");
			logger.info("预定机器人服务[新购浮动IP的JSON串为]" + buyNewIpResult);
			logger.info("预定机器人服务[旧IP地址为]" + oldIP);
			if (!StringUtils.isEmpty(buyNewIpResult)) {
				JSONObject buyNewIpObject = JSONObject.fromObject(buyNewIpResult);
				newIP = buyNewIpObject.getString("publicIp");
				logger.info("预定机器人服务[新购浮动IP地址为]" + newIP);
			}

			// 切换IP
			if (newIP != null && !"".equals(newIP)) {
				String changeIpResult = HttpUtil.sendByPost(ConfigUtil.getProperty("mtyunReplaceAddress"),
						"commond=" + "ReplaceAddress" + "&allocationId=" + oldIP + "&newAllocationId=" + newIP, "utf-8");
				logger.info("预定机器人服务[切换云产品上绑定的浮动IP]返回结果为: " + changeIpResult);
			}

			// 释放旧IP 中间间隔太短会报异常，另写一个定时任务来释放IP
			// String releaseIpResult = HttpUtil.sendByPost(ConfigUtil
			// .getProperty("mtyunReleaseAddress"), "commond="
			// + "ReleaseAddress" + "&allocationId=" + oldIP, "utf-8");
			// logger.info("[释放浮动IP]返回结果为: " + releaseIpResult);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
