package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.PassengerDao;
import com.l9e.transaction.vo.Passenger;

@Repository("passengerDao")
public class PassengerDaoImpl extends BaseDao implements PassengerDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Passenger> selectPassenger(Map<String, Object> params) {
		return getSqlMapClientTemplate().queryForList("passenger.selectPassenger", params);
	}

}
