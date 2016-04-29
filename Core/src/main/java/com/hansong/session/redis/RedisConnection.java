package com.hansong.session.redis;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by xhans on 2016/4/28.
 */
public interface RedisConnection {
    void close();
    Boolean isConnected();
    Long hset(String key, String field, Serializable object);
    Object hget(String key, String field);
    Long del(String... keys);
    Long hdel(String key, String... fields);
    Long expire(String key, int seconds);
    Set<String> hkeys(String key);
    Boolean exists(String key);
}
