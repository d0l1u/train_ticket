package com.l9e.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

public abstract class BaseRedis {

	@Autowired
	protected RedisTemplate<String,String> redisTemplate ;

	/** 
	 * 设置redisTemplate 
	 * @param redisTemplate the redisTemplate to set 
	 */  
	public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {  
		this.redisTemplate = redisTemplate;  
	}  
	  
	/** 
	 * 获取 RedisSerializer 
	 * <br>------------------------------<br> 
	 */  
	private RedisSerializer<String> getRedisSerializer() {  
		return redisTemplate.getStringSerializer();  
	}  
	
	/**
	 *获取key 操作
	 * @param key
	 * @return
	 */
	protected byte[] getRedisKeySerializer(String key){
		return getRedisSerializer().serialize(key);
	}

	
}
