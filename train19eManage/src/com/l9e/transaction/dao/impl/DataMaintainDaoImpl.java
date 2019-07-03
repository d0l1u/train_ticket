package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.DataMaintainDao;
import com.l9e.transaction.vo.DataMaintainVo;
import com.l9e.transaction.vo.DataTrainMaintainVo;
import com.l9e.transaction.vo.NewPriceAppendData;
import com.l9e.transaction.vo.TrainMaintainVo;

@Repository("dataMaintainDao")
public class DataMaintainDaoImpl extends BaseDao implements DataMaintainDao{

	@SuppressWarnings("unchecked")
	@Override
	public List<DataMaintainVo> getNewDataMaintain(Map<String, Integer> paramMap) {
		return (List<DataMaintainVo>)this.getSqlMapClientTemplate().queryForList("datamaintain.queryNewDataMaintain",paramMap);
	}
	
	@Override
	public DataMaintainVo getOldDataMaintain(Map<String, String> paramData) {
		return (DataMaintainVo)this.getSqlMapClientTemplate().queryForObject("datamaintain.queryOldDataMaintain",paramData);
	}

	@Override
	public void updateDataMaintain(DataMaintainVo dmv) {
		this.getSqlMapClientTemplate().update("datamaintain.updateDataMaintain", dmv);
	}
	
	@Override
	public void insertDataMaintain(DataMaintainVo dmv) {
		this.getSqlMapClientTemplate().update("datamaintain.insertDataMaintain", dmv);
	}
	
	@Override
	public void insertDataMaintainTemp(DataMaintainVo dmv) {
		this.getSqlMapClientTemplate().update("datamaintain.insertDataMaintainTemp", dmv);
	}
	
	@Override
	public void insertDataMaintainUpdateTemp(DataMaintainVo dmv) {
		this.getSqlMapClientTemplate().update("datamaintain.insertDataMaintainUpdateTemp", dmv);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TrainMaintainVo> queryNewDataMaintain(Map<String, Integer> paramMap){
		return (List<TrainMaintainVo>)this.getSqlMapClientTemplate().queryForList("datamaintain.queryNewDataMaintain",paramMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> queryCheciStartEndStationName(String checi) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("datamaintain.queryCheciStartEndStationName",checi);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> queryCheciStartEndStationTime(
			Map<String, String> paramMap) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("datamaintain.queryCheciStartEndStationTime",paramMap);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DataTrainMaintainVo> getDataTrainMaintain(
			Map<String, Integer> paramMap) {
		return (List<DataTrainMaintainVo>)this.getSqlMapClientTemplate().queryForList("datamaintain.queryDataTrainMaintain",paramMap);
	}

	@Override
	public void addDataTrainMaintain(DataTrainMaintainVo dtmo) {
		this.getSqlMapClientTemplate().insert("datamaintain.addDataTrainMaintain", dtmo);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DataMaintainVo> findInsertOfficialPriceData(
			Map<String, Integer> paramMap) {
		return (List<DataMaintainVo>)this.getSqlMapClientTemplate().queryForList("datamaintain.findInsertOfficialPriceData",paramMap);
	}

	@Override
	public void tempDataInsertNewPrice(DataMaintainVo dmv) {
		this.getSqlMapClientTemplate().insert("datamaintain.tempDataInsertNewPrice", dmv);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DataMaintainVo> findUpdateOfficialPriceData(
			Map<String, Integer> paramMap) {
		return (List<DataMaintainVo>)this.getSqlMapClientTemplate().queryForList("datamaintain.findUpdateOfficialPriceData",paramMap);
	}

	@Override
	public void tempDataUpdateNewPrice(DataMaintainVo dmv) {
		this.getSqlMapClientTemplate().insert("datamaintain.tempDataUpdateNewPrice", dmv);
	}

	@Override
	public void deleteOfficialPriceDataTemp() {
		this.getSqlMapClientTemplate().delete("datamaintain.deleteOfficialPriceDataTemp");
	}

	@Override
	public void deleteOfficialPriceNameTimeDataTemp() {
		this.getSqlMapClientTemplate().delete("datamaintain.deleteOfficialPriceNameTimeDataTemp");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DataTrainMaintainVo> findUpdateOfficialPriceNameTimeData(
			Map<String, Integer> paramMap) {
		return (List<DataTrainMaintainVo>)this.getSqlMapClientTemplate().queryForList("datamaintain.findUpdateOfficialPriceNameTimeData",paramMap);
	}

	@Override
	public void tempDataUpdateNewPriceNameTime(DataTrainMaintainVo dtmv) {
		this.getSqlMapClientTemplate().insert("datamaintain.tempDataUpdateNewPriceNameTime", dtmv);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DataMaintainVo> queryOldDataMaintainList(
			Map<String, Integer> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("datamaintain.queryOldDataMaintainList", paramMap);
	}

	@Override
	public void appendNewPriceDataMaintain(NewPriceAppendData data) {
		this.getSqlMapClientTemplate().update("datamaintain.appendNewPriceDataMaintain", data);
	}
}
