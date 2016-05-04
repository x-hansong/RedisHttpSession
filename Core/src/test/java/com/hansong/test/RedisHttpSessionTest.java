package com.hansong.test;

import com.hansong.session.RedisHttpSessionRepository;
import org.junit.Test;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.HashSet;

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

        System.out.println(redisHttpSession.getAttribute("id"));
        System.out.println(redisHttpSession.getAttribute("map"));

        for (String str : redisHttpSession.getValueNames()){
            System.out.println(str);
        }

        redisHttpSession.invalidate();
        System.out.println(redisHttpSession.getAttribute("id"));
        System.out.println(redisHttpSession.getAttribute("id"));

    }

//    @Test
    public void testMultiSession(){
        RedisHttpSessionRepository repository = RedisHttpSessionRepository.getInstance();
        HashSet<HttpSession> set = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            set.add(repository.newSession(null));
            System.out.println("session" + i);
        }
    }
}
