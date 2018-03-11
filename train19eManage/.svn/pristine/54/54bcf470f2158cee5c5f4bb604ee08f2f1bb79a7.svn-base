package com.l9e.transaction.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.HcStatDao;
import com.l9e.transaction.service.HcStatService;

@Service("hcStatService")
public class HcStatServiceImpl implements HcStatService{
	@Resource
	private HcStatDao  hcStatDao ;

	public int queryHcStatCount(Map<String, Object> query_Map) {
		return hcStatDao.queryHcStatCount(query_Map);
	}

	public List<Map<String, Object>> queryHcStatList(
			Map<String, Object> query_Map) {
		return hcStatDao.queryHcStatList(query_Map);
	}
	
	public List<Map<String, String>> queryHcStatExcel(
			Map<String, Object> query_Map) {
		return hcStatDao.queryHcStatExcel(query_Map);
	}

	public int querytongjiList(
			Map<String, Object> query_Map) {
		return hcStatDao.querytongjiList(query_Map);
	}
	
	public int querytongjiListQunar(
			Map<String, Object> query_Map) {
		return hcStatDao.querytongjiListQunar(query_Map);
	}
	public int querytongjiListInner(
			Map<String, Object> query_Map) {
		return hcStatDao.querytongjiListInner(query_Map);
	}
	public int querytongjiListExt(
			Map<String, Object> query_Map) {
		return hcStatDao.querytongjiListExt(query_Map);
	}
	
	public int queryActiveUser(String order_time) {
		return hcStatDao.queryActiveUser(order_time);
	}

	public List<Map<String, String>> queryPictureLineParam() {
		return hcStatDao.queryPictureLineParam();
	}

	public List<Map<String, Object>> queryHc_statInfo_provinceList(
			String create_time) {
		return hcStatDao.queryHc_statInfo_provinceList(create_time);
	}

	public Map<String,String> queryProvinceTotal_Ticket(String province_id , String create_time) {
		Map<String,String>query_Map = new HashMap<String,String>();
		query_Map.put("province_id", province_id);
		query_Map.put("create_time", create_time);
		return hcStatDao.queryProvinceTotal_Ticket(query_Map);
	}
	
	public Map<String, String> querySupervise_nameToArea_no(String area_name) {
		return hcStatDao.querySupervise_nameToArea_no(area_name);
	}

	public int querySuperviseAreaCount(Map<String, Object> paramMap) {
		return hcStatDao.querySuperviseAreaCount(paramMap);
	}

	public List<Map<String, Object>> querySuperviseAreaList(
			Map<String, Object> paramMap) {
		return hcStatDao.querySuperviseAreaList(paramMap);
	}

	public int queryVipLose(String order_time) {
		return hcStatDao.queryVipLose(order_time);
	}

	public Map<String, String> queryHistory() {
		return hcStatDao.queryHistory();
	}

	public Map<String, Object> queryYesterdayTicketMoney(String createTime) {
		return hcStatDao.queryYesterdayTicketMoney(createTime);
	}

	
	public int check_num(Map<String, Object> map){
		return hcStatDao.check_num(map);
	}

	public int success_num(Map<String, Object> map){
		return hcStatDao.success_num(map);
	}
	public int fail_num(Map<String, Object> map){
		return hcStatDao.fail_num(map);
	}

	public int order_num_elong(Map<String, Object> map){
		return hcStatDao.order_num_elong(map);
	}
	
	public int order_num_qunar(Map<String, Object> map){
		return hcStatDao.order_num_qunar(map);
	}

	@Override
	public int add_user_num(Map<String, Object> map) {
		return hcStatDao.add_user_num(map);
	}

	@Override
	public int regist_num(Map<String, Object> map) {
		return hcStatDao.regist_num(map);
	}

	@Override
	public int querytongjiListExtOrder(Map<String, Object> queryMap) {
		return hcStatDao.querytongjiListExtOrder(queryMap);
	}

	@Override
	public int querytongjiListInnerOrder(Map<String, Object> queryMap) {
		return hcStatDao.querytongjiListInnerOrder(queryMap);
	}

	@Override
	public int querytongjiListOrder(Map<String, Object> queryMap) {
		return hcStatDao.querytongjiListOrder(queryMap);
	}

	@Override
	public int querytongjiListQunarOrder(Map<String, Object> queryMap) {
		return hcStatDao.querytongjiListQunarOrder(queryMap);
	}

	
	//在此新增查询同程和美团订单数接口 
	@Override
	public int order_num_meituan(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return hcStatDao.order_num_meituan(map);
	}

	@Override
	public int order_num_tongcheng(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return hcStatDao.order_num_tongcheng(map);
	}

	@Override
	public List<Map<String, Object>> queryTjCode(String date) {
		
		return hcStatDao.queryTjCode(date);
	}

	@Override
	public int order_num_gt(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return hcStatDao.order_num_gt(map);
	}

}
