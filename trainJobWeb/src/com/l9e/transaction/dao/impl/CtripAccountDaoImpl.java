package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.CtripAccountDao;
import com.l9e.transaction.vo.CtripAcc;

@Repository("ctripAccountDao")
public class CtripAccountDaoImpl extends BaseDao implements CtripAccountDao {

	@Override
	public List<CtripAcc> selectCtripAccountList(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return this.getSqlMapClientTemplate().queryForList("ctripAccount.selectCtripAccountList", paramMap);
	}

	@Override
	public int updateCtripAccount(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return this.getSqlMapClientTemplate().update("ctripAccount.updateCtripAccount", paramMap);
	}


}
