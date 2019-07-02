package com.nineteen.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nineteen.mapper.CountMapper;
import com.nineteen.service.Alipay_balanceService;
import com.nineteen.vo.AlipayUser;

@Component("alipay_balanceService")
public class Alipay_balanceServiceImpl implements Alipay_balanceService {

	@Autowired  
    public CountMapper countMapper;
	@Override
	public List<AlipayUser> getInfo() {
		// TODO Auto-generated method stub
		return countMapper.getInfo();
	}
	@Override
	public int updateBalance(Map<String, String> map) {
		// TODO Auto-generated method stub
		return countMapper.updateBalance(map);
	}
	@Override
	public int updateDate(Map<String, String> map) {
		// TODO Auto-generated method stub
		return countMapper.updateDate(map);
	}
	@Override
	public List queryAlpayCode(Map<String, String> map) {
		// TODO Auto-generated method stub
		return countMapper.queryAlpayCode(map);
	}
	@Override
	public int queryAlipayBill(Map<String, String> map) {
		// TODO Auto-generated method stub
		return countMapper.queryAlipayBill(map);
	}
	@Override
	public int insertAlipayBill(Map<String, String> map) {
		// TODO Auto-generated method stub
		return countMapper.insertAlipayBill(map);
	}
	@Override
	public String queryCalendar(Map<String, String> map) {
		// TODO Auto-generated method stub
		return countMapper.queryCalendar(map);
	}
	@Override
	public List<AlipayUser> getAirInfo() {
		// TODO Auto-generated method stub
		return countMapper.getAirInfo();
	}
	@Override
	public int updateAir(Map<String, String> map) {
		// TODO Auto-generated method stub
		return countMapper.updateAir(map);
	}
}
