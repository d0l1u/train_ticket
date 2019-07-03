package com.l9e.transaction.controller;

import java.io.IOException;
import java.net.URLDecoder;
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

import com.hisun.iposm.HiiposmUtil;
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
import com.l9e.transaction.vo.WeekDay;
import com.l9e.util.AmountUtil;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.DateUtil;

/**
 * 下单
 * 
 * @author zhangjun
 * 
 */
@Controller
@RequestMapping("/order")
public class CreateOrderController extends BaseController {

	private static final Logger logger = Logger.getLogger(CreateOrderController.class);

	@Resource
	private OrderService orderService;
	

	@Value("#{propertiesReader[merchantId]}")
	private String merchantId;
	
	@Value("#{propertiesReader[signKey]}")
	private String signKey;//验签key
	
	@Value("#{propertiesReader[req_url]}")
	private String req_url;//cmpay请求地址
	
	@Value("#{propertiesReader[characterSet]}")
	private String characterSet;
	
	@Value("#{propertiesReader[callbackUrl]}")
	private String callbackUrl;//cmpay下单同步调用接口地址
	
	@Value("#{propertiesReader[notifyUrl]}")
	private String notifyUrl;//cmpay下单异步调用接口地址
	
	@Value("#{propertiesReader[showUrl]}")
	private String showUrl;//cmpay查看订单信息
	/**
	 * 订购下单
	 * @param bookInfo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/createOrder.jhtml")
	public String createTrainOrder(BookInfo bookInfo,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		logger.info("进入下单");
/*		//9炄1�7-21点可仄1�7
		Date date = new Date();
		String datePre = DateUtil.dateToString(date, "yyyyMMdd");		
		Date begin = DateUtil.stringToDate(datePre + "090000", "yyyyMMddHHmmss");//8:00
		Date end = DateUtil.stringToDate(datePre + "210000", "yyyyMMddHHmmss");//22:00
		if(date.before(begin) || date.after(end)){
			String errMsg = "由于节前订票高峰，又遇火车票系统调整，临时订票时间修改为早9点-晚21点，带来的不便敬请谅解！";
			errMsg = URLEncoder.encode(errMsg, "UTF-8");
			return "redirect:/common/goToErrPage.jhtml?errMsg="+errMsg;
		}*/
		LoginUserInfo loginUser = this.getLoginUser(request);
		String cm_phone = loginUser.getCm_phone();//loginUser.getCm_phone();
		
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setCm_phone(cm_phone);
		
		OrderInfoPs orderInfoPs = new OrderInfoPs();
		List<OrderInfoCp> orderInfoCpList = new ArrayList<OrderInfoCp>();
		List<OrderInfoBx> orderInfoBxList = new ArrayList<OrderInfoBx>();
		System.out.println("测试：：：" + bookInfo.getPay_money()+bookInfo.getBookDetailInfoList());
		
		String product_id = this.getParam(request, "product_id");
		//String product_id = "PT000000001";
		System.out.println(product_id);
		for(BookDetailInfo detailInfo : bookInfo.getBookDetailInfoList()){
			detailInfo.setProduct_id(product_id);
		}
		System.out.println("bookInfo.getBookDetailInfoList():"
				+bookInfo.getBookDetailInfoList().get(0).getUser_name());
		this.groupData(bookInfo, orderInfo, orderInfoPs, orderInfoCpList, orderInfoBxList, request);//组合封装数据
		
		Map<String, String> bxfpMap = null;//保险发票
		if("1".equals(this.getParam(request, "fpNeed"))){
			bxfpMap = new HashMap<String, String>(4);
			bxfpMap.put("order_id", orderInfo.getOrder_id());
			bxfpMap.put("fp_receiver", this.getParam(request, "fp_receiver"));
			bxfpMap.put("fp_phone", this.getParam(request, "fp_phone"));
			bxfpMap.put("fp_zip_code", this.getParam(request, "fp_zip_code"));
			bxfpMap.put("fp_address", this.getParam(request, "fp_address"));
		}

		//向火车票库下单，返回火车票订单号
		String order_id = orderService.addOrder(orderInfo, orderInfoPs, orderInfoCpList, orderInfoBxList, bxfpMap);
		
		logger.info("[火车票下单]：火车票asp_order_id:" + order_id);
		
		String totalPay4Show = getTotalPay4Show(bookInfo, request);
		
		return "redirect:/order/orderComfirm.jhtml?order_id=" + order_id 
				+ "&totalPay4Show=" + totalPay4Show;

	}
	
	/**
	 * 下单重定向查询
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/orderComfirm.jhtml")
	public String orderComfirm(HttpServletRequest request, HttpServletResponse response) {
		String order_id= this.getParam(request, "order_id");
		OrderInfo orderInfo = orderService.queryOrderInfo(order_id);
		OrderInfoPs orderInfoPs = orderService.queryOrderInfoPs(order_id);
		List<Map<String, String>> detailList = orderService.queryOrderDetailList(order_id);
		
		if(orderInfo!=null && !StringUtils.isEmpty(orderInfo.getExt_seat())
				&& orderInfo.getExt_seat().indexOf(TrainConsts.SEAT_9)!=-1){//备选无座
			request.setAttribute("wz_ext", 1);
		}
		String travel_time = orderInfo.getTravel_time();
		int day = DateUtil.getDay(travel_time, "yyyy-MM-dd");
		String dayOfWeek = WeekDay.getDay(day);
		request.setAttribute("day", dayOfWeek);
		request.setAttribute("order_id", order_id);
		request.setAttribute("orderInfo", orderInfo);
		request.setAttribute("orderInfoPs", orderInfoPs);
		request.setAttribute("detailList", detailList);		
		request.setAttribute("ticketTypeMap", TrainConsts.getTicketType());
		request.setAttribute("seatTypeMap", TrainConsts.getSeatType());
		request.setAttribute("idsTypeMap", TrainConsts.getIdsType());
		request.setAttribute("trainTypeCn", this.getTrainTypeCn(orderInfo.getTrain_no()));
		request.setAttribute("totalPay4Show", this.getParam(request, "totalPay4Show"));
		
		return "book/bookConfirm";
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
		String order_id = CreateIDUtil.createID("cmpay");
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
		orderInfo.setBx_pay_money(this.getBxTotalPay(bookInfo, request));
		orderInfo.setTicket_pay_money(this.getTicketTotalPay(bookInfo));
		if(TrainConsts.OUT_TICKET_TYPE_PS.equals(bookInfo.getOut_ticket_type())){//配送票
			orderInfo.setPs_pay_money(bookInfo.getPs_pay_money());
		}
		orderInfo.setPay_money(getTotalPay4Show(bookInfo, request));
		orderInfo.setOut_ticket_type(bookInfo.getOut_ticket_type());
		orderInfo.setSeat_type(bookInfo.getSeat_type());
		orderInfo.setOrder_status(TrainConsts.PRE_ORDER);//预下单
		
		if(!StringUtils.isEmpty(bookInfo.getSeat_type())
				&& (TrainConsts.SEAT_8.equals(bookInfo.getSeat_type()) || TrainConsts.SEAT_2.equals(bookInfo.getSeat_type()) ||TrainConsts.SEAT_3.equals(bookInfo.getSeat_type()))
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
				orderInfoBx.setFrom_name(bookInfo.getFrom_city());//出发城市
				orderInfoBx.setTo_name(bookInfo.getTo_city());//到达城市
				//orderInfoBx.setBx_status("0");//未发送
				orderInfoBx.setPay_money(bookDetailInfo.getSale_price());
				orderInfoBx.setProduct_id(bookDetailInfo.getProduct_id());
				//orderInfoBx.setEffect_date(bookInfo.getTravelTime());//生效日期
				orderInfoBx.setEffect_date(bookInfo.getFrom_time()+":00");
				orderInfoBx.setTrain_no(bookInfo.getTrain_no());//车次
				orderInfoBx.setTelephone(bookInfo.getLink_phone());//联系人电话
				orderInfoBx.setBx_channel(bx_channel);
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
			//查询产品列表
			ProductVo product = new ProductVo();
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
		if(StringUtils.isEmpty(totalPay)){//是否有值保险总价为空导致
			double money = Double.parseDouble(getTicketTotalPay(bookInfo));
			double bxPay = Double.parseDouble(getBxTotalPay(bookInfo, request));//保险支付金额
			if(bxPay > 0){
				money = AmountUtil.add(Double.parseDouble(getTicketTotalPay(bookInfo)),bxPay);
			}
			
			//配�1�7�票霄1�7要加上配送物流费用
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
	 * 湖南移动下单重定向支付页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/orderCmpay.jhtml")
	public void orderCmpay(HttpServletRequest request, HttpServletResponse response) {
		logger.info("跳转支付页面");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		//商户会计日期（自定义）
		String merAcDate = sdf.format(new Date()).substring(0, 8);
		String orderId = this.getParam(request, "order_id");
		OrderInfo orderInfo = orderService.queryOrderInfo(orderId);
		
		//获取用户的IP地址，作为防钓鱼的一种方法
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
		String ipAddress = clientIp;
		String requestId= CreateIDUtil.createID("CM");
		String signType = "MD5";
		String type = "WAPDirectPayConfirm";
		String version = "2.0.0";
		String period = "30";
		String periodUnit = "00";
		
		//金额单位为分
		String amount = String.valueOf((int)(Double.valueOf(orderInfo.getPay_money())*100));
//		String amount = orderInfo.getPay_money();
		String bankAbbr = this.getParam(request, "bankAbbr");
		String currency = "00";
		String orderDate = merAcDate;
		
		String merchantAbbr = "";	//商户展示名称
		String productDesc = "";	//商品描述
		String productId = "";		//商品编号
		String productNum = this.getParam(request, "productNum");
		String productName = "火车票";	//商品名称
		String reserved1 = "";		//保留字段
		String reserved2 = "";		//保留字段
		String userToken = "";		//用户标识
		String showUrl1 = showUrl+"?order_id="+orderId;
		String couponsFlag = "";	//营销工具使用控制
		//-- 签名报文
		StringBuffer signData = new StringBuffer();
		signData.append(characterSet).append(callbackUrl).append(notifyUrl);
		signData.append(ipAddress).append(merchantId).append(requestId);
		signData.append(signType).append(type).append(version);
		signData.append(amount).append(bankAbbr).append(currency);
		signData.append(orderDate).append(orderId).append(merAcDate);
		signData.append(period).append(periodUnit).append(merchantAbbr);
		signData.append(productDesc).append(productId).append(productName);
		signData.append(productNum).append(reserved1).append(reserved2);
		signData.append(userToken).append(showUrl1).append(couponsFlag);
		
		logger.info("订单号："+orderId+"---签名报文："+signData.toString());
		HiiposmUtil util = new HiiposmUtil();
		//数据签名
		String hmac = util.MD5Sign(signData.toString(), signKey);
		logger.info("数据签名："+hmac);
		//-- 请求报文
		StringBuffer buf = new StringBuffer();
		//-- 带上消息摘要
		buf.append("hmac=" + hmac);
		buf.append("&characterSet="+ characterSet);
		buf.append("&callbackUrl="+callbackUrl);
		buf.append("&notifyUrl="+notifyUrl);
		buf.append("&ipAddress="+ipAddress);
		buf.append("&merchantId="+merchantId);
		buf.append("&requestId="+ requestId);
		buf.append("&signType="+ signType);
		buf.append("&type=" + type);
		buf.append("&version=" + version);
		buf.append("&amount=" + amount);
		buf.append("&bankAbbr=" + bankAbbr);
		buf.append("&currency=" + currency);
		buf.append("&orderDate=" + orderDate);
		buf.append("&orderId=" + orderId);
		buf.append("&merAcDate=" + merAcDate);
		buf.append("&period=" + period );
		buf.append("&periodUnit=" + periodUnit);
		buf.append("&productName=" + productName);
		buf.append("&productNum="+ productNum);
		buf.append("&showUrl=" + showUrl1);
		logger.info("下单参数："+buf.toString());
		//发起http请求，并获取响应报文
		String res = "";
		try {
			res = util.sendAndRecv(req_url, buf.toString(), characterSet);
		} catch (Exception e) {
			logger.error("订单号："+orderId+"---发起下单请求失败败！", e);
			return;
		}
		logger.info("订单号："+orderId+"---下单成功，返回信息："+res);
		String hmac1 = "";
		String vfsign = "";
		try{
			//获得手机支付平台的消息摘要，用于验签,
			hmac1 = util.getValue(res, "hmac");
			logger.info("订单号："+orderId+"---获得手机支付平台的消息摘要"+hmac1);
			
			vfsign = util.getValue(res, "merchantId")
					+ util.getValue(res, "requestId")
					+ util.getValue(res, "signType")
					+ util.getValue(res, "type")
					+ util.getValue(res, "version")
					+ util.getValue(res, "returnCode")
					+ URLDecoder.decode(util.getValue(res, "message"),"UTF-8") + util.getValue(res, "payUrl");
			logger.info("订单号："+orderId+"---获得手机支付平台返回参数"+vfsign);
		}catch(Exception e){
			logger.error("订单号："+orderId+"---手机支付平台返回参数异常！",e);
			return;
		}
		
		//响应码
		String code = util.getValue(res, "returnCode");
		try{
			//下单交易失败
			if (!code.equals("000000")) {
				logger.info("订单号："+orderId+"---下单错误:" + code + URLDecoder.decode(util.getValue(res,"message"),"UTF-8"));
				return;
			}else{
				Map<String,String> map = new HashMap<String,String>();
				map.put("order_id", orderId);
				map.put("request_id", requestId);
				map.put("pay_no", null);
				orderService.updateOrderPayNo(map);
			}
		}catch(Exception e){
			logger.error("订单号："+orderId+"--根据响应码处理更新支付流水号异常！响应码"+code,e);
			return;
		}

		// -- 验证签名
		boolean flag = false;
		flag = util.MD5Verify(vfsign, hmac1, signKey);

		if (!flag) {
			//request.getSession().setAttribute("message", "验证签名失败！");
			logger.info("订单号："+orderId+"--手机支付平台的消息摘要验签失败！");
			return;
		}

		String payUrl = util.getValue(res, "payUrl");
		String submit_url = util.getRedirectUrl(payUrl);

		logger.info("订单号："+orderId+"--支付平台路径submit_url:" + submit_url);
		
		try {
			response.sendRedirect(submit_url);
		} catch (IOException e) {
			logger.error("订单号："+orderId+"--跳转支付平台异常！",e);
			return;
		}
	}
	
	/**
	 * 湖南移动异步支付成功反馈
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/modifyOrderStatus_no.jhtml")
	public void modifyOrderStatus(HttpServletRequest request, HttpServletResponse response) {
		logger.info("订单号："+this.getParam(request, "orderId")+"调用异步支付成功反馈");
		String merchantId = this.getParam(request, "merchantId");
		String payNo = this.getParam(request, "payNo");
		String returnCode = this.getParam(request, "returnCode");
		String message = this.getParam(request, "message");
		String signType = this.getParam(request, "signType");
		String type = this.getParam(request, "type");
		String version = this.getParam(request, "version");
		String amount = this.getParam(request, "amount");
		String amtItem = this.getParam(request, "amtItem");
		String bankAbbr = this.getParam(request, "bankAbbr");
		String mobile = this.getParam(request, "mobile");
		String orderId = this.getParam(request, "orderId");
		String payDate = this.getParam(request, "payDate");
		String accountDate = this.getParam(request, "accountDate");
		String reserved1 = this.getParam(request, "reserved1");
		String reserved2 = this.getParam(request, "reserved2");
		String status = this.getParam(request, "status");
		String orderDate = this.getParam(request, "orderDate");
		String fee = this.getParam(request, "fee");
		String serverCert = this.getParam(request, "serverCert");
		String hmac = this.getParam(request, "hmac");
		
		String vfsign = merchantId+payNo+returnCode+message+signType+type+version+amount+amtItem+bankAbbr+
				mobile+orderId+payDate+accountDate+reserved1+reserved2+status+orderDate+fee+serverCert;
		logger.info("订单号："+this.getParam(request, "orderId")+" 异步参数："+vfsign);
		// -- 验证签名
		boolean flag = false;
		HiiposmUtil util = new HiiposmUtil();
		flag = util.MD5Verify(vfsign, hmac, signKey);
		if(flag){
			if("SUCCESS".equals(status)){
				try {
					Map<String,String> map = new HashMap<String,String>();
					map.put("asp_order_id", orderId);
					map.put("order_status", "11");
					map.put("old_status", "00");
					orderService.updateOrderStatus(map);
					
					Map<String,String> paramMap = new HashMap<String,String>();
					paramMap.put("order_id", orderId);
					paramMap.put("pay_no", payNo);
					paramMap.put("request_id", null);
					orderService.updateOrderPayNo(paramMap);
					
					write2Response(response,"SUCCESS");
				} catch (Exception e) {
					logger.error("订单号："+orderId+"--异步反馈，更新订单支付平台交易流水号失败！", e);
					return;
				}
			}else{
				logger.info("订单号："+orderId+"异步支付失败！");
				return;
			}
		}else{
			try {
				logger.error("订单号："+orderId+"--异步支付成功反馈信息摘要验签失败！");
				write2Response(response,"FAILED");
			}catch(Exception e){
				logger.error("订单号："+orderId+"输出异常！", e);
				return;
			}
		}
	}
	
	/**
	 * 湖南移动同步支付平台跳转页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/orderResult_no.jhtml")
	public String orderResult(HttpServletRequest request, HttpServletResponse response) {
		logger.info("订单号："+this.getParam(request, "orderId")+"调用同步支付平台");
		String merchantId = this.getParam(request, "merchantId");
		String payNo = this.getParam(request, "payNo");
		String returnCode = this.getParam(request, "returnCode");
		String message = this.getParam(request, "message");
		String signType = this.getParam(request, "signType");
		String type = this.getParam(request, "type");
		String version = this.getParam(request, "version");
		String amount = this.getParam(request, "amount");
		String amtItem = this.getParam(request, "amtItem");
		String bankAbbr = this.getParam(request, "bankAbbr");
		String mobile = this.getParam(request, "mobile");
		String orderId = this.getParam(request, "orderId");
		String payDate = this.getParam(request, "payDate");
		String accountDate = this.getParam(request, "accountDate");
		String reserved1 = this.getParam(request, "reserved1");
		String reserved2 = this.getParam(request, "reserved2");
		String status = this.getParam(request, "status");
		String orderDate = this.getParam(request, "orderDate");
		String fee = this.getParam(request, "fee");
		String serverCert = this.getParam(request, "serverCert");
		String hmac = this.getParam(request, "hmac");
		
		String vfsign = merchantId+payNo+returnCode+message+signType+type+version+amount+amtItem+bankAbbr+
				mobile+orderId+payDate+accountDate+reserved1+reserved2+status+orderDate+fee+serverCert;
		logger.info("订单号："+this.getParam(request, "orderId")+" 同步参数："+vfsign);
		// -- 验证签名
		boolean flag = false;
		HiiposmUtil util = new HiiposmUtil();
		flag = util.MD5Verify(vfsign, hmac, signKey);
		if(!flag){
			logger.error("订单号："+orderId+"同步返回结果验签失败！");
			return null;
		}
		return "redirect:/query/queryOrderDetail.jhtml?fromFunc=payNotify&order_id=" + orderId;

	}
}
