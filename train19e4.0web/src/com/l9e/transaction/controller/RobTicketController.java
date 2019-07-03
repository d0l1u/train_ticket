package com.l9e.transaction.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.jiexun.iface.bean.DeliverBean;
import com.jiexun.iface.bean.OrderBean;
import com.jiexun.iface.bean.RefundBean;
import com.jiexun.iface.util.ASPUtil;
import com.l9e.common.BaseController;
import com.l9e.common.LoginUserInfo;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.pubsub.RobRefund;
import com.l9e.transaction.service.CommonService;
import com.l9e.transaction.service.JoinUsService;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.service.RobTicketService;
import com.l9e.transaction.service.UserInfoService;
import com.l9e.transaction.vo.BookDetailInfo;
import com.l9e.transaction.vo.DBStudentInfo;
import com.l9e.transaction.vo.OrderInfoBx;
import com.l9e.transaction.vo.OrderInfoPs;
import com.l9e.transaction.vo.ProductVo;
import com.l9e.transaction.vo.RobTicketFormVo;
import com.l9e.transaction.vo.RobTicketVo;
import com.l9e.transaction.vo.RobTicket_CP;
import com.l9e.transaction.vo.RobTicket_OI;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.JedisUtil;
import com.l9e.util.MemcachedUtil;
import com.l9e.util.PageUtil;
import com.l9e.util.RobTicketUtils;
import com.sun.xml.internal.ws.api.addressing.AddressingVersion.EPR;

@Controller
@RequestMapping("/rob")
public class RobTicketController extends BaseController {
	private static final String MEM_ROB_PREFIX = "ROB_FAIL_";

	private static final String EOP_ORDER_URL = "eop_order_url";

	private static final String ORDER_INFO = "orderInfo";

	private static final String AGENT_ID = "agentId";

	private static final String DBVO = "dbvo";

	private static final String FORM_VO = "formVo";

	private static final Logger logger = Logger.getLogger(RobTicketController.class);

	/*
	 * 支付重试的 缓存时间
	 */
	private static final long EXPIRE_TIME = 30 * 1000 * 60;

	private static final int PAGE_SIZE = 10;

	@Resource
	private NewBuyTicketController newBuyTicketController;

	@Resource
	private SoukdBuyTicketController soukd;

	@Resource
	private JoinUsService joinUsService;

	@Resource
	private UserInfoService userInfoService;

	@Resource
	private CommonService commonService;

	@Resource
	private OrderService orderService;

	@Resource
	private RobTicketService robTicketService;
	@Resource
	private RobRefund robRefund;

	@Value("#{propertiesReader[ASP_ID]}")
	private String asp_id;

	@Value("#{propertiesReader[ASP_BIZ_ID]}")
	private String asp_biz_id;// 业务id

	@Value("#{propertiesReader[ASP_PRODUCT_ID]}")
	private String asp_product_id;// 商品ID

	@Value("#{propertiesReader[ASP_VERIFY_KEY]}")
	private String asp_verify_key;// 验签key

	@Value("#{propertiesReader[PAY_RESULT_BACK_NOTIFY_URL]}")
	private String pay_result_back_notify_url;// 支付结果后台通知地址

	@Value("#{propertiesReader[PAY_RESULT_FRONT_NOTIFY_URL]}")
	private String pay_result_front_notify_url;// 支付结果前台页面地址

	@Value("#{propertiesReader[ORDER_DETAIL_URL]}")
	private String order_detail_url;// 订单详情连接地址

	@Value("#{propertiesReader[REFUND_RESULT_NOTIFY_URL]}")
	private String refund_result_notify_url;// 退款完成通知地址

	@Value("#{propertiesReader[query_left_ticket_url]}")
	protected String query_left_ticket_url;// 12306新接口

	@RequestMapping("/robTickect.jhtml")
	public String robTicket(HttpServletRequest request, HttpServletResponse response) {
		try {
			logger.info("进入抢票");
			String travelTime = this.getParam(request, "travelTime");
			String trainCode = this.getParam(request, "trainCode");
			String startCity = "";
			String endCity = "";
			String seatMsg = "";

			startCity = URLDecoder.decode(this.getParam(request, "startCity"), "UTF-8");
			endCity = URLDecoder.decode(this.getParam(request, "endCity"), "UTF-8");
			seatMsg = URLDecoder.decode(this.getParam(request, "seatMsg"), "UTF-8");

			String startTime = this.getParam(request, "startTime");
			String endTime = this.getParam(request, "endTime");
			String defaultSelect = this.getParam(request, "defaultSelect");

			LinkedList<Map<String, String>> seatInfoList = new LinkedList<Map<String, String>>();
			Map<String, String> seatPrizeMap = new HashMap<String, String>();// 座席与价格映射

			Map<String, String> seatMap = new HashMap<String, String>();
			seatMap.put("cc", trainCode);
			seatMap.put("fz", startCity);
			seatMap.put("dz", endCity);
			// 解析座位信息
			logger.info("进入抢票--开始解析 车次座位信息--" + seatMap + "--" + seatMsg + "--" + seatInfoList);
			RobTicketUtils.deSeatMsg(seatInfoList, seatMsg, seatPrizeMap, defaultSelect, seatMap, commonService);
			logger.info("进入抢票--解析座位信息成功");
			int amount = Integer.valueOf(this.getSysSettingValue("queuing_tickets_amount", "queuing_tickets_amount"));
			Random random = new Random();
			int wait_amount = amount + random.nextInt(10);
			String hours = this.getSysSettingValue("queuing_tickets_time", "queuing_tickets_time");

			List<ProductVo> productList = null;

			String book_day_num = commonService.querySysSettingByKey("book_day_num");
			int i = Integer.parseInt(book_day_num);
			int j = i - 1;
			Date canBookDate = DateUtil.dateAddDays(new Date(), j);
			Date travel_time = DateUtil.stringToDate(travelTime, DateUtil.DATE_FMT1);// 出发日期

			request.setAttribute("travelTime", travelTime);
			request.setAttribute("trainCode", trainCode);
			request.setAttribute("trainTypeCn", this.getTrainTypeCn(trainCode));
			request.setAttribute("startCity", startCity);
			request.setAttribute("endCity", endCity);
			request.setAttribute("startTime", startTime);
			request.setAttribute("endTime", endTime);
			request.setAttribute("seatInfoList", seatInfoList);
			request.setAttribute("seatPrizeMapper", JSONObject.fromObject(seatPrizeMap).toString());
			request.setAttribute("productList", productList);
			request.setAttribute("wait_amount", wait_amount);
			request.setAttribute("wait_time", hours);
			String ps_order_status = this.getTrainSysSettingValue("ps_order_status", "ps_order_status");
			request.setAttribute("ps_order_status", ps_order_status);
			logger.info("进入抢票--抢票逻辑结束");
			return "rob/robInfoInput";
		} catch (Exception e) {
			logger.error("进入抢票--业务异常-->" + e.toString());
			String errMsg = "<span style='line-height:40px;'>您当前抢票车次,申请人数过多,请稍后重试或更换其他车次</span>";
			try {
				errMsg = URLEncoder.encode(errMsg, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			return "redirect:/common/goToErrPage.jhtml?errMsg=" + errMsg;
		}

	}

	/**
	 * 抢票预订单生成,确认并且付款
	 * 
	 * @param bookInfo
	 * @param request
	 * @param response
	 * @return
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/createRobOrder.jhtml")
	public String createRobOrder(RobTicketFormVo formVo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("抢票前台表单数据--" + JSON.toJSONString(formVo));
		// throw new RuntimeException("测试用");
		RobTicketVo dbvo = new RobTicketVo();
		// 支付逻辑, fromvo ---> dbvo
		Map<String, String> payResult = pay(formVo, request, response, dbvo);
		if (payResult == null) {
			logger.error("支付其他错误");
			// 跳转 列表页 重新支付
			return "redirect:/rob/robOrderList.jhtml";
		} else {
			// 用户session超时 或 登出
			if (payResult.containsKey("AGENT_LOGIN_OUT")) {
				return "common/sessionTimeOut";
			}
		}
		// 跳转页面
		Map parameterMap = request.getParameterMap();
		Set<Entry<String, String>> entrySet = parameterMap.entrySet();
		for (Entry<String, String> entry : entrySet) {
			String key = entry.getKey();
			String value = getParam(request, key);
			request.setAttribute(key, value.toString());
		}
		request.setAttribute("trainTypeCn", RobTicketUtils.getTrainTypeCn(formVo.getTrain_no()));
		request.setAttribute("max12306Price", getParam(request, "max12306Price"));
		request.setAttribute("detailList", formVo.getBookDetailInfoList());
		request.setAttribute("ticketTypeMap", TrainConsts.getTicketType());
		request.setAttribute("idsTypeMap", TrainConsts.getIdsType());
		request.setAttribute("totalPay", formVo.getTotalPay());
		request.setAttribute("pay_url", payResult.get("pay_url"));// 支付地址
		return "rob/rob_confirm_pay";
	}

	/**
	 * 支付逻辑
	 * 
	 * @param formVo
	 * @param request
	 * @param response
	 * @param dbvo
	 * @return
	 */
	private Map<String, String> pay(RobTicketFormVo formVo, HttpServletRequest request, HttpServletResponse response,
			RobTicketVo dbvo) {
		logger.info("进入下单");
		// 获取用户信息
		LoginUserInfo loginUser = this.getLoginUser(request);
		// 测试环境 !
		loginUser = RobTicketUtils.getTestUser(loginUser);
		String provinceId = loginUser.getProvinceId();
		String cityId = loginUser.getCityId();
		String agentId = loginUser.getAgentId();
		String agentName = loginUser.getAgentName();
		String eop_order_url = loginUser.getEop_order_url();// eop下单地址

		// hc_rob_orderinfo 封装
		RobTicket_OI orderInfo = dbvo.getOi();
		orderInfo.setAccountId(agentId);
		/*
		 * orderInfo.setAt_province_id(provinceId);
		 * orderInfo.setAt_city_id(cityId); orderInfo.setDealer_id(agentId);
		 * orderInfo.setDealer_name(agentName);
		 */

		OrderInfoPs orderInfoPs = new OrderInfoPs();
		List<OrderInfoBx> orderInfoBxList = new ArrayList<OrderInfoBx>();

		// zuoyuxing 2013-12-02
		String product_id = this.getParam(request, "product_id");
		logger.info("product_id：" + product_id);
		for (BookDetailInfo detailInfo : formVo.getBookDetailInfoList()) {
			detailInfo.setProduct_id(product_id);
		}
		this.groupData(formVo, orderInfo, orderInfoPs, orderInfoBxList, request, dbvo);// 组合封装数据

		// 数据入库
		try {
			robTicketService.insertOrderAndTickets(dbvo.getOi(), dbvo.getCps());
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, String> pushOrder2EOP = pushOrder2EOP(formVo, dbvo, agentId, eop_order_url, orderInfo);

		return pushOrder2EOP;
	}

	/**
	 * @param formVo
	 * @param dbvo
	 * @param agentId
	 * @param eop_order_url
	 * @param orderInfo
	 */
	private Map<String, String> pushOrder2EOP(RobTicketFormVo formVo, RobTicketVo dbvo, String agentId,
			String eop_order_url, RobTicket_OI orderInfo) {
		// 向EOP下单
		OrderBean bean = new OrderBean();
		bean.setAsp_verify_key(asp_verify_key);
		bean.setPartner_id(asp_id);
		bean.setAgent_id(agentId);
		bean.setAsp_order_id(orderInfo.getOrderId());
		bean.setOrder_name("火车票");
		bean.setBiz_id(asp_biz_id);
		bean.setGoods_id(asp_product_id);
		bean.setGoods_name("火车票 " + orderInfo.getFromCity() + "-" + orderInfo.getToCity() + " " + orderInfo.getTrainNo()
				+ " " + orderInfo.getTravelTime());
		bean.setBuy_cnt(String.valueOf(dbvo.getCps().size()));// eop商品数量
		bean.setGoods_price(formVo.getSingleSVIP_Price().add(formVo.getMax12306Price()).toString());// eop商品单价
		bean.setOrder_price(orderInfo.getPayMoney().toString());// eop订单售价
		bean.setFace_value(orderInfo.getPayMoney().toString());// 面值（用于打印小票）
		// 代理商分钱
		bean.setAgent_get_monney(String.valueOf("0.00"));// 网点分润
		bean.setOrder_keywords(orderInfo.getTrainNo());
		// logger.info("eop商品单价"+orderInfo.getPay_money()+"0");//eop商品单价254.0

		// 支付结果通知地址
		StringBuffer payResultNotifyUrl = new StringBuffer();
		payResultNotifyUrl.append(pay_result_back_notify_url).append("?asp_order_id=").append(orderInfo.getOrderId())
				.append("&asp_order_type=").append("hc");

		// 支付结果前台页面地址
		StringBuffer dealResultUrl = new StringBuffer();
		dealResultUrl.append(pay_result_front_notify_url).append("?asp_order_id=").append(orderInfo.getOrderId())
				.append("&asp_order_type=").append("hc");

		// 订单详情连接地址
		StringBuffer orderDetailUrl = new StringBuffer();
		orderDetailUrl.append(order_detail_url).append("?order_id=").append(orderInfo.getOrderId())
				.append("&asp_order_type=").append("hc");

		StringBuffer refundResultNotifyUrl = new StringBuffer();
		refundResultNotifyUrl.append(refund_result_notify_url).append("?asp_order_type=").append("hc");

		bean.setPay_result_notify_url(payResultNotifyUrl.toString());// 支付结果通知地址
		bean.setDeal_result_url(dealResultUrl.toString());// 支付结果前台页面地址
		bean.setOrder_detail_url(orderDetailUrl.toString());// //订单详情连接地址
		bean.setAsp_refund_url(refundResultNotifyUrl.toString());

		StringBuffer eopOrderUrl = new StringBuffer();
		eopOrderUrl.append(eop_order_url)
				.append((eop_order_url.indexOf("?") != -1) ? "?data_type=JSON" : "&data_type=JSON");
		ASPUtil.createOrder(bean, eopOrderUrl.toString());
		System.out.println("支付地址2：" + bean.getPay_url());
		if (StringUtils.isEmpty(bean.getResult_code())) {// 状态码为空
			logger.info("【下单接口】EOP返回的接口码为空，下单失败！" + "ASP订单号" + orderInfo.getOrderId());
		} else if ("SUCCESS".equalsIgnoreCase(bean.getResult_code())) {// 下单成功
			logger.info("【下单接口】EOP下单成功，EOP订单号" + bean.getEop_order_id());
			Map<String, String> eopInfo = new HashMap<String, String>();
			eopInfo.put("eop_order_id", bean.getEop_order_id());// EOP订单号
			eopInfo.put("pay_url", bean.getPay_url());// 支付地址
			eopInfo.put("query_result_url", bean.getQuery_result_url());// 支付结果查询地址
			eopInfo.put("asp_order_id", orderInfo.getOrderId());// ASP订单号
			logger.info("抢票-存入 hc_order_eop数据" + JSON.toJSONString(eopInfo));
			commonService.addOrderEopInfo(eopInfo);// 添加EOP下单信息
			return eopInfo;
		} else if ("AGENT_LOGIN_OUT".equalsIgnoreCase(bean.getResult_code())) {// 代理商登录失效
			logger.info("【下单接口】代理商登录失效，下单失败！" + "ASP订单号" + orderInfo.getOrderId());
			Map<String, String> errMap = new HashMap<String, String>();
			errMap.put("AGENT_LOGIN_OUT", "AGENT_LOGIN_OUT");// 代理商登录失效
			cacheRobOrderAndEOP(formVo, dbvo, agentId, eop_order_url, orderInfo);
			return errMap;

		} else {// 其他
			logger.info("【下单接口】错误码为" + bean.getResult_code() + "，下单失败！" + "ASP订单号" + orderInfo.getOrderId());
		}

		cacheRobOrderAndEOP(formVo, dbvo, agentId, eop_order_url, orderInfo);
		return null;
	}

	/**
	 * @param formVo
	 * @param dbvo
	 * @param agentId
	 * @param eop_order_url
	 * @param orderInfo
	 */
	private void cacheRobOrderAndEOP(RobTicketFormVo formVo, RobTicketVo dbvo, String agentId, String eop_order_url,
			RobTicket_OI orderInfo) {
		if (MemcachedUtil.getInstance().getAttribute(orderInfo.getOrderId()) != null) {
			return;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(FORM_VO, formVo);
		map.put(DBVO, dbvo);
		map.put(AGENT_ID, agentId);
		map.put(ORDER_INFO, orderInfo);
		map.put(EOP_ORDER_URL, eop_order_url);
		MemcachedUtil.getInstance().setAttribute(MEM_ROB_PREFIX + orderInfo.getOrderId(), map, EXPIRE_TIME);
	}

	/**
	 * 组合封装数据
	 * 
	 * @param bookInfo
	 * @param orderInfo
	 * @param orderInfoCpList
	 * @param orderInfoBxList
	 */
	private void groupData(RobTicketFormVo formVo, RobTicket_OI orderInfo, OrderInfoPs orderInfoPs,
			List<OrderInfoBx> orderInfoBxList, HttpServletRequest request, RobTicketVo dbvo) {
		String order_id = CreateIDUtil.createID("HC_ROB");
		// 订单
		orderInfo.setOrderId(order_id);
		RobTicketUtils.formVo2DBVO(formVo, dbvo);
	}

	@ResponseBody
	@RequestMapping("/passKey.jhtml")
	public String passKey(HttpServletRequest request, HttpServletResponse response) {
		String parameter = request.getParameter("key");
		if (StringUtils.isEmpty(parameter)) {

		} else {
			if (parameter.equals("19erob")) {
				return "pass";
			}
		}

		return "noWay";

	}

	/**
	 * 抢票订单页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/robOrderList.jhtml")
	public String robOrderList(HttpServletRequest request, HttpServletResponse response) {
		String pageIndex = this.getParam(request, "pageIndex");
		if (StringUtils.isEmpty(pageIndex)) {
			pageIndex = "0";
		}
		int page = Integer.parseInt(pageIndex);
		if (page > 0) {
			page = (page - 1) * PAGE_SIZE;
		}

		Map<String, String> request2Map = RobTicketUtils.request2Map(request);
		LoginUserInfo loginUser = getLoginUser(request);
		// 测试用
		loginUser = RobTicketUtils.getTestUser(loginUser);
		request2Map.put("agent_id", loginUser.getAgentId());

		Map<String, String> statusSum = null;
		try {
			statusSum = robTicketService.selectOrderStatusNum((HashMap<String, String>) request2Map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<RobTicket_OI> ois = null;
		int count = 0;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.putAll(request2Map);
			map.put("page", page);
			map.put("pageSize", PAGE_SIZE);
			count = robTicketService.selectOrderInfoByConditionsCount(request2Map);
			ois = robTicketService.selectOrderInfoByConditions(map);
			PageUtil.getInstance().paging(request, PAGE_SIZE, count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		request.setAttribute("orderList", ois);
		request.setAttribute("selectType", request2Map.get("selectType"));
		request.setAttribute("oneMonthOrder", request2Map.get("oneMonthOrder"));
		request.setAttribute("createTime", request2Map.get("createTime"));
		request.setAttribute("orderId", request2Map.get("orderId"));
		request.setAttribute("outTicketBillno", request2Map.get("outTicketBillno"));
		// 分页显示
		request.setAttribute("isShowList", 1);
		request.setAttribute("orderStatusMap", RobTicketUtils.getOrderStatusMap());
		request.setAttribute("status", statusSum);

		return "rob/robOrderList";
	}

	/**
	 * 抢票订单页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryOrderDetail.jhtml")
	public String queryOrderDetail(HttpServletRequest request, HttpServletResponse response) {
		String orderId = getParam(request, "orderId");
		RobTicket_OI oi = new RobTicket_OI();
		oi.setOrderId(orderId);
		List<RobTicket_CP> selectCps = null;
		try {
			selectCps = robTicketService.selectCps(oi);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			oi = robTicketService.selectOrderInfo(oi);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<BookDetailInfo> infos = new ArrayList<BookDetailInfo>();
		for (RobTicket_CP robTicketCP : selectCps) {
			BookDetailInfo info = new BookDetailInfo();
			info.setUser_name(robTicketCP.getUserName());
			info.setUser_ids(robTicketCP.getCertNo());
			info.setIds_type(robTicketCP.getCertType());
			info.setSale_price(robTicketCP.getBuyMoney());
			info.setTicket_type(robTicketCP.getTicketType().toString());
			infos.add(info);
		}
		HashMap<String, String> seatMap = RobTicketUtils.getCtripSeatMapReverse();
		String string = seatMap.get(oi.getSeatType());
		Map<String, String> ch_price_map = RobTicketUtils.getChinese_Price_SeatMap(oi, commonService);
		request.setAttribute("seatType", string);
		request.setAttribute("Seats", ch_price_map);
		request.setAttribute("order", oi);
		request.setAttribute("trainTypeCn", RobTicketUtils.getTrainTypeCn(oi.getTrainNo()));
		request.setAttribute("max12306Price", selectCps.get(0).getBuyMoney());
		request.setAttribute("detailList", infos);
		request.setAttribute("ticketTypeMap", TrainConsts.getTicketType());
		request.setAttribute("idsTypeMap", TrainConsts.getIdsType());
		request.setAttribute("totalPay", oi.getPayMoney());
		request.setAttribute("svip_price", oi.getBuyMoneyExtSum().toString());
		request.setAttribute("fromTime", DateUtil.dateToString(oi.getFromTime(), "HH:mm"));
		request.setAttribute("toTime", DateUtil.dateToString(oi.getToTime(), "HH:mm"));
		return "rob/robOrderDetail";
	}

	/**
	 * 支付结果响应
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/payResultAction.jhtml")
	public String payResultAction(HttpServletRequest request, HttpServletResponse response) {
		return "redirect:/rob/robOrderList.jhtml";
	}

	@RequestMapping("/payAgain.jhtml")
	public String payRobOrder(HttpServletRequest request, HttpServletResponse response) {
		String orderId = getParam(request, "orderId");
		RobTicket_OI oi = new RobTicket_OI();
		oi.setOrderId(orderId);
		List<RobTicket_CP> selectCps = null;
		try {
			selectCps = robTicketService.selectCps(oi);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			oi = robTicketService.selectOrderInfo(oi);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<BookDetailInfo> infos = new ArrayList<BookDetailInfo>();
		for (RobTicket_CP robTicketCP : selectCps) {
			BookDetailInfo info = new BookDetailInfo();
			info.setUser_name(robTicketCP.getUserName());
			info.setUser_ids(robTicketCP.getCertNo());
			info.setIds_type(robTicketCP.getCertType());
			info.setSale_price(robTicketCP.getBuyMoney());
			info.setTicket_type(robTicketCP.getTicketType().toString());
			infos.add(info);
		}
		HashMap<String, String> seatMap = RobTicketUtils.getCtripSeatMapReverse();
		String string = seatMap.get(oi.getSeatType());
		Map<String, String> ch_price_map = RobTicketUtils.getChinese_Price_SeatMap(oi, commonService);
		request.setAttribute("seatType", string);
		request.setAttribute("Seats", ch_price_map);
		request.setAttribute("order", oi);
		request.setAttribute("isRePay", "yes");
		request.setAttribute("travelTime", oi.getTravelTime());
		request.setAttribute("link_name", oi.getContactPerson());
		request.setAttribute("link_phone", oi.getContactPhone());
		request.setAttribute("trainTypeCn", RobTicketUtils.getTrainTypeCn(oi.getTrainNo()));
		request.setAttribute("max12306Price", selectCps.get(0).getBuyMoney());
		request.setAttribute("detailList", infos);
		request.setAttribute("ticketTypeMap", TrainConsts.getTicketType());
		request.setAttribute("idsTypeMap", TrainConsts.getIdsType());
		request.setAttribute("totalPay", oi.getPayMoney());
		request.setAttribute("svip_price", oi.getBuyMoneyExtSum().toString());
		request.setAttribute("fromTime", DateUtil.dateToString(oi.getFromTime(), "HH:mm"));
		request.setAttribute("toTime", DateUtil.dateToString(oi.getToTime(), "HH:mm"));
		Map<String, String> queryEopInfo = commonService.queryEopInfo(orderId);
		// EOP 下单失败.
		if (queryEopInfo == null) {
			Map mapping = (Map) MemcachedUtil.getInstance().getAttribute(MEM_ROB_PREFIX + orderId);
			String agent_id = (String) mapping.get(AGENT_ID);
			RobTicketVo dbvo = (RobTicketVo) mapping.get(DBVO);
			RobTicketFormVo formVo = (RobTicketFormVo) mapping.get(FORM_VO);
			String eop_url = (String) mapping.get(EOP_ORDER_URL);
			RobTicket_OI orderInfo = (RobTicket_OI) mapping.get(ORDER_INFO);
			Map<String, String> resultMap = pushOrder2EOP(formVo, dbvo, agent_id, eop_url, orderInfo);
			if (resultMap == null) {
				logger.error("抢票模块 再次向EOP 下单 失败 --单号 " + orderInfo.getOrderId());
				throw new RuntimeException("抢票模块 再次向EOP 下单 失败 --单号 " + orderInfo.getOrderId());
			}

			// 用户session超时 或 登出
			if (resultMap.containsKey("AGENT_LOGIN_OUT")) {
				return "common/sessionTimeOut";
			}

			request.setAttribute("pay_url", resultMap.get("pay_url"));

		} else {
			request.setAttribute("pay_url", queryEopInfo.get("pay_url"));
		}
		return "rob/rob_confirm_pay";

	}

	/**
	 * 抢票 取消
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/cancel.jhtml")
	public void cancel(HttpServletRequest request, HttpServletResponse response) {

		String orderId = request.getParameter("orderId");
		String result = RobTicketUtils.cancelRefund(orderId, RobTicketVo.CANCEL_TYPE);
		if (StringUtils.isEmpty(result)) {
			write2Response(response, "取消操作异常,请稍后重试!");
			return;
		}

		if (result.indexOf("success") != -1) {
			RobTicket_OI oi = new RobTicket_OI();
			oi.setOrderId(orderId);
			oi.setOrderStatus(RobTicketVo.OI_ORDER_STATUS_CANCELING);
			try {
				robTicketService.updateOrderInfo(oi);
			} catch (Exception e) {
				e.printStackTrace();
			}
			write2Response(response, "success");
		} else {
			write2Response(response, "取消操作异常,请稍后重试!");
		}

	}

	/**
	 * 抢票 退款确认页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/refundDetail.jhtml")
	public String refundConfirm(HttpServletRequest request, HttpServletResponse response) {
		String orderId = getParam(request, "orderId");
		RobTicket_OI oi = new RobTicket_OI();
		oi.setOrderId(orderId);
		List<RobTicket_CP> selectCps = null;
		try {
			selectCps = robTicketService.selectCps(oi);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			oi = robTicketService.selectOrderInfo(oi);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<BookDetailInfo> infos = new ArrayList<BookDetailInfo>();
		for (RobTicket_CP robTicketCP : selectCps) {
			BookDetailInfo info = new BookDetailInfo();
			info.setCp_id(robTicketCP.getCpId());
			info.setUser_name(robTicketCP.getUserName());
			info.setUser_ids(robTicketCP.getCertNo());
			info.setIds_type(robTicketCP.getCertType());
			info.setSale_price(robTicketCP.getBuyMoney());
			info.setTicket_type(robTicketCP.getTicketType().toString());
			info.setOrderTicketPrice(robTicketCP.getOrderTicketPrice());
			info.setOrderTicketSeat(robTicketCP.getOrderTicketSeat());
			info.setRefund_status(robTicketCP.getRefundStatus());
			info.setSeat_no(robTicketCP.getSeatNo());
			infos.add(info);

		}
		HashMap<String, String> seatMap = RobTicketUtils.getCtripSeatMapReverse();
		String string = seatMap.get(oi.getSeatType());
		Map<String, String> ch_price_map = RobTicketUtils.getChinese_Price_SeatMap(oi, commonService);
		request.setAttribute("seatType", string);
		request.setAttribute("Seats", ch_price_map);
		request.setAttribute("order", oi);
		HashMap<String, String> ctripSeatMapReverse = RobTicketUtils.getCtripSeatMapReverse();
		request.setAttribute("seatTypeMap", ctripSeatMapReverse);
		HashMap<String, String> orderStatusMap = RobTicketUtils.getOrderStatusMap();
		request.setAttribute("orderStatusMap", orderStatusMap);
		request.setAttribute("trainTypeCn", RobTicketUtils.getTrainTypeCn(oi.getTrainNo()));
		request.setAttribute("max12306Price", selectCps.get(0).getBuyMoney());
		request.setAttribute("detailList", infos);
		request.setAttribute("ticketTypeMap", TrainConsts.getTicketType());
		request.setAttribute("idsTypeMap", TrainConsts.getIdsType());
		request.setAttribute("totalPay", oi.getPayMoney());
		request.setAttribute("svip_price", oi.getBuyMoneyExtSum().toString());
		request.setAttribute("fromTime", DateUtil.dateToString(oi.getFromTime(), "HH:mm"));
		request.setAttribute("toTime", DateUtil.dateToString(oi.getToTime(), "HH:mm"));

		return "rob/robRefund";
	}

	/**
	 * 抢票 退款 调用接口
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/refund.jhtml")
	public String refund(HttpServletRequest request, HttpServletResponse response) {
		// 已经有退款过了
		Object attribute = MemcachedUtil.getInstance().getAttribute("refund_eop_" + request.getParameter("order_id"));
		if (attribute != null) {
			String errMsg = "<span style='line-height:40px;'>您的退票申请系统正在处理中,请您耐心等候</span>";
			try {
				errMsg = URLEncoder.encode(errMsg, "UTF-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "redirect:/common/goToErrPage.jhtml?errMsg=" + errMsg;
		}
		// 早7:00～晚23:00
		Date currentDate = new Date();
		String datePre = DateUtil.dateToString(currentDate, "yyyyMMdd");
		Date begin = DateUtil.stringToDate(datePre + "070000", "yyyyMMddHHmmss");// 7:00
		Date end = DateUtil.stringToDate(datePre + "224500", "yyyyMMddHHmmss");// 23:00
		if (currentDate.before(begin) || currentDate.after(end)) {
			logger.info("不受理该退款，发起时间为：" + DateUtil.dateToString(currentDate, "yyyy-MM-dd HH:mm:ss"));
			String errMsg = "<span style='line-height:40px;'>每日受理退票时间为早7:00～晚23:00，请见谅。<br />如有特殊情况请先到车站退票，第二日持车站退款小票到代理商处退款。</span>";
			try {
				errMsg = URLEncoder.encode(errMsg, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return "redirect:/common/goToErrPage.jhtml?errMsg=" + errMsg;
		}
		// 携程交互
		String orderId = request.getParameter("order_id");
		RobTicket_OI oi = new RobTicket_OI();
		oi.setOrderId(orderId);
		oi.setOrderStatus(RobTicketVo.OI_ORDER_STATUS_REFUNDING);
		oi.setRefundTime(new Date());
		try {
			robTicketService.updateFrontAndBack(oi);
			robTicketService.insertJLHistory(orderId, "抢票-用户申请退票", "19e前台");
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("{抢票订单退款申请已经完成}==>" + orderId);

		String cp_id_str = this.getParam(request, "cp_id_str");
		String[] cpids = cp_id_str.split(",");
		robTicketService.updateFrontBackCP_Refund(cpids, RobTicket_CP.REFUNDING);
		Jedis jedis = JedisUtil.getJedis();
		try {
			logger.info("抢票退票存入Redis[开始]--"+orderId);
			for (int i = 0; i < cpids.length; i++) {
				jedis.lpush("ROB_TICKET_RETURN", cpids[i]+"##"+orderId);
			}
			logger.info("抢票退票存入Redis[结束]--"+orderId);
		} catch (Exception e) {
			logger.error("抢票退票存入Redis[异常]--"+orderId+"--"+cpids);
		}finally {
			jedis.close();
		}
		return "redirect:/rob/robOrderList.jhtml?selectType=4";

	}

	@RequestMapping("/deleteOrder.jhtml")
	@ResponseBody
	public String deleteOrder(HttpServletRequest request, HttpServletResponse response) {
		try {
			String orderId = request.getParameter("orderId");
			RobTicket_OI oi = new RobTicket_OI();
			oi.setOrderId(orderId);
			robTicketService.deleteOiAndCps(oi);
			return "success";
		} catch (Exception e) {
			logger.error("抢票 删除订单 失败--异常--" + e);
			return "error";
		}

	}

	@RequestMapping("/callBack.do")
	public String callBack(HttpServletRequest request, HttpServletResponse response) {
		String result = "fail";
		String pubType = "";
		String message = "";
		try {
			request.setCharacterEncoding("UTF-8");
			pubType = request.getParameter("pubType");
			message = request.getParameter("message");
			long begin = System.currentTimeMillis();
			logger.info("19e前台接收携程回调并开始处理逻辑--" + pubType + "--业务数据为--" + message);
			robTicketService.updateWithCtripCallback("", pubType, message);
			long end = System.currentTimeMillis();
			logger.info("19e前台接收携程回调并处理成功--" + pubType + "--业务数据为--" + message + "--耗时--" + (end - begin));
			result = "success";
		} catch (Exception e) {
			logger.error("19e前台接收携程回调,处理异常了--" + pubType + "--业务数据为--" + message);
		}
		return result;
	}

	@RequestMapping("/train_interface/order/manul.do")
	public void pushOrderManul(HttpServletRequest request, HttpServletResponse response) {
		String orderId = "";
		String result = "fail";
		try {
			orderId = getParam(request, "orderId");
			RobTicket_OI oi = new RobTicket_OI();
			oi.setOrderId(orderId);
			String pushRobTicket = RobTicketUtils.pushRobTicket(robTicketService, oi);
			logger.info(pushRobTicket);
			if (StringUtils.isEmpty(pushRobTicket)) {
				result = "网络异常,返回结果为空";
			} else if (pushRobTicket.contains("failure") || pushRobTicket.contains("exception")) {
				result = "train_interface_dev 异常";
			} else {
				result = "success";
			}
		} catch (Exception e) {
			logger.error("手动下单失败了--" + orderId+e.toString());
		}
		write2Response(response, result);
	}
	
	@RequestMapping("/train_interface/order/eop_refund.do")
	public void eop_refund(HttpServletRequest request, HttpServletResponse response) {
		String log_pattern = "抢票手动EOP退款-订单号[%s]-退款金额[%s]-业务类型[%s]-退款状态[%s]";
		String orderId = "";
		String bizType = "";
		String refundMoney = "";
		String result = "fail";
		try {
			orderId = getParam(request, "orderId");
			refundMoney = getParam(request, "refundMoney");
			bizType = getParam(request, "bizType");
			robRefund.refundFromEOP(orderId, refundMoney, bizType);
			String format = String.format(log_pattern, orderId,refundMoney,bizType,"成功");
			robTicketService.insertJLHistory(orderId, format, "man");
			logger.info(format);
			result = "SUCCESS";
		} catch (Exception e) {
			logger.error(String.format(log_pattern, orderId,refundMoney,bizType,"失败"));
		}
		write2Response(response, result);
	}

}
