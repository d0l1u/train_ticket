package com.l9e.transaction.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.RobTicketDao;
import com.l9e.transaction.dao.impl.RobTicketDaoImpl;
import com.l9e.transaction.service.RobTicketService;
import com.l9e.transaction.vo.RobTicketVo.RobTicket_CP;
import com.l9e.transaction.vo.RobTicketVo.RobTicket_History;
import com.l9e.transaction.vo.RobTicketVo.RobTicket_Notify;
import com.l9e.transaction.vo.RobTicketVo.RobTicket_OI;
/**
 * 19e后台-抢票service
 * @author yangwei01
 *
 */
@Service("robTicketService")
public class RobTicketServiceImpl implements RobTicketService {
	@Resource
	private RobTicketDao robTicketDao;

	@Override
	public List<Map<String, String>> queryRobList(Map<String, Object> paramMap) {
		 
		return robTicketDao.queryRobList(paramMap);
	}

	@Override
	public int queryRobListCount(Map<String, Object> paramMap) {
		 
		return robTicketDao.queryRobListCount(paramMap);
	}

	@Override
	public int queryRobListCountCp(Map<String, Object> paramMap) {
		 
		return robTicketDao.queryRobListCountCp(paramMap);
	}

	@Override
	public List<Map<String, String>> queryRobListCp(Map<String, Object> paramMap) {
		 
		return robTicketDao.queryRobListCp(paramMap);
	}

	@Override
	public void deleteCP(RobTicket_CP cp) {
		 
		robTicketDao.deleteCP(cp);
	}

	@Override
	public void deleteHistory(RobTicket_History history) {
		 
		robTicketDao.deleteHistory(history);
	}

	@Override
	public void deleteNotify(RobTicket_Notify notify) {
		 
		robTicketDao.deleteNotify(notify);
	}

	@Override
	public void deleteOrderInfo(RobTicket_OI oi) {
		 
		robTicketDao.deleteOrderInfo(oi);
	}

	@Override
	public void insertCP(RobTicket_CP cp) {
		 
		robTicketDao.insertCP(cp);
	}

	@Override
	public void insertHistory(RobTicket_History history) {
		 
		robTicketDao.insertHistory(history);
	}

	@Override
	public void insertNotify(RobTicket_Notify notify) {
		 
		robTicketDao.insertNotify(notify);
	}

	@Override
	public void insertOrderInfo(RobTicket_OI oi) {
		 
		robTicketDao.insertOrderInfo(oi);
	}

	@Override
	public List<Map<String, String>> queryCP(Map<String, Object> paramMap) {
		 
		return robTicketDao.queryCP(paramMap);
	}

	@Override
	public List<Map<String, String>> queryHistory(Map<String, Object> paramMap) {
		 
		return robTicketDao.queryHistory(paramMap);
	}

	@Override
	public List<Map<String, String>> queryNotify(Map<String, Object> paramMap) {
		 
		return robTicketDao.queryNotify(paramMap);
	}

	@Override
	public List<Map<String, String>> queryOrderInfo(Map<String, Object> paramMap) {
		 
		return robTicketDao.queryOrderInfo(paramMap);
	}

	@Override
	public void updateCP(RobTicket_CP cp) {
		 
		robTicketDao.updateCP(cp);
	}

	@Override
	public void updateHistory(RobTicket_History history) {
		 
		robTicketDao.updateHistory(history);
	}

	@Override
	public void updateNotify(RobTicket_Notify notify) {
		 
		robTicketDao.updateNotify(notify);
	}

	@Override
	public void updateOrderInfo(RobTicket_OI oi) {
		 
		robTicketDao.updateOrderInfo(oi);
	}

	@Override
	public void updateFrontBackCP_Refund(HashMap<String, String> map) {
		robTicketDao.updateFrontBackCP_Refund(map);
		
	}

	@Override
	public void insertJLHistory(Map<String, Object> map) {
		robTicketDao.insertJLHistory(map);
	}

	@Override
	public List<Map<String, String>> queryRobListForExcel(Map<String, Object> map) {
		return robTicketDao.queryRobListForExcel(map);
	}

}
