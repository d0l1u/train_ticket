package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.ElongNoticeDao;
import com.l9e.transaction.vo.ChangePassengerInfo;
import com.l9e.transaction.vo.ElongNoticeVo;
import com.l9e.transaction.vo.ElongOrderNoticeVo;
/**elong 通知相关dao */
@Repository("elongNoticeDao")
public class ElongNoticeDaoImpl  extends BaseDao  implements ElongNoticeDao{


	@Override
	public List<ElongOrderNoticeVo> getOrderNoticesList() {
		return this.getSqlMapClientTemplate().queryForList("elongNotice.getOrderNoticesList");
	}

	@Override
	public List<Map<String, Object>> getRefundNoticesList() {
		return	this.getSqlMapClientTemplate().queryForList("elongNotice.getRefundNoticesList");
	}

	@Override
	public void insertTrainInterfaceNotices(Map<String, String> map) {
		this.getSqlMapClientTemplate().insert("elongNotice.insertTrainInterfaceNotices",map);
		
	}

	@Override
	public List<Map<String,Object>> querySysNoticeList() {
		return	this.getSqlMapClientTemplate().queryForList("elongNotice.querySysNoticeList");

	}

	@Override
	public void updateSysNotice(Map<String, Object> sysNoticeInfo) {
		this.getSqlMapClientTemplate().update("elongNotice.updateSysNotice",sysNoticeInfo);
	}


	@Override
	public String queryNoticeStatusByOrderId(String order_id) {
		return	(String)this.getSqlMapClientTemplate().queryForObject("elongNotice.queryNoticeStatusByOrderId",order_id);
	}

	@Override
	public List<Map<String, Object>> getRefundNoticesListById(String order_id) {
		return	this.getSqlMapClientTemplate().queryForList("elongNotice.getRefundNoticesListById",order_id);

	}

	@Override
	public Map<String, Object> queryCpInfoById(String cp_id) {
		return (Map<String, Object>)this.getSqlMapClientTemplate().queryForObject("elongNotice.queryCpInfoById",cp_id);
	}

	@Override
	public int updateNoticeBegin(ElongOrderNoticeVo orderNoticeVo) {
		return (Integer)this.getSqlMapClientTemplate().update("elongNotice.updateNoticeBegin",orderNoticeVo);
	}

	@Override
	public void updateNoticeTime(ElongOrderNoticeVo orderNoticeVo) {
		this.getSqlMapClientTemplate().update("elongNotice.updateNoticeTime",orderNoticeVo);
	}

	@Override
	public int updateStartOfflineRefundNotice(Map<String,Object> map) {
		return (Integer)this.getSqlMapClientTemplate().update("elongNotice.updateStartOfflineRefundNotice",map);
	}

	@Override
	public int updateBeginRefundNotice(Map<String, Object> map) {
		return (Integer)this.getSqlMapClientTemplate().update("elongNotice.updateBeginRefundNotice",map);
	}

	@Override
	public int updateStartNoticeOutSys(Map<String, Object> map) {
		return (Integer)this.getSqlMapClientTemplate().update("elongNotice.updateStartNoticeOutSys",map);
	}

	@Override
	public ChangePassengerInfo queryChangeCpInfoByNewCp(String cpId) {
		return (ChangePassengerInfo)this.getSqlMapClientTemplate().queryForObject("elongNotice.queryChangeCpInfoByNewCp", cpId);
	}


	
}
