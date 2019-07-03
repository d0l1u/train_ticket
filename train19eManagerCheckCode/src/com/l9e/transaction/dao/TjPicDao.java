package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.CodeRateVo;

public interface TjPicDao {

	void addTjPic(Map<String, Object> addMap);

	String queryChannel(String optRen);

	List<String> queryDate_List();

	List<Map<String, String>> queryOptrenAndPiccount(String createTime);

	Integer queryPicSuccess(Map<String, String> queryMap);

	int queryTable_Count();

	Integer queryPicFail(Map<String, String> queryMap);

	String queryOptname(String optRen);

	Integer queryPicUnknown(Map<String, String> queryMap);

	void clearRh_picture();

	int queryRhPicHourTable_Count();

	void addToTj_rhPic_hour(Map<String, String> pramaMap);

	List<Map<String, String>> queryRhPicHour(String createTime);

	String queryRhPicHourCodeFail(Map<String, String> pramaMap);

	String queryRhPicHourCodeSuccess(Map<String, String> pramaMap);

	String queryRhPicHourCodeUnknown(Map<String, String> pramaMap);

	Map<String, Object> queryNullCodeCount(String createTime);

	Integer queryPicNotOpt(Map<String, String> queryMap);

	Integer queryNullAndNotoptCodeCount(String createTime);

	Integer queryNullAndUnknownCodeCount(String createTime);
	
	void updateTjPic(Map<String, Object> updateMap);
	
	int queryTodayCount(Map<String, Object> map);

	void updateToTj_rhPic_hour(Map<String, String> updateMap);
	
	int queryTodayHourCount(Map<String, String> map);

	List<CodeRateVo> queryCodeUserList(Map<String, String> param);

	void addTotj_code_rate(Map<String, String> map);

	String queryCodeFail(Map<String, String> map);

	int queryCodeHourCount(Map<String, String> map);

	String queryCodeOver(Map<String, String> map);

	String queryCodeSuccess(Map<String, String> map);

	void updateTotj_code_rate(Map<String, String> map);

	String queryCodeCount(Map<String, String> map);
}
