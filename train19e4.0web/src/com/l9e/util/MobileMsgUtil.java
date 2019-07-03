package com.l9e.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import com.l9e.transaction.service.CommonService;

/**
 * 短信接口
 * @author zhangjun
 *
 */
public class MobileMsgUtil {
	
	private static final Logger logger = Logger.getLogger(MobileMsgUtil.class);
	
	@Value("#{propertiesReader[msm_strKey]}")
	private String strKey;
	
	@Value("#{propertiesReader[msm_strSysid]}")
	private String strSysid;
	
	@Value("#{propertiesReader[msm_strVersion]}")
	private String strVersion;
	
	@Value("#{propertiesReader[msm_strEncoding]}")
	private String strEncoding;
	
	@Value("#{propertiesReader[msm_url]}")
	private String strUrl;
	
	@Value("#{propertiesReader[msg_url]}")
	private String msg_url;
	
	@Resource
	protected CommonService commonService;
	
	public void send(String phoneNo, String content){
		//查询短信渠道  1、19e 2、鼎鑫亿动
		//查询短信渠道  1、19e 2、鼎鑫亿动 3、企信通
		String channel = commonService.querySysSettingByKey("mobile_msg_channel");
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("telephone", phoneNo);
		if ("2".equals(channel)){
			paramMap.put("content", content.replace(" ", "")+"【19易】");
		}
		else if("3".equals(channel)){
			paramMap.put("content", "【19旅行】"+content.replace(" ", ""));
		}
		else{
			paramMap.put("content", content.replace(" ", ""));
		}
		paramMap.put("phone_channel", channel);//发送短信的渠道
		paramMap.put("source_channel", "19e");//短信来源的渠道
		String params = "";
		try {
			params = UrlFormatUtil.CreateUrl("", paramMap, "", "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("调用短信平台："+msg_url+params);
		HttpUtil.sendByPost(msg_url, params, "utf-8");
	}
	
	/**
	 * 通过19e发送
	 * @param phoneNo
	 * @param content
	 */
	public boolean sendBy19e(String phoneNo, String content){
		String contentStr = new String(content).replace(" ", "");
		long tms = new Date().getTime();
		
		StringBuffer verData = new StringBuffer();		
		verData.append("versionid=").append(strVersion)
			   .append("&sysid=").append(strSysid)
			   .append("&mobilenum=").append(phoneNo)
			   .append("&sendcontent=").append(contentStr)
			   .append("&desc=Advertisment")
			   .append("&type=0")
			   .append("&key=").append(strKey)
			   .append("|ts=").append(tms);
		
		logger.info("[火车票短信]发送" + verData.toString());
		
		String vertify = Md5Encrypt.md5(verData.toString(), "gbk");
		String sendContent = "";
		try {
			sendContent = URLEncoder.encode(contentStr, strEncoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		StringBuffer sendData = new StringBuffer();
		sendData.append(strUrl)
				.append("?versionid=").append(strVersion)
				.append("&sysid=").append(strSysid)
				.append("&mobilenum=").append(phoneNo)
				.append("&sendcontent=").append(sendContent)
				.append("&vertify=").append(vertify)
				.append("&ts=").append(tms)
				.append("&desc=Advertisment&type=0");
		
		String result = HttpUtil.sendByGet(sendData.toString(), strEncoding, "30000", "30000");
		
		logger.info("[火车票短信]返回" + result);
		if(!StringUtils.isEmpty(result)){
			String code = result.split("&")[0].split("=")[1];
			if("0".equals(code)){
				logger.info("[火车票短信]短信发送成功");
				return true;
			}else if("1".equals(code)){
				logger.info("[火车票短信]短信接口参数错误");
				return false;
			}else if("9".equals(code)){
				logger.info("[火车票短信]短信接口调用存储过程错误");
				return false;
			}else{
				logger.info("[火车票短信]短信接口其它未知错误");
				return false;
			}
		}else{
			logger.info("[火车票短信]短信接口其它未知错误");
			return false;
		}
	}
	
	/**
	 * 通过鼎鑫亿动发送
	 * @param phoneNo
	 * @param content
	 */
	public boolean sendByDxyd(String phoneNo, String content){
		//String content = URLEncoder.encode("一九易是将有资质的火车票代售点和互联网提供的火车票服务信息汇集于平台，为一九易代理商提供互联网信息查询服务，协助代理商周边的用户通过互联网或火车票代售点联系并预订相关服务项目。在涉及到具体服务的过程中的问题，但我们会将尽力协助代理商或者用户与相关服务提供商进行协商，不能协商解决的，用户自己可以向消费者协会投诉或通过法律途径解决。","GBK");
		//String url = "http://221.122.114.116:8080/dxyd/sendSms.do?cpid=20130828&password=654321&phone=13716579764&spnumber=&msgcont="+content;
		String contentStr = new String(content+"【19易】").replace(" ", "");
		String sendContent = "";
		try {
			sendContent = URLEncoder.encode(contentStr, "GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		StringBuffer sendData = new StringBuffer();
		sendData.append("http://221.122.114.116:8080/dxyd/sendSms.do?cpid=20130828&password=aikugui")
				.append("&phone=").append(phoneNo)
				.append("&spnumber=&msgcont=").append(sendContent);
		String result = HttpUtil.sendByGet(sendData.toString(), "GBK", "30000", "30000");
		if(!StringUtils.isEmpty(result)){
			String code = result.split("&")[0];
			String errMsg = result.split("&")[1];
			if("0".equals(code)){
				logger.info("[鼎鑫短信]短信发送成功");
				return true;
			}else{
				logger.info("[鼎鑫短信]"+errMsg);
				return false;
			}
		}else{
			logger.info("[鼎鑫短信]短信接口其它未知错误");
			return false;
		}
	}
}
