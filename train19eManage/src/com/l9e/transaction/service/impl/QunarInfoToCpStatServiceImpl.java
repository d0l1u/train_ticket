package com.l9e.transaction.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.QunarInfoToCpStatDao;
import com.l9e.transaction.service.QunarInfoToCpStatService;
@Service("qunarInfoToCpStatService")
public class QunarInfoToCpStatServiceImpl implements QunarInfoToCpStatService {

	@Resource
	private QunarInfoToCpStatDao qunarInfoToCpStatDao;
	
	 //插入统计完成的信息
	public void addOrderInfoToCpStat(Map<String, Object> map) {
		// TODO Auto-generated method stub
		qunarInfoToCpStatDao.addOrderInfoToCpStat(map);
	}

	//查询前一天总条数
	public int queryPre_day_order_count() {
		// TODO Auto-generated method stub
		return qunarInfoToCpStatDao.queryPre_day_order_count();
	}
	
	//查询前一天订单失败的条数
	public int queryPre_day_out_ticket_defeated() {
		// TODO Auto-generated method stub
		return qunarInfoToCpStatDao.queryPre_day_out_ticket_defeated();
	}

	//查询前一天订单成功的条数
	public int queryPre_day_out_ticket_succeed() {
		// TODO Auto-generated method stub
		return qunarInfoToCpStatDao.queryPre_day_out_ticket_succeed();
	}

	//查询前一天订单失败的总价钱
	public double queryPre_defeated_money() {
		// TODO Auto-generated method stub
		return qunarInfoToCpStatDao.queryPre_defeated_money();
	}

	//统计下单的个数
	public int queryPre_preparative_count() {
		// TODO Auto-generated method stub
		return qunarInfoToCpStatDao.queryPre_preparative_count();
	}

	//统计同意退款的个数
	public int queryPre_refund_count() {
		// TODO Auto-generated method stub
		return qunarInfoToCpStatDao.queryPre_refund_count();
	}

	//查询前一天订单成功的总价钱
	public double queryPre_succeed_money() {
		// TODO Auto-generated method stub
		return qunarInfoToCpStatDao.queryPre_succeed_money();
	}

	//查询前一天的票数
	public int queryPre_ticket_count() {
		// TODO Auto-generated method stub
		return qunarInfoToCpStatDao.queryPre_ticket_count();
	}

	public int query_Qunar_StatInfo() {
		// TODO Auto-generated method stub
		return qunarInfoToCpStatDao.query_Qunar_StatInfo();
	}

}
