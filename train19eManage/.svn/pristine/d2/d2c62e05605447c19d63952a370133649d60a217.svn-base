package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
@Repository("fileDao")
public class FileDaoImpl extends BaseDao implements com.l9e.transaction.dao.FileDao {

	@Override
	public void insertFile(Map<String, String> paramMap) {
		this.getSqlMapClientTemplate().insert("file.insertFile", paramMap);
	}

	@Override
	public int queryFileCount(Map<String, Object> paramMap) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("file.queryFileCount", paramMap);
	}

	@Override
	public List<Map<String, Object>> queryFileList(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("file.queryFileList", paramMap);
	}

	@Override
	public String queryFilepath(String id) {
		return (String) this.getSqlMapClientTemplate().queryForObject("file.queryFilepath", id);
	}

	@Override
	public void deleteFile(String id) {
		this.getSqlMapClientTemplate().delete("file.deleteFile", id);
	}

}
