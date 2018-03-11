package com.l9e.transaction.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.l9e.transaction.vo.CtripStation;
import com.l9e.transaction.vo.Station;

public class StationDao extends BaseDao {

	private Logger logger = Logger.getLogger(StationDao.class);

	public List<Station> queryStation(Map<String, String> map) {
		List<Station> stationList = null;
		try {
			stationList = this.getSqlMapClient().queryForList(
					"station.queryStation", map);
		} catch (SQLException e) {
			logger.error("【车站】查询b_station表是否有该车站失败："+e.getMessage());
		}
		return stationList;
	}

	public List<Station> queryStationFromZm(Map<String, String> map) {
		List<Station> stationList = null;
		try {
			stationList = this.getSqlMapClient().queryForList(
					"station.queryStationFromZm", map);
		} catch (SQLException e) {
			logger.error("【车站】查询t_zm表是否有该车站失败："+e.getMessage());
		}
		return stationList;
	}

	public void updateStation(Station station) {
		try {
			this.getSqlMapClient().update("station.updateStation", station);
		} catch (SQLException e) {
			logger.error("���³�վʱ�������" + e.getMessage());
		}
	}

	public void updateStationOfZm(Station station) {
		try {
			this.getSqlMapClient().update("station.updateStationOfZm", station);
		} catch (SQLException e) {
			logger.error("【车站】更新车站信息失败：" + e.getMessage());
		}
	}

	public void insertStation(Station station) {
		try {
			this.getSqlMapClient().insert("station.insertStation", station);
		} catch (SQLException e) {
			logger.error("��station���в��복վʱ�������" + e.getMessage());
		}
	}

	public void insertStationIntoZm(Station station) {
		try {
			this.getSqlMapClient().insert("station.insertStationIntoZm",
					station);
		} catch (SQLException e) {
			logger.error("【车站】向t_zm插入新车站名失败：" + e.getMessage());
		}
	}
	
	//*********************//

	public int deteStationByCode(List<Station> zmList) {
		// TODO Auto-generated method stub
		int count=0;
		try {
			count=this.getSqlMapClient().delete("midway.deleteMidwayByCheci",zmList);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.info("deteStationByCode",e);
		}
				
		logger.info(zmList+",删除结果："+count);
		
		return count;
	}

	public int deteStationByName(List<Station> zmList) {
		// TODO Auto-generated method stub
		int count=0;
		try {
			count=this.getSqlMapClient().delete("midway.deleteMidwayByCheci",zmList);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.info("deteStationByName",e);
		}
				
		logger.info(zmList+",删除结果："+count);
		
		return count;
	}
	
	
	/**
	 * 查询t_ctrip_zm
	 * @param map
	 * @return
	 */
	@SuppressWarnings({ "static-access", "unchecked" })
	public List<CtripStation> queryCtripStation(Map<String, String> map) {
		// TODO Auto-generated method stub
		List<CtripStation> stationList = null;
		try {
			stationList = this.getSqlMapClient().queryForList(
					"station.queryCtripStation", map);
		} catch (SQLException e) {
			logger.info("【车站】查询t_ctrip_zm表是否有该车站失败："+e.getMessage(),e);
		}
		return stationList;
	}

	
	
	/**
	 *  插入t_ctrip_zm
	 * @param station
	 */
	@SuppressWarnings("static-access")
	public void insertCtripStation(CtripStation station) {
		// TODO Auto-generated method stub
		try {
			this.getSqlMapClient().insert("station.insertCtripStation",
					station);
		} catch (SQLException e) {
			logger.info("【车站】向t_ctrip_zm插入新车站名失败：" + e.getMessage(),e);
		}
	}

	public void updateStation(CtripStation station) {
		// TODO Auto-generated method stub
		try {
			this.getSqlMapClient().insert("station.updateCtripStation",
					station);
		} catch (SQLException e) {
			logger.error("【车站】向t_ctrip_zm更新车站名失败：" + e.getMessage(),e);
		}
	}
	
	

}
