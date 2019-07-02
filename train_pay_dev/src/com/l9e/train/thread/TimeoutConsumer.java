package com.l9e.train.thread;



import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.l9e.train.channel.request.IRequest;
import com.l9e.train.channel.request.RequestFactory;
import com.l9e.train.po.Order;
import com.l9e.train.po.Result;
import com.l9e.train.producerConsumer.distinct.DistinctConsumer;
import com.l9e.train.service.impl.TrainServiceImpl;
import com.unlun.commons.exception.DatabaseException;
import com.unlun.commons.exception.RepeatException;


public class TimeoutConsumer extends DistinctConsumer<Order> {

	private Logger logger=Logger.getLogger(this.getClass());
	

	@Override
	public boolean consume(Order orderbill) {
		TrainServiceImpl service = new TrainServiceImpl();
		logger.info("pay selfid:"+orderbill.getOrderId()+" orderStr:"+orderbill.getBuymoney());
		
		try {
			//start 记录日志
			String optlog =null;
			
			if("55".contains(orderbill.getOrderStatus())){//如果状态为开始支付
				optlog = "train_pay_new进入正在支付，选择支付方式"; 
			}else{
				optlog = "支付状态异常，状态为："+orderbill.getOrderStatus();
			}
			
			service.insertHistory(orderbill.getOrderId(), optlog);
			//end
			
			//start 选择账号、处理人员和处理类
			logger.info("select request="+orderbill.getOrderId()+" start");
			IRequest request  = new RequestFactory().select(orderbill);
			logger.info("select request="+orderbill.getOrderId()+" end requestImpl:"+request);
			
			if(request == null){
					
				service.payIsManual(orderbill);
				
				logger.error(orderbill.getOrderId()+" not Card");
				service.insertHistory(orderbill.getOrderId(), "没有空闲卡号、工号和账号，请进行人工核对后进行处理。");
				return false;
			}
			//end
			Result result = null;
			//start 利用处理类和其它信息进行订单的处理
			Integer workerReportId = null;
			try {
				logger.info("request="+orderbill.getOrderId()+" start");
				workerReportId = service.startWorkerReport(request.getWorker(), orderbill, "2");
				result = request.request(orderbill);
				logger.info("request="+orderbill.getOrderId()+" end retValue:"+result.getRetValue());
			} finally {
				service.endWorkerReport(workerReportId);
			}
			logger.info("modify orderbill cpid="+orderbill.getOrderId()+" workerName:"+result.getWorker().getWorkerName()+" status:"+result.getRetValue()+" start!");
			//end
		
			//start 对处理后的结果的返回值，进行订单的调整
			if (StringUtils.equals(result.getRetValue(), Result.SUCCESS)) {// 订单表改为成功，通知表改为正在通知
				if(service.querySameOut_ticket_billno(orderbill)>1){
					optlog = result.getWorker().getWorkerName()+"，支付成功，可能存在一人多单，或者往返票赔款的情况，转入人工处理";
					logger.info(orderbill.getOrderId()+optlog);
					logger.info(orderbill.getOrderId()+" pay is manal!");
					orderbill.setPaybillno(result.getPaybillno());
					service.payIsManual(orderbill);
				}else{	
					optlog = result.getWorker().getWorkerName()+"，支付成功，进入核对流程！["+result.getErrorInfo()+"]";
					logger.info(orderbill.getOrderId()+" pay is success!");
					service.payIsSuccess(orderbill, result);
					service.balance4PayCard(request.getPayCard(), result);
				}
			} else if (StringUtils.equals(result.getRetValue(), Result.FAILURE)) {// 订单表改为失败，通知表改为正在通知
				
				optlog = result.getWorker().getWorkerName()+"，支付失败！["+result.getErrorInfo()+"]";
				logger.info(orderbill.getOrderId()+" pay is failure!");
				orderbill.setPaybillno(result.getPaybillno());
				service.payIsManual(orderbill);
				
				//释放卡号
				//service.freeCard(result.getPayCard().getCardId());
				
			} else if (StringUtils.equals(result.getRetValue(), Result.MANUAL)) {// 订单表改为人工，通知表改为正在通知
				if(paySuccessNoOrder(service) && result.getErrorInfo().contains("已成功付款,但是没有查询到完成订单")) {
					optlog = result.getWorker().getWorkerName() + ",支付成功，未找到完成订单！[" + result.getErrorInfo() + "]";
					logger.info(orderbill.getOrderId()+" pay is success! no finished order");
					service.payIsSuccess(orderbill, result);
					service.order2find(orderbill.getOrderId());
				} else {
					
					optlog = result.getWorker().getWorkerName()+"，支付人工！["+result.getErrorInfo()+"]";
					logger.info(orderbill.getOrderId()+" pay is manal!");
					orderbill.setPaybillno(result.getPaybillno());
					service.payIsManual(orderbill);
				}
				
				//释放卡号
				//service.freeCard(result.getPayCard().getCardId());
				
			}else if (StringUtils.equals(result.getRetValue(), Result.RESEND)) {// 订单表改为人工，通知表改为正在通知
				
				optlog = result.getWorker().getWorkerName()+"，支付重发！["+result.getErrorInfo()+"]";
				logger.info(orderbill.getOrderId()+" pay is resend!");
				orderbill.setPaybillno(result.getPaybillno());
				service.payIsResend(orderbill);
				//释放卡号
				//service.freeCard(result.getPayCard().getCardId());
				
			}else{// 异常
				
				optlog = result.getWorker().getWorkerName()+"，支付异常！";
				logger.warn(result.getSelfId()+" find order is exception, restor find!");
				//释放卡号
				//service.freeCard(result.getPayCard().getCardId());
			}
			//end
			
			//start 记录日志
			service.insertHistory(orderbill.getOrderId(), optlog);	
			logger.info("modify orderbill cpid="+orderbill.getOrderId()+" status:"+result.getRetValue()+" end!");
			//end
			
			/*停用被封停机器人*/
			if(result != null) {
				String errorInfo = result.getErrorInfo();
				if(errorInfo != null && errorInfo.contains("您的操作频率过快")) {
					
					if(request != null && request.getWorker() != null) {
						logger.info("机器人被封停。停用机器人 workerId : " + request.getWorker().getWorkerId());
						service.stopWorker(request.getWorker());
					}
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.warn("exception!!:"+e);
		}
		return true;
	}
	@Override
	public String getObjectKeyId(Order t) {
		// TODO Auto-generated method stub
		return t.getOrderId();
	}
	/**
	 * 是否开启了支付成功但未找到完成订单检测功能
	 * @return
	 */
	private boolean paySuccessNoOrder(TrainServiceImpl service) {
		boolean flag = false;
		try {
			int ret = service.isPaySuccessNoOrder();
			if(ret == 1) flag = true;
		} catch (RepeatException e) {
			e.printStackTrace();
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		return flag;
	}

}