package com.nineteen.service;

import java.util.List;
import java.util.Map;

import com.nineteen.vo.AlipayUser;
/**
 * 支付宝账号余额操作֧
 * @author wangxg
 *
 */
public interface Alipay_balanceService {
	
	//查询航旅版账号
	List<AlipayUser> getAirInfo();
	//更新航旅版时间
	int updateAir(Map<String,String> map);
	//查询
	List<AlipayUser> getInfo();
	//更新
	int updateBalance(Map<String,String> map);
	
	int updateDate(Map<String,String> map);
	//查询支付宝验证码
	List queryAlpayCode(Map<String,String> map);
	//查询当天该账户下的账单是否已经下载
	int queryAlipayBill(Map<String,String> map);
	//插入下载后账单
	int insertAlipayBill(Map<String,String> map);
	//查询当月中日历前缀差值
	String queryCalendar(Map<String,String> map);
}
