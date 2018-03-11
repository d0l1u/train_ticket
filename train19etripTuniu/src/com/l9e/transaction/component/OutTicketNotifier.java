package com.l9e.transaction.component;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.l9e.transaction.component.model.NoticeObserver;
import com.l9e.transaction.service.TuniuNoticeService;
import com.l9e.transaction.service.TuniuOrderService;
import com.l9e.transaction.service.TuniuRefundService;
import com.l9e.transaction.service.TuniuCommonService;
import com.l9e.transaction.vo.Notice;
import com.l9e.transaction.vo.TuniuOrder;
import com.l9e.transaction.vo.TuniuRefund;
import com.l9e.util.UrlFormatUtil;

/**
 * 出票系统 预订/出票/取消/退票 通知器
 * 
 * @author licheng
 * 
 */
@Component("outTicketNotifier")
@Scope("prototype")
public class OutTicketNotifier implements NoticeObserver {

	private static final Logger logger = Logger
			.getLogger(OutTicketNotifier.class);

	@Resource
	private TuniuOrderService tuniuOrderService;

	@Resource
	private TuniuRefundService tuniuRefundService;

	@Resource
	private TuniuNoticeService tuniuNoticeService;

	/**
	 * 业务：预订、出票、退款
	 */
	private String service;
	/**
	 * 通知对象
	 */
	private Notice notice;
	/**
	 * 参数集合
	 */
	private Map<String, String> parameters;

	public OutTicketNotifier() {
		super();
	}

	public OutTicketNotifier(String service, Notice notice,
			Map<String, String> parameters) {
		super();
		this.service = service;
		this.notice = notice;
		this.parameters = parameters;
	}

	@Override
	public void afterResponse(String content) {
		logger.info(service + "通知请求noticeId: " + notice.getId()
				+ ", orderId : " + notice.getOrderId() + ", 返回result: "
				+ content);

		if (!StringUtils.isEmpty(content)) {
			String orderId = notice.getOrderId();

			if (NoticeObserver.BOOK.equals(service)) {// 预订
				String[] results = content.trim().split("\\|");
				if ("success".equalsIgnoreCase(results[0])
						&& results.length == 2 && orderId.equals(results[1])) {// 通知成功
					logger.info("通知出票系统占座成功，orderId : " + orderId);
					TuniuOrder order = tuniuOrderService.getOrderById(orderId,
							false);
					order.setOrderStatus(TuniuOrderService.STATUS_NOTIFY_SUCCESS);
					notice.setCpNotifyStatus(TuniuCommonService.NOTIFY_SUCCESS);
					notice.setCpNotifyFinishTime("cpNotifyFinishTime");

					tuniuOrderService.updateOrder(order, false);
				}

			} else if (NoticeObserver.OUT.equals(service)) {// 出票
				if ("success".equalsIgnoreCase(content)) {// 通知成功
					notice.setCpNotifyStatus(TuniuCommonService.NOTIFY_SUCCESS);
					notice.setCpNotifyFinishTime("cpNotifyFinishTime");
				}
			} else if (NoticeObserver.CANCEL.equals(service)) {// 取消
				if ("success".equalsIgnoreCase(content)) {// 通知成功
					TuniuOrder order = tuniuOrderService.getOrderById(orderId, false);
					order.setOrderStatus(TuniuOrderService.STATUS_CANCEL_ING);
					notice.setCpNotifyStatus(TuniuCommonService.NOTIFY_SUCCESS);
					notice.setCpNotifyFinishTime("cpNotifyFinishTime");
					//notice.setNotifyStatus(TuniuCommonService.NOTIFY_PREPARED);
					tuniuOrderService.updateOrder(order, false);
				}
			} else if (NoticeObserver.REFUND.equals(service)) {// 退款
				String[] results = content.trim().split("\\|");
				if ("success".equalsIgnoreCase(results[0])
						&& results.length == 2 && orderId.equals(results[1])) {// 通知成功

					TuniuRefund refund = tuniuRefundService
							.getRefundById(notice.getRefundId());
					refund
							.setRefundStatus(TuniuRefundService.STATUS_START_ROBOT_ALTER);
					notice.setCpNotifyStatus(TuniuCommonService.NOTIFY_SUCCESS);
					notice.setCpNotifyFinishTime("cpNotifyFinishTime");

					tuniuRefundService.updateRefund(refund);
				}
			}
		}
		notice.setCpNotifyNum(notice.getCpNotifyNum() + 1);
		if (notice.getCpNotifyNum() == 5
				&& !notice.getCpNotifyStatus().equals(
						TuniuCommonService.NOTIFY_SUCCESS)
				&& StringUtils.isEmpty(notice.getCpNotifyFinishTime())) {
			notice.setCpNotifyFinishTime("cpNotifyFinishTime");
			notice.setCpNotifyStatus(TuniuCommonService.NOTIFY_FAILURE);
		}
		if (NoticeObserver.BOOK.equals(service)) {
			tuniuNoticeService.updateBookNotice(notice);
		} else if (NoticeObserver.OUT.equals(service)
				|| NoticeObserver.CANCEL.equals(service)) {
			tuniuNoticeService.updateOutNotice(notice);
		} else if (NoticeObserver.REFUND.equals(service)) {
			tuniuNoticeService.updateRefundNotice(notice);
		}

	}

	@Override
	public void beforeRequest() {
		logger
				.info("途牛通知出票系统" + service + ", orderId : "
						+ notice.getOrderId());
	}

	@Override
	public Map<String, String> getParameters() {
		return parameters;
	}

	@Override
	public String getEntity() {
		return UrlFormatUtil.createUrl("", parameters);
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

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
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

}
