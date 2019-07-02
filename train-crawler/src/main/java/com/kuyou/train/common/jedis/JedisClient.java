package com.kuyou.train.common.jedis;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.*;
import redis.clients.jedis.ZParams.Aggregate;
import redis.clients.jedis.params.sortedset.ZAddParams;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName: SingleJedisClient
 * @Description: 单机Jedis客户端
 * @author: taokai
 * @date: 2017年8月30日 下午5:24:29
 * @Copyright: 2017 www.19e.cn Inc. All rights reserved.
 */
@Slf4j
public class JedisClient implements JedisInterface<Jedis> {

    private static final String NX = "NX";
    private static final String XX = "XX";
    private static final String EX = "EX";
    private static final String PX = "PX";

    private static final String STRING_SUCCESS = "OK";
    private static final Long LONG_SUCCESS = Long.valueOf(1);

    private static final String LOCK_SUCCESS = "OK";
    private static final String RELEASE_SHELL = "if redis.call(\"get\",KEYS[1]) == ARGV[1] then return redis.call(\"del\",KEYS[1]) else return 0 end";
    private static final Long RELEASE_SUCCESS = 1L;

    /**
     * 通过Spring配置注入JedisPool
     */
    private JedisPool jedisPool;

    @Override
    public Jedis getResource() {
        /*
        Jedis jedis = null;
        int max = 3;
        while (jedis == null) {
            if (max != 3) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (max <= 0) {
                throw new JedisConnectionException("Get jedis client fail, Because exceeding the maximum number");
            }
            try {
                jedis = jedisPool.getResource();
            } catch (JedisConnectionException e) {
                log.info("Get jedis client exception max:{}, message:{}", (Math.abs(max - 3)), e.getMessage());
            }
            max--;
        }
        return jedis;
        */

        return jedisPool.getResource();
    }

    @Override
    public void close(Jedis jedis) {
        if (jedis != null) {
            jedis.quit();
            jedis.close();
        }
    }

    @Override
    public Long del(String key) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.del(key);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long del(String... keys) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.del(keys);
        } finally {
            close(jedis);
        }
    }

    @Override
    public byte[] dump(String key) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.dump(key);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Boolean exists(String key) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.exists(key);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long exists(String... keys) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.exists(keys);
        } finally {
            close(jedis);
        }
    }

    @Override
    public boolean expire(String key, int seconds) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return LONG_SUCCESS.equals(jedis.expire(key, seconds));
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long expireat(String key, long unixTime) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.expireAt(key, unixTime);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long pexpire(String key, long milliseconds) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.pexpire(key, milliseconds);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long pexpireAt(String key, long millisecondsTimestamp) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.pexpireAt(key, millisecondsTimestamp);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Set<String> keys(String pattern) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.keys(pattern);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long move(String key, int dbIndex) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.move(key, dbIndex);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long persist(String key) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.persist(key);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long pttl(String key) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.pttl(key);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long ttl(String key) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.ttl(key);
        } finally {
            close(jedis);
        }
    }

    @Override
    public String rename(String oldkey, String newkey) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.rename(oldkey, newkey);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long renamenx(String oldkey, String newkey) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.renamenx(oldkey, newkey);
        } finally {
            close(jedis);
        }
    }

    @Override
    public String type(String key) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.type(key);
        } finally {
            close(jedis);
        }
    }

    @Override
    public boolean set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return STRING_SUCCESS.equals(jedis.set(key, value));
        } finally {
            close(jedis);
        }
    }

    @Override
    public boolean set(String key, String value, int time) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            jedis.set(key, value);
            jedis.expire(key, time);
            return STRING_SUCCESS.equals(jedis.set(key, value));
        } finally {
            close(jedis);
        }
    }

    @Override
    public String get(String key) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.get(key);
        } finally {
            close(jedis);
        }
    }

    @Override
    public String getrange(String key, long startIndex, long endIndex) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.getrange(key, startIndex, endIndex);
        } finally {
            close(jedis);
        }
    }

    @Override
    public String getSet(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.getSet(key, value);
        } finally {
            close(jedis);
        }
    }

    @Override
    public List<String> mget(String... keys) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.mget(keys);
        } finally {
            close(jedis);
        }
    }

    @Override
    public String setex(String key, int seconds, String value) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.setex(key, seconds, value);
        } finally {
            close(jedis);
        }
    }

    @Override
    public String psetex(String key, long milliseconds, String value) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.psetex(key, milliseconds, value);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long setnx(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.setnx(key, value);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long setrange(String key, long startIndex, String value) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.setrange(key, startIndex, value);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long strlen(String key) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.strlen(key);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long incr(String key) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.incr(key);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long incrby(String key, int addInt) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.incrBy(key, addInt);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long decrby(String key, int subInt) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.decrBy(key, subInt);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Double incrbyfloat(String key, double doubleValue) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.incrByFloat(key, doubleValue);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long append(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.append(key, value);
        } finally {
            close(jedis);
        }
    }

    @Override
    public List<String> blpop(int timeout, String key) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.blpop(timeout, key);
        } finally {
            close(jedis);
        }
    }

    @Override
    public List<String> brpop(int timeout, String key) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.brpop(timeout, key);
        } finally {
            close(jedis);
        }
    }

    @Override
    public String brpoplpush(String source, String destination, int timeout) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.brpoplpush(source, destination, timeout);
        } finally {
            close(jedis);
        }
    }

    @Override
    public String lindex(String key, long index) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.lindex(key, index);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long linsert(String key, LIST_POSITION where, String pivot, String value) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.linsert(key, where, pivot, value);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long llen(String key) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.llen(key);
        } finally {
            close(jedis);
        }
    }

    @Override
    public String lpop(String key) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.lpop(key);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long lpush(String key, String... values) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.lpush(key, values);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long lpushx(String key, String... values) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.lpushx(key, values);
        } finally {
            close(jedis);
        }
    }

    @Override
    public List<String> lrange(String key, long start, long end) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.lrange(key, start, end);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long lrem(String key, long count, String value) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.lrem(key, count, value);
        } finally {
            close(jedis);
        }
    }

    @Override
    public String lset(String key, long index, String value) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.lset(key, index, value);
        } finally {
            close(jedis);
        }
    }

    @Override
    public String ltrim(String key, long start, long end) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.ltrim(key, start, end);
        } finally {
            close(jedis);
        }
    }

    @Override
    public String rpop(String key) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.rpop(key);
        } finally {
            close(jedis);
        }
    }

    @Override
    public String rpoplpush(String srckey, String dstkey) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.rpoplpush(srckey, dstkey);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long rpush(String key, String... values) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.rpush(key, values);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long rpushx(String key, String... values) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.rpushx(key, values);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long sadd(String key, String... values) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.sadd(key, values);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long scard(String key) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.scard(key);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Set<String> sdiff(String keys) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.sdiff(keys);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long sdiffstore(String dstkey, String... keys) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.sdiffstore(dstkey, keys);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Set<String> sinter(String... keys) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.sinter(keys);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long sinterstore(String dstkey, String... keys) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.sinterstore(dstkey, keys);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Boolean sismember(String key, String member) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.sismember(key, member);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Set<String> smembers(String key) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.smembers(key);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long smove(String srckey, String dstkey, String member) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.smove(srckey, dstkey, member);
        } finally {
            close(jedis);
        }
    }

    @Override
    public String spop(String key) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.spop(key);
        } finally {
            close(jedis);
        }
    }

    @Override
    public String srandmember(String key) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.srandmember(key);
        } finally {
            close(jedis);
        }
    }

    @Override
    public List<String> srandmember(String key, int count) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.srandmember(key, count);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long srem(String key, String... members) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.srem(key, members);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Set<String> sunion(String... keys) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.sunion(keys);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long sunionstore(String dstkey, String... keys) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.sunionstore(dstkey, keys);
        } finally {
            close(jedis);
        }
    }

    @Override
    public ScanResult<String> sscan(String key, String cursor) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.sscan(key, cursor);
        } finally {
            close(jedis);
        }
    }

    @Override
    public ScanResult<String> sscan(String key, String cursor, ScanParams params) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.sscan(key, cursor, params);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long zadd(String key, double score, String member) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.zadd(key, score, member);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long zaddNx(String key, double score, String member) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.zadd(key, score, member, ZAddParams.zAddParams().nx());
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long zaddXx(String key, double score, String member) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.zadd(key, score, member, ZAddParams.zAddParams().xx());
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long zadd(String key, Map<String, Double> scoreMembers) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.zadd(key, scoreMembers);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long zaddNx(String key, Map<String, Double> scoreMembers) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.zadd(key, scoreMembers, ZAddParams.zAddParams().nx());
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long zaddXx(String key, Map<String, Double> scoreMembers) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.zadd(key, scoreMembers, ZAddParams.zAddParams().xx());
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long zcard(String key) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.zcard(key);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long zcount(String key, String min, String max) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.zcount(key, min, max);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long zcount(String key, double min, double max) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.zcount(key, min, max);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Double zincrby(String key, double score, String member) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.zincrby(key, score, member);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long zinterstore(String dstkey, String... setKeys) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.zinterstore(dstkey, setKeys);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long zinterstore(String dstkey, Aggregate aggregate, String... setKeys) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.zinterstore(dstkey, new ZParams().aggregate(aggregate), setKeys);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long zinterstore(String dstkey, ZParams zParams, String... setKeys) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.zinterstore(dstkey, zParams, setKeys);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long zlexcount(String key, String minMember, String maxMember) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.zlexcount(key, minMember, maxMember);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Set<String> zrange(String key, long startIndex, long endIndex) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.zrange(key, startIndex, endIndex);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Set<Tuple> zrangeWithScores(String key, long startIndex, long endIndex) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.zrangeWithScores(key.getBytes(), startIndex, endIndex);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Set<String> zrangeByLex(String key, String memberMin, String memberMax) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.zrangeByLex(key, memberMin, memberMax);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Set<String> zrangeByScore(String key, double scoreMin, double scoreMax) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.zrangeByScore(key, scoreMin, scoreMax);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Set<String> zrangeByScore(String key, double scoreMin, double scoreMax, int offset, int end) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.zrangeByScore(key, scoreMin, scoreMax, offset, end);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Set<String> zrangeByScore(String key, String scoreMin, String scoreMax) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.zrangeByScore(key, scoreMin, scoreMax);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Set<String> zrangeByScore(String key, String scoreMin, String scoreMax, int offset, int end) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.zrangeByScore(key, scoreMin, scoreMax, offset, end);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, double scoreMin, double scoreMax) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.zrangeByScoreWithScores(key, scoreMin, scoreMax);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, double scoreMin, double scoreMax, int offset, int end) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.zrangeByScoreWithScores(key, scoreMin, scoreMax, offset, end);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, String scoreMin, String scoreMax) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.zrangeByScoreWithScores(key, scoreMin, scoreMax);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, String scoreMin, String scoreMax, int offset, int end) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.zrangeByScoreWithScores(key, scoreMin, scoreMax, offset, end);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long zrank(String key, String member) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.zrank(key, member);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long zrem(String key, String... members) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.zrem(key, members);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long zremrangeByLex(String key, String memberMin, String memberMax) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.zremrangeByLex(key, memberMin, memberMax);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long zremrangeByRank(String key, long startIndex, long endIndex) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.zremrangeByRank(key, startIndex, endIndex);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long zremrangeByScore(String key, double scoreMin, double scoreMax) {

        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.zremrangeByScore(key, scoreMin, scoreMax);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Set<String> zrevrange(String key, long startIndex, long endIndex) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.zrevrange(key, startIndex, endIndex);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Set<String> zrevrangeByScore(String key, double scoreMax, double scoreMin) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.zrevrangeByScore(key, scoreMax, scoreMin);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Set<String> zrevrangeByScore(String key, double scoreMax, double scoreMin, int offset, int end) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.zrevrangeByScore(key, scoreMax, scoreMin, offset, end);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Set<String> zrevrangeByScore(String key, String scoreMax, String scoreMin) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.zrevrangeByScore(key, scoreMax, scoreMin);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Set<String> zrevrangeByScore(String key, String scoreMax, String scoreMin, int offset, int end) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.zrevrangeByScore(key, scoreMax, scoreMin, offset, end);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, double scoreMax, double scoreMin) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.zrevrangeByScoreWithScores(key, scoreMax, scoreMin);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, double scoreMax, double scoreMin, int offset, int end) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.zrevrangeByScoreWithScores(key, scoreMax, scoreMin, offset, end);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, String scoreMax, String scoreMin) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.zrevrangeByScoreWithScores(key, scoreMax, scoreMin);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, String scoreMax, String scoreMin, int offset, int end) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.zrevrangeByScoreWithScores(key, scoreMax, scoreMin, offset, end);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long zrevrank(String key, String member) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.zrevrank(key, member);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Double zscore(String key, String member) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.zscore(key, member);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long zunionstore(String dstkey, String... setKeys) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.zunionstore(dstkey, setKeys);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long zunionstore(String dstkey, Aggregate aggregate, String... setKeys) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.zunionstore(dstkey, new ZParams().aggregate(aggregate), setKeys);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long zunionstore(String dstkey, ZParams zParams, String... setKeys) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.zinterstore(dstkey, zParams, setKeys);
        } finally {
            close(jedis);
        }
    }

    @Override
    public ScanResult<Tuple> zscan(String key, String cursor) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.zscan(key, cursor);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long hdel(String key, String... fields) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.hdel(key, fields);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Boolean hexists(String key, String field) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.hexists(key, field);
        } finally {
            close(jedis);
        }
    }

    @Override
    public String hget(String key, String field) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.hget(key, field);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Map<String, String> hgetAll(String key) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.hgetAll(key);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long hincrby(String key, String field, long value) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.hincrBy(key, field, value);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Double hincrByFloat(String key, String field, double value) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.hincrByFloat(key, field, value);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Set<String> hkeys(String key) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.hkeys(key);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long hlen(String key) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.hlen(key);
        } finally {
            close(jedis);
        }
    }

    @Override
    public List<String> hmget(String key, String... fields) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.hmget(key, fields);
        } finally {
            close(jedis);
        }
    }

    @Override
    public String hmset(String key, Map<String, String> hash) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.hmset(key, hash);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long hset(String key, String field, String value) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.hset(key, field, value);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long hsetnx(String key, String field, String value) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.hsetnx(key, field, value);
        } finally {
            close(jedis);
        }
    }

    @Override
    public List<String> hvals(String key) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.hvals(key);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long pfadd(String key, String... elements) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.pfadd(key, elements);
        } finally {
            close(jedis);
        }
    }

    @Override
    public long pfadd(String... keys) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.pfcount(keys);
        } finally {
            close(jedis);
        }
    }

    @Override
    public String pfmerge(String destkey, String... sourcekeys) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.pfmerge(destkey, sourcekeys);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Object eval(String script, int keyCount, String... params) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.eval(script, keyCount, params);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Object eval(String script, List<String> keys, List<String> args) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.eval(script, keys, args);
        } finally {
            close(jedis);
        }
    }

    @Override
    public String set(String key, String value, String nxxx, String expx, int ms) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.set(key, value, nxxx, expx, ms);
        } finally {
            close(jedis);
        }
    }

    /**
     * 分布式锁: 只在键已经存在时，才对键进行设置操作。
     *
     * @param key
     * @param value
     * @param expireSecond
     * @return
     */
    public boolean lockByXX(String key, String value, int expireSecond) {
        return LOCK_SUCCESS.equals(set(key, value, XX, EX, expireSecond)) ? true : false;
    }

    /**
     * 分布式锁: 只在键不存在时，才对键进行设置操作。
     *
     * @param key
     * @param value
     * @param expireSecond
     * @return
     */
    public boolean lockByNX(String key, String value, int expireSecond) {
        return LOCK_SUCCESS.equals(set(key, value, NX, EX, expireSecond)) ? true : false;
    }

    /**
     * 释放锁
     *
     * @param key
     * @param value
     * @return
     */
    public boolean releaseLock(String key, String value) {
        Jedis jedis = null;
        Object returnValue;
        try {
            jedis = getResource();
            returnValue = jedis.eval(RELEASE_SHELL, 1, key, value);
        } finally {
            close(jedis);
        }
        return RELEASE_SUCCESS.equals(returnValue) ? true : false;
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }
}
