package com.l9e.transaction.controller;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
import com.l9e.util.EmappSignService;
import com.l9e.util.PayUtil;

/**
 * 下单
 * 
 * @author yangchao
 * 
 */
@Controller
@RequestMapping("/order")
public class YjpayController extends BaseController {

	private static final Logger logger = Logger
			.getLogger(YjpayController.class);

	@Resource
	private OrderService orderService;
	
	//19pay开始
	
	@Value("#{propertiesReader[appInitKey]}")
	private String appInitKey;//验签静态key
	
	@Value("#{propertiesReader[paynotifyurl]}")
	private String paynotifyurl;//支付结果通知地址
	
	@Value("#{propertiesReader[refundnotifyurl]}")
	private String refundnotifyurl;//退款结果通知地址
	
	@Value("#{propertiesReader[orderdetail]}")
	private String orderdetail;//订单明细地址
	
	@Value("#{propertiesReader[characterSet]}")
	private String characterSet;//编码
	

	
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
		LoginUserInfo loginUser = this.getLoginUser(request);
		String user_id = loginUser.getUser_id();
		String user_name=loginUser.getUser_name();
		
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setUser_id(user_id);
		orderInfo.setUser_name(user_name);
		
		OrderInfoPs orderInfoPs = new OrderInfoPs();
		List<OrderInfoCp> orderInfoCpList = new ArrayList<OrderInfoCp>();
		List<OrderInfoBx> orderInfoBxList = new ArrayList<OrderInfoBx>();
		
		//zuoyuxing 2013-12-02
		String product_id = this.getParam(request, "product_id");
		for(BookDetailInfo detailInfo : bookInfo.getBookDetailInfoList()){
			detailInfo.setProduct_id(product_id);
		}
		
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
		String order_id = CreateIDUtil.createID("PAY");
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
			}
		}
		money = AmountUtil.sub(money, profit);
		return String.valueOf(money);
	}
	

	
	
	/**
	 * train对19pay的支付请求
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/orderYjpay.jhtml")
	public String orderYjpay(HttpServletRequest request, HttpServletResponse response) {
		logger.info("请求19pay支付,跳转支付页面！");
		String orderId = this.getParam(request, "order_id");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String mxdate = sdf.format(new Date());

		//从session中取出用户信息
		LoginUserInfo loginUserInfo=(LoginUserInfo) request.getSession().getAttribute(TrainConsts.INF_LOGIN_USER);
		String plat_order_url=loginUserInfo.getPlat_order_url();
		String custid=loginUserInfo.getUser_id();
		
		
		OrderInfo orderInfo = orderService.queryOrderInfo(orderId);
		//订单金额 amount 保留两位小数
		if(orderInfo.getPay_money()==null || orderInfo.getPay_money()=="" || Double.parseDouble(orderInfo.getPay_money())==0.0){
			logger.info("发起支付请求失败！支付金额为0"+" 订单号："+orderId);
			return null;
		}
		//支付金额，从三位小数变成两位小数
		String amount=orderInfo.getPay_money();
		amount=amount.substring(0, amount.length()-1);
		
		String extend1="";
		String extend2="";
		//签名  hmac
		String hmac="";
		//请求参数+签名
		String req_param="";

		String[][] resParam=new String[9][2];
		resParam[0][0]="mxorderid";
		resParam[0][1]=orderId;
		resParam[1][0]="mxdate";
		resParam[1][1]=mxdate;
		resParam[2][0]="custid";
		resParam[2][1]=custid;
		resParam[3][0]="amount";
		resParam[3][1]=amount;
		resParam[4][0]="paynotifyurl";
		resParam[4][1]=paynotifyurl;
		resParam[5][0]="refundnotifyurl";
		resParam[5][1]=refundnotifyurl;
		resParam[6][0]="orderdetail";
		resParam[6][1]=orderdetail;
		resParam[7][0]="extend1";
		resParam[7][1]=extend1;
		resParam[8][0]="extend2";
		resParam[8][1]=extend2;
		req_param=EmappSignService.creatResSign(resParam, appInitKey);
		
		hmac=PayUtil.getValue(req_param, "hmac").trim();
		//logger.info("签名报文："+hmac +" 订单号："+orderId);
		
		String [] url=plat_order_url.split("\\?");
		//logger.info("支付请求的url地址为："+url[0]);
		StringBuffer sbpara=new StringBuffer();
		try {
			sbpara.append(url[1])
				.append("&amount=").append(URLEncoder.encode(amount,characterSet))
				.append("&custid=").append(URLEncoder.encode(custid,characterSet))
				.append("&extend1=").append(URLEncoder.encode(extend1,characterSet))
				.append("&extend2=").append(URLEncoder.encode(extend2,characterSet))
				.append("&mxdate=").append(URLEncoder.encode(mxdate,characterSet))
				.append("&mxorderid=").append(URLEncoder.encode(orderId,characterSet))
				.append("&orderdetail=").append(URLEncoder.encode(orderdetail, characterSet))
				.append("&paynotifyurl=").append(URLEncoder.encode(paynotifyurl, characterSet))
				.append("&refundnotifyurl=").append(URLEncoder.encode(refundnotifyurl, characterSet));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		//logger.info("url："+url[0]+" url1："+url[1]);
		logger.info("支付请求的完整参数串为："+sbpara.toString().trim()+"&hmac="+hmac);
		try {
			//发起http请求 http://192.168.32.14:8090/train/trainticket.jhtml?way=confirm
			//页面跳转
			return "redirect:"+url[0]+"?"+sbpara.toString().trim()+"&hmac="+hmac;
		} catch (Exception e) {
			logger.error("订单号："+orderId+"---发起支付请求失败！", e);
			return null;
		}
	
	
	}
	
	/**
	 * 接收19pay的支付结果通知请求
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/resultYjpay_no.jhtml")
	public void resultYjpay(HttpServletRequest request, HttpServletResponse response) {
		logger.info("接收19pay的支付结果通知请求！");
		Map<String,String> resultMap=new HashMap<String,String>();

		String sign=this.getParam(request, "sign");//签名值
		String order_id=this.getParam(request, "order_id");//train订单号
		String pay_status=this.getParam(request, "pay_status"); //支付状态
		String reason=this.getParam(request, "reason");//如果支付失败，失败信息
		String refund_url=this.getParam(request, "refund_url");//退款接口url
		String pay_time=this.getParam(request, "pay_time");
		String timestamp=this.getParam(request, "timestamp"); //访问时间戳
		String service=this.getParam(request, "service");//接口名称
		String sign_type=this.getParam(request, "sign_type");//签名方式
		String partner_id=this.getParam(request, "partner_id");//合作伙伴id
		String pay_seq=this.getParam(request, "pay_seq"); //19pay支付流水号
		String plat_order_id=this.getParam(request, "plat_order_id");//19pay订单号
		String ticket_url=this.getParam(request, "ticket_url"); //出票结果通知地址
		
		resultMap.put("service", service);
		resultMap.put("sign_type", sign_type); 
		resultMap.put("timestamp", timestamp);
		resultMap.put("partner_id", partner_id);
		resultMap.put("refund_url", refund_url);
		resultMap.put("pay_seq", pay_seq); 
		resultMap.put("order_id", order_id);
		resultMap.put("plat_order_id", plat_order_id);
		resultMap.put("pay_status", pay_status);
		resultMap.put("reason", reason);
		resultMap.put("pay_time", pay_time);
		resultMap.put("ticket_url", ticket_url);
		
		//验证签名
		boolean flag = false;
		logger.info("接收19pay的支付结果通知签名为："+sign);
		flag=EmappSignService.checkReqSign(resultMap, sign, appInitKey);
		if(flag){
			logger.info("支付结果通知验签成功！订单号："+order_id);
			if("SUCCESS".equals(pay_status)){
				try {
					logger.info("订单号："+order_id+" 支付 成功！");
					//订单状态,11:支付成功
					resultMap.put("order_status", TrainConsts.PAY_SUCCESS);
					int update=0;
					update=orderService.updateOrderPayInfo(resultMap);
					if(update==1){
						write2Response(response,"Y");
						logger.info("已更新19pay订单号及交易流水号！");
					}else{
						logger.info("更新19pay订单号及交易流水号失败！");
						write2Response(response,"N");
					}
				} catch (Exception e) {
					write2Response(response,"N");
					logger.error("订单号："+order_id+"--异步反馈，更新订单支付平台交易流水号失败！", e);
				}
			}else {
				logger.info("订单号："+order_id+" 支付失败，失败原因为："+reason);
			}
		}else{
			try {
				logger.error("订单号："+order_id+"--异步支付成功反馈信息摘要验签失败！");
				write2Response(response,"N");
			}catch(Exception e){
				logger.error("验签失败，订单号："+order_id+" 输出异常！", e);
				return;
			}
		} 
		
	}
	
	/**
	 * 19pay同步支付平台跳转页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/orderResult_no.jhtml")
	public String orderResult(HttpServletRequest request, HttpServletResponse response) {
		logger.info("订单号："+this.getParam(request, "order_id")+"支付完成跳转到订单明细页面！");
		String order_id=this.getParam(request, "order_id");
		String plat_order_id=this.getParam(request, "plat_order_id");
		logger.info("订单号："+order_id+" 19pay订单号："+plat_order_id);
		//直接跳转页面------------
		return "redirect:/query/queryOrderDetail.jhtml?fromFunc=payNotify&order_id=" + order_id+"&plat_order_id="+plat_order_id;

	}
	
	
}

