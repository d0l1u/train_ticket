package com.l9e.transaction.service;

public interface HcpService {

	int queryAdminCurrentNameCount();

	int queryCodeCountToday();

	int queryCodeToday();

	int queryMinutes();

	int querySuccess1();

	int querySuccess2();

	int queryUncode(String channel);

	String codeQunarType();
}
