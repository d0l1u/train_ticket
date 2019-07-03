package com.l9e.transaction.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.l9e.common.TrainConsts;
import com.l9e.transaction.dao.CommonDao;
import com.l9e.transaction.dao.ExtShijiDao;
import com.l9e.transaction.service.ExtShijiService;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.transaction.vo.OrderInfoBx;
import com.l9e.transaction.vo.OrderInfoCp;
import com.l9e.transaction.vo.OrderInfoPs;
import com.l9e.transaction.vo.RefundVo;
import com.l9e.util.AmountUtil;
@Service("extShijiService")
public class ExtShijiServiceImpl implements ExtShijiService {
	
	private static final Logger logger = Logger.getLogger(ExtShijiServiceImpl.class);

	@Resource
	private ExtShijiDao extShijiDao;
	@Resource
	private CommonDao commonDao;
	
	public OrderInfo queryOrderInfo(String order_id) {
		return extShijiDao.queryOrderInfo(order_id);
	}
	
	public OrderInfoPs queryOrderInfoPs(String order_id) {
		return extShijiDao.queryOrderInfoPs(order_id);
	}
	
	public List<Map<String, String>> queryOrderDetailList(String order_id) {
		return extShijiDao.queryOrderDetailList(order_id);
	}
	
	public List<OrderInfo> queryOrderList(Map<String, Object> paramMap) {
		return extShijiDao.queryOrderList(paramMap);
	}
	
	public int queryOrderListCount(Map<String, Object> paramMap) {
		return extShijiDao.queryOrderListCount(paramMap);
	}
	
	public List<Map<String, String>> queryRefundStreamList(String order_id) {
		return extShijiDao.queryRefundStreamList(order_id);
	}
	
	public Map<String, String> querySpecTimeBeforeFrom(String order_id) {
		return extShijiDao.querySpecTimeBeforeFrom(order_id);
	}
	
	@Override
	public Map<String, Object> queryAgentOrderNum(Map<String, Object> map) {
		//拼接出票中，出票失败，出票成功和退款的数据
		Map<String,Object> trainMap=extShijiDao.queryAgentOrderNum(map);
		Map<String,Object> refundMap=extShijiDao.queryAgentRefundNum(map);
		if(refundMap.get("refundNum")==null){
			//避免空指针异常
		}else{
			trainMap.put("refundNum", refundMap.get("refundNum"));
		}
		return trainMap;
	}
	
	@Override
	public String selectRefundPassengers(String cp_id) {
		return extShijiDao.selectRefundPassengers(cp_id);
	}
	
	@Override
	public List<OrderInfo> queryOrderRefundList(Map<String, Object> paramMap) {
		return extShijiDao.queryOrderRefundList(paramMap);
	}

	@Override
	public List<OrderInfo> queryExtAccountOrderList(Map<String, Object> paramMap) {
		return extShijiDao.queryExtAccountOrderList(paramMap);
	}

	@Override
	public int queryExtAccountOrderListCount(Map<String, Object> paramMap) {
		return extShijiDao.queryExtAccountOrderListCount(paramMap);
	}

	@Override
	public List<Map<String, String>> queryRefundList(String orderId) {
		return extShijiDao.queryRefundList(orderId);
	}

	@Override
	public List<OrderInfo> queryExtAccountOrderExcelList(Map<String, Object> paramMap) {
		return extShijiDao.queryExtAccountOrderExcelList(paramMap);
	}

}
