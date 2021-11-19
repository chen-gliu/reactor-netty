package com.superxiaobailong.netty.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @description:
 * @author: liucheng
 * @createTime:2021/11/19 13:51
 */
public class RouterFunctions {


    public static Builder route() {
        return new RouterFunctionBuilder();
    }

    public static <T extends ServerResponse> RouterFunction<T> route(RequestPredicate pattern, HandlerFunction<ServerResponse> handlerFunction) {
        return new DefaultRouterFunction(pattern, handlerFunction);
    }

    abstract static class AbstractRouterFunction<T extends ServerResponse> implements RouterFunction<T> {

    }


    /**
     * 组合两个相同类型的 {@link RouterFunction }。
     * 当第一个{@link RouterFunction }没有匹配指定的{@link ServerRequest}时会调用第二个{@link RouterFunction }，这样形成一个链表。
     */
    static final class SameComposedRouterFunction<T extends ServerResponse> extends AbstractRouterFunction<T> {

        private final RouterFunction<T> first;
        private final RouterFunction<T> second;

        public SameComposedRouterFunction(RouterFunction<T> first, RouterFunction<T> second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public HandlerFunction<T> route(ServerRequest request) {
            return Optional.ofNullable(first.route(request)).orElse(second.route(request));
        }
    }


    /**
     * 组合两个不同类型的 {@link RouterFunction }
     * 当第一个{@link RouterFunction }没有匹配指定的{@link ServerRequest}时会调用第二个{@link RouterFunction }，这样形成一个链表。
     */
    static final class DifferentComposedRouterFunction extends AbstractRouterFunction<ServerResponse> {

        private final RouterFunction<?> first;
        private final RouterFunction<?> second;

        public DifferentComposedRouterFunction(RouterFunction<?> first, RouterFunction<?> second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public HandlerFunction<ServerResponse> route(ServerRequest request) {
            return Optional.ofNullable(first.route(request)).orElse((HandlerFunction) second.route(request));
        }
    }

    private static class DefaultRouterFunction<T extends ServerResponse> extends AbstractRouterFunction<T> {

        private RequestPredicate requestPredicate;
        private HandlerFunction<T> handlerFunction;

        public DefaultRouterFunction(RequestPredicate requestPredicate, HandlerFunction<T> handlerFunction) {
            this.requestPredicate = requestPredicate;
            this.handlerFunction = handlerFunction;
        }

        /**
         * 判断当前RouterFunction是否可处理指定{@link ServerRequest}
         *
         * @param request
         * @return 如果可以处理则返回对应的 {@link HandlerFunction },如果不能处理则返回null。
         */
        @Override
        public HandlerFunction<T> route(ServerRequest request) {
            if (requestPredicate.test(request)) {
                return handlerFunction;
            }
            return null;
        }
    }

    static class BuiltRouterFunction extends RouterFunctions.AbstractRouterFunction<ServerResponse> {

        private final List<RouterFunction<ServerResponse>> routerFunctions;

        public BuiltRouterFunction(List<RouterFunction<ServerResponse>> routerFunctions) {
            if (routerFunctions == null || routerFunctions.isEmpty()) {
                throw new IllegalArgumentException("RouterFunctions must not be empty");
            }
            this.routerFunctions = new ArrayList<>(routerFunctions);
        }

        @Override
        public HandlerFunction<ServerResponse> route(ServerRequest request) {
            return routerFunctions.stream()
                    .map(routerFunction -> routerFunction.route(request))
                    .filter(handlerFunction -> handlerFunction != null)
                    .findFirst()
                    .orElse(null);

        }

    }


    public interface Builder {
        /**
         * 构建一个Get请求的Router
         *
         * @param pattern
         * @param handlerFunction
         * @return
         */
        Builder GET(String pattern, HandlerFunction<ServerResponse> handlerFunction);

        /**
         * 构建一个POST请求的router
         *
         * @param pattern
         * @param handlerFunction
         * @return
         */
        Builder POST(String pattern, HandlerFunction<ServerResponse> handlerFunction);

        /**
         * 构造
         *
         * @return
         */
        RouterFunction<ServerResponse> build();

    }

}
