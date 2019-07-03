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

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jiexun.iface.util.StringUtil;
import com.l9e.common.ExternalBase;
import com.l9e.transaction.service.CommonService;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.vo.BookDetailInfo;
import com.l9e.transaction.vo.BookInfo;
import com.l9e.transaction.vo.OrderInfoBx;
import com.l9e.util.HttpUtil;
import com.l9e.util.Md5Encrypt;
import com.l9e.util.MemcachedUtil;
import com.l9e.util.ParamCheckUtil;
import com.l9e.util.UrlFormatUtil;

/**
 * 对外接口
 * 
 * @author zhangjc02
 * 
 */

@Controller
public class ExternalMainController extends ExternalBase {
	@Resource
	protected NewBuyTicketController newBuyTicketController;
	@Resource
	private CreateOrderController createOrder;
	@Resource
	private OrderService orderService;
	@Resource
	private QueryOrderController queryOrder;
	@Resource
	private RefundTicketController refundTicketController;
	@Resource
	private CommonService commonService;

	/**
	 * web对外接口主入口
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/externalInterface.do")
	public void ExternalInterface(HttpServletRequest request,
			HttpServletResponse response) {
		String type = this.getParam(request, "type");
		 if ("createOrder".equals(type)) {
			createOrder(request, response); // 下单
		} else if ("queryOrderInfo".equals(type)) {
			queryOrderInfo(request, response); // 查询订单
		}else if ("refundTicket".equals(type)) {
			refundTicketController.refundTicket(request, response); // 退款
		}else if ("queryBxInfo".equals(type)) {
			queryBxInfo(request, response); // 保险信息查询
		}else {
			logger.info("hcp对外接口-非法的type接口参数 ：type=" + type);
			logger.info("请求参数异常type:"+type);
			printJson(response, getJson("003").toString());
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
			if(null == MemcachedUtil.getInstance().getAttribute(key)){
				 logger.info("缓存当前商户订单号："+key);
				MemcachedUtil.getInstance().setAttribute(key, key, 5*1000);
			}else{
				String str = (String)MemcachedUtil.getInstance().getAttribute(key);
				if(str.equals(key)){
					logger.info("购票下单接口-下单规则error:重复下单异常："+bookInfo.getMerchant_order_id());
					printJson(response, getJson("201").toString());
					return;
				}
			}
			logger.info("购票下单接口-用户下单参数校验开始");
			if (ParamCheckUtil.createOrderParamIsEmpty(bookInfo)) {
				logger.info("购票下单接口-下单参数error:下订单参数为空!");
				printJson(response, getJson("204").toString());
				return;
			}
			if (ParamCheckUtil.createOrderParamCheck(bookInfo)) {
				logger.info("购票下单接口-下单参数error:下订单参数格式错误!");
				printJson(response, getJson("204").toString());
				return;
			}
			if (ParamCheckUtil.bookDetailInfoCheck(bookInfo
					.getBook_detail_list(), bookInfo.getOrder_level())) {
				logger.info("购票下单接口-下单参数error:订单内乘客信息参数为空或者格式错误!");
				printJson(response, getJson("205").toString());
				return;
			}

			if (bookInfo.getBook_detail_list().size() > 5) {
				logger.info("购票下单接口-下单规则error:下单票数异常");
				printJson(response, getJson("203").toString());
				return;
			}
			//验证是否重复下单
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("merchant_id", merchant_id);
			paramMap.put("merchant_order_id", bookInfo.getMerchant_order_id());
			int count = orderService.queryOrderListCount(paramMap);
			if(count>0){
				logger.info("重复下单异常:" + bookInfo.getMerchant_order_id());
				printJson(response, getJson("201").toString());
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
	        try{
	        	String reqParams = UrlFormatUtil.CreateUrl("", maps, "", "UTF-8");
	        	result = HttpUtil.sendByPost(real_name_verify_url, reqParams, "UTF-8","15000","15000");
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
	        }
//	        logger.info("不核验用户信息，默认成功！");
	        JSONObject returnJson = new JSONObject();
		    if("".equals(fail_pas)){
		    	createOrder.createTrainOrder(merchantSetting ,bookInfo, request, response);
		    }else{
		    	logger.info("以下用户："+fail_pas+"未通过铁路系统审核,merchant_order_id="+bookInfo.getMerchant_order_id());
			    returnJson.put("return_code", "203");
			    returnJson.put("message", "以下用户："+fail_pas+"未通过铁路系统审核");
			    printJson(response, returnJson.toString());
		    }
		} catch (Exception e) {
			logger.error("购票下单接口-下单异常exception:第三方下单失败", e);
			printJson(response, getJson("001").toString());
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
//				|| StringUtil.isEmpty(order_id)
				|| ParamCheckUtil.isNotCheck(timestamp,
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
			queryOrder.queryOrderList(request, response, order_id,merchant_order_id);
		}
	}


}
