package com.kuyou.train.common.jedis;

import redis.clients.jedis.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * JedisInterface
 *
 * @author taokai3
 * @date 2018/10/28
 */
public interface JedisInterface<T> {

    /**
     * 获取jedis客户端
     *
     * @return T
     * @author: taoka
     * @date: 2017年11月14日 下午6:57:12
     */
    T getResource();

    /**
     * 关闭客户端
     *
     * @param jedis
     * @author: taokai
     * @date: 2017年8月30日 下午3:39:52
     */
    void close(T jedis);

    /** ===================== KEY TODO ================== */

    /**
     * 删除KEY，返回被删除 key的数量，不存返回0。
     *
     * @param key
     * @return Long
     * @author: taokai
     * @date: 2017年8月30日 下午4:13:07
     */
    Long del(String key);

    /**
     * 删除KEY，返回被删除 key的数量，不存返回0。
     *
     * @param keys
     * @return Long
     * @author: taokai
     * @date: 2017年8月21日 下午2:29:14
     */
    Long del(String... keys);

    /**
     * 命令用于序列化指定 key ，并返回被序列化的值。如果 key 不存在，那么返回 null
     *
     * @param key
     * @return byte[]
     * @author: taokai
     * @date: 2017年8月21日 下午2:43:04
     */
    byte[] dump(String key);

    /**
     * 是否存在指定key
     *
     * @param key
     * @return Boolean
     * @author: taokai
     * @date: 2017年8月21日 下午2:45:24
     */
    Boolean exists(String key);

    /**
     * 是否存在指定key，返回存在个数
     *
     * @param keys
     * @return Boolean
     * @author: taokai
     * @date: 2017年8月21日 下午2:45:24
     */
    Long exists(String... keys);

    /**
     * 设置 key 的过期时间，单位：秒。成功返回1，失败返回0(从当前时间算起)
     *
     * @param key
     * @param seconds
     * @return Long
     * @author: taokai
     * @date: 2017年8月21日 下午2:52:58
     */
    boolean expire(String key, int seconds);

    /**
     * 设置 key 的过期时间，单位：时间戳(秒)。成功返回1，失败返回0(时间戳对应的时间)
     *
     * @param key
     * @param unixTime
     * @return Long
     * @author: taokai
     * @date: 2017年8月21日 下午2:57:42
     */
    Long expireat(String key, long unixTime);

    /**
     * 设置 key 的过期时间，单位：毫秒。成功返回1，失败返回0(从当前时间算起)
     *
     * @param key
     * @param milliseconds
     * @return Long
     * @author: taokai
     * @date: 2017年8月21日 下午3:03:22
     */
    Long pexpire(String key, long milliseconds);

    /**
     * 设置 key 的过期时间，单位：时间戳(毫秒)。成功返回1，失败返回0(时间戳对应的时间)
     *
     * @param key
     * @param millisecondsTimestamp
     * @return Long
     * @author: taokai
     * @date: 2017年8月21日 下午3:03:40
     */
    Long pexpireAt(String key, long millisecondsTimestamp);

    /**
     * 返回与表达式相匹配的所有key。没有返回空集合
     *
     * @param pattern
     * @return Set<String>
     * @author: taokai
     * @date: 2017年8月21日 下午3:07:46
     */
    Set<String> keys(String pattern);

    /**
     * 用于将当前数据库的 key 移动到给定的数据库 db 当中。成功返回1，失败返回0
     *
     * @param key
     * @param dbIndex
     * @return Long
     * @author: taokai
     * @date: 2017年8月21日 下午3:13:09
     */
    Long move(String key, int dbIndex);

    /**
     * 移除给定 key 的过期时间，使得 key 永不过期。成功返回1，失败返回0
     *
     * @param key
     * @return Long
     * @author: taokai
     * @date: 2017年8月21日 下午3:16:45
     */
    Long persist(String key);

    /**
     * 以毫秒为单位返回 key 的剩余过期时间。当 key 不存在时，返回 -2 。 当 key 存在但没有设置剩余生存时间时，返回 -1 。
     * 否则，以毫秒为单位，返回 key 的剩余生存时间。
     *
     * @param key
     * @return Long
     * @author: taokai
     * @date: 2017年8月21日 下午3:22:45
     */
    Long pttl(String key);

    /**
     * 以秒为单位返回 key 的剩余过期时间。当 key 不存在时，返回 -2 。 当 key 存在但没有设置剩余生存时间时，返回 -1 。
     * 否则，以秒为单位，返回 key 的剩余生存时间。
     *
     * @param key
     * @return Long
     * @author: taokai
     * @date: 2017年8月21日 下午3:23:32
     */
    Long ttl(String key);

    /**
     * 改名成功时提示 OK ，失败时候返回一个错误(redis.clients.jedis.exceptions.
     * JedisDataException)。当 oldkey 和 newkey 相同，或者 oldkey 不存在时，返回一个错误。 当 newkey
     * 已经存在时， rename 命令将覆盖旧值。
     *
     * @param oldkey
     * @param newkey
     * @return String
     * @author: taokai
     * @date: 2017年8月21日 下午3:33:21
     */
    String rename(String oldkey, String newkey);

    /**
     * 在新的 key 不存在时修改 key 的名称 。修改成功时，返回 1 。 如果 newkey 已经存在，返回 0。当 oldkey 和
     * newkey 相同，或者 oldkey 不存在时，返回一个错误。
     *
     * @param oldkey
     * @param newkey
     * @return Long
     * @author: taokai
     * @date: 2017年8月21日 下午3:34:57
     */
    Long renamenx(String oldkey, String newkey);

    /**
     * 返回 key 所储存的值的类型。none：key不存在； string：字符串； list：列表： set：集合； zset：有序集；
     * hash：哈希表
     *
     * @param key
     * @return String
     * @author: taokai
     * @date: 2017年8月21日 下午3:47:03
     */
    String type(String key);

    /**
     * ================================= STRING TODO ========================
     */
    /**
     * 设置给定 key 的值。如果 key 已经存储其他值， SET 就覆写旧值，且无视类型。如果 key
     * 不存在，则会先创建key并存储值。设置成功返回OK
     *
     * @param key
     * @param value
     * @return String
     * @author: taokai
     * @date: 2017年8月21日 下午3:51:24
     */
    boolean set(String key, String value);

    /**
     * 获取指定 key 的值。如果 key 不存在，返回 null 。如果key 储存的值不是字符串类型，返回一个错误
     *
     * @param key
     * @return String
     * @author: taokai
     * @date: 2017年8月21日 下午3:54:57
     */
    String get(String key);

    /**
     * 获取存储在指定 key 中字符串的子字符串。字符串的截取范围由 start 和 end 两个偏移量决定(包括 start 和 end
     * 在内)。如果key不存在，则返回空串
     *
     * @param key
     * @param startIndex
     * @param endIndex
     * @return String
     * @author: taokai
     * @date: 2017年8月21日 下午3:56:43
     */
    String getrange(String key, long startIndex, long endIndex);

    /**
     * 将给定 key 的值设为 value ，并返回 key 的旧值(old value)。
     *
     * @param key
     * @param value
     * @return String
     * @author: taokai
     * @date: 2017年8月30日 下午4:26:48
     */
    String getSet(String key, String value);

    /**
     * 返回所有(一个或多个)给定 key 的值。 如果给定的 key 里面，有某个 key 不存在，那么这个 key 返回特殊值
     * null。返回结果与keys索引对应
     *
     * @param keys
     * @return List<String>
     * @author: taokai
     * @date: 2017年8月21日 下午4:05:19
     */
    List<String> mget(String... keys);

    /**
     * 指定的 key 设置值及其过期时间(单位：秒)。如果 key 已经存在， SETEX 命令将会替换旧的值。设置成功时返回 OK 。
     *
     * @param key
     * @param seconds
     * @param value
     * @return String
     * @author: taokai
     * @date: 2017年8月21日 下午4:29:20
     */
    String setex(String key, int seconds, String value);

    /**
     * 指定的 key 设置值及其过期时间(单位：毫秒)。如果 key 已经存在， SETEX 命令将会替换旧的值。设置成功时返回 OK 。
     *
     * @param key
     * @param milliseconds
     * @param value
     * @return String
     * @author: taokai
     * @date: 2017年8月21日 下午4:29:20
     */
    String psetex(String key, long milliseconds, String value);

    /**
     * 在指定的 key 不存在时，为 key 设置指定的值。设置成功，返回 1 。 设置失败，返回 0
     *
     * @param key
     * @param value
     * @return Long
     * @author: taokai
     * @date: 2017年8月21日 下午4:30:12
     */
    Long setnx(String key, String value);

    /**
     * 用指定的字符串覆盖给定 key 所储存的字符串值，覆盖的位置从偏移量 offset 开始。返回被修改后的字符串长度。
     *
     * @param key
     * @param startIndex
     * @param value
     * @return Long
     * @author: taokai
     * @date: 2017年8月21日 下午4:33:52
     */
    Long setrange(String key, long startIndex, String value);

    /**
     * 获取指定 key 所储存的字符串值的长度。当 key 储存的不是字符串值时，返回一个错误。当 key 不存在时，返回 0。
     *
     * @param key
     * @return Long
     * @author: taokai
     * @date: 2017年8月21日 下午4:34:56
     */
    Long strlen(String key);

    /**
     * 将 key 中储存的数字值增一。 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作。
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。 本操作的值限制在 64 位(bit)有符号数字表示之内。返回值：执行
     * INCR 命令之后 key 的值。
     *
     * @param key
     * @return Long
     * @author: taokai
     * @date: 2017年8月21日 下午4:45:10
     */
    Long incr(String key);

    /**
     * 将 key 中储存的数字值增指定增量。
     *
     * @param key
     * @param addInt
     * @return Long
     * @author: taokai
     * @date: 2017年8月21日 下午4:46:29
     */
    Long incrby(String key, int addInt);

    /**
     * 将 key 所储存的值减去指定的减量值
     *
     * @param key
     * @param subInt
     * @return Long
     * @author: taokai
     * @date: 2017年8月21日 下午4:49:21
     */
    Long decrby(String key, int subInt);

    /**
     * 为 key 中所储存的值加上指定的浮点数增量值。 如果 key 不存在，那么 INCRBYFLOAT 会先将 key 的值设为 0
     * ，再执行加法操作。返回执行命令之后 key 的值。
     *
     * @param key
     * @param doubleValue
     * @return Double
     * @author: taokai
     * @date: 2017年8月21日 下午4:47:43
     */
    Double incrbyfloat(String key, double doubleValue);

    /**
     * 为指定的 key 追加值。 如果 key 已经存在并且是一个字符串， APPEND 命令将 value 追加到 key 原来的值的末尾。 如果
     * key 不存在， APPEND 就简单地将给定 key 设为 value ，就像执行 SET key value 一样。返回追加指定值之后，
     * key 中字符串的长度
     *
     * @param key
     * @param value
     * @return Long
     * @author: taokai
     * @date: 2017年8月21日 下午4:50:04
     */
    Long append(String key, String value);

    /**
     * ============================== LIST TODO ===============================
     */
    /**
     * 移出并获取列表的第一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。等待超时单位是秒。返回List为两个元素，
     * 第一个元素是key，第二个元素是取到的值
     *
     * @param timeout
     * @param key
     * @return List<String>
     * @author: taokai
     * @date: 2017年8月21日 下午4:58:30
     */
    List<String> blpop(int timeout, String key);

    /**
     * 移出并获取列表的最后一个元素
     *
     * @param timeout
     * @param key
     * @return List<String>
     * @author: taokai
     * @date: 2017年8月21日 下午5:04:12
     */
    List<String> brpop(int timeout, String key);

    /**
     * 从列表中弹出一个值，将弹出的元素插入到另外一个列表中并返回它；
     * 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。假如在指定时间内没有任何元素被弹出，则返回一个 null 和等待时长。
     * 反之，返回一个含有两个元素的列表，第一个元素是被弹出元素的值，第二个元素是等待时长。
     *
     * @param source
     * @param destination
     * @param timeout
     * @return String
     * @author: taokai
     * @date: 2017年8月21日 下午5:06:45
     */
    String brpoplpush(String source, String destination, int timeout);

    /**
     * 通过索引获取列表中的元素。你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2
     * 表示列表的倒数第二个元素，以此类推。如果索引超出范围则返回null
     *
     * @param key
     * @param index
     * @return String
     * @author: taokai
     * @date: 2017年8月21日 下午5:10:51
     */
    String lindex(String key, long index);

    /**
     * 在列表的元素前或者后插入元素。 当指定元素不存在于列表中时，不执行任何操作。 当列表不存在时，被视为空列表，不执行任何操作。 如果 key
     * 不是列表类型，返回一个错误。如果命令执行成功，返回插入操作完成之后，列表的长度。 如果没有找到指定元素 ，返回 -1 。 如果 key
     * 不存在或为空列表，返回 0 。
     *
     * @param key
     * @param where
     * @param pivot
     * @param value
     * @return Long
     * @author: taokai
     * @date: 2017年8月21日 下午5:13:10
     */
    Long linsert(String key, BinaryClient.LIST_POSITION where, String pivot, String value);

    /**
     * 返回列表的长度。 如果列表 key 不存在，则 key 被解释为一个空列表，返回 0 。 如果 key 不是列表类型，返回一个错误。
     *
     * @param key
     * @return Long
     * @author: taokai
     * @date: 2017年8月21日 下午5:15:10
     */
    Long llen(String key);

    /**
     * 移除并返回列表的第一个元素。当key不存在是返回null
     *
     * @param key
     * @return String
     * @author: taokai
     * @date: 2017年8月21日 下午5:21:30
     */
    String lpop(String key);

    /**
     * 将一个或多个值插入到列表头部。 如果 key 不存在，一个空列表会被创建并执行 LPUSH 操作。 当 key
     * 存在但不是列表类型时，返回一个错误。返回列表长度
     *
     * @param key
     * @param values
     * @return Long
     * @author: taokai
     * @date: 2017年8月21日 下午5:23:01
     */
    Long lpush(String key, String... values);

    /**
     * 将一个或多个值插入到已存在的列表头部，列表不存在时操作无效。返回列表长度
     *
     * @param key
     * @param values
     * @return Long
     * @author: taokai
     * @date: 2017年8月21日 下午5:23:53
     */
    Long lpushx(String key, String... values);

    /**
     * 返回列表中指定区间内的元素，区间以偏移量 START 和 END 指定。 其中 0 表示列表的第一个元素， 1 表示列表的第二个元素，以此类推。
     * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
     *
     * @param key
     * @param start
     * @param end
     * @return List<String>
     * @author: taokai
     * @date: 2017年8月21日 下午5:25:34
     */
    List<String> lrange(String key, long start, long end);

    /**
     * 根据参数 COUNT 的值，移除列表中与参数 VALUE 相等的元素。 COUNT 的值可以是以下几种： count > 0 :
     * 从表头开始向表尾搜索，移除与 VALUE 相等的元素，数量为 COUNT 。 count < 0 : 从表尾开始向表头搜索，移除与 VALUE
     * 相等的元素，数量为 COUNT 的绝对值。 count = 0 : 移除表中所有与 VALUE 相等的值。返回被移除元素的数量。 列表不存在时返回
     * 0 。
     *
     * @param key
     * @param count
     * @param value
     * @return Long
     * @author: taokai
     * @date: 2017年8月21日 下午5:26:44
     */
    Long lrem(String key, long count, String value);

    /**
     * 通过索引来设置元素的值。 当索引参数超出范围，或对一个空列表进行 LSET 时，返回一个错误。操作成功返回 ok ，否则返回错误信息
     *
     * @param key
     * @param index
     * @param value
     * @return String
     * @author: taokai
     * @date: 2017年8月21日 下午5:28:53
     */
    String lset(String key, long index, String value);

    /**
     * 对一个列表进行修剪(trim)，就是说，让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除。 下标 0 表示列表的第一个元素，以 1
     * 表示列表的第二个元素，以此类推。 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
     * 操作成功返回 ok
     *
     * @param key
     * @param start
     * @param end
     * @return String
     * @author: taokai
     * @date: 2017年8月21日 下午5:29:46
     */
    String ltrim(String key, long start, long end);

    /**
     * 命令用于移除并返回列表的最后一个元素。当列表不存在时，返回 null
     *
     * @param key
     * @return String
     * @author: taokai
     * @date: 2017年8月21日 下午5:30:42
     */
    String rpop(String key);

    /**
     * 移除列表的最后一个元素，并将该元素添加到另一个列表并返回。
     *
     * @param srckey
     * @param dstkey
     * @return String
     * @author: taokai
     * @date: 2017年8月21日 下午5:31:41
     */
    String rpoplpush(String srckey, String dstkey);

    /**
     * 将一个或多个值插入到列表的尾部(最右边)。 如果列表不存在，一个空列表会被创建并执行 RPUSH 操作。
     * 当列表存在但不是列表类型时，返回一个错误。返回列表的长度
     *
     * @param key
     * @param values
     * @return Long
     * @author: taokai
     * @date: 2017年8月21日 下午5:32:27
     */
    Long rpush(String key, String... values);

    /**
     * 将一个或多个值插入到已存在的列表尾部(最右边)。如果列表不存在，操作无效。
     *
     * @param key
     * @param values
     * @return Long
     * @author: taokai
     * @date: 2017年8月21日 下午5:33:08
     */
    Long rpushx(String key, String... values);

    /** ================================ SET TODO =========================== */

    /**
     * 将一个或多个成员元素加入到集合中，已经存在于集合的成员元素将被忽略。 假如集合 key 不存在，则创建一个只包含添加的元素作成员的集合。 当集合
     * key 不是集合类型时，返回一个错误。
     *
     * @param key
     * @param values
     * @return
     * @author: taokai
     */
    Long sadd(String key, String... values);

    /**
     * 返回集合中元素的数量
     *
     * @param key
     * @return Long
     * @author: taoka
     * @date: 2017年11月14日 下午6:57:46
     */
    Long scard(String key);

    /**
     * 返回给定集合之间的差集。不存在的集合 key 将视为空集。
     *
     * @param keys
     * @return Set<String>
     * @author: taoka
     * @date: 2017年11月14日 下午7:00:14
     */
    Set<String> sdiff(String keys);

    /**
     * 将给定集合之间的差集存储在指定的集合中。如果指定的集合 key 已存在，则会被覆盖，返回结果集中的元素数量。
     *
     * @param dstkey
     * @param keys
     * @return Long
     * @author: taoka
     * @date: 2017年11月14日 下午7:02:24
     */
    Long sdiffstore(String dstkey, String... keys);

    /**
     * 返回给定所有给定集合的交集。 不存在的集合 key 被视为空集。 当给定集合当中有一个空集时，结果也为空集(根据集合运算定律)。
     *
     * @param keys
     * @return Set<String>
     * @author: taoka
     * @date: 2017年11月14日 下午7:04:24
     */
    Set<String> sinter(String... keys);

    /**
     * 将给定集合之间的交集存储在指定的集合中。如果指定的集合已经存在，则将其覆盖，返回结果集中的元素数量。
     *
     * @param dstkey
     * @param keys
     * @return Long
     * @author: taoka
     * @date: 2017年11月14日 下午7:06:35
     */
    Long sinterstore(String dstkey, String... keys);

    /**
     * 判断成员元素是否是集合的成员。
     *
     * @param key
     * @param member
     * @return Boolean
     * @author: taoka
     * @date: 2017年11月14日 下午7:08:53
     */
    Boolean sismember(String key, String member);

    /**
     * 返回集合中的所有的成员。 不存在的集合 key 被视为空集合。
     *
     * @param key
     * @return Set<String>
     * @author: taoka
     * @date: 2017年11月14日 下午7:11:36
     */
    Set<String> smembers(String key);

    /**
     * 将指定成员 member 元素从 srckey 集合移动到 dstkey 集合。SMOVE 是原子性操作。 如果 source
     * 集合不存在或不包含指定的 member 元素，则 SMOVE 命令不执行任何操作，仅返回 0 。 否则， member 元素从 source
     * 集合中被移除，并添加到 destination 集合中去。 当 destination 集合已经包含 member 元素时， SMOVE
     * 命令只是简单地将 source 集合中的 member 元素删除。 当 source 或 destination 不是集合类型时，返回一个错误。
     * 如果成员元素被成功移除，返回 1 。 如果成员元素不是 source 集合的成员，并且没有任何操作对 destination 集合执行，那么返回
     * 0 。
     *
     * @param srckey
     * @param dstkey
     * @param member
     * @return Long
     * @author: taoka
     * @date: 2017年11月14日 下午7:20:46
     */
    Long smove(String srckey, String dstkey, String member);

    /**
     * 移除并返回集合中的一个随机元素
     *
     * @param key
     * @return String
     * @author: taoka
     * @date: 2017年11月14日 下午7:32:49
     */
    String spop(String key);

    /**
     * 返回集合中的一个随机元素，该操作和 SPOP 相似，但 SPOP 将随机元素从集合中移除并返回 而 Srandmember
     * 则仅仅返回随机元素，而不对集合进行任何改动。
     *
     * @param key
     * @return String
     * @author: taoka
     * @date: 2017年11月14日 下午7:42:34
     */
    String srandmember(String key);

    /**
     * 如果 count 为正数，且小于集合基数，那么命令返回一个包含 count 个元素的数组，数组中的元素各不相同。 如果 count
     * 大于等于集合基数，那么返回整个集合。 如果 count 为负数，那么命令返回一个数组，数组中的元素可能会重复出现多次，而数组的长度为 count
     * 的绝对值。
     *
     * @param key
     * @param count
     * @return List<String>
     * @author: taoka
     * @date: 2017年11月14日 下午7:41:42
     */
    List<String> srandmember(String key, int count);

    /**
     * 移除集合中的一个或多个成员元素，不存在的成员元素会被忽略。当 key 不是集合类型，返回一个错误。返回被成功移除的元素的数量，不包括被忽略的元素。
     *
     * @param key
     * @param members
     * @return Long
     * @author: taoka
     * @date: 2017年11月14日 下午7:50:56
     */
    Long srem(String key, String... members);

    /**
     * 返回给定集合的并集。不存在的集合 key 被视为空集。
     *
     * @param keys
     * @return Set<String>
     * @author: taoka
     * @date: 2017年11月14日 下午7:53:10
     */
    Set<String> sunion(String... keys);

    /**
     * 将给定集合的并集存储在指定的集合 destination 中。如果 destination 已经存在，则将其覆盖。返回结果集中的元素数量。
     *
     * @param dstkey
     * @param keys
     * @return Long
     * @author: taoka
     * @date: 2017年11月14日 下午7:54:27
     */
    Long sunionstore(String dstkey, String... keys);

    /**
     * 迭代集合键中的元素。返回值数组列表。
     *
     * @param key
     * @param cursor
     * @return ScanResult<String>
     * @author: taoka
     * @date: 2017年11月14日 下午8:06:40
     */
    ScanResult<String> sscan(String key, String cursor);

    /**
     * 迭代集合键中的元素。返回值数组列表。
     *
     * @param key
     * @param cursor
     * @param params
     * @return ScanResult<String>
     * @author: taoka
     * @date: 2017年11月14日 下午8:08:34
     */
    ScanResult<String> sscan(String key, String cursor, ScanParams params);

    /**
     * ================================ SORTED SET TODO
     * ===========================
     */
    /**
     * 将一个成员元素及其分数值加入到有序集当中。
     * 如果某个成员已经是有序集的成员，那么更新这个成员的分数值，并通过重新插入这个成员元素，来保证该成员在正确的位置上。
     * 分数值可以是整数值或双精度浮点数。 如果有序集合 key 不存在，则创建一个空的有序集并执行 ZADD 操作。
     * 返回值：被成功添加的新成员的数量，不包括那些被更新的、已经存在的成员。
     *
     * @param key
     * @param score
     * @param member
     * @return Long
     * @author: taoka
     * @date: 2017年11月15日 上午9:44:04
     */
    Long zadd(String key, double score, String member);

    /**
     * 将一个成员元素及其分数值加入到有序集当中。 如果有序集合 key 不存在，则创建一个空的有序集并执行 ZADD 操作。 如果有序集合 key
     * 存在，则会判断 member 是否在这个集合中，如果在，则不进行操作。不在，则执行 ZADD 操作。
     * 返回值：被成功添加的新成员的数量，不包括那些被更新的、已经存在的成员。
     *
     * @param key
     * @param score
     * @param member
     * @return Long
     * @author: taoka
     * @date: 2017年11月15日 上午11:16:21
     */
    Long zaddNx(String key, double score, String member);

    /**
     * 将一个成员元素及其分数值加入到有序集当中。 如果有序集合 key 不存在，则不进行任何操作，不会创建任何集合。 如果有序集合 key
     * 存在，则会判断 member 是否在这个集合中，如果在，则不进行操作。不在，则执行 ZADD 操作。
     * 返回值：被成功添加的新成员的数量，不包括那些被更新的、已经存在的成员。
     *
     * @param key
     * @param score
     * @param member
     * @return Long
     * @author: taoka
     * @date: 2017年11月15日 上午11:18:19
     */
    Long zaddXx(String key, double score, String member);

    /**
     * 将一个或多个成员元素及其分数值加入到有序集当中。
     * 如果某个成员已经是有序集的成员，那么更新这个成员的分数值，并通过重新插入这个成员元素，来保证该成员在正确的位置上。
     * 分数值可以是整数值或双精度浮点数。 如果有序集合 key 不存在，则创建一个空的有序集并执行 ZADD 操作。
     * 返回值：被成功添加的新成员的数量，不包括那些被更新的、已经存在的成员。
     *
     * @param key
     * @param scoreMembers
     * @return Long
     * @author: taoka
     * @date: 2017年11月15日 上午9:41:35
     */
    Long zadd(String key, Map<String, Double> scoreMembers);

    /**
     * 将一个或多个成员元素及其分数值加入到有序集当中。 如果有序集合 key 不存在，则创建一个空的有序集并执行 ZADD 操作。 如果有序集合 key
     * 存在，则会判断 member 是否在这个集合中，如果在，则不进行操作。不在，则执行 ZADD 操作。
     * 返回值：被成功添加的新成员的数量，不包括那些被更新的、已经存在的成员。
     *
     * @param key
     * @param scoreMembers
     * @return Long
     * @author: taoka
     * @date: 2017年11月15日 上午11:16:21
     */
    Long zaddNx(String key, Map<String, Double> scoreMembers);

    /**
     * 将一个或多个成员元素及其分数值加入到有序集当中。 如果有序集合 key 不存在，则不进行任何操作，不会创建任何集合。 如果有序集合 key
     * 存在，则会判断 member 是否在这个集合中，如果在，则不进行操作。不在，则执行 ZADD 操作。
     * 返回值：被成功添加的新成员的数量，不包括那些被更新的、已经存在的成员。
     *
     * @param key
     * @param scoreMembers
     * @return Long
     * @author: taoka
     * @date: 2017年11月15日 上午11:18:19
     */
    Long zaddXx(String key, Map<String, Double> scoreMembers);

    /**
     * 计算集合中元素的数量。当 key 存在且是有序集类型时，返回有序集的基数。 当 key 不存在时，返回 0 。
     *
     * @param key
     * @return Long
     * @author: taoka
     * @date: 2017年11月15日 下午2:29:42
     */
    Long zcard(String key);

    /**
     * 计算有序集合中指定分数区间的成员数量。
     *
     * @param key
     * @param min
     * @param max
     * @return Long
     * @author: taoka
     * @date: 2017年11月15日 下午2:32:03
     */
    Long zcount(String key, String min, String max);

    /**
     * 计算有序集合中指定分数区间的成员数量。
     *
     * @param key
     * @param min
     * @param max
     * @return Long
     * @author: taoka
     * @date: 2017年11月15日 下午2:32:07
     */
    Long zcount(String key, double min, double max);

    /**
     * 对有序集合中指定成员的分数加上增量 increment 可以通过传递一个负数值 increment ，让分数减去相应的值，比如 ZINCRBY
     * key -5 member ，就是让 member 的 score 值减去 5 。 当 key 不存在，或分数不是 key 的成员时，
     * ZINCRBY key increment member 等同于 ZADD key increment member 。 返回 member
     * 成员的新分数值
     *
     * @param key
     * @param score
     * @param member
     * @return Double
     * @author: taoka
     * @date: 2017年11月15日 下午2:36:18
     */
    Double zincrby(String key, double score, String member);

    /**
     * 计算给定的一个或多个有序集的交集 setKeys ，并将该交集(结果集)储存到 dstkey 。 如果 setKeys
     * 中有一个有序集为空(不存在视为空)时， 返回操作失败0。 默认情况下，结果集 dstkey 中某个成员的分数值是所有给定集 setKeys
     * 下该成员分数值之和 Aggregate.SUM。 返回值:保存到目标结果集 dstkey 的成员数量 。
     *
     * @param dstkey
     * @param setKeys
     * @return Long
     * @author: taoka
     * @date: 2017年11月15日 下午3:10:27
     */
    Long zinterstore(String dstkey, String... setKeys);

    /**
     * 计算给定的一个或多个有序集的交集 setKeys ，并将该交集(结果集)储存到 dstkey 。 如果 setKeys
     * 中有一个有序集为空(不存在视为空)时， 返回操作失败0。 结果集 dstkey 中某个成员的分数值是所有给定集 setKeys 下该成员分数值 的
     * Aggregate(SUM,MAX,MIN)。 返回值:保存到目标结果集 dstkey 的成员数量。
     *
     * @param dstkey
     * @param aggregate
     * @param setKeys
     * @return Long
     * @author: taoka
     * @date: 2017年11月15日 下午3:26:26
     */
    Long zinterstore(String dstkey, ZParams.Aggregate aggregate, String... setKeys);

    /**
     * 计算给定的一个或多个有序集的交集 setKeys ，并将该交集(结果集)储存到 dstkey 。 如果 setKeys
     * 中有一个有序集为空(不存在视为空)时， 返回操作失败0。 结果集 dstkey 中某个成员的分数值是所有给定集 setKeys 下该成员分数值 有
     * zParams 配置决定。 返回值:保存到目标结果集 dstkey 的成员数量。
     *
     * @param dstkey
     * @param zParams
     * @param setKeys
     * @return Long
     * @author: taoka
     * @date: 2017年11月15日 下午3:29:31
     */
    Long zinterstore(String dstkey, ZParams zParams, String... setKeys);

    /**
     * 计算有序集合中指定字典区间内成员数量。指定区间内的成员数量。
     *
     * @param key
     * @param minMember
     * @param maxMember
     * @return Long
     * @author: taoka
     * @date: 2017年11月15日 下午3:32:10
     */
    Long zlexcount(String key, String minMember, String maxMember);

    /**
     * 通过索引区间返回有序集合成指定区间内的成员,其中成员的位置按分数值递增(从小到大)来排序。具有相同分数值的成员按字典序(
     * lexicographical order )来排列。 以 0 表示有序集第一个成员，以 1 表示有序集第二个成员，以 -1 表示最后一个成员，
     * -2 表示倒数第二个成员，以此类推
     *
     * @param key
     * @param startIndex
     * @param endIndex
     * @return Set<String>
     * @author: taoka
     * @date: 2017年11月15日 下午3:39:06
     */
    Set<String> zrange(String key, long startIndex, long endIndex);

    /**
     * 通过字典区间返回有序集合的成员
     *
     * @param key
     * @param memberMin
     * @param memberMax
     * @return Set<String>
     * @author: taoka
     * @date: 2017年11月15日 下午3:50:50
     */
    Set<String> zrangeByLex(String key, String memberMin, String memberMax);

    /**
     * 通过分数返回有序集合指定区间内的成员 有序集成员按分数值递增(从小到大)次序排列。 具有相同分数值的成员按字典序来排列
     * (该属性是有序集提供的，不需要额外的计算)。 默认情况下，区间的取值使用闭区间 (小于等于或大于等于)。返回区间成员
     *
     * @param key
     * @param scoreMin
     * @param scoreMax
     * @return Set<String>
     * @author: taoka
     * @date: 2017年11月15日 下午3:57:51
     */
    Set<String> zrangeByScore(String key, double scoreMin, double scoreMax);

    /**
     * 通过分数返回有序集合指定区间内的成员 有序集成员按分数值递增(从小到大)次序排列。 具有相同分数值的成员按字典序来排列
     * (该属性是有序集提供的，不需要额外的计算)。 区间的取值 >= offset， <= end。返回区间成员
     *
     * @param key
     * @param scoreMin
     * @param scoreMax
     * @param offset
     * @param end
     * @return Set<String>
     * @author: taoka
     * @date: 2017年11月15日 下午4:02:05
     */
    Set<String> zrangeByScore(String key, double scoreMin, double scoreMax, int offset, int end);

    /**
     * 通过分数返回有序集合指定区间内的成员 有序集成员按分数值递增(从小到大)次序排列。 具有相同分数值的成员按字典序来排列
     * (该属性是有序集提供的，不需要额外的计算)。 默认情况下，区间的取值使用闭区间 (小于等于或大于等于)。返回区间成员
     *
     * @param key
     * @param scoreMin
     * @param scoreMax
     * @return Set<String>
     * @author: taoka
     * @date: 2017年11月15日 下午3:57:51
     */
    Set<String> zrangeByScore(String key, String scoreMin, String scoreMax);

    /**
     * 通过分数返回有序集合指定区间内的成员 有序集成员按分数值递增(从小到大)次序排列。 具有相同分数值的成员按字典序来排列
     * (该属性是有序集提供的，不需要额外的计算)。 区间的取值 >= offset， <= end。返回区间成员
     *
     * @param key
     * @param scoreMin
     * @param scoreMax
     * @param offset
     * @param end
     * @return Set<String>
     * @author: taoka
     * @date: 2017年11月15日 下午4:02:05
     */
    Set<String> zrangeByScore(String key, String scoreMin, String scoreMax, int offset, int end);

    /**
     * 通过分数返回有序集合指定区间内的成员 有序集成员按分数值递增(从小到大)次序排列。 具有相同分数值的成员按字典序来排列
     * (该属性是有序集提供的，不需要额外的计算)。 默认情况下，区间的取值使用闭区间 (小于等于或大于等于)。返回区间成员和分数
     *
     * @param key
     * @param scoreMin
     * @param scoreMax
     * @return Set<Tuple>
     * @author: taoka
     * @date: 2017年11月15日 下午3:57:51
     */
    Set<Tuple> zrangeByScoreWithScores(String key, double scoreMin, double scoreMax);

    /**
     * 通过分数返回有序集合指定区间内的成员 有序集成员按分数值递增(从小到大)次序排列。 具有相同分数值的成员按字典序来排列
     * (该属性是有序集提供的，不需要额外的计算)。 区间的取值 >= offset， <= end。返回区间成员和分数
     *
     * @param key
     * @param scoreMin
     * @param scoreMax
     * @param offset
     * @param end
     * @return Set<Tuple>
     * @author: taoka
     * @date: 2017年11月15日 下午4:02:05
     */
    Set<Tuple> zrangeByScoreWithScores(String key, double scoreMin, double scoreMax, int offset, int end);

    /**
     * 通过分数返回有序集合指定区间内的成员 有序集成员按分数值递增(从小到大)次序排列。 具有相同分数值的成员按字典序来排列
     * (该属性是有序集提供的，不需要额外的计算)。 默认情况下，区间的取值使用闭区间 (小于等于或大于等于)。返回区间成员和分数
     *
     * @param key
     * @param scoreMin
     * @param scoreMax
     * @return Set<Tuple>
     * @author: taoka
     * @date: 2017年11月15日 下午3:57:51
     */
    Set<Tuple> zrangeByScoreWithScores(String key, String scoreMin, String scoreMax);

    /**
     * 通过分数返回有序集合指定区间内的成员 有序集成员按分数值递增(从小到大)次序排列。 具有相同分数值的成员按字典序来排列
     * (该属性是有序集提供的，不需要额外的计算)。 区间的取值 >= offset， <= end。返回区间成员和分数
     *
     * @param key
     * @param scoreMin
     * @param scoreMax
     * @param offset
     * @param end
     * @return Set<Tuple>
     * @author: taoka
     * @date: 2017年11月15日 下午4:02:05
     */
    Set<Tuple> zrangeByScoreWithScores(String key, String scoreMin, String scoreMax, int offset, int end);

    /**
     * 返回有序集合中指定成员的索引。 有序队列排序 分数从小到大排序
     *
     * @param key
     * @param member
     * @return Long
     * @author: taoka
     * @date: 2017年11月15日 下午4:17:05
     */
    Long zrank(String key, String member);

    /**
     * 于移除有序集中的一个或多个成员，不存在的成员将被忽略。 返回值:被成功移除的成员的数量，不包括被忽略的成员。
     *
     * @param key
     * @param members
     * @return Long
     * @author: taoka
     * @date: 2017年11月15日 下午4:21:26
     */
    Long zrem(String key, String... members);

    /**
     * 移除有序集合中给定的字典区间的所有成员 返回值:被成功移除的成员的数量，不包括被忽略的成员。
     *
     * @param key
     * @param memberMin
     * @param memberMax
     * @return Long
     * @author: taoka
     * @date: 2017年11月15日 下午4:23:42
     */
    Long zremrangeByLex(String key, String memberMin, String memberMax);

    /**
     * 移除有序集合中给定的排名区间的所有成员，有序集合排名分数从小到大 返回值:被移除成员的数量。
     *
     * @param key
     * @param startIndex
     * @param endIndex
     * @return Long
     * @author: taoka
     * @date: 2017年11月15日 下午4:27:23
     */
    Long zremrangeByRank(String key, long startIndex, long endIndex);

    /**
     * 移除有序集合中给定的分数区间的所有成员 。 返回值:被移除成员的数量。
     *
     * @param key
     * @param scoreMin
     * @param scoreMax
     * @return Long
     * @author: taoka
     * @date: 2017年11月15日 下午4:30:47
     */
    Long zremrangeByScore(String key, double scoreMin, double scoreMax);

    /**
     * 返回有序集中指定索引区间内的成员，分数从高到底 ， 具有相同分数值的成员按字典序的逆序(reverse lexicographical
     * order)排列。
     *
     * @param key
     * @param startIndex
     * @param endIndex
     * @return Set<String>
     * @author: taoka
     * @date: 2017年11月15日 下午4:49:23
     */
    Set<String> zrevrange(String key, long startIndex, long endIndex);

    /**
     * 返回有序集中指定分数区间内的成员，分数从高到低排序，具有相同分数值的成员按字典序的逆序(reverse lexicographical order
     * )排列。 返回区间内的成员
     *
     * @param key
     * @param scoreMax
     * @param scoreMin
     * @return Set<String>
     * @author: taoka
     * @date: 2017年11月15日 下午4:51:54
     */
    Set<String> zrevrangeByScore(String key, double scoreMax, double scoreMin);

    /**
     * 返回有序集中指定分数区间内的成员，分数从高到低排序，具有相同分数值的成员按字典序的逆序(reverse lexicographical order
     * )排列。 区间 >= offset , <= end。 返回区间内的成员
     *
     * @param key
     * @param scoreMax
     * @param scoreMin
     * @param offset
     * @param end
     * @return Set<String>
     * @author: taoka
     * @date: 2017年11月15日 下午4:59:53
     */
    Set<String> zrevrangeByScore(String key, double scoreMax, double scoreMin, int offset, int end);

    /**
     * 返回有序集中指定分数区间内的成员，分数从高到低排序，具有相同分数值的成员按字典序的逆序(reverse lexicographical order
     * )排列。 返回区间内的成员
     *
     * @param key
     * @param scoreMax
     * @param scoreMin
     * @return Set<String>
     * @author: taoka
     * @date: 2017年11月15日 下午4:51:54
     */
    Set<String> zrevrangeByScore(String key, String scoreMax, String scoreMin);

    /**
     * 返回有序集中指定分数区间内的成员，分数从高到低排序，具有相同分数值的成员按字典序的逆序(reverse lexicographical order
     * )排列。 区间 >= offset , <= end。 返回区间内的成员
     *
     * @param key
     * @param scoreMax
     * @param scoreMin
     * @param offset
     * @param end
     * @return Set<String>
     * @author: taoka
     * @date: 2017年11月15日 下午4:59:53
     */
    Set<String> zrevrangeByScore(String key, String scoreMax, String scoreMin, int offset, int end);

    /**
     * 返回有序集中指定分数区间内的成员，分数从高到低排序，具有相同分数值的成员按字典序的逆序(reverse lexicographical order
     * )排列。 返回区间内的成员和分数
     *
     * @param key
     * @param scoreMax
     * @param scoreMin
     * @return Set<Tuple>
     * @author: taoka
     * @date: 2017年11月15日 下午4:51:54
     */
    Set<Tuple> zrevrangeByScoreWithScores(String key, double scoreMax, double scoreMin);

    /**
     * 返回有序集中指定分数区间内的成员，分数从高到低排序，具有相同分数值的成员按字典序的逆序(reverse lexicographical order
     * )排列。 区间 >= offset , <= end。 返回区间内的成员和分数
     *
     * @param key
     * @param scoreMax
     * @param scoreMin
     * @param offset
     * @param end
     * @return Set<Tuple>
     * @author: taoka
     * @date: 2017年11月15日 下午4:59:53
     */
    Set<Tuple> zrevrangeByScoreWithScores(String key, double scoreMax, double scoreMin, int offset, int end);

    /**
     * 返回有序集中指定分数区间内的成员，分数从高到低排序，具有相同分数值的成员按字典序的逆序(reverse lexicographical order
     * )排列。 返回区间内的成员和分数
     *
     * @param key
     * @param scoreMax
     * @param scoreMin
     * @return Set<Tuple>
     * @author: taoka
     * @date: 2017年11月15日 下午4:51:54
     */
    Set<Tuple> zrevrangeByScoreWithScores(String key, String scoreMax, String scoreMin);

    /**
     * 返回有序集中指定分数区间内的成员，分数从高到低排序，具有相同分数值的成员按字典序的逆序(reverse lexicographical order
     * )排列。 区间 >= offset , <= end。 返回区间内的成员和分数
     *
     * @param key
     * @param scoreMax
     * @param scoreMin
     * @param offset
     * @param end
     * @return Set<Tuple>
     * @author: taoka
     * @date: 2017年11月15日 下午4:59:53
     */
    Set<Tuple> zrevrangeByScoreWithScores(String key, String scoreMax, String scoreMin, int offset, int end);

    /**
     * 返回有序集合中指定成员的排名，有序集成员按分数值递减(从大到小)排序
     *
     * @param key
     * @param member
     * @return Long
     * @author: taoka
     * @date: 2017年11月15日 下午5:21:30
     */
    Long zrevrank(String key, String member);

    /**
     * 返回有序集中，成员的分数值。 如果成员元素不是有序集 key 的成员，或 key 不存在，返回 null
     *
     * @param key
     * @param member
     * @return Double
     * @author: taoka
     * @date: 2017年11月15日 下午5:23:49
     */
    Double zscore(String key, String member);

    /**
     * 计算给定的一个或多个有序集的交集 setKeys ，并将该并集(结果集)储存到 dstkey 。 如果 setKeys
     * 中有一个有序集不存在，视为空。 默认情况下，结果集 dstkey 中某个成员的分数值是所有给定集 setKeys 下该成员分数值之和
     * Aggregate.SUM。 返回值:保存到目标结果集 dstkey 的成员数量 。
     *
     * @param dstkey
     * @param setKeys
     * @return Long
     * @author: taoka
     * @date: 2017年11月15日 下午5:26:14
     */
    Long zunionstore(String dstkey, String... setKeys);

    /**
     * 计算给定的一个或多个有序集的交集 setKeys ，并将该并集(结果集)储存到 dstkey 。 如果 setKeys
     * 中有一个有序集不存在，视为空。 结果集 dstkey 中某个成员的分数值是所有给定集 setKeys 下该成员分数值 的
     * Aggregate(SUM,MAX,MIN)。 返回值:保存到目标结果集 dstkey 的成员数量。
     *
     * @param dstkey
     * @param aggregate
     * @param setKeys
     * @return Long
     * @author: taoka
     * @date: 2017年11月15日 下午5:29:26
     */
    Long zunionstore(String dstkey, ZParams.Aggregate aggregate, String... setKeys);

    /**
     * 计算给定的一个或多个有序集的交集 setKeys ，并将该并集(结果集)储存到 dstkey 。 如果 setKeys
     * 中有一个有序集不存在，视为空。 结果集 dstkey 中某个成员的分数值是所有给定集 setKeys 下该成员分数值 有 zParams
     * 配置决定。 返回值:保存到目标结果集 dstkey 的成员数量。
     *
     * @param dstkey
     * @param zParams
     * @param setKeys
     * @return Long
     * @author: taoka
     * @date: 2017年11月15日 下午5:29:31
     */
    Long zunionstore(String dstkey, ZParams zParams, String... setKeys);

    /**
     * 迭代有序集合中的元素（包括元素成员和元素分值）
     *
     * @param key
     * @param cursor
     * @return ScanResult<Tuple>
     * @author: taoka
     * @date: 2017年11月15日 下午7:54:27
     */
    ScanResult<Tuple> zscan(String key, String cursor);

    /**
     * ================================ HASH TODO ===========================
     */
    /**
     * 删除哈希表 key 中的一个或多个指定字段，不存在的字段将被忽略。返回被成功删除字段的数量，不包括被忽略的字段。
     *
     * @param key
     * @param fields
     * @return Long
     * @author: taokai
     * @date: 2017年8月30日 下午5:02:12
     */
    Long hdel(String key, String... fields);

    /**
     * 查看哈希表的指定字段是否存在。
     *
     * @param key
     * @param field
     * @return Boolean
     * @author: taoka
     * @date: 2017年12月12日 下午3:22:02
     */
    Boolean hexists(String key, String field);

    /**
     * 获取哈希表中指定字段的值。
     *
     * @param key
     * @param field
     * @return String
     * @author: taoka
     * @date: 2017年12月12日 下午3:24:43
     */
    String hget(String key, String field);

    /**
     * 获取哈希表中，所有的字段和值。
     *
     * @param key
     * @return Map<String                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               ,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               String>
     * @author: taoka
     * @date: 2017年12月12日 下午3:26:04
     */
    Map<String, String> hgetAll(String key);

    /**
     * 为哈希表中的字段值加上指定增量值。增量也可以为负数，相当于对指定字段进行减法操作。 如果哈希表的 key 不存在，一个新的哈希表被创建并执行
     * HINCRBY 命令。 如果指定的字段不存在，那么在执行命令前，字段的值被初始化为 0 。
     *
     * @param key
     * @param field
     * @param value
     * @return Long
     * @author: taoka
     * @date: 2017年12月12日 下午3:37:49
     */
    Long hincrby(String key, String field, long value);

    /**
     * 为哈希表中的字段值加上指定浮点数增量值。如果指定的字段不存在，那么在执行命令前，字段的值被初始化为 0
     *
     * @param key
     * @param field
     * @param value
     * @return Double
     * @author: taoka
     * @date: 2017年12月12日 下午3:55:39
     */
    Double hincrByFloat(String key, String field, double value);

    /**
     * 获取哈希表中的所有域（field）
     *
     * @param key
     * @return Set<String>
     * @author: taoka
     * @date: 2017年12月12日 下午4:03:11
     */
    Set<String> hkeys(String key);

    /**
     * 获取哈希表中字段的数量。
     *
     * @param key
     * @return Long
     * @author: taoka
     * @date: 2017年12月12日 下午4:04:22
     */
    Long hlen(String key);

    /**
     * 获取哈希表中，一个或多个给定字段的值。 如果指定的字段不存在于哈希表，那么返回一个 nil 值。
     *
     * @param key
     * @param fields
     * @return List<String>
     * @author: taoka
     * @date: 2017年12月12日 下午4:09:13
     */
    List<String> hmget(String key, String... fields);

    /**
     * 将多个 field-value (字段-值)对设置到哈希表中。 此命令会覆盖哈希表中已存在的字段。 如果哈希表不存在，会创建一个空哈希表，并执行
     * HMSET 操作。
     *
     * @param key
     * @param hash
     * @return String
     * @author: taoka
     * @date: 2017年12月12日 下午4:15:02
     */
    String hmset(String key, Map<String, String> hash);

    /**
     * 为哈希表中的字段赋值 。如果哈希表不存在，一个新的哈希表被创建并进行 HSET
     * 操作。如果字段已经存在于哈希表中，旧值将被覆盖。如果字段是哈希表中的一个新建字段，并且值设置成功，返回 1 。
     * 如果哈希表中域字段已经存在且旧值已被新值覆盖，返回 0 。
     *
     * @param key
     * @param field
     * @param value
     * @return Long
     * @author: taoka
     * @date: 2017年12月12日 下午4:21:56
     */
    Long hset(String key, String field, String value);

    /**
     * 为哈希表中不存在的的字段赋值 。如果哈希表不存在，一个新的哈希表被创建并进行 HSET 操作。如果字段已经存在于哈希表中，操作无效。如果 key
     * 不存在，一个新哈希表被创建并执行 HSETNX 命令。
     *
     * @param key
     * @param field
     * @param value
     * @return Long
     * @author: taoka
     * @date: 2017年12月12日 下午4:23:38
     */
    Long hsetnx(String key, String field, String value);

    /**
     * 获取哈希表所有域(field)的值。
     *
     * @param key
     * @return List<String>
     * @author: taoka
     * @date: 2017年12月12日 下午4:31:37
     */
    List<String> hvals(String key);

    /**
     * ============================ HyperLogLog TODO =======================
     * Redis 在 2.8.9 版本添加了 HyperLogLog 结构。 Redis HyperLogLog 是用来做基数统计的算法。 比如数据集
     * {1, 3, 5, 7, 5, 7, 8}， 那么这个数据集的基数集为 {1, 3, 5 ,7, 8}, 基数(不重复元素)为5。
     * 基数估计就是在误差可接受的范围内，快速计算基数。
     */

    /**
     * 将所有元素参数添加到 HyperLogLog 数据结构中。整型，如果至少有个元素被添加返回 1， 否则返回 0。
     *
     * @param key
     * @param elements
     * @return Long
     * @author: taoka
     * @date: 2017年12月12日 下午4:35:35
     */
    Long pfadd(String key, String... elements);

    /**
     * 获取给定 HyperLogLog 的基数估算值。如果多个 HyperLogLog 则返回基数估值之和。
     *
     * @param keys
     * @return long
     * @author: taoka
     * @date: 2017年12月12日 下午4:38:03
     */
    long pfadd(String... keys);

    /**
     * 将多个 HyperLogLog 合并为一个 HyperLogLog ，合并后的 HyperLogLog 的基数估算值是通过对所有 给定
     * HyperLogLog 进行并集计算得出的。
     *
     * @param destkey
     * @param sourcekeys
     * @return String
     * @author: taoka
     * @date: 2017年12月12日 下午4:47:39
     */
    String pfmerge(String destkey, String... sourcekeys);

    Object eval(String script, int keyCount, String... params);

    Object eval(String script, List<String> keys, List<String> args);

    /**
     * TODO
     *
     * @param key
     * @param value
     * @param nxxx
     * @param expx
     * @param ms
     * @return String
     * @author: taoka
     * @date: 2018年4月2日 下午3:21:28
     */
    String set(String key, String value, String nxxx, String expx, int ms);
}
