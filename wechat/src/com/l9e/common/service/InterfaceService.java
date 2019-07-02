package com.l9e.common.service;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import com.l9e.common.message.resp.TextMessage;
import com.l9e.weixin.util.MessageUtil;
import com.l9e.weixin.util.WeChatUtil;

public class InterfaceService {
	protected static final Logger logger = Logger
			.getLogger(InterfaceService.class);

	private static String query_order_url;
	private static String change_password_url;
	private static String register_url;
	static {
		Properties props = new Properties();
		String url = WeChatUtil.class.getClassLoader().getResource(
				"config.properties").getPath();
		// System.out.println(url);
		// url = url.substring(6);
		String empUrl = url.replace("%20", " ");// 如果你的文件路径中包含空格，是必定会报错的
		logger.info("The path of config.properties is " + empUrl);
		InputStream in = null;
		// in = WeChatUtil.class.getResourceAsStream("/config.properties");

		try {
			in = new BufferedInputStream(new FileInputStream(empUrl));
			props.load(in);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		query_order_url = (String) props.get("query_order_url");
		register_url = (String) props.get("register_url");
		change_password_url = (String) props.get("change_password_url");
	}

	/**
	 * 处理微信发来的请求
	 * 
	 * @param request
	 * @return
	 */
	public static String processRequest(HttpServletRequest request) {
		String respMessage = null;
		try {
			// 默认返回的文本消息内容
			String respContent = "请求处理异常，请稍候尝试！";

			// xml请求解析
			Map<String, String> requestMap = MessageUtil.parseXml(request);
			logger.info("[service处理微信发来的请求]默认返回的文本消息内容:" + requestMap);
			// 发送方帐号（open_id）
			String fromUserName = requestMap.get("FromUserName");
			// 公众帐号
			String toUserName = requestMap.get("ToUserName");
			// 消息类型
			String msgType = requestMap.get("MsgType");

			// 回复文本消息
			TextMessage textMessage = new TextMessage();
			textMessage.setToUserName(fromUserName);
			textMessage.setFromUserName(toUserName);
			textMessage.setCreateTime(new Date().getTime());
			textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
			textMessage.setFuncFlag(0);

			// 文本消息
			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
				respContent = "您发送的是文本消息！";
				// 文本消息内容
				String content = requestMap.get("Content");

				// 判断用户发送的是否是单个QQ表情
				if (MessageUtil.isQqFace(content)) {
					// 回复文本消息
					textMessage.setToUserName(fromUserName);
					textMessage.setFromUserName(toUserName);
					textMessage.setCreateTime(new Date().getTime());
					textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
					textMessage.setFuncFlag(0);
					// 用户发什么QQ表情，就返回什么QQ表情
					respContent = content;
				}
			}
			// 图片消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
				respContent = "您发送的是图片消息！";
			}
			// 地理位置消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
				respContent = "您发送的是地理位置消息！";
			}
			// 链接消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
				respContent = "您发送的是链接消息！";
			}
			// 音频消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
				respContent = "您发送的是音频消息！";
			}
			// 事件推送
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
				// 事件类型
				String eventType = requestMap.get("Event");
				// 订阅
				if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
					respContent = "谢谢您的关注！";
				}
				// 取消订阅
				else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
				}
				// 自定义菜单点击事件
				else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
					String eventKey = requestMap.get("EventKey");
					if ("11".equals(eventKey)) {
						StringBuffer sb = new StringBuffer();
						sb.append("您可以通过酷游购买火车票哦\n");
						sb.append("<a href=\"" + WeChatUtil.TRAINHOMEURL
								+ fromUserName + "\">点击这里，立即购买</a>\n");
						respContent = sb.toString();
					} else if ("12".equals(eventKey)) {
						StringBuffer sb = new StringBuffer();
						sb.append("如果您是酷游的注册用户\n");
						sb.append("<a href=\"" + query_order_url + "?openID="
								+ fromUserName + "\">请点击这里，立即查询</a>\n");
						sb.append("如果您还不是酷游的注册用户\n");
						sb.append("<a href=\"" + change_password_url + "?r=1&openID="
								+ fromUserName + "\">请点击这里，立刻注册</a>\n");
						respContent = sb.toString();
					} else if("15".equals(eventKey)) {
						StringBuffer sb = new StringBuffer();
						sb.append("如果您是酷游的注册用户\n");
						sb.append("<a href=\"" + change_password_url + "?openID="
								+ fromUserName + "\">请点击这里，立即重置</a>\n");
						sb.append("如果您还不是酷游的注册用户\n");
						sb.append("<a href=\"" + change_password_url + "?r=1&openID="
								+ fromUserName + "\">请点击这里，立刻注册</a>\n");
						respContent = sb.toString();
					}else if("13".equals(eventKey)){
						StringBuffer sb = new StringBuffer();
						sb.append("联系酷游旅游客服,").append("请拨打电话：\n").append("027-87618711\n").append("027-87618633");
						respContent = sb.toString();
					}
				}
			}

			textMessage.setContent(respContent);
			respMessage = MessageUtil.textMessageToXml(textMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return respMessage;
	}

	/**
	 * emoji表情转换(hex -> utf-16)
	 * 
	 * @param hexEmoji
	 * @return
	 */
	public static String emoji(int hexEmoji) {
		return String.valueOf(Character.toChars(hexEmoji));
	}

}
