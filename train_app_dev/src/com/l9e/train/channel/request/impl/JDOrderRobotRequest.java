package com.l9e.train.channel.request.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.l9e.train.po.Account;
import com.l9e.train.po.JDOrderCP;
import com.l9e.train.po.JDOrderVo;
import com.l9e.train.po.JDReturnVO;
import com.l9e.train.po.JdAcc;
import com.l9e.train.po.JdPrePayCard;
import com.l9e.train.po.Order;
import com.l9e.train.po.RequestParam;
import com.l9e.train.po.Result;
import com.l9e.train.po.Worker;
import com.l9e.train.service.impl.SysSettingServiceImpl;
import com.l9e.train.service.impl.TrainServiceImpl;
import com.l9e.train.util.ConfigUtil;
import com.l9e.train.util.DateUtil;
import com.l9e.train.util.HttpUtil;
import com.unlun.commons.exception.DatabaseException;
import com.unlun.commons.exception.RepeatException;

@Deprecated
public class JDOrderRobotRequest extends RequestImpl{

	private Logger logger = LoggerFactory.getLogger(JDOrderRobotRequest.class);
	
	public JDOrderRobotRequest(Account account,Worker worker,JdAcc jdAcc,List<JdPrePayCard> jdPrePayCardList) {
		super(account, worker,jdAcc,jdPrePayCardList);
	}
	
	
	@Override
	public Result request(Order order, String weight, String logid) {
		String logPre = "[京东出票]";
	
		Date date = new Date();
		String datePre = DateUtil.dateToString(date, "yyyyMMdd");		
		Date begin = DateUtil.stringToDate(datePre + "060000", "yyyyMMddHHmmss");//6:00
		Date end = DateUtil.stringToDate(datePre + "230000", "yyyyMMddHHmmss");//23:00
		if(date.before(begin) || date.after(end)){
			logger.info(order.getOrderId()+"【向机器人发送预订请求】发送时间在23:00~06:00之间，不予发送！");
			result.setRetValue(Result.MANUAL);
			return result;
		}
		//查询系统设置的预订机器人版本
		SysSettingServiceImpl sysImpl = new SysSettingServiceImpl();
		try {
			sysImpl.queryLogList();
		}catch(Exception e){
			logger.error(logPre+"获取出票系统日志异常！",e);
		}
		String robotVer = "0";//预订机器人版本 0：旧版预订机器人 1：新版预订机器人 默认是旧版
		//重发日志
		try {
			logger.info(logPre+"start RobotOrderRequest orderid:"+order.getOrderId());
			
			sysImpl.querySysVal("book_robot_version");
			
			if(StringUtils.isNotEmpty(sysImpl.getSysVal())){
				robotVer = sysImpl.getSysVal();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String randCodeType = "0";//打码方式：0、手动打码 1、机器识别
		sysImpl = new SysSettingServiceImpl();
		try {
			sysImpl.querySysVal("rand_code_type");
			if(StringUtils.isNotEmpty(sysImpl.getSysVal())){
				randCodeType = sysImpl.getSysVal();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info(order.getOrderId()+logPre+(randCodeType.equals("0")?"human input randcode":"robot input randcode"));
	
		//在此开始封装请求机器人参数
		RequestParam requestParam = new RequestParam();
		TrainServiceImpl service = new TrainServiceImpl();
		
		//超时时长从后台配置查询得出
		try {
			sysImpl.querySysVal("book_timeout_times");
		} catch (RepeatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String timeOut = sysImpl.getSysVal();
		
		 //在此获取订单中出发城市和到达城市的三字码 
		String fromCity = order.getFrom();
		logger.info(order.getOrderId()+"订单中出发城市为：" + fromCity);
		String toCity = order.getTo();
		logger.info(order.getOrderId()+"订单中到达城市为：" + toCity);
		String fromCity3c = "";//出发城市三字码
		String toCity3c = "";//到达城市三字码
		if ((null != fromCity && "" != fromCity)
				&& (null != toCity && "" != toCity)) {
				Order orderCity3c = new Order();
				// 查询出包含站点城市和三字码的order实体
				try {
					service.queryOrderCity3c(fromCity, toCity);
				} catch (RepeatException e) {
					logger.info("queryOrderCity3c：" + e.toString());
					e.printStackTrace();
				} catch (DatabaseException e) {
					logger.info("queryOrderCity3c2：" + e.toString());
					e.printStackTrace();
				} 
				orderCity3c = service.getOrder3c();
				if (null != orderCity3c) {
					fromCity3c = orderCity3c.getFromCity_3c();
					toCity3c = orderCity3c.getToCity_3c();
					logger.info(order.getOrderId()+"从数据库中取出的出发三字码---"+fromCity3c);
					logger.info(order.getOrderId()+"从数据库中取出的到达三字码---"+toCity3c);
				}

		}
		
		//京东预付卡（可能有多张）
		List<JdPrePayCard> prePayCardList = new ArrayList<JdPrePayCard>();
	
		for(JdPrePayCard jdPrePayCard : jdPrePayCardList){
			JdPrePayCard payCard = new JdPrePayCard();
			payCard.setCardNo(jdPrePayCard.getCardNo());
			payCard.setCardPwd(jdPrePayCard.getCardPwd());
			payCard.setCardMoney(jdPrePayCard.getCardMoney());
			
			prePayCardList.add(payCard);
		}
		
		//京东订单乘客信息列表
		List<JDOrderCP> passengersList = new ArrayList<JDOrderCP>();
		List<String> orderCps = order.getOrderCps();
		String contacts = null;
		for (String jdOrderCP : orderCps) {
			// 车票类型0：成人票 1：儿童票
			// cp_id|user_name|ticket_type|cert_type|cert_no|seat_type
			// 证件类型１、一代身份证、２、二代身份证、３、港澳通行证、４、台湾通行证、５、护照
			String[] cps = jdOrderCP.split("\\|");
			JDOrderCP cp = new JDOrderCP();
			cp.setCpId(cps[0]);
			cp.setName(cps[1]);
			cp.setIdType(cps[3]);
			cp.setIdNo(cps[4]);
			cp.setSeatType(cps[5]);
			
			contacts = cp.getName();//京东订票成功后接收短信的联系人姓名,任何一个乘客都行
			passengersList.add(cp);
		}
		
		/**
		 * 京东预定是否使用19e的出票账号开关: 0:是 1:否
		 * 如果用咱们的出票账号，则给账号赋值，
		 * 如果不使用，则赋值为null
		 */
		String jdbookAccountSwitch = "1";//0:是  1:否
		sysImpl = new SysSettingServiceImpl();
		try {
			sysImpl.querySysVal("jdbook_account_switch");
			
			if(StringUtils.isNotEmpty(sysImpl.getSysVal())){
				jdbookAccountSwitch = sysImpl.getSysVal();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info(order.getOrderId()+" 京东预定是否使用19e的出票账号开关为："+jdbookAccountSwitch);
		
		//开始给请求参数赋值
		requestParam.setTimeOut(timeOut);//超时时间
		requestParam.setTrainCode(order.getTrainno());//车次
		requestParam.setTrainDate(order.getTravel_time());//乘车日期
		requestParam.setFromName(order.getFrom());//出发站
		requestParam.setToName(order.getTo());//到达站
		requestParam.setFromCode(fromCity3c);//出发站三字码
		requestParam.setToCode(toCity3c);//到达站三字码
		requestParam.setPayMoney(order.getPaymoney());//支付金额
		
		if("0".equals(jdbookAccountSwitch)){
			
		requestParam.setRailwayName(account.getUsername());//12306用户名
		requestParam.setRailwayPwd(account.getPassword());//12306账号登录密码
		}else if("1".equals(jdbookAccountSwitch)){
			
			requestParam.setRailwayName(null);//12306用户名
			requestParam.setRailwayPwd(null);//12306账号登录密码
		}
		requestParam.setChannalName(jdAcc.getAccountName());//京东账号名
		requestParam.setChannalPwd(jdAcc.getAccountPwd());//京东账户登录密码
		requestParam.setChannalPayPwd(jdAcc.getAccountPaypwd());//京东账户支付密码
		requestParam.setCouponNo(jdAcc.getCouponNo());//优惠券编号
		requestParam.setPhone(jdAcc.getAccountName());//京东订票成功后接收短信的手机号  和京东账号名相同
		requestParam.setContacts(contacts);//京东订票成功后接收短信的联系人姓名  
		requestParam.setPassengers(passengersList);//订单中的乘客车票信息列表
		requestParam.setCards(prePayCardList);//匹配到的京东预付卡列表
		
		//把请求参数对象封装成jsonObject
		String paramsJsonStr = JSONObject.toJSONString(requestParam);
			
		logger.info(order.getOrderId()+" start post request params:"+paramsJsonStr+" orderUrl:"+worker.getWorkerExt());
		
		//String url =worker.getWorkerExt();
		String url = ConfigUtil.getValue("jdRobotUrl");
		logger.info(order.getOrderId()+" 京东预定请求url为："+url);
		String reqResult = HttpUtil.sendByPostforJD(url, paramsJsonStr, "UTF-8");
		
		reqResult = reqResult.replace("[\"{", "[{");
		reqResult = reqResult.replace("}\"]", "}]");
		reqResult = reqResult.replace("\\", "");
		
		logger.info(order.getOrderId()+" reValue:"+reqResult);
		
		//只要有返回值，就解锁京东预付卡
		try {
			service.unLockJDPreCardInfos(jdPrePayCardList);
		} catch (RepeatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (DatabaseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(null == reqResult){
			logger.error(logPre+"post request Error retValue:"+reqResult);
			result.setErrorInfo("预定错误，没有返回结果！【"+reqResult+"】");
			result.setRetValue(Result.RESEND);
			return result;
		}
			
		JDReturnVO retObj = null;
		try {
			ObjectMapper map = new ObjectMapper();
			retObj = map.readValue(reqResult, JDReturnVO.class);
		} catch (Exception e) {
			logger.error(logPre+"json request exception retValue:"+reqResult);
			result.setErrorInfo("JSON分析异常【"+reqResult+"】");
			result.setRetValue(Result.RESEND);
			return result;
		}
			
		//重发
		if(new Integer(10).equals(retObj.getErrorCode())){
			result.setRetValue(Result.RESEND);
			result.setErrorInfo("【"+reqResult+"】");
			return result;
		}
		
		//切换12306账号后重发
		if(new Integer(11).equals(retObj.getErrorCode())){
			result.setRetValue(Result.CHANGE_ACCOUNT_RESEND);
			result.setErrorInfo("【"+reqResult+"】");
			return result;
		}
		
		//切换京东账号后重发
		if(new Integer(12).equals(retObj.getErrorCode())){
			result.setRetValue(Result.CHANGE_JDACCOUNT_RESEND);
			result.setErrorInfo("【"+reqResult+"】");
			return result;
		}
		
		//切换京东预付卡后重发
		if(new Integer(13).equals(retObj.getErrorCode())){
			result.setRetValue(Result.CHANGE_JDCARD_RESEND);
			result.setErrorInfo("【"+reqResult+"】");
			return result;
		}
		
		if(retObj.getErrorInfo().size()>0){
			JDOrderVo val = retObj.getErrorInfo().get(0);
			String retValue = val.getRetValue();
			String retInfo = val.getRetInfo();
			result.setJdOrderId(val.getJdOrderId());
			
			if("success".equalsIgnoreCase(retValue)){
				//出票请求成功
				result.setRetValue(Result.SUCCESS);
				result.setPayMoney(val.getPayMoney());
				result.setJdOrderNo(val.getJdOrderNo());
				result.setJdPayMoney(val.getJdPayMoney());
				result.setJdRebateTicketId(val.getJdRebateTicketId());//优惠券编号
				result.setJdRebateTicketMoney(val.getJdRebateTicketMoney());
				result.setKltOrderNo(val.getKltOrderNo());//开联通流水号
				return result;
			}else{
				//转人工预订
				result.setErrorInfo(retInfo);
				result.setRetValue(Result.MANUAL);
				return result;
			}
			
		}else{
			result.setRetValue(Result.MANUAL);
			result.setErrorInfo("【"+reqResult+"】");
			return result;
		}	
		
	}
	
}
