package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.InnerNoticeDao;
import com.l9e.transaction.service.InnerNoticeService;
import com.l9e.transaction.vo.AreaVo;
import com.l9e.transaction.vo.NoticeVo;

	@Service("innerNoticeServiceImpl")
	public class InnerNoticeServiceImpl implements InnerNoticeService {
		
		@Resource
		InnerNoticeDao innerNoticeDao;
		public List<Map<String, Object>> queryNoticeList(
				Map<String, Object> paramMap) {
			return innerNoticeDao.queryNoticeList(paramMap);
		}

		public int queryNoticeListCount(Map<String, Object> paramMap) {
			return innerNoticeDao.queryNoticeListCount(paramMap);
		}

		public Map<String, Object> queryNotice(String notice_id) {
			return innerNoticeDao.queryNotice(notice_id);
		}

		public void updateNotice(NoticeVo  notice) {
			innerNoticeDao.updateNotice(notice);
		}

		public void insertNotice(NoticeVo  notice) {
			innerNoticeDao.insertNotice(notice);
		}

		public void deleteNotice(NoticeVo notice) {
			innerNoticeDao.deleteNotice(notice);
		}

		public List<AreaVo> getArea(String cityid) {
			return innerNoticeDao.getArea(cityid);
		}

		public List<AreaVo> getCity(String provinceid) {
			return innerNoticeDao.getCity(provinceid);
		}

		public List<AreaVo> getProvince() {
			return innerNoticeDao.getProvince();
		}

	}