package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.CodeVo;

public interface DMService {

	Map<String, String> queryCodeBusinesInfo();

	List<CodeVo> updateQuerySendCodeList(Map<String, String> info);

	List<CodeVo> queryCatchCodeList(Map<String, String> info);
	
	/*void updateCodeStatus(CodeVo vo);*/

	void updateCodeInfo(CodeVo vo);

	List<CodeVo> queryErrorCodeList(Map<String, String> info);

	void updateErrorInfo(CodeVo vo);

	void updateCodeUserPicStatus(CodeVo vo);

	void updateUploadStatus(CodeVo vo);

}
