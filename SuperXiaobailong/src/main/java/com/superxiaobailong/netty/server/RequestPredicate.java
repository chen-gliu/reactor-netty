package com.superxiaobailong.netty.server;

/**
 * @description:
 * @author: liucheng
 * @createTime:2021/11/19 14:25
 */
@FunctionalInterface
public interface RequestPredicate {

    /**
     * 指定serverRequest是否满足条件
     *
     * @param serverRequest
     * @return
     */
    boolean test(ServerRequest serverRequest);

    /**
     * 将指定的other与当前实例进行组合，进行test判断时，会同时将两个{@link RequestPredicate}一起判断。
     *
     * @param other
     * @return
     */
    default RequestPredicate and(RequestPredicate other) {
        return new RequestPredicates.AndRequestPredicate(this, other);
    }

}
