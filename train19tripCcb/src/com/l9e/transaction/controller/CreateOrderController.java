package com.l9e.transaction.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jiexun.iface.util.StringUtil;
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
import com.l9e.util.DateUtil;
import com.l9e.util.Md5Encrypt;

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
		LoginUserInfo loginUser = this.getLoginUser(request);
		/**
		 * 下单时把建行传递过来的数据存入订单表中
		 */
		String user_id = loginUser.getUserId();
		String user_id_temp=user_id;
		if(StringUtil.isEmpty(user_id) || "D63D1A292A70CA8E".equals(user_id)){
			//如果是游客，则存入其购票时填入的手机号
			user_id=bookInfo.getLink_phone();
		}
		String itemNo=loginUser.getItemNo();
		String cityName=loginUser.getCityName();
		
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setUser_id(user_id);
		orderInfo.setItemNo(itemNo);
		orderInfo.setCityName(cityName);
		if(StringUtil.isEmpty(user_id_temp) || "D63D1A292A70CA8E".equals(user_id_temp)){
			//如果是游客，则存入其购票时填入的手机号
			user_id_temp=bookInfo.getLink_phone();
			orderInfo.setCm_phone(user_id_temp);
		}else{
			orderInfo.setCm_phone(loginUser.getCellPhone());
		}
		
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
		
		return "redirect:/order/orderComfirm.jhtml?order_id=" + order_id;

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
		request.setAttribute("totalPay4Show", orderInfo.getPay_money());
		
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
		String order_id = CreateIDUtil.createID("CCB");
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
	 * 建行下单重定向支付页面
	 * @param request
	 * @param response
	 * @return 
	 * @return
	 */
	@RequestMapping("/orderCmpay.jhtml")
	public String orderCmpay(HttpServletRequest request, HttpServletResponse response) {
		logger.info("跳转支付页面");
		response.setContentType("text/html; charset=gbk");
        response.setHeader("Cache-Control", "no-cache");  
        LoginUserInfo loginInfo = (LoginUserInfo) request.getSession().getAttribute(TrainConsts.INF_LOGIN_USER);
		//商户会计日期（自定义）
		String order_id = this.getParam(request, "order_id");
		OrderInfo orderInfo = orderService.queryOrderInfo(order_id);
		OrderInfoPs orderPs = orderService.queryOrderInfoPs(order_id);
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
		
		String version_id = "2.00";
		String order_date = DateUtil.dateToString(new Date(), DateUtil.DATE_YMD);
		
		//金额单位为元
		BigDecimal bg = new BigDecimal(Double.valueOf(orderInfo.getPay_money()));
        double amount = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		String currency = "RMB";
		
		String pm_id = "CCB";
		String mx_ip = clientIp;
		String pc_id = "BC00010005";
		String order_pname = "火车订单";
		String order_pdesc = "19trip火车票";
		
		String user_name = "";
		String user_phone = "";
		String user_mobile = orderPs.getLink_phone();
		String user_email = "";
		
		String md5_str = "";
		md5_str="version_id="+version_id+"&merchant_id="+merchant_id+"&order_date="+
		order_date+"&order_id="+order_id+"&amount="+amount+"&currency="+currency+"&returl="+callbackUrl
		+"&pm_id="+pm_id+"&pc_id="+pc_id+"&merchant_key="+merchant_key;
		System.out.println("md5_str:"+md5_str);
		String verifystring = Md5Encrypt.getKeyedDigestFor19Pay(md5_str,"",characterSet_GBK);
		try {
			order_pname = new String(order_pname.getBytes("UTF-8"),"GBK");
			order_pname = URLEncoder.encode(order_pname, characterSet_GBK);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}  
		StringBuffer param = new StringBuffer();
		param.append("?version_id=").append(version_id);
		param.append("&merchant_id=").append(merchant_id);
		param.append("&order_date=").append(order_date);
		param.append("&order_id=").append(order_id);
		param.append("&amount=").append(amount);
		param.append("&currency=").append(currency);
		param.append("&returl=").append(callbackUrl);
		param.append("&notify_url=").append(notifyUrl);
		param.append("&pm_id=").append(pm_id);
		param.append("&mx_ip=").append(mx_ip);
		param.append("&pc_id=").append(pc_id);
		param.append("&order_pname=").append(order_pname);
		param.append("&order_pdesc=").append(order_pdesc);
		param.append("&user_name=").append(user_name);
		param.append("&user_phone=").append(user_phone);
		param.append("&user_mobile=").append(user_mobile);
		param.append("&user_email=").append(user_email);
		param.append("&verifystring=").append(verifystring);
		param.append("&mx_home_page=").append(mx_home_page).append(order_id);
		param.append("&page_type=").append("INNER");
		param.append("&mx_userid=").append(loginInfo.getUserId());
		param.append("&mx_busi_app_id=").append(loginInfo.getItemNo());
		
		return "redirect:"+req_url+param.toString();
	}
	/**
	 * 19pay同步支付平台跳转页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/orderResult_no.jhtml")
	public String orderResult(HttpServletRequest request, HttpServletResponse response) {
		logger.info("订单号："+this.getParam(request, "order_id")+"调用同步支付平台");
		String merchant_id = this.getParam(request, "merchant_id");
		String pay_sq = this.getParam(request, "pay_sq");
		String result = this.getParam(request, "result");
		String version_id = this.getParam(request, "version_id");
		String amount = this.getParam(request, "amount");
		String currency = this.getParam(request, "currency");
		String order_id = this.getParam(request, "order_id");
		String pay_date = this.getParam(request, "pay_date");
		String pm_id = this.getParam(request, "pm_id");
		String pc_id = this.getParam(request, "pc_id");
		String pay_cardno = this.getParam(request, "pay_cardno");
		String pay_cardpwd= this.getParam(request, "pay_cardpwd");
		String order_date = this.getParam(request, "order_date");
		String hmac = this.getParam(request, "verifystring");
		
		StringBuffer md5_url = new StringBuffer();
		md5_url.append("version_id=").append(version_id);
		md5_url.append("&merchant_id=").append(merchant_id);
		md5_url.append("&order_date=").append(order_date);
		md5_url.append("&order_id=").append(order_id);
		md5_url.append("&amount=").append(amount);
		md5_url.append("&currency=").append(currency);
		md5_url.append("&pay_sq=").append(pay_sq);
		md5_url.append("&pay_date=").append(pay_date);
		md5_url.append("&pc_id=").append(pc_id);
		md5_url.append("&result=").append(result);
		md5_url.append("&merchant_key=").append(merchant_key);
		
		logger.info("订单号："+this.getParam(request, "orderId")+" 同步参数："+md5_url.toString());
		String hmac_param = Md5Encrypt.getKeyedDigestFor19Pay(md5_url.toString(),"",characterSet_GBK);
		if(hmac_param.equals(hmac)){
			return "redirect:/query/queryOrderDetail.jhtml?fromFunc=payNotify&order_id=" + order_id;
		}else{
			logger.error("支付请求返回验签失败！");
			return "common/error";
		}
	}
	/**
	 * 异步支付成功反馈
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/modifyOrderStatus_no.jhtml")
	public void modifyOrderStatus(HttpServletRequest request, HttpServletResponse response) {
		logger.info("订单号："+this.getParam(request, "order_id")+"调用异步支付成功反馈");
		String merchant_id = this.getParam(request, "merchant_id");
		String pay_sq = this.getParam(request, "pay_sq");
		String result = this.getParam(request, "result");
		String version_id = this.getParam(request, "version_id");
		String amount = this.getParam(request, "amount");
		String currency = this.getParam(request, "currency");
		String order_id = this.getParam(request, "order_id");
		String pay_date = this.getParam(request, "pay_date");
		String pm_id = this.getParam(request, "pm_id");
		String pc_id = this.getParam(request, "pc_id");
		String pay_cardno = this.getParam(request, "pay_cardno");
		String pay_cardpwd= this.getParam(request, "pay_cardpwd");
		String order_date = this.getParam(request, "order_date");
		String hmac = this.getParam(request, "verifystring");
		
		StringBuffer md5_url = new StringBuffer();
		md5_url.append("version_id=").append(version_id);
		md5_url.append("&merchant_id=").append(merchant_id);
		md5_url.append("&order_id=").append(order_id);
		md5_url.append("&result=").append(result);
		md5_url.append("&order_date=").append(order_date);
		md5_url.append("&amount=").append(amount);
		md5_url.append("&currency=").append(currency);
		md5_url.append("&pay_sq=").append(pay_sq);
		md5_url.append("&pay_date=").append(pay_date);
		md5_url.append("&pc_id=").append(pc_id);
		md5_url.append("&merchant_key=").append(merchant_key);
		System.out.println("返回加密串hmac:"+hmac);
		String hmac_param = Md5Encrypt.getKeyedDigestFor19Pay(md5_url.toString(),"","GBK");
		logger.info("订单号："+this.getParam(request, "orderId")+" 异步参数："+md5_url.toString());
		// -- 验证签名
		if(hmac_param.equals(hmac)){
			if("Y".equals(result)){
				try {
					Map<String,String> map = new HashMap<String,String>();
					map.put("asp_order_id", order_id);
					map.put("order_status", "11");
					map.put("old_status", "00");
					map.put("pay_seq", pay_sq);
					orderService.updateOrderStatus(map);
					
					write2Response(response,"Y");
				} catch (Exception e) {
					logger.error("订单号："+order_id+"--异步反馈，更新订单支付平台交易流水号失败！", e);
					return;
				}
			}else{
				logger.error("订单号："+order_id+"--异步反馈，更新订单支付平台交易流水号失败！");
				return;
			}
		}else{
			write2Response(response,"N");
			logger.error("支付请求返回验签失败！");
			return;
		}
	}
}
