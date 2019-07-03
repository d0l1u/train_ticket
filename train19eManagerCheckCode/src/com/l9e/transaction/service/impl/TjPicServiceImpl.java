package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.TjPicDao;
import com.l9e.transaction.service.TjPicService;
import com.l9e.transaction.vo.CodeRateVo;

@Service("tjPicService")
public class TjPicServiceImpl implements TjPicService {
	@Resource
	private TjPicDao tjPicDao;
	
	public void addTjPic(Map<String, Object> addMap) {
		tjPicDao.addTjPic(addMap);
	}

	public String queryChannel(String optRen) {
		return tjPicDao.queryChannel(optRen);
	}

	public List<String> queryDate_List() {
		return tjPicDao.queryDate_List();
	}

	public List<Map<String, String>> queryOptrenAndPiccount(String createTime) {
		return tjPicDao.queryOptrenAndPiccount(createTime);
	}

	public Integer queryPicSuccess(Map<String, String> queryMap) {
		return tjPicDao.queryPicSuccess(queryMap);
	}

	public int queryTable_Count() {
		return tjPicDao.queryTable_Count();
	}

	public Integer queryPicFail(Map<String, String> queryMap) {
		return tjPicDao.queryPicFail(queryMap);
	}

	public String queryOptname(String optRen) {
		return tjPicDao.queryOptname(optRen);
	}

	public Integer queryPicUnknown(Map<String, String> queryMap) {
		return tjPicDao.queryPicUnknown(queryMap);
	}

	@Override
	public void clearRh_picture() {
		tjPicDao.clearRh_picture();
	}

	@Override
	public int queryRhPicHourTable_Count() {
		return tjPicDao.queryRhPicHourTable_Count();
	}

	@Override
	public void addToTj_rhPic_hour(Map<String, String> pramaMap) {
		tjPicDao.addToTj_rhPic_hour(pramaMap);
	}

	@Override
	public List<Map<String, String>> queryRhPicHour(String createTime) {
		return tjPicDao.queryRhPicHour(createTime);
	}

	@Override
	public String queryRhPicHourCodeFail(Map<String, String> pramaMap) {
		return tjPicDao.queryRhPicHourCodeFail(pramaMap);
	}

	@Override
	public String queryRhPicHourCodeSuccess(Map<String, String> pramaMap) {
		return tjPicDao.queryRhPicHourCodeSuccess(pramaMap);
	}

	@Override
	public String queryRhPicHourCodeUnknown(Map<String, String> pramaMap) {
		return tjPicDao.queryRhPicHourCodeUnknown(pramaMap);
	}

	@Override
	public Map<String, Object> queryNullCodeCount(String createTime) {
		return tjPicDao.queryNullCodeCount(createTime);
	}

	@Override
	public Integer queryPicNotOpt(Map<String, String> queryMap) {
		return tjPicDao.queryPicNotOpt(queryMap);
	}

	@Override
	public int queryNullAndNotoptCodeCount(String createTime) {
		return (Integer)tjPicDao.queryNullAndNotoptCodeCount(createTime);
	}

	@Override
	public int queryNullAndUnknownCodeCount(String createTime) {
		return (Integer)tjPicDao.queryNullAndUnknownCodeCount(createTime);
	}

	@Override
	public int queryTodayCount(Map<String, Object> map) {
		return (Integer)tjPicDao.queryTodayCount(map);
	}

	@Override
	public void updateTjPic(Map<String, Object> updateMap) {
		tjPicDao.updateTjPic(updateMap);
	}

	@Override
	public int queryTodayHourCount(Map<String, String> map) {
		return (Integer)tjPicDao.queryTodayHourCount(map);
	}

	@Override
	public void updateToTj_rhPic_hour(Map<String, String> updateMap) {
		tjPicDao.updateToTj_rhPic_hour(updateMap);
	}

	@Override
	public List<CodeRateVo> queryCodeUserList(Map<String, String> param) {
		return tjPicDao.queryCodeUserList(param);
	}

	@Override
	public void addTotj_code_rate(Map<String, String> map) {
		tjPicDao.addTotj_code_rate(map);
	}

	@Override
	public String queryCodeFail(Map<String, String> map) {
		return tjPicDao.queryCodeFail(map);
	}

	@Override
	public int queryCodeHourCount(Map<String, String> map) {
		return tjPicDao.queryCodeHourCount(map);
	}

	@Override
	public String queryCodeOver(Map<String, String> map) {
		return tjPicDao.queryCodeOver(map);
	}

	@Override
	public String queryCodeSuccess(Map<String, String> map) {
		return tjPicDao.queryCodeSuccess(map);
	}

	@Override
	public void updateTotj_code_rate(Map<String, String> map) {
		tjPicDao.updateTotj_code_rate(map);
	}

	@Override
	public String queryCodeCount(Map<String, String> map) {
		return tjPicDao.queryCodeCount(map);
	}
}
