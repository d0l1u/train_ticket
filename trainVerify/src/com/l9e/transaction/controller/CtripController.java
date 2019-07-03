package com.l9e.transaction.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.AccountService;
import com.l9e.transaction.service.CommonService;
import com.l9e.util.DateUtil;

@Controller
@RequestMapping("/ctrip")
public class CtripController extends BaseController {
	private static final Logger logger = Logger.getLogger(CtripController.class);
	
	@Resource
	private AccountService accountService;
	
	@Resource
	private CommonService commonService;
	
	@RequestMapping("/interface.do")
	public void query(HttpServletRequest request, HttpServletResponse response) {
		String commond = this.getParam(request, "commond");
		
		if ("isOpen".equalsIgnoreCase(commond)){//train_system_setting 中配置携程注册开关。0-关闭  1-开启
			this.queryIsOpen(request, response);
		}else if ("queryGiftCard".equalsIgnoreCase(commond)){//获取礼品卡
			this.queryCardInfo(request, response);
		}else if("registerResult".equals(commond)){//更新账号信息(邮箱)
			this.updateCardInfo(request, response);
		}else if("GetAccountAndGiftCard".equals(commond)){ //随机获取注册成功待绑卡的携程账号及礼品卡并更新状态
			this.queryRandomAccAndUpdateStatus(request, response);
		}else if("CompleteRechange".equals(commond)){ //绑定携程礼品卡后的回调，根据返回状态更新
			this.updateAccAndCardByStatus(request, response);
		}else if("GetRegEmail".equals(commond)){//获取邮箱
			this.getRegEmail(request, response);
		}else if("GetModifyEmail".equals(commond)){//获取未修改密码的邮箱
			this.queryModifyEmail(request, response);
		}else if("CallBackEmail".equals(commond)){//修改邮箱密码后的回调
			this.callBackEmail(request, response);
		}else if("getCtripAccInfo".equals(commond)){//获取所有的携程账号和密码信息
			this.getCtripAccInfo(request, response);
		}else if("backAccount".equals(commond)){//获取携程账号随机一条：注册成功，开支付密码失败13
			this.backAccount(request, response);
		}else if("backLoginFaile".equals(commond)){//携程开支付密码的回调（更新登录失败的账号状态）
			this.backLoginFaile(request, response);
		}else if ("registerBack".equals(commond)) {//注册回调---最新手机注册
			this.registerBack(request, response);
		}
	}
	


	/**
	   * 随机获取注册成功待绑卡的携程账号及礼品卡，并更新状态
	   * @param request
	   * @param response
	   */
	private void queryRandomAccAndUpdateStatus(HttpServletRequest request,
			HttpServletResponse response){
		
		JSONObject resultJson = new JSONObject(); // 返回的json对象

		// 随机获取一个携程账号，ctrip_status状态为12
		Map<String, Object> queryAccMap = new HashMap<String, Object>();
		Map<String, Object> accountMap = null;
		queryAccMap.put("ctripStatus", "12") ;
		logger.info("随机获取携程账号查询参数queryAccMap为: " + queryAccMap);

		accountMap = accountService.queryRandomOneCtripAcc(queryAccMap);
		logger.info("随机获取的携程账号accountMap为: " + accountMap);

		// 随机获取一个礼品卡账号，ctrip_status状态为00（未使用）
		Map<String, Object> queryCardMap = new HashMap<String, Object>();
		Map<String, Object> cardMap = null;
		queryCardMap.put("ctripStatus", "00");
		logger.info("随机获取携程礼品卡账号查询参数queryCardMap为: " + queryCardMap);

		cardMap = accountService.queryRandomOneCtripCard(queryCardMap);
		logger.info("随机获取的携程礼品卡账号cardMap为: " + cardMap);

		//如果随机获取的携程账号和礼品卡账号都有值，则更新状态
		if (accountMap != null && cardMap != null) {
			Integer ctripAccId = Integer.parseInt(accountMap.get("ctrip_id").toString()); // 携程账号ID
			logger.info("随机获取的携程账号ID ctripAccId为: " + ctripAccId);

			Integer cardId = Integer.parseInt(cardMap.get("card_id").toString()); // 礼品卡ID
			logger.info("随机获取的携程礼品卡账号ID cardId为: " + cardId);

			// 更新ctrip_accountinfo表的ctrip_status状态为13(绑定礼品卡中)
			Map<String, Object> updateAccMap = new HashMap<String, Object>();
			updateAccMap.put("ctripId", ctripAccId);
			updateAccMap.put("ctripStatus", "13");
			accountService.modifyCtripAccountInfo(updateAccMap);
			logger.info("更新ctrip_accountinfo表的ctrip_status状态为13完成！");

			// 更新ctrip_cardinfo表的ctrip_status状态为11(使用中)
			Map<String, Object> updateCardMap = new HashMap<String, Object>();
			updateCardMap.put("cardId", cardId);
			updateCardMap.put("ctripStatus", "11");
			accountService.modifyCtripCardInfo(updateCardMap);
			logger.info("更新ctrip_cardinfo表的ctrip_status状态为11完成！");

			// 组装返回的json对象
			resultJson.put("code", "success");
			resultJson.put("ctrip_id", accountMap.get("ctrip_id"));
			resultJson.put("cookie", accountMap.get("cookie"));
			resultJson.put("cid", accountMap.get("cid"));
			resultJson.put("auth", accountMap.get("auth"));
			resultJson.put("sauth", accountMap.get("sauth"));

			resultJson.put("card_id", cardMap.get("card_id"));
			resultJson.put("ctrip_card_no", cardMap.get("ctrip_card_no"));
			resultJson.put("ctrip_card_pwd", cardMap.get("ctrip_card_pwd"));

		} else {
			resultJson.put("code", "failure");
		}
		logger.info("随机获取携程注册成功待绑卡的账号及礼品卡,更新状态后返回给机器人的json对象  resultJson为: "+ resultJson);
		printJson(response, resultJson.toString());
		logger.info("随机获取携程注册成功待绑卡的账号及礼品卡,更新状态完成！");

	}
	
	/**
	 * 绑定携程礼品卡后的回调，根据返回状态更新
	 * @param request
	 * @param response
	 */
	private void updateAccAndCardByStatus(HttpServletRequest request,
			HttpServletResponse response){
		
		// 先获取入参
		String ctripAccID = this.getParam(request, "account_id");// 携程账号ID
		String balance = this.getParam(request, "balance");// 携程账号礼品卡余额
		String registerStatus = this.getParam(request, "register_status");// 返回状态
		String giftCardID = this.getParam(request, "giftCard_id");// 礼品卡id
		//测试用
//		String ctripAccID = "385";// 携程账号ID
//		String balance = "500";// 携程账号礼品卡余额
//		String registerStatus = "2";// 返回状态
//		String giftCardID = "305";// 礼品卡id
		logger.info("绑定礼品卡后的回调  传入的参数: account_id 为: " + ctripAccID
				+ " balance为: " + balance + " register_status为: "
				+ registerStatus + " giftCard_id为: " + giftCardID);
		String accDegree = "";// 礼品卡等级
		String resultStr = "";// 返回提示信息
		
		/**
		 * 根据registerStatus状态来确定具体的操作:
		 * 如果状态为1（绑定礼品卡成功），则更新礼品卡表的状态为22（已使用），
		 * 并更新ctrip_accountinfo表的状态，余额，卡等级和礼品卡ID
		 * 如果状态为（0,2,3)则只更新礼品卡表的状态为33（不能使用）
		 */
		
		if ("1".equals(registerStatus)) {

			// 更新ctrip_cardinfo表的ctrip_status状态为22(已使用)
			Map<String, Object> updateCardMap = new HashMap<String, Object>();
			updateCardMap.put("ctripStatus", "22");
			updateCardMap.put("cardId", Integer.parseInt(giftCardID));
			logger.info("绑定携程礼品卡后的回调,返回状态为1时，更新礼品卡表的参数updateCardMap为: "+ updateCardMap);

			accountService.modifyCtripCardInfo(updateCardMap);
			logger.info("绑定携程礼品卡后的回调,返回状态为1时，更新礼品卡表状态完成！");

			
			// 更新ctrip_accountinfo表的ctrip_status状态为00(启用)，余额，礼品卡ID和等级
			accDegree = accountService.queryCtripAccDegree(balance);
			logger.info("绑定携程礼品卡后的回调,返回状态为1时，根据余额查询的礼品卡等级accDegree为: "+ accDegree);

			Map<String, Object> updateAccMap = new HashMap<String, Object>();
			updateAccMap.put("ctripStatus", "00"); // 00：启用
			updateAccMap.put("balance", Double.parseDouble(balance));
			if (accDegree != null && StringUtils.isNotEmpty(accDegree)) {
				updateAccMap.put("accDegree", Integer.parseInt(accDegree));
			}
			updateAccMap.put("cardId", giftCardID);
			updateAccMap.put("ctripId", Integer.parseInt(ctripAccID));
			logger.info("绑定携程礼品卡后的回调,返回状态为1时，更新携程账号表的参数updateAccMap为: "+ updateAccMap);

			accountService.modifyCtripAccountInfo(updateAccMap);
			logger.info("绑定携程礼品卡后的回调,返回状态为1时，更新携程账号表完成！");
			resultStr = "绑定携程礼品卡后的回调,返回状态为1时,更新携程账号表信息和礼品卡表的状态成功！";

		} else {
			// 更新ctrip_cardinfo表的ctrip_status状态为33(不能使用)
			Map<String, Object> updateCardMap = new HashMap<String, Object>();
			updateCardMap.put("ctripStatus", "33");
			updateCardMap.put("cardId", Integer.parseInt(giftCardID));
			logger.info("绑定携程礼品卡后的回调,返回状态为"+registerStatus+"时，更新礼品卡表的参数updateCardMap为: "+ updateCardMap);

			accountService.modifyCtripCardInfo(updateCardMap);
			logger.info("绑定携程礼品卡后的回调,返回状态为"+registerStatus+"时，更新礼品卡表状态完成！");
			resultStr = "绑定携程礼品卡后的回调,返回状态为"+registerStatus+"时,更新礼品卡表的状态成功！";
		}

		printJson(response, resultStr);
		logger.info("绑定携程礼品卡后的回调完成！");

	}
	
	
	//train_system_setting 中配置携程注册开关。0-关闭  1-开启
	private void queryIsOpen(HttpServletRequest request,
			HttpServletResponse response) {
		String ctrip_register_switch = commonService.querySysSettingByKey("ctrip_register_switch");
		printJson(response, ctrip_register_switch);
		logger.info("[获取携程注册开关]"+ctrip_register_switch);
	}

	//获取礼品卡
	private void queryCardInfo(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, String> map = accountService.queryCtripCard();
		
		if(map==null){
			printJson(response, "noCard");
			logger.info("[获取礼品卡]noCard");
		}else{
			//卡id|卡号|卡密|卡状态 (0-未使用)
			StringBuilder result = new StringBuilder();
			result.append(map.get("card_id")).append("|").append(map.get("ctrip_card_no")).append("|")
				  .append(map.get("ctrip_card_pwd")).append("|").append(map.get("ctrip_status"));
			
			printJson(response, result.toString());
			logger.info("[获取礼品卡]"+result.toString());
		}
		
	}
	
	//更新礼品卡、账号信息---注册成功绑定礼品卡回调
	private void updateCardInfo(HttpServletRequest request,
			HttpServletResponse response) {
		try{
			String account_name = this.getParam(request, "account_name");//登陆名
			String account_password = this.getParam(request, "account_password");//登陆密码
			String pay_password = this.getParam(request, "pay_password");//支付密码
			String mail_id = this.getParam(request, "mail_id");//邮箱id
			String cookie = this.getParam(request, "cookie");//携程wap端帐号cookie
			String cid = this.getParam(request, "cid");//携程wap端帐号cid
			String auth = this.getParam(request, "auth");//携程wap端帐号auth
			String sauth = this.getParam(request, "sauth");//携程wap端帐号sauth
			String ctrip_phone = this.getParam(request, "ctrip_phone");//携程手机号码
			String open_status = this.getParam(request, "open_status");//开支付密码状态，无：成功，1失败
//			String register_status = this.getParam(request, "register_status");//
//			String giftCard_id = this.getParam(request, "giftCard_id");//礼品卡id
//			String banlance = this.getParam(request, "banlance");//礼品卡金额
			//+"|register_status="+register_status+"|giftCard_id="+giftCard_id+"|banlance="+banlance
			
			logger.info("[注册成功绑定邮箱回调param]account_name="+account_name+"|account_password="+account_password
					+"|pay_password="+pay_password+"|cookie="+cookie+"|cid="+cid+"|auth="+auth+"|sauth="+sauth+"|mail_id="+mail_id);
			
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("ctrip_name", account_name);
			//paramMap.put("ctrip_password", account_password);
			List<Map<String, String>> ctripList = accountService.queryCtripAccount(paramMap);//根据account_name查询是否存在
			if(ctripList.size()>0){
				Map<String, String> map = ctripList.get(0);
				if(map.get("ctrip_status")!= null && "12".equals(map.get("ctrip_status"))){
					printJson(response, "fail");
					logger.info("[注册成功绑定邮箱回调fail]"+account_name+"账号名已存在");
				}else{
					//更新
					map.put("ctrip_name", account_name);
					map.put("ctrip_password", account_password);
					map.put("pay_password", pay_password);
					map.put("opt_person", "ctrip_app");
					map.put("cookie", cookie);
					map.put("cid", cid);
					map.put("auth", auth);
					map.put("sauth", sauth);
					map.put("ctrip_username", "zuoA");
					map.put("ctrip_phone", ctrip_phone);
					map.put("mail_id", mail_id);
					map.put("ctrip_status", "12");//12，注册成功
					accountService.updateAccountInfoById(map);
					//更新ctrip_cardinfo表的map
					Map<String, String> updateMailMap = new HashMap<String, String>();
					updateMailMap.put("mail_id", mail_id);
					updateMailMap.put("new_email_status", "2");//将该邮箱更新为2  已使用
					accountService.updateRegEmail(updateMailMap);//更新邮箱的状态
					
					printJson(response, "success");
					logger.info("[注册成功绑定邮箱回调success]"+account_name+"插入ctrip_accountinfo表");
				}
				
			}else{
				//校验mail_id是否存在
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", Integer.parseInt(mail_id));
				map.put("mail_account", account_name);
				List<Map<String, Object>> querylist = accountService.queryCtripMailInfo(map);
				if(querylist.size()==0){
					printJson(response, "fail");
					logger.info("[注册成功绑定邮箱回调fail]"+mail_id+"邮箱id不存在");
				}else{
					//插入ctrip_accountinfo表的map
					Map<String, String> insertMap = new HashMap<String, String>();
					insertMap.put("ctrip_name", account_name);
					insertMap.put("ctrip_password", account_password);
					insertMap.put("pay_password", pay_password);
					insertMap.put("opt_person", "ctrip_app");
					insertMap.put("cookie", cookie);
					insertMap.put("cid", cid);
					insertMap.put("auth", auth);
					insertMap.put("sauth", sauth);
	//				insertMap.put("card_id", giftCard_id);
					insertMap.put("ctrip_username", "zuoA");
					insertMap.put("ctrip_phone", ctrip_phone);
					insertMap.put("mail_id", mail_id);
					
					//更新ctrip_cardinfo表的map
	//				Map<String, String> updateMap = new HashMap<String, String>();
	//				updateMap.put("card_id", giftCard_id);
					
					if("1".equals(open_status)){
						insertMap.put("ctrip_status", "13");//13，注册成功，开支付密码失败
					}else{
						insertMap.put("ctrip_status", "12");//12，注册成功待绑卡
					}
	//				updateMap.put("new_ctrip_status", "22");//礼品卡状态：00未使用 11使用中 22已使用 33不能使用
					
					/*//register_status 0-注册成功绑定礼品卡失败 1-注册成功绑定礼品卡成功 2-注册成功绑定礼品卡成功，查询余额失败 3-注册成功,礼品卡无效
					//账号状态： 44-注册成功绑定礼品卡成功，暂时停用 55-注册成功绑定礼品卡失败，暂时停用 66-注册成功绑定礼品卡成功，查询余额失败，暂时停用
					if("1".equals(register_status)){//1-注册成功绑定礼品卡成功
						insertMap.put("ctrip_status", "44");
						insertMap.put("balance", banlance);
						
						//acc_degree根据余额查询ctrip_amountarea_conf
						String acc_degree = accountService.queryCtripAccDegree(banlance);
						
						if(acc_degree!=null && StringUtils.isNotEmpty(acc_degree)){
							insertMap.put("acc_degree", acc_degree);
							logger.info("[注册成功绑定礼品卡回调]查询acc_degree="+acc_degree+",banlance="+banlance);
						}else{
							insertMap.put("acc_degree", "99");
							logger.info("[注册成功绑定礼品卡回调]查询acc_degree=99,banlance="+banlance+"(ctrip_amountarea_conf表没有符合条件的面值)");
						}
						
						updateMap.put("new_ctrip_status", "22");//礼品卡状态：00未使用 11使用中 22已使用 33不能使用
					}else if("2".equals(register_status)){// 2-注册成功绑定礼品卡成功，查询余额失败
						insertMap.put("ctrip_status", "66");
						
						updateMap.put("new_ctrip_status", "22");//礼品卡状态：00未使用 11使用中 22已使用 33不能使用
					}else if("3".equals(register_status)){//3-注册成功,礼品卡无效
						insertMap.put("ctrip_status", "55");
						
						updateMap.put("new_ctrip_status", "33");//礼品卡状态：00未使用 11使用中 22已使用 33不能使用
					}else if("0".equals(register_status)){//0-注册成功绑定礼品卡失败
						insertMap.put("ctrip_status", "55");
						
						updateMap.put("new_ctrip_status", "00");//礼品卡状态：00未使用 11使用中 22已使用 33不能使用
					}*/
					
					accountService.addCtripAccount(insertMap);
					
	//				accountService.updateCardInfo(updateMap);//更新礼品卡的状态
					
					//更新ctrip_cardinfo表的map
					Map<String, String> updateMailMap = new HashMap<String, String>();
					updateMailMap.put("mail_id", mail_id);
					updateMailMap.put("new_email_status", "2");//将该邮箱更新为2  已使用
					accountService.updateRegEmail(updateMailMap);//更新邮箱的状态
					
					printJson(response, "success");
					logger.info("[注册成功绑定邮箱回调success]"+account_name+"插入ctrip_accountinfo表");
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			printJson(response, "exception");
			logger.info("[注册成功绑定邮箱回调exception]");
		}
		
	}
	
	
	
	//更新账号信息---注册回调
	private void registerBack(HttpServletRequest request, HttpServletResponse response) {
		try{
			String account_name = this.getParam(request, "account_name");//登陆名
			String account_password = this.getParam(request, "account_password");//登陆密码
			String pay_password = this.getParam(request, "pay_password");//支付密码 
			String cookie = this.getParam(request, "cookie");//携程wap端帐号cookie
			String cid = this.getParam(request, "cid");//携程wap端帐号cid
			String auth = this.getParam(request, "auth");//携程wap端帐号auth
			String sauth = this.getParam(request, "sauth");//携程wap端帐号sauth
			String ctrip_phone = this.getParam(request, "ctrip_phone");//携程手机号码
			String open_status = this.getParam(request, "open_status");//开支付密码状态，无：成功，1失败 
			
			logger.info("[注册成功回调param]account_name="+account_name+"|account_password="+account_password
					+"|pay_password="+pay_password+"|cookie="+cookie+"|cid="+cid+"|auth="+auth+"|sauth="+sauth);
			
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("ctrip_name", account_name); 
			List<Map<String, String>> ctripList = accountService.queryCtripAccount(paramMap);//根据account_name查询是否存在
			if(ctripList.size()>0){
				Map<String, String> map = ctripList.get(0);
				if(map.get("ctrip_status")!= null && "12".equals(map.get("ctrip_status"))){
					printJson(response, "fail");
					logger.info("[注册成功回调fail]"+account_name+"账号名已存在");
				}else{
					//更新
					map.put("ctrip_name", account_name);
					map.put("ctrip_password", account_password);
					map.put("pay_password", pay_password);
					map.put("opt_person", "ctrip_app");
					map.put("cookie", cookie);
					map.put("cid", cid);
					map.put("auth", auth);
					map.put("sauth", sauth);
					map.put("ctrip_username", "zuoA");
					map.put("ctrip_phone", ctrip_phone); 
					map.put("ctrip_status", "12");//12，注册成功
					accountService.updateAccountInfoById(map);  
					printJson(response, "success");
					logger.info("[注册成功回调success]"+account_name+"插入ctrip_accountinfo表");
				} 
			}else{
				// 插入ctrip_accountinfo表的map
				Map<String, String> insertMap = new HashMap<String, String>();
				insertMap.put("ctrip_name", account_name);
				insertMap.put("ctrip_password", account_password);
				insertMap.put("pay_password", pay_password);
				insertMap.put("opt_person", "ctrip_app");
				insertMap.put("cookie", cookie);
				insertMap.put("cid", cid);
				insertMap.put("auth", auth);
				insertMap.put("sauth", sauth);
				insertMap.put("ctrip_username", "zuoA");
				insertMap.put("ctrip_phone", ctrip_phone);
				if ("1".equals(open_status)) {
					insertMap.put("ctrip_status", "13");// 13，注册成功，开支付密码失败
				} else {
					insertMap.put("ctrip_status", "12");// 12，注册成功待绑卡
				}
				accountService.addCtripAccount(insertMap); 
				printJson(response, "success");
				logger.info("[注册成功回调success]" + account_name + "插入ctrip_accountinfo表");

			}
		}catch(Exception e){
			e.printStackTrace();
			printJson(response, "exception");
			logger.info("[注册成功回调exception]");
		}
		
	}

	
	//获取邮箱
	private void getRegEmail(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, String> map = accountService.queryRegEmail();
		
		if(map==null){
			printJson(response, "noEmail");
			logger.info("[获取邮箱]noEmail");
		}else{
			//邮箱id|账号|密码
			StringBuilder result = new StringBuilder();
			result.append(map.get("mail_id")).append("|").append(map.get("mail_account")).append("|")
				  .append(map.get("mail_pwd"));
			
			printJson(response, result.toString());
			logger.info("[获取邮箱]"+result.toString());
		}
	}
	
	
	//获取邮箱
	private void queryModifyEmail(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, String> map = accountService.queryModifyEmail();
		
		if(map==null){
			printJson(response, "noEmail");
			logger.info("[获取邮箱]noEmail");
		}else{
			//邮箱id|账号|密码
			StringBuilder result = new StringBuilder();
			result.append(map.get("mail_id")).append("|").append(map.get("mail_account")).append("|")
				  .append(map.get("mail_pwd")); 
			printJson(response, result.toString());
			logger.info("[获取邮箱]"+result.toString());
		}
	}
	
	//修改邮箱密码后的回调
	private void callBackEmail(HttpServletRequest request,
			HttpServletResponse response) {
		String mail_id = this.getParam(request, "mail_id");
		String mail_account = this.getParam(request, "mail_account");//邮箱账号
		String mail_newPwd = this.getParam(request, "mail_newPwd");//邮箱新密码密码
		String mail_oldPwd = this.getParam(request, "mail_oldPwd");//邮箱原密码
		logger.info("修改邮箱密码后的回调,邮箱账号:"+mail_account+",邮箱新密码:"+mail_newPwd+",邮箱原密码:"+mail_oldPwd);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("mail_id", Integer.parseInt(mail_id));
		map.put("mail_account", mail_account);
		map.put("mail_pwd", mail_newPwd);
		map.put("is_modify", 0);
		map.put("remark", mail_oldPwd);
		accountService.updateModifyEmail(map);
		printJson(response, "success");
		logger.info("修改邮箱密码回调成功!");
	} 
	
	//获取所有的携程账号和密码信息
	private void getCtripAccInfo(HttpServletRequest request,
			HttpServletResponse response) {
		List<Map<String, String>> list = accountService.queryCtripAccInfo();
		if(list.size()>0){
			JSONArray arr = new JSONArray();
			for(Map<String, String> map : list){
				JSONObject json = new JSONObject();
				json.put("ctrip_name", map.get("ctrip_name"));
				json.put("ctrip_password", map.get("ctrip_password"));
				arr.add(json);
			}
			printJson(response, arr.toString());
			logger.info("[获取携程账号]"+arr.toString());
		}else{
			printJson(response, "noAcc");
			logger.info("[获取携程账号]noAcc");
		}
	}
	
	//获取携程账号随机一条：注册成功，开支付密码失败13
	private void backAccount(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> map = accountService.queryCtripAccInfoByBack();
		if(map != null){
			//更新状态
			Map<String, Object> updateMap = new HashMap<String, Object>();
			updateMap.put("ctripId", map.get("ctrip_id"));
			Date y = (Date)map.get("open_time");//null：直接改成3，等于当天：次数+1，不等于当天：=1
			if(y == null){
				updateMap.put("count", 3);
			}else{
				String openTime = DateUtil.dateToString(y, DateUtil.DATE_FMT1);
				String todayTime = DateUtil.dateToString(new Date(), DateUtil.DATE_FMT1);
				if(openTime.equals(todayTime)){
					int dayCount = Integer.parseInt((String)map.get("open_day_count"));
					updateMap.put("count", dayCount+1);
				}else{
					updateMap.put("count", 1);
				}
			}
			int sumCount = 1;
			if(map.get("open_sum_count") != null ){
				sumCount = Integer.parseInt((String)map.get("open_sum_count")) +sumCount;
			}
			updateMap.put("sumCount", sumCount);
			accountService.updateAccountById(updateMap);
			//返回
			JSONObject json = new JSONObject();
			json.put("account_id", map.get("ctrip_id"));
			json.put("account_name", map.get("ctrip_name"));
			json.put("account_password", map.get("ctrip_password"));
			json.put("mail_pwd", map.get("mail_pwd"));
			printJson(response, json.toString());
			logger.info("[获取携程账号]"+json.toString());
		}else{
			printJson(response, "noAcc");
			logger.info("[获取携程账号]noAcc");
		}
	}
	
	//携程开支付密码的回调（更新登录失败的账号状态）
	private void backLoginFaile(HttpServletRequest request,
			HttpServletResponse response) {
		String ctrip_id = this.getParam(request, "account_id");//登录ID
		//String ctrip_name = this.getParam(request, "account_name");//登陆名
		//String ctrip_password = this.getParam(request, "account_password");//登陆密码
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ctripId", ctrip_id);
		map.put("ctripStatus", "11");
		accountService.modifyCtripAccountInfo(map);
	}
}
