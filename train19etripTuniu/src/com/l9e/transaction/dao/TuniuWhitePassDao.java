package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.TuniuWhitePass;

public interface TuniuWhitePassDao {
	List<TuniuWhitePass> getWhitePassList(Map<String,Object> param);
}
