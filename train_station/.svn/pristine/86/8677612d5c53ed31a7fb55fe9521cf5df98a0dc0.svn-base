package com.l9e.transaction.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.l9e.transaction.vo.Train;
import com.l9e.transaction.vo.TrainCtrip;
import com.l9e.transaction.vo.TrainSeat;

public class CtripDao extends BaseDao {

	private Logger logger = Logger.getLogger(CtripDao.class);
	
	//查询该车次信息的数据是否存在
	public int queryCtripIsExist(TrainCtrip train) {
		int count = 0;
		try {
			count = (Integer) this.getSqlMapClient().queryForObject("ctrip.queryCtripIsExist", train);
		} catch (SQLException e) {
			logger.error("[查询该车次信息的数据是否存在 exception]" + e.getMessage());
		}
		return count;
	}

	//修改车次信息
	public int updateCtrip(TrainCtrip train) {
		int count = 0;
		try {
			count = (Integer) this.getSqlMapClient().update("ctrip.updateCtrip", train);
		} catch (SQLException e) {
			logger.error("[修改车次信息 exception]" + e.getMessage());
		}
		return count;
	}

	//新增车次信息
	public void addCtrip(TrainCtrip train) {
		try {
			this.getSqlMapClient().insert("ctrip.addCtrip", train);
		} catch (SQLException e) {
			logger.error("[新增车次信息 exception]" + e.getMessage());
		}
	}

	////查询所有的车次信息
	public List<TrainCtrip> queryTrainList(Map<String, String> paramMap) {
		List<TrainCtrip> list = null;
		try {
			list = this.getSqlMapClient().queryForList("ctrip.queryTrainList", paramMap);
		} catch (SQLException e) {
			logger.error("[查询所有列车车次信息 exception]" + e.getMessage());
		}
		return list;
	}

	//查询该车次途经站信息的数据是否存在
	public int queryMidWayIsExist(Map<String, String> map) {
		int count = 0;
		try {
			count = (Integer) this.getSqlMapClient().queryForObject("ctrip.queryMidWayIsExist", map);
		} catch (SQLException e) {
			logger.error("[查询该车次途经站信息的数据是否存在 exception]" + e.getMessage());
		}
		return count;
	}

	//更新改车次的途经站信息
	public int updateMidWay(Map<String, String> map) {
		int count = 0;
		try {
			count = (Integer) this.getSqlMapClient().update("ctrip.updateMidWay", map);
		} catch (SQLException e) {
			logger.error("[修改途经站信息 exception]" + e.getMessage());
		}
		return count;
	}

	//新增改车次的途经站信息
	public void addMidWay(Map<String, String> map) {
		try {
			this.getSqlMapClient().insert("ctrip.addMidWay", map);
		} catch (SQLException e) {
			logger.error("[新增途经站信息 exception]" + e.getMessage());
		}
	}

	//遍历所有的车站的信息----始发站
	public List<Map<String, String>> queryZmStartList(int size) {
		List<Map<String, String>> list = null;
		try {
			list = this.getSqlMapClient().queryForList("ctrip.queryZmStartList", size);
		} catch (SQLException e) {
			logger.error("[遍历所有的车站的信息 exception]" + e.getMessage());
		}
		return list;
	}
	
	//遍历所有的车站的信息----始发站
	public List<Map<String, String>> queryZmById(int id) {
		List<Map<String, String>> list = null;
		try {
			list = this.getSqlMapClient().queryForList("ctrip.queryZmById", id);
		} catch (SQLException e) {
			logger.error("[查询始发站的信息 exception]" + e.getMessage());
		}
		return list;
	}
	
	//遍历所有的车站的信息----到达站
	public List<Map<String, String>> queryZmArrivalList() {
		List<Map<String, String>> list = null;
		try {
			list = this.getSqlMapClient().queryForList("ctrip.queryZmArrivalList");
		} catch (SQLException e) {
			logger.error("[遍历所有的车站的信息 exception]" + e.getMessage());
		}
		return list;
	}

	public int updateZm(Map<String, Object> zmMap) {
		int count = 0;
		try {
			count = (Integer) this.getSqlMapClient().update("ctrip.updateZm", zmMap);
		} catch (SQLException e) {
			logger.error("[修改车站zm状态 exception]" + e.getMessage());
		}
		return count;
	}

	
	//查询该车次坐席票价信息的数据是否存在
	public int querySeatIsExist(TrainSeat seat) {
		int count = 0;
		try {
			count = (Integer) this.getSqlMapClient().queryForObject("ctrip.querySeatIsExist", seat);
		} catch (SQLException e) {
			logger.error("[查询该车次坐席票价信息的数据是否存在 exception]" + e.getMessage());
		}
		return count;
	}

	public int updateSeat(TrainSeat seat) {
		int count = 0;
		try {
			count = (Integer) this.getSqlMapClient().update("ctrip.updateSeat", seat);
		} catch (SQLException e) {
			logger.error("[修改车站坐席票价信息 exception]" + e.getMessage());
		}
		return count;
	}

	public void addSeat(TrainSeat seat) {
		try {
			this.getSqlMapClient().insert("ctrip.addSeat", seat);
		} catch (SQLException e) {
			logger.error("[新增坐席票价信息 exception]" + e.getMessage());
		}
		
	}


	@SuppressWarnings({ "unchecked", "static-access" })
	public List<Map<String, String>> queryZmByCCStatus(String ccStatus) {
		// TODO Auto-generated method stub
		List<Map<String, String>> list = null;
		try {
			list = this.getSqlMapClient().queryForList("ctrip.queryZmByCCStatus",ccStatus);
		}catch(SQLException e){
			logger.info("[查询始发站 exception]" + e.getMessage(),e);
		}
		return list;
	}

	@SuppressWarnings("static-access")
	public int updateZmCCStatus(Integer id) {
		// TODO Auto-generated method stub
		int count = 0;
		try {
			count = (Integer) this.getSqlMapClient().update("ctrip.updateZmCCStatus", id);
		} catch (SQLException e) {
			logger.error("[修改车站名信息 exception]" + e.getMessage(),e);
		}
		return count;
	}


}
