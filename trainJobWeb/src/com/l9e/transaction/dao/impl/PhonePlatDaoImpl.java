package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.PhonePlatDao;
import com.l9e.transaction.vo.Phone;

@Repository("phonePlatDao")
public class PhonePlatDaoImpl extends BaseDao  implements PhonePlatDao{
	@Override
	public List<Phone> queryWaitPhoneList() {
		return this.getSqlMapClientTemplate().queryForList("phone.queryWaitPhoneList");
	}

	@Override
	public void updatePhoneNum(Map<String,Object> map) {
		this.getSqlMapClientTemplate().update("phone.updatePhoneNum",map);
	}

	@Override
	public void addPhone(Phone phone) {
		this.getSqlMapClientTemplate().insert("phone.addPhone",phone);
	}

	@Override
	public Integer queryPhoneByPhone(Map<String,String> map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("phone.queryPhoneByPhone",map);
	}

	@Override
	public Integer queryPhoneNumDay(Map<String,String> map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("phone.queryPhoneNumDay",map);
	}

	@Override
	public Integer queryPhoneNumHour(Map<String,String> map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("phone.queryPhoneNumHour",map);
	}
}
