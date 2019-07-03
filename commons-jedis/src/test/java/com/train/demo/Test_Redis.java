package com.train.demo;

import com.train.commons.jedis.SingleJedisClient;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author: taoka
 * @date: 2018/6/8 15:02
 * @description:
 */
public class Test_Redis {

    SingleJedisClient jedisClient;

    @Before
    public void initPool() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(100);
        jedisPoolConfig.setMaxTotal(600);
        jedisPoolConfig.setMaxWaitMillis(10000);
        jedisPoolConfig.setTestOnBorrow(true);

        JedisPool jedisPool = new JedisPool(jedisPoolConfig, "118.190.15.78", 6379, 3000, "hcpkuyou123", 14);
        jedisClient = new SingleJedisClient();
        jedisClient.setJedisPool(jedisPool);
    }


    @Test
    public void set() {

        String ipStr = "121.42.148.121:16819:1909353107:6n67enlu\n" +
                "120.76.121.150:16819:1909353107:6n67enlu\n" +
                "42.51.205.96:16819:1909353107:6n67enlu\n" +
                "115.28.146.28:16819:1909353107:6n67enlu\n" +
                "211.149.189.148:16819:1909353107:6n67enlu\n" +
                "110.76.185.162:16818:1909353107:6n67enlu\n" +
                "120.76.130.77:16818:1909353107:6n67enlu\n" +
                "122.114.247.216:16818:1909353107:6n67enlu";

        String[] split = ipStr.split("\n");
//        String[] arr = new String[split.length];
//        for(int i = 0; i < split.length; i++){
//            arr[i] = split[i]+":qincai6255013:111h07gm";
//        }
//        System.out.println(arr[1]);


//        jedisClient.del("PROXY_IP_LIST");
        jedisClient.lpush("PROXY_IP_LIST", split);

    }

    @Test
    public void del() {
        Long test = jedisClient.del("test");
        System.out.println("del result:" + test);
    }

    @Test
    public void lock() {
        String result = jedisClient.set("test", "123456", "NX", "EX", 20);
        System.out.println("lock result:" + result);
        try {
            Thread.sleep(1000 * 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Object eval = jedisClient.eval("if redis.call(\"get\",KEYS[1]) == ARGV[1] then return redis.call(\"del\",KEYS[1]) else return 0 end", 1, "test", "123456");
        System.out.println("release result:" + eval);
    }

}
