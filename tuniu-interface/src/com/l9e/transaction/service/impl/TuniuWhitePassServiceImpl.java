package com.l9e.transaction.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.TuniuWhitePassDao;
import com.l9e.transaction.service.TuniuWhitePassService;
import com.l9e.transaction.vo.TuniuWhitePass;
@Service("tuniuWhitePassService")
public class TuniuWhitePassServiceImpl implements TuniuWhitePassService{
	@Resource
	private TuniuWhitePassDao tuniuWhitePassDao;
	@Override
	public List<TuniuWhitePass> getWhitePassList(int lastId, int num) {
		Map<String ,Object> param = new HashMap<String,Object>();
		param.put("lastId", lastId);
		param.put("num", num);
		return tuniuWhitePassDao.getWhitePassList(param);
	}

}
