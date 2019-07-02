package com.train.system.center.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.train.system.center.entity.Refund;

public interface RefundDao {

	List<Refund> queryListWaitRefund(@Param("limit") int limit);

	int updateById(@Param("refund") Refund refund, @Param("whereOrderStatus") String orderStatus);

	Refund queryByOrderIdAndSequence(@Param("orderId") String orderId, @Param("subSequence") String subSequence);

	int insertBatchUnder(@Param("refundList")List<Refund> refundList);

}
