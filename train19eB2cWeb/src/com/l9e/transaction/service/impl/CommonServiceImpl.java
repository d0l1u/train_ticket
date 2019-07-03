package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.CommonDao;
import com.l9e.transaction.service.CommonService;
import com.l9e.transaction.vo.ProductVo;
import com.l9e.transaction.vo.SuitVo;

@Service("commonService")
public class CommonServiceImpl implements CommonService {
	
	@Resource
	private CommonDao commonDao;

	public List<ProductVo> queryProductInfoList(ProductVo product) {
		return commonDao.queryProductInfoList(product);
	}

	public void addOrderEopInfo(Map<String, String> eopInfo) {
		commonDao.addOrderEopInfo(eopInfo);
	}

	public Map<String, String> queryEopInfo(String asp_order_id) {
		return commonDao.queryEopInfo(asp_order_id);
	}

	public void updateOrderEopInfo(Map<String, String> eopInfo) {
		commonDao.updateOrderEop(eopInfo);
	}

	public void updateJmOrderEopInfo(Map<String, String> eopInfo) {
		commonDao.updateOrderEop(eopInfo);
		
	}

	public String queryOrderIdByEopId(String eop_order_id) {
		return commonDao.queryOrderIdByEopId(eop_order_id);
	}


	public String queryProvinceName(String provinceId) {
		return commonDao.queryProvinceName(provinceId);
	}

	public List<Map<String, String>> queryNoticeList(String provinceName) {
		return commonDao.queryNoticeList(provinceName);
	}

	public Map<String, String> queryNoticeInfo(String noticeId) {
		return commonDao.queryNoticeInfo(noticeId);
	}

	public Map<String, String> queryOldAreaInfo(String provinceId) {
		return commonDao.queryOldAreaInfo(provinceId);
	}

	public String queryInterfaceChannel() {
		return commonDao.queryInterfaceChannel();
	}

	public List<String> querySysInterfaceUrl() {
		return commonDao.querySysInterfaceUrl();
	}
	
	public List<String> querySysSettingValue(Map<String, String> paramMap) {
		return commonDao.querySysSettingValue(paramMap);
	}

	public List<Map<String, String>> queryNoticeAllList(String provinceName) {
		return commonDao.queryNoticeAllList(provinceName);
	}

	public String querySysSettingByKey(String key) {
		return commonDao.querySysSettingByKey(key);
	}

	//是否投诉
	public SuitVo querySuit(String agentId) {
		return commonDao.querySuit(agentId);
	}
	public void updateSuit(SuitVo suit){
		 commonDao.updateSuit(suit);
	}

	
	public String querySysStopTime() {
		return commonDao.querySysStopTime();
	}

	
	public List<ProductVo> queryProductInfoYuyueList(ProductVo product) {
		return commonDao.queryProductInfoYuyueList(product);
	}

	
	public List<Map<String, String>> queryExtNoticeList(String extChannel) {
		return commonDao.queryExtNoticeList(extChannel);
	}

	
	public Map<String, String> queryExtNoticeInfo(String noticeId) {
		return commonDao.queryExtNoticeInfo(noticeId);
	}

	
	public List<Map<String, String>> queryExtNoticeAllList(String extChannel) {
		return commonDao.queryExtNoticeAllList(extChannel);
	}

	
	public Map<String, String> querySeatMap(Map<String, String> seatMap) {
		return commonDao.querySeatMap(seatMap);
	}

	public List<String> queryOftenStation() {
		return commonDao.queryOftenStation();
	}

	public List<Map<String, String>> queryOftenStationInfo(String station) {
		return commonDao.queryOftenStationInfo(station);
	}

	public List<Map<String, String>> getProvince() {
		return commonDao.getProvince();
	}

	public List<Map<String, String>> getArea(String cityid) {
		return commonDao.getArea(cityid);
	}

	public List<Map<String, String>> getCity(String provinceid) {
		return commonDao.getCity(provinceid);
	}
}