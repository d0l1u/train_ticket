package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.ExcelDao;
import com.l9e.transaction.vo.ExcelAdminVo;
@Repository("excelDao")
public class ExcelDaoImpl extends BaseDao implements ExcelDao {

	@Override
	public List<ExcelAdminVo> getExcelAdminList(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("excel.getExcelAdminList", paramMap);
	}

	@Override
	public List<Map<String, String>> queryAdminCodeList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("excel.queryAdminCodeList", paramMap);
	}

}
