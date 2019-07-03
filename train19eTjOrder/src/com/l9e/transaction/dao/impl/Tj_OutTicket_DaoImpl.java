package com.l9e.transaction.dao.impl;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.Tj_OutTicket_Dao;

@Repository("tj_OutTicket_Dao")
public class Tj_OutTicket_DaoImpl extends BaseDao implements Tj_OutTicket_Dao{

	public void addToTj_OutTicket(Map<String, Object> map) {
		this.getSqlMapClientTemplate().insert("tj_OutTicket.addToTj_OutTicket",map);
	}
	public void updateToTj_OutTicket(Map<String, Object> map) {
		this.getSqlMapClientTemplate().update("tj_OutTicket.updateToTj_OutTicket", map);
	}
	public int query19eTodayCount(Map<String, Object> map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_OutTicket.query19eTodayCount", map);
	}
	public int queryBook(Map<String, Object> map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_OutTicket.queryBook", map);
	}
	public int queryBook_xl(Map<String, Object> map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_OutTicket.queryBook_xl", map);
	}
	public int queryFenfa(Map<String, Object> map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_OutTicket.queryFenfa", map);
	}
	public int queryNotify(Map<String, Object> map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_OutTicket.queryNotify", map);
	}
	public int queryOutticket_xl(Map<String, Object> map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_OutTicket.queryOutticket_xl", map);
	}
	public int queryPay_xl(Map<String, Object> map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_OutTicket.queryPay_xl", map);
	}
	public int queryNotifyPay(Map<String, Object> map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_OutTicket.queryNotifyPay", map);
	}
	public int queryShoudan(Map<String, Object> map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_OutTicket.queryShoudan", map);
	}
	public int queryBook_xlTuniu(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_OutTicket.queryBook_xlTuniu", paramMap);
	}
	public int queryNotifyPayTuniu(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_OutTicket.queryNotifyPayTuniu", paramMap);
	}
	public int queryNotifyTuniu(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_OutTicket.queryNotifyTuniu", paramMap);
	}
	public int queryOutticket_xlTuniu(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_OutTicket.queryOutticket_xlTuniu", paramMap);
	}
	public int queryShoudanTuniu(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_OutTicket.queryShoudanTuniu", paramMap);
	}
	
}
