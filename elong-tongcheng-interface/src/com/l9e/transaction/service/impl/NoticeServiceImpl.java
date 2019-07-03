package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.NoticeDao;
import com.l9e.transaction.service.NoticeService;
import com.l9e.transaction.vo.DBNoticeVo;
import com.l9e.transaction.vo.DBRemedyNoticeVo;

@Service("noticeService")
public class NoticeServiceImpl implements NoticeService{
	
	@Resource
	private NoticeDao noticeDao;
	
	
	@Override
	public void insertNotice(DBNoticeVo notice) {
		noticeDao.insertNotice(notice);
	}

	@Override
	public List<DBNoticeVo> selectWaitNoticeList(Map<String, String> param) {
		return noticeDao.selectWaitNoticeList(param);
	}

	@Override
	public List<DBNoticeVo> selectOrderResultList(Map<String, String> param) {
		return noticeDao.selectOrderResultList(param);
	}

	@Override
	public List<DBNoticeVo> selectBookResultList(Map<String, String> param) {
		return noticeDao.selectBookResultList(param);
	}

	@Override
	public DBNoticeVo queryNoticeInfoById(String orderId) {
		return noticeDao.queryNoticeInfoById(orderId);
	}

	@Override
	public List<DBRemedyNoticeVo> selectOrderRemedyList() {
		return noticeDao.selectOrderRemedyList();
	}

	@Override
	public void updateNotice(DBNoticeVo notice) {
		noticeDao.updateNotice(notice);		
	}

	@Override
	public int updateStartWaitNoticeList(DBNoticeVo vo) {
		return noticeDao.updateStartWaitNoticeList(vo);
	}

	@Override
	public int updateStartOrderResultNotice(DBNoticeVo vo) {
		return noticeDao.updateStartOrderResultNotice(vo);
	}

	@Override
	public int updateStartBookResultNotice(DBNoticeVo vo) {
		return noticeDao.updateStartBookResultNotice(vo);
	}

	@Override
	public List<DBNoticeVo> selectWaitNoticeListsx(Map<String, String> param) {
		return noticeDao.selectWaitNoticeListsx(param);
	}

	@Override
	public String selectBookNoticeStatus(String order_id) {
		return noticeDao.selectBookNoticeStatus(order_id);
	}

	@Override
	public void insertRemedyNotice(String order_id) {
		 noticeDao.insertRemedyNotice(order_id);
	}

}
