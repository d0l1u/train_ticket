package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

public interface Tj_Hc_orderInfo_Today_Service {


	int queryPre_day_out_ticket_succeed(Map<String, Object> paramMap);

	int queryPre_day_out_ticket_defeated(Map<String, Object> paramMap);

	int queryPre_day_order_count(Map<String, Object> paramMap);

	int queryPre_preparative_count(Map<String, Object> paramMap);

	int queryPre_pay_defeated(Map<String, Object> paramMap);

	int queryPre_refund_count(Map<String, Object> paramMap);

	double queryPre_succeed_money(Map<String, Object> paramMap);

	double queryPre_defeated_money(Map<String, Object> paramMap);

	int queryPre_bx_count(Map<String, Object> paramMap);

	int queryPre_ticket_count(Map<String, Object> paramMap);

	double queryPre_bx_countMoney10(Map<String, Object> paramMap);

	double queryPre_bx_countMoney20(Map<String, Object> paramMap);

	double queryPre_change_money(Map<String, Object> paramMap);

	double queryPre_refund_money(Map<String, Object> paramMap);
	
	void addToTj_Hc_orderInfo(Map<String, Object> map);

	int queryTable_Count();

	List<String> queryDate_List();

	int queryActiveTotal(Map<String, Object> paramMap);
	

	int queryVip_lose(Map<String, Object> paramMap);  
	
	int queryVip_count(Map<String,Object> paramMap);
	
	double queryPay_time(Map<String,Object> paramMap);
	
	//以下为去哪儿的统计
	int qunar_query_day_out_ticket_succeed(Map<String, Object> paramMap);

	int qunar_query_day_out_ticket_defeated(Map<String, Object> paramMap);

	int qunar_query_day_order_count(Map<String, Object> paramMap);

	int qunar_query_refund_count(Map<String, Object> paramMap);

	double qunar_query_defeated_money(Map<String, Object> paramMap);

	double qunar_query_succeed_money(Map<String, Object> paramMap);
	
	double qunar_query_refund_money(Map<String, Object> paramMap);

	int qunar_query_ticket_count(Map<String, Object> paramMap);

	double queryOut_ticket_XL(Map<String, Object> paramMap);
	
	
	//内嵌统计
	int inner_query_day_out_ticket_succeed(Map<String, Object> paramMap);

	int inner_query_day_out_ticket_defeated(Map<String, Object> paramMap);

	int inner_query_refund_count(Map<String, Object> paramMap);

	int inner_query_day_order_count(Map<String, Object> paramMap);

	double inner_query_succeed_money(Map<String, Object> paramMap);

	double inner_query_defeated_money(Map<String, Object> paramMap);
	
	double inner_query_refund_money(Map<String, Object> paramMap);

	int inner_query_ticket_count(Map<String, Object> paramMap);

	int inner_queryPre_preparative_count(Map<String, Object> paramMap);

	int inner_queryPre_pay_defeated(Map<String, Object> paramMap);

	double inner_queryPre_change_money(Map<String, Object> paramMap);

	int inner_queryPre_bx_count(Map<String, Object> paramMap);

	double inner_queryPre_bx_countMoney10(Map<String, Object> paramMap);

	double inner_queryPre_bx_countMoney20(Map<String, Object> paramMap);
	
	int inner_over_time(Map<String, Object> paramMap);

	//19pay统计
	int pay_query_day_out_ticket_succeed(Map<String, Object> paramMap);

	int pay_query_day_out_ticket_defeated(Map<String, Object> paramMap);

	int pay_query_refund_count(Map<String, Object> paramMap);

	int pay_query_day_order_count(Map<String, Object> paramMap);

	double pay_query_succeed_money(Map<String, Object> paramMap);

	double pay_query_defeated_money(Map<String, Object> paramMap);
	
	double pay_query_refund_money(Map<String, Object> paramMap);

	int pay_query_ticket_count(Map<String, Object> paramMap);

	int pay_queryPre_preparative_count(Map<String, Object> paramMap);

	int pay_queryPre_pay_defeated(Map<String, Object> paramMap);

	double pay_queryPre_change_money(Map<String, Object> paramMap);

	int pay_queryPre_bx_count(Map<String, Object> paramMap);

	double pay_queryPre_bx_countMoney10(Map<String, Object> paramMap);

	double pay_queryPre_bx_countMoney20(Map<String, Object> paramMap);



	//cmpay统计
	int cmpay_query_day_out_ticket_succeed(Map<String, Object> paramMap);

	int cmpay_query_day_out_ticket_defeated(Map<String, Object> paramMap);

	int cmpay_query_day_order_count(Map<String, Object> paramMap);

	int cmpay_query_refund_count(Map<String, Object> paramMap);

	double cmpay_query_defeated_money(Map<String, Object> paramMap);

	double cmpay_query_succeed_money(Map<String, Object> paramMap);

	double cmpay_query_refund_money(Map<String, Object> paramMap);

	int cmpay_query_ticket_count(Map<String, Object> paramMap);

	int cmpay_over_time(Map<String, Object> paramMap);

	int cmpay_queryPre_preparative_count(Map<String, Object> paramMap);

	int cmpay_queryPre_pay_defeated(Map<String, Object> paramMap);

	double cmpay_queryPre_change_money(Map<String, Object> paramMap);

	int cmpay_queryPre_bx_count(Map<String, Object> paramMap);

	double cmpay_queryPre_bx_countMoney10(Map<String, Object> paramMap);

	double cmpay_queryPre_bx_countMoney20(Map<String, Object> paramMap);

	//cmwap统计
	int cmwap_query_day_out_ticket_succeed(Map<String, Object> paramMap);

	int cmwap_query_day_out_ticket_defeated(Map<String, Object> paramMap);

	int cmwap_query_day_order_count(Map<String, Object> paramMap);

	int cmwap_query_refund_count(Map<String, Object> paramMap);

	double cmwap_query_defeated_money(Map<String, Object> paramMap);

	double cmwap_query_succeed_money(Map<String, Object> paramMap);

	double cmwap_query_refund_money(Map<String, Object> paramMap);

	int cmwap_query_ticket_count(Map<String, Object> paramMap);

	int cmwap_over_time(Map<String, Object> paramMap);

	int cmwap_queryPre_preparative_count(Map<String, Object> paramMap);

	int cmwap_queryPre_pay_defeated(Map<String, Object> paramMap);

	double cmwap_queryPre_change_money(Map<String, Object> paramMap);

	int cmwap_queryPre_bx_count(Map<String, Object> paramMap);

	double cmwap_queryPre_bx_countMoney10(Map<String, Object> paramMap);

	double cmwap_queryPre_bx_countMoney20(Map<String, Object> paramMap);
	
	//以下为app统计
	int app_query_day_out_ticket_succeed(Map<String, Object> paramMap);

	int app_query_day_out_ticket_defeated(Map<String, Object> paramMap);

	int app_queryPre_preparative_count(Map<String, Object> paramMap);

	int app_queryPre_pay_defeated(Map<String, Object> paramMap);

	int app_query_day_order_count(Map<String, Object> paramMap);

	int app_query_refund_count(Map<String, Object> paramMap);

	double app_query_succeed_money(Map<String, Object> paramMap);

	double app_query_defeated_money(Map<String, Object> paramMap);

	double app_queryPre_change_money(Map<String, Object> paramMap);
	
	double app_query_refund_money(Map<String, Object> paramMap);

	int app_query_ticket_count(Map<String, Object> paramMap);

	int app_over_time(Map<String, Object> paramMap);

	int app_queryPre_bx_count(Map<String, Object> paramMap);

	double app_queryPre_bx_countMoney10(Map<String, Object> paramMap);

	double app_queryPre_bx_countMoney20(Map<String, Object> paramMap);
	
	//ccb统计
	int ccb_query_day_out_ticket_succeed(Map<String, Object> paramMap);

	int ccb_query_day_out_ticket_defeated(Map<String, Object> paramMap);

	int ccb_query_day_order_count(Map<String, Object> paramMap);

	int ccb_query_refund_count(Map<String, Object> paramMap);

	double ccb_query_defeated_money(Map<String, Object> paramMap);

	double ccb_query_succeed_money(Map<String, Object> paramMap);
	
	double ccb_query_refund_money(Map<String, Object> paramMap);

	int ccb_query_ticket_count(Map<String, Object> paramMap);

	int queryPre_over_time(Map<String, Object> paramMap);

	int qunar_over_time(Map<String, Object> paramMap);

	int pay_over_time(Map<String, Object> paramMap);

	int ccb_over_time(Map<String, Object> paramMap);

	int ccb_queryPre_preparative_count(Map<String, Object> paramMap);

	int ccb_queryPre_pay_defeated(Map<String, Object> paramMap);

	double ccb_queryPre_change_money(Map<String, Object> paramMap);

	int ccb_queryPre_bx_count(Map<String, Object> paramMap);

	double ccb_queryPre_bx_countMoney10(Map<String, Object> paramMap);

	double ccb_queryPre_bx_countMoney20(Map<String, Object> paramMap);
	
	//chq统计
	int chq_query_day_out_ticket_succeed(Map<String, Object> paramMap);

	int chq_query_day_out_ticket_defeated(Map<String, Object> paramMap);

	int chq_query_day_order_count(Map<String, Object> paramMap);

	int chq_query_refund_count(Map<String, Object> paramMap);

	double chq_query_defeated_money(Map<String, Object> paramMap);

	double chq_query_succeed_money(Map<String, Object> paramMap);
	
	double chq_query_refund_money(Map<String, Object> paramMap);

	int chq_query_ticket_count(Map<String, Object> paramMap);

	int chq_over_time(Map<String, Object> paramMap);

	int chq_queryPre_preparative_count(Map<String, Object> paramMap);

	int chq_queryPre_pay_defeated(Map<String, Object> paramMap);

	double chq_queryPre_change_money(Map<String, Object> paramMap);

	int chq_queryPre_bx_count(Map<String, Object> paramMap);

	double chq_queryPre_bx_countMoney10(Map<String, Object> paramMap);

	double chq_queryPre_bx_countMoney20(Map<String, Object> paramMap);
	
	//对外商户ext统计
	List<Map<String, String>> queryMerchantId();//统计对外商户的商户id
	
	int ext_query_day_out_ticket_succeed(Map<String, Object> paramMap);

	int ext_query_day_out_ticket_defeated(Map<String, Object> paramMap);

	int ext_queryPre_preparative_count(Map<String, Object> paramMap);

	int ext_queryPre_pay_defeated(Map<String, Object> paramMap);

	int ext_query_day_order_count(Map<String, Object> paramMap);

	int ext_query_refund_count(Map<String, Object> paramMap);

	double ext_query_succeed_money(Map<String, Object> paramMap);

	double ext_query_defeated_money(Map<String, Object> paramMap);

	double ext_queryPre_change_money(Map<String, Object> paramMap);
	
	double ext_query_refund_money(Map<String, Object> paramMap);

	int ext_query_ticket_count(Map<String, Object> paramMap);

	int ext_over_time(Map<String, Object> paramMap);

	int ext_queryPre_bx_count(Map<String, Object> paramMap);
	
	//10元保险和20元保险的个数
	int ext_queryPre_bx_count10(Map<String, Object> paramMap);
	int ext_queryPre_bx_count20(Map<String, Object> paramMap);
	
	double ext_queryPre_bx_countMoney10(Map<String, Object> paramMap);

	double ext_queryPre_bx_countMoney20(Map<String, Object> paramMap);
	
	double queryExtOut_ticket_XL(Map<String, Object> extMap);
	
	double queryExtPay_time(Map<String, Object> extMap);
	//以下为艺龙的统计
	int elong_query_day_out_ticket_succeed(Map<String, Object> paramMap);
	
	int elong_query_day_out_ticket_num_succeed(Map<String, Object> paramMap);

	int elong_query_day_out_ticket_defeated(Map<String, Object> paramMap);

	int elong_query_day_order_count(Map<String, Object> paramMap);

	int elong_query_refund_count(Map<String, Object> paramMap);
	
	int elong_query_refund_cp_count(Map<String, Object> paramMap);

	double elong_query_defeated_money(Map<String, Object> paramMap);

	double elong_query_succeed_money(Map<String, Object> paramMap);
	
	double elong_query_refund_money(Map<String, Object> paramMap);

	int elong_query_ticket_count(Map<String, Object> paramMap);

	int elong_over_time(Map<String, Object> paramMap);
	
	int elong_out_time_order(Map<String, Object> paramMap);
	int elong_cancel_order(Map<String, Object> paramMap);
	
	
	//添加火车更新数据到统计表
	int query19eTodayCount(Map<String, Object> map);

	void updateToTj_Hc_orderInfo(Map<String, Object> map);

	int queryPre_svip_count(Map<String, Object> paramMap);

	int queryPre_svip_lose(Map<String, Object> paramMap);

	//利安出票效率和支付效率
	double queryExtOut_ticket_XL_lian(Map<String, Object> extMap);
	
	double queryExtPay_time_lian(Map<String, Object> extMap);

	//高铁管家
	int gt_query_day_out_ticket_succeed(Map<String, Object> paramMap);

	int gt_query_day_out_ticket_defeated(Map<String, Object> paramMap);

	int gt_queryPre_preparative_count(Map<String, Object> paramMap);

	int gt_queryPre_pay_defeated(Map<String, Object> paramMap);

	int gt_query_day_order_count(Map<String, Object> paramMap);

	int gt_query_refund_count(Map<String, Object> paramMap);

	double gt_query_succeed_money(Map<String, Object> paramMap);

	double gt_query_defeated_money(Map<String, Object> paramMap);

	double gt_queryPre_change_money(Map<String, Object> paramMap);
	
	double gt_query_refund_money(Map<String, Object> paramMap);

	int gt_query_ticket_count(Map<String, Object> paramMap);

	int gt_over_time(Map<String, Object> paramMap);

	int gt_queryPre_bx_count(Map<String, Object> paramMap);
	
	//携程
	int xc_query_day_out_ticket_succeed(Map<String, Object> paramMap);

	int xc_query_day_out_ticket_defeated(Map<String, Object> paramMap);

//	int xc_queryPre_preparative_count(Map<String, Object> paramMap);

//	int xc_queryPre_pay_defeated(Map<String, Object> paramMap);

	int xc_query_day_order_count(Map<String, Object> paramMap);

	int xc_query_refund_count(Map<String, Object> paramMap);

	double xc_query_succeed_money(Map<String, Object> paramMap);

	double xc_query_defeated_money(Map<String, Object> paramMap);

	double xc_queryPre_change_money(Map<String, Object> paramMap);
	
	double xc_query_refund_money(Map<String, Object> paramMap);

	int xc_query_ticket_count(Map<String, Object> paramMap);

//	int xc_over_time(Map<String, Object> paramMap);

	int xc_queryPre_bx_count(Map<String, Object> paramMap);

	//美团
	int meituan_query_day_out_ticket_succeed(Map<String, Object> paramMap);

//	int meituan_query_day_out_ticket_num_succeed(Map<String, Object> paramMap);

	int meituan_query_day_out_ticket_defeated(Map<String, Object> paramMap);

	int meituan_query_day_order_count(Map<String, Object> paramMap);

	int meituan_query_refund_count(Map<String, Object> paramMap);

//	int meituan_query_refund_cp_count(Map<String, Object> paramMap);

	double meituan_query_succeed_money(Map<String, Object> paramMap);

	double meituan_query_defeated_money(Map<String, Object> paramMap);

	double meituan_query_refund_money(Map<String, Object> paramMap);

	int meituan_query_ticket_count(Map<String, Object> paramMap);

	int meituan_over_time(Map<String, Object> paramMap);

	int meituan_out_time_order(Map<String, Object> paramMap);

	int meituan_cancel_order(Map<String, Object> paramMap);

	//途牛
	int tuniu_query_day_out_ticket_succeed(Map<String, Object> paramMap);

	int tuniu_query_day_out_ticket_defeated(Map<String, Object> paramMap);

	int tuniu_query_day_order_count(Map<String, Object> paramMap);

	int tuniu_query_refund_count(Map<String, Object> paramMap);

	double tuniu_query_succeed_money(Map<String, Object> paramMap);

	double tuniu_query_defeated_money(Map<String, Object> paramMap);

	double tuniu_query_refund_money(Map<String, Object> paramMap);

	int tuniu_query_ticket_count(Map<String, Object> paramMap);

	int tuniu_over_time(Map<String, Object> paramMap);

	int tuniu_out_time_order(Map<String, Object> paramMap);

	int tuniu_cancel_order(Map<String, Object> paramMap);

}
