package com.soservice.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.soservice.mina.LongConnectionService;

/**
 * 监听服务 在服务启动时 启动mina长连接服务
 * @author zhangyou
 *
 */
public class MinaServerListenter implements ServletContextListener{
    private  static final Logger  logger=Logger.getLogger(MinaServerListenter.class);
	@Override
	public void contextDestroyed(ServletContextEvent servletcontextevent) {
		// TODO Auto-generated method stub
		
	}

    /**
     * 上下文初始化
     */
	public void contextInitialized(ServletContextEvent servletcontextevent) {
		logger.info("启动mina长连接服务");
		new LongConnectionService().init();
	}
	

}
