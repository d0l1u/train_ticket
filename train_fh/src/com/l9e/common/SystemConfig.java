/*
 * Copyright (C) 2009-2012 19e Inc.All Rights Reserved.		
 * 																	
 * FileName：SystemConfig.java						
 *			
 * Description：	读取配置文件信息，并做交验				
 * 																	
 * History：
 *  版本号 作者 日期       简要介绍相关操作
 *  1.0   夏茂健 2012-09-15  Create		
 */
package com.l9e.common;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import com.unlun.commons.res.Config;

/**
 * 系统启动项
 * 
 * @author liuyi02
 * 
 */
public class SystemConfig extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 生成日志对象
	 */
	private static final Logger plog = Logger.getLogger(SystemConfig.class);

	private static SystemConfig inst;

	public static SystemConfig getInstance() {
		if (inst == null) {
			inst = new SystemConfig();
		}
		return inst;
	}

	@Override
	public void init() {
		plog.info("================ 加载系统配置文件==================");
		try {
			/*
			 * System.setProperty("http.proxyHost", "192.168.65.126");
			 * System.setProperty("http.proxyPort", "3128");
			 */
			String databasePath = SystemConfig.class.getClassLoader()
					.getResource("database.properties").getPath();
			Config.setConfigResource(databasePath);

			FileInputStream pInStream = new FileInputStream(new File(
					SystemConfig.class.getClassLoader().getResource(
							"config.properties").getPath()));
			Properties properties = new Properties();
			// 加载配置文件
			properties.load(pInStream);
			Consts.ORDERURL = properties.getProperty("orderUrl");
			Consts.GETACCURL = properties.getProperty("getAccUrl");
			Consts.READYPAYURL = properties.getProperty("readyToPayUrl");
			
			/*机器人服务*/
			Consts.GET_WORKER_BY_ID = properties.getProperty("getWorkerById");
			Consts.GET_WORKER_BY_TYPE = properties.getProperty("getWorkerByType");
			Consts.RELEASE_WORKER = properties.getProperty("releaseWorker");
			Consts.STOP_WORKER = properties.getProperty("stopWorker");
			Consts.GET_JAVA_WORKER = properties.getProperty("getOneJavaWorker");
			
			/*账号服务*/
			Consts.GET_CHANNEL_ACCOUNT = properties.getProperty("getChannelAccount");
			Consts.GET_ORDER_ACCOUNT = properties.getProperty("getOrderAccount");
			Consts.STOP_ACCOUNT = properties.getProperty("stopAccount");
			Consts.UPDATE_ACCOUNT = properties.getProperty("updateAccount");
			Consts.FILTER_ACCOUNT = properties.getProperty("filterAccount");
			Consts.RELEASE_ACCOUNT = properties.getProperty("releaseAccount");
			//12306登录账号验证
			//Consts.VALIDATE_ACCOUNT = properties.getProperty("identityURL");
			//自带12306账号错误信息处理
			Consts.HANDLE_BIND_ACCOUNT = properties.getProperty("handleBindAccErrorCode");
			
			/*订单服务*/
			Consts.GET_ORDER_PASSENGER = properties.getProperty("getOrderPassenger");
			Consts.UPDATE_ORDER = properties.getProperty("updateOrder");
			
			/*IP服务*/
			Consts.GET_IP_BY_ID = properties.getProperty("getIpById");
			Consts.GET_IP_BY_TYPE = properties.getProperty("getIpByType");
			
		} catch (Exception e) {
			e.printStackTrace();
			plog.error("================= 系统配置文件加载异常=================" + e);
		}
	}

}
