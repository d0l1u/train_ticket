package com.soservice.mina;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler;
import org.apache.mina.filter.logging.MdcInjectionFilter;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.soservice.listener.MinaServerListenter;
import com.soservice.mina.factory.MessageCodecFactory;
import com.soservice.mina.factory.SessionManagerFactory;
import com.soservice.mina.factory.SoServiceKeepAliveFactory;
import com.soservice.mina.handler.SoCallServerHandler;
import com.soservice.mina.handler.SoKeepAliveTimeoutHandler;
import com.soservice.mina.task.SessionMonitorTask;
import com.soservice.util.ConfigUtil;

/**
 * 长连接服务
 * 
 * @author zhangyou
 * 
 */
public class LongConnectionService {
	private Logger logger = Logger.getLogger(MinaServerListenter.class);

	/**
	 * 初始化长连接服务
	 */
	public void init() {
		SocketAcceptor acceptor = new NioSocketAcceptor();
		acceptor.getSessionConfig().setUseReadOperation(true);
		//空闲时间 5秒
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE,  
	                5);  
		// 添加多线程过滤器
		MdcInjectionFilter mdcFilter = new MdcInjectionFilter();
		acceptor.getFilterChain().addLast("threadPool", mdcFilter);
		// 编解码
		acceptor.getFilterChain().addLast("codec",
				new ProtocolCodecFilter(new MessageCodecFactory()));
		// so服务调用处理
		acceptor.setHandler(new SoCallServerHandler());
		//缓冲区大小
		acceptor.getSessionConfig().setReadBufferSize(4096);
		acceptor.getSessionConfig().setSendBufferSize(4096);
		//这个方法设置写操作的超时时间  
		acceptor.getSessionConfig().setWriteTimeout(2000);
		//心跳协议
		KeepAliveMessageFactory heartBeatFactory=new SoServiceKeepAliveFactory();
		KeepAliveRequestTimeoutHandler heartBeatTimeoutHandler=new SoKeepAliveTimeoutHandler();
		//心跳检测过滤器
		KeepAliveFilter heartBeatFilter=new KeepAliveFilter(heartBeatFactory,IdleStatus.BOTH_IDLE,heartBeatTimeoutHandler);
		 //设置是否forward到下一个filter  
		heartBeatFilter.setForwardEvent(true);  
        //设置心跳频率  
		heartBeatFilter.setRequestInterval(Integer.parseInt(ConfigUtil
				.getProperty("heartBeatRate"))); 
		//心跳检测超时
		heartBeatFilter.setRequestTimeout(Integer.parseInt(ConfigUtil
				.getProperty("heartBeatTimeOut")));
        acceptor.getFilterChain().addLast("heartbeat", heartBeatFilter); 
		int bindPoit = Integer.parseInt(ConfigUtil
				.getProperty("serverMonitorPort"));
		try {
			logger.info("绑定端口  " + bindPoit);
			// 绑定端口
			acceptor.bind(new InetSocketAddress(bindPoit));
			logger.info("绑定端口  " + bindPoit+"成功");
			// 启动监听
			new SessionMonitorTask(SessionManagerFactory.getSessionManager())
					.start();
		} catch (IOException e) {
			logger.info("绑定端口  " + bindPoit+"失败 原因"+e.getMessage());
			e.printStackTrace();
		}
	}

}
