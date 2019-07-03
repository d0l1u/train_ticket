package com.l9e.transaction.redis;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.stereotype.Service;

import com.l9e.common.BaseRedis;
import com.l9e.util.SerializeUtil;



@Service("redisService")
public class RedisServiceImpl extends BaseRedis implements RedisService{
	
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
//              System.out.println(SerializeUtil.unserialize(value)+"--------------------------------");
                
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





}
