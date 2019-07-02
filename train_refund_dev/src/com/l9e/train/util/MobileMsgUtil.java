package com.l9e.train.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * 短信接口
 * @author zhangjun
 *
 */
public class MobileMsgUtil {
	
	private static final Logger logger = Logger.getLogger(MobileMsgUtil.class);
	/**
	 * 通过企信通发送
	 * @param phoneNo
	 * @param content
	 */
	public static boolean sendByQxt(String phoneNo, String content){
		String contentStr = new String("【19旅行】"+content).replace(" ", "");
		String sendContent = "";
		try {
			sendContent = URLEncoder.encode(contentStr, "GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		StringBuffer sendData = new StringBuffer();
		sendData.append("http://www.ydqxt.com:8080/sendsms.asp?username=KYHKH&password=888888")
				.append("&mobile=").append(phoneNo)
				.append("&message=").append(sendContent);
		String result = HttpUtil.sendByGet(sendData.toString(), "GBK", "30000", "30000");
		if(!StringUtils.isEmpty(result)){
			if(Integer.valueOf(result)>0){
				logger.info("[企信通]短信发送成功");
				return true;
			}else{
				logger.info("[企信通]发送短信失败，错误码："+result);
				return false;
			}
		}else{
			logger.info("[企信通]短信接口其它未知错误");
			return false;
		}
	}
}
