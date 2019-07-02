package com.train.system.center.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.train.system.center.dao.ChangeDao;
import com.train.system.center.dao.PassengerDao;
import com.train.system.center.entity.Change;
import com.train.system.center.entity.Passenger;
import com.train.system.center.service.ChangeService;

@Service("changeService")
public class ChangeServiceImpl implements ChangeService {
	private Logger logger = LoggerFactory.getLogger(ChangeServiceImpl.class);

	@Resource
	private ChangeDao changeDao;

	@Resource
	private PassengerDao passengerDao;

	@Override
	public List<Change> queryWaitChange(int limit) {
		return changeDao.queryWaitChange(limit);
	}

	@Override
	public int updateOrder(Change change, boolean updatePassenger, String changeStatus) {
		List<Passenger> passengers = change.getPassengerList();
		for (int i = 0; updatePassenger && passengers != null && i < passengers.size(); i++) {
			// 更新乘客数据
			int update = passengerDao.updateChangePassenger(passengers.get(i));
			logger.info("Update Change Passenger Result:{}", update);
		}
		return changeDao.updateOrder(change, changeStatus);
	}

	@Override
	public Change queryByChangeId(Integer changeId) {
		return changeDao.queryByChangeId(changeId);
	}

	@Override
	public List<Change> queryWaitCancel(int limit) {
		return changeDao.queryWaitCancel(limit);
	}

	@Override
	public List<Change> queryWaitPay(int limit) {
		return changeDao.queryWaitPay(limit);
	}

	@Override
	public List<Change> queryWaitUpadate(int limit) {
		return changeDao.queryWaitUpadate(limit);
	}
}
