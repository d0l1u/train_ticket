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

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jiexun.iface.util.StringUtil;
import com.l9e.common.BaseController;
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
import com.l9e.transaction.vo.OuterSoukdNewData;
import com.l9e.transaction.vo.ProductVo;
import com.l9e.transaction.vo.TrainNewData;
import com.l9e.util.AmountUtil;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.EmappSignUtil;
import com.l9e.util.HttpPostUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.MobileMsgUtil;
import com.l9e.util.PayUtil;

/**
 * 下单
 * 
 * 
 */
@Controller
@RequestMapping("/order")
public class CreateOrderController extends BaseController {

	private static final Logger logger = Logger.getLogger(CreateOrderController.class);

	@Resource
	private OrderService orderService;
	
	@Resource
	private UserInfoService userInfoService;
	
	@Resource
	private MobileMsgUtil mobileMsgUtil;
	
	
	@Value("#{propertiesReader[appInitKey]}")
	private String appInitKey;//验签静态key
	
	@Value("#{propertiesReader[merchantId]}")
	private String merchantId;//合作商户ID
	
	@Value("#{propertiesReader[paynotifyurl]}")
	private String paynotifyurl;//支付结果通知地址
	
	@Value("#{propertiesReader[refundnotifyurl]}")
	private String refundnotifyurl;//退款结果通知地址
	
	@Value("#{propertiesReader[orderdetail]}")
	private String orderdetail;//订单明细地址
	
	@Value("#{propertiesReader[characterSet]}")
	private String characterSet;//编码

	@Value("#{propertiesReader[plat_order_url]}")
	private String plat_order_url;//19pay发起支付地址
	
	@RequestMapping("/createOrder.jhtml")
	public void createTrainOrder(BookInfo bookInfo,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		logger.info("进入下单");
		OrderInfo orderInfo = new OrderInfo();
		OrderInfoPs orderInfoPs = new OrderInfoPs();
		List<OrderInfoCp> orderInfoCpList = new ArrayList<OrderInfoCp>();
		List<OrderInfoBx> orderInfoBxList = new ArrayList<OrderInfoBx>();
		
		String product_id = this.getParam(request, "product_id");
		logger.info("product_id："+product_id);
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
		
		logger.info("[火车票先预定后支付]：火车票order_id:" + order_id);
		
		String totalPay4Show = getTotalPay4Show(bookInfo, request);
		JSONObject json = new JSONObject();
		json.put("result", "SUCCESS");
		json.put("totalPay4Show", totalPay4Show);
		json.put("order_id", order_id);
		write2Response(response, json.toString());
	}
	
	//更新常用联系人信息和报销凭证数据
	@RequestMapping("/createOrderLink.jhtml")
	public void createOrderLink(BookInfo bookInfo,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		String order_id = this.getParam(request, "order_id");
		//判断用户是否已经登录
		LoginUserInfo loginUser = this.getLoginUser(request);
		if(loginUser==null || StringUtils.isEmpty(loginUser.getUser_id())){
			loginUser = userInfoService.queryUserInfo(bookInfo.getLink_phone());
			if(loginUser==null || StringUtils.isEmpty(loginUser.getUser_phone())){//常用联系人的电话号码没有注册过
				loginUser = new LoginUserInfo();
				String phoneCode = Long.toString(Math.round(Math.random()*899999+100000));//随机生成六位数的验证码
				String user_id = CreateIDUtil.createID("WBU");
				loginUser.setUser_id(user_id);
				loginUser.setUser_name(bookInfo.getLink_name());
				loginUser.setUser_phone(bookInfo.getLink_phone());
				loginUser.setUser_password(phoneCode);//用户密码:随机生成6位数
				loginUser.setUser_source("web");
				loginUser.setWeather_able("1");
				loginUser.setLogin_num("1");
				loginUser.setLogin_ip(request.getRemoteAddr());//获取登录IP地址
				loginUser.setUser_verify("00");//用户证件信息审核 00：正在审核 11：审核通过 22：审核未通过
				loginUser.setScore_num("0");//用户积分
				loginUser.setCreate_time("now()");
				loginUser.setLogin_time("now()");
				System.out.println("*************************"+loginUser.getUser_id()+","+bookInfo.getLink_name()+","
						+bookInfo.getLink_phone()+","+loginUser.getUser_password());
				userInfoService.addUserInfo(loginUser);//数据库添加新用户
				//TODO 向用户下发短信，告知其密码
				StringBuffer content = new StringBuffer();
				//158XXXXXXXX，初始密码：XXXXXX；为了您的账号安全，请尽快登陆我的旅行修改密码。
				content.append("【19旅行】尊敬的用户，您好！恭喜您成为19旅行网的会员，用户名："+bookInfo.getLink_phone()+"，初始密码："+phoneCode+"；为了您的账号安全，请尽快登陆我的旅行修改密码。");
				boolean flag = mobileMsgUtil.send(bookInfo.getLink_phone(), content.toString(), "22");
				logger.info("自动为用户注册，下发登录密码短信："+bookInfo.getLink_phone()+"【"+content+"】");
			}else{//若已经注册过，则从数据库取值放入session
				//TODO 向用户下发短信，告知其已经注册过
				StringBuffer content = new StringBuffer();
				//【19旅行】尊敬的用户，您好！您已注册过19旅行网的会员，您的密码：XXXX。欢迎您继续使用19旅行网！
				content.append("【19旅行】尊敬的用户，您好！您已注册过19旅行网的会员，您的密码："+loginUser.getUser_password()+"。欢迎您继续使用19旅行网！");
				boolean flag = mobileMsgUtil.send(bookInfo.getLink_phone(), content.toString(), "22");
				logger.info("自动为用户注册，下发登录密码短信："+bookInfo.getLink_phone()+"【"+content+"】");
			}
			//用户信息放入session
			request.getSession().setAttribute(TrainConsts.INF_LOGIN_USER, loginUser);
		}
		
		OrderInfo orderInfo = new OrderInfo();//app_orderinfo表
		orderInfo.setOrder_id(order_id);
		orderInfo.setUser_id(loginUser.getUser_id());
		orderInfo.setLink_name(bookInfo.getLink_name());
		orderInfo.setLink_phone(bookInfo.getLink_phone());
		
		OrderInfoBx orderInfoBx = new OrderInfoBx();//cp_orderinfo_bx表
		orderInfoBx.setOrder_id(order_id);
		orderInfoBx.setTelephone(bookInfo.getLink_phone());//联系人电话号码
		
		OrderInfoPs orderInfoPs = new OrderInfoPs();//cp_orderinfo_ps表
		orderInfoPs.setOrder_id(order_id);
		orderInfoPs.setLink_name(bookInfo.getLink_name());
		orderInfoPs.setLink_phone(bookInfo.getLink_phone());
		
		Map<String, String> bxfpMap = null;//保险发票 cp_orderinfo_bxfp表
		if("1".equals(this.getParam(request, "fpNeed"))){
			bxfpMap = new HashMap<String, String>(4);
			bxfpMap.put("order_id", orderInfo.getOrder_id());
			bxfpMap.put("fp_receiver", this.getParam(request, "fp_receiver"));
			bxfpMap.put("fp_phone", this.getParam(request, "fp_phone"));
			bxfpMap.put("fp_zip_code", this.getParam(request, "fp_zip_code"));
			bxfpMap.put("fp_address", this.getParam(request, "fp_address"));
		}

		//根据火车票订单号更新该订单的联系人信息和报销凭证
		orderService.updateOrder(orderInfo, orderInfoBx, orderInfoPs, bxfpMap);
		
		logger.info("[火车票下单更新该订单的联系人信息和报销凭证]：火车票order_id:" + order_id);
		
		JSONObject json = new JSONObject();
		json.put("result", "SUCCESS");
		json.put("order_id", order_id);
		write2Response(response, json.toString());
	}
	
	//跳转至支付页面
	@RequestMapping("/toOrderPay.jhtml")
	public String toOrderPay(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String order_id = this.getParam(request, "order_id");
		return "redirect:/order/orderComfirm.jhtml?order_id=" + order_id;
	}
	
	/**
	 * 下单查询支付结果
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryOrderResult.jhtml")
	public void queryOrderResult(HttpServletRequest request, HttpServletResponse response) {
		String order_id= this.getParam(request, "order_id");
		OrderInfo orderInfo = orderService.queryOrderInfo(order_id);
		if("45".equals(orderInfo.getOrder_status())){//出票失败
			String error_info = (String) request.getSession().getAttribute(orderInfo.getOrder_id()+"_error");
			if(null == error_info){
				 error_info = "1";
			}
			this.write2Response(response, "FAILURE"+TrainConsts.getOutFailReason().get(error_info));
		}
		if(null==orderInfo.getOut_ticket_time()){
			this.write2Response(response, "FALSE");
		}
		this.write2Response(response, "SUCCESS");
	}
	
	/**
	 * 订单超时未支付
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/orderCancle.jhtml")
	public String orderCancle(HttpServletRequest request, HttpServletResponse response) {
		String order_id= this.getParam(request, "order_id");
		String type= this.getParam(request, "type");
		Map<String,String> map = new HashMap<String,String>();
		map.put("old_status", "33");
		map.put("order_status", TrainConsts.OVER_NOPAY);
		map.put("asp_order_id", order_id);
		orderService.updateOrderStatus(map);
		//HttpUtil.sendByPost(this.getTrainSysSettingValue("notify_cancle_interface_url","inner_notify_cancle_interface_url"), "order_id="+order_id, "UTF-8");
		if("detail".equals(type)){
			return "redirect:/query/queryOrderList.jhtml";
		}else{
			return "redirect:/buyTicket/bookIndex.jhtml";
		}
	}
	
	//跳转至19pay支付页面
	@RequestMapping("/gotoPay.jhtml")
	public String gotoPay(HttpServletRequest request, HttpServletResponse response){
		String buyBx= this.getParam(request, "buyBx");//yes买保险  no不买保险
		//TODO
		
		logger.info("请求19pay支付,跳转支付页面！");
		String orderId = this.getParam(request, "order_id");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String mxdate = sdf.format(new Date());

		//从session中取出用户信息
		LoginUserInfo loginUserInfo=(LoginUserInfo) request.getSession().getAttribute(TrainConsts.INF_LOGIN_USER);
		String custid=loginUserInfo.getUser_id();
		
		
		OrderInfo orderInfo = orderService.queryOrderInfo(orderId);
		//订单金额 amount 保留两位小数
		if(orderInfo.getPay_money()==null || orderInfo.getPay_money()=="" || Double.parseDouble(orderInfo.getPay_money())==0.0){
			logger.info("发起支付请求失败！支付金额为0"+" 订单号："+orderId);
			return null;
		}
		String order_id = orderInfo.getOrder_id();
		String order_date = orderInfo.getOrder_time();
		
		//支付金额，从1位小数变成两位小数
		String amount=orderInfo.getPay_money();
		amount=amount+"0";
		String version_id = "2.00";//19pay支付接口版本号
		String currency = "RMB";//货币类型:RMB人民币
		String pm_id = "";//支付方式id:同时显示19PAY钱包和网银，则置为空 
		String pc_id = "";//支付通道id:如果不显示某一支付方式，可以赋空串
		String mx_ip = request.getRemoteAddr();//获取登录IP地址----支付用户IP:19pay系统防钓鱼参数
		String order_pname = "train_ticket";
		/**
		 * 商户首先需要向19PAY在线支付网关申请商户代码（merchant_id）及加密串（merchant_key），
		 * 并按照以下规则来构造验证摘要串（verifystring）。
		 */
		//签名  verifystring
		String verifystring="";
		//请求参数+签名
		String req_param="";
		//verifystring=version_id=%s&merchant_id=%s&order_date=%s&order_id=%s
		//&amount=%s&currency=%s&returl=%s&pm_id=%s&pc_id=%s
		//&merchant_key=%s
		String[][] resParam=new String[10][2];
		resParam[0][0]="version_id";
		resParam[0][1]=version_id;
		resParam[1][0]="merchant_id";//19PAY统一分配的商户代码
		resParam[1][1]=merchantId;
		resParam[2][0]="order_date";//订单日期：格式为‘YYYYMMDD’
		resParam[2][1]=order_date;
		resParam[3][0]="order_id";
		resParam[3][1]=order_id;
		resParam[4][0]="amount";//paynotifyurl
		resParam[4][1]=amount;
		resParam[5][0]="currency";//refundnotifyurl
		resParam[5][1]=currency;
		resParam[6][0]="returl";//支付请求返回url:本接口处理完成后，19PAY平台将重定向到该url
		resParam[6][1]=orderdetail;
		resParam[7][0]="pm_id";
		resParam[7][1]=pm_id;
		resParam[8][0]="pc_id";
		resParam[8][1]=pc_id;
		resParam[9][0]="merchant_key";//加密串
		resParam[9][1]=appInitKey;
		req_param=EmappSignUtil.creatResSign(resParam, appInitKey);
		
//		verifystring=PayUtil.getValue(req_param, "hmac").trim();
		verifystring=req_param.trim();
		//logger.info("签名报文："+verifystring +" 订单号："+orderId);
		
		String url=plat_order_url;
		//logger.info("支付请求的url地址为："+url[0]);
		StringBuffer sbpara=new StringBuffer();
		try {
			sbpara.append("version_id=").append(URLEncoder.encode(version_id,characterSet))
				.append("&merchant_id=").append(URLEncoder.encode(merchantId,characterSet))
				.append("&verifystring=").append(URLEncoder.encode(verifystring,characterSet))
				.append("&order_date=").append(URLEncoder.encode(order_date,characterSet))
				.append("&order_id=").append(URLEncoder.encode(order_id,characterSet))
				.append("&amount=").append(URLEncoder.encode(amount,characterSet))
				.append("&currency=").append(URLEncoder.encode(currency,characterSet))
				.append("&returl=").append(URLEncoder.encode(orderdetail,characterSet))
				.append("&notify_url=").append(URLEncoder.encode(paynotifyurl,characterSet))
				.append("&pm_id=").append(URLEncoder.encode(pm_id,characterSet))
				.append("&mx_ip=").append(URLEncoder.encode(mx_ip,characterSet))
				.append("&pc_id=").append(URLEncoder.encode(pc_id,characterSet))
				.append("&order_pname=").append(URLEncoder.encode(order_pname,characterSet))
				.append("&order_pdesc=").append(URLEncoder.encode(orderInfo.getFrom_city()+orderInfo.getTo_city()+orderInfo.getTravel_time(),characterSet))
				.append("&user_name=").append(URLEncoder.encode(orderInfo.getUser_name(), characterSet))
				.append("&user_phone=").append(URLEncoder.encode(orderInfo.getLink_phone(), characterSet))
				.append("&user_mobile=").append(URLEncoder.encode(orderInfo.getLink_phone(), characterSet))
				.append("&user_email=").append(URLEncoder.encode("", characterSet));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		//logger.info("url："+url[0]+" url1："+url[1]);
		logger.info("支付请求的完整参数串为："+sbpara.toString().trim());
		System.out.println("----------------------"+url+"?"+sbpara.toString().trim());
		try {
			//发起http请求 http://192.168.32.14:8090/train/trainticket.jhtml?way=confirm
			//页面跳转
			return "redirect:"+url+"?"+sbpara.toString().trim();
		} catch (Exception e) {
			logger.error("订单号："+orderId+"---发起支付请求失败！", e);
			return null;
		}
	}
	
	/**
	 * 接收19pay的支付结果通知请求---后台通知
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/resultYjpay_no.jhtml")
	public void resultYjpay(HttpServletRequest request, HttpServletResponse response) {
		logger.info("接收19pay的支付结果后台通知请求！");
		Map<String,String> resultMap=new HashMap<String,String>();
		//version_id=%s&merchant_id=%s&order_id=%s&result=%s&order_date=%s&amount=%s&currency=%s&pay_sq=%s&pay_date=%s&pc_id=%s&merchant_key=%s
		String verifystring=this.getParam(request, "verifystring");//签名值
		String version_id=this.getParam(request, "version_id");//版本号
		String merchant_id=this.getParam(request, "merchant_id");//商户代码
		String order_date=this.getParam(request, "order_date");//订单日期：格式为‘YYYYMMDD’
		String order_id=this.getParam(request, "order_id");//
		String result=this.getParam(request, "result");//支付结果  Y：成功
		String amount=this.getParam(request, "amount");//金额
		String currency=this.getParam(request, "currency");//RMB
		String pay_sq=this.getParam(request, "pay_sq");//支付流水号
		String pay_date=this.getParam(request, "pay_date");//支付时间：格式为‘YYYYMMDDHHMMSS’
		String pm_id=this.getParam(request, "pm_id");//支付方式
		String pc_id=this.getParam(request, "pc_id");//支付通道编号
		String pay_cardno=this.getParam(request, "pay_cardno");//卡序列号：可空
		String Pay_cardpwd=this.getParam(request, "Pay_cardpwd");//卡密码：可空
		
		resultMap.put("version_id", version_id);
		resultMap.put("merchant_id", merchant_id);
		resultMap.put("order_id", order_id);
		resultMap.put("result", result);
		resultMap.put("order_date", order_date);
		resultMap.put("amount", amount);
		resultMap.put("currency", currency);
		resultMap.put("pay_sq", pay_sq);
		resultMap.put("pay_date", pay_date);////支付时间：格式为‘YYYYMMDDHHMMSS’
		resultMap.put("pc_id", pc_id);
		resultMap.put("merchant_key", appInitKey);
		String pay_time = DateUtil.dateToString(DateUtil.stringToDate(pay_date, DateUtil.DATE_FMT2), DateUtil.DATE_FMT3);
		resultMap.put("pay_time", pay_time);
		
		//version_id=%s&merchant_id=%s&order_id=%s&result=%s&order_date=%s&amount=%s&currency=%s&pay_sq=%s&pay_date=%s&pc_id=%s&merchant_key=%s
		String[][] resParam=new String[11][2];
		resParam[0][0]="version_id";
		resParam[0][1]=version_id;
		resParam[1][0]="merchant_id";//19PAY统一分配的商户代码
		resParam[1][1]=merchant_id;
		resParam[2][0]="order_id";//订单日期：格式为‘YYYYMMDD’
		resParam[2][1]=order_id;
		resParam[3][0]="result";
		resParam[3][1]=result;
		resParam[4][0]="order_date";//paynotifyurl
		resParam[4][1]=order_date;
		resParam[5][0]="amount";//refundnotifyurl
		resParam[5][1]=amount;
		resParam[6][0]="currency";//支付请求返回url:本接口处理完成后，19PAY平台将重定向到该url
		resParam[6][1]=currency;
		resParam[7][0]="pay_sq";
		resParam[7][1]=pay_sq;
		resParam[8][0]="pay_date";
		resParam[8][1]=pay_date;
		resParam[9][0]="pc_id";//加密串
		resParam[9][1]=pc_id;
		resParam[10][0]="merchant_key";//加密串
		resParam[10][1]=appInitKey;
		//验证签名
		boolean flag = false;
		logger.info("接收19pay的支付结果通知签名为："+verifystring);
		//flag=EmappSignUtil.checkReqSign(resParam, verifystring, appInitKey);
		String verifystringOur= EmappSignUtil.creatResSign(resParam, appInitKey);
		flag = verifystringOur.equals(verifystring);
		if(flag){
			logger.info("支付结果通知验签成功！订单号："+order_id);
			if("Y".equals(result)){
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
				logger.info("订单号："+order_id+" 支付失败");
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
	 * 19pay同步支付平台跳转页面(前台通知地址)
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/orderResult_no.jhtml")
	public String orderResult(HttpServletRequest request, HttpServletResponse response) {
		logger.info("19pay同步支付平台跳转页面(前台通知地址),订单号："+this.getParam(request, "order_id")+"支付完成！");
		String order_id=this.getParam(request, "order_id");
		String amount=this.getParam(request, "amount");
		String pay_sq=this.getParam(request, "pay_sq");
		String pay_date=this.getParam(request, "pay_date");//支付时间：格式为‘YYYYMMDDHHMMSS’
		String result=this.getParam(request, "result");//支付结果:Y：成功;以下返回码为正在处理中40008 50010 81008 81012 81029 81030 81033 81034 81035 81036  81037 81088 

		logger.info("订单号："+order_id+"，19PAY支付流水号："+pay_sq+"，支付结果："+result+"【Y：成功；其余为正在处理中】");
		//直接跳转页面------------
		//return "redirect:/query/queryOrderDetail.jhtml?fromFunc=payNotify&order_id=" + order_id+"&plat_order_id="+plat_order_id;
		if(result.equals("Y")){//支付成功
			return "redirect:/order/gotoPaySuccess.jhtml?order_id=" + order_id;
		}else{
			return "redirect:/query/queryOrderList.jhtml?type=hcp";
		}
	}
	
	
	//支付成功回调页面
	@RequestMapping("/gotoPaySuccess.jhtml")
	public String gotoPaySuccess(HttpServletRequest request, HttpServletResponse response){
		//TODO
		String order_id= this.getParam(request, "order_id");
		
		OrderInfo orderInfo = orderService.queryOrderInfo(order_id);
		List<Map<String, String>> detailList = orderService.queryOrderDetailList(order_id);
		
		String travel_time = orderInfo.getTravel_time();//yyyy-MM-dd
		String week = DateUtil.getWeek(DateUtil.stringToDate(travel_time, DateUtil.DATE_FMT1));
		request.setAttribute("orderInfo", orderInfo);
		request.setAttribute("detailList", detailList);
		request.setAttribute("ticketTypeMap", TrainConsts.getTicketType());
		request.setAttribute("seatTypeMap", TrainConsts.getSeatType());
		request.setAttribute("idsTypeMap", TrainConsts.getIdsType());
		request.setAttribute("week", week);
		
		if(Float.parseFloat(orderInfo.getBx_pay_money())>0){//购买保险
			return "book/bookSuccessBx";
		}else{
			return "book/bookSuccessNoBx";
		}
	}
	
	
	//重新支付
	@RequestMapping("/orderRepay.jhtml")
	public void orderRepay(HttpServletRequest request, HttpServletResponse response){
		String order_id= this.getParam(request, "order_id");
		OrderInfo orderInfo = orderService.queryOrderInfo(order_id);
		
		//验证该订单内车票是否可支付
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("from_city", orderInfo.getFrom_city());
		paramMap.put("to_city", orderInfo.getTo_city());
		paramMap.put("travel_time", orderInfo.getTravel_time());
		paramMap.put("method", "DGTrain");//调用方法
		String result = "";
		ObjectMapper mapper = new ObjectMapper();
		OuterSoukdNewData osnd = new OuterSoukdNewData();
		StringBuffer param = new StringBuffer();
		param.append("travel_time=").append(orderInfo.getTravel_time()).append("&from_station=").append(paramMap.get("from_city"))
		.append("&arrive_station=").append(paramMap.get("to_city")).append("&channel=").append("app");
		try{
			String jsonStr = HttpPostUtil.sendAndRecive(getSysSettingValue("query_left_ticket_url","query_left_ticket_url"),param.toString());//调用接口
			if("NO_DATAS".equals(jsonStr) || "ERROR".equals(jsonStr)){
				osnd = null;
			}else{
				osnd = mapper.readValue(jsonStr, OuterSoukdNewData.class);
				osnd.setSdate(orderInfo.getTravel_time());
			}
		}catch(Exception e){
			logger.error("查询异常！",e);
		}
		if(osnd != null){
			for (TrainNewData trainNewData : osnd.getDatajson()){
				if(orderInfo.getTrain_no().equals(trainNewData.getStation_train_code())){
					if("2".equals(orderInfo.getSeat_type())){
						if(!"-".equals(trainNewData.getZy_num())){
							result = "success";
						}
					}else if("3".equals(orderInfo.getSeat_type())){
						if(!"-".equals(trainNewData.getZe_num())){
							result = "success";
						}
					}else if("5".equals(orderInfo.getSeat_type())){
						if(!"-".equals(trainNewData.getRz_num())){
							result = "success";
						}
					}else if("6".equals(orderInfo.getSeat_type())){
						if(!"-".equals(trainNewData.getYw_num())){
							result = "success";
						}
					}else if("7".equals(orderInfo.getSeat_type())){
						if(!"-".equals(trainNewData.getRw_num())){
							result = "success";
						}
					}else if("8".equals(orderInfo.getSeat_type())){
						if(!"-".equals(trainNewData.getYz_num())){
							result = "success";
						}
					}
					break;
				}
			}
		}
		
		write2Response(response, result);
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
		String travel_time = orderInfo.getTravel_time();//yyyy-MM-dd
		String week = DateUtil.getWeek(DateUtil.stringToDate(travel_time, DateUtil.DATE_FMT1));
		request.setAttribute("orderInfo", orderInfo);
		request.setAttribute("orderInfoPs", orderInfoPs);
		request.setAttribute("detailList", detailList);		
		request.setAttribute("ticketTypeMap", TrainConsts.getTicketType());
		request.setAttribute("seatTypeMap", TrainConsts.getSeatType());
		request.setAttribute("idsTypeMap", TrainConsts.getIdsType());
		request.setAttribute("week", week);
		request.setAttribute("count", detailList.size());
		//request.setAttribute("pay_url", eopInfo.get("pay_url"));//支付地址
		request.setAttribute("trainTypeCn", this.getTrainTypeCn(orderInfo.getTrain_no()));
		double money = AmountUtil.add(Double.valueOf(orderInfo.getBx_pay_money()),Double.valueOf(orderInfo.getTicket_pay_money()));//保险
		
		if(!StringUtil.isEmpty(orderInfo.getPay_money())){
			request.setAttribute("totalPay4Show", String.valueOf(money));	
		}else{
			request.setAttribute("totalPay4Show", orderInfo.getPay_money());	
		}
		if(Float.parseFloat(orderInfo.getBx_pay_money())>0){//已经购买保险
			Integer mid_min = 20;
			if(null==orderInfo.getOut_ticket_time()){
				request.setAttribute("bookResult", "false");
				request.setAttribute("mid_req", "false");
			}else{
				request.setAttribute("bookResult", "true");
			}
			if(null!=orderInfo.getOut_ticket_time()){
				String now_date = DateUtil.dateToString(new Date(),DateUtil.DATE_HM);
				String out_ticket = DateUtil.dateToString(DateUtil.stringToDate(orderInfo.getOut_ticket_time(),DateUtil.DATE_FMT3),DateUtil.DATE_HM);
				mid_min = 25-Integer.valueOf(DateUtil.minuteDiff(out_ticket, now_date));
			}
			request.setAttribute("mid_min", mid_min);
			if(detailList.get(0).get("train_box")==null || StringUtils.isEmpty(detailList.get(0).get("train_box")) 
					|| detailList.get(0).get("seat_no")==null || StringUtils.isEmpty(detailList.get(0).get("seat_no"))){
				return "book/bookNobxConfirm";//跳转至不购买保险页面
			}else{
				return "book/bookConfirm";
			}
		}else{
			return "book/bookNobxConfirm";//跳转至不购买保险页面
		}
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
		String order_id = CreateIDUtil.createID("BC");
		//订单
		orderInfo.setOrder_id(order_id);
		orderInfo.setOrder_name(bookInfo.getFrom_city() + "/" + bookInfo.getTo_city());
		/*
		 * 必须先给主订单表pay_money等金额赋值后再处理明细表(涉及匹配产品的问题)
		 */
		orderInfo.setBx_pay_money(this.getBxTotalPay(bookInfo, request));
		orderInfo.setTicket_pay_money(this.getTicketTotalPay(bookInfo));
		orderInfo.setPay_money(this.getTotalPay4Show(bookInfo, request));
		orderInfo.setTicket_num(bookInfo.getBookDetailInfoList().size()+"");
		orderInfo.setOrder_status(TrainConsts.PRE_ORDER);//预下单
		String style = this.getSysSettingValueNow("cmpay_buy_ticket_style");
		if("00".equals(style)){
			orderInfo.setOrder_status(TrainConsts.PRE_ORDER);//预下单
		}else{
			orderInfo.setOrder_status(TrainConsts.PRE_BOOK);//先预订后支付
		}
		
//		orderInfo.setLink_name(bookInfo.getLink_name());
//		orderInfo.setLink_phone(bookInfo.getLink_phone());
		if(Float.parseFloat(this.getBxTotalPay(bookInfo, request))>0){
			orderInfo.setOrder_level("1");//订单级别：0：普通订单；1：VIP订单
		}else{
			orderInfo.setOrder_level("0");
		}
		
		if(!StringUtils.isEmpty(bookInfo.getSeat_type())
				&& (TrainConsts.SEAT_8.equals(bookInfo.getSeat_type()) || TrainConsts.SEAT_2.equals(bookInfo.getSeat_type()) ||TrainConsts.SEAT_3.equals(bookInfo.getSeat_type()))
				&& !StringUtils.isEmpty(bookInfo.getWz_ext())
				&& "1".equals(bookInfo.getWz_ext())){//硬座、一等座、二等座选择无座备选
			String ext_seat = "" + TrainConsts.SEAT_9 + "," + bookInfo.getDanjia();
			orderInfo.setExt_seat(ext_seat);
		}
		
		//配送
		orderInfoPs.setOrder_id(order_id);
//		orderInfoPs.setLink_name(bookInfo.getLink_name());
//		orderInfoPs.setLink_phone(bookInfo.getLink_phone());
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
			String cp_id = CreateIDUtil.createID("BCCP");//车票id
			orderInfoCp.setCp_id(cp_id);
			orderInfoCp.setOrder_id(order_id);	
			orderInfoCp.setTrain_no(bookInfo.getTrain_no());
			orderInfoCp.setFrom_station(bookInfo.getFrom_city());
			orderInfoCp.setArrive_station(bookInfo.getTo_city());
			orderInfoCp.setFrom_time(bookInfo.getFrom_time().substring(11));
			orderInfoCp.setArrive_time(bookInfo.getTo_time().substring(11));
			orderInfoCp.setTravel_time(bookInfo.getTravelTime());
			orderInfoCp.setUser_name(bookDetailInfo.getUser_name().trim());//姓名
			orderInfoCp.setTicket_type(bookDetailInfo.getTicket_type());//车票类型0：成人票 1：儿童票
			orderInfoCp.setIds_type(bookDetailInfo.getIds_type());
			orderInfoCp.setUser_ids(bookDetailInfo.getUser_ids().trim());//证件号
			orderInfoCp.setPay_money(bookInfo.getDanjia());
			orderInfoCp.setSeat_type(bookInfo.getSeat_type());//坐席
			orderInfoCpList.add(orderInfoCp);
			
			//保险单价大于0则保存该保险记录
			String sale_price = bookDetailInfo.getSale_price();//保险单价
			if(!StringUtils.isEmpty(sale_price) && (Double.parseDouble(sale_price) > 0 || bookDetailInfo.getProduct_id().equals("BX_0"))){
				//查询保险公司渠道: 1、快保 2、合众 3、平安保险
				String bx_channel = "1";
				//保险
				OrderInfoBx orderInfoBx = new OrderInfoBx();
				orderInfoBx.setBx_id(CreateIDUtil.createID("BCBX"));
				orderInfoBx.setOrder_id(order_id);
				orderInfoBx.setCp_id(cp_id);//车票id
				orderInfoBx.setUser_name(bookDetailInfo.getUser_name().trim());//姓名
				orderInfoBx.setIds_type(bookDetailInfo.getIds_type());
				orderInfoBx.setUser_ids(bookDetailInfo.getUser_ids().trim());
				orderInfoBx.setFrom_name(bookInfo.getFrom_city());//出发城市
				orderInfoBx.setTo_name(bookInfo.getTo_city());//到达城市
				orderInfoBx.setBx_status("0");//未发送
				orderInfoBx.setPay_money(bookDetailInfo.getSale_price());
				orderInfoBx.setProduct_id(bookDetailInfo.getProduct_id());
				orderInfoBx.setEffect_date(bookInfo.getFrom_time()+":00");
				orderInfoBx.setTrain_no(bookInfo.getTrain_no());//车次
				//orderInfoBx.setTelephone(bookInfo.getLink_phone());//联系人电话
				orderInfoBx.setBx_channel(bx_channel);
				if(bookDetailInfo.getTicket_type().equals("0")){//车票类型 0：成人票 1：儿童票
					orderInfoBx.setBx_type("1");//保险类型：0、儿童；1、成人
				}else{
					orderInfoBx.setBx_type("0");
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
			logger.info("车票的单价是："+bookInfo.getDanjia()+"，出发："+bookInfo.getFrom_city()+"，到达："+bookInfo.getTo_city()
					+"，乘客信息："+bookInfo.getBookDetailInfoList()+"，订单号："+bookInfo.getOrder_id()+"，乘客坐席："+bookInfo.getSeat_type());
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
			String provinceId = "";
			String cityId = "";
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
}
