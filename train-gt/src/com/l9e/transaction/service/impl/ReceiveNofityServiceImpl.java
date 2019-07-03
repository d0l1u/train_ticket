package com.l9e.transaction.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.l9e.common.TrainConsts;
import com.l9e.transaction.dao.OrderDao;
import com.l9e.transaction.dao.ReceiveNotifyDao;
import com.l9e.transaction.dao.RefundDao;
import com.l9e.transaction.service.CommonService;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.service.ReceiveNotifyService;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.util.AmountUtil;
import com.l9e.util.CreateIDUtil;

@Service("receiveNotifyService")
public class ReceiveNofityServiceImpl implements ReceiveNotifyService {
	private static final Logger logger = Logger.getLogger(ReceiveNofityServiceImpl.class);
	
	@Resource
	private ReceiveNotifyDao receiveNotifyDao;
	
	@Resource
	private OrderDao orderDao;
	
	@Resource
	private OrderService orderService;
	
	@Resource
	private RefundDao refundDao;
	
	@Resource
	private CommonService commonService;

	public int updateOrderWithCpNotify(Map<String, String> paraMap, 
			List<Map<String, String>> cpMapList, Map<String, String> failRefundMap) {
		try {
			
			//String out_notify = commonService.querySysSettingByKey("out_notify");
			String order_id = paraMap.get("order_id");
			//String book_flow = paraMap.get("book_flow");
			//OrderInfo orderInfo = orderService.queryOrderInfo(order_id);
	
			receiveNotifyDao.updateOrderWithCpNotify(paraMap);
			if (cpMapList != null) {
				for (Map<String, String> cpMap : cpMapList) {
					receiveNotifyDao.updateCpOrderWithCpNotify(cpMap);
				}
				//出票成功则更新保险状态
				if(!StringUtils.isEmpty(paraMap.get("order_status"))
						&& TrainConsts.OUT_SUCCESS.equals(paraMap.get("order_status"))){
					orderDao.updateBxStatusNotSend(order_id);
				}
				
				//生成差额退款数据
				Map<String, String> differMap = orderDao.queryOrderDiffer(order_id);
				Map<String, String> refundMap = new HashMap<String, String>();
				String is_travel = paraMap.get("is_travel");
				if(!"".equals(is_travel)){
					refundMap.put("is_travel",is_travel);
				}
				
				//更新退款总金额
				Map<String, String> tkMoneyMap = new HashMap<String, String>();
				if(differMap != null && !StringUtils.isEmpty(differMap.get("refund_money"))
					&& Double.parseDouble(differMap.get("refund_money")) > 0){
					String remark = "差额退款，退款金额：" + differMap.get("refund_money");
					refundMap.put("order_id", order_id);
					refundMap.put("refund_type", TrainConsts.ORDER_RETURN_TYPE_1);//差额退款
					refundMap.put("refund_amount", differMap.get("refund_money"));
/*					if("00".equals(book_flow)){//先支付后出票
						//预定结果通知
						if("1".equals(out_notify) && TrainConsts.BOOK_SUCCESS.equals(paraMap.get("order_status"))){
							//生成订单处理结果通知数据
							receiveNotifyDao.addOrderResultNotify(refundMap);
							//更新商户出票数量
							orderDao.updateMerchantTicketNum(order_id);
						}else if(TrainConsts.OUT_SUCCESS.equals(paraMap.get("order_status"))){
							//生成订单处理结果通知数据
							receiveNotifyDao.addOrderResultNotify(refundMap);
							//更新商户出票数量
							orderDao.updateMerchantTicketNum(order_id);
						}
					}else*/{//先预定后支付，出票成功通知
						if(TrainConsts.OUT_SUCCESS.equals(paraMap.get("order_status"))){
							//生成订单处理结果通知数据
							receiveNotifyDao.addOrderResultNotify(refundMap);
							//更新商户出票数量
							orderDao.updateMerchantTicketNum(order_id);
						}
					}
					
					double refund_total = Double.parseDouble(differMap.get("refund_total"));
					double refund_money = Double.parseDouble(differMap.get("refund_money"));
					refund_total = AmountUtil.add(refund_total, refund_money);
					tkMoneyMap.put("refund_total", String.valueOf(refund_total));
					tkMoneyMap.put("order_id", order_id);
					orderDao.updateOrderRefundTotal(tkMoneyMap);
					//生成差额退款数据
					String refund_seq = CreateIDUtil.createID("TK");
					OrderInfo orderInfo = orderDao.queryOrderInfo(order_id);
					Map<String,String> refundStreamMap = new HashMap<String, String>();
					refundStreamMap.put("refund_type", TrainConsts.REFUND_TYPE_DIFF);//差额退款退款
					refundStreamMap.put("order_id", order_id);
					refundStreamMap.put("merchant_order_id", orderInfo.getMerchant_order_id());
					refundStreamMap.put("refund_seq", refund_seq);//退款请求流水号
					refundStreamMap.put("refund_money", String.valueOf(refund_money));//退款金额
					refundStreamMap.put("user_remark", "自动添加差额退款数据");
					refundStreamMap.put("refund_status", TrainConsts.REFUND_STATUS_AGREE);//同意退票
					refundStreamMap.put("channel", orderInfo.getMerchant_id());//渠道
					refundDao.addRefundStream(refundStreamMap);
					//如果商户支付方式是代付，则向平台退款通知表添加退款一条数据
					Map<String,String> merchantMap = commonService.queryMerchantInfo(orderInfo.getMerchant_id());
					if("11".equals(merchantMap.get("pay_type"))){
						Map<String,String> param = new HashMap<String,String>();
						param.put("refund_money", String.valueOf(refund_money));
						param.put("order_id", order_id);
						param.put("eop_order_id", orderInfo.getEop_order_id());
						param.put("refund_seq",refund_seq);
						param.put("refund_reason", "差额退款");
						param.put("notify_status", "11");
						param.put("eop_refund_url", orderInfo.getEop_refund_url());
						receiveNotifyDao.addEopRefundNotify(param);
					}
					//添加操作日志
					Map<String, String> logMap = new HashMap<String, String>();
					logMap.put("order_id", order_id);
					logMap.put("order_optlog", remark);
					logMap.put("opter", "19trip");
					orderDao.addOrderOptLog(logMap);
				}else{
/*					if("00".equals(book_flow)){//先支付后出票
						//预定结果通知
						if("1".equals(out_notify) && TrainConsts.BOOK_SUCCESS.equals(paraMap.get("order_status"))){
							refundMap.put("order_id", order_id);
							refundMap.put("refund_type", TrainConsts.ORDER_RETURN_TYPE_1);//差额退款
							refundMap.put("refund_amount","0.00");
							//生成订单处理结果通知数据
							receiveNotifyDao.addOrderResultNotify(refundMap);
							//更新商户出票数量
							orderDao.updateMerchantTicketNum(order_id);
						}else if(TrainConsts.OUT_SUCCESS.equals(paraMap.get("order_status"))){
							refundMap.put("order_id", order_id);
							refundMap.put("refund_type", TrainConsts.ORDER_RETURN_TYPE_1);//差额退款
							refundMap.put("refund_amount","0.00");
							//生成订单处理结果通知数据
							receiveNotifyDao.addOrderResultNotify(refundMap);
							//更新商户出票数量
							orderDao.updateMerchantTicketNum(order_id);
						}
					}else*/{//先预定后支付
						if(TrainConsts.OUT_SUCCESS.equals(paraMap.get("order_status"))){
							//出票成功通知
							refundMap.put("order_id", order_id);
							refundMap.put("refund_type", TrainConsts.ORDER_RETURN_TYPE_1);//差额退款
							refundMap.put("refund_amount","0.00");
							//生成订单处理结果通知数据
							receiveNotifyDao.addOrderResultNotify(refundMap);
							//更新商户出票数量
							orderDao.updateMerchantTicketNum(order_id);
						}else{
							//预定结果通知
							Map<String,String> map = new HashMap<String,String>();
							map.put("order_id", order_id);
							OrderInfo orderInfo = orderDao.queryOrderInfo(order_id);
							if(null==orderInfo.getEop_pay_number()){
								map.put("old_notify_status", "00");
								map.put("notify_status", "11");
								map.put("notify_time","notify_time");//不更新notify_time值
								receiveNotifyDao.updateOrderBookStatus(map);
								orderService.updateBookSuccessInfo("update", order_id, orderInfo.getMerchant_id());
							}else{
								map.put("old_notify_status", "00");
								map.put("notify_status", "33");
								receiveNotifyDao.updateOrderBookStatus(map);
								orderDao.updateBookSuccessTime(order_id);
							}
						}
					}
				}
			}
			//生成出票失败退款数据
			if (failRefundMap != null) {
				OrderInfo orderInfo = orderDao.queryOrderInfo(order_id);
				String  order_type = orderInfo.getOrder_type(); //11.先下单后付款,22.先支付后预定
				logger.info(order_id+",订单类型："+order_type);
				Map<String,String> merchantMap = commonService.queryMerchantInfo(orderInfo.getMerchant_id());	
				if(null==failRefundMap.get("eop_pay_number") || "11".equals(merchantMap.get("book_flow"))){
					failRefundMap.put("refund_amount", "0.0");		//退给用户的金额
					failRefundMap.put("refund_money", "0.0");	    //退给商户的金额
					if ("11".equals(order_type)) {
						logger.info(order_id+",先预定后支付,占座失败通知");
						Map<String, String> logMap = new HashMap<String, String>();
						logMap.put("order_id", failRefundMap.get("order_id"));
						logMap.put("order_optlog", "占座失败，异步通知高铁管家,失败原因："+TrainConsts.getOutFailReason().get(failRefundMap.get("fail_reason")));
						logMap.put("opter", "gt_app");
						orderDao.addOrderOptLog(logMap);
						Map<String,String> map = new HashMap<String,String>();
						map.put("order_id", order_id);
						//更新订单失败原因
						map.put("fail_reason", failRefundMap.get("fail_reason"));
						orderDao.updateOrderInfo(map);//更新失败原因
						if(null==orderInfo.getEop_pay_number()){
							String book_notify_status =receiveNotifyDao.queryBookNotifyStatus(order_id);
							if ("22".equals(book_notify_status)) {//出票失败时,预定是通知完成，不再次通知
								Map<String,String> pMap = new HashMap<String,String>();
								if ("6".equals(failRefundMap.get("fail_reason"))) {//用户取消订单,修改取消成功,不给回调
									logger.info(order_id+",用户取消订单，不给回调,订单状态："+orderInfo.getOrder_status());
									pMap.put("asp_order_id", order_id);
									pMap.put("order_status", TrainConsts.CANCEL_SUCCESS);
									pMap.put("old_status", TrainConsts.OUT_FAIL);
									orderService.updateOrderStatus(pMap);
									return 1;
								} else if ("11".equals(failRefundMap.get("fail_reason"))) {//超时订单,自动取消,修改为超时状态，不作回调
									logger.info(order_id+",超时订单，自动取消，不给回调,订单状态："+orderInfo.getOrder_status());
									pMap.put("asp_order_id", order_id);
									pMap.put("order_status", TrainConsts.ORDER_OUT_TIME);
									pMap.put("old_status", TrainConsts.OUT_FAIL);
									orderService.updateOrderStatus(pMap);
									
									//receiveNotifyDao.addOrderResultNotify(failRefundMap); //超时订单，再次确认出票，需回调(不符合逻辑)
																	
									return 1;
								}else{
									logger.info(order_id+"，预定成功的订单，再次出票失败，不修改预定通知表,预定通知状态"+book_notify_status);
									receiveNotifyDao.addOrderResultNotify(failRefundMap);
									return 1;
								}		
							}else {
								/*	Map<String,String> pMap = new HashMap<String,String>();
								if ("6".equals(failRefundMap.get("fail_reason"))) {////用户取消订单,修改取消成功,不给回调
									logger.info(order_id+",超时订单，自动取消，不给回调,订单状态："+orderInfo.getOrder_status());
									pMap.put("asp_order_id", order_id);
									pMap.put("order_status", TrainConsts.CANCEL_SUCCESS);
									pMap.put("old_status", TrainConsts.OUT_FAIL);
									orderService.updateOrderStatus(pMap);
									return 1;
								}else if ("11".equals(failRefundMap.get("fail_reason"))) {//超时订单,自动取消,修改为超时状态，不作回调
									logger.info(order_id+",超时订单，自动取消，不给回调,订单状态："+orderInfo.getOrder_status());
									pMap.put("asp_order_id", order_id);
									pMap.put("order_status", TrainConsts.ORDER_OUT_TIME);
									pMap.put("old_status", TrainConsts.OUT_FAIL);
									orderService.updateOrderStatus(pMap);
									return 1;
								}else*/{
								    logger.info(order_id+",占座未通知,更改为准备通知："+orderInfo.getOrder_status()+",失败原因："+failRefundMap.get("fail_reason"));
									map.put("old_notify_status", "00");
									map.put("notify_status", "11");
									map.put("notify_time","notify_time");//不更新notify_time值
									receiveNotifyDao.updateOrderBookStatus(map);
									logger.info(order_id+"更新预定通知为准备通知："+map);
									return 1;
									}
							}
						}else{
							map.put("old_notify_status", "00");
							map.put("notify_status", "33");
							receiveNotifyDao.updateOrderBookStatus(map);
							//orderDao.updateBookSuccessTime(order_id);
							return 1;
						}	
					}else{ //先支付后预定
						logger.info(order_id+",先支付后预定,出票失败通知");
						//生成订单处理结果通知数据
						receiveNotifyDao.addOrderResultNotify(failRefundMap);
						//添加操作日志
						Map<String, String> logMap = new HashMap<String, String>();
						logMap.put("order_id", failRefundMap.get("order_id"));
						logMap.put("order_optlog", "出票失败，通知高铁管家全额退款。");
						logMap.put("opter", "gt_app");
						orderDao.addOrderOptLog(logMap);
						return 1;
					}
					
				}	
				//生成订单处理结果通知数据
				receiveNotifyDao.addOrderResultNotify(failRefundMap);
				//生成出票失败退款数据
				String refund_seq = CreateIDUtil.createID("TK");
				Map<String,String> refundStreamMap = new HashMap<String, String>();
				refundStreamMap.put("refund_type", TrainConsts.REFUND_TYPE_FAIL);//出票失败退款
				refundStreamMap.put("order_id", order_id);
				refundStreamMap.put("merchant_order_id", orderInfo.getMerchant_order_id());
				refundStreamMap.put("refund_seq", refund_seq);//退款请求流水号
				refundStreamMap.put("refund_money", failRefundMap.get("refund_money"));//退款金额
				refundStreamMap.put("user_remark", "出票失败退款");
				refundStreamMap.put("refund_status", TrainConsts.REFUND_STATUS_WAIT);//等待退票
				refundStreamMap.put("channel", orderInfo.getMerchant_id());//渠道
				refundDao.addRefundStream(refundStreamMap);
				
				//如果商户支付方式是代付，则向平台退款通知表添加退款一条数据
				if("11".equals(merchantMap.get("pay_type"))){
					Map<String,String> param = new HashMap<String,String>();
					param.put("refund_money", failRefundMap.get("refund_amount"));
					param.put("order_id", order_id);
					param.put("eop_order_id", orderInfo.getEop_order_id());
					param.put("refund_seq",refund_seq);
					param.put("refund_reason", "出票失败退款");
					param.put("notify_status", "00");
					param.put("eop_refund_url", orderInfo.getEop_refund_url());
					receiveNotifyDao.addEopRefundNotify(param);
				}
				//更新退款总金额，是否能退款
				Map<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("refund_total", failRefundMap.get("refund_money"));
				paramMap.put("order_id", failRefundMap.get("order_id"));
				paramMap.put("can_refund", "0");//不能退款
				orderDao.updateOrderRefundTotal(paramMap);
				
				//添加操作日志
				Map<String, String> logMap = new HashMap<String, String>();
				logMap.put("order_id", failRefundMap.get("order_id"));
				logMap.put("order_optlog", "系统自动发起出票失败退款，退款金额：" + failRefundMap.get("refund_money"));
				logMap.put("opter", "19trip");
				orderDao.addOrderOptLog(logMap);
			}
		} catch (Exception e) {
			//logger.info("Exception"+order_id+":"+e);
			logger.info("Exception"+paraMap.get("order_id"), e);
			e.printStackTrace();
			return 0;
		}
		return 1;
		
	}

	@Override
	public List<Map<String, String>> findOrderResultNotify() {
		return receiveNotifyDao.findOrderResultNotify();
	}

	@Override
	public void updateOrderResultNotify(Map<String, String> map) {
		receiveNotifyDao.updateOrderResultNotify(map);
	}
	
	@Override
	public void updateOrderResultNotifyStartNum(String order_id) {
		receiveNotifyDao.updateOrderResultNotifyStartNum(order_id);
	}

	@Override
	public void updatePayResultNotify(String order_id) {
		receiveNotifyDao.updatePayResultNotify(order_id);
	}

	@Override
	public List<Map<String, String>> findPayResultNotify() {
		return receiveNotifyDao.findPayResultNotify();
	}

	@Override
	public int queryEopRefundNotifyAlterNum(String refund_seq) {
		return receiveNotifyDao.queryEopRefundNotifyAlterNum(refund_seq);
	}

	@Override
	public void updateEopRefundResult(Map<String, String> paramMap) {
		receiveNotifyDao.updateEopRefundResult(paramMap);
	}

	@Override
	public void addEopRefundNotify(Map<String, String> paramMap) {
		receiveNotifyDao.addEopRefundNotify(paramMap);
	}

	@Override
	public void updateOrderReturnStatus(Map<String, String> map) {
		receiveNotifyDao.updateOrderReturnStatus(map);
	}

	@Override
	public List<Map<String, String>> findOrderBookNotify() {
		return receiveNotifyDao.findOrderBookNotify();
	}

	@Override
	public void updateOrderBookStatus(Map<String, String> map) {
		receiveNotifyDao.updateOrderBookStatus(map);
	}

	@Override
	public void updateOrderBookNotifyStartNum(String order_id) {
		receiveNotifyDao.updateOrderBookNotifyStartNum(order_id);
	}

	@Override
	public void updatePayResultNotifyStatus(Map<String, String> map) {
		receiveNotifyDao.updatePayResultNotifyStatus(map);
	}

	@Override
	public void updateOrderBookNotifyFinish(String order_id) {
		receiveNotifyDao.updateOrderBookNotifyFinish(order_id); 
	}

	@Override
	public List<Map<String, String>> queryEopAndPayNotify() {
		return receiveNotifyDao.queryEopAndPayNotify(); 
	}

	@Override
	public void updateEopAndPayNotifyInfo(Map<String, String> param) {
		receiveNotifyDao.updateEopAndPayNotifyInfo(param); 
	}

	@Override
	public void updateEopAndPayNotifyFinish(Map<String, String> param) {
		receiveNotifyDao.updateEopAndPayNotifyFinish(param); 
	}

	@Override
	public void updateEopAndPayNotifyNums(Map<String, String> param) {
		receiveNotifyDao.updateEopAndPayNotifyNums(param); 
	}

	@Override
	public void insertPayReturnNotify(Map<String, String> param) {
		receiveNotifyDao.insertPayReturnNotify(param);
	}

	@Override
	public void updateOrderPayNotifyFinish(Map<String, String> param) {
		receiveNotifyDao.updateOrderPayNotifyFinish(param);
	}

	@Override
	public List<Map<String, String>> findOrderPayNotify() {
		return receiveNotifyDao.findOrderPayNotify();
	}

	@Override
	public void updatePayReturnNotifyNums(String orderId) {
		receiveNotifyDao.updatePayReturnNotifyNums(orderId);
	}

	@Override
	public Integer queryOrderResultNotifyStartNum(String orderId) {
		return receiveNotifyDao.queryOrderResultNotifyStartNum(orderId);
	}

	@Override
	public String queryMerchantIdByOrderId(String orderId) {
		return receiveNotifyDao.queryMerchantIdByOrderId(orderId);
	}

	@Override
	public Integer updateOrderGtBookNotifyStatus(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return receiveNotifyDao.updateOrderGtBookNotifyStatus(paramMap);
	}

	@Override
	public int updateOrderResultStatus(Map<String, String> updatenew) {
		// TODO Auto-generated method stub
		return receiveNotifyDao.updateOrderResultStatus(updatenew);
	}

	@Override
	public int queryOrderBookNotifyStartNum(String order_id) {
		return receiveNotifyDao.queryOrderBookNotifyStartNum(order_id);
	}

}
