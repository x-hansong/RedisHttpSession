package com.hansong.session;

import com.hansong.session.redis.RedisConnection;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by xhans on 2016/4/29.
 */
public class RedisHttpSessionProxy implements InvocationHandler {

    private Object originalObj;

    private RedisHttpSessionRepository repository = RedisHttpSessionRepository.getInstance();

    public Object bind(Object originalObj) {
        this.originalObj = originalObj;
        return Proxy.newProxyInstance(originalObj.getClass().getClassLoader(), originalObj.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RedisHttpSession session = (RedisHttpSession) originalObj;
        //check redis connection
        RedisConnection connection = session.getRedisConnection();
        if (!connection.isConnected()){
            connection.close();
            session.setRedisConnection(repository.getRedisConnection());
        }
        //For every methods of interface, check it valid or not
        if (session.isInvalidated()){
            throw new IllegalStateException("The HttpSession has already be invalidated.");
        } else {
            Object result =  method.invoke(originalObj, args);
            //if not invalidate method, refresh expireTime and lastAccessedTime;
            if (!method.getName().equals("invalidate")) {
                session.refresh();
                session.setLastAccessedTime(System.currentTimeMillis());
            }
            return result;
        }
    }
}
