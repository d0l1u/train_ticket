package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.AreaVo;
import com.l9e.transaction.vo.RefundVo;

public interface BookService {

	List<Map<String, String>> queryBookList(Map<String, Object> paramMap);

	int queryBookListCount(Map<String, Object> paramMap);

	Map<String, String> queryBookOrderInfo(String order_id);

	List<Map<String, String>> queryBookOrderInfoBx(String order_id);

	List<Map<String, String>> queryBookOrderInfoCp(String order_id);
	
	
	void refunding(RefundVo refund,Map<String,String>userMap);
	
	void addUserAccount(Map<String, String> paramMap);
	
	void updateRefuseRefund(String order_id,Map<String,String>userMap);
	
	void updateCancel_book(Map<String,String>paramMap);
	
	
	void updateRefund_memo(Map<String, String> map);
	
	List<Map<String, Object>> queryHistroyByOrderId(String order_id);
	
	//差额退款
	void updateDifferRefunding(Map<String, String> map);

	/**
	 * 获取省份
	 * @return
	 */
	public List<AreaVo> getProvince();
	
	
	/**
	 * 获取城市
	 * @return
	 */
	public List<AreaVo>  getCity(String provinceid);
	
	
	/**
	 * 获取区县
	 * @return
	 */
	public List<AreaVo>  getArea(String cityid);

	List<Map<String, String>> queryAllExcel();
	//切换无视截止时间
	void updateSwitch_ignore(Map<String, String> map);

	List<Map<String, Object>> queryOutTicket_info(String order_id);
	//省级负责人
	Map<String, String> querySupervise_nameToArea_no(String area_name);

	List<Map<String, String>> queryNoAndName();

	

	
}
