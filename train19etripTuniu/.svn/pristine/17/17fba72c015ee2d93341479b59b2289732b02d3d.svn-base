package com.l9e.transaction.service.impl;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.l9e.common.TuniuConstant;
import com.l9e.transaction.component.TuniuCallback;
import com.l9e.transaction.dao.TuniuChangeDao;
import com.l9e.transaction.dao.TuniuCommonDao;
import com.l9e.transaction.dao.TuniuOrderDao;
import com.l9e.transaction.exception.TuniuCommonException;
import com.l9e.transaction.exception.TuniuException;
import com.l9e.transaction.exception.TuniuOrderException;
import com.l9e.transaction.service.TuniuChangeService;
import com.l9e.transaction.service.TuniuCommonService;
import com.l9e.transaction.service.TuniuOrderService;
import com.l9e.transaction.thread.SimpleRequest;
import com.l9e.transaction.thread.TuniuThreadPool;
import com.l9e.transaction.vo.AsynchronousOutput;
import com.l9e.transaction.vo.Notice;
import com.l9e.transaction.vo.Station;
import com.l9e.transaction.vo.TuniuChangeInfo;
import com.l9e.transaction.vo.TuniuChangeLogVO;
import com.l9e.transaction.vo.TuniuChangePassengerInfo;
import com.l9e.transaction.vo.TuniuOrder;
import com.l9e.transaction.vo.TuniuParameter;
import com.l9e.transaction.vo.TuniuPassenger;
import com.l9e.transaction.vo.TuniuResult;
import com.l9e.transaction.vo.model.Parameter;
import com.l9e.transaction.vo.model.Result;
import com.l9e.util.AmountUtil;
import com.l9e.util.BeanUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.IDGenerator;
import com.l9e.util.UrlFormatUtil;
@Service("tuniuChangeService")
public class TuniuChangeServiceImpl extends TuniuCommonServiceImpl implements TuniuChangeService{
	private static final Logger logger = Logger.getLogger(TuniuChangeServiceImpl.class);
	@Resource
	private TuniuChangeDao tuniuChangeDao;
	@Resource
	private TuniuOrderDao tuniuOrderDao;
	@Resource 
	private TuniuCommonDao tuniuCommonDao;

	@Override
	public Result addChange(Parameter parameter) throws TuniuCommonException {
		TuniuResult result = new TuniuResult();
		//改签日志
		TuniuChangeLogVO log=new TuniuChangeLogVO();
		logger.info("途牛请求改签");
		String vendorOrderId = parameter.getString("vendorOrderId");//我库订单号
		String orderId = parameter.getString("orderId");//途牛订单号
		String orderNumber = parameter.getString("orderNumber");//12306取票号
		String changeId = parameter.getString("changeId");//途牛请求改签流水号
		String changeCheCi = parameter.getString("changeCheCi");//改签车次
		String changeDateTime = parameter.getString("changeDateTime");//改签发车时间
		String changeZwCode = parameter.getString("changeZwCode");//改签坐席编码
		Boolean hasSeat = parameter.getBoolean("hasSeat");//是否支持无座 true:不接受 false：接受
		String oldZwCode = parameter.getString("oldZwCode");//原车票坐席编码
		String userName = parameter.getString("userName");//原下单账号用户名
		String userPassword = parameter.getString("userPassword");//原下单账号密码
		JSONArray tickets = parameter.getJSONArray("oldTicketInfos");//原车票信息
		logger.info("tickets ：          "+tickets);
		Boolean isChild = false;
		String callBackUrl = parameter.getString("callBackUrl");   //回调地址
		
		Boolean isChooseSeats= parameter.getBoolean("isChooseSeats"); //是否选座, true:选, false:非选
		String  chooseSeats= parameter.getString("chooseSeats");  //选座信息(选座个数要和乘客数量一致)
		
		logger.info("orderId:"+orderId+",isChooseSeats:"+isChooseSeats+",chooseSeats:"+chooseSeats);
		
		result.putData("vendorOrderId", vendorOrderId);
		
		try{
			/*业务参数检查*/
			if("".equals(vendorOrderId) || "".equals(orderId)|| "".equals(orderNumber) || "".equals(changeId) || 
					"".equals(changeCheCi) || "".equals(changeDateTime) || "".equals(changeZwCode) 
					|| "".equals(hasSeat)|| "".equals(oldZwCode)|| "".equals(tickets) ||"".equals(callBackUrl)) {
				logger.info("途牛请求改签 ERROR,参数有空order_id:"+vendorOrderId);
				throw new TuniuCommonException(TuniuCommonService.RETURN_CODE_PARAM_ERROR);
			}
			//检查订单
			Map<String,Object> queryParam = new HashMap<String,Object>();
			queryParam.put("orderId", vendorOrderId);
			TuniuOrder orderInfo = tuniuOrderDao.selectOneOrder(queryParam);
			if(orderInfo==null){
				logger.info("途牛改签，订单不存在，orderId:"+vendorOrderId);
				throw new TuniuCommonException(CHANGE_NOT_EXIST_ERROR);
			}else if(!orderInfo.getOrderStatus().equals(TuniuOrderService.STATUS_OUT_SUCCESS)){
				logger.info("途牛改签，订单状态不是出票成功，orderId:"+vendorOrderId);
				throw new TuniuCommonException(CHANGE_STATUS_ERROR);
			}
		/*	//出发站和到达站，变更了，直接返回错误，待修改
			if(orderInfo.getFromCity().equals("")||orderInfo.getToCity().equals("")){
				logger.info("途牛改签，接口暂时不支持变更到站，orderId:"+vendorOrderId);
				throw new TuniuCommonException(TuniuCommonService.RETURN_CODE_PARAM_ERROR);
			}
			*/
			if(userName!=""){
				//传账号信息
				if(!userName.equals(orderInfo.getUserName())){
					logger.info("途牛改签，传参账号有误，数据库userName:"+orderInfo.getOrderName()+"传参userName:"+userName);
					throw new TuniuOrderException(APPLY_CHAGNE_ACCOUNT_ERROR);
				}else{
					//更新订单系统
					orderInfo.setUserPassword(userPassword);
					tuniuOrderDao.updateOrder(orderInfo);
					//更新出票系统账号
					//参数拼接整合
					Map<String,String> params = new HashMap<String,String>();
					params.put("userName", userName);
					params.put("userPassWord", userPassword);
					String uri = UrlFormatUtil.createUrl("", params, "utf-8");
					//post请求
					String editAccountresult = HttpUtil.sendByPost(TuniuConstant.EDIT_ACCOUNT, uri, "UTF-8");
					logger.info("途牛改签，userName" + orderInfo.getUserName()+"修改账号返回结果："+editAccountresult);
					//解析返回参数
					JSONObject strObject = JSONObject.fromObject(editAccountresult);
					Boolean flag = strObject.getBoolean("success");
					if(flag){
						logger.info("途牛改签，修改账号成功，userName" + orderInfo.getUserName());
					}else{
						logger.info("途牛改签，修改账号失败 ，userName" + orderInfo.getUserName());
					}
				}
			}
			/*查询该订单号下的改签特征值，排除重复请求*/
			Map<String,Object> changeParam = new HashMap<String,Object>();
			changeParam.put("reqtoken", changeId);
			TuniuChangeInfo changeInfo = tuniuChangeDao.selectChangeInfo(changeParam);
			if(changeInfo!=null){
				logger.info("途牛请求改签，该请求已存在，reqtoken为"+changeId);
				throw new TuniuCommonException(APPLY_CHANGE_REPEAT_ERROR);
			}
			
			/*改签时间验证*/
			if(DateUtil.minuteDiff(orderInfo.getFromTime(), new Date()) < 30) {
				/*距离发车时间小于30分*/
				logger.info("途牛-请求改签ERROR,距离开车时间太近无法改签");
				throw new TuniuCommonException(APPLY_CHANGE_TIME_ERROR);
			}
			/*车票信息*/
			if(tickets.size() == 0) {
				logger.info("途牛-请求改签ERROR,没有车票信息");
				throw new TuniuCommonException(TuniuCommonService.RETURN_CODE_PARAM_ERROR);
			}
			if(tickets.size() > 1) {
				/*批量改签*/
				if(oldZwCode.equals("3") ||oldZwCode.equals("4") ||oldZwCode.equals("6")||oldZwCode.equals("F")
						||changeZwCode.equals("3") ||changeZwCode.equals("4") ||changeZwCode.equals("6")||changeZwCode.equals("F")) {
					/*批量改签原票坐席不能为卧铺*/
					logger.info("途牛-请求改签ERROR,批量改签原票或新票坐席不能为卧铺");
					throw new TuniuCommonException(APPLY_CHANGE_SEAT_TYPE_ERROR);
				}
			} 
			/*组装改签车票信息*/
			changeInfo = new TuniuChangeInfo();
			List<TuniuChangePassengerInfo> changePassengers = new ArrayList<TuniuChangePassengerInfo>();
			for(int i = 0; i < tickets.size(); i++) {
				/*传入的参数数据*/
				JSONObject ticket = tickets.getJSONObject(i);
				//改签车票信息
				TuniuChangePassengerInfo cp = new TuniuChangePassengerInfo();
				Map<String, Object> changeCpParam = new HashMap<String, Object>();
				changeCpParam.put("cp_id", ticket.getString("oldTicketNo"));
				List<TuniuChangePassengerInfo> cps = tuniuChangeDao.selectChangeCp(changeCpParam);
				if(cps !=  null) {
					for(TuniuChangePassengerInfo changeCp:cps){
						/*每张车票只能改签一次*/
						if(changeCp.getIs_changed().equals("Y")){
							logger.info("途牛-请求改签ERROR,车票已改签过,车票id:" + changeCp.getCp_id());
							throw new TuniuCommonException(APPLY_CHANGE_CHANGED_ERROR);
						}
					}
				} 
				cp.setOrder_id(vendorOrderId);//订单id
				cp.setCp_id(ticket.getString("oldTicketNo"));//车票id(原票)
				cp.setNew_cp_id(IDGenerator.generateID("TN_"));//改签后车票id
				cp.setChange_seat_type(sysSeatType(changeZwCode));//19e改签后新座位席别
				logger.info("途牛改签转换后的坐席："+vendorOrderId+",途牛的坐席:"+changeZwCode+",转换后的坐席:"+cp.getChange_seat_type());
				cp.setSeat_type(sysSeatType(oldZwCode));//19e原座位类型
				cp.setTn_seat_type(oldZwCode);//途牛原座位类型
				cp.setTn_change_seat_type(changeZwCode);
				cp.setIs_changed("N");
				
				/*原票信息*/
				Map<String,Object> cpParam = new HashMap<String,Object>();
				cpParam.put("orderId", vendorOrderId);
				cpParam.put("cpId", ticket.getString("oldTicketNo"));
				TuniuPassenger p  = tuniuOrderDao.selectOnePassenger(cpParam);
				if(p != null) {
					cp.setBuy_money(p.getBuyMoney());//成本价格
					cp.setSeat_no(p.getSeatNo());//座位号
					cp.setSeat_type(p.getSeatType());//座位席别
					cp.setTrain_box(p.getTrainBox());//车厢
					cp.setTicket_type(p.getTicketType());//车票类型
					if(p.getTicketType().equals("1")){
						//儿童票转人工
						isChild  = true;
					}
					cp.setIds_type(p.getIdsType());//证件类型
					cp.setUser_ids(p.getUserIds());//证件号码
					cp.setUser_name(p.getUserName());//乘客姓名
					cp.setTn_ids_type(p.getTuniuIdsType());//途牛证件类型
					cp.setTn_ticket_type(p.getTuniuTicketType());//途牛车票类型
				}else{
					logger.info("途牛改签，根据车票号找到不到信息，orderId:"+vendorOrderId);
					throw new TuniuCommonException(CHANGE_NOT_EXIST_ERROR);
				}
				changePassengers.add(cp);
			}
			/*组装改签记录信息*/
			changeInfo.setChange_travel_time(changeDateTime);//改签后乘车日期
			changeInfo.setTrain_no(orderInfo.getTrainNo());//车次
			changeInfo.setChange_train_no(changeCheCi);//改签后车次
			changeInfo.setFrom_time(DateUtil.dateToString(orderInfo.getFromTime(), DateUtil.DATE_FMT3));//发车时间
			changeInfo.setChange_from_time(changeDateTime);//改签后发车时间
			changeInfo.setTravel_time(DateUtil.dateToString(orderInfo.getTravelDate(), DateUtil.DATE_FMT3));	
			changeInfo.setFrom_city(orderInfo.getFromCity());//出发车站
			changeInfo.setTo_city(orderInfo.getToCity());//到达车站
			changeInfo.setFrom_station_code(orderInfo.getFromCityCode());
			changeInfo.setTo_station_code(orderInfo.getToCityCode());
			changeInfo.setIschangeto(0); //区分是否，1：变更到站，0 ：改签
			changeInfo.setOut_ticket_billno(orderNumber);//12306单号
			changeInfo.setOrder_id(vendorOrderId);
			changeInfo.setIsasync("Y");//异步
			changeInfo.setCallbackurl(callBackUrl);
			changeInfo.setReqtoken(changeId);
			changeInfo.setHasSeat(hasSeat?1:0);
			
			changeInfo.setIsChooseSeats(isChooseSeats!=null&&isChooseSeats?1:0); //1： 选 , 0： 非选
			changeInfo.setChooseSeats(chooseSeats);//座位信息, 例如：1A2B
			
			if(isChild){
				changeInfo.setChange_status(TRAIN_REQUEST_CHANGE_ARTIFICIAL);//11改签预定
			}else{
				changeInfo.setChange_status(TRAIN_REQUEST_CHANGE);//11改签预定
			}
			changeInfo.setcPassengers(changePassengers);//改签、车票关系
			changeInfo.setBook_ticket_time(DateUtil.dateToString(new Date(), DateUtil.DATE_FMT3));
			String acc_id = tuniuOrderDao.selectAccountByOrder(vendorOrderId);
			changeInfo.setAccount_id(acc_id);//出票账号id
			changeInfo.setChange_notify_count(0);
			changeInfo.setChange_notify_status(CHANGE_NOTIFY_PRE);
			changeInfo.setMerchant_id(TuniuConstant.merchantId);
			/*改签信息入库*/
			tuniuChangeDao.insertChangeInfo(changeInfo);
			int change_id = changeInfo.getChange_id();
			List<TuniuChangePassengerInfo> cPassengers = changeInfo.getcPassengers();
			if (cPassengers != null && cPassengers.size() != 0) {
				for (TuniuChangePassengerInfo cPassenger : cPassengers) {
					cPassenger.setChange_id(change_id);
					tuniuChangeDao.insertChangeCp(cPassenger);
				}
			}
			log.setChange_id(change_id);
			log.setOrder_id(vendorOrderId);
			log.setContent("途牛请求改签成功");
		    logger.info("途牛请求改签成功,orderId : " + vendorOrderId );
		}catch (TuniuException e) {
			result.setCode(e.getMessage());
		} catch (Exception e) {
			logger.info("途牛请求改签异常"+e);
			log.setContent("途牛请求改签异常!");
			result.setCode(TuniuChangeService.APPLY_CHANGE_ERROR);
			e.printStackTrace();
		} finally {
			log.setOrder_id(vendorOrderId);
			log.setOpt_person("tuniu");
			tuniuChangeDao.addChangeLog(log);
		}
		return result;
	}

	@Override
	public Result cancelChange(Parameter parameter) throws TuniuCommonException {
		TuniuResult result = new TuniuResult ();
		String vendorOrderId = parameter.getString("vendorOrderId");
		String orderId = parameter.getString("orderId");
		String changeId = parameter.getString("changeId");
		String callBackUrl = parameter.getString("callBackUrl");
		result.putData("vendorOrderId", vendorOrderId);
		logger.info("途牛取消改签开始,order_id :"+vendorOrderId);
		//改签日志
		TuniuChangeLogVO log=new TuniuChangeLogVO();
		try {
			/*检查业务参数*/
			if("".equals(vendorOrderId) || "".equals(orderId)||"".equals(changeId)||"".equals(callBackUrl)) {
				logger.info("途牛取消改签 ERROR,参数有空order_id:"+vendorOrderId);
				throw new TuniuCommonException(TuniuCommonService.RETURN_CODE_PARAM_ERROR);
			}
			/*查询订单*/
			Map<String,Object> queryParam = new HashMap<String,Object>();
			queryParam.put("orderId", vendorOrderId);
			TuniuOrder orderInfo = tuniuOrderDao.selectOneOrder(queryParam);
			Map<String,Object> changeParam = new HashMap<String,Object>();
			changeParam.put("reqtoken", changeId);
			TuniuChangeInfo changeInfo = tuniuChangeDao.selectChangeInfo(changeParam);
			if(orderInfo==null||changeInfo==null){
				logger.info("途牛取消改签，订单不存在，orderId:"+vendorOrderId);
				throw new TuniuCommonException(CANCEL_CHANGE_NOTEXIST_ERROR);
			}else if(!orderInfo.getOrderStatus().equals(TuniuOrderService.STATUS_OUT_SUCCESS)){
				logger.info("途牛取消改签，订单状态不是出票成功，orderId:"+vendorOrderId);
				throw new TuniuCommonException(CHANGE_STATUS_ERROR);
			}
			if(changeInfo.getChange_status().startsWith("3")){
				/*订单已确认改签，不能取消改签*/
				logger.info("订单已确认改签，不能取消改签，orderId:"+vendorOrderId);
				throw new TuniuCommonException(CANCEL_CHANGE_ALREADYCONFIRM_ERROR);
			}
			if(!changeInfo.getChange_status().equals(TRAIN_REQUEST_CHANGE_SUCCESS)){
				/*订单状态不正确*/
				logger.info("途牛改签，订单状态不是改签成功，orderId:"+vendorOrderId);
				throw new TuniuCommonException(CHANGE_STATUS_ERROR);
			}
		
			log.setChange_id(changeInfo.getChange_id());
			Date currentTime = new Date();
			/*预订成功后的30分钟内才能取消改签*/
			Date bookTime = DateUtil.stringToDate(changeInfo.getBook_ticket_time(), DateUtil.DATE_FMT3);
			long minuteDiff = DateUtil.minuteDiff(currentTime, bookTime);
			if(minuteDiff > 30) {
				logger.info("途牛取消改签ERROR,距离改签车票预订时间超过30分钟");
				throw new TuniuCommonException( CANCEL_CHANGE_TIMEOUT_ERROR );
			}
			/*将状态为14、预订成功的改签状态都改为21、改签取消*/
			changeInfo.setChange_status(TRAIN_CANCEL_CHANGE);
			changeInfo.setChange_notify_count(0);
			changeInfo.setChange_notify_status(CHANGE_NOTIFY_PRE);
			changeInfo.setCallbackurl(callBackUrl);
			/*更新改签状态*/
			tuniuChangeDao.updateChangeInfo(changeInfo);
			log.setContent("途牛取消改签请求成功");
		}catch (TuniuException e) {
			result.setCode(e.getMessage());
		} catch (Exception e) {
			logger.info("途牛取消改签异常"+e);
			result.setCode(CANCEL_CHANGE_ERROR);
			log.setContent("途牛取消改签异常!");
			e.printStackTrace();
		} finally {
			log.setOrder_id(vendorOrderId);
			log.setOpt_person("tuniu");
			tuniuChangeDao.addChangeLog(log);
		}
		return result;

	}

	@Override
	public Result confirmChange(Parameter parameter) throws TuniuCommonException{
		TuniuResult result = new TuniuResult ();
		String vendorOrderId = parameter.getString("vendorOrderId");
		String orderId = parameter.getString("orderId");
		String changeId = parameter.getString("changeId");
		String callBackUrl = parameter.getString("callBackUrl");
		result.putData("vendorOrderId", vendorOrderId);
		result.putData("orderId", vendorOrderId);
		logger.info("途牛确认改签开始,order_id:"+vendorOrderId);
		//改签日志
		TuniuChangeLogVO log=new TuniuChangeLogVO();
		try {
			/*检查业务参数*/
			if("".equals(vendorOrderId) || "".equals(orderId)||"".equals(changeId)||"".equals(callBackUrl)) {
				logger.info("途牛请求改签 ERROR,参数有空order_id:"+vendorOrderId);
				throw new TuniuCommonException(TuniuCommonService.RETURN_CODE_PARAM_ERROR);
			}
			/*查询订单*/
			Map<String,Object> queryParam = new HashMap<String,Object>();
			queryParam.put("orderId", vendorOrderId);
			TuniuOrder orderInfo = tuniuOrderDao.selectOneOrder(queryParam);
			Map<String,Object> changeParam = new HashMap<String,Object>();
			changeParam.put("reqtoken", changeId);
			TuniuChangeInfo changeInfo = tuniuChangeDao.selectChangeInfo(changeParam);
			if(orderInfo==null||changeInfo==null){
				logger.info("途牛改签，订单不存在，orderId:"+vendorOrderId);
				throw new TuniuCommonException(CONFIRM_CHANGE_NOTEXIST_ERROR);
			}else if(!orderInfo.getOrderStatus().equals(TuniuOrderService.STATUS_OUT_SUCCESS)){
				logger.info("途牛改签，订单状态不是出票成功，orderId:"+vendorOrderId);
				throw new TuniuCommonException(CHANGE_STATUS_ERROR);
			}
			if(!changeInfo.getChange_status().equals(TRAIN_REQUEST_CHANGE_SUCCESS)){
				/*订单状态不正确*/
				logger.info("途牛改签，订单状态不是改签成功，orderId:"+vendorOrderId);
				throw new TuniuCommonException(CHANGE_STATUS_ERROR);
			}
			log.setChange_id(changeInfo.getChange_id());
			/*22:34:59*/
			Calendar time_22_44_59 = Calendar.getInstance();
			time_22_44_59.set(Calendar.HOUR_OF_DAY, 22);
			time_22_44_59.set(Calendar.MINUTE, 44);
			time_22_44_59.set(Calendar.SECOND, 59);
			/*23:30:00*/
			Calendar time_23_30_00 = Calendar.getInstance();
			time_23_30_00.set(Calendar.HOUR_OF_DAY, 23);
			time_23_30_00.set(Calendar.MINUTE, 30);
			time_23_30_00.set(Calendar.SECOND, 00);
			Calendar currentTime = Calendar.getInstance();
			Calendar bookTime = Calendar.getInstance();
			Date book = DateUtil.stringToDate(changeInfo.getBook_ticket_time(), DateUtil.DATE_FMT3);
			bookTime.setTime(book);
			
			boolean timeOut = false;
			if(bookTime.before(time_22_44_59)) {//当天22:44:59之前预订
				/*30分钟的付款时间*/
				System.out.println("current : " + DateUtil.dateToString(currentTime.getTime(), "yyyy-MM-dd HH:mm:ss"));
				System.out.println("book : " + DateUtil.dateToString(book, "yyyy-MM-dd HH:mm:ss"));
				if(DateUtil.minuteDiff(currentTime.getTime(), book) > 30) {
					timeOut = true;
				}
				logger.info("当天22:44:59之前预订,30分钟的付款时间,timeout" + timeOut);
			} else if(bookTime.after(time_22_44_59)) {//当天22:44:59之后预订
				/*23:30:00之前付款*/
				if(currentTime.after(time_23_30_00)) {
					timeOut = true;
				}
				logger.info("当天22:44:59之后预订,23:30:00之前付款,timeout" + timeOut);
			}
			if(timeOut) {
				logger.info("途牛-确认改签ERROR,确认改签的请求时间已超过规定时间");
				throw new TuniuCommonException(CONFIRM_CHANGE_TIMEOUT_ERROR);
			}
			/*确认改签*/
			changeInfo.setChange_status(TRAIN_CONFIRM_CHANGE);
			changeInfo.setCallbackurl(callBackUrl);
			changeInfo.setReqtoken(changeId);
			changeInfo.setChange_notify_count(0);
			changeInfo.setChange_notify_status(CHANGE_NOTIFY_PRE);
			/*更新车票改签状态*/
			tuniuChangeDao.updateChangeInfo(changeInfo);
			log.setContent("途牛确认改签success ,orderId" + vendorOrderId);
		}catch (TuniuException e) {
			result.setCode(e.getMessage());
		}catch (Exception e) {
			logger.info("途牛确认改签异常"+e);
			log.setContent("途牛请求改签支付异常!");
			result.setCode(CONFIRM_CHANGE_ERROR);
			e.printStackTrace();
		} finally {
			log.setOrder_id(vendorOrderId);
			log.setOpt_person("tuniu");
			tuniuChangeDao.addChangeLog(log);
		}
		return result;
	}

	@Override
	public List<TuniuChangeInfo> getNoticeChangeInfo(String merchantId) {
		return tuniuChangeDao.selectNoticeChangeInfo(merchantId);
	}

	@Override
	public void callbackChangeNotice(List<TuniuChangeInfo> notifyList) {
		logger.info("途牛改签回调");
		try {
			for(TuniuChangeInfo changeInfo:notifyList){
				//先将通知信息更新状态
				TuniuChangeInfo updateInfo = new TuniuChangeInfo();
				updateInfo.setChange_id(changeInfo.getChange_id());
				updateInfo.setChange_notify_status(TuniuCommonService.CHANGE_NOTIFY_START);
				updateInfo.setChange_notify_time(DateUtil.dateToString(new Date(), DateUtil.DATE_FMT3));
				int count=tuniuChangeDao.updateChangeInfo(updateInfo);//更新通知状态
				logger.info("改签订单号："+changeInfo.getOrder_id()+",回调地址："+changeInfo.getCallbackurl()+",count:"+count+",通知状态："+changeInfo.getChange_notify_status());
				
				//改签车票信息
				Map<String, Object> cpParam = new HashMap<String, Object>();
				cpParam.put("change_id", changeInfo.getChange_id());
				/*获取改签车票信息*/
				List<TuniuChangePassengerInfo> cPassengers = tuniuChangeDao.selectChangeCp(cpParam);
				
				/*返回data*/
				JSONObject dataParameter = new JSONObject();
				String returnCode="";
				String errorMsg="";
				String changeStatus = changeInfo.getChange_status();
				String service ="";
				//改签占座
				if(changeStatus.startsWith("1")) {
					service = "applyChange";
					/*请求改签*/
					dataParameter.put("vendorOrderId", changeInfo.getOrder_id());
					dataParameter.put("orderId", changeInfo.getOrder_id());
					dataParameter.put("orderNumber", changeInfo.getOut_ticket_billno());
					dataParameter.put("changeId", changeInfo.getReqtoken());
					dataParameter.put("changeSuccess",changeStatus.equals("14")?true:false);
					dataParameter.put("cheCi", changeInfo.getChange_train_no());
					dataParameter.put("fromStationCode", changeInfo.getFrom_station_code());
					dataParameter.put("fromStationName", changeInfo.getFrom_city());
					dataParameter.put("toStationCode", changeInfo.getTo_station_code());
					dataParameter.put("toStationName", changeInfo.getTo_city());
					dataParameter.put("trainDate", changeInfo.getChange_from_time().split(" ")[0]);
					dataParameter.put("clearTime", changeInfo.getPay_limit_time()==null?"":changeInfo.getPay_limit_time()); //清位时间，支付截至时间
				
					if(changeStatus.equals("14")) {
						logger.info("改签成功，orderId" +changeInfo.getOrder_id());
						returnCode="231000";
						/*查询时间*/
						Map<String, String> timeParam = new HashMap<String, String>();
						timeParam.put("train_date", changeInfo.getChange_from_time().split(" ")[0]);
						timeParam.put("from_station", changeInfo.getFrom_station_code());
						timeParam.put("to_station",changeInfo.getTo_station_code());
						timeParam.put("train_code", changeInfo.getChange_train_no());
						String startTime ="";
						if(changeInfo.getChange_from_time()!=null && changeInfo.getChange_from_time().length()>16){
							startTime = changeInfo.getChange_from_time().split(" ")[1].substring(0, 5);
						}
						String endTime = "";
						if(changeInfo.getChange_to_time()!=null&&changeInfo.getChange_to_time().length()>16){
							endTime = changeInfo.getChange_to_time().split(" ")[1].substring(0, 5);
						}
						if(startTime==null ||startTime.equals("")||endTime==null ||endTime.equals("")){
							//途牛查询车次出发到达时间
							Map<String,Object> timeResult = this.queryTime(changeInfo);
							logger.info("途牛改签回调查询到达时间,orderId"+changeInfo.getOrder_id()+"结果"+timeResult.toString());
							if(timeResult!=null && !"".equals(timeResult)){
								dataParameter.put("arriveTime",timeResult.get("arrive_time"));
								dataParameter.put("startTime",timeResult.get("start_time"));
								
							}else{
								logger.info("途牛改签回调查询时间，未查询到   "+changeInfo.getOrder_id());
								dataParameter.put("arriveTime","");
								dataParameter.put("startTime","");
							}
						}else{
							dataParameter.put("arriveTime",endTime); //到达时间
							dataParameter.put("startTime",startTime); //出发时间
						}
					} else if(changeStatus.equals("15")){
						logger.info("改签失败，orderId" +changeInfo.getOrder_id());
						String code = changeInfo.getFail_reason();
						returnCode = getChangeErrorCode(code);
						errorMsg = getChangeErrorInfo(code);
						dataParameter.put("arriveTime","");
						dataParameter.put("startTime","");
					}
					
					//车票信息
					JSONArray newTickets = new JSONArray();
					Date book_ticket_time;
					if(changeInfo.getBook_ticket_time()!=null){
						book_ticket_time=DateUtil.stringToDate(changeInfo.getBook_ticket_time(), DateUtil.DATE_FMT3);
					}else{
						book_ticket_time=new Date();
					}
					
					//费率
					double diffRate=getDiffRate(changeInfo.getFrom_time(), book_ticket_time);
					double totalBuyMoney = 0.0;
					double totalChangeBuyMoney = 0.0;
					double totalDiff = 0.0;
					double totalPriceDiff = 0.0;
					double fee = 0.0;
					Integer priceInfoType = 0;
					String priceInfo = null;
					int cp_num=cPassengers.size();
					if(changeStatus.equals("14")) {
						/*成功*/
						for(int i=0;i< cPassengers.size();i++) {
							TuniuChangePassengerInfo cPassenger = cPassengers.get(i);
							JSONObject newTicket = new JSONObject();
							newTicket.put("reason", 0);
							//newTicket.put("passengerId", i+1);
							newTicket.put("oldTicketNo", cPassenger.getCp_id());
							newTicket.put("newTicketNo", cPassenger.getNew_cp_id());
							newTicket.put("zwCode", cPassenger.getTn_change_seat_type());
							newTicket.put("cxin", cPassenger.getChange_train_box() + "车厢," + cPassenger.getChange_seat_no());
							newTicket.put("passportNo", cPassenger.getUser_ids());
							newTicket.put("passengerName", cPassenger.getUser_name());
							newTicket.put("piaoType", cPassenger.getTn_ticket_type());
							newTicket.put("passportTypeId", cPassenger.getTn_ids_type());
							newTicket.put("passportTypeName", tuniuIdsName(cPassenger.getTn_ids_type()));
							newTicket.put("zwName", tuniuSeatName(cPassenger.getTn_change_seat_type()));
							newTicket.put("piaoTypeName", tuniuTicketName( cPassenger.getTn_ticket_type()));
							newTicket.put("price", cPassenger.getChange_buy_money());
							newTicket.put("cxin", cPassenger.getChange_train_box() + "车厢," + cPassenger.getChange_seat_no());
							totalBuyMoney += cPassenger.getBuy_money();//改签之前总成本价
							totalChangeBuyMoney += cPassenger.getChange_buy_money();//改签之后总成本价
							newTickets.add(newTicket);
						}
						logger.info("totalBuyMoney : " + totalBuyMoney);
						logger.info("totalChangeBuyMoney : " + totalChangeBuyMoney);
						totalDiff = totalChangeBuyMoney - totalBuyMoney;//总差价
						if(totalDiff < 0.0) {//新票款低于原票款
							priceInfoType = 3;
							priceInfo = "退还票款差额:" + totalDiff + "元";
						} else if(totalDiff == 0.0) {//新票款等于原票款
							priceInfoType = 2;
							priceInfo = "改签票款差价:0.0元";
						} else if(totalDiff > 0.0) {//新票款大于原票款
							priceInfoType = 1;
							priceInfo = "收取新票款:" + totalChangeBuyMoney + "元,退还原票票款:" + totalBuyMoney + "元";
						}
					
						if(priceInfoType != null) {
							/*改签成功后生成流水号、计算手续费*/
							if(priceInfoType == 3) {//新票款低于原票款
								if(diffRate==0){
									fee=0;
								}else{
									fee = AmountUtil.quarterConvert(Math.abs(AmountUtil.mul(totalDiff, diffRate)));//手续费=退款差额 * 费率
									int less_fee=2*cp_num;
									fee=fee<less_fee?less_fee:fee;
									
									if(fee > Math.abs(totalDiff)) {
										fee = Math.abs(totalDiff);
									}
								}
								totalPriceDiff = AmountUtil.sub(Math.abs(totalDiff), fee);//实际退款=退款差额-手续费
								updateInfo.setFee(fee+"");
								updateInfo.setDiffrate(diffRate+"");
								updateInfo.setTotalpricediff(totalPriceDiff+"");
							} else if(priceInfoType == 1) {//新票款大于原票款
								updateInfo.setChange_refund_money(totalBuyMoney + "");
								updateInfo.setChange_receive_money(totalChangeBuyMoney + "");
								diffRate=0.0;
							}else{
								diffRate=0.0;
							}
						}
					}
					
					tuniuChangeDao.updateChangeInfo(updateInfo); //更新一些价格信息,通知状态,时间
					
					dataParameter.put("priceChangeType",priceInfoType);
					dataParameter.put("priceChangeTypeInfo",priceInfo);
					dataParameter.put("priceDifference", totalDiff);
					dataParameter.put("diffRate", diffRate);
					dataParameter.put("totalPriceDiff", totalPriceDiff);
					dataParameter.put("fee", fee);
					dataParameter.put("newTicketInfos", newTickets);
				
				} else if(changeStatus.startsWith("2")) {
					service = "cancelChange";
					//改签取消
					dataParameter.put("vendorOrderId", changeInfo.getOrder_id());
					dataParameter.put("orderId", changeInfo.getOrder_id());
					dataParameter.put("changeId", changeInfo.getReqtoken());
					/*取消改签*/
					if(changeStatus.equals("23")) {
						logger.info("取消成功，orderId:" +changeInfo.getOrder_id());
						returnCode = "231000";
					} else if(changeStatus.equals("24")) {
						logger.info("取消失败，orderId:" +changeInfo.getOrder_id());
						returnCode = getChangeErrorCode(changeInfo.getFail_reason());
						errorMsg = getChangeErrorInfo(changeInfo.getFail_reason());
					}
				} else if(changeStatus.startsWith("3")) {
					service = "confirmChange";
					/*确认改签*/
					dataParameter.put("vendorOrderId", changeInfo.getOrder_id());
					dataParameter.put("orderId", changeInfo.getOrder_id());
					dataParameter.put("changeId", changeInfo.getReqtoken());
					if(changeStatus.equals("34")) {
						logger.info("确认改签成功，orderId:" +changeInfo.getOrder_id());
						returnCode = "231000";
						for(TuniuChangePassengerInfo cPassenger : cPassengers) {
							cPassenger.setIs_changed("Y");
						    tuniuChangeDao.updateChangeCp(cPassenger);
						}
					} else if(changeStatus.equals("35")) {
						logger.info("确认改签失败，orderId:" +changeInfo.getOrder_id());
						returnCode = getChangeErrorCode(changeInfo.getFail_reason());
						errorMsg = getChangeErrorInfo(changeInfo.getFail_reason());
					}
				}
				
				/*回调*/
				TuniuChangeLogVO log = new TuniuChangeLogVO();
				log.setOrder_id(changeInfo.getOrder_id());
				log.setChange_id(changeInfo.getChange_id());
				log.setContent("途牛改签回调类型:"+service);
				log.setOpt_person("tuniu");
				tuniuChangeDao.addChangeLog(log);
				String dataJson = dataParameter.toString();
				logger.info("途牛改签回调参数"+dataJson);
				AsynchronousOutput out = new AsynchronousOutput();
				out.setData(dataJson);
				out.setReturnCode(returnCode);
				out.setErrorMsg(errorMsg);
				
				/* 处理请求返回结果对象 */
				TuniuCallback callback = (TuniuCallback) BeanUtil.getBean("tuniuCallback");
				Notice notice = new Notice();
				notice.setOrderId(changeInfo.getOrder_id());
				notice.setTuniuChangeId(changeInfo.getChange_id().toString());
				notice.setNotifyNum(changeInfo.getChange_notify_count());
				notice.setNotifyStatus(TuniuCommonService.CHANGE_NOTIFY_START);
				callback.setNotice(notice);
				callback.setAsynOutput(out);
				callback.setService(service);
				/* 请求线程 */
				SimpleRequest request = new SimpleRequest(SimpleRequest.METHOD_POST,
						changeInfo.getCallbackurl(), callback);
				
				TuniuThreadPool.tuniuCallbackSubmit(request);
			} 
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("途牛改签回调异常",e);
		}
	}
	@Override
	public void updateNotice(Notice notice) {
		/*回调*/
		TuniuChangeLogVO log = new TuniuChangeLogVO();
		log.setOrder_id(notice.getOrderId());
		log.setChange_id(new Integer(notice.getTuniuChangeId()));
		log.setOpt_person("tuniu");
		//改签成功
		if(notice.getNotifyStatus().equals(TuniuCommonService.CHANGE_NOTIFY_SUCCESS)){
			logger.info("改签回调成功" );
			log.setContent("途牛改签回调成功");
		}else{
			//改签失败
			logger.info("改签回调请求失败,order_id:"+notice.getOrderId());
			log.setContent("途牛改签回调失败");
		}
		TuniuChangeInfo change = new TuniuChangeInfo();
		change.setChange_id(new Integer(notice.getTuniuChangeId()));
		change.setChange_notify_status(notice.getNotifyStatus());
		change.setChange_notify_count(notice.getNotifyNum());
		change.setChange_notify_finish_time(notice.getNotifyFinishTime());
		tuniuChangeDao.updateChangeInfo(change);
		tuniuChangeDao.addChangeLog(log);
	}
	
	
	/*---------------------------dqh2015-11-23---------------------------------------------------*/
	@Override
	public Result addChangeCaptcha(Parameter parameter,String captchaResultUrl,String robotUrl) throws TuniuCommonException {
		TuniuResult result = new TuniuResult();
		//改签日志
		TuniuChangeLogVO log=new TuniuChangeLogVO();
		logger.info("途牛请求改签");
		String orderId = parameter.getString("requestId");// 途牛订单号 
		result.putData("requestId", orderId);
//		String orderId = parameter.getString("orderId");//途牛订单号 
		JSONObject busData = parameter.getJSONObject("busData");
		Parameter newParameter = new TuniuParameter(busData.toString());
//		String vendorOrderId = newParameter.getString("vendorOrderId");//我库订单号
		String orderNumber = newParameter.getString("orderNumber");//12306取票号
//		String changeId = newParameter.getString("changeId");//途牛请求改签流水号
//		String changeCheCi = newParameter.getString("changeCheCi");//改签车次
		Object changeCheCi =newParameter.getString("changeCheCi").equals("")?newParameter.getInt("changeCheCi"):newParameter.getString("changeCheCi");
		String fromStationName = newParameter.getString("fromStationName");//改签出发站
		String toStationName = newParameter.getString("toStationName");//改签到达站
		String changeDateTime = newParameter.getString("changeDateTime");//改签发车时间
		String changeZwCode = newParameter.getString("changeZwCode");//改签坐席编码
		Boolean hasSeat = newParameter.getBoolean("hasSeat");//是否支持无座 true:不接受 false：接受
		String oldZwCode = newParameter.getString("oldZwCode");//原车票坐席编码
		String userName = newParameter.getString("userName");//原下单账号用户名
		String userPassword = newParameter.getString("userPassword");//原下单账号密码
		String fromCity3c =  newParameter.getString("fromStationCode");
		String toCity3c =  newParameter.getString("toStationCode");
		JSONArray tickets = newParameter.getJSONArray("oldTicketInfos");//原车票信息
		logger.info("tickets ：          "+tickets);
		Boolean isChild = false; 
//		result.putData("vendorOrderId", vendorOrderId);
		try{
			/*业务参数检查*/
			if("".equals(orderId)|| "".equals(orderNumber) || 
					"".equals(changeCheCi) || "".equals(changeDateTime) || "".equals(changeZwCode) 
					|| "".equals(hasSeat)|| "".equals(oldZwCode)|| "".equals(tickets)) {
				logger.info("途牛请求改签 ERROR,参数有空order_id:"+orderId);
				throw new TuniuCommonException(TuniuCommonService.RETURN_CODE_PARAM_ERROR);
			}
//			//检查订单
//			Map<String,Object> queryParam = new HashMap<String,Object>();
//			queryParam.put("orderId", vendorOrderId);
//			TuniuOrder orderInfo = tuniuOrderDao.selectOneOrder(queryParam);
//			if(orderInfo==null){
//				logger.info("途牛改签，订单不存在，orderId:"+vendorOrderId);
//				throw new TuniuCommonException(CHANGE_NOT_EXIST_ERROR);
//			}else if(!orderInfo.getOrderStatus().equals(TuniuOrderService.STATUS_OUT_SUCCESS)){
//				logger.info("途牛改签，订单状态不是出票成功，orderId:"+vendorOrderId);
//				throw new TuniuCommonException(CHANGE_STATUS_ERROR);
//			}
			StringBuffer param1 = new StringBuffer();
			if(userName!=""){
				//传账号信息
				param1.append(userName).append("|").append(userPassword).append("|"); 
			}else{
				Map<String,Object> queryParam = new HashMap<String,Object>();
				queryParam.put("orderId", orderId);
				TuniuOrder orderInfo = tuniuOrderDao.selectOneOrder(queryParam);
				if(orderInfo==null){
					logger.info("途牛改签，订单不存在，orderId:"+orderId);
					throw new TuniuCommonException(CHANGE_NOT_EXIST_ERROR);
				}else if(!orderInfo.getOrderStatus().equals(TuniuOrderService.STATUS_OUT_SUCCESS)){
					logger.info("途牛改签，订单状态不是出票成功，orderId:"+orderId);
					throw new TuniuCommonException(CHANGE_STATUS_ERROR);
				}
				param1.append(orderInfo.getUserName()).append("|").append(orderInfo.getUserPassword()).append("|");
			}
//			/*查询该订单号下的改签特征值，排除重复请求*/
//			Map<String,Object> changeParam = new HashMap<String,Object>();
//			changeParam.put("reqtoken", changeId);
//			TuniuChangeInfo changeInfo = tuniuChangeDao.selectChangeInfo(changeParam);
//			if(changeInfo!=null){
//				logger.info("途牛请求改签，该请求已存在，reqtoken为"+changeId);
//				throw new TuniuCommonException(APPLY_CHANGE_REPEAT_ERROR);
//			}
			
			/*改签时间验证*/
//			if(DateUtil.minuteDiff(DateUtil.stringToDate(changeDateTime, DateUtil.DATE_FMT3), new Date()) < 30) {
//				/*距离发车时间小于30分*/
//				logger.info("途牛-请求改签ERROR,距离开车时间太近无法改签");
//				throw new TuniuCommonException(APPLY_CHANGE_TIME_ERROR);
//			}
			/*车票信息*/
			if(tickets.size() == 0) {
				logger.info("途牛-请求改签ERROR,没有车票信息");
				throw new TuniuCommonException(TuniuCommonService.RETURN_CODE_PARAM_ERROR);
			}
			//访问lua机器人
			StringBuffer luaParams = new StringBuffer();  
			param1.append(orderId).append("|").append(orderNumber)
			.append("|").append(fromStationName).append("|").append(toStationName).append("|")
			.append(changeDateTime).append("|").append(changeDateTime).append("|").append(changeCheCi).append("|")
			.append(changeCheCi).append("|").append("0");
			//总参数拼接
			luaParams.append("ScriptPath=").append("changeTicket.lua")
			.append("&commond=").append("changeTicketCaptcha")
			.append("&SessionID=").append(String.valueOf(System.currentTimeMillis()))
			.append("&Timeout=").append("1000000")
			.append("&ParamCount=").append(2)
			.append("&DeviceNo=").append("s9eyi3p5evmc9g83")
			.append("&FromCity3c=").append(fromCity3c)
			.append("&ToCity3c=").append(toCity3c)
			.append("&HasSeat=").append(hasSeat?1:0) 
			.append("&Param1=").append(URLEncoder.encode(param1.toString(),"utf-8"));
			//params2
			for (int i = 0; i < tickets.size(); i++) {
				luaParams.append("&Param").append(i+2).append("=");
				JSONObject jsonObject = tickets.getJSONObject(i); 
				StringBuffer param = new StringBuffer();
				param.append(jsonObject.get("passengerId")).append("|")
				.append(jsonObject.get("passengerName")).append("|")
				.append(jsonObject.get("piaoType")).append("|")
				.append(jsonObject.get("passportTypeId")).append("|")
				.append(jsonObject.get("passportNo")).append("|")
				.append(sysSeatType(changeZwCode)).append("|")
				.append(sysSeatType(oldZwCode)).append("|")
				.append(jsonObject.get("zwName"));
				luaParams.append(URLEncoder.encode(param.toString(),"UTF-8"));
			}
			logger.info("请求验证码机器人参数:"+luaParams);
			String luaResult = HttpUtil.sendByGet(robotUrl+"?"+luaParams.toString(),"utf-8", "1000", "1000");//请求机器人
			System.err.println(luaResult);
			//获取验证码结果 
			StringBuffer captchaBuffer = new StringBuffer();
			captchaBuffer.append("commond=").append("getCaptcha")
			.append("&requestId=").append(orderId).append("&captchaType=").append("change");
			String captchaResult = "";
			logger.info("开始获取验证码图片结果:"+captchaBuffer);
			for (int i = 0; i < 30; i++) {
				captchaResult = HttpUtil.sendByPost(captchaResultUrl, captchaBuffer.toString(), "utf-8"); 
				logger.info("验证码图片返回结果:"+captchaResult);
				if (!"fail".equals(captchaResult))
					break;
				else
					Thread.sleep(3000);
			}

			result.putData("captchaType", "change"); 
			if ("fail".equals(captchaResult)) {
				result.setCode("250012"); 
			}else if (captchaResult.contains("restart")) {
				if (captchaResult.contains("不存在"))
					result.setCode("250001");	
				else if (captchaResult.contains("密码输入错误"))
					result.setCode("250002");	
				else if (captchaResult.contains("锁定时间为20分钟"))
					result.setCode("250003");	
				else if (captchaResult.contains("冒用"))
					result.setCode("250004");	
				else if (captchaResult.contains("未通过核验"))
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
				else if (captchaResult.contains("未找到匹配改签信息"))
					result.setCode("250011");
				else
					result.setCode(TuniuCommonService.RETURN_CODE_UNKNOWN_ERROR);
			}else if("success".equals(captchaResult)){
				result.putData("needCaptcha",false); 
			}else{
				result.putData("needCaptcha",true);
				result.putData("file", captchaResult); 
			}
//			if(tickets.size() > 1) {
//				/*批量改签*/
//				if(oldZwCode.equals("3") ||oldZwCode.equals("4") ||oldZwCode.equals("6")
//						||changeZwCode.equals("3") ||changeZwCode.equals("4") ||changeZwCode.equals("6")) {
//					/*批量改签原票坐席不能为卧铺*/
//					logger.info("途牛-请求改签ERROR,批量改签原票或新票坐席不能为卧铺");
//					throw new TuniuCommonException(APPLY_CHANGE_SEAT_TYPE_ERROR);
//				}
//			} 
//			/*组装改签车票信息*/
//			changeInfo = new TuniuChangeInfo();
//			List<TuniuChangePassengerInfo> changePassengers = new ArrayList<TuniuChangePassengerInfo>();
//			for(int i = 0; i < tickets.size(); i++) {
//				/*传入的参数数据*/
//				JSONObject ticket = tickets.getJSONObject(i);
//				//改签车票信息
//				TuniuChangePassengerInfo cp = new TuniuChangePassengerInfo();
//				Map<String, Object> changeCpParam = new HashMap<String, Object>();
//				changeCpParam.put("cp_id", ticket.getString("oldTicketNo"));
//				List<TuniuChangePassengerInfo> cps = tuniuChangeDao.selectChangeCp(changeCpParam);
//				if(cps !=  null) {
//					for(TuniuChangePassengerInfo changeCp:cps){
//						/*每张车票只能改签一次*/
//						if(changeCp.getIs_changed().equals("Y")){
//							logger.info("途牛-请求改签ERROR,车票已改签过,车票id:" + changeCp.getCp_id());
//							throw new TuniuCommonException(APPLY_CHANGE_CHANGED_ERROR);
//						}
//					}
//				} 
//				cp.setOrder_id(vendorOrderId);//订单id
//				cp.setCp_id(ticket.getString("oldTicketNo"));//车票id(原票)
//				cp.setNew_cp_id(IDGenerator.generateID("TN_"));//改签后车票id
//				cp.setChange_seat_type(sysSeatType(changeZwCode));//19e改签后新座位席别
//				cp.setSeat_type(sysSeatType(oldZwCode));//19e原座位类型
//				cp.setTn_seat_type(oldZwCode);//途牛原座位类型
//				cp.setTn_change_seat_type(changeZwCode);
//				cp.setIs_changed("N");
//				
//				/*原票信息*/
//				Map<String,Object> cpParam = new HashMap<String,Object>();
//				cpParam.put("orderId", vendorOrderId);
//				cpParam.put("cpId", ticket.getString("oldTicketNo"));
//				TuniuPassenger p  = tuniuOrderDao.selectOnePassenger(cpParam);
//				if(p != null) {
//					cp.setBuy_money(p.getBuyMoney());//成本价格
//					cp.setSeat_no(p.getSeatNo());//座位号
//					cp.setSeat_type(p.getSeatType());//座位席别
//					cp.setTrain_box(p.getTrainBox());//车厢
//					cp.setTicket_type(p.getTicketType());//车票类型
//					if(p.getTicketType().equals("1")){
//						//儿童票转人工
//						isChild  = true;
//					}
//					cp.setIds_type(p.getIdsType());//证件类型
//					cp.setUser_ids(p.getUserIds());//证件号码
//					cp.setUser_name(p.getUserName());//乘客姓名
//					cp.setTn_ids_type(p.getTuniuIdsType());//途牛证件类型
//					cp.setTn_ticket_type(p.getTuniuTicketType());//途牛车票类型
//				}else{
//					logger.info("途牛改签，根据车票号找到不到信息，orderId:"+vendorOrderId);
//					throw new TuniuCommonException(CHANGE_NOT_EXIST_ERROR);
//				}
//				changePassengers.add(cp);
//			}
//			/*组装改签记录信息*/
//			changeInfo.setChange_travel_time(changeDateTime);//改签后乘车日期
//			changeInfo.setTrain_no(orderInfo.getTrainNo());//车次
//			changeInfo.setChange_train_no(changeCheCi);//改签后车次
//			changeInfo.setFrom_time(DateUtil.dateToString(orderInfo.getFromTime(), DateUtil.DATE_FMT3));//发车时间
//			changeInfo.setChange_from_time(changeDateTime);//改签后发车时间
//			changeInfo.setTravel_time(DateUtil.dateToString(orderInfo.getTravelDate(), DateUtil.DATE_FMT3));
//			changeInfo.setFrom_city(orderInfo.getFromCity());//出发车站
//			changeInfo.setTo_city(orderInfo.getToCity());//到达车站
//			changeInfo.setFrom_station_code(orderInfo.getFromCityCode());
//			changeInfo.setTo_station_code(orderInfo.getToCityCode());
//			changeInfo.setIschangeto(0);
//			changeInfo.setOut_ticket_billno(orderNumber);//12306单号
//			changeInfo.setOrder_id(vendorOrderId);
//			changeInfo.setIsasync("Y");//异步
//			changeInfo.setCallbackurl(callBackUrl);
//			changeInfo.setReqtoken(changeId);
//			changeInfo.setHasSeat(hasSeat?1:0);
//			if(isChild){
//				changeInfo.setChange_status(TRAIN_REQUEST_CHANGE_ARTIFICIAL);//11改签预定
//			}else{
//				changeInfo.setChange_status(TRAIN_REQUEST_CHANGE);//11改签预定
//			}
//			changeInfo.setcPassengers(changePassengers);//改签、车票关系
//			changeInfo.setBook_ticket_time(DateUtil.dateToString(new Date(), DateUtil.DATE_FMT3));
//			String acc_id = tuniuOrderDao.selectAccountByOrder(vendorOrderId);
//			changeInfo.setAccount_id(acc_id);//出票账号id
//			changeInfo.setChange_notify_count(0);
//			changeInfo.setChange_notify_status(CHANGE_NOTIFY_PRE);
//			changeInfo.setMerchant_id(TuniuConstant.merchantId);
//			/*改签信息入库*/
//			tuniuChangeDao.insertChangeInfo(changeInfo);
//			int change_id = changeInfo.getChange_id();
//			List<TuniuChangePassengerInfo> cPassengers = changeInfo.getcPassengers();
//			if (cPassengers != null && cPassengers.size() != 0) {
//				for (TuniuChangePassengerInfo cPassenger : cPassengers) {
//					cPassenger.setChange_id(change_id);
//					tuniuChangeDao.insertChangeCp(cPassenger);
//				}
//			}
//			log.setChange_id(change_id);
//			log.setOrder_id(vendorOrderId);
//			log.setContent("途牛请求改签成功");
		    logger.info("途牛请求改签成功,orderId : " + orderId );
		}catch (TuniuException e) {
			result.setCode(e.getMessage());
		} catch (Exception e) {
			logger.info("途牛请求改签异常"+e);
			log.setContent("途牛请求改签异常!");
			result.setCode(TuniuChangeService.APPLY_CHANGE_ERROR);
			e.printStackTrace();
		} finally {
			log.setOrder_id(orderId);
			log.setOpt_person("tuniu");
//			tuniuChangeDao.addChangeLog(log);
		}
		return result;
	}

	/*---------------------------dqh2015-11-23---------------------------------------------------*/
	
	
	
	
	//改签结算费率
	private  Double getDiffRate(String from_time, Date current) {
		Double rate = 0.0;//默认值
		
		Date from = DateUtil.stringToDate(from_time, DateUtil.DATE_FMT3);
		Date from_24hour = DateUtil.dateAddDays(from, -1);//24小时以内
		Date from_48hour = DateUtil.dateAddDays(from, -2);//48小时以内
		Date from_15Day = DateUtil.dateAddDays(from, -15);
		
		if(current.after(from_24hour)) {
			rate = 0.2;
		} else if(current.before(from_24hour) && current.after(from_48hour)) {
			rate = 0.1;
		} else if(current.before(from_48hour) && current.after(from_15Day)) {
			rate = 0.05;
		} else {
			
		}
		return rate;
	}
	private static Map<String, String> changeErrorInfo = new HashMap<String, String>();
	static {
		changeErrorInfo.put("1002","距离开车时间太近无法改签");
		changeErrorInfo.put("1004","取消改签次数超过上限,无法继续操作");
		changeErrorInfo.put("301","没有余票");
		changeErrorInfo.put("310","本次购票与其他订单冲突");
		changeErrorInfo.put("506","系统异常,无法正常占座操作");
		changeErrorInfo.put("313","订单内乘客已被法院依法限制高消费，禁止乘坐当前预订席别");
		changeErrorInfo.put("314","账号登陆失败");
		changeErrorInfo.put("999","未定义的12306错误");
		changeErrorInfo.put("9991","旅游票,请到车站办理");
		changeErrorInfo.put("308","存在未完成订单，请支付后再试");
		changeErrorInfo.put("316","开车前48小时（不含）以上，可改签预售期内其他列车；开车前48小时以内且非开车当日，可改签票面日期当日及以前的其他列车；开车当日，可改签票面日期当日的其他列车");
		changeErrorInfo.put("318","已退票，不可改签");
		changeErrorInfo.put("320","未找到改签待支付订单");
		changeErrorInfo.put("315","未找到要改签的车次");
		changeErrorInfo.put("317","已出票，请到车站办理");
		changeErrorInfo.put("319","已改签，不可再次改签");
		changeErrorInfo.put("322","当前的排队购票人数过多，请稍后重试");
		changeErrorInfo.put("324","已确认改签，不可取消");
	}
	private static Map<String, String> changeErrorCode = new HashMap<String, String>();
	static {
		changeErrorCode.put("506","1600101");
		changeErrorCode.put("999","1600101");
		changeErrorCode.put("1004","1600102");
		changeErrorCode.put("1002","1600103");
		changeErrorCode.put("315","1600104");
		changeErrorCode.put("314","1600106");
		changeErrorCode.put("317","1600107");
		changeErrorCode.put("319","1600108");
		changeErrorCode.put("318","1600109");
		changeErrorCode.put("9991","1600110");
		changeErrorCode.put("301","1600114");
		changeErrorCode.put("322","1600115");
		changeErrorCode.put("313","1600116");
		changeErrorCode.put("308","1600117");
		changeErrorCode.put("316","1600119");
		changeErrorCode.put("310","1600120");
		changeErrorCode.put("324","1600302");
		changeErrorCode.put("320","1600203");
	}
	private  String getChangeErrorInfo(String errorCode) {
		String errorInfo = changeErrorInfo.get(errorCode);
		return errorInfo;
	}
	private  String getChangeErrorCode(String errorCode) {
		String errorInfo = changeErrorCode.get(errorCode);
		return errorInfo;
	}
	private Map<String,Object>  queryTime(TuniuChangeInfo changeInfo){
		// 针对没有to_time采用查询补全
		Map<String,Object> timeResult = new HashMap<String,Object>();
		try{
			//查询数据库
			Map<String, Object> queryParam = new HashMap<String, Object>();
			queryParam.put("trainCode", changeInfo.getChange_train_no());
			queryParam.put("name", changeInfo.getFrom_city());
			logger.info(changeInfo.getOrder_id()+ ","+queryParam);
			Station fromStation = tuniuCommonDao.selectOneStation(queryParam);
			queryParam.put("name",changeInfo.getTo_city());
			logger.info(changeInfo.getOrder_id()+ ","+queryParam);
			Station toStation = tuniuCommonDao.selectOneStation(queryParam);
			String start_query_time = null;
			String arrive_query_time = null;
			String arrive_days = null;
			
			logger.info(changeInfo.getOrder_id() + " 途牛数据库时间："+fromStation.getStartTime()+","+toStation.getArriveTime());
			if(fromStation == null || toStation == null) {
				logger.info(changeInfo.getOrder_id()  + " 途牛改签回调-数据库时间补全失败!");

				// 针对没有to_time采用查询补全
				Map<String, String> paramMap = new HashMap<String, String>();
				String url = "http://10.3.12.95:18013/queryTicket";
				paramMap.put("channel", "ext_tuniu");//途牛渠道,改签补全时间,渠道名必须以ext开头
				paramMap.put("from_station", changeInfo.getFrom_city());
				paramMap.put("arrive_station", changeInfo.getTo_city());
				paramMap.put("travel_time", changeInfo.getChange_from_time().split(" ")[0]);
				paramMap.put("purpose_codes", "ADULT");
				/*paramMap.put("train_code", changeInfo.getChange_train_no());*/
	
				String params="";
				params = UrlFormatUtil.createUrl("", paramMap);
				logger.info(changeInfo.getOrder_id()+" 途牛发起余票查询" + paramMap.toString() + "url" + url);
				String result = HttpUtil.sendByPost(url, params, "UTF-8");
				logger.info(changeInfo.getOrder_id()+ " 途牛发起余票查询，查询结果"+result);

				String runtime = null;
				if(result == null || result == ""|| result.equalsIgnoreCase("STATION_ERROR")|| result.equalsIgnoreCase("ERROR")){
					result = HttpUtil.sendByPost(url, params, "UTF-8");
					if(result == null || result == ""|| result.equalsIgnoreCase("STATION_ERROR")|| result.equalsIgnoreCase("ERROR")){
						result = HttpUtil.sendByPost(url, params, "UTF-8");
					}
				}
				if (result == null || result == ""|| result.equalsIgnoreCase("STATION_ERROR")|| result.equalsIgnoreCase("ERROR")) {
					logger.info(changeInfo.getOrder_id()+"途牛改签回调通知,针对没有to_time采用查询补全_查询失败");
				} else {
					try{
						JSONObject dataTicket=JSONObject.fromObject(result);
						JSONArray arr =dataTicket.getJSONArray("datajson");
						int index = arr.size();
						for (int i = 0; i < index; i++) {
							if (arr.getJSONObject(i).get("station_train_code").equals(changeInfo.getChange_train_no())) {
								runtime = arr.getJSONObject(i).getString("lishiValue");
								start_query_time = arr.getJSONObject(i).getString("start_time");
								arrive_query_time = arr.getJSONObject(i).getString("arrive_time");
								arrive_days = arr.getJSONObject(i).getString("day_difference");
								break;
							}
						}
						if (start_query_time == null|| arrive_query_time == null || runtime == null) {
							logger.info("途牛改签回调通知,针对没有to_time采用查询补全_查询失败,未匹配到结果");
						} else {
							logger.info("途牛改签回调通知,针对没有to_time采用查询补全_查询成功"+ start_query_time + "_"+ arrive_query_time + "_" + runtime);
							timeResult.put("start_time", start_query_time);
							timeResult.put("arrive_time", arrive_query_time);
						}
					}catch(JSONException e1){
						logger.info(changeInfo.getOrder_id()+"途牛改签回调通知,针对没有to_time采用查询补全_查询异常, result="+result,e1);
					}
				}
			
			} else {
				logger.info(changeInfo.getOrder_id() + " 途牛数据库时间补全成功："+fromStation.getStartTime()+","+toStation.getArriveTime());
				timeResult.put("start_time",  fromStation.getStartTime());
				timeResult.put("arrive_time", toStation.getArriveTime());
			}
		} catch (Exception e) {
			logger.info(changeInfo.getOrder_id() +",途牛改签回调补全时间发生异常",e);
		}
		
		logger.info(changeInfo.getOrder_id() + "timeResult::"+timeResult);
		
		return timeResult;
	}
	
	
}
