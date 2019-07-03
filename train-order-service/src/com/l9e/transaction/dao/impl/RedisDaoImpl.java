package com.l9e.transaction.dao.impl;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.stereotype.Service;

import com.l9e.common.BaseRedis;
import com.l9e.transaction.dao.RedisDao;
import com.l9e.util.SerializeUtil;


@Service("redisDao")
public class RedisDaoImpl extends BaseRedis implements RedisDao{
	
	/*
	 * 增加redis 的
	 * redis 的原子性增加，判断如果有则返回增加失败
	 * timeout 秒数
	 * @see com.retrieve.transaction.dao.RedisDao#setVal(java.lang.String, java.lang.Object)
	 */
	@Override
	public boolean setNXVal(final String key, final Object obj,final long timeout) {
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {  
			public Boolean doInRedis(RedisConnection connection)  
					throws DataAccessException {  
				byte[] rediskey = getRedisKeySerializer(key);
				boolean flag = connection.setNX(rediskey, SerializeUtil.serialize(obj));  
				//timeout 设置为0 表示永久不过期
				if(flag&&0!=timeout){
					connection.expire(rediskey, timeout);
				}
				return flag;
			}  
		});  
		return result;  
	}
	

	
	/*
	 * 设置redis 值存在则更新
	 * @see com.retrieve.transaction.dao.RedisDao#setVal(java.lang.String, java.lang.Object, long)
	 */
	@Override
	public boolean setVal(final String key, final Object obj, final long timeout) {
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {  
			public Boolean doInRedis(RedisConnection connection)  
					throws DataAccessException {  
				byte[] rediskey = getRedisKeySerializer(key);
				connection.set(rediskey, SerializeUtil.serialize(obj));  
				connection.expire(rediskey, timeout);
				return true;
			}  
		});  
		return result;  
	}
	
	
	/*
	 * redis 删除key
	 * @see com.retrieve.transaction.dao.RedisDao#delVal(java.lang.String)
	 */
	@Override
	public void delVal(String key) {
		 this.redisTemplate.delete(key);
	}

	/*
	 * 获取redis 里面的值
	 * @see com.retrieve.transaction.dao.RedisDao#getVal(java.lang.String)
	 */
	@Override
	public Object getVal(final String key) {
		Object result = redisTemplate.execute(new RedisCallback<Object>() {  
            public Object doInRedis(RedisConnection connection)  
                    throws DataAccessException {  
                byte[] value = connection.get(getRedisKeySerializer(key));  
               
                if (value == null) {  
                    return null;  
                }  
              //  System.out.println(SerializeUtil.unserialize(value)+"--------------------------------");
                
                return SerializeUtil.unserialize(value);  
            }  
        });  
        return result;  
	}

	/*
	 * 值加1
	 * @see com.retrieve.transaction.service.base.redis.RedisService#INCR(java.lang.String)
	 */
	@Override
	public long INCR(final String key) {
		return (Long) redisTemplate.execute(new RedisCallback<Object>() {  
            public Object doInRedis(RedisConnection connection)  
                    throws DataAccessException {  
                 long val = connection.incr(getRedisKeySerializer(key));  
                 return val;
            }  
        }); 
	}

	/*
	 * 值减1
	 * @see com.retrieve.transaction.service.base.redis.RedisService#DECR(java.lang.String)
	 */
	@Override
	public void DECR(final String key) {
		redisTemplate.execute(new RedisCallback<Object>() {  
            public Object doInRedis(RedisConnection connection)  
                    throws DataAccessException {  
                long val = connection.decr(getRedisKeySerializer(key));  
               return val;
            }  
        }); 
	}



	@Override
	public long getINDEVal(final String key) {
		return (Long) redisTemplate.execute(new RedisCallback<Object>() {  
            public Object doInRedis(RedisConnection connection)  
                    throws DataAccessException {
            	byte[] keyb = getRedisKeySerializer(key);
            	connection.incr(keyb);
                long val = connection.decr(keyb);  
               return val;
            }  
        }); 
	}



	@Override
	public long getTTL(final String key) {
		 return (Long)redisTemplate.execute(new RedisCallback<Object>() {  
            public Object doInRedis(RedisConnection connection)  
                    throws DataAccessException {
            	byte[] keyb = getRedisKeySerializer(key);
            	return connection.ttl(keyb);
            }  
        }); 
	}



	@Override
	public void setExpire(final String key, final long time) {
		redisTemplate.execute(new RedisCallback<Object>() {  
            public Object doInRedis(RedisConnection connection)  
                    throws DataAccessException {
            	byte[] keyb = getRedisKeySerializer(key);
            	return connection.expire(keyb,time);
            }  
        }); 		
	}



//	@Override
//	public Boolean exists(final String key) {
//		return redisTemplate.execute(new RedisCallback<Boolean>() {
//
//			@Override
//			public Boolean doInRedis(RedisConnection conn)
//					throws DataAccessException {
//				byte[] keyByte = getRedisKeySerializer(key);
//				return conn.exists(keyByte);
//			}
//		});
//		
//	}



	@Override
	public Long LLEN(final String key) {
		return redisTemplate.execute(new RedisCallback<Long>(){

			@Override
			public Long doInRedis(RedisConnection conn)
					throws DataAccessException {
				return conn.lLen(getRedisKeySerializer(key));
			}
			
		});
	}



	@Override
	public void LPUSH(final String key, final Object value) {
		redisTemplate.execute(new RedisCallback<Object>() {

			@Override
			public Object doInRedis(RedisConnection conn)
					throws DataAccessException {
				return conn.lPush(getRedisKeySerializer(key), SerializeUtil.serialize(value));
			}
		});
	}



	@Override
	public Object RPOP(final String key) {
		return redisTemplate.execute(new RedisCallback<Object>() {

			@Override
			public Object doInRedis(RedisConnection conn)
					throws DataAccessException {
				byte[] valueByte = conn.rPop(getRedisKeySerializer(key));
				return SerializeUtil.unserialize(valueByte);
			}
		});
	}



	@Override
	public Long LREM(final String key, final int count, final Object value) {
		return redisTemplate.execute(new RedisCallback<Long>() {

			@Override
			public Long doInRedis(RedisConnection conn)
					throws DataAccessException {
				return conn.lRem(getRedisKeySerializer(key), count, SerializeUtil.serialize(value));
			}
		});
	}



	@Override
	public Object HGET(final String mapKey, final String field) {
		return redisTemplate.execute(new RedisCallback<Object>() {

			@Override
			public Object doInRedis(RedisConnection conn)
					throws DataAccessException {
				byte[] keyByte = getRedisKeySerializer(mapKey);
				byte[] fieldByte = getRedisKeySerializer(field);
				byte[] valueByte = conn.hGet(keyByte, fieldByte);
				return SerializeUtil.unserialize(valueByte);
			}
			
		});
	}



	@Override
	public void HSET(final String mapKey, final String field, final Object value) {
		redisTemplate.execute(new RedisCallback<Object>() {

			@Override
			public Object doInRedis(RedisConnection conn)
					throws DataAccessException {
				byte[] keyByte = getRedisKeySerializer(mapKey);
				byte[] fieldByte = getRedisKeySerializer(field);
				byte[] valueByte = SerializeUtil.serialize(value);
				return conn.hSet(keyByte, fieldByte, valueByte);
			}
			
		});
	}



	@Override
	public Boolean setCacheExpire(final String key, final long timeout) {
		return cacheRedisTemplate.execute(new RedisCallback<Boolean>() {

			@Override
			public Boolean doInRedis(RedisConnection conn)
					throws DataAccessException {
				byte[] keyByte = getCacheRedisKeySerializer(key);
				return conn.expire(keyByte, timeout);
			}
		});
	}



	@Override
	public boolean setCacheVal(final String key, final Object value) {
		return cacheRedisTemplate.execute(new RedisCallback<Boolean>() {

			@Override
			public Boolean doInRedis(RedisConnection conn)
					throws DataAccessException {
				byte[] keyByte = getCacheRedisKeySerializer(key);
				byte[] valueByte = SerializeUtil.serialize(value);
				conn.set(keyByte, valueByte);
				return true;
			}
		});
	}



	@Override
	public Object getCacheVal(final String key) {
		return cacheRedisTemplate.execute(new RedisCallback<Object>() {

			@Override
			public Object doInRedis(RedisConnection conn)
					throws DataAccessException {
				byte[] keyByte = getCacheRedisKeySerializer(key);
				byte[] valueByte = conn.get(keyByte);
				return SerializeUtil.unserialize(valueByte);
			}
			
		});
	}

}
