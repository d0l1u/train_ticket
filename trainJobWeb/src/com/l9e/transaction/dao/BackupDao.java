package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.BatchData;

public interface BackupDao {
	void backupOrderInfo(Map<String,String> param);
	void backupCpInfo(Map<String,String> param);
	void backupCpOrderNotifyInfo(Map<String,String> param);
	int  updateOrderStatus(String beginTime);
	
	List<Map<String, Object>> getRecords(Map<String, Object> param);
	
	int batchInsert(String backupTableName, String columnString, List<Map<String, Object>> param);
	int batchDelete(String tableName, String primaryKeyName, List<String> primaryKeys);
	
	List<Map<String, Object>> selectColumns(String tableName);
	//void testDDL(String ddlString);
	
	int batchExecute(List<BatchData> batchDatas);
	
	//
	int batchInsert(List<BatchData> batchDatas);
	int batchDelete(List<BatchData> batchdatas);
}
