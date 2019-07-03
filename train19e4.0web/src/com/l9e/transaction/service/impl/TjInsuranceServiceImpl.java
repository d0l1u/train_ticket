package com.l9e.transaction.service.impl;

import java.util.HashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.TjInsuranceDao;
import com.l9e.transaction.service.TjInsuranceService;
@Service("tjInsuranceService")
public class TjInsuranceServiceImpl implements TjInsuranceService{
	@Resource
	private TjInsuranceDao tjInsuranceDao;
	@Override
	public void clickNumAdd(HashMap<String, Object> map) {
		 tjInsuranceDao.clickNumAdd(map);
	}
	@Override
	public int clickNumUpdate(HashMap<String, Object> map) {
		return tjInsuranceDao.clickNumUpdate(map);
	}
	@Override
	public int queryclickNum(HashMap<String, Object> map) {
		return tjInsuranceDao.queryclickNum(map);
	}

}
