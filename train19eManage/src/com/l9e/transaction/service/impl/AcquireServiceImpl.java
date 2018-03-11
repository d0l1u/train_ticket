package com.l9e.transaction.service.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.AccountDao;
import com.l9e.transaction.dao.AcquireDao;
import com.l9e.transaction.service.AcquireService;
import com.l9e.transaction.vo.AccountVo;
import com.l9e.transaction.vo.AcquireVo;
import com.l9e.transaction.vo.CpVo;
import com.l9e.util.JSONUtil;
import com.l9e.util.StringUtil;

@Service("acquireService")
public class AcquireServiceImpl implements AcquireService {
	private static Logger logger = Logger.getLogger(AcquireServiceImpl.class);
	
	@Resource
	private AcquireDao acquireDao;
	@Resource
	private AccountDao accountDao;

	public List<Map<String, String>> queryAcquireList(
			Map<String, Object> paramMap) {
		return acquireDao.queryAcquireList(paramMap);
	}
	
	public List<Map<String, String>> queryAcquireListCp(
			Map<String, Object> paramMap) {
		return acquireDao.queryAcquireListCp(paramMap);
	}
	
	public List<Map<String, String>> queryAcquireExcel(
			Map<String, Object> paramMap) {
		return acquireDao.queryAcquireExcel(paramMap);
	}
	
	public List<Map<String, String>> queryAcquireFailExcel(
			Map<String, Object> paramMap) {
		return acquireDao.queryAcquireFailExcel(paramMap);
	}

	public int queryAcquireListCount(Map<String, Object> paramMap) {
		return acquireDao.queryAcquireListCount(paramMap);
	}
	
	public int queryAcquireListCountCp(Map<String, Object> paramMap) {
		return acquireDao.queryAcquireListCountCp(paramMap);
	}

	public Map<String, String> queryAcquireOrderInfo(String order_id) {
		return acquireDao.queryAcquireOrderInfo(order_id);
	}

	public List<Map<String, Object>> queryAcquireOrderInfoCp(String order_id) {
		return acquireDao.queryAcquireOrderInfoCp(order_id);
	}

	public void updateAcquire(AcquireVo acquire,AccountVo account){
			acquireDao.updateAcquire(acquire);
       		logger.info("acquireDao.updateAcquire开始执行。。");
			if("77".equals(acquire.getOrder_status())){//支付失败，出票失败，发送通知
				acquireDao.updateNotify(acquire);
			}
			if("10".equals(acquire.getOrder_status())){
				logger.info("进入出票失败方法！");
				acquireDao.freeAccount(acquire.getAccount_id());
				logger.info("释放账号执行完毕。。");
				acquireDao.updateNotify(acquire);
				logger.info("更新通知表完毕。。。");
				accountDao.insertAcc_logs(account);
				logger.info("----------------------插入Acc_log完成。。");
			}
			if("88".equals(acquire.getOrder_status())){//支付完成，释放账号或者停用账号
				logger.info("进入支付完成方法！");
				String account_id = acquire.getAccount_id();
				if(!StringUtils.isEmpty(account_id)){
					int contact_num = acquireDao.queryAccountContactNum(account_id);
					if(contact_num>=15){
						//该账号的常用联系人个数>=15，则停用账号，原因为：联系人达上限
						acquireDao.updateAccountStop(acquire.getAccount_id());
						logger.info("联系人达上限，停用账号执行完毕。。");
					}else{//否则释放账号
						acquireDao.freeAccount(acquire.getAccount_id());
						logger.info("释放账号执行完毕。。");
					}
				}else{
					logger.info("account_id为空。");
				}
			}
	}

//	public Map<String, String> queryPayOrderInfo(String order_id) {
//		return acquireDao.queryPayOrderInfo(order_id);
//	}

	public List<Map<String, Object>> queryHistroyByOrderId(String order_id) {
		return acquireDao.queryHistroyByOrderId(order_id);
	}

	public void updateAccount(Map<String,String>map,AccountVo account) {
		try{
			//切换
			acquireDao.updateAccount(map);
			//把之前获取到得账号设置为繁忙
			acquireDao.updateAccountStart(map);
			map.put("acc_status", "33");
			if(map.get("isStopAccount").equals("yes")){
				map.put("acc_status", "22");
				if(map.get("stop_reason").equals("6")){
					//勾选停用并且停用原因为用户取回
					acquireDao.updateRegistTo33(map);
				}
				if(map.get("stop_reason").equals("7")){
					//勾选停用并且停用原因为手机验证
				    //acquireDao.updateRegistTo33(map);
					logger.info("修改手机验证表");
				}
			}
			//释放上一个
			acquireDao.updateAccountAssoil(map);
			//添加日志
			accountDao.insertAcc_logs(account);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public String queryAccount(String channel) {
		return acquireDao.queryAccount(channel);
	}

	//82状态下修改的参数（与12306上的一致）
	public void updateCpDetail(Map<String,String> map, List<Map<String, String>> cpList) {
		String order_id =map.get("order_id").toString();
		logger.info("出票成功"+order_id);
		acquireDao.updateCp_Orderinfo_Buy_money(map);//修改主表中的价钱
		
		for(Map<String,String> cpInfo : cpList){
			acquireDao.updateCp_Orderinfo_Cp(cpInfo); //修改字表中车厢座位号和价钱
		}

		acquireDao.updateCp_Orderinfo_Notify(order_id); //修改通知
		map.put("opt_person", map.get("user"));
		acquireDao.updateAccountAssoil(map);
		AccountVo account = new AccountVo();
		account.setAcc_username(map.get("acc_username"));
		account.setOpt_person(map.get("user"));
		account.setOpt_logs(map.get("user")+"点击了出票成功！");
		account.setOrder_id(order_id);
		accountDao.insertAcc_logs(account);
	}

	public String queryDbOrder_status(String order_id) {
		return acquireDao.queryDbOrder_status(order_id);
	}

	public void addUserAccount(Map<String, String> paramMap) {
		acquireDao.addUserAccount(paramMap);
	}

	public void updateInfoOrderStatus(String order_id,String user) {
		Map<String,String>hashMap = new HashMap<String,String>();
		hashMap.put("order_id", order_id);
		hashMap.put("user", user);
		String is_pay=acquireDao.queryOrderIsPay(order_id);
		if("11".equals(is_pay))
			hashMap.put("order_status", "45");
		else
			hashMap.put("order_status", "00");
		acquireDao.updateInfoOrderStatus(hashMap);
	}

	public void updateChangeSeatTypeAndOrderInfo(Map<String, String> modify_Map,Map<String, String> modify_CpMap) {
		acquireDao.updateChangeSeatType(modify_Map);
		acquireDao.updateOrderInfo(modify_CpMap);//更新主表的坐席
		acquireDao.updateOrderInfo_cp(modify_CpMap);//更新字表的坐席和价钱
	}

	public void updateOrderInfo(Map<String, String> modify_CpMap) {
		acquireDao.updateOrderInfo(modify_CpMap);//更新主表的坐席
		acquireDao.updateOrderInfo_cp(modify_CpMap);//更新字表的坐席和价钱
	}

	public void updateCpListFor61(List<Map<String, String>> cpList) {
		for(Map<String,String> cpInfo : cpList){
			acquireDao.updateCp_Orderinfo_Cp(cpInfo); //修改字表中车厢座位号和价钱
		}
	}

	public void updateOrderInfoFor61(Map<String, String> update_Map) {
		acquireDao.updateOrderInfoFor61(update_Map);
	}

	public void updateOrderInfoFor45(Map<String, String> update_Map) {
		acquireDao.updateOrderInfoFor45(update_Map);
	}
	
	public void updateNotify(String order_id) {
		acquireDao.updateCp_Orderinfo_Notify(order_id);
	}

	@Override
	public String updateReceiveOrderInfo(String data) {
		String flag = "success";
		boolean orderflag = true;
		try{
			Map<String,Object> resultMap= JSONUtil.getMapFromJson(data);
			
			Map<String,String>order_Map = new HashMap<String,String>();
			Map<String,String>cp_Map = new HashMap<String,String>();
			if(resultMap.get("orderId")!=null && StringUtil.isNotEmpty(resultMap.get("orderId").toString())){
				order_Map.put("order_id", resultMap.get("orderId").toString());
			}else{
				logger.info("JSON解析异常，order_id为null");
				return flag="JSON解析异常，order_id为null";
			}
			if(resultMap.get("summoney")!=null && StringUtil.isNotEmpty(resultMap.get("summoney").toString())){
				order_Map.put("buy_money", resultMap.get("summoney").toString());
			}else{
				logger.info("JSON解析异常，buy_money为null");
				return flag="JSON解析异常，summoney为null";
			}
			if(resultMap.get("from")!=null && StringUtil.isNotEmpty(resultMap.get("from").toString())){
				order_Map.put("from_city", resultMap.get("from").toString());
			}else{
				logger.info("JSON解析异常，from_city为null");
				return flag="JSON解析异常，from为null";
			}
			if(resultMap.get("to")!=null && StringUtil.isNotEmpty(resultMap.get("to").toString())){
				order_Map.put("to_city", resultMap.get("to").toString());
			}else{
				logger.info("JSON解析异常，to_city为null");
				return flag="JSON解析异常，to为null";
			}
			if(resultMap.get("seattime")!=null && StringUtil.isNotEmpty(resultMap.get("seattime").toString())){
				order_Map.put("from_time", resultMap.get("seattime").toString());
			}else{
				logger.info("JSON解析异常，from_time为null");
				return flag="JSON解析异常，seattime为null";
			}
			if(resultMap.get("trainno")!=null && StringUtil.isNotEmpty(resultMap.get("trainno").toString())){
				order_Map.put("train_no", resultMap.get("trainno").toString());
			}else{
				logger.info("JSON解析异常，train_no为null");
				return flag="JSON解析异常，trainno为null";
			}
			if(resultMap.get("outTicketBillno")!=null && StringUtil.isNotEmpty(resultMap.get("outTicketBillno").toString())){
				order_Map.put("out_ticket_billno", resultMap.get("outTicketBillno").toString());
			}else{
				logger.info("JSON解析异常，out_ticket_billno为null");
				return flag="JSON解析异常，outTicketBillno为null";
			}
			
			//添加日志
			Map<String,String>log_Map = new HashMap<String,String>();
			Map<String,String>orderMap = acquireDao.queryAcquireOrderInfo(resultMap.get("orderId").toString());
			boolean time = resultMap.get("seattime").toString().equals(orderMap.get("from_time_all"));//时间异常
			boolean price = (Double.parseDouble(resultMap.get("summoney").toString())>Double.parseDouble(orderMap.get("pay_money")));//价格异常，差价
			boolean priceConfirm = (Double.parseDouble(resultMap.get("summoney").toString())<Double.parseDouble(orderMap.get("pay_money")));//价格异常，确认
			if(orderMap.get("order_status").equals(AcquireVo.ORDER_MANUAL)){ //如果主订单状态为人工预订则更改
				if(!time){
					orderflag=false;
					log_Map.put("userAccount", "乘车时间异常，处理：请出票后把确定的乘车时间通知客户！真实日期："+resultMap.get("seattime").toString()); 	
					acquireDao.updateReceiveOrderInfo(order_Map);
					flag="此订单乘车时间与传递的参数不一致";
				}else if(!time && price){	
					orderflag=false;
					log_Map.put("userAccount", "乘车时间异常，处理：请出票后把确定的乘车时间通知客户！真实日期："+resultMap.get("seattime").toString()
							+"<b>价格不相同，报价接口报价错误，10元误差可以出票！</b>"); 	
					acquireDao.updateReceiveOrderInfo(order_Map);
					flag="此订单乘车时间与传递的参数不一致";
					
				}else{
					acquireDao.updateReceiveOrderInfo(order_Map);
					log_Map.put("userAccount", "机器人预订更改订单参数，并预订成功！订单号为："+resultMap.get("orderId").toString()); 
				}
				if(price){
					acquireDao.updateReceiveOrderInfo(order_Map);
					log_Map.put("userAccount", "<b>价格不相同，报价接口报价错误，10元误差可以出票！</b>");
					orderflag=false;
				}else if(priceConfirm){
					acquireDao.updateReceiveOrderInfo(order_Map);
					log_Map.put("userAccount", "价格不相同，请确认价格【儿童票、特价票、卧铺票】！");
				}
			}else{
				//添加日志
				log_Map.put("userAccount", "机器人预订，此订单状态不为人【人工预订】！订单号为："+resultMap.get("orderId").toString()); 	
				return flag="此订单不为【人工预订】状态订单";
			}
			
			
			
			JSONArray jsonarray = JSONArray.fromObject(resultMap.get("cps")); 
			List list = (List)JSONArray.toList(jsonarray,CpVo.class); 
			Iterator it = list.iterator();  
	        while(it.hasNext()){  
	        	CpVo cp_info = (CpVo)it.next();
	        	if(StringUtil.isNotEmpty(cp_info.getCpId())){
	        		cp_Map.put("cp_id", cp_info.getCpId());
	        	}else{
	        		logger.info("子订单cp_id为空..");
	        		return flag="子订单cpid为空..";
	        	}
	        	if(StringUtil.isNotEmpty(cp_info.getPaymoney())){
	        		cp_Map.put("buy_money", cp_info.getPaymoney());
	        	}else{
	        		logger.info("子订单buy_money为空..");
	        		return flag="子订单cpid为空..";
	        	}
	        	if(StringUtil.isNotEmpty(cp_info.getSeatNo())){
	        		cp_Map.put("seat_no", cp_info.getSeatNo());
	        	}else{
	        		logger.info("子订单seat_no为空..");
	        		return flag="子订单seatno为空..";
	        	}
	        	if(StringUtil.isNotEmpty(cp_info.getTrainbox())){
	        		cp_Map.put("train_box", cp_info.getTrainbox());
	        	}else{
	        		logger.info("子订单train_box为空..");
	        		return flag="子订单trainbox为空..";
	        	}
	        	Map<String,Object>cp_map = acquireDao.queryCpInfo(cp_info.getCpId());
	        	boolean noseat = cp_info.getSeatNo().contains("无座")&& !cp_map.get("seat_type").equals(AcquireVo.SEAT_no);//无座
	        	boolean childPrice = Double.parseDouble(cp_info.getPaymoney().toString())!= Double.parseDouble(cp_map.get("pay_money").toString());//子订单票价不一致
	        	if(noseat){
	        		log_Map.put("userAccount", "帮助用户定到无座坐席，处理：请跟用户确认后是否需要再选择继续预定，还是预定失败！");
	        		orderflag=false;
	        	}else if(noseat && childPrice){
	        		log_Map.put("userAccount", "帮助用户定到无座坐席，处理：请跟用户确认后是否需要再选择继续预定，还是预定失败！"
	        				+"bookApp子订单票价不一致，请核对之后再做处理！");
	        	}else if(noseat && !time){
	        		log_Map.put("userAccount", "帮助用户定到无座坐席，处理：请跟用户确认后是否需要再选择继续预定，还是预定失败！"
	        				+"乘车时间异常，处理：请出票后把确定的乘车时间通知客户！真实日期："+resultMap.get("seattime").toString());
	        		acquireDao.updateReceiveOrderInfo(order_Map);
					flag="此订单乘车时间与传递的参数不一致";
	        		orderflag=false;
	        	}else if(noseat && price){
	        		log_Map.put("userAccount", "帮助用户定到无座坐席，处理：请跟用户确认后是否需要再选择继续预定，还是预定失败！"
	        				+"<b>价格不相同，报价接口报价错误，10元误差可以出票！</b>");
	        		acquireDao.updateReceiveOrderInfo(order_Map);
	        		orderflag=false;
	        	}else if(noseat && !time && price){
	        		log_Map.put("userAccount", "帮助用户定到无座坐席，处理：请跟用户确认后是否需要再选择继续预定，还是预定失败！"
	        				+"乘车时间异常，处理：请出票后把确定的乘车时间通知客户！真实日期："+resultMap.get("seattime").toString()
	        				+"<b>价格不相同，报价接口报价错误，10元误差可以出票！</b>");
	        		acquireDao.updateReceiveOrderInfo(order_Map);
					flag="此订单乘车时间与传递的参数不一致";
	        		orderflag=false;
	        	}
	        	if(childPrice){
	        		log_Map.put("userAccount", "bookApp子订单票价不一致，请核对之后再做处理！");
	        		orderflag=false;
	        	}
	        	acquireDao.updateReceiveCpInfo(cp_Map);
	        }  
	        /**************************添加日志************************/
	        log_Map.put("order_id", resultMap.get("orderId").toString());
			log_Map.put("order_time", acquireDao.queryOrder_time(resultMap.get("orderId").toString()));
			log_Map.put("user", "book_app");
			acquireDao.addUserAccount(log_Map);
			
			if(orderflag){
				String order_status = AcquireVo.PAY_START;
				acquireDao.updateOrderStatus(order_status,resultMap.get("orderId").toString());
				acquireDao.updateCp_Orderinfo_Notify(resultMap.get("orderId").toString());
			}
		}catch(Exception e){
			flag = "exception";
			logger.error("机器人传递预订成功参数Json解析异常。。Json为："+data);
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	public boolean updateLockAccount(String lockAccount) {
		boolean flag = true;
		try{
			acquireDao.updateLockAccount(lockAccount);
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	public String updateAcquireToRobot(Map<String, String> updateMap) {
		String result = "yes";
		try {
			acquireDao.updateAcquireToRobot(updateMap);
		} catch (Exception e) {
			result="no";
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public String queryCMpayOrderStatus(Map<String, String> paramMap) {
		return acquireDao.queryCMpayOrderStatus(paramMap);
	}

	@Override
	public String queryCodeType() {
		return acquireDao.queryCodeType();
	}

	@Override
	public String queryRobotRandom() {
		return acquireDao.queryRobotRandom();
	}

	@Override
	public Map<String,String> queryMoney(Map<String, String> updateMap) {
		return acquireDao.queryMoney(updateMap);
	}

	@Override
	public void updateCpPrice(Map<String, String> updateMap) {
		acquireDao.updateCpPrice(updateMap);
	}

	@Override
	public String queryOrderMoney(Map<String, String> updateMap) {
		return acquireDao.queryOrderMoney(updateMap);
	}

	@Override
	public List<Map<String, String>> queryAcquireOvertimeList(
			Map<String, Object> paramMap) {
		return acquireDao.queryAcquireOvertimeList(paramMap);
	}

	@Override
	public int queryOvertimeListCount(Map<String, Object> paramMap) {
		return acquireDao.queryOvertimeListCount(paramMap);

	}

	@Override
	public List<Map<String, String>> queryAcquireFailList(
			Map<String, Object> paramMap) {
		return acquireDao.queryAcquireFailList(paramMap);
	}

	@Override
	public int queryAcquireFailListCount(Map<String, Object> paramMap) {
		return acquireDao.queryAcquireFailListCount(paramMap);
	}

	@Override
	public String queryCurrentCodeType() {
		return acquireDao.queryCurrentCodeType();
	}

	@Override
	public void updateInfoOrderStatusTo55(Map<String, String> param_map) {
		acquireDao.updateInfoOrderStatusTo55(param_map);
	}

	@Override
	public List<Map<String, String>> queryAcquireExcelXl(
			Map<String, Object> paramMap) {
		return acquireDao.queryAcquireExcelXl(paramMap);
	}

	@Override
	public int queryAcquireListCountXl(Map<String, Object> paramMap) {
		return acquireDao.queryAcquireListCountXl(paramMap);
	}

	@Override
	public List<Map<String, String>> queryAcquireListXl(
			Map<String, Object> paramMap) {
		return acquireDao.queryAcquireListXl(paramMap);
	}

	@Override
	public void updateStatus00To11(Map<String, String> paramMap) {
		acquireDao.updateStatus00To11(paramMap);
	}

	@Override
	public String queryOrderIsPay(String order_id) {
		return acquireDao.queryOrderIsPay(order_id);
	}
	@Override
	public void updateManualToRobot(Map<String,String>map,Map<String,String> updateMap){
		acquireDao.updateManualToRobot(updateMap);
		//切换
		acquireDao.updateAccountToNull(map);
		//释放上一个
		acquireDao.updateAccountAssoil(map);
		//添加账号日志
		AccountVo account = new AccountVo();
		account.setAcc_username(map.get("acc_username"));
		account.setOpt_logs(map.get("opt_person")+"点击了切换人工出票到出票管理》》》释放该账号");
		account.setOpt_person(map.get("opt_person"));
		account.setOrder_id(map.get("order_id"));
		accountDao.insertAcc_logs(account);
	}

	@Override
	public void updateCtripToRobot(Map<String, String> updateMap) {
		acquireDao.updateCtripToRobot(updateMap);
		acquireDao.updateCtripGoStatusTo44(updateMap);
	}

	@Override
	public void updateEndOpt_Ren(Map<String, String> userMap) {
		acquireDao.updateEndOpt_Ren(userMap);
	}

	@Override
	public void ctripSearchAgain(Map<String, String> map) {
		acquireDao.ctripSearchAgain(map);
	}

	@Override
	public void updateCtripGoStatusTo44(Map<String, String> updateMap) {
		acquireDao.updateCtripGoStatusTo44(updateMap);
	}

	@Override
	public int updateInfoOrderStatusToManual(Map<String, String> param_map) {
		return	acquireDao.updateInfoOrderStatusToManual(param_map);
	}
	
}


