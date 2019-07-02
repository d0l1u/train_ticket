package com.train.system.center.service.impl;

import com.train.system.center.jedis.JedisClient;

import javax.annotation.Resource;

public class BaseService {
	@Resource
	protected JedisClient privateLockJedisClient;

	protected String lock(String key, String value, int timeout) {
		String lockResult = "";
		try {
			lockResult = privateLockJedisClient.set(key, value, "NX", "EX", timeout);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lockResult;
	}

	Object release(String key, String value) {
		Object returnValue = null;
		try {
			returnValue = privateLockJedisClient.eval(
					"if redis.call(\"get\",KEYS[1]) == ARGV[1] then return redis.call(\"del\",KEYS[1]) else return 0 end",
					1, key, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnValue;
	}

}
