package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.Passenger;

/**
 * 乘客-车票信息持久
 * @author licheng
 *
 */
public interface PassengerDao {

	/**
	 * 查询乘客-车票信息
	 * @param params
	 * @return
	 */
	List<Passenger> selectPassenger(Map<String, Object> params);
}
