package com.l9e.transaction.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.jiexun.iface.bean.OrderBean;
import com.jiexun.iface.util.ASPUtil;
import com.jiexun.iface.util.StringUtil;
import com.l9e.common.BaseController;
import com.l9e.common.LoginUserInfo;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.JoinUsService;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.vo.AgentVo;
import com.l9e.transaction.vo.BookDetailInfo;
import com.l9e.transaction.vo.BookInfo;
import com.l9e.transaction.vo.DBStudentInfo;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.transaction.vo.OrderInfoBx;
import com.l9e.transaction.vo.OrderInfoCp;
import com.l9e.transaction.vo.OrderInfoPs;
import com.l9e.transaction.vo.Outer12306NewData;
import com.l9e.transaction.vo.OuterSoukdNewData;
import com.l9e.transaction.vo.ProductVo;
import com.l9e.transaction.vo.RobTicket_CP;
import com.l9e.transaction.vo.TrainNewData;
import com.l9e.util.AmountUtil;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.HttpPostUtil;
import com.l9e.util.JedisUtil;
import com.l9e.util.MemcachedUtil;
import com.l9e.util.RobTicketUtils;

import redis.clients.jedis.Jedis;

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
	private JoinUsService joinUsService;
	
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

	@Value("#{propertiesReader[query_left_ticket_url]}")
	protected String query_left_ticket_url;//12306新接口
	
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
		if (loginUser==null) {
			loginUser = RobTicketUtils.getTestUser(loginUser);
			request.getSession().setAttribute(
					TrainConsts.INF_LOGIN_USER,loginUser);
		}
		String provinceId = loginUser.getProvinceId();
		String cityId = loginUser.getCityId();
		String agentId = loginUser.getAgentId();
		String agentName = loginUser.getAgentName();
		String eop_order_url = loginUser.getEop_order_url();//eop下单地址
		
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setAt_province_id(provinceId);
		orderInfo.setAt_city_id(cityId);
		orderInfo.setDealer_id(agentId);
		orderInfo.setDealer_name(agentName);
		
		OrderInfoPs orderInfoPs = new OrderInfoPs();
		List<OrderInfoCp> orderInfoCpList = new ArrayList<OrderInfoCp>();
		List<OrderInfoBx> orderInfoBxList = new ArrayList<OrderInfoBx>();
		
		//zuoyuxing 2013-12-02
		String product_id = this.getParam(request, "product_id");
		logger.info("product_id："+product_id);
		for(BookDetailInfo detailInfo : bookInfo.getBookDetailInfoList()){
			detailInfo.setProduct_id(product_id);
		}
		
		this.groupData(bookInfo, orderInfo, orderInfoPs, orderInfoCpList, orderInfoBxList, request);//组合封装数据
		Jedis jedis = null;
		try {
			String userName12306 = getParam(request, "userName12306");
			String password12306 = getParam(request, "password12306");
			if (!userName12306.equals("")&&!password12306.equals("")) {
				Map<String, String> map = new HashMap<String, String>();
				map.put(orderInfo.getOrder_id(), userName12306+"##"+password12306);
				jedis = JedisUtil.getJedis();
				jedis.hmset("12306UserInfo", map);
				logger.info("前台用户输入自有账号,Redis 缓存成功!--"+orderInfo.getOrder_id());
			}
			
		} catch (Exception e) {
			logger.info("前台用户输入自有账号,Redis 缓存失败!--"+orderInfo.getOrder_id());
		}finally {
			if (jedis!=null) {
				jedis.close();
			}
		}
		
		Map<String, String> bxfpMap = null;//保险发票
		if("1".equals(this.getParam(request, "fpNeed"))){
			bxfpMap = new HashMap<String, String>(4);
			bxfpMap.put("order_id", orderInfo.getOrder_id());
			bxfpMap.put("fp_receiver", this.getParam(request, "fp_receiver"));
			bxfpMap.put("fp_phone", this.getParam(request, "fp_phone"));
			bxfpMap.put("fp_zip_code", this.getParam(request, "fp_zip_code"));
			bxfpMap.put("fp_address", this.getParam(request, "fp_address"));
		}

		String choose_type_value = this.getParam(request, "choose_type_value");
		String ps_money ="25";
		logger.info("choose_type_value："+choose_type_value);
		Map<String, String> spsmMap = null;//送票上门
		if("choose_type_22".equals(choose_type_value)){
			spsmMap = new HashMap<String, String>(10);
			spsmMap.put("order_id", orderInfo.getOrder_id());
			spsmMap.put("choose_seat_num", this.getParam(request, "choose_seat_num"));
			spsmMap.put("choose_ext", this.getParam(request, "choose_ext"));
			spsmMap.put("link_name_ps", this.getParam(request, "link_name_ps"));
			spsmMap.put("link_phone_ps", this.getParam(request, "link_phone_ps"));
			spsmMap.put("province", "110000");
			spsmMap.put("city", "110100");
//			spsmMap.put("province", this.getParam(request, "province"));
//			spsmMap.put("city", this.getParam(request, "city"));
			spsmMap.put("district", this.getParam(request, "district"));
			spsmMap.put("ps_address", this.getParam(request, "ps_address"));
			spsmMap.put("pay_money", ps_money);
			spsmMap.put("buy_money", ps_money);
			logger.info("spsmMap:"+ spsmMap.toString());
			
			orderInfoPs.setLink_name(this.getParam(request, "link_name_ps"));
			orderInfoPs.setLink_phone(this.getParam(request, "link_phone_ps"));
			orderInfo.setTicket_ps_type("11");
		}
		
		//向火车票库下单，返回火车票订单号
		String order_id = orderService.addOrder(orderInfo, orderInfoPs, orderInfoCpList, orderInfoBxList, bxfpMap,spsmMap);
		
		logger.info("[火车票下单]：火车票asp_order_id:" + order_id);
		
		//向EOP下单
		OrderBean bean = new OrderBean();
		bean.setAsp_verify_key(asp_verify_key);
		bean.setPartner_id(asp_id);
		bean.setAgent_id(agentId);
		bean.setAsp_order_id(order_id);
		bean.setOrder_name("火车票");
		bean.setBiz_id(asp_biz_id);
		bean.setGoods_id(asp_product_id);
		bean.setGoods_name("火车票 "+orderInfo.getFrom_city()+"-"+orderInfo.getTo_city()+" "+orderInfo.getTrain_no()+" "+orderInfo.getFrom_time().substring(5));
		bean.setBuy_cnt(String.valueOf(orderInfoCpList.size()));//eop商品数量
		bean.setGoods_price(orderInfo.getPay_money());//eop商品单价
		/*选择送票上门再加上20元配送费和5元纸票费*/
		String totalPay = getTotalPay4Show(bookInfo, request);
		logger.info("getTotalPay4Show(bookInfo, request)="+totalPay+" ps_money="+ps_money);
		if("choose_type_22".equals(choose_type_value)){
			totalPay = String.valueOf(Double.valueOf(totalPay)+Double.valueOf(ps_money));
		}
		logger.info("totalPay="+totalPay);
		bean.setOrder_price(totalPay);//eop订单售价
		bean.setFace_value(totalPay);//面值（用于打印小票）
		Double agent_get_monney=Double.valueOf(getTotalPay4Show(bookInfo, request))-Double.valueOf(getTotalPay(bookInfo, request));
		bean.setAgent_get_monney(String.valueOf(agent_get_monney));//网点分润
		bean.setOrder_keywords(orderInfo.getTrain_no());
		//logger.info("eop商品单价"+orderInfo.getPay_money()+"0");//eop商品单价254.0
		
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
				   .append((eop_order_url.indexOf("?")!=-1) ? "?data_type=JSON" : "&data_type=JSON");
		ASPUtil.createOrder(bean, eopOrderUrl.toString());
		System.out.println("支付地址2："+bean.getPay_url());
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
			//更新订单状态为正在支付
//			Map<String,String> map = new HashMap<String,String>();
//			map.put("asp_order_id", order_id);
//			map.put("old_status", TrainConsts.PRE_ORDER);
//			map.put("order_status", "01");
//			orderService.updateOrderStatus(map);
			
		}else if("AGENT_LOGIN_OUT".equalsIgnoreCase(bean.getResult_code())){//代理商登录失效
			logger.info("【下单接口】代理商登录失效，下单失败！" + "ASP订单号" + order_id);
			Map<String, String> errMap = new HashMap<String, String>();
			errMap.put("AGENT_LOGIN_OUT", "AGENT_LOGIN_OUT");//代理商登录失效
			return "common/sessionTimeOut";
			
		}else{//其他
			logger.info("【下单接口】错误码为" + bean.getResult_code() + "，下单失败！" + "ASP订单号" + order_id);
		}
		return "redirect:/order/orderComfirm.jhtml?order_id=" + order_id;
	}
	
	//重新支付
	@RequestMapping("/orderRepay.jhtml")
	public void orderRepay(HttpServletRequest request, HttpServletResponse response){
		String order_id= this.getParam(request, "order_id");
		Map<String, String> paramMap1 = new HashMap<String, String>();
		paramMap1.put("order_id", order_id);
		paramMap1.put("refund_before_time",  this.getSysSettingValue("before_refundTicket_time", "before_refundTicket_time"));
		paramMap1.put("refund_time",  this.getSysSettingValue("stop_refundTicket_time", "stop_refundTicket_time"));
		OrderInfo orderInfo = orderService.queryOrderInfo2(paramMap1);
		
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
		.append("&arrive_station=").append(paramMap.get("to_city")).append("&channel=").append("19e");
		try{
			String jsonStr = HttpPostUtil.sendAndRecive(query_left_ticket_url,param.toString());//调用接口
//			if(!"error".equals(jsonStr)){
//				Outer12306NewData outer12306NewData = mapper.readValue(jsonStr, Outer12306NewData.class);
//				osnd = outer12306NewData.getErrorInfo().get(0);
//			}
			if("error".equals(jsonStr)){
//				jsonStr = HttpPostUtil.sendAndRecive(query_left_ticket_url,param.toString());//调用接口
//				if("error".equals(jsonStr)){
//					osnd = null;
//				}else{
//					osnd = mapper.readValue(jsonStr, OuterSoukdNewData.class);
//				}
				osnd = null;
			}else{
				osnd = mapper.readValue(jsonStr, OuterSoukdNewData.class);
			}
		}catch(Exception e){
			logger.error("查询异常！",e);
		}
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
		LoginUserInfo loginUser = this.getLoginUser(request);
		String agentId = loginUser.getAgentId();
		AgentVo agentVo = joinUsService.queryAgentInfo(agentId);
		String provinceId = agentVo.getProvince_id();
		
		String order_id= this.getParam(request, "order_id");
		Map<String, String> paramMap1 = new HashMap<String, String>();
		paramMap1.put("order_id", order_id);
		paramMap1.put("refund_before_time",  this.getSysSettingValue("before_refundTicket_time", "before_refundTicket_time"));
		paramMap1.put("refund_time",  this.getSysSettingValue("stop_refundTicket_time", "stop_refundTicket_time"));
		OrderInfo orderInfo = orderService.queryOrderInfo2(paramMap1);
		OrderInfoPs orderInfoPs = orderService.queryOrderInfoPs(order_id);
		
		Map<String, String> orderInfoPssm = orderService.queryOrderInfoPssm(order_id);
		List<Map<String, String>> detailList = orderService.queryOrderDetailList(order_id);
		Map<String, String> eopInfo = commonService.queryEopInfo(order_id);
		
		if(orderInfo!=null && !StringUtils.isEmpty(orderInfo.getExt_seat())
				&& orderInfo.getExt_seat().indexOf(TrainConsts.SEAT_9)!=-1){//备选无座
			request.setAttribute("wz_ext", 1);
		}
		request.setAttribute("orderInfo", orderInfo);
		request.setAttribute("orderInfoPs", orderInfoPs);
		request.setAttribute("orderInfoPssm", orderInfoPssm);
		request.setAttribute("detailList", detailList);		
		request.setAttribute("ticketTypeMap", TrainConsts.getTicketType());
		request.setAttribute("seatTypeMap", TrainConsts.getSeatType());
		request.setAttribute("idsTypeMap", TrainConsts.getIdsType());
		request.setAttribute("pay_url", eopInfo.get("pay_url"));//支付地址
		request.setAttribute("trainTypeCn", this.getTrainTypeCn(orderInfo.getTrain_no()));
		double money = AmountUtil.add(Double.valueOf(orderInfo.getBx_pay_money()),Double.valueOf(orderInfo.getTicket_pay_money()));//保险
		
		if(Double.valueOf(orderInfo.getServer_pay_money())>0){//SVIP服务费>0
			request.setAttribute("engineeringCost", "5");
			money = AmountUtil.add(Double.valueOf(orderInfo.getServer_pay_money()), Double.valueOf(orderInfo.getTicket_pay_money()));//SVIP服务费
		}
		
		if(!StringUtil.isEmpty(orderInfo.getUser_pay_money())){
			request.setAttribute("totalPay4Show", String.valueOf(money));	
		}else{
			request.setAttribute("totalPay4Show", orderInfo.getUser_pay_money());	
		}
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
		String order_id = CreateIDUtil.createID("HC");
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
		orderInfo.setServer_pay_money(this.getServerTotalPay(bookInfo, request));//SVIP服务费
		orderInfo.setTicket_pay_money(this.getTicketTotalPay(bookInfo));
		if(TrainConsts.OUT_TICKET_TYPE_PS.equals(bookInfo.getOut_ticket_type())){//配送票
			orderInfo.setPs_pay_money(bookInfo.getPs_pay_money());
		}
		orderInfo.setPay_money(this.getTotalPay(bookInfo, request));
		orderInfo.setUser_pay_money(this.getTotalPay4Show(bookInfo, request));
		orderInfo.setOut_ticket_type(bookInfo.getOut_ticket_type());
		orderInfo.setSeat_type(bookInfo.getSeat_type());
		orderInfo.setTicket_num(bookInfo.getBookDetailInfoList().size()+"");
		orderInfo.setOrder_status(TrainConsts.PRE_ORDER);//预下单
		
		if(!StringUtils.isEmpty(bookInfo.getSeat_type())
				&& (TrainConsts.SEAT_8.equals(bookInfo.getSeat_type()) || TrainConsts.SEAT_2.equals(bookInfo.getSeat_type()) ||TrainConsts.SEAT_3.equals(bookInfo.getSeat_type()))
				&& !StringUtils.isEmpty(bookInfo.getWz_ext())
				&& "1".equals(bookInfo.getWz_ext())){//硬座、一等座、二等座选择无座备选
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
			orderInfoCp.setTicket_type(bookDetailInfo.getTicket_type());//车票类型0：成人票 1：儿童票
			orderInfoCp.setUser_name(bookDetailInfo.getUser_name().trim());//姓名
			orderInfoCp.setIds_type(bookDetailInfo.getIds_type());
			orderInfoCp.setUser_ids(bookDetailInfo.getUser_ids().trim());//证件号
			orderInfoCp.setSeat_type(bookInfo.getSeat_type());//坐席
			orderInfoCpList.add(orderInfoCp);
			
			//保险单价大于0则保存该保险记录
			String sale_price = bookDetailInfo.getSale_price();//保险单价
			if(!StringUtils.isEmpty(sale_price) && (Double.parseDouble(sale_price) > 0 || bookDetailInfo.getProduct_id().equals("BX_0"))){
				//查询保险公司渠道: 1、快保 2、合众 3、平安保险
				String bx_channel = commonService.querySysSettingByKey("bx_channel");
				if(bookDetailInfo.getProduct_id().equals("BX_0")){//选择的是“赠送保险”则保险公司选择3
					bx_channel = "3";
				}else if(StringUtils.isEmpty(bx_channel)){
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
				if(bookDetailInfo.getTicket_type().equals("0")){//车票类型 0：成人票 1：儿童票
					orderInfoBx.setBx_type("1");//保险类型：0、儿童；1、成人
				}else{
					orderInfoBx.setBx_type("0");
				}
				orderInfoBxList.add(orderInfoBx);
			}
		}
		
		
		try {
			String param = getParam(request, "stu_infos");
			logger.info("获取学生信息--stu_infos--"+param);
			List<DBStudentInfo> parseArray = JSON.parseArray(param,DBStudentInfo.class);
			for (OrderInfoCp cp : orderInfoCpList) {
				// 不是学生票 跳过
				if (!cp.getTicket_type().equals(TrainConsts.TICKETTYPE_STUDENT)) {
					continue ;
				}
				for (DBStudentInfo stu : parseArray) {
					if (StringUtils.equals(stu.getStu_name(), cp.getUser_name())) {
						stu.setChannel("19e");
						stu.setCp_id(cp.getCp_id());
						stu.setOrder_id(cp.getOrder_id());
						stu.setPreference_from_station_code(RobTicketUtils.getPreferenceCityCode(stu.getPreference_from_station_name()));
						stu.setPreference_to_station_code(RobTicketUtils.getPreferenceCityCode(stu.getPreference_to_station_name()));
						stu.setSchool_code(RobTicketUtils.getschoolCode(stu.getSchool_name()));
						stu.setProvince_code(RobTicketUtils.getProvinceCode(stu.getProvince_name()));
						orderService.insertStuCpInfo(stu);
					}
				}
			}
		} catch (Exception e) {
			logger.error("学生票信息入库失败!");
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
	 * SVIP服务费总额
	 * @param bookInfo
	 * @return
	 */
	private String getServerTotalPay(BookInfo bookInfo, HttpServletRequest request){
		String serverTotalPay = bookInfo.getServer_pay_money();
		if(StringUtils.isEmpty(serverTotalPay)){//SVIP服务费为空 则计算保险金额
			LoginUserInfo loginUser = this.getLoginUser(request);
			String provinceId = loginUser.getProvinceId();
			String cityId = loginUser.getCityId();
			//查询产品列表
			ProductVo product = new ProductVo();
			product.setProvince_id(provinceId);
			product.setCity_id(cityId);
			product.setType(TrainConsts.PRODUCT_TYPE_1);//保险
			product.setStatus(TrainConsts.PRODUCT_STATUS_1);//上架
			
			String product_id = bookInfo.getBookDetailInfoList().get(0).getProduct_id();//乘客所购买的保险类型
			if(product_id.equals("BX_0")){//SVIP服务，赠送保险
				serverTotalPay = String.valueOf("5");//若选择SVIP服务，则收取5元服务费
			}else{
				serverTotalPay = String.valueOf("0");//否则服务费为0
			}
			bookInfo.setServer_pay_money(serverTotalPay);
		}
		return serverTotalPay;
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
			double serverPay = Double.parseDouble(getServerTotalPay(bookInfo, request));//SBIP服务费金额
			if(bxPay > 0){
				money = AmountUtil.add(Double.parseDouble(getTicketTotalPay(bookInfo)),bxPay);
			}
			if(serverPay > 0){
				money = AmountUtil.add(money, serverPay);
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
		//LoginUserInfo loginUser = this.getLoginUser(request);
		//String provinceId = loginUser.getProvinceId();//获取当前登录人的省份信息（网点所在省份ID）
		//String agentId = loginUser.getAgentId();
		//AgentVo agentVo = joinUsService.queryAgentInfo(agentId);
		//String provinceId = agentVo.getProvince_id();
		
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
		if(Double.parseDouble(bookInfo.getServer_pay_money())>0){//SVIP服务费>0
			profit = AmountUtil.add(profit, 2);//返还给代理商2块钱，我们实际收取3块钱
		}
//		if(provinceId.equals("370000") || provinceId.equals("440000")){//山东省和广东省 加收5元技术费 370000
//			money = AmountUtil.sub(money, profit)+5;
//		}else if( provinceId.equals("140000") ){//山西省140000在支付的时候直接返给代理商3块钱技术服务费,我们收取2块钱
//			money = AmountUtil.sub(money, profit)+2;
//		}else{//除山东省以外的其他省不加收元技术费
//			money = AmountUtil.sub(money, profit);
//		}
		money = AmountUtil.sub(money, profit);
		return String.valueOf(money);
	}
	
	
		//删除订单
		@RequestMapping("/deleteOrder.jhtml")
		public void deleteOrder(HttpServletRequest request, HttpServletResponse response){
			LoginUserInfo loginUser = this.getLoginUser(request);
			String agentName = loginUser.getAgentName();
			String order_id= this.getParam(request, "order_id");
			String result = "failure";
			try{
			Map<String, String> logMap = new HashMap<String, String>();
			logMap.put("order_id", order_id);
			logMap.put("cp_id", "");
			logMap.put("opter",agentName);
			logMap.put("order_optlog", "代理商："+agentName+"点击了删除订单！");
			orderService.addOrderOptLog(logMap);
			int count = orderService.deleteOrder(order_id);
			if(count>0){
				result = "success";
			}
			}catch (Exception e) {
				logger.error("删除订单失败！"+e);
			}
			write2Response(response, result);
		}
}
