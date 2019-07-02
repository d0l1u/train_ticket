package com.l9e.train.channel.request.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import com.l9e.train.po.DeviceWeight;
import com.l9e.train.po.OrderCP;
import com.l9e.train.po.ResultCP;
import com.l9e.train.po.ReturnOptlog;
import com.l9e.train.po.ReturnRefundInfo;
import com.l9e.train.po.ReturnRefundPasEntity;
import com.l9e.train.po.TrainConsts;
import com.l9e.train.po.Worker;
import com.l9e.train.service.impl.SysSettingServiceImpl;
import com.l9e.train.service.impl.TrainServiceImpl;
import com.l9e.train.util.HttpUtil;
import com.l9e.train.util.MD5;
import com.l9e.train.util.MemcachedUtil;

public class RobotRefundRequest extends RequestImpl {

	private Logger logger=Logger.getLogger(this.getClass());

	
	public RobotRefundRequest( Worker worker) {
		super(worker);
	}
	
	@Override
	public ResultCP request(OrderCP order, String weight) {
		logger.info("refund request~~~");
		//查询返回日志的list start
		SysSettingServiceImpl sysImpl = new SysSettingServiceImpl();
		try {
			sysImpl.queryLogList();
		}catch(Exception e){
			logger.error("获取出票系统日志异常！",e);
		}
		List<ReturnOptlog> list_return = sysImpl.getList_return();
		//查询返回日志的list end
		
		//查询系统设置的预订机器人版本
		TrainServiceImpl trainImpl = new TrainServiceImpl();
		int resend_num = 0;
		if(null != MemcachedUtil.getInstance().getAttribute("refund_resend_times_"+order.getOrderId())){
			resend_num = Integer.valueOf(String.valueOf(MemcachedUtil.getInstance().getAttribute("refund_resend_times_"+order.getOrderId())));
		}
		try {
			sysImpl.querySysVal("rand_code_type");
			
			if(StringUtils.isNotEmpty(sysImpl.getSysVal())){
				order.setInputCode(sysImpl.getSysVal());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String optlog = "";
		String order_id = order.getOrderId();
		ResultCP resultCp = new ResultCP();
		resultCp.setOrderId(order_id);
		resultCp.setWorker(worker);
		String cp_id = order.getCpId();
		try{
			String url = getRefundUrl(order,worker.getWorkerExt(), weight);
			logger.info(order_id+"---------------------------------------------------"+url);
			optlog =weight+"退票机器人url:"+worker.getWorkerExt();
			trainImpl.insertHistory(order_id, cp_id, optlog);
			String jsonStr = HttpUtil.sendByGet(url, "UTF-8", "200000", "200000");//调用接口
			if("TIMEOUT".equals(jsonStr)){
				logger.info(order_id+"连接机器人超时，"+jsonStr+cp_id);
				optlog = "该机器人连接超时，若该机器此异常出现次数过多，请联系技术人员！";
				trainImpl.insertHistory(order_id, cp_id, optlog);
				resultCp.setCpId(cp_id);
				resultCp.setRetValue(resultCp.FAILURE);
				resultCp.setErrorInfo(optlog);
			}
			if(!jsonStr.contains("\"ErrorCode\":0")){
				logger.info(order_id+"机器人异常，"+jsonStr+cp_id);
				optlog = "机器人异常，"+jsonStr;
				trainImpl.insertHistory(order_id, cp_id, optlog);
				resultCp.setCpId(cp_id);
				resultCp.setRetValue(resultCp.RESEND);
				resultCp.setErrorInfo(optlog);
				//resultCp_list.add(resultCp);
			}
			
			jsonStr = jsonStr.replace("\\\"", "\"")
			.replace("[\"{", "[{")
			.replace("}\"]", "}]");
			logger.info(order_id+"；退票返回结果："+jsonStr);
			ObjectMapper mapper = new ObjectMapper();
			if(jsonStr.contains("html") || jsonStr.contains("HTML")){
				ReturnRefundInfo refundResult = mapper.readValue(jsonStr, ReturnRefundInfo.class);
				if(jsonStr.contains("springframework")){
					optlog ="发起机器退票失败【服务异常】,转为人工处理！ "+refundResult.getErrorInfo().get(0).getRetInfo();
					logger.info(order_id+"发起机器退票失败【服务异常】,转为人工处理---"+jsonStr+cp_id);
				}else{
					optlog ="发起机器退票失败【12306异常】,转为人工处理！ "+refundResult.getErrorInfo().get(0).getRetInfo();
					logger.info(order_id+"发起机器退票失败【12306异常】,转为人工处理---"+jsonStr+cp_id);
				}
				trainImpl.insertHistory(order_id, cp_id, optlog);
				resultCp.setCpId(cp_id);
				resultCp.setRetValue(resultCp.FAILURE);
				resultCp.setErrorInfo(optlog);
			}else if(jsonStr.contains("\"retValue\":\"failure\"")){
				ReturnRefundInfo refundResult = mapper.readValue(jsonStr, ReturnRefundInfo.class);
				optlog ="发起机器退票失败,转为人工处理！ "+refundResult.getErrorInfo().get(0).getRetInfo();
				trainImpl.insertHistory(order_id, cp_id, optlog);
				logger.info(order_id+"发起机器退票失败,转为人工处理---"+jsonStr+cp_id);
				resultCp.setCpId(cp_id);
				resultCp.setRetValue(resultCp.FAILURE);
				resultCp.setErrorInfo(optlog);
			}else if(jsonStr.contains("已出票")){
				ReturnRefundInfo refundResult = mapper.readValue(jsonStr, ReturnRefundInfo.class);
				optlog ="已出票,转为人工处理！ "+refundResult.getErrorInfo().get(0).getRetInfo();
				trainImpl.insertHistory(order_id, cp_id, optlog);
				logger.info(order_id+"已出票,转为人工处理---"+jsonStr+cp_id);
				resultCp.setCpId(cp_id);
				resultCp.setRetValue(resultCp.MANUAL);
				resultCp.setErrorInfo(optlog);
				resultCp.setReturnOptlog(TrainConsts.RETURN_G3);
			}else if(jsonStr.contains("已退票")){
				ReturnRefundInfo refundResult = mapper.readValue(jsonStr, ReturnRefundInfo.class);
				optlog ="已退票,转为人工处理！ "+refundResult.getErrorInfo().get(0).getRetInfo();
				trainImpl.insertHistory(order_id, cp_id, optlog);
				logger.info(order_id+"已退票,转为人工处理---"+jsonStr+cp_id);
				resultCp.setCpId(cp_id);
				resultCp.setRetValue(resultCp.MANUAL);
				resultCp.setErrorInfo(optlog);
				resultCp.setReturnOptlog(TrainConsts.RETURN_G4);
			}else if(jsonStr.contains("旅游旺季")){
				ReturnRefundInfo refundResult = mapper.readValue(jsonStr, ReturnRefundInfo.class);
				optlog ="旅游旺季,转为人工处理！ "+refundResult.getErrorInfo().get(0).getRetInfo();
				trainImpl.insertHistory(order_id, cp_id, optlog);
				logger.info(order_id+"旅游旺季,转为人工处理---"+jsonStr+cp_id);
				resultCp.setCpId(cp_id);
				resultCp.setRetValue(resultCp.MANUAL);
				resultCp.setErrorInfo(optlog);
				resultCp.setReturnOptlog(TrainConsts.REFUNN_G8);
			}else{
				ReturnRefundInfo refundResult = mapper.readValue(jsonStr, ReturnRefundInfo.class);
				List<ReturnRefundPasEntity> list_pas = refundResult.getErrorInfo().get(0).getCps();
				if(list_pas.size()==0){
					logger.info(order_id+"机器退票返回结果为空！请人工确认并处理"+cp_id);
					optlog ="机器退票返回结果为空！请人工确认并处理！ "+refundResult.getErrorInfo().get(0).getRetInfo();
					trainImpl.insertHistory(order_id, cp_id, optlog);
					resultCp.setCpId(cp_id);
					resultCp.setRetValue(resultCp.FAILURE);
					resultCp.setErrorInfo(optlog);
				}
				for(ReturnRefundPasEntity rrps : list_pas){
					if("00".equals(rrps.getStatus())){
						//更新车票表退票后信息
						Map<String,String> refund_map = new HashMap<String,String>();
						refund_map.put("order_id", order_id);
						refund_map.put("cp_id",rrps.getCpId());
						refund_map.put("refund_12306_money",rrps.getRefund_money());
						refund_map.put("refund_12306_seq", rrps.getTradeNo());
						trainImpl.updateCPOrderRefund(refund_map);
						logger.info(order_id+"退票完成，机器自动审核"+cp_id);
						optlog ="退票完成，准备机器审核！ ";
						trainImpl.insertHistory(order_id, cp_id, optlog);
						resultCp.setCpId(cp_id);
						resultCp.setRetValue(resultCp.SUCCESS);
						resultCp.setErrorInfo(optlog);
						continue; 
					}if("09".equals(rrps.getStatus())){
						//更新车票表退票后信息
						Map<String,String> refund_map = new HashMap<String,String>();
						refund_map.put("order_id", order_id);
						refund_map.put("cp_id",rrps.getCpId());
						refund_map.put("refund_12306_money",rrps.getRefund_money());
						refund_map.put("refund_12306_seq", rrps.getTradeNo());
						trainImpl.updateCPOrderRefund(refund_map);
						logger.info(order_id+"机器自动审核退票完成，请人工审核"+cp_id);
						optlog ="机器自动审核退票完成，请人工审核！ ";
						trainImpl.insertHistory(order_id, cp_id, optlog);
						resultCp.setCpId(cp_id);
						resultCp.setRetValue(resultCp.SUCCESS);
						resultCp.setErrorInfo(optlog);
						continue; 
					}else if("05".equals(rrps.getStatus())){
						logger.info(order_id+"退票失败，请人工处理"+cp_id);
						optlog ="退票失败，请人工处理！ "+rrps.getMsg();
						trainImpl.insertHistory(order_id, cp_id, optlog);
						resultCp.setCpId(cp_id);
						resultCp.setRetValue(resultCp.FAILURE);
						resultCp.setErrorInfo(optlog);
						continue; 
					}else if("04".equals(rrps.getStatus())){
						if("N".equals(rrps.getReturn_flag())){
							logger.info(order_id+"12306上不能退票，请人工处理"+cp_id);
							optlog ="12306上不能退票，请人工处理！ "+rrps.getMsg();
						}else{
							logger.info(order_id+"退票失败，请人工处理"+cp_id);
							optlog ="退票失败，请人工处理！ "+rrps.getMsg();
						}
						trainImpl.insertHistory(order_id, cp_id, optlog);
						resultCp.setCpId(cp_id);
						resultCp.setRetValue(resultCp.MANUAL);
						resultCp.setErrorInfo(optlog);
						continue; 
					}else{
						logger.info(order_id+"退票失败，请人工处理"+cp_id);
						optlog ="退票失败，请人工处理！ "+rrps.getMsg();
						trainImpl.insertHistory(order_id, cp_id, optlog);
						resultCp.setCpId(cp_id);
						resultCp.setRetValue(resultCp.FAILURE);
						resultCp.setErrorInfo(optlog);
						continue; 
					}
				}
			}
		}catch(Exception e){
			logger.error(order_id+"退票失败，请人工处理"+e);
			optlog ="退票失败，请人工处理！ ";
			try {
				trainImpl.insertHistory(order_id, cp_id, optlog);
			} catch (Exception e1) {
				logger.error("插入日志异常:"+order_id,e);
			}
			resultCp.setCpId(cp_id);
			resultCp.setRetValue(resultCp.MANUAL);
			resultCp.setErrorInfo(optlog);
		}
		
		return resultCp;		
	}
	/**
	 * alterURL
	 * @param map
	 * @return
	 */
	protected String getRefundUrl(OrderCP refund, String interfaceUrl, String weight){
		String scriptPath = "refund.lua";
		String accountPwd = refund.getAccountPwd();
		if(DeviceWeight.WEIGHT_PC.equals(weight)) {
			/*PC出票*/
		} else if(DeviceWeight.WEIGHT_APP.equals(weight)) {
			/*APP出票*/
			scriptPath = "appRefund.lua";
			accountPwd = MD5.getMd5_UTF8(accountPwd);
		}
		
		String url = new String(interfaceUrl);
		StringBuffer sb1 = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		sb1.append(refund.getAccountName())
		  .append("|")
		  .append(accountPwd)
		  .append("|")
		  .append(refund.getOrderId())
		  .append("|")
		  .append(refund.getOutTicketBillno())
		  .append("|")
		  .append(refund.getInputCode())
		  .append("|")
		  .append(refund.getOutTicketTime());
		
		sb2.append(refund.getCpId())
		  .append("|")
		  .append(refund.getUserName())
		  .append("|")
		  .append(refund.getTicketType())
		  .append("|")
		  .append(refund.getIdsType())
		  .append("|")
		  .append(refund.getIdsCard())
		  .append("|")
		  .append(seatTypeTurn(refund.getSeatType()))
		  .append("|")
		  .append(refund.getTrainBox())
		  .append("|")
		  .append(refund.getSeatNo());
		String param1 = "";
		try {
			param1 = URLEncoder.encode(sb1.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		String param2 = "";
		try {
			param2 = URLEncoder.encode(sb2.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		logger.info("发起退票的车次信息:Param1="+sb1.toString()+"Param2="+sb2.toString());
		String session_id = String.valueOf(System.currentTimeMillis());
		logger.info("[机器人url]="+url+" [sessionid]:" + session_id);
		
		url += "?ScriptPath="+scriptPath+"&SessionID=$session_id&Timeout=240000&ParamCount=2&Param1=$param1&Param2=$param2";
		
		String refund_url =url.replace("$session_id", session_id)
				.replace("$param1", param1)
				.replace("$param2", param2);
		return refund_url;
	}
	
	//列车席位转换--针对卧铺
	public static String seatTypeTurn(String seat_type){
		return seat_type.contains("6") ? "6" : seat_type.contains("5") ? "5" : seat_type.contains("4") ? "4" : seat_type;
	}
}
