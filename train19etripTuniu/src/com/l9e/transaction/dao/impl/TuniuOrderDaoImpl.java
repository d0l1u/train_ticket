package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.TuniuOrderDao;
import com.l9e.transaction.vo.CpSysLogVO;
import com.l9e.transaction.vo.OutimeOrderVO;
import com.l9e.transaction.vo.TicketEntrance;
import com.l9e.transaction.vo.TuniuOrder;
import com.l9e.transaction.vo.TuniuPassenger;

@Repository("tuniuOrderDao")
public class TuniuOrderDaoImpl extends BaseDao implements TuniuOrderDao {

	@Override
	public int selectOrderCount(String orderId) {
		return (Integer) getSqlMapClientTemplate().queryForObject("order.selectOrderCount", orderId);
	}

	@Override
	public void insertOrder(TuniuOrder order) {
		getSqlMapClientTemplate().insert("order.insertOrder", order);
	}

	@Override
	public void insertPassenger(TuniuPassenger passenger) {
		getSqlMapClientTemplate().insert("order.insertPassenger", passenger);
	}

	@Override
	public void insertStudent(TuniuPassenger passenger) {
		getSqlMapClientTemplate().insert("order.insertStudent", passenger);
	}

	@Override
	public TuniuOrder selectOneOrder(Map<String, Object> queryParam) {
		return (TuniuOrder) getSqlMapClientTemplate().queryForObject("order.selectOrder", queryParam);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TuniuPassenger> selectPassengers(Map<String, Object> queryParam) {
		return getSqlMapClientTemplate().queryForList("order.selectPassenger", queryParam);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TicketEntrance> selectTicketEntrances(Map<String, Object> queryParam) {
		return getSqlMapClientTemplate().queryForList("order.selectTicketEntrance", queryParam);
	}

	@Override
	public void updateOrder(TuniuOrder order) {
		logger.info("更新订单信息："+order.getOrderId()+",状态："+order.getOrderStatus()+",原因："+order.getOutFailReason());
		getSqlMapClientTemplate().update("order.updateOrder", order);
	}

	@Override
	public void updatePassenger(TuniuPassenger passenger) {
		getSqlMapClientTemplate().update("order.updatePassenger", passenger);
	}

	@Override
	public TuniuPassenger selectOnePassenger(Map<String, Object> queryParam) {
		return (TuniuPassenger) getSqlMapClientTemplate().queryForObject("order.selectPassenger", queryParam);
	}

	@Override
	public String selectOrderLog(Map<String, Object> map) {
		return (String)getSqlMapClientTemplate().queryForObject("order.selectOrderLog", map);
	}

	@Override
	public int checkOrderIsRepeat(Map<String, Object> param) {
		return (Integer) getSqlMapClientTemplate().queryForObject("order.checkOrderIsRepeat", param);
	}

	@Override
	public String selectAccountByOrder(String orderId) {
		return (String) getSqlMapClientTemplate().queryForObject("order.selectAccountByOrder", orderId);

	}

	@Override
	public void insertCpStudent(TuniuPassenger passenger) {
		getSqlMapClientTemplate().insert("order.insertCpStudent", passenger);

	}

	@Override
	public void inserOutimeOrderVO(OutimeOrderVO outTimeOrder) {
		// TODO Auto-generated method stub
		getSqlMapClientTemplate().insert("order.inserOutimeOrderVO", outTimeOrder);
		
	}

	@Override
	public String selectCpSysOrderStaus(String orderId) {
		// TODO Auto-generated method stub
		return (String) getSqlMapClientTemplate().queryForObject("order.selectCpSysOrderStaus", orderId);
	}

	@Override
	public void inserCpSysOrderLog(CpSysLogVO log) {
		// TODO Auto-generated method stub
		getSqlMapClientTemplate().insert("order.inserCpSysOrderLog", log);
	}

	@Override
	public void updateCpSysOrderStatus(Map<String, String> updateCpSysMap) {
		// TODO Auto-generated method stub
		getSqlMapClientTemplate().update("order.updateCpSysOrderStatus", updateCpSysMap);
	}

	@Override
	public void updateOutTimeOrder(OutimeOrderVO outTimeOrder) {
		// TODO Auto-generated method stub
		getSqlMapClientTemplate().update("order.updateOutTimeOrder", outTimeOrder);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> selectCpSysChangeOrderStatus(String orderId) {
		// TODO Auto-generated method stub
		return (Map<String, Object>) getSqlMapClientTemplate().queryForObject("order.selectCpSysChangeOrderStatus", orderId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> selectCpSysChangeOrderStatusByChangeId(
			Map<String, String> changeQueryMap) {
		// TODO Auto-generated method stub
		return  (Map<String, Object>) getSqlMapClientTemplate().queryForObject("order.selectCpSysChangeOrderStatusByChangeId", changeQueryMap);
	}

}
