package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.CodeVo;

public interface DMDao {

	List<CodeVo> querySendCodeList(Map<String, String> info);

	List<CodeVo> queryCatchCodeList(Map<String, String> info);

	Map<String, String> queryCodeBusinesInfo();

	int updateCodeUserPicStatus(CodeVo vo);

	void updateUploadStatus(CodeVo vo);

	void updateCodeInfo(CodeVo vo);

	List<CodeVo> queryErrorCodeList(Map<String, String> info);

	void updateErrorInfo(CodeVo vo);

}
