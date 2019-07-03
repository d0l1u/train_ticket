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

	@Override
	public String querySysSettingByKey(String key) {
		return commonDao.querySysSettingByKey(key);
	}

	@Override
	public Worker queryRandomWorker() {
		return commonDao.queryRandomWorker();
	}

	@Override
	public List<Map<String, String>> queryWaitNotify() {
		return commonDao.queryWaitNotify();
	}

	@Override
	public void updateRobotStartNotify(Map<String, String> map) {
		commonDao.updateRobotStartNotify(map);
	}

	@Override
	public List<Map<String, String>> queryLockOrderList() {
		return commonDao.queryLockOrderList();
	}

	@Override
	public void addCpOrderHistory(Map<String, String> map) {
		commonDao.addCpOrderHistory(map);
	}

	@Override
	public void updateCpOrderStatus(Map<String, String> map) {
		commonDao.updateCpOrderStatus(map);
	}

	@Override
	public void updateCpOrderNotify(Map<String, String> map) {
		commonDao.updateCpOrderNotify(map);
	}

	@Override
	public List<Map<String, String>> queryLockAlterOrderList() {
		return commonDao.queryLockAlterOrderList();
	}

	@Override
	public void updateQunarRefundOrderStatus(Map<String, String> map) {
		commonDao.updateQunarRefundOrderStatus(map);
	}

	@Override
	public void update19eRefundOrderStatus(Map<String, String> map) {
		commonDao.update19eRefundOrderStatus(map);
	}

	@Override
	public List<String> queryPinganMessageList() {
		return commonDao.queryPinganMessageList();
	}

	@Override
	public List<Map<String,String>> queryAccountSeatList(Map<String, String> param) {
		return commonDao.queryAccountSeatList(param);
	}

	@Override
	public void updateAccountStatusFree(String acc_id) {
		commonDao.updateAccountStatusFree(acc_id);
	}

	@Override
	public int updateQunarSysNoticeStaus(Map<String, String> param) {
		return commonDao.updateQunarSysNoticeStaus(param);
	}

	@Override
	public List<Map<String, String>> queryExtMerchantList() {
		return commonDao.queryExtMerchantList();
	}

	@Override
	public List<String> queryLockBookOrder(Map<String, String> param) {
		return commonDao.queryLockBookOrder(param);
	}

	@Override
	public void updateCpOrderinfoNotify(Map<String, String> param) {
		commonDao.updateCpOrderinfoNotify(param);
	}

	@Override
	public void insertCpHistory(Map<String, String> param) {
		commonDao.insertCpHistory(param);
	}

	@Override
	public int updateElongSysNoticeStaus(Map<String, String> paramElong) {
		return commonDao.updateElongSysNoticeStaus(paramElong);
	}
	
	@Override
	public void addCpOrderRefundHistory(Map<String, String> param) {
		commonDao.addCpOrderRefundHistory(param);
	}

	@Override
	public List<Map<String, String>> queryInnerLockOrderList() {
		return commonDao.queryInnerLockOrderList();
	}

	@Override
	public void updateLockCpOrderinfoNotify(Map<String, String> map) {
		commonDao.updateLockCpOrderinfoNotify(map);
	}

	@Override
	public List<Map<String, String>> query19eEopLockOrderList() {
		return commonDao.query19eEopLockOrderList();
	}

	@Override
	public void updateEopLockOrder(Map<String, String> map) {
		commonDao.updateEopLockOrder(map);
	}

	@Override
	public Map<String, String> queryCpOrderList(Map<String, String> map) {
		return commonDao.queryCpOrderList(map);
	}

	@Override
	public List<Map<String, String>> query19eLockOrderList() {
		return commonDao.query19eLockOrderList();
	}

	@Override
	public List<Map<String, String>> queryElongLockOrderList() {
		return commonDao.queryElongLockOrderList();
	}

	@Override
	public List<Map<String, String>> queryExtLockOrderList() {
		return commonDao.queryExtLockOrderList();
	}

	@Override
	public List<Map<String, String>> queryQunarLockOrderList() {
		return commonDao.queryQunarLockOrderList();
	}

	@Override
	public List<Map<String, String>> queryChinaCitysInfo(
			Map<String, Integer> paramMap) {
		return commonDao.queryChinaCitysInfo(paramMap);
	}

	@Override
	public String queryAccountIdByOrderId(String orderId) {
		return commonDao.queryAccountIdByOrderId(orderId);
	}
}
