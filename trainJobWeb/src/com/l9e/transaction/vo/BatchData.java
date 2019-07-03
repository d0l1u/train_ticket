package com.l9e.transaction.vo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class BatchData {

	/**
	 * 插入标记
	 */
	public static final int INSERT = 0;
	/**
	 * 更新标记
	 */
	public static final int UPDATE = 1;
	/**
	 * 删除标记
	 */
	public static final int DELETE = 2;

	/**
	 * 操作
	 */
	private int opter = -1;
	/**
	 * 表名
	 */
	private String tableName;

	/**
	 * 插入和更新时的字段数据
	 */
	private LinkedHashMap<String, Object> data = new LinkedHashMap<String, Object>();
	/**
	 * 主键名称
	 */
	private String pkName;
	/**
	 * 主键值
	 */
	private Object pkValue;

	public BatchData() {

	}

	public BatchData(int opter, String tableName) {
		super();
		this.opter = opter;
		this.tableName = tableName;
	}

	/**
	 * 插入时需要的字段字符串
	 * 
	 * @return
	 */
	public String getColumnString() {

		StringBuilder sb = new StringBuilder();
		for(String field : data.keySet()) {
			sb.append(field).append(",");
		}
		sb.setLength(sb.length() - 1);
		return sb.toString();
	}
	
	public List<Object> iterateData() {
		return new ArrayList<Object>(data.entrySet());
	}

	/**
	 * 插入时数据有序集合(与字段字符串一一对应)
	 * 
	 * @return
	 */
	public List<Object> dataValues() {
		return new ArrayList<Object>(data.values());
	}

	public int getOpter() {
		return opter;
	}

	public void setOpter(int opter) {
		this.opter = opter;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public LinkedHashMap<String, Object> getData() {
		return data;
	}

	public void setData(LinkedHashMap<String, Object> data) {
		this.data = data;
	}

	public String getPkName() {
		return pkName;
	}

	public void setPkName(String pkName) {
		this.pkName = pkName;
	}

	public Object getPkValue() {
		return pkValue;
	}

	public void setPkValue(Object pkValue) {
		this.pkValue = pkValue;
	}

	@Override
	public String toString() {
		return "BatchData [data=" + data + ", opter=" + opter + ", pkName="
				+ pkName + ", pkValue=" + pkValue + ", tableName=" + tableName
				+ "]";
	}

	
}
