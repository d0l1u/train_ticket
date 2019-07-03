package com.l9e.util;

import org.apache.log4j.Logger;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
//动态切换数据源工具类
public class DynamicDataSource extends AbstractRoutingDataSource {

	static Logger log = Logger.getLogger("DynamicDataSource");

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource#
	 * determineCurrentLookupKey()
	 */

	protected Object determineCurrentLookupKey() {

		return DbContextHolder.getDbType();

	}

}
