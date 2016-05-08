package com.hansong.session.redis;

import com.hansong.session.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.NoSuchFileException;
import java.util.List;

/**
 * Created by xhans on 2016/4/27.
 */
public class RedisConfig {

    static Logger logger = LoggerFactory.getLogger(RedisConfig.class);

    private static final String CONNECTION_CONFIG = "ConnectionConfig";
    private static final String REDIS_SERVER = "RedisServer";

    private ConnectionConfig connectionConfig;

    private List<RedisServer> redisServers;


    public ConnectionConfig getConnectionConfig() {
        return connectionConfig;
    }

    public void setConnectionConfig(ConnectionConfig connectionConfig) {
        this.connectionConfig = connectionConfig;
    }

    public List<RedisServer> getRedisServers() {
        return redisServers;
    }

    public void setRedisServers(List<RedisServer> redisServers) {
        this.redisServers = redisServers;
    }


    public static RedisConfig create(){
        String redisJson = readConfig();
        if (redisJson != null){
            RedisConfig redisConfig = JsonUtils.decode(redisJson, RedisConfig.class);
            return redisConfig;
        } else {
            throw new InternalError("Read redis config failed");
        }
    }

    private static final String REDIS_CONFIG = "redis.json";

    private static String readConfig(){
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream is =  classLoader.getResourceAsStream(REDIS_CONFIG);
            if (is == null){
                throw new NoSuchFileException("Resource file not found. Note that the current directory is the source folder!");
            }
            StringBuilder sb = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line = bufferedReader.readLine();
            while (line != null){
                sb.append(line);
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            is.close();

            String redisJson = sb.toString();
            logger.debug("Read {}: {}", REDIS_CONFIG, redisJson);

            return redisJson;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class RedisServer{
        private String ip;

        private int port;

        private String password;

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class ConnectionConfig {
        //可用连接实例的最大数目，默认值为8；
        //如果赋值为-1，则表示不限制；如果pool已经分配了maxTotal个jedis实例，则此时pool的状态为exhausted(耗尽)。
        private int maxTotal;
        //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
        private int maxIdle;
        //空闲连接的过期时间
        private int timeout;
        //等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
        private int maxWait;

        public int getMaxTotal() {
            return maxTotal;
        }

        public void setMaxTotal(int maxTotal) {
            this.maxTotal = maxTotal;
        }

        public int getMaxIdle() {
            return maxIdle;
        }

        public void setMaxIdle(int maxIdle) {
            this.maxIdle = maxIdle;
        }

        public int getTimeout() {
            return timeout;
        }

        public void setTimeout(int timeout) {
            this.timeout = timeout;
        }

        public int getMaxWait() {
            return maxWait;
        }

        public void setMaxWait(int maxWait) {
            this.maxWait = maxWait;
        }
    }
}
