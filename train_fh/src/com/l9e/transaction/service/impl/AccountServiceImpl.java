package com.l9e.transaction.service.impl;

import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.l9e.common.Consts;
import com.l9e.transaction.service.AccountService;
import com.l9e.transaction.service.BaseHttpService;
import com.l9e.transaction.vo.Account;
import com.l9e.util.JsonlibUtil;

import common.Logger;


@Service("accountService")
public class AccountServiceImpl extends BaseHttpService implements AccountService {
	
	private static final Logger logger = Logger.getLogger(AccountServiceImpl.class);

	@Override
	public Account getChannelAccount(String channel) {
		Account account = null;
		
		int count = 0;
		do {
			try {
				String result = requestPost(Consts.GET_CHANNEL_ACCOUNT, "channel=" + channel, "UTF-8", 3, 500);
				logger.info("get account by channel, account result : " + result);
				if(!StringUtils.isEmpty(result)) {
					JSONObject resultJsonObject = JSONObject.fromObject(result);
					if(resultJsonObject.has("success") && resultJsonObject.getBoolean("success")) {
						account = JsonlibUtil.json2Bean(resultJsonObject.getJSONObject("data").toString(), Account.class);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			count++;
		} while(account == null && count < 5);
		return account;
	}

	@Override
	public Account getOrderAccount(Integer id) {
		Account account = null;
		
		try {
			String result = requestPost(Consts.GET_ORDER_ACCOUNT, "id=" + id, "utf-8", 3, 500);
			logger.info("get account by id , account result : " + result);
			if(!StringUtils.isEmpty(result)) {
				JSONObject resultJsonObject = JSONObject.fromObject(result);
				if(resultJsonObject.has("success") && resultJsonObject.getBoolean("success")) {
					account = JsonlibUtil.json2Bean(resultJsonObject.getJSONObject("data").toString(), Account.class);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return account;
	}

	@Override
	public void stopAccount(Integer id, String reason) {
		try {
			String result = requestPost(Consts.STOP_ACCOUNT, "reason=" + reason + "&id=" + id, "utf-8", 0, 0);
			logger.info("stop account result : " + result);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateAccount(Account account) {
		/*账号实体转json串*/
		String accountJson = "";
		
		try {
			accountJson = JSONObject.fromObject(account).toString();
		} catch (Exception ee) {
			logger.info("json account error ,account : " + account + ", exception : " + ee.getMessage());
			ee.printStackTrace();
		}
		
		try {
			String result = requestPost(Consts.UPDATE_ACCOUNT, "account=" + accountJson, "utf-8", 0, 0);
			logger.info("update account result : " + result);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Account filterAccount(String passportNo) {
		Account account = null;
		
		try {
			String result = requestPost(Consts.FILTER_ACCOUNT, "passportNo=" + passportNo, "utf-8", 3, 500);
			logger.info("passportNo filter account, account result : " + result);
			if(!StringUtils.isEmpty(result)) {
				JSONObject resultJsonObject = JSONObject.fromObject(result);
				if(resultJsonObject.has("success") && resultJsonObject.getBoolean("success")) {
					account = JsonlibUtil.json2Bean(resultJsonObject.getJSONObject("data").toString(), Account.class);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return account;
	}

	@Override
	public void releaseAcoount(Integer id) {
		try {
			String result = requestPost(Consts.RELEASE_ACCOUNT, "id=" + id, "utf-8", 0, 0);
			logger.info("release account result : " + result);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * add by wangsf
	 * 对自带12306账号登录验证的结果处理
	 * @param userName，pass,passportNo
	 * @return errorCode
	 */
	@Override
	public String handleBindAccErrorCode(String userName, String pass,String passportNo) {
		// TODO Auto-generated method stub
		String errorCode = "0";
		try {
			String result = requestPost(Consts.HANDLE_BIND_ACCOUNT, "userName="+ userName + "&pass=" + pass + "&passportNo=" + passportNo,"utf-8", 3, 500);
			logger.info("verifyAccountforTuniu , errorcode result : " + result);
			if (!StringUtils.isEmpty(result)) {
				JSONObject resultJsonObject = JSONObject.fromObject(result);
				Map resultJsonMap = (Map) resultJsonObject;
				String errorCodeStr=resultJsonMap.get("data").toString();
				if (resultJsonObject.has("success")
						&& resultJsonObject.getBoolean("success")) {
					if(null != errorCodeStr && "" != errorCodeStr){
						errorCode = errorCodeStr;
					}
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return errorCode;
	}

}
