package com.l9e.transaction.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface AllRefundService {

	// 获取按条件查询的订单数 
	public int queryAllRefundCounts(Map<String, Object> paramMap);
	//获取按条件查询所有的订单
	public List<Map<String, String>> queryAllRefundList(Map<String, Object> paramMap);
	//导出excel
	public List<Map<String, String>> queryAllRefundExcel(Map<String, Object> paramMap);
	//查询明细订单退款明细
	public List<Map<String, String>> queryAllRefundInfo(Map<String,Object> paramMap) ;
	//增加操作日志 
	public void addAllRefundlog(Map<String, String> log_Map) ;
	//执行退款
	public int updateAllRefundInfo(Map<String, Object> refund_Map);
	//查询退款日志
	public List<Map<String, Object>> queryHistroy(Map<String, Object> refund_Map)  ;
	//增加线下/差额退票
	public List<Map<String, Object>> addAllRefund(Map<String, Object> mapAdd) ;
	//更新refund表中操作人信息
	public void updateRefundOpt(HashMap<String, Object> map);
	//拒绝退款时修改状态
	public void updateRefuse(HashMap<String, Object> map) ;
	//限制金额
	Map<String, String> queryRefundMoney(Map<String, Object> paramMap);
	//重新通知
	public int updateNotify_Again(Map<String,String> paramMap);
	//机器改签，机器退票，搁置订单
	public void updateOrderstatusToRobotGai(Map<String, String> updateMap);
	//查询人工退票订单号
	public List<String> queryManualOrderList();
	//查询改签单明细
	public Map<String, String> queryAlterInfo(Map<String, String> map);
	public void updateAccountToManual(String accountName);
	
}