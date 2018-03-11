package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.QueryTicketDao;
import com.l9e.transaction.vo.Station;
import com.l9e.transaction.vo.TrainDataFake;
import com.l9e.transaction.vo.TrainNewData;
import com.l9e.transaction.vo.TrainNewDataFake;
import com.l9e.transaction.vo.TrainNewDataFakeAppendTrain;
import com.l9e.transaction.vo.TrainStationVo;
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
	public List<TrainStationVo> queryWayStationInfo(String checi){
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

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryChinaCitysInfo(Map<String, Integer> param) {
		return this.getSqlMapClientTemplate().queryForList("queryTicket.queryChinaCitysInfo", param);
	}

	@Override
	public void appendChinaPin(Map<String, String> param) {
		this.getSqlMapClientTemplate().update("queryTicket.appendChinaPin", param);
	}

	@Override
	public void appendChinaPinBatch(List<Map<String, String>> list) {
		this.getSqlMapClientTemplate().update("queryTicket.appendChinaPinBatch", list);
	}

	@Override
	public void addWaitQueryPrice(TrainNewData trainNewData) {
		this.getSqlMapClientTemplate().update("queryTicket.addWaitQueryPrice", trainNewData);
	}

	@Override
	public int queryTicketPriceExist(Map<String, String> param) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("queryTicket.queryTicketPriceExist", param);
		
		
	}

	@Override
	public String queryZmByStationCode(String code) {
		// TODO Auto-generated method stub
		return (String) this.getSqlMapClientTemplate().queryForObject("queryTicket.queryZmByStationCode", code);
	}

	@Override
	public String queryStartStationBySinfo(String train_code) {
		// TODO Auto-generated method stub
		return (String) this.getSqlMapClientTemplate().queryForObject("queryTicket.queryStartStationBySinfo", train_code);
	}

	@Override
	public String queryEndStationBySinfo(String train_code) {
		// TODO Auto-generated method stub
		return (String) this.getSqlMapClientTemplate().queryForObject("queryTicket.queryEndStationBySinfo", train_code);
	}
	
	
	@Override
	public Station findStartStationBySinfo(String train_code) {
		// TODO Auto-generated method stub
		return (Station) this.getSqlMapClientTemplate().queryForObject("queryTicket.selectStartStation", train_code);
	}

	@Override
	public Station findEndStationBySinfo(String train_code) {
		// TODO Auto-generated method stub
		return (Station) this.getSqlMapClientTemplate().queryForObject("queryTicket.selectEndStation", train_code);
	}
}
