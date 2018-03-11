package com.l9e.transaction.vo;

import java.util.HashMap;
import java.util.Map;

import com.l9e.transaction.service.TuniuCommonService;
import com.l9e.transaction.vo.model.Result;

/**
 * 途牛业务结果数据
 * 
 * @author licheng
 * 
 */
public class TuniuResult implements Result {

	private Map<String, Object> resultMap;
	private String code = TuniuCommonService.RETURN_CODE_SUCCESS;

	@Override
	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public Map<String, Object> resultMap() {
		return resultMap;
	}

	@Override
	public void putData(String key, Object value) {
		if (resultMap == null) {
			resultMap = new HashMap<String, Object>();
		}
		resultMap.put(key, value);
	}

}
