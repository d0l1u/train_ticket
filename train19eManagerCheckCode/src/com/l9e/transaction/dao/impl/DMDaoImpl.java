package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.DMDao;
import com.l9e.transaction.vo.CodeVo;
@Repository("dMDao")
public class DMDaoImpl extends BaseDao implements DMDao{

	@Override
	public Map<String, String> queryCodeBusinesInfo() {
		return null;
	}

	@Override
	public List<CodeVo> queryCatchCodeList(Map<String, String> info) {
		return  this.getSqlMapClientTemplate().queryForList("dm.queryCatchCodeList", info);
	}

	@Override
	public List<CodeVo> querySendCodeList(Map<String, String> info) {
		return  this.getSqlMapClientTemplate().queryForList("dm.querySendCodeList", info);
	}

	@Override
	public int updateCodeUserPicStatus(CodeVo vo) {
		return (Integer)this.getSqlMapClientTemplate().update("dm.updateCodeUserPicStatus",vo);
	}

	@Override
	public void updateUploadStatus(CodeVo vo) {
		this.getSqlMapClientTemplate().update("dm.updateUploadStatus",vo);
	}

	@Override
	public void updateCodeInfo(CodeVo vo) {
		this.getSqlMapClientTemplate().update("dm.updateCodeInfo",vo);
	}

	@Override
	public List<CodeVo> queryErrorCodeList(Map<String, String> info) {
		return  this.getSqlMapClientTemplate().queryForList("dm.queryErrorCodeList", info);
	}

	@Override
	public void updateErrorInfo(CodeVo vo) {
		this.getSqlMapClientTemplate().update("dm.updateErrorInfo",vo);
	}


}
