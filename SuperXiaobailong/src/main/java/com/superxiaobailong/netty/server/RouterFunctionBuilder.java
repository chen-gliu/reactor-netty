package com.superxiaobailong.netty.server;

import java.util.List;

/**
 * @description:
 * @author: liucheng
 * @createTime:2021/11/19 16:57
 */
public class RouterFunctionBuilder implements RouterFunctions.Builder {

    private List<RouterFunction<ServerResponse>> routerFunctionList;

    private RouterFunctions.Builder add(RequestPredicate predicate, HandlerFunction<ServerResponse> handlerFunction) {
        this.routerFunctionList.add(RouterFunctions.route(predicate, handlerFunction));
        return this;
    }


    @Override
    public RouterFunctions.Builder GET(String pattern, HandlerFunction<ServerResponse> handlerFunction) {
        return add(RequestPredicates.GET(pattern), handlerFunction);
    }

    @Override
    public RouterFunctions.Builder POST(String pattern, HandlerFunction<ServerResponse> handlerFunction) {
        return add(RequestPredicates.POST(pattern), handlerFunction);
    }

    @Override
    public RouterFunction<ServerResponse> build() {
        if (this.routerFunctionList.isEmpty()) {
            throw new IllegalStateException("No routes registered.");
        }
        return new RouterFunctions.BuiltRouterFunction(routerFunctionList);
    }


}
