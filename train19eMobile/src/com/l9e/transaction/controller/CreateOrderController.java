package com.l9e.transaction.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jiexun.iface.bean.OrderBean;
import com.jiexun.iface.util.ASPUtil;
import com.l9e.common.BaseController;
import com.l9e.common.LoginUserInfo;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.vo.BookDetailInfo;
import com.l9e.transaction.vo.BookInfo;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.transaction.vo.OrderInfoBx;
import com.l9e.transaction.vo.OrderInfoCp;
import com.l9e.transaction.vo.OrderInfoPs;
import com.l9e.transaction.vo.ProductVo;
import com.l9e.util.AmountUtil;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.UrlFormatUtil;

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
	
	@Value("#{propertiesReader[ASP_ID]}")
	private String asp_id;
	
	@Value("#{propertiesReader[ASP_BIZ_ID]}")
	private String asp_biz_id;//业务id
	
	@Value("#{propertiesReader[ASP_PRODUCT_ID]}")
	private String asp_product_id;//商品ID
	
	@Value("#{propertiesReader[ASP_VERIFY_KEY]}")
	private String asp_verify_key;//验签key
	
	@Value("#{propertiesReader[PAY_RESULT_BACK_NOTIFY_URL]}")
	private String pay_result_back_notify_url;//支付结果后台通知地址
	
	@Value("#{propertiesReader[PAY_RESULT_FRONT_NOTIFY_URL]}")
	private String pay_result_front_notify_url;//支付结果前台页面地址
	
	@Value("#{propertiesReader[ORDER_DETAIL_URL]}")
	private String order_detail_url;//订单详情连接地址
	
	@Value("#{propertiesReader[REFUND_RESULT_NOTIFY_URL]}")
	private String refund_result_notify_url;//退款完成通知地址

	@Value("#{propertiesReader[passenger_verify_url]}")
	private String passenger_verify_url;//乘客核验接口
	/**
	 * 订购下单
	 * @param bookInfo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/createOrder.jhtml")
	public void createTrainOrder(HttpServletRequest request, HttpServletResponse response) throws Exception{
		logger.info("========= 进入下单 =========");
/*		//9点-21点可以
		Date date = new Date();
		String datePre = DateUtil.dateToString(date, "yyyyMMdd");		
		Date begin = DateUtil.stringToDate(datePre + "090000", "yyyyMMddHHmmss");//8:00
		Date end = DateUtil.stringToDate(datePre + "210000", "yyyyMMddHHmmss");//22:00
		if(date.before(begin) || date.after(end)){
			String errMsg = "由于节前订票高峰，又遇火车票系统调整，临时订票时间修改为早9点-晚21点，带来的不便敬请谅解！";
			errMsg = URLEncoder.encode(errMsg, "UTF-8");
			return "redirect:/common/goToErrPage.jhtml?errMsg="+errMsg;
		}*/
		BookInfo bookInfo = new BookInfo();;
		Map<String, String> bxfpMap = new HashMap<String, String>(5);//保险发票
		
		boolean isPacked = packRequestData(request, bookInfo, bxfpMap);
		if(!isPacked){
			logger.info("输入参数错误或为空！");
			JSONObject returnJson = new JSONObject();
			returnJson.put("return_code", "PARAM_EMPTY");
			returnJson.put("message", "请求的必填参数不能为空！");
			printJson(response,returnJson.toString());
			return;
		}
		
		
		LoginUserInfo loginUser = this.getLoginUser(request);
		String provinceId = loginUser.getProvinceId();
		String cityId = loginUser.getCityId();
		String agentId = loginUser.getAgentId();
		String agentName = loginUser.getAgentName();
		String eop_order_url = loginUser.getEop_order_url();//eop下单地址
		
		//String eop_order_url = this.getParam(request, "eop_order_url");//eop下单地址
		
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setAt_province_id(provinceId);
		orderInfo.setAt_city_id(cityId);
		orderInfo.setDealer_id(agentId);
		orderInfo.setDealer_name(agentName);
		
		OrderInfoPs orderInfoPs = new OrderInfoPs();
		List<OrderInfoCp> orderInfoCpList = new ArrayList<OrderInfoCp>();
		List<OrderInfoBx> orderInfoBxList = new ArrayList<OrderInfoBx>();
		
		this.groupData(bookInfo, orderInfo, orderInfoPs, orderInfoCpList, orderInfoBxList, request);//组合封装数据
		
		//校验乘客核验信息
		List<Map<String, String>> passengers = new ArrayList<Map<String, String>>();
		Map<String, String> passenger = null;
		for(OrderInfoCp cp : orderInfoCpList){
			//{"user_name":"杨超","cert_no":"522222199007112835","cert_type":"2"}
			passenger = new HashMap<String, String>(3);
			passenger.put("user_name", cp.getUser_name());
			passenger.put("cert_no", cp.getUser_ids());
			passenger.put("cert_type", cp.getIds_type());
			passengers.add(passenger);
		}
		

		
		Map<String, String> maps = new HashMap<String,String>();
	    maps.put("data", JSONArray.fromObject(passengers).toString());
	    String reqParams = UrlFormatUtil.CreateUrl("", maps, "", "UTF-8");
	    String result = HttpUtil.sendByPost(passenger_verify_url, reqParams, "UTF-8", 5000, 5000);
	       
	    logger.info("下单调用乘客核验接口返回result=" + result);
	    if(StringUtils.isNotEmpty(result) && result.indexOf("FAIL")!= -1){
	    	//核验未通过
			printJson(response, result.replaceAll("FAIL", "VERIFY_FAIL"));
			return;
	    }
	    
		
		if(bxfpMap!=null && !bxfpMap.isEmpty()){
			bxfpMap.put("order_id", orderInfo.getOrder_id());
		}else{
			bxfpMap = null;
		}

		logger.info("乘客数量:"+orderInfoCpList.size());
		//向订单表插入乘客数量 
		orderInfo.setTicket_num(orderInfoCpList.size());
		//向火车票库下单，返回火车票订单号
		logger.info("向火车票库下单，返回火车票订单号");
		String order_id = orderService.addOrder(orderInfo, orderInfoPs, orderInfoCpList, orderInfoBxList, bxfpMap);
		
		logger.info("[火车票下单]：火车票asp_order_id:" + order_id);
		

		
		String totalPay4Show = getTotalPay4Show(bookInfo, request);
		Double agent_get_monney=Double.valueOf(totalPay4Show)-Double.valueOf(getTotalPay(bookInfo, request));
		
		//向EOP下单
		OrderBean bean = new OrderBean();
		bean.setAsp_verify_key(asp_verify_key);
		bean.setPartner_id(asp_id);
		bean.setAgent_id(agentId);
		bean.setAsp_order_id(order_id);
		bean.setOrder_name("火车票订购订单");
		bean.setBiz_id(asp_biz_id);
		bean.setGoods_id(asp_product_id);
		bean.setGoods_name("火车票（"+order_id+")");
		bean.setBuy_cnt("1");//eop商品数量
		bean.setGoods_price(orderInfo.getPay_money());//eop商品单价
		bean.setOrder_price(totalPay4Show);//eop订单售价
		bean.setFace_value(totalPay4Show);//面值（用于打印小票）
		bean.setAgent_get_monney(agent_get_monney.toString());//网点分润
		
		//支付结果通知地址
		StringBuffer payResultNotifyUrl = new StringBuffer();
		payResultNotifyUrl.append(pay_result_back_notify_url)
						  .append("?asp_order_id=")
						  .append(order_id)
						  .append("&asp_order_type=")
						  .append("hc");
		
		//支付结果前台页面地址
		StringBuffer dealResultUrl = new StringBuffer();
		dealResultUrl.append(pay_result_front_notify_url)
					 .append("?asp_order_id=")
					 .append(order_id)
					 .append("&asp_order_type=")
					 .append("hc");
		
		//订单详情连接地址
		StringBuffer orderDetailUrl = new StringBuffer();
		orderDetailUrl.append(order_detail_url)
					  .append("?order_id=")
					  .append(order_id)
					  .append("&asp_order_type=")
				      .append("hc");
		
		StringBuffer refundResultNotifyUrl = new StringBuffer();
		refundResultNotifyUrl.append(refund_result_notify_url)
							 .append("?asp_order_type=")
							 .append("hc");
		
		bean.setPay_result_notify_url(payResultNotifyUrl.toString());//支付结果通知地址
		bean.setDeal_result_url(dealResultUrl.toString());//支付结果前台页面地址
		bean.setOrder_detail_url(orderDetailUrl.toString());////订单详情连接地址
		bean.setAsp_refund_url(refundResultNotifyUrl.toString());
		
		StringBuffer eopOrderUrl = new StringBuffer();
		eopOrderUrl.append(eop_order_url)
				   .append((eop_order_url.indexOf("?")!=-1) ? "&data_type=JSON" : "?data_type=JSON");
		ASPUtil.createOrder(bean, eopOrderUrl.toString());
		
		if(StringUtils.isEmpty(bean.getResult_code())){//状态码为空
			logger.info("【下单接口】EOP返回的接口码为空，下单失败！" + "ASP订单号" + order_id);
			
		}else if("SUCCESS".equalsIgnoreCase(bean.getResult_code())){//下单成功
			logger.info("【下单接口】EOP下单成功，EOP订单号" + bean.getEop_order_id());
			Map<String, String> eopInfo = new HashMap<String, String>();
			eopInfo.put("eop_order_id", bean.getEop_order_id());//EOP订单号
			eopInfo.put("pay_url", bean.getPay_url());//支付地址
			eopInfo.put("query_result_url", bean.getQuery_result_url());//支付结果查询地址
			eopInfo.put("asp_order_id", order_id);//ASP订单号
			commonService.addOrderEopInfo(eopInfo);//添加EOP下单信息
			
			String orderJson = this.queryOrderInfoJson(order_id, totalPay4Show, bean.getEop_order_id(), bean.getPay_url());
			printJson(response,orderJson);
			
		}else if("AGENT_LOGIN_OUT".equalsIgnoreCase(bean.getResult_code())){//代理商登录失效
			logger.info("【下单接口】代理商登录失效，下单失败！" + "ASP订单号" + order_id);
			logger.info("代理商登录失效，下单失败！");
			JSONObject returnJson = new JSONObject();
			returnJson.put("return_code","AGENT_LOGIN_OUT");
			returnJson.put("message","代理商登录失效，下单失败！");
			printJson(response, returnJson.toString());
			
		}else{//其他
			logger.info("【下单接口】错误码为" + bean.getResult_code() + "，下单失败！" + "ASP订单号" + order_id);
			logger.info("未知错误，下单失败！");
			JSONObject returnJson = new JSONObject();
			returnJson.put("return_code","UNKNOWN_EOP_ERROR");
			returnJson.put("message","未知EOP下单错误，下单失败！");
			printJson(response, returnJson.toString());
		}
		logger.info("========= 下单结束 =========");
	}
	
	/**
	 * 组合请求数据
	 * @param request
	 * @return
	 */
	public boolean packRequestData(HttpServletRequest request, BookInfo bookInfo, Map<String, String> bxfpMap){
		String train_no = this.getParam(request, "train_no");
		String from_station = this.getParam(request, "from_station");
		String arrive_station = this.getParam(request, "arrive_station");
		String from_time = this.getParam(request, "from_time");
		String arrive_time = this.getParam(request, "arrive_time");
		String travel_time = this.getParam(request, "travel_time");
		String link_name = this.getParam(request, "link_name");
		String link_phone = this.getParam(request, "link_phone");
		String danjia = this.getParam(request, "ticket_face_price");
		String wz_ext = this.getParam(request, "wz_ext");
		String ticket_pay_money = this.getParam(request, "ticket_pay_money");
		String bx_pay_money = this.getParam(request, "bx_pay_money");
		String ps_pay_money = this.getParam(request, "ps_pay_money");
		String seat_type = this.getParam(request, "seat_type");
		String out_ticket_type = this.getParam(request, "out_ticket_type");
		String json_passengers = this.getParam(request, "json_passengers");
		String isFpNeed = this.getParam(request, "fpNeed");
		
		if(StringUtils.isEmpty(train_no) || StringUtils.isEmpty(from_station)
				|| StringUtils.isEmpty(arrive_station) || StringUtils.isEmpty(from_time) || StringUtils.isEmpty(arrive_time)
				|| StringUtils.isEmpty(travel_time) || StringUtils.isEmpty(link_name) || StringUtils.isEmpty(link_phone)
				|| StringUtils.isEmpty(danjia) || StringUtils.isEmpty(ticket_pay_money) || StringUtils.isEmpty(wz_ext)
				|| StringUtils.isEmpty(bx_pay_money) || StringUtils.isEmpty(ps_pay_money) || StringUtils.isEmpty(seat_type)
				|| StringUtils.isEmpty(out_ticket_type) || StringUtils.isEmpty(json_passengers)|| StringUtils.isEmpty(isFpNeed)){
			return false;
		}

		String fp_receiver = this.getParam(request, "fp_receiver");
		String fp_phone = this.getParam(request, "fp_phone");
		String fp_zip_code = this.getParam(request, "fp_zip_code");
		String fp_address = this.getParam(request, "fp_address");
		
		if("1".equals(isFpNeed)){
			if(StringUtils.isEmpty(fp_receiver) || StringUtils.isEmpty(fp_phone)
					|| StringUtils.isEmpty(fp_zip_code) || StringUtils.isEmpty(fp_address)){
				return false;
			}else{
				bxfpMap.put("fp_receiver", this.getParam(request, "fp_receiver"));
				bxfpMap.put("fp_phone", this.getParam(request, "fp_phone"));
				bxfpMap.put("fp_zip_code", this.getParam(request, "fp_zip_code"));
				bxfpMap.put("fp_address", this.getParam(request, "fp_address"));
			}

		}

		bookInfo.setTrain_no(train_no);
		bookInfo.setFrom_city(from_station);
		bookInfo.setTo_city(arrive_station);
		bookInfo.setFrom_time(from_time);
		bookInfo.setTo_time(arrive_time);
		bookInfo.setTravelTime(travel_time);
		bookInfo.setLink_name(link_name);
		bookInfo.setLink_phone(link_phone);
		bookInfo.setDanjia(danjia);
//		bookInfo.setWz_ext(wz_ext);
		bookInfo.setWz_ext("0");
		bookInfo.setTicket_pay_money(ticket_pay_money);
		bookInfo.setBx_pay_money(bx_pay_money);
		bookInfo.setPs_pay_money(ps_pay_money);
		bookInfo.setSeat_type(seat_type);
		bookInfo.setOut_ticket_type(out_ticket_type);
		
		//[{"user_name":"","ticket_type":"0","ids_type":"2","user_ids":"","bx_product_id":""}]
		JSONArray jsonArray = JSONArray.fromObject(json_passengers);
		List<BookDetailInfo> bookDetailList = new ArrayList<BookDetailInfo>();
		BookDetailInfo bookDetail = null;
		JSONObject jsobj = null;
		String bx_product_id = null;
		for(int i=0; i<jsonArray.size(); i++){
			bookDetail = new BookDetailInfo();
			jsobj = jsonArray.getJSONObject(i);
			if(null == jsobj.getString("user_name")){
				return false;
			}
			bookDetail.setUser_name(jsobj.getString("user_name"));
			bookDetail.setTicket_type(jsobj.getString("ticket_type"));
			bookDetail.setIds_type(jsobj.getString("ids_type"));
			bookDetail.setUser_ids(jsobj.getString("user_ids"));
			
			bx_product_id = jsobj.getString("bx_product_id");
			if("BX_10".equalsIgnoreCase(bx_product_id)){//10元保险
				bookDetail.setProduct_id(bx_product_id);
				bookDetail.setSale_price("10");
			}else if("BX_0".equalsIgnoreCase(bx_product_id)){//5元保险
				bookDetail.setProduct_id(bx_product_id);
				bookDetail.setSale_price("5");
			}else if("BX_20".equalsIgnoreCase(bx_product_id)){//20元保险
				bookDetail.setProduct_id(bx_product_id);
				bookDetail.setSale_price("20");
			}else{//不购买保险
				bookDetail.setProduct_id(bx_product_id);
				bookDetail.setSale_price("0");
			}
			bookDetailList.add(bookDetail);
		}
		bookInfo.setBookDetailInfoList(bookDetailList);
		
		return true;
	}
	
	/**
	 * 根据订单号查询订单信息(json格式)
	 * @param order_id
	 */
	private String queryOrderInfoJson(String order_id, String totalPay4Show, String eop_order_id, String pay_url) {
		OrderInfo orderInfo = orderService.queryOrderInfo(order_id);
		OrderInfoPs orderInfoPs = orderService.queryOrderInfoPs(order_id);
		List<Map<String, String>> detailList = orderService.queryOrderDetailList(order_id);
		//Map<String, String> eopInfo = commonService.queryEopInfo(order_id);
		
/*		{"arrive_station":"北京","arrive_time":"01:30","bx_pay_money":"40.00",
			"from_station":"上海","from_time":"00:32","merchant_id":"882399475495",
			"merchant_order_id":"xxx","message":"","order_id":"xxx","order_status":"00",
			"pay_money":"62.00","return_code":"000","seat_type":"8","spare_pro1":"","spare_pro2":"",
			"ticket_pay_money":"22.00","train_code":"K589","travel_time":"2014-01-23"}*/
		
		JSONObject returnJson = new JSONObject();
		returnJson.put("return_code", "SUCCESS");
		returnJson.put("message", "下单成功！");
		returnJson.put("order_id", orderInfo.getOrder_id());
		returnJson.put("eop_order_id", eop_order_id);
		returnJson.put("train_no", orderInfo.getTrain_no());
		returnJson.put("trainTypeCn", this.getTrainTypeCn(orderInfo.getTrain_no()));
		returnJson.put("from_station", orderInfo.getFrom_city());
		returnJson.put("arrive_station", orderInfo.getTo_city());
		returnJson.put("from_time", orderInfo.getFrom_time());
		returnJson.put("arrive_time", orderInfo.getTo_time());
		returnJson.put("travel_time", orderInfo.getTravel_time());
		returnJson.put("link_name", orderInfoPs.getLink_name());
		returnJson.put("link_phone", orderInfoPs.getLink_phone());
		
		
		if(orderInfo!=null && !StringUtils.isEmpty(orderInfo.getExt_seat())
				&& orderInfo.getExt_seat().indexOf(TrainConsts.SEAT_9)!=-1){//备选无座
			returnJson.put("wz_ext", "1");
		}else{
			returnJson.put("wz_ext", "0");
		}
		
		returnJson.put("total_pay", AmountUtil.formatMoney(orderInfo.getPay_money()));
		returnJson.put("total_pay_show", AmountUtil.formatMoney(totalPay4Show));
		returnJson.put("ticket_pay_money", AmountUtil.formatMoney(orderInfo.getTicket_pay_money()));
		returnJson.put("bx_pay_money", AmountUtil.formatMoney(orderInfo.getBx_pay_money()));
		returnJson.put("ps_pay_money", AmountUtil.formatMoney(orderInfo.getPs_pay_money()));
		returnJson.put("seat_type", orderInfo.getSeat_type());
		returnJson.put("out_ticket_type", orderInfo.getOut_ticket_type());
		
		JSONArray passengers = new JSONArray();
		JSONObject passenger = null;
		String bx_product_id = null;
		for(Map<String, String> detailMap : detailList){
			passenger = new JSONObject();
			passenger.put("user_name", detailMap.get("user_name"));
			passenger.put("ids_type", detailMap.get("ids_type"));
			passenger.put("user_ids", detailMap.get("user_ids"));
			
			bx_product_id = detailMap.get("product_id");
			if(StringUtils.isEmpty(bx_product_id)){
				passenger.put("bx_product_id", "BX_NO");
				passenger.put("bx_product_name", "不购买保险");
			}else if("BX_0".equalsIgnoreCase(bx_product_id)){//5元保险
				passenger.put("bx_product_id", "BX_0");
				passenger.put("bx_product_name", detailMap.get("bx_name"));
			}else if("BX_10".equalsIgnoreCase(bx_product_id)){//10元保险
				passenger.put("bx_product_id", "BX_10");
				passenger.put("bx_product_name", detailMap.get("bx_name"));
			}else if("BX_20".equalsIgnoreCase(bx_product_id)){//20元保险
				passenger.put("bx_product_id", "BX_20");
				passenger.put("bx_product_name", detailMap.get("bx_name"));
			}else{//不购买保险
				passenger.put("bx_product_id", "BX_NO");
				passenger.put("bx_product_name", "不购买保险");
			}
			
			passengers.add(passenger);
		}
		returnJson.put("passengers", passengers.toString());
		returnJson.put("pay_url", pay_url);
		
		return returnJson.toString();
		
		
		
/*		if(orderInfo!=null && !StringUtils.isEmpty(orderInfo.getExt_seat())
				&& orderInfo.getExt_seat().indexOf(TrainConsts.SEAT_9)!=-1){//备选无座
			request.setAttribute("wz_ext", 1);
		}
		request.setAttribute("orderInfo", orderInfo);
		request.setAttribute("orderInfoPs", orderInfoPs);
		request.setAttribute("detailList", detailList);		
		request.setAttribute("ticketTypeMap", TrainConsts.getTicketType());
		request.setAttribute("seatTypeMap", TrainConsts.getSeatType());
		request.setAttribute("idsTypeMap", TrainConsts.getIdsType());
		request.setAttribute("pay_url", eopInfo.get("pay_url"));//支付地址
		request.setAttribute("trainTypeCn", this.getTrainTypeCn(orderInfo.getTrain_no()));
		request.setAttribute("totalPay4Show", this.getParam(request, "totalPay4Show"));*/

	}

	/**
	 * 组合封装数据
	 * @param bookInfo
	 * @param orderInfo
	 * @param orderInfoCpList
	 * @param orderInfoBxList
	 */
	private void groupData(BookInfo bookInfo, OrderInfo orderInfo, OrderInfoPs orderInfoPs,
			List<OrderInfoCp> orderInfoCpList, List<OrderInfoBx> orderInfoBxList, HttpServletRequest request) {
		String order_id = CreateIDUtil.createID("SJ");
		//订单
		orderInfo.setOrder_id(order_id);
		orderInfo.setOrder_name(bookInfo.getFrom_city() + "/" + bookInfo.getTo_city());
		
		/*
		 * 必须先给主订单表pay_money等金额赋值后再处理明细表(涉及匹配产品的问题)
		 */
		orderInfo.setFrom_city(bookInfo.getFrom_city());
		orderInfo.setTo_city(bookInfo.getTo_city());
		orderInfo.setFrom_time(bookInfo.getFrom_time());
		orderInfo.setTo_time(bookInfo.getTo_time());
		orderInfo.setTrain_no(bookInfo.getTrain_no());
		orderInfo.setTravel_time(bookInfo.getTravelTime());
		if(bookInfo.getBookDetailInfoList().get(0).getProduct_id().equals("BX_0")){
			orderInfo.setServer_pay_money("5");		
			orderInfo.setBx_pay_money("0");
		}else{
			orderInfo.setBx_pay_money(this.getBxTotalPay(bookInfo, request));
		}
		orderInfo.setTicket_pay_money(this.getTicketTotalPay(bookInfo));
		if(TrainConsts.OUT_TICKET_TYPE_PS.equals(bookInfo.getOut_ticket_type())){//配送票
			orderInfo.setPs_pay_money(bookInfo.getPs_pay_money());
		}
		orderInfo.setPay_money(this.getTotalPay(bookInfo, request));
		orderInfo.setOut_ticket_type(bookInfo.getOut_ticket_type());
		orderInfo.setSeat_type(bookInfo.getSeat_type());
		orderInfo.setOrder_status(TrainConsts.PRE_ORDER);//预下单
		
		if(!StringUtils.isEmpty(bookInfo.getSeat_type())
				&& TrainConsts.SEAT_8.equals(bookInfo.getSeat_type()) 
				&& !StringUtils.isEmpty(bookInfo.getWz_ext())
				&& "1".equals(bookInfo.getWz_ext())){//硬座选择无座备选
			String ext_seat = "" + TrainConsts.SEAT_9 + "," + bookInfo.getDanjia();
			orderInfo.setExt_seat(ext_seat);
		}
		
		//配送
		orderInfoPs.setOrder_id(order_id);
		orderInfoPs.setLink_name(bookInfo.getLink_name());
		orderInfoPs.setLink_phone(bookInfo.getLink_phone());
		orderInfoPs.setLink_mail(bookInfo.getLink_mail());
		if(TrainConsts.OUT_TICKET_TYPE_ELEC.equals(bookInfo.getOut_ticket_type())){//电子票
			orderInfoPs.setPs_status(TrainConsts.PS_STATUS_SENDED);//配送完成
			orderInfoPs.setPay_money("0");//支付0
			orderInfoPs.setBuy_money("0");//成本0
		}else{//配送票
			orderInfoPs.setPs_status(TrainConsts.PS_STATUS_WAITING);//等待配送
			orderInfoPs.setLink_address(bookInfo.getLink_address());
		}
		
		for (BookDetailInfo bookDetailInfo : bookInfo.getBookDetailInfoList()) {
			//车票
			OrderInfoCp orderInfoCp = new OrderInfoCp();			
			String cp_id = CreateIDUtil.createID("CP");//车票id
			orderInfoCp.setCp_id(cp_id);
			orderInfoCp.setOrder_id(order_id);			
			orderInfoCp.setPay_money(bookInfo.getDanjia());
			orderInfoCp.setTicket_type(bookDetailInfo.getTicket_type());//车票类型
			orderInfoCp.setUser_name(bookDetailInfo.getUser_name().trim());//姓名
			orderInfoCp.setIds_type(bookDetailInfo.getIds_type());
			orderInfoCp.setUser_ids(bookDetailInfo.getUser_ids().trim());//证件号
			orderInfoCp.setSeat_type(bookInfo.getSeat_type());//坐席
			orderInfoCpList.add(orderInfoCp);
			
			//保险单价大于0则保存该保险记录
			String sale_price = bookDetailInfo.getSale_price();//保险单价
			if(!StringUtils.isEmpty(sale_price) && Double.parseDouble(sale_price) > 0){
				//查询保险渠道 保险渠道: 1、快保 2、合众
				String bx_channel = commonService.querySysSettingByKey("bx_channel");
				if(StringUtils.isEmpty(bx_channel)){
					bx_channel = "1";
				}
				//保险
				OrderInfoBx orderInfoBx = new OrderInfoBx();
				orderInfoBx.setBx_id(CreateIDUtil.createID("BX"));
				orderInfoBx.setOrder_id(order_id);
				orderInfoBx.setCp_id(cp_id);//车票id
				orderInfoBx.setUser_name(bookDetailInfo.getUser_name().trim());//姓名
				orderInfoBx.setIds_type(bookDetailInfo.getIds_type());
				orderInfoBx.setUser_ids(bookDetailInfo.getUser_ids().trim());
				orderInfoBx.setBx_type(changeType(bookDetailInfo.getTicket_type()));
				orderInfoBx.setFrom_name(bookInfo.getFrom_city());//出发城市
				orderInfoBx.setTo_name(bookInfo.getTo_city());//到达城市
				//orderInfoBx.setBx_status("0");//未发送
				orderInfoBx.setProduct_id(bookDetailInfo.getProduct_id());
				//orderInfoBx.setEffect_date(bookInfo.getTravelTime());//生效日期
				orderInfoBx.setEffect_date(bookInfo.getFrom_time()+":00");
				orderInfoBx.setTrain_no(bookInfo.getTrain_no());//车次
				orderInfoBx.setTelephone(bookInfo.getLink_phone());//联系人电话
				if("BX_0".equals(bookDetailInfo.getProduct_id())){
					orderInfoBx.setBx_channel("3");
					orderInfoBx.setPay_money("0");
				}else{
					orderInfoBx.setPay_money(bookDetailInfo.getSale_price());
					orderInfoBx.setBx_channel(bx_channel);
				}
				orderInfoBxList.add(orderInfoBx);
			}
		}
	}
	
	/**
	 * 票价总额
	 * @param bookInfo
	 * @return
	 */
	private String getTicketTotalPay(BookInfo bookInfo){
		String ticketTotalPay = bookInfo.getTicket_pay_money();
		if(StringUtils.isEmpty(ticketTotalPay)){
			ticketTotalPay = String.valueOf(AmountUtil.mul(Double.parseDouble(bookInfo.getDanjia()), bookInfo.getBookDetailInfoList().size()));
			bookInfo.setTicket_pay_money(ticketTotalPay);
		}
		return ticketTotalPay;
	}
	
	/**
	 * 保险总额
	 * @param bookInfo
	 * @return
	 */
	private String getBxTotalPay(BookInfo bookInfo, HttpServletRequest request){
		String bxTotalPay = bookInfo.getBx_pay_money();
		if(StringUtils.isEmpty(bxTotalPay)){//保险为空 则计算保险金额
			LoginUserInfo loginUser = this.getLoginUser(request);
			String provinceId = loginUser.getProvinceId();
			String cityId = loginUser.getCityId();
			//查询产品列表
			ProductVo product = new ProductVo();
			product.setProvince_id(provinceId);
			product.setCity_id(cityId);
			product.setType(TrainConsts.PRODUCT_TYPE_1);//保险
			product.setStatus(TrainConsts.PRODUCT_STATUS_1);//上架
			
			List<ProductVo> productList = commonService.queryProductInfoList(product);
			
			double bx_total_pay = 0;//保险总计
			for(BookDetailInfo detail : bookInfo.getBookDetailInfoList()){
				for(ProductVo pvo : productList){
					if(detail.getProduct_id().equals(pvo.getProduct_id())){
						detail.setBx_name(pvo.getName());//保险名称
						detail.setSale_price(String.valueOf(pvo.getSale_price()));
						bx_total_pay = AmountUtil.add(bx_total_pay, pvo.getSale_price());
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
	 * @param bookInfo
	 * @return
	 */
	private String getTotalPay4Show(BookInfo bookInfo, HttpServletRequest request){
		String totalPay = bookInfo.getPay_money();
		if(StringUtils.isEmpty(totalPay)){//是否有值 保险总价为空导致
			double money = Double.parseDouble(getTicketTotalPay(bookInfo));
			double bxPay = Double.parseDouble(getBxTotalPay(bookInfo, request));//保险支付金额
			if(bxPay > 0){
				money = AmountUtil.add(Double.parseDouble(getTicketTotalPay(bookInfo)),bxPay);
			}
			
			//配送票需要加上配送物流费用
			if(!StringUtils.isEmpty(bookInfo.getOut_ticket_type())
					&& TrainConsts.OUT_TICKET_TYPE_PS.equals(bookInfo.getOut_ticket_type())){
				money = AmountUtil.add(money, Double.parseDouble(bookInfo.getPs_pay_money()));
			}

			totalPay = String.valueOf(money);
			bookInfo.setPay_money(totalPay);
		}
		return totalPay;
	}
	
	/**
	 * 实际支付总金额（抛去保险返利）
	 * @param bookInfo
	 * @param request
	 * @return
	 */
	private String getTotalPay(BookInfo bookInfo, HttpServletRequest request){
		double money = Double.parseDouble(getTotalPay4Show(bookInfo, request));
		double profit = 0;
		//保险中每张保险返给代理商5块钱
		for(BookDetailInfo detail : bookInfo.getBookDetailInfoList()){
			if(!StringUtils.isEmpty(detail.getSale_price())){
				double bx_salePrice = Double.parseDouble(detail.getSale_price());
				if(bx_salePrice == 20){
					profit = AmountUtil.add(profit, 5);
				}else if(bx_salePrice == 10){
					profit = AmountUtil.add(profit, 2);
				}
				/**liuyi02 保险调整 start */
				else if(bx_salePrice == 5){
					profit = AmountUtil.add(profit, 2);
					break;
				}
				/**liuyi02 保险调整 end */
			}
		}
		money = AmountUtil.sub(money, profit);
		return String.valueOf(money);
	}
	
	public String changeType(String str){
		return str.equals("1") ? "0" :"1";
	}
	
	public static void main(String[] args) {
		String str="aaaa";
		System.out.println(str.indexOf("?")!=-1 ? "&data_type=JSON" : "?data_type=JSON");
	}
	
}
