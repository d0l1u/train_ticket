package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.TrainDataFake;
import com.l9e.transaction.vo.TrainNewDataFake;
import com.l9e.transaction.vo.TrainNewDataFakeAppendTrain;
import com.l9e.transaction.vo.TrainNewStationVo;

public interface QueryTicketDao {
	List<TrainDataFake> queryProperTrainData(Map<String, String> param);
	
	List<TrainNewDataFake> queryProperTrainNewData(Map<String, String> param);
	
	List<TrainNewStationVo> queryWayStationInfo(String checi);
	
	List<TrainNewDataFakeAppendTrain> queryAppendTrainNewData(Map<String,String> param);
	
	String queryTheCheciForStation(String checi);
	
	List<String> queryOftenStation();
	
	List<Map<String,String>> queryOftenStationInfo(String station);
}
