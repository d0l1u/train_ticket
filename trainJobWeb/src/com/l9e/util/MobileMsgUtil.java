package com.l9e.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.l9e.transaction.vo.Phone;

import net.sf.json.JSONObject;

/**
 * 短信接口
 * 
 * @author zyx
 *
 */
public class MobileMsgUtil {

	private static final Logger logger = Logger.getLogger(MobileMsgUtil.class);

	private String strKey = "96a3ef62681f1c58a88ec3b76eb2bc55";

	private String strSysid = "1027";

	private String strVersion = "2.00";

	private String strEncoding = "GBK";

	private String strUrl = "http://20.1.2.112:18090/sendSms";

	public String send(Phone phone) {
		String channel = phone.getPhone_channel();
		// 短信渠道：1、19e；2、鼎鑫移动；3、企信通;
		if (StringUtils.isEmpty(channel) || "1".equals(channel)) {
			return sendBy19e(phone);
		} else if ("2".equals(channel)) {
			// return sendByDxyd(phone);
			return sendByDxyd_new(phone);

		} else if ("3".equals(channel)) {
			return sendByQxt(phone);
		} else {
			return sendBy19e(phone);
		}
	}

	/**
	 * 已经停用，接入第三方 通过19e发送
	 * 
	 * @author:
	 * @date: 2018年5月23日 上午10:38:42
	 * @param phone
	 * @return String
	 */
	@Deprecated
	public String sendBy19e(Phone phone) {
		String contentStr = new String(phone.getContent()).replace(" ", "");
		long tms = new Date().getTime();

		StringBuffer verData = new StringBuffer();
		verData.append("versionid=").append(strVersion).append("&sysid=").append(strSysid).append("&mobilenum=").append(phone.getTelephone())
				.append("&sendcontent=").append(contentStr).append("&desc=Advertisment").append("&type=0").append("&key=").append(strKey).append("|ts=")
				.append(tms);

		logger.info("[火车票短信]发送" + verData.toString());

		String vertify = Md5Encrypt.md5(verData.toString(), "gbk");
		String sendContent = "";
		try {
			sendContent = URLEncoder.encode(contentStr, strEncoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		StringBuffer sendData = new StringBuffer();
		sendData.append(strUrl).append("?versionid=").append(strVersion).append("&sysid=").append(strSysid).append("&mobilenum=").append(phone.getTelephone())
				.append("&sendcontent=").append(sendContent).append("&vertify=").append(vertify).append("&ts=").append(tms).append("&desc=Advertisment&type=0");

		String result = HttpUtil.sendByGet(sendData.toString(), strEncoding, "30000", "30000");

		logger.info("[火车票短信]返回" + result);
		if (!StringUtils.isEmpty(result)) {
			String code = result.split("&")[0].split("=")[1];
			if ("0".equals(code)) {
				logger.info("[火车票短信]短信发送成功");
				return "SUCCESS";
			} else if ("1".equals(code)) {
				logger.info("[火车票短信]短信接口参数错误");
				return "[火车票短信]短信接口参数错误";
			} else if ("9".equals(code)) {
				logger.info("[火车票短信]短信接口调用存储过程错误");
				return "[火车票短信]短信接口调用存储过程错误";
			} else {
				logger.info("[火车票短信]短信接口其它未知错误");
				return "[火车票短信]短信接口其它未知错误";
			}
		} else {
			logger.info("[火车票短信]短信接口其它未知错误");
			return "[火车票短信]短信接口其它未知错误";
		}
	}

	/**
	 * 通过鼎鑫亿动发送
	 * 
	 * @param phoneNo
	 * @param content
	 */
	public String sendByDxyd(Phone phone) {
		String contentStr = new String(phone.getContent()).replace(" ", "");
		String sendContent = "";
		try {
			sendContent = URLEncoder.encode(contentStr, "GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		StringBuffer sendData = new StringBuffer();
		sendData.append("http://221.122.114.116:8080/dxyd/sendSms.do?cpid=20130828&password=aikugui").append("&phone=").append(phone.getTelephone().trim())
				.append("&spnumber=&msgcont=").append(sendContent.trim());
		String result = HttpUtil.sendByGet(sendData.toString().trim(), "GBK", "30000", "30000");
		if (!StringUtils.isEmpty(result)) {
			String code = result.split("&")[0];
			String errMsg = result.split("&")[1];
			if ("0".equals(code)) {
				logger.info("[鼎鑫短信]短信发送成功");
				return "SUCCESS";
			} else {
				logger.info("[鼎鑫短信]" + errMsg);
				return "[鼎鑫短信]发送失败" + errMsg;
			}
		} else {
			logger.info("[鼎鑫短信]短信接口其它未知错误");
			return "[鼎鑫短信]短信接口其它未知错误";
		}
	}

	/**
	 * 通过鼎鑫亿动——新接口--发送
	 * 
	 * @param phoneNo
	 * @param content
	 *            http://60.205.143.44:8088/v2sms.aspx 对应UTF-8
	 *            http://60.205.143.44:8088/v2smsGBK.aspx 对应GB2312
	 * 
	 */
	public String sendByDxyd_new(Phone phone) {

		String contentStr = new String(phone.getContent()).replace(" ", "");

		String url = "http://60.205.143.44:8088/v2smsGBK.aspx";
		String userid = "39";// 企业ID
		String user = "20130828";// 账号
		String pwd = "20130828@swdx";// 密码

		String timestamp = DateUtil.dateToString(new Date(), DateUtil.DATE_FMT2);
		String md5str = user + pwd + timestamp;
		logger.info("鼎鑫亿动new:" + md5str);
		String sign = Md5Encrypt.md5(md5str, "utf-8");
		String mobile = phone.getTelephone().trim(); // 全部被叫号码
		String content = contentStr.trim(); // 发送内容
		String sendTime = ""; // 定时发送时间 ,为空表示立即发送
		String action = "send"; // 发送任务命令
		String extno = ""; // 扩展子号

		Map<String, String> map = new HashMap<String, String>();
		map.put("userid", userid);
		map.put("timestamp", timestamp);
		map.put("sign", sign);
		map.put("mobile", mobile);
		map.put("content", content);
		map.put("sendTime", sendTime);
		map.put("action", action);
		map.put("extno", extno);

		String sendurl = null;
		try {
			sendurl = UrlFormatUtil.CreateUrl(url, map);
			logger.info("请求url：" + sendurl);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.info("异常", e);
		}
		String result;
		try {
			result = HttpUtil.sendByGet(sendurl.toString().trim(), "GB2312", "30000", "30000");
			logger.info("鼎鑫new:" + result);

			if (!StringUtils.isEmpty(result)) {

				String result_json = XmlToJsonUtil.getJSONFromXml(result).toString(1);
				logger.info("鼎鑫new:" + result_json);
				JSONObject jsonobject = JSONObject.fromObject(result_json);
				String returnstatus = (String) jsonobject.get("returnstatus");
				String message = (String) jsonobject.get("message");
				String remainpoint = (String) jsonobject.get("remainpoint");
				String taskID = (String) jsonobject.get("taskID");
				String successCounts = (String) jsonobject.get("successCounts");
				logger.info("鼎鑫new：" + returnstatus + "," + message + "," + remainpoint + "," + taskID + "," + successCounts);
				if ("Success".equals(returnstatus)) {
					logger.info("[鼎鑫短信]短信发送成功");
					return "SUCCESS";
				} else {
					logger.info("[鼎鑫短信]" + message);
					return "[鼎鑫短信]发送失败" + message;
				}

			} else {
				logger.info("[鼎鑫短信]短信接口其它未知错误2");
				return "[鼎鑫短信]短信接口其它未知错误2";
			}
		} catch (Exception e) {
			logger.info("异常：", e);
			return "[鼎鑫短信]短信接口其它未知错误1";
		}

	}

	/**
	 * 通过企信通发送
	 * 
	 * @param phoneNo
	 * @param content
	 */
	public static String sendByQxt(Phone phone) {
		String channel_ext = phone.getPhone_channel_ext();
		String contentStr = new String(phone.getContent()).replace(" ", "");
		String sendContent = "";
		try {
			sendContent = URLEncoder.encode(contentStr, "GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		StringBuffer sendData = new StringBuffer();
		sendData.append("http://www.ydqxt.com:8080/sendsms.asp?username=KYHKH&password=888888&Ext=" + channel_ext).append("&mobile=")
				.append(phone.getTelephone().trim()).append("&message=").append(sendContent.trim());
		String result = HttpUtil.sendByGet(sendData.toString().trim(), "GBK", "30000", "30000");
		if (!StringUtils.isEmpty(result)) {
			if (Integer.valueOf(result) > 0) {
				logger.info("[企信通]短信发送成功");
				return "SUCCESS";
			} else {
				logger.info("[企信通]发送短信失败，错误码：" + result);
				return "[企信通]发送短信失败，错误码：" + result;
			}
		} else {
			logger.info("[企信通]短信接口其它未知错误");
			return "[企信通]短信接口其它未知错误";
		}
	}

	public static void main(String[] args) {

		MobileMsgUtil msgUtil = new MobileMsgUtil();
		Phone phone = new Phone();
		phone.setContent("【19易】:" + "您好，111-2222-3333--33222222");
		phone.setTelephone("13051979731");
		String reString = msgUtil.sendByDxyd_new(phone);
		System.out.println(reString);

	}
}
