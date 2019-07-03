package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.PictureDao;
import com.l9e.transaction.dao.TjPicDao;
import com.l9e.transaction.service.PictureService;
@Service("pictureService")
public class PictureServiceImpl implements PictureService {
	@Resource
	private PictureDao pictureDao;

	@Override
	public int queryPictureCount(Map<String, Object> paramMap) {
		return pictureDao.queryPictureCount(paramMap);
	}

	@Override
	public List<Map<String, String>> queryPictureList(
			Map<String, Object> paramMap) {
		return pictureDao.queryPictureList(paramMap);
	}

	@Override
	public int queryCodeHourCount(Map<String, Object> paramMap) {
		return pictureDao.queryCodeHourCount(paramMap);
	}

	@Override
	public List<Map<String, String>> queryCodeHourList(
			Map<String, Object> paramMap) {
		return pictureDao.queryCodeHourList(paramMap);
	}

	@Override
	public int queryUserHourCount(Map<String, Object> paramMap) {
		return pictureDao.queryUserHourCount(paramMap);
	}

	@Override
	public List<Map<String, String>> queryUserHourList(
			Map<String, Object> paramMap) {
		return pictureDao.queryUserHourList(paramMap);
	}

	@Override
	public List<Map<String, String>> queryCodeHourEveryDayList(
			String paramMap) {
		return pictureDao.queryCodeHourEveryDayList(paramMap);
	}

	@Override
	public List<Map<String, String>> queryUserHourEveryDayList(String string) {
		return pictureDao.queryUserHourEveryDayList(string);
	}

	@Override
	public List<Map<String, String>> queryFailCodeHourEveryDayList(String string) {
		return pictureDao.queryFailCodeHourEveryDayList(string);
	}

	@Override
	public List<Map<String, String>> queryFailCodeHourList(
			Map<String, Object> paramMap) {
		return pictureDao.queryFailCodeHourList(paramMap);
	}

	@Override
	public List<Map<String, String>> querySuccessCodeHourEveryDayList(
			String string) {
		return pictureDao.querySuccessCodeHourEveryDayList(string);
	}

	@Override
	public List<Map<String, String>> querySuccessCodeHourList(
			Map<String, Object> paramMap) {
		return pictureDao.querySuccessCodeHourList(paramMap);
	}
	
}
