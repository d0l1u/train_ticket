package com.l9e.transaction.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.PhoneCheckService;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.DbContextHolder;
import com.l9e.util.HttpPostUtil;

/**
 * 手机核验
 * @author zhangjc02
 *
 */
@Component("PhoneCheck_Job")
public class PhoneCheck_Job {
	private static final Logger logger = Logger.getLogger(PhoneCheck_Job.class);
	@Resource
	private PhoneCheckService phoneCheckService;
	
	static String status = "start";
	static String status2 = "start";
	
	public void queryNeedCheck(){
		DbContextHolder.setDbType("dataSource1");// 设置数据源为备份数据库
			logger.info("status:"+status+"status2:"+status2);
			if(status=="start" || status == "finish"){
				this.getPhoneAccount();//待核验
			}else{
				logger.info("getPhoneAccount上次执行未结束，本次不做处理!!!");
			}
			
			if(status2=="start" || status2 == "finish"){
				this.updateToRobot();//在退款表为45的且查询核验成功的，改成机器处理
			}else{
				logger.info("updateToRobot上次执行未结束，本次不做处理!!!");
			}
		
		}
	
	private void getPhoneAccount(){
		status = "running";
		try {
			Map<String ,Object> querymap = new HashMap<String, Object>();
			querymap.put("order_status", "44");
			List<Map<String , String>> list = phoneCheckService.queryAccountInfo(querymap);
			if(list.size()>0){
				logger.info("[手机核验]查询【需要绑定手机号】 的账号个数为："+list.size());
//				String url= "http://www.19trip.com/train_phone/phone/getPhone";
//				StringBuffer sb = new StringBuffer();
//				sb.append("count=").append(list.size());//获取数量没有则默认返回最多10个手机号
//				String result = HttpPostUtil.sendAndRecive(url, sb.toString());
//				if(!StringUtils.isEmpty(result)){
//					logger.info("[手机核验]获取手机号result:"+result);
//					try {
//						JSONObject json = JSONObject.fromObject(result);
//						if(json.getBoolean("success")){
//							JSONArray arr = JSONArray.fromObject(json.getString("data"));
//							logger.info("[手机核验]接口获取手机号个数为："+arr.size());
//							for(int i=0;i<=arr.size();i++){
						int num = 0;
							for(int i=0;i<list.size();i++){
//								if(sendMsg(arr.getString(i))){
								if(true){
									Map<String, String> map = new HashMap<String, String>();
//									map.put("phone", arr.getString(i));
									map.put("check_status", "1");//修改到等待接收短信
									map.put("account_id", String.valueOf(CreateIDUtil.createAccountId()));
									map.put("account_username", list.get(i).get("account_name"));
									map.put("account_password", list.get(i).get("account_pwd"));
									map.put("user_id", list.get(i).get("refund_seq"));
									map.put("opt_ren", "pc_job");
									map.put("check_status", "0");
									
									map.put("old_status", "44");
									map.put("new_status", "45");
									map.put("order_id", list.get(i).get("order_id"));
									map.put("cp_id", list.get(i).get("cp_id"));
									map.put("refund_seq", list.get(i).get("refund_seq"));
									
									boolean bb = phoneCheckService.addAccountCheckInfo(map);
									if(bb){
										logger.info("[手机核验]填入数据库成功，"+list.get(i).get("order_id"));
									}else{
										logger.info("[手机核验]填入数据库失败，"+list.get(i).get("order_id"));
									}
								}
//									else{
//									logger.info("[手机核验]发送短信失败phone:"+arr.getString(i));
//								}
								num ++;
							}
							logger.info("num="+num+"list.size()="+list.size());
							if(num == list.size()){
								status = "finish";
							}
//						}else{
//							logger.info("[手机核验]接口获取手机号失败。");
//						}
//					} catch (Exception e) {
//						logger.error("[手机核验]获取手机号异常"+e);
//					}
					
//				}else{
//					logger.error("[手机核验]获取手机号result:空");
//				}
			}else{
				logger.info("[手机核验]查询需要核验的账号个数为：0");
				status = "finish";
			}
		} catch (Exception e) {
			logger.error("[手机核验]异常："+e);
		}
	}
	
	private void updateToRobot(){
		status2 = "running";
		try {
			Map<String ,Object> querymap = new HashMap<String, Object>();
			querymap.put("order_status", "45");
			querymap.put("check_status", "2");
			List<Map<String , String>> list = phoneCheckService.queryAccountInfo(querymap);
			if(list.size()>0){
				logger.info("[手机核验]查询需要改机器处理 的个数为："+list.size());
					try {
						int num = 0;
							for(int i=0;i<list.size();i++){
									Map<String, String> map = new HashMap<String, String>();
									map.put("old_status", "45");
									map.put("new_status", "04");
									map.put("opt_ren", "pc_job");
									map.put("order_id", list.get(i).get("order_id"));
									map.put("cp_id", list.get(i).get("cp_id"));
									map.put("refund_seq", list.get(i).get("refund_seq"));
									
									boolean bb = phoneCheckService.updateRefundStatus(map);
									if(bb){
										logger.info("[手机核验]upToRobot成功，"+list.get(i).get("order_id"));
									}else{
										logger.info("[手机核验]upToRobot失败，"+list.get(i).get("order_id"));
									}
								num ++;
							}
							logger.info("num="+num+"list.size()="+list.size());
							if(num == list.size()){
								status2 = "finish";
							}
					} catch (Exception e) {
						logger.error("[手机核验]查询需要改机器处理异常"+e);
					}
			}else{
				logger.info("[手机核验]查询需要改机器处理个数为：0");
				status2 = "finish";
			}
		} catch (Exception e) {
			logger.error("[手机核验]异常："+e);
		}
	}
	
//	private void queryAccount(){
//		try {
//			Map<String ,Object> querymap = new HashMap<String, Object>();
//			querymap.put("is_put", "11");//未填入cp_accountinfo_check
//			List<Map<String , String>> list = phoneCheckService.queryAccountInfo(querymap);//查询需要核验的账号cp_orderinfo_refund 44且 未填入cp_accountinfo_check
//			logger.info("[手机核验]查询【已绑定手机号】的账号个数为："+list.size());
//			if(list.size()>0){
//					for(int i=0;i<=list.size();i++){
//					String phone = list.get(i).get("phone");
//					if(sendMsg(phone)){
//						Map<String, String> map = new HashMap<String, String>();
//						map.put("phone", phone);
//						map.put("check_status", "1");//修改到等待接收短信
//						map.put("account_username", list.get(i).get("account_name"));
//						map.put("account_password", list.get(i).get("account_pwd"));
//						map.put("user_id", list.get(i).get("refund_seq"));
//						map.put("old_status", "44");
//						map.put("new_status", "45");
//						boolean bb = phoneCheckService.updateAccountCheckInfo(map);
//						if(bb){
//							logger.info("[手机核验]重新发送成功，"+phone); 
//						}else{
//							logger.info("[手机核验]重新发送失败，"+phone);
//						}
//					}else{
//						logger.info("[手机核验]发送短信失败phone:"+phone);
//					}
//				}
//			}else{
//				logger.info("[手机核验]查询需要核验的账号个数为：0");
//			}
//		} catch (Exception e) {
//			logger.error("[手机核验]异常："+e);
//		}
//	}
	
	
//	//发送手机短信
//	private boolean sendMsg(String phone){
//		boolean flag = false;
//		try {
//			String url= "http://www.19trip.com/train_phone/phone/sendMsg";
//			StringBuffer sb = new StringBuffer();
//			sb.append("dest=").append("12306").append("&message=").append("999").append("&number=").append(phone);//发送短信
//			logger.info("[手机核验]发送短信param="+sb.toString());
//			String result = HttpPostUtil.sendAndRecive(url, sb.toString());
//			logger.info("[手机核验]发送短信结果："+result);
//			JSONObject json = JSONObject.fromObject(result);
//			flag = json.getBoolean("success");
//		} catch (Exception e) {
//			logger.error("[手机核验]发送短信异常"+e);
//		}
//		return flag;
//	}
	
//	//获取短信
//	private void getPhoneMsg(){
//		List<Map<String, String>> phonelist = phoneCheckService.getPhoneMsgList();
//		logger.info("[手机核验]待查询的手机号个数为："+phonelist.size());
//		if(phonelist.size()>0){
//			for(int i=0;i<=phonelist.size();i++){
//				try {
//					String url= "http://www.19trip.com/train_phone/phone/getMsg";
//					StringBuffer sb = new StringBuffer();
//					sb.append("number=").append(phonelist.get(i).get("phone"));//发送短信
//					logger.info("[手机核验]接收短信param="+sb.toString());
//					String result = HttpPostUtil.sendAndRecive(url, sb.toString());
//					logger.info("[手机核验]接收短信结果："+result);
//					JSONObject json = JSONObject.fromObject(result);
//					if(json.getBoolean("success")){
//						JSONObject data = JSONObject.fromObject(json.getString("data"));
//						String message = data.getString("message");
//						logger.info("[手机核验]phone:"+phonelist.get(i)+"获取短信内容为："+message);
//						Map<String, String> map = new HashMap<String, String>();
//						map.put("phone", phonelist.get(i).get("phone"));
//						map.put("account_id", phonelist.get(i).get("account_id"));
//						map.put("user_id", phonelist.get(i).get("user_id"));
//						String phone_code = message;//截取短信内的验证码
//						map.put("phone_code", phone_code);
//						if(!MapUtils.isEmpty(map)){
//							sendCheck(map);
//						}
//					}else{
//						logger.error("[手机核验]phone:"+phonelist.get(i)+"获取短信失败，不更新状态等待重新获取。");
//					}
//				} catch (Exception e) {
//					logger.error("[手机核验]发送短信异常"+e);
//				}
//			}
//		}
//	}
	
//	//发送验证请求
//	private void sendCheck(Map<String,String> map){
//		logger.info("[手机核验]待查询的手机号个数为："+map.get("phone"));
//		String result = "";
//		StringBuffer sb = new StringBuffer();
//		sb.append("userId=").append(map.get("user_id"))
//		  .append("&phone=").append(map.get("phone"))
//		  .append("&checkcode=").append(map.get("phone_code"))
//		  .append("&accountId=").append(map.get("account_id"));
//		String url ="http://10.3.12.95:18088/check_account/checkAccount/sendCheck";
//			try {
//					logger.info("【手机核验】开始验证，agentId:"+map.get("user_id")+"|accountId："+map.get("account_id"));
//					result = HttpPostUtil.sendAndRecive(url, sb.toString());
//					logger.info("【手机核验】发送验证result="+result);
//					JSONObject json =JSONObject.fromObject(result);
//					if(json.getBoolean("success")){
//						logger.info("【手机核验】发送验证成功accountId:"+map.get("account_id"));
//					}else{
//						logger.info("【手机核验】发送验证失败accountId:"+map.get("account_id"));
//					}
//				} catch (Exception e) {
//					logger.error("【手机核验】发送验证请求到服务失败:"+e);
//			}
//	}
	
}
