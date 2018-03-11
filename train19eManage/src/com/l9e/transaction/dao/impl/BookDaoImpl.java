package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.BookDao;
import com.l9e.transaction.vo.AreaVo;
import com.l9e.transaction.vo.RefundVo;

@Repository("bookDao")
public class BookDaoImpl extends BaseDao implements BookDao {

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryBookList(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("book.queryBookList", paramMap);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryBookListOnRefundTime(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("book.queryBookListOnRefundTime",paramMap);
	}
	
	@SuppressWarnings("unchecked")
	public int queryBookListCount(Map<String, Object> paramMap) {
		return getTotalRows("book.queryBookListCount", paramMap);
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> queryBookOrderInfo(String order_id) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("book.queryBookOrderInfo", order_id);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryBookOrderInfoBx(String order_id) {
		return this.getSqlMapClientTemplate().queryForList("book.queryBookOrderInfoBx", order_id);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryBookOrderInfoCp(String order_id) {
		return this.getSqlMapClientTemplate().queryForList("book.queryBookOrderInfoCp", order_id);
	}

	@SuppressWarnings("unchecked")
	public void updateOrder(String orderid) {
		this.getSqlMapClientTemplate().update("book.updateOrderForRefunding", orderid);
	}

	@SuppressWarnings("unchecked")
	public void updateRefund(RefundVo refund) {
		this.getSqlMapClientTemplate().update("book.updateRefundForRefunding", refund);
	}
	@SuppressWarnings("unchecked")
	public void updateEndOpt_Ren(Map<String, String> userMap) {
		this.getSqlMapClientTemplate().update("book.updateEndOpt_Ren",userMap);
	}
	@SuppressWarnings("unchecked")
	public List<AreaVo> getProvince() {
		return this.getSqlMapClientTemplate().queryForList("book.getProvince");
	}
	@SuppressWarnings("unchecked")
	public List<AreaVo> getCity(String provinceid) {
		return this.getSqlMapClientTemplate().queryForList("book.getCity", provinceid);
	}
	@SuppressWarnings("unchecked")
	public List<AreaVo> getArea(String cityid) {
		return this.getSqlMapClientTemplate().queryForList("book.getArea", cityid);
	}

	public void addUserAccount(Map<String, String> paramMap) {
		this.getSqlMapClientTemplate().insert("book.addUserAccount",paramMap);
	}

	public void updateRefuseRefund_Agree(String order_id) {
		this.getSqlMapClientTemplate().update("book.updateRefuseRefund_Agree",order_id);
	}

	public void updateRefuseOrder_Status(String order_id) {
		this.getSqlMapClientTemplate().update("book.updateRefuseOrder_Status",order_id);
	}
	
	public void updateCancel_book_Status(Map<String, String> paramMap) {
		this.getSqlMapClientTemplate().update("book.updateCancel_book_Status",paramMap);
	}
	public void updateRefund_memo(Map<String,String>map) {
		this.getSqlMapClientTemplate().update("book.updateRefund_memo",map);
	}
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryHistroyByOrderId(String order_id) {
		return this.getSqlMapClientTemplate().queryForList("book.queryHistroyByOrderId",order_id);
	}
	public void updateDifferRefunding(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("book.updateDifferRefunding", map);
	}
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryAllExcel() {
		return this.getSqlMapClientTemplate().queryForList("book.queryAllExcel");
	}

	public void updateSwitch_ignore(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("book.updateSwitch_ignore",map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryOutTicket_info(String order_id) {
		return this.getSqlMapClientTemplate().queryForList("book.queryOutTicket_info",order_id);
	}

	@SuppressWarnings("unchecked") //省级负责人
	public Map<String,String> querySupervise_nameToArea_no(String area_name) {
		return (Map<String,String>)this.getSqlMapClientTemplate().queryForObject("book.querySupervise_nameToArea_no",area_name);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryNoAndName() {
		return this.getSqlMapClientTemplate().queryForList("book.queryNoAndName");
	}

}
