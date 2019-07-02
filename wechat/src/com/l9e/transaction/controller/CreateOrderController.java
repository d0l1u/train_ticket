package com.l9e.transaction.controller;

import java.io.IOException;
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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.common.ErrorCode;
import com.l9e.common.LoginUserInfo;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.service.UserInfoService;
import com.l9e.transaction.vo.BookDetailInfo;
import com.l9e.transaction.vo.BookInfo;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.transaction.vo.OrderInfoBx;
import com.l9e.transaction.vo.OrderInfoCp;
import com.l9e.transaction.vo.OrderInfoPs;
import com.l9e.transaction.vo.ProductVo;
import com.l9e.transaction.vo.WeekDay;
import com.l9e.util.AmountUtil;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.HttpUtil;
import com.l9e.weixin.exception.SDKRuntimeException;
import com.l9e.weixin.pojo.WxPayHelper;
import com.l9e.weixin.util.CommonUtil;
import com.l9e.weixin.util.SHA1Util;
import com.l9e.weixin.util.WeChatUtil;
import com.umpay.api.exception.ReqDataException;
import com.umpay.api.exception.RetDataException;
import com.umpay.api.exception.VerifyException;
import com.umpay.api.paygate.v40.Mer2Plat_v40;
import com.umpay.api.paygate.v40.Plat2Mer_v40;

/**
 * 下单
 * 
 * @author zhangjun
 * 
 */
@Controller
@RequestMapping("/order")
public class CreateOrderController extends BaseController {

	private static final Logger logger = Logger
			.getLogger(CreateOrderController.class);

	@Resource
	private OrderService orderService;
	@Resource
	private UserInfoService userInfoService;

	@Value("#{propertiesReader[merchantId]}")
	private String merchantId;

	@Value("#{propertiesReader[signKey]}")
	private String signKey;// 验签key

	@Value("#{propertiesReader[req_url]}")
	private String req_url;// cmpay请求地址

	@Value("#{propertiesReader[characterSet]}")
	private String characterSet;

	@Value("#{propertiesReader[callbackUrl]}")
	private String callbackUrl;// cmpay下单同步调用接口地址

	@Value("#{propertiesReader[notifyUrl]}")
	private String notifyUrl;// cmpay下单异步调用接口地址

	@Value("#{propertiesReader[showUrl]}")
	private String showUrl;// cmpay查看订单信息

	@Value("#{propertiesReader[paySignKey]}")
	private String paySignKey;// 公众号中用于支付请求中用于加密的密钥

	@Value("#{propertiesReader[partnerID]}")
	private String partnerID;// 财付通商户身份的标识

	@Value("#{propertiesReader[partnerKey]}")
	private String partnerKey;// 财付通商户权限密钥
	
	@Value("#{propertiesReader[recevice_pay_url]}")
	private String recevice_pay_url;

	/**
	 * 订购下单
	 * 
	 * @param bookInfo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/createOrder.jhtml")
	public String createTrainOrder(BookInfo bookInfo,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		logger.info("进入下单");
		/*
		 * //9炄1�7-21点可仄1�7 Date date = new Date(); String datePre =
		 * DateUtil.dateToString(date, "yyyyMMdd"); Date begin =
		 * DateUtil.stringToDate(datePre + "090000", "yyyyMMddHHmmss");//8:00
		 * Date end = DateUtil.stringToDate(datePre + "210000",
		 * "yyyyMMddHHmmss");//22:00 if(date.before(begin) || date.after(end)){
		 * String errMsg = "由于节前订票高峰，又遇火车票系统调整，临时订票时间修改为早9点-晚21点，带来的不便敬请谅解！";
		 * errMsg = URLEncoder.encode(errMsg, "UTF-8"); return
		 * "redirect:/common/goToErrPage.jhtml?errMsg="+errMsg; }
		 */
		String location = this.getParam(request, "location");
		String openID = (String) request.getSession().getAttribute(
				"weixinOpenID");
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("openID", openID);
		/*List<Map<String, String>> userInfoList = userInfoService
				.queryWeChartUser(paramMap);*/
		/*
		 * if (userInfoList == null || userInfoList.size() == 0) {
		 * request.setAttribute("openID", openID);
		 * request.setAttribute("location", location); return "book/register"; }
		 */
		/*if (userInfoList != null && userInfoList.size() > 0) {
			LoginUserInfo loginUser = new LoginUserInfo();
			loginUser.setUser_phone(userInfoList.get(0).get("user_phone"));
			loginUser.setUser_id(userInfoList.get(0).get("user_id"));
			loginUser.setTerminal("weixin");
			// 用户信息放入session
			request.getSession().setAttribute(TrainConsts.INF_LOGIN_USER,
					loginUser);
		}*/
		// LoginUserInfo loginUser = this.getLoginUser(request);
		// String user_phone = loginUser.getUser_phone();//
		// loginUser.getCm_phone();

		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setLink_phone(this.getParam(request, "link_phone"));

		OrderInfoPs orderInfoPs = new OrderInfoPs();
		List<OrderInfoCp> orderInfoCpList = new ArrayList<OrderInfoCp>();
		List<OrderInfoBx> orderInfoBxList = new ArrayList<OrderInfoBx>();
		System.out.println("测试：：：" + bookInfo.getPay_money()
				+ bookInfo.getBookDetailInfoList());

		String product_id = this.getParam(request, "product_id");
		// String product_id = "PT000000001";
		System.out.println(product_id);
		for (BookDetailInfo detailInfo : bookInfo.getBookDetailInfoList()) {
			detailInfo.setProduct_id(product_id);
		}
		System.out.println("bookInfo.getBookDetailInfoList():"
				+ bookInfo.getBookDetailInfoList().toString());
		String link_phone = this.getParam(request, "link_phone");
		String link_name = this.getParam(request, "link_name");
		orderInfo.setLink_phone(link_phone);
		orderInfo.setProduct_type("10000");
		orderInfo.setLink_name(link_name);
		orderInfo.setChannel("weixin");
		if (request.getSession().getAttribute(TrainConsts.INF_LOGIN_USER) != null) {
			orderInfo.setUser_id(((LoginUserInfo) request.getSession()
					.getAttribute(TrainConsts.INF_LOGIN_USER)).getUser_id());
		}
		this.groupData(bookInfo, orderInfo, orderInfoPs, orderInfoCpList,
				orderInfoBxList, request);// 组合封装数据

		Map<String, String> bxfpMap = null;// 保险发票
		if ("1".equals(this.getParam(request, "fpNeed"))) {
			bxfpMap = new HashMap<String, String>(4);
			bxfpMap.put("order_id", orderInfo.getOrder_id());
			bxfpMap.put("fp_receiver", this.getParam(request, "fp_receiver"));
			bxfpMap.put("fp_phone", this.getParam(request, "fp_phone"));
			bxfpMap.put("fp_zip_code", this.getParam(request, "fp_zip_code"));
			bxfpMap.put("fp_address", this.getParam(request, "fp_address"));
		}

		// 向火车票库下单，返回火车票订单号
		String order_id = orderService.addOrder(orderInfo, orderInfoPs,
				orderInfoCpList, orderInfoBxList, bxfpMap);

		logger.info("[火车票下单]：火车票asp_order_id:" + order_id);

		String totalPay4Show = getTotalPay4Show(bookInfo, request);

		//logger.info("userInfoList：" + userInfoList);

		return "redirect:/order/orderComfirm.jhtml?order_id=" + order_id
				+ "&totalPay4Show=" + totalPay4Show;

	}

	/**
	 * 下单重定向查询
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/orderComfirm.jhtml")
	public String orderComfirm(HttpServletRequest request,
			HttpServletResponse response) {
		String order_id = this.getParam(request, "order_id");
		Map<String, String> orderInfo = orderService.queryOrderInfo(order_id);
		// OrderInfoPs orderInfoPs = orderService.queryOrderInfoPs(order_id);
		List<Map<String, String>> detailList = orderService
				.queryOrderDetailList(order_id);

		if (orderInfo != null
				&& !StringUtils.isEmpty(orderInfo.get("ext_seat"))
				&& orderInfo.get("ext_seat").indexOf(TrainConsts.SEAT_9) != -1) {// 备选无座
			request.setAttribute("wz_ext", 1);
		}

		String totalPay = this.getParam(request, "totalPay4Show");
		int totalFee = (int) (Double.parseDouble(totalPay) * 100);
		WxPayHelper wxPayHelper = new WxPayHelper();
		// 先设置基本信息
		wxPayHelper.SetAppId(WeChatUtil.APPID);
		wxPayHelper.SetAppKey(paySignKey);
		wxPayHelper.SetPartnerKey(partnerKey);
		wxPayHelper.SetSignType("sha1");
		// 设置请求package信息
		wxPayHelper.SetParameter("bank_type", "WX");
		wxPayHelper.SetParameter("body", "火车票");
		
		wxPayHelper.SetParameter("partner", partnerID);
		wxPayHelper.SetParameter("out_trade_no", order_id);
		logger.info("订单号为：" + order_id);
		wxPayHelper.SetParameter("total_fee", String.valueOf(totalFee));
		logger.info("支付总额为：" + totalFee);
		wxPayHelper.SetParameter("fee_type", "1");
		wxPayHelper.SetParameter("notify_url", recevice_pay_url);
		wxPayHelper.SetParameter("spbill_create_ip", getIpAddr(request));
		wxPayHelper.SetParameter("input_charset", "UTF-8");
		String WPayPackage = null;

		String nonceStr = CommonUtil.CreateNoncestr();
		String timeStamp = String.valueOf(new Date().getTime() / 1000);
		try {
			WPayPackage = wxPayHelper.CreateBizPackage();
		} catch (SDKRuntimeException e) {
			logger.error("生成微信支付package时发生异常：" + e);
		}
		String str = "appid=" + WeChatUtil.APPID + "&appkey=" + paySignKey
				+ "&noncestr=" + nonceStr + "&package=" + WPayPackage
				+ "&timestamp=" + timeStamp;

		String paySign = SHA1Util.Sha1(str);
		request.setAttribute("appId", WeChatUtil.APPID);
		WPayPackage.replace("\"", "\\\"");
		logger.info("package: " + WPayPackage);
		request.setAttribute("package", WPayPackage);
		request.setAttribute("paySign", paySign);
		request.setAttribute("timeStamp", timeStamp);
		request.setAttribute("nonceStr", nonceStr);

		String travel_time = orderInfo.get("travel_time");
		int day = DateUtil.getDay(travel_time, "yyyy-MM-dd");
		String dayOfWeek = WeekDay.getDay(day);
		request.setAttribute("day", dayOfWeek);
		request.setAttribute("order_id", order_id);
		request.setAttribute("orderInfo", orderInfo);
		request.setAttribute("totalFee", String.valueOf(totalFee));
		// request.setAttribute("orderInfoPs", orderInfoPs);
		request.setAttribute("detailList", detailList);
		request.setAttribute("ticketTypeMap", TrainConsts.getTicketType());
		request.setAttribute("seatTypeMap", TrainConsts.getSeatType());
		request.setAttribute("idsTypeMap", TrainConsts.getIdsType());
		request.setAttribute("trainTypeCn", this.getTrainTypeCn(orderInfo
				.get("train_no")));
		request.setAttribute("totalPay4Show", this.getParam(request,
				"totalPay4Show"));
		request.setAttribute("ip", getIpAddr(request));

		return "book/bookConfirm";
	}

	public String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		logger.info("请求支付的ip为" + ip);
		return ip;
	}

	/**
	 * 组合封装数据
	 * 
	 * @param bookInfo
	 * @param orderInfo
	 * @param orderInfoCpList
	 * @param orderInfoBxList
	 * @throws ParseException
	 */
	private void groupData(BookInfo bookInfo, OrderInfo orderInfo,
			OrderInfoPs orderInfoPs, List<OrderInfoCp> orderInfoCpList,
			List<OrderInfoBx> orderInfoBxList, HttpServletRequest request)
			throws ParseException {
		String order_id = CreateIDUtil.createID("WX");
		// 订单
		orderInfo.setOrder_id(order_id);
		orderInfo.setOrder_name(bookInfo.getFrom_city() + "/"
				+ bookInfo.getTo_city());

		/*
		 * 必须先给主订单表pay_money等金额赋值后再处理明细表(涉及匹配产品的问题)
		 */
		// orderInfo.setFrom_station(bookInfo.getFrom_city());
		// orderInfo.setTo_station(bookInfo.getTo_city());
		// orderInfo.setFrom_time(bookInfo.getFrom_time());
		// orderInfo.setTo_time(bookInfo.getTo_time());
		// orderInfo.setTrain_no(bookInfo.getTrain_no());
		// orderInfo.setTravel_time(bookInfo.getTravelTime());
		String totalBxMoney = this.getBxTotalPay(bookInfo, request);
		orderInfo.setChannel("weixin");
		orderInfo.setBx_pay_money(totalBxMoney);
		if (totalBxMoney == null || "0".equals(totalBxMoney)
				|| "".equals(totalBxMoney)) {
			orderInfo.setOrder_level("0");
		}
		if (totalBxMoney != null && totalBxMoney.length() > 0
				&& Double.parseDouble(totalBxMoney) > 0) {
			orderInfo.setOrder_level("1");
		}
		orderInfo.setTicket_pay_money(this.getTicketTotalPay(bookInfo));
		if (TrainConsts.OUT_TICKET_TYPE_PS
				.equals(bookInfo.getOut_ticket_type())) {// 配送票
			// orderInfo.setPs_pay_money(bookInfo.getPs_pay_money());
		}
		orderInfo.setPay_money(getTotalPay4Show(bookInfo, request));
		// orderInfo.setOut_ticket_type(bookInfo.getOut_ticket_type());
		// orderInfo.setSeat_type(bookInfo.getSeat_type());
		orderInfo.setOrder_status(TrainConsts.PRE_ORDER);// 预下单

		if (!StringUtils.isEmpty(bookInfo.getSeat_type())
				&& (TrainConsts.SEAT_8.equals(bookInfo.getSeat_type())
						|| TrainConsts.SEAT_2.equals(bookInfo.getSeat_type()) || TrainConsts.SEAT_3
						.equals(bookInfo.getSeat_type()))
				&& !StringUtils.isEmpty(bookInfo.getWz_ext())
				&& "1".equals(bookInfo.getWz_ext())) {// 硬座选择无座备选
			String ext_seat = "" + TrainConsts.SEAT_9 + ","
					+ bookInfo.getDanjia();
			orderInfo.setExt_seat(ext_seat);
		}

		// 配送
		orderInfoPs.setOrder_id(order_id);
		orderInfoPs.setLink_name(bookInfo.getLink_name());
		orderInfoPs.setLink_phone(bookInfo.getLink_phone());
		orderInfoPs.setLink_mail(bookInfo.getLink_mail());
		if (TrainConsts.OUT_TICKET_TYPE_ELEC.equals(bookInfo
				.getOut_ticket_type())) {// 电子票
			orderInfoPs.setPs_status(TrainConsts.PS_STATUS_SENDED);// 配送完成
			orderInfoPs.setPay_money("0");// 支付0
			orderInfoPs.setBuy_money("0");// 成本0
		} else {// 配送票
			orderInfoPs.setPs_status(TrainConsts.PS_STATUS_WAITING);// 等待配送
			orderInfoPs.setLink_address(bookInfo.getLink_address());
		}

		for (BookDetailInfo bookDetailInfo : bookInfo.getBookDetailInfoList()) {
			// 车票
			OrderInfoCp orderInfoCp = new OrderInfoCp();
			String cp_id = CreateIDUtil.createID("CP");// 车票id
			orderInfoCp.setFrom_station(bookInfo.getFrom_city());
			orderInfoCp.setArrive_station(bookInfo.getTo_city());
			String from_time = bookInfo.getFrom_time();
			if (from_time.length() < 19) {
				from_time = from_time + ":00";
			}
			String to_time = bookInfo.getTo_time();
			if (to_time.length() < 19) {
				to_time = to_time + ":00";
			}
			orderInfoCp.setFrom_time(from_time);
			orderInfoCp.setArrive_time(to_time);
			orderInfoCp.setTrain_no(bookInfo.getTrain_no());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date travel_time = sdf.parse(bookInfo.getTravelTime());
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			orderInfoCp.setTravel_time(sdf.format(travel_time));
			orderInfoCp.setCp_id(cp_id);
			orderInfoCp.setOrder_id(order_id);
			orderInfoCp.setPay_money(bookInfo.getDanjia());
			orderInfoCp.setTicket_type(bookDetailInfo.getTicket_type());// 车票类型
			orderInfoCp.setUser_name(bookDetailInfo.getUser_name().trim());// 姓名
			orderInfoCp.setIds_type(bookDetailInfo.getIds_type());
			orderInfoCp.setUser_ids(bookDetailInfo.getUser_ids().trim());// 证件号
			orderInfoCp.setSeat_type(bookInfo.getSeat_type());// 坐席
			orderInfoCpList.add(orderInfoCp);

			// 保险单价大于0则保存该保险记录
			String sale_price = bookDetailInfo.getSale_price();// 保险单价
			if (!StringUtils.isEmpty(sale_price)
					&& Double.parseDouble(sale_price) > 0) {
				// 查询保险渠道 保险渠道: 1、快保 2、合众
				String bx_channel = commonService
						.querySysSettingByKey("bx_channel");
				if (StringUtils.isEmpty(bx_channel)) {
					bx_channel = "1";
				}
				// 保险
				OrderInfoBx orderInfoBx = new OrderInfoBx();
				orderInfoBx.setBx_id(CreateIDUtil.createID("BX"));
				orderInfoBx.setOrder_id(order_id);
				orderInfoBx.setCp_id(cp_id);// 车票id
				orderInfoBx.setUser_name(bookDetailInfo.getUser_name().trim());// 姓名
				orderInfoBx.setIds_type(bookDetailInfo.getIds_type());
				orderInfoBx.setUser_ids(bookDetailInfo.getUser_ids().trim());
				orderInfoBx.setFrom_name(bookInfo.getFrom_city());// 出发城市
				orderInfoBx.setTo_name(bookInfo.getTo_city());// 到达城市
				// orderInfoBx.setBx_status("0");//未发送
				orderInfoBx.setPay_money(bookDetailInfo.getSale_price());
				orderInfoBx.setProduct_id(bookDetailInfo.getProduct_id());
				// orderInfoBx.setEffect_date(bookInfo.getTravelTime());//生效日期
				orderInfoBx.setEffect_date(bookInfo.getFrom_time() + ":00");
				orderInfoBx.setTrain_no(bookInfo.getTrain_no());// 车次
				orderInfoBx.setTelephone(bookInfo.getLink_phone());// 联系人电话
				orderInfoBx.setBx_channel(bx_channel);
				orderInfoBxList.add(orderInfoBx);
			}
		}
	}

	/**
	 * 票价总额
	 * 
	 * @param bookInfo
	 * @return
	 */
	private String getTicketTotalPay(BookInfo bookInfo) {
		String ticketTotalPay = bookInfo.getTicket_pay_money();
		if (StringUtils.isEmpty(ticketTotalPay)) {
			ticketTotalPay = String.valueOf(AmountUtil.mul(Double
					.parseDouble(bookInfo.getDanjia()), bookInfo
					.getBookDetailInfoList().size()));
			bookInfo.setTicket_pay_money(ticketTotalPay);
		}
		return ticketTotalPay;
	}

	/**
	 * 保险总额
	 * 
	 * @param bookInfo
	 * @return
	 */
	private String getBxTotalPay(BookInfo bookInfo, HttpServletRequest request) {
		String bxTotalPay = bookInfo.getBx_pay_money();
		if (StringUtils.isEmpty(bxTotalPay)) {// 保险为空 则计算保险金额
			// 查询产品列表
			ProductVo product = new ProductVo();
			product.setType(TrainConsts.PRODUCT_TYPE_1);// 保险
			product.setStatus(TrainConsts.PRODUCT_STATUS_1);// 上架

			List<ProductVo> productList = commonService
					.queryProductInfoList(product);

			double bx_total_pay = 0;// 保险总计
			for (BookDetailInfo detail : bookInfo.getBookDetailInfoList()) {
				for (ProductVo pvo : productList) {
					if (detail.getProduct_id().equals(pvo.getProduct_id())) {
						detail.setBx_name(pvo.getName());// 保险名称
						detail.setSale_price(String
								.valueOf(pvo.getSale_price()));
						bx_total_pay = AmountUtil.add(bx_total_pay, pvo
								.getSale_price());
						break;
					}
				}
			}
			bxTotalPay = String.valueOf(bx_total_pay);
			bookInfo.setBx_pay_money(String.valueOf(bx_total_pay));
		}
		return bxTotalPay;
	}

	/**
	 * 显示支付总金额（未抛去保险返利）
	 * 
	 * @param bookInfo
	 * @return
	 */
	private String getTotalPay4Show(BookInfo bookInfo,
			HttpServletRequest request) {
		String totalPay = bookInfo.getPay_money();
		if (StringUtils.isEmpty(totalPay)) {// 是否有值保险总价为空导致
			double money = Double.parseDouble(getTicketTotalPay(bookInfo));
			double bxPay = Double.parseDouble(getBxTotalPay(bookInfo, request));// 保险支付金额
			if (bxPay > 0) {
				money = AmountUtil.add(Double
						.parseDouble(getTicketTotalPay(bookInfo)), bxPay);
			}

			// 配�1�7�票霄1�7要加上配送物流费用
			if (!StringUtils.isEmpty(bookInfo.getOut_ticket_type())
					&& TrainConsts.OUT_TICKET_TYPE_PS.equals(bookInfo
							.getOut_ticket_type())) {
				money = AmountUtil.add(money, Double.parseDouble(bookInfo
						.getPs_pay_money()));
			}

			totalPay = String.valueOf(money);
			bookInfo.setPay_money(totalPay);
		}
		return totalPay;
	}

	/**
	 * 联动优势下单重定向支付页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/orderUpay.jhtml")
	public void orderUpay(HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("向联动优势下单");

		Map<String, String> payErrorCodeMap = ErrorCode.getPayErrorCode();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		// 商户会计日期（自定义）
		String merAcDate = sdf.format(new Date()).substring(0, 8);
		String orderId = this.getParam(request, "order_id");
		Map<String, String> orderInfo = orderService.queryOrderInfo(orderId);
		// 获取用户的IP地址，作为防钓鱼的一种方法
		String clientIp = request.getHeader("x-forwarded-for");
		if ((clientIp == null) || (clientIp.length() == 0)
				|| ("unknown".equalsIgnoreCase(clientIp))) {
			clientIp = request.getHeader("Proxy-Client-IP");
		}
		if ((clientIp == null) || (clientIp.length() == 0)
				|| ("unknown".equalsIgnoreCase(clientIp))) {
			clientIp = request.getHeader("WL-Proxy-Client-IP");
		}
		if ((clientIp == null) || (clientIp.length() == 0)
				|| ("unknown".equalsIgnoreCase(clientIp))) {
			clientIp = request.getRemoteAddr();
		}
		String amount = String.valueOf((int) (Double.valueOf(orderInfo
				.get("pay_money")) * 100));
		String amt_type = "RMB";
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("service", "pay_req");
		paramMap.put("charset", "UTF-8");
		paramMap.put("sign_type", "RSA");
		paramMap.put("version", "4.0");
		paramMap.put("mer_id", merchantId);
		paramMap.put("mer_date", merAcDate);
		paramMap.put("amount", amount);
		paramMap.put("amt_type", amt_type);
		paramMap.put("notify_url", notifyUrl);
		paramMap.put("order_id", orderId);
		String url = null;
		try {
			url = Mer2Plat_v40.ReqDataByGet(paramMap).getUrl();
		} catch (ReqDataException e) {
			logger.info("验证字段规则或者数据签名发生异常");
			return;
		}
		logger.info("url:::" + url);

		String html = HttpUtil.sendByGet(url, "UTF-8", null, null);
		Map<String, Object> resultMap = null;
		try {
			resultMap = Plat2Mer_v40.getResData(html);
		} catch (RetDataException e) {
			logger.info("校验失败");
			return;
		}

		String retCode = (String) resultMap.get("ret_code");
		logger.info("ret_code:" + retCode);
		if ("0000".equals(retCode)) {
			String retOrderId = (String) resultMap.get("order_id");
			if (!orderId.equals(retOrderId)) {
				logger.error("联通优势返回订单号为" + retOrderId + ", 与原订单号" + orderId
						+ "不同， 发生未知错误！");
				return;
			}
			logger.info("订单号为" + orderId + "向联通优势下订单成功！");
			String tradeNo = (String) resultMap.get("trade_no");
			String upayUrl = "https://m.soopay.net/m/xhtml/index.do?tradeNo="
					+ tradeNo;
			logger.info("订单号" + orderId + "的联通优势支付路径为" + upayUrl);

			try {
				response.sendRedirect(upayUrl);
			} catch (IOException e) {
				logger.error("订单号" + orderId + "的联通优势支付路径" + upayUrl
						+ "跳转发生错误！");
				return;
			}
		} else {
			String errorInfo = payErrorCodeMap.get("retCode");
			logger.error("订单号" + orderId + "支付时发生错误, 错误原因为:" + errorInfo);
		}

	}

	/*
	 * 联通优势异步通知
	 */
	@RequestMapping("/receiveUpayNofity.jhtml")
	public void receiveUpayNofity(HttpServletRequest request,
			HttpServletResponse response) {

		Map<String, String> upayOrderStatusMap = ErrorCode
				.getUpayOrderStatusMap();
		String service = this.getParam(request, "service");
		String merId = this.getParam(request, "mer_id");
		String charset = this.getParam(request, "charset");
		String signType = this.getParam(request, "sign_type");
		String sign = this.getParam(request, "sign");
		String version = this.getParam(request, "version");
		String tradeNo = this.getParam(request, "trade_no");
		String goodsId = this.getParam(request, "goods_id");
		String orderId = this.getParam(request, "order_id");
		String merDate = this.getParam(request, "mer_date");
		String payDate = this.getParam(request, "pay_date");
		String amount = this.getParam(request, "amount");
		String amtType = this.getParam(request, "amt_type");
		String payType = this.getParam(request, "pay_type");
		String mediaId = this.getParam(request, "media_id");
		String mediaType = this.getParam(request, "media_type");
		String settleDate = this.getParam(request, "settle_date");
		String merPriv = this.getParam(request, "mer_priv");
		String tradeState = this.getParam(request, "trade_state");
		String paySeq = this.getParam(request, "pay_seq");
		String errorCode = this.getParam(request, "error_code");

		Map<String, String> receviceMap = new HashMap<String, String>();
		receviceMap.put("service", service);
		receviceMap.put("mer_id", merId);
		receviceMap.put("charset", charset);
		receviceMap.put("sign_type", signType);
		receviceMap.put("sign", sign);
		receviceMap.put("version", version);
		receviceMap.put("trade_no", tradeNo);
		if (goodsId != null && goodsId.trim().length() != 0) {
			logger.error("goods_id ::::::::::: " + goodsId + " ::: length ::::"
					+ goodsId.length());
			receviceMap.put("goods_id", goodsId);
		}
		receviceMap.put("mer_date", merDate);
		receviceMap.put("order_id", orderId);
		receviceMap.put("pay_date", payDate);
		receviceMap.put("amount", amount);
		receviceMap.put("amt_type", amtType);
		receviceMap.put("pay_type", payType);
		receviceMap.put("media_id", mediaId);
		receviceMap.put("media_type", mediaType);
		receviceMap.put("settle_date", settleDate);
		if (merPriv != null && merPriv.trim().length() != 0) {
			logger.error("merPriv ::::::::::: " + merPriv + " ::: length ::::"
					+ merPriv.length());
			receviceMap.put("mer_priv", merPriv);
		}
		receviceMap.put("trade_state", tradeState);
		receviceMap.put("pay_seq", paySeq);
		receviceMap.put("error_code", errorCode);
		logger.info("联动优势通知参数为：" + receviceMap.toString());
		Map<String, String> handledMap = null;
		try {
			handledMap = Plat2Mer_v40.getPlatNotifyData(receviceMap);
		} catch (VerifyException e) {
			logger.info("接收通知验签失败！");
			return;
		}
		logger.info("联动优势返回交易状态为" + tradeState + ", 交易流水号为" + tradeNo);
		Map<String, String> updateMap = new HashMap<String, String>();
		updateMap.put("order_id", orderId);
		if ("TRADE_SUCCESS".equals(tradeState)) {
			logger.info("订单号为" + orderId + "的订单支付成功！");
			updateMap.put("order_status", "11");
		} else if ("WAIT_BUYER_PAY".equals(tradeState)) {
			logger.info("订单号为" + orderId + "的订单状态为订单已创建" + tradeState);
			updateMap.put("order_status", null);
		} else if ("TRADE_FAIL".equals(tradeState)) {
			logger.info("订单号为" + orderId + "的订单支付失败！");
			updateMap.put("order_status", "99");
		} else if ("TRADE_CANCEL".equals(tradeState)) {
			logger.info("订单号为" + orderId + "的订单已取消支付！");
			updateMap.put("order_status", "99");
		} else if ("TRADE_CLOSED".equals(tradeState)) {
			logger.info("订单号为" + orderId + "的订单已关闭！");
			updateMap.put("order_status", "99");
		} else {
			logger.info("联通优势返回的订单状态异常，订单状态为" + tradeState);
		}
		updateMap.put("pay_no", paySeq);
		updateMap.put("pay_order_id", tradeNo);
		updateMap.put("pay_type", "11");
		orderService.updateOrderPayNo(updateMap);
		Map<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("mer_id", merchantId);
		responseMap.put("version", "4.0");
		responseMap.put("ret_code", "0000");
		responseMap.put("mer_date", merDate);
		responseMap.put("order_id", orderId);
		responseMap.put("sign_type", "RSA");
		String responseMetaStr = Mer2Plat_v40.merNotifyResData(responseMap);
		responseMetaStr = "<html><head><META NAME=\"MobilePayPlatform\" CONTENT=\""
				+ responseMetaStr + "\" /></head></html>";
		try {
			write2Response(response, responseMetaStr);
		} catch (IOException e) {
			logger.error("订单号：" + orderId + "--异步反馈，更新订单支付平台交易流水号失败！", e);
			return;
		}
	}
}
