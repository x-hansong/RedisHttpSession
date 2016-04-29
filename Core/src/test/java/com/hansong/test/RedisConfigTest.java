package com.hansong.test;

import com.hansong.session.redis.RedisConfig;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xhans on 2016/4/28.
 */
public class RedisConfigTest {
    static Logger logger = LoggerFactory.getLogger(RedisConfig.class);
    @Test
    public void testRedisConfig(){
        logger.debug(System.getProperty("user.dir"));
        RedisConfig redisConfig = RedisConfig.create();
        logger.debug(String.valueOf(redisConfig.isCluster()));
        logger.debug(String.valueOf(redisConfig.getRedisServers()));
        logger.debug(String.valueOf(redisConfig.getConnectionConfig()));
    }
}
