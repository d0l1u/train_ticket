package com.l9ea.train.dao;

/**
 * redis持久接口
 * @author licheng
 *
 */
public interface RedisDao {
	
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
	public boolean setVal(String key,Object obj,long timeout);
	
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
	
	public long getTTL(String key);
	
	public void setExpire(String key ,long time );
	
	/**
	 * 向队列key中添加value值
	 * @param key
	 * @param value
	 */
	public void LPUSH(String key, Object value);
	
	/**
	 * 从队列key中获取值
	 * @param key
	 * @return
	 */
	public Object RPOP(String key);
	
	/**
	 * 获取队列key长度
	 * @param key
	 * @return
	 */
	public Long LLEN(String key);
	
	/**
	 * 从队列key中移除count个元素value
	 * 
	 * @param key
	 * @param count 0表示全部value被移除 正数从头到尾，负数从尾到头
	 * @param value
	 * @return
	 */
	public Long LREM(String key, int count, Object value);
	
	/**
	 * 向图表mapKey中放入键值对field-value
	 * 
	 * @param mapKey
	 * @param field
	 * @param value
	 */
	public void HSET(String mapKey, String field, Object value);
	
	/**
	 * 从图表mapKey中取出field的对应value
	 * 
	 * @param mapKey
	 * @param field
	 * @return
	 */
	public Object HGET(String mapKey, String field);
	
	/**
	 * 设置缓存键值对
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean setCacheVal(String key, Object value);
	
	/**
	 * 设置缓存key有效时间
	 * @param key
	 * @param timeout 单位：秒
	 */
	public Boolean setCacheExpire(String key, long timeout);
	
	/**
	 * 获取缓存值
	 * @param key
	 * @return
	 */
	public Object getCacheVal(String key);
}
