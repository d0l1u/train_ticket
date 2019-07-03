package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.Tj_Hc_orderInfo_TodayDao;
import com.l9e.transaction.service.Tj_Hc_orderInfo_Today_Service;

@Service("tj_Hc_orderInfo_Today_Service")
public class Tj_Hc_orderInfo_Today_ServiceImpl implements
		Tj_Hc_orderInfo_Today_Service {
	@Resource
	private Tj_Hc_orderInfo_TodayDao tj_Hc_orderInfo_TodayDao;

	public int queryPre_day_out_ticket_succeed(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.queryPre_day_out_ticket_succeed(paramMap);
	}

	public int queryPre_day_out_ticket_defeated(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.queryPre_day_out_ticket_defeated(paramMap);
	}

	public int queryPre_day_order_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.queryPre_day_order_count(paramMap);
	}

	public int queryPre_preparative_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.queryPre_preparative_count(paramMap);
	}

	public int queryPre_pay_defeated(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.queryPre_pay_defeated(paramMap);
	}

	// 超过10分钟订单
	public int queryPre_over_time(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.queryPre_over_time(paramMap);
	}

	public int queryPre_bx_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.queryPre_bx_count(paramMap);
	}

	public double queryPre_bx_countMoney10(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.queryPre_bx_countMoney10(paramMap);
	}

	public double queryPre_bx_countMoney20(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.queryPre_bx_countMoney20(paramMap);
	}

	public double queryPre_defeated_money(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.queryPre_defeated_money(paramMap);
	}

	public int queryPre_refund_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.queryPre_refund_count(paramMap);
	}

	public double queryPre_succeed_money(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.queryPre_succeed_money(paramMap);
	}

	public double queryPre_change_money(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.queryPre_change_money(paramMap);
	}
	
	public double queryPre_refund_money(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.queryPre_refund_money(paramMap);
	}

	public int queryPre_ticket_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.queryPre_ticket_count(paramMap);
	}

	public void addToTj_Hc_orderInfo(Map<String, Object> map) {
		tj_Hc_orderInfo_TodayDao.addToTj_Hc_orderInfo(map);
	}

	public int queryTable_Count() {
		return tj_Hc_orderInfo_TodayDao.queryTable_Count();
	}

	public List<String> queryDate_List() {
		return tj_Hc_orderInfo_TodayDao.queryDate_List();
	}

	public int queryActiveTotal(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.queryActiveTotal(paramMap);
	}

	public int queryVip_lose(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.queryVip_lose(paramMap);
	}

	public int queryVip_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.queryVip_count(paramMap);
	}
	
	public double queryPay_time(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.queryPay_time(paramMap);
	}
	

	// 以下为去哪儿统计
	public int qunar_query_day_order_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.qunar_query_day_order_count(paramMap);
	}

	public int qunar_query_day_out_ticket_defeated(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.qunar_query_day_out_ticket_defeated(paramMap);
	}

	public int qunar_query_day_out_ticket_succeed(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.qunar_query_day_out_ticket_succeed(paramMap);
	}

	public double qunar_query_defeated_money(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.qunar_query_defeated_money(paramMap);
	}

	public int qunar_query_refund_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.qunar_query_refund_count(paramMap);
	}

	public double qunar_query_succeed_money(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.qunar_query_succeed_money(paramMap);
	}

	public int qunar_query_ticket_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.qunar_query_ticket_count(paramMap);
	}

	public double queryOut_ticket_XL(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.queryOut_ticket_XL(paramMap);
	}

	public int qunar_over_time(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.qunar_over_time(paramMap);
	}
	

	public double qunar_query_refund_money(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.qunar_query_refund_money(paramMap);
	}
	

	// 19pay统计
	public int pay_query_day_order_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.pay_query_day_order_count(paramMap);
	}

	public int pay_query_day_out_ticket_defeated(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.pay_query_day_out_ticket_defeated(paramMap);
	}

	public int pay_query_day_out_ticket_succeed(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.pay_query_day_out_ticket_succeed(paramMap);
	}

	public double pay_query_defeated_money(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.pay_query_defeated_money(paramMap);
	}

	public int pay_query_refund_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.pay_query_refund_count(paramMap);
	}

	public double pay_query_succeed_money(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.pay_query_succeed_money(paramMap);
	}

	public int pay_query_ticket_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.pay_query_ticket_count(paramMap);
	}

	public int pay_over_time(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.pay_over_time(paramMap);
	}

	public int pay_queryPre_bx_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.pay_queryPre_bx_count(paramMap);
	}

	public double pay_queryPre_bx_countMoney10(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.pay_queryPre_bx_countMoney10(paramMap);
	}

	public double pay_queryPre_bx_countMoney20(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.pay_queryPre_bx_countMoney20(paramMap);
	}

	public double pay_queryPre_change_money(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.pay_queryPre_change_money(paramMap);
	}

	public int pay_queryPre_pay_defeated(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.pay_queryPre_pay_defeated(paramMap);
	}

	public int pay_queryPre_preparative_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.pay_queryPre_preparative_count(paramMap);
	}
	
	public double pay_query_refund_money(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.pay_query_refund_money(paramMap);
	}

	// cmpay统计
	public int cmpay_query_day_order_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.cmpay_query_day_order_count(paramMap);
	}

	public int cmpay_query_day_out_ticket_defeated(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.cmpay_query_day_out_ticket_defeated(paramMap);
	}

	public int cmpay_query_day_out_ticket_succeed(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.cmpay_query_day_out_ticket_succeed(paramMap);
	}

	public double cmpay_query_defeated_money(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.cmpay_query_defeated_money(paramMap);
	}

	public int cmpay_query_refund_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.cmpay_query_refund_count(paramMap);
	}

	public double cmpay_query_succeed_money(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.cmpay_query_succeed_money(paramMap);
	}

	public int cmpay_query_ticket_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.cmpay_query_ticket_count(paramMap);
	}

	public int cmpay_over_time(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.cmpay_over_time(paramMap);
	}

	public int cmpay_queryPre_preparative_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.cmpay_queryPre_preparative_count(paramMap);
	}

	public int cmpay_queryPre_pay_defeated(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.cmpay_queryPre_pay_defeated(paramMap);
	}

	public double cmpay_queryPre_change_money(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.cmpay_queryPre_change_money(paramMap);
	}

	public int cmpay_queryPre_bx_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.cmpay_queryPre_bx_count(paramMap);
	}

	public double cmpay_queryPre_bx_countMoney10(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.cmpay_queryPre_bx_countMoney10(paramMap);
	}

	public double cmpay_queryPre_bx_countMoney20(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.cmpay_queryPre_bx_countMoney20(paramMap);
	}

	public int query19eTodayCount(Map<String, Object> map) {
		return tj_Hc_orderInfo_TodayDao.query19eTodayCount(map);
	}

	public void updateToTj_Hc_orderInfo(Map<String, Object> map) {
		tj_Hc_orderInfo_TodayDao.updateToTj_Hc_orderInfo(map);
	}
	

	public double cmpay_query_refund_money(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.cmpay_query_refund_money(paramMap);
	}
	
	// app统计
	public int app_query_day_order_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.app_query_day_order_count(paramMap);
	}

	public int app_query_day_out_ticket_defeated(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.app_query_day_out_ticket_defeated(paramMap);
	}

	public int app_query_day_out_ticket_succeed(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.app_query_day_out_ticket_succeed(paramMap);
	}

	public double app_query_defeated_money(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.app_query_defeated_money(paramMap);
	}

	public int app_query_refund_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.app_query_refund_count(paramMap);
	}

	public double app_query_succeed_money(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.app_query_succeed_money(paramMap);
	}

	public int app_query_ticket_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.app_query_ticket_count(paramMap);
	}

	public int app_over_time(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.app_over_time(paramMap);
	}

	public int app_queryPre_preparative_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.app_queryPre_preparative_count(paramMap);
	}

	public int app_queryPre_pay_defeated(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.app_queryPre_pay_defeated(paramMap);
	}

	public double app_queryPre_change_money(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.app_queryPre_change_money(paramMap);
	}

	public int app_queryPre_bx_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.app_queryPre_bx_count(paramMap);
	}

	public double app_queryPre_bx_countMoney10(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.app_queryPre_bx_countMoney10(paramMap);
	}

	public double app_queryPre_bx_countMoney20(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.app_queryPre_bx_countMoney20(paramMap);
	}
	
	public double app_query_refund_money(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.app_query_refund_money(paramMap);
	}

	
	
	// ccb统计
	public int ccb_query_day_order_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.ccb_query_day_order_count(paramMap);
	}

	public int ccb_query_day_out_ticket_defeated(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.ccb_query_day_out_ticket_defeated(paramMap);
	}

	public int ccb_query_day_out_ticket_succeed(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.ccb_query_day_out_ticket_succeed(paramMap);
	}

	public double ccb_query_defeated_money(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.ccb_query_defeated_money(paramMap);
	}

	public int ccb_query_refund_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.ccb_query_refund_count(paramMap);
	}

	public double ccb_query_succeed_money(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.ccb_query_succeed_money(paramMap);
	}

	public int ccb_query_ticket_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.ccb_query_ticket_count(paramMap);
	}

	public int ccb_over_time(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.ccb_over_time(paramMap);
	}

	public int ccb_queryPre_preparative_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.ccb_queryPre_preparative_count(paramMap);
	}

	public int ccb_queryPre_pay_defeated(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.ccb_queryPre_pay_defeated(paramMap);
	}

	public double ccb_queryPre_change_money(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.ccb_queryPre_change_money(paramMap);
	}

	public int ccb_queryPre_bx_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.ccb_queryPre_bx_count(paramMap);
	}

	public double ccb_queryPre_bx_countMoney10(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.ccb_queryPre_bx_countMoney10(paramMap);
	}

	public double ccb_queryPre_bx_countMoney20(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.ccb_queryPre_bx_countMoney20(paramMap);
	}
	
	public double ccb_query_refund_money(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.ccb_query_refund_money(paramMap);
	}

	
	// chq统计
	public int chq_query_day_order_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.chq_query_day_order_count(paramMap);
	}

	public int chq_query_day_out_ticket_defeated(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.chq_query_day_out_ticket_defeated(paramMap);
	}

	public int chq_query_day_out_ticket_succeed(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.chq_query_day_out_ticket_succeed(paramMap);
	}

	public double chq_query_defeated_money(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.chq_query_defeated_money(paramMap);
	}

	public int chq_query_refund_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.chq_query_refund_count(paramMap);
	}

	public double chq_query_succeed_money(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.chq_query_succeed_money(paramMap);
	}

	public int chq_query_ticket_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.chq_query_ticket_count(paramMap);
	}

	public int chq_over_time(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.chq_over_time(paramMap);
	}

	public int chq_queryPre_preparative_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.chq_queryPre_preparative_count(paramMap);
	}

	public int chq_queryPre_pay_defeated(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.chq_queryPre_pay_defeated(paramMap);
	}

	public double chq_queryPre_change_money(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.chq_queryPre_change_money(paramMap);
	}

	public int chq_queryPre_bx_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.chq_queryPre_bx_count(paramMap);
	}

	public double chq_queryPre_bx_countMoney10(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.chq_queryPre_bx_countMoney10(paramMap);
	}

	public double chq_queryPre_bx_countMoney20(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.chq_queryPre_bx_countMoney20(paramMap);
	}
	
	public double chq_query_refund_money(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.chq_query_refund_money(paramMap);
	}


	// ext统计
	public int ext_query_day_order_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.ext_query_day_order_count(paramMap);
	}

	public int ext_query_day_out_ticket_defeated(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.ext_query_day_out_ticket_defeated(paramMap);
	}

	public int ext_query_day_out_ticket_succeed(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.ext_query_day_out_ticket_succeed(paramMap);
	}

	public double ext_query_defeated_money(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.ext_query_defeated_money(paramMap);
	}

	public int ext_query_refund_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.ext_query_refund_count(paramMap);
	}

	public double ext_query_succeed_money(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.ext_query_succeed_money(paramMap);
	}

	public int ext_query_ticket_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.ext_query_ticket_count(paramMap);
	}

	public int ext_over_time(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.ext_over_time(paramMap);
	}

	public int ext_queryPre_preparative_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.ext_queryPre_preparative_count(paramMap);
	}

	public int ext_queryPre_pay_defeated(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.ext_queryPre_pay_defeated(paramMap);
	}

	public double ext_queryPre_change_money(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.ext_queryPre_change_money(paramMap);
	}

	public int ext_queryPre_bx_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.ext_queryPre_bx_count(paramMap);
	}

	public double ext_queryPre_bx_countMoney10(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.ext_queryPre_bx_countMoney10(paramMap);
	}

	public double ext_queryPre_bx_countMoney20(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.ext_queryPre_bx_countMoney20(paramMap);
	}

	public List<Map<String, String>> queryMerchantId() {
		return tj_Hc_orderInfo_TodayDao.queryMerchantId();
	}

	public double queryExtOut_ticket_XL(Map<String, Object> extMap) {
		return tj_Hc_orderInfo_TodayDao.queryExtOut_ticket_XL(extMap);
	}
	public double queryExtPay_time(Map<String, Object> extMap) {
		return tj_Hc_orderInfo_TodayDao.queryExtPay_time(extMap);
	}
	public double ext_query_refund_money(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.ext_query_refund_money(paramMap);
	}

	
	// 以下为艺龙统计
	public int elong_query_day_order_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.elong_query_day_order_count(paramMap);
	}

	public int elong_query_day_out_ticket_defeated(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.elong_query_day_out_ticket_defeated(paramMap);
	}

	public int elong_query_day_out_ticket_succeed(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.elong_query_day_out_ticket_succeed(paramMap);
	}
	
	public int elong_query_day_out_ticket_num_succeed(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.elong_query_day_out_ticket_num_succeed(paramMap);
	}

	public double elong_query_defeated_money(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.elong_query_defeated_money(paramMap);
	}

	public int elong_query_refund_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.elong_query_refund_count(paramMap);
	}

	public int elong_query_refund_cp_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.elong_query_refund_cp_count(paramMap);
	}

	public double elong_query_succeed_money(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.elong_query_succeed_money(paramMap);
	}

	public int elong_query_ticket_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.elong_query_ticket_count(paramMap);
	}

	public int elong_over_time(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.elong_over_time(paramMap);
	}

	public int queryPre_svip_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.queryPre_svip_count(paramMap);
	}
	public int queryPre_svip_lose(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.queryPre_svip_lose(paramMap);
	}

	public double elong_query_refund_money(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.elong_query_refund_money(paramMap);
	}

	public int elong_out_time_order(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.elong_out_time_order(paramMap);
	}

	public int elong_cancel_order(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.elong_cancel_order(paramMap);
	}
	//内嵌统计
	public int inner_over_time(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.inner_over_time(paramMap);
	}


	public int inner_queryPre_bx_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.inner_queryPre_bx_count(paramMap);
	}


	public double inner_queryPre_bx_countMoney10(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.inner_queryPre_bx_countMoney10(paramMap);
	}


	public double inner_queryPre_bx_countMoney20(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.inner_queryPre_bx_countMoney20(paramMap);
	}


	public double inner_queryPre_change_money(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.inner_queryPre_change_money(paramMap);
	}


	public int inner_queryPre_pay_defeated(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.inner_queryPre_pay_defeated(paramMap);
	}


	public int inner_queryPre_preparative_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.inner_queryPre_preparative_count(paramMap);
	}


	public int inner_query_day_order_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.inner_query_day_order_count(paramMap);
	}


	public int inner_query_day_out_ticket_defeated(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.inner_query_day_out_ticket_defeated(paramMap);
	}


	public int inner_query_day_out_ticket_succeed(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.inner_query_day_out_ticket_succeed(paramMap);
	}


	public double inner_query_defeated_money(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.inner_query_defeated_money(paramMap);
	}


	public int inner_query_refund_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.inner_query_refund_count(paramMap);
	}


	public double inner_query_refund_money(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.inner_query_refund_money(paramMap);
	}


	public double inner_query_succeed_money(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.inner_query_succeed_money(paramMap);
	}


	public int inner_query_ticket_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.inner_query_ticket_count(paramMap);
	}


	//10元保险和20元保险的个数
	public int ext_queryPre_bx_count10(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.ext_queryPre_bx_count10(paramMap);
	}

	public int ext_queryPre_bx_count20(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.ext_queryPre_bx_count20(paramMap);
	}

	// cmwap统计
	public int cmwap_query_day_order_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.cmwap_query_day_order_count(paramMap);
	}

	public int cmwap_query_day_out_ticket_defeated(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.cmwap_query_day_out_ticket_defeated(paramMap);
	}

	public int cmwap_query_day_out_ticket_succeed(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.cmwap_query_day_out_ticket_succeed(paramMap);
	}

	public double cmwap_query_defeated_money(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.cmwap_query_defeated_money(paramMap);
	}

	public int cmwap_query_refund_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.cmwap_query_refund_count(paramMap);
	}

	public double cmwap_query_succeed_money(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.cmwap_query_succeed_money(paramMap);
	}

	public int cmwap_query_ticket_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.cmwap_query_ticket_count(paramMap);
	}

	public int cmwap_over_time(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.cmwap_over_time(paramMap);
	}

	public int cmwap_queryPre_preparative_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.cmwap_queryPre_preparative_count(paramMap);
	}

	public int cmwap_queryPre_pay_defeated(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.cmwap_queryPre_pay_defeated(paramMap);
	}

	public double cmwap_queryPre_change_money(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.cmwap_queryPre_change_money(paramMap);
	}

	public int cmwap_queryPre_bx_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.cmwap_queryPre_bx_count(paramMap);
	}

	public double cmwap_queryPre_bx_countMoney10(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.cmwap_queryPre_bx_countMoney10(paramMap);
	}

	public double cmwap_queryPre_bx_countMoney20(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.cmwap_queryPre_bx_countMoney20(paramMap);
	}

	public double cmwap_query_refund_money(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.cmwap_query_refund_money(paramMap);
	}
	
	//利安出票效率和支付效率
	public double queryExtOut_ticket_XL_lian(Map<String, Object> extMap) {
		return tj_Hc_orderInfo_TodayDao.queryExtOut_ticket_XL_lian(extMap);
	}
	public double queryExtPay_time_lian(Map<String, Object> extMap) {
		return tj_Hc_orderInfo_TodayDao.queryExtPay_time_lian(extMap);
	}

	//高铁管家
	public int gt_query_day_order_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.gt_query_day_order_count(paramMap);
	}

	public int gt_query_day_out_ticket_defeated(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.gt_query_day_out_ticket_defeated(paramMap);
	}

	public int gt_query_day_out_ticket_succeed(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.gt_query_day_out_ticket_succeed(paramMap);
	}

	public double gt_query_defeated_money(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.gt_query_defeated_money(paramMap);
	}

	public int gt_query_refund_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.gt_query_refund_count(paramMap);
	}

	public double gt_query_succeed_money(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.gt_query_succeed_money(paramMap);
	}

	public int gt_query_ticket_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.gt_query_ticket_count(paramMap);
	}

	public int gt_over_time(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.gt_over_time(paramMap);
	}

	public int gt_queryPre_preparative_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.gt_queryPre_preparative_count(paramMap);
	}

	public int gt_queryPre_pay_defeated(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.gt_queryPre_pay_defeated(paramMap);
	}

	public double gt_queryPre_change_money(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.gt_queryPre_change_money(paramMap);
	}

	public int gt_queryPre_bx_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.gt_queryPre_bx_count(paramMap);
	}

	public double gt_query_refund_money(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.gt_query_refund_money(paramMap);
	}

	
	//携程
	public int xc_query_day_order_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.xc_query_day_order_count(paramMap);
	}

	public int xc_query_day_out_ticket_defeated(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.xc_query_day_out_ticket_defeated(paramMap);
	}

	public int xc_query_day_out_ticket_succeed(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.xc_query_day_out_ticket_succeed(paramMap);
	}

	public double xc_query_defeated_money(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.xc_query_defeated_money(paramMap);
	}

	public int xc_query_refund_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.xc_query_refund_count(paramMap);
	}

	public double xc_query_succeed_money(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.xc_query_succeed_money(paramMap);
	}

	public int xc_query_ticket_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.xc_query_ticket_count(paramMap);
	}

//	public int xc_over_time(Map<String, Object> paramMap) {
//		return tj_Hc_orderInfo_TodayDao.xc_over_time(paramMap);
//	}

//	public int xc_queryPre_preparative_count(Map<String, Object> paramMap) {
//		return tj_Hc_orderInfo_TodayDao.xc_queryPre_preparative_count(paramMap);
//	}

//	public int xc_queryPre_pay_defeated(Map<String, Object> paramMap) {
//		return tj_Hc_orderInfo_TodayDao.xc_queryPre_pay_defeated(paramMap);
//	}

	public double xc_queryPre_change_money(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.xc_queryPre_change_money(paramMap);
	}

	public int xc_queryPre_bx_count(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.xc_queryPre_bx_count(paramMap);
	}

	public double xc_query_refund_money(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.xc_query_refund_money(paramMap);
	}

	
	//美团统计
	public int meituan_cancel_order(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.meituan_cancel_order(paramMap);
	}

	public int meituan_out_time_order(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.meituan_out_time_order(paramMap);
	}

	public int meituan_over_time(Map<String, Object> paramMap) {
		return  tj_Hc_orderInfo_TodayDao.meituan_over_time(paramMap);
	}

	public int meituan_query_day_order_count(Map<String, Object> paramMap) {
		return  tj_Hc_orderInfo_TodayDao.meituan_query_day_order_count(paramMap);
	}

	public int meituan_query_day_out_ticket_defeated(
			Map<String, Object> paramMap) {
		return  tj_Hc_orderInfo_TodayDao.meituan_query_day_out_ticket_defeated(paramMap);
	}

	public int meituan_query_day_out_ticket_succeed(Map<String, Object> paramMap) {
		return  tj_Hc_orderInfo_TodayDao.meituan_query_day_out_ticket_succeed(paramMap);
	}

	public double meituan_query_defeated_money(Map<String, Object> paramMap) {
		return  tj_Hc_orderInfo_TodayDao.meituan_query_defeated_money(paramMap);
	}

	public int meituan_query_refund_count(Map<String, Object> paramMap) {
		return  tj_Hc_orderInfo_TodayDao.meituan_query_refund_count(paramMap);
	}

	public double meituan_query_refund_money(Map<String, Object> paramMap) {
		return  tj_Hc_orderInfo_TodayDao.meituan_query_refund_money(paramMap);
	}

	public double meituan_query_succeed_money(Map<String, Object> paramMap) {
		return  tj_Hc_orderInfo_TodayDao.meituan_query_succeed_money(paramMap);
	}

	public int meituan_query_ticket_count(Map<String, Object> paramMap) {
		return  tj_Hc_orderInfo_TodayDao.meituan_query_ticket_count(paramMap);
	}
	
	//途牛统计
	public int tuniu_cancel_order(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.tuniu_cancel_order(paramMap);
	}

	public int tuniu_out_time_order(Map<String, Object> paramMap) {
		return tj_Hc_orderInfo_TodayDao.tuniu_out_time_order(paramMap);
	}

	public int tuniu_over_time(Map<String, Object> paramMap) {
		return  tj_Hc_orderInfo_TodayDao.tuniu_over_time(paramMap);
	}

	public int tuniu_query_day_order_count(Map<String, Object> paramMap) {
		return  tj_Hc_orderInfo_TodayDao.tuniu_query_day_order_count(paramMap);
	}

	public int tuniu_query_day_out_ticket_defeated(
			Map<String, Object> paramMap) {
		return  tj_Hc_orderInfo_TodayDao.tuniu_query_day_out_ticket_defeated(paramMap);
	}

	public int tuniu_query_day_out_ticket_succeed(Map<String, Object> paramMap) {
		return  tj_Hc_orderInfo_TodayDao.tuniu_query_day_out_ticket_succeed(paramMap);
	}

	public double tuniu_query_defeated_money(Map<String, Object> paramMap) {
		return  tj_Hc_orderInfo_TodayDao.tuniu_query_defeated_money(paramMap);
	}

	public int tuniu_query_refund_count(Map<String, Object> paramMap) {
		return  tj_Hc_orderInfo_TodayDao.tuniu_query_refund_count(paramMap);
	}

	public double tuniu_query_refund_money(Map<String, Object> paramMap) {
		return  tj_Hc_orderInfo_TodayDao.tuniu_query_refund_money(paramMap);
	}

	public double tuniu_query_succeed_money(Map<String, Object> paramMap) {
		return  tj_Hc_orderInfo_TodayDao.tuniu_query_succeed_money(paramMap);
	}

	public int tuniu_query_ticket_count(Map<String, Object> paramMap) {
		return  tj_Hc_orderInfo_TodayDao.tuniu_query_ticket_count(paramMap);
	}
}
