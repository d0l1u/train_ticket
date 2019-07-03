package com.l9e.transaction.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.AccountDao;
import com.l9e.transaction.service.AccountService;
import com.l9e.transaction.vo.Account;
import com.l9e.transaction.vo.AccountFilter;
import com.l9e.transaction.vo.PassWhiteListVo;
import com.l9e.transaction.vo.Passenger;
import com.l9e.util.ConfigUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.JsonlibUtil;

@Service("accountService")
public class AccountServiceImpl implements AccountService {

	private static final Logger logger = Logger
			.getLogger(AccountServiceImpl.class);

	@Resource
	private AccountDao accountDao;

	/**
	 * 获取核验用的账号
	 */
	
	public Account queryAccountForVerify(Map<String, Object> paramMap) {
		// List<String> acc_list = accountDao.queryAccountIdForVerify(paramMap);
		// Account account = null;
		// for(String acc_id : acc_list){
		// paramMap.put("acc_id", acc_id);
		// logger.info("获取帐号:"+acc_id);
		// account = accountDao.queryAccountForVerify(paramMap);
		// if(null == account){//获取账号成功
		// logger.info("获取帐号失败:"+acc_id);
		// continue;
		// }else{
		// logger.info("获取帐号成功:"+account.getAccId()+account.getAccUsername());
		// break;
		// }
		// }

		// if(account!=null){//获取账号成功
		// Map<String, String> beginMap = new HashMap<String, String>();
		// beginMap.put("acc_id", account.getAccId());
		// beginMap.put("acc_status", Account.VERIFY);
		// beginMap.put("old_status", Account.FREE);
		// int count = accountDao.udpateAccountStatusByAccId(beginMap);
		// if(count == 0){//更新账号失败
		// logger.error("更新账号开始核验失败，accUsername="+account.getAccUsername());
		// return null;
		// }
		// }
		Account account = accountDao.queryAccountForVerify(paramMap);
		if (null == account) {
			logger.info("获取帐号失败");
			account = accountDao.queryAccountForVerify(paramMap);
		}
		if (null != account) {
			logger.info("获取帐号成功:" + account.getAccId()
					+ account.getAccUsername());
		}
		return account;
	}

	/**
	 * 查询账号是否在过滤表中存在
	 */
	
	public boolean queryAccoutsDBExist(List<Passenger> userList) {
		Set<String> container = new HashSet<String>();
		String element = null;
		for (Passenger p : userList) {
			element = p.getUser_name() + "/" + p.getCert_no();
			
			
			if (!container.contains(element)) {
				container.add(element);
				int count =accountDao.queryAccoutIsNotInFilter(p);
				if(count<=0){
					logger.info("db verify count=" + count +",Cert_no"+p.getCert_no()+" false");
					return false;
				}else{
					logger.info("db verify count=" + count +",Cert_no"+p.getCert_no()+" true");
				}
				
			}
		}
		
		return true;
		/*int count = accountDao.queryAccoutExistInFilter(userList);

		logger.info("db verify count=" + count + ":" + container.size());
		if (count == container.size()) {
			return true;
		} else {
			return false;
		}*/

	}

	
	public int updateAccoutFree(Account account) {
		Map<String, String> endMap = new HashMap<String, String>();
		endMap.put("acc_id", account.getAccId());
		endMap.put("acc_status", Account.FREE);
		//endMap.put("old_status", Account.VERIFY);
		int count = accountDao.udpateAccountStatusByAccId(endMap);
		return count;
	}

	
	public void addUserToAccountFilter(List<AccountFilter> afs) {

		int count = 0;
		for (AccountFilter af : afs) {
			count = accountDao.queryAccountFilterCountById(af.getIds_card());
			if (count == 0) {// 不存在则保存到数据库中
				accountDao.addUserToAccountFilter(af);
				logger.info("passenger :" + af.getReal_name() + "("
						+ af.getIds_card() + ") save into DB!");
			} else {
				logger.info("passenger :" + af.getReal_name() + "("
						+ af.getIds_card() + ") is already in DB!");
			}
		}
	}

	
	public List<Account> queryAliveAccouts(Map<String, Object> map) {
		List<Account> accounts = accountDao.queryAliveAccouts(map);
		if (accounts != null && accounts.size() > 0) {
			accountDao.updateAccoutLivetimeBatch(accounts);
		}
		return accounts;
	}

	
	public int queryAliveAccoutsCount(String channel) {
		return accountDao.queryAliveAccoutsCount(channel);
	}

	
	public void updateSupplyAccountNum(Map<String, Object> paramMap) {
		List<Account> accounts = accountDao.querySupplyAccouts(paramMap);
		if (accounts != null && accounts.size() > 0) {
			int count = accountDao.updateSupplyAccoutsAlive(accounts);
			logger.info("渠道:" + paramMap.get("channel") + "成功分配待唤醒" + count
					+ "个账号！");
		}
	}

	
	public int updateLimitAccountStop(Account account) {
		return accountDao.updateLimitAccountStop(account);
	}

	
	public void updateWorkerinfoNum() {
		accountDao.updateWorkerinfoNum();
		logger.info("成功清空机器人处理订单数量");
	}

	
	public String queryOrderStatus(String order_id) {
		return accountDao.queryOrderStatus(order_id);
	}

	
	public void updateCPOrderStatusAndMoney(Map<String, Object> paramMap) {
		accountDao.updateCPOrderStatusAndMoney(paramMap);
	}

	
	public void updateAccountActive(Map<String, Object> map) {
		accountDao.updateAccountActive(map);
	}

	
	public Map<String, String> queryAccountByName(Map<String, Object> map) {
		return accountDao.queryAccountByName(map);
	}

	
	public int queryAccoutsCount(Map<String, Object> map) {
		return accountDao.queryAccoutsCount(map);
	}

	
	public List<Account> queryAccount(Map<String, Object> map) {
		List<Account> accountList = accountDao.queryAccount(map);
		for (Account account : accountList) {
			if (account.getOldPass() == null
					|| account.getOldPass().length() == 0) {
				account.setOldPass(account.getAccPassword());
			}
			account.setModifyStatus("01"); // 将账号更改状态变为准备更改
			if (account.getAccId() == null || account.getAccId().length() > 0) {
				updateModifyStatus(account);
			}
		}
		return accountList;
	}

	public void updateModifyStatus(Account account) {
		accountDao.updateModifyStatus(account);
	}

	
	public void addUserToPassWhiteList(List<PassWhiteListVo> passWhiteListVoList) {
		int count = 0;
		for (PassWhiteListVo af : passWhiteListVoList) {
			Map queryMap = new HashMap();
			queryMap.put("cert_no", af.getCert_no());
			queryMap.put("acc_id", af.getAcc_id());
			logger.info(queryMap.toString());
			count = accountDao.queryAccountPassWhiteListCount(queryMap);
			if (count == 0) {// 不存在则保存到数据库中
				af.setAcc_status("2");
				af.setContact_status("0");
				accountDao.addUserToPassWhiteList(af);
				logger.info("passenger :" + af.getContact_name() + "("
						+ af.getCert_no() + ") save into DB!");
			} else {
				logger.info("passenger :" + af.getContact_name() + "("
						+ af.getCert_no() + ") is already in DB!");
			}
		}
		
	}

	/**
	 * 按账号id获取账号
	 * @param id
	 * add by wangsf
	 */
	@Override
	public Account getAccountById(Integer id) {
		Account account = null;

		try {
			String result = HttpUtil.sendByPost(ConfigUtil
					.getProperty("getOrderAccount"), "id=" + id, "utf-8");
			logger.info("get account by id , account result : " + result);
			if (!StringUtils.isEmpty(result)) {
				JSONObject resultJsonObject = JSONObject.fromObject(result);
				if (resultJsonObject.has("success")
						&& resultJsonObject.getBoolean("success")) {
					account = JsonlibUtil.json2Bean(resultJsonObject
							.getJSONObject("data").toString(), Account.class);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return account;
	}

	/**
	 * 白名单表匹配到历史账号的ID和次数  
	 * @param passportNo 乘客证件号码串
	 * @ add by wangsf
	 */
	@Override
	public Account queryAccountMappingNum(String passportNo) {
		Account account = null;
		logger.info("请求接口的身份证字符串"+passportNo);
		try {
			String result = HttpUtil.sendByPost(ConfigUtil
					.getProperty("getAccountMappingNum"), "passportNo="
					+ passportNo, "utf-8");

			logger.info("passportNo filter account, account result : "+ result);
			if (!StringUtils.isEmpty(result)) {
				JSONObject resultJsonObject = JSONObject.fromObject(result);
				if (resultJsonObject.has("success")
						&& resultJsonObject.getBoolean("success")) {
					account = JsonlibUtil.json2Bean(resultJsonObject
							.getJSONObject("data").toString(), Account.class);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return account;
	}

	@Override
	public Map<String, String> queryCtripCard() {
		Map<String, String> map = accountDao.queryCtripCard();
		
		if(map==null){
			return null;
		}else{
			map.put("new_ctrip_status", "11");//将该礼品卡更新为11使用中
			accountDao.updateCardInfo(map);
			
			return map;
		}
		
	}

	@Override
	public void updateCardInfo(Map<String, String> map) {
		accountDao.updateCardInfo(map);
	}

	@Override
	public void addCtripAccount(Map<String, String> insertMap) {
		accountDao.addCtripAccount(insertMap);
	}

	@Override
	public String queryCtripAccDegree(String banlance) {
		return accountDao.queryCtripAccDegree(banlance);
	}

	@Override
	public List<Map<String, String>> queryCtripAccount(
			Map<String, String> paramMap) {
		return accountDao.queryCtripAccount(paramMap);
	}
	
	
	@Override
	public int modifyCtripAccountInfo(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return accountDao.updateCtripAccountInfo(paramMap);
	}

	@Override
	public int modifyCtripCardInfo(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return accountDao.updateCtripCardInfo(paramMap);
	}

	@Override
	public Map<String, Object> queryRandomOneCtripAcc(
			Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return accountDao.selectRandomOneCtripAcc(paramMap);
	}

	@Override
	public Map<String, Object> queryRandomOneCtripCard(
			Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return accountDao.selectRandomOneCtripCard(paramMap);
	}

	@Override
	public Map<String, String> queryRegEmail() {
		Map<String, String> map = accountDao.queryRegEmail();
		if(map==null){
			return null;
		}else{
			map.put("new_email_status", "1");//将该邮箱更新为1取用中
			accountDao.updateRegEmail(map);
			
			return map;
		}
	}

	@Override
	public Map<String, String> queryModifyEmail() {
		return accountDao.queryModifyEmail(); 
	}
	@Override
	public void updateModifyEmail(Map<String, Object> updateMailMap) {
		accountDao.updateModifyEmail(updateMailMap);
	}

	@Override
	public void updateRegEmail(Map<String, String> updateMailMap) {
		accountDao.updateRegEmail(updateMailMap);
	}

	
	/**
	 * 从队列中按渠道获取账号
	 * 
	 * @param channel
	 * @return
	 */
	@Override
	public Account getChannelAccount(String channel,Integer passengerSize) {
		// TODO Auto-generated method stub
		Account account = null;

		int count = 0;
		do {
			try {
				String result = HttpUtil.sendByPost(ConfigUtil.getProperty("getChannelAccount"),"channel=" + channel+"&passengerSize=" + passengerSize, "utf-8");
				logger.info("get account by channel, account result : "+ result);
				if (!StringUtils.isEmpty(result)) {
					JSONObject resultJsonObject = JSONObject.fromObject(result);
					if (resultJsonObject.has("success")
							&& resultJsonObject.getBoolean("success")) {
						account = JsonlibUtil.json2Bean(resultJsonObject.getJSONObject("data").toString(),Account.class);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			count++;
		} while (account == null && count < 5);
		return account;
	}

	@Override
	public List<Map<String, String>> queryCtripAccInfo() {
		return accountDao.queryCtripAccInfo();
	}
	
	@Override
	public Map<String, Object> queryCtripAccInfoByBack() {
		return accountDao.queryCtripAccInfoByBack();
	}

	@Override
	public void updateAccountById(Map<String, Object> map) {
		accountDao.updateAccountById(map);
	}

	@Override
	public void updateAccountInfoById(Map<String, String> map) {
		accountDao.updateAccountInfoById(map);
		
	}

	@Override
	public List<Map<String, Object>> queryCtripMailInfo(Map<String, Object> map) {
		return accountDao.queryCtripMailInfo(map);
	}

}
