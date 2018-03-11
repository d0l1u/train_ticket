package com.l9e.transaction.service.impl;

import java.util.List;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.CommonDao;
import com.l9e.transaction.service.CommonService;
import com.l9e.transaction.vo.AreaVo;

@Service("commonService")
public class CommonServiceImpl implements CommonService {
	
	@Resource
	private CommonDao commonDao;
	
	public String query() {
		return commonDao.query();
	}

	public List<AreaVo> getProvince() {
		return commonDao.getProvince();
	}

	public List<AreaVo> getCity(String provinceid) {
		return commonDao.getCity(provinceid);
	}

	public List<AreaVo> getArea(String cityid) {
		return commonDao.getArea(cityid);
	}
}
