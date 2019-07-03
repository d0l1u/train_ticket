package com.l9e.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

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
	
	public void send(String phoneNo, String content){
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
			}else if("1".equals(code)){
				logger.info("[火车票短信]短信接口参数错误");
			}else if("9".equals(code)){
				logger.info("[火车票短信]短信接口调用存储过程错误");
			}else{
				logger.info("[火车票短信]短信接口其它未知错误");
			}
		}
	}
}
