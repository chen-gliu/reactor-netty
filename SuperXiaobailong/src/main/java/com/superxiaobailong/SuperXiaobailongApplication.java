package com.superxiaobailong;

import com.superxiaobailong.netty.server.HttpServer;

/**
 * @description:
 * @author: liucheng
 * @createTime:2021/11/16 19:54
 */
public class SuperXiaobailongApplication {
    public static void main(String[] args) {
        HttpServer.create().bind();
    }
}
