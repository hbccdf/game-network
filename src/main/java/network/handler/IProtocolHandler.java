package network.handler;

import io.netty.channel.ChannelHandlerContext;

public interface IProtocolHandler<T> {

    void sessionOpened(ChannelHandlerContext ctx);

    void sessionClosed(ChannelHandlerContext ctx);

    void messageReceived(ChannelHandlerContext ctx, T msg);

    void exceptionCaught(ChannelHandlerContext ctx, Throwable cause);

    void release();
}
