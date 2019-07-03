package com.l9e.transaction.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.CheckPriceDao;
import com.l9e.transaction.service.CheckPriceService;

@Service("checkPriceService")
public class CheckPriceServiceImpl implements CheckPriceService{
	@Resource
	private CheckPriceDao checkPriceDao;
	
	@Override
	public void addAlipayInfo(Map<String, Object> paramMap) {
		checkPriceDao.addAlipayInfo(paramMap);
	}

	@Override
	public int queryAlipayCounts(Map<String, Object> pMap) {
		return checkPriceDao.queryAlipayCounts(pMap);
	}

	@Override
	public List<Map<String, String>> queryCheckPriceList(
			Map<String, Object> paramMap) {
		String type = String.valueOf(paramMap.get("query_type"));
		if("11".equals(type)||"33".equals(type)){//出票完全配对
			return checkPriceDao.queryOutTicketList(paramMap);
		}else if( "22".equals(type)){//退票完全配对
			return checkPriceDao.queryRefundTicketList(paramMap);
		}else{
			return null;
		}
	}
	
	@Override
	public List<Map<String, String>> queryAlipayBalanceList(
			Map<String, Object> paramMap) {
			return checkPriceDao.queryAlipayBalanceList(paramMap);
	}
	
	@Override
	public int queryCheckPriceCounts(Map<String, Object> paramMap) {
		String type = String.valueOf(paramMap.get("query_type"));
		if("11".equals(type)||"33".equals(type)){//出票完全配对
			return checkPriceDao.queryOutTicketCounts(paramMap);
		}else if( "22".equals(type)){//退票完全配对
			return checkPriceDao.queryRefundTicketCounts(paramMap);
		}else{
			return 0;
		}
	}

	@Override
	public int queryAlipayBalanceCounts(Map<String, Object> paramMap) {
			return checkPriceDao.queryAlipayBalanceCounts(paramMap);
	}
	
	@Override
	public List<Map<String, String>> queryCheckPriceExcel(
			HashMap<String, Object> paramMap) {
		String type = String.valueOf(paramMap.get("query_type"));
		if("11".equals(type)||"33".equals(type)){//出票完全配对
			return checkPriceDao.queryOutTicketExcel(paramMap);
		}else if( "22".equals(type)){//退票完全配对
			return checkPriceDao.queryRefundTicketExcel(paramMap);
		}else{
			return null;
		}
	}

	@Override
	public List<Map<String, String>> queryAlipayBalanceExcel(
			HashMap<String, Object> paramMap) {
			return checkPriceDao.queryOutAlipayBalanceExcel(paramMap);
	}
	
	@Override
	public int updateSeqById(Map<String, String> map) {
		return checkPriceDao.updateSeqById(map);
	}

	@Override
	public List<Map<String, String>> queryAlipayExcel(HashMap<String, Object> map) {
		return checkPriceDao.queryAlipayExcel(map);
	}

	@Override
	public int updateOrderInfo(Map<String, Object> paramMap) {
		return checkPriceDao.updateOrderInfo(paramMap);
	}

	@Override
	public List<Map<String, String>> queryNeedRefund(HashMap<String, Object> map) {
		return checkPriceDao.queryNeedRefund(map);
	}

	@Override
	public int updateRefundPrice(Map<String, String> m) {
		return checkPriceDao.updateRefundPrice(m);
	}

	@Override
	public String queryPriceByOrderId(Map<String, String> m) {
		String price = "0";
		//按渠道查退款成功总金额
		if("elong".equals(m.get("channel"))
				|| "tongcheng".equals(m.get("channel"))) price = checkPriceDao.queryElongRefund(m);//艺龙同程
		else if("301030".equals(m.get("channel")))price = checkPriceDao.queryGtRefund(m);//高铁
		else if("meituan".equals(m.get("channel")))price = checkPriceDao.queryMtRefund(m);//美团
		else if("tuniu".equals(m.get("channel")))price = checkPriceDao.queryTuniuRefund(m);//途牛
		else if("19e".equals(m.get("channel")))price = checkPriceDao.queryl9eRefund(m);//19e
		else if("qunar".equals(m.get("channel")))price = checkPriceDao.queryQunarRefund(m);//去哪
		else if("301031".equals(m.get("channel")))price = checkPriceDao.queryXcRefund(m);//携程
		else if("cmpay".equals(m.get("channel"))
				|| "cmwap".equals(m.get("channel"))
				|| "ccb".equals(m.get("channel"))
				|| "chq".equals(m.get("channel"))
				|| "19pay".equals(m.get("channel")))price = checkPriceDao.queryInnerRefund(m);//内嵌
		else if("app".equals(m.get("channel"))
				|| "weixin".equals(m.get("channel")))price = checkPriceDao.queryAppRefund(m);//app
		else price = checkPriceDao.queryExtRefund(m);//商户
		if(StringUtils.isEmpty(price))price ="0";
		return price;
	}

	@Override
	public List<String> selectAlipay(Map<String, String> m) {
		return checkPriceDao.selectAlipay(m);
	}

	@Override
	public int deleteTicketById(Map<String, Object> map) { 
		return checkPriceDao.deleteTicketById(map);
	}


}
