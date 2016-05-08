package com.hansong.test;

import com.hansong.session.RedisHttpSessionRepository;
import org.junit.Assert;
import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by xhans on 2016/4/29.
 */
public class RedisHttpSessionTest {
    @Test
    public void testRedisHttpSession(){
        RedisHttpSessionRepository repository = RedisHttpSessionRepository.getInstance();
        HttpSession redisHttpSession = repository.newSession(null);

        System.out.println(redisHttpSession.getCreationTime());
        System.out.println(redisHttpSession.getMaxInactiveInterval());
        System.out.println(redisHttpSession.getLastAccessedTime());

        redisHttpSession.setAttribute("id", "123");
        HashMap<String,String> map = new HashMap<>();
        map.put("test", "test");
        redisHttpSession.setAttribute("map", map);

        Assert.assertEquals("123", redisHttpSession.getAttribute("id"));
        Assert.assertEquals(map, redisHttpSession.getAttribute("map"));

        for (String str : redisHttpSession.getValueNames()){
            System.out.println(str);
        }

        redisHttpSession.invalidate();
        //access a invalidated session will throw IllegalStateException
        System.out.println(redisHttpSession.getAttribute("id"));

    }

    @Test
    public void testMultiSession(){
        RedisHttpSessionRepository repository = RedisHttpSessionRepository.getInstance();
        HashSet<HttpSession> set = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            set.add(repository.newSession(null));
            System.out.println("session" + i);
        }
    }

//    @Test
    public void testCluster(){

        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
        //Jedis Cluster will attempt to discover cluster nodes automatically
        jedisClusterNodes.add(new HostAndPort("127.0.0.1", 7000));
        JedisCluster jc = new JedisCluster(jedisClusterNodes);
        jc.set("foo", "bar");
        String value = jc.get("foo");
        System.out.println(value);
    }
}
