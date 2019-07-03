package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.PhoneCheckDao;
import com.l9e.transaction.service.PhoneCheckService;

@Service("phoneCheckServiceImpl")
public class PhoneCheckServiceImpl implements PhoneCheckService {
	@Resource
	private PhoneCheckDao phoneCheckDao;
	
	public List<Map<String, String>> queryAccountInfo(Map<String, Object> querymap) {
		return phoneCheckDao.queryAccountInfo(querymap);
	}
	
	public boolean addAccountCheckInfo(Map<String, String> map) {
		boolean flag = false;
		int count = phoneCheckDao.updateRefundStatus(map); //修改退款表44-->45
		if(count > 0){
			int accountNum = phoneCheckDao.queryAccountNum(map);//查询账号表是否已经存在
			if(accountNum > 0){//已存在则修改
				System.out.println("已存在则不做处理"+map.get("account_username"));
			}else{
				phoneCheckDao.addAccountCheckInfo(map);//不存在的插入账号核验表格
				phoneCheckDao.addAccountCheckRefundInfo(map);//不存在的插入备份表
			}
			flag = true;
		}
		return flag;
	}

	public boolean updateAccountCheckInfo(Map<String, String> map) {
		boolean flag = false;
		int count = phoneCheckDao.updateRefundStatus(map); 
		if(count > 0){
			phoneCheckDao.updateAccountCheckInfo(map);
			flag = true;
		}
		return flag;
	}
	
	public List<Map<String, String>> getPhoneMsgList() {
		return phoneCheckDao.getPhoneMsgList();
	}

	public boolean updateRefundStatus(Map<String, String> map) {
		boolean flag = false;
		int count = phoneCheckDao.updateRefundStatus(map); //修改退款表
		if(count > 0){
			flag = true;
		}
		return flag;
	}

}
