package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.ReceiveNotifyDao;
import com.l9e.transaction.service.ReceiveNotifyService;

@Service("receiveNotifyService")
public class ReceiveNofityServiceImpl implements ReceiveNotifyService {
	
	@Resource
	private ReceiveNotifyDao receiveNotifyDao;

	public int updateOrderWithCpNotify(Map<String, String> paraMap, 
			List<Map<String, String>> cpMapList) {
		try {
			receiveNotifyDao.updateOrderWithCpNotify(paraMap);
			if (cpMapList != null) {
				for (Map<String, String> cpMap : cpMapList) {
					receiveNotifyDao.updateCpOrderWithCpNotify(cpMap);
				}
			}
		} catch (Exception e) {
			return 0;
		}
		return 1;
		
	}
}
