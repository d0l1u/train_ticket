package com.l9e.transaction.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.stereotype.Component;

import com.l9e.common.BaseController;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.ChangeService;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.service.RefundService;
import com.l9e.transaction.vo.OrderInfoCp;
import com.l9e.util.AmountUtil;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.MathUtil;
import com.l9e.util.Md5Encrypt;
import com.l9e.util.MemcachedUtil;

@Component
public class RefundTicketController extends BaseController{
	
	private static final Logger logger = Logger.getLogger(RefundTicketController.class);
	
	@Resource
	private OrderService orderService;
	
	@Resource
	private RefundService refundService;
	@Resource 
	private ChangeService changeService;
	
	/**
	 * 申请退票接口
	 * @param request
	 * @param response
	 */
	public void refundTicket(HttpServletRequest request, HttpServletResponse response){
		logger.info("调用对外申请退款接口操作！");
		
		JSONObject jsonRes = new JSONObject(); 
		
		//获取协议参数
		String terminal = this.getParam(request, "terminal");
		String merchant_id = this.getParam(request, "merchant_id");
		String timestamp = this.getParam(request, "timestamp");
		String type = this.getParam(request, "type");
		String version = this.getParam(request, "version");
		String spare_pro1 = this.getParam(request, "spare_pro1");
		String spare_pro2 = this.getParam(request, "spare_pro2");
		String json_param = this.getParam(request, "json_param");
		String hmac = this.getParam(request, "hmac");
		logger.info("申请退款，json_param："+json_param);
		String merchant_order_id = "";//初始化
		String order_id = "";
		Map<String,String> merchantSetting = commonService.queryMerchantInfo(merchant_id);
		try{
			//验签
			String source_str = terminal + merchant_id + timestamp + type + version + spare_pro1 + spare_pro2 + json_param;
			logger.info("source_str="+source_str);
			String hmac_19 = Md5Encrypt.getKeyedDigestFor19Pay(source_str+merchantSetting.get("sign_key"),"","utf-8");
			if(!hmac.equalsIgnoreCase(hmac_19)){
				logger.info("验签失败！");
				jsonRes.put("merchant_order_id", merchant_order_id);
				jsonRes.put("message", "安全验证错误，不符合安全校验规则。");
				jsonRes.put("order_id", order_id);
				jsonRes.put("return_code", "002");
				jsonRes.put("trip_no","");
				jsonRes.put("status", "FAILURE");//申请失败
				jsonRes.put("fail_reason", "验签失败");
				write2Response(response, jsonRes.toString());
				return;
			}
			
			//解析参数
			JSONObject jsonObj = JSONObject.fromObject(json_param);
			
			String comment = this.getParam(request, "comment");
			merchant_order_id = jsonObj.getString("merchant_order_id");
			order_id = jsonObj.getString("order_id");
			String refund_picture_url = jsonObj.getString("refund_picture_url");
			String refund_result_url = jsonObj.getString("refund_result_url");
			String refund_type = jsonObj.getString("refund_type");
			JSONArray refundinfoJa = JSONArray.fromObject(jsonObj.get("refundinfo"));
			String request_id = jsonObj.getString("request_id");
			String refund_order_type="";
			if(jsonObj.containsKey("refund_order_type")){
				refund_order_type = jsonObj.getString("refund_order_type");//1：原票退款 3：改签退款
			}
			//验证参数是否为空
			if(checkParamEmpty(merchant_order_id, "merchant_order_id", jsonRes)){
				write2Response(response, jsonRes.toString());
				return;
			}
			if(checkParamEmpty(order_id, "order_id", jsonRes)){
				write2Response(response, jsonRes.toString());
				return;
			}
			if(checkParamEmpty(refund_result_url, "refund_result_url", jsonRes)){
				write2Response(response, jsonRes.toString());
				return;
			}
			if(checkParamEmpty(refund_type, "refund_type", jsonRes)){
				write2Response(response, jsonRes.toString());
				return;
			}
			if(checkParamEmpty(request_id, "request_id", jsonRes)){
				write2Response(response, jsonRes.toString());
				return;
			}
			
			//参数refund_type="part" refundinfo必填
			if("part".equalsIgnoreCase(refund_type)){
				if(refundinfoJa == null || refundinfoJa.size()==0 || "[]".equals(refundinfoJa.toString())){
					logger.info("refundinfo为空！");
					jsonRes.put("merchant_order_id", merchant_order_id);
					jsonRes.put("message", "参数refund_type=part，退票乘客信息refundinfo必填。");
					jsonRes.put("order_id", order_id);
					jsonRes.put("return_code", "003");
					jsonRes.put("trip_no","");
					jsonRes.put("status", "FAILURE");//申请失败
					jsonRes.put("fail_reason", "参数refund_type=part，退票乘客信息refundinfo必填。");
					write2Response(response, jsonRes.toString());
					return;
				}
			}
			
			
			//解析退票乘客信息，查询退的车票id
			List<String> cp_list = new ArrayList<String>();
			List<String> refund_cp_list = new ArrayList<String>();
			
			/**查询订单下是否已有退票信息*/
			refund_cp_list = orderService.queryRefund_cp_list(order_id);
			//改签退票
			if(("3").equals(refund_order_type)){
				if("all".equals(refund_type)){
					cp_list = changeService.getCpListByOrderId(order_id);
				}else{
					for(int i=0; i<refundinfoJa.size(); i++){
						JSONObject obj = (JSONObject) refundinfoJa.get(i);
						String cp_id= obj.getString("cp_id");
						if(!cp_list.contains(cp_id)){
							cp_list.add(cp_id);
						}
					}
				}
			}else{
				if("all".equals(refund_type)){
					cp_list = orderService.queryCp_idList(order_id);
				}else{
					Map<String,String> paramMap = new HashMap<String, String>();
					paramMap.put("order_id", order_id);
					for(int i=0; i<refundinfoJa.size(); i++){
						JSONObject obj = (JSONObject) refundinfoJa.get(i);
						paramMap.put("id_type", obj.getString("id_type"));
						paramMap.put("user_ids", obj.getString("user_ids"));
						paramMap.put("user_name", obj.getString("user_name"));
						paramMap.put("ticket_type", obj.getString("ticket_type"));
						paramMap.put("cp_id", obj.getString("cp_id"));
						List<String> cpid_list = orderService.queryCp_idByIds(paramMap);
						
						if(cpid_list.size()==1){
							cp_list.add(cpid_list.get(0));
						}else{
							for(String cp_id : cpid_list){
								if(!cp_list.contains(cp_id)){
									cp_list.add(cp_id);
									break;
								}
							}
						}
					}
				}
			}
			
		
			if(cp_list.size()==0){
				logger.info("本次退票请求乘客信息参数有误，merchant_refund_seq=" + request_id
						+"&channel=" + merchant_id + "&merchant_order_id=" + merchant_order_id);
				
				jsonRes.put("merchant_order_id", merchant_order_id);
				jsonRes.put("message", "本次退票请求乘客信息参数有误。");
				jsonRes.put("order_id", order_id);
				jsonRes.put("return_code", "003");
				jsonRes.put("trip_no","");
				jsonRes.put("status", "FAILURE");
				jsonRes.put("fail_reason", "本次退票请求乘客信息参数有误，不能申请退票操作。");
				write2Response(response, jsonRes.toString());
				return;
			}
			/**拦截频繁提交*/
			if("part".equalsIgnoreCase(refund_type)){
				String key = merchant_order_id+cp_list.get(0);
				if(null == MemcachedUtil.getInstance().getAttribute(key)){
					 logger.info("缓存当前商户订单号："+key);
					MemcachedUtil.getInstance().setAttribute(key, key, 10*1000);
				}else{
					String str = (String)MemcachedUtil.getInstance().getAttribute(key);
					if(str.equals(key)){
						logger.info("本次退票请求为重复请求，请求过于频繁，merchant_refund_seq=" + request_id
								+"&channel=" + merchant_id + "&merchant_order_id" + merchant_order_id);
						
						jsonRes.put("merchant_order_id", merchant_order_id);
						jsonRes.put("message", "本次退票请求为重复请求，请求过于频繁。");
						jsonRes.put("order_id", order_id);
						jsonRes.put("return_code", "000");
						jsonRes.put("trip_no","");
						jsonRes.put("status", "REPEAT");//重复申请
						jsonRes.put("fail_reason", "本次退票请求为重复请求，请求过于频繁。");
						write2Response(response, jsonRes.toString());
					}
				}
			}
			
			/**重复提交去重*/
			cp_list=getNowRefundCpList(order_id,cp_list,refund_cp_list);
			
			if(cp_list.size()==0){
				logger.info("商户退票流水已经发生过退票，拒绝本次操作，merchant_refund_seq=" + request_id
						+"&channel=" + merchant_id + "&merchant_order_id=" + merchant_order_id);
				
				jsonRes.put("merchant_order_id", merchant_order_id);
				jsonRes.put("message", "本次退票请求为重复请求。");
				jsonRes.put("order_id", order_id);
				jsonRes.put("return_code", "000");
				jsonRes.put("trip_no","");
				jsonRes.put("status", "REPEAT");//重复申请 返回退款申请成功
				jsonRes.put("fail_reason", "本次退票请求为重复请求，退款流水号已存在或已经退款完成。");
				write2Response(response, jsonRes.toString());
				return;
			}
			if(cp_list.size()==1 && cp_list.get(0).contains("success")){
				String refund_seq = cp_list.get(0).split("_")[1];
				jsonRes.put("merchant_order_id", merchant_order_id);
				jsonRes.put("message", "");
				jsonRes.put("order_id", order_id);
				jsonRes.put("return_code", "000");
				jsonRes.put("trip_no", refund_seq);
				jsonRes.put("status", "SUCCESS");//申请成功
				jsonRes.put("fail_reason", "");
				logger.info(jsonRes.toString());
				write2Response(response, jsonRes.toString());
				return;
			}
			
		/*	//退票受理时间
			Date currentDate = new Date();
			String datePre = DateUtil.dateToString(currentDate, "yyyyMMdd");		
			Date begin = DateUtil.stringToDate(datePre + "060000", "yyyyMMddHHmmss");//6:00
			Date end = DateUtil.stringToDate(datePre + "230000", "yyyyMMddHHmmss");//23:00
			if(currentDate.before(begin) || currentDate.after(end)){
				logger.info("不受理该退款，发起时间为："+DateUtil.dateToString(currentDate, "yyyy-MM-dd HH:mm:ss"));
				
				jsonRes.put("merchant_order_id", merchant_order_id);
				jsonRes.put("message", "不受理本次退票申请，每日受理退票时间为早6点至晚23点。");
				jsonRes.put("order_id", order_id);
				jsonRes.put("return_code", "000");
				jsonRes.put("trip_no","");
				jsonRes.put("status", "ILLEGAL_TIME");//受理退票时间
				jsonRes.put("fail_reason", "不受理本次退票申请，每日受理退票时间为早6点至晚23点。");
				write2Response(response, jsonRes.toString());
				return;
				
			}*/
			logger.info("[对外接口使用商主动发起退款]order_id="+order_id);
			
			//验证退票申请是否为重复申请
			Map<String, String> merchantMap = new HashMap<String, String>();
			merchantMap.put("merchant_refund_seq", request_id);//商户退款流水号
			merchantMap.put("channel", merchant_id);//渠道
			merchantMap.put("merchant_order_id", merchant_order_id);
			int rcount = refundService.queryRefundCountByMerchantSeq(merchantMap);
			
			if(rcount > 0){
				logger.info("商户退票流水已经发生过退票，拒绝本次操作，merchant_refund_seq=" + request_id
						+"&channel=" + merchant_id + "&merchant_order_id" + merchant_order_id);
				
				jsonRes.put("merchant_order_id", merchant_order_id);
				jsonRes.put("message", "本次退票请求为重复请求。");
				jsonRes.put("order_id", order_id);
				jsonRes.put("return_code", "000");
				jsonRes.put("trip_no","");
				jsonRes.put("status", "REPEAT");//重复申请
				jsonRes.put("fail_reason", "本次退票请求为重复请求，退款流水号已存在。");
				write2Response(response, jsonRes.toString());
				return;
			}
			
			
			//验证是否出票完成的，否则不能进行退票申请
			Map<String, String> orderMap = new HashMap<String, String>();
			orderMap.put("order_id", order_id);
			orderMap.put("merchant_order_id", merchant_order_id);
			orderMap.put("merchant_id", merchant_id);
			String status = refundService.queryOrderStatusById(orderMap);
			
			if(StringUtils.isEmpty(status) || !TrainConsts.OUT_SUCCESS.equals(status)){
				
				logger.info("商户退票流水不是出票成功状态，不能申请退票操作，merchant_refund_seq=" + request_id
						+"&channel=" + merchant_id + "&merchant_order_id" + merchant_order_id + "&status=" + status);
				
				jsonRes.put("merchant_order_id", merchant_order_id);
				jsonRes.put("message", "该订单不是出票成功状态，不能申请退票操作。");
				jsonRes.put("order_id", order_id);
				jsonRes.put("return_code", "000");
				jsonRes.put("trip_no","");
				jsonRes.put("status", "FAILURE");//申请失败
				jsonRes.put("fail_reason", "该订单不是出票成功状态，不能申请退票操作。");
				write2Response(response, jsonRes.toString());
				return;
				
			}
			
			
			//计算退票金额
			double buy_money = 0;
			Double refund_money = 0.0;
			String refund_seq = CreateIDUtil.createID("TK");//生成退票流水号
			Double feePercent = 0.0;
			List<Map<String, String>> refundList = new ArrayList<Map<String, String>>();
			Map<String, String> refundMap = new HashMap<String, String>();
			for(String cp_id:cp_list){
				//计算费率
				if(("3").equals(refund_order_type)){
					//改签退票
					Map<String,Object> param = new HashMap<String,Object>();
					param.put("order_id", order_id);
					param.put("new_cp_id", cp_id);
					Map<String,Object> changeCpInfo = changeService.queryChangeCpInfo(param);
					String start_time=DateUtil.timestampToString((java.sql.Timestamp)changeCpInfo.get("change_from_time"),"yyyy-MM-dd HH:mm:ss");			
					feePercent = this.calculateRefundPercent(start_time);
					buy_money = MathUtil.bigDecimalTODouble((java.math.BigDecimal)changeCpInfo.get("change_buy_money"));
					refundMap.put("refund_type", TrainConsts.REFUND_TYPE_ALTER);//用户线上退款
				}else{
					Map<String, String> timeMap = refundService.querySpecTimeBeforeFrom(order_id);
					String start_time = timeMap.get("travel_time")+" "+ timeMap.get("from_time")+":00";
					feePercent = this.calculateRefundPercent(start_time);
					OrderInfoCp cpInfo = orderService.queryCpInfoByCpId(cp_id);
					buy_money = Double.parseDouble(cpInfo.getBuy_money());
					refundMap.put("refund_type", TrainConsts.REFUND_TYPE_ONLINE);//用户线上退款
				}
				double sxf = 0.0;
				if(feePercent!=0){
					sxf = AmountUtil.quarterConvert(AmountUtil.mul(buy_money, feePercent));//手续费
					sxf = sxf > 2 ? sxf : 2;
				}
				refund_money = AmountUtil.ceil(AmountUtil.sub(buy_money, sxf));
				refundMap.put("order_id", order_id);
				refundMap.put("merchant_order_id", merchant_order_id);
				refundMap.put("cp_id", cp_id);
				refundMap.put("merchant_refund_seq", request_id);//商户退款流水号
				refundMap.put("refund_seq", refund_seq+"AA"+cp_id.substring(cp_id.length()-2, cp_id.length()));//生成退票流水号;
				refundMap.put("refund_money", String.valueOf(refund_money));//退款金额
				refundMap.put("user_remark", comment);
				refundMap.put("refund_purl", refund_picture_url);
				refundMap.put("refund_status", TrainConsts.REFUND_STATUS_WAIT);//等待退票
				Map<String, String> cpMap = new HashMap<String, String>();
				cpMap.put("order_id", order_id);
				cpMap.put("cp_id", cp_id);
				refundMap.put("old_refund_status", refundService.queryRefundStatusByCpId(cpMap));//旧的退款状态
				refundMap.put("refund_percent", feePercent.toString());
				refundMap.put("notify_url", refund_result_url);
				refundMap.put("channel", merchant_id);//渠道
				refundList.add(refundMap);
			
			}
			boolean ret = refundService.addUserRefundStream(refundList, jsonRes);//生成用户申请退票
			Map<String,String> map = new HashMap<String,String>();
			map.put("order_id", order_id);
			map.put("status", "REFUNDING");
			//更新订单退款状态
			refundService.updateOrderRefundStatus(map);
			if(ret){//申请成功
				jsonRes.put("merchant_order_id", merchant_order_id);
				jsonRes.put("message", "");
				jsonRes.put("order_id", order_id);
				jsonRes.put("return_code", "000");
				jsonRes.put("trip_no", refund_seq);
				jsonRes.put("status", "SUCCESS");//申请成功
				jsonRes.put("fail_reason", "");
				logger.info(jsonRes.toString());
				write2Response(response, jsonRes.toString());
			}else{//失败
				logger.info(jsonRes.toString());
				write2Response(response, jsonRes.toString());
			}
			
		}catch(Exception e){
			logger.error("对外退票接口操作异常！", e);
			
			jsonRes.put("merchant_order_id", merchant_order_id);
			jsonRes.put("message", "系统错误，未知服务异常。");
			jsonRes.put("order_id", order_id);
			jsonRes.put("return_code", "001");
			jsonRes.put("trip_no","");
			jsonRes.put("status", "FAILURE");//申请失败
			jsonRes.put("fail_reason", "程序未知异常，请重新申请。");
			write2Response(response, jsonRes.toString());
		}
	}
	
	/**
	 * 参数是否为空
	 * @param param_val
	 * @param param_name
	 * @param jsonRes
	 * @return
	 */
	private boolean checkParamEmpty(String param_val, String param_name, JSONObject jsonRes){
		if(StringUtils.isEmpty(param_val)){
			logger.info("输入参数错误，" + param_name + "不能为空");
			
			jsonRes.put("merchant_order_id", "");
			jsonRes.put("message", "输入参数错误，" + param_name + "不能为空");
			jsonRes.put("order_id", "");
			jsonRes.put("return_code", "003");//输入参数错误。
			jsonRes.put("trip_no","");
			jsonRes.put("status", "FAILURE");//申请失败
			jsonRes.put("fail_reason", param_name + "不能为空");
			return true;
		}else{
			return false;
		}
	}

	/**
	private void eopRefund(Map<String,String> map){
		RefundBean refundBean = new RefundBean();
		refundBean.setService("refund");
		refundBean.setSign_type("MD5");
		refundBean.setTimestamp(DateUtil.dateToString(new Date(), DateUtil.DATE_FMT2));
		refundBean.setPartner_id(partner_id);
		refundBean.setInput_charset("utf-8");
		refundBean.setAsp_refund_number(refund_seq);
		refundBean.setAsp_refund_money(notifyMap.get("refund_money"));
		refundBean.setRefund_reason(notifyMap.get("refund_reason"));
		refundBean.setEop_order_id(notifyMap.get("eop_order_id"));
		
		RefundBean reBean = ASPUtil.refund(refundBean, notifyMap.get("notify_url")+"?data_type=JSON");
		if("SUCCESS".equals(reBean.getResult_code())){
			//更新通知完成
			Map<String, String> map = new HashMap<String, String>();
			map.put("notify_id", notify_id);
			map.put("order_id", order_id);
			map.put("eop_refund_seq", reBean.getEop_refund_seq());
			map.put("notify_status", TrainConsts.REFUND_NOTIFY_FINISH);
			refundService.updateEopRefundNotfiyFinish(map);
			logger.info("退票结果通知平台成功" + "订单号：" + order_id
					+ "，退款流水号：" + refund_seq);
		}else if("REFUNDING".equals(reBean.getResult_code())){
			logger.info("退票结果通知平台重复" + "订单号：" + order_id
					+ "，退款流水号：" + refund_seq);
		}else{
			logger.info("退票结果通知商户失败" + "订单号：" + order_id
					+ "，退款流水号：" + refund_seq+";失败原因："+reBean.getResult_desc());
		}
	}
	*/
	
	
	/**
	 * 通过匹配已退款cp list
	 * 和请求 cp list 
	 * 获取入库 cp list
	 * */
	private List<String> getNowRefundCpList( String order_id,List<String> actionRefundCpList, List<String> hadRefundCpList){
		List<String> cp_list = new ArrayList<String>();
		int hadSize=hadRefundCpList.size();
		int actionSize=actionRefundCpList.size();
		List<String> refund_seqList = new ArrayList<String>();
		if(hadSize==0){
			return actionRefundCpList;
		}else{
			boolean wea_up = false;
			String refund_seq = "";
			for(int i=0;i<actionSize;i++){
				String cp=actionRefundCpList.get(i);
				if(!hadRefundCpList.contains(cp)){
					cp_list.add(cp);//请求退款 原数据没有
				}else{
					//原有 对拒绝退款状态进行更新操作
					Map<String,String> param=new HashMap<String, String>();
					param.put("order_id", order_id);
					param.put("cp_id", cp);
					int count =	orderService.updateRefuseOrderStatus(param);
					refund_seq = orderService.queryRefundStreamSeq(param);
					refund_seqList.add(refund_seq);
					if(count!=0){
					logger.info("更新退款："+order_id+"/"+cp);
					wea_up = true;
					}
				}
			}
			if(cp_list.size()==0 && wea_up){
				cp_list.add("success_"+refund_seq);
			}
			return cp_list;
		}
	}
	/*
	 * 计算费率 
	 * 春运期间：
	 * 1、改签到春运期间，退票时收取20%手续费
	 * 2、其他情况参照非春运期间
	 * 
	 * 非春运期间
	 * ①开车前15天以上的，退票时不收退票费；
	 * ②开车前48小时以上、不足15天的，退票时收取票价5%的退票费；
	 * ③开车前24以上，不足48小时的，退票时收取票价10%退票费。
	 * 4开车前不足24小时，退票时收取20%退票费
	 */
	
	private Double calculateRefundPercent(String startTime){
		String from_15d = DateUtil.dateAddDaysFmt3(startTime,"-15");
		String from_24 = DateUtil.dateAddDaysFmt3(startTime,"-1");
		String from_48 = DateUtil.dateAddDaysFmt3(startTime,"-2");
		
		double feePercent = 0;
		if(new Date().before(DateUtil.stringToDate(from_15d, "yyyy-MM-dd HH:mm:ss"))){
			feePercent = 0.0;
		}else if(new Date().before(DateUtil.stringToDate(from_48, "yyyy-MM-dd HH:mm:ss"))){
			feePercent = 0.05;
		}else if(new Date().before(DateUtil.stringToDate(from_24, "yyyy-MM-dd HH:mm:ss"))){
			feePercent = 0.1;
		}else{
			feePercent = 0.2;
		}
		return feePercent;
	}
}
