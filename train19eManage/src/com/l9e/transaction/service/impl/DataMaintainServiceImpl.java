package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.DataMaintainDao;
import com.l9e.transaction.service.DataMaintainService;
import com.l9e.transaction.vo.DataMaintainVo;
import com.l9e.transaction.vo.DataTrainMaintainVo;
import com.l9e.transaction.vo.NewPriceAppendData;
import com.l9e.transaction.vo.TrainMaintainVo;

@Service("dataMaintainService")
public class DataMaintainServiceImpl implements DataMaintainService {

	@Resource
	private DataMaintainDao dataMaintain;
	
	@Override
	public List<DataMaintainVo> getNewDataMaintain(Map<String, Integer> paramMap) {
		return dataMaintain.getNewDataMaintain(paramMap);
	}
	
	@Override
	public DataMaintainVo getOldDataMaintain(Map<String, String> paramData) {
		return dataMaintain.getOldDataMaintain(paramData);
	}

	@Override
	public void updateDataMaintain(DataMaintainVo dmv) {
		dataMaintain.updateDataMaintain(dmv);
	}
	
	@Override
	public void insertDataMaintain(DataMaintainVo dmv){
		dataMaintain.insertDataMaintain(dmv);
	}
	
	@Override
	public void insertDataMaintainTemp(DataMaintainVo dmv){
		dataMaintain.insertDataMaintainTemp(dmv);
	}
	
	@Override
	public void insertDataMaintainUpdateTemp(DataMaintainVo dmv){
		dataMaintain.insertDataMaintainUpdateTemp(dmv);
	}
	
	@Override
	public List<TrainMaintainVo> queryNewDataMaintain(Map<String, Integer> paramMap){
		return dataMaintain.queryNewDataMaintain(paramMap);
	}

	@Override
	public Map<String,String> queryCheciStartEndStationName(String checi) {
		return dataMaintain.queryCheciStartEndStationName(checi);
	}

	@Override
	public Map<String,String> queryCheciStartEndStationTime(
			Map<String, String> paramMap) {
		return dataMaintain.queryCheciStartEndStationTime(paramMap);
	}
	
	@Override
	public List<DataTrainMaintainVo> getDataTrainMaintain(
			Map<String, Integer> paramMap) {
		return dataMaintain.getDataTrainMaintain(paramMap);
	}

	@Override
	public void addDataTrainMaintain(DataTrainMaintainVo dtmo) {
		dataMaintain.addDataTrainMaintain(dtmo);
		
	}

	@Override
	public List<DataMaintainVo> findInsertOfficialPriceData(Map<String, Integer> paramMap) {
		return dataMaintain.findInsertOfficialPriceData(paramMap);
	}

	@Override
	public void tempDataInsertNewPrice(DataMaintainVo dmv) {
		dataMaintain.tempDataInsertNewPrice(dmv);
	}
	
	@Override
	public List<DataMaintainVo> findUpdateOfficialPriceData(Map<String, Integer> paramMap) {
		return dataMaintain.findUpdateOfficialPriceData(paramMap);
	}

	@Override
	public void tempDataUpdateNewPrice(DataMaintainVo dmv) {
		dataMaintain.tempDataUpdateNewPrice(dmv);
	}

	@Override
	public void deleteOfficialPriceDataTemp() {
		dataMaintain.deleteOfficialPriceDataTemp();
	}

	@Override
	public void deleteOfficialPriceNameTimeDataTemp() {
		dataMaintain.deleteOfficialPriceNameTimeDataTemp();
	}

	@Override
	public List<DataTrainMaintainVo> findUpdateOfficialPriceNameTimeData(
			Map<String, Integer> paramMap) {
		return dataMaintain.findUpdateOfficialPriceNameTimeData(paramMap);
	}

	@Override
	public void tempDataUpdateNewPriceNameTime(DataTrainMaintainVo dmv) {
		dataMaintain.tempDataUpdateNewPriceNameTime(dmv);
	}

	@Override
	public List<DataMaintainVo> queryOldDataMaintainList(
			Map<String, Integer> paramMap) {
		return dataMaintain.queryOldDataMaintainList(paramMap);
	}

	@Override
	public void appendNewPriceDataMaintain(NewPriceAppendData data) {
		dataMaintain.appendNewPriceDataMaintain(data);
	}
}
