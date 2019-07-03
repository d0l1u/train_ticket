package com.l9e.transaction.job;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.AccountService;
import com.l9e.transaction.service.CommonService;
import com.l9e.transaction.vo.Account;
import com.l9e.util.DateUtil;

/**
 * 启用停用账号job
 * 
 * @author lihaichao
 * 
 */
@Component("startUseAccountJob")
public class StartUseAccountJob {

	private static final Logger logger = Logger
			.getLogger(StartUseAccountJob.class);
	private Integer limit_num = 10;
	@Resource
	private AccountService accountService;
	
	@Resource
	private CommonService commonService;

	public void startUseAccount() {
		logger.info("[重置账号预订次数]开始");
		logger.info("start set all account book_num 0");
		Map<String, Object> bookNumMap = new HashMap<String, Object>();
		accountService.updateBookNum(bookNumMap); 
		logger.info("finish setting all account book_num 0");
		logger.info("start set book_num 0 automatically");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("acc_status", Account.ACC_STATUS_SUSPENDED);
		map.put("stop_reason", Account.STOP_REASON_SUSPENDED);
		map.put("start_account_limit_num", limit_num);
		List<Account> accountList = accountService.queryAccount(map);
		while (accountList != null && accountList.size() != 0) {
			for (Account account : accountList) {
				account.setAccStatus(Account.ACC_STATUS_FREE);
				account.setBookNum(0);
				logger.info("[重置账号预订次数]账号为" + account.getAccUsername());
				if (account == null) {
					logger.error("【重置账号预订次数】账号为null，不予更新！");
				}
				if (account.getAccId() == null
						|| account.getAccId().length() <= 0) {
					logger.error("[重置账号预订次数]账号" + account.getAccUsername()
							+ "的acc_id为空，不予更新！");
					continue;
				}
				int updateLine = accountService.updateAccount(account);
				logger.info("[重置账号预订次数]更新账号" + account.getAccUsername()
						+ "影响的数据库数据行有" + updateLine + "行");
			}
			logger.info("finish seting " + accountList.size() + " book_num 0 automatically");
			accountList = accountService.queryAccount(map);
		}
		logger.info("finish seting book_num 0 automatically");
		
	}
	
	public void startUpdateAccountPriority() {
		logger.info("更新帐号未下单天数");
		int index = 0;
		while(true){
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("before", index);
			paramMap.put("after", 1000);
			List<Map<String,String>> list = accountService.queryAccoutPriority(paramMap);
			
			if(null==list || list.size()==0){
				break;
			}
			for(Map<String,String> map : list){
				String option_time = map.get("option_time");
				String contact_num = map.get("contact_num");
				
				if(null == option_time || "0".equals(contact_num)){
					logger.info("new add account default priority is 10");
					map.put("priority", "10");
					accountService.updateAccountPriority(map);
				}else{
					if(!"0".equals(contact_num) && null != option_time){
						long priority = DateUtil.dayDiff(DateUtil.dateToString(new Date(), DateUtil.DATE_FMT1),DateUtil.stringToString(option_time,DateUtil.DATE_FMT1));
						map.put("priority", String.valueOf(priority));
						logger.info(map.get("acc_username")+" update account the priority is "+priority);
						accountService.updateAccountPriority(map);
					}
				}
			}
			index+=1000;
		}
		
	}
	
	public void startUpdateAccountStatus() {
		int index = 0;
		String value = commonService.querySysSettingByKey("robot_no_order_day");
		while(true){
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("before", index);
			paramMap.put("after", 1000);
			paramMap.put("priority", value);
			List<Map<String,String>> list = accountService.queryAccoutWaitList(paramMap);
			
			if(null==list || list.size()==0){
				break;
			}
			for(Map<String,String> map : list){
				map.put("acc_status", "33");
				accountService.updateAccountStatus(map);
				logger.info("重置符合条件的帐号空闲状态"+map.get("acc_username"));
			}
			index+=1000;
		}
		
	}

}
