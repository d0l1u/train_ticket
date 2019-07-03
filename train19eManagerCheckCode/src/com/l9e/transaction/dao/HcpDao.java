package com.l9e.transaction.dao;

public interface HcpDao {

	int queryAdminCurrentNameCount();

	int queryCodeCountToday();

	int queryCodeToday();

	int queryMinutes();

	int querySuccess1();

	int querySuccess2();

	int queryUncode(String channel);

	String codeQunarType();
}
