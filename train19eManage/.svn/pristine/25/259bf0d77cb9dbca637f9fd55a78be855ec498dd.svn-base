package com.l9e.transaction.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.JoinUsDao;
import com.l9e.transaction.vo.AreaVo;
import com.l9e.transaction.vo.JoinDetailVo;
import com.l9e.transaction.vo.JoinUsVo;

@Repository("joinUsDao")
public class JoinUsDaoImpl extends BaseDao implements JoinUsDao{

	@SuppressWarnings("unchecked")
	public int queryJoinUsListCount(Map<String, Object> paramMap) {
		return getTotalRows("joinUs.queryJoinUsListCount", paramMap);
	}
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryJoinUsList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("joinUs.queryJoinUsList", paramMap);
	}
	
//	@SuppressWarnings("unchecked")
//	public List<Map<String, String>> queryJoinUsListSwitch(
//			Map<String, Object> paramMap) {
//		return this.getSqlMapClientTemplate().queryForList("joinUs.queryJoinUsListSwitch",paramMap);
//	}
	@SuppressWarnings("unchecked")
	public Map<String, String> queryUpdateJoinUsInfo(String user_id) {
		return (Map<String,String>)this.getSqlMapClientTemplate().queryForObject("joinUs.queryUpdateJoinUsInfo",user_id);
	}
	@SuppressWarnings("unchecked")
	public void updateJoinUs(JoinUsVo join) {
		this.getSqlMapClientTemplate().update("joinUs.updateJoinUsInfo",join) ;
	}
	@SuppressWarnings("unchecked")
	public void deleteJoinUs(JoinUsVo join){
		this.getSqlMapClientTemplate().delete("joinUs.deleteJoinUs", join) ;
	}
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryJoinDetail(Map<String, Object> paramMap) {
		return (List<Map<String, String>>)this.getSqlMapClientTemplate().queryForList("joinUs.queryJoinDetailUp",paramMap);
	}
	@SuppressWarnings("unchecked")
	public ArrayList<JoinDetailVo> queryJoinDetailNowMouth(String user_id) {
		return (ArrayList<JoinDetailVo>) this.getSqlMapClientTemplate().queryForList("joinUs.queryJoinDetailRightDown",user_id);
	}
	@SuppressWarnings("unchecked")
	public ArrayList<JoinDetailVo> queryJoinDetailPreMouth(String user_id) {
		return (ArrayList<JoinDetailVo>) this.getSqlMapClientTemplate().queryForList("joinUs.queryJoinDetailLeftDown",user_id);
	}
	@SuppressWarnings("unchecked")
	public Map<String, String> querySumNow(String user_id) {
		return (Map<String, String>)this.getSqlMapClientTemplate().queryForObject("joinUs.sumNowMouthMoney",user_id);
	}
	@SuppressWarnings("unchecked")
	public Map<String,String> querySumPre(String user_id) {
		return (Map<String,String>)this.getSqlMapClientTemplate().queryForObject("joinUs.sumPreMouthMoney",user_id) ;
	}
	@SuppressWarnings("unchecked")
	public Map<String, String> queryLastCreate(String user_id) {
		return (Map<String,String>)this.getSqlMapClientTemplate().queryForObject("joinUs.lastCreateTime",user_id);
	}
	@SuppressWarnings("unchecked")
	public ArrayList<JoinDetailVo> queryUserOrder(String user_id) {
		return (ArrayList<JoinDetailVo>) this.getSqlMapClientTemplate().queryForList("joinUs.queryUserOrder",user_id);
	}
	@SuppressWarnings("unchecked")
	public int queryUsOrderCount(Map<String, Object> paramMap) {
		return getTotalRows("joinUs.queryUserOrderCount", paramMap);
	}
	@SuppressWarnings("unchecked")
	public JoinUsVo  queryUserEstate(String user_id) {
		return (JoinUsVo) this.getSqlMapClientTemplate().queryForObject("joinUs.queryUserEstate", user_id);
		
	}
	@SuppressWarnings("unchecked")
	public void updateOrderStatus(Map<String,String>paramMap) {
		this.getSqlMapClientTemplate().update("joinUs.updateOrderStatus",paramMap);
		
	}
	@SuppressWarnings("unchecked")
	public String queryGetEstate(String user_id) {
		return  (String) this.getSqlMapClientTemplate().queryForObject("joinUs.queryGetEstate",user_id);
	}
	
	@SuppressWarnings("unchecked")
	public void updateJoinTime(String user_id) {
		this.getSqlMapClientTemplate().update("joinUs.updateJoinTime",user_id);
	}

	@SuppressWarnings("unchecked")
	public List<AreaVo> getProvince() {
		return this.getSqlMapClientTemplate().queryForList("joinUs.getProvince");
	}
	@SuppressWarnings("unchecked")
	public List<AreaVo> getCity(String provinceid) {
		return this.getSqlMapClientTemplate().queryForList("joinUs.getCity", provinceid);
	}
	@SuppressWarnings("unchecked")
	public List<AreaVo> getArea(String cityid) {
		return this.getSqlMapClientTemplate().queryForList("joinUs.getArea", cityid);
	}
	
	@SuppressWarnings("unchecked")
	public void updateEstatePass(String user_id) {
		this.getSqlMapClientTemplate().update("joinUs.updateEstatePass", user_id);
	}
	
	@SuppressWarnings("unchecked")
	public List<String> queryJoinUsEstateCount() {
		return this.getSqlMapClientTemplate().queryForList("joinUs.queryJoinUsEstateCount");
	}
	
	@SuppressWarnings("unchecked")
	public void updateEstateNot(String user_id) {
		this.getSqlMapClientTemplate().update("joinUs.updateEstateNot",user_id);
		
	}
	@SuppressWarnings("unchecked")
	public void updateOpt_ren(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("joinUs.updateOpt_ren",map);
	}
	@SuppressWarnings("unchecked")
	public Map<String, String> querySupervise_nameToArea_no(String area_name) {
		return (Map<String, String>)this.getSqlMapClientTemplate().queryForObject("joinUs.querySupervise_nameToArea_no",area_name);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryUserRegistInfo(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("joinUs.queryUserRegistInfo", paramMap);

	}
	@SuppressWarnings("unchecked")
	@Override
	public int ueryUserRegistInfoCount(Map<String, Object> paramMap) {
		return getTotalRows("joinUs.ueryUserRegistInfoCount", paramMap);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void updateOpt_time(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("joinUs.updateOpt_time",map);
	}

}
