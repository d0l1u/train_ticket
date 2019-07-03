package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.QueryTicketDao;
import com.l9e.transaction.service.QueryTicketService;
import com.l9e.transaction.vo.TrainDataFake;
import com.l9e.transaction.vo.TrainNewData;
import com.l9e.transaction.vo.TrainNewDataFake;
import com.l9e.transaction.vo.TrainNewDataFakeAppendTrain;
import com.l9e.transaction.vo.TrainStationVo;
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
	public List<TrainStationVo> queryWayStationInfo(String checi){
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
	public List<Map<String, String>> queryChinaCitysInfo(Map<String, Integer> param) {
		return queryDao.queryChinaCitysInfo(param);
	}

	@Override
	public void appendChinaPin(Map<String, String> param) {
		queryDao.appendChinaPin(param);
	}

	@Override
	public void appendChinaPinBatch(List<Map<String, String>> list) {
		queryDao.appendChinaPinBatch(list);
	}

	@Override
	public void addWaitQueryPrice(TrainNewData trainNewData) {
		queryDao.addWaitQueryPrice(trainNewData);
	}

	@Override
	public int queryTicketPriceExist(Map<String, String> param) {
		return queryDao.queryTicketPriceExist(param);
	};
	
}
