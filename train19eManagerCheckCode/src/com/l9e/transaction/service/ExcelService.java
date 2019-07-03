package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.ExcelAdminVo;

public interface ExcelService {

	List<ExcelAdminVo> getExcelAdminList(Map<String, Object> paramMap);

	List<Map<String, String>> queryAdminCodeList(Map<String, Object> paramMap);

}
