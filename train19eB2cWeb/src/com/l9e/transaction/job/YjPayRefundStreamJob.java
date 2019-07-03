package com.l9e.transaction.job;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.util.DESEncrypt;
import com.l9e.util.DateUtil;
import com.l9e.util.EmappSignUtil;
import com.l9e.util.HttpPostUtil;
import com.l9e.util.KeyedDigestMD5;
import com.l9e.util.PayUtil;

/**
 * 19pay扫描退款流水发起退款请求job
 * @author yangchao
 *
 */
@Component("YjPayRefundStreamJob")
public class YjPayRefundStreamJob {
	
	private static final Logger logger = Logger.getLogger(YjPayRefundStreamJob.class);
	
	@Resource
	private OrderService orderService;
	
	
	@Value("#{propertiesReader[appInitKey]}")
	private String appInitKey;//静态验签key
	
	@Value("#{propertiesReader[merchantId]}")
	private String merchantId;//合作商户ID
	
	@Value("#{propertiesReader[plat_refund_url]}")
	private String plat_refund_url;//19pay发起退款地址
	
	@Value("#{propertiesReader[refundnotifyurl]}")
	private String refundnotifyurl;//19pay发起退款通知地址
	
	public void refund(){
		List<Map<String, String>> refundList = orderService.queryTimedRefundStreamList();
		if(refundList!=null && refundList.size()>0){
			for (Map<String, String> refundMap : refundList) {
				this.sendRefundStreamRequest(refundMap);
			}
		}
	}
	
	private void sendRefundStreamRequest(Map<String, String> refundMap){
		String characterSet = "UTF-8";//编码
		logger.info("Job退款！");
		String oriPayOrderId = refundMap.get("order_id");
		OrderInfo orderInfo = orderService.queryOrderInfo(oriPayOrderId);
		//得到退款接口url
		//TODO
		String refund_url = plat_refund_url;
		String refund_seq = refundMap.get("refund_seq");//退款请求流水号
		if(StringUtils.isEmpty(refund_seq)){
			logger.info("【退款流水接口】退款流水号为空，订单号：" + refundMap.get("order_id"));
			return;
		}
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("stream_id", refundMap.get("stream_id"));
		paramMap.put("order_id", refundMap.get("order_id"));
		paramMap.put("refund_seq", refund_seq);
		paramMap.put("refund_status", TrainConsts.REFUND_STREAM_BEGIN_REFUND);//开始退款
		
		//int count = orderService.updateRefundStreamBegin(paramMap);//更新退款请求开始
		int count = 1;
		
		if(count == 1){
			if(TrainConsts.REFUND_TYPE_1.equals(refundMap.get("refund_type"))){
				logger.info("【退款流水接口】用户线上退款，A订单号：" + refundMap.get("order_id"));
				
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
		
		
		try {
			String version = "2.00";//商户退款请求版本号
			String merchant_id = merchantId;//商户代码
			String mxResq = refundMap.get("refund_seq");//商家退款流水
			//退款金额，从三位小数变成两位小数
			if(refundMap.get("refund_money")=="" || refundMap.get("refund_money")==null || Double.parseDouble(refundMap.get("refund_money"))==0.0){
				logger.info("【退款流水接口】退款金额不正确！"+" 订单号："+oriPayOrderId);
				return;
			}
			String amount=refundMap.get("refund_money");
			amount=amount+"00";//退款金额
			String notifyUrl = refundnotifyurl;//退款请求后台通知地址
			String pay_date = orderInfo.getPay_time();//2014-12-26 09:06:00
			String oriPayDate = DateUtil.dateToString(DateUtil.stringToDate(pay_date, DateUtil.DATE_FMT3), DateUtil.DATE_FMT2);//原支付订单时间，格式：20090505050505
			String tradeDesc = "trainRefund";//"train_refund";//退款业务描述:对于退款交易的描述
			//String remark = refundMap.get("our_remark");//商户备注信息:商户的备注信息,50汉字以内
			String remark = "trainRefund";
			String reserved = "";//商户退款业务拓展保留字段:业务扩展字段
			String verifyString = "";//验证摘要串
			String tradeType = "E_BANK";//退款交易类型:QUICKPAY：快捷支付退款 E_BANK：页面网银支付 D_BANK：直连网银支付
			String currencyType = "RMB";//货币类型:RMB
			
			String req_param="";
			String[][] resParam=new String[12][2];
			resParam[0][0]="version";
			resParam[0][1]=version;
			resParam[1][0]="merchantId";
			resParam[1][1]=merchant_id;
			resParam[2][0]="mxResq";
			resParam[2][1]=mxResq;
			resParam[3][0]="oriPayOrderId";
			resParam[3][1]=oriPayOrderId;
			resParam[4][0]="amount";
			resParam[4][1]=amount;
			resParam[5][0]="notifyUrl";
			resParam[5][1]=notifyUrl;
			resParam[6][0]="oriPayDate";
			resParam[6][1]=oriPayDate;
			resParam[7][0]="tradeDesc";
			resParam[7][1]=tradeDesc;
			resParam[8][0]="remark";
			resParam[8][1]=remark;
			resParam[9][0]="reserved";
			resParam[9][1]=reserved;
			resParam[10][0]="tradeType";
			resParam[10][1]=tradeType;
			resParam[11][0]="currencyType";
			resParam[11][1]=currencyType;
			req_param=EmappSignUtil.creatResParam(resParam);
			System.out.println("发起退款请求的参数是："+req_param);
			//先des加密
			//String desStr = new String(DESEncrypt.encrypt(req_param.getBytes(), appInitKey));
			//System.out.println("发起退款请求DES加密结果是："+desStr);
			//按后在按照md5加密
			verifyString = KeyedDigestMD5.getKeyedDigest(req_param.toString(), appInitKey);
			System.out.println("发起退款请求MD5加密结果是："+verifyString);
			
			StringBuffer sbPara=new StringBuffer();
			sbPara.append("version=").append(URLEncoder.encode(version, characterSet))
				  .append("&merchantId=").append(URLEncoder.encode(merchant_id, characterSet))
				  .append("&mxResq=").append(URLEncoder.encode(mxResq, characterSet))
				  .append("&oriPayOrderId=").append(URLEncoder.encode(oriPayOrderId, characterSet))
				  .append("&amount=").append(URLEncoder.encode(amount, characterSet))
				  .append("&notifyUrl=").append(notifyUrl)
				  .append("&oriPayDate=").append(URLEncoder.encode(oriPayDate, characterSet))
				  .append("&tradeDesc=").append(URLEncoder.encode(tradeDesc, characterSet))
				  .append("&remark=").append(URLEncoder.encode(remark, characterSet))
				  .append("&reserved=").append(URLEncoder.encode(reserved, characterSet))
				  .append("&tradeType=").append(URLEncoder.encode(tradeType, characterSet))
				  .append("&currencyType=").append(URLEncoder.encode(currencyType, characterSet));
			
			//发起退款http请求,得到响应
			logger.info("【退款流水接口】订单号："+oriPayOrderId+" 请求地址："+refund_url+"  请求参数："+sbPara.toString().trim()+"  签名："+verifyString);
			String res="";
			try {
				res=HttpPostUtil.sendAndRecive(refund_url, sbPara.toString().trim()+"&verifyString="+verifyString);
			} catch (Exception e) {
				logger.error("【退款流水接口】向19pay发起退款请求异常！订单号："+oriPayOrderId  +" "+e);
			}
			logger.info("【退款流水接口】请求19pay退款返回的数据为："+res);
			
			
			
			//res=“version=null&mxresq=null&status=FAIL&amount=null&retCode=10001&verifyString=null&oriPayOrderId=null&fee=null”
//			version=null
//			mxresq=null
//			amount=null
//			oriPayOrderId=null
//			fee=null
			//获取返回码
			String retCode=PayUtil.getValue(res, "retCode");
			String status=PayUtil.getValue(res, "status");//SUCCESS、FAIL
			Map<String,String>	resMap=new HashMap<String,String>();
			//把参数串转换成map
			resMap=PayUtil.getValueMap(res);
			//收单结果 0:收单成功   其他:收单异常
			status=resMap.get("status");
			String sign=resMap.get("verifyString");
			//去除掉map里面的签名值
			resMap.remove("verifyString");
			
			String refundsno=resMap.get("refundsno");//19pay订单号退款流水
			//验证签名
			boolean flag = false;
			String now_time = DateUtil.dateToString(new Date(), DateUtil.DATE_FMT3);
			//添加日志
			Map<String,String> logMap = new HashMap<String,String>();
			logMap.put("order_id", oriPayOrderId);
			logMap.put("opter", "web");
			logMap.put("order_time", now_time);
			//flag=EmappSignUtil.checkReqSign(resMap, sign, appInitKey);
			flag = verifyString.equals(sign);
			if(flag){
				logger.info("【退款流水接口】退款返回参数验签成功！订单号："+oriPayOrderId);
				if(StringUtils.isEmpty(status)){
					logger.info("【退款流水接口】接口返回收单结果为空，订单号："+ oriPayOrderId);
					logMap.put("order_optlog", "【退款流水接口】申请19pay退款返回状态码为空失败");
				}else if("SUCCESS".equals(status)){
					logger.info("【退款流水接口】 收单成功！订单号："+oriPayOrderId);
					/*
					 * 19pay收单成功，更新 19pay订单号退款流水和退款状态
					 */
					Map<String,String> statusMap=new HashMap<String,String>();
					statusMap.put("order_id", oriPayOrderId);
					statusMap.put("refund_seq", refund_seq);
					statusMap.put("plat_refund_seq", refundsno);
//					statusMap.put("refund_status", TrainConsts.REFUND_STREAM_EOP_REFUNDING);
					try {
						orderService.updateRefundSStatus(statusMap);
						logger.info("【退款流水接口】19pay收单成功，退款状态更新成功，订单号："+ oriPayOrderId);
					} catch (Exception e) {
						logger.info("【退款流水接口】19pay收单成功，退款状态更新失败，订单号："+ oriPayOrderId);
					}
					logMap.put("order_optlog", "【退款流水接口】申请19pay退款成功");
				}else{
					if(StringUtils.isEmpty(retCode)){
						logger.info("【退款流水接口】订单号："+oriPayOrderId+" 返回码为空！");
					}else if("15001".equals(retCode)){
						logger.info("【退款流水接口】接口版本号错误"+" 订单号："+oriPayOrderId);
						logMap.put("order_optlog", "【退款流水接口】申请19pay退款失败，接口版本号错误");
					}else if("15002".equals(retCode)){
						logger.info("【退款流水接口】命令字错误"+" 订单号："+oriPayOrderId);
						logMap.put("order_optlog", "【退款流水接口】申请19pay退款失败，命令字错误");
					}else if("15005".equals(retCode)){
						logger.info("【退款流水接口】日期格式错误（请求日期）"+" 订单号："+oriPayOrderId);
						logMap.put("order_optlog", "【退款流水接口】申请19pay退款失败，日期格式错误（请求日期）");
					}else if("15031".equals(retCode)){
						logger.info("【退款流水接口】加密验证失败"+" 订单号："+oriPayOrderId);
						logMap.put("order_optlog", "【退款流水接口】申请19pay退款失败，加密验证失败");
					}else if("15040".equals(retCode)){
						logger.info("【退款流水接口】请求平台无权限调用"+" 订单号："+oriPayOrderId);
						logMap.put("order_optlog", "【退款流水接口】申请19pay退款失败，请求平台无权限调用");
					}else if("15105".equals(retCode)){
						logger.info("【退款流水接口】订单不存在"+" 订单号："+oriPayOrderId);
					}else if("15104".equals(retCode)){
						logger.info("【退款流水接口】订单已存在"+" 订单号："+oriPayOrderId);
					}else if("15204".equals(retCode)){
						logger.info("【退款流水接口】回退金额不正确"+" 订单号："+oriPayOrderId);
					}else if("10001".equals(retCode)){
						logger.info("【退款流水接口】发起方用户不存在"+" 订单号："+oriPayOrderId);
					}else if("10017".equals(retCode)){
						logger.info("【退款流水接口】发起方账户余额不足"+" 订单号："+oriPayOrderId);
					}else if("10071".equals(retCode)){
						logger.info("【退款流水接口】转账类型错误"+" 订单号："+oriPayOrderId);
					}else if("15009".equals(retCode)){
						logger.info("【退款流水接口】商户订单金额为空或格式不正确"+" 订单号："+oriPayOrderId);
					}else if("25001".equals(retCode)){
						logger.info("【退款流水接口】订单回退中"+" 订单号："+oriPayOrderId);
					}else if("11111".equals(retCode)){
						logger.info("【退款流水接口】对不起 ,系统忙"+" 订单号："+oriPayOrderId);
					}else if("28108".equals(retCode)){
						logger.info("【退款流水接口】退款金额超出 "+" 订单号："+oriPayOrderId);
					}else if("0".equals(retCode)){
						logger.info("【退款流水接口】成功"+" 订单号："+oriPayOrderId);
					}else{
						logger.info("【退款流水接口】接口返回收单结果出现其他异常,订单号："+oriPayOrderId);
					}
					logMap.put("order_optlog", "【退款流水接口】申请19pay退款失败,错误代码"+retCode);
				}
				orderService.addOrderOptLog(logMap);
			}else{
				logger.error("【退款流水接口】订单号："+oriPayOrderId+"--退款请求反馈信息摘要验签失败！");
			}
		} catch (Exception e) {
			logger.error("【退款流水接口】发起退款请求Exception，订单号："+oriPayOrderId, e);
		}

	}

}
