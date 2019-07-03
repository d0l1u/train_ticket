package com.soservice.mina.handler;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.soservice.controller.SoCallController;
import com.soservice.mina.factory.SessionManagerFactory;
import com.soservice.mina.session.SessionManager;
import com.soservice.mina.task.AsynchronousHandleResultTask;

/**
 * so服务调用处理
 * 
 * @author zhangyou
 * 
 */
public class SoCallServerHandler extends IoHandlerAdapter {
	/**
	 * 日志
	 */
	private static final Logger logger = Logger
			.getLogger(SoCallController.class);
	/**
	 * 线程池
	 */
	private static final ExecutorService ex = Executors.newCachedThreadPool();
	/**
	 * 连接会话管理
	 */
	private static final SessionManager sessionManager = SessionManagerFactory
			.getSessionManager();

	/**
	 * 接受app端信息触发的方法
	 */
	public void messageReceived(IoSession session, Object message)
			throws Exception {

		if (message.toString().equals("")) {
		}
		// 检测成功
		else if (message.toString().equals("OK")) {

		}
		// 如果是心跳检测包
		else if (message.toString().equals("heartBeat")) {
			// 连接正常
			session.write("heartBeat-ok");
		}
		// 如果收到信息
		else {
			logger.info("收到来自" + session + "app客户端的返回" + message);
			// 交给线程异步处理
			AsynchronousHandleResultTask asynTask = new AsynchronousHandleResultTask(
					sessionManager, message.toString());
			ex.submit(asynTask);
		}
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {

	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		super.sessionClosed(session);
		logger.info("sessionClosed:" + session);
		InetSocketAddress socketAddress = (InetSocketAddress) session
				.getRemoteAddress();
		String cilentIp = socketAddress.getAddress().getHostAddress();
		String host = String.valueOf(socketAddress.getPort());
		String cilent = cilentIp + ":" + host;
		// 销毁连接会话
		sessionManager.destroyIoSession(cilent);

	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		super.sessionCreated(session);
		logger.info("sessionCreated:" + session);
		// 连接被创建设置空闲时间 每一分钟进行一次心跳检测
		//session.getConfig().setIdleTime(IdleStatus.BOTH_IDLE, 6 * 10000);
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		super.sessionIdle(session, status);
		// logger.info("sessionIdle:" + session);
		// // 执行心跳检测
		// if (status == IdleStatus.BOTH_IDLE) {
		// session.write("heartBeat");
		// }
	}

	/**
	 * 长连接创建
	 */
	public void sessionOpened(IoSession session) throws Exception {
		super.sessionOpened(session);
		InetSocketAddress socketAddress = (InetSocketAddress) session
				.getRemoteAddress();
		String cilentIp = socketAddress.getAddress().getHostAddress();
		String host = String.valueOf(socketAddress.getPort());
		String cilent = cilentIp + ":" + host;
		logger.info("客户端" + cilent + " 请求打开连接");
		// 把session放入共享内存中
		sessionManager.putIoSession(cilent, session);
		logger.info("sessionOpened:" + session);
		session.write("Hello");
	}

}
