package com.hansong.session.redis;

import org.apache.commons.lang.SerializationUtils;
import redis.clients.jedis.JedisCluster;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by xhans on 2016/4/28.
 */
public class ClusterRedisConnection implements RedisConnection {

    private JedisCluster jedisCluster;

    public ClusterRedisConnection(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }

    @Override
    public void close() {

    }

    @Override
    public Boolean isConnected() {
        return true;
    }

    @Override
    public Long hset(String key, String field, Serializable object) {
        return jedisCluster.hset(field.getBytes(),
                field.getBytes(),
                SerializationUtils.serialize(object));
    }


    @Override
    public Object hget(String key, String field) {
        return SerializationUtils.deserialize(jedisCluster.hget(key.getBytes(), field.getBytes()));
    }

    @Override
    public Long del(String... keys) {
        return jedisCluster.del(keys);
    }

    @Override
    public Long hdel(String key, String... fields) {
        return jedisCluster.hdel(key, fields);
    }

    @Override
    public Long expire(String key, int seconds) {
        return jedisCluster.expire(key, seconds);
    }

    @Override
    public Set<String> hkeys(String key) {
        return jedisCluster.hkeys(key);
    }

    @Override
    public Boolean exists(String key) {
        return jedisCluster.exists(key);
    }
}
