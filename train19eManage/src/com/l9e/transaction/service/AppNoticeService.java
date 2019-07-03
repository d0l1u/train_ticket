package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.AreaVo;
import com.l9e.transaction.vo.AppNoticeVo;

public interface AppNoticeService {
	List<Map<String, Object>> queryNoticeList(Map<String, Object> paramMap);

	int queryNoticeListCount(Map<String, Object> paramMap);

	Map<String, Object> queryNotice(String notice_id);

	void updateNotice(AppNoticeVo  Notice);
	
	void insertNotice(AppNoticeVo  Notice);
	
	void deleteNotice(AppNoticeVo  Notice);
	
	/**
	 * 获取省份
	 * @return
	 */
	public List<AreaVo> getProvince();
	
	
	/**
	 * 获取城市
	 * @return
	 */
	public List<AreaVo>  getCity(String provinceid);
	
	
	/**
	 * 获取区县
	 * @return
	 */
	public List<AreaVo>  getArea(String cityid);

	void addNoticeUser(Map<String, String> paramMap);

	String queryNoticeId(AppNoticeVo notice);

	List<String> queryUserPhone(String noticeId);
}
