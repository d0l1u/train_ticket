package com.l9e.transaction.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.RemindDao;
@Repository("remindDao")
public class RemindDaoImpl extends BaseDao implements RemindDao{
	@Override
	public int query19eFailCount() {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("remind.query19eFailCount");
	}

	@Override
	public int queryAllRefundCount(String channel) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("remind.queryAllRefundCount",channel);
	}

	@Override
	public int queryB2CFailCount() {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("remind.queryB2CFailCount");
	}

	@Override
	public int queryElongFailCount() {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("remind.queryElongFailCount");
	}

	@Override
	public int queryExtFailCount() {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("remind.queryExtFailCount");
	}

	@Override
	public int queryQunarFailCount() {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("remind.queryQunarFailCount");
	}

	@Override
	public int queryTcFailCount() {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("remind.queryTcFailCount");
	}

	@Override
	public int querybxCount() {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("remind.querybxCount");
	}

	@Override
	public int querycomplainCount() {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("remind.querycomplainCount");
	}
	@Override
	public int queryinnerFailCount() {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("remind.queryinnerFailCount");
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryRobotCount() {
		return this.getSqlMapClientTemplate().queryForList("remind.queryRobotCount");
	}

	@Override
	public int bookExtCount() {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("remind.refundExtCount");
	}

	@Override
	public int bookElongCount() {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("remind.bookElongCount");
	}

	@Override
	public int bookQunarCount() {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("remind.bookQunarCount");
	}

	@Override
	public int bookTcCount() {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("remind.bookTcCount");
	}

	@Override
	public int refundExtCount() {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("remind.refundExtCount");
	}

	@Override
	public int refundElongCount() {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("remind.refundElongCount");
	}

	@Override
	public int refundQunarCount() {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("remind.refundQunarCount");
	}

	@Override
	public int refundTcCount() {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("remind.refundTcCount");
	}
	
	@Override
	public int alterCount() {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("remind.alterCount");
	}

	//打码
	@Override
	public String codeQunarType() {
		return (String)this.getSqlMapClientTemplate().queryForObject("remind.codeQunarType");
	}

	@Override
	public int queryAdminCurrentNameCount() {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("remind.queryAdminCurrentNameCount");
	}

	@Override
	public int queryCodeCountToday() {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("remind.queryCodeCountToday");
	}

	@Override
	public int queryCodeToday() {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("remind.queryCodeToday");
	}

	@Override
	public int queryUncode(String channel) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("remind.queryUncode",channel);
	}

	@Override
	public int queryAdminCurrentNameCount2(Map<String, Object> paramMap1) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("remind.queryAdminCurrentNameCount2",paramMap1);
	}


	@Override
	public int queryWaitCodeQueenCount(String department) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("remind.queryWaitCodeQueenCount",department);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryAccountList() {
		return this.getSqlMapClientTemplate().queryForList("remind.queryAccountList");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryAccountMarginList(
			HashMap<String, Integer> map) {
		return  this.getSqlMapClientTemplate().queryForList("remind.queryAccountMarginList",map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryAccountContactList(
			HashMap<String, Integer> map) {
		return  this.getSqlMapClientTemplate().queryForList("remind.queryAccountContactList",map);
	}

	@Override
	public int queryOrderNumber() {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("remind.queryOrderNumber");
	}

	@Override
	public int queryRobotNumber(String string) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("remind.queryRobotNumber",string);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryZhifubaoMoney(String name) {
		Map param = new HashMap();
		param.put("name", name);
		return this.getSqlMapClientTemplate().queryForList("remind.queryZhifubaoMoney",param);
	}

	@Override
	public int bookGtCount() {
		// TODO Auto-generated method stub
		return (Integer)this.getSqlMapClientTemplate().queryForObject("remind.bookGtCount");
	}

	@Override
	public int bookMtCount() {
		// TODO Auto-generated method stub
		return (Integer)this.getSqlMapClientTemplate().queryForObject("remind.bookMtCount");
	}

	@Override
	public int bookTuniuCount() {
		// TODO Auto-generated method stub
		return (Integer)this.getSqlMapClientTemplate().queryForObject("remind.bookTuniuCount");
	}

}
