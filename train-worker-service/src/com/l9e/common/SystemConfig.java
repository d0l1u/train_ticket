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
import java.net.URLDecoder;

import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
/**
 * 系统启动项
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
		plog.info("==================== 加载系统配置文件 =====================");
		try {
			loadXml();
		} catch (Exception e) {
			plog.error("PageServlet False : ", e);
			plog.error("======================= 系统配置文件加载异常 ====================");
		}
	}

	/**
	 * 加载页面展示系统用配置文件
	 * @throws DocumentException 
	 */
	public void loadXml() throws DocumentException {

		try {
//			String pageConfig = SystemConfig.class.getClassLoader().getResource("pageconfig.xml").getPath();
//			pageConfig = URLDecoder.decode(pageConfig, "UTF-8");
//			File configFile = new File(pageConfig);
//
//			boolean initResult = initParams(configFile);
//			if (!initResult) {
//				plog.error("加载配置文件[pageconfig.xml]时失败.");
//				System.exit(0);
//			}
//			plog.info("加载页面展示系统配置文件[pageconfig.xml]成功");
		} catch (Exception e) {
			plog.error("由于["+e.toString()+"],加载配置文件[pageconfig.xml]时失败.");
			System.exit(0);
		}

	}

	/**
	 * 逻辑系统配置信息
	 * Description：初始化数据
	 * @param configFile
	 * @return
	 */
	public static boolean initParams(File configFile) {
		plog.info(configFile);
		SAXReader reader = new SAXReader();
		Document document;
		try {
				document = reader.read(configFile);
				Element phone = (Element) document.selectObject("sysInfo/phone");
				
				if (phone == null) {
					plog.error("读取pageconfig.xml文件发生错误，无指定的相关节点参数[sysInfo/phone]");
					return false;
				} else {
					plog.info("加载手机平台相关配置信息开始");
					try {
						
					} catch (Exception e) {
						plog.error("加载手机平台相关配置信息时 发生异常"+e);
						return false;
					}
				}
				
			} catch (DocumentException e) {
				plog.error("解析pageconfig.xml文件发生错误"+e);
				return false;
			}
		return true;
	}
	
}
