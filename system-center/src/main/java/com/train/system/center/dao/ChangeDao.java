package com.train.system.center.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.train.system.center.entity.Change;
import com.train.system.center.entity.Passenger;

public interface ChangeDao {

	Passenger queryCp(@Param("orderId") String orderId, @Param("subSequence") String subSequence);

	List<Change> queryWaitChange(@Param("limit") int limit);

	int updateOrder(@Param("change") Change change, @Param("status") String status);

	Change queryByChangeId(@Param("changeId") Integer changeId);

	List<Change> queryWaitCancel(@Param("limit") int limit);

	List<Change> queryWaitPay(@Param("limit") int limit);

	List<Change> queryWaitUpadate(@Param("limit") int limit);

}
