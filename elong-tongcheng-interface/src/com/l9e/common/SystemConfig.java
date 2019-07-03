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
import java.io.UnsupportedEncodingException;
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
			/*System.setProperty("http.proxyHost", "192.168.65.126");
			System.setProperty("http.proxyPort", "3128");*/
			
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
				Element eLong = (Element) document.selectObject("sysInfo/eLong");
				Element sys = (Element) document.selectObject("sysInfo/sys");
				Element tc = (Element) document.selectObject("sysInfo/tc");
				if (eLong == null) {
					plog.error("读取pageconfig.xml文件发生错误，无指定的相关节点参数[sysInfo/eLong]");
					return false;
				} else {
					plog.info("加载eLong相关配置信息开始");
					try {
						Element elongMerchantCode = (Element)eLong.selectObject("merchantCode");
						Element elongSign = (Element)eLong.selectObject("sign");
						Element elongProcessPurchaseResult = (Element)eLong.selectObject("url/process_purchase_result");
						Element elongProcessReturnResult = (Element)eLong.selectObject("url/process_return_result");
						Element elongProcessRefund = (Element)eLong.selectObject("url/process_refund");
						Element elongOrderReturn = (Element)eLong.selectObject("url/order_return");
						Element elongOrderBook = (Element)eLong.selectObject("url/order_book");
						//先预订后支付的出票结果
						Element elongBookReturn = (Element)eLong.selectObject("url/process_purchase_book_result");
						
						
						Consts.ELONG_MERCHANTCODE=elongMerchantCode.getTextTrim();
						Consts.ELONG_SIGNKEY=elongSign.getTextTrim();
						Consts.ELONG_PROCESSPURCHASERESULT_URL=elongProcessPurchaseResult.getTextTrim();
						Consts.ELONG_PROCESSRETURNRESULT_URL=elongProcessReturnResult.getTextTrim();
						Consts.ELONG_PROCESSREFUND_URL=elongProcessRefund.getTextTrim();
						Consts.ELONG_ORDERRETURN_URL=elongOrderReturn.getTextTrim();
						Consts.ELONG_ORDERRBOOK_URL=elongOrderBook.getTextTrim();
						Consts.ELONG_BOOK_RETURN=elongBookReturn.getTextTrim();
						
						
					} catch (Exception e) {
						plog.error("加载eLong相关配置信息时 发生异常"+e);
						return false;
					}
				}
				if (tc == null) {
					plog.error("读取pageconfig.xml文件发生错误，无指定的相关节点参数[sysInfo/tc]");
					return false;
				} else {
					plog.info("加载同程相关配置信息开始");
					try {
						//Element elongMerchantCode = (Element)eLong.selectObject("merchantCode");
						//Element elongOrderReturn = (Element)eLong.selectObject("url/order_return");
						Element tcKey = (Element)tc.selectObject("key");
						Element tcPartnerid = (Element)tc.selectObject("partnerid");
						Element tcOutNotifyUrl=(Element)tc.selectObject("tc_out_notify_url");
						Element tcBookNotfyUrl=(Element)tc.selectObject("tc_book_notify_url");
						Element tcRemedyNotfyUrl=(Element)tc.selectObject("tc_remedy_notify_url");
						Element tcRefundNotifyUrl=(Element)tc.selectObject("tc_refund_notify_url");						
						Element tcCancelBackUrl=(Element)tc.selectObject("tc_cancel_back_url");	
						
						Element tcSynchronized = (Element) tc.selectObject("synchronized");
						Element tcScanTimeout = (Element) tcSynchronized.selectObject("scanTimeout");
						Element tcDelay = (Element) tcSynchronized.selectObject("delay");
						
						Consts.TC_SIGNKEY=tcKey.getTextTrim();
						Consts.TC_PARTNERID=tcPartnerid.getTextTrim();
						Consts.TC_OUTNOTIFYURL=tcOutNotifyUrl.getTextTrim();
						Consts.TC_BOOKNOTIFYURL=tcBookNotfyUrl.getTextTrim();
						Consts.TC_REMEDY_NOTIFY_URL=tcRemedyNotfyUrl.getTextTrim();
						Consts.TC_REFUND_NOTIFY_URL=tcRefundNotifyUrl.getTextTrim();
						
						Consts.TC_CANCELBACKURL=tcCancelBackUrl.getTextTrim();
						
						Consts.TC_SCAN_TIMEOUT = Long.valueOf(tcScanTimeout.getTextTrim());
						Consts.TC_DELAY = Long.valueOf(tcDelay.getTextTrim());
					} catch (Exception e) {
						plog.error("加载tongcheng相关配置信息时 发生异常"+e);
						return false;
					}
				}
				if (sys == null) {
					plog.error("读取pageconfig.xml文件发生错误，无指定的相关节点参数[sysInfo/sys]");
					return false;
				} else {
					try {
						plog.info("加载系统相关配置信息开始");
						Element sysMerchantId = (Element)sys.selectObject("sysMerchantId");
						Element sysSignKey = (Element)sys.selectObject("sysSignKey");
						Consts.SYS_MERCHANT_ID=sysMerchantId.getTextTrim();
						Consts.SYS_SIGN_KEY=sysSignKey.getTextTrim();
					} catch (Exception e) {
						plog.error("加载SYS相关配置信息时 发生异常"+e);
						return false;
					}
				}
				
				plog.info("加载系统其它相关配置信息开始");
				try {
					Element notifyCpBackUrl = (Element)document.selectObject("sysInfo/notify_cp_back_url");
					Element notifyCpInterfaceUrl = (Element)document.selectObject("sysInfo/notify_cp_interface_url");
					Element  notifyCpAllchannelBackUrl = (Element)document.selectObject("sysInfo/notify_cp_allchannel_back_url");
					
					Element notifyRefundInterfaceUrl = (Element)document.selectObject("sysInfo/notify_refund_interface_url");
					Element notifyRefundBackUrl = (Element)document.selectObject("sysInfo/notify_refund_back_url");
					
					Element notifyCpCancelUrl = (Element)document.selectObject("sysInfo/notify_cp_cancel_url");
					Element notifyCpPayUrl = (Element)document.selectObject("sysInfo/notify_cp_pay_url");
					
					
					Element price = (Element)document.selectObject("sysInfo/java_query_price");
					Element ticket = (Element)document.selectObject("sysInfo/java_query_ticket");
					Element trainInfo = (Element)document.selectObject("sysInfo/java_query_trainInfo");
					
					
					Consts.NOTIFY_CP_BACK_URL=notifyCpBackUrl.getTextTrim();
					Consts.NOTIFY_CP_INTERFACE_URL=notifyCpInterfaceUrl.getTextTrim();
					Consts.NOTIFY_CP_ALLCHANNEL_BACK_URL=notifyCpAllchannelBackUrl.getTextTrim();
					
					Consts.NOTIFY_REFUND_INTERFACE_URL=notifyRefundInterfaceUrl.getTextTrim();
					Consts.NOTIFY_REFUND_BACK_URL=notifyRefundBackUrl.getTextTrim();
					
					Consts.NOTIFY_CP_CANCEL_URL=notifyCpCancelUrl.getTextTrim();
					Consts.NOTIFY_CP_PAY_URL=notifyCpPayUrl.getTextTrim();
					
					/*public static String QUERY_TICKET="";
					public static String QUERY_PRICE="";*/
					Consts.QUERY_TRAIN_INFO = trainInfo.getTextTrim();
					Consts.QUERY_TICKET=ticket.getTextTrim();
					Consts.QUERY_PRICE=price.getTextTrim();
				} catch (Exception e) {
					plog.error("加载系统其它相关配置信息时发生错误");
					e.printStackTrace();
				}
			} catch (DocumentException e) {
				plog.error("解析pageconfig.xml文件发生错误"+e);
				return false;
			}
		return true;
	}
	
	/*public static void main(String[] args) throws UnsupportedEncodingException, DocumentException {
		try {
			String pageConfig = SystemConfig.class.getClassLoader().getResource("pageconfig.xml").getPath();
			pageConfig = URLDecoder.decode(pageConfig, "UTF-8");
			File configFile = new File(pageConfig);
			SAXReader reader = new SAXReader();
			Document document;
			document = reader.read(configFile);
			Element eLong = (Element) document.selectObject("sysInfo/eLong");
		
			SystemConfig s=new SystemConfig();
			s.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	
}
