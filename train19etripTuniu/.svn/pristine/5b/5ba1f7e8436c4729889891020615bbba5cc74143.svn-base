package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.CpSysLogVO;
import com.l9e.transaction.vo.OutimeOrderVO;
import com.l9e.transaction.vo.TicketEntrance;
import com.l9e.transaction.vo.TuniuOrder;
import com.l9e.transaction.vo.TuniuPassenger;

/**
 * 途牛订单持久接口
 * @author licheng
 *
 */
public interface TuniuOrderDao {

	/**
	 * 根据订单id查询数量
	 * @param orderId
	 * @return
	 */
	int selectOrderCount(String orderId);
	/**
	 * 查询一个订单
	 * @param queryParam
	 * @return
	 */
	TuniuOrder selectOneOrder(Map<String, Object> queryParam);
	/**
	 * 查询乘客
	 * @param queryParam
	 * @return
	 */
	List<TuniuPassenger> selectPassengers(Map<String, Object> queryParam);
	/**
	 * 查询一个乘客车票信息
	 * @param queryParam
	 * @return
	 */
	TuniuPassenger selectOnePassenger(Map<String, Object> queryParam);
	/**
	 * 途牛订单入库
	 * @param order
	 */
	void insertOrder(TuniuOrder order);
	/**
	 * 途牛乘客信息入库
	 * @param passenger
	 */
	void insertPassenger(TuniuPassenger passenger);
	/**
	 * 途牛学生信息入库
	 * @param passenger
	 */
	void insertStudent(TuniuPassenger passenger);
	/**
	 * 途牛学生信息入库
	 * @param passenger
	 */
	void insertCpStudent(TuniuPassenger passenger);
	/**
	 * 更新途牛订单
	 * @param order
	 */
	void updateOrder(TuniuOrder order);
	/**
	 * 更新乘客车票信息
	 * @param passenger
	 */
	void updatePassenger(TuniuPassenger passenger);
	/**
	 * 查询订单的操作日志，，查询表cp_orderinfo_history
	 * @param orderId
	 * @return
	 */
	String selectOrderLog(Map<String, Object> map);
	/**
	 * 检查订单是否是重复订单
	 * @param param
	 * @return
	 */
	int checkOrderIsRepeat(Map<String, Object> param);
	/**
	 * 根据订单号获取account_id
	 * @param orderId
	 * @return
	 */
	String selectAccountByOrder(String orderId);
	
	

	/**
	 * 根据订单号，查询检票口信息
	 * @param queryParam
	 * @return
	 */
	List<TicketEntrance> selectTicketEntrances(Map<String, Object> queryParam);
	
	/**
	 * @param outTimeOrder
	 * 超时订单存库
	 */
	void inserOutimeOrderVO(OutimeOrderVO outTimeOrder);
	
	
	/**
	 * @param orderId
	 * @return
	 * 查询出票系统的订单状态
	 */
	String selectCpSysOrderStaus(String orderId);
	
	/**
	 * @param log
	 * 向出票系统插入日志
	 */
	void inserCpSysOrderLog(CpSysLogVO log);
	
	/**
	 * @param updateCpSysMap
	 * 更新出票系统的订单状态
	 */
	void updateCpSysOrderStatus(Map<String, String> updateCpSysMap);
	
	/**
	 * @param outTimeOrder
	 * 更新超时订单的处理状态
	 */
	void updateOutTimeOrder(OutimeOrderVO outTimeOrder);
	
	
	/**
	 * @param orderId
	 * @return
	 * 查询改签订单的状态
	 */
	Map<String,Object> selectCpSysChangeOrderStatus(String orderId);
	
	
	/**
	 * @param changeQueryMap
	 * @return
	 * 查询改签订单的状态,改签流水做限制
	 */

	Map<String, Object> selectCpSysChangeOrderStatusByChangeId(
			Map<String, String> changeQueryMap);
	
	
}