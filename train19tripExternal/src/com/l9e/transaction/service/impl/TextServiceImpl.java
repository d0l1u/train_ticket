package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.TextDao;
import com.l9e.transaction.service.TextService;

@Service("textService")
public class TextServiceImpl implements TextService{
	private static final Logger logger = Logger.getLogger(OrderServiceImpl.class);
	@Resource
	private TextDao textDao;
	@Override
	public List<String> queryOutTicket() {
		// TODO Auto-generated method stub
		return textDao.queryOutTicket();
	}

	@Override
	public Map<String, Object> queryOrderInfo(String orderId) {
		// TODO Auto-generated method stub
		return textDao.queryOrderInfo(orderId);
	}

	@Override
	public int updateNoticeInfo(Map<String, Object> orderInfo) {
		// TODO Auto-generated method stub
		return textDao.updateNoticeInfo(orderInfo);
	}

	@Override
	public void updateOrderInfo(Map<String, Object> orderInfo) {
		// TODO Auto-generated method stub
		textDao.updateOrderInfo(orderInfo);
	}

	@Override
	public void updateRefundOrderInfo(Map<String, Object> refundInfo) {
		// TODO Auto-generated method stub
		textDao.updateRefundOrderInfo(refundInfo);
	}

	@Override
	public List<Map<String,Object>> queryRefundTicket() {
		// TODO Auto-generated method stub
		return textDao.queryRefundTicket();
	}

/*	@Override
	public Map<String, String> queryRefundOrderInfo(Map<String, String> param) {
		// TODO Auto-generated method stub
		return textDao.queryRefundOrderInfo(param);
	}*/

	@Override
	public Map<String, Object> queryCpInfo(String orderId) {
		// TODO Auto-generated method stub
		return textDao.queryCpInfo(orderId);
	}

	@Override
	public List<String> queryOutTicketCpId(String orderId) {
		// TODO Auto-generated method stub
		return textDao.queryOutTicketCpId(orderId);
	}

	@Override
	public void updateCpInfo(Map<String, Object> cpInfo) {
		textDao.updateCpInfo(cpInfo);
		
	}

	@Override
	public Map<String, Object> queryMerchantinfo(String channel) {
		// TODO Auto-generated method stub
		return textDao.queryMerchantinfo(channel);
	}

	@Override
	public List<String> queryYdToOutTicket() {
		// TODO Auto-generated method stub
		return textDao.queryYdToOutTicket();
	}

	@Override
	public void updateRefundNoticeInfo(Map<String, String> map) {
		// TODO Auto-generated method stub
		textDao.updateRefundNoticeInfo(map);
	}

	@Override
	public int selectCountNumFromNotice(Map<String, Object> orderInfo) {
		// TODO Auto-generated method stub
		return textDao.selectCountNumFromNotice(orderInfo);
	}

	@Override
	public List<String> queryPaySuccess() {
		return textDao.queryPaySuccess();
	}

	@Override
	public String selectSeatType(String orderId) {
		// TODO Auto-generated method stub
		return textDao.selectSeatType(orderId);
	}
	
}
