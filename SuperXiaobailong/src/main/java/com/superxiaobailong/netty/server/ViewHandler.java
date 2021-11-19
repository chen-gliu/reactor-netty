package com.superxiaobailong.netty.server;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @description:
 * @author: liucheng
 * @createTime:2021/11/16 16:37
 */
public class ViewHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private static final Logger log = LogManager.getLogger(ViewHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext,
                                FullHttpRequest message) throws Exception {


        if (message instanceof HttpRequest) {
            log.info("hahah" + ((HttpRequest) message).uri());
        }

        if (message instanceof HttpContent) {

        }

        if (message instanceof LastHttpContent) {

        }


        log.info("hahahhaahhahahahahahhhhhh--------------------------------------------------");
        log.info("接收：");
        FullHttpResponse response = new DefaultFullHttpResponse(
                HTTP_1_1, OK,
                Unpooled.copiedBuffer("buf.toString()", CharsetUtil.UTF_8));
        if (isKeepAlive((HttpRequest) message)) {
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH.toString(), Unpooled.copiedBuffer("buf.toString()", CharsetUtil.UTF_8).readableBytes());
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            channelHandlerContext
                    .writeAndFlush(response)
                    .addListener(future -> {
                        if (future.isDone()) {
                            log.info("消息发送完成");
                        }
                        if (future.isSuccess()) {
                            log.info("消息发送成功");
                        }
                    });
            return;
        }
        channelHandlerContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    private boolean isKeepAlive(HttpRequest httpRequest) {
        return HttpUtil.isKeepAlive(httpRequest);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
