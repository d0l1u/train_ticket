package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.ExcelDao;
import com.l9e.transaction.dao.PictureDao;
import com.l9e.transaction.service.ExcelService;
import com.l9e.transaction.vo.ExcelAdminVo;
@Service("excelService")
public class ExcelServiceImpl implements ExcelService {
	@Resource
	private ExcelDao excelDao;
	
	@Override
	public List<ExcelAdminVo> getExcelAdminList(Map<String, Object> paramMap) {
		return excelDao.getExcelAdminList(paramMap);
	}

	@Override
	public List<Map<String, String>> queryAdminCodeList(
			Map<String, Object> paramMap) {
		return excelDao.queryAdminCodeList(paramMap);
	}

}
