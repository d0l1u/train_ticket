package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.QueryTicketDao;
import com.l9e.transaction.service.QueryTicketService;
import com.l9e.transaction.vo.TrainDataFake;
import com.l9e.transaction.vo.TrainNewDataFake;
import com.l9e.transaction.vo.TrainNewDataFakeAppendTrain;
import com.l9e.transaction.vo.TrainNewStationVo;
@Service("queryTicketService")
public class QueryTicketServiceImpl implements QueryTicketService {

	@Resource
	private QueryTicketDao queryDao;
	
	@Override
	public List<TrainDataFake> queryProperTrainData(Map<String, String> param) {
		return queryDao.queryProperTrainData(param);
	}

	@Override
	public List<TrainNewDataFake> queryProperTrainNewData(Map<String, String> param) {
		return queryDao.queryProperTrainNewData(param);
	}
	@Override
	public List<TrainNewStationVo> queryWayStationInfo(String checi){
		return queryDao.queryWayStationInfo(checi);
	};
	
	@Override
	public List<TrainNewDataFakeAppendTrain> queryAppendTrainNewData(Map<String, String> param){
		return queryDao.queryAppendTrainNewData(param);
	}

	@Override
	public String queryTheCheciForStation(String checi) {
		return queryDao.queryTheCheciForStation(checi);
	}

	@Override
	public List<String> queryOftenStation() {
		return queryDao.queryOftenStation();
	}

	@Override
	public List<Map<String, String>> queryOftenStationInfo(String station) {
		return queryDao.queryOftenStationInfo(station);
	};
	
}
