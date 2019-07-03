package com.l9e.transaction.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.AccountService;
import com.l9e.transaction.vo.Account;

//账号清理，非19e渠道的更改为19e，状态非33的改为999
@Component("accountHandleJob")
public class AccountHandleJob {
	private static final Logger logger = Logger.getLogger(AccountHandleJob.class);
	
	@Resource
	private AccountService accountService;
	
	public void accountHandle(){
		String logPre = "[账号清理]";
		logger.info(logPre+" start~~~");
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("acc_status", "33");
		paramMap.put("nochannel", "19e");
		paramMap.put("start_account_limit_num", 100);
		List<Account> accountList = accountService.queryAccount(paramMap);
		
		for(Account account : accountList){
			Map<String, Object> accMap = new HashMap<String, Object>();
			accMap.put("acc_username", account.getAccUsername());
			accMap.put("acc_password", account.getAccPassword());
			//accMap.put("acc_status", "33");
			List<Account> accList = accountService.queryAccount(accMap);
			if(accList.size()>1){
				logger.info(logPre+account.getAccUsername()+"/"+account.getAccPassword()+"有"+accList.size()+"条记录");
			}
			
			for(Account acc : accList){
				if(!"19e".equals(acc.getChannel())){
					Map<String, Object> updateMap = new HashMap<String, Object>();
					updateMap.put("acc_id", acc.getAccId());
					updateMap.put("channel", "19e");
					updateMap.put("opt_person", "handle_job");
					if(!"33".equals(acc.getAccStatus())){
						updateMap.put("acc_status", "999");
					}
					
					accountService.updateAccountInfo(updateMap);
				}
			}
		}
		
		logger.info(logPre+" end~~~");
	}
}
