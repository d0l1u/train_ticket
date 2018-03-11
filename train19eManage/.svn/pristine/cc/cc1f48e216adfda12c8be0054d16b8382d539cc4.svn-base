package com.l9e.transaction.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.RemindDao;
import com.l9e.transaction.service.RemindService;

@Service("remindService")
public class RemindServiceImpl implements RemindService{
	@Resource
	private RemindDao remindDao;

	public Map<String, String> queryCounts() {
		Map<String, String> map = new HashMap<String, String>();
			map.put("l9eCount", String.valueOf(remindDao.query19eFailCount()+remindDao.queryAllRefundCount("19e")));
			map.put("qunarCount", String.valueOf(remindDao.queryQunarFailCount()+remindDao.queryAllRefundCount("qunar")));
			map.put("elongCount", String.valueOf(remindDao.queryElongFailCount()+remindDao.queryAllRefundCount("elong")));	
			map.put("tcCount", String.valueOf(remindDao.queryTcFailCount()+remindDao.queryAllRefundCount("tongcheng")));	
			map.put("extCount", String.valueOf(remindDao.queryExtFailCount()));
			map.put("B2CCount", String.valueOf(remindDao.queryB2CFailCount()));
			map.put("innerCount", String.valueOf(remindDao.queryinnerFailCount()));
			map.put("complain",String.valueOf(remindDao.querycomplainCount()));
			map.put("bxCount",String.valueOf(remindDao.querybxCount()));
			map.put("bookExtCount",String.valueOf(remindDao.bookExtCount()));
			map.put("bookQunarCount",String.valueOf(remindDao.bookQunarCount()));
			map.put("bookElongCount",String.valueOf(remindDao.bookElongCount()));
			map.put("bookTcCount",String.valueOf(remindDao.bookTcCount()));
			
			map.put("bookGtCount",String.valueOf(remindDao.bookGtCount()));
			map.put("bookMtCount",String.valueOf(remindDao.bookMtCount()));
			map.put("bookTuniuCount",String.valueOf(remindDao.bookTuniuCount()));
			
			map.put("refundExtCount",String.valueOf(remindDao.refundExtCount()));
			map.put("refundQunarCount",String.valueOf(remindDao.refundQunarCount()));
			map.put("refundElongCount",String.valueOf(remindDao.refundElongCount()));
			map.put("refundTcCount",String.valueOf(remindDao.refundTcCount()));
			
			map.put("alterCount",String.valueOf(remindDao.alterCount()));
		return map;
	}

	@Override
	public Map<String, String> queryRobotCounts() {
		Map<String, String> robotMap = new HashMap<String, String>();
		List<Map<String, Object>> list=remindDao.queryRobotCount();
		for(int i=0;i<list.size();i++){
			robotMap.put(String.valueOf(list.get(i).get("worker_type")),String.valueOf( list.get(i).get("count")));
		}
		return robotMap;
	}
	
	@Override
	public String codeQunarType() {
		return remindDao.codeQunarType();
	}

	@Override
	public int queryAdminCurrentNameCount() {
		return remindDao.queryAdminCurrentNameCount();
	}

	@Override
	public int queryCodeCountToday() {
		return remindDao.queryCodeCountToday();
	}

	@Override
	public int queryCodeToday() {
		return remindDao.queryCodeToday();
	}

	@Override
	public int queryUncode(String channel) {
		return remindDao.queryUncode(channel);
	}

	@Override
	public int queryAdminCurrentNameCount2(Map<String, Object> paramMap1) {
		return remindDao.queryAdminCurrentNameCount2(paramMap1);
	}

	@Override
	public int queryWaitCodeQueenCount(String department) {
		return remindDao.queryWaitCodeQueenCount(department);
	}

	@Override
	public List<Map<String, String>> queryAccountList() {
		return remindDao.queryAccountList();
	}

	@Override
	public List<Map<String, String>> queryAccountMarginList(
			HashMap<String, Integer> map) {
		return remindDao.queryAccountMarginList(map);
	}

	@Override
	public List<Map<String, String>> queryAccountContactList(
			HashMap<String, Integer> map) {
		return remindDao.queryAccountContactList(map);
	}

	@Override
	public int queryOrderNumber() {
		return remindDao.queryOrderNumber();
	}

	@Override
	public int queryRobotNumber(String string) {
		return remindDao.queryRobotNumber(string);
	}

	@Override
	public List<Map<String, String>> queryZhifubaoMoney(String name) {
		return remindDao.queryZhifubaoMoney(name);
	}


}
