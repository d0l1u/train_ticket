package com.l9e.transaction.service;

import java.util.List;

import com.l9e.transaction.vo.TuniuWhitePass;

public interface TuniuWhitePassService {
	List<TuniuWhitePass> getWhitePassList(int lastId,int num);
	
}
