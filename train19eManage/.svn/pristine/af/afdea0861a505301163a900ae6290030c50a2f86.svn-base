package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.ExtNoticeDao;
import com.l9e.transaction.service.ExtNoticeService;
import com.l9e.transaction.vo.AreaVo;
import com.l9e.transaction.vo.NoticeVo;

	@Service("extNoticeServiceImpl")
	public class ExtNoticeServiceImpl implements ExtNoticeService {
		
		@Resource
		ExtNoticeDao extNoticeDao;
		public List<Map<String, Object>> queryNoticeList(
				Map<String, Object> paramMap) {
			return extNoticeDao.queryNoticeList(paramMap);
		}

		public int queryNoticeListCount(Map<String, Object> paramMap) {
			return extNoticeDao.queryNoticeListCount(paramMap);
		}

		public Map<String, Object> queryNotice(String notice_id) {
			return extNoticeDao.queryNotice(notice_id);
		}

		public void updateNotice(NoticeVo  notice) {
			extNoticeDao.updateNotice(notice);
		}

		public void insertNotice(NoticeVo  notice) {
			extNoticeDao.insertNotice(notice);
		}

		public void deleteNotice(NoticeVo notice) {
			extNoticeDao.deleteNotice(notice);
		}

		public List<AreaVo> getArea(String cityid) {
			return extNoticeDao.getArea(cityid);
		}

		public List<AreaVo> getCity(String provinceid) {
			return extNoticeDao.getCity(provinceid);
		}

		public List<AreaVo> getProvince() {
			return extNoticeDao.getProvince();
		}

		@Override
		public List<NoticeVo> queryNoticeChannelList(
				Map<String, Object> paramMap) {
			return extNoticeDao.queryNoticeChannelList(paramMap);
		}

	}