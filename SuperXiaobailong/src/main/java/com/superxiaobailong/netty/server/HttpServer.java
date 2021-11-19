package com.superxiaobailong.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

/**
 * @description:
 * @author: liucheng
 * @createTime:2021/11/16 16:20
 */
public class HttpServer {

    private static final Logger log = LogManager.getLogger(HttpServer.class);

    static final HttpServer INSTANCE = new HttpServer();
    final ServerBootstrap serverBootstrap;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workGroup;

    public HttpServer() {
        serverBootstrap = createServerBootstrap();
    }

    ServerBootstrap createServerBootstrap() {
        HttpServerConfiguration httpServerConfiguration = HttpServerConfiguration.config();
        bossGroup = new NioEventLoopGroup(httpServerConfiguration.getBossThreadCount());
        workGroup = new NioEventLoopGroup(httpServerConfiguration.getWorkerThreadCount());
        return new ServerBootstrap()
                .group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.DEBUG))
//                .option(ChannelOption.SO_REUSEADDR, true)
                .childOption(ChannelOption.AUTO_READ, true)
//                .childOption(ChannelOption.TCP_NODELAY, true)
//                .localAddress(new InetSocketAddress(httpServerConfiguration.getPort()))
                .childHandler(new HttpServerInitializer());

    }

    public static HttpServer create() {
        return INSTANCE;
    }

    public void bind() {
        try {
            serverBootstrap.bind(HttpServerConfiguration.config().getPort()).sync();
            log.debug("launch server success on {}", HttpServerConfiguration.config().getPort());
        } catch (InterruptedException e) {
            e.printStackTrace();
            Optional.ofNullable(bossGroup).ifPresent(work -> bossGroup.shutdownGracefully());
            Optional.ofNullable(workGroup).ifPresent(work -> workGroup.shutdownGracefully());
        }
    }


}
