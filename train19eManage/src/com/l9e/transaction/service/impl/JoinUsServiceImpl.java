package com.l9e.transaction.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.JoinUsDao;
import com.l9e.transaction.service.JoinUsService;
import com.l9e.transaction.vo.AreaVo;
import com.l9e.transaction.vo.JoinDetailVo;
import com.l9e.transaction.vo.JoinUsVo;

@Service("joinUsService")
public class JoinUsServiceImpl implements JoinUsService {
	@Resource 
	private JoinUsDao joinUsDao;
	
	public int queryJoinUsListCount(Map<String, Object> paramMap) {
		return joinUsDao.queryJoinUsListCount(paramMap);
	}
	public List<Map<String, String>> queryJoinUsList(
			Map<String, Object> paramMap) {
		return joinUsDao.queryJoinUsList(paramMap);
	}
	
//	public List<Map<String, String>> queryJoinUsListSwitch(
//			Map<String, Object> paramMap) {
//		// TODO Auto-generated method stub
//		return joinUsDao.queryJoinUsListSwitch(paramMap);
//	}
	

	public Map<String, String> queryUpdateJoinUsInfo(String user_id) {
		return joinUsDao.queryUpdateJoinUsInfo(user_id);
	}
	public void updateJoinUs(JoinUsVo join ) {
		String user_id = join.getUser_id();
		String estate = join.getEstate() ; //状态
		JoinUsVo joinUsVo =joinUsDao.queryUserEstate(user_id) ; //查询免费用户信息
		
		if(joinUsVo != null && !join.getUser_level().equals(JoinUsVo.FREE)){ //如果用户等级不是免费用户
			//条件2：如果页面取出的状态信息与数据库取出的信息不一样 与 数据库取出的信息不为空
			//与 页面的状态为审核通过 执行更改begin_time 和 end_time  
			if(joinUsVo != null && !estate.equals(joinUsVo.getEstate()) && estate.equals(JoinUsVo.PASS)){
				joinUsDao.updateJoinTime(user_id); //修改审核通过为开始时间，同时加操作时间
			}
			//条件2：如果页面取出来的状态等于审核未通过 而且数据库查出来的信息不为空 执行IF 语句
			if(joinUsVo != null && estate.equals(JoinUsVo.DOESNOT) 
					&& !estate.equals(joinUsVo.getEstate())
					&& StringUtils.isNotEmpty(joinUsVo.getJm_order_id())
					&& StringUtils.isNotEmpty(joinUsVo.getProduct_id())){
				String product_id = join.getProduct_id() ;
				String order_id = join.getJm_order_id() ;
				
				Map<String,String>paramMap = new HashMap<String,String>();
				paramMap.put("user_id", user_id) ;
				paramMap.put("product_id", product_id) ;
				paramMap.put("order_id", order_id) ;
				// 修改订单状态为等待退款
				joinUsDao.updateOrderStatus(paramMap);
			}
		}
		
		// 修改基本信息
		joinUsDao.updateJoinUs(join); 
		//审核通过则更改审核通过时间
		if(joinUsVo != null && !estate.equals(joinUsVo.getEstate()) 
				&& estate.equals(JoinUsVo.PASS)){
			joinUsDao.updateEstatePass(user_id);
		}
	}
	
	public void deleteJoinUs(JoinUsVo join) {
		joinUsDao.deleteJoinUs(join) ;
	}
	public List<Map<String, String>> queryJoinDetail(
			Map<String,Object>paramMap) {
		return joinUsDao.queryJoinDetail(paramMap);
	}
	public ArrayList<JoinDetailVo> queryJoinDetailNowMouth(String user_id) {
		return (ArrayList<JoinDetailVo>) joinUsDao.queryJoinDetailNowMouth(user_id);
	}
	public ArrayList<JoinDetailVo> queryJoinDetailPreMouth(String user_id) {
		return (ArrayList<JoinDetailVo>) joinUsDao.queryJoinDetailPreMouth(user_id);
	}
	public Map<String, String> querySumNow(String user_id) {
		return joinUsDao.querySumNow(user_id);
	}
	
	public Map<String,String> querySumPre(String user_id) {
		return joinUsDao.querySumPre(user_id) ;
	}
	public Map<String, String> queryLastCreate(String user_id) {
		return joinUsDao.queryLastCreate(user_id);
	}
	public ArrayList<JoinDetailVo> queryUserOrder(String user_id) {
		return joinUsDao.queryUserOrder(user_id);
	}
	public int queryUserOrderCount(Map<String, Object> paramMap) {
		return joinUsDao.queryUsOrderCount(paramMap);
	}
	public String queryGetEstate(String user_id) {
		return joinUsDao.queryGetEstate(user_id) ;
	}
	

	public List<AreaVo> getProvince() {
		return joinUsDao.getProvince();
	}

	public List<AreaVo> getCity(String provinceid) {
		return joinUsDao.getCity(provinceid);
	}

	public List<AreaVo> getArea(String cityid) {
		return joinUsDao.getArea(cityid);
	}
	public void updateEstatePass(String user_id) {
		joinUsDao.updateEstatePass(user_id) ;
	}
	public List<String>queryJoinUsEstateCount() {
		return joinUsDao.queryJoinUsEstateCount();
	}
	public void updateEstateNot(String user_id) {
		joinUsDao.updateEstateNot(user_id) ;
		
	}
	
	public void updateOpt_ren(Map<String, String> map) {
		joinUsDao.updateOpt_ren(map);
		joinUsDao.updateOpt_time(map);
	}
	public Map<String, String> querySupervise_nameToArea_no(String area_name) {
		return joinUsDao.querySupervise_nameToArea_no(area_name);
	}

	public List<Map<String, String>> queryUserRegistInfo(Map<String, Object> paramMap) {
		return joinUsDao.queryUserRegistInfo(paramMap);
	}
	public int ueryUserRegistInfoCount(Map<String, Object> paramMap) {
		return joinUsDao.ueryUserRegistInfoCount(paramMap);
	}


}
