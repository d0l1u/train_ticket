package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.IpInfoReleaseDao;
import com.l9e.transaction.service.IpInfoReleaseService;
import com.l9e.transaction.vo.IpInfoRelease;

@Service("ipInfoReleaseService")
public class IpInfoReleaseServiceImpl implements IpInfoReleaseService {
	
	private static final Logger logger = Logger.getLogger(IpInfoReleaseServiceImpl.class);
	
	@Resource
	private IpInfoReleaseDao ipInfoReleaseDao;

	@Override
	public int modifyIpInfoRelease(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return ipInfoReleaseDao.updateIpInfoRelease(paramMap);
	}

	@Override
	public List<IpInfoRelease> queryIpInfoReleaseList(
			Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return ipInfoReleaseDao.selectIpInfoReleaseList(paramMap);
	}

	

}
