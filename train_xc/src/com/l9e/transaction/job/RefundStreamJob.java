package com.l9e.transaction.job;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.jiexun.iface.util.StringUtil;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.CommonService;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.service.ReceiveNotifyService;
import com.l9e.transaction.service.RefundService;
import com.l9e.transaction.vo.ExternalLogsVo;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.transaction.vo.RefundReasonVo;
import com.l9e.util.AmountUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.HttpPostJsonUtil;
import com.l9e.util.Md5Encrypt;

/**
 * 退款结果通知job
 * @author zhangjc02
 *
 */
@Component("refundStreamJob")
public class RefundStreamJob {
	
	private static final Logger logger = Logger.getLogger(RefundStreamJob.class);
	
	@Resource
	ReceiveNotifyService receiveService;
	
	@Resource
	private RefundService refundService;
	
	@Resource
	private CommonService commonService;
	
	@Resource
	private OrderService orderService;
	
	public void refund(){
		//定时查询待通知退票结果的列表
		List<Map<String, String>> notifyList = refundService.queryRefundResultWaitList();
		for (Map<String, String> notifyMap : notifyList) {
			String order_id = notifyMap.get("order_id");
			String refund_seq = notifyMap.get("refund_seq");
			try {
				this.sendRefundNotfiyRequest(notifyMap);
			} catch (Exception e) {
				logger.error("发起退票结果通知异常，order_id="
						+order_id+"&refund_seq="+refund_seq, e);
			}
		}
	}
	
	private void sendRefundNotfiyRequest(Map<String, String> notifyMap) throws Exception{
		String order_id = notifyMap.get("order_id");
		String refund_seq = notifyMap.get("refund_seq");
		logger.info("发起退款通知，订单号："+order_id+"，流水号："+refund_seq);
		String notify_id = notifyMap.get("notify_id");
		
		//更新通知开始
		Map<String, String> beginMap = new HashMap<String, String>();
		beginMap.put("notify_id", notify_id);
		beginMap.put("order_id", order_id);
		beginMap.put("notify_status", TrainConsts.REFUND_NOTIFY_BEGIN);//开始通知
		beginMap.put("old_notify_status", notifyMap.get("notify_status"));//旧状态
		int count = refundService.updateRefundNotfiyBegin(beginMap);
		if(count == 0){
			logger.info("发起退票结果通知状态已经变化！order_id="+order_id);
			return;
		}
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("order_id", order_id);
		paramMap.put("refund_seq", refund_seq);
		List<Map<String, String>> rsList = refundService.queryRefundStreamListBySeq(paramMap);//根据流水号查询退票的流水
		
		List<Map<String, String>> cpList = refundService.queryCpListByOrderId(order_id);//根据订单号查询车票信息
		
		//生成json_param
		JSONArray jsArray = new JSONArray();
		JSONObject jsonObj = null;
		Double total_money = 0.0;//退票总金额
		int agreeNum = 0;//同意退票数
		int refuseNum = 0;//拒绝退票数
		String all_status = "";//整体退票状态
		
		for(Map<String, String> refundMap: rsList){
			jsonObj = new JSONObject();
			String status = refundMap.get("refund_status");
			String refund_money = refundMap.get("refund_money");
			
			Double refund_amount = Double.parseDouble(refund_money);

			for(Map<String, String> cpMap: cpList){
				if(refundMap.get("cp_id").equals(cpMap.get("cp_id"))){
					jsonObj.put("cp_id", cpMap.get("cp_id"));
					jsonObj.put("ids_type", cpMap.get("ids_type"));
					jsonObj.put("ticket_type", cpMap.get("ticket_type"));
					jsonObj.put("user_ids", cpMap.get("user_ids"));
					jsonObj.put("user_name", cpMap.get("user_name"));
					if(StringUtils.isEmpty(status)){
						logger.error("退票状态status为空！");
						return;
					}else if(TrainConsts.REFUND_STATUS_AGREE.equals(status) || TrainConsts.REFUND_STATUS_SUCCESS.equals(status)){
						jsonObj.put("fail_reason", "");
						jsonObj.put("status", "SUCCESS");
						total_money = AmountUtil.add(total_money, refund_amount);
						agreeNum++;
					}else if(TrainConsts.REFUND_STATUS_REFUSE.equals(status)){
						String reason = "其他";
						if(StringUtils.isNotEmpty(refundMap.get("our_remark")) 
								&& StringUtils.isNotEmpty(RefundReasonVo.getRefuseReason().get(refundMap.get("our_remark")))){
							reason = RefundReasonVo.getRefuseReason().get(refundMap.get("our_remark"));
						}
						if(StringUtils.isEmpty(reason)){
							reason = "其他";
						}
						jsonObj.put("fail_reason", reason);
						jsonObj.put("status", "FAILURE");
						refuseNum++;
					}else if(TrainConsts.REFUND_STATUS_SUCCESS.equals(status)){
						logger.error("已经退票成功一笔车票号："+refundMap.get("cp_id"));
					}else{
						logger.error("退票状态status为错误！status="+status);
						return;
					}
					jsonObj.put("refund_amount", refund_money);//单票退票金额
					jsArray.add(jsonObj);
					break;
				}
			}
		}
		//判断整体的退票状态
		logger.info("order_id="+order_id+"rsList.size()"+rsList.size()+"agreeNum="+agreeNum+"refuseNum="+refuseNum);
		if(agreeNum == 0 && refuseNum == 0){
			logger.error("退票数为0");
			paramMap.put("status", "REFUNDING");
			return;
		}else if(agreeNum == cpList.size() && refuseNum == 0){
			all_status = "SUCCESS";
			paramMap.put("status", "REFUND_FINISH");
		}else if(agreeNum == 0 && refuseNum == cpList.size()){
			all_status = "FAILURE";
			paramMap.put("status", "REFUSE_REFUND");
		}else if(agreeNum > 0){
			all_status = "SUCCESS";
			paramMap.put("status", "PART_REFUND");
		}else if(refuseNum > 0 && agreeNum ==0){
			all_status = "FAILURE";
			paramMap.put("status", "REFUSE_REFUND");
		}else{
			logger.info("退款状态异常");
		}
		
		OrderInfo order = orderService.queryOrderInfo(order_id);
		Map<String, String> merchantSetting = commonService.queryMerchantInfo(order.getMerchant_id());
		if(TrainConsts.ORDER_PAY_TYPE_11.equals(merchantSetting.get("pay_type"))){
			if(!"FAILURE".equals(all_status) && agreeNum >0){
				if(!StringUtil.isEmpty(order.getEop_refund_url())){
					Map<String,String> param = new HashMap<String,String>();
					param.put("refund_money", String.valueOf(total_money));
					param.put("order_id", order_id);
					param.put("eop_order_id", order.getEop_order_id());
					param.put("refund_seq",refund_seq);
					param.put("refund_reason", "申请退款");
					param.put("notify_status", "11");
					param.put("eop_refund_url", order.getEop_refund_url());
					if(receiveService.queryEopRefundNotifyAlterNum(refund_seq)==0){
						receiveService.addEopRefundNotify(param);//发生过退款不添加
						logger.info("添加eop退款请求。order_id:"+order_id+"refund_seq:"+refund_seq);
					}else{
						logger.info("发生过退款不添加eop退款请求。order_id:"+order_id+"refund_seq:"+refund_seq);
					}
				}else{
					logger.info("支付异常订单不添加eop退款请求。order_id:"+order_id+"refund_seq:"+refund_seq);
				}
			}else{
				logger.info("拒绝订单不添加eop退款请求。order_id:"+order_id+"refund_seq:"+refund_seq);
			}
		}else{
			logger.info("非平台代扣商户。不添加eop退款请求。order_id:"+order_id+"refund_seq:"+refund_seq);
		}
		
//		JSONObject json_param = new JSONObject();
//		json_param.put("request_id", rsList.get(0).get("merchant_refund_seq"));
//		json_param.put("trip_no", refund_seq);
//		json_param.put("merchant_order_id", rsList.get(0).get("merchant_order_id"));
//		json_param.put("status", all_status);
//		json_param.put("refund_total_amount", String.valueOf(total_money));//退票总金额
//		json_param.put("refund_type", rsList.get(0).get("refund_type"));
//		json_param.put("order_id", order_id);
//		json_param.put("order_userinfo", jsArray);

		//协议参数
		String merchant_id = rsList.get(0).get("channel");//合作商户编号
		String timestamp = DateUtil.dateToString(new Date() ,DateUtil.DATE_FMT2);
		String version = "1.0.0";
		String spare_pro1 = "";
		String spare_pro2 = "";
		
		String url = notifyMap.get("notify_url");
		//xcjson
		JSONObject xc_json = new JSONObject();
		xc_json.put("merchant_id", merchant_id);
		xc_json.put("timestamp", timestamp);
		xc_json.put("version", version);
		xc_json.put("spare_pro1", spare_pro1);
		xc_json.put("spare_pro2", spare_pro2);
		xc_json.put("request_id", rsList.get(0).get("merchant_refund_seq"));
		xc_json.put("trip_no", refund_seq);
		xc_json.put("merchant_order_id", rsList.get(0).get("merchant_order_id"));
		xc_json.put("status", all_status);
		xc_json.put("refund_total_amount", String.valueOf(total_money));//退票总金额
		xc_json.put("refund_type", rsList.get(0).get("refund_type"));
		xc_json.put("order_id", order_id);
		xc_json.put("order_userinfo", jsArray);
		//验签
		String source_str = xc_json.toString();
		logger.info("验签串source_str："+source_str);
		String str = merchant_id +timestamp+version +rsList.get(0).get("merchant_order_id");
		String hmac = Md5Encrypt.getKeyedDigestFor19Pay(str+ merchantSetting.get("sign_key"), "", "utf-8");
		xc_json.put("hmac", hmac);
		logger.info("加密串hmac="+hmac);
		
		//更新退款状态
		refundService.updateOrderRefundStatus(paramMap);
		logger.info("退票结果请求："+url+"?"+xc_json.toString());
		//发送通知
		String res = "";
//		if(url.contains("https")){
//			res = HttpsUtil.sendHttps(url+"?"+xc_json.toString());
//		}else{
			res = HttpPostJsonUtil.sendJsonPost(url, xc_json.toString(), "utf-8");
//		}
		ExternalLogsVo logs=new ExternalLogsVo();
		logs.setOrder_id(order_id);
		logs.setOpter("xc_app");
		logger.info("通知携程【退票处理通知】返回结果："+res);
		if(!StringUtils.isEmpty(res) && ("SUCCESS".equals(res.toUpperCase().trim())|| res.contains("SUCCESS"))){
			
			//如果refund_type为1，则重新回调一次
			if("1".equals(rsList.get(0).get("refund_type"))){
			xc_json.put("refund_type", "2");
			String secondUrl = "http://m.ctrip.com/restapi/soa2/11009/PartnerKuYouRefundTicketResult.json";
			String secondRes = "";
			secondRes = HttpPostJsonUtil.sendJsonPost(secondUrl, xc_json.toString(), "utf-8");
			logger.info("通知携程【退票处理通知】二次回调返回结果："+secondRes);

				if (!StringUtils.isEmpty(secondRes)
						&& ("SUCCESS".equals(secondRes.toUpperCase().trim()) || secondRes.contains("SUCCESS"))) {
					//更新通知完成
					Map<String, String> map = new HashMap<String, String>();
					map.put("notify_id", notify_id);
					map.put("order_id", order_id);
					map.put("notify_status", TrainConsts.REFUND_NOTIFY_FINISH);
					refundService.updateRefundNotfiyFinish(map);
					logger.info("退票结果通知商户成功" + "订单号：" + order_id+ "车票号：" + rsList.get(0).get("cp_id")
							+ "，退款流水号：" + refund_seq);
					logs.setOrder_optlog("退票结果通知商户成功"+ "车票号：" + rsList.get(0).get("cp_id"));
					if(TrainConsts.ORDER_PAY_TYPE_22.equals(merchantSetting.get("pay_type"))){//查询如果是商户自行扣款则 同意退款的通知成功则 改为退款完成
						Map<String, String> updateMap = new HashMap<String, String>();
						updateMap.put("order_id", order_id);
						updateMap.put("refund_seq", refund_seq);
						try{
							refundService.updateRefundStreamTo33(updateMap);
							logs.setOrder_optlog("退票结果通知商户成功（属于商户自行扣费直接改为退款完成）"+ "车票号：" + rsList.get(0).get("cp_id"));
						}catch (Exception e) {
							logger.error("属于商户自行扣费直接改为退款完成【失败】"+e);
							}
						}

				}else{
					logger.info("退票结果通知商户失败" + "订单号：" + order_id
							+ "，退款流水号：" + refund_seq+"secondRes:"+secondRes);
					logs.setOrder_optlog("退票结果通知商户失败。"+ "车票号：" + rsList.get(0).get("cp_id"));
				}
			}else{
			
			//更新通知完成
			Map<String, String> map = new HashMap<String, String>();
			map.put("notify_id", notify_id);
			map.put("order_id", order_id);
			map.put("notify_status", TrainConsts.REFUND_NOTIFY_FINISH);
			refundService.updateRefundNotfiyFinish(map);
			logger.info("退票结果通知商户成功" + "订单号：" + order_id+ "车票号：" + rsList.get(0).get("cp_id")
					+ "，退款流水号：" + refund_seq);
			logs.setOrder_optlog("退票结果通知商户成功"+ "车票号：" + rsList.get(0).get("cp_id"));
			if(TrainConsts.ORDER_PAY_TYPE_22.equals(merchantSetting.get("pay_type"))){//查询如果是商户自行扣款则 同意退款的通知成功则 改为退款完成
				Map<String, String> updateMap = new HashMap<String, String>();
				updateMap.put("order_id", order_id);
				updateMap.put("refund_seq", refund_seq);
				try{
					refundService.updateRefundStreamTo33(updateMap);
					logs.setOrder_optlog("退票结果通知商户成功（属于商户自行扣费直接改为退款完成）"+ "车票号：" + rsList.get(0).get("cp_id"));
				}catch (Exception e) {
					logger.error("属于商户自行扣费直接改为退款完成【失败】"+e);
					}
				}
			}
			
		}else{
			logger.info("退票结果通知商户失败" + "订单号：" + order_id
					+ "，退款流水号：" + refund_seq+"res:"+res);
			logs.setOrder_optlog("退票结果通知商户失败。"+ "车票号：" + rsList.get(0).get("cp_id"));
		}
		orderService.insertOrderLogs(logs);
	}

}
