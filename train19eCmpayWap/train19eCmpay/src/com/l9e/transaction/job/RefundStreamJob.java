package com.l9e.transaction.job;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hisun.iposm.HiiposmUtil;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.OrderService;

/**
 * 扫描退款流水发起退款请求job
 * @author zhangjun
 *
 */
@Component("refundStreamJob")
public class RefundStreamJob {
	
	private static final Logger logger = Logger.getLogger(RefundStreamJob.class);
	
	@Resource
	private OrderService orderService;
	
	@Value("#{propertiesReader[merchantId]}")
	private String merchantId;
	
	@Value("#{propertiesReader[signKey]}")
	private String signKey;//验签key
	
	@Value("#{propertiesReader[req_url]}")
	private String req_url;//cmpay请求地址
	
	@Value("#{propertiesReader[characterSet]}")
	private String characterSet;
	
	public void refund(){
		List<Map<String, String>> refundList = orderService.queryTimedRefundStreamList();
		for (Map<String, String> refundMap : refundList) {
			this.sendRefundStreamRequest(refundMap);
		}
	}
	
	private void sendRefundStreamRequest(Map<String, String> refundMap){
		
		String requestId = refundMap.get("refund_seq");//ASP退款请求流水号
		if(StringUtils.isEmpty(requestId)){
			logger.info("【退款流水接口】退款流水号为空，订单号：" + refundMap.get("order_id"));
			return;
		}
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("stream_id", refundMap.get("stream_id"));
		paramMap.put("order_id", refundMap.get("order_id"));
		paramMap.put("refund_seq", requestId);
		paramMap.put("refund_status", TrainConsts.REFUND_STREAM_BEGIN_REFUND);//开始退款
		
		int count = orderService.updateRefundStreamBegin(paramMap);//更新退款请求开始
		
		if(count == 1){
			if(TrainConsts.REFUND_TYPE_1.equals(refundMap.get("refund_type"))){
				logger.info("【退款流水接口】用户退款，A订单号：" + refundMap.get("order_id"));
				
			}else if(TrainConsts.REFUND_TYPE_2.equals(refundMap.get("refund_type"))){
				logger.info("【退款流水接口】差额退款，订单号：" + refundMap.get("order_id"));
				
			}else if(TrainConsts.REFUND_TYPE_3.equals(refundMap.get("refund_type"))){
				logger.info("【退款流水接口】出票失败退款，订单号：" + refundMap.get("order_id"));
				
			}else if(TrainConsts.REFUND_TYPE_4.equals(refundMap.get("refund_type"))){
				logger.info("【退款流水接口】改签差额退款，订单号：" + refundMap.get("order_id"));
				
			}else if(TrainConsts.REFUND_TYPE_5.equals(refundMap.get("refund_type"))){
				logger.info("【退款流水接口】改签单退款，订单号：" + refundMap.get("order_id"));
			}
		}else{
			logger.info("【退款流水接口】更新退款开始异常，退款请求取消！" + refundMap.get("order_id"));
			return;
		}
		
		String type = "OrderRefund";
		String signType = "MD5";
		String version = "2.0.0";
		//request.setCharacterEncoding("GBK");
		String orderId = refundMap.get("order_id");
		try {
			//String amount = "1";
			
			String amount = String.valueOf((Double.parseDouble(refundMap.get("refund_money")) * 100)).split("\\.")[0];
			
			HiiposmUtil util = new HiiposmUtil();
			
			//-- 签名
			String signData = merchantId + requestId + signType
					+ type + version + orderId + amount;
			String hmac = util.MD5Sign(signData, signKey);
			
			//-- 请求报文
			String buf = "merchantId=" + merchantId + "&requestId=" + requestId
			           + "&signType=" + signType + "&type=" + type
			           + "&version=" + version + "&orderId=" + orderId
			           + "&amount=" + amount;
        buf = "hmac=" + hmac + "&" + buf;
		
        //发起http请求，并获取响应报文
        	logger.info("【退款流水接口】参数为" + buf);
			String res = util.sendAndRecv(req_url, buf, characterSet);


			//获取返回码
			String code = util.getValue(res, "returnCode");
			logger.info("【退款流水接口】code="+code+"，orderId="+ orderId);
			
			if(StringUtils.isEmpty(code)){
				
			}else if(!"000000".equals(code)){
				logger.error("【退款流水接口】退款失败："+code+" "+URLDecoder.decode(util.getValue(res,"message"),"UTF-8"));
				
				if("IPS0008".equals(code)){
					logger.info("【退款流水接口】签名不符，一般都是有由于中文编码引起！"+"orderId="+ orderId);
				}else if("IPS0001".equals(code)){
					logger.info("【退款流水接口】一般都是因为商户编号或者密钥不对引起的！"+"orderId="+ orderId);
				}else if("C01088".equals(code)){
					logger.info("【退款流水接口】退货金额域非法！"+"orderId="+ orderId);
				}else if("C23190".equals(code)){
					logger.info("【退款流水接口】退款日期超过最大有效期！"+"orderId="+ orderId);
				}else if("C22407".equals(code)){
					logger.info("【退款流水接口】退款金额大于可退款金额！"+"orderId="+ orderId);
				}else if("C22401".equals(code)){
					logger.info("【退款流水接口】退货金额大于可退货金额！"+"orderId="+ orderId);
				}else if("D22208".equals(code)){
					logger.info("【退款流水接口】订单状态异常，详询cmpay网站！"+"orderId="+ orderId);
					
				}else if("D22201".equals(code)){
					logger.info("【退款流水接口】订单不存在！"+"orderId="+ orderId);
				}else if("D77040".equals(code)){
					logger.info("【退款流水接口】商户没有开通此类交易的权限！"+"orderId="+ orderId);
				}else if("D22208".equals(code)){
					logger.info("【退款流水接口】订单状态异常。除了订单号重复外，requestId也不能重复！"+"orderId="+ orderId);
					
				}
				return;
			}else{
				//手机支付返回报文的消息摘要，用于商户验签
				String hmac1 = util.getValue(res, "hmac");
				String vfsign = util.getValue(res, "merchantId")
				      + util.getValue(res, "payNo")
				      + util.getValue(res, "returnCode")
						+ URLDecoder.decode(util.getValue(res, "message"), "UTF-8")
						+ util.getValue(res, "signType")
						+ util.getValue(res, "type")
						+ util.getValue(res, "version")
						+ util.getValue(res, "amount")
						+ util.getValue(res, "orderId")
						+ util.getValue(res, "status");
				
				logger.info("【退款流水接口】退款返回数据 vfsign="+vfsign);
				
				// -- 验证签名
				boolean flag = false;
				flag = util.MD5Verify(vfsign, hmac1, signKey);
	
				if(!flag){
					logger.info("【退款流水接口】对退款返回数据验签失败，orderId="+ orderId);
					return;
				}else{
					String status = util.getValue(res, "status");
					String pay_amount = util.getValue(res, "amount");
					if(StringUtils.isEmpty(status)){
						logger.info("【退款流水接口】接口返回退款状态为空，orderId="+ orderId);
					}else if("SUCCESS".equals(status)){
						logger.info("【退款流水接口】退款成功，orderId="+ orderId+"，pay_amount="+pay_amount);
						
						 /*
					     * 申请成功更新退款状态信息
					     */
						Map<String, String> map = new HashMap<String, String>();
						map.put("stream_id", refundMap.get("stream_id"));
						map.put("order_id", refundMap.get("order_id"));
						map.put("refund_seq", requestId);//ASP退款请求流水号
					    map.put("eop_refund_seq", util.getValue(res, "payNo"));//EOP退款流水号
					    map.put("refund_status", TrainConsts.REFUND_STREAM_REFUND_FINISH);//退款完成
					    map.put("old_refund_status", TrainConsts.REFUND_STREAM_BEGIN_REFUND);//开始退款
					    orderService.updateOrderStreamStatus(map);
						
						logger.info("【退款流水接口】退款状态修改成功，orderId="+ orderId+"，pay_amount="+pay_amount+"，pay_no="+util.getValue(res, "payNo"));
					}else if("FAILED".equals(status)){
						logger.info("【退款流水接口】接口返回退款状态为退款失败，orderId="+ orderId);
					}else{
						logger.info("【退款流水接口】接口返回退款状态未知，orderId="+ orderId);
					}
					
				}
			}
		} catch (Exception e) {
			logger.error("【退款流水接口】发起退款请求Exception，orderId="+orderId, e);
			//调用查询接口确认订单状态
			//...
			
		}

	}

}
