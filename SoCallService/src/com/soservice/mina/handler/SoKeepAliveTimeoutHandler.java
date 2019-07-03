package com.soservice.mina.handler;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler;

import com.soservice.listener.MinaServerListenter;

/**
 * 连接超时处理
 * 
 * @author zhangyou
 * 
 */
public class SoKeepAliveTimeoutHandler implements
		KeepAliveRequestTimeoutHandler {
	private Logger logger = Logger.getLogger(SoKeepAliveTimeoutHandler.class);

	/**
	 * 连接超时处理
	 */
	public void keepAliveRequestTimedOut(KeepAliveFilter keepalivefilter,
			IoSession iosession) throws Exception {
		logger.info(iosession + "心跳检测超时 请求关闭");
		iosession.close(false);
	}

}
