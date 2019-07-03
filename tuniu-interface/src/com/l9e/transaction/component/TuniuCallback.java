package com.l9e.transaction.component;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.l9e.common.TuniuConstant;
import com.l9e.transaction.component.model.NoticeObserver;
import com.l9e.transaction.service.TuniuChangeService;
import com.l9e.transaction.service.TuniuCommonService;
import com.l9e.transaction.service.TuniuNoticeService;
import com.l9e.transaction.service.TuniuOrderService;
import com.l9e.transaction.service.TuniuRefundService;
import com.l9e.transaction.vo.AsynchronousOutput;
import com.l9e.transaction.vo.Notice;
import com.l9e.transaction.vo.TuniuOrder;
import com.l9e.util.DateUtil;
import com.l9e.util.EncryptUtil;
import com.l9e.util.JacksonUtil;

/**
 * 途牛异步回调组件
 * 
 * @author licheng
 * 
 */
@Component("tuniuCallback")
@Scope("prototype")
public class TuniuCallback implements NoticeObserver {

	private static final Logger logger = Logger.getLogger(TuniuCallback.class);

	@Resource
	private TuniuOrderService tuniuOrderService;

	@Resource
	private TuniuRefundService tuniuRefundService;

	@Resource
	private TuniuNoticeService tuniuNoticeService;

	@Resource
	private TuniuCommonService tuniuCommonService;
	
	@Resource
	private TuniuChangeService tuniuChangeService;

	/**
	 * 业务：预订、出票/取消、退款、改签
	 */
	private String service;
	/**
	 * 通知对象
	 */
	private Notice notice;
	/**
	 * 回调对象
	 */
	private AsynchronousOutput asynOutput;

	public TuniuCallback() {
		super();
	}

	public TuniuCallback(String service, Notice notice,
			AsynchronousOutput asynOutput) {
		super();
		this.service = service;
		this.notice = notice;
		this.asynOutput = asynOutput;
	}

	@Override
	public void afterResponse(String content) {
		if (StringUtils.isEmpty(service) || notice == null) {
			logger.info("错误异步响应返回: " + content);
			return;
		}
		logger.info(service + "通知请求noticeId: " + notice.getId()
				+ ", orderId : " + notice.getOrderId() + ", 返回result: "+ content);

		String orderId = notice.getOrderId();
		TuniuOrder order = tuniuOrderService.getOrderById(orderId, false);
		boolean updateOrder = false;
		if (!StringUtils.isEmpty(content)) {
			boolean success = success(content);

			if (NoticeObserver.BOOK.equals(service)) {// 预订
				/* 途牛返回值处理 */
				if (success) {
					notice.setNotifyStatus(TuniuCommonService.NOTIFY_SUCCESS);
					notice.setNotifyFinishTime("notifyFinishTime");
				}
			} else if (NoticeObserver.OUT.equals(service)) {// 出票
				/* 途牛返回值处理 */

				if (success) {
					order.setNotifyStatus(TuniuCommonService.NOTIFY_SUCCESS);
					updateOrder = true;

					notice.setNotifyStatus(TuniuCommonService.NOTIFY_SUCCESS);
					notice.setNotifyFinishTime("notifyFinishTime");
				}
			} else if (NoticeObserver.CANCEL.equals(service)) {// 取消
				/* 途牛返回值处理 */
				if (success) {
					notice.setNotifyStatus(TuniuCommonService.NOTIFY_SUCCESS);
					notice.setNotifyFinishTime("notifyFinishTime");
				}
			} else if (NoticeObserver.REFUND.equals(service)) {// 退款
				/* 途牛返回值处理 */
				if (success) {
					notice.setNotifyStatus(TuniuCommonService.NOTIFY_SUCCESS);
					notice.setNotifyFinishTime("notifyFinishTime");
				}
			} else if (service.toLowerCase().contains(NoticeObserver.CHANGE)) {// 改签
				/* 途牛返回值处理 */
				if (success) {
					notice.setNotifyStatus(TuniuCommonService.CHANGE_NOTIFY_SUCCESS);
					notice.setNotifyFinishTime(DateUtil.dateToString(new Date(), DateUtil.DATE_FMT3));
				}
			}
		}
		notice.setNotifyNum(notice.getNotifyNum() + 1);
		if (notice.getNotifyNum() == 10&& (!notice.getNotifyStatus().equals(TuniuCommonService.NOTIFY_SUCCESS)||!notice.getNotifyStatus().equals(TuniuCommonService.CHANGE_NOTIFY_SUCCESS))&& StringUtils.isEmpty(notice.getNotifyFinishTime())) {
			notice.setNotifyFinishTime(DateUtil.dateToString(new Date(), DateUtil.DATE_FMT3));
			if(service.toLowerCase().contains(NoticeObserver.CHANGE)){
				notice.setNotifyStatus(TuniuCommonService.CHANGE_NOTIFY_FAILURE);
			}else{
				notice.setNotifyStatus(TuniuCommonService.NOTIFY_FAILURE);
				order.setNotifyStatus(TuniuCommonService.NOTIFY_FAILURE);
				updateOrder = true;
			}
		}

		if (updateOrder) {
			tuniuOrderService.updateOrder(order, false);
		}
		if (NoticeObserver.BOOK.equals(service)) {
			tuniuNoticeService.updateBookNotice(notice);
		} else if (NoticeObserver.OUT.equals(service)
				|| NoticeObserver.CANCEL.equals(service)) {
			tuniuNoticeService.updateOutNotice(notice);
		} else if (NoticeObserver.REFUND.equals(service)) {
			tuniuNoticeService.updateRefundNotice(notice);
		}else if(service.toLowerCase().contains(NoticeObserver.CHANGE)){
			logger.info(order.getOrderId()+"改签通知时间："+notice.getNotifyFinishTime());
			tuniuChangeService.updateNotice(notice);
		}
	}

	@Override
	public void beforeRequest() {
		logger.info("途牛结果回调" + service + ", orderId : " + notice.getOrderId());
	}

	@Override
	public Map<String, String> getParameters() {
		return null;
	}

	@Override
	public String getEntity() {
		if (asynOutput == null)
			return null;
		try {
			/* 时间戳 */
			String timestamp = DateUtil.dateToString(new Date(),
					TuniuConstant.TIMESTAMP_FORMAT);
			asynOutput.setTimestamp(timestamp);
			/* 账号 */
			asynOutput.setAccount(TuniuConstant.account);
			/* 输出参数base64加密 */
			String data = asynOutput.getData();
			if (!StringUtils.isEmpty(data)) {
				data = EncryptUtil.encode(data, TuniuConstant.signKey, "UTF-8");
				asynOutput.setData(data);
			}
			/* 返回信息匹配 */
			String returnCode = asynOutput.getReturnCode();
			String errorMsg = asynOutput.getErrorMsg();
			if(StringUtils.isEmpty(errorMsg)) {
				errorMsg = tuniuCommonService.getErrorMessage(returnCode);
			}
			if(StringUtils.isEmpty(errorMsg)) {
				logger.info("途牛orderid : " + notice.getOrderId() + ", 返回码信息为空, code : " + returnCode);
				returnCode = TuniuCommonService.RETURN_CODE_UNKNOWN_ERROR;
				asynOutput.setReturnCode(returnCode);
				errorMsg = tuniuCommonService.getErrorMessage(returnCode);
			}
			logger.info("途牛orderid : " + notice.getOrderId() + ",返回码"+ returnCode + ",信息"+errorMsg);
			asynOutput.setErrorMsg(URLEncoder.encode(errorMsg, "UTF-8"));
			asynOutput.set_returnCode(Integer.valueOf(returnCode));

			/* 签名 */
			Map<String, String> params = new HashMap<String, String>();
			params.put("account", TuniuConstant.account);
			params.put("timestamp", timestamp);
			params.put("returnCode", returnCode);
			params.put("errorMsg", errorMsg);
			params.put("data", data);
			String sign = tuniuCommonService.generateSign(params,
					TuniuConstant.signKey);

			asynOutput.setSign(sign);

			return JacksonUtil.generateJson(asynOutput);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public Notice getNotice() {
		return notice;
	}

	public void setNotice(Notice notice) {
		this.notice = notice;
	}

	public AsynchronousOutput getAsynOutput() {
		return asynOutput;
	}

	public void setAsynOutput(AsynchronousOutput asynOutput) {
		this.asynOutput = asynOutput;
	}

	public TuniuOrderService getTuniuOrderService() {
		return tuniuOrderService;
	}

	public void setTuniuOrderService(TuniuOrderService tuniuOrderService) {
		this.tuniuOrderService = tuniuOrderService;
	}

	public TuniuRefundService getTuniuRefundService() {
		return tuniuRefundService;
	}

	public void setTuniuRefundService(TuniuRefundService tuniuRefundService) {
		this.tuniuRefundService = tuniuRefundService;
	}

	public TuniuNoticeService getTuniuNoticeService() {
		return tuniuNoticeService;
	}

	public void setTuniuNoticeService(TuniuNoticeService tuniuNoticeService) {
		this.tuniuNoticeService = tuniuNoticeService;
	}

	public TuniuCommonService getTuniuCommonService() {
		return tuniuCommonService;
	}

	public void setTuniuCommonService(TuniuCommonService tuniuCommonService) {
		this.tuniuCommonService = tuniuCommonService;
	}

	public boolean success(String result) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode root = mapper.readTree(result);
			JsonNode valueNode = root.path("success");
			if (valueNode.isBoolean()) {
				return valueNode.asBoolean();
			} else {
				return false;
			}
		} catch (JsonProcessingException e) {
			logger.info("途牛返回结果解析异常,result : " + result);
			return false;
		} catch (IOException e) {
			logger.info("途牛返回结果解析异常,result : " + result);
			return false;
		}
	}
}
