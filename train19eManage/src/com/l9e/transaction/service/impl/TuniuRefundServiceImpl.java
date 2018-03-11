package com.l9e.transaction.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.TuniuRefundDao;
import com.l9e.transaction.service.TuniuRefundService;
@Service("tuniuRefundService")
public class TuniuRefundServiceImpl implements TuniuRefundService {
	@Resource
	private TuniuRefundDao tuniuRefundDao ;

	public int queryRefundTicketCount(Map<String, Object> paramMap) {
		return tuniuRefundDao.queryRefundTicketCount(paramMap);
	}

	public List<Map<String, String>> queryRefundTicketList(
			Map<String, Object> paramMap) {
		return tuniuRefundDao.queryRefundTicketList(paramMap);
	}

	public List<Map<String, String>> queryRefundTicketInfo(Map<String,String>id_Map) {
		return tuniuRefundDao.queryRefundTicketInfo(id_Map);
	}

	public List<Map<String, String>> queryRefundTicketOrderInfoCp(String cp_id) {
		return tuniuRefundDao.queryRefundTicketOrderInfoCp(cp_id);
	}
	
	public void queryrefundTicketAdd(Map<String, String> log_Map,Map<String, Object> mapAdd) {
		tuniuRefundDao.addOrderRefundTicket_log(log_Map);//增加操作日志
		tuniuRefundDao.queryrefundTicketAdd(mapAdd);//增加差额退款，向tuniu_orderinfo_refundstream表添加数据
		//根据order_id查询eop_order_id和eop_refund_url
		List<Map<String, String>> orderList = tuniuRefundDao.queryOrderInfo(mapAdd.get("order_id"));
		Map<String, String> orderMap = orderList.get(0);
		orderMap.put("refund_reason", mapAdd.get("user_remark").toString());
		tuniuRefundDao.addOrderRefundNotify(orderMap);//向tuniu_orderinfo_refundeopnotify表添加数据（平台通知表）
	}
	
	public void updateRefundTicket(Map<String, String> log_Map,
			Map<String, Object> refund_Map) {
		tuniuRefundDao.addCpRefundTicket_log(log_Map);
		tuniuRefundDao.updateRefundTicketInfo(refund_Map);
	}

	public List<Map<String, Object>> queryHistroyByCpId(String cp_id) {
		return tuniuRefundDao.queryHistroyByCpId(cp_id);
	}

	public List<Map<String, Object>> queryHistroyByOrderId(String order_id) {
		return tuniuRefundDao.queryHistroyByOrderId(order_id);
	}
	
	public Map<String, String> queryBuymoneyAndTicketpaymoney(String order_id) {
		Map<String,String>money_Map = new HashMap<String,String>();
		String sumRefundMoney = tuniuRefundDao.querySumRefundMoney(order_id);
		String pay_money = tuniuRefundDao.queryBuymoneyAndTicketpaymoney(order_id);
		money_Map.put("sumRefundMoney", sumRefundMoney);
		money_Map.put("pay_money", pay_money);
		return money_Map;
	}
	
	public Map<String, String> queryRefundMoney(Map<String, Object> paramMap,String stream_id) {
		Map<String,String>money_Map = new HashMap<String,String>();
		String sumRefundMoney = tuniuRefundDao.queryCpSumRefundMoney(paramMap);
		List<Map<String, String>> buy_moneyAndPaymoney = tuniuRefundDao.queryCpBuyAndPayMoney(paramMap);
		if(!stream_id.isEmpty()){
		String refund_money =tuniuRefundDao.queryRefundMoney(stream_id);
		money_Map.put("refund_money", refund_money);
		}
		money_Map.put("sumRefundMoney", sumRefundMoney);
		if(buy_moneyAndPaymoney.size() != 0) {
			money_Map.put("pay_money", buy_moneyAndPaymoney.get(0).get("ticket_pay_money"));
			money_Map.put("buy_money", buy_moneyAndPaymoney.get(0).get("ticket_buy_money"));
		}
	
		return money_Map;
	}
	
	public void updateDifferRefund(Map<String, String> log_Map,
			Map<String, Object> differ_Map) {
		tuniuRefundDao.addOrderRefundTicket_log(log_Map);
		tuniuRefundDao.updateDifferRefund(differ_Map);
	}


	public void updateOut_Ticket_Refund(Map<String, String> log_Map,
			Map<String, Object> outTicket_Defeated_Map) {
		tuniuRefundDao.addCpRefundTicket_log(log_Map);
		tuniuRefundDao.updateOut_Ticket_Refund(outTicket_Defeated_Map);
//		tuniuRefundDao.updateOutTicketRefundEOP(outTicket_Defeated_Map);//更新tuniu_orderinfo_refundeopnotify表数据
	}

	public void updateRefreshNotice(Map<String, Object> update_RefreshNoticeMap) {
		tuniuRefundDao.updateRefreshNotice(update_RefreshNoticeMap);
	}

	public void addErrorLogInfo(Map<String, String> log_Map) {
		tuniuRefundDao.addCpRefundTicket_log(log_Map);
	}

	public String queryRefundTicketOrderId(String order_id) {
		return tuniuRefundDao.queryRefundTicketOrderId(order_id);
	}

	public String queryRefundTicketOrderIdExists(String orderId) {
		return tuniuRefundDao.queryRefundTicketOrderIdExists(orderId);
	}

	@Override
	public Map<String, String> queryBookOrderInfo(String orderId) {
		return tuniuRefundDao.queryBookOrderInfo(orderId);
	}

	@Override
	public List<Map<String, String>> queryBookOrderInfoBx(String orderId) {
		return tuniuRefundDao.queryBookOrderInfoBx(orderId);
	}

	@Override
	public List<Map<String, String>> queryBookOrderInfoCp(String orderId) {
		return tuniuRefundDao.queryBookOrderInfoCp(orderId);
	}
	
	//同意退款
	@Override
	public void updateRefundNotify(Map<String,Object>map,Map<String,String>log_Map) {
		tuniuRefundDao.updateRefundNotify(map);				 //更新tuniu_orderinfo_refundstream表的信息（同意退款）
		tuniuRefundDao.updateCpRefundStatusSuccess(map.get("cp_id").toString());//更新tuniu_orderinfo_cp表的信息
		int count = tuniuRefundDao.queryRefundCount(map.get("refund_seq").toString());//根据refund_seq查询tuniu_orderinfo_refundstream有几条退款记录
		if(count > 0){
			tuniuRefundDao.addCpRefundTicket_log(log_Map);		 //添加操作日志
		}else{
			tuniuRefundDao.updateTuniuRefundNotifyStatus(map);		 //更新tuniu_orderinfo_refundnotify表的信息（同意退款）
			tuniuRefundDao.addCpRefundTicket_log(log_Map);		 //添加操作日志
		}
	}

	//拒绝退款
	public void updateRefuseRefund(Map<String, String> log_Map, Map<String, Object> refuse_Map, Map<String,String> order_Map) {
		tuniuRefundDao.updateRefund_StatusTo55(refuse_Map);	//修改退款状态为拒绝退款同时备注,操作人和审核时间也会修改 tuniu_orderinfo_refundstream
		tuniuRefundDao.updateCpRefundStatusFail(refuse_Map);  //更新tuniu_orderinfo_cp表的信息
		int count = tuniuRefundDao.queryRefundCount(refuse_Map.get("refund_seq").toString());//根据refund_seq查询tuniu_orderinfo_refundstream有几条退款记录
		if(count > 0){
			tuniuRefundDao.addCpRefundTicket_log(log_Map);		//添加操作日志
		}else{
			tuniuRefundDao.addCpRefundTicket_log(log_Map);		//添加操作日志
			//tuniuRefundDao.deleteRefundNotify(refuse_Map);		//删除退款信息  tuniu_orderinfo_refundnotify
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("order_id", refuse_Map.get("order_id"));
			map.put("refund_seq", refuse_Map.get("refund_seq"));
			tuniuRefundDao.updateTuniuRefundNotifyStatus(map);		 //更新tuniu_orderinfo_refundnotify表的信息（同意退款）
			tuniuRefundDao.updateOrderInfo_can_refundTo1_And_refund_total(order_Map);//修改主表中can_refund为1与更新refund_total(tuniu_orderinfo)
		}
		
	}
	
	@Override
	public void updateGezhiRefund(Map<String, String> logMap,
			Map<String, Object> refuseMap) {
		tuniuRefundDao.addCpRefundTicket_log(logMap);//添加操作日志
		tuniuRefundDao.updateGezhiRefund(refuseMap);//搁置订单
	}
	
	@Override
	public List<Map<String, String>> queryTuniuMerchantinfo() {
		return tuniuRefundDao.queryTuniuMerchantinfo();
	}

	@Override
	public void updateTuniuRefundNotifyNum(Map<String, String> paramMap) {
		tuniuRefundDao.updateTuniuRefundNotifyNum(paramMap);
	}

	@Override
	public List<Map<String, Object>> queryRefundStationTicketAdd(
			Map<String, String> logMap, Map<String, Object> mapAdd) {
		tuniuRefundDao.addOrderRefundTicket_log(logMap);
		tuniuRefundDao.addRefundStationTicketNotify(mapAdd);
		return null;//tuniuRefundDao.queryRefundStationTicketAdd(mapAdd);
	}

	@Override
	public String queryRefundStationTicketCpId(Map<String, Object> paramMap) {
		return tuniuRefundDao.queryRefundStationTicketCpId(paramMap);
	}

	@Override
	public String queryCpidIsRefund(Map<String, Object> paramMap) {
		return tuniuRefundDao.queryCpidIsRefund(paramMap);
	}

	@Override
	public String queryMerchantOrderId(String orderId) {
		return tuniuRefundDao.queryMerchantOrderId(orderId);
	}

	@Override
	public void insertLog(HashMap<String, Object> map) {
		tuniuRefundDao.insertLog(map);
	}

	@Override
	public void updateOrder(HashMap<String, Object> map) {
		tuniuRefundDao.updateOrder(map);
	}

	@Override
	public void updateOrderstatusToRobotGai(Map<String, String> updateMap) {
		tuniuRefundDao.updateOrderstatusToRobotGai(updateMap);
	}

	@Override
	public void updateRefundOpt(HashMap<String, Object> map) {
		tuniuRefundDao.updateRefundOpt(map);
	}

	@Override
	public int queryCpidIsRefundNum(Map<String, Object> pMap) {
		return tuniuRefundDao.queryCpidIsRefundNum(pMap);
	}

	@Override
	public Map<String, String> queryMoneyByCpId(Map<String, Object> pMap) {
		return tuniuRefundDao.queryMoneyByCpId(pMap);
	}

	@Override
	public String queryStatusByOrderId(Map<String, Object> pMap) {
		return tuniuRefundDao.queryStatusByOrderId(pMap);
	}

	@Override
	public String queryMerchantIdByOrderId(Map<String, Object> pMap) {
		return tuniuRefundDao.queryMerchantIdByOrderId(pMap);
	}

	@Override
	public List<Map<String, String>> queryTicketCpId(
			Map<String, Object> paramMap) {
		return tuniuRefundDao.queryTicketCpId(paramMap);
	}
	
	public Map<String, String> checkRefundMoney(Map<String, Object> paramMap) {
		Map<String,String>money_Map = new HashMap<String,String>();
		String sumRefundMoney = tuniuRefundDao.queryCpSumRefundMoney(paramMap);
		List<Map<String, String>> buy_moneyAndPaymoney = tuniuRefundDao.queryCpBuyAndPayMoney(paramMap);
		money_Map.put("sumRefundMoney", sumRefundMoney);
		if(buy_moneyAndPaymoney.size() != 0) {
			money_Map.put("pay_money", buy_moneyAndPaymoney.get(0).get("ticket_pay_money"));
			money_Map.put("buy_money", buy_moneyAndPaymoney.get(0).get("ticket_buy_money"));
		}
		return money_Map;
	}

	@Override
	public void queryRefundTicketAdd(Map<String, Object> mapAdd) {
		String refund_id = tuniuRefundDao.queryRefundStationTicketAdd(mapAdd);
		mapAdd.put("refund_id", refund_id);
		tuniuRefundDao.addRefundStationTicketNotify(mapAdd);
	}

	@Override
	public String queryRefundTime(Map<String, Object> param) {
		return tuniuRefundDao.queryRefundTime(param);
	}

	@Override
	public List<Map<String, String>> queryChangeRefundTicket(
			Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return tuniuRefundDao.queryChangeRefundTicket(paramMap);
	}

	
}
