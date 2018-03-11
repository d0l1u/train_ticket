package com.l9e.transaction.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.GtRefundDao;
import com.l9e.transaction.service.GtRefundService;
@Service("gtRefundService")
public class GtRefundServiceImpl implements GtRefundService {
	@Resource
	private GtRefundDao gtRefundDao ;

	public int queryRefundTicketCount(Map<String, Object> paramMap) {
		return gtRefundDao.queryRefundTicketCount(paramMap);
	}

	public List<Map<String, String>> queryRefundTicketList(
			Map<String, Object> paramMap) {
		return gtRefundDao.queryRefundTicketList(paramMap);
	}

	public List<Map<String, String>> queryRefundTicketInfo(Map<String,String>id_Map) {
		return gtRefundDao.queryRefundTicketInfo(id_Map);
	}

	public List<Map<String, String>> queryRefundTicketOrderInfoCp(String cp_id) {
		return gtRefundDao.queryRefundTicketOrderInfoCp(cp_id);
	}
	
	public void queryrefundTicketAdd(Map<String, String> log_Map,Map<String, Object> mapAdd) {
		gtRefundDao.addOrderRefundTicket_log(log_Map);//增加操作日志
		gtRefundDao.queryrefundTicketAdd(mapAdd);//增加差额退款，向gt_orderinfo_refundstream表添加数据
		//根据order_id查询eop_order_id和eop_refund_url
		List<Map<String, String>> orderList = gtRefundDao.queryOrderInfo(mapAdd.get("order_id"));
		Map<String, String> orderMap = orderList.get(0);
		orderMap.put("refund_reason", mapAdd.get("user_remark").toString());
		gtRefundDao.addOrderRefundNotify(orderMap);//向gt_orderinfo_refundeopnotify表添加数据（平台通知表）
	}
	
	public void updateRefundTicket(Map<String, String> log_Map,
			Map<String, Object> refund_Map) {
		gtRefundDao.addCpRefundTicket_log(log_Map);
		gtRefundDao.updateRefundTicketInfo(refund_Map);
	}

	public List<Map<String, Object>> queryHistroyByCpId(String cp_id) {
		return gtRefundDao.queryHistroyByCpId(cp_id);
	}

	public List<Map<String, Object>> queryHistroyByOrderId(String order_id) {
		return gtRefundDao.queryHistroyByOrderId(order_id);
	}
	
	public Map<String, String> queryBuymoneyAndTicketpaymoney(String order_id) {
		Map<String,String>money_Map = new HashMap<String,String>();
		String sumRefundMoney = gtRefundDao.querySumRefundMoney(order_id);
		String pay_money = gtRefundDao.queryBuymoneyAndTicketpaymoney(order_id);
		money_Map.put("sumRefundMoney", sumRefundMoney);
		money_Map.put("pay_money", pay_money);
		return money_Map;
	}
	
	public Map<String, String> queryRefundMoney(String order_id,String stream_id) {
		Map<String,String>money_Map = new HashMap<String,String>();
		String sumRefundMoney = gtRefundDao.querySumRefundMoney(order_id);
		String pay_money = gtRefundDao.queryBuymoneyAndTicketpaymoney(order_id);
		String refund_money =gtRefundDao.queryRefundMoney(stream_id);
		money_Map.put("sumRefundMoney", sumRefundMoney);
		money_Map.put("pay_money", pay_money);
		money_Map.put("refund_money", refund_money);
		return money_Map;
	}
	
	public void updateDifferRefund(Map<String, String> log_Map,
			Map<String, Object> differ_Map) {
		gtRefundDao.addOrderRefundTicket_log(log_Map);
		gtRefundDao.updateDifferRefund(differ_Map);
	}


	public void updateOut_Ticket_Refund(Map<String, String> log_Map,
			Map<String, Object> outTicket_Defeated_Map) {
		gtRefundDao.addCpRefundTicket_log(log_Map);
		gtRefundDao.updateOut_Ticket_Refund(outTicket_Defeated_Map);
//		gtRefundDao.updateOutTicketRefundEOP(outTicket_Defeated_Map);//更新gt_orderinfo_refundeopnotify表数据
	}

	public void updateRefreshNotice(Map<String, Object> update_RefreshNoticeMap) {
		gtRefundDao.updateRefreshNotice(update_RefreshNoticeMap);
	}

	public void addErrorLogInfo(Map<String, String> log_Map) {
		gtRefundDao.addCpRefundTicket_log(log_Map);
	}

	public String queryRefundTicketOrderId(String order_id) {
		return gtRefundDao.queryRefundTicketOrderId(order_id);
	}

	public String queryRefundTicketOrderIdExists(String orderId) {
		return gtRefundDao.queryRefundTicketOrderIdExists(orderId);
	}

	@Override
	public Map<String, String> queryBookOrderInfo(String orderId) {
		return gtRefundDao.queryBookOrderInfo(orderId);
	}

	@Override
	public List<Map<String, String>> queryBookOrderInfoBx(String orderId) {
		return gtRefundDao.queryBookOrderInfoBx(orderId);
	}

	@Override
	public List<Map<String, String>> queryBookOrderInfoCp(String orderId) {
		return gtRefundDao.queryBookOrderInfoCp(orderId);
	}
	
	//同意退款
	@Override
	public void updateRefundNotify(Map<String,Object>map,Map<String,String>log_Map) {
		gtRefundDao.updateRefundNotify(map);				 //更新gt_orderinfo_refundstream表的信息（同意退款）
		gtRefundDao.updateCpRefundStatusSuccess(map.get("cp_id").toString());//更新gt_orderinfo_cp表的信息
		int count = gtRefundDao.queryRefundCount(map.get("refund_seq").toString());//根据refund_seq查询gt_orderinfo_refundstream有几条退款记录
		if(count > 0){
			gtRefundDao.addCpRefundTicket_log(log_Map);		 //添加操作日志
		}else{
			gtRefundDao.updateGtRefundNotifyStatus(map);		 //更新gt_orderinfo_refundnotify表的信息（同意退款）
			gtRefundDao.addCpRefundTicket_log(log_Map);		 //添加操作日志
		}
	}

	//拒绝退款
	public void updateRefuseRefund(Map<String, String> log_Map, Map<String, Object> refuse_Map, Map<String,String> order_Map) {
		gtRefundDao.updateRefund_StatusTo55(refuse_Map);	//修改退款状态为拒绝退款同时备注,操作人和审核时间也会修改 gt_orderinfo_refundstream
		gtRefundDao.updateCpRefundStatusFail(refuse_Map);  //更新gt_orderinfo_cp表的信息
		int count = gtRefundDao.queryRefundCount(refuse_Map.get("refund_seq").toString());//根据refund_seq查询gt_orderinfo_refundstream有几条退款记录
		if(count > 0){
			gtRefundDao.addCpRefundTicket_log(log_Map);		//添加操作日志
		}else{
			gtRefundDao.addCpRefundTicket_log(log_Map);		//添加操作日志
			//gtRefundDao.deleteRefundNotify(refuse_Map);		//删除退款信息  gt_orderinfo_refundnotify
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("order_id", refuse_Map.get("order_id"));
			map.put("refund_seq", refuse_Map.get("refund_seq"));
			gtRefundDao.updateGtRefundNotifyStatus(map);		 //更新gt_orderinfo_refundnotify表的信息（同意退款）
			gtRefundDao.updateOrderInfo_can_refundTo1_And_refund_total(order_Map);//修改主表中can_refund为1与更新refund_total(gt_orderinfo)
		}
		
	}
	
	@Override
	public void updateGezhiRefund(Map<String, String> logMap,
			Map<String, Object> refuseMap) {
		gtRefundDao.addCpRefundTicket_log(logMap);//添加操作日志
		gtRefundDao.updateGezhiRefund(refuseMap);//搁置订单
	}
	
	@Override
	public List<Map<String, String>> queryGtMerchantinfo() {
		return gtRefundDao.queryGtMerchantinfo();
	}

	@Override
	public void updateGtRefundNotifyNum(Map<String, String> paramMap) {
		gtRefundDao.updateGtRefundNotifyNum(paramMap);
	}

	@Override
	public List<Map<String, Object>> queryRefundStationTicketAdd(
			Map<String, String> logMap, Map<String, Object> mapAdd) {
		gtRefundDao.addOrderRefundTicket_log(logMap);
		gtRefundDao.addRefundStationTicketNotify(mapAdd);
		return gtRefundDao.queryRefundStationTicketAdd(mapAdd);
	}

	@Override
	public String queryRefundStationTicketCpId(Map<String, Object> paramMap) {
		return gtRefundDao.queryRefundStationTicketCpId(paramMap);
	}

	@Override
	public String queryCpidIsRefund(Map<String, Object> paramMap) {
		return gtRefundDao.queryCpidIsRefund(paramMap);
	}

	@Override
	public String queryMerchantOrderId(String orderId) {
		return gtRefundDao.queryMerchantOrderId(orderId);
	}

	@Override
	public void insertLog(HashMap<String, Object> map) {
		gtRefundDao.insertLog(map);
	}

	@Override
	public void updateOrder(HashMap<String, Object> map) {
		gtRefundDao.updateOrder(map);
	}

	@Override
	public void updateOrderstatusToRobotGai(Map<String, String> updateMap) {
		gtRefundDao.updateOrderstatusToRobotGai(updateMap);
	}

	@Override
	public void updateRefundOpt(HashMap<String, Object> map) {
		gtRefundDao.updateRefundOpt(map);
	}

	@Override
	public int queryCpidIsRefundNum(Map<String, Object> pMap) {
		return gtRefundDao.queryCpidIsRefundNum(pMap);
	}

	@Override
	public Map<String, String> queryMoneyByCpId(Map<String, Object> pMap) {
		return gtRefundDao.queryMoneyByCpId(pMap);
	}

	@Override
	public String queryStatusByOrderId(Map<String, Object> pMap) {
		return gtRefundDao.queryStatusByOrderId(pMap);
	}

	@Override
	public String queryMerchantIdByOrderId(Map<String, Object> pMap) {
		return gtRefundDao.queryMerchantIdByOrderId(pMap);
	}

	@Override
	public List<Map<String, String>> queryTicketCpId(
			Map<String, Object> paramMap) {
		return gtRefundDao.queryTicketCpId(paramMap);
	}
	
	public Map<String, String> checkRefundMoney(Map<String, Object> paramMap) {
		Map<String,String>money_Map = new HashMap<String,String>();
		String sumRefundMoney = gtRefundDao.queryCpSumRefundMoney(paramMap);
		List<Map<String, String>> buy_moneyAndPaymoney = gtRefundDao.queryCpBuyAndPayMoney(paramMap);
		money_Map.put("sumRefundMoney", sumRefundMoney);
		if(buy_moneyAndPaymoney !=null && buy_moneyAndPaymoney.size() != 0) {
			money_Map.put("pay_money", buy_moneyAndPaymoney.get(0).get("ticket_pay_money"));
			money_Map.put("buy_money", buy_moneyAndPaymoney.get(0).get("ticket_buy_money"));
		}else{
			String  changeCpPayMoney = gtRefundDao.queryChangeCpPayMoney(paramMap);
			money_Map.put("pay_money", changeCpPayMoney);
			money_Map.put("buy_money", changeCpPayMoney);
		}
		return money_Map;
	}

	@Override
	public List<Map<String, String>> queryChangeRefundTicket(Map<String, Object> paramMap) {
		return gtRefundDao.queryChangeRefundTicket(paramMap);
	}

	@Override
	public String queryChangeCpPayMoney(Map<String, Object> paramMap) {
		
		return gtRefundDao.queryChangeCpPayMoney(paramMap);
	}

	
}
