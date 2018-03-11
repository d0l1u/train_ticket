package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.CpInfoCensusDao;
import com.l9e.transaction.service.CpInfoCensusService;
	@Service("cpInfoCensusService")
public class CpInfoCensusServiceImpl implements CpInfoCensusService{
		@Resource
		private  CpInfoCensusDao cpInfoCensusDao;

		public int queryPre_day_order_succeed(Map<String, String> query_Map) {
			return cpInfoCensusDao.queryPre_day_order_succeed(query_Map);
		}

		public int queryPre_day_order_defeated(Map<String, String> query_Map) {
			return cpInfoCensusDao.queryPre_day_order_defeated(query_Map);
		}

		public double queryPre_succeed_money(Map<String, String> query_Map) {
			return cpInfoCensusDao.queryPre_succeed_money(query_Map);
		}

		public double queryPre_defeated_money(Map<String, String> query_Map) {
			return cpInfoCensusDao.queryPre_defeated_money(query_Map);
		}

		public int queryPre_ticket_count(Map<String, String> query_Map) {
			return cpInfoCensusDao.queryPre_ticket_count(query_Map);
		}

		public void addCensusTocp_statInfo(Map<String, Object> add_Map_19e,
				Map<String, Object> add_Map_qunar) {
			cpInfoCensusDao.addCensusTocp_statInfo_19e(add_Map_19e);
			cpInfoCensusDao.addCensusTocp_statInfo_qunar(add_Map_qunar);
		}

		public int query_table_count() {
			return cpInfoCensusDao.query_table_count();
		}

		public List<Map<String, String>> queryDateList() {
			return cpInfoCensusDao.queryDateList();
		}

		
}
