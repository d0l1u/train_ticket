package com.l9e.transaction.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.XcRefundDao;
import com.l9e.transaction.service.XcRefundService;
@Service("xcRefundService")
public class XcRefundServiceImpl implements XcRefundService {
	@Resource
	private XcRefundDao xcRefundDao ;

	public int queryRefundTicketCount(Map<String, Object> paramMap) {
		return xcRefundDao.queryRefundTicketCount(paramMap);
	}

	public List<Map<String, String>> queryRefundTicketList(
			Map<String, Object> paramMap) {
		return xcRefundDao.queryRefundTicketList(paramMap);
	}

	public List<Map<String, String>> queryRefundTicketInfo(Map<String,String>id_Map) {
		return xcRefundDao.queryRefundTicketInfo(id_Map);
	}

	public List<Map<String, String>> queryRefundTicketOrderInfoCp(String cp_id) {
		return xcRefundDao.queryRefundTicketOrderInfoCp(cp_id);
	}
	
	public void queryrefundTicketAdd(Map<String, String> log_Map,Map<String, Object> mapAdd) {
		xcRefundDao.addOrderRefundTicket_log(log_Map);//增加操作日志
		xcRefundDao.queryrefundTicketAdd(mapAdd);//增加差额退款，向xc_orderinfo_refundstream表添加数据
		//根据order_id查询eop_order_id和eop_refund_url
		List<Map<String, String>> orderList = xcRefundDao.queryOrderInfo(mapAdd.get("order_id"));
		Map<String, String> orderMap = orderList.get(0);
		orderMap.put("refund_reason", mapAdd.get("user_remark").toString());
		xcRefundDao.addOrderRefundNotify(orderMap);//向xc_orderinfo_refundeopnotify表添加数据（平台通知表）
	}
	
	public void updateRefundTicket(Map<String, String> log_Map,
			Map<String, Object> refund_Map) {
		xcRefundDao.addCpRefundTicket_log(log_Map);
		xcRefundDao.updateRefundTicketInfo(refund_Map);
	}

	public List<Map<String, Object>> queryHistroyByCpId(String cp_id) {
		return xcRefundDao.queryHistroyByCpId(cp_id);
	}

	public List<Map<String, Object>> queryHistroyByOrderId(String order_id) {
		return xcRefundDao.queryHistroyByOrderId(order_id);
	}
	
	public Map<String, String> queryBuymoneyAndTicketpaymoney(String order_id) {
		Map<String,String>money_Map = new HashMap<String,String>();
		String sumRefundMoney = xcRefundDao.querySumRefundMoney(order_id);
		String pay_money = xcRefundDao.queryBuymoneyAndTicketpaymoney(order_id);
		money_Map.put("sumRefundMoney", sumRefundMoney);
		money_Map.put("pay_money", pay_money);
		return money_Map;
	}
	
	public Map<String, String> queryRefundMoney(String order_id,String stream_id) {
		Map<String,String>money_Map = new HashMap<String,String>();
		String sumRefundMoney = xcRefundDao.querySumRefundMoney(order_id);
		String pay_money = xcRefundDao.queryBuymoneyAndTicketpaymoney(order_id);
		String refund_money =xcRefundDao.queryRefundMoney(stream_id);
		money_Map.put("sumRefundMoney", sumRefundMoney);
		money_Map.put("pay_money", pay_money);
		money_Map.put("refund_money", refund_money);
		return money_Map;
	}
	
	public void updateDifferRefund(Map<String, String> log_Map,
			Map<String, Object> differ_Map) {
		xcRefundDao.addOrderRefundTicket_log(log_Map);
		xcRefundDao.updateDifferRefund(differ_Map);
	}


	public void updateOut_Ticket_Refund(Map<String, String> log_Map,
			Map<String, Object> outTicket_Defeated_Map) {
		xcRefundDao.addCpRefundTicket_log(log_Map);
		xcRefundDao.updateOut_Ticket_Refund(outTicket_Defeated_Map);
//		xcRefundDao.updateOutTicketRefundEOP(outTicket_Defeated_Map);//更新xc_orderinfo_refundeopnotify表数据
	}

	public void updateRefreshNotice(Map<String, Object> update_RefreshNoticeMap) {
		xcRefundDao.updateRefreshNotice(update_RefreshNoticeMap);
	}

	public void addErrorLogInfo(Map<String, String> log_Map) {
		xcRefundDao.addCpRefundTicket_log(log_Map);
	}

	public String queryRefundTicketOrderId(String order_id) {
		return xcRefundDao.queryRefundTicketOrderId(order_id);
	}

	public String queryRefundTicketOrderIdExists(String orderId) {
		return xcRefundDao.queryRefundTicketOrderIdExists(orderId);
	}

	@Override
	public Map<String, String> queryBookOrderInfo(String orderId) {
		return xcRefundDao.queryBookOrderInfo(orderId);
	}

	@Override
	public List<Map<String, String>> queryBookOrderInfoBx(String orderId) {
		return xcRefundDao.queryBookOrderInfoBx(orderId);
	}

	@Override
	public List<Map<String, String>> queryBookOrderInfoCp(String orderId) {
		return xcRefundDao.queryBookOrderInfoCp(orderId);
	}
	
	//同意退款
	@Override
	public void updateRefundNotify(Map<String,Object>map,Map<String,String>log_Map) {
		xcRefundDao.updateRefundNotify(map);				 //更新xc_orderinfo_refundstream表的信息（同意退款）
		xcRefundDao.updateCpRefundStatusSuccess(map.get("cp_id").toString());//更新xc_orderinfo_cp表的信息
		int count = xcRefundDao.queryRefundCount(map.get("refund_seq").toString());//根据refund_seq查询xc_orderinfo_refundstream有几条退款记录
		if(count > 0){
			xcRefundDao.addCpRefundTicket_log(log_Map);		 //添加操作日志
		}else{
			xcRefundDao.updateXcRefundNotifyStatus(map);		 //更新xc_orderinfo_refundnotify表的信息（同意退款）
			xcRefundDao.addCpRefundTicket_log(log_Map);		 //添加操作日志
		}
	}

	//拒绝退款
	public void updateRefuseRefund(Map<String, String> log_Map, Map<String, Object> refuse_Map, Map<String,String> order_Map) {
		xcRefundDao.updateRefund_StatusTo55(refuse_Map);	//修改退款状态为拒绝退款同时备注,操作人和审核时间也会修改 xc_orderinfo_refundstream
		xcRefundDao.updateCpRefundStatusFail(refuse_Map);  //更新xc_orderinfo_cp表的信息
		int count = xcRefundDao.queryRefundCount(refuse_Map.get("refund_seq").toString());//根据refund_seq查询xc_orderinfo_refundstream有几条退款记录
		if(count > 0){
			xcRefundDao.addCpRefundTicket_log(log_Map);		//添加操作日志
		}else{
			xcRefundDao.addCpRefundTicket_log(log_Map);		//添加操作日志
			//xcRefundDao.deleteRefundNotify(refuse_Map);		//删除退款信息  xc_orderinfo_refundnotify
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("order_id", refuse_Map.get("order_id"));
			map.put("refund_seq", refuse_Map.get("refund_seq"));
			xcRefundDao.updateXcRefundNotifyStatus(map);		 //更新xc_orderinfo_refundnotify表的信息（同意退款）
			xcRefundDao.updateOrderInfo_can_refundTo1_And_refund_total(order_Map);//修改主表中can_refund为1与更新refund_total(xc_orderinfo)
		}
		
	}
	
	@Override
	public void updateGezhiRefund(Map<String, String> logMap,
			Map<String, Object> refuseMap) {
		xcRefundDao.addCpRefundTicket_log(logMap);//添加操作日志
		xcRefundDao.updateGezhiRefund(refuseMap);//搁置订单
	}
	
	@Override
	public List<Map<String, String>> queryXcMerchantinfo() {
		return xcRefundDao.queryXcMerchantinfo();
	}

	@Override
	public void updateXcRefundNotifyNum(Map<String, String> paramMap) {
		xcRefundDao.updateXcRefundNotifyNum(paramMap);
	}

	@Override
	public List<Map<String, Object>> queryRefundStationTicketAdd(
			Map<String, String> logMap, Map<String, Object> mapAdd) {
		xcRefundDao.addOrderRefundTicket_log(logMap);
		xcRefundDao.addRefundStationTicketNotify(mapAdd);
		return xcRefundDao.queryRefundStationTicketAdd(mapAdd);
	}

	@Override
	public String queryRefundStationTicketCpId(Map<String, Object> paramMap) {
		return xcRefundDao.queryRefundStationTicketCpId(paramMap);
	}

	@Override
	public String queryCpidIsRefund(Map<String, Object> paramMap) {
		return xcRefundDao.queryCpidIsRefund(paramMap);
	}

	@Override
	public String queryMerchantOrderId(String orderId) {
		return xcRefundDao.queryMerchantOrderId(orderId);
	}

	@Override
	public void insertLog(HashMap<String, Object> map) {
		xcRefundDao.insertLog(map);
	}

	@Override
	public void updateOrder(HashMap<String, Object> map) {
		xcRefundDao.updateOrder(map);
	}

	@Override
	public void updateOrderstatusToRobotGai(Map<String, String> updateMap) {
		xcRefundDao.updateOrderstatusToRobotGai(updateMap);
	}

	@Override
	public void updateRefundOpt(HashMap<String, Object> map) {
		xcRefundDao.updateRefundOpt(map);
	}

	@Override
	public int queryCpidIsRefundNum(Map<String, Object> pMap) {
		return xcRefundDao.queryCpidIsRefundNum(pMap);
	}

	@Override
	public String queryMerchantIdByOrderId(Map<String, Object> pMap) {
		return xcRefundDao.queryMerchantIdByOrderId(pMap);
	}

	@Override
	public Map<String, String> queryMoneyByCpId(Map<String, Object> pMap) {
		return xcRefundDao.queryMoneyByCpId(pMap);
	}

	@Override
	public String queryStatusByOrderId(Map<String, Object> pMap) {
		return xcRefundDao.queryStatusByOrderId(pMap);
	}

	@Override
	public Map<String, String> checkRefundMoney(Map<String, Object> paramMap) {
		Map<String,String>money_Map = new HashMap<String,String>();
		String sumRefundMoney = xcRefundDao.queryCpSumRefundMoney(paramMap);
		List<Map<String, String>> buy_moneyAndPaymoney = xcRefundDao.queryCpBuyAndPayMoney(paramMap);
		money_Map.put("sumRefundMoney", sumRefundMoney);
		if(buy_moneyAndPaymoney.size() != 0) {
			money_Map.put("pay_money", buy_moneyAndPaymoney.get(0).get("ticket_pay_money"));
			money_Map.put("buy_money", buy_moneyAndPaymoney.get(0).get("ticket_buy_money"));
		}
		return money_Map;
	}

	@Override
	public List<Map<String, String>> queryTicketCpId(Map<String, Object> paramMap) {
		return xcRefundDao.queryTicketCpId(paramMap);
	}



	
}
