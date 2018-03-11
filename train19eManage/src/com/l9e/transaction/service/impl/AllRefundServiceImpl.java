package com.l9e.transaction.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.AllRefundDao;
import com.l9e.transaction.service.AllRefundService;


@Service("allRefundService")
public class AllRefundServiceImpl implements AllRefundService{
	@Resource
	private AllRefundDao allRefundDao ;

	@Override
	public List<Map<String, Object>> addAllRefund(Map<String, Object> mapAdd) {
		return allRefundDao.addAllRefund(mapAdd);
	}

	@Override
	public void addAllRefundlog(Map<String, String> log_Map) {
		allRefundDao.addAllRefundlog(log_Map);
	}

	@Override
	public int queryAllRefundCounts(Map<String, Object> paramMap) {
		return allRefundDao.queryAllRefundCounts(paramMap);
	}

	@Override
	public List<Map<String, String>> queryAllRefundInfo(
			Map<String, Object> paramMap) {
		return allRefundDao.queryAllRefundInfo(paramMap);
	}

	@Override
	public List<Map<String, String>> queryAllRefundList(
			Map<String, Object> paramMap) {
		return allRefundDao.queryAllRefundList(paramMap);
	}
	//导出excel
	@Override
	public List<Map<String, String>> queryAllRefundExcel(
			Map<String, Object> paramMap) {
		return allRefundDao.queryAllRefundExcel(paramMap);
	}

	@Override
	public List<Map<String, Object>> queryHistroy(Map<String, Object> refund_Map) {
		return allRefundDao.queryHistroy(refund_Map);
	}

	@Override
	public int updateAllRefundInfo(Map<String, Object> refund_Map) {
		return allRefundDao.updateAllRefundInfo(refund_Map);
	}

	@Override
	public void updateRefundOpt(HashMap<String, Object> map) {
		allRefundDao.updateRefundOpt(map);
	}

	@Override
	public void updateRefuse(HashMap<String, Object> map) {
		allRefundDao.updateRefuse(map);
		allRefundDao.updateOrderInfo_can_refundTo1_And_refund_total(map);
	}
	public Map<String, String> queryRefundMoney(Map<String, Object> paramMap) {
		Map<String,String>money_Map = new HashMap<String,String>();
		String sumRefundMoney = allRefundDao.querySumRefundMoney(paramMap);
		if("elong".equals(paramMap.get("channel")) ||"tongcheng".equals(paramMap.get("channel"))){
			String elongOrtongcheng=allRefundDao.queryelongOrtongcheng(paramMap);
			sumRefundMoney=String.valueOf(Double.valueOf(sumRefundMoney)+Double.valueOf(elongOrtongcheng));
		}
		if("meituan".equals(paramMap.get("channel"))){
			String meituan=allRefundDao.queryMeituan(paramMap);
			sumRefundMoney=String.valueOf(Double.valueOf(sumRefundMoney)+Double.valueOf(meituan));
		}
		if("301030".equals(paramMap.get("channel"))){
			String gaotie=allRefundDao.queryGaotie(paramMap);
			sumRefundMoney=String.valueOf(Double.valueOf(sumRefundMoney)+Double.valueOf(gaotie));
		}
		if("301031".equals(paramMap.get("channel"))){
			String xiecheng=allRefundDao.queryXiecheng(paramMap);
			sumRefundMoney=String.valueOf(Double.valueOf(sumRefundMoney)+Double.valueOf(xiecheng));
		}
		if("tuniu".equals(paramMap.get("channel"))){
			String tuniu=allRefundDao.queryTuniu(paramMap);
			sumRefundMoney=String.valueOf(Double.valueOf(sumRefundMoney)+Double.valueOf(tuniu));
		}
		
		if("19e".equals(paramMap.get("channel"))){
			String l9e=allRefundDao.queryl9e(paramMap);
			sumRefundMoney=String.valueOf(Double.valueOf(sumRefundMoney)+Double.valueOf(l9e));
		}
		String pay_money = allRefundDao.queryBuymoneyAndTicketpaymoney(paramMap);
		String refund_money =allRefundDao.queryRefundMoney(paramMap);
		money_Map.put("refund_money", refund_money);
		money_Map.put("sumRefundMoney", sumRefundMoney);
		money_Map.put("pay_money", pay_money);
		return money_Map;
	}
	//重新通知
	public int updateNotify_Again(Map<String, String> paramMap) {
		return allRefundDao.updateNotify_Again(paramMap);
		
	}
	@Override
	public void updateOrderstatusToRobotGai(Map<String, String> updateMap) {
		allRefundDao.updateOrderstatusToRobotGai(updateMap);
	}

	@Override
	public List<String> queryManualOrderList() {
		return allRefundDao.queryManualOrderList();
	}

	@Override
	public Map<String, String> queryAlterInfo(Map<String, String> map) {
		Map<String, String>  alterMap = new HashMap<String, String>();
		Map<String, String>	queryMap = allRefundDao.queryAlterInfo(map);
		if(!MapUtils.isEmpty(queryMap)){
			alterMap = queryMap;
		}else{
			alterMap.put("order_id", "XXX");
			alterMap.put("change_buy_money", "XXX");
			alterMap.put("cp_id", "XXX");
			alterMap.put("new_cp_id", "XXX");
		}
		List<Map<String, String>> refundList = allRefundDao.queryAllRefund(map);
		String all_refund = "0.00";
		if(refundList.size()>0){
			all_refund = refundList.toString();
		}
//		order_id,change_buy_money,cp_id,new_cp_id 
//		alterMap.put("order_id", String.valueOf(queryMap.get("order_id")));
//		alterMap.put("change_buy_money", String.valueOf(queryMap.get("change_buy_money")));
//		alterMap.put("cp_id", String.valueOf(queryMap.get("cp_id")));
//		alterMap.put("new_cp_id", String.valueOf(queryMap.get("new_cp_id")));
		alterMap.put("all_refund", all_refund);
		alterMap.put("cp_id_count",  String.valueOf(allRefundDao.queryCpIdCount(map)));
		return alterMap;
	}

	@Override
	public void updateAccountToManual(String accountName) {
		allRefundDao.updateAccountToManual(accountName);
	}
}
