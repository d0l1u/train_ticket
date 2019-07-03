package com.l9e.transaction.redis;

public interface RedisService {
	
	/**
	 * 增加redis值
	 * 存在则返回false
	 * @param key
	 * @param obj
	 */
	public boolean setNXVal(String key,Object obj,long timeout);
	
	
	/**
	 * 删除redis 值
	 * @param key
	 */
	public void delVal(String key);
	
	/**
	 * 获取redis 值
	 * @param key
	 */
	public Object getVal(String key);
	
	/**
	 * 设置redis 值存在则更新
	 * @param key
	 * @param obj
	 * @param timeout
	 */
	public boolean setVal(String key, Object obj, long timeout);
	
	/**
	 * 如果key不存在 增加 默认为0 然后增加1
	 * @param key
	 */
	public long INCR(String key);
	
	/**
	 * 如果key 不存在默认为零 值减去1
	 * @param key
	 */
	public void DECR(String key);
	
	/**
	 * 获取递增值
	 * @param key
	 * @return
	 */
	public long getINDEVal(String key);
	
	
}
