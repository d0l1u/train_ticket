package com.l9e.transaction.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.stereotype.Repository;

import com.ibatis.sqlmap.client.SqlMapExecutor;
import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.BackupDao;
import com.l9e.transaction.vo.BatchData;

@Repository("backupDao")
public class BackupDaoImpl extends BaseDao implements BackupDao {
	private Logger logger = Logger.getLogger(BackupDaoImpl.class);

	@Override
	public void backupCpInfo(Map<String, String> param) {
		this.getSqlMapClientTemplate().insert("backup.backupCpInfo", param);
	}

	@Override
	public void backupOrderInfo(Map<String, String> param) {
		this.getSqlMapClientTemplate().insert("backup.backupOrderInfo", param);
	}

	@Override
	public int updateOrderStatus(String beginTime) {
		return this.getSqlMapClientTemplate().update("backup.updateOrderStatus", beginTime);
	}

	@Override
	public void backupCpOrderNotifyInfo(Map<String, String> param) {
		this.getSqlMapClientTemplate().insert("backup.backupCpOrderNotifyInfo", param);
	}

	@Override
	public List<Map<String, Object>> getRecords(Map<String, Object> param) {
		return this.getSqlMapClientTemplate().queryForList("backup.queryRecords", param);
	}

	@Override
	public int batchInsert(final String backupTableName, final String columnString, final List<Map<String, Object>> records) {
		return (Integer) this.getSqlMapClientTemplate().execute(new SqlMapClientCallback<Object>() {

			@Override
			public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
				executor.startBatch();
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("tableName", backupTableName);
				paramMap.put("columnString", columnString);
				for (int i = 0; i < records.size(); i++) {
					List<Object> values = new ArrayList<Object>(records.get(i).values());
					paramMap.put("values", values);
					executor.insert("backup.insertBatch", paramMap);
				}

				int lines = executor.executeBatch();
				return lines;
			}
		});
	}

	@Override
	public int batchDelete(final String tableName, final String primaryKeyName, final List<String> primaryKeys) {
		return (Integer) this.getSqlMapClientTemplate().execute(new SqlMapClientCallback<Object>() {

			@Override
			public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
				executor.startBatch();
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("tableName", tableName);
				paramMap.put("primaryKeyName", primaryKeyName);
				for (String key : primaryKeys) {
					paramMap.put("key", key);
					executor.delete("backup.deleteBatch", paramMap);
				}
				int lines = executor.executeBatch();
				return lines;
			}
		});
	}

	@Override
	public List<Map<String, Object>> selectColumns(String tableName) {
		return this.getSqlMapClientTemplate().queryForList("backup.queryColumns", tableName);
	}

	@Override
	public int batchExecute(final List<BatchData> batchDatas) {
		return this.getSqlMapClientTemplate().execute(new SqlMapClientCallback<Integer>() {

			@Override
			public Integer doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
				executor.startBatch();

				// 参数集合
				Map<String, Object> paramMap = new HashMap<String, Object>();

				// //插入
				// for(BatchData bd : inserts) {
				// paramMap.clear();
				// paramMap.put("tableName", bd.getTableName());//表名
				// paramMap.put("columnString",
				// bd.getColumnString());//要插入的字段字符串,
				// column1,column2,column3,....
				// paramMap.put("values", bd.dataValues());//与字段对应的值集合
				// paramMap.put("data", bd.iterateData());//完整数据集合，用于更新
				// executor.insert("backup.insertBatch", paramMap);
				// }
				// int insert = executor.executeBatch();
				//
				// //删除
				// for(BatchData bd : deletes) {
				// paramMap.clear();
				// paramMap.put("tableName", bd.getTableName());//表名
				// paramMap.put("primaryKeyName", bd.getPkName());//主键名称
				// paramMap.put("key", bd.getPkValue());//主键值
				// executor.delete("backup.deleteBatch", paramMap);
				// }
				// int delete = executor.executeBatch();
				for (BatchData bd : batchDatas) {
					paramMap.clear();
					if (bd.getOpter() == BatchData.INSERT) {// 插入
						paramMap.put("tableName", bd.getTableName());// 表名
						paramMap.put("columnString", bd.getColumnString());// 要插入的字段字符串,
																			// column1,column2,column3,....
						paramMap.put("values", bd.dataValues());// 与字段对应的值集合
						paramMap.put("data", bd.iterateData());// 完整数据集合，用于更新
						executor.insert("backup.insertBatch", paramMap);
					} else if (bd.getOpter() == BatchData.UPDATE) {// 更新
						// 暂无实现

					} else if (bd.getOpter() == BatchData.DELETE) {// 删除
						paramMap.put("tableName", bd.getTableName());// 表名
						paramMap.put("primaryKeyName", bd.getPkName());// 主键名称
						paramMap.put("key", bd.getPkValue());// 主键值
						executor.delete("backup.deleteBatch", paramMap);
					} else {
						logger.info("不合法的批处理输入记录,必须是insert,update,delete的一种!" + bd);
					}
				}
				return executor.executeBatch();
			}
		});
	}

	@Override
	public int batchDelete(final List<BatchData> batchDatas) {
		return (Integer) this.getSqlMapClientTemplate().execute(new SqlMapClientCallback<Object>() {

			@Override
			public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
				if (batchDatas.size() == 0)
					return 0;
				executor.startBatch();
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("tableName", batchDatas.get(0).getTableName());
				paramMap.put("primaryKeyName", batchDatas.get(0).getPkName());
				for (BatchData bd : batchDatas) {
					paramMap.put("key", bd.getPkValue());
					System.out.println(paramMap);
					executor.delete("backup.deleteBatch", paramMap);
				}

				int lines = executor.executeBatch();
				return lines;
			}
		});
	}

	@Override
	public int batchInsert(final List<BatchData> batchDatas) {
		return (Integer) this.getSqlMapClientTemplate().execute(new SqlMapClientCallback<Object>() {

			@Override
			public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
				if (batchDatas.size() == 0)
					return 0;
				executor.startBatch();
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("tableName", batchDatas.get(0).getTableName());// 表名
				paramMap.put("columnString", batchDatas.get(0).getColumnString());// 要插入的字段字符串,
																					// column1,column2,column3,....
				for (BatchData bd : batchDatas) {
					paramMap.put("values", bd.dataValues());// 与字段对应的值集合
					paramMap.put("data", bd.iterateData());// 完整数据集合，用于更新
					System.out.println(paramMap);
					executor.insert("backup.insertBatch", paramMap);
				}

				int lines = executor.executeBatch();
				return lines;
			}
		});
	}

	/*
	 * @Override public void testDDL(String ddlString) {
	 * this.getSqlMapClientTemplate().update("backup.createTable"); }
	 */

}
