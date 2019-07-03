package com.l9e.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.BatchData;

/**
 * 备份辅助工具
 * @author licheng
 *
 */
public class BackupUtil {
	
	private static int co_count;
	private static int coc_count;
	private static int coh_count;
	private static int con_count;
	
	/**
	 * 获取列名集合
	 * @param columns 列信息
	 * @return
	 */
	public static List<String> fields(List<Map<String, Object>> columns, String key) {
		
		List<String> fields = new ArrayList<String>();
		for(Map<String, Object> col : columns) {
			String fieldName = (String) col.get(key);
			fields.add(fieldName);
		}
		
		return fields;
	}
	
	/**
	 * 解析表名参数
	 * @param tableNames 主表名(原表):副表名1,副表名2,...
	 * @param backupTableNames 主表名(备份表):副表名1,副表名2,...
	 * @return
	 */
	public static Map<String, String> tableNameEntry(String tableNames, String backupTableNames) {
		//除主表外，所有副表从前往后的优先级从高到低，使用LinkedHashMap
		Map<String, String> tableEntrys = new LinkedHashMap<String, String>();
		String[] tn = tableNames.split(":");//{"主表","副表,副表,副表,..."}
		String[] btn = backupTableNames.split(":");//{"主表","副表,副表,副表,..."}
		String[] f_tn = tn[1].split(",");
		String[] f_btn = btn[1].split(",");
		//副表
		for(int i = 0; i < f_tn.length;i++) {
			String tname = f_tn[i];
			String btname = f_btn[i];
			tableEntrys.put(tname, btname);
		}
		//主表
		tableEntrys.put(tn[0], btn[0]);
		return tableEntrys;
	}
	
	public static void amount(String tableLag, int count) {
		if("co".equals(tableLag)) {
			co_count+=count;
		} else if("coc".equals(tableLag)){
			coc_count+=count;
		} else if("coh".equals(tableLag)){
			coh_count+=count;
		} else if("con".equals(tableLag)){
			con_count+=count;
		}
	}
	
	public static String countString() {
		try{
			String result = "备份记录信息: cp_orderinfo 表 ---> " + co_count
							+ "条记录, cp_orderinfo_cp 表 ---> " + coc_count
							+ "条记录, cp_orderinfo_history 表 ---> " + coh_count
							+ "条记录, cp_orderinfo_notify 表 ---> " + con_count;
			return result;
		} finally {
			co_count = 0;
			coc_count = 0;
			coh_count = 0;
			con_count = 0;
		}
	}
	
}
