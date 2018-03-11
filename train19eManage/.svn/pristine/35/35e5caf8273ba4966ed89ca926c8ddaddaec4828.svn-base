package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

public interface HcStatService {

	int queryHcStatCount(Map<String, Object> query_Map);

	List<Map<String, Object>> queryHcStatList(Map<String, Object> query_Map);
	
	List<Map<String, String>> queryHcStatExcel(Map<String, Object> query_Map);

	//统计票数
	int querytongjiList(Map<String, Object> query_Map);
	int querytongjiListQunar(Map<String, Object> query_Map);
	int querytongjiListInner(Map<String, Object> query_Map);
	int querytongjiListExt(Map<String, Object> query_Map);

	//统计订单数
	int querytongjiListOrder(Map<String, Object> query_Map);
	int querytongjiListQunarOrder(Map<String, Object> query_Map);
	int querytongjiListInnerOrder(Map<String, Object> query_Map);
	int querytongjiListExtOrder(Map<String, Object> query_Map);

	
	int queryActiveUser(String order_time);

	List<Map<String, String>> queryPictureLineParam();

	List<Map<String, Object>> queryHc_statInfo_provinceList(String create_time);

	Map<String,String> queryProvinceTotal_Ticket(String province_id, String create_time);
	
	Map<String, String> querySupervise_nameToArea_no(String area_name);

	int querySuperviseAreaCount(Map<String, Object> paramMap);

	List<Map<String, Object>> querySuperviseAreaList(
			Map<String, Object> paramMap);

	int queryVipLose(String order_time);

	Map<String, String> queryHistory();

	Map<String, Object> queryYesterdayTicketMoney(String createTime);


	int check_num(Map<String, Object> map);

	int success_num(Map<String, Object> map);

	int fail_num(Map<String, Object> map);

	int order_num_elong(Map<String, Object> map);
	
	int order_num_qunar(Map<String, Object> map);
	
	int add_user_num(Map<String, Object> map);
	
	int regist_num(Map<String, Object> map);
	
	//在此新增查询同程和美团订单数接口 
	int order_num_tongcheng(Map<String, Object> map);
	
	int order_num_meituan(Map<String, Object> map);
	
	/**
	 * 查询高铁管家渠道的订单数
	 * @param map
	 * @return
	 */
	int order_num_gt(Map<String, Object> map);
	/**
	 * 查询支付宝发送验证码数量
	 * @param date
	 * @return
	 */
	List<Map<String,Object>> queryTjCode(String date);
	
}
