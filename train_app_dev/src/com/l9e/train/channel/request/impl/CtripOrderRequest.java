package com.l9e.train.channel.request.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l9e.train.main.App;
import com.l9e.train.po.Account;
import com.l9e.train.po.CtripAcc;
import com.l9e.train.po.CtripOrderVo;
import com.l9e.train.po.CtripReturnVO;
import com.l9e.train.po.DeviceWeight;
import com.l9e.train.po.IpInfo;
import com.l9e.train.po.Order;
import com.l9e.train.po.Result;
import com.l9e.train.po.Worker;
import com.l9e.train.service.impl.SysSettingServiceImpl;
import com.l9e.train.service.impl.TrainServiceImpl;
import com.l9e.train.util.DateUtil;
import com.l9e.train.util.HttpUtil;
import com.l9e.train.util.PostRequestUtil;
import com.l9e.train.util.UrlFormatUtil;
import com.unlun.commons.exception.DatabaseException;
import com.unlun.commons.exception.RepeatException;

@Deprecated
public class CtripOrderRequest extends RequestImpl{
	
	private Logger logger = LoggerFactory.getLogger(CtripOrderRequest.class);
	
	private static final Random WEIGHT_RANDOM = new Random();
	//public List<String> ctripPhoneNumList; //携程手机号码列表
	
	public CtripOrderRequest(Account account, Worker worker, CtripAcc ctripAcc) {
		super(account, worker,ctripAcc);
	}
	
	
	@Override
	public Result request(Order order, String weight, String logid) {
		String logPre = "[携程出票]";
		//weight = deviceCtripWeight();
		/**成功测试*/
		/*boolean lss=true;
		if(lss){
			result.setRetValue(Result.SUCCESS);
			result.setCtripId("trip_order_id");
			result.setBuyMoney("2222.2");
			result.setCtrip_bx_money("12.0");
			//result.setSelfId("E12312");
			return result;
		}*/
		
		
		
		Date date = new Date();
		String datePre = DateUtil.dateToString(date, "yyyyMMdd");		
		Date begin = DateUtil.stringToDate(datePre + "060000", "yyyyMMddHHmmss");//6:00
		Date end = DateUtil.stringToDate(datePre + "230000", "yyyyMMddHHmmss");//23:00
		if(date.before(begin) || date.after(end)){
			logger.info(logPre+"【向机器人发送预订请求】发送时间在23:00~06:00之间，不予发送！");
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
		logger.info(logPre+(randCodeType.equals("0")?"human input randcode":"robot input randcode"));
		
		/*
		 发送请求：
		http://localhost:8091/RunScript?ScriptPath=ctrip.lua&SessionID=1434504154351&Timeout=240000&ParamCount=2&Param1=gu22aen57|kfcalb82|TGT_S5580CBCU2100412450|南京南|常州北|2015-06-19|G1227|0|159|18611339439|adai1988116|马添马|198811|100&Param2=tc150617092226303|王子阳|0|2|310106199602011637|3
		Param1=12306用户名|密码|订单号|出发站|到达站|乘车日期|车次|是否人工打码|worker_id|携程账号（手机号）|登陆密码|联系人|支付密码|支付金额|cookie|cid|auth|sauth
		* 
		* */
		Map<String, String> maps = new HashMap<String,String>();
		
		//在此先获取携程手机号码  将来可能会用到！
//		TrainServiceImpl trainServiceImpl =new TrainServiceImpl();
//	    List<String> ctripPhoneNumList = null; //手机号码列表
//				
//		String ctripPhoneNumber = ""; // 手机号码
//		String ctripPhoneKey = "ctrip_phoneNum";// 缓存key值
//		Long lLen = App.redisDao.LLEN(ctripPhoneKey);
//		logger.info("携程手机号在缓存中的队列长度为：" + lLen);
//
//		if (lLen > 0) {
//			ctripPhoneNumber = App.redisDao.RPOP(ctripPhoneKey).toString();
//		} else {
//
//			try {
//				trainServiceImpl.queryPhoneNumList();
//				ctripPhoneNumList = trainServiceImpl.getCtripPhoneNumList();
//			} catch (RepeatException e2) {
//				// TODO Auto-generated catch block
//				e2.printStackTrace();
//			} catch (DatabaseException e2) {
//				// TODO Auto-generated catch block
//				e2.printStackTrace();
//			}
//
//			if (null != ctripPhoneNumList) {
//				for (int i = 0; i < ctripPhoneNumList.size(); i++) {
//					App.redisDao.LPUSH(ctripPhoneKey, ctripPhoneNumList.get(i));
//				}
//
//				ctripPhoneNumber = App.redisDao.RPOP(ctripPhoneKey).toString();
//			}
//			
//
//		}
//		logger.info("从缓存中获取的携程手机号为：" + ctripPhoneNumber);
	
		
		//22 携程出票模式（不买保险）model=ctripApp  33 携程出票模式（买保险）model=ctripPc
		String dealType = "ctripApp";
		// "22".equals(order.getManual_order()条件已删除，以后走app
		if("33".equals(order.getManual_order())) {
			/*PC出票*/
			dealType = "ctripPC";
		}
		maps.put("dealType", dealType);//执行的脚本路径
		maps.put("ScriptPath", "confirmCtrip");//执行的脚本路径
		maps.put("SessionID", String.valueOf(System.currentTimeMillis()));//任务完成后，机器人回应该请求时携带
		maps.put("Timeout", "240000");//供货商价格
		maps.put("ParamCount", String.valueOf(order.getOrderCps().size()+1));
		
		//为携程请求传入一个代理IP
		String ctripProxyIP = "";// 携程代理IP
		//代理IP开关
		String ctripProxyIPSwitch = "0";//携程代理IP开关：   0：代理IP打开   1：代理IP关闭
		sysImpl = new SysSettingServiceImpl();
		try {
			sysImpl.querySysVal("ctripProxyIP_switch");
			
			if(StringUtils.isNotEmpty(sysImpl.getSysVal())){
				ctripProxyIPSwitch = sysImpl.getSysVal();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if ("0".equals(ctripProxyIPSwitch)) {
			IpInfo ipInfo = App.ipInfoService.getIpInfoByType(IpInfo.TYPE_CTRIP);
			// IpInfo ipInfo=null;
			logger.info("根据IP类型获取的携程代理IP对象为：" + ipInfo);

			if (ipInfo != null) {
				ctripProxyIP = ipInfo.getIpExt();
				logger.info("根据IP类型获取的携程代理IP地址为：" + ctripProxyIP);
			}
		}
		maps.put("Param1", account.getUsername()+"|"+account.getPassword()+"|"+order.getOrderstr()+"|"+randCodeType+"|"+worker.getWorkerId()+"|"+ctripAcc.getCtrip_name()
				+"|"+ctripAcc.getCtrip_password()+"|"+ctripAcc.getCtrip_phone()+"|"+ctripAcc.getPay_password()+"|"+order.getPaymoney()
				+"|"+ctripAcc.getCookie()+"|"+ctripAcc.getCid()+"|"+ctripAcc.getAuth()+"|"+ctripAcc.getSauth()+"|"+ctripProxyIP);
		int i = 2;
		
		for (String param : order.getOrderCps()) {
			param=param +"|"+order.getSeatType();
			String[] paramArr=param.split("\\|");
			maps.put("Param"+i, param );
			i++;
		}
		
		logger.info(logPre+"start post request params:"+maps.toString());
		String param=null;
		try {
			param = UrlFormatUtil.createUrl("", maps, i);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		logger.info(logPre+"start post request params:"+param+" orderUrl:"+worker.getWorkerExt());
		
		//String reqResult=PostRequestUtil.getPostRes("UTF-8", worker.getWorkerExt(), param);
		String reqResult=HttpUtil.sendByGet(worker.getWorkerExt()+"?"+param, "UTF-8", "180000", "900000");
		
		reqResult = reqResult.replace("[\"{", "[{");
		reqResult = reqResult.replace("}\"]", "}]");
		reqResult = reqResult.replace("\\", "");
		
		//reValue:{"ErrorCode":0,"ErrorInfo":[{"payMoney":"75.50","arrivetime":"2015-07-28 10:10","robotNum":5,"outTicketBillno":"","retInfo":"","orderId":"HC1507240941445652","ctrip_id":"1404477111","retValue":"success"}]}
		logger.info(logPre+"reValue:"+reqResult);
		
		if(null == reqResult){
			logger.error(logPre+"post request Error retValue:"+reqResult);
			result.setErrorInfo("预定错误，没有返回结果！【"+reqResult+"】");
			//result.setReturnOptlog(ReturnOptlog.NORESULT);
			result.setRetValue(Result.MANUAL);
			return result;
		}
		
		//请求错误
		if(reqResult.equals(PostRequestUtil.TIME_OUT)||reqResult.equals(PostRequestUtil.CONNECT_ERROR)|reqResult.equals(PostRequestUtil.URL_ERROR)){
			logger.error(logPre+"post request Error retValue:"+reqResult);
			if(reqResult.contains("CONNECTERROR")){
				//result.setReturnOptlog(ReturnOptlog.SOS);
				result.setErrorInfo("机器异常，请尽快进入后台机器管理，针对该机器人进行【短信停用】！");
				result.setRetValue(Result.MANUAL);
				return result;
			}else{
				//result.setReturnOptlog(ReturnOptlog.TIMEOUT);
				result.setErrorInfo("机器预定超时，【"+reqResult+"】");
				result.setRetValue(Result.MANUAL);
				return result;
			}
		}
		
		//机器人处理超时
		if(reqResult.contains("\"ErrorCode\":9")){
			logger.error(logPre+"robot request Error retValue:"+reqResult);
			result.setErrorInfo("机器人处理超时【"+reqResult+"】处理：请核对12306网站是否已经订购！");
			//result.setReturnOptlog(ReturnOptlog.TIMEOUT);
			result.setRetValue(Result.MANUAL);
			return result;
		}
		
		if(reqResult.contains("\"ErrorCode\":88")){
			logger.error(logPre+"robot request Error retValue:"+reqResult);
			result.setErrorInfo("携程机器人处理异常【"+reqResult+"】处理：预订成功支付异常！");
			result.setRetValue(Result.MANUAL);
			return result;
		}
		/*
		 	{
			ctrip_id ="",携程订单号
			orderId   ="", --订单号
			outTicketBillno ="", --12306订单号
			retValue  ="", --结果状态
			retInfo     ="", --错误信息
			robotNum   =0,
			payMoney = 0，支付金额
			}
			retValue分为success和failure，当success时，ctrip_id不应该为空；当为failure时，ctrip_id为空，retInfo不为空
		 * */
		
		
		
		CtripReturnVO retObj = null;
		try {
			ObjectMapper map = new ObjectMapper();
			retObj = map.readValue(reqResult, CtripReturnVO.class);
		} catch (Exception e) {
			logger.error(logPre+"json request exception retValue:"+reqResult);
			result.setErrorInfo("JSON分析异常【"+reqResult+"】");
			//result.setReturnOptlog(ReturnOptlog.ROBOTEXCP);
			result.setRetValue(Result.MANUAL);
			return result;
		}
		
		try {
			String spare_thread =  retObj.getErrorInfo().get(0).getRobotNum();
			if(Integer.valueOf(spare_thread)>0){
				sysImpl.updateWorkerSpareThread(worker.getWorkerId() + "",Integer.valueOf(spare_thread));
			}
		} catch (Exception e1) {
			logger.info(logPre+"更新机器人空闲进程数异常！",e1);
		}
		
		
		if(!new Integer(0).equals(retObj.getErrorCode())){
			result.setRetValue(Result.MANUAL);
			result.setErrorInfo("【"+reqResult+"】");
			return result;
		}
		if(retObj.getErrorInfo().size()>0){
			CtripOrderVo val = retObj.getErrorInfo().get(0);
			String retValue = val.getRetValue();
			String retInfo = val.getRetInfo();
			
			result.setCtripId(val.getCtrip_id());
			if("success".equalsIgnoreCase(retValue)){
				//出票请求成功
				result.setRetValue(Result.SUCCESS);
				result.setPayMoney(val.getPayMoney());
				result.setSelfId(val.getOutTicketBillno());
				result.setCtrip_bx_money(val.getBxPrice());//携程的保险金额
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
	
	/**
	 * 权重----携程出票
	 * @throws DatabaseException 
	 * @throws RepeatException 
	 */
	private String deviceCtripWeight() {
		TrainServiceImpl service = new TrainServiceImpl();
		/*PC&APP 模式权重分配*/
		String defaultWeightMode = DeviceWeight.WEIGHT_PC;
		
		try {
			String pcWeightValue = service.getSysSettingValue("ctrip_" + DeviceWeight.WEIGHT_PC);//pc权重
			String appWeightValue = service.getSysSettingValue("ctrip_" + DeviceWeight.WEIGHT_APP);//app权重
			
			logger.info("[携程出票]权重系统设置pc_weight ,PC端: " + pcWeightValue + "app_weight ,移动端: " + appWeightValue);
			/*设置权重置*/
			List<DeviceWeight> modeCategories = new ArrayList<DeviceWeight>();//放各个权重的集合
			
			DeviceWeight pcMode = new DeviceWeight(DeviceWeight.WEIGHT_PC, Integer.parseInt(pcWeightValue));//pc权重
			DeviceWeight appMode = new DeviceWeight(DeviceWeight.WEIGHT_APP, Integer.parseInt(appWeightValue));//app权重
			
			modeCategories.add(pcMode);
			modeCategories.add(appMode);
			
			int count = 0;
			for(DeviceWeight category : modeCategories) {
				count += category.getWeight();
			}
			logger.info("[携程出票]权重最大边界 count : " + count);
			Integer nchannel = WEIGHT_RANDOM.nextInt(count); // n in [0, weightSum)
			logger.info("[携程出票]权重随机值 nchannel : " + nchannel);
			Integer mchannel = 0;
			for (DeviceWeight weightCategory : modeCategories) {
				if (mchannel <= nchannel && nchannel < mchannel + weightCategory.getWeight()) {
					defaultWeightMode = weightCategory.getCategory();
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
		
		return defaultWeightMode;

	}
	
	
	public static void main(String[] args) {
		//String reqResult="{\"ErrorCode\":0,\"ErrorInfo\":[{\"payMoney\":0,\"arrivetime\":\"\",\"robotNum\":8,\"outTicketBillno\":\"\",\"retInfo\":\"没有进入携程会员信息页面\",\"orderId\":\"HC1506191415009395\",\"ctrip_id\":\"22\",\"retValue\":\"failure\"}]}";
		
		String reqResult="{\"ErrorCode\":0,\"ErrorInfo\":[{\"payMoney\":\"75.50\",\"arrivetime\":\"2015-07-28 10:10\",\"robotNum\":5,\"outTicketBillno\":\"\",\"retInfo\":\"\",\"orderId\":\"HC1507240941445652\",\"ctrip_id\":\"1404477111\",\"retValue\":\"success\"}]}";
		System.out.println(reqResult);
		CtripReturnVO retObj = null;
		try {
			ObjectMapper map = new ObjectMapper();
			retObj = map.readValue(reqResult, CtripReturnVO.class);
			CtripOrderVo val = retObj.getErrorInfo().get(0);
			System.out.println(val.getCtrip_id());
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
