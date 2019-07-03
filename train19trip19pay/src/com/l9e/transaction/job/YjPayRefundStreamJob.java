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
import com.l9e.util.DateUtil;
import com.l9e.util.EmappSignService;
import com.l9e.util.HttpPostUtil;
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
	
	@Value("#{propertiesReader[characterSet]}")
	private String characterSet;//编码
	
	
	public void refund(){
		List<Map<String, String>> refundList = orderService.queryTimedRefundStreamList();
		if(refundList!=null && refundList.size()>0){
			for (Map<String, String> refundMap : refundList) {
				this.sendRefundStreamRequest(refundMap);
			}
		}
	}
	
	private void sendRefundStreamRequest(Map<String, String> refundMap){
		logger.info("退款！");
		String mxorderid = refundMap.get("order_id");
		OrderInfo orderInfo = orderService.queryOrderInfo(mxorderid);
		//得到退款接口url
		String refund_url=orderInfo.getRefund_url();
		String [] url=refund_url.split("\\?");
		String [] urlPara=url[1].split("\\=");
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
		
		
		try {
			//商家退款流水
			String mxrefundsno=refundMap.get("refund_seq");
			//退款金额，从三位小数变成两位小数
			if(refundMap.get("refund_money")=="" || refundMap.get("refund_money")==null || Double.parseDouble(refundMap.get("refund_money"))==0.0){
				logger.info("【退款流水接口】退款金额不正确！"+" 订单号："+mxorderid);
				return;
			}
			String refundamount=refundMap.get("refund_money");
			refundamount=refundamount.substring(0, refundamount.length()-1);
			
			//商家时间戳
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String mxdate = sdf.format(new Date());
			//扩展字段1
			String extend1="";
			//扩展字段2 extend2
			String extend2="";
			//签名 
			String hmac="";
			
			String req_param="";
			String[][] resParam=new String[6][2];
			resParam[0][0]="mxorderid";
			resParam[0][1]=mxorderid;
			resParam[1][0]="mxdate";
			resParam[1][1]=mxdate;
			resParam[2][0]="mxrefundsno";
			resParam[2][1]=mxrefundsno;
			resParam[3][0]="extend1";
			resParam[3][1]=extend1;
			resParam[4][0]="extend2";
			resParam[4][1]=extend2;
			resParam[5][0]="refundamount";
			resParam[5][1]=refundamount;
			req_param=EmappSignService.creatResSign(resParam, appInitKey);
			
			StringBuffer sbPara=new StringBuffer();
			sbPara.append("extend1=").append(URLEncoder.encode(extend1, characterSet))
				  .append("&extend2=").append(URLEncoder.encode(extend2, characterSet))
				  .append("&mxdate=").append(URLEncoder.encode(mxdate, characterSet))
				  .append("&mxorderid=").append(URLEncoder.encode(mxorderid, characterSet))
				  .append("&mxrefundsno=").append(URLEncoder.encode(mxrefundsno, characterSet))
				  .append("&refundamount=").append(URLEncoder.encode(refundamount, characterSet))
				  .append("&"+urlPara[0]+"=").append(URLEncoder.encode(urlPara[1], characterSet));
			
			hmac=PayUtil.getValue(req_param, "hmac");
			
			//发起退款http请求,得到响应
			logger.info("【退款流水接口】订单号："+mxorderid+" 请求地址："+url[0]+"  请求参数："+sbPara.toString().trim()+"  签名："+hmac);
			String res="";
			try {
				res=HttpPostUtil.sendAndRecive(url[0], sbPara.toString().trim()+"&hmac="+hmac);
			} catch (Exception e) {
				logger.error("【退款流水接口】向19pay发起退款请求异常！订单号："+mxorderid  +" "+e);
			}
			logger.info("【退款流水接口】请求19pay退款返回的数据为："+res);
			//获取返回码
			String errcode=PayUtil.getValue(res, "errcode");
			String status=PayUtil.getValue(res, "status");
			Map<String,String>	resMap=new HashMap<String,String>();
			//把参数串转换成map
			resMap=PayUtil.getValueMap(res);
			//收单结果 0:收单成功   其他:收单异常
			status=resMap.get("status");
			String sign=resMap.get("hmac");
			//去除掉map里面的签名值
			resMap.remove("hmac");
			
			String refundsno=resMap.get("refundsno");//19pay订单号退款流水
			//验证签名
			boolean flag = false;
			String now_time = DateUtil.dateToString(new Date(), DateUtil.DATE_FMT3);
			//添加日志
			Map<String,String> logMap = new HashMap<String,String>();
			logMap.put("order_id", mxorderid);
			logMap.put("opter", "ccb");
			logMap.put("order_time", now_time);
			flag=EmappSignService.checkReqSign(resMap, sign, appInitKey);
			if(flag){
				logger.info("【退款流水接口】退款返回参数验签成功！订单号："+mxorderid);
				if(StringUtils.isEmpty(status)){
					logger.info("【退款流水接口】接口返回收单结果为空，订单号："+ mxorderid);
					logMap.put("order_optlog", "【退款流水接口】申请19pay退款返回状态码为空失败");
				}else if("0".equals(status)){
					logger.info("【退款流水接口】 收单成功！订单号："+mxorderid);
					/*
					 * 19pay收单成功，更新 19pay订单号退款流水和退款状态
					 */
					Map<String,String> statusMap=new HashMap<String,String>();
					statusMap.put("order_id", mxorderid);
					statusMap.put("refund_seq", refund_seq);
					statusMap.put("plat_refund_seq", refundsno);
					statusMap.put("refund_status", TrainConsts.REFUND_STREAM_EOP_REFUNDING);
					try {
						orderService.updateRefundSStatus(statusMap);
						logger.info("【退款流水接口】19pay收单成功，退款状态更新成功，订单号："+ mxorderid);
					} catch (Exception e) {
						logger.info("【退款流水接口】19pay收单成功，退款状态更新失败，订单号："+ mxorderid);
					}
					logMap.put("order_optlog", "【退款流水接口】申请19pay退款成功");
				}else{
					if(StringUtils.isEmpty(errcode)){
						logger.info("【退款流水接口】订单号："+mxorderid+" 返回码为空！");
					}else if("15001".equals(errcode)){
						logger.info("【退款流水接口】接口版本号错误"+" 订单号："+mxorderid);
						logMap.put("order_optlog", "【退款流水接口】申请19pay退款失败，接口版本号错误");
					}else if("15002".equals(errcode)){
						logger.info("【退款流水接口】命令字错误"+" 订单号："+mxorderid);
						logMap.put("order_optlog", "【退款流水接口】申请19pay退款失败，命令字错误");
					}else if("15005".equals(errcode)){
						logger.info("【退款流水接口】日期格式错误（请求日期）"+" 订单号："+mxorderid);
						logMap.put("order_optlog", "【退款流水接口】申请19pay退款失败，日期格式错误（请求日期）");
					}else if("15031".equals(errcode)){
						logger.info("【退款流水接口】加密验证失败"+" 订单号："+mxorderid);
						logMap.put("order_optlog", "【退款流水接口】申请19pay退款失败，加密验证失败");
					}else if("15040".equals(errcode)){
						logger.info("【退款流水接口】请求平台无权限调用"+" 订单号："+mxorderid);
						logMap.put("order_optlog", "【退款流水接口】申请19pay退款失败，请求平台无权限调用");
					}else if("15105".equals(errcode)){
						logger.info("【退款流水接口】订单不存在"+" 订单号："+mxorderid);
					}else if("15104".equals(errcode)){
						logger.info("【退款流水接口】订单已存在"+" 订单号："+mxorderid);
					}else if("15204".equals(errcode)){
						logger.info("【退款流水接口】回退金额不正确"+" 订单号："+mxorderid);
					}else if("10001".equals(errcode)){
						logger.info("【退款流水接口】发起方用户不存在"+" 订单号："+mxorderid);
					}else if("10017".equals(errcode)){
						logger.info("【退款流水接口】发起方账户余额不足"+" 订单号："+mxorderid);
					}else if("10071".equals(errcode)){
						logger.info("【退款流水接口】转账类型错误"+" 订单号："+mxorderid);
					}else if("15009".equals(errcode)){
						logger.info("【退款流水接口】商户订单金额为空或格式不正确"+" 订单号："+mxorderid);
					}else if("25001".equals(errcode)){
						logger.info("【退款流水接口】订单回退中"+" 订单号："+mxorderid);
					}else if("11111".equals(errcode)){
						logger.info("【退款流水接口】对不起 ,系统忙"+" 订单号："+mxorderid);
					}else if("28108".equals(errcode)){
						logger.info("【退款流水接口】退款金额超出 "+" 订单号："+mxorderid);
					}else if("0".equals(errcode)){
						logger.info("【退款流水接口】成功"+" 订单号："+mxorderid);
					}else{
						logger.info("【退款流水接口】接口返回收单结果出现其他异常,订单号："+mxorderid);
					}
					logMap.put("order_optlog", "【退款流水接口】申请19pay退款失败,错误代码"+errcode);
				}
				orderService.addOrderinfoHistory(logMap);
			}else{
				logger.error("【退款流水接口】订单号："+mxorderid+"--退款请求反馈信息摘要验签失败！");
			}
		} catch (Exception e) {
			logger.error("【退款流水接口】发起退款请求Exception，订单号："+mxorderid, e);
		}

	}

}
