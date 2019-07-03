package com.l9e.transaction.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.l9e.transaction.vo.Midway;

public class MidwayDao extends BaseDao {

	private Logger logger = Logger.getLogger(MidwayDao.class);

	public List<Midway> queryMidwayFromSinfo(
			Map<String, Object> map) {
		List<Midway> midwayList = new ArrayList<Midway>();

		logger.isDebugEnabled();
		logger.info("[途经站]查询map：" + map);
		try {
			midwayList = this.getSqlMapClient().queryForList(
					"midway.queryMidwayFromSinfo", map);

		} catch (SQLException e) {
			logger.error("[途经站]查询是否有该车次信息" + e.getMessage());
			e.printStackTrace();
		}
		return midwayList;
	}

	public List<Midway> queryMidway(Map<String, Object> map) {
		List<Midway> midwayList = new ArrayList<Midway>();
		try {
			this.getSqlMapClient().queryForList("midway.queryMidway", map);
		} catch (SQLException e) {
			logger.error("��midway�в�ѯ;��վ��Ϣʱ�������" + e.getMessage());
		}
		return midwayList;
	}

	public void insertMidway(List<Midway> midwayList) {
		// int updateCount = 0;
		try {
			// updateCount =
			this.getSqlMapClient().insert("midway.insertMidway", midwayList);
		} catch (SQLException e) {
			logger.error("��midway�в���;��վʱ�������" + e.getMessage());
		}
		// logger.info("[midway����]����" + updateCount + "��;��վ��Ϣ");
	}

	public void insertMidwayIntoSinfo(List<Midway> midwayList) {
		// int updateCount = 0;
		try {
			// updateCount =
			this.getSqlMapClient().insert("midway.insertMidwayIntoSinfo",
					midwayList);
		} catch (SQLException e) {
			logger.error("��sinfo�в���;��վʱ�������" + e.getMessage());
			e.printStackTrace();
		}
		// logger.info("[sinfo����]����" + updateCount + "��;��վ��Ϣ");
	}

	public void insertMidwayIntoSinfo(Midway midway) {
		// int updateCount = 0;
		try {
			// updateCount =
			this.getSqlMapClient().insert("midway.insertMidwayVoIntoSinfo",
					midway);
		} catch (SQLException e) {
			logger.error("[途经站]插入信息失败：" + e.getMessage());
			e.printStackTrace();
		}
		// logger.info("[sinfo����]����" + updateCount + "��;��վ��Ϣ");
	}

	public void updateMidway(Midway midway) {
		int updateCount = 0;
		try {
			updateCount = this.getSqlMapClient().update("midway.updateMidway",
					midway);
		} catch (SQLException e) {
			logger.error("����midway��;��վʱ�������" + e.getMessage());
		}
		logger.info("[midway����]����" + updateCount + "��;��վ��Ϣ");
	}

	public void updateMidwayOfSinfo(Midway midway) {
		int updateCount = 0;
		try {
			updateCount = this.getSqlMapClient().update("midway.updateMidwayOfTsinfo",
					midway);
		} catch (SQLException e) {
			logger.error("[途经站]更新失败：" + e.getMessage());
			e.printStackTrace();
		}
		logger.info("[途经站]更新" + updateCount + "条数据");
	}

	public void deleteMidwayFromSinfo(Map<String, Object> map) {
		int deleteCount = 0;
		try {
			deleteCount = this.getSqlMapClient().delete(
					"midway.deleteMidwayFromSinfo", map);
		} catch (SQLException e) {
			logger.error("����sinfo��;��վʱ�������" + e.getMessage());
			e.printStackTrace();
		}
		logger.info("[sinfo����]����" + deleteCount + "��;��վ��Ϣ");
	}

	public void deleteMidway(Map<String, Object> map) {
		int deleteCount = 0;
		try {
			deleteCount = this.getSqlMapClient().delete("midway.deleteMidway",
					map);
		} catch (SQLException e) {
			logger.error("����sinfo��;��վʱ�������" + e.getMessage());
		}
		logger.info("[sinfo����]����" + deleteCount + "��;��վ��Ϣ");
	}

	public int deleteMidwayByCheci(Map<String, String> delteMap) {
		// TODO Auto-generated method stub
		int count=0;
		try {
			count=this.getSqlMapClient().delete("midway.deleteMidwayByCheci",delteMap);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.info("deleteMidwayByCheci",e);
		}
				
		logger.info(delteMap+",删除结果："+count);
		
		return count;
			
	}
	
	
	@SuppressWarnings("static-access")
	public void insertMidwayBybach(List<Midway> midwayList) {

		try {

		    this.getSqlMapClient().insert("midway.insertMidwayBybach",midwayList);
		    		
		} catch (SQLException e) {
			logger.info("[途经站]插入信息失败：" ,e);
			
		}
		logger.info("[途经站]插入结果："+midwayList);
	}
	
}
