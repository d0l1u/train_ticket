
package com.l9e.transaction.channel.request.impl;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import com.l9e.common.Consts;
import com.l9e.transaction.service.impl.SysSettingServiceImpl;
import com.l9e.transaction.service.impl.TrainServiceImpl;
import com.l9e.transaction.vo.Account;
import com.l9e.transaction.vo.DeviceWeight;
import com.l9e.transaction.vo.ErrorInfo;
import com.l9e.transaction.vo.IpInfo;
import com.l9e.transaction.vo.Order;
import com.l9e.transaction.vo.Passenger;
import com.l9e.transaction.vo.Result;
import com.l9e.transaction.vo.ReturnOptlog;
import com.l9e.transaction.vo.ReturnVO;
import com.l9e.transaction.vo.Worker;
import com.l9e.util.DateUtil;
import com.l9e.util.MD5;
import com.l9e.util.MemcachedUtil;
import com.l9e.util.PostRequestUtil;
import com.l9e.util.StrUtil;
import com.l9e.util.UrlFormatUtil;
import com.unlun.commons.exception.DatabaseException;
import com.unlun.commons.exception.RepeatException;


/**
 * 机器人预定类
 * @author guobin
 *
 */
public class RobotOrderRequest extends RequestImpl{
	
	private Logger logger=Logger.getLogger(this.getClass());
		
	public RobotOrderRequest(Account account, Worker worker) {
		super(account, worker);
	}
	
	@Override
	public Result request(Order order,String weight) {
		
		TrainServiceImpl service = new TrainServiceImpl();
		
		
		//生成一个随机的16位的字符串(设备号）
		//String deviceNo=RandomStringUtils.randomAlphanumeric(16);
		String deviceNo=StrUtil.getRandomString(16);
		Date date = new Date();
		String datePre = DateUtil.dateToString(date, "yyyyMMdd");		
		Date begin = DateUtil.stringToDate(datePre + "060000", "yyyyMMddHHmmss");//6:00
		Date end = DateUtil.stringToDate(datePre + "230000", "yyyyMMddHHmmss");//23:00
		if(date.before(begin) || date.after(end)){
			logger.info("【向机器人发送预订请求】发送时间在23:00~06:00之间，不予发送！");
			result.setRetValue(Result.MANUAL);
			return result;
		}
		
		/**
		 * 在此先判断订单是否自带12306账号
		 * 如果订单中没带12306的账号密码，则跳过下面一步的流程
		 * 账号来源： 0：公司自有账号 ； 1：12306自带账号
		 */
		Integer accountFromWay = order.getAccountFromWay();
		if (null != accountFromWay && 1 == accountFromWay) {

			List<Passenger> passengers = Consts.orderService.getPassengersByOrderId(order.getOrderId());
			String errorCode = "1"; // 登录验证返回的错误码信息。 默认核验成功 1：成功
			if (passengers != null && passengers.size() > 0) {

				// 把订单下面的多个身份证号用","分隔，拼成一个全新的字符串。
				StringBuilder passportNos = new StringBuilder();
				String passportNo = null;
				for (int i = 0; i < passengers.size(); i++) {
					passportNo = passengers.get(i).getPassportNo();
					passportNos.append(passportNo);
					passportNos.append(",");
				}
				errorCode = Consts.accountService.handleBindAccErrorCode(account.getUsername(), account.getPassword(),passportNos.toString());
			}
			
			if("0".equals(errorCode)){
				logger.info("登录核验发生未知错误，预定失败！");
				result.setErrorInfo("自带12306账号订单，登录核验发生未知错误！");
				result.setFailReason("登录核验发生未知错误，预定失败！");
				result.setRetValue(Result.FAILURE);
				return result;
			} else if ("21".equals(errorCode)) {
				logger.info("传入12306账号未进行手机核验，预定失败！");
				result.setErrorInfo("自带12306账号订单，传入的12306账号未进行手机核验！");
				result.setFailReason(ErrorInfo.MOBILEPHONE_NO_VERIFY);
				result.setRetValue(Result.FAILURE);
				return result;
			} else if ("22".equals(errorCode)) {
				logger.info("传入12306账号 用户达上限，预定失败！");
				result.setErrorInfo("自带12306账号订单，传入的12306账号用户达上限！");
				result.setFailReason(ErrorInfo.USERNUMBERS_OVER_TOPLIMIT);
				result.setRetValue(Result.FAILURE);
				return result;
			}else if("25".equals(errorCode)){
				logger.info("传入的12306账号待核验，预定失败！");
				result.setErrorInfo("自带12306账号订单，传入的12306账号待核验！");
				result.setFailReason(ErrorInfo.ACCOUNT_WAIT_VERIFY);
				result.setRetValue(Result.FAILURE);
				return result;
			}else if("26".equals(errorCode)){
				logger.info("传入的12306账号不存在，预定失败！");
				result.setErrorInfo("自带12306账号订单，传入的12306账号不存在！");
				result.setFailReason(ErrorInfo.ACCOUNT_NO_EXIST);
				result.setRetValue(Result.FAILURE);
				return result;
			}else if("27".equals(errorCode)){
				logger.info("传入的12306账号登录密码错误，预定失败！");
				result.setErrorInfo("自带12306账号订单，传入的12306账号登录密码错误！");
				result.setFailReason(ErrorInfo.PASSWORD_WRONG);
				result.setRetValue(Result.FAILURE);
				return result;
			}

		}
		//查询系统设置的预订机器人版本
		SysSettingServiceImpl sysImpl = new SysSettingServiceImpl();
		try {
			sysImpl.queryLogList();
		}catch(Exception e){
			logger.error("获取出票系统日志异常！",e);
		}
		List<ReturnOptlog> list_return = sysImpl.getList_return();
		
		int resend_num = 0;
		if(null != MemcachedUtil.getInstance().getAttribute("resend_times_"+order.getOrderId())){
			resend_num = Integer.valueOf(String.valueOf(MemcachedUtil.getInstance().getAttribute("resend_times_"+order.getOrderId())));
			logger.info(order.getOrderId()+" resend_num:"+resend_num);
		}
		
		int fail_num = 0;
		if(null != MemcachedUtil.getInstance().getAttribute("fail_times_"+order.getOrderId())){
			fail_num = Integer.valueOf(String.valueOf(MemcachedUtil.getInstance().getAttribute("fail_times_"+order.getOrderId())));
			logger.info(order.getOrderId()+" fail_num:"+fail_num);
		}
		
		int manual_num = 0;
		if(null != MemcachedUtil.getInstance().getAttribute("manual_times_"+order.getOrderId())){
			manual_num = Integer.valueOf(String.valueOf(MemcachedUtil.getInstance().getAttribute("manual_times_"+order.getOrderId())));
			logger.info(order.getOrderId()+" manual_num:"+manual_num);
		}
		
		int wait_num = 0;
		if(null != MemcachedUtil.getInstance().getAttribute("wait_times_"+order.getOrderId())){
			wait_num = Integer.valueOf(String.valueOf(MemcachedUtil.getInstance().getAttribute("wait_times_"+order.getOrderId())));
			logger.info(order.getOrderId()+" wait_num:"+wait_num);
		}
		
		String robotVer = "0";//预订机器人版本 0：旧版预订机器人 1：新版预订机器人 默认是旧版
		//重发日志
		try {
			logger.info("start RobotOrderRequest orderid:"+order.getOrderId());
			
			sysImpl.querySysVal("book_robot_version");
			
			if(StringUtils.isNotEmpty(sysImpl.getSysVal())){
				robotVer = sysImpl.getSysVal();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Map<String, String> maps = new HashMap<String,String>();
		
		//在此获取学生票预定类型的开关
		String preStudentTicketSelect = "javaApp";//学生票预定类型  javaApp:走java版脚本预定   luaPC：走lua脚本预定
		boolean flag = false; //是否走java版脚本的预定url  默认不走
		sysImpl = new SysSettingServiceImpl();
		try {
			sysImpl.querySysVal("pre_studentTicket_select");
			
			if(StringUtils.isNotEmpty(sysImpl.getSysVal())){
				preStudentTicketSelect = sysImpl.getSysVal();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("学生票预定类型脚本为："+preStudentTicketSelect);
		
		if("0".equals(robotVer)){
			maps.put("ScriptPath", "12306.lua");//执行的脚本路径
			logger.info("request lua script:12306.lua");
		}else{
			String scriptPath = "12306new.lua";
			
			//在此判断，如果是学生票的话则走pc端，否则都走app端
			//orderCp= cpId|userName|trainType|certType|certNo
			String trainType = "0"; // 火车票类型 0：成人票 1：儿童票 3：学生票
			for (String orderCp : order.getOrderCps()) {
				String[] orderCpArr = orderCp.split("\\|");
				if ("" != orderCpArr[2] && null != orderCpArr[2]) {
					trainType = orderCpArr[2];
				}
			}
			if(DeviceWeight.WEIGHT_PC.equals(weight)) {
				/*PC出票*/
				try {
					service.updateDevice(order.getOrderId(), 0);
				} catch(Exception e) {
					e.printStackTrace();
				}
			} else if(DeviceWeight.WEIGHT_APP.equals(weight)) {
				/*APP出票*/
				try {
					service.updateDevice(order.getOrderId(), 1);
				} catch(Exception e) {
					e.printStackTrace();
				}
				
				if (!("3".equals(trainType))) {
					scriptPath = "app12306.lua";
				} else {

					if ("javaApp".equals(preStudentTicketSelect)) {
						flag = true;
					}
				}
			}
			
			
			maps.put("ScriptPath", scriptPath);//执行的脚本路径
			logger.info("request lua script:" + scriptPath);
			try {
				service.insertHistory(order.getOrderId(), "本次预订使用设备: " + weight);
			} catch (RepeatException e) {
				e.printStackTrace();
			} catch (DatabaseException e) {
				e.printStackTrace();
			}
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
		
		/*时间间隔参数*/
		
		
		logger.info(randCodeType.equals("0")?"human input randcode":"robot input randcode");
		
		maps.put("commond", "trainOrder");//java版脚本学生票预定
		
		Integer workerLangType = worker.getWorkerLangType(); // 1：lua脚本语言 2：java脚本语言
		logger.info("机器脚本语言类型为：" + workerLangType);
		
		if (workerLangType == 2) {
			
			//添加代理IP开关
			String proxyIPSwitch = "0";//0：代理IP打开   1：代理IP关闭
			sysImpl = new SysSettingServiceImpl();
			try {
				sysImpl.querySysVal("proxyIP_switch");
				
				if(StringUtils.isNotEmpty(sysImpl.getSysVal())){
					proxyIPSwitch = sysImpl.getSysVal();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if ("0".equals(proxyIPSwitch)) {
				IpInfo ipInfo = Consts.ipInfoService.getIpInfoByType(IpInfo.TYPE_BOOKIP);
				// IpInfo ipInfo=null;
				logger.info("根据IP类型获取的预定代理IP对象为：" + ipInfo);

				if (ipInfo != null) {
					maps.put("proxyIP", ipInfo.getIpExt()); // 代理IP
					logger.info("根据IP类型获取的预定代理IP地址为：" + ipInfo.getIpExt());
				}
			}
		}
		
		maps.put("SessionID", String.valueOf(System.currentTimeMillis()));//任务完成后，机器人回应该请求时携带
		maps.put("Timeout", "240000");//供货商价格
		maps.put("ParamCount", String.valueOf(order.getOrderCps().size()+1));
		
		//机器人返回信息改造
		StringBuffer sb = new StringBuffer();
		sb.append(account.getUsername()+"|");
		sb.append(account.getPassword()+"|");
		sb.append(order.getOrderstr()+"|");
		sb.append(randCodeType+"|");
		sb.append(worker.getWorkerId() + "|");
		sb.append(timeIntervalParam(service));
		//测试用
//        String json="{\"order_button_time\":0,\"login_time\":0,\"order_time\":0,\"account_time\":0,\"adduser_time\":0,\"getuser_time\":0,\"submit_time\":0}";
//		sb.append(json);
		/*
		 * 在此判断weight权重是否是app端！   0：PC端  100：app端
		 * 如果走的是app端，则在Param1里加上设备号deviceNo
		 */
//		if(DeviceWeight.WEIGHT_APP.equals(weight)){
//			
//			sb.append("|").append(deviceNo);
//						
//		}
		
		//放开限制
		sb.append("|").append(deviceNo);
		
		//在此获取订单中出发城市和到达城市的三字码  add by wangsf
		String fromCity = order.getFrom();
		logger.info("订单中出发城市为：" + fromCity);
		String toCity = order.getTo();
		logger.info("订单中到达城市为：" + toCity);

		String fromCity3c = order.getFromCity_3c();
		logger.info("订单中出发城市三字码为：" + fromCity3c);
		String toCity3c = order.getToCity_3c();
		logger.info("订单中到达城市三字码为：" + toCity3c);
		if ((null != fromCity3c && "" != fromCity3c)
				&& (null != toCity3c && "" != toCity3c)) {
			sb.append("|").append(fromCity3c);
			sb.append("|").append(toCity3c);

		} else {

			if ((null != fromCity && "" != fromCity)
					&& (null != toCity && "" != toCity)) {

				Object fromCity3cOb = null;
				Object toCity3cOb = null;
				try {
					fromCity3cOb = Consts.redisDao.getCacheVal(MD5.getMd5_UTF8(fromCity));

				} catch (Exception e) {
					e.printStackTrace();
				}

				if (null != fromCity3cOb) {
					fromCity3c = fromCity3cOb.toString(); // redis缓存中出发城市三字码
					logger.info("redis缓存中出发城市三字码为：" + fromCity3c);
				}

				try {
					toCity3cOb = Consts.redisDao.getCacheVal(MD5.getMd5_UTF8(toCity));

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (null != toCity3cOb) {
					toCity3c = toCity3cOb.toString(); // redis缓存中到达城市三字码
					logger.info("redis缓存中到达城市三字码为：" + toCity3c);
				}
			
				if ((null != fromCity3c && "" != fromCity3c)
						&& (null != toCity3c && "" != toCity3c)) {
					sb.append("|").append(fromCity3c);
					sb.append("|").append(toCity3c);
				} else {
					
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
					logger.info("从数据库中取出三字码");
					orderCity3c = service.getOrder3c();
					if (null != orderCity3c) {
						fromCity3c = orderCity3c.getFromCity_3c();
						toCity3c = orderCity3c.getToCity_3c();

						if (null != fromCity3c && "" != fromCity3c) {
							Consts.redisDao.setCacheVal(MD5.getMd5_UTF8(fromCity), fromCity3c);
							sb.append("|").append(fromCity3c);
							logger.info("数据库查询的出发城市三字码为：" + fromCity3c);

						}

						if (null != toCity3c && "" != toCity3c) {
							Consts.redisDao.setCacheVal(MD5.getMd5_UTF8(toCity), toCity3c);
							sb.append("|").append(toCity3c);
							logger.info("数据库查询的到达城市三字码为：" + toCity3c);
						}

					}
				}
			}
		}
		logger.info("最终的出发城市的三字码为：" + fromCity3c);
		logger.info("最终的到达城市的三字码为：" + toCity3c);
		
		//新增动态库或黑盒调用参数
		String dynamicDepotOrBlackBox = "0";//动态库或黑盒选择  0：动态库  1：黑盒
		sysImpl = new SysSettingServiceImpl();
		try {
			sysImpl.querySysVal("dynamicDepot_or_blackBox");
			
			if(StringUtils.isNotEmpty(sysImpl.getSysVal())){
				dynamicDepotOrBlackBox = sysImpl.getSysVal();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		sb.append("|").append(dynamicDepotOrBlackBox);
		
		maps.put("Param1", sb.toString());
		//maps.put("Param1", account.getUsername()+"|"+account.getPassword()+"|"+order.getOrderstr()+"|"+randCodeType+"|"+worker.getWorkerId() + "|" + timeIntervalParam(service)+"|"+deviceNo);
		int i;
		try {
			i = 2;
			if("tongcheng".equals(order.getChannel())){
				for (String param : order.getOrderCps()) {
					//学生票处理sql.append("select cp_id, CONCAT(cp_id, '|', user_name, '|',CONVERT(ticket_type,CHAR), '|',CONVERT(cert_type,CHAR), '|',cert_no, '|',seat_type) ,ticket_type from cp_orderinfo_cp where order_id=?");
					String[] paramArr=param.split("\\|");
					if("3".equals(paramArr[2])){
						service.queryStudentInfo(order.getOrderId(),paramArr[0],param);
						param=service.getParam();
						logger.info("student param:"+param);
					}
					maps.put("Param"+i, param );
					i++;
				}
			}else{
				for (String param : order.getOrderCps()) {
					//学生票处理
					param=param +"|"+order.getSeatType();
					String[] paramArr=param.split("\\|");
					if("3".equals(paramArr[2])){
						service.queryStudentInfo(order.getOrderId(),paramArr[0],param);
						param=service.getParam();
					}
					maps.put("Param"+i, param );
					i++;
				}
			}
		} catch (Exception e2) {
			e2.printStackTrace();
			logger.info("student send Exception"+e2);
			i = 2;
			if("tongcheng".equals(order.getChannel())){
				for (String param : order.getOrderCps()) {
					maps.put("Param"+i, param );
					i++;
				}
			}else{
				for (String param : order.getOrderCps()) {
					maps.put("Param"+i, param +"|"+order.getSeatType());
					i++;
				}
			}
		}
		
		
		
		String param=null;
		try {
			param = UrlFormatUtil.createUrl("", maps, i);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		logger.info("start post request params:"+param+" orderUrl:"+worker.getWorkerExt());
		
		
		//String reqResult=PostRequestUtil.getPostRes("UTF-8", worker.getWorkerExt(), param);
		String reqResult = null;
		String javaAppUrl = "http://43.241.225.144:8091/RunScript";
		if (flag) {
			reqResult = PostRequestUtil.getPostRes("UTF-8", javaAppUrl, param);
		} else {
			reqResult = PostRequestUtil.getPostRes("UTF-8", worker.getWorkerExt(), param);
		}
		//测试用
//		String url="http://43.241.225.69:8091/RunScript";
//		String reqResult=PostRequestUtil.getPostRes("UTF-8", url, param);
		reqResult = reqResult.replace("[\"{", "[{");
		reqResult = reqResult.replace("}\"]", "}]");
		reqResult = reqResult.replace("\\", "");
		
		
		logger.info("reValue:"+reqResult);
		
		if(null == reqResult){
			logger.error("post request Error retValue:"+reqResult);
			result.setErrorInfo("预定错误，没有返回结果！【"+reqResult+"】");
			result.setReturnOptlog(ReturnOptlog.NORESULT);
			if(resend_num>=5){
				result.setRetValue(Result.MANUAL);
				return result;
			}else{
				MemcachedUtil.getInstance().setAttribute("resend_times_"+order.getOrderId(), ++resend_num, 90*1000);
				result.setRetValue(Result.RESEND);
				return result;
			}
		}
		
		//请求错误
		if(reqResult.equals(PostRequestUtil.TIME_OUT)||reqResult.equals(PostRequestUtil.CONNECT_ERROR)|reqResult.equals(PostRequestUtil.URL_ERROR)){
			logger.error("post request Error retValue:"+reqResult);
			if(reqResult.contains("CONNECTERROR")){
				result.setReturnOptlog(ReturnOptlog.SOS);
				result.setErrorInfo("机器异常，请尽快进入后台机器管理，针对该机器人进行【短信停用】！");
				result.setRetValue(Result.MANUAL);
				return result;
			}else{
				result.setReturnOptlog(ReturnOptlog.TIMEOUT);
				result.setErrorInfo("机器预定超时，自动重发！【"+reqResult+"】");
			}
			if(resend_num>=5){
				result.setRetValue(Result.MANUAL);
				return result;
			}else{
				MemcachedUtil.getInstance().setAttribute("resend_times_"+order.getOrderId(), ++resend_num, 90*1000);
				result.setRetValue(Result.RESEND);
				return result;
			}
		}
		
		//机器人处理超时
		if(reqResult.contains("\"ErrorCode\":9")){
			logger.error("robot request Error retValue:"+reqResult);
			result.setErrorInfo("机器人处理超时【"+reqResult+"】处理：请核对12306网站是否已经订购！");
			result.setReturnOptlog(ReturnOptlog.TIMEOUT);
			if(resend_num>=5){
				result.setRetValue(Result.MANUAL);
				return result;
			}else{
				MemcachedUtil.getInstance().setAttribute("resend_times_"+order.getOrderId(), ++resend_num, 90*1000);
				result.setRetValue(Result.RESEND);
				return result;
			}
		}
		
		ReturnVO retObj = null;
		try {
			ObjectMapper map = new ObjectMapper();
			retObj = map.readValue(reqResult, ReturnVO.class);
		} catch (Exception e) {
			logger.error("json request exception retValue:"+reqResult);
			result.setErrorInfo("JSON分析异常【"+reqResult+"】");
			result.setReturnOptlog(ReturnOptlog.ROBOTEXCP);
			if(resend_num>=5){
				result.setRetValue(Result.MANUAL);
				return result;
			}else{
				MemcachedUtil.getInstance().setAttribute("resend_times_"+order.getOrderId(), ++resend_num, 90*1000);
				result.setRetValue(Result.RESEND);
				return result;
			}
		}
		
		try {
			String spare_thread =  retObj.getErrorInfo().get(0).getRobotNum();
			if(Integer.valueOf(spare_thread)>0){
				sysImpl.updateWorkerSpareThread(worker.getWorkerId() + "",Integer.valueOf(spare_thread));
			}
		} catch (Exception e1) {
			logger.info("更新机器人空闲进程数异常！",e1);
		}
		
		logger.info(order.getOrderId()+" errorCode:"+retObj.getErrorCode());
		
		if(!new Integer(0).equals(retObj.getErrorCode())){
			result.setRetValue(Result.FAILURE);
			result.setErrorInfo("【"+reqResult+"】");
			return result;
		}
		logger.info("orderid:"+order.getOrderId()+"retValue:"+retObj.getErrorInfo().get(0));
		
		if(retObj.getErrorInfo().size()>0){
			String channel = order.getChannel();
			Order val = retObj.getErrorInfo().get(0);
			String retValue = val.getRetValue();
			String retInfo = val.getRetInfo();
			if(Order.CHANNEL_GROUP_1.equals(order.getChannelGroup())){
			  String formatStr_new = "yyyy-MM-dd HH:mm:ss";
				try{
					if(null == val.getSeattime() || "".equals(val.getSeattime())){
						order.setSeattime("");
					}else{
						order.setSeattime(DateUtil.stringToString(val.getSeattime()+":00", formatStr_new));
					}
					if("".equals(val.getArrivetime())){
						order.setArrivetime("");
					}else{
						order.setArrivetime(DateUtil.stringToString(val.getArrivetime()+":00", formatStr_new));	
					}
				}catch(Exception e){
					logger.error("时间异常！",e);
				}
			}
			logger.info(order.getOrderId()+"tongcheng arrive:"+val.getArrivetime());
			
			logger.info("end RobotOrderRequest orderid:"+order.getOrderId());
			String loginFailNum = String.valueOf(MemcachedUtil.getInstance().getAttribute("robot" + worker.getWorkerId()));
			if(StringUtils.equals(retValue, "success")){//成功
				MemcachedUtil.getInstance().setAttribute("robot" + worker.getWorkerId(), 0);
				result.setSelfId(val.getOrderId());
				result.setSheId(val.getOutTicketBillNo());
				result.setSelfId(val.getOrderId());
				result.setSheId(val.getOutTicketBillNo());
				result.setFromCity(val.getFrom());
				result.setToCity(val.getTo());
				result.setTrainNo(val.getTrainno());
				result.setPayMoney(val.getBuymoney());
				result.setOrderCps(val.getCps());
				result.setSeattime(val.getSeattime());
				result.setBuyMoney(val.getBuymoney());
				result.setRefundOnline(val.getRefundOnline());
				result.setRetValue(Result.SUCCESS);
				result.setManual(false);
				
				logger.info("contacts num = "+val.getContactsnum());
				result.setContactsNum(val.getContactsnum());
				
				//常用联系人添加成功需要添加到账号过滤表中
				result.setInsertFilter(true);
				result.setFilterScope(Result.FILTER_ALL);
				int isPay = 0;
				try {
					isPay = service.orderIsPay(order.getOrderId());
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(null!=order.getPaymoney() && isPay == 1){
					if(!new Double(val.getBuymoney()).equals(new Double(order.getPaymoney()))){
						result.setErrorInfo("价格不相同，请确认价格【儿童票、特价票、卧铺票】！");	
					}
					
					if(new Double(val.getBuymoney()).doubleValue() > new Double(order.getPaymoney()).doubleValue()){
						logger.info("price not same, setPriceModify=true,need update base price!");
						double buyMoney = Double.parseDouble(val.getBuymoney());
						double payMoney = Double.parseDouble(order.getPaymoney());
						double diffMoney = buyMoney - payMoney;
						if(StringUtils.isNotEmpty(channel) && "19e".equals(channel)){
							if(diffMoney <= 10){
								logger.info("19e channel price differ=" + diffMoney+"<=10, can out ticket!");
								result.setErrorInfo("<b>价格不相同，报价接口报价错误，19e渠道10元误差可以出票！真实价格"+buyMoney+"</b>");
							}else{
								logger.info("19e channel price differ=" + diffMoney+">10, out ticket fail!");
								result.setErrorInfo("出票失败，请确定错误！【价格不相同，报价接口报价错误，19e渠道大于10元误差，出票失败！真实价格"+buyMoney+"】");
								result.setFailReason(ErrorInfo.TICKET_PRICE_WRONG);//票价和12306不符
								result.setRetValue(Result.CANCEL);
								result.setPriceModify(true);//需要更新车票表
								return result;
							}
						}else{
							logger.info("other channel price differ =" + diffMoney+", out ticket fail!");
							result.setErrorInfo("出票失败，请确定错误！【价格不相同，报价接口报价错误，非19e渠道出票失败！真实价格"+buyMoney+"】");
							result.setFailReason(ErrorInfo.TICKET_PRICE_WRONG);//票价和12306不符
							result.setPriceModify(true);//需要更新车票表
							result.setRetValue(Result.CANCEL);
							return result;
						}
					}
				}
				String formatStr = "yyyy-MM-dd HH:mm";
				if(!Order.CHANNEL_GROUP_1.equals(order.getChannelGroup())){//同程不需要比较发车时间
					Date oSeattime = DateUtil.stringToDate(order.getSeattime(), formatStr);
					
					Date rSeattime = DateUtil.stringToDate(val.getSeattime(), formatStr);
					
					logger.info("orderid:"+order.getOrderId()+" rSeattime:"+val.getSeattime()+" oSeattime:"+order.getSeattime());
					
					if(!oSeattime.equals(rSeattime)){
						long diffms = 0L;
						if(oSeattime.after(rSeattime)){
							diffms = oSeattime.getTime() - rSeattime.getTime();
						}else{
							diffms = rSeattime.getTime() - oSeattime.getTime();
						}
						long diffm = diffms/1000/60;
						if(diffm >= 10L){
							logger.info("time diff="+diffm+">=20min,out ticket fail!");
							result.setErrorInfo("出票失败，请确定错误！【乘车时间异常，相差10分钟以上，以无票处理！真实日期："+val.getSeattime()+"】");
							result.setFailReason(ErrorInfo.RIDING_TIME_EXCEPTION);//乘车时间异常
							result.setRetValue(Result.CANCEL);
							result.setPriceModify(true);//需要更新车票表
							return result;
						}else{
							logger.info("time diff="+diffm+"<20min,can out ticket!");
							result.setErrorInfo("<b>乘车时间异常，处理：相差10分钟以内可以开始出票！真实日期："+val.getSeattime()+"</b>");
						}
					}
				}
				
				if(StringUtils.isNotEmpty(order.getLevel()) && "10".equals(order.getLevel())){
					if(reqResult.contains("无座") && !StringUtils.equals(order.getSeatType(), "9")){
						if(!order.isWea_wz()){
							logger.info("订到无票坐席，并且备选坐席没有选择\"无座\"");
							result.setErrorInfo("出票失败，请确定错误！订到无票坐席，并且备选坐席没有选择\"无座\"");
							result.setFailReason(ErrorInfo.WITHOUT_TICKET);//失败原因为：无票
							result.setRetValue(Result.CANCEL);
							result.setPriceModify(true);//需要更新车票表
							return result;
						}
					}
					result.setErrorInfo("<b>本订单为联程订单，处理：请确认联程订单的相关订单都预订成功后再出票！</b>" + retInfo);
					result.setManual(true);
					return result;
				}
				
				if(reqResult.contains("无座") && !StringUtils.equals(order.getSeatType(), "9")){
					if(!order.isWea_wz()){
						logger.info("订到无票坐席，并且备选坐席没有选择\"无座\"");
						result.setErrorInfo("出票失败，请确定错误！订到无票坐席，并且备选坐席没有选择\"无座\"");
						result.setFailReason(ErrorInfo.WITHOUT_TICKET);//失败原因为：无票
						result.setRetValue(Result.CANCEL);
						result.setPriceModify(true);//需要更新车票表
						return result;
					}else{
						result.setErrorInfo("订到无票坐席，并且备选坐席选择无座，直接预订成功！</b>" + retInfo);
						result.setRetValue(Result.SUCCESS);
						return result;
					}
				}
				return result;	
			}else if(StringUtils.equals(retValue, "manual")){//人工处理
				MemcachedUtil.getInstance().setAttribute("robot" + worker.getWorkerId(), 0);
				result.setRetValue(Result.MANUAL);
				return result;
			}else if(StringUtils.equals(retValue, "failure")){//失败
			  result.setSelfId(val.getOrderId());
			  result.setErrorInfo("出票失败，请确定错误！【"+retInfo+"】");
			  if(StringUtils.isEmpty(retInfo)){
				  result.setRetValue(Result.MANUAL);
				  MemcachedUtil.getInstance().setAttribute("robot" + worker.getWorkerId(), 0);
				  return result;
			  }
			  if(StrUtil.containRegex(retInfo, "waitTime")) {//12306排队
				  result.setRetValue(Result.QUEUE);
				  return result;
			  }
			  if(retInfo.contains("用户信息是否真实、完整、准确")){ 
				  result.setErrorInfo("STOP");
				  result.setRetValue(Result.END);
				  return result;
			  }
			  if(retInfo.contains("账号状态为：待核验")){ 
				  result.setErrorInfo("WAITCHECK");
				  result.setRetValue(Result.END);
				  return result;
			  }
			  //出票系统日志
			  int ifResend = 0;
			  for(ReturnOptlog optlog :list_return){
				  if(retInfo.contains(optlog.getReturn_name())){
					  logger.info("针对错误日志："+optlog.getReturn_name()+"/"+optlog.getReturn_id());
					  if("00".equals(optlog.getReturn_type())){//重发
						  result.setReturnOptlog(optlog.getReturn_id());
						  if("1".equals(optlog.getReturn_join())){//是否参数重发
							  ifResend = 1;		//参与重发次数
						  }else{
							  ifResend = 2;		//直接重发
						  }
						  break;
					  }else if("11".equals(optlog.getReturn_type())){	//直接失败
						  result.setReturnOptlog(optlog.getReturn_id());
						//因为接口返回的报错信息不一致，所以把|| "elong".equals(channel)这个条件去掉  modify by wangsf 2016.4.27
						  if("8".equals(optlog.getFail_reason()) && StringUtils.isNotEmpty(channel) && ("qunar".equals(channel))){
							  result.setFailReason(ErrorInfo.CANCEL_ORDER);
						  }else if("12".equals(optlog.getFail_reason()) && StringUtils.isNotEmpty(channel) && "qunar".equals(channel)){
							  result.setFailReason(ErrorInfo.NOPASS_REALNAME_VERIFY);
						  }else{
							  result.setFailReason(optlog.getFail_reason());
						  }
						  
						  if("1".equals(optlog.getReturn_join())){//是否参数重发
							  ifResend = 3;		//参与失败次数
						  }else{
							  ifResend = 4;		//直接失败
						  }
						  break;
					  }else if("33".equals(optlog.getReturn_type())){	//按时重发
						  result.setReturnOptlog(optlog.getReturn_id());
						//因为接口返回的报错信息不一致，所以把|| "elong".equals(channel)这个条件去掉  modify by wangsf 2016.4.27
						  if("8".equals(optlog.getFail_reason()) && StringUtils.isNotEmpty(channel) && ("qunar".equals(channel))){
							  result.setFailReason(ErrorInfo.CANCEL_ORDER);
						  }else{
							  result.setFailReason(optlog.getFail_reason());
						  }
						  if("1".equals(optlog.getReturn_join())){//是否参数重发
							  ifResend = 5;		//参与失败次数
						  }else{
							  ifResend = 6;		//直接失败
						  }
						  break;						 
					  }else if("22".equals(optlog.getReturn_type())){	//人工
						  result.setReturnOptlog(optlog.getReturn_id());
						  if("1".equals(optlog.getReturn_join())){//是否参数重发
							  ifResend = 7;		//参与失败次数
						  }else{
							  ifResend = 8;		//直接失败
						  }
						  break;						 
					  }
					  						 
				  }
			  }
			  
			  logger.info("错误信息为：" + retInfo);
			  if(retInfo.indexOf("网络可能存在问题") >= 0 || retInfo.indexOf("12306服务器压力太大") >= 0) {
				  ifResend = 1;
			  }
			  if(ifResend!=0){
				  if(ifResend==1){
					  logger.info("预定重发1");
					  MemcachedUtil.getInstance().setAttribute("robot" + worker.getWorkerId(), 0);
					  if(resend_num>=5){
						result.setRetValue(Result.MANUAL);
						return result;
					  }else{
						MemcachedUtil.getInstance().setAttribute("resend_times_"+order.getOrderId(), ++resend_num, 90*1000);
						result.setRetValue(Result.RESEND);
						return result;
					  }
				 }else if(ifResend==2){
					 logger.info("预定重发2");
					 result.setRetValue(Result.RESEND);
					 return result;
				 }else if(ifResend==3){//出票失败
					  logger.info("出票失败超时重发3");
					  MemcachedUtil.getInstance().setAttribute("robot" + worker.getWorkerId(), 0);
					  if(fail_num>=3){
						  if(StringUtils.isNotEmpty(order.getLevel()) && "10".equals(order.getLevel())){
								result.setErrorInfo("<b>本订单为联程订单，处理：请确认联程订单的相关订单都预订成功后再出票！</b>"+retInfo);
								result.setRetValue(Result.MANUAL);
								return result;
						  }else{
							  if(!Result.NOPASS.equals(result.getRetValue())){
								  result.setRetValue(Result.FAILURE);
							  }
							  return result;
						  }
					  }else{
							MemcachedUtil.getInstance().setAttribute("fail_times_"+order.getOrderId(), ++fail_num, 90*1000);
							result.setRetValue(Result.RESEND);
							return result;
					  }
				 }else if(ifResend==4){
					 logger.info("出票失败4");
					 if(StringUtils.isNotEmpty(order.getLevel()) && "10".equals(order.getLevel())){
							result.setErrorInfo("<b>本订单为联程订单，处理：请确认联程订单的相关订单都预订成功后再出票！</b>"+retInfo);
							result.setRetValue(Result.MANUAL);
							return result;
					  }else{
						  if(!Result.NOPASS.equals(result.getRetValue())){
							  result.setRetValue(Result.FAILURE);
						  }
						  return result;
					  }
				 }else if(ifResend==5){	//按时重发
					 logger.info("按时重发5");
					  MemcachedUtil.getInstance().setAttribute("robot" + worker.getWorkerId(), 0);
					  if(wait_num>=3){
						  if(!Result.NOPASS.equals(result.getRetValue())){
							  result.setRetValue(Result.FAILURE);
						  }
						  return result;
					  }else{
						  MemcachedUtil.getInstance().setAttribute("wait_times_"+order.getOrderId(), ++wait_num, 4*60*1000);
						  result.setRetValue(Result.WAIT);
						  return result;
					  }
				 }else if(ifResend==6){
					 logger.info("按时重发6");
					 if(StringUtils.isNotEmpty(order.getLevel()) && "10".equals(order.getLevel())){
							result.setErrorInfo("<b>本订单为联程订单，处理：请确认联程订单的相关订单都预订成功后再出票！</b>"+retInfo);
							result.setRetValue(Result.MANUAL);
							return result;
					  }else{
						  result.setRetValue(Result.FAILURE);
						  return result;
					  }
				 }else if(ifResend==7){
					  logger.info("预定人工1");
					  MemcachedUtil.getInstance().setAttribute("robot" + worker.getWorkerId(), 0);
					  if(manual_num>=2){
						result.setRetValue(Result.MANUAL);
						return result;
					  }else{
						MemcachedUtil.getInstance().setAttribute("manual_times_"+order.getOrderId(), ++manual_num, 90*1000);
						result.setRetValue(Result.RESEND);
						return result;
					  }
				 }else if(ifResend==8){
					 logger.info("预定人工2");
					 result.setRetValue(Result.MANUAL);
					 return result;
				 }
			  }
			  
			  /*自动切换账号*/
			  //|| retInfo.contains("账号状态为：未通过") 
			if(retInfo.contains("您取消次数过多") || StrUtil.containRegex(retInfo, "手机.*核验")
					|| StrUtil.containRegex(retInfo, "联系人.*上限")
					|| StrUtil.containRegex(retInfo, "重新.*注册")
					|| retInfo.contains("账号状态为：未通过")
					|| retInfo.contains("未通过核验，不能添加常用联系人")
					|| retInfo.contains("通过后即可在网上购票")
					|| retInfo.contains("身份证件号码填写是否正确")) {
				 logger.info("错误信息为：" + retInfo);
				  MemcachedUtil.getInstance().setAttribute("robot" + worker.getWorkerId(), 0);
				  result.setRetValue(Result.STOP);
				  result.setErrorInfo("【" + retInfo + "】");
				  return result;
				  
			  }else if(retInfo.contains("登录失败")) {
				  logger.info("错误信息为：" + retInfo);
				  
				  if(loginFailNum != null && !"".equals(loginFailNum)) {
					  int loginFailNumber = Integer.parseInt(loginFailNum);
					  loginFailNumber += 1;
					  MemcachedUtil.getInstance().setAttribute("robot" + worker.getWorkerId(), loginFailNumber);
					  if(loginFailNumber >= 10) {
						  try {
							sysImpl.updateRobot(worker.getWorkerId() + "");
							sysImpl.insertWarning(worker.getWorkerName());
						  } catch (RepeatException e) {
							logger.error("登录失败更新数据库发生异常：" + e);
						  } catch (DatabaseException e) {
							logger.error("登录失败更新数据库发生异常：" + e);
						  }
						  MemcachedUtil.getInstance().setAttribute("robot" + worker.getWorkerId(), 0);
					  }
					  if(loginFailNumber==1){
						  result.setRetValue(Result.RESEND);
					  }else{
						  result.setRetValue(Result.MANUAL);
					  }
					 
					  return result;
				  }else {
					  MemcachedUtil.getInstance().setAttribute("robot" + worker.getWorkerId(), "1");
					  result.setRetValue(Result.MANUAL);
					  return result;
					  
				  }
			  }else{
				  MemcachedUtil.getInstance().setAttribute("robot" + worker.getWorkerId(), 0);
				  result.setRetValue(Result.MANUAL);
				  return result;
			  }
			}else if(StringUtils.equals(retValue, "nopass")){//用户未通过
				  result.setSelfId(val.getOrderId());
				  result.setErrorInfo(retInfo);
				  
				  MemcachedUtil.getInstance().setAttribute("robot" + worker.getWorkerId(), 0);
				  logger.info("订票渠道："+order.getChannel()+ "错误信息：" +retInfo+ "修改订单状态为出票失败（身份信息待审核）");
				  
				  //因为接口返回的报错信息不一致，所以把|| "elong".equals(channel)这个条件去掉  modify by wangsf 2016.4.27
				  if(StringUtils.isNotEmpty(channel) &&( "qunar".equals(channel))){
					  result.setFailReason(ErrorInfo.CANCEL_ORDER);
					  result.setRetValue(Result.NOPASS);
					  return result;
				  }else{
					  result.setFailReason(ErrorInfo.PASSENGER_NO_VERIFY);
					  result.setRetValue(Result.NOPASS);
					  return result;
				  }
				  
			}else{//查询异常
				logger.error("returncode="+retValue);
				result.setErrorInfo("出票异常，请检查机器人！【"+retInfo+"】");
			    result.setRetValue(Result.MANUAL);
			    return result;
			}
		}else{
			logger.warn("RobotOrderRequest orderid:"+order.getOrderId()+" ERROR");
			result.setErrorInfo("出票异常，请检查机器人！【"+reqResult+"】");
			result.setRetValue(Result.MANUAL);
			return result;
		}

	}

	/**
	 * 时间间隔参数json串
	 * @return
	 */
	private String timeIntervalParam(TrainServiceImpl service) {
		MemcachedUtil mUtil = MemcachedUtil.getInstance();
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		long signInInterval = 5;
		long checkAccountInterval = 5;
		long unfinishedOrderInterval = 5;
		long bookButtonInterval = 5;
		long getContactInterval = 5;
		long addContactInterval = 5;
		long submitBookInterval = 5;
		
		try {
			//登录间隔时间
			if(mUtil.getAttribute("sign_in_time_interval") == null) {
				signInInterval = Long.valueOf(service.getSysSettingValue("sign_in_time_interval"));
				mUtil.setAttribute("sign_in_time_interval", signInInterval, 30 * 1000);
			} else {
				signInInterval = (Long) mUtil.getAttribute("sign_in_time_interval");
			}
			
			//检测账号间隔时间
			if(mUtil.getAttribute("check_account_time_interval") == null) {
				checkAccountInterval = Long.valueOf(service.getSysSettingValue("check_account_time_interval"));
				mUtil.setAttribute("check_account_time_interval", signInInterval, 30 * 1000);
			} else {
				checkAccountInterval = (Long) mUtil.getAttribute("check_account_time_interval");
			}
			
			//未完成订单查询间隔时间
			if(mUtil.getAttribute("unfinished_order_time_interval") == null) {
				unfinishedOrderInterval = Long.valueOf(service.getSysSettingValue("unfinished_order_time_interval"));
				mUtil.setAttribute("unfinished_order_time_interval", signInInterval, 30 * 1000);
			} else {
				unfinishedOrderInterval = (Long) mUtil.getAttribute("unfinished_order_time_interval");
			}
			
			//点击预订按钮间隔时间
			if(mUtil.getAttribute("book_button_time_interval") == null) {
				bookButtonInterval = Long.valueOf(service.getSysSettingValue("book_button_time_interval"));
				mUtil.setAttribute("book_button_time_interval", signInInterval, 30 * 1000);
			} else {
				bookButtonInterval = (Long) mUtil.getAttribute("book_button_time_interval");
			}
			
			//获取联系人间隔时间
			if(mUtil.getAttribute("get_contact_time_interval") == null) {
				getContactInterval = Long.valueOf(service.getSysSettingValue("get_contact_time_interval"));
				mUtil.setAttribute("get_contact_time_interval", signInInterval, 30 * 1000);
			} else {
				getContactInterval = (Long) mUtil.getAttribute("get_contact_time_interval");
			}
			
			//添加联系人间隔时间
			if(mUtil.getAttribute("add_contact_time_interval") == null) {
				addContactInterval = Long.valueOf(service.getSysSettingValue("add_contact_time_interval"));
				mUtil.setAttribute("add_contact_time_interval", signInInterval, 30 * 1000);
			} else {
				addContactInterval = (Long) mUtil.getAttribute("add_contact_time_interval");
			}
			
			//提交预订信息间隔时间
			if(mUtil.getAttribute("submit_book_time_interval") == null) {
				submitBookInterval = Long.valueOf(service.getSysSettingValue("submit_book_time_interval"));
				mUtil.setAttribute("submit_book_time_interval", signInInterval, 30 * 1000);
			} else {
				submitBookInterval = (Long) mUtil.getAttribute("submit_book_time_interval");
			}
			
			paramMap.put("login_time", signInInterval * 1000);
			paramMap.put("account_time", checkAccountInterval * 1000);
			paramMap.put("order_time", unfinishedOrderInterval * 1000);
			paramMap.put("order_button_time", bookButtonInterval * 1000);
			paramMap.put("getuser_time", getContactInterval * 1000);
			paramMap.put("adduser_time", addContactInterval * 1000);
			paramMap.put("submit_time", submitBookInterval * 1000);
			
			ObjectMapper mapper = new ObjectMapper();
			String result = mapper.writeValueAsString(paramMap);
			logger.info("time_interval : " + result);
			return result;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (RepeatException e) {
			e.printStackTrace();
		} catch (DatabaseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
