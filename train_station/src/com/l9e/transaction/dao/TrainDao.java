package com.l9e.transaction.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.l9e.transaction.vo.Train;

public class TrainDao extends BaseDao {

	private Logger logger = Logger.getLogger(TrainDao.class);

	public List<Train> queryTrain(Map<String, String> map) {
		List<Train> trainList = new ArrayList<Train>();
		try {
			trainList = this.getSqlMapClient().queryForList("train.queryTrain",
					map);
		} catch (SQLException e) {
			logger.error("[��ѯ�г�ʱ�������]��" + e.getMessage());
		}
		return trainList;
	}

	public void updateTrain(Train train) {
		try {
			this.getSqlMapClient().update("train.updateTrain", train);
		} catch (SQLException e) {
			logger.error("[�����г�ʱ�������]��" + e.getMessage());
		}
	}

	public void insertTrain(List<Train> trainList) {
		try {
			this.getSqlMapClient().insert("train.insertTrain", trainList);
		} catch (SQLException e) {
			logger.error("[�������г�ʱ�������]��" + e.getMessage());
		}
	}
	
	public void updateTrainOfLccc(Train train) {
		try {
			this.getSqlMapClient().update("train.updateTrainOfLccc", train);
		} catch (SQLException e) {
			logger.error("[车次�]更新失败：" + e.getMessage());
		}
	}
	
	public void insertTrainIntoLccc(Train train) {
		try {
			this.getSqlMapClient().insert("train.insertTrainVoIntoLccc", train);
		} catch (SQLException e) {
			logger.error("[车次]添加信息失败：" + e.getMessage());
			e.printStackTrace();
		}
	}
	public void insertTrainIntoLccc(List<Train> trainList) {
		try {
			this.getSqlMapClient().insert("train.insertTrainIntoLccc", trainList);
		} catch (SQLException e) {
			logger.error("[�������г�ʱ�������]��" + e.getMessage());
		}
	}
	
	public List<Train> queryTrainfromLccc(Map<String, String> map) {
		List<Train> trainList = new ArrayList<Train>();
		try {
			trainList = this.getSqlMapClient().queryForList("train.queryTrainfromLccc", map);
		} catch (SQLException e) {
			logger.error("[车次]查询车次是否存在失败：" + e.getMessage());
		}
		return trainList;
	}

	public void updateTrainOfLcccByccno(Train train) {
		// TODO Auto-generated method stub
		try {
			this.getSqlMapClient().update("train.updateTrainOfLcccByccno", train);
		} catch (SQLException e) {
			logger.error("[车次�]更新失败：" + e.getMessage());
		}
	}

	public void updateTrainOfLcccBycc(Train train) {
		// TODO Auto-generated method stub
		try {
			this.getSqlMapClient().update("train.updateTrainOfLcccBycc", train);
		} catch (SQLException e) {
			logger.error("[车次�]更新失败：" + e.getMessage());
		}
	}
}
