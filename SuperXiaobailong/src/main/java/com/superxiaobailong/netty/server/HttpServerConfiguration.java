package com.superxiaobailong.netty.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.Properties;

/**
 * @description:
 * @author: liucheng
 * @createTime:2021/11/16 16:21
 */
public class HttpServerConfiguration {
    private static final Logger log = LogManager.getLogger(HttpServerConfiguration.class);

    private final static Integer DEFAULT_PORT = 80;
    private final static Integer DEFAULT_BOSS_THREAD_COUNT = 5;
    private final static Integer DEFAULT_WORK_THREAD_COUNT = 5;
    private final static Boolean DEFAULT_KEEP_ALIVE = Boolean.TRUE;
    private static HttpServerConfiguration DEFAULT_INSTANCE = new HttpServerConfiguration(DEFAULT_PORT,
            DEFAULT_BOSS_THREAD_COUNT, DEFAULT_WORK_THREAD_COUNT, DEFAULT_KEEP_ALIVE);

    private volatile static HttpServerConfiguration httpServerConfiguration = null;

    private Integer port;
    private Integer bossThreadCount;
    private Integer workerThreadCount;
    private Boolean keepAlive;


    private HttpServerConfiguration(Integer port, Integer bossThreadCount, Integer workerThreadCount, Boolean keepAlive) {
        this.port = port;
        this.bossThreadCount = bossThreadCount;
        this.workerThreadCount = workerThreadCount;
        this.keepAlive = keepAlive;
    }

    public static HttpServerConfiguration config() {
        //读取资源配置文件
        InputStream is = HttpServerConfiguration.class.getClassLoader().getResourceAsStream("HttpServer.properties");
        Properties prop = new Properties();
        String className = "person.name";//可以作为一个函数的变量
        try {
            prop.load(is);
        } catch (Exception e) {
            log.warn("未找到配置文件HttpServer.properties");
            prop = null;
        }

        if (prop == null) {
            return DEFAULT_INSTANCE;
        }
        if (httpServerConfiguration != null) {
            return httpServerConfiguration;
        }
        synchronized (HttpServerConfiguration.class) {
            if (httpServerConfiguration != null) {
                return httpServerConfiguration;
            }
            int port = Integer.parseInt((String) prop.getOrDefault("port", DEFAULT_PORT.toString()));
            //todo cpu的数量
            int bossThread = Integer.parseInt((String) prop.getOrDefault("bossThreadCount", DEFAULT_BOSS_THREAD_COUNT.toString()));
            int workerThread = Integer.parseInt((String) prop.getOrDefault("workerThreadCount", DEFAULT_WORK_THREAD_COUNT.toString()));
            boolean keepAlive = Boolean.parseBoolean((String) prop.getOrDefault("keepAlive", DEFAULT_KEEP_ALIVE.toString()));
            httpServerConfiguration = new HttpServerConfiguration(port, bossThread, workerThread, keepAlive);
            return httpServerConfiguration;
        }
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getBossThreadCount() {
        return bossThreadCount;
    }

    public void setBossThreadCount(Integer bossThreadCount) {
        this.bossThreadCount = bossThreadCount;
    }

    public Integer getWorkerThreadCount() {
        return workerThreadCount;
    }

    public void setWorkerThreadCount(Integer workerThreadCount) {
        this.workerThreadCount = workerThreadCount;
    }

    public Boolean getKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(Boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    public boolean isKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

}
