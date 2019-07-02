package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.DBNoticeVo;
import com.l9e.transaction.vo.DBRemedyNoticeVo;

public interface NoticeDao {
	void insertNotice(DBNoticeVo notice);
	
	void updateNotice(DBNoticeVo notice);
	
	
	/**查询通知出票系统 请求出票结果*/
	List<DBNoticeVo> selectWaitNoticeList(Map<String, String> param);
	
	/**查询通知渠道系统 异步通知出票结果 */
	List<DBNoticeVo> selectOrderResultList(Map<String, String> param);

	List<DBNoticeVo> selectBookResultList(Map<String, String> param);

	DBNoticeVo queryNoticeInfoById(String orderId);

	String selectBookNoticeStatus(String orderId);



	int updateStartWaitNoticeList(DBNoticeVo vo);
	int updateStartOrderResultNotice(DBNoticeVo vo);
	int updateStartBookResultNotice(DBNoticeVo vo);
	
	List<DBNoticeVo> selectWaitNoticeListsx(Map<String, String> param);
}
