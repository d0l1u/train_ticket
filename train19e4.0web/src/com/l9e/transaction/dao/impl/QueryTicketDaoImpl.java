package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.QueryTicketDao;
import com.l9e.transaction.vo.TrainDataFake;
import com.l9e.transaction.vo.TrainNewDataFake;
import com.l9e.transaction.vo.TrainNewDataFakeAppendTrain;
import com.l9e.transaction.vo.TrainNewStationVo;
@Repository("queryTicketDao")
public class QueryTicketDaoImpl extends BaseDao implements QueryTicketDao {

	@SuppressWarnings("unchecked")
	public List<TrainDataFake> queryProperTrainData(Map<String, String> param) {
		return this.getSqlMapClientTemplate().queryForList("queryTicket.queryTicketDataList", param);
	}

	@SuppressWarnings("unchecked")
	public List<TrainNewDataFake> queryProperTrainNewData(Map<String, String> param) {
		return this.getSqlMapClientTemplate().queryForList("queryTicket.queryTicketNewDataList", param);
	}
	public String queryWayStationCheciInfo(Map<String, String> param) {
		return (String) this.getSqlMapClientTemplate().queryForObject("queryTicket.queryWayStationCheciInfo", param);
	}
	
	@SuppressWarnings("unchecked")
	public List<TrainNewStationVo> queryWayStationInfo(String checi){
		return this.getSqlMapClientTemplate().queryForList("queryTicket.queryWayStationInfo",checi);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TrainNewDataFakeAppendTrain> queryAppendTrainNewData(
			Map<String, String> param) {
		return this.getSqlMapClientTemplate().queryForList("queryTicket.queryTicketAppendTrainList", param);
	}

	@Override
	public String queryTheCheciForStation(String checi) {
		return (String) this.getSqlMapClientTemplate().queryForObject("queryTicket.queryTheCheciForStation", checi);
	}
}
