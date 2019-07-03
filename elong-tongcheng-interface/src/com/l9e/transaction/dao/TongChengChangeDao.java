package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.DBChangeInfo;
import com.l9e.transaction.vo.DBPassengerChangeInfo;
import com.l9e.transaction.vo.TongchengChangeLogVO;

/**
 * 同城同步、异步改签dao
 * @author licheng
 *
 */
public interface TongChengChangeDao {

	/*改签业务*/
	void insertChangeCp(DBPassengerChangeInfo cPassenger);
	void insertChangeInfo(DBChangeInfo changeInfo);
	List<DBPassengerChangeInfo> selectChangeCp(Map<String, Object> param);
	void updateChangeCp(DBPassengerChangeInfo cPassenger);
	DBPassengerChangeInfo selectChangePassenger(Map<String, Object> param);
	DBChangeInfo getChangeInfo(Map<String, Object> param);
	void updateChangeInfo(DBChangeInfo changeInfo);
	String selectAccountId(String order_id);
	List<String> selectReqtokens(String orderId);
	
	List<DBChangeInfo> selectTimeoutRequestChange();
	List<DBChangeInfo> selectTimeoutConfirmChange();
	
	/*改签回调*/
	List<DBChangeInfo> selectNotifyList();
	int updateNotifyBegin(DBChangeInfo changeInfo);
	void updateNotifyEnd(DBChangeInfo changeInfo);
	DBPassengerChangeInfo selectChangePassengerByCpId(String cp_id);
	
	/*日志*/
	void insertChangeLog(TongchengChangeLogVO log);
}
