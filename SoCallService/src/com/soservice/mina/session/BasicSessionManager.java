package com.soservice.mina.session;

import java.net.InetSocketAddress;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;

import com.soservice.controller.SoCallController;
import com.soservice.mina.exception.IoSessionException;
import com.soservice.mina.scheduler.PollingScheduler;
import com.soservice.mina.scheduler.Scheduler;

/**
 * 基本的会话管理
 * 
 * @author zhangyou
 * 
 */
public class BasicSessionManager extends AbstractSessionManager {
	/**
	 * 日志
	 */
	private static Logger logger = Logger.getLogger(BasicSessionManager.class);
	/**
	 * IOSession调度器
	 */
	private Scheduler pollingScheduler = null;

	/**
	 * 构造器
	 * 
	 * @param scheduler
	 */
	public BasicSessionManager() {
		pollingScheduler = new PollingScheduler(sessionMap);
	}

	private final ReentrantLock resetLock = new ReentrantLock();

	/**
	 * 销毁连接会话
	 * 
	 * @param cilent
	 * @param ioSession
	 * @return
	 */
	public void destroyIoSession(String cilent) {
		//synchronized(sessionMap){
		sessionMap.remove(cilent);
		//}
		// 重置调度器
		ResetScheduler();
	}

	/**
	 * 取得下个连接会话
	 * 
	 * @return
	 * @throws Exception
	 */
	public IoSession getNextIoSession() throws Exception {
		return getNextIoSession(0);
	}

	/**
	 * 取得下个连接会话
	 * 
	 * @param i
	 *            调用次数
	 * @return
	 * @throws Exception
	 */
	public IoSession getNextIoSession(int i) throws Exception {
		IoSession iosession = (IoSession) this.pollingScheduler.getNextObject();
		// logger.info("本次轮询的session"+iosession);
		// 如果没有获取到 抛出异常
		if (iosession == null || i > 3) {
			throw new IoSessionException("没有找到与app端保持连接的session");
		}
		// 如果连接关闭 寻找下一个
		 if (iosession.isClosing() || !iosession.isConnected()) {
			logger.info(iosession + "关闭状态 销毁连接");
			// InetSocketAddress socketAddress = (InetSocketAddress) iosession
			// .getRemoteAddress();
			// String cilentIp = socketAddress.getAddress().getHostAddress();
			// String host = String.valueOf(socketAddress.getPort());
			// String cilent = cilentIp + ":" + host;
			// 关闭连接
			iosession.close(false);
			// logger.info("sessionClosed:" + iosession);
			// this.destroyIoSession(cilent);
			return getNextIoSession(++i);
		}
		return iosession;
	}

	/**
	 * 写入信息
	 * 
	 * @param messegae
	 * @return
	 * @throws Exception
	 */
	public String writeMesseage(String id, String messeage) throws Exception {
		String result = null;
		IoSession iosession = this.getNextIoSession();
		logger.info("本次调度的iosession " + iosession);
		iosession.write(messeage);
		// 等待通知结果 每隔200毫秒去获取一次 5秒超时
		int i = 0;
		while (i < 25) {
			i++;
			Thread.sleep(200);
			if ((result = this.getResultMap().get(id)) == null) {
				continue;
			} else {
				// 销毁
				this.getResultMap().remove(id);
				break;
			}

		}
		StringBuilder resultBuilder = new StringBuilder();
		if (result == null) {
			resultBuilder.append(id).append("@@").append(1).append("@@")
					.append("app客户端处理超时");
			// 关闭连接
			iosession.close(false);
			result = resultBuilder.toString();
		}
		return result;
	}

	/**
	 * 放入连接会话
	 * 
	 * @param cilent
	 * @param ioSession
	 * @return
	 */
	public void putIoSession(String cilent, IoSession ioSession) {
		//synchronized(sessionMap){
		sessionMap.put(cilent, ioSession);
		//}
		// 重置调度器
		ResetScheduler();
	}

	// 重置调度器
	public void ResetScheduler() {
		resetLock.lock();
		try {
			pollingScheduler.init(sessionMap);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 释放锁
			resetLock.unlock();
		}
	}

}
