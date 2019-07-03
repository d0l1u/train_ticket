package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.Phone;

public interface PhonePlatDao {
	List<Phone> queryWaitPhoneList();
	
	void updatePhoneNum(Map<String,Object> map);
	
	void addPhone(Phone phone);
	
	Integer queryPhoneByPhone(Map<String,String> map);
	
	Integer queryPhoneNumHour(Map<String,String> map);
	
	Integer queryPhoneNumDay(Map<String,String> map);
}
