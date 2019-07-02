package com.l9e.transaction.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.l9e.common.BaseController;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.service.RefundNotifyService;
import com.l9e.transaction.vo.RefundVo;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.DateUtil;
import com.umpay.api.exception.VerifyException;
import com.umpay.api.paygate.v40.Mer2Plat_v40;
import com.umpay.api.paygate.v40.Plat2Mer_v40;

/**
 * 车票退订
 * 
 * @author zhangjun
 * 
 */
@Controller
@RequestMapping("/refund")
public class RefundTicketController extends BaseController {

	private static final Logger logger = Logger
			.getLogger(RefundTicketController.class);

	@Resource
	private OrderService orderService;
	@Resource
	private RefundNotifyService refundNotifyService;

	@Value("#{propertiesReader[merchantId]}")
	private String merchantId;

	@Value("#{propertiesReader[notify_refund_rul]}")
	private String notify_refund_rul;// 退款结果通知地址

	@Value("#{propertiesReader[signKey]}")
	private String signKey;// 验签key

	@Value("#{propertiesReader[req_url]}")
	private String req_url;// 移动优势请求地址

	@RequestMapping("/refunding.jhtml")
	public String refunding(RefundVo refund,
			MultipartHttpServletRequest request, HttpServletResponse response)
			throws IOException {

		String order_id = this.getParam(request, "order_id");
		logger.info("-----------------order_id=" + order_id);
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("order_id", order_id);
		paramMap.put("refund_status", TrainConsts.REFUND_STREAM_WAIT_REFUND);
		logger.info("[代理商主动发起退款]order_id=" + refund.getOrder_id());

		// 9点-21点可以
		Date currentDate = new Date();
		String datePre = DateUtil.dateToString(currentDate, "yyyyMMdd");
		Date begin = DateUtil
				.stringToDate(datePre + "090000", "yyyyMMddHHmmss");// 9:00
		Date end = DateUtil.stringToDate(datePre + "210000", "yyyyMMddHHmmss");// 21:00
		if (currentDate.before(begin) || currentDate.after(end)) {
			logger
					.info("不受理该退款，发起时间为："
							+ DateUtil.dateToString(currentDate,
									"yyyy-MM-dd HH:mm:ss"));
			String errMsg = "<span style='line-height:40px;'>由于火车票业务新上线，每日临时受理退款时间为早9:00～晚21:00，请见谅。<br />如有特殊情况请先到车站退票，第二日持车站退款小票到代理商处退款。</span>";
			errMsg = URLEncoder.encode(errMsg, "UTF-8");
			return "redirect:/common/goToErrPage.jhtml?errMsg=" + errMsg;
		}
		String tipUrl = null;// 小票地址
		// 上传小票
		MultipartFile upfile = request.getFile("upfile");
		/*
		 * if(!upfile.isEmpty()){ String fileSuffix =
		 * upfile.getOriginalFilename().substring(upfile.getOriginalFilename().lastIndexOf("."));
		 * 
		 * if(".jpg".equalsIgnoreCase(fileSuffix) ||
		 * ".png".equalsIgnoreCase(fileSuffix)){ String fileName =
		 * CreateIDUtil.createID("IMG") + fileSuffix; String prePath =
		 * request.getSession().getServletContext().getRealPath("/upload");
		 * String date = DateUtil.dateToString(new Date(), "yyyyMMdd"); prePath =
		 * prePath + "/" + date; File targetFile = new File(prePath, fileName);
		 * if(!targetFile.exists()){ targetFile.mkdirs(); }
		 * upfile.transferTo(targetFile); tipUrl = request.getScheme() + "://" +
		 * request.getServerName() + ":" + request.getServerPort() + "/upload/" +
		 * date + "/" + fileName; } }
		 */
		String old_refund_status = this.getParam(request, "old_refund_status");// 原来的退款状态
		refund.setRefund_status(TrainConsts.REFUND_PRE_REFUND);// 准备退款
		refund.setRefund_purl(tipUrl);
		// orderService.refunding(refund, old_refund_status);
		orderService.updateRefundStream(paramMap);

		return "redirect:/query/queryOrderList.jhtml";
	}

	/**
	 * 退款
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/refund.jhtml")
	public String refund(MultipartHttpServletRequest request,
			HttpServletResponse response) throws IOException, ParseException {

		/**
		 * token防止重复提交
		 */
		String token = this.getParam(request, "token");
		if (null == request.getSession().getAttribute("sessionToken")
				|| !token.equals((String) request.getSession().getAttribute(
						"sessionToken"))) {
			logger.info("[token校验]退款发生重复提交，请求被拒绝，重定向到订单查询页面");
			return "redirect:/query/queryOrderList.jhtml";
		} else {
			request.getSession().removeAttribute("sessionToken");
		}
		String order_id = this.getParam(request, "order_id");
		logger.info("[代理商主动发起退款]order_id=" + order_id);
		// 9点-21点可以
		Date currentDate = new Date();
		Map<String, String> orderInfo = orderService.queryOrderInfo(order_id);
		String pay_time = orderInfo.get("pay_time");
		String refund_plan_time = "";
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = fmt.parse(pay_time);
			long ms = date.getTime();
			ms = ms + 24 * 60 * 60 * 1000;
			date = new Date(ms);
			refund_plan_time = fmt.format(date);
			logger.info("[代理商主动发起退款]计划退款时间为" + refund_plan_time);
		} catch (ParseException e) {
			logger.error("[代理商主动发起退款]日期、字符串转化发生错误！");
		}
		String datePre = DateUtil.dateToString(currentDate, "yyyyMMdd");
		Date begin = DateUtil
				.stringToDate(datePre + "070000", "yyyyMMddHHmmss");// 7:00
		Date end = DateUtil.stringToDate(datePre + "230000", "yyyyMMddHHmmss");// 23:00
		if (currentDate.before(begin) || currentDate.after(end)) {
			logger
					.info("不受理该退款，发起时间为："
							+ DateUtil.dateToString(currentDate,
									"yyyy-MM-dd HH:mm:ss"));
			String errMsg = "<span style='line-height:40px;'>每日受理退票时间为早7:00～晚23:00，请见谅。<br />如有特殊情况请先到车站退票，第二日持车站退款小票到代理商处退款。</span>";
			errMsg = URLEncoder.encode(errMsg, "UTF-8");
			return "redirect:/common/goToErrPage.jhtml?errMsg=" + errMsg;
		}

		String cp_id_str = this.getParam(request, "cp_id_str");
		List<Map<String, String>> refundList = new ArrayList<Map<String, String>>();
		Map<String, String> refundMap = null;
		double this_refund_money = 0;
		String refundSeq = CreateIDUtil.createID("WTK");
		for (String cp_id : cp_id_str.split(",")) {
			logger.info("[代理商主动发起退款]cp_id=" + cp_id);
			refundMap = new HashMap<String, String>();

			String refund_purl = null;// 小票地址
			// 上传小票
			// MultipartFile upfile = request.getFile("upfile_" + cp_id);
			// if(!upfile.isEmpty()){
			// String fileSuffix =
			// upfile.getOriginalFilename().substring(upfile.getOriginalFilename().lastIndexOf("."));
			//	
			// if(".jpg".equalsIgnoreCase(fileSuffix) ||
			// ".png".equalsIgnoreCase(fileSuffix)
			// || ".jpeg".equalsIgnoreCase(fileSuffix) ||
			// ".bmp".equalsIgnoreCase(fileSuffix)){
			// String fileName = CreateIDUtil.createID("IMG") + fileSuffix;
			// String prePath =
			// request.getSession().getServletContext().getRealPath("/upload");
			// String date = DateUtil.dateToString(new Date(), "yyyyMMdd");
			// prePath = prePath + "/" + date;
			// File targetFile = new File(prePath, fileName);
			// if(!targetFile.exists()){
			// targetFile.mkdirs();
			// }
			// upfile.transferTo(targetFile);
			// refund_purl = request.getScheme() + "://"
			// + request.getServerName() + ":" + request.getServerPort()
			// + "/upload/" + date + "/" + fileName;
			// logger.info("退款小票" + refund_purl);
			// }
			// }
			refundMap.put("refund_type", TrainConsts.REFUND_TYPE_1);// 用户退款
			refundMap.put("order_id", order_id);
			refundMap.put("cp_id", cp_id);
			refundMap.put("refund_seq", refundSeq);// ASP退款请求流水号
			System.out.println("fffffffffffffffffffff====="
					+ "refund_money_"
					+ cp_id
					+ (this.getParam(request, "refund_money_" + cp_id))
							.toString());
			refundMap.put("refund_money", this.getParam(request,
					"refund_money_" + cp_id));// 退款金额
			
			this_refund_money = this_refund_money
					+ Double.parseDouble(this.getParam(request, "refund_money_"
							+ cp_id));
			refundMap.put("refund_purl", refund_purl);
			refundMap.put("user_remark", this.getParam(request, "user_remark_"
					+ cp_id));
			refundMap
					.put("refund_status", TrainConsts.REFUND_STREAM_WAIT_REFUND);// 准备退款
			refundMap.put("old_refund_status", this.getParam(request,
					"refund_status_" + cp_id));// 旧的退款状态
			refundMap.put("refund_percent", this.getParam(request,
					"refund_percent_" + cp_id));
			refundMap.put("refund_plan_time", refund_plan_time);
			refundMap.put("refund_no", CreateIDUtil.createID(""));
			refundList.add(refundMap);
		}
		orderService.addRefundStream(refundList, order_id);

		Map<String, Object> notifyMap = new HashMap<String, Object>();
		notifyMap.put("order_id", order_id);
		notifyMap.put("refund_type", "00");
		notifyMap.put("refund_money", String.valueOf(this_refund_money));
		notifyMap.put("refund_seq", refundSeq);
		notifyMap.put("notify_status", "00");
		notifyMap.put("notify_num", 0);
		// Map<String, String> orderInfo =
		// orderService.queryOrderInfo(order_id);
		notifyMap.put("order_money", orderInfo.get("pay_money"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date createDate = sdf.parse(orderInfo.get("pay_time"));
		String notify_time = null;
		if (new Date().getTime() - createDate.getTime() > 24 * 60 * 60 * 1000) {
			notify_time = sdf.format(new Date());
		} else {
			notify_time = sdf.format(new Date(createDate.getTime() + 24 * 60
					* 60 * 1000));
		}
		notifyMap.put("notify_time", notify_time);
		refundNotifyService.insertIntoNotify(notifyMap);

		return "redirect:/query/queryOrderList.jhtml";
	}

	// 接收联通优势退款通知
	@RequestMapping("/receviceNotification.jhtml")
	public void receviceNotification(HttpServletRequest request,
			HttpServletResponse response) {
		String merId = this.getParam(request, "mer_id");
		if (!merchantId.equals(merId)) {
			logger.error("移动优势传递的商户号为" + merId + ",与19e的商户号" + merchantId
					+ "不同");
			return;
		}
		String signType = this.getParam(request, "sign_type");
		String sign = this.getParam(request, "sign");
		String version = this.getParam(request, "version");
		String refund_no = this.getParam(request, "refund_no");
		String order_id = this.getParam(request, "order_id");
		String mer_date = this.getParam(request, "mer_date");
		String refund_state = this.getParam(request, "refund_state");
		String refund_amt = this.getParam(request, "refund_amt");
		Map<String, String> receviceMap = new HashMap<String, String>();
		receviceMap.put("mer_id", merId);
		receviceMap.put("sign_type", signType);
		receviceMap.put("version", version);
		receviceMap.put("sign", sign);
		receviceMap.put("refund_no", refund_no);
		receviceMap.put("order_id", order_id);
		receviceMap.put("mer_date", mer_date);
		receviceMap.put("refund_state", refund_state);
		receviceMap.put("refund_amt", refund_amt);
		logger.info("[退款状态变更通知]接受到的参数为" + receviceMap.toString());
		Map<String, String> exportMap = null;
		try {
			exportMap = Plat2Mer_v40.getPlatNotifyData(receviceMap);
		} catch (VerifyException e) {
			logger.info("[退款状态变更通知]验签失败！");
			return;
		}

		Map<String, String> updateMap = new HashMap<String, String>();
		updateMap.put("order_id", order_id);
		updateMap.put("refund_seq", refund_no);
		updateMap.put("refund_amt", refund_amt);
		if ("REFUND_PROCESS".equals(refund_state)) {
			updateMap.put("refund_status",
					TrainConsts.REFUND_STREAM_BEGIN_REFUND);
		} else if ("REFUND_SUCCESS".equals(refund_state)) {
			updateMap.put("refund_status",
					TrainConsts.REFUND_STREAM_REFUND_FINISH);
		} else if ("REFUND_FAIL".equals(refund_state)) {
			updateMap.put("refund_status", TrainConsts.REFUND_STREAM_FAIL);
		}
		orderService.updateOrderStreamStatus(updateMap);

		Map<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("mer_id", merId);
		responseMap.put("sign_type", signType);
		responseMap.put("version", "4.0");
		responseMap.put("ret_code", "0000");
		String responseStr = Mer2Plat_v40.merNotifyResData(responseMap);
		String html = "<html><head><META NAME=\"MobilePayPlatform\" CONTENT=\""
				+ responseStr + "\" /></head></html>";
		logger.info("[退款状态变更通知]向联通优势反馈html: " + html);
		try {
			write2Response(response, html);
		} catch (IOException e) {
			logger.error("[退款状态变更通知]向联通优势反馈失败！");
		}
	}

}
