package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.TrainDataFake;
import com.l9e.transaction.vo.TrainNewDataFake;
import com.l9e.transaction.vo.TrainNewDataFakeAppendTrain;
import com.l9e.transaction.vo.TrainStationVo;

public interface QueryTicketService {

	List<TrainDataFake> queryProperTrainData(Map<String, String> param);
	//保留
	List<TrainNewDataFake> queryProperTrainNewData(Map<String, String> param);

	List<TrainStationVo> queryWayStationInfo(String checi);
	//保留
	List<TrainNewDataFakeAppendTrain> queryAppendTrainNewData(Map<String, String> param);
	
	String queryTheCheciForStation(String checi);
	
	List<Map<String,String>> queryChinaCitysInfo(Map<String,Integer> param);
	
	void appendChinaPin(Map<String,String> param);
	
	void appendChinaPinBatch(List<Map<String,String>> list);
}
