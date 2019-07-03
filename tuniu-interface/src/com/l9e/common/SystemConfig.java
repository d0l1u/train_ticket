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
			loadXml();
		} catch (Exception e) {
			plog.error("PageServlet False : ", e);
			plog.error("======================= 系统配置文件加载异常 ====================");
		}
	}

	/**
	 * 加载页面展示系统用配置文件
	 * 
	 * @throws DocumentException
	 */
	public void loadXml() throws DocumentException {

		try {
			String pageConfig = SystemConfig.class.getClassLoader().getResource("pageconfig.xml").getPath();
			pageConfig = URLDecoder.decode(pageConfig, "UTF-8");
			File configFile = new File(pageConfig);

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
			Element tuniu = (Element) document.selectObject("sysInfo/tuniu");
			Element cp = (Element) document.selectObject("sysInfo/cp");
			Element inter = (Element) document.selectObject("sysInfo/interface");

			if (tuniu == null) {
				plog.error("读取pageconfig.xml文件发生错误，无指定的相关节点参数[sysInfo/tuniu]");
				return false;
			} else {
				plog.info("加载途牛相关配置信息开始");
				try {
					Element account = (Element) tuniu.selectObject("account");
					Element signKey = (Element) tuniu.selectObject("signKey");
					Element merchantId = (Element) tuniu.selectObject("merchantId");
					Element idenValiUploadUrl = (Element) tuniu.selectObject("idenValiUploadUrl");
					Element reserveStatusPushUrl = (Element) tuniu.selectObject("reserveStatusPushUrl");
					TuniuConstant.account = account.getTextTrim();
					plog.info("系统参数-途牛接入账号account：" + TuniuConstant.account);
					TuniuConstant.signKey = signKey.getTextTrim();
					plog.info("系统参数-途牛签名密匙signKey：" + TuniuConstant.signKey);
					TuniuConstant.merchantId = merchantId.getTextTrim();
					plog.info("系统参数-途牛合作商户id：" + TuniuConstant.merchantId);
					TuniuConstant.idenValiUploadUrl = idenValiUploadUrl.getTextTrim();
					plog.info("系统参数-途牛身份核验信息上传地址：" + TuniuConstant.idenValiUploadUrl);
					TuniuConstant.reserveStatusPushUrl = reserveStatusPushUrl.getTextTrim();
					plog.info("系统参数-途牛12306排队订单推送地址：" + TuniuConstant.reserveStatusPushUrl);

				} catch (Exception e) {
					plog.error("加载tongcheng相关配置信息时 发生异常" + e);
					return false;
				}
			}

			if (cp == null) {
				plog.error("读取pageconfig.xml文件发生错误，无指定的相关节点参数[sysInfo/cp]");
				return false;
			} else {
				plog.info("加载出票系统相关配置信息开始");
				try {
					Element cpOutTicketUrl = (Element) cp.selectObject("cp_out_ticket_url");
					Element cpCancelUrl = (Element) cp.selectObject("cp_cancel_url");
					Element cpPayUrl = (Element) cp.selectObject("cp_pay_url");
					Element cpRefundUrl = (Element) cp.selectObject("cp_refund_url");
					Element cpOutTicketCallback = (Element) cp.selectObject("cp_out_ticket_callback");
					Element cpRefundCallback = (Element) cp.selectObject("cp_refund_callback");

					TuniuConstant.CP_OUT_TICKET_URL = cpOutTicketUrl.getTextTrim();
					plog.info("系统参数-出票接口：" + TuniuConstant.CP_OUT_TICKET_URL);
					TuniuConstant.CP_CANCEL_URL = cpCancelUrl.getTextTrim();
					plog.info("系统参数-取消接口：" + TuniuConstant.CP_CANCEL_URL);
					TuniuConstant.CP_PAY_URL = cpPayUrl.getTextTrim();
					plog.info("系统参数-支付接口：" + TuniuConstant.CP_PAY_URL);
					TuniuConstant.CP_REFUND_URL = cpRefundUrl.getTextTrim();
					plog.info("系统参数-退票接口：" + TuniuConstant.CP_REFUND_URL);
					TuniuConstant.CP_OUT_TICKET_CALLBACK = cpOutTicketCallback.getTextTrim();
					plog.info("系统参数-出票接口结果回调：" + TuniuConstant.CP_OUT_TICKET_CALLBACK);
					TuniuConstant.CP_REFUND_CALLBACK = cpRefundCallback.getTextTrim();
					plog.info("系统参数-退票接口结果回调：" + TuniuConstant.CP_REFUND_CALLBACK);
				} catch (Exception e) {
					plog.error("加载出票系统相关配置信息时 发生异常" + e);
					return false;
				}
			}

			if (inter == null) {
				plog.error("读取pageconfig.xml文件发生错误，无指定的相关节点参数[sysInfo/interface]");
				return false;
			} else {
				plog.info("加载出票系统相关配置信息开始");
			}
		} catch (DocumentException e) {
			plog.error("解析pageconfig.xml文件发生错误" + e);
			return false;
		}
		return true;
	}

}
