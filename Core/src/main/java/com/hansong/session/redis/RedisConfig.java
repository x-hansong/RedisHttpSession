package com.hansong.session.redis;

import com.hansong.session.utils.JsonUtils;

import java.io.*;
import java.nio.file.NoSuchFileException;
import java.util.List;

/**
 * Created by xhans on 2016/4/27.
 */
public class RedisConfig {

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
            return JsonUtils.decode(redisJson, RedisConfig.class);
        } else {
            throw new InternalError("Read redis config failed");
        }
    }

    private static final String REDIS_CONFIG = "redis.json";

    private static String readConfig(){
        try {
            //read config file from src folder
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

            return sb.toString();
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
        private int maxTotal;
        private int maxIdle;
        private int timeout;
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
