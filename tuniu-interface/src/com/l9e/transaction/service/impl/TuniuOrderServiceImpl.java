package com.l9e.transaction.service.impl;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.l9e.common.TuniuConstant;
import com.l9e.transaction.component.OutTicketNotifier;
import com.l9e.transaction.component.TuniuCallback;
import com.l9e.transaction.component.model.NoticeObserver;
import com.l9e.transaction.dao.TuniuChangeDao;
import com.l9e.transaction.dao.TuniuCommonDao;
import com.l9e.transaction.dao.TuniuNoticeDao;
import com.l9e.transaction.dao.TuniuOrderDao;
import com.l9e.transaction.dao.impl.TuniuRefundDaoImpl;
import com.l9e.transaction.exception.TuniuCommonException;
import com.l9e.transaction.exception.TuniuException;
import com.l9e.transaction.exception.TuniuOrderException;
import com.l9e.transaction.service.TuniuCommonService;
import com.l9e.transaction.service.TuniuOrderService;
import com.l9e.transaction.thread.SimpleRequest;
import com.l9e.transaction.thread.TuniuThreadPool;
import com.l9e.transaction.vo.Account;
import com.l9e.transaction.vo.AsynchronousOutput;
import com.l9e.transaction.vo.CpSysLogVO;
import com.l9e.transaction.vo.Notice;
import com.l9e.transaction.vo.OutimeOrderVO;
import com.l9e.transaction.vo.Station;
import com.l9e.transaction.vo.SynchronousOutput;
import com.l9e.transaction.vo.TicketEntrance;
import com.l9e.transaction.vo.TuniuChangeInfo;
import com.l9e.transaction.vo.TuniuChangeLogVO;
import com.l9e.transaction.vo.TuniuOrder;
import com.l9e.transaction.vo.TuniuParameter;
import com.l9e.transaction.vo.TuniuPassenger;
import com.l9e.transaction.vo.TuniuQueueOrder;
import com.l9e.transaction.vo.TuniuResult;
import com.l9e.transaction.vo.model.Parameter;
import com.l9e.transaction.vo.model.Result;
import com.l9e.util.BeanUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.EncryptUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.IDGenerator;
import com.l9e.util.JacksonUtil;
import com.l9e.util.UrlFormatUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service("tuniuOrderService")
public class TuniuOrderServiceImpl extends TuniuCommonServiceImpl implements TuniuOrderService {

	private static final Logger logger = Logger.getLogger(TuniuOrderServiceImpl.class);

	@Resource
	private TuniuCommonDao tuniuCommonDao;
	
	@Resource
	private TuniuOrderDao tuniuOrderDao;

	@Resource
	private TuniuNoticeDao tuniuNoticeDao;
	@Resource
	private TuniuRefundDaoImpl tuniuRefundDao;
	@Resource
	private TuniuCommonService tuniuCommonService;
	@Resource
	private  TuniuChangeDao  tuniuChangeDao;
	
	@Value("#{config}")
	private Properties config;

	public Result trainBook(Parameter parameter) {
		String orderId = parameter.getString("orderId");// 途牛订单号
		String callBackUrl = parameter.getString("callBackUrl");// 回调地址

		logger.info("途牛异步[占座]开始，orderId：" + orderId);
		Result result = new TuniuResult();
		result.putData("vendorOrderId", orderId);
		try {
			if (!isDuplicateOrder(orderId)) {//查询是否已经入库
				long ansynStart = System.currentTimeMillis();
				/* 新订单入库 */
				TuniuOrder order = tuniuOrder(parameter);//封装数据
				if(order == null) {
					logger.info("途牛请求占座，传参有误");
					throw new TuniuCommonException(RETURN_CODE_PARAM_ERROR);
				}
				Notice notice = tuniuNotice(orderId, callBackUrl);//封装通知对象
				addOrder(order);//插入预订表
				addNotice(notice, "book");//插入预订结果通知表(通知出票状态00准备通知；通知途牛状态为空，不通知)
				
				if(order.getUserName()!=""&&order.getUserPassword()!=""){
					//更新出票系统账号
					//参数拼接整合
					Map<String,String> params = new HashMap<String,String>();
					params.put("userName", order.getUserName());
					params.put("userPassWord", order.getUserPassword());
					String uri = UrlFormatUtil.createUrl("", params, "utf-8");
					//post请求
					String editAccountresult = HttpUtil.sendByPost(config.getProperty("cp_edit_account"), uri, "UTF-8");
					logger.info("途牛请求占座，userName:" + order.getUserName()+",修改账号结果："+editAccountresult);
					//解析返回参数
					JSONObject strObject = JSONObject.fromObject(editAccountresult);
					Boolean flag = strObject.getBoolean("success");
					if(flag){
						logger.info("途牛请求占座，修改账号成功，userName" + order.getUserName());
					}else{
						logger.info("途牛请求占座，修改账号失败 ，userName" + order.getUserName());
					}
					
				}
				
				long ansynEnd = System.currentTimeMillis();
				logger.info("途牛异步请求[占座]成功，orderId：" + orderId + ", 耗时:"+ (ansynEnd - ansynStart) + " ms");
			} else {
				result.setCode(RETURN_CODE_REPEAT_ORDER);
				logger.info("途牛[占座]重复下单, orderId : " + orderId);
			}
		} catch (TuniuException e) {
			result.setCode(e.getMessage());
		} catch (Exception e) {
			logger.info("途牛预订异常 orderId : " + orderId + ",e: " + e.getMessage());
			result.setCode(RETURN_CODE_SYSTEM_ERROR);
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public Result trainConfirm(Parameter parameter) {
		String orderId = parameter.getString("orderId");// 途牛订单号
		String callBackUrl = parameter.getString("callBackUrl");// 回调地址

		logger.info("途牛[确认出票]开始，orderId：" + orderId + " , callback : " + callBackUrl);

		Result result = new TuniuResult();
		result.putData("vendorOrderId", orderId);
		result.putData("orderId", orderId);
		try {
			long ansynStart = System.currentTimeMillis();
			/* 查询订单并检查状态 */
			Map<String, Object> queryParam = new HashMap<String, Object>();
			queryParam.put("orderId", orderId);
			TuniuOrder order = tuniuOrderDao.selectOneOrder(queryParam);//查询订单是否存在
			if (order == null)
				throw new TuniuOrderException(RETURN_CODE_CONFIRM_FAIL);

			String status = order.getOrderStatus();
			if (STATUS_WAIT_PAY.equals(status)) {
				//暂无
			} else if (STATUS_BOOK_SUCCESS.equals(status)) {
				/* 添加通知记录 */
				Notice notice = tuniuNotice(orderId, callBackUrl);
				addNotice(notice, "out");//添加出票通知，通知出票状态为00 准备通知，通知途牛状态为空
				long ansynEnd = System.currentTimeMillis();
				logger.info("途牛异步请求[确认出票]成功，orderId：" + orderId + ", 耗时:"+ (ansynEnd - ansynStart) + " ms");
			} else if (STATUS_OUT_FAILURE.equals(status)) {
				logger.info("订单超时,确认支付失败, orderId : " + orderId);
				throw new TuniuOrderException(RETURN_CODE_ORDER_TIME_OUT);
			} else if(STATUS_CANCEL_SUCCESS.equals(status)){
				logger.info("订单已取消，不可出票, orderId : " + orderId);
				throw new TuniuOrderException(RETURN_CODE_ORDER_CANCELD);
			}else {
				logger.info("订单状态异常,确认支付失败, orderId : " + orderId);
				throw new TuniuOrderException(RETURN_CODE_CONFIRM_FAIL);
			}
		} catch (TuniuException e) {
			result.setCode(e.getMessage());
		} catch (Exception e) {
			logger.info("途牛确认支付异常 orderId : " + orderId + ",e: " + e.getMessage());
			result.setCode(RETURN_CODE_CONFIRE_SYS_ERROR);
		}
		return result;
	}

	@Override
	public Result trainCancel(Parameter parameter) {
		String orderId = parameter.getString("orderId");// 途牛订单号
		String callBackUrl = parameter.getString("callBackUrl");// 回调地址

		logger.info("途牛[取消]订单开始，orderId：" + orderId);
		Result result = new TuniuResult();
		result.putData("vendorOrderId", orderId);
		try {
			long ansynStart = System.currentTimeMillis();
			/* 查询订单并检查状态 */
			Map<String, Object> queryParam = new HashMap<String, Object>();
			queryParam.put("orderId", orderId);
			TuniuOrder order = tuniuOrderDao.selectOneOrder(queryParam);
			Notice notice = tuniuNoticeDao.selectOutOneNotice(queryParam);
			if (order == null){
				logger.info("途牛异步请求[取消]订单不存在，orderId：" + orderId);
				throw new TuniuOrderException(RETURN_CODE_CANCEL_FAIL);//订单不存在
			}
			
			String status = order.getOrderStatus();
			if(notice == null) {
				notice = tuniuNotice(orderId, callBackUrl);
			}
			notice.setNotifyUrl(callBackUrl);
			//00下单成功 11通知出票成功 22预订成功 32、 正在出票 33出票成功 44出票失败 51撤销中 52撤销失败 88 超时订单 23、正在取消 24 取消成功
			if (STATUS_WAIT_PAY.equals(status)||STATUS_OUT_SUCCESS.equals(status)) {//32、 正在出票 33出票成功
				logger.info("途牛异步请求[取消]订单已出票不能取消，orderId：" + orderId);
				throw new TuniuOrderException(RETURN_CODE_TICKET_OUT);//订单已出票，不能取消
				
			} else if ( STATUS_OUT_FAILURE.equals(status)||STATUS_TIME_OUT.equals(status)) {// 44出票失败 88 超时订单
				logger.info("途牛异步请求[取消]订单已经超时/出票失败，orderId：" + orderId);
				throw new TuniuOrderException(RETURN_CODE_CANCEL_ILLEGAL_STATUS);//订单状态非法，只有占位成功的订单才能取消
				
			} else if ( STATUS_CANCEL_ING.equals(status)) {//23、正在取消
				logger.info("途牛异步请求[取消]订单正在取消，orderId：" + orderId);
				throw new TuniuOrderException(RETURN_CODE_CANCEL_ORDER_ING);//订单正在取消中

			} else if(STATUS_CANCEL_SUCCESS.equals(status)){// 24 取消成功
				logger.info("途牛异步请求[取消]订单已经取消，orderId：" + orderId);
				throw new TuniuOrderException(RETURN_CODE_CANCEL_ORDER_ALREADY_CANCEL);//订单正在取消中
			}else if (STATUS_BOOK_SUCCESS.equals(status) ||STATUS_PLACE_ORDER_SUCCESS.equals(status)||STATUS_NOTIFY_SUCCESS.equals(status)) {//00下单成功 11通知出票成功 22预订成功 
				logger.info("途牛异步请求[取消]【请求成功】准备通知出票系统，orderId：" + orderId);
				notice.setCpNotifyStatus(NOTIFY_PREPARED);
				order.setOrderStatus(STATUS_CANCEL_ING);
				tuniuOrderDao.updateOrder(order);
				
			} else {
				logger.info("途牛异步请求[取消]失败，orderId：" + orderId+"订单状态："+status );
				throw new TuniuOrderException(RETURN_CODE_CANCEL_ILLEGAL_STATUS);
			}
			/* 更新通知记录 */
			addNotice(notice, "out");
			// 取消和出票结果共用回调地址字段
			long ansynEnd = System.currentTimeMillis();
			logger.info("途牛异步请求取消订单成功，orderId：" + orderId + ", 耗时:"+ (ansynEnd - ansynStart) + " ms");
		} catch (TuniuException e) {
			logger.info("途牛异步取消订单，返回码"+e.getMessage());
			result.setCode(e.getMessage());
		} catch (Exception e) {
			logger.info("途牛取消订单异常 orderId : " + orderId + ",e: " + e.getMessage());
			result.setCode(TuniuCommonService.RETURN_CODE_UNKNOWN_ERROR);
		}
		return result;
	}

	@Override
	public void addOrder(TuniuOrder order) {
		if (order == null) {
			logger.info("添加订单失败，订单对象为空!");
			return;
		}

		List<TuniuPassenger> passengers = order.getPassengers();
		tuniuOrderDao.insertOrder(order);
		if (passengers != null) {
			for (TuniuPassenger passenger : passengers) {
				tuniuOrderDao.insertPassenger(passenger);
				if (passenger.getTuniuTicketType().equals(PIAO_TYPE_STUDENT)) {
					tuniuOrderDao.insertStudent(passenger);
					tuniuOrderDao.insertCpStudent(passenger);
				}
			}
		}
	}

	@Override
	public void addNotice(Notice notice, String service) {
		if (service == null || service.equals(""))
			return;

		if (service.equals("book")) {
			tuniuNoticeDao.insertBookNotice(notice);
		} else if (service.equals("out")) {
			tuniuNoticeDao.insertOutNotice(notice);
		} else {

		}
	}

	@Override
	public TuniuOrder getOrderById(String orderId, boolean cascade) {

		Map<String, Object> queryParam = new HashMap<String, Object>();
		queryParam.put("orderId", orderId);
		TuniuOrder order = tuniuOrderDao.selectOneOrder(queryParam);

		if (cascade && order != null) {
			List<TuniuPassenger> passengers = tuniuOrderDao.selectPassengers(queryParam);
			order.setPassengers(passengers);
		}
		return order;
	}

	@Override
	public TuniuPassenger getPassengerById(String cpId) {
		Map<String, Object> queryParam = new HashMap<String, Object>();
		queryParam.put("cpId", cpId);
		return tuniuOrderDao.selectOnePassenger(queryParam);
	}

	@Override
	public void updateOrder(TuniuOrder order, boolean cascade) {
		tuniuOrderDao.updateOrder(order);
		if (cascade) {
			List<TuniuPassenger> passengers = order.getPassengers();
			if (passengers != null) {
				for (TuniuPassenger passenger : passengers) {
					tuniuOrderDao.updatePassenger(passenger);
				}
			}
		}
	}

	@Override
	public void sendBookOrder(TuniuOrder order, Notice notice) {
		logger.info("途牛异步请求出票系统占座，orderId：" + order.getOrderId());
		Map<String, String> params = new HashMap<String, String>();
		
		params.put("orderid", order.getOrderId());
		params.put("ordername", order.getOrderName());
		params.put("paymoney", order.getPayMoney().toString());//车票总价
		params.put("trainno", order.getTrainNo());
		params.put("fromcity", order.getFromCity());
		params.put("tocity", order.getToCity());
		params.put("traveltime", DateUtil.dateToString(order.getTravelDate(), DateUtil.DATE_FMT1));
		params.put("outtickettype", "11");//出票方式11、电子票 22、配送票
		params.put("channel", "tuniu");//合作商编号
		params.put("username", order.getUserName());//12306账号
		params.put("password", order.getUserPassword());//12306密码
		String ispay = "00";
		//订单类型 1、先预订后支付 2 先支付后预订
		if(order.getOrderType().equals(TYPE_BOOK_OUT)) {
			ispay = "11";
		} else if(order.getOrderType().equals(TYPE_OUT_BOOK)) {
			ispay = "00";
		}
		params.put("ispay", ispay);	//是否支付；00：是；11：否
		StringBuilder builder = new StringBuilder();
		List<TuniuPassenger> passengers = order.getPassengers();
		for(TuniuPassenger passenger : passengers) {
			builder.append(passenger.getCpId()).append("|")
				.append(passenger.getUserName()).append("|")
				.append(passenger.getTicketType()).append("|")
				.append(passenger.getIdsType()).append("|")
				.append(passenger.getUserIds()).append("|")
				.append(passenger.getSeatType()).append("|")
				.append(passenger.getPayMoney());
			builder.append("#");
		}
		builder.setLength(builder.length() - 1);
		params.put("seattrains", builder.toString());
		params.put("backurl", TuniuConstant.CP_OUT_TICKET_CALLBACK);
		
		params.put("seattype", passengers.get(0).getSeatType());
		//备选无座
		if(order.getHasSeat() == 0){
			params.put("extseattype", passengers.get(0).getSeatType() + "#9,0.0");
		}
		
		Integer isChooseSeats=order.getIsChooseSeats(); //是否选座
		if(1==isChooseSeats) {
			params.put("choose_seats",order.getChooseSeats()); //选座信息，例如：1A1B2C2F
		}
		
		logger.info("orderid:"+order.getOrderId()+",isChooseSeats:"+isChooseSeats+",choose_seats:"+order.getChooseSeats());
		
		
		/* 处理请求返回结果对象 */
		OutTicketNotifier notifier = (OutTicketNotifier) BeanUtil.getBean("outTicketNotifier");
		notifier.setNotice(notice);
		notifier.setParameters(params);
		notifier.setService(NoticeObserver.BOOK);
		/* 请求线程 */
		SimpleRequest request = new SimpleRequest(SimpleRequest.METHOD_POST,
				TuniuConstant.CP_OUT_TICKET_URL, notifier);
		
		TuniuThreadPool.cpNoticeThreadSubmit(request);
	}

	@Override
	public void sendOutOrder(TuniuOrder order, Notice notice) {
		logger.info("途牛异步请求出票系统出票/取消，orderId：" + order.getOrderId());
		Double buyMoney = order.getBuyMoney();
		Map<String, String> params = new HashMap<String, String>();
		params.put("order_id", order.getOrderId());
		params.put("pay_money", buyMoney.toString());
		/* 处理请求返回结果对象 */
		OutTicketNotifier notifier = (OutTicketNotifier) BeanUtil.getBean("outTicketNotifier");
		notifier.setNotice(notice);
		notifier.setParameters(params);
		notifier.setService(NoticeObserver.OUT);
		/* 请求线程 */
		SimpleRequest request = new SimpleRequest(SimpleRequest.METHOD_POST,
				TuniuConstant.CP_PAY_URL, notifier);
		
		TuniuThreadPool.cpNoticeThreadSubmit(request);
	}

	@Override
	public void sendCancelOrder(TuniuOrder order, Notice notice) {
		logger.info("途牛异步请求出票系统取消订单，orderId：" + order.getOrderId());
		Map<String, String> params = new HashMap<String, String>();
		params.put("order_id", order.getOrderId());
		/* 处理请求返回结果对象 */
		OutTicketNotifier notifier = (OutTicketNotifier) BeanUtil.getBean("outTicketNotifier");
		notifier.setNotice(notice);
		notifier.setParameters(params);
		notifier.setService(NoticeObserver.CANCEL);
		/*请求线程*/
		SimpleRequest request = new SimpleRequest(SimpleRequest.METHOD_POST,TuniuConstant.CP_CANCEL_URL, notifier);
		TuniuThreadPool.cpNoticeThreadSubmit(request);
	}
	
	@Override
	public void callbackBookOrder(TuniuOrder order, Notice notice) {
		String orderId = order.getOrderId();
		StringBuilder builder = new StringBuilder();
		
		String returnCode = null;
		for(TuniuPassenger passenger : order.getPassengers()) {
			/*处理乘客车票输出信息*/
			String tuniuIdsTypeId = passenger.getTuniuIdsType();
			String tuniuTicketTypeId = passenger.getTuniuTicketType();
			String tuniuSeatTypeId = passenger.getTuniuSeatType();
			
			if(STATUS_BOOK_SUCCESS.equals(order.getOrderStatus())||STATUS_OUT_SUCCESS.equals(order.getOrderStatus())||STATUS_WAIT_PAY.equals(order.getOrderStatus())) {
				builder.setLength(0);
				String trainBox = passenger.getTrainBox() + "车厢";
				String seatNo = passenger.getSeatNo();
				builder.append(trainBox)
				.append(",")
				.append(seatNo);
				
				String cxin = builder.toString();
				passenger.setCxin(cxin);
				passenger.setPayMoney(passenger.getBuyMoney());
			} else {
				passenger.setCpId(null);
				order.setOrderSuccess(false);
			}
			
			passenger.setPassportTypeName(tuniuIdsName(tuniuIdsTypeId));
			passenger.setZwName(tuniuSeatName(tuniuSeatTypeId));
			passenger.setPiaoTypeName(tuniuTicketName(tuniuTicketTypeId));
		}
		if(STATUS_BOOK_SUCCESS.equals(order.getOrderStatus()) || STATUS_OUT_SUCCESS.equals(order.getOrderStatus())||STATUS_WAIT_PAY.equals(order.getOrderStatus())) {
			order.setOrderSuccess(true);
			returnCode = TuniuCommonService.RETURN_CODE_SUCCESS;
			order.setOutFailReason("");
		} else {
			order.setOrderSuccess(false);
			returnCode = errorReturnCode(order.getOutFailReason());
		}
		/*toTime或fromTime为空*/
		if(order.getToTime() == null || order.getFromTime() == null) {
			try {
				adaptTime(order);
			} catch (Exception e) {
				e.printStackTrace();
			}
//			if(order.getToTime() == null || order.getFromTime() == null) {
//				logger.info("途牛补全到达时间查询异常,终止book回调,orderId : " + order.getOrderId());
//				return;
//			}
		}
		
		order.setVendorOrderId(orderId);//合作订单id和途牛订单id保持一致
		
		try {
			/*data进行64base加密之前的json*/
			String dataJson = JacksonUtil.generateJson(order);
			logger.info(order.getOrderId()+"占座回调参数："+dataJson);
			AsynchronousOutput out = new AsynchronousOutput();
			out.setData(dataJson);
			out.setReturnCode(returnCode);
			String errorMsg = errorReturnReason(order.getOutFailReason());
			if(order.getOutFailReason()!=null&&order.getOutFailReason().equals("12")){
				if(order.getPassengers().size()==1){
					errorMsg = order.getPassengers().get(0).getUserName()+"涉嫌冒用";
				
				}else{
					//信息冒用返回哪个乘客信息冒用 ,从操作日志表中获得
					Map<String,Object> paramMap = new HashMap<String,Object>();
					paramMap.put("orderId", orderId);
					paramMap.put("content", "%冒用%");
					String errorInfoLog =  tuniuOrderDao.selectOrderLog(paramMap);
					logger.info("途牛占座回调，信息冒用，查询日志返回结果为"+errorInfoLog);
					if(errorInfoLog!=null){
						for(TuniuPassenger cp : order.getPassengers()){
							String  fail_user_name = cp.getUserName();
							if(errorInfoLog.contains(fail_user_name)){
								errorMsg = fail_user_name+"涉嫌冒用";
								break;
							}
						}
					}
				}
				logger.info("途牛占座回调，信息冒用，错误信息为"+errorMsg);
			}
			if(order.getOutFailReason()!=null&&order.getOutFailReason().equals("8")){
				if(order.getPassengers().size()==1){
					errorMsg = order.getPassengers().get(0).getUserName()+"尚未通过身份信息核验";
				}
				else{
					//身份待核验返回哪个乘客信息待核验 ,从操作日志表中获得
					Map<String,Object> paramMap = new HashMap<String,Object>();
					paramMap.put("orderId", orderId);
					paramMap.put("content", "%尚未通过身份信息核验%");
					String log  = tuniuOrderDao.selectOrderLog(paramMap);
					if(log==null || log.equals("")){
						paramMap.put("content", "%待核验%");
						log = tuniuOrderDao.selectOrderLog(paramMap);
						if(log==null || log.equals("")){
							paramMap.put("content", "%待审核%");
							log = tuniuOrderDao.selectOrderLog(paramMap);
						}
					}
					logger.info("途牛占座回调，身份待核验，查询日志返回结果为"+log);
					if(log!=null){
						for(TuniuPassenger cp : order.getPassengers()){
							String  fail_user_name = cp.getUserName();
							if(log.contains(fail_user_name)){
								errorMsg = fail_user_name+"未通过身份信息核验";
								break;
							}
						}
					}
					
				}
				logger.info("途牛占座回调，身份待核验，错误信息为"+errorMsg);
			}
			if(order.getOutFailReason()!=null&&order.getOutFailReason().equals("10")){
				if(order.getPassengers().size()==1){
					errorMsg = order.getPassengers().get(0).getUserName()+"限制高消费";
				}else{
					//限制高消费具体到个人 ,从操作日志表中获得
					Map<String,Object> paramMap = new HashMap<String,Object>();
					paramMap.put("orderId", orderId);
					paramMap.put("content", "%限制高消费%");
					String log  = tuniuOrderDao.selectOrderLog(paramMap);
					logger.info("途牛占座回调，限制高消费，查询日志返回结果为"+log);
					if(log!=null){
						for(TuniuPassenger cp : order.getPassengers()){
							String  fail_user_name = cp.getUserName();
							if(log.contains(fail_user_name)){
								errorMsg = fail_user_name+"限制高消费";
								break;
							}
						}
					}
				}
				logger.info("途牛占座回调,限制高消费，错误信息为"+errorMsg);
			}
			out.setErrorMsg(errorMsg);
			/* 处理请求返回结果对象 */
			TuniuCallback callback = (TuniuCallback) BeanUtil.getBean("tuniuCallback");
			callback.setNotice(notice);
			callback.setAsynOutput(out);
			callback.setService(NoticeObserver.BOOK);
			/* 请求线程 */
			SimpleRequest request = new SimpleRequest(SimpleRequest.METHOD_POST,
					notice.getNotifyUrl(), callback);
			
			TuniuThreadPool.tuniuCallbackSubmit(request);
		} catch (IOException e) {
			logger.info("途牛占座回调data参数json格式化异常,orderId: " + orderId);
		}
	}

	@Override
	public void callbackOutOrder(TuniuOrder order, Notice notice) {
		String orderId = order.getOrderId();
		String status = order.getOrderStatus();
		Map<String, Object> dataMap = new HashMap<String, Object>();
	
		Map<String, Object> queryParam=new HashMap<String, Object>();
		queryParam.put("order_id", orderId);
		
		List<TicketEntrance> ticketEntrances=this.tuniuOrderDao.selectTicketEntrances(queryParam);
		logger.info("订单号："+orderId+"，检票口："+ticketEntrances);
		
		String returnCode = null;
		String service = null;
		String errorMsg = null;
		dataMap.put("vendorOrderId", orderId);//合作订单id和途牛订单id保持一致
		if(STATUS_CANCEL_SUCCESS.equals(status)) {//取消成功
			returnCode = RETURN_CODE_CANCEL_SUCCESS;
			service = NoticeObserver.CANCEL;
		}else if(STATUS_CANCEL_FAILURE.equals(status)){
			//取消失败 TODO  现有流程有问题待解决-无论取消成功或者失败都出票失败，返回了failure
			/*service = NoticeObserver.CANCEL;
			returnCode = cancelErrorReturnCode(order.getOutFailReason());
			errorMsg = cancelErrorReturnReason(order.getOutFailReason());*/
			
			returnCode = RETURN_CODE_CANCEL_SUCCESS;
			service = NoticeObserver.CANCEL;
		}
		
		else {//出票
			order.setNotifyStatus(NOTIFY_START);
			
			updateOrder(order, false);
			service = NoticeObserver.OUT;
			dataMap.put("orderId", orderId);
			if(STATUS_OUT_SUCCESS.equals(status)) {//出票成功
				returnCode = RETURN_CODE_CONFIRM_ORDER_SUCCESS;
				order.setOutFailReason("");
				dataMap.put("ticketEntrances", ticketEntrances);
			} else if(STATUS_OUT_FAILURE.equals(status)) {//出票失败
				//returnCode = RETURN_CODE_CONFIRM_FAIL;
				returnCode = confirmErrorReturnCode(order.getOutFailReason());
				errorMsg = errorReturnReason(order.getOutFailReason());
				dataMap.put("ticketEntrances", ticketEntrances);
			}
		}
		try {
			String dataJson = JacksonUtil.generateJson(dataMap);
			logger.info(orderId+",回调出票结果通知参数："+dataJson);
			AsynchronousOutput out = new AsynchronousOutput();
			
			out.setData(dataJson);
			out.setReturnCode(returnCode);
			out.setErrorMsg(errorMsg);
			/* 处理请求返回结果对象 */
			TuniuCallback callback = (TuniuCallback) BeanUtil.getBean("tuniuCallback");
			callback.setNotice(notice);
			callback.setAsynOutput(out);
			callback.setService(service);
			/* 请求线程 */
			SimpleRequest request = new SimpleRequest(SimpleRequest.METHOD_POST,
					notice.getNotifyUrl(), callback);
			
			TuniuThreadPool.tuniuCallbackSubmit(request);
		} catch (IOException e) {
			logger.info("途牛出票或取消回调data参数json格式化异常,orderId: " + orderId);
		}
	}
	
	/**
	 * 订单是否已存在
	 * 
	 * @param orderId
	 * @return
	 */
	private boolean isDuplicateOrder(String orderId) {
		return tuniuOrderDao.selectOrderCount(orderId) > 0;
	}
	
	/**
	 * 用传入参数封装订单对象
	 * 
	 * @param parameter
	 * @return
	 * @throws TuniuOrderException
	 */
	private TuniuOrder tuniuOrder(Parameter parameter)
			throws TuniuOrderException {

		StringBuilder builder = new StringBuilder();
		TuniuOrder order = null;

		if (parameter != null) {
			/* 订单对象封装 */
			order = new TuniuOrder();
			String orderId = parameter.getString("orderId");// 订单号
			String trainNo = parameter.getString("cheCi");// 车次
			String fromCity = parameter.getString("fromStationName");// 出发城市
			String toCity = parameter.getString("toStationName");// 到达城市
			String trainDate = parameter.getString("trainDate");// 乘车日期
			Date _trainDate = DateUtil.stringToDate(trainDate,
					DateUtil.DATE_FMT1);

			Boolean hasSeat = parameter.getBoolean("hasSeat");// 是否限定有座
			List<TuniuPassenger> passengers = parameter.getList("passengers",
					TuniuPassenger.class);// 乘客信息

			if (passengers == null || passengers.size() == 0) {
				logger.info("途牛乘客信息有误，orderId：" + orderId);
				throw new TuniuOrderException(RETURN_CODE_PASSENGER_INFO_ERROR);
			}
			
			
			builder.setLength(0);
			String orderName = builder.append(fromCity).append("/").append(
					toCity).toString();

			order.setOrderId(orderId);
			order.setOrderName(orderName);
			order.setTrainNo(trainNo);
			order.setFromCity(fromCity);
			order.setToCity(toCity);
			order.setFromCityCode(parameter.getString("fromStationCode"));
			order.setToCityCode(parameter.getString("toStationCode"));
			order.setTravelDate(_trainDate);
			order.setOrderStatus(STATUS_PLACE_ORDER_SUCCESS);
			order.setContact(parameter.getString("contact"));
			order.setInsureCode(parameter.getString("insureCode"));
			order.setPhone(parameter.getString("phone"));
			order.setUserName(parameter.getString("userName"));
			order.setUserPassword(parameter.getString("userPassword"));
			order.setTicketNumber(passengers.size());
			
			Boolean isChooseSeats = parameter.getBoolean("isChooseSeats"); //是否选座, true:选, false:非选
			order.setIsChooseSeats(isChooseSeats!=null&&isChooseSeats?1:0);
			order.setChooseSeats(parameter.getString("chooseSeats")); //选座信息
			
			double payMoney = 0.0;
			/* 乘客车票信息处理 */
			for (TuniuPassenger passenger : passengers) {
				passenger.setOrderId(orderId);
				passenger.setCpId(IDGenerator.generateID("TN_"));
				String tuniuIdsType = passenger.getTuniuIdsType();
				String tuniuSeatType = passenger.getTuniuSeatType();
				String tuniuTicketType = passenger.getTuniuTicketType();
				passenger.setIdsType(sysIdsType(tuniuIdsType));
				passenger.setSeatType(sysSeatType(tuniuSeatType));
				passenger.setTicketType(sysTicketType(tuniuTicketType));
				payMoney += passenger.getPayMoney();
				if(!tuniuSeatType.equals("1")&&!tuniuSeatType.equals("O")){
					hasSeat = true;
				}
			}
			order.setPayMoney(payMoney);
			order.setHasSeat(hasSeat != null && hasSeat ? 1 : 0);//1.有座 0.无座
			order.setPassengers(passengers);
		}

		return order;
	}

	/**
	 * 封装通知对象
	 * 
	 * @param orderId
	 * @param bookCallBackUrl
	 * @param outCallBackUrl
	 * @return
	 */
	private Notice tuniuNotice(String orderId, String callBackUrl) {
		if (orderId == null || orderId.equals(""))
			return null;

		Notice notice = new Notice();
		notice.setOrderId(orderId);
		if (callBackUrl != null && !callBackUrl.equals(""))
			notice.setNotifyUrl(callBackUrl);

		notice.setCpNotifyStatus(NOTIFY_PREPARED);
		return notice;
	}
	
	/**
	 * 失败错误匹配返回码
	 * @param errorInfo
	 * @return
	 */
	private String errorReturnCode(String errorInfo) {
		if(StringUtils.isEmpty(errorInfo))
			return RETURN_CODE_NO_REMAINDER_TICKETS;
		Integer valueCode;
		try {
			valueCode = Integer.valueOf(errorInfo);
		} catch (NumberFormatException e) {
			return RETURN_CODE_NO_REMAINDER_TICKETS;
		}
		/* 19e失败原因：
			1、所购买的车次坐席已无票	2、身份证件已经实名制购票3、票价和12306不符 4、乘车时间异常5、证件错误 6、用户要求取消订单 
			7、未通过12306实名认证 8、乘客身份信息待核验9、系统异常11、超时未支付10、高消费限制失败12、信息冒用
	 		21-----传入12306账号未进行手机核验  
			22-----传入12306账号 用户达上限  
			23-----传入12306账号中存在未完成订单
			24-----传入12306账号取消次数过多 
			25---账号待核验 
			26---用户不存在  
			27---密码错误  
			28— 不存在未支付订单或该订单状态不可支付 
			29---该车次未到起售时间
			30---暂未查询到该车次信息或车次已经停运
			31---账号已被锁定
			37---距离开车时间太近
		 * */
		switch (valueCode) {
		case 1: return "110000";
		case 2: return "110002";
		case 5: return "110001";
		case 8: return "110003";
		case 10: return "110012";
		case 12: return "110005";
		case 21: return "110006";
		case 22: return "110011";
		case 23: return "110013";
		case 24: return "110014";
		case 25: return "110006";
		case 26: return "110022";
		case 27: return "110022";
		case 29: return "110017";
		case 30: return "110021";
		case 31: return "110023";
		case 37: return "110004";
		default:
			return "110000";
		}
	}
	/**
	 * 出票失败错误匹配返回码
	 * @param errorInfo
	 * @return
	 */
	private String confirmErrorReturnCode(String errorInfo) {
		if(StringUtils.isEmpty(errorInfo))
			return RETURN_CODE_CONFIRM_FAIL;
		Integer valueCode;
		try {
			valueCode = Integer.valueOf(errorInfo);
		} catch (NumberFormatException e) {
			return RETURN_CODE_CONFIRM_FAIL;
		}
		/* 19e失败原因：
		1、所购买的车次坐席已无票	2、身份证件已经实名制购票3、票价和12306不符 4、乘车时间异常5、证件错误 6、用户要求取消订单 
		7、未通过12306实名认证 8、乘客身份信息待核验9、系统异常11、超时未支付10、高消费限制失败12、信息冒用
 		21-----传入12306账号未进行手机核验  
		22-----传入12306账号 用户达上限  
		23-----传入12306账号中存在未完成订单
		24-----传入12306账号取消次数过多 
		25---账号待核验 
		26---用户不存在  
		27---密码错误  
		28— 不存在未支付订单或该订单状态不可支付 
		29---该车次未到起售时间
		30---暂未查询到该车次信息或车次已经停运
		31---账号已被锁定
		38--支付超时，12306已经自动取消，出票失败
	 * */
		switch (valueCode) {
		case 27: return "120006";
		case 28: return "120005";
		case 31: return "120007";
		case 38: return "120002";
		default:
			return "120000";

		}
	}
	/**
	 * 取消失败错误码
	 * @param errorInfo
	 * @return
	 */
	private String cancelErrorReturnCode(String errorInfo) {
		if(StringUtils.isEmpty(errorInfo))
			return RETURN_CODE_CANCEL_FAIL;
		Integer valueCode;
		try {
			valueCode = Integer.valueOf(errorInfo);
		} catch (NumberFormatException e) {
			return RETURN_CODE_CANCEL_FAIL;
		}
		/* 19e失败原因：
		31---账号已被锁定
		32---已出票，不可取消
		33---当前订单状态不可取消
		34---账号密码错误
	 * */
		switch (valueCode) {
		case 31: return "130009";
		case 32: return "130000";
		case 33: return "130005";
		case 27: return "130008";
		default:
			return "130001";
		}
	}
	/**
	 * 取消失败原因
	 * @param errorInfo
	 * @return
	 */
	private String cancelErrorReturnReason(String errorInfo) {
		if(StringUtils.isEmpty(errorInfo))
			return "取消失败";
		Integer valueCode;
		try {
			valueCode = Integer.valueOf(errorInfo);
		} catch (NumberFormatException e) {
			return "取消失败";
		}
		/* 19e失败原因：
		31---账号已被锁定
		32---已出票，不可取消
		33---当前订单状态不可取消
		34---账号密码错误
	 * */
		switch (valueCode) {
		case 31: return "账号已被锁定";
		case 32: return "已出票，不可取消";
		case 33: return "当前订单状态不可取消";
		case 27: return "账号密码错误";
		default:
			return "取消失败";
		}
	}
	
	/**
	 * 失败错误匹配返回信息
	 * @param errorInfo
	 * @return
	 */
	private String errorReturnReason(String errorInfo) {
		if(StringUtils.isEmpty(errorInfo))
			return null;
		Integer valueCode;
		try {
			valueCode = Integer.valueOf(errorInfo);
		} catch (NumberFormatException e) {
			return null;
		}
		/* 19e失败原因：
			1、所购买的车次坐席已无票	2、身份证件已经实名制购票3、票价和12306不符 4、乘车时间异常5、证件错误 6、用户要求取消订单 
			7、未通过12306实名认证 8、乘客身份信息待核验9、系统异常11、超时未支付10、高消费限制失败12、信息冒用
		 */
		switch (valueCode) {
		case 1: return "所购买的车次坐席已无票";
		case 2: return "行程冲突";
		case 5: return "乘客信息有误";
		case 8: return "乘客身份信息待核验";
		case 10: return "限制高消费";
		case 12: return "乘客身份信息冒用";
		case 21: return "12036账号未通过手机核验";
		case 22: return "12306账号所添加的常旅数量已达到上限";
		case 23: return "存在未完成订单";
		case 24: return "取消次数过多";
		case 25: return "您的账号尚未通过身份信息核验，不可购票，详见《铁路互联网购票身份核验须知》。 ";
		case 26: return "账号用户名不存在";
		case 27: return "账号密码错误";
		case 28: return "不存在未支付订单或该订单状态不可支付";
		case 29: return "该车次未到起售时间，请稍后再试";
		case 30: return "12306暂未查询到该车次信息或车次已经停运";
		case 31: return "12306账号已被锁定，请稍后再试";
		case 32: return "已出票，不可取消";
		case 33: return "当前订单状态不可取消";
		case 37: return "距离开车时间太近";
		case 38: return "支付超时，12306已经自动取消，出票失败";
		default:
			return "所购买的车次坐席已无票";
		}
	}
	
	/**
	 * 补全时间
	 * @param order
	 */
	private void adaptTime(TuniuOrder order) {

		String trainDate = order.get_travelDate();
		
		String startTime = null;
		String arriveTime = null;
		//针对没有to_time采用数据库补全
		/*数据库补全start*/
		Map<String, Object> queryParam = new HashMap<String, Object>();
		queryParam.put("trainCode", order.getTrainNo());
		
		queryParam.put("name", order.getFromCity());
		Station fromStation = tuniuCommonDao.selectOneStation(queryParam);
		queryParam.put("name", order.getToCity());
		Station toStation = tuniuCommonDao.selectOneStation(queryParam);
		String arrive_days = null;
		
		if(fromStation == null || toStation == null) {
			logger.info(order.getOrderId()+",途牛-数据库时间补全失败!");
		} else {
			startTime = fromStation.getStartTime();
			arriveTime = toStation.getArriveTime();
			logger.info(order.getOrderId() + " 途牛数据库时间补全成功,startTime : " + startTime + " ,arriveTime : " + arriveTime);
		}
		/*数据库补全end*/
		
		if(StringUtils.isEmpty(startTime) || StringUtils.isEmpty(arriveTime)) {
			//针对没有to_time采用查询补全
			/*查询补全start*/
			Map<String,String> paramMap=new HashMap<String, String>();
			
			String url = config.getProperty("queryTicket");
			paramMap.put("channel", "ext_tuniu");
			paramMap.put("from_station", order.getFromCity());
			paramMap.put("arrive_station", order.getToCity());
			paramMap.put("travel_time", trainDate);
			paramMap.put("purpose_codes","ADULT");
			/*paramMap.put("train_code","");*/
			String params;
			params = UrlFormatUtil.createUrl("", paramMap);
			logger.info(order.getOrderId()+"途牛发起余票查询: " + paramMap.toString() + " ,url : " + url);
			String result = HttpUtil.sendByPost(url, params, "UTF-8");
			logger.info(order.getOrderId()+"途牛补全返回结果result : " + result);
		    
			String runtime = null;
		    if(result==null||result.equalsIgnoreCase("STATION_ERROR")||result.equalsIgnoreCase("ERROR")){
				result = HttpUtil.sendByPost(url, params, "UTF-8");
				if(result==null||result.equalsIgnoreCase("STATION_ERROR")||result.equalsIgnoreCase("ERROR")){
					result = HttpUtil.sendByPost(url, params, "UTF-8");
				}
			}
			if(result==null||result.equalsIgnoreCase("STATION_ERROR")||result.equalsIgnoreCase("ERROR")){
				logger.info(order.getOrderId()+"途牛book通知,针对没有to_time采用查询补全_查询失败");
				return;
			}else{
				
				try {
					JSONObject dataTicket=JSONObject.fromObject(result);
					JSONArray arr =dataTicket.getJSONArray("datajson");
					int index = arr.size();
					for (int i = 0; i < index; i++) {
						if (arr.getJSONObject(i).get("station_train_code").equals(order.getTrainNo())) {
							runtime = arr.getJSONObject(i).getString("lishiValue");
							startTime = arr.getJSONObject(i).getString("start_time");
							arriveTime = arr.getJSONObject(i).getString("arrive_time");
							arrive_days = arr.getJSONObject(i).getString("day_difference");
							break;
						}
					}
				} catch (Exception e) {
					logger.info(order.getOrderId() +"途牛时间补全查询异常, e: " + e.getMessage());
				}
				
			}
			/*查询补全end*/
		}
		
		if(StringUtils.isEmpty(startTime) || StringUtils.isEmpty(arriveTime)) {
			logger.info(order.getOrderId() +"途牛book通知,针对没有to_time采用查询补全_查询失败,未匹配到结果");
			return;
		} else {
			Date startDate = DateUtil.stringToDate(trainDate + " " + startTime, "yyyy-MM-dd HH:mm");
			Date arriveDate = DateUtil.stringToDate(trainDate + " " + arriveTime, "yyyy-MM-dd HH:mm");
			order.setFromTime(order.getFromTime() == null ? startDate : order.getFromTime());
			order.setToTime(order.getToTime() == null ? arriveDate : order.getToTime());
			
			logger.info(order.getOrderId() +"更新订单到达出发时间："+order.getOrderId()+",原因："+order.getOutFailReason()+",状态："+order.getOrderStatus());
			//只修改时间，不修改订单状态，和原因，构造一个新的order
			TuniuOrder order2=new TuniuOrder();
			order2.setOrderId(order.getOrderId());
			order2.setFromTime(order.getFromTime());
			order2.setToTime(order.getToTime());
			//更新补全的时间
			tuniuOrderDao.updateOrder(order2);
		}
		
	}

	@Override
	public Result searchStatusFrom12306(Parameter parameter) {
		TuniuResult result = new TuniuResult ();
		logger.info("途牛获取订单信息中的车票状态信息开始");
		String orderId = parameter.getString("orderId");
		String orderNumber = parameter.getString("orderNumber");//火车票取票号
		String userName = parameter.getString("userName");
		String password = parameter.getString("password");
		JSONArray ticket = parameter.getJSONArray("ticket");
		JSONArray resultTickets = new JSONArray();
		try {
			/*检查业务参数*/
			if("".equals(orderId)||"".equals(orderNumber)) {
				logger.info("途牛获取订单信息中的车票状态 ERROR,参数有空order_id:"+orderId);
				throw new TuniuCommonException(TuniuCommonService.RETURN_CODE_PARAM_ERROR);
			}
			/*查询订单*/
			Map<String,Object> orderParam = new HashMap<String,Object>();
			orderParam.put("orderId", orderId);
			TuniuOrder orderInfo = tuniuOrderDao.selectOneOrder(orderParam);
			if(orderInfo==null){
				logger.info("途牛获取订单信息中的车票状态，订单不存在，orderId:"+orderId);
				throw new TuniuCommonException(RETURN_CODE_SEARCHSTATUS_ORDERNOTEXIST);
			}
			if(!orderInfo.getOutTicketBillno().equals(orderNumber)){
				logger.info("途牛获取订单信息中的车票状态，取票号为："+orderNumber+"，orderId:"+orderId);
				throw new TuniuCommonException(TuniuCommonService.RETURN_CODE_PARAM_ERROR);
			}
			//查询车票信息
			if(ticket!=null && ticket.size()>0){
				for(int i=0;i<ticket.size();i++){
					//查询车票信息
					JSONObject jsonPass= (JSONObject) ticket.get(i);
					String ticketNo = jsonPass.getString("ticketNo");
					Map<String, Object> passParam = new HashMap<String, Object>();
					passParam.put("cpId", ticketNo);
					TuniuPassenger passenger = tuniuOrderDao.selectOnePassenger(passParam);
					if(passenger!=null){
						jsonPass.put("cert_no", passenger.getUserIds().toUpperCase());
					}else{
						logger.info("途牛获取订单信息中的车票状态 ERROR,ticketNo有误查不到乘客信息，order_id:"+orderId);
						throw new TuniuCommonException(TuniuCommonService.RETURN_CODE_PARAM_ERROR);
					}
				}
			}else{
				ticket = new JSONArray();
				List<TuniuPassenger> passengers = tuniuOrderDao.selectPassengers(orderParam);
				for(int i=0;i<passengers.size();i++){
					JSONObject jsonPass= new JSONObject();
					String ticketNo = passengers.get(i).getCpId();
					Map<String, Object> passParam = new HashMap<String, Object>();
					passParam.put("cpId", ticketNo);
					TuniuPassenger passenger = tuniuOrderDao.selectOnePassenger(passParam);
					jsonPass.put("cert_no", passenger.getUserIds().toUpperCase());
					jsonPass.put("ticketNo", ticketNo);
					ticket.add(jsonPass);
				}
			}
		/*	//订单状态是用户要求取消或超时未支付
			if(orderInfo.getOrderStatus().equals(STATUS_CANCEL_SUCCESS)||(orderInfo.getOrderStatus().equals(STATUS_OUT_FAILURE)&&(orderInfo.getOutFailReason().equals("11")||orderInfo.getOutFailReason().equals("6")))){
				logger.info("查询订单状态，订单已取消。orderId"+orderId);
				for(int i=0;i<ticket.size();i++){
					JSONObject ticketInfo= (JSONObject) ticket.get(i);
					JSONObject resultTicket= new JSONObject();
					JSONObject statusInfo= new  JSONObject(); 
					statusInfo.put("operationTime", orderInfo.getOptTime());
					statusInfo.put("status", "其他");
					statusInfo.put("channel", "app");
					statusInfo.put("msg", "订单已取消");
					resultTicket.put("statusInfo", statusInfo);
					resultTicket.put("ticketNo", ticketInfo.getString("ticketNo"));
					resultTickets.add(resultTicket);
				}
				
			}else{*/
			//查询订单状态
			Account account = tuniuRefundDao.selectOneAccount(orderParam);
			JSONObject queryParam = new JSONObject();
			queryParam.put("orderId", orderId);
			queryParam.put("orderNumber", orderNumber);
			queryParam.put("userName",account.getUsername());
			queryParam.put("userPassword", account.getPassword());
			queryParam.put("beginTime",DateUtil.dateToString(orderInfo.getCreateTime(), DateUtil.DATE_FMT1));
			queryParam.put("endTime", DateUtil.dateToString(new Date(), DateUtil.DATE_FMT1));
			queryParam.put("ticket", ticket);
			logger.info("查询订单状态 ,orderId:"+orderId+"，参数："+queryParam.toString());
			Date datebegin = new Date();
			String searchResult = HttpUtil.sendByPost(config.getProperty("search_status"), queryParam.toString(), "UTF-8");
			logger.info("查询订单状态 ,orderId:"+orderId+",查询结果："+searchResult);
			Date dateend= new Date();
			logger.info("查询耗时："+(dateend.getTime()-datebegin.getTime()));
			//解析返回参数
			JSONObject resultObject = JSONObject.fromObject(searchResult);
			resultTickets = (JSONArray) resultObject.get("ticket");
			String flag = resultObject.getString("flag");
			if(flag.equals("success")){
				logger.info("途牛获取订单信息中的车票状态!!!!查询成功，orderId:"+orderId);
				//查询成功
				for(int i=0;i<resultTickets.size();i++){
					JSONObject resultTicket= (JSONObject) resultTickets.get(i);
					String operationTime = resultTicket.getString("operationTime");
					String originStatus = resultTicket.getString("status");
					String status = changeStatus(originStatus);
					String channel = resultTicket.getString("channel");
					JSONObject statusInfo= new  JSONObject(); 
					statusInfo.put("operationTime", operationTime);
					statusInfo.put("status", status);
					statusInfo.put("channel", channel);
					statusInfo.put("msg", "");
					if(status.contains("其他")){
						statusInfo.put("msg", originStatus);
					}
					resultTicket.put("statusInfo", statusInfo);
					resultTicket.remove("operationTime");
					resultTicket.remove("status");
					resultTicket.remove("channel");
					resultTicket.remove("cert_no");
				}
				//返回
				result.putData("orderId", orderId);
				result.putData("orderNumber", orderNumber);
				result.putData("vendorOrderId", orderId);
				result.putData("ticket", resultTickets);
				result.putData("msg", "");
			}
			else{
				//查询失败
				logger.info("途牛获取订单信息中的车票状态!!!!查询失败，orderId:"+orderId);
				result.setCode(RETURN_CODE_SEARCHSTATUS_ORDERNOTEXIST);
			}
		} 
		catch(TuniuCommonException e){
			logger.info("途牛获取订单信息中的车票状态失败"+e);
			result.setCode(e.getMessage());
			e.printStackTrace();	
		}
		catch (Exception e) {
			logger.info("途牛获取订单信息中的车票状态异常"+e);
			result.setCode(RETURN_CODE_SEARCHSTATUS_ERROR);
			e.printStackTrace();
		} 
		return result;

	}
	/**
	 * 将12306订单状态转化为途牛订单状态
	 * @param originStatus
	 * @return：支付成功/办理改签/改签成功/办理退票/制票成功…
	 */
	private String changeStatus(String originStatus){
		String status="";
		if(originStatus.contains("变更到站待支付")){
			status="变更到站中";
		}else if(originStatus.contains("变更到站中")){
			status="变更到站中";
		}else if(originStatus.contains("变更到站")){
			status="变更到站";
		}else if(originStatus.contains("检票出站")){
			status="检票出站";
		}else if(originStatus.contains("检票进站")){
			status="检票进站";
		}else if(originStatus.contains("改签中")){
			status="改签中";
		}else if(originStatus.contains("待支付")){
			status="占位成功";
		}else if(originStatus.contains("已支付")){
			status="支付成功";
		}else if(originStatus.contains("已出票")){
			status="制票成功";
		}else if(originStatus.contains("已改签")){
			status="办理改签";
		}else if(originStatus.contains("已退票")){
			status="办理退票";
		}else{
			status="其他";
		}
		return status;
	}
	
	/*---------------------------dqh2015-11-23---------------------------------------------------*/
	
	//途牛获取验证码时的下单
	@Override
	@Deprecated
	public Result captchaTrainBook(Parameter parameter,String accountUrl,String captchaResultUrl,String robotUrl,int num) {
		String orderId = parameter.getString("requestId");// 途牛订单号 
//		String callBackUrl = parameter.getString("callBackUrl");// 回调地址

		logger.info("途牛同步[获取验证码占座]开始，requestId：" + orderId);
		Result result = new TuniuResult(); 
		result.putData("requestId", orderId);
		try {
			if(isDuplicateOrder(orderId)&&num==0) {//查询是否已经入库
				result.setCode("250011");
				logger.info("途牛同步获取验证码请求[占座]重复下单, orderId : " + orderId);
			} else {
				long ansynStart = System.currentTimeMillis();
				/* 新订单入库 */
				TuniuOrder order = captchaOrder(parameter);//封装数据
				if(order == null) {
					logger.info("途牛请求验证码占座，传参有误");
					throw new TuniuCommonException(RETURN_CODE_PARAM_ERROR);
				}
				if(num==0)addOrder(order);//插入预订表  
				StringBuffer param1 = new StringBuffer();
				
				if(order.getUserName()!=""&&order.getUserPassword()!=""){
					//用于发送lua机器人
					param1.append(order.getUserName()).append("|").append(order.getUserPassword()).append("|"); 
				}else {
					//获取19e出票系统的12306账号密码
					StringBuffer accountBuffer = new StringBuffer(); 
					accountBuffer.append("channel=").append("19e");
					String account = HttpUtil.sendByPost(accountUrl, accountBuffer.toString(), "UTF-8"); 
					JSONObject accountObject = JSONObject.fromObject(account);
					logger.info("获取验证码12306账号返回:"+account);
					if (accountObject.has("success")&&accountObject.getBoolean("success")) {
						JSONObject dataObject = accountObject.getJSONObject("data");
						String userName = dataObject.get("username").toString();
						String password = dataObject.get("password").toString();
						param1.append(userName).append("|").append(password).append("|");
					}
				}
				
				//获取车站三字码
				String fromCityCode ="";
				String toCityCode ="";
				StringBuffer cityBuffer = new StringBuffer(); 
				cityBuffer.append("commond=").append("cityCode")
				.append("&fromCity=").append(order.getFromCity())
				.append("&toCity=").append(order.getToCity());
				String cityCode = HttpUtil.sendByPost(captchaResultUrl, cityBuffer.toString(), "UTF-8"); 
				String[] cityCodeList = cityCode.split("\\|");
				for (int i = 0; i < cityCodeList.length; i++) {
					logger.info("获取验证码出发到达站三字码:"+cityCodeList[i]);
					String[] lhList = cityCodeList[i].split(",");
					if (lhList[0].equals(order.getFromCity())) {
						fromCityCode = lhList[1];
					}else if (lhList[0].equals(order.getToCity())) {
						toCityCode = lhList[1];
					}
				}
				//访问lua机器人
				StringBuffer luaParams = new StringBuffer();  
				param1.append(orderId).append("|").append(order.getFromCity())
				.append("|").append(order.getToCity()).append("|")
				.append(DateUtil.dateToString(order.getTravelDate(), DateUtil.DATE_FMT1)).append("|")//parameter.getString("trainDate")
				.append(order.getTrainNo()).append("|").append("0").append("|")
				.append("1234").append("|")//work_id
				.append("{\"order_button_time\":0,\"login_time\":0,\"order_time\":0,\"account_time\":0,\"adduser_time\":0,\"getuser_time\":0,\"submit_time\":0}")
				.append("|").append("s9eyi3p5evmc9g83").append("|")//设备编号
				.append(fromCityCode).append("|").append(toCityCode).append("|").append("0");//车站三字码
				//总参数拼接
				luaParams.append("ScriptPath=").append("appTuniuBooking.lua")
				.append("&commond=").append("trainOrder")
				.append("&SessionID=").append(String.valueOf(System.currentTimeMillis()))
				.append("&Timeout=").append("1000000")
				.append("&ParamCount=").append(order.getPassengers().size()+1)
				.append("&Param1=").append(URLEncoder.encode(param1.toString(),"UTF-8"));
				
				for (int i = 0; i < order.getPassengers().size(); i++) {
					luaParams.append("&Param").append(i+2).append("=");
					//获取乘客信息
					TuniuPassenger passenger = order.getPassengers().get(i);
					StringBuffer param = new StringBuffer();
					param.append(passenger.getCpId()).append("|")
					.append(passenger.getUserName()).append("|")
					.append(passenger.getTicketType()).append("|")
					.append(passenger.getIdsType()).append("|")
					.append(passenger.getUserIds()).append("|")
					.append(passenger.getSeatType());
					luaParams.append(URLEncoder.encode(param.toString(),"UTF-8"));
				}
				logger.info("请求验证码机器人参数:"+luaParams);
				String luaResult = HttpUtil.sendByGet(robotUrl+"?"+luaParams.toString(),"UTF-8", "1000", "1000");//请求机器人
				System.err.println(luaResult);
				//获取验证码结果 
				StringBuffer captchaBuffer = new StringBuffer();
				captchaBuffer.append("commond=").append("getCaptcha")
				.append("&requestId=").append(orderId).append("&captchaType=").append("occupy");
				String captchaResult = "";
				logger.info("开始获取验证码图片结果:"+captchaBuffer);
				for (int i = 0; i < 40; i++) {
					captchaResult = HttpUtil.sendByPost(captchaResultUrl, captchaBuffer.toString(), "UTF-8");
					logger.info("验证码图片返回结果:"+captchaResult);
					if (!"fail".equals(captchaResult))
						break;
					else
						Thread.sleep(3000);
				}

				long ansynEnd = System.currentTimeMillis();
				result.putData("captchaType", "occupy");
				if ("fail".equals(captchaResult)) {
					result.setCode(TuniuCommonService.RETURN_CODE_UNKNOWN_ERROR);
					logger.info("途牛同步获取验证码请求[占座]失败，orderId：" + orderId + ", 耗时:"+ (ansynEnd - ansynStart) + " ms");
				}else if (captchaResult.contains("restart")) {
					if (captchaResult.contains("不存在"))
						result.setCode("250001");	
					else if (captchaResult.contains("密码输入错误"))
						result.setCode("250002");	
					else if (captchaResult.contains("锁定时间为20分钟"))
						result.setCode("250003");	
					else if (captchaResult.contains("冒用"))
						result.setCode("250004");	
					else if (captchaResult.contains("尚未通过身份信息核验"))
						result.setCode("250005");	
					else if (captchaResult.contains("上限"))
						result.setCode("250006");	
					else if (captchaResult.contains("待核验"))
						result.setCode("250007");
					else if (captchaResult.contains("手机核验"))
						result.setCode("250008");
					else if (captchaResult.contains("输入有误"))
						result.setCode("250009");
					else if (captchaResult.contains("取消次数过多"))
						result.setCode("260010");
					else if (captchaResult.contains("未完成订单"))
						result.setCode("250010"); 
					else if (captchaResult.contains("已订")||captchaResult.contains("已购买")||captchaResult.contains("预订成功"))
						result.putData("needCaptcha",false);
					else
						result.setCode(TuniuCommonService.RETURN_CODE_UNKNOWN_ERROR);
					logger.info("途牛同步获取验证码请求[占座]失败，orderId：" + orderId + ",result"+captchaResult+" 耗时:"+ (ansynEnd - ansynStart) + " ms");
				}else if("success".equals(captchaResult)){
					result.putData("needCaptcha",false);
					logger.info("途牛同步获取验证码请求[占座]成功，orderId：" + orderId + ", 耗时:"+ (ansynEnd - ansynStart) + " ms");
				}else{			  
					result.putData("needCaptcha",true);
					result.putData("file", captchaResult);
					logger.info("途牛同步获取验证码请求[获取验证码]成功，orderId：" + orderId + ", 耗时:"+ (ansynEnd - ansynStart) + " ms");
				}
			}
		} catch (TuniuException e) {
			result.setCode(e.getMessage());
		} catch (Exception e) {
			logger.info("途牛验证码预订异常 orderId : " + orderId + ",e: " + e.getMessage());
			result.setCode(TuniuCommonService.RETURN_CODE_UNKNOWN_ERROR);
			e.printStackTrace();
		}
		return result;
	}
	
	
	/**
	 * 用传入参数封装订单对象
	 * 
	 * @param parameter
	 * @return
	 * @throws TuniuOrderException
	 * @throws TuniuCommonException 
	 */
	private TuniuOrder captchaOrder(Parameter parameter)
			throws TuniuOrderException, TuniuCommonException {

		StringBuilder builder = new StringBuilder();
		TuniuOrder order = null;

		if (parameter != null) {
			/* 订单对象封装 */
			order = new TuniuOrder();
			String orderId = parameter.getString("orderId");// 订单号
			String requestId = parameter.getString("requestId");// requestId
			JSONObject busData = parameter.getJSONObject("busData");
			Parameter newParameter = new TuniuParameter(busData.toString());
//			String trainNo = newParameter.getString("cheCi");// 车次
			Object trainNoObject = newParameter.getString("cheCi").equals("")?newParameter.getInt("cheCi"):newParameter.getString("cheCi");
			String trainNo = trainNoObject.toString();
			String fromCity = newParameter.getString("fromStationName");// 出发城市
			String toCity = newParameter.getString("toStationName");// 到达城市
			String trainDate = newParameter.getString("trainDate");// 乘车日期
			Date _trainDate = DateUtil.stringToDate(trainDate,
					DateUtil.DATE_FMT1);

			Boolean hasSeat = newParameter.getBoolean("hasSeat");// 是否限定有座

			List<TuniuPassenger> passengers = newParameter.getList("passengers",
					TuniuPassenger.class);// 乘客信息

			if (passengers == null || passengers.size() == 0) {
				logger.info("途牛乘客信息有误，orderId：" + orderId);
				throw new TuniuOrderException(RETURN_CODE_PASSENGER_INFO_ERROR);
			}
			
			
			builder.setLength(0);
			String orderName = builder.append(fromCity).append("/").append(
					toCity).toString();

			order.setOrderId(requestId);
//			order.setRequestId(requestId);
			order.setOrderName(orderName);
			order.setTrainNo(trainNo);
			order.setFromCity(fromCity);
			order.setToCity(toCity);
			order.setFromCityCode(newParameter.getString("fromStationCode"));
			order.setToCityCode(newParameter.getString("toStationCode"));
			order.setTravelDate(_trainDate);
			order.setOrderStatus(STATUS_PLACE_ORDER_SUCCESS);
			order.setHasSeat(hasSeat != null && hasSeat ? 1 : 0);
			order.setContact(newParameter.getString("contact"));
//			order.setInsureCode(parameter.getString("insureCode"));//保险编号
			order.setPhone(newParameter.getString("phone"));
			order.setUserName(newParameter.getString("userName"));
			order.setUserPassword(newParameter.getString("userPassword"));
			order.setTicketNumber(passengers.size());

			double payMoney = 0.0;
			/* 乘客车票信息处理 */
			for (TuniuPassenger passenger : passengers) {
				passenger.setOrderId(orderId);
				passenger.setCpId(IDGenerator.generateID("TN_"));
				String tuniuIdsType = passenger.getTuniuIdsType();
				String tuniuSeatType = passenger.getTuniuSeatType();
				String tuniuTicketType = passenger.getTuniuTicketType();
				passenger.setIdsType(sysIdsType(tuniuIdsType));
				passenger.setSeatType(sysSeatType(tuniuSeatType));
				passenger.setTicketType(sysTicketType(tuniuTicketType));
				payMoney += passenger.getPayMoney();
			}
			order.setPayMoney(payMoney);

			order.setPassengers(passengers);
		}

		return order;
	}

	@Override
	public void callbackQueueOrder(TuniuQueueOrder notice) {
		try {
			//Map<String, Object> resultMap = new HashMap<String, Object>();
			JSONObject resultMap = new JSONObject();
			resultMap.put("account", TuniuConstant.account);
			String timestamp = DateUtil.dateToString(new Date(),TuniuConstant.TIMESTAMP_FORMAT);
			resultMap.put("timestamp", timestamp);
			resultMap.put("returnCode", "231000");
			Map<String,Object> dataMap = new HashMap<String,Object>();
			dataMap.put("orderId", notice.getOrder_id());
			dataMap.put("vendorOrderId", notice.getOrder_id());
			dataMap.put("queueNumber", notice.getQueue_number());
			String wait_time = notice.getWait_time();
			Integer time=0;
			if(wait_time!=null){
				time = new Integer(wait_time);
			}
			dataMap.put("waitTime", time/60+"分"+time%60+"秒");
			dataMap.put("msg", notice.getMsg());
			String data = "";
			if (!StringUtils.isEmpty(dataMap.toString())) {
				data = EncryptUtil.encode(JacksonUtil.generateJson(dataMap), TuniuConstant.signKey, "UTF-8");
				resultMap.put("data", data);
			}
			/* 签名 */
			Map<String, String> params = new HashMap<String, String>();
			params.put("account", TuniuConstant.account);
			params.put("timestamp", timestamp);
			params.put("returnCode", "231000");
			params.put("data", data);
			String sign = tuniuCommonService.generateSign(params,TuniuConstant.signKey);
			resultMap.put("sign", sign);
			String url = TuniuConstant.reserveStatusPushUrl;
			logger.info("post请求地址: " + url + ", 数据参数: " + dataMap.toString()+",全部参数"+resultMap.toString());
			String result = HttpUtil.sendByPost(url, JacksonUtil.generateJson(resultMap), "UTF-8");
			logger.info(notice.getOrder_id()+",请求返回结果,result:" + result);
			if(new TuniuCallback().success(result)){
				//通知成功
				logger.info("途牛排队订单通知成功，orderId,"+notice.getOrder_id());
				notice.setNotify_num(notice.getNotify_num()+1);
				notice.setNotify_status(3);
				
			}else{
				logger.info("途牛排队订单通知失败，orderId,"+notice.getOrder_id());
				//通知失败
				if(notice.getNotify_num()+1==5){
					notice.setNotify_num(notice.getNotify_num()+1);
					notice.setNotify_status(4);
				}else{
					notice.setNotify_num(notice.getNotify_num()+1);
				}
			}
			tuniuNoticeDao.updateQueueNotice(notice);
			
		} catch (IOException e) {
			logger.info("途牛回调排队订单,orderId: " + notice.getOrder_id());
			
		}
		
	}

	@Override
	public void tuniuPushOrderStatus(Parameter parameter,
			SynchronousOutput synOutput) throws TuniuCommonException {

		String orderId=parameter.getString("orderId"); //订单号
		String vendorOrderId=parameter.getString("vendorOrderId"); //供应商订单号
		Integer status=parameter.getInt("status"); //1、  预定占位失败 2、  出票失败 3、  改签占位失败 4、 改签确认失败
		String msg=parameter.getString("msg");  //备注信息
		String updateTime=parameter.getString("updateTime"); //状态变更时间 ,可选字段
		String changeId=parameter.getString("changeId");  //改签流水 ,非改签为空
		
		
		//将超时订单存库
		OutimeOrderVO  outTimeOrder=new OutimeOrderVO();
		outTimeOrder.setOrder_id(orderId);
		outTimeOrder.setStatus(status);
		outTimeOrder.setMsg(msg);
		outTimeOrder.setUpdateTime(updateTime);
		outTimeOrder.setDeal_status("00");//未处理
		outTimeOrder.setChangeId(changeId); //改签流水
		
		
		
		if(null!=status&&1==status){
			logger.info(orderId+"预定占位超时告警,");
			tuniuOrderDao.inserOutimeOrderVO(outTimeOrder);
			
			CpSysLogVO log=new CpSysLogVO();
			log.setOrder_id(orderId);
			log.setOpter("tuniu_app");
			log.setOrder_optlog("途牛方推送超时订单【预定占位超时告警】,超过30分钟自动处理失败,请人工及时处理！");
			tuniuOrderDao.inserCpSysOrderLog(log);
			
			 outTimeOrder.setDeal_status("33");//告警
			 outTimeOrder.setMsg(null);//这些个字段不做更新
			 outTimeOrder.setUpdateTime(null);
			 tuniuOrderDao.updateOutTimeOrder(outTimeOrder);
			 
			
		}else if(null!=status&&2==status){
			logger.info(orderId+",出票超时告警");
			tuniuOrderDao.inserOutimeOrderVO(outTimeOrder);
			
			CpSysLogVO log=new CpSysLogVO();
			log.setOrder_id(orderId);
			log.setOpter("tuniu_app");
			log.setOrder_optlog("途牛方推送超时订单【出票超时告警】,超过30分钟自动处理失败,请人工及时处理！");
			tuniuOrderDao.inserCpSysOrderLog(log);
			
			 outTimeOrder.setDeal_status("33");//告警
			 outTimeOrder.setMsg(null);//这些个字段不做更新
			 outTimeOrder.setUpdateTime(null);
			 tuniuOrderDao.updateOutTimeOrder(outTimeOrder);
				
			
		}else if(null!=status&&3==status){
			  logger.info(orderId+"改签占位超时告警");
			  tuniuOrderDao.inserOutimeOrderVO(outTimeOrder);
			
			  TuniuChangeLogVO  changeLog=new TuniuChangeLogVO();
			  changeLog.setOrder_id(orderId);
			  changeLog.setContent("途牛方推送超时订单【改签占位超时告警】,超过30分钟自动处理失败,请人工及时处理！");
			  changeLog.setOpt_person("tuniu_app");
			  tuniuChangeDao.addChangeLog(changeLog);
			  
			  outTimeOrder.setDeal_status("33");//告警
			  outTimeOrder.setMsg(null);//这些个字段不做更新
			  outTimeOrder.setUpdateTime(null);
			  tuniuOrderDao.updateOutTimeOrder(outTimeOrder);
			
			
		}else if(null!=status&&4==status){
			 logger.info(orderId+"改签确认超时告警");
			 tuniuOrderDao.inserOutimeOrderVO(outTimeOrder);
			
			 TuniuChangeLogVO  changeLog=new TuniuChangeLogVO();
			 changeLog.setOrder_id(orderId);
			 changeLog.setContent("途牛方推送超时订单【改签确认超时告警】,超过30分钟自动处理失败,请人工及时处理！");
			 changeLog.setOpt_person("tuniu_app");
			 tuniuChangeDao.addChangeLog(changeLog);
			 
			 outTimeOrder.setDeal_status("33");//告警
			 outTimeOrder.setMsg(null);//这些个字段不做更新
			 outTimeOrder.setUpdateTime(null);
			 tuniuOrderDao.updateOutTimeOrder(outTimeOrder);
			
			
		}else if(null!=status&&5==status){
			
			logger.info(orderId+",预定占位失败,");
			tuniuOrderDao.inserOutimeOrderVO(outTimeOrder);
						 
			//修改途牛订单状态为出票失败，通知状态为完成
			//修改出票系统的订单状态，为失败，原因：超时
			Map<String, Object> queryOrderMap=new HashMap<String, Object>();
			queryOrderMap.put("orderId", orderId);
			TuniuOrder order=tuniuOrderDao.selectOneOrder(queryOrderMap);
			if(null==order){
				throw new TuniuCommonException(TuniuCommonService.RETURN_CODE_PARAM_ERROR);
			}
			order.setOrderId(orderId);
			if(TuniuOrderService.STATUS_OUT_SUCCESS.equals(order.getOrderStatus())) {//途牛管理下为出票成功,预定成功，不操作订单状态
				logger.info(orderId+",订单已出票成功,不操作订单状态-"+",订单状态："+order.getOrderStatus());
			}else{
				order.setOrderStatus(TuniuOrderService.STATUS_OUT_FAILURE);
				tuniuOrderDao.updateOrder(order);
				Map<String, Object> queryNoticeParam = new HashMap<String, Object>();
				queryNoticeParam.put("orderId", orderId);
				Notice notice=tuniuNoticeDao.selectBookOneNotice(queryNoticeParam);
				notice.setNotifyStatus(TuniuCommonService.NOTIFY_SUCCESS);
				tuniuNoticeDao.updateBookNotice(notice);
			}

			//查询出票系统的订单状态,插入一条日志
			String order_staus=tuniuOrderDao.selectCpSysOrderStaus(orderId);
			logger.info(orderId+",出票系统的订单状态:"+order_staus);
			//出票系统状态：33、 预定成功  88、支付成功   99、出票成功  10、出票失败   66、正在支付    11、正在预定
			CpSysLogVO log=new CpSysLogVO();
			log.setOrder_id(orderId);
			log.setOpter("tuniu_app");
			log.setOrder_optlog("途牛方推送超时订单【预定占位失败】,超过30分钟自动处理失败,请人工处理该订单为失败！");
			tuniuOrderDao.inserCpSysOrderLog(log);
			
			if("33".equals(order_staus)||"88".equals(order_staus)||"99".equals(order_staus)||"66".equals(order_staus)||"11".equals(order_staus)){
			   logger.info(orderId+",出票系统的订单状态:"+order_staus);
			   log.setOrder_optlog("途牛方推送超时订单【预定占位失败】,但出票系统的订单状态为成功,或正在处理,订单状态为："+order_staus+",请人工确认,以失败处理！");
			   tuniuOrderDao.inserCpSysOrderLog(log);
			   
			   outTimeOrder.setDeal_status("22");//人工处理
			   outTimeOrder.setMsg(null);//这些个字段不做更新
			   outTimeOrder.setUpdateTime(null);
			   tuniuOrderDao.updateOutTimeOrder(outTimeOrder);
			   
			}else{
				//修改出票系统订单状态为出票失败
				Map<String,String> updateCpSysMap=new HashMap<String,String>();
				updateCpSysMap.put("order_id", orderId);
				updateCpSysMap.put("order_status", "10");
				tuniuOrderDao.updateCpSysOrderStatus(updateCpSysMap);
				
				log.setOrder_optlog("途牛方推送超时订单【预定占位失败】,当前订单状态为："+order_staus+",直接修改该订单为失败！");
				tuniuOrderDao.inserCpSysOrderLog(log);
				
				outTimeOrder.setDeal_status("11");//已处理
				outTimeOrder.setMsg(null);//这些个字段不做更新
				outTimeOrder.setUpdateTime(null);
				tuniuOrderDao.updateOutTimeOrder(outTimeOrder);
			}
					
			
		}else if(null!=status&&6==status){
	
			logger.info(orderId+",出票支付失败。"); //不操作订单状态
			tuniuOrderDao.inserOutimeOrderVO(outTimeOrder);
			
			Map<String, Object> queryOrderMap=new HashMap<String, Object>();
			queryOrderMap.put("orderId", orderId);
			TuniuOrder order=tuniuOrderDao.selectOneOrder(queryOrderMap);
			if(null==order){
				throw new TuniuCommonException(TuniuCommonService.RETURN_CODE_PARAM_ERROR);
			}
			order.setOrderId(orderId);
			if(TuniuOrderService.STATUS_OUT_SUCCESS.equals(order.getOrderStatus())) {//途牛管理下为出票成功，不操作订单状态
				logger.info(orderId+",订单已出票成功,不操作订单状态-"+",订单状态："+order.getOrderStatus());
			}else{
				logger.info(orderId+",订单状态："+order.getOrderStatus()+",不操作订单表状态！！");
				
				/*order.setOrderStatus(TuniuOrderService.STATUS_OUT_FAILURE);
				tuniuOrderDao.updateOrder(order); //更新途牛管理下，订单状态为出票失败
				//查询一条出票通知记录，更新为成功
				try {
					Map<String, Object> queryNoticeParam = new HashMap<String, Object>();
					queryNoticeParam.put("orderId", orderId);
					Notice notice=tuniuNoticeDao.selectOutOneNotice(queryNoticeParam);
					notice.setNotifyStatus(TuniuCommonService.NOTIFY_SUCCESS);
					tuniuNoticeDao.updateOutNotice(notice);
				} catch (Exception e) {
					logger.info(orderId+",该订单状态："+order.getOrderStatus(),e);
				}*/
			}
				
			//查询出票系统的订单状态,插入一条日志
			String order_staus=tuniuOrderDao.selectCpSysOrderStaus(orderId);
			logger.info(orderId+",出票系统的订单状态:"+order_staus);
			//出票系统状态：33、 预定成功  88、支付成功   99、出票成功  10、出票失败   66、正在支付    11、正在预定
			CpSysLogVO log=new CpSysLogVO();
			log.setOrder_id(orderId);
			log.setOpter("tuniu_app");
			log.setOrder_optlog("途牛方推送超时订单【出票支付失败】,超过30分钟自动处理失败,请人工处理该订单为失败！");
			tuniuOrderDao.inserCpSysOrderLog(log);
			
			if("88".equals(order_staus)||"99".equals(order_staus)||"66".equals(order_staus)||"61".equals(order_staus)){
			   logger.info(orderId+",出票系统的订单状态:"+order_staus);
			   log.setOrder_optlog("1途牛方推送超时订单【出票支付失败】,当前订单状态为："+order_staus+",请人工确认,以失败处理,并退票！");
			   tuniuOrderDao.inserCpSysOrderLog(log);
			   
			   outTimeOrder.setDeal_status("22");//人工处理
			   outTimeOrder.setMsg(null);//这些个字段不做更新
			   outTimeOrder.setUpdateTime(null);
			   tuniuOrderDao.updateOutTimeOrder(outTimeOrder);
			   
			}else{
				logger.info(orderId+",出票系统的订单状态:"+order_staus+",不操作订单表状态！！");
				
				//修改出票系统订单状态为出票失败
				/*Map<String,String> updateCpSysMap=new HashMap<String,String>();
				updateCpSysMap.put("order_id", orderId);
				updateCpSysMap.put("order_status", "10");
				tuniuOrderDao.updateCpSysOrderStatus(updateCpSysMap);*/
				
				log.setOrder_optlog("2途牛方推送超时订单【出票支付失败】,当前订单状态为："+order_staus+",请人工确认,以失败处理,并退票！");
				tuniuOrderDao.inserCpSysOrderLog(log);
				
				outTimeOrder.setDeal_status("22");//人工处理
				outTimeOrder.setMsg(null);//这些个字段不做更新
				outTimeOrder.setUpdateTime(null);
				tuniuOrderDao.updateOutTimeOrder(outTimeOrder);
			}
				
			
		}else if(null!=status&&7==status){
			
			  logger.info(orderId+",改签占位失败,");
			  tuniuOrderDao.inserOutimeOrderVO(outTimeOrder);//存库记录
			  //查询出票系统中,改签单的状态：12、正在改签预订   14、改签成功等待确认  32、正在支付  34、支付成功    37、改签超时订单(途牛)
			 	  
		      Map<String, String>  changeQueryMap=new HashMap<String,String>();
			  changeQueryMap.put("orderId", orderId);
			  changeQueryMap.put("changeId", changeId);//改签表中的reqtoken
			  Map<String, Object>  changeOrderStatusMap= tuniuOrderDao.selectCpSysChangeOrderStatusByChangeId(changeQueryMap); 
			  
			  String changeOrderStatus=(String) changeOrderStatusMap.get("change_status");
			  Integer change_id =  (Integer) changeOrderStatusMap.get("change_id");
			 
			  logger.info(orderId+",出票系统的改签订单状态:"+changeOrderStatus+",change_id:"+change_id);
			  
			  TuniuChangeLogVO  changeLog=new TuniuChangeLogVO();
			  changeLog.setOrder_id(orderId);
			  changeLog.setContent("途牛方推送超时订单【改签占位失败】,超过30分钟自动处理失败,请人工处理该订单为失败！");
			  changeLog.setOpt_person("tuniu_app");
			  tuniuChangeDao.addChangeLog(changeLog);
			  
			  if("32".equals(changeOrderStatus)||"34".equals(changeOrderStatus)){//支付成功，正在支付,不做处理,否则将其改为超时订单状态
				  logger.info(orderId+",出票系统的改签订单状态:"+changeOrderStatus);
				  changeLog.setContent("途牛方推送超时订单【改签占位失败】,但出票系统中改签订单状态为成功,或正在处理,订单状态为："+changeOrderStatus+",请人工确认,以失败处理！");
				  tuniuChangeDao.addChangeLog(changeLog);
		  
			      outTimeOrder.setDeal_status("22");//人工处理
			      outTimeOrder.setMsg(null);//这些个字段不做更新
				  outTimeOrder.setUpdateTime(null);
				  tuniuOrderDao.updateOutTimeOrder(outTimeOrder);
				  	  
			  }else{
				  TuniuChangeInfo changeVo=new TuniuChangeInfo();
				  changeVo.setChange_id(change_id);
				  changeVo.setChange_status("37");//37、改签超时订单(途牛)
				  changeVo.setChange_notify_status(CHANGE_NOTIFY_SUCCESS);
				  changeVo.setChange_notify_time(DateUtil.dateToString(new Date(),DateUtil.DATE_FMT3));
				  changeVo.setChange_notify_count(10); 
				  tuniuChangeDao.updateChangeInfo(changeVo);  //修改为订单状态为：改签超时，通知状态为完成 
				  changeLog.setContent("途牛方推送超时订单【改签占位失败】,出票系统中改签订单状态："+changeOrderStatus+",直接修改为改签超时，通知状态为完成");  
				  tuniuChangeDao.addChangeLog(changeLog);
				  
				  outTimeOrder.setDeal_status("11");//已处理
				  outTimeOrder.setStatus(status);
				  outTimeOrder.setMsg(null);//这些个字段不做更新
				  outTimeOrder.setUpdateTime(null);
				  tuniuOrderDao.updateOutTimeOrder(outTimeOrder);
			  }
			  
		}else if(null!=status&&8==status){
			  logger.info(orderId+",改签确认失败,");
			  tuniuOrderDao.inserOutimeOrderVO(outTimeOrder);//存库记录
			  //查询出票系统中,改签单的状态：12、正在改签预订   14、改签成功等待确认  32、正在支付  34、支付成功    37、改签超时订单(途牛)
			  Map<String, String>  changeQueryMap=new HashMap<String,String>();
			  changeQueryMap.put("orderId", orderId);
			  changeQueryMap.put("changeId", changeId);//改签表中的reqtoken
			  Map<String, Object>  changeOrderStatusMap= tuniuOrderDao.selectCpSysChangeOrderStatusByChangeId(changeQueryMap);
			  
			  String changeOrderStatus=(String) changeOrderStatusMap.get("change_status");
			  Integer change_id =  (Integer) changeOrderStatusMap.get("change_id");
			 
			  logger.info(orderId+",出票系统的改签订单状态:"+changeOrderStatus+",change_id:"+change_id);
			  
			  TuniuChangeLogVO  changeLog=new TuniuChangeLogVO();
			  changeLog.setOrder_id(orderId);
			  changeLog.setContent("途牛方推送超时订单【改签确认失败】,超过30分钟自动处理失败,请人工处理该订单为失败！");
			  changeLog.setOpt_person("tuniu_app");
			  tuniuChangeDao.addChangeLog(changeLog);
			  
			  if("32".equals(changeOrderStatus)||"34".equals(changeOrderStatus)){//支付成功，正在支付,不做处理,否则将其改为超时订单状态
				 
				  logger.info(orderId+",出票系统的改签订单状态:"+changeOrderStatus);
				  changeLog.setContent("途牛方推送超时订单【改签确认失败】,但出票系统中改签订单状态为成功,或正在处理,订单状态为："+changeOrderStatus+",请人工确认,以失败处理！");
				  tuniuChangeDao.addChangeLog(changeLog);
		  
			      outTimeOrder.setDeal_status("22");//人工处理
			      outTimeOrder.setMsg(null);//这些个字段不做更新
				  outTimeOrder.setUpdateTime(null);
				  tuniuOrderDao.updateOutTimeOrder(outTimeOrder);
			  }else{
				  TuniuChangeInfo changeVo=new TuniuChangeInfo();
				  changeVo.setChange_id(change_id);
				  changeVo.setChange_status("37");//37、改签超时订单(途牛)
				  changeVo.setChange_notify_status(CHANGE_NOTIFY_SUCCESS);
				  changeVo.setChange_notify_time(DateUtil.dateToString(new Date(),DateUtil.DATE_FMT3));
				  changeVo.setChange_notify_count(10); 
				  tuniuChangeDao.updateChangeInfo(changeVo);  //修改为订单状态为：改签超时，通知状态为完成 
				  changeLog.setContent("途牛方推送超时订单【改签确认失败】,出票系统中改签订单状态："+changeOrderStatus+",直接修改为改签超时，通知状态为完成");  
				  tuniuChangeDao.addChangeLog(changeLog);
				  
				  outTimeOrder.setDeal_status("11");//已处理
				  outTimeOrder.setStatus(status);
				  outTimeOrder.setMsg(null);//这些个字段不做更新
				  outTimeOrder.setUpdateTime(null);
				  tuniuOrderDao.updateOutTimeOrder(outTimeOrder);
			  }
			 		
		}else{
			logger.info(orderId+",推送的类型未知，status:"+status);
			throw new TuniuCommonException(TuniuCommonService.RETURN_CODE_PARAM_ERROR);
		}
		
		
	}

	
	//*************************************************************// 
		
}
