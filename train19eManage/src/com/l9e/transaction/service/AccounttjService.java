package com.l9e.transaction.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.AccountStatistics;

public interface AccounttjService {

	// 获取按条件查询数 
	public int queryAccounttjCounts(Map<String, Object> paramMap);
	//获取按条件查询所有
	public List<Map<String, String>> queryAccounttjList(Map<String, Object> paramMap);
	//导出excel
	public List<Map<String, String>> queryAccounttjExcel(Map<String, Object> paramMap);
	/** 查询 统计表 记录数
	 * @param AccountStatistics
	 * @return
	 */
	public Integer queryAccountStatisticsTotal(AccountStatistics accountStatistics);
	
	/** 查询 统计表 信息
	 * @param accountStatistics
	 * @return
	 */
	public List<AccountStatistics> queryAccountStatistics(AccountStatistics accountStatistics);
	
	/**
	 * 查询总帐号数量
	 * @return
	 */
	public HashMap<String, String> queryAccountTotals();
	
	/**查询新增帐号
	 * @param date
	 * @return
	 */
	public Integer queryNewaddAccountTotal(Date date);
	
	/** 查询总白名单数量
	 * @param date
	 * @return
	 */
	public Integer queryWhiteListTotal();
	
	/** 查询剩余可添加联系人数量
	 * @param date
	 * @return
	 */
	public Integer querySurplusPassengerTotal();
	
	/**查询总票数
	 * @param date
	 * @return
	 */
	public Integer queryTicketTotal(Date date);
	
	/** 查询匹配白名单成功个数
	 * @param date
	 * @return
	 */
	public Integer queryMatchWhiteListTotal(Date date);
	
	/** 查询停用帐号
	 * @param date
	 * @return
	 */
	public Integer queryAccountStopTotal(Date date);
	
	/** 查询联系人大上限
	 * @param date
	 * @return
	 */
	public Integer queryAccountOfUpperlimit(Date date);
	
	/** 查询帐号待核验
	 * @param date
	 * @return
	 */
	public Integer queryAccountOfCheckUser(Date date);
	
	/** 查询手机待核验
	 * @param date
	 * @return
	 */
	public Integer queryAccountOfCheckPhone(Date date);
	
	/** 查询x人单
	 * @param date
	 * @param x 
	 * @return
	 */
	public Integer queryTicketX(Date date, int x);
	/**
	 * @param statistics
	 */
	public void insertStatistics(AccountStatistics statistics);
	/**
	 * @param date
	 * @return
	 */
	public Integer queryAddWhiteListTotal(Date date);
}
