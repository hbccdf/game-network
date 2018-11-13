package network.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.EventExecutorGroup;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MultiThreadProtocolHandler<T> implements IProtocolHandler<T> {
    private final IProtocolHandler<T> handler;
    private final IEventExecutorChooser chooser;
    private final EventExecutorGroup group;

    private static final int DEFAULT_EVENT_LOOP_THREADS;

    static {
        DEFAULT_EVENT_LOOP_THREADS = Math.max(1, Runtime.getRuntime().availableProcessors() * 2);
    }

    public MultiThreadProtocolHandler(IProtocolHandler<T> handler) {
        this(handler, ThreadPerConnEventExecutorChooserFactory.INSTANCE);
    }

    public MultiThreadProtocolHandler(IProtocolHandler<T> handler, IEventExecutorChooserFactory chooserFactory) {
        this(handler, new DefaultEventExecutorGroup(DEFAULT_EVENT_LOOP_THREADS), chooserFactory);
    }

    public MultiThreadProtocolHandler(IProtocolHandler<T> handler, EventExecutorGroup group, IEventExecutorChooserFactory chooserFactory) {
        super();
        this.handler = handler;
        this.group = group;
        this.chooser = chooserFactory.newChooser(this.group);
    }

    @Override
    public void sessionOpened(ChannelHandlerContext ctx) {
        this.handler.sessionOpened(ctx);
    }

    @Override
    public void sessionClosed(ChannelHandlerContext ctx) {
        this.handler.sessionClosed(ctx);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, T msg) {
        EventExecutor executor = this.chooser.choose(msg);
        if(executor == null){
            log.error("can't choose executor, message={}", msg);
        }else{
            executor.execute(()-> handler.messageReceived(ctx, msg));
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        this.handler.exceptionCaught(ctx, cause);
    }

    @Override
    public void release() {
        if(handler != null){
            handler.release();
        }
        if(group != null){
            group.shutdownGracefully();
        }
    }
}
