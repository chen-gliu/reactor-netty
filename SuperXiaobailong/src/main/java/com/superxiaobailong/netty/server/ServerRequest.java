package com.superxiaobailong.netty.server;

import io.netty.handler.codec.http.HttpMethod;

/**
 * @description:
 * @author: liucheng
 * @createTime:2021/11/19 13:49
 */
public interface ServerRequest {

    /**
     * 获取请求方法
     *
     * @return
     */
    HttpMethod method();

    /**
     * 获取请求uri
     *
     * @return
     */
    String uri();
}
