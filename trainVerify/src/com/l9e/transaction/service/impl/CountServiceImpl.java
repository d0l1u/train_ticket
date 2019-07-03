package com.l9e.transaction.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.CountDao;
import com.l9e.transaction.service.CountService;
import com.l9e.transaction.vo.Count;
@Service("countService")
public class CountServiceImpl implements CountService {
	
	@Resource
	private CountDao countDao;
	
	public void insertIntoCount(Count count) {
		countDao.insertIntoCount(count);
	}

}
