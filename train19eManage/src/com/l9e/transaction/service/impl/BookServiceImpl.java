package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.BookDao;
import com.l9e.transaction.service.BookService;
import com.l9e.transaction.vo.AreaVo;
import com.l9e.transaction.vo.RefundVo;

@Service("bookService")
public class BookServiceImpl implements BookService {
	
	@Resource
	private BookDao bookDao;
	
	public List<Map<String, String>> queryBookList(Map<String, Object> paramMap) {
			return bookDao.queryBookList(paramMap);
		}

	public int queryBookListCount(Map<String, Object> paramMap) {
		return bookDao.queryBookListCount(paramMap);
	}

	public Map<String, String> queryBookOrderInfo(String order_id) {
		return bookDao.queryBookOrderInfo(order_id);
	}

	public List<Map<String, String>> queryBookOrderInfoBx(String order_id) {
		return bookDao.queryBookOrderInfoBx(order_id);
	}

	public List<Map<String, String>> queryBookOrderInfoCp(String order_id) {
		return bookDao.queryBookOrderInfoCp(order_id);
	}


	public void refunding(RefundVo refund,Map<String,String>userMap) {
		bookDao.updateOrder(refund.getOrder_id());
		//更新预定退款为退款中
		bookDao.updateRefund(refund);
		
		bookDao.updateEndOpt_Ren(userMap);
	}
	
	public List<AreaVo> getProvince() {
		return bookDao.getProvince();
	}

	public List<AreaVo> getCity(String provinceid) {
		return bookDao.getCity(provinceid);
	}

	public List<AreaVo> getArea(String cityid) {
		return bookDao.getArea(cityid);
	}

	public void addUserAccount(Map<String, String> paramMap) {
		bookDao.addUserAccount(paramMap);
	}

	public void updateRefuseRefund(String order_id,Map<String,String>userMap) {
		//修改拒绝退款中的hc_orderinfo_refund表中的refund_status为 88:拒绝退款
		bookDao.updateRefuseRefund_Agree(order_id);
	
		//修改订单状态为
		bookDao.updateRefuseOrder_Status(order_id);
		//修改主表中的操作人
		bookDao.updateEndOpt_Ren(userMap);
	}
	
	public void updateCancel_book(Map<String,String>paramMap) {
		//修改订单状态为
		bookDao.updateCancel_book_Status(paramMap);
		
	}

	public void updateRefund_memo(Map<String,String>map) {
		bookDao.updateRefund_memo(map);
	}

	public List<Map<String, Object>> queryHistroyByOrderId(String order_id) {
		return bookDao.queryHistroyByOrderId(order_id);
	}

	public void updateDifferRefunding(Map<String, String> map) {
		//差额退款（状态，退款金额，更新时间，操作人）
		bookDao.updateDifferRefunding(map);
		//添加操作日志
		bookDao.addUserAccount(map);
		//更新主表操作人
		String opt_ren = map.get("user");
		map.put("opt_ren", opt_ren);
		bookDao.updateEndOpt_Ren(map);
	}

	public List<Map<String, String>> queryAllExcel() {
		return bookDao.queryAllExcel();
	}

	public void updateSwitch_ignore(Map<String, String> map) {
		bookDao.updateSwitch_ignore(map);
	}

	public List<Map<String, Object>> queryOutTicket_info(String order_id) {
		return bookDao.queryOutTicket_info(order_id);
	}
	//省级负责人
	public Map<String,String> querySupervise_nameToArea_no(String area_name) {
		return bookDao.querySupervise_nameToArea_no(area_name);
	}

	@Override
	public List<Map<String, String>> queryNoAndName() {
		return bookDao.queryNoAndName();
	}


}
