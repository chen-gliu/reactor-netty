package com.superxiaobailong.netty.server;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;

/**
 * @description:
 * @author: liucheng
 * @createTime:2021/11/19 16:38
 */
public class DefaultServerRequest implements ServerRequest {
    private FullHttpRequest nettyRequst;


    @Override
    public HttpMethod method() {
        return nettyRequst.method();
    }

    @Override
    public String uri() {
        return nettyRequst.uri();
    }
}
