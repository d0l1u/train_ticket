package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.ExtShijiDao;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.transaction.vo.OrderInfoBx;
import com.l9e.transaction.vo.OrderInfoCp;
import com.l9e.transaction.vo.OrderInfoPs;
import com.l9e.transaction.vo.RefundVo;
@Repository("extShijiDao")
public class ExtShijiDaoImpl extends BaseDao implements ExtShijiDao {

	public OrderInfo queryOrderInfo(String order_id) {
		return (OrderInfo) this.getSqlMapClientTemplate().queryForObject("extShiji.queryOrderInfo", order_id);
	}

	public OrderInfoPs queryOrderInfoPs(String order_id) {
		return (OrderInfoPs) this.getSqlMapClientTemplate().queryForObject("extShiji.queryOrderInfoPs", order_id);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryOrderDetailList(String order_id) {
		return this.getSqlMapClientTemplate().queryForList("extShiji.queryOrderDetailList", order_id);
	}
	
	@SuppressWarnings("unchecked")
	public List<OrderInfo> queryOrderList(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("extShiji.queryOrderList", paramMap);
	}

	public int queryOrderListCount(Map<String, Object> paramMap) {
		return this.getTotalRows("extShiji.queryOrderListCount", paramMap);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryRefundStreamList(String order_id) {
		return this.getSqlMapClientTemplate().queryForList("extShiji.queryRefundStreamList", order_id);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, String> querySpecTimeBeforeFrom(String order_id) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("extShiji.querySpecTimeBeforeFrom", order_id);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> queryAgentOrderNum(Map<String, Object> map) {
		return (Map<String, Object>) this.getSqlMapClientTemplate().queryForObject("extShiji.queryAgentOrderNum", map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> queryAgentRefundNum(Map<String, Object> map) {
		return (Map<String, Object>) this.getSqlMapClientTemplate().queryForObject("extShiji.queryAgentRefundNum", map);
	}

	@Override
	public String selectRefundPassengers(String cp_id) {
		return (String) this.getSqlMapClientTemplate().queryForObject("extShiji.selectRefundPassengers", cp_id);
	}

	@Override
	public List<OrderInfo> queryOrderRefundList(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("extShiji.queryOrderRefundList", paramMap);
	}

	@Override
	public List<OrderInfo> queryExtAccountOrderList(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("extShiji.queryExtAccountOrderList", paramMap);
	}

	@Override
	public int queryExtAccountOrderListCount(Map<String, Object> paramMap) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("extShiji.queryExtAccountOrderListCount", paramMap);
	}

	@Override
	public List<Map<String, String>> queryRefundList(String orderId) {
		return this.getSqlMapClientTemplate().queryForList("extShiji.queryRefundList", orderId);
	}

	@Override
	public List<OrderInfo> queryExtAccountOrderExcelList(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("extShiji.queryExtAccountOrderExcelList", paramMap);
	}

}
