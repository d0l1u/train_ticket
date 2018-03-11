package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.AreaVo;
import com.l9e.transaction.vo.RefundVo;

public interface BookDao {

	List<Map<String, String>> queryBookList(Map<String, Object> paramMap);
	
	List<Map<String, String>> queryBookListOnRefundTime(
			Map<String, Object> paramMap);

	int queryBookListCount(Map<String, Object> paramMap);

	Map<String, String> queryBookOrderInfo(String order_id);

	List<Map<String, String>> queryBookOrderInfoBx(String order_id);

	List<Map<String, String>> queryBookOrderInfoCp(String order_id);
	
	
	void updateOrder(String orderid);
	
	void updateRefund(RefundVo refund);

	void updateEndOpt_Ren(Map<String, String> userMap);
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

	void addUserAccount(Map<String, String> paramMap);

	void updateRefuseRefund_Agree(String order_id);

	void updateRefuseOrder_Status(String order_id);
	
	void updateCancel_book_Status(Map<String, String> paramMap);
	

	void updateRefund_memo(Map<String, String> map);

	List<Map<String, Object>> queryHistroyByOrderId(String order_id);
	//差额退款
	void updateDifferRefunding(Map<String, String> map);

	List<Map<String, String>> queryAllExcel();

	void updateSwitch_ignore(Map<String, String> map);

	List<Map<String, Object>> queryOutTicket_info(String order_id);

	//省级负责人获取
	Map<String,String> querySupervise_nameToArea_no(String area_name);

	List<Map<String, String>> queryNoAndName();





}
