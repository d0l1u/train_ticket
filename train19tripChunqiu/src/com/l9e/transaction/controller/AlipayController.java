package com.l9e.transaction.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alipay.services.AlipayService;
import com.alipay.util.AlipayNotify;
import com.l9e.common.BaseController;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.service.TradeService;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.transaction.vo.TradeVo;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.XmlUtil;

@Controller
@RequestMapping("/alipay")
public class AlipayController extends BaseController {

	private static final Logger logger = Logger
			.getLogger(AlipayController.class);
	@Resource
	private TradeService tradeService;
	@Resource
	private OrderService orderService;
	
	/**
	 * 支付宝异步通知接口
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/notify.jhtml")
	public void notify(HttpServletRequest request, HttpServletResponse response) {
		String requestString = request.getRequestURL().toString();
		logger.info("【支付宝异步通知接口】支付宝通知的完整字符串为：" + requestString);
		// 获取支付宝POST过来反馈信息
		Map<String, String> params = new HashMap<String, String>();
		Map requestParams = request.getParameterMap();
		logger.info("");
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			// valueStr = new String(valueStr.getBytes("ISO-8859-1"), "UTF-8");
			params.put(name, valueStr);
		}

		// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
		logger.info("【支付宝异步通知接口】支付宝通知的参数为：" + params);
		String trade_no = this.getParam(request, "trade_no"); // 支付宝交易号
		String order_no = this.getParam(request, "out_trade_no"); // 获取订单号
		String total_fee = this.getParam(request, "total_fee");// 获取总金额
		String body = "";
		String subject = "";
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			logger.error("【支付宝异步通知接口】response获取printWriter发生异常！");
			e.printStackTrace();
		}
		try {
			subject = new String(request.getParameter("subject").getBytes(
					"ISO-8859-1"), "utf-8");
			if (request.getParameter("body") != null) {
				body = new String(request.getParameter("body").getBytes(
						"ISO-8859-1"), "utf-8");// 商品描述、订单备注、描述
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("【支付宝异步通知接口】subject或者body编码发生错误！");
			e.printStackTrace();
		}
		// 商品名称、订单名称

		String buyer_email = request.getParameter("buyer_email"); // 买家支付宝账号
		String trade_status = request.getParameter("trade_status"); // 交易状态
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("order_id", order_no);
		paramMap.put("trade_type", TradeVo.INCOME);
		List<TradeVo> tradeList = tradeService.queryTrade(paramMap);
		// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
		if (AlipayNotify.verify(params)) {// 验证成功
			// ////////////////////////////////////////////////////////////////////////////////////////
			// 请在这里加上商户的业务逻辑程序代码

			// ——请根据您的业务逻辑来编写程序（以下代码仅作参考）——

			if (trade_status.equals("TRADE_FINISHED")
					|| trade_status.equals("TRADE_SUCCESS")) {
				logger.info("【支付宝异步通知接口】验证消息证实是支付宝发出的合法消息！");
				// 判断该笔订单是否在商户网站中已经做过处理（可参考“集成教程”中“3.4返回数据处理”）
				// 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				// 如果有做过处理，不执行商户的业务程序
				String notify_time = request.getParameter("notify_time");
				TradeVo trade = new TradeVo();
				OrderInfo orderInfo = orderService.queryOrderInfo(order_no);
				if (orderInfo != null) {
					if ("00".equals(orderInfo.getOrder_status())
							|| "99".equals(orderInfo.getOrder_status())
							|| "11".equals(orderInfo.getOrder_status())
							|| orderInfo.getOrder_status() == null
							|| orderInfo.getOrder_status().length() == 0) {
						Map<String, String> orderInfoMap = new HashMap<String, String>();
						orderInfoMap.put("order_status", "11");
						orderInfoMap.put("asp_order_id", order_no);
						orderService.updateOrderStatus(orderInfoMap);
					} else {
						logger.warn("【支付宝异步通知接口】数据库中订单号为" + order_no
								+ "的状态不为预下单或支付失败， 不予更改为支付成功！");
					}
				} else {
					logger.error("【支付宝异步通知接口】数据库中没有订单号为：" + order_no + "的订单！");
				}

				if (trade_status.equals("TRADE_FINISHED")) {
					logger.info("【支付宝异步通知接口】交易状态trade_status为TRADE_FINISHED！");
					trade.setTrade_status(TradeVo.TRADE_FINISHED);
				} else {
					logger.info("【支付宝异步通知接口】交易状态trade_status为TRADE_SUCCESS！");
					trade.setTrade_status(TradeVo.TRADE_SUCCESS);
				}
				trade.setTrade_time(notify_time);
				if (tradeList.size() <= 0) {
					trade.setTrade_id(CreateIDUtil.createID("CQT"));
					trade.setOrder_id(order_no);
					trade.setTrade_no(trade_no);
					trade.setChannel("chunqiu");
					trade.setBuyer_id(buyer_email);
					trade.setTrade_fee(total_fee);
					trade.setTrade_channel(TradeVo.ALIPAY_TRADE_CHANNEL);
					trade.setTrade_type(TradeVo.INCOME);
					tradeService.insertIntoTrade(trade);
				} else {
					tradeService.updateTrade(trade);
				}

				out.println("success"); // 请不要修改或删除
			} else {
				logger.info("【支付宝异步通知接口】订单号：" + order_no + "的状态为："
						+ trade_status);
				out.println("success"); // 请不要修改或删除
				
			}

			// ——请根据您的业务逻辑来编写程序（以上代码仅作参考）——

			// ////////////////////////////////////////////////////////////////////////////////////////
		} else {// 验证失败
			logger.error("【支付宝异步通知接口】验证消息不是支付宝发出的合法消息！");
			out.println("fail");
		}

	}

	/**
	 * 支付宝同步通知接口
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/return.jhtml")
	public String syncReturn(HttpServletRequest request,
			HttpServletResponse response) {

		String requestString = request.getRequestURL().toString();
		if(!requestString.contains("&")){
			logger.info("【支付宝同步通知接口】支付宝请求为post请求！");
			requestString = getPostString(request, response);
		}
		logger.info("【支付宝同步通知接口】支付宝通知的完整字符串为：" + requestString);
		// 获取支付宝GET过来反馈信息
		Map<String, String> params = new HashMap<String, String>();
		Map requestParams = request.getParameterMap();
		/*PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			logger.error("【支付宝同步通知接口】response获取printWriter发生异常！");
			e.printStackTrace();
		}*/
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			try {
				valueStr = new String(valueStr.getBytes(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				logger.error("【支付宝同步通知接口】参数解决乱码时发生异常！");
				e.printStackTrace();
			}
			params.put(name, valueStr);
		}

		logger.info("【支付宝同步通知接口】支付宝通知的参数为：" + params);
		// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
		String trade_no = request.getParameter("trade_no"); // 支付宝交易号
		String order_no = request.getParameter("out_trade_no"); // 获取订单号
		String total_fee = request.getParameter("total_fee"); // 获取总金额
		String subject = "";
		String body = "";

		try {
			subject = new String(request.getParameter("subject").getBytes(
					"ISO-8859-1"), "utf-8");

			if (request.getParameter("body") != null) {
				body = new String(request.getParameter("body").getBytes(
						"ISO-8859-1"), "utf-8");// 商品描述、订单备注、描述
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("【支付宝同步通知接口】subject或者body编码发生错误！");
			e.printStackTrace();
		}
		// 商品名称、订单名称
		String buyer_email = request.getParameter("buyer_email"); // 买家支付宝账号
		String trade_status = request.getParameter("trade_status"); // 交易状态
	
		// 查询数据库中order_id为order_no的流水
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("order_id", order_no);
		paramMap.put("trade_type", TradeVo.INCOME);
		logger.info("【支付宝同步通知接口】查询数据库中的交易流水参数为：" + paramMap);
		List<TradeVo> tradeList = tradeService.queryTrade(paramMap);
		logger.info("【支付宝同步通知接口】查询数据库中符合条件的流水条数为：" + tradeList.size());
		// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//

		// 计算得出通知验证结果
		boolean verify_result = AlipayNotify.verify(params);

		if (verify_result) {// 验证成功
			logger.info("【支付宝同步通知接口】支付宝通知验证成功！");
			// ////////////////////////////////////////////////////////////////////////////////////////
			// 请在这里加上商户的业务逻辑程序代码

			// ——请根据您的业务逻辑来编写程序（以下代码仅作参考）——

			if (trade_status.equals("TRADE_FINISHED")
					|| trade_status.equals("TRADE_SUCCESS")) {
				// 判断该笔订单是否在商户网站中已经做过处理（可参考“集成教程”中“3.4返回数据处理”）
				// 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				// 如果有做过处理，不执行商户的业务程序

				String notify_time = request.getParameter("notify_time");
				TradeVo trade = new TradeVo();
				OrderInfo orderInfo = orderService.queryOrderInfo(order_no);
				if (orderInfo != null) {
					logger.info("【支付宝同步通知接口】数据库中订单号为：" + order_no + "的原始状态为："
							+ orderInfo.getOrder_status());
					if ("00".equals(orderInfo.getOrder_status())
							|| "99".equals(orderInfo.getOrder_status())
							|| "11".equals(orderInfo.getOrder_status())
							|| orderInfo.getOrder_status() == null
							|| orderInfo.getOrder_status().length() == 0) {
						Map<String, String> orderInfoMap = new HashMap<String, String>();
						orderInfoMap.put("order_status", "11");
						orderInfoMap.put("asp_order_id", order_no);
						orderService.updateOrderStatus(orderInfoMap);
					} else {
						logger.warn("【支付宝同步通知接口】数据库中订单号为" + order_no
								+ "的状态不为预下单或支付失败， 不予更新inner_orderinfo");
					}
				} else {
					logger.error("【支付宝同步通知接口】数据库中没有订单号为：" + order_no + "的订单！");
				}

				if (trade_status.equals("TRADE_FINISHED")) {
					logger.info("【支付宝同步通知接口】交易状态trade_status为TRADE_FINISHED！");
					trade.setTrade_status(TradeVo.TRADE_FINISHED);
				} else {
					logger.info("【支付宝同步通知接口】交易状态trade_status为TRADE_SUCCESS！");
					trade.setTrade_status(TradeVo.TRADE_SUCCESS);
				}
				trade.setTrade_time(notify_time);
				if (tradeList.size() <= 0) {
					trade.setTrade_id(CreateIDUtil.createID("CQT"));
					trade.setOrder_id(order_no);
					trade.setTrade_no(trade_no);
					trade.setChannel("chunqiu");
					trade.setBuyer_id(params.get("buyer_id"));
					trade.setBuyer_name(buyer_email);
					trade.setSeller_id(params.get("seller_id"));
					trade.setSeller_name(params.get("seller_email"));
					trade.setTrade_fee(total_fee);

					trade.setTrade_channel(TradeVo.ALIPAY_TRADE_CHANNEL);
					trade.setTrade_type(TradeVo.INCOME);
					logger.info("【支付宝同步通知接口】插入流水详细信息为：" + trade.toString());
					tradeService.insertIntoTrade(trade);
				} else {
					logger.info("【支付宝同步通知接口】更新流水详细信息为：" + trade.toString());
					tradeService.updateTrade(trade);
				}
			} else {
				logger.info("【支付宝同步通知接口】交易状态trade_status为:"+trade_status);
			}

			// 该页面可做页面美工编辑
			//out.println("验证成功<br />");
			//out.println("trade_no=" + trade_no);

			// ——请根据您的业务逻辑来编写程序（以上代码仅作参考）——

			// ////////////////////////////////////////////////////////////////////////////////////////
		} else {
			// 该页面可做页面美工编辑
			logger.error("【支付宝同步通知接口】支付宝通知验证失败！");
			//out.println("验证失败");
		}
		/*if (out != null) {
			out.close();
		}*/
		return "redirect:/chunqiu/query/queryOrderDetail.jhtml?order_id=" + order_no;
	}

	public String pay(HttpServletRequest request, HttpServletResponse response) {
		// 必填参数//
		PrintWriter out = null;
		String subject = "火车票";
		String body = "";
		try {
			out = response.getWriter();
		} catch (IOException e) {
			logger.error("【支付宝支付接口】response获取printWriter发生异常！");
			e.printStackTrace();
		}

		// 请与贵网站订单系统中的唯一订单号匹配
		// String out_trade_no = UtilDate.getOrderNum();
		String out_trade_no = request.getParameter("order_id");

		try {
			// 订单名称，显示在支付宝收银台里的“商品名称”里，显示在支付宝的交易管理的“商品名称”的列表里。
			subject = new String(request.getParameter("subject").getBytes(
					"ISO-8859-1"), "utf-8");
			// 订单描述、订单详细、订单备注，显示在支付宝收银台里的“商品描述”里
			body = new String(request.getParameter("alibody").getBytes(
					"ISO-8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("【支付宝支付接口】subject或者body编码发生错误！");
			e.printStackTrace();
		}
		// 订单总金额，显示在支付宝收银台里的“应付总额”里
		String total_fee = request.getParameter("total_fee");

		// 扩展功能参数——默认支付方式//

		// 默认支付方式，取值见“即时到帐接口”技术文档中的请求参数列表
		String paymethod = "";
		// 默认网银代号，代号列表见“即时到帐接口”技术文档“附录”→“银行列表”
		String defaultbank = "";

		// 扩展功能参数——防钓鱼//

		// 防钓鱼时间戳
		String anti_phishing_key = "";
		// 获取客户端的IP地址，建议：编写获取客户端IP地址的程序
		String exter_invoke_ip = request.getRemoteAddr();
		// 注意：
		// 1.请慎重选择是否开启防钓鱼功能
		// 2.exter_invoke_ip、anti_phishing_key一旦被设置过，那么它们就会成为必填参数
		// 3.开启防钓鱼功能后，服务器、本机电脑必须支持远程XML解析，请配置好该环境。
		// 4.建议使用POST方式请求数据
		// 示例：
		// anti_phishing_key = AlipayService.query_timestamp(); //获取防钓鱼时间戳函数
		// exter_invoke_ip = "202.1.1.1";

		// 扩展功能参数——其他///

		// 自定义参数，可存放任何内容（除=、&等特殊字符外），不会显示在页面上
		String extra_common_param = "";
		// 默认买家支付宝账号
		String buyer_email = "";
		// 商品展示地址，要用http:// 格式的完整路径，不允许加?id=123这类自定义参数
		// String show_url = "http://www.xxx.com/order/myorder.jsp";
		String show_url = "";

		// 扩展功能参数——分润(若要使用，请按照注释要求的格式赋值)//

		// 提成类型，该值为固定值：10，不需要修改
		String royalty_type = "";
		// 提成信息集
		String royalty_parameters = "";
		// 注意：
		// 与需要结合商户网站自身情况动态获取每笔交易的各分润收款账号、各分润金额、各分润说明。最多只能设置10条
		// 各分润金额的总和须小于等于total_fee
		// 提成信息集格式为：收款方Email_1^金额1^备注1|收款方Email_2^金额2^备注2
		// 示例：
		// royalty_type = "10"
		// royalty_parameters = "111@126.com^0.01^分润备注一|222@126.com^0.01^分润备注二"

		// ////////////////////////////////////////////////////////////////////////////////

		// 把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("payment_type", "1");
		sParaTemp.put("out_trade_no", out_trade_no);
		sParaTemp.put("subject", subject);
		sParaTemp.put("body", body);
		sParaTemp.put("total_fee", total_fee);
		sParaTemp.put("show_url", show_url);
		sParaTemp.put("paymethod", paymethod);
		sParaTemp.put("defaultbank", defaultbank);
		sParaTemp.put("anti_phishing_key", anti_phishing_key);
		sParaTemp.put("exter_invoke_ip", exter_invoke_ip);
		sParaTemp.put("extra_common_param", extra_common_param);
		sParaTemp.put("buyer_email", buyer_email);
		sParaTemp.put("royalty_type", royalty_type);
		sParaTemp.put("royalty_parameters", royalty_parameters);

		// 构造函数，生成请求URL
		String sHtmlText = AlipayService.create_direct_pay_by_user(sParaTemp);
		out.println(sHtmlText);

		return null;
	}
	
	@RequestMapping("/turnToAlipay.jhtml")
	public String turnToAlipay(HttpServletRequest request,
			HttpServletResponse response) {
		//请与贵网站订单系统中的唯一订单号匹配
		String out_trade_no = request.getParameter("order_id");
		//订单名称，显示在支付宝收银台里的“商品名称”里，显示在支付宝的交易管理的“商品名称”的列表里。
		String subject = request.getParameter("subject");// new String(request.getParameter("subject").getBytes("ISO-8859-1"), "utf-8");
		//订单描述、订单详细、订单备注，显示在支付宝收银台里的“商品描述”里
		String body = request.getParameter("alibody");//new String(request.getParameter("alibody").getBytes("ISO-8859-1"), "utf-8");
		//订单总金额，显示在支付宝收银台里的“应付总额”里
		String total_fee = request.getParameter("total_fee");
		
	//	String from_station = request.getParameter("from_station");
		//String to_station = request.getParameter("to_station");
		//String start_time = request.getParameter("start_time");
		//try {
	//	subject = from_station +"--" + to_station;
		//} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		//}
	//	OrderServiceImpl orderService = new OrderServiceImpl();
		Map<String, String> payInfoMap = new HashMap<String, String>();
		payInfoMap.put("pay_channel", "alipay");
		payInfoMap.put("order_id", out_trade_no);
		orderService.updateOrderPayNo(payInfoMap);
		//HttpSession session = request.getSession();
		request.setAttribute("order_id", out_trade_no);
		request.setAttribute("subject", subject);
		request.setAttribute("alibody", body);
		request.setAttribute("total_fee", total_fee);
		return "book/alipayto";//.jsp?order_id=" + out_trade_no + "&subject=" + subject + "&alibody=" + body + "&total_fee=" + total_fee; 
		
	}
	

	/**
	 * 支付宝退款异步通知接口
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/refundNotify.jhtml")
	public void refundNotify(HttpServletRequest request,
			HttpServletResponse response) {

		String requestString = request.getRequestURL().toString();
		logger.info("【支付宝退款异步通知接口】支付宝通知的完整字符串为：" + requestString);
		// 获取支付宝POST过来反馈信息
		Map<String, String> params = new HashMap<String, String>();
		Map requestParams = request.getParameterMap();
		logger.info("【支付宝退款异步通知接口】批次号为" + request.getParameter("batch_no")
				+ "订单号成功退款了" + request.getParameter("success_num") + "笔");
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			// valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}

		PrintWriter out;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			logger.error("【支付宝退款异步通知接口】response获得out发生异常！");
			return;
		}

		// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
		String result_details = request.getParameter("result_details"); // 处理结果详情
		logger.info("【支付宝退款异步通知接口】退款详细数据为：：" + result_details);
		// ----------------result_details格式-------------
		// 第一笔交易#第二笔交易#第三笔交易...#第N笔交易

		// ----------------第N笔交易格式------------------
		// 第一种：交易退款数据集$收费退款数据集|分润退款数据集|分润退款数据集...|分润退款数据集
		// 第二种：交易退款数据集$收费退款数据集
		// 第三种：交易退款数据集|分润退款数据集|分润退款数据集...|分润退款数据集
		// 第四种：交易退款数据集

		// ----------------交易退款信息格式----------------
		// 原付款支付宝交易号^退款总金额^处理结果码

		// ----------------收费退款数据集格式--------------
		// 被收费人支付宝账号[交易时支付宝收取服务费的账户]^被收费人支付宝账号对应用户ID[2088开头16位纯数字]^退款金额^处理结果码

		// ----------------分润退款数据集------------------
		// 转出人支付宝账号[原分润帐户]^转出人支付宝账号对应用户ID[2088开头16位纯数字]^转入人支付宝账号[平台中间帐户]^转入人支付宝账号对应用户ID^退款金额^处理结果代码

		// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//

		logger.info("【支付宝退款异步通知接口】params::" + params);
		if (AlipayNotify.verify(params)) {// 验证成功

			logger.info("【支付宝退款异步通知接口】验证消息是支付宝发出的合法消息！");
			// ////////////////////////////////////////////////////////////////////////////////////////
			// 请在这里加上商户的业务逻辑程序代码
			TradeVo trade = new TradeVo();
			trade.setBatch_no(params.get("batch_no"));
			String[] trades = result_details.split("#");
			
		/*
			if(Integer.parseInt(request.getParameter("success_num")) > trades.length) {
				logger.error("【支付宝退款异步通知接口】退交易成功的笔数大于交交易");
				return;
			}
		*/
			for (String tradeStr : trades) {
				logger.info("退款信息为：" + tradeStr);
				String[] refundDataSet = tradeStr.split("\\$");
				String refundResult = refundDataSet[0];
				String[] tradeInfo = refundResult.split("\\^");
				String trade_no = tradeInfo[0];
				String trade_fee = tradeInfo[1];
				String trade_status = tradeInfo[2];
				trade.setTrade_no(trade_no);
				trade.setTrade_fee(trade_fee);
				if ("success".equalsIgnoreCase(trade_status.trim())) {
					trade.setTrade_status(TradeVo.REFUND_SUCCESS);
				}else {
					trade.setTrade_status(TradeVo.REFUND_FAILURE);
					trade.setFail_reason(trade_status);
				}
				Map<String, Object> tradeMap = new HashMap<String, Object>();
				tradeMap.put("batch_no", params.get("batch_no"));
				tradeMap.put("trade_no", trade_no);//params.get("trade_no"));
				tradeMap.put("trade_type", TradeVo.EXPENSES);
				List<TradeVo> tradeList = tradeService.queryTrade(tradeMap);
				
				if(tradeList == null || tradeList.size() == 0) {
					logger.error("【支付宝退款异步通知接口】数据库内没有批次号为：" + params.get("batch_no") + ", 流水号为：" + trade_no + "的退款交易流水！");
					continue;
				}else {
					if(tradeList.size() == 1) {
						TradeVo databaseTrade = tradeList.get(0);
						Map<String, String> orderMap = new HashMap<String, String>();
						//orderMap.put("stream_id", map.get("stream_id"));
						orderMap.put("order_id", databaseTrade.getOrder_id());
						orderMap.put("refund_seq", databaseTrade.getTrade_seq());
						
						//判断是否做过成功处理
						if(TradeVo.REFUND_SUCCESS.equalsIgnoreCase(databaseTrade.getTrade_status())){
							logger.info("【支付宝退款异步通知接口】数据库内批次号为：" + params.get("batch_no") + ", 流水号为：" + trade_no + "的退款交易流水已成功，未更新数据库！");
							logger.info("【支付宝退款异步通知接口】数据库内批次号为：" + params.get("batch_no") + ", 流水号为：" + trade_no + "的退款交易流水退款金额为：" + databaseTrade.getTrade_fee());
							logger.info("【支付宝退款异步通知接口】支付宝通知批次号为：" + params.get("batch_no") + ", 流水号为：" + trade_no + "的退款交易流水退款金额为：" + trade_fee);
							orderMap.put("refund_status", TrainConsts.REFUND_STREAM_REFUND_FINISH);//成功退款
							orderService.updateOrderStreamStatus(orderMap);//更新退款请求开始
							continue;
						}else{
							logger.error("【支付宝退款异步通知接口】数据库内批次号为：" + params.get("batch_no") + ", 流水号为：" +trade_no + "的退款交易流水状态为" + databaseTrade.getTrade_status() 
									+ ", 交易金额为：" + databaseTrade.getTrade_fee());
							tradeService.updateTrade(trade);
							orderMap.put("refund_status", TrainConsts.REFUND_STREAM_REFUND_FINISH);//成功退款
							orderService.updateOrderStreamStatus(orderMap);//更新退款请求开始
						}
					}else {
						logger.error("【支付宝退款异步通知接口】数据库内有" + tradeList.size() + "笔批次号为：" + params.get("batch_no") + ", 流水号为：" + trade_no + "的退款交易流水，未更新数据库！");
						continue;
					}
				}
			}
			

			// ——请根据您的业务逻辑来编写程序（以下代码仅作参考）——

			// 判断是否在商户网站中已经做过了这次通知返回的处理（可参考“集成教程”中“3.4返回数据处理”）
			// 如果没有做过处理，那么执行商户的业务程序
			// 如果有做过处理，那么不执行商户的业务程序

			out.println("success"); // 请不要修改或删除

			// ——请根据您的业务逻辑来编写程序（以上代码仅作参考）——

			// ////////////////////////////////////////////////////////////////////////////////////////
		} else {// 验证失败
			logger.error("【支付宝退款异步通知接口】验证消息不是支付宝发出的合法消息！");
			out.println("fail");
		}
		if (out != null) {
			out.close();
		}
		return;
	}
	
	
	/**
	 * 支付宝交易信息查询
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryAlipay.jhtml")
	public void queryAlipay(HttpServletRequest request, HttpServletResponse response) {
		String requestString = request.getRequestURL().toString();
		logger.info("【支付宝交易信息查询接口】支付宝查询结果的完整字符串为：" + requestString);
		
		////////////////////////////////////请求参数//////////////////////////////////////
				
		//商户网站已经付款完成的商户网站订单号
		String out_trade_no = request.getParameter("out_trade_no");
		
		//已经付款完成的支付宝交易号，与商户网站订单号out_trade_no相对应
		String trade_no = request.getParameter("trade_no");
		
		//out_trade_no、trade_no须至少填写一项
		
		//////////////////////////////////////////////////////////////////////////////////
		
		//把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("out_trade_no", out_trade_no);
		sParaTemp.put("trade_no", trade_no);
		
		//构造函数，生成请求URL
		String sHtmlText = "";
		try {
			sHtmlText = AlipayService.single_trade_query(sParaTemp);
			logger.info("【支付宝交易信息查询接口】支付宝查询结果的完整字符串::" + sHtmlText);
			Map<String, Object> resultMap = XmlUtil.Dom2Map(sHtmlText);
			if("T".equalsIgnoreCase((String)resultMap.get("is_success"))){
				
			}else {
				
			}
		} catch (Exception e) {
			logger.info("【支付宝交易信息查询接口】支付宝查询过程中发生异常：" + e.getCause());
			e.printStackTrace();
		}
	
	}
	
	private String getPostString(HttpServletRequest request,
			HttpServletResponse response){
	//	PrintWriter writer = response.getWriter();
		Map<String, String[]> params = request.getParameterMap();
		String queryString = "";
		for (String key : params.keySet()) {
			String[] values = params.get(key);
			for (int i = 0; i < values.length; i++) {
				String value = values[i];
				queryString += key + "=" + value + "&";
			}
		}
		// 去掉最后一个空格
		queryString = queryString.substring(0, queryString.length() - 1);
		return queryString;
		//writer.println("POST " + request.getRequestURL() + " " + queryString);
	}
}
