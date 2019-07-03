package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.DataMaintainVo;
import com.l9e.transaction.vo.DataTrainMaintainVo;
import com.l9e.transaction.vo.NewPriceAppendData;
import com.l9e.transaction.vo.TrainMaintainVo;

public interface DataMaintainService {
	
	public List<DataMaintainVo> getNewDataMaintain(Map<String,Integer> paramMap);
	
	public DataMaintainVo getOldDataMaintain(Map<String,String> paramMap);
	
	public void updateDataMaintain(DataMaintainVo dmv);
	
	public void insertDataMaintain(DataMaintainVo dmv);
	
	public void insertDataMaintainTemp(DataMaintainVo dmv);
	
	public void insertDataMaintainUpdateTemp(DataMaintainVo dmv);
	
	public List<TrainMaintainVo> queryNewDataMaintain(Map<String,Integer> paramMap);
	
	public Map<String,String> queryCheciStartEndStationName(String checi);
	
	public Map<String,String> queryCheciStartEndStationTime(Map<String,String> paramMap);
	
	public List<DataTrainMaintainVo> getDataTrainMaintain(Map<String, Integer> paramMap);
	
	public void addDataTrainMaintain(DataTrainMaintainVo dtmo);
	
	public List<DataMaintainVo> findInsertOfficialPriceData(Map<String, Integer> paramMap);
	
	public void tempDataInsertNewPrice(DataMaintainVo dmv);
	
	public List<DataMaintainVo> findUpdateOfficialPriceData(Map<String, Integer> paramMap);
	
	public void tempDataUpdateNewPrice(DataMaintainVo dmv);
	
	public void deleteOfficialPriceDataTemp();
	
	public List<DataTrainMaintainVo> findUpdateOfficialPriceNameTimeData(Map<String, Integer> paramMap);
	
	public void tempDataUpdateNewPriceNameTime(DataTrainMaintainVo dmv);
	
	public void deleteOfficialPriceNameTimeDataTemp();
	
	List<DataMaintainVo> queryOldDataMaintainList(Map<String, Integer> paramMap);
	
	void appendNewPriceDataMaintain(NewPriceAppendData data);
	
}
