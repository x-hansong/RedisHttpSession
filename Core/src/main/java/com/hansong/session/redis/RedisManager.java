package com.hansong.session.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by xhans on 2016/4/28.
 */
public class RedisManager {

    static Logger logger = LoggerFactory.getLogger(RedisManager.class);

    private JedisCluster jedisCluster;
    private JedisPool jedisPool;
    private boolean isCluster;

    private static class RedisManagerHolder{
        private static final RedisManager instance = new RedisManager();
    }

    public static RedisManager getInstance(){
        return RedisManagerHolder.instance;
    }

    private RedisManager(){
        RedisConfig redisConfig = RedisConfig.create();
        JedisPoolConfig jedisPoolConfig = getConfig(redisConfig);
        isCluster = redisConfig.isCluster();
        if (isCluster){
            Set<HostAndPort> clusterNodes = getClusterNode(redisConfig);
            jedisCluster  = new JedisCluster(clusterNodes,
                    redisConfig.getConnectionConfig().getTimeout(),
                    jedisPoolConfig);
        } else {
            RedisConfig.RedisServer redisServer = redisConfig.getRedisServers().get(0);
            //if the redis set password
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
    }

    public RedisConnection getConnection(){
        if (isCluster){
            return new ClusterRedisConnection(jedisCluster);
        } else {
            return new SingleRedisConnection(jedisPool.getResource());
        }
    }

    private Set<HostAndPort> getClusterNode(RedisConfig redisConfig){
        Set<HostAndPort> clusterNodes = new HashSet<>();
        for (RedisConfig.RedisServer redisServer : redisConfig.getRedisServers()){
            clusterNodes.add(new HostAndPort(redisServer.getIp(), redisServer.getPort()));
        }
        return clusterNodes;
    }

    private JedisPoolConfig getConfig(RedisConfig redisConfig){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(redisConfig.getConnectionConfig().getMaxIdle());
        jedisPoolConfig.setMaxTotal(redisConfig.getConnectionConfig().getMaxTotal());
        jedisPoolConfig.setMaxWaitMillis(redisConfig.getConnectionConfig().getMaxWait());
        //取出连接时检测连接是否有效
        jedisPoolConfig.setTestOnBorrow(true);
        //归还连接时检测连接是否有效
        jedisPoolConfig.setTestOnReturn(true);
        return jedisPoolConfig;
    }

}
