package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.TuniuCommonDao;
import com.l9e.transaction.vo.Station;

@Repository("tuniuCommonDao")
public class TuniuCommonDaoImpl extends BaseDao implements TuniuCommonDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Station> selectStations(Map<String, Object> queryParam) {
		return getSqlMapClientTemplate().queryForList("common.selectStation", queryParam);
	}

	@Override
	public Station selectOneStation(Map<String, Object> queryParam) {
		return (Station) getSqlMapClientTemplate().queryForObject("common.selectStation", queryParam);
	}

}
