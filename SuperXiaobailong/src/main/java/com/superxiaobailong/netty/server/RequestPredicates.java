package com.superxiaobailong.netty.server;


import io.netty.handler.codec.http.HttpMethod;

import java.util.*;

/**
 * @description:
 * @author: liucheng
 * @createTime:2021/11/19 15:34
 */
public class RequestPredicates {


    public static RequestPredicate GET(String pattern) {
        return method(HttpMethod.GET).and(path(pattern));
    }


    public static RequestPredicate HEAD(String pattern) {
        return method(HttpMethod.HEAD).and(path(pattern));
    }


    public static RequestPredicate POST(String pattern) {
        return method(HttpMethod.POST).and(path(pattern));
    }

    public static RequestPredicate method(HttpMethod httpMethod) {
        return new HttpMethodPredicate(httpMethod);
    }

    public static RequestPredicate path(String pattern) {
        Objects.requireNonNull(pattern, "pattern must not be null");
        if (!pattern.isEmpty() && !pattern.startsWith("/")) {
            pattern = "/" + pattern;
        }
        return new PathPredicate(pattern);
    }


    static class AndRequestPredicate implements RequestPredicate {

        private RequestPredicate left;
        private RequestPredicate right;

        public AndRequestPredicate(RequestPredicate left, RequestPredicate right) {
            Objects.requireNonNull(left, "Left RequestPredicate must not be null");
            Objects.requireNonNull(right, "right RequestPredicate must not be null");
            this.left = left;
            this.right = right;
        }


        @Override
        public boolean test(ServerRequest serverRequest) {
            return left.test(serverRequest) && right.test(serverRequest);
        }
    }

    private static class HttpMethodPredicate implements RequestPredicate {

        private final Set<HttpMethod> httpMethods;


        public HttpMethodPredicate(HttpMethod... httpMethods) {
            if (httpMethods == null || httpMethods.length <= 0) {
                throw new IllegalArgumentException("HttpMethods must not be empty");
            }
            this.httpMethods = new HashSet<>(Arrays.asList(httpMethods));
        }

        @Override
        public boolean test(ServerRequest request) {
            HttpMethod method = method(request);
            boolean match = this.httpMethods.contains(method);
            return match;
        }

        private static HttpMethod method(ServerRequest request) {
            return request.method();
        }

        @Override
        public String toString() {
            if (this.httpMethods.size() == 1) {
                return this.httpMethods.iterator().next().toString();
            } else {
                return this.httpMethods.toString();
            }
        }
    }

    private static class PathPredicate implements RequestPredicate {

        private String matchUrl;

        public PathPredicate(String matchUrl) {
            Objects.requireNonNull(matchUrl, "matchUrl must not be null");
            this.matchUrl = matchUrl;
        }

        @Override
        public boolean test(ServerRequest request) {
            if (matchUrl.equals(request.uri())) {
                return true;
            }
            return false;
        }


        @Override
        public String toString() {
            return matchUrl;
        }

    }


}
