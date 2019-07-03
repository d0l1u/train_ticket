package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.NoticeDao;
import com.l9e.transaction.service.NoticeService;
import com.l9e.transaction.vo.AreaVo;
import com.l9e.transaction.vo.NoticeVo;
import com.l9e.transaction.vo.NoticeVo;


@Service("noticeService")
public class NoticeServiceImpl implements NoticeService{

	@Resource
	NoticeDao noticeDao;
	
	

	public List<Map<String, Object>> queryNoticeList(
			Map<String, Object> paramMap) {
		return noticeDao.queryNoticeList(paramMap);
	}

	public int queryNoticeListCount(Map<String, Object> paramMap) {
		return noticeDao.queryNoticeListCount(paramMap);
	}

	public Map<String, Object> queryNotice(String notice_id) {
		return noticeDao.queryNotice(notice_id);
	}

	public void updateNotice(NoticeVo  notice) {
		noticeDao.updateNotice(notice);
	}

	public void insertNotice(NoticeVo  notice) {
		noticeDao.insertNotice(notice);
	}

	public void deleteNotice(NoticeVo notice) {
		noticeDao.deleteNotice(notice);
	}

	public List<AreaVo> getArea(String cityid) {
		return noticeDao.getArea(cityid);
	}

	public List<AreaVo> getCity(String provinceid) {
		return noticeDao.getCity(provinceid);
	}

	public List<AreaVo> getProvince() {
		return noticeDao.getProvince();
	}


}
