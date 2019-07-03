package com.l9e.transaction.service;

import java.util.List;

import com.l9e.transaction.vo.Passenger;

/**
 * 乘客信息业务接口
 * @author licheng
 *
 */
public interface PassengerService {

	/**
	 * 查询订单中的乘客信息
	 * @param orderId
	 * @return
	 */
	List<Passenger> findPassengerByOrder(String orderId);
}
