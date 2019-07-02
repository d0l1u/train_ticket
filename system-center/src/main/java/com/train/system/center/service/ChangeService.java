package com.train.system.center.service;

import java.util.List;

import com.train.system.center.entity.Change;

public interface ChangeService {

	List<Change> queryWaitChange(int limit);

	int updateOrder(Change change, boolean updatePassenger, String changeStatus);

	Change queryByChangeId(Integer changeId);

	List<Change> queryWaitCancel(int limit);

	List<Change> queryWaitPay(int limit);

	List<Change> queryWaitUpadate(int limit);

}
