package com.l9e.transaction.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.vo.InterAccount;
import com.l9e.transaction.vo.QunarResult;
import com.l9e.transaction.vo.SysConfig;
import com.l9e.util.HttpUtil;
import com.l9e.util.UrlFormatUtil;

/**
 * 退票结果通知job
 * @author zhangjun
 *
 */
@Component("refundNotifyJob")
public class RefundNotifyJob {
	
	private static final Logger logger = Logger.getLogger(RefundNotifyJob.class);
	
	@Resource
	private OrderService orderService;
	
	@Value("#{propertiesReader[qunarReqUrl]}")
	private String qunarReqUrl;//Qunar请求地址
	
	public void refundNotify() throws Exception {
		
		List<Map<String, String>> refundList = orderService.queryTimedWaitingRefundList();//定时扫描待退票结果通知的订单
		for(Map<String, String> refundMap : refundList){
			orderService.updateQunarRefundNotifyBegin(refundMap);
			this.sendNotifyRequest(refundMap);
		}
	}
	
	private void sendNotifyRequest(Map<String, String> refundMap) throws Exception {
		String order_id = refundMap.get("order_id");
		
		//获取qunar订单来源账号
		String order_source = null;
		if(StringUtils.isEmpty(refundMap.get("order_source"))){
			order_source = "qunar1";
		}else{
			order_source = refundMap.get("order_source");
		}
		InterAccount account = SysConfig.getAccountByName(order_source);
		if(account == null){
			logger.error("fatal error InterAccount is null!");
			return;
		}
		String md5Key = account.getMd5Key();
		String merchantCode = account.getMerchantCode();
		String logPre = "【退票结果<"+order_source+">】";
		
		String opt = null;//协议
		String reason = null;//退票原因
		String comment = null;//备注
		if(StringUtils.isEmpty(refundMap.get("order_id")) 
				|| StringUtils.isEmpty(refundMap.get("refund_status"))){
			logger.info(logPre+"order_id="+order_id+"，参数为空！");
			return;
		}else if(TrainConsts.REFUND_AGREE.equals(refundMap.get("refund_status"))){
			opt = "AGREE";
			reason = "您的车票已经完成退票。";
			comment = "您的车票已经完成退票。";
		}else if(TrainConsts.REFUND_REFUSE.equals(refundMap.get("refund_status"))){
			opt = "REFUSE";
			if(TrainConsts.REFUSE_REASON_1.equals(refundMap.get("refuse_reason"))){//已取票
				reason = refundMap.get("refuse_reason");
				comment = "已取票";
			}else if(TrainConsts.REFUSE_REASON_2.equals(refundMap.get("refuse_reason"))){//已过时间
				reason = refundMap.get("refuse_reason");
				comment = "已过时间";
			}else if(TrainConsts.REFUSE_REASON_3.equals(refundMap.get("refuse_reason"))){//来电取消
				reason = refundMap.get("refuse_reason");
				comment = "来电取消";
			}else if(TrainConsts.REFUSE_REASON_6.equals(refundMap.get("refuse_reason"))){//针对急速退票
				reason = refundMap.get("refuse_reason");
				comment = "退款金额有损失";
			}else{
				reason = refundMap.get("our_remark");
			}
		}else{
			logger.info("[退票结果通知]order_id="+order_id+"，refund_status="+refundMap.get("refund_status")+"，退票状态异常！");
			return;
		}
		
		Map<String, String> map = new HashMap<String,String>();		
		map.put("merchantCode", merchantCode);
		map.put("orderNo", order_id);
//		if("AGREE".equals(opt)){
//			map.put("refundCash", refundMap.get("refund_money"));
//		}
		map.put("opt", opt);
		map.put("comment", comment);
		map.put("reason", reason);
		String hMac = DigestUtils.md5Hex(md5Key+merchantCode + order_id + opt+ comment + reason).toUpperCase();
		map.put("HMAC", hMac);
		System.out.println("hMac="+hMac);
		String reqParams = UrlFormatUtil.CreateUrl("", map, "", "UTF-8");
		logger.info("通知去哪退票结果参数reqParams="+reqParams);
		
		StringBuffer reqUrl = new StringBuffer();
		reqUrl.append(qunarReqUrl).append("ProcessRefund.do");
		
		String jsonRs = HttpUtil.sendByPost(reqUrl.toString(), reqParams, "UTF-8");
		logger.info(order_id+"通知去哪退票结果反馈："+jsonRs);
		ObjectMapper mapper = new ObjectMapper();
		QunarResult rs = mapper.readValue(jsonRs, QunarResult.class);
		if(rs.isRet()){
			logger.info(logPre+"通知qunar成功，order_id="+order_id);
			orderService.updateQunarRefundNotifyEnd(refundMap);
		}else{
			if("008".equals(rs.getErrCode())){
				int count = orderService.queryRefundCountByRefuse(order_id);
				if(count>0){
					logger.info("重复拒绝退款通知成功，order_id="+order_id);
					orderService.updateQunarRefundNotifyEnd(refundMap);
				}else{
					if(refundMap.get("notify_num").equals("9")){
						logger.info(logPre+"通知qunar失败，order_id="+order_id+"，10次通知完毕【未成功】");
						/**更新为通知失败态*/
						orderService.updateQunarRefundStatus(order_id);
						//添加日志
						Map<String, String> logMap = new HashMap<String, String>();
						logMap.put("order_id",order_id);
						logMap.put("content", "退票结果通知qunar失败，errCode="+rs.getErrCode()+"&errMsg="+rs.getErrMsg()+"，10次通知完毕");
						logMap.put("opt_person", "qunar_app");
						orderService.addOrderInfoLog(logMap);
					}else{
						logger.info(logPre+"通知qunar失败，order_id="+order_id+"，系统将在约1分钟后重新通知");
						//添加日志
						Map<String, String> logMap = new HashMap<String, String>();
						logMap.put("order_id",order_id);
						logMap.put("content", "退票结果通知qunar失败，errCode="+rs.getErrCode()+"&errMsg="+rs.getErrMsg());
						logMap.put("opt_person", "qunar_app");
						orderService.addOrderInfoLog(logMap);
					}
				}
			}else if("300".equals(rs.getErrCode())){
				if(refundMap.get("notify_num").equals("9")){
					map = new HashMap<String,String>();		
					map.put("merchantCode", merchantCode);
					map.put("orderNo", order_id);
					map.put("refundCash", refundMap.get("refund_money"));
					map.put("opt", opt);
					map.put("comment", comment);
					map.put("reason", reason);
					hMac = DigestUtils.md5Hex(md5Key+merchantCode + order_id + refundMap.get("refund_money") + opt+ comment + reason).toUpperCase();
					map.put("HMAC", hMac);
					System.out.println("hMac="+hMac);
					reqParams = UrlFormatUtil.CreateUrl("", map, "", "UTF-8");
					logger.info("第十次通知去哪退票结果参数reqParams="+reqParams);
					
					reqUrl = new StringBuffer();
					reqUrl.append(qunarReqUrl).append("ProcessRefund.do");
					
					jsonRs = HttpUtil.sendByPost(reqUrl.toString(), reqParams, "UTF-8");
					logger.info(order_id+"第十次通知去哪退票结果反馈："+jsonRs);
					mapper = new ObjectMapper();
					rs = mapper.readValue(jsonRs, QunarResult.class);
					if(rs.isRet()){
						logger.info(logPre+"通知qunar成功，order_id="+order_id);
						orderService.updateQunarRefundNotifyEnd(refundMap);
					}else{
						logger.info(logPre+"通知qunar失败，order_id="+order_id+"，10次通知完毕【未成功】");
						/**更新为通知失败态*/
						orderService.updateQunarRefundStatus(order_id);
						//添加日志
						Map<String, String> logMap = new HashMap<String, String>();
						logMap.put("order_id",order_id);
						logMap.put("content", "退票结果通知qunar失败，errCode="+rs.getErrCode()+"&errMsg="+rs.getErrMsg()+"，10次通知完毕");
						logMap.put("opt_person", "qunar_app");
						orderService.addOrderInfoLog(logMap);
					}
				}else{
					logger.info(logPre+"通知qunar失败，order_id="+order_id+"，系统将在约1分钟后重新通知");
					//添加日志
					Map<String, String> logMap = new HashMap<String, String>();
					logMap.put("order_id",order_id);
					logMap.put("content", "退票结果通知qunar失败，errCode="+rs.getErrCode()+"&errMsg="+rs.getErrMsg());
					logMap.put("opt_person", "qunar_app");
					orderService.addOrderInfoLog(logMap);
				}
			}else{
				if(refundMap.get("notify_num").equals("9")){
					logger.info(logPre+"通知qunar失败，order_id="+order_id+"，10次通知完毕【未成功】");
					/**更新为通知失败态*/
					orderService.updateQunarRefundStatus(order_id);
					//添加日志
					Map<String, String> logMap = new HashMap<String, String>();
					logMap.put("order_id",order_id);
					logMap.put("content", "退票结果通知qunar失败，errCode="+rs.getErrCode()+"&errMsg="+rs.getErrMsg()+"，10次通知完毕");
					logMap.put("opt_person", "qunar_app");
					orderService.addOrderInfoLog(logMap);
				}else{
					logger.info(logPre+"通知qunar失败，order_id="+order_id+"，系统将在约1分钟后重新通知");
					//添加日志
					Map<String, String> logMap = new HashMap<String, String>();
					logMap.put("order_id",order_id);
					logMap.put("content", "退票结果通知qunar失败，errCode="+rs.getErrCode()+"&errMsg="+rs.getErrMsg());
					logMap.put("opt_person", "qunar_app");
					orderService.addOrderInfoLog(logMap);
				}
			}
			
		}
	}
	
}
