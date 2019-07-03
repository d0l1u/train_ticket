package com.l9e.transaction.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.Tj_OutTicket_Dao;
import com.l9e.transaction.service.Tj_OutTicket_Service;

@Service("tj_OutTicket_Service")
public class Tj_OutTicket_ServiceImpl implements Tj_OutTicket_Service {
	@Resource
	private Tj_OutTicket_Dao tj_OutTicket_Dao;
	
	public void addToTj_OutTicket(Map<String, Object> map) {
		tj_OutTicket_Dao.addToTj_OutTicket(map);
	}
	public int query19eTodayCount(Map<String, Object> map) {
		return tj_OutTicket_Dao.query19eTodayCount(map);
	}
	
	public void updateToTj_OutTicket(Map<String, Object> map) {
		tj_OutTicket_Dao.updateToTj_OutTicket(map);		
	}
	public int queryBook(Map<String, Object> map) {
		return tj_OutTicket_Dao.queryBook(map);
	}
	public int queryBook_xl(Map<String, Object> map) {
		return tj_OutTicket_Dao.queryBook_xl(map);
	}
	public int queryFenfa(Map<String, Object> map) {
		return tj_OutTicket_Dao.queryFenfa(map);
	}
	public int queryNotify(Map<String, Object> map) {
		return tj_OutTicket_Dao.queryNotify(map);
	}
	public int queryOutticket_xl(Map<String, Object> map) {
		return tj_OutTicket_Dao.queryOutticket_xl(map);
	}
	public int queryPay_xl(Map<String, Object> map) {
		return tj_OutTicket_Dao.queryPay_xl(map);
	}
	public int queryNotifyPay(Map<String, Object> map) {
		return tj_OutTicket_Dao.queryNotifyPay(map);
	}
	public int queryShoudan(Map<String, Object> map) {
		return tj_OutTicket_Dao.queryShoudan(map);
	}
	public int queryBook_xlTuniu(Map<String, Object> paramMap) {
		return tj_OutTicket_Dao.queryBook_xlTuniu(paramMap);
	}
	public int queryNotifyPayTuniu(Map<String, Object> paramMap) {
		return tj_OutTicket_Dao.queryNotifyPayTuniu(paramMap);
	}
	public int queryNotifyTuniu(Map<String, Object> paramMap) {
		return tj_OutTicket_Dao.queryNotifyTuniu(paramMap);
	}
	public int queryOutticket_xlTuniu(Map<String, Object> paramMap) {
		return tj_OutTicket_Dao.queryOutticket_xlTuniu(paramMap);
	}
	public int queryShoudanTuniu(Map<String, Object> paramMap) {
		return tj_OutTicket_Dao.queryShoudanTuniu(paramMap);
	}

}
