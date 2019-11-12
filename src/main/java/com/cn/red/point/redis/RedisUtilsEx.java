package com.cn.red.point.redis;

import com.cn.red.point.common.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Set;

/**
 * redis_ip:127.0.0.1
	redis_port:6379
	redis_pwd:123456
 * @author Administrator
 *
 */
public class RedisUtilsEx {

	protected static final Logger logger =  LoggerFactory.getLogger(RedisUtilsEx.class);

//	private static String redis_ip = "192.168.3.35";
//    private static String redis_pwd = "";
    private static String redis_ip = "127.0.0.1";
    private static String redis_pwd = "";
	private static Integer redis_port = 6379;

    // 可用连接实例的最大数目，默认值为8；
    // 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
    private static int MAX_ACTIVE = 1024;

    // 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
    private static int MAX_IDLE = 200;

    // 等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
    private static int MAX_WAIT = 10000;

    private static int TIMEOUT = 10000;

    // 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
    private static boolean TEST_ON_BORROW = true;

    private static JedisPool jedisPool = null;

    /**
     * 初始化Redis连接池
     */
    static {
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(MAX_ACTIVE);
            config.setMaxIdle(MAX_IDLE);
            config.setMaxWaitMillis(MAX_WAIT);
            config.setTestOnBorrow(TEST_ON_BORROW);

            if(StringUtils.isNotEmpty(redis_pwd)) {
                jedisPool = new JedisPool(config, redis_ip, redis_port, TIMEOUT, redis_pwd);
            }else {
                jedisPool = new JedisPool(config, redis_ip, redis_port, TIMEOUT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
      * 获取Jedis实例
     *
     * @return
     */
    protected synchronized static Jedis getJedis() {
        try {
            if (jedisPool != null) {
                Jedis resource = jedisPool.getResource();
                return resource;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
         * 释放jedis资源
     *
     * @param jedis
     */
    protected static void returnResource(Jedis jedis) {
        if (jedis != null) {
        	jedis.close();
        }
    }

    public static Jedis getRedis(){
        return getJedis();
    }

    public static void set(String key, String value,Integer seconds){
    	logger.debug("redis 存入 key"+key);
		Jedis jedis = getJedis();
        assert jedis != null;
        jedis.set(key,value);
		if(seconds != null) {
			jedis.expire(key, seconds);
		}
		returnResource(jedis);
	}

    public static void set(String key, String value){
    	set(key, value, null);
    }

    public static String get(String key){
    	Jedis jedis = getJedis();
        assert jedis != null;
        String string = jedis.get(key);
        returnResource(jedis);
        return string;
	}

	public static Set<String> keys(String key) {
        Jedis jedis = getJedis();
        assert jedis != null;
        Set<String> val = jedis.keys(key);
        returnResource(jedis);
        return val;
    }

    public static void del(String key) {
    	logger.debug("redis 移除 key"+key);
    	Jedis jedis = getJedis();
        assert jedis != null;
        jedis.del(key);
		returnResource(jedis);
	}

	public static long getTTL(String key) {
        Jedis jedis = getJedis();
        assert jedis != null;
        long TTL = jedis.ttl(key);
        returnResource(jedis);
        return TTL;
    }
}

