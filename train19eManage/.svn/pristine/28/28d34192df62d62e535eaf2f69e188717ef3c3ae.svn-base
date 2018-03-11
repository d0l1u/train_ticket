package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.AppNoticeDao;
import com.l9e.transaction.service.AppNoticeService;
import com.l9e.transaction.vo.AreaVo;
import com.l9e.transaction.vo.AppNoticeVo;

@Service("appNoticeServiceImpl")
public class AppNoticeServiceImpl implements AppNoticeService {
	@Resource
	AppNoticeDao appNoticeDao;

	public List<Map<String, Object>> queryNoticeList(
			Map<String, Object> paramMap) {
		return appNoticeDao.queryNoticeList(paramMap);
	}

	public int queryNoticeListCount(Map<String, Object> paramMap) {
		return appNoticeDao.queryNoticeListCount(paramMap);
	}

	public Map<String, Object> queryNotice(String notice_id) {
		return appNoticeDao.queryNotice(notice_id);
	}

	public void updateNotice(AppNoticeVo notice) {
		appNoticeDao.updateNotice(notice);
	}

	public void insertNotice(AppNoticeVo notice) {
		appNoticeDao.insertNotice(notice);
	}

	public void deleteNotice(AppNoticeVo notice) {
		appNoticeDao.deleteNotice(notice);
	}

	public List<AreaVo> getArea(String cityid) {
		return appNoticeDao.getArea(cityid);
	}

	public List<AreaVo> getCity(String provinceid) {
		return appNoticeDao.getCity(provinceid);
	}

	public List<AreaVo> getProvince() {
		return appNoticeDao.getProvince();
	}

	@Override
	public void addNoticeUser(Map<String, String> paramMap) {
		appNoticeDao.addNoticeUser(paramMap);
	}

	@Override
	public String queryNoticeId(AppNoticeVo notice) {
		return appNoticeDao.queryNoticeId(notice);
	}

	@Override
	public List<String> queryUserPhone(String noticeId) {
		return appNoticeDao.queryUserPhone(noticeId);
	}
}
