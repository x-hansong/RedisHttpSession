package com.hansong.session;

import com.hansong.session.redis.RedisConnection;
import com.hansong.session.redis.RedisManager;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

/**
 * Created by xhans on 2016/4/27.
 */
public class RedisHttpSessionRepository {
    private RedisManager redisManager;

    //all session use one redis connection
    private RedisConnection redisConnection;


    private RedisHttpSessionRepository() {
        redisManager = RedisManager.getInstance();
        redisConnection = redisManager.getConnection();
    }


    private final static RedisHttpSessionRepository instance = new RedisHttpSessionRepository();

    public static RedisHttpSessionRepository getInstance(){
        return instance;
    }

    public HttpSession newSession(ServletContext servletContext) {
        checkConnection();
        RedisHttpSession redisHttpSession = RedisHttpSession.createNew(servletContext, redisConnection);

        return (HttpSession) new RedisHttpSessionProxy().bind(redisHttpSession);
    }

    /**
     * get session according to token
     * @param token
     * @param servletContext
     * @return session associate to token or null if the token is invalid
     */
    public HttpSession getSession(String token, ServletContext servletContext){
        checkConnection();
        if (redisConnection.exists(RedisHttpSession.SESSION_PREFIX + token)) {
            RedisHttpSession redisHttpSession = RedisHttpSession.createWithExistSession(token, servletContext, redisConnection);
            return (HttpSession) new RedisHttpSessionProxy().bind(redisHttpSession);
        } else {
            return null;
        }
    }

    public RedisConnection getRedisConnection(){
        checkConnection();
        return redisConnection;
    }

    private void checkConnection(){
        if (!redisConnection.isConnected()){
            redisConnection.close();
            redisConnection = redisManager.getConnection();
        }
    }
}

