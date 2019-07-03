package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.CommonDao;
import com.l9e.transaction.service.CommonService;
import com.l9e.transaction.vo.Worker;

@Service("commonService")
public class CommonServiceImpl implements CommonService {
	@Resource
	private CommonDao commonDao;

	
	public String querySysSettingByKey(String key) {
		return commonDao.querySysSettingByKey(key);
	}

	
	public Integer queryHeyanWorkNum(Integer workType) {
		return commonDao.queryHeyanWorkNum(workType);
	}
	
	
	public Worker queryRandomWorker(Map<String, Object> map) {
		return commonDao.queryRandomWorker(map);
	}

	
	public List<Map<String, String>> queryWaitNotify() {
		return commonDao.queryWaitNotify();
	}

	
	public void updateRobotStartNotify(Map<String, String> map) {
		commonDao.updateRobotStartNotify(map);
	}

	
	public List<Map<String, String>> queryLockOrderList() {
		return commonDao.queryLockOrderList();
	}

	
	public void addCpOrderHistory(Map<String, String> map) {
		commonDao.addCpOrderHistory(map);
	}

	
	public void updateCpOrderStatus(Map<String, String> map) {
		commonDao.updateCpOrderStatus(map);
	}

	
	public void updateCpOrderNotify(Map<String, String> map) {
		commonDao.updateCpOrderNotify(map);
	}

	
	public List<Map<String, String>> queryLockAlterOrderList() {
		return commonDao.queryLockAlterOrderList();
	}

	
	public void updateQunarRefundOrderStatus(Map<String, String> map) {
		commonDao.updateQunarRefundOrderStatus(map);
	}

	
	public void update19eRefundOrderStatus(Map<String, String> map) {
		commonDao.update19eRefundOrderStatus(map);
	}

	
	public List<String> queryPinganMessageList() {
		return commonDao.queryPinganMessageList();
	}

	
	public List<Map<String,String>> queryAccountSeatList(Map<String, String> param) {
		return commonDao.queryAccountSeatList(param);
	}

	
	public void updateAccountStatusFree(String acc_id) {
		commonDao.updateAccountStatusFree(acc_id);
	}

	
	public int updateQunarSysNoticeStaus(Map<String, String> param) {
		return commonDao.updateQunarSysNoticeStaus(param);
	}

	
	public List<Map<String, String>> queryExtMerchantList() {
		return commonDao.queryExtMerchantList();
	}

	
	public List<String> queryLockBookOrder(Map<String, String> param) {
		return commonDao.queryLockBookOrder(param);
	}

	
	public void updateCpOrderinfoNotify(Map<String, String> param) {
		commonDao.updateCpOrderinfoNotify(param);
	}

	
	public void insertCpHistory(Map<String, String> param) {
		commonDao.insertCpHistory(param);
	}

	
	public void updateElongRefundOrderStatus(Map<String, String> map) {
		commonDao.updateElongRefundOrderStatus(map);
	}

	
	public int updateElongSysNoticeStaus(Map<String, String> paramElong) {
		return commonDao.updateElongSysNoticeStaus(paramElong);
	}


	public void updateBalance(Map<String, String> map) {
		// TODO Auto-generated method stub
		 commonDao.updateBalance(map);
	}

}
