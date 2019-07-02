package com.l9e.train.channel.request.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import com.l9e.train.po.Account;
import com.l9e.train.po.DevicePayWeight;
import com.l9e.train.po.Order;
import com.l9e.train.po.PayCard;
import com.l9e.train.po.Result;
import com.l9e.train.po.ReturnOptlog;
import com.l9e.train.po.ReturnVO;
import com.l9e.train.po.Worker;
import com.l9e.train.service.impl.SysSettingServiceImpl;
import com.l9e.train.service.impl.TrainServiceImpl;
import com.l9e.train.util.DateUtil;
import com.l9e.train.util.MD5;
import com.l9e.train.util.MemcachedUtil;
import com.l9e.train.util.MobileMsgUtil;
import com.l9e.train.util.PostRequestUtil;
import com.l9e.train.util.StrUtil;
import com.l9e.train.util.UrlFormatUtil;
import com.unlun.commons.exception.DatabaseException;
import com.unlun.commons.exception.RepeatException;

public class RobotPayRequest extends RequestImpl {
	private Logger logger=Logger.getLogger(this.getClass());
	private static final Random WEIGHT_RANDOM = new Random();
	
	public RobotPayRequest(Account account, Worker worker, PayCard payCard) {
		super(account, worker, payCard);
	}
	
	@Override
	public Result request(Order order) {
		TrainServiceImpl service = new TrainServiceImpl();
		//生成一个随机的16位的字符串(设备号）
		String deviceNo=StrUtil.getRandomString(16);
		//支付宝支付时选择的打码器。0:公司自己的打码动态库；1：第三方的打码动态库
		String codeEditor=null;		
		try {
			codeEditor = service.getSysSettingValue("codeEditorSelect");
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (RepeatException e) {
			e.printStackTrace();
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		//查询系统设置的预订机器人版本
		SysSettingServiceImpl sysImpl = new SysSettingServiceImpl();
		try {
			sysImpl.queryLogList();
		}catch(Exception e){
			logger.error("获取支付系统日志异常！",e);
		}
		List<ReturnOptlog> list_return = sysImpl.getList_return();
		
		int resend_num = 0;
		if(null != MemcachedUtil.getInstance().getAttribute("pay_resend_times_"+order.getOrderId())){
			resend_num = Integer.valueOf(String.valueOf(MemcachedUtil.getInstance().getAttribute("pay_resend_times_"+order.getOrderId())));
			logger.info(order.getOrderId()+" pay_resend_times:"+resend_num);
		}
		int fail_num = 0;
		if(null != MemcachedUtil.getInstance().getAttribute("pay_fail_times_"+order.getOrderId())){
			fail_num = Integer.valueOf(String.valueOf(MemcachedUtil.getInstance().getAttribute("pay_fail_times_"+order.getOrderId())));
			logger.info(order.getOrderId()+" pay_fail_times:"+fail_num);
		}
		
		int manual_num = 0;
		if(null != MemcachedUtil.getInstance().getAttribute("pay_manual_times_"+order.getOrderId())){
			manual_num = Integer.valueOf(String.valueOf(MemcachedUtil.getInstance().getAttribute("pay_manual_times_"+order.getOrderId())));
			logger.info(order.getOrderId()+" pay_manual_times:"+manual_num);
		}		
		logger.info("start RobotOrderRequest orderid:"+order.getOrderId());
		
		Map<String, String> maps = new HashMap<String,String>();
		
		maps.put("SessionID", String.valueOf(System.currentTimeMillis()));//任务完成后，机器人回应该请求时携带
		maps.put("Timeout", "120000");//供货商价格
		maps.put("ParamCount", "1");
		
		String randCodeType = "0";//打码方式：0、手动打码 1、机器识别
		//SysSettingServiceImpl sysImpl = new SysSettingServiceImpl();
		String resend_log = "";
		try {
			if(null != MemcachedUtil.getInstance().getAttribute("pay_resend_log")){
				resend_log = String.valueOf(MemcachedUtil.getInstance().getAttribute("pay_resend_log"));
				logger.info(" resend_log:"+resend_log);
			}else{
				sysImpl.querySysVal("pay_again");
				resend_log = sysImpl.getSysVal();
				MemcachedUtil.getInstance().setAttribute("pay_resend_log",resend_log,5*60*1000);
			}
			
			sysImpl.querySysVal("rand_code_type");
			
			if(StringUtils.isNotEmpty(sysImpl.getSysVal())){
				randCodeType = sysImpl.getSysVal();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info(randCodeType.equals("0")?"human input randcode":"robot input randcode");
		
		String[] pay_arr = new String[10];
		if(StringUtils.isNotEmpty(resend_log)){
			pay_arr = resend_log.split("@");
		}
		//登陆账号|登陆账号密码|订单号|12306订单号|支付金额|支付账号|支付密码|安全手机号
		StringBuffer sb = new StringBuffer();
		sb.append(account.getAccUsername()+"|");
		if(StringUtils.isNotEmpty(payCard.getPayType()) && "55".equals(payCard.getPayType())){//支付宝
			logger.info("select alipay");
//			maps.put("ScriptPath", "alipaynew.lua");
//			TrainServiceImpl service = new TrainServiceImpl();
//			String weight = devicePayWeight(service);
//			if(DevicePayWeight.PAY_WEIGHT_PC.equals(weight)) {
//				maps.put("ScriptPath", "alipaynew.lua");
//				sb.append(account.getAccPassword()+"|");
//			} else if(DevicePayWeight.PAY_WEIGHT_APP.equals(weight)) {
//				maps.put("ScriptPath", "appalipay.lua");
//				sb.append(MD5.getMd5_UTF8(account.getAccPassword())+"|");
//			}
			logger.info("选用支付类型--"+worker.getPay_device_type());
			
			//pay_device_type 支付端类型  ：   0--PC端支付  1-APP端支付
			if(worker.getPay_device_type()!=null && "1".equals(worker.getPay_device_type())){
				maps.put("ScriptPath", "appalipay.lua");
				sb.append(MD5.getMd5_UTF8(account.getAccPassword())+"|");
			}else{
				maps.put("ScriptPath", "alipaynew.lua");
				sb.append(account.getAccPassword()+"|");
			}			
		}else{
			logger.info("select ccb bank");
			maps.put("ScriptPath", "paymentnew.lua");
		}
		
		sb.append(order.getOrderId()+"|");
		sb.append(order.getOutTicketBillNo()+"|");
		sb.append(order.getBuymoney()+"|");
		sb.append(payCard.getCardNo()+"|");//支付账号
        sb.append(payCard.getCardPwd()+"|");//支付密码
		sb.append(payCard.getCardPhone()+"|");//安全手机号
		sb.append(payCard.getPayType()+"|");//支付类型
		sb.append(payCard.getBankType()+"|");//银行类型
		sb.append(randCodeType);
		sb.append("|").append(order.getUserName());
		sb.append("|").append(worker.getWorkerId());
		
		
		/*
		 * 在此判断支付端是否是app端，pay_device_type 支付端类型  ：   0--PC端支付  1-APP端支付
		 * 如果走的是app端，则在Param1里加上设备号deviceNo
		 */
		if(worker.getPay_device_type()!=null && "1".equals(worker.getPay_device_type())){
			sb.append("|").append(deviceNo);
			try {
				sysImpl.querySysVal("appPay_wait_second");
			} catch (RepeatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DatabaseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String wait_second = sysImpl.getSysVal();
			if(wait_second!=null){
				sb.append("|").append(wait_second);
			}
		}
		//添加支付宝支付时选择的打码器
		sb.append("|").append(codeEditor);

		/*
		 * 在此判断订单支付时间是否超时，如果下单等待支付的开始时间与列车发车时间之间大于3小时，且等待支付时间不超过29分钟，则isPay为1，否则为0；
		 * 如果下单等待支付的开始时间与列车发车时间之间小于3小时，且等待支付时间不超过9分钟，则isPay为1，否则为0。
		 * add by wangsf
		 */
		String isPay=""; //支付时间是否超时， 0：超时； 1：不超时
		Date outTicketTime=order.getOutTicketTime();//下订单时间
		logger.info("订单开始支付时间为："+outTicketTime);
		Date fromTime=order.getFromTime();//列车发车时间
		logger.info("列车发车时间为："+fromTime);
		Date date=new Date();//当前时间
		String format="yyyy-MM-dd HH:mm:ss"; //转换的格式
		long diffHours=0;//相差的小时数
		long diffMinutes=0;//相差的分钟数
		DateUtil dateUtil=new DateUtil();
		if(null != outTicketTime && null != fromTime){
		diffHours=dateUtil.twoDateDiffHours(outTicketTime, fromTime,format);
		logger.info("相差的小时数为："+diffHours);
		diffMinutes=dateUtil.twoDateDiffMinutes(date, outTicketTime,format);
		logger.info("相差的分钟数为："+diffMinutes);
		}
		if(diffHours >= 3){
			
			if(diffMinutes <= 29){
				isPay="1";
			}else{
				isPay="0";
			}
			
		}else{
			
			if(diffMinutes <= 9){
				isPay="1";
			}else{
				isPay="0";
			}
		}
		sb.append("|").append(isPay);
		
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
		
		maps.put("Param1",sb.toString());
		
		logger.info("start post request params:"+maps+" orderUrl:"+worker.getWorkerExt());
		
		String param=null;
		try {
			param = UrlFormatUtil.createUrl("", maps);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		String reqResult=PostRequestUtil.getPostRes("UTF-8", worker.getWorkerExt(), param);
		
		reqResult = reqResult.replace("[\"{", "[{");
		reqResult = reqResult.replace("}\"]", "}]");
		reqResult = reqResult.replace("\\", "");
		
		
		logger.info("reValue:"+reqResult);
		
		if(null ==reqResult){
			logger.error("post request Error retValue:"+reqResult);
			result.setErrorInfo("支付错误，没有返回结果！");
			if(resend_num>=3){
				result.setRetValue(Result.MANUAL);
				return result;
			}else{
				MemcachedUtil.getInstance().setAttribute("pay_resend_times_"+order.getOrderId(), ++resend_num, 5*60*1000);
				result.setRetValue(Result.RESEND);
				return result;
			}
		}
		
		//订单不允许支付的处理
		if(reqResult.contains("该订单不允许支付")){
			logger.error("robot request Error retValue:"+reqResult);
			if("11".equals(order.getIsClickButton())){
			result.setErrorInfo("该订单不允许支付");
			result.setRetValue(Result.FAILURE);
			return result;
			}			
		}
		
		//请求错误
		if(reqResult.equals(PostRequestUtil.TIME_OUT)||reqResult.equals(PostRequestUtil.CONNECT_ERROR)|reqResult.equals(PostRequestUtil.URL_ERROR)){
			logger.error("post request Error retValue:"+reqResult);
			result.setErrorInfo("支付超时，通知管理员！");
			if(resend_num>=3){
				result.setRetValue(Result.MANUAL);
				return result;
			}else{
				MemcachedUtil.getInstance().setAttribute("pay_resend_times_"+order.getOrderId(), ++resend_num, 5*60*1000);
				result.setRetValue(Result.RESEND);
				return result;
			}
		}
		
		//机器人处理超时
		if(reqResult.contains("\"ErrorCode\":9")){
			logger.error("robot request Error retValue:"+reqResult);
			result.setErrorInfo("机器人处理超时"+reqResult);
			if(resend_num>=3){
				result.setRetValue(Result.MANUAL);
				return result;
			}else{
				MemcachedUtil.getInstance().setAttribute("pay_resend_times_"+order.getOrderId(), ++resend_num, 5*60*1000);
				result.setRetValue(Result.RESEND);
				return result;
			}
		}
		
		//机器人登录失败
		if(reqResult.contains("登录失败")){
			logger.error("robot request Error retValue:"+reqResult);
			result.setErrorInfo("机器人登录失败"+reqResult);
			if(resend_num>=3){
				result.setRetValue(Result.MANUAL);
				return result;
			}else{
				MemcachedUtil.getInstance().setAttribute("pay_resend_times_"+order.getOrderId(), ++resend_num, 5*60*1000);
				result.setRetValue(Result.RESEND);
				return result;
			}
		}
		
		
		//机器人帐号被锁
		if(reqResult.contains("支付密码已被锁定")){
			logger.error("robot request Error retValue:"+reqResult);
			result.setErrorInfo("支付密码已被锁定，关闭机器人"+reqResult);
			try {
				//停用机器人
				sysImpl.stopWorker(worker.getWorkerId());
				//发送短信
				sysImpl.queryRobotWarnPhone();
				String phones = sysImpl.getPhones();
				for(String phone : phones.split(",")){
					MobileMsgUtil msg = new MobileMsgUtil();
					msg.send(phone, "支付机器人"+worker.getWorkerName()+",支付密码已被锁定，已停用！");
				}
			} catch (Exception e) {
				logger.error("stop robot error:");
			}
			result.setRetValue(Result.MANUAL);
			return result;
		}
		
		ReturnVO retObj = null;
		try {
			ObjectMapper map = new ObjectMapper();
			retObj = map.readValue(reqResult, ReturnVO.class);
		} catch (Exception e) {
			logger.error("json request exception retValue:"+reqResult);
			result.setErrorInfo("JSON分析异常"+reqResult);
			if(resend_num>=3){
				result.setRetValue(Result.MANUAL);
				return result;
			}else{
				MemcachedUtil.getInstance().setAttribute("pay_resend_times_"+order.getOrderId(), ++resend_num, 5*60*1000);
				result.setRetValue(Result.RESEND);
				return result;
			}
		}
		
		try {
			String spare_thread =  retObj.getErrorInfo().get(0).getRobotNum();
			if(Integer.valueOf(spare_thread)>0){
				sysImpl.updateWorkerSpareThread(worker.getWorkerId(),Integer.valueOf(spare_thread));
			}
		} catch (Exception e1) {
			logger.info("update pay_worker spare_thread exception:",e1);
		}
		logger.info(order.getOrderId()+" errorCode:"+retObj.getErrorCode());
		
		if(!new Integer(0).equals(retObj.getErrorCode())){
			result.setRetValue(Result.FAILURE);
			result.setErrorInfo(reqResult);
			return result;
		}
		logger.info("orderid:"+order.getOrderId()+"retValue:"+retObj.getErrorInfo().get(0));
		
		String retInfo = retObj.getErrorInfo().get(0).getRetInfo();
		if(retObj.getErrorInfo().size()>0){
			Order val = retObj.getErrorInfo().get(0);
			String retValue = val.getRetValue();
			logger.info("end RobotOrderRequest orderid:"+order.getOrderId());
			if(StringUtils.equals(retValue, "success")){//成功
				result.setSelfId(val.getOrderId());
				result.setSheId(val.getOutTicketBillNo());
				result.setPaybillno(val.getPaybillno());
				result.setRetValue(Result.SUCCESS);
				result.setBalance(val.getBalance());
				
				return result;	
			}else if(StringUtils.equals(retValue, "manual")){//人工处理
				MemcachedUtil.getInstance().setAttribute("robot" + worker.getWorkerId(), 0);
				result.setRetValue(Result.MANUAL);
				return result;
			}else if(StringUtils.equals(retValue, "failure")){//失败
			  result.setSelfId(val.getOrderId());
			  result.setSheId(val.getOutTicketBillNo());
			  result.setPaybillno(val.getPaybillno());
			  result.setErrorInfo(retInfo);
			  
			  //支付系统日志
			  Integer isResend=0;
			  for(ReturnOptlog optlog :list_return){
				  if(retInfo.contains(optlog.getReturn_name())){
					  logger.info("针对错误日志："+optlog.getReturn_name()+"/"+optlog.getReturn_id());
					  if("00".equals(optlog.getReturn_type())){//重发
						  result.setReturnOptlog(optlog.getReturn_id());
						  if("1".equals(optlog.getReturn_join())){//是否参数重发
							  isResend = 1;		//参与重发次数
						  }else{
							  isResend = 2;		//直接重发
						  }
						  break;
					  }else if("11".equals(optlog.getReturn_type())){	//直接失败
						  result.setReturnOptlog(optlog.getReturn_id());
						  
						  if("1".equals(optlog.getReturn_join())){//是否参数重发
							  isResend = 3;		//参与失败次数
						  }else{
							  isResend = 4;		//直接失败
						  }
						  break;
					  }else if("22".equals(optlog.getReturn_type())){	//人工
						  result.setReturnOptlog(optlog.getReturn_id());
						  if("1".equals(optlog.getReturn_join())){//是否参数重发
							  isResend = 7;		//参与失败次数
						  }else{
							  isResend = 8;		//直接失败
						  }
						  break;						 
					  }
					  						 
				  }
			  }
			  
			  logger.info("错误信息为：" + retInfo);
			
			  if(isResend!=0){
				  if(isResend==1){
					  logger.info("预定重发1");
					  MemcachedUtil.getInstance().setAttribute("robot" + worker.getWorkerId(), 0);
					  if(resend_num>=3){
						result.setRetValue(Result.MANUAL);
						return result;
					  }else{
						MemcachedUtil.getInstance().setAttribute("pay_resend_times_"+order.getOrderId(), ++resend_num, 90*1000);
						result.setRetValue(Result.RESEND);
						return result;
					  }
				 }else if(isResend==2){
					 logger.info("预定重发2");
					 result.setRetValue(Result.RESEND);
					 return result;
				 }else if(isResend==3){//支付失败
					  logger.info("支付失败超时重发3");
					  MemcachedUtil.getInstance().setAttribute("robot" + worker.getWorkerId(), 0);
					  if(fail_num>=3){
						  if(StringUtils.isNotEmpty(order.getLevel()) && "10".equals(order.getLevel())){
								result.setErrorInfo("<b>本订单为联程订单，处理：请确认联程订单的相关订单都预订成功后再支付！</b>"+retInfo);
								result.setRetValue(Result.MANUAL);
								return result;
						  }else{
							  if(!Result.NOPASS.equals(result.getRetValue())){
								  result.setRetValue(Result.FAILURE);
							  }
							  return result;
						  }
					  }else{
							MemcachedUtil.getInstance().setAttribute("pay_fail_times_"+order.getOrderId(), ++fail_num, 90*1000);
							result.setRetValue(Result.RESEND);
							return result;
					  }
				 }else if(isResend==4){
					 logger.info("支付失败4");
					 if(StringUtils.isNotEmpty(order.getLevel()) && "10".equals(order.getLevel())){
							result.setErrorInfo("<b>本订单为联程订单，处理：请确认联程订单的相关订单都预订成功后再支付！</b>"+retInfo);
							result.setRetValue(Result.MANUAL);
							return result;
					  }else{
						  if(!Result.NOPASS.equals(result.getRetValue())){
							  result.setRetValue(Result.FAILURE);
						  }
						  return result;
					  }
				 }else if(isResend==7){
					  logger.info("预定人工1");
					  MemcachedUtil.getInstance().setAttribute("robot" + worker.getWorkerId(), 0);
					  if(manual_num>=2){
						result.setRetValue(Result.MANUAL);
						return result;
					  }else{
						MemcachedUtil.getInstance().setAttribute("pay_manual_times_"+order.getOrderId(), ++manual_num, 90*1000);
						result.setRetValue(Result.RESEND);
						return result;
					  }
				 }else if(isResend==8){
					 logger.info("预定人工2");
					 result.setRetValue(Result.MANUAL);
					 return result;
				 }
			  }
			  
			  
			  boolean ifResend = false;
			  for(String str :pay_arr){
				  if(retInfo.contains(str)){
					  result.setRetValue(Result.RESEND);
					  ifResend = true;
					  break;
				  } 
			  }
			  
			  if(ifResend){
				  MemcachedUtil.getInstance().setAttribute("robot" + worker.getWorkerId(), 0);
				  if(resend_num>=3){
					result.setRetValue(Result.MANUAL);
					return result;
				  }else{
					MemcachedUtil.getInstance().setAttribute("pay_resend_times_"+order.getOrderId(), ++resend_num, 180*1000);
					result.setRetValue(Result.RESEND);
					return result;
				  }
			 }
			  if(retInfo.contains("已完成支付")||retInfo.contains("已出票")){
				  result.setRetValue(Result.SUCCESS);
			  }else{
				  result.setRetValue(Result.MANUAL);
			  }
			  return result;
			}else{//查询异常
				logger.error("returncode="+retValue);
				result.setErrorInfo("支付异常，请检查机器人！["+retInfo+"]");
			    result.setRetValue(Result.MANUAL);
			    return result;
			}
		}else{
			logger.warn("RobotOrderRequest orderid:"+order.getOrderId()+" ERROR");
			result.setErrorInfo("支付异常，请检查机器人！["+retInfo+"]");
			result.setRetValue(Result.MANUAL);
			return result;
		}
	}

	/**
	 * 权重
	 * @throws DatabaseException 
	 * @throws RepeatException 
	 */
	private String devicePayWeight(TrainServiceImpl service) {
		/*PC&APP 模式权重分配*/
		String defaultPayWeightMode = DevicePayWeight.PAY_WEIGHT_PC;
		
		try {
			String pcWeightValue = service.getSysSettingValue("device_paymode_" + DevicePayWeight.PAY_WEIGHT_PC);//pc权重
			String appWeightValue = service.getSysSettingValue("device_paymode_" + DevicePayWeight.PAY_WEIGHT_APP);//app权重
			
			logger.info("权重系统设置pc_payweight ,PC端: " + pcWeightValue + "app_payweight ,移动端: " + appWeightValue);
			/*设置权重置*/
			List<DevicePayWeight> modeCategories = new ArrayList<DevicePayWeight>();//放各个权重的集合
			
			DevicePayWeight pcMode = new DevicePayWeight(DevicePayWeight.PAY_WEIGHT_PC, Integer.parseInt(pcWeightValue));//pc权重
			DevicePayWeight appMode = new DevicePayWeight(DevicePayWeight.PAY_WEIGHT_APP, Integer.parseInt(appWeightValue));//app权重
			
			modeCategories.add(pcMode);
			modeCategories.add(appMode);
			
			int count = 0;
			for(DevicePayWeight category : modeCategories) {
				count += category.getWeight();
			}
			logger.info("权重最大边界 count : " + count);
			Integer nchannel = WEIGHT_RANDOM.nextInt(count); // n in [0, weightSum)
			logger.info("权重随机值 nchannel : " + nchannel);
			Integer mchannel = 0;
			for (DevicePayWeight weightCategory : modeCategories) {
				if (mchannel <= nchannel && nchannel < mchannel + weightCategory.getWeight()) {
					defaultPayWeightMode = weightCategory.getCategory();
					break;
				}
				mchannel += weightCategory.getWeight();
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (RepeatException e) {
			e.printStackTrace();
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		
		return defaultPayWeightMode;

	} 
	
	public static void main(String[] args){
		Account account = new Account();
		account.setAccId("123123");
		account.setAccUsername("GMiyCOlWkc");
		account.setAccPassword("GMiyCOlWkc_");
		Worker worker = new Worker();
		worker.setWorkerId("758");
		worker.setWorkerName("机器人164.216");
		worker.setWorkerType(3);
		worker.setWorkerExt("http://120.25.164.216:9010/RunScript");
		PayCard payCard = new PayCard();
		payCard.setBankType("33");
		payCard.setCardExt("");
		payCard.setCardId("123");
		payCard.setCardNo("huochepiaokuyou@163.com");
		payCard.setCardPhone("18611733723");
		payCard.setCardPwd("han0147");
		payCard.setCardRemain("885");
		payCard.setPayStatus("11");
		payCard.setPayType("55");
		RobotPayRequest robotPayRequest = new RobotPayRequest(account, worker, payCard) ;
		Order order = new Order();
		order.setOrderId("TGT_SE4DAC9DF5B749619");
		order.setOutTicketBillNo("E166897973");
		order.setBuymoney("44.5");
		order.setUserName("李永先");
		robotPayRequest.request(order);
	}
}
