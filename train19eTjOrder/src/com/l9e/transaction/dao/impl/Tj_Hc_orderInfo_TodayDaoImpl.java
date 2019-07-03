package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.Tj_Hc_orderInfo_TodayDao;
@Repository("tj_Hc_orderInfo_TodayDao")
public class Tj_Hc_orderInfo_TodayDaoImpl extends BaseDao implements Tj_Hc_orderInfo_TodayDao {

	public int queryPre_day_out_ticket_succeed(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.queryPre_day_out_ticket_succeed",paramMap);
	}

	public int queryPre_day_out_ticket_defeated(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.queryPre_day_out_ticket_defeated",paramMap);
	}

	public int queryPre_day_order_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.queryPre_day_order_count",paramMap);
	}

	public int queryPre_preparative_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.queryPre_preparative_count",paramMap);
	}

	public int queryPre_pay_defeated(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.queryPre_pay_defeated",paramMap);
	}

	public int queryPre_over_time(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.queryPre_over_time",paramMap);
	}
	
	public int queryPre_bx_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.queryPre_bx_count",paramMap);
	}

	public double queryPre_bx_countMoney10(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.queryPre_bx_countMoney10",paramMap);
	}
	
	public double queryPre_bx_countMoney20(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.queryPre_bx_countMoney20",paramMap);
	}
	
	public double queryPre_defeated_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.queryPre_defeated_money",paramMap);
	}

	public int queryPre_refund_count(Map<String, Object> paramMap) {
		 return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.queryPre_refund_count",paramMap);
	}

	public double queryPre_succeed_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.queryPre_succeed_money",paramMap);
	}

	public int queryPre_ticket_count(Map<String, Object> paramMap) {
		 return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.queryPre_ticket_count",paramMap);
	}

	public double queryPre_change_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.queryPre_change_money",paramMap);
	}
	
	public double queryPre_refund_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.queryPre_refund_money",paramMap);
	}


	public void addToTj_Hc_orderInfo(Map<String, Object> map) {
		this.getSqlMapClientTemplate().insert("tj_Hc_orderInfo_today.addToTj_Hc_orderInfo",map);
	}

	public int queryTable_Count() {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.queryTable_Count");
	}

	@SuppressWarnings("unchecked")
	public List<String> queryDate_List() {
		return this.getSqlMapClientTemplate().queryForList("tj_Hc_orderInfo_today.queryDate_List");
	}
	

	public int queryActiveTotal(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.queryActiveTotal",paramMap);
	}

	public int queryVip_lose(Map<String, Object> paramMap) {  
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.queryVip_lose",paramMap);
	}
	public int queryVip_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.queryVip_count",paramMap);
	}
	public double queryPay_time(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.queryPay_time",paramMap);
	}
	//以下为去哪儿的统计
	public int qunar_query_day_order_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.qunar_query_day_order_count",paramMap);
	}

	public int qunar_query_day_out_ticket_defeated(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.qunar_query_day_out_ticket_defeated",paramMap);
	}

	public int qunar_query_day_out_ticket_succeed(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.qunar_query_day_out_ticket_succeed",paramMap);
	}

	public double qunar_query_defeated_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.qunar_query_defeated_money",paramMap);
	}

	public int qunar_query_refund_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.qunar_query_refund_count",paramMap);
	}

	public double qunar_query_succeed_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.qunar_query_succeed_money",paramMap);
	}

	public int qunar_query_ticket_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.qunar_query_ticket_count",paramMap);
	}

	public double queryOut_ticket_XL(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.queryOut_ticket_XL",paramMap);
	}
	
	public int qunar_over_time(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.qunar_over_time",paramMap);
	}

	//19pay统计
	public double payOut_ticket_XL(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.payOut_ticket_XL",paramMap);
	}

	public int pay_query_day_order_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.pay_query_day_order_count",paramMap);
	}

	public int pay_query_day_out_ticket_defeated(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.pay_query_day_out_ticket_defeated",paramMap);
	}

	public int pay_query_day_out_ticket_succeed(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.pay_query_day_out_ticket_succeed",paramMap);
	}

	public double pay_query_defeated_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.pay_query_defeated_money",paramMap);
	}

	public int pay_query_refund_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.pay_query_refund_count",paramMap);
	}

	public double pay_query_succeed_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.pay_query_succeed_money",paramMap);
	}

	public int pay_query_ticket_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.pay_query_ticket_count",paramMap);
	}
	
	public int pay_over_time(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.pay_over_time",paramMap);
	}
	
	public int pay_queryPre_bx_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.pay_queryPre_bx_count",paramMap);
	}

	public double pay_queryPre_bx_countMoney10(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.pay_queryPre_bx_countMoney10",paramMap);
	}

	public double pay_queryPre_bx_countMoney20(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.pay_queryPre_bx_countMoney20",paramMap);
	}

	public double pay_queryPre_change_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.pay_queryPre_change_money",paramMap);
	}

	public int pay_queryPre_pay_defeated(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.pay_queryPre_pay_defeated",paramMap);
	}

	public int pay_queryPre_preparative_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.pay_queryPre_preparative_count",paramMap);
	}

	//cmpay统计
	public int cmpay_query_day_order_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.cmpay_query_day_order_count",paramMap);
	}

	public int cmpay_query_day_out_ticket_defeated(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.cmpay_query_day_out_ticket_defeated",paramMap);
	}

	public int cmpay_query_day_out_ticket_succeed(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.cmpay_query_day_out_ticket_succeed",paramMap);
	}

	public double cmpay_query_defeated_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.cmpay_query_defeated_money",paramMap);
	}

	public int cmpay_query_refund_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.cmpay_query_refund_count",paramMap);
	}

	public double cmpay_query_succeed_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.cmpay_query_succeed_money",paramMap);
	}

	public int cmpay_query_ticket_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.cmpay_query_ticket_count",paramMap);
	}

	public int cmpay_over_time(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.cmpay_over_time",paramMap);
	}

	public int cmpay_queryPre_preparative_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.cmpay_queryPre_preparative_count",paramMap);
	}

	public int cmpay_queryPre_pay_defeated(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.cmpay_queryPre_pay_defeated",paramMap);
	}

	public double cmpay_queryPre_change_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.cmpay_queryPre_change_money",paramMap);
	}

	public int cmpay_queryPre_bx_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.cmpay_queryPre_bx_count",paramMap);
	}

	public double cmpay_queryPre_bx_countMoney10(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.cmpay_queryPre_bx_countMoney10",paramMap);
	}

	public double cmpay_queryPre_bx_countMoney20(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.cmpay_queryPre_bx_countMoney20",paramMap);
	}

	public int query19eTodayCount(Map<String, Object> map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.query19eTodayCount", map);
	}

	public void updateToTj_Hc_orderInfo(Map<String, Object> map) {
		this.getSqlMapClientTemplate().update("tj_Hc_orderInfo_today.updateToTj_Hc_orderInfo", map);
	}


	//app统计
	public int app_query_day_order_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.app_query_day_order_count",paramMap);
	}

	public int app_query_day_out_ticket_defeated(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.app_query_day_out_ticket_defeated",paramMap);
	}

	public int app_query_day_out_ticket_succeed(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.app_query_day_out_ticket_succeed",paramMap);
	}

	public double app_query_defeated_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.app_query_defeated_money",paramMap);
	}

	public int app_query_refund_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.app_query_refund_count",paramMap);
	}

	public double app_query_succeed_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.app_query_succeed_money",paramMap);
	}

	public int app_query_ticket_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.app_query_ticket_count",paramMap);
	}

	public int app_over_time(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.app_over_time",paramMap);
	}

	public int app_queryPre_preparative_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.app_queryPre_preparative_count",paramMap);
	}

	public int app_queryPre_pay_defeated(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.app_queryPre_pay_defeated",paramMap);
	}

	public double app_queryPre_change_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.app_queryPre_change_money",paramMap);
	}

	public int app_queryPre_bx_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.app_queryPre_bx_count",paramMap);
	}

	public double app_queryPre_bx_countMoney10(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.app_queryPre_bx_countMoney10",paramMap);
	}

	public double app_queryPre_bx_countMoney20(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.app_queryPre_bx_countMoney20",paramMap);
	}

	//建行ccb统计
	public int ccb_query_day_order_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.ccb_query_day_order_count",paramMap);
	}

	public int ccb_query_day_out_ticket_defeated(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.ccb_query_day_out_ticket_defeated",paramMap);
	}

	public int ccb_query_day_out_ticket_succeed(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.ccb_query_day_out_ticket_succeed",paramMap);
	}

	public double ccb_query_defeated_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.ccb_query_defeated_money",paramMap);
	}

	public int ccb_query_refund_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.ccb_query_refund_count",paramMap);
	}

	public double ccb_query_succeed_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.ccb_query_succeed_money",paramMap);
	}

	public int ccb_query_ticket_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.ccb_query_ticket_count",paramMap);
	}

	public int ccb_over_time(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.ccb_over_time",paramMap);
	}

	public int ccb_queryPre_preparative_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.ccb_queryPre_preparative_count",paramMap);
	}

	public int ccb_queryPre_pay_defeated(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.ccb_queryPre_pay_defeated",paramMap);
	}

	public double ccb_queryPre_change_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.ccb_queryPre_change_money",paramMap);
	}

	public int ccb_queryPre_bx_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.ccb_queryPre_bx_count",paramMap);
	}

	public double ccb_queryPre_bx_countMoney10(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.ccb_queryPre_bx_countMoney10",paramMap);
	}

	public double ccb_queryPre_bx_countMoney20(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.ccb_queryPre_bx_countMoney20",paramMap);
	}
	
	

	//建行chq统计
	public int chq_query_day_order_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.chq_query_day_order_count",paramMap);
	}

	public int chq_query_day_out_ticket_defeated(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.chq_query_day_out_ticket_defeated",paramMap);
	}

	public int chq_query_day_out_ticket_succeed(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.chq_query_day_out_ticket_succeed",paramMap);
	}

	public double chq_query_defeated_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.chq_query_defeated_money",paramMap);
	}

	public int chq_query_refund_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.chq_query_refund_count",paramMap);
	}

	public double chq_query_succeed_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.chq_query_succeed_money",paramMap);
	}

	public int chq_query_ticket_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.chq_query_ticket_count",paramMap);
	}

	public int chq_over_time(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.chq_over_time",paramMap);
	}

	public int chq_queryPre_preparative_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.chq_queryPre_preparative_count",paramMap);
	}

	public int chq_queryPre_pay_defeated(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.chq_queryPre_pay_defeated",paramMap);
	}

	public double chq_queryPre_change_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.chq_queryPre_change_money",paramMap);
	}

	public int chq_queryPre_bx_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.chq_queryPre_bx_count",paramMap);
	}

	public double chq_queryPre_bx_countMoney10(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.chq_queryPre_bx_countMoney10",paramMap);
	}

	public double chq_queryPre_bx_countMoney20(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.chq_queryPre_bx_countMoney20",paramMap);
	}
	
	//ext统计
	public int ext_query_day_order_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.ext_query_day_order_count",paramMap);
	}

	public int ext_query_day_out_ticket_defeated(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.ext_query_day_out_ticket_defeated",paramMap);
	}

	public int ext_query_day_out_ticket_succeed(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.ext_query_day_out_ticket_succeed",paramMap);
	}

	public double ext_query_defeated_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.ext_query_defeated_money",paramMap);
	}

	public int ext_query_refund_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.ext_query_refund_count",paramMap);
	}

	public double ext_query_succeed_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.ext_query_succeed_money",paramMap);
	}

	public int ext_query_ticket_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.ext_query_ticket_count",paramMap);
	}

	public int ext_over_time(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.ext_over_time",paramMap);
	}

	public int ext_queryPre_preparative_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.ext_queryPre_preparative_count",paramMap);
	}

	public int ext_queryPre_pay_defeated(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.ext_queryPre_pay_defeated",paramMap);
	}

	public double ext_queryPre_change_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.ext_queryPre_change_money",paramMap);
	}

	public int ext_queryPre_bx_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.ext_queryPre_bx_count",paramMap);
	}
	
	//10元保险和20元保险的个数
	public int ext_queryPre_bx_count10(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.ext_queryPre_bx_count10",paramMap);
	}
	public int ext_queryPre_bx_count20(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.ext_queryPre_bx_count20",paramMap);
	}


	public double ext_queryPre_bx_countMoney10(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.ext_queryPre_bx_countMoney10",paramMap);
	}

	public double ext_queryPre_bx_countMoney20(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.ext_queryPre_bx_countMoney20",paramMap);
	}

	public List<Map<String, String>> queryMerchantId() {
		return this.getSqlMapClientTemplate().queryForList("tj_Hc_orderInfo_today.queryMerchantId");
	}

	public double queryExtOut_ticket_XL(Map<String, Object> extMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.queryExtOut_ticket_XL",extMap);
	}
	public double queryExtPay_time(Map<String, Object> extMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.queryExtPay_time",extMap);
	}
	
	//以下艺龙的统计
	public int elong_query_day_order_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.elong_query_day_order_count",paramMap);
	}

	public int elong_query_day_out_ticket_defeated(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.elong_query_day_out_ticket_defeated",paramMap);
	}

	public int elong_query_day_out_ticket_succeed(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.elong_query_day_out_ticket_succeed",paramMap);
	}
	
	public int elong_query_day_out_ticket_num_succeed(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.elong_query_day_out_ticket_num_succeed",paramMap);
	}

	public double elong_query_defeated_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.elong_query_defeated_money",paramMap);
	}

	public int elong_query_refund_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.elong_query_refund_count",paramMap);
	}
	
	public int elong_query_refund_cp_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.elong_query_refund_cp_count",paramMap);
	}

	public double elong_query_succeed_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.elong_query_succeed_money",paramMap);
	}

	public int elong_query_ticket_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.elong_query_ticket_count",paramMap);
	}

	public int elong_over_time(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.elong_over_time",paramMap);
	}

	public int queryPre_svip_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.queryPre_svip_count",paramMap);
	}
	public int queryPre_svip_lose(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.queryPre_svip_lose",paramMap);
	}

	
	
	public double app_query_refund_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.app_query_refund_money",paramMap);
	}

	public double ccb_query_refund_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.ccb_query_refund_money",paramMap);
	}

	public double chq_query_refund_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.chq_query_refund_money",paramMap);
	}

	public double cmpay_query_refund_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.cmpay_query_refund_money",paramMap);
	}
	public double elong_query_refund_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.elong_query_refund_money",paramMap);
	}

	public double ext_query_refund_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.ext_query_refund_money",paramMap);
	}

	public double pay_query_refund_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.pay_query_refund_money",paramMap);
	}
	public double qunar_query_refund_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.qunar_query_refund_money",paramMap);
	}

	
	
	//内嵌统计
	public int inner_over_time(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.inner_over_time",paramMap);
	}


	public int inner_queryPre_bx_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.inner_queryPre_bx_count",paramMap);
	}


	public double inner_queryPre_bx_countMoney10(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.inner_queryPre_bx_countMoney10",paramMap);
	}

	public double inner_queryPre_bx_countMoney20(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.inner_queryPre_bx_countMoney20",paramMap);
	}

	public double inner_queryPre_change_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.inner_queryPre_change_money",paramMap);
	}

	public int inner_queryPre_pay_defeated(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.inner_queryPre_pay_defeated",paramMap);
	}


	public int inner_queryPre_preparative_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.inner_queryPre_preparative_count",paramMap);
	}


	public int inner_query_day_order_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.inner_query_day_order_count",paramMap);
	}


	public int inner_query_day_out_ticket_defeated(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.inner_query_day_out_ticket_defeated",paramMap);
	}

	public int inner_query_day_out_ticket_succeed(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.inner_query_day_out_ticket_succeed",paramMap);
	}


	public double inner_query_defeated_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.inner_query_defeated_money",paramMap);
	}

	public int inner_query_refund_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.inner_query_refund_count",paramMap);
	}


	public double inner_query_refund_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.inner_query_refund_money",paramMap);
	}

	public double inner_query_succeed_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.inner_query_succeed_money",paramMap);
	}

	public int inner_query_ticket_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.inner_query_ticket_count",paramMap);
	}

	//cmwap统计
	public int cmwap_query_day_order_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.cmwap_query_day_order_count",paramMap);
	}

	public int cmwap_query_day_out_ticket_defeated(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.cmwap_query_day_out_ticket_defeated",paramMap);
	}

	public int cmwap_query_day_out_ticket_succeed(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.cmwap_query_day_out_ticket_succeed",paramMap);
	}

	public double cmwap_query_defeated_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.cmwap_query_defeated_money",paramMap);
	}

	public int cmwap_query_refund_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.cmwap_query_refund_count",paramMap);
	}

	public double cmwap_query_succeed_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.cmwap_query_succeed_money",paramMap);
	}

	public int cmwap_query_ticket_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.cmwap_query_ticket_count",paramMap);
	}

	public int cmwap_over_time(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.cmwap_over_time",paramMap);
	}

	public int cmwap_queryPre_preparative_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.cmwap_queryPre_preparative_count",paramMap);
	}

	public int cmwap_queryPre_pay_defeated(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.cmwap_queryPre_pay_defeated",paramMap);
	}

	public double cmwap_queryPre_change_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.cmwap_queryPre_change_money",paramMap);
	}

	public int cmwap_queryPre_bx_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.cmwap_queryPre_bx_count",paramMap);
	}

	public double cmwap_queryPre_bx_countMoney10(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.cmwap_queryPre_bx_countMoney10",paramMap);
	}

	public double cmwap_queryPre_bx_countMoney20(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.cmwap_queryPre_bx_countMoney20",paramMap);
	}

	public double cmwap_query_refund_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.cmwap_query_refund_money",paramMap);
	}

	
	public int elong_out_time_order(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.elong_out_time_order",paramMap);
	}
	public int elong_cancel_order(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.elong_cancel_order",paramMap);
	}
	
	//利安出票效率和支付效率
	public double queryExtOut_ticket_XL_lian(Map<String, Object> extMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.queryExtOut_ticket_XL_lian",extMap);
	}

	public double queryExtPay_time_lian(Map<String, Object> extMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.queryExtPay_time_lian",extMap);
	}

	
	//高铁管家
	public int gt_query_day_order_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.gt_query_day_order_count",paramMap);
	}

	public int gt_query_day_out_ticket_defeated(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.gt_query_day_out_ticket_defeated",paramMap);
	}

	public int gt_query_day_out_ticket_succeed(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.gt_query_day_out_ticket_succeed",paramMap);
	}

	public double gt_query_defeated_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.gt_query_defeated_money",paramMap);
	}

	public int gt_query_refund_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.gt_query_refund_count",paramMap);
	}

	public double gt_query_succeed_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.gt_query_succeed_money",paramMap);
	}

	public int gt_query_ticket_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.gt_query_ticket_count",paramMap);
	}

	public int gt_over_time(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.gt_over_time",paramMap);
	}

	public int gt_queryPre_preparative_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.gt_queryPre_preparative_count",paramMap);
	}

	public int gt_queryPre_pay_defeated(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.gt_queryPre_pay_defeated",paramMap);
	}

	public double gt_queryPre_change_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.gt_queryPre_change_money",paramMap);
	}

	public int gt_queryPre_bx_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.gt_queryPre_bx_count",paramMap);
	}

	public double gt_query_refund_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.gt_query_refund_money",paramMap);
	}
	
	
	//携程
	public int xc_query_day_order_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.xc_query_day_order_count",paramMap);
	}

	public int xc_query_day_out_ticket_defeated(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.xc_query_day_out_ticket_defeated",paramMap);
	}

	public int xc_query_day_out_ticket_succeed(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.xc_query_day_out_ticket_succeed",paramMap);
	}

	public double xc_query_defeated_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.xc_query_defeated_money",paramMap);
	}

	public int xc_query_refund_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.xc_query_refund_count",paramMap);
	}

	public double xc_query_succeed_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.xc_query_succeed_money",paramMap);
	}

	public int xc_query_ticket_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.xc_query_ticket_count",paramMap);
	}

//	public int xc_over_time(Map<String, Object> paramMap) {
//		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.xc_over_time",paramMap);
//	}

//	public int xc_queryPre_preparative_count(Map<String, Object> paramMap) {
//		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.xc_queryPre_preparative_count",paramMap);
//	}

//	public int xc_queryPre_pay_defeated(Map<String, Object> paramMap) {
//		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.xc_queryPre_pay_defeated",paramMap);
//	}

	public double xc_queryPre_change_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.xc_queryPre_change_money",paramMap);
	}

	public int xc_queryPre_bx_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.xc_queryPre_bx_count",paramMap);
	}

	public double xc_query_refund_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.xc_query_refund_money",paramMap);
	}

	//美团
	public int meituan_cancel_order(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.meituan_cancel_order",paramMap);
	}

	public int meituan_out_time_order(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.meituan_out_time_order",paramMap);
	}

	public int meituan_over_time(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.meituan_over_time",paramMap);
	}

	public int meituan_query_day_order_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.meituan_query_day_order_count",paramMap);
	}

	public int meituan_query_day_out_ticket_defeated(
			Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.meituan_query_day_out_ticket_defeated",paramMap);
	}

	public int meituan_query_day_out_ticket_succeed(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.meituan_query_day_out_ticket_succeed",paramMap);
	}

	public double meituan_query_defeated_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.meituan_query_defeated_money",paramMap);
	}

	public int meituan_query_refund_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.meituan_query_refund_count",paramMap);
	}

	public double meituan_query_refund_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.meituan_query_refund_money",paramMap);
	}

	public double meituan_query_succeed_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.meituan_query_succeed_money",paramMap);
	}

	public int meituan_query_ticket_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.meituan_query_ticket_count",paramMap);
	}

	//途牛
	public int tuniu_cancel_order(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.tuniu_cancel_order",paramMap);
	}

	public int tuniu_out_time_order(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.tuniu_out_time_order",paramMap);
	}

	public int tuniu_over_time(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.tuniu_over_time",paramMap);
	}

	public int tuniu_query_day_order_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.tuniu_query_day_order_count",paramMap);
	}

	public int tuniu_query_day_out_ticket_defeated(
			Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.tuniu_query_day_out_ticket_defeated",paramMap);
	}

	public int tuniu_query_day_out_ticket_succeed(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.tuniu_query_day_out_ticket_succeed",paramMap);
	}

	public double tuniu_query_defeated_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.tuniu_query_defeated_money",paramMap);
	}

	public int tuniu_query_refund_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.tuniu_query_refund_count",paramMap);
	}

	public double tuniu_query_refund_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.tuniu_query_refund_money",paramMap);
	}

	public double tuniu_query_succeed_money(Map<String, Object> paramMap) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.tuniu_query_succeed_money",paramMap);
	}

	public int tuniu_query_ticket_count(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_Hc_orderInfo_today.tuniu_query_ticket_count",paramMap);
	}
}
