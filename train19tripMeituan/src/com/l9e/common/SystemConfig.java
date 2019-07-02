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
		plog.info("==================== 加载系统配置文件 =====================");
		try {
			/*
			 * System.setProperty("http.proxyHost", "192.168.65.126");
			 * System.setProperty("http.proxyPort", "3128");
			 */

			loadXml();
		} catch (Exception e) {
			plog.error("PageServlet False : ", e);
			plog
					.error("======================= 系统配置文件加载异常 ====================");
		}
	}

	/**
	 * 加载页面展示系统用配置文件
	 * 
	 * @throws DocumentException
	 */
	public void loadXml() throws DocumentException {

		try {
			String pageConfig = SystemConfig.class.getClassLoader()
					.getResource("pageconfig.xml").getPath();

			pageConfig = URLDecoder.decode(pageConfig, "UTF-8");
			File configFile = new File(pageConfig);
			plog.info("----------------------------------" + pageConfig);
			boolean initResult = initParams(configFile);
			if (!initResult) {
				plog.error("加载配置文件[pageconfig.xml]时失败.");
				System.exit(0);
			}
			plog.info("加载页面展示系统配置文件[pageconfig.xml]成功");
		} catch (Exception e) {
			plog.error("由于[" + e.toString() + "],加载配置文件[pageconfig.xml]时失败.");
			System.exit(0);
		}

	}

	/**
	 * 逻辑系统配置信息 Description：初始化数据
	 * 
	 * @param configFile
	 * @return
	 */
	public static boolean initParams(File configFile) {
		plog.info(configFile);
		SAXReader reader = new SAXReader();
		Document document;
		try {
			document = reader.read(configFile);
			Element mt = (Element) document.selectObject("sysInfo/mt");

			if (mt == null) {
				plog.error("读取pageconfig.xml文件发生错误，无指定的相关节点参数[sysInfo/mt]");
				return false;
			} else {
				plog.info("加载[meituan]相关配置信息开始");
				try {
					Element mtKey = (Element) mt.selectObject("mt_key");
					Element mtUrl = (Element) mt.selectObject("mt_url");
					Element resignTicketNotifyUrl = (Element) mt.selectObject("mt_resign_ticket_notify_url");
					Element cancelResignNotifyUrl = (Element) mt.selectObject("mt_cancel_ticket_notify_url");
					Element confirmResignNotifyUrl = (Element) mt.selectObject("mt_confirm_ticket_notify_url");

					Consts.MT_KEY = mtKey.getTextTrim();
					Consts.MT_URL = mtUrl.getTextTrim();
					Consts.RESIGN_TICKET_NOTIFY_URL=resignTicketNotifyUrl.getTextTrim();
					Consts.CANCEL_TICKET_NOTIFY_URL=cancelResignNotifyUrl.getTextTrim();
					Consts.CONFIRM_TICKET_NOTIFY_URL=confirmResignNotifyUrl.getTextTrim();
					plog.info("美团申请改签回调地址:"+Consts.RESIGN_TICKET_NOTIFY_URL);
					plog.info("美团取消改签回调地址:"+Consts.CANCEL_TICKET_NOTIFY_URL);
					plog.info("美团确认改签回调地址:"+Consts.CONFIRM_TICKET_NOTIFY_URL);
				} catch (Exception e) {
					plog.error("加载[meituan]相关配置信息时 发生异常" + e);
					return false;
				}
			}

			plog.info("加载系统其它相关配置信息开始");
			try {
				Element notifyCpBackUrl = (Element) document
						.selectObject("sysInfo/notify_cp_back_url");
				Element notifyCpInterfaceUrl = (Element) document
						.selectObject("sysInfo/notify_cp_interface_url");
				Element notifyCpAllchannelBackUrl = (Element) document
						.selectObject("sysInfo/notify_cp_allchannel_back_url");

				Element notifyRefundInterfaceUrl = (Element) document
						.selectObject("sysInfo/notify_refund_interface_url");
				Element notifyRefundBackUrl = (Element) document
						.selectObject("sysInfo/notify_refund_back_url");

				Element notifyCpCancelUrl = (Element) document
						.selectObject("sysInfo/notify_cp_cancel_url");
				Element notifyCpPayUrl = (Element) document
						.selectObject("sysInfo/notify_cp_pay_url");
				Element realNameVerifyUrl = (Element) document
						.selectObject("sysInfo/real_name_verify_url");
				Element verifyCountUrl = (Element) document
						.selectObject("sysInfo/verify_count_url");

				Element price = (Element) document
						.selectObject("sysInfo/java_query_price");
				Element ticket = (Element) document
						.selectObject("sysInfo/java_query_ticket");
				Element trainInfo = (Element) document
						.selectObject("sysInfo/java_query_trainInfo");
				Element trainTime = (Element) document
						.selectObject("sysInfo/java_query_trainTime");
				Consts.NOTIFY_CP_BACK_URL = notifyCpBackUrl.getTextTrim();
				Consts.NOTIFY_CP_INTERFACE_URL = notifyCpInterfaceUrl
						.getTextTrim();
				Consts.NOTIFY_CP_ALLCHANNEL_BACK_URL = notifyCpAllchannelBackUrl
						.getTextTrim();

				Consts.NOTIFY_REFUND_INTERFACE_URL = notifyRefundInterfaceUrl
						.getTextTrim();
				Consts.NOTIFY_REFUND_BACK_URL = notifyRefundBackUrl
						.getTextTrim();

				Consts.NOTIFY_CP_CANCEL_URL = notifyCpCancelUrl.getTextTrim();
				Consts.NOTIFY_CP_PAY_URL = notifyCpPayUrl.getTextTrim();
				
				Consts.REAL_NAME_VERIFY_URL = realNameVerifyUrl.getTextTrim();
				Consts.VERIFY_COUNT_URL = verifyCountUrl.getTextTrim();

				Consts.QUERY_TRAIN_INFO = trainInfo.getTextTrim();
				Consts.QUERY_TICKET = ticket.getTextTrim();
				Consts.QUERY_PRICE = price.getTextTrim();
				Consts.QUERY_TRAIN_TIME = trainTime.getTextTrim();
			} catch (Exception e) {
				plog.error("加载系统其它相关配置信息时发生错误");
				e.printStackTrace();
			}
		} catch (DocumentException e) {
			plog.error("解析pageconfig.xml文件发生错误" + e);
			return false;
		}
		return true;
	}

	/*
	 * public static void main(String[] args) throws
	 * UnsupportedEncodingException, DocumentException { try { String pageConfig
	 * =
	 * SystemConfig.class.getClassLoader().getResource("pageconfig.xml").getPath
	 * (); pageConfig = URLDecoder.decode(pageConfig, "UTF-8"); File configFile
	 * = new File(pageConfig); SAXReader reader = new SAXReader(); Document
	 * document; document = reader.read(configFile); Element eLong = (Element)
	 * document.selectObject("sysInfo/eLong");
	 * 
	 * SystemConfig s=new SystemConfig(); s.init(); } catch (Exception e) {
	 * e.printStackTrace(); } }
	 */

}
