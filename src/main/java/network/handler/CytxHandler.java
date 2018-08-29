package network.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class CytxHandler<T> extends ChannelInboundHandlerAdapter {
    private IProtocolHandler<T> handler;

    public CytxHandler(IProtocolHandler<T> handler) {
        super();
        this.handler = handler;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if (handler != null) {
            handler.sessionOpened(ctx);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (handler != null) {
            handler.sessionClosed(ctx);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (handler != null) {
            handler.messageReceived(ctx, (T)msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (handler != null) {
            handler.exceptionCaught(ctx, cause);
        }
    }
}
