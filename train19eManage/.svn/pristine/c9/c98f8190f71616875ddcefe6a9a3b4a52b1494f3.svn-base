package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.NeedRepayDao;
import com.l9e.transaction.service.NeedRepayService;

@Service("NeedRepayService")
public class NeedRepayServiceImpl implements NeedRepayService{
	@Resource
	private NeedRepayDao needRepayDao ;
	public List<String> queryNeedRepay() {
		return needRepayDao.queryNeedRepay();
	}
	public void updateNeedRepay(String user_id) {
		needRepayDao.updateNeedRepay(user_id);
	}


}
