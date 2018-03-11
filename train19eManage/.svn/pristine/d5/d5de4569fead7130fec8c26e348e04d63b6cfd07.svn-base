package com.l9e.transaction.service.impl;

import com.l9e.transaction.service.AllComplainService;



import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.l9e.transaction.dao.AllComplainDao;
import com.l9e.transaction.vo.AllComplainVo;
import com.l9e.transaction.vo.AreaVo;
import com.l9e.transaction.vo.ComplainVo;
@Service("AllComplainService")
public class AllComplainServiceImpl implements AllComplainService{

		@Resource
		private AllComplainDao allComplainDao;
		
		public List<Map<String,String>> queryComplainList(
				Map<String, Object> paramMap) {
			return allComplainDao.queryComplainList(paramMap);
		}
		
		public int queryComplainListCount(
				Map<String, Object> paramMap) {
			return allComplainDao.queryComplainListCount(paramMap);
		}

		public Map<String, String> queryComplainParticularInfo(
				String complain_id) {

			return allComplainDao.queryComplainParticularInfo(complain_id);
		}

		public void updateComplainInfo(AllComplainVo complain) {
			allComplainDao.updateComplainInfo(complain) ;
			allComplainDao.addComplainHistoryInfo(complain);//增加历史日志
		}
		
		public void deleteComplain(String complain_id) {
			allComplainDao.deleteComplain(complain_id) ;//修改详细
		}
		
		public List<Map<String,String>> queryComplainStatCount() {
			return allComplainDao.queryComplainStatCount();
		}
		
		public List<AreaVo> getProvince() {
			return allComplainDao.getProvince();
		}

		public List<AreaVo> getCity(
				String provinceid) {
			return allComplainDao.getCity(provinceid);
		}

		public List<AreaVo> getArea(
				String cityid) {
			return allComplainDao.getArea(cityid);
		}

		public Map<String, String> querySupervise_nameToArea_no(String string) {
			return allComplainDao.querySupervise_nameToArea_no(string);
		}

		@Override
		public List<Map<String, Object>> queryHistroyByComplainId(String complainId) {
			return allComplainDao.queryHistroyByComplainId(complainId);
		}

		@Override
		public void insertLog(Map<String, String> map) {
			allComplainDao.insertLog(map);
			
		}


	}
