package com.hansong.session.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by xhans on 2016/4/28.
 */
public class RedisManager {

    static Logger logger = LoggerFactory.getLogger(RedisManager.class);

    private JedisPool jedisPool;

    private static class RedisManagerHolder {
        private static final RedisManager instance = new RedisManager();
    }

    public static RedisManager getInstance() {
        return RedisManagerHolder.instance;
    }

    private RedisManager() {
        RedisConfig redisConfig = RedisConfig.create();
        JedisPoolConfig jedisPoolConfig = getConfig(redisConfig);
        RedisConfig.RedisServer redisServer = redisConfig.getRedisServers().get(0);
        //if the redis have password
        if (redisServer.getPassword() != null) {
            jedisPool = new JedisPool(jedisPoolConfig,
                    redisServer.getIp(),
                    redisServer.getPort(),
                    redisConfig.getConnectionConfig().getTimeout(),
                    redisServer.getPassword());
        } else {
            jedisPool = new JedisPool(jedisPoolConfig,
                    redisServer.getIp(),
                    redisServer.getPort(),
                    redisConfig.getConnectionConfig().getTimeout());
        }
    }

    public RedisConnection getConnection() {
        return new SingleRedisConnection(jedisPool.getResource());
    }


    private JedisPoolConfig getConfig(RedisConfig redisConfig) {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(redisConfig.getConnectionConfig().getMaxIdle());
        jedisPoolConfig.setMaxTotal(redisConfig.getConnectionConfig().getMaxTotal());
        jedisPoolConfig.setMaxWaitMillis(redisConfig.getConnectionConfig().getMaxWait());
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setTestOnReturn(true);
        return jedisPoolConfig;
    }

}
