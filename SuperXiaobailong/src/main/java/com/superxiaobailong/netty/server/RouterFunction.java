package com.superxiaobailong.netty.server;

/**
 * @description:
 * @author: liucheng
 * @createTime:2021/11/18 10:55
 */
@FunctionalInterface
public interface RouterFunction<T extends ServerResponse> {
    /**
     * 路由
     *
     * @param request
     * @return
     */
    HandlerFunction<T> route(ServerRequest request);


    /**
     * 合并两个返回类型一致的{@link RouterFunction}
     *
     * @param other
     * @return
     */
    default RouterFunction<T> and(RouterFunction<T> other) {
        return new RouterFunctions.SameComposedRouterFunction<>(this, other);
    }


    /**
     * 合并两个返回类型不一致的{@link RouterFunction}
     *
     * @param other
     * @return
     */
    default RouterFunction<?> andOther(RouterFunction<?> other) {
        return new RouterFunctions.DifferentComposedRouterFunction(this, other);
    }
}
