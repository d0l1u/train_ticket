package com.l9e.transaction.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.BackupDao;
import com.l9e.transaction.service.BackupService;
import com.l9e.transaction.vo.BatchData;
import com.l9e.util.BackupUtil;
import com.l9e.util.DateUtil;

/**
 * 表数据备份业务实现new
 * @author licheng
 *
 */
@Service("backupServiceNew")
public class BackupServiceImplNew implements BackupService {
	
	private static final Logger logger = Logger.getLogger(BackupServiceImpl.class);
	
	@Resource
	private BackupDao backupDao;
	
	@Override
	public void addBackup(Date begin, Date end) {
		try {
			
			//要备份的数据起始和结束时间
			Map<String,Object> param=new HashMap<String, Object>();
			param.put("begin", DateUtil.dateToString(begin, "yyyy-MM-dd HH:mm:ss"));
			param.put("end", DateUtil.dateToString(end, "yyyy-MM-dd HH:mm:ss"));
			param.put("limit", LIMIT);
			
			//备份
//			backupDao.backupCpInfo(param);//cp_orderinfo_cp -> cp_orderinfo_cp_backup
//			backupDao.backupOrderInfo(param);//cp_orderinfo -> cp_orderinfo_backup
//			backupDao.backupCpOrderNotifyInfo(param);//cp_orderinfo_notify -> cp_orderinfo_notify_backup
			
			doBackup(param);
			
			//备份成功
			logger.info(BackupUtil.countString());
			logger.info("日期下:"+DateUtil.dateToString(begin, "yyyy-MM-dd HH:mm:ss")+"订单、车票信息日备份【成功】");
		} catch (Exception e) {
			e.printStackTrace();
			//备份出错
			logger.info("日期下:"+DateUtil.dateToString(begin, "yyyy-MM-dd HH:mm:ss")+"订单、车票信息日备份发生异常"+e);
		}

	}

	@Override
	public void addBackupAccountFilter() {
		//
	}

	@Override
	public int updateOrderStatus(String beginTime) {
		return backupDao.updateOrderStatus(beginTime);
	}
	
	/**
	 * 备份指定日期范围内的表记录
	 * @param param 参数(begin：开始日期yyyy-MM-dd HH:mm:ss, end：结束日期yyyy-MM-dd HH:mm:ss,limit: 拉取原表数据最大数)
	 */
	public void doBackup(Map<String, Object> param) {
		
		//表名
		//cp_orderinfo
		String coName = "cp_orderinfo";
		String cobName = "cp_orderinfo_backup";
//		String coName = "cp_orderinfo_test";
//		String cobName = "cp_orderinfo_bak";
		
		//cp_orderinfo_cp
		String cocName = "cp_orderinfo_cp";
		String cocbName = "cp_orderinfo_cp_backup";
//		String cocName = "cp_orderinfo_cp_test";
//		String cocbName = "cp_orderinfo_cp_bak";
		
		//cp_orderinfo_history
		String cohName = "cp_orderinfo_history";
		String cohbName = "cp_orderinfo_history_backup";
//		String cohName = "cp_orderinfo_history_test";
//		String cohbName = "cp_orderinfo_history_bak";
		
		//cp_orderinfo_notify
		String conName = "cp_orderinfo_notify";
		String conbName = "cp_orderinfo_notify_backup";
//		String conName = "cp_orderinfo_notify_test";
//		String conbName = "cp_orderinfo_notify_bak";
		
		String pk = "order_id";//主键名
		String coc_pk = "cp_id";
		String coh_pk = "history_id";
		String con_pk = "order_id";
		param.put("tableName", coName);
		//字段字符串
		String columnString = preColumnString(coName, cobName);
			
		param.put("columnString", columnString);
		//cp_orderinfo_cp
		String coc_columnString = preColumnString(cocName, cocbName);
		//cp_orderinfo_history
		String coh_columnString = preColumnString(cohName, cohbName);
		//cp_orderinfo_notify
		String con_columnString = preColumnString(conName, conbName);
		//获取原表数据
		List<Map<String, Object>> records = backupDao.getRecords(param);
		
		List<BatchData> batchDatas = new ArrayList<BatchData>();
		
		Map<String, Object> subParam = new HashMap<String, Object>();
		subParam.put("conditionName", pk);
		subParam.put("limit", LIMIT);
		while(records.size() != 0) {
			if(records.size() == 0) break;
			
			//每BackupService.BATCHCOUNT条记录批量备份一次
			for(int i = 0; i < records.size();i++) {
				
				Map<String, Object> record = records.get(i);
				String pkValue = (String) record.get(pk);//cp_orderinfo主键值
				subParam.put("conditionValue", pkValue);
				
				//根据主键先备份子表
				//子表cp_orderinfo_cp
				subParam.put("tableName", cocName);
				subParam.put("columnString", coc_columnString);
				//拉取数据
				List<Map<String, Object>> sub_records = backupDao.getRecords(subParam);
				if(sub_records.size() != 0) {//拉取出数据则添加到批处理队列
					batchDatas.addAll(batchData(BatchData.INSERT, cocbName, sub_records, coc_pk));
					batchDatas.addAll(batchData(BatchData.DELETE, cocName, sub_records, coc_pk));
					//统计
					BackupUtil.amount("coc", sub_records.size());
				}
				//子表cp_orderinfo_history
				subParam.put("tableName", cohName);
				subParam.put("columnString", coh_columnString);
				//拉取数据
				sub_records = backupDao.getRecords(subParam);
				if(sub_records.size() != 0) {//拉取出数据则添加到批处理队列
					batchDatas.addAll(batchData(BatchData.INSERT, cohbName, sub_records, coh_pk));
					batchDatas.addAll(batchData(BatchData.DELETE, cohName, sub_records, coh_pk));
					BackupUtil.amount("coh", sub_records.size());
				}
				//子表cp_orderinfo_notify
				subParam.put("tableName", conName);
				subParam.put("columnString", con_columnString);
				//拉取数据
				sub_records = backupDao.getRecords(subParam);
				if(sub_records.size() != 0) {//拉取出数据则添加到批处理队列
					batchDatas.addAll(batchData(BatchData.INSERT, conbName, sub_records, con_pk));
					batchDatas.addAll(batchData(BatchData.DELETE, conName, sub_records, con_pk));
					BackupUtil.amount("con", sub_records.size());
				}
				
				
				//子表完毕
				BatchData insertData = new BatchData(BatchData.INSERT, cobName);
				BatchData deleteData = new BatchData(BatchData.DELETE, coName);
				insertData.setData((LinkedHashMap)record);
				deleteData.setPkName(pk);
				deleteData.setPkValue(record.get(pk));
				batchDatas.add(insertData);
				batchDatas.add(deleteData);
				BackupUtil.amount("co", 1);
				
				if(batchDatas.size() > BackupService.BATCHCOUNT) {
					backupFlush(batchDatas);
				}
			}
		
			
			backupFlush(batchDatas);
			records = backupDao.getRecords(param);
		}
		
		
	}

	/**
	 * 备份指定日期范围内的表记录
	 * @param tableName 需要备份的表名
	 * @param backupTableName 备份目标表名
	 * @param param 参数(begin：开始日期yyyy-MM-dd HH:mm:ss, end：结束日期yyyy-MM-dd HH:mm:ss,limit: 拉取原表数据最大数)
	 */
	public void backup(String tableName, String backupTableName, Map<String, Object> param) {
		
		String columnString = "";//字段字符串
		String pk = "";//主键名
		List<String> primaryKeys = new ArrayList<String>();//默认第一个字段为主键
		param.put("tableName", tableName);
		
		//查询备份目标表的列信息
		List<Map<String, Object>> columns = backupDao.selectColumns(backupTableName);
		
		//查询原表的列信息
		List<Map<String, Object>> cols = backupDao.selectColumns(tableName);
		
		//生成字段字符串时，原表有而备份表无的字段不受影响
		List<String> backupFields = BackupUtil.fields(columns, "Field");//备份表列名集合
		List<String> fields = BackupUtil.fields(cols, "Field");//原表列名集合
		
		//生成字段字符串
		StringBuilder sb = new StringBuilder();
		for(String field : backupFields) {
			//备份表中存在而原表中不存在的字段去除
			if(!fields.contains(field)) continue;
			
			if("".equals(pk)) {//主键名称
				pk = field;
			}
			sb.append(field).append(",");
		}
		sb.setLength(sb.length() - 1);
		
		columnString = sb.toString();
			
		param.put("columnString", columnString);
		//获取原表数据
		List<Map<String, Object>> records = backupDao.getRecords(param);
		while(records.size() != 0) {
			if(records.size() == 0) break;
			
			List<Map<String, Object>> subRecords = new ArrayList<Map<String, Object>>();
			//每BackupService.BATCHCOUNT条记录批量备份一次
			for(int i = 0; i < records.size();i++) {
				
				subRecords.add(records.get(i));
				primaryKeys.add((String)records.get(i).get(pk));
				
				if((i + 1) % BackupService.BATCHCOUNT == 0) {
					
					try {
						backupBatch(tableName, backupTableName, columnString, pk, subRecords, primaryKeys);
						
					} catch (Exception e) {
						e.printStackTrace();
						//备份出错
						logger.info("主键" + pk + " : " + (String)records.get(i).get(pk) + " 之前的 " + BackupService.BATCHCOUNT + " 条记录备份失败,异常:" + e);
					}
					//清除上一批数据
					subRecords.clear();
					primaryKeys.clear();
				}
				
			}
			if(subRecords.size() != 0) {
				try {
					backupBatch(tableName, backupTableName, columnString, pk, subRecords, primaryKeys);
					
				} catch (Exception e) {
					e.printStackTrace();
					//备份出错
					logger.info("最后 " + BackupService.BATCHCOUNT + " 条记录备份失败,异常:" + e);
				}
				//清除上一批数据
				subRecords.clear();
				primaryKeys.clear();
			}
			
			records = backupDao.getRecords(param);
		}
		
	}
	
	/**
	 * 批量备份
	 * @param tableName 需要备份的表名
	 * @param backupTableName 备份目标表名
	 * @param columnString 表字段字符串(column1,column2,column3,...)
	 * @param pk 主键名
	 * @param records 本批次记录
	 * @param primaryKeys 本批次记录主键集合
	 */
	public void backupBatch(String tableName, String backupTableName, String columnString, String pk, List<Map<String, Object>> records, List<String> primaryKeys) {
		//插入数据到备份表
		backupDao.batchInsert(backupTableName, columnString, records);
		//删除原表数据
		backupDao.batchDelete(tableName, pk, primaryKeys);
	}
	
	public String preColumnString(String table, String bakTable) {
		String columnString = "";
		//查询备份目标表的列信息
		List<Map<String, Object>> columns = backupDao.selectColumns(bakTable);
		
		//查询原表的列信息
		List<Map<String, Object>> cols = backupDao.selectColumns(table);
		
		//生成字段字符串时，原表有而备份表无的字段不受影响
		List<String> backupFields = BackupUtil.fields(columns, "Field");//备份表列名集合
		List<String> fields = BackupUtil.fields(cols, "Field");//原表列名集合
		
		//生成字段字符串
		StringBuilder sb = new StringBuilder();
		for(String field : backupFields) {
			//备份表中存在而原表中不存在的字段去除
			if(!fields.contains(field)) continue;
			
			sb.append(field).append(",");
		}
		sb.setLength(sb.length() - 1);
		
		columnString = sb.toString();
		return columnString;
	}
	
	public List<BatchData> batchData(int opter, String tableName, List<Map<String, Object>> records, String pkName) {
		List<BatchData> batchData = new ArrayList<BatchData>();
		if(opter == BatchData.INSERT) {
			for(Map<String, Object> record : records) {
				BatchData data = new BatchData(opter, tableName);
				data.setData((LinkedHashMap)record);
				batchData.add(data);
			}
		} else if (opter == BatchData.UPDATE) {
			//暂无实现
		} else if(opter == BatchData.DELETE) {
			for(Map<String, Object> record : records) {
				BatchData data = new BatchData(opter, tableName);
				data.setPkName(pkName);
				data.setPkValue(record.get(pkName));
				batchData.add(data);
			}
		} else {
			
		}
		return batchData;
	}
	
	public void backupFlush(List<BatchData> batchDatas) {
		backupDao.batchExecute(batchDatas);
		batchDatas.clear();
	}
}
