package com.l9ea.train.dao;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

public abstract class BaseRedis {

	@Resource
	protected RedisTemplate<String, String> redisTemplate;

	@Resource
	protected RedisTemplate<String, String> cacheRedisTemplate;

	/**
	 * 设置redisTemplate
	 * 
	 * @param redisTemplate
	 *            the redisTemplate to set
	 */
	public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	/**
	 * 设置cacheRedisTemplate
	 * 
	 * @param cacheRedisTemplate
	 */
	public void setCacheRedisTemplate(
			RedisTemplate<String, String> cacheRedisTemplate) {
		this.cacheRedisTemplate = cacheRedisTemplate;
	}

	/**
	 * 获取 RedisSerializer
	 * 
	 */
	private RedisSerializer<String> getRedisSerializer() {
		return redisTemplate.getStringSerializer();
	}

	/**
	 *获取key 操作
	 * 
	 * @param key
	 * @return
	 */
	protected byte[] getRedisKeySerializer(String key) {
		return getRedisSerializer().serialize(key);
	}

	/**
	 * 获取key操作
	 * 
	 * @param key
	 * @return
	 */
	protected byte[] getCacheRedisKeySerializer(String key) {
		return cacheRedisTemplate.getStringSerializer().serialize(key);
	}
}
