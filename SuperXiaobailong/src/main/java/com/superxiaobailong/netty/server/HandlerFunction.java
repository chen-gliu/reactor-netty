package com.superxiaobailong.netty.server;

/**
 * @description:
 * @author: liucheng
 * @createTime:2021/11/19 13:40
 */
public interface HandlerFunction<T extends ServerResponse> {

    /**
     * Handle the given request.
     * @param request the request to handle
     * @return the response
     */
    T handle(ServerRequest request);
}
