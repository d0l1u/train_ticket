package com.l9e.transaction.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jiexun.iface.util.StringUtil;
import com.l9e.common.ExternalBase;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.ChangeService;
import com.l9e.transaction.service.CommonService;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.service.QueryTicketService;
import com.l9e.transaction.vo.BookDetailInfo;
import com.l9e.transaction.vo.BookInfo;
import com.l9e.transaction.vo.ChangeInfo;
import com.l9e.transaction.vo.ChangeLogVO;
import com.l9e.transaction.vo.ChangePassengerInfo;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.transaction.vo.OrderInfoBx;
import com.l9e.transaction.vo.OrderInfoCp;
import com.l9e.transaction.vo.TrainStationVo;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.Md5Encrypt;
import com.l9e.util.MemcachedUtil;
import com.l9e.util.ParamCheckUtil;
import com.l9e.util.StrUtil;
import com.l9e.util.UrlFormatUtil;

/**
 * 对外接口
 * 
 * @author zuoyuxing
 * 
 */

@Controller
public class ExternalMainController extends ExternalBase {
	@Resource
	protected CtripBuyTicketController ctripBuyTicketController;
	@Resource
	protected NewBuyTicketController newBuyTicketController;
	@Resource
	private SoukdBuyTicketController soukd;
	@Resource
	private CreateOrderController createOrder;
	@Resource
	private OrderService orderService;
	@Resource
	private QueryOrderController queryOrder;
	@Resource
	private QueryTicketService ticketService;
	@Resource
	private RefundTicketController refundTicketController;
	@Resource
	private CommonService commonService;
	@Resource
	private ChangeService changeService;
	/**
	 * web对外接口主入口
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/externalInterface.do")
	public void ExternalInterface(HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("请求参数列表：" + getFullURL(request));
		String type = this.getParam(request, "type");
		if ("queryLeftTicket".equals(type)) {
			queryLeftTicket(request, response); // 查询余票
		} else if ("createOrder".equals(type)) {
			createOrder(request, response); // 下单
		} else if ("payOrder".equals(type)) {
			payOrder(request, response); // 无密支付
		} else if ("queryOrderInfo".equals(type)) {
			queryOrderInfo(request, response); // 查询订单
		} else if ("cancelOrder".equals(type)) {
			cancelOrder(request, response); // 取消订单
		} else if ("refundTicket".equals(type)) {
			refundTicketController.refundTicket(request, response); // 退款
		} else if ("querySubwayStation".equals(type)) {
			querySubwayStation(request, response); // 途经站
		} else if ("checkTicketNum".equals(type)) {
			checkTicketNum(request, response); // 验证余票
		}else if ("queryBxInfo".equals(type)) {
			queryBxInfo(request, response); // 保险信息查询
		}else if ("alterTicket".equals(type)) {
			alterTicket(request, response); // 改签车次接口
		}else if ("bookReturn".equals(type)) {
			bookReturn(request, response); // 预订成功返回
		}else if ("uploadStation".equals(type)) {
			uploadStation(request, response); // 车站信息查询
		}else if ("verifyUsers".equals(type)) {
			verifyUsers(request, response); // 验证乘客信息接口
		}else if("requestChange".equals(type)){
			requestChange(request,response);// 请求改签
		}else if("cancelChange".equals(type)){
			cancelChange(request,response);// 取消改签
		}else if("confirmChange".equals(type)){
			confirmChange(request,response);//确认改签
		}else if("queryCheciPrice".equals(type)){
			queryCheciPrice(request,response);//查询某个车次的票价
		}
		else {
			logger.info("hcp对外接口-非法的type接口参数 ：type=" + type);
			logger.info("请求参数异常type:"+type);
			printJson(response, getJson("003").toString());
		}
	}

	/**
	 * 查询某个车次的票价
	 */
	public void queryCheciPrice(HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("**********查询某个车次的票价**********");
		String train_code = this.getParam(request, "train_code");
		String from_station = this.getParam(request, "from_station");
		String arrive_station = this.getParam(request, "arrive_station");
		String terminal = this.getParam(request, "terminal");
		String merchant_id = this.getParam(request, "merchant_id");
		String timestamp = this.getParam(request, "timestamp");
		String type = this.getParam(request, "type");
		String version = this.getParam(request, "version");
		String hmac = this.getParam(request, "hmac");

		logger.info(terminal + merchant_id + timestamp + type + version
				+ train_code + from_station + arrive_station+hmac);
		if (StringUtil.isEmpty(from_station)
				|| StringUtil.isEmpty(arrive_station)
				|| StringUtil.isEmpty(train_code)
				|| StringUtil.isEmpty(terminal)
				|| StringUtil.isEmpty(merchant_id)
				|| StringUtil.isEmpty(timestamp) || StringUtil.isEmpty(type)
				|| StringUtil.isEmpty(version) || StringUtil.isEmpty(hmac)) {
			logger.info("车次价格查询接口-参数校验error:输入参数为空!");
			printJson(response, getJson("003").toString());
			return;
		}
		if (ParamCheckUtil.isNotCheck(from_station,
						ParamCheckUtil.ZHONGWEN_REGEX_BLANK)
				|| ParamCheckUtil.isNotCheck(arrive_station,
						ParamCheckUtil.ZHONGWEN_REGEX_BLANK)
				|| ParamCheckUtil.isNotCheck(timestamp,
						ParamCheckUtil.TIMESTAMP_REGEX)) {
			logger.info("车次价格查询接口-参数校验error:输入参数格式错误!"+from_station+arrive_station+timestamp);
			printJson(response, getJson("003").toString());
			return;
		}

		Map<String, String> merchantSetting = commonService.queryMerchantInfo(merchant_id);
		if (merchantSetting == null) {
			logger.info("车次价格查询接口-用户身份校验error:不存在的用户!");
			printJson(response, getJson("004").toString());
			return;
		}

		logger.info("车次价格查询接口-用户:【" + merchantSetting.get("merchant_name")
				+ "】接入成功!");
		logger.info("车次价格查询接口-用户传递hmac：【" + hmac + "】");
		// 加密明文
		String md_str = terminal + merchant_id + timestamp + type + version
				+ train_code + from_station + arrive_station;
		String hmac_19 = Md5Encrypt.getKeyedDigestFor19Pay(md_str
					+ merchantSetting.get("sign_key"), "", "utf-8");
		logger.info("车次价格查询接口-用户传递参数：" + md_str+ merchantSetting.get("sign_key"));
		logger.info("车次价格查询接口-系统加密hmac：【" + hmac_19 + "】");
		if (!hmac_19.equals(hmac)) {
			logger.info("车次价格查询接口-安全校验error:不符合安全校验规则。");
			printJson(response, getJson("002").toString());
			return;
		}

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("from_station", from_station);
		paramMap.put("arrive_station", arrive_station);
		paramMap.put("train_code", train_code);
		logger.info("车次价格查询开始");
		
		Map<String, String> priceMap=commonService.queryCheciPrice(paramMap);
		
		if(priceMap.isEmpty()) {
			printJson(response,getJson("109").toString());
			return;
		}else {	
		   JSONObject resultObject=new JSONObject();
		   resultObject.put("from_station", from_station);
		   resultObject.put("arrive_station", arrive_station);
		   resultObject.put("train_code", train_code);
		   
		   JSONObject priceObject=new JSONObject();
		   priceObject.put("wz", (priceMap.get("wz").equals("0") || StrUtil.isEmpty(priceMap.get("wz")))?"-":priceMap.get("wz"));
		   priceObject.put("yz", (priceMap.get("yz").equals("0") || StrUtil.isEmpty(priceMap.get("yz")))?"-":priceMap.get("yz"));
		   priceObject.put("rz", (priceMap.get("rz").equals("0") || StrUtil.isEmpty(priceMap.get("rz")))?"-":priceMap.get("rz"));
		   priceObject.put("ywx", (priceMap.get("ywx").equals("0") || StrUtil.isEmpty(priceMap.get("ywx")))?"-":priceMap.get("ywx"));
		   priceObject.put("rwx", (priceMap.get("rwx").equals("0") || StrUtil.isEmpty(priceMap.get("rwx")))?"-":priceMap.get("rwx"));
		   priceObject.put("rz2", (priceMap.get("rz2").equals("0") || StrUtil.isEmpty(priceMap.get("rz2")))?"-":priceMap.get("rz2"));
		   priceObject.put("rz1", (priceMap.get("rz1").equals("0") || StrUtil.isEmpty(priceMap.get("rz1")))?"-":priceMap.get("rz1"));
		   priceObject.put("swz", (priceMap.get("swz").equals("0") || StrUtil.isEmpty(priceMap.get("swz")))?"-":priceMap.get("swz"));
		   priceObject.put("tdz", (priceMap.get("tdz").equals("0") || StrUtil.isEmpty(priceMap.get("tdz")))?"-":priceMap.get("tdz"));
		   priceObject.put("gwx", (priceMap.get("gwx").equals("0") || StrUtil.isEmpty(priceMap.get("gwx")))?"-":priceMap.get("gwx"));
		   priceObject.put("dwx", (priceMap.get("dwx").equals("0") || StrUtil.isEmpty(priceMap.get("dwx")))?"-":priceMap.get("dwx"));
		   resultObject.put("price", priceObject);
		  
	       String result = resultObject.toString(); 
		   logger.info("输出结果:"+result);
		   printJson(response,result);
		   return;
		}
		
	}

	/**
	 * 保险信息查询
	 * 
	 * @param request
	 * @param response
	 */
	public void queryBxInfo(HttpServletRequest request,
			HttpServletResponse response) {
		String terminal = this.getParam(request, "terminal");
		String merchant_id = this.getParam(request, "merchant_id");
		String timestamp = this.getParam(request, "timestamp");
		String type = this.getParam(request, "type");
		String version = this.getParam(request, "version");
		String hmac = this.getParam(request, "hmac");
		String order_id=this.getParam(request, "order_id");
		if (StringUtil.isEmpty(order_id)
				|| StringUtil.isEmpty(terminal)
				|| StringUtil.isEmpty(merchant_id)
				|| StringUtil.isEmpty(timestamp) || StringUtil.isEmpty(type)
				|| StringUtil.isEmpty(version) || StringUtil.isEmpty(hmac)) {
			logger.info("保险信息查询接口-参数校验error:输入参数为空!");
			printJson(response, getJson("003").toString());
			return;
		}
		Map<String, String> merchantSetting = commonService.queryMerchantInfo(merchant_id);
		if (merchantSetting == null) {
			logger.info("保险信息查询接口-用户身份校验error:不存在的用户!");
			printJson(response, getJson("004").toString());
			return;
		}
		String md_str = terminal + merchant_id + timestamp + type + version
		+ order_id;
		String hmac_19 = "";
		if ("net".equals(merchantSetting.get("md5_type"))) {
			hmac_19 = Md5Encrypt.getKeyedDigestFor19Pay(md_str
					+ merchantSetting.get("sign_key"), "", "utf-8");
		} else {
			hmac_19 = Md5Encrypt.encodeMD5Hex(md_str
					+ merchantSetting.get("sign_key"));
		}
		logger.info("保险信息查询接口-用户传递参数：" + md_str
						+ merchantSetting.get("sign_key"));
		logger.info("保险信息查询接口-系统加密hmac：【" + hmac_19 + "】");
		if (!hmac_19.equals(hmac)) {
			logger.info("保险信息查询接口-安全校验error:不符合安全校验规则。");
			printJson(response, getJson("002").toString());
			return;
		}
		/**查询保险信息*/
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("order_id", order_id);
		paramMap.put("merchant_id", merchant_id);
		paramMap.put("order_channel", "ext");//对外商户标识
		List<OrderInfoBx> bxInfos = new ArrayList<OrderInfoBx>();
		bxInfos=orderService.queryBxInfosById(paramMap);
		if(bxInfos.size()==0){
			logger.info("没有找到该订单下的保险信息"+order_id);
			printJson(response, getJson("701").toString());
			return;
		}
		JSONObject json = new JSONObject();
		json.put("order_id", order_id);
		JSONArray arr=new JSONArray();
		for(OrderInfoBx bxInfo:bxInfos){
			Map<String, String> returnInfo = new HashMap<String, String>();
			returnInfo.put("train_no", bxInfo.getTrain_no());
			returnInfo.put("buy_money", bxInfo.getBuy_money());
			returnInfo.put("user_name", bxInfo.getUser_name());
			returnInfo.put("user_ids", bxInfo.getUser_ids());
			returnInfo.put("telephone", bxInfo.getTelephone());
			returnInfo.put("bx_code", bxInfo.getBx_code());
			returnInfo.put("bx_status", "2".equals(bxInfo.getBx_status())?"SUCCESS":"FAILURE");
			arr.add(returnInfo);
		}
		json.put("bxInfos", arr);
		System.out.println("订单保险信息返回结果："+json.toString());
		printJson(response, json.toString());
	}

	/**
	 * 对外车票查询接口
	 * 
	 * @param request
	 * @param response
	 */
	public void queryLeftTicket(HttpServletRequest request,
			HttpServletResponse response) {
		String from_station = this.getParam(request, "from_station");
		String arrive_station = this.getParam(request, "arrive_station");
		String travel_time = this.getParam(request, "travel_time");
		String terminal = this.getParam(request, "terminal");
		String merchant_id = this.getParam(request, "merchant_id");
		String timestamp = this.getParam(request, "timestamp");
		String type = this.getParam(request, "type");
		String version = this.getParam(request, "version");
		String hmac = this.getParam(request, "hmac");

		logger.info(terminal + merchant_id + timestamp + type + version
				+ travel_time + from_station + arrive_station+hmac);
		if (StringUtil.isEmpty(from_station)
				|| StringUtil.isEmpty(arrive_station)
				|| StringUtil.isEmpty(travel_time)
				|| StringUtil.isEmpty(terminal)
				|| StringUtil.isEmpty(merchant_id)
				|| StringUtil.isEmpty(timestamp) || StringUtil.isEmpty(type)
				|| StringUtil.isEmpty(version) || StringUtil.isEmpty(hmac)) {
			logger.info("车票查询接口-参数校验error:输入参数为空!");
			printJson(response, getJson("003").toString());
			return;
		}
		if (ParamCheckUtil.isNotCheck(travel_time, ParamCheckUtil.DATA_REGEX)
				|| ParamCheckUtil.isNotCheck(from_station,
						ParamCheckUtil.ZHONGWEN_REGEX_BLANK)
				|| ParamCheckUtil.isNotCheck(arrive_station,
						ParamCheckUtil.ZHONGWEN_REGEX_BLANK)
				|| ParamCheckUtil.isNotCheck(timestamp,
						ParamCheckUtil.TIMESTAMP_REGEX)) {
			logger.info("车票查询接口-参数校验error:输入参数格式错误!"+from_station+arrive_station+timestamp);
			printJson(response, getJson("003").toString());
			return;
		}

		Map<String, String> merchantSetting = commonService.queryMerchantInfo(merchant_id);
		if (merchantSetting == null) {
			logger.info("车票查询接口-用户身份校验error:不存在的用户!");
			printJson(response, getJson("004").toString());
			return;
		}

		logger.info("车票查询接口-用户:【" + merchantSetting.get("merchant_name")
				+ "】接入成功!");
		logger.info("车票查询接口-用户传递hmac：【" + hmac + "】");
		// 加密明文
		String md_str = terminal + merchant_id + timestamp + type + version
				+ travel_time + from_station + arrive_station;
		String hmac_19 = Md5Encrypt.getKeyedDigestFor19Pay(md_str
					+ merchantSetting.get("sign_key"), "", "utf-8");
		logger.info("车票查询接口-用户传递参数：" + md_str+ merchantSetting.get("sign_key"));
		logger.info("车票查询接口-系统加密hmac：【" + hmac_19 + "】");
		if (!hmac_19.equals(hmac)) {
			logger.info("车票查询接口-安全校验error:不符合安全校验规则。");
			printJson(response, getJson("002").toString());
			return;
		}

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("from_station", from_station);
		paramMap.put("arrive_station", arrive_station);
		paramMap.put("travel_time", travel_time);
		paramMap.put("method", "DGTrain");// 调用方法
		paramMap.put("terminal", terminal);
		paramMap.put("stop_buy_time", merchantSetting
				.get("stop_buyTicket_time"));
		paramMap.put("spare_ticket_amount", merchantSetting
				.get("spare_ticket_amount"));
		paramMap.put("merchant_id", merchantSetting.get("merchant_id"));
		logger.info("实时余票查询接口-调用实时余票查询接口开始");
		//3.自有12306余票查询  2.携程查询
		String channel = getSysInterfaceChannel("INTERFACE_CHANNEL");
		try{
			if (StringUtils.isEmpty(channel) || "3".equals(channel)) {
				newBuyTicketController.newQueryData(paramMap, request, response);
			} else if ("2".equals(channel)) {
			  //soukd.soukdQueryData(paramMap, request, response);  SOUKD接口已停用,不能提供服务
			  logger.info("**********查询携程数据接口*********channel："+channel);
			  ctripBuyTicketController.newQueryData(paramMap, request, response);
			}
		}catch(Exception e){
			logger.info("查询异常",e);
			printJson(response, getJson("001").toString());
			return;
		}
	}

	// 购票下单接口
	public void createOrder(HttpServletRequest request,
			HttpServletResponse response) {
		String terminal = this.getParam(request, "terminal");
		String merchant_id = this.getParam(request, "merchant_id");
		String timestamp = this.getParam(request, "timestamp");
		String type = this.getParam(request, "type");
		String version = this.getParam(request, "version");
		String json_param = this.getParam(request, "json_param");
		String hmac = this.getParam(request, "hmac");
		
		String check_key ="";
		try {
			if (StringUtil.isEmpty(terminal) || StringUtil.isEmpty(merchant_id)
					|| StringUtil.isEmpty(timestamp) || StringUtil.isEmpty(type)
					|| StringUtil.isEmpty(version) || StringUtil.isEmpty(hmac)
					|| StringUtil.isEmpty(json_param)|| ParamCheckUtil.isNotCheck(timestamp,
							ParamCheckUtil.TIMESTAMP_REGEX)) {
				logger.info("购票下单接口-参数校验error:必要参数为空或格式错误!");
				printJson(response, getJson("003").toString());
				return;
			}
	
			Map<String, String> merchantSetting = commonService
					.queryMerchantInfo(merchant_id);
			if (merchantSetting == null) {
				logger.info("购票下单接口-用户身份校验error:不存在的用户!");
				printJson(response, getJson("004").toString());
				return;
			}
	
			logger.info("购票下单接口-用户:【" + merchantSetting.get("merchant_name")
					+ "】登入成功!");
			logger.info("购票下单接口-用户传递hmac：【" + hmac + "】");
			// 加密明文
			String md_str = terminal + merchant_id + timestamp + type + version
					+ json_param;
			String hmac_19 = Md5Encrypt.getKeyedDigestFor19Pay(md_str
						+ merchantSetting.get("sign_key"), "", "utf-8");
			logger.info("购票下单接口-用户下单参数：" + md_str);
			logger.info("-系统加密hmac：【" + hmac_19 + "】");
			if (!hmac_19.equals(hmac)) {
				logger.info("购票下单接口-安全校验error:不符合安全校验规则。");
				printJson(response, getJson("002").toString());
				return;
			}
			ObjectMapper mapper = new ObjectMapper();
			BookInfo bookInfo = null;
			JSONArray users = new JSONArray();
			if ("00".equals(merchantSetting.get("merchant_status"))) {
				logger.info("购票下单接口-下单规则error:合作商户已禁用，请客服人员通知商户！");
				printJson(response, getJson("007").toString());
				return;
			}
			bookInfo = mapper.readValue(json_param, BookInfo.class);
			String key = merchant_id+bookInfo.getMerchant_order_id();
			check_key=key; //	
			if(null == MemcachedUtil.getInstance().getAttribute(key)){
				 logger.info("缓存当前商户订单号："+key);
				 MemcachedUtil.getInstance().setAttribute(key, key,30*1000);
			}else{
				String str = (String)MemcachedUtil.getInstance().getAttribute(key);
				if(str.equals(key)){
					logger.info("购票下单接口-下单规则error:重复下单异常："+bookInfo.getMerchant_order_id());
					printJson(response, getJson("201").toString(),check_key);
					return;
				}
			}
			logger.info("购票下单接口-用户下单参数校验开始");
			if (ParamCheckUtil.createOrderParamIsEmpty(bookInfo)) {
				logger.info("购票下单接口-下单参数error:下订单参数为空!");
				printJson(response, getJson("204").toString(),check_key);
				return;
			}
			if (ParamCheckUtil.createOrderParamCheck(bookInfo)) {
				logger.info("购票下单接口-下单参数error:下订单参数格式错误!");
				printJson(response, getJson("204").toString(),check_key);
				return;
			}
			if (ParamCheckUtil.bookDetailInfoCheck(bookInfo
					.getBook_detail_list(), bookInfo.getOrder_level())) {
				logger.info("购票下单接口-下单参数error:订单内乘客信息参数为空或者格式错误!");
				printJson(response, getJson("205").toString(),check_key);
				return;
			}

			if (bookInfo.getBook_detail_list().size() > 5) {
				logger.info("购票下单接口-下单规则error:下单票数异常");
				printJson(response, getJson("203").toString(),check_key);
				return;
			}
			//进行身份校验
			for(BookDetailInfo user:bookInfo.getBook_detail_list()){
				JSONObject json = new JSONObject();
				json.put("cert_no", user.getUser_ids());
				json.put("cert_type", user.getIds_type());
				json.put("user_name", user.getUser_name());
				users.add(json);
			}
			Map<String, String> maps = new HashMap<String,String>();
	        maps.put("command", "verify");//请求核验用户信息接口
	        maps.put("passengers", users.toString());
	        maps.put("channel", "qunar");
	        String result  = "";
	        String fail_pas = "";
	        /*try{
	        	String reqParams = UrlFormatUtil.CreateUrl("", maps, "", "UTF-8");
	        	result = HttpUtil.sendByPost(real_name_verify_url, reqParams, "UTF-8");
	        	logger.info("核验结果:"+result);
	        	JSONArray jsonArr = JSONArray.fromObject(result);
			    int index = jsonArr.size();
		    	for(int i=0;i<index;i++){
			    	if(!"0".equals((jsonArr.getJSONObject(i).get("check_status")))){
			    		fail_pas += jsonArr.getJSONObject(i).get("user_name")+",";
			    	}
			    }
	        }catch(Exception e){
	        	logger.info("核验用户信息失败！默认成功",e);
	        }*/
	        JSONObject returnJson = new JSONObject();
		    if("".equals(fail_pas)){
		    	createOrder.createTrainOrder(merchantSetting ,bookInfo, request, response,check_key);
		    }else{
		    	logger.info("以下用户："+fail_pas+"未通过铁路系统审核,merchant_order_id="+bookInfo.getMerchant_order_id());
			    returnJson.put("return_code", "203");
			    returnJson.put("message", "以下用户："+fail_pas+"未通过铁路系统审核");
			    printJson(response, returnJson.toString(),check_key);
		    }
		} catch (Exception e) {
			logger.info("购票下单接口-下单异常exception:第三方下单失败", e);
			printJson(response, getJson("001").toString(),check_key);
		}
		
		

	}
	//改签接口
	public void alterTicket(HttpServletRequest request,
			HttpServletResponse response){
		logger.info("改签接口-调用接口开始");
		String terminal = this.getParam(request, "terminal");
		String merchant_id = this.getParam(request, "merchant_id");
		String timestamp = this.getParam(request, "timestamp");
		String type = this.getParam(request, "type");
		String version = this.getParam(request, "version");
		String order_id = this.getParam(request, "order_id");
		String alter_travel_time = this.getParam(request, "alter_travel_time");
		String alter_train_code = this.getParam(request, "alter_train_code");
		String alter_train_time = this.getParam(request, "alter_train_time");
		String alter_train_type = this.getParam(request, "alter_train_type");
		String alter_seat_type = this.getParam(request, "alter_seat_type");
		String alter_result_url = this.getParam(request, "alter_result_url");
		String hmac = this.getParam(request, "hmac");
		
		logger.info("改签接口-参数校验开始");
		if (StringUtil.isEmpty(terminal) || StringUtil.isEmpty(merchant_id)
				|| StringUtil.isEmpty(timestamp) || StringUtil.isEmpty(type)
				|| StringUtil.isEmpty(version) || StringUtil.isEmpty(hmac)
				|| StringUtil.isEmpty(order_id)|| StringUtil.isEmpty(alter_travel_time)
				|| StringUtil.isEmpty(alter_seat_type)|| StringUtil.isEmpty(alter_result_url)) {
			logger.info("支付订单接口-参数校验error:必要参数为空或格式错误。");
			printJson(response, getJson("003").toString());
			return;
		}
		
		if(StringUtil.isEmpty(alter_train_code)){
			if(StringUtil.isEmpty(alter_train_time)||StringUtil.isEmpty(alter_train_type)){
				logger.info("支付订单接口-参数校验error:必要参数为空或格式错误。");
				printJson(response, getJson("003").toString());
				return;
			}
		}
		logger.info("支付订单接口-用户身份校验开始");
		Map<String, String> merchantSetting = commonService
				.queryMerchantInfo(merchant_id);
		if (merchantSetting == null) {
			logger.info("支付订单接口-用户身份校验error:不存在该用户!");
			printJson(response, getJson("004").toString());
			return;
		}
		logger.info("支付订单接口-用户:【" + merchantSetting.get("merchant_name")
				+ "】接入成功!");

		logger.info("支付订单接口-安全校验开始");
		logger.info("支付订单接口-用户传递hmac:【" + hmac + "】");
		// 加密明文
		String md_str = terminal + merchant_id + timestamp + type + version
				+ order_id + alter_travel_time + alter_train_code + alter_train_time
				+ alter_train_type + alter_seat_type + alter_result_url;
		String hmac_19 = Md5Encrypt.getKeyedDigestFor19Pay(md_str
					+ merchantSetting.get("sign_key"), "", "utf-8");
		
		if (!hmac_19.equals(hmac)) {
			printJson(response, getJson("002").toString());
		} else {
			try{
				
				Map<String,String> param = new HashMap<String,String>();
				Map<String,String> param_notify = new HashMap<String,String>();
				List<Map<String,String>> param_list = new ArrayList<Map<String,String>>();
				param.put("order_id", order_id);
				param_notify.put("order_id", order_id);
				OrderInfo orderInfo = orderService.queryOrderInfo(order_id);
				
				List<Map<String,String>> list = orderService.queryOrderDetailList(order_id);
				if(orderService.queryAlterOrderifoNum(param)==0 && orderInfo!=null){
					if("44".equals(orderInfo.getOrder_status())){
						for(Map<String,String> orderCp :list){
							param.put("cp_id", orderCp.get("cp_id"));
							param.put("alter_train_code", alter_train_code);
							param.put("alter_train_time", alter_train_time);
							param.put("alter_train_type", alter_train_type);
							param.put("alter_seat_type", alter_seat_type);
							param.put("alter_travel_time", alter_travel_time);
							param.put("from_station", orderInfo.getFrom_station());
							param.put("arrive_station", orderInfo.getArrive_station());
							param.put("old_travel_time", orderInfo.getTravel_time());
							param.put("old_ticket_type", orderCp.get("ticket_type"));
							param.put("old_train_code", orderInfo.getTrain_no());
							param.put("old_seat_type", orderInfo.getSeat_type());
							param.put("old_from_time", orderInfo.getFrom_time());
							param.put("old_seat_no", orderCp.get("seat_no"));
							param.put("old_train_box", orderCp.get("train_box"));
							param.put("old_pay_money", String.valueOf(orderCp.get("cp_pay_money")));
							param_list.add(param);
						}
						param_notify.put("alter_train_code", alter_train_code);
						param_notify.put("alter_train_time", alter_train_time);
						param_notify.put("alter_train_type", alter_train_type);
						param_notify.put("alter_seat_type", alter_seat_type);
						param_notify.put("notify_url", alter_result_url);
						orderService.addAlterOrderinfo(param_list,param_notify);
						printJson(response, getJson("000").toString());
					}else{
						printJson(response, getJson("802").toString());
						return;
					}
				}else{
					printJson(response, getJson("801").toString());
					return;
				}
			}catch(Exception e){
				logger.info("申请改签失败！", e);
				printJson(response, getJson("001").toString());
			}
		}
		
	}
	// 支付订单
	public void payOrder(HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("支付订单接口-调用接口开始");
		String terminal = this.getParam(request, "terminal");
		String merchant_id = this.getParam(request, "merchant_id");
		String timestamp = this.getParam(request, "timestamp");
		String type = this.getParam(request, "type");
		String version = this.getParam(request, "version");
		String order_id = this.getParam(request, "order_id");
		String merchant_order_id = this.getParam(request, "merchant_order_id");
		String pay_money = this.getParam(request, "pay_money");
		String pay_result_url = this.getParam(request, "pay_result_url");
		String user_ip = this.getParam(request, "user_ip");
		String hmac = this.getParam(request, "hmac");

		logger.info("支付订单接口-参数校验开始");
		if (StringUtil.isEmpty(terminal) || StringUtil.isEmpty(merchant_id)
				|| StringUtil.isEmpty(timestamp) || StringUtil.isEmpty(type)
				|| StringUtil.isEmpty(version) || StringUtil.isEmpty(hmac)
				|| StringUtil.isEmpty(order_id)
				|| StringUtil.isEmpty(merchant_order_id)
				|| StringUtil.isEmpty(pay_money)|| ParamCheckUtil.isNotCheck(timestamp,
						ParamCheckUtil.TIMESTAMP_REGEX)) {
			logger.info("支付订单接口-参数校验error:必要参数为空或格式错误。");
			printJson(response, getJson("003").toString());
			return;
		}

		logger.info("支付订单接口-用户身份校验开始");
		Map<String, String> merchantSetting = commonService
				.queryMerchantInfo(merchant_id);
		if (merchantSetting == null) {
			logger.info("支付订单接口-用户身份校验error:不存在该用户!");
			printJson(response, getJson("004").toString());
			return;
		}
		logger.info("支付订单接口-用户:【" + merchantSetting.get("merchant_name")
				+ "】接入成功!");

		logger.info("支付订单接口-安全校验开始");
		logger.info("支付订单接口-用户传递hmac:【" + hmac + "】");
		// 加密明文
		String md_str = terminal + merchant_id + timestamp + type + version
				+ order_id + merchant_order_id + pay_money+ pay_result_url + user_ip;
		String hmac_19 = Md5Encrypt.getKeyedDigestFor19Pay(md_str
					+ merchantSetting.get("sign_key"), "", "utf-8");
		logger.info("支付订单接口-用户传递余票查询参数:" + md_str);
		logger.info("支付订单接口-系统加密hmac:【" + hmac_19 + "】");
		if (!hmac_19.equals(hmac)) {
			logger.info("支付订单接口-验签失败！merchant_id="+merchant_id);
			printJson(response, getJson("002").toString());
		} else {
			try {
				Map<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("order_id", order_id);
				OrderInfo orderInfo = orderService.queryOrderInfo(order_id);
				//是否开启预定期间支付订单权限
				if("00".equals(merchantSetting.get("mid_pay")) && null==orderInfo.getOut_ticket_time()){
					printJson(response, getJson("603").toString());
					return;
				}
				//超过约定支付期，不允许支付
				String book_limit_time = merchantSetting.get("book_limit_time");
				String book_ticket_time = orderInfo.getOut_ticket_time();
				if(null!=book_ticket_time){
					long diff = DateUtil.minuteDiff(new Date(),DateUtil.stringToDate(book_ticket_time, DateUtil.DATE_FMT3));
					if(diff>Integer.valueOf(book_limit_time)){
						printJson(response, getJson("604").toString());
						return;
					}
				}
				
				String key = "pay_"+merchant_id+orderInfo.getMerchant_order_id();
				if(null == MemcachedUtil.getInstance().getAttribute(key)){
					 logger.info("缓存当前支付商户订单号："+key);
					MemcachedUtil.getInstance().setAttribute(key, key, 10*1000);
				}else{
					String str = (String)MemcachedUtil.getInstance().getAttribute(key);
					if(str.equals(key)){
						logger.info("购票支付接口-支付规则error:重复支付异常："+orderInfo.getMerchant_order_id());
						printJson(response, getJson("602").toString());
						return;
					}
				}
				Map<String,String> map = orderService.queryCpSizeAndPrice(orderInfo.getOrder_id());
				if("11".equals(merchantSetting.get("pay_type"))){
					if(null==orderInfo.getEop_pay_number()){
							map.put("pay_type", merchantSetting.get("pay_type"));
							orderInfo.setAgent_name(merchantSetting.get("agent_name"));
							double merchant_money = Double.valueOf(pay_money);
							double order_money = Double.valueOf((orderInfo.getPay_money()));
							if (merchant_money == order_money) {
								String result = orderService.eopSendOrderAndPay(orderInfo,map);
								if("no_money".equals(result)){
									printJson(response,getJson("007").toString());
								}
							} else {
								printJson(response, getJson("601").toString());
							}
					}else{
						printJson(response, getJson("602").toString());
					}
				}else if("22".equals(merchantSetting.get("pay_type"))){
					try{
						orderService.startNotifyPayOrder(orderInfo.getOrder_id(),String.valueOf(map.get("buy_money")),merchantSetting.get("pay_type"));
					}catch (Exception e) {
						logger.info("插入支付信息通知失败！");
					}
				}
				printJson(response, getJson("000").toString());
			} catch (Exception e) {
				logger.info("支付订单接口-Exception:支付订单失败!", e);
				printJson(response, getJson("001").toString());
			}
		}
	}

	// 查询订单信息
	public void queryOrderInfo(HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("查询订单信息接口-调用接口开始");
		String terminal = this.getParam(request, "terminal");
		String merchant_id = this.getParam(request, "merchant_id");
		String timestamp = this.getParam(request, "timestamp");
		String type = this.getParam(request, "type");
		String version = this.getParam(request, "version");
		String order_id = this.getParam(request, "order_id");
		String merchant_order_id = this.getParam(request, "merchant_order_id");
		String hmac = this.getParam(request, "hmac");
		logger.info("查询订单信息接口-参数校验开始");
		if (StringUtil.isEmpty(terminal) || StringUtil.isEmpty(merchant_id)
				|| StringUtil.isEmpty(timestamp) || StringUtil.isEmpty(type)
				|| StringUtil.isEmpty(version) || StringUtil.isEmpty(hmac)
				|| StringUtil.isEmpty(order_id)|| ParamCheckUtil.isNotCheck(timestamp,
						ParamCheckUtil.TIMESTAMP_REGEX)) {
			logger.info("查询订单信息接口-参数校验error:必要参数或为空或格式错误!");
			printJson(response, getJson("003").toString());
			return;
		}

		logger.info("查询订单信息接口-用户身份校验开始");
		Map<String, String> merchantSetting = commonService
				.queryMerchantInfo(merchant_id);
		if (merchantSetting == null) {
			logger.info("查询订单信息接口-用户身份校验error:不存在该用户!");
			printJson(response, getJson("004").toString());
			return;
		}

		logger.info("查询订单信息接口-用户:【" + merchantSetting.get("merchant_name")
				+ "】接入成功!");

		logger.info("查询订单信息接口-安全校验开始");
		logger.info("查询订单信息接口-用户传递hmac：" + hmac);
		// 加密明文
		String md_str = terminal + merchant_id + timestamp + type + version
				+ order_id + merchant_order_id;
		String hmac_19 = Md5Encrypt.getKeyedDigestFor19Pay(md_str
					+ merchantSetting.get("sign_key"), "", "utf-8");
		logger.info("查询订单信息接口-用户传递查询参数：" + md_str);
		logger.info("查询订单信息接口-系统加密后hmac：" + hmac_19);
		if (!hmac_19.equals(hmac)) {
			printJson(response, getJson("002").toString());
		} else {
			queryOrder.queryOrderList(request, response, order_id,
					merchant_order_id);
		}
	}

	// 取消订单
	public void cancelOrder(HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("取消订单接口-调用接口开始");
		String terminal = this.getParam(request, "terminal");
		String merchant_id = this.getParam(request, "merchant_id");
		String timestamp = this.getParam(request, "timestamp");
		String type = this.getParam(request, "type");
		String version = this.getParam(request, "version");
		String order_id = this.getParam(request, "order_id");
		String merchant_order_id = this.getParam(request, "merchant_order_id");
		String hmac = this.getParam(request, "hmac");

		if (StringUtil.isEmpty(terminal) || StringUtil.isEmpty(merchant_id)
				|| StringUtil.isEmpty(timestamp) || StringUtil.isEmpty(type)
				|| StringUtil.isEmpty(version) || StringUtil.isEmpty(hmac)
				|| StringUtil.isEmpty(order_id)
				|| StringUtil.isEmpty(merchant_order_id)|| ParamCheckUtil.isNotCheck(timestamp,
						ParamCheckUtil.TIMESTAMP_REGEX)) {
			logger.info("输入参数错误或为空或格式错误。");
			printJson(response, getJson("003").toString());
			return;
		}
		Map<String, String> merchantSetting = commonService
				.queryMerchantInfo(merchant_id);
		if (merchantSetting == null) {
			printJson(response, getJson("004").toString());
			return;
		}
		logger.info("加密前hmac：" + hmac);
		// 加密明文
		String md_str = terminal + merchant_id + timestamp
				+ type + version + order_id + merchant_order_id;
		String hmac_19 = Md5Encrypt.getKeyedDigestFor19Pay(md_str
					+ merchantSetting.get("sign_key"), "", "utf-8");
		logger.info("余票查询参数：" + md_str);
		logger.info("加密后hmac：" + hmac_19);
		if (!hmac_19.equals(hmac)) {
			printJson(response, getJson("002").toString());
			return;
		} else {
			JSONObject jsonObject = new JSONObject();
			try {
				Map<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("merchant_id", merchant_id);
				paramMap.put("order_id", order_id);
				paramMap.put("merchant_order_id", merchant_order_id);
				String order_status = orderService.queryOrderStatusById(paramMap);
				if ("44".equals(order_status) || "88".equals(order_status)) {
					jsonObject.put("order_id", order_id);
					jsonObject.put("merchant_order_id", merchant_order_id);
					jsonObject.put("return_code", "000");
					jsonObject.put("status", "FAILURE");
					jsonObject.put("message", "");
					jsonObject.put("fail_reason", "该订单内车票已经支付完成，不能取消，请选择退款流程！");
					printJson(response, jsonObject.toString());
					return;
				} else {
//					String cp_orderinfo_status = orderService.queryCpOrderinfoStatusById(paramMap);//查询cp_orderinfo 表的订单状态
//					if(!StringUtils.isEmpty(cp_orderinfo_status) && 
//							(!"".equals(cp_orderinfo_status)||!"".equals(cp_orderinfo_status))){
//						11111	
//					}else{
						// 更新订单状态为取消订单
						String result = HttpUtil.sendByPost(commonService.getTrainSysSettingValue("notify_cancle_interface_url"), "order_id="+order_id, "UTF-8");
						logger.info(order_id+" 请求取消订单接口返回：" + result);
						if("success".equals(result)){
							jsonObject.put("return_code", "000");
							jsonObject.put("status", "SUCCESS");
							jsonObject.put("order_id", order_id);
							jsonObject.put("merchant_order_id", merchant_order_id);
							jsonObject.put("message", "");
							jsonObject.put("fail_reason", "");
							printJson(response, jsonObject.toString());
						}else{
							jsonObject.put("return_code", "000");
							jsonObject.put("status", "FAILURE");
							jsonObject.put("order_id", order_id);
							jsonObject.put("merchant_order_id", merchant_order_id);
							jsonObject.put("message", "");
							jsonObject.put("fail_reason", "该订单状态不支持取消操作！");
							printJson(response, jsonObject.toString());
						}
						return;
//					}
					
					
				}
			} catch (Exception e) {
				logger.info("取消订单操作异常！", e);
				printJson(response, getJson("001").toString());
				return;
			}
		}
	}

	// 查询途经站信息
	public void querySubwayStation(HttpServletRequest request,
			HttpServletResponse response) {
		// queryOrder.queryOrderList(request,response);
		logger.info("查询途经站信息接口-调用接口开始");
		String terminal = this.getParam(request, "terminal");
		String merchant_id = this.getParam(request, "merchant_id");
		String timestamp = this.getParam(request, "timestamp");
		String type = this.getParam(request, "type");
		String version = this.getParam(request, "version");
		String train_code = this.getParam(request, "train_code");
		String hmac = this.getParam(request, "hmac");

		logger.info("查询途经站信息接口-参数校验开始");
		JSONObject json = new JSONObject();
		if (StringUtil.isEmpty(terminal) || StringUtil.isEmpty(merchant_id)
				|| StringUtil.isEmpty(timestamp) || StringUtil.isEmpty(type)
				|| StringUtil.isEmpty(version) || StringUtil.isEmpty(hmac)
				|| StringUtil.isEmpty(train_code)|| ParamCheckUtil.isNotCheck(timestamp,
						ParamCheckUtil.TIMESTAMP_REGEX)) {
			logger.info("查询途经站信息接口-参数校验error:必要参数为空或格式错误!");
			printJson(response, getJson("003").toString());
			return;
		}
		logger.info("查询途经站信息接口-用户身份校验开始");
		Map<String, String> merchantSetting = commonService
				.queryMerchantInfo(merchant_id);
		if (merchantSetting == null) {
			logger.info("查询途经站信息接口-用户身份校验error:不存在该用户!");
			printJson(response, getJson("004").toString());
			return;
		}

		logger.info("查询途经站信息接口-用户:【" + merchantSetting.get("merchant_name")
				+ "】接入成功!");
		logger.info("查询途经站信息接口-用户传递hmac：" + hmac);
		// 加密明文
		String md_str = terminal + merchant_id + timestamp + type + version
				+ train_code;
		logger.info(md_str + merchantSetting.get("sign_key"));
		String hmac_19 = Md5Encrypt.getKeyedDigestFor19Pay(md_str
					+ merchantSetting.get("sign_key"), "", "utf-8");
		logger.info("查询途经站信息接口-用户传递查询参数：" + md_str);
		logger.info("查询途经站信息接口-系统加密hmac：" + hmac_19);
		if (!hmac_19.equals(hmac)) {
			logger.info("查询途经站信息接口-安全验证error:不符合安全校验的规则");
			printJson(response, getJson("002").toString());
			return;
		} else {
			try {
				train_code = ticketService.queryTheCheciForStation(train_code);
				List<TrainStationVo> list = ticketService
						.queryWayStationInfo(train_code);

				if (list == null || list.size() == 0) {
					logger.info("查询途经站信息接口-查询结果error:暂时没有该途经站信息!");
					printJson(response, getJson("501").toString());
					return;
				} else {
					for (TrainStationVo tnv : list) {
						tnv.reSetInterval();
					}
					// 返回正确结果值
					json.put("return_code", "000");
					json.put("message", "");
					JSONArray jsonArray = JSONArray.fromObject(list);
					json.put("train_stationinfo", jsonArray);
					printJson(response, json.toString());
				}
			} catch (Exception e) {
				logger.info("查询途经站信息接口-途径车站解析失败Exception", e);
				printJson(response, getJson("001").toString());
				return;
			}
		}
	}

	// 实时验证余票信息
	public void checkTicketNum(HttpServletRequest request,
			HttpServletResponse response) {
		logger.info(CreateIDUtil.createID("HC"));
		// queryOrder.queryOrderList(request,response);
		logger.info("实时余票查询接口-调用接口开始");
		String terminal = this.getParam(request, "terminal");
		String merchant_id = this.getParam(request, "merchant_id");
		String timestamp = this.getParam(request, "timestamp");
		String type = this.getParam(request, "type");
		String version = this.getParam(request, "version");
		String train_code = this.getParam(request, "train_code");
		String travel_time = this.getParam(request, "travel_time");
		String from_station = this.getParam(request, "from_station");
		String arrive_station = this.getParam(request, "arrive_station");
		String hmac = this.getParam(request, "hmac");

		logger.info("实时余票查询接口-参数校验开始");
		if (StringUtil.isEmpty(terminal) || StringUtil.isEmpty(merchant_id)
				|| StringUtil.isEmpty(timestamp) || StringUtil.isEmpty(type)
				|| StringUtil.isEmpty(version) || StringUtil.isEmpty(hmac)
				|| StringUtil.isEmpty(train_code)
				|| StringUtil.isEmpty(from_station)
				|| StringUtil.isEmpty(arrive_station)
				|| StringUtil.isEmpty(travel_time)) {
			logger.info("实时余票查询接口-参数校验error:必要参数为空");
			printJson(response, getJson("003").toString());
			return;
		}
		if (ParamCheckUtil.isNotCheck(travel_time, ParamCheckUtil.DATA_REGEX)
				|| ParamCheckUtil.isNotCheck(from_station,
						ParamCheckUtil.ZHONGWEN_REGEX_BLANK)
				|| ParamCheckUtil.isNotCheck(arrive_station,
						ParamCheckUtil.ZHONGWEN_REGEX_BLANK)
				|| ParamCheckUtil.isNotCheck(timestamp,
						ParamCheckUtil.TIMESTAMP_REGEX)) {
			logger.info("实时余票查询接口-参数校验error:输入参数格式错误!");
			printJson(response, getJson("003").toString());
			return;
		}

		logger.info("实时余票查询接口-用户身份校验开始");
		Map<String, String> merchantSetting = commonService
				.queryMerchantInfo(merchant_id);
		if("00".equals(merchantSetting.get("check_left_ticket"))){
			logger.info("实时余票查询接口-处于禁用状态!");
			printJson(response, getJson("108").toString());
			return;
		}
		if (merchantSetting == null) {
			logger.info("实时余票查询接口-用户身份校验error:不存在该用户!");
			printJson(response, getJson("004").toString());
			return;
		}

		logger.info("实时余票查询接口-用户:【" + merchantSetting.get("merchant_name")
				+ "】接入成功!");
		logger.info("实时余票查询接口-安全校验开始");
		logger.info("实时余票查询接口-用户传递hmac【" + hmac + "】");
		// 加密明文
		String md_str = terminal + merchant_id + timestamp + type + version
				+ train_code + travel_time + from_station + arrive_station;
		logger.info("实时余票查询接口-用户请求参数" + md_str);
		String hmac_19 = Md5Encrypt.getKeyedDigestFor19Pay(md_str
					+ merchantSetting.get("sign_key"), "", "utf-8");
		logger.info("实时余票查询接口-系统加密hmac" + hmac_19);
		if (!hmac_19.equals(hmac)) {
			logger.info("实时余票查询接口-安全校验error:安全验证错误,不符合安全校验规则!");
			printJson(response, getJson("002").toString());
			return;
		} else {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("from_station", from_station);
			paramMap.put("arrive_station", arrive_station);
			paramMap.put("travel_time", travel_time);
			paramMap.put("method", "DGTrain");// 调用方法
			paramMap.put("train_code", train_code);
			paramMap.put("terminal", terminal);
			paramMap.put("check_spare_num", "false");
			paramMap.put("stop_buy_time", merchantSetting
					.get("stop_buyTicket_time"));
			paramMap.put("spare_ticket_amount", merchantSetting
					.get("spare_ticket_amount"));
			logger.info("实时余票查询接口-调用实时余票查询接口开始");
			//3.自有12306余票查询  2.携程查询
			String channel = getSysInterfaceChannel("INTERFACE_CHANNEL");
			try{
				if (StringUtils.isEmpty(channel) || "3".equals(channel)) {
					newBuyTicketController.newQueryData(paramMap, request, response);
				} else if ("2".equals(channel)) {
				   //soukd.soukdQueryData(paramMap, request, response);  SOUKD接口已停用,不能提供服务
				   logger.info("**********查询携程数据接口*********channel："+channel);
				   ctripBuyTicketController.newQueryData(paramMap, request, response);
				}
			}catch(Exception e){
				printJson(response, getJson("001").toString());
				return;
			}
		}
	}
	
	// 预定成功
	public void bookReturn(HttpServletRequest request,
			HttpServletResponse response) {
			printJson(response,"SUCCESS");
	}
	

	// 查询车站信息
	public void uploadStation(HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("下载车站信息接口-调用接口开始");
		String terminal = this.getParam(request, "terminal");
		String merchant_id = this.getParam(request, "merchant_id");
		String timestamp = this.getParam(request, "timestamp");
		String type = this.getParam(request, "type");
		String version = this.getParam(request, "version");
		String hmac = this.getParam(request, "hmac");
//		String upload_type = this.getParam(request, "upload_type");
		String upload_type = "TXT";
		if (StringUtil.isEmpty(terminal) || StringUtil.isEmpty(merchant_id)
				|| StringUtil.isEmpty(timestamp) || StringUtil.isEmpty(type)
				|| StringUtil.isEmpty(version) || StringUtil.isEmpty(hmac)
				) {
			logger.info("车站信息接口-参数校验error:必要参数为空");
			printJson(response, getJson("003").toString());
			return;
		}
		//当天
		Calendar theCa = Calendar.getInstance(); 
		theCa.setTime(new Date());  
		theCa.add(theCa.DATE, 0); 
		Date date = theCa.getTime();
		//前一天
		Calendar theCa2 = Calendar.getInstance(); 
		theCa2.setTime(new Date());  
		theCa2.add(theCa.DATE, -1); 
		Date date2 = theCa2.getTime();
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String querydate=df.format(date);
		String querydate2=df.format(date2);
		String dirPath="",dirPath2="";
		if("TXT".equals(upload_type) || "txt".equals(upload_type)){
//			dirPath="D:\\data\\station_files\\citys_json"+querydate.trim()+".txt";  // 线下
//			dirPath2="D:\\data\\station_files\\citys_json"+querydate2.trim()+".txt"; //线下
		
			dirPath="//data//station_files//citys_json"+querydate.trim()+".txt";   //线上
			dirPath2="//data//station_files//citys_json"+querydate2.trim()+".txt"; //线上
			
//			dirPath="//data//station_files//citys_json2015-11-26.txt";   //测试
//			dirPath2="//data//station_files//citys_json2015-11-26.txt"; //测试
			
		}else if("XML".equals(upload_type)||"xml".equals(upload_type)){
//			dirPath="D:\\data\\station_files\\citys_pinyin"+querydate.trim()+".xml";
//			dirPath2="D:\\data\\station_files\\citys_pinyin"+querydate2.trim()+".xml";
		}else{
			logger.info("upload_type格式错误");
			printJson(response, getJson("003").toString());
			return;
		}
		logger.info("执行文件下载"+dirPath);
		// ************取得文件的路径和文件名***************//
		String fileName = dirPath.substring(dirPath.lastIndexOf(File.separator) + 1);
		// ************判断文件是否存在********************//
		File file =  new File(dirPath);
		if (!file.exists()) {
			file = new File(dirPath2);
			if(!file.exists()){
				printJson(response, getJson("001").toString());
				return;
			}else{
				logger.info("当天没有更新，下载前一天的地址"+dirPath2);
			}
		}
		
		long fileLength = file.length();
		String length = String.valueOf(fileLength);
		// 设置返回文件的类型和头信息，application/octet-stream:文件类型的通用格式//
		response.setContentType("application/octet-stream");
		response.setHeader("Content-disposition", "attachment;filename="+ fileName);
		response.setHeader("Content_Length", length);
		response.setCharacterEncoding("GBK"); 
		String str="";
		PrintWriter writer = null;
		InputStreamReader ins = null;
		try {
			writer=	response.getWriter();
			ins=new InputStreamReader(new FileInputStream(file),"GBK");
			BufferedReader br = new BufferedReader(ins);
			while ((str = br.readLine())!=null){
				writer.write(str);
				}
			
		} catch (IOException e) {
			logger.info(e);
		} finally {
			if(writer!=null){
				writer.flush();
				writer.close();
			}
		}	
		logger.info("下载车站信息接口-调用接口结束");
	}
	
	// 验证乘客信息接口
	public void verifyUsers(HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("验证乘客信息接口-调用接口开始");
		String terminal = this.getParam(request, "terminal");
		String merchant_id = this.getParam(request, "merchant_id");
		String timestamp = this.getParam(request, "timestamp");
		String type = this.getParam(request, "type");
		String version = this.getParam(request, "version");
//		String ids_type = this.getParam(request, "ids_type");
//		String user_ids = this.getParam(request, "user_ids");
//		String user_name = this.getParam(request, "user_name");
		String json_param = this.getParam(request, "json_param");
		String hmac = this.getParam(request, "hmac");
		String md_str = terminal + merchant_id + timestamp + type + version+json_param;
		logger.info("验证乘客信息接口-用户参数：" + md_str);
		try {
			if (StringUtil.isEmpty(terminal) || StringUtil.isEmpty(merchant_id)
					|| StringUtil.isEmpty(timestamp) || StringUtil.isEmpty(type)
					|| StringUtil.isEmpty(version) || StringUtil.isEmpty(hmac)
					|| StringUtil.isEmpty(json_param)|| ParamCheckUtil.isNotCheck(timestamp,ParamCheckUtil.TIMESTAMP_REGEX)) {
				logger.info("验证乘客信息接口-参数校验error:必要参数为空或格式错误!");
				printJson(response, getJson("003").toString());
				return;
			}
	
			Map<String, String> merchantSetting = commonService.queryMerchantInfo(merchant_id);
			if (merchantSetting == null) {
				logger.info("验证乘客信息接口-用户身份校验error:不存在的用户!");
				printJson(response, getJson("004").toString());
				return;
			}
	
			logger.info("验证乘客信息接口-用户:【" + merchantSetting.get("merchant_name")	+ "】登入成功!");
			logger.info("验证乘客信息接口-用户传递hmac：【" + hmac + "】");
			// 加密明文
			
			String hmac_19 = Md5Encrypt.getKeyedDigestFor19Pay(md_str
						+ merchantSetting.get("sign_key"), "", "utf-8");
			
			logger.info("验证乘客信息接口-系统加密hmac：【" + hmac_19 + "】");
			if (!hmac_19.equals(hmac)) {
				logger.info("验证乘客信息接口-安全校验error:不符合安全校验规则。");
				printJson(response, getJson("002").toString());
				return;
			}
			if ("00".equals(merchantSetting.get("merchant_status"))||"00".equals(merchantSetting.get("verify_status"))) {
				logger.info("验证乘客信息接口-规则error:合作商户已禁用，请客服人员通知商户！");
				printJson(response, getJson("007").toString());
				return;
			}
			JSONObject json_params = JSONObject.fromObject(json_param);
			List<Map<String, String>> detail_list = json_params.getJSONArray("detail_list");
//			logger.info("detail_list:"+detail_list.toString());
			if (detail_list.size()<=0) {
				logger.info("验证乘客信息接口-参数error:订单内乘客信息参数为空或者格式错误!");
				printJson(response, getJson("205").toString());
				return;
			}
			
			if (ParamCheckUtil.userInfoCheck(detail_list)) {
				logger.info("验证乘客信息接口-参数error:订单内乘客信息参数为空或者格式错误!");
				printJson(response, getJson("205").toString());
				return;
			}
			
			String key = merchant_id+detail_list.get(0).get("user_ids");
			if(null == MemcachedUtil.getInstance().getAttribute(key)){
				 logger.info("缓存当前用户证件号："+key);
				MemcachedUtil.getInstance().setAttribute(key, key, 5*1000);
			}else{
				String str = (String)MemcachedUtil.getInstance().getAttribute(key);
				if(str.equals(key)){
					logger.info("验证乘客信息接口-规则error:重复验证异常："+detail_list.get(0).get("user_ids"));
					printJson(response, getJson("201").toString());
					return;
				}
			}
		
			JSONArray users = new JSONArray();
			//进行身份校验
			for(Map<String, String> map :detail_list){
				JSONObject json = new JSONObject();
				json.put("cert_no",map.get("user_ids"));
				json.put("cert_type", map.get("ids_type"));
				json.put("user_name", map.get("user_name"));
				users.add(json);
			}
			Map<String, String> maps = new HashMap<String,String>();
	        maps.put("command", "verify");//请求核验用户信息接口
	        maps.put("passengers", users.toString());
	        maps.put("channel", "qunar");
	        String result  = "";
	        String fail_pas = "";
	        int fail_num = 0;
	        try{
	        	String reqParams = UrlFormatUtil.CreateUrl("", maps, "", "UTF-8");
	        	result = HttpUtil.sendByPost(real_name_verify_url, reqParams, "UTF-8");
	        	logger.info("验证乘客信息接口-核验结果:"+result);
	        	JSONArray jsonArr = JSONArray.fromObject(result);
			    int index = jsonArr.size();
		    	for(int i=0;i<index;i++){
		    		
		    		String certype = (String) jsonArr.getJSONObject(i).get("cert_type");
		    		logger.info(jsonArr.getJSONObject(i).get("cert_no")+",证件类型："+certype);
		    		
		    		if(!("5".indexOf(certype)>-1||"4".indexOf(certype)>-1||"3".indexOf(certype)>-1)){//对非身份证，不做处理。
		    		   
			    		if("1".equals((jsonArr.getJSONObject(i).get("check_status")))){//待核验
				    		fail_pas += jsonArr.getJSONObject(i).get("user_name")+",";
				    		fail_num ++;
				    	}
			    		
				    	if("2".equals((jsonArr.getJSONObject(i).get("check_status")))){//未通过
				    		String message= (String) jsonArr.getJSONObject(i).get("message");
				    		logger.info(jsonArr.getJSONObject(i).get("cert_no")+",message:"+message);
				    		if (null!=message) {
				    			if ("".equals(message)||message.indexOf("冒用")!=-1) {
				    				fail_pas += jsonArr.getJSONObject(i).get("user_name")+",";
						    		fail_num ++;
								}else{
									logger.info(jsonArr.getJSONObject(i).get("cert_no")+",默认通过核验");
								}
							}
				    	} 
		    		}
			    	
			   }
	        }catch(Exception e){
	        	logger.info("验证乘客信息接口-核验用户信息失败！默认成功"+e.getMessage());
	        }
	        
	        JSONObject returnJson = new JSONObject();
		    if("".equals(fail_pas)&& !"".equals(result)){
		    	logger.info("验证乘客信息接口-用户已通过铁路系统审核，可正常购票！");
		    	for(int i=0;i<detail_list.size();i++){
		    		commonService.updateMerchantVerifyNum(merchant_id);
		    	}
		    	 returnJson.put("return_code", "000");
				 returnJson.put("message", "用户已通过铁路系统审核，可正常购票！");
				 printJson(response, returnJson.toString());
		    }else  if("".equals(fail_pas)&& "".equals(result)){
		    	  logger.info("验证乘客信息接口-验证失败，请稍候重试！");
		    	 returnJson.put("return_code", "001");
				 returnJson.put("message", "验证失败，请稍候重试！");
				 printJson(response, returnJson.toString());
				 
		    }else{
		    	logger.info("验证乘客信息接口-以下用户："+fail_pas+"未通过铁路系统审核");
		    	for(int i=0;i<detail_list.size()-fail_num;i++){
		    		commonService.updateMerchantVerifyNum(merchant_id);
		    	}
		    	returnJson.put("return_code", "203");
			    returnJson.put("message", "以下用户："+fail_pas+"未通过铁路系统审核");
			    printJson(response, returnJson.toString());
		    }
		} catch (Exception e) {
			logger.info("验证乘客信息接口-异常exception:", e);
			printJson(response, getJson("001").toString());
		}
	}
	
	/**
	 * 请求改签
	 */
	public void requestChange(HttpServletRequest request, HttpServletResponse response) {
		logger.info("请求改签开始");
		String terminal = this.getParam(request, "terminal");
		String merchant_id = this.getParam(request, "merchant_id");
		String timestamp = this.getParam(request, "timestamp");
		String version = this.getParam(request, "version");
		String type = this.getParam(request, "type");
		String json_param = this.getParam(request, "json_param");
		String hmac = this.getParam(request, "hmac");
		String md_str = terminal+ merchant_id + timestamp  + type+version+json_param;
		String merchant_name="";
		String order_id="";
		String msg ="";
		//改签日志
		ChangeLogVO log=new ChangeLogVO();
		logger.info("请求改签接口-用户参数：" + md_str);
		try {
			if (StringUtil.isEmpty(merchant_id)
					|| StringUtil.isEmpty(timestamp) || StringUtil.isEmpty(type)
					|| StringUtil.isEmpty(version) || StringUtil.isEmpty(hmac)
					|| StringUtil.isEmpty(json_param)|| ParamCheckUtil.isNotCheck(timestamp,ParamCheckUtil.TIMESTAMP_REGEX)) {
				logger.info("请求改签接口-参数校验error:必要参数为空或格式错误!");
				printJson(response, getJson("003").toString());
				return;
			}
	
			Map<String, String> merchantSetting = commonService.queryMerchantInfo(merchant_id);
			if (merchantSetting == null) {
				logger.info("请求改签接口-用户身份校验error:不存在的用户!");
				printJson(response, getJson("004").toString());
				return;
			}
			if ("00".equals(merchantSetting.get("merchant_status"))) {
				logger.info("请求改签接口-规则error:合作商户已禁用，请客服人员通知商户！");
				printJson(response, getJson("007").toString());
				return;
			}
			merchant_name = merchantSetting.get("merchant_name");
			logger.info("请求改签接口-用户:【" + merchant_name+ "】登入成功!");
			logger.info("请求改签接口-用户传递hmac：【" + hmac + "】");
			// 加密明文
			String hmac_19 = Md5Encrypt.getKeyedDigestFor19Pay(md_str
						+ merchantSetting.get("sign_key"), "", "utf-8");
			
			logger.info("请求改签接口-系统加密hmac：【" + hmac_19 + "】");
			if (!hmac_19.equals(hmac)) {
				logger.info("请求改签接口-安全校验error:不符合安全校验规则。");
				printJson(response, getJson("002").toString());
				return;
			}
			
			JSONObject json_params = JSONObject.fromObject(json_param);
			//验证业务参数
			order_id = json_params.getString("order_id");//订单号
			String from_station_code = json_params.getString("from_station_code");//出发站简称
			String from_station_name = json_params.getString("from_station_name");//出发站名称
			String to_station_code = json_params.getString("to_station_code");//到达站简称
			String to_station_name = json_params.getString("to_station_name");//到达站名称
			String isChangeTo = json_params.getString("isChangeTo");//0：改签 1：变更到站
			String out_ticket_billno = json_params.getString("out_ticket_billno");//出票单号 12306单号
			String change_train_no = json_params.getString("change_train_no");//改签后车次
			String change_from_time = json_params.getString("change_from_time");//改签后发车时间
			String seat_type = json_params.getString("seat_type");//座位类型
			String change_seat_type = json_params.getString("change_seat_type");//改签后座位类型
			String callbackurl = json_params.getString("callbackurl");//回调地址
			String reqtoken = json_params.getString("reqtoken");//唯一标识
			String ticketinfo = json_params.getString("ticketinfo");//车票信息
			Boolean hasSeat = true;   //true:不接受 false：接受 ,无座
			if(json_params.containsKey("hasSeat")){
				hasSeat=json_params.getBoolean("hasSeat");
			}
			Boolean isChooseSeats = false;   //true:选座  ,false：不选座			
			if(json_params.containsKey("isChooseSeats")) {
				isChooseSeats=json_params.getBoolean("isChooseSeats");
			}
			String chooseSeats="";
			if(json_params.containsKey("chooseSeats")) { //选座信息
				if(isChooseSeats) {
					chooseSeats=json_params.getString("chooseSeats");
				}
			}
			logger.info("order_id:"+order_id+",isChooseSeats:"+isChooseSeats+",chooseSeats:"+chooseSeats);
			
			JSONArray tickets = JSONArray.fromObject(ticketinfo);
			if(isChooseSeats) { //如果选座，做检验
				if(StringUtil.isEmpty(chooseSeats)||ParamCheckUtil.isNotCheck(chooseSeats, ParamCheckUtil.chooseSeat)||chooseSeats.length()!=tickets.size()*2) {//校验不通过，例如：1A2B,
					logger.info(merchant_name+"请求"+msg+"ERROR,参数有空order_id:"+order_id);
					printJson(response, getJson(TrainConsts.RETURN_CODE_003).toString());
					return;
				}
			}

			msg = isChangeTo.equals("1")?"变更到站":"改签";
			logger.info(merchantSetting.get("merchant_name")+msg+",orderId : " + order_id +",备选坐席无座:"+hasSeat);
			/*业务参数检查*/
			if("".equals(order_id) || "".equals(out_ticket_billno) || "".equals(change_train_no) || 
					"".equals(change_from_time) || "".equals(seat_type) || "".equals(change_seat_type) 
					|| "".equals(ticketinfo)|| "".equals(from_station_name)|| "".equals(to_station_name)) {
				logger.info(merchant_name+"请求"+msg+"ERROR,参数有空order_id:"+order_id);
				printJson(response, getJson(TrainConsts.RETURN_CODE_003).toString());
				return;
			}
			
			/*查询订单*/
			OrderInfo orderInfo = orderService.queryOrderInfo(order_id);
			if(orderInfo == null || orderInfo.getOrder_status() == null){
				/*订单不存在*/
				logger.info(merchant_name+"请求"+msg+"ERROR,订单不存在order_id:"+order_id);
				printJson(response,getJson(TrainConsts.RETURN_CODE_301).toString());
				return;
			}
			
			/*检查订单状态*/
			String orderStatus = orderInfo.getOrder_status();
			if(!"44".equals(orderStatus)) {
				/*出票成功才可以改签*/
				logger.info(merchant_name+"请求"+msg+"ERROR,订单状态不正确，订单号为"+order_id);
				printJson(response,getJson(TrainConsts.RETURN_CODE_803).toString());
				return;
			}
			/*查询该订单号下的改签特征值，排除重复请求*/
			ChangeInfo reqtokenChangeInfo = changeService.getChangeInfoByReqtoken(reqtoken);
			if(reqtokenChangeInfo!=null){
				logger.info(merchant_name+"请求"+msg+"该请求已存在，reqtoken为"+reqtoken);
				printJson(response,getJson(TrainConsts.RETURN_CODE_802).toString());
				return;
			}
			int nocomplete=changeService.selectNoCompleteChangeOrderLastOneCount(order_id); //查询该订单最新提交的订单通知状态，判断是否接受本次改签请求
			if(nocomplete==1) {
				logger.info(order_id+",上一次的改签请求，未成功通知商户，本次请求忽略！"+reqtoken);
				printJson(response,getJson(TrainConsts.RETURN_CODE_808).toString());
				return;
			}
		
			/*改签时间验证*/
			Date old_from_time = DateUtil.stringToDate(orderInfo.getTravel_time().split(" ")[0]+" "+orderInfo.getFrom_time()+":00", DateUtil.DATE_FMT3);
			//变更到站         
			if(isChangeTo.equals("1")){
				if(DateUtil.minuteDiff(old_from_time, new Date()) < 48*60) {
					/*距离发车时间小于48小时*/
					logger.info(merchant_name+"-请求变更到站ERROR,距离开车时间太近无法变更到站");
					printJson(response,getJson(TrainConsts.RETURN_CODE_805).toString());
					return;
				}
			}
			else{
				if(DateUtil.minuteDiff(old_from_time, new Date()) < 30) {
					/*距离发车时间小于30分*/
					logger.info(merchant_name+"-请求改签ERROR,距离开车时间太近无法改签");
					printJson(response,getJson(TrainConsts.RETURN_CODE_804).toString());
					return;
				}
			}
			
			/*车票信息*/
			if(tickets.size() == 0) {
				logger.info(merchant_name+"-请求"+msg+"ERROR,没有车票信息");
				printJson(response,getJson(TrainConsts.RETURN_CODE_205).toString());
				return;
			}
			if(tickets.size() > 1) {
				/*批量改签*/
				if(seat_type.equals(TrainConsts.SEAT_4) ||seat_type.equals(TrainConsts.SEAT_5) ||
						seat_type.equals(TrainConsts.SEAT_6) ||change_seat_type.equals(TrainConsts.SEAT_4) ||
						change_seat_type.equals(TrainConsts.SEAT_5) ||change_seat_type.equals(TrainConsts.SEAT_6)) {
					/*批量改签原票坐席不能为卧铺*/
					logger.info(merchant_name+"-请求改签ERROR,批量改签原票或新票坐席不能为卧铺");
					printJson(response,getJson(TrainConsts.RETURN_CODE_806).toString());
					return;
				}
			} 
			/*组装改签车票信息*/
			ChangeInfo changeInfo = new ChangeInfo();
			List<ChangePassengerInfo> changePassengers = new ArrayList<ChangePassengerInfo>();
			String cp_id="";
			for(int i = 0; i < tickets.size(); i++) {
				/*传入的参数数据*/
				JSONObject ticket = tickets.getJSONObject(i);
				
				//获取原车票id*****************************************//
				String user_ids = ticket.getString("user_ids");//身份证号
				String train_box=null;
				String  seat_no =null;
				String  ticket_type=null;
				Map<String, String> param = new HashMap<String,String>();
				param.put("user_ids", user_ids);
				param.put("order_id", order_id);
				
				if(ticket.containsKey("train_box")&&ticket.getString("train_box") != null){
					train_box = ticket.getString("train_box"); //车厢号
					param.put("train_box", train_box); //车厢号
				}
				if(ticket.containsKey("seat_no")&&ticket.getString("seat_no") != null){
					seat_no =  ticket.getString("seat_no"); //座位号
					param.put("seat_no", seat_no); //座位号
				}
				if(ticket.containsKey("ticket_type")&&ticket.getString("ticket_type") != null){
					ticket_type =  ticket.getString("ticket_type"); //车票类型
					param.put("ticket_type", ticket_type); //车票类型
				}
				logger.info("order_id:"+order_id+","+train_box+","+seat_no+",ticket_type:"+ticket_type+",user_ids:"+user_ids);
				List<String> cp_ids = orderService.queryCp_idByIds(param);
				//获取原车票id*****************************************//
				
				if(cp_ids==null || cp_ids.size()!=1){
					logger.info(merchant_name+"-请求改签ERROR,乘客信息有误,乘客user_ids:" + user_ids);
					printJson(response,getJson(TrainConsts.RETURN_CODE_205).toString());
					return;
				}
			
				cp_id = cp_ids.get(0);
				ChangePassengerInfo cp = changeService.getChangeCpById(cp_id);
				if(cp !=  null) {
					/*每张车票只能改签一次*/
					if(cp.getIs_changed().equals("Y")){
						logger.info(merchant_name+"-请求改签ERROR,车票已"+msg+"过,车票id:" + cp.getCp_id());
						printJson(response,getJson(TrainConsts.RETURN_CODE_807).toString());
						return;
					}
				} else {
					cp = new ChangePassengerInfo();
				}
				String new_cp_id = CreateIDUtil.createID("EXCP");
				cp.setOrder_id(order_id);//订单id
				cp.setCp_id(cp_id);//车票id(原票)
				cp.setNew_cp_id(new_cp_id);//改签后车票id
				cp.setChange_seat_type(change_seat_type);//19e改签后新座位席别
				cp.setSeat_type(seat_type);
				cp.setIs_changed("N");
				
				/*原票信息*/
				OrderInfoCp p = orderService.queryCpInfoByCpId(cp.getCp_id());
				if(p != null) {
					cp.setBuy_money(p.getBuy_money());//成本价格
					cp.setSeat_no(p.getSeat_no());//座位号
					cp.setSeat_type(p.getSeat_type());//座位席别
					cp.setTrain_box(p.getTrain_box());//车厢
					cp.setTicket_type(p.getTicket_type());//车票类型
					cp.setIds_type(p.getIds_type());//证件类型
					cp.setUser_ids(p.getUser_ids());//证件号码
					cp.setUser_name(p.getUser_name());//乘客姓名
				}
				changePassengers.add(cp);
			}
			/*组装改签记录信息*/
			changeInfo.setChange_travel_time(change_from_time);//改签后乘车日期
			changeInfo.setTrain_no(orderInfo.getTrain_no());//车次
			changeInfo.setChange_train_no(change_train_no);//改签后车次
			changeInfo.setFrom_time(orderInfo.getTravel_time().split(" ")[0]+" "+orderInfo.getFrom_time());//发车时间
			changeInfo.setChange_from_time(change_from_time);//改签后发车时间
			changeInfo.setFrom_city(from_station_name);//出发车站
			changeInfo.setTo_city(to_station_name);//到达车站
			changeInfo.setFrom_station_code(from_station_code);
			changeInfo.setTo_station_code(to_station_code);
			changeInfo.setIschangeto(new Integer(isChangeTo));
			changeInfo.setOut_ticket_billno(out_ticket_billno);//12306单号
			changeInfo.setOrder_id(order_id);
			changeInfo.setIsasync("Y");//异步
			changeInfo.setCallbackurl(callbackurl);
			changeInfo.setReqtoken(reqtoken);
			changeInfo.setChange_status(TrainConsts.TRAIN_REQUEST_CHANGE);//11改签预定
			changeInfo.setHasSeat(hasSeat?1:0);  //是否支持改签到无座票，1、不允许 0、允许
			
			changeInfo.setIsChooseSeats(isChooseSeats?1:0);
			changeInfo.setChooseSeats(chooseSeats);
			
			logger.info("order_id:"+order_id+",isChooseSeats:"+isChooseSeats+",chooseSeats:"+chooseSeats);
			
			changeInfo.setcPassengers(changePassengers);//改签、车票关系
			Map<String,String> cpParam = new HashMap<String,String>();
			cpParam.put("order_id", order_id);
			String acc_id = orderService.queryAccountOrderInfo(cpParam).get("acc_id").toString();
			changeInfo.setAccount_id(acc_id);//出票账号id
			changeInfo.setChange_notify_count(0);
			changeInfo.setChange_notify_status(TrainConsts.CHANGE_NOTIFY_PRE);
			changeInfo.setMerchant_id(merchant_id);
			/*改签信息入库*/
			changeService.addChangeInfo(changeInfo);
			int change_id = changeInfo.getChange_id();
			log.setChange_id(change_id);
		    logger.info(merchant_name+"请求"+msg+"异步成功,orderId : " + order_id + "车票信息 : " + ticketinfo);
			log.setContent(merchant_name+"请求"+msg+"异步success");
			printJson(response,getJson(TrainConsts.RETURN_CODE_000).toString());
		} catch (Exception e) {
			logger.info(merchant_name+"请求"+msg+"异常"+e.getMessage(),e);
			e.printStackTrace();
			printJson(response,getJson(TrainConsts.RETURN_CODE_001).toString());
			log.setContent(merchant_name+"请求"+msg+"异常!");
		} finally {
			log.setOrder_id(order_id);
			log.setOpt_person(merchant_id);
			changeService.addChangeLog(log);
		}
	} 
	public void cancelChange(HttpServletRequest request, HttpServletResponse response){
		logger.info("取消改签开始");
		String terminal = this.getParam(request, "terminal");
		String merchant_id = this.getParam(request, "merchant_id");
		String timestamp = this.getParam(request, "timestamp");
		String version = this.getParam(request, "version");
		String type = this.getParam(request, "type");
		String json_param = this.getParam(request, "json_param");
		String hmac = this.getParam(request, "hmac");
		String md_str = terminal +merchant_id + timestamp  +type+ version+json_param;
		String merchant_name="";
		String order_id="";
		//改签日志
		ChangeLogVO log=new ChangeLogVO();
		logger.info("取消改签接口-用户参数：" + md_str);
		try {
			if (StringUtil.isEmpty(merchant_id)
					|| StringUtil.isEmpty(timestamp) || StringUtil.isEmpty(type)
					|| StringUtil.isEmpty(version) || StringUtil.isEmpty(hmac)
					|| StringUtil.isEmpty(json_param)|| ParamCheckUtil.isNotCheck(timestamp,ParamCheckUtil.TIMESTAMP_REGEX)) {
				logger.info("取消改签接口-参数校验error:必要参数为空或格式错误!");
				printJson(response, getJson("003").toString());
				return;
			}
	
			Map<String, String> merchantSetting = commonService.queryMerchantInfo(merchant_id);
			if (merchantSetting == null) {
				logger.info("取消改签接口-用户身份校验error:不存在的用户!");
				printJson(response, getJson("004").toString());
				return;
			}
			if ("00".equals(merchantSetting.get("merchant_status"))) {
				logger.info("取消改签接口-规则error:合作商户已禁用，请客服人员通知商户！");
				printJson(response, getJson("007").toString());
				return;
			}
			merchant_name = merchantSetting.get("merchant_name");
			logger.info("取消改签接口-用户:【" + merchant_name+ "】登入成功!");
			logger.info("取消改签接口-用户传递hmac：【" + hmac + "】");
			// 加密明文
			String hmac_19 = Md5Encrypt.getKeyedDigestFor19Pay(md_str
						+ merchantSetting.get("sign_key"), "", "utf-8");
			
			logger.info("取消改签接口-系统加密hmac：【" + hmac_19 + "】");
			if (!hmac_19.equals(hmac)) {
				logger.info("取消改签接口-安全校验error:不符合安全校验规则。");
				printJson(response, getJson("002").toString());
				return;
			}
			//业务参数
			JSONObject json_params = JSONObject.fromObject(json_param);
			order_id = json_params.getString("order_id");//订单号
			String callbackurl = json_params.getString("callbackurl");//回调地址
			String reqtoken = json_params.getString("reqtoken");//唯一标识
			logger.info(merchantSetting.get("merchant_name")+"取消改签,orderId : " + order_id + "reqtoken: " + reqtoken);
			
			/*检查业务参数*/
			if("".equals(order_id) || "".equals(callbackurl)) {
				printJson(response, getJson(TrainConsts.RETURN_CODE_003).toString());
				return;
			}
			/*查询订单*/
			OrderInfo orderInfo = orderService.queryOrderInfo(order_id);
			if(orderInfo == null || orderInfo.getOrder_status() == null){
				/*订单不存在*/
				logger.info(order_id+"-取消改签ERROR,订单不存在order_id:"+order_id);
				printJson(response,getJson(TrainConsts.RETURN_CODE_301).toString());
				return;
			}
		
			/*查询改签预订票并更新状态*/
			ChangeInfo changeInfo = changeService.getChangeInfoByReqtoken(reqtoken);
			if(changeInfo==null){
				/*订单不存在*/
				logger.info(order_id+"-取消改签ERROR,订单不存在：order_id:"+order_id);
				printJson(response,getJson(TrainConsts.RETURN_CODE_811).toString());
				return;
			}else if(!(changeInfo.getChange_status().equals(TrainConsts.TRAIN_REQUEST_CHANGE_SUCCESS)||changeInfo.getChange_status().equals(TrainConsts.TRAIN_CANCEL_CHANGE_FAIL))){
				//取消失败和改签占位成功，可以申请取消
				/*订单状态不正确*/
				logger.info(order_id+"-取消改签ERROR,订单状态不正确order_id:"+order_id);
				printJson(response,getJson(TrainConsts.RETURN_CODE_811).toString());
				return;
			}
		
			log.setChange_id(changeInfo.getChange_id());
			Date currentTime = new Date();
			/*预订成功后的30分钟内才能取消改签*/
			Date bookTime = DateUtil.stringToDate(changeInfo.getBook_ticket_time(), DateUtil.DATE_FMT3);
			long minuteDiff = DateUtil.minuteDiff(currentTime, bookTime);
			if(minuteDiff > 30) {
				logger.info(order_id+"-取消改签ERROR,距离改签车票预订时间超过30分钟");
				printJson(response,getJson(TrainConsts.RETURN_CODE_809).toString());
				return;
			}
			/*将状态为14或24、预订成功的改签状态都改为21、改签取消*/ 
			//改签占位成功,改签取消失败 ->改签取消
			changeInfo.setChange_status(TrainConsts.TRAIN_CANCEL_CHANGE);
			changeInfo.setChange_notify_count(0);
			changeInfo.setChange_notify_status(TrainConsts.CHANGE_NOTIFY_PRE);
			changeInfo.setCallbackurl(callbackurl);
			/*更新改签状态*/
			changeService.updateChangeInfo(changeInfo);
			log.setContent(merchant_name+"取消改签success ,orderId" + order_id);
			printJson(response,getJson(TrainConsts.RETURN_CODE_000).toString());
		} catch (Exception e) {
			logger.info(merchant_name+"取消改签异常"+e.getMessage(),e);
			printJson(response,getJson(TrainConsts.RETURN_CODE_001).toString());
			log.setContent(merchant_name+"取消改签异常!");
		} finally {
			log.setOrder_id(order_id);
			log.setOpt_person(merchant_id);
			changeService.addChangeLog(log);
		}

	}
	
	/**
	 * 确认改签
	 */
	public void confirmChange(HttpServletRequest request,HttpServletResponse response) {
		logger.info("确认改签开始");
		String terminal = this.getParam(request, "terminal");
		String merchant_id = this.getParam(request, "merchant_id");
		String timestamp = this.getParam(request, "timestamp");
		String version = this.getParam(request, "version");
		String type = this.getParam(request, "type");
		String json_param = this.getParam(request, "json_param");
		String hmac = this.getParam(request, "hmac");
		String md_str = terminal+merchant_id+timestamp+type+version+json_param;
		String merchant_name="";
		String order_id="";
		//改签日志
		ChangeLogVO log=new ChangeLogVO();
		logger.info("确认改签接口-用户参数：" + md_str);
		try {
			if (StringUtil.isEmpty(merchant_id)
					|| StringUtil.isEmpty(timestamp) || StringUtil.isEmpty(type)
					|| StringUtil.isEmpty(version) || StringUtil.isEmpty(hmac)
					|| StringUtil.isEmpty(json_param)|| ParamCheckUtil.isNotCheck(timestamp,ParamCheckUtil.TIMESTAMP_REGEX)) {
				logger.info("确认改签接口-参数校验error:必要参数为空或格式错误!");
				printJson(response, getJson("003").toString());
				return;
			}
	
			Map<String, String> merchantSetting = commonService.queryMerchantInfo(merchant_id);
			if (merchantSetting == null) {
				logger.info("确认改签接口-用户身份校验error:不存在的用户!");
				printJson(response, getJson("004").toString());
				return;
			}
			if ("00".equals(merchantSetting.get("merchant_status"))) {
				logger.info("确认改签接口-规则error:合作商户已禁用，请客服人员通知商户！");
				printJson(response, getJson("007").toString());
				return;
			}
			merchant_name = merchantSetting.get("merchant_name");
			logger.info("确认改签接口-用户:【" + merchant_name+ "】登入成功!");
			logger.info("确认改签接口-用户传递hmac：【" + hmac + "】");
			// 加密明文
			String hmac_19 = Md5Encrypt.getKeyedDigestFor19Pay(md_str
						+ merchantSetting.get("sign_key"), "", "utf-8");
			
			logger.info("确认改签接口-系统加密hmac：【" + hmac_19 + "】");
			if (!hmac_19.equals(hmac)) {
				logger.info("确认改签接口-安全校验error:不符合安全校验规则。");
				printJson(response, getJson("002").toString());
				return;
			}
			//业务参数
			JSONObject json_params = JSONObject.fromObject(json_param);
			order_id = json_params.getString("order_id");//订单号
			String callbackurl = json_params.getString("callbackurl");//回调地址
			String reqtoken = json_params.getString("reqtoken");//唯一标识
			logger.info(merchantSetting.get("merchant_name")+"确认改签,orderId : " + order_id + "reqtoken: " + reqtoken);
			
			/*检查业务参数*/
			if("".equals(order_id) || "".equals(callbackurl)) {
				printJson(response, getJson(TrainConsts.RETURN_CODE_003).toString());
				return;
			}
			/*查询订单*/
			OrderInfo orderInfo = orderService.queryOrderInfo(order_id);
			if(orderInfo == null || orderInfo.getOrder_status() == null){
				/*订单不存在*/
				logger.info(order_id+"-确认改签ERROR,订单不存在order_id:"+order_id);
				printJson(response,getJson(TrainConsts.RETURN_CODE_301).toString());
				return;
			}
		
			/*查询改签预订票并更新状态*/
			ChangeInfo changeInfo = changeService.getChangeInfoByReqtoken(reqtoken);
			if(changeInfo==null){
				/*订单状态不正确*/
				logger.info(order_id+"-确认改签ERROR,订单不存在order_id:"+order_id);
				printJson(response,getJson(TrainConsts.RETURN_CODE_812).toString());
				return;
			}
			if(!changeInfo.getChange_status().equals(TrainConsts.TRAIN_REQUEST_CHANGE_SUCCESS)){
				/*订单状态不正确*/
				logger.info(order_id+"-确认改签ERROR,订单状态不正确order_id:"+order_id);
				printJson(response,getJson(TrainConsts.RETURN_CODE_812).toString());
				return;
			}
		
			log.setChange_id(changeInfo.getChange_id());
			/*22:34:59*/
			Calendar time_22_44_59 = Calendar.getInstance();
			time_22_44_59.set(Calendar.HOUR_OF_DAY, 22);
			time_22_44_59.set(Calendar.MINUTE, 44);
			time_22_44_59.set(Calendar.SECOND, 59);
			/*23:30:00*/
			Calendar time_23_30_00 = Calendar.getInstance();
			time_23_30_00.set(Calendar.HOUR_OF_DAY, 23);
			time_23_30_00.set(Calendar.MINUTE, 30);
			time_23_30_00.set(Calendar.SECOND, 00);
			Calendar currentTime = Calendar.getInstance();
			Calendar bookTime = Calendar.getInstance();
			Date book = DateUtil.stringToDate(changeInfo.getBook_ticket_time(), DateUtil.DATE_FMT3);
			bookTime.setTime(book);
			
			boolean timeOut = false;
			if(bookTime.before(time_22_44_59)) {//当天22:44:59之前预订
				/*30分钟的付款时间*/
				System.out.println("current : " + DateUtil.dateToString(currentTime.getTime(), "yyyy-MM-dd HH:mm:ss"));
				System.out.println("book : " + DateUtil.dateToString(book, "yyyy-MM-dd HH:mm:ss"));
				if(DateUtil.minuteDiff(currentTime.getTime(), book) > 30) {
					timeOut = true;
				}
				logger.info("当天22:44:59之前预订,30分钟的付款时间,timeout" + timeOut);
			} else if(bookTime.after(time_22_44_59)) {//当天22:44:59之后预订
				/*23:30:00之前付款*/
				if(currentTime.after(time_23_30_00)) {
					timeOut = true;
				}
				logger.info("当天22:44:59之后预订,23:30:00之前付款,timeout" + timeOut);
			}
			if(timeOut) {
				logger.info(order_id+"确认改签ERROR,确认改签的请求时间已超过规定时间");
				printJson(response,getJson(TrainConsts.RETURN_CODE_810).toString());
				return;
			}
			/*确认改签*/
			/*if(StrUtil.isNotEmpty(changeInfo.getChange_refund_money()) && StrUtil.isNotEmpty(changeInfo.getChange_receive_money())) {
				
				Double change_refund_money = Double.parseDouble(changeInfo.getChange_refund_money());
				Double change_receive_money = Double.parseDouble(changeInfo.getChange_receive_money());
				if(change_receive_money > change_refund_money) {
					changeInfo.setChange_status(TrainConsts.TRAIN_CONFIRM_CHANGE_START_PAY);
				} else {
					changeInfo.setChange_status(TrainConsts.TRAIN_CONFIRM_CHANGE);
				}
			} else {
				changeInfo.setChange_status(TrainConsts.TRAIN_CONFIRM_CHANGE);
			}*/
			changeInfo.setChange_status(TrainConsts.TRAIN_CONFIRM_CHANGE);
			changeInfo.setCallbackurl(callbackurl);
			changeInfo.setReqtoken(reqtoken);
			changeInfo.setChange_notify_count(0);
			changeInfo.setChange_notify_status(TrainConsts.CHANGE_NOTIFY_PRE);
			/*更新车票改签状态*/
			logger.info(order_id+",callbackurl:"+callbackurl+"reqtoken:"+reqtoken);
			int count=changeService.updateChangeInfo(changeInfo);
			logger.info(order_id+",callbackurl:"+callbackurl+"reqtoken:"+reqtoken+"count:"+count);
			log.setContent(merchant_name+"确认改签success ,orderId" + order_id);
			printJson(response,getJson(TrainConsts.RETURN_CODE_000).toString());
		} catch (Exception e) {
			logger.info(merchant_name+"-确认改签异常"+e.getMessage(),e);
			printJson(response,getJson(TrainConsts.RETURN_CODE_001).toString());
			log.setContent(merchant_name+"确认改签异常!");
		} finally {
			log.setOrder_id(order_id);
			log.setOpt_person(merchant_id);
			changeService.addChangeLog(log);
		}
	} 
	@RequestMapping(value = "/testJob.do")
	public void  testJob(HttpServletRequest request,HttpServletResponse response){
		System.out.println(request.getAttribute("return_code"));
	}
	
}
