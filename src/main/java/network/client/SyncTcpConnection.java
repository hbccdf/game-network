package network.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import network.protocol.DefaultMessage;
import network.protocol.codec.CytxFrameDecoder;
import network.protocol.codec.CytxFrameEncoder;
import network.protocol.codec.DefaultMessageCodecFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class SyncTcpConnection {
    private static final Logger logger = LoggerFactory.getLogger(SyncTcpConnection.class);

    private String ip;
    private int port;

    //ms
    private static final int DEFAULT_TIME_OUT = 60000;
    private ConcurrentHashMap<Integer, List<WriteFuture<?>>> requests;
    private Bootstrap bootstrap;
    private Channel channel;

    private ConnectFuture connectFuture;

    public SyncTcpConnection(String ip, int port) {
        this.ip = ip;
        this.port = port;
        requests = new ConcurrentHashMap<>();

        new DefaultEventExecutorGroup(1).next().scheduleAtFixedRate(new ClearExpiredFuture(), 0, 1, TimeUnit.MINUTES);
    }

    public boolean start() throws Exception{
        if (bootstrap != null) {
            return false;
        }

        EventLoopGroup group = new NioEventLoopGroup();
        io.netty.bootstrap.Bootstrap b = new io.netty.bootstrap.Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new CytxFrameEncoder(DefaultMessageCodecFactory.INSTANCE.getEncoder(ch)));
                        p.addLast(new CytxFrameDecoder<>(DefaultMessageCodecFactory.INSTANCE.getDecoder(ch)));
                        p.addLast(new CytxHandler());
                    }
                })
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.SO_KEEPALIVE, true);

        // Start the connection attempt.
        channel = b.connect(ip, port).sync().channel();
        bootstrap = b;

        connectFuture = new ConnectFuture();
        return true;
    }

    public boolean connect() throws ExecutionException, InterruptedException {
        return connectAsync().get().booleanValue();
    }

    public ConnectFuture connectAsync() {
        return connectFuture;
    }

//    public boolean reconnect() throws ExecutionException, InterruptedException {
//        if (!connectFuture.isDone()) {
//            return connect();
//        } else if (isValidate()) {
//            return true;
//        } else {
//        }
//    }

    public void close() throws Exception{
        if(channel != null){
            channel.close().sync();
            channel = null;
        }
        if(bootstrap != null){
            bootstrap.config().group().shutdownGracefully();
            bootstrap = null;
        }
    }

    public boolean isValidate() {
        if (this.channel != null) {
            return this.channel.isActive();
        }
        return false;
    }

    public <T> T write(DefaultMessage msg, IResultHandler<T> handler){
        try {
            Future<T> future = writeAsync(msg, handler);
            return future.get(DEFAULT_TIME_OUT, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            logger.error("{}", msg, e);
        } finally{
        }
        return null;
    }
    public <T> Future<T> writeAsync(DefaultMessage msg, IResultHandler<T> handler){
        try {
            WriteFuture<T> future = new WriteFuture<>(handler);
            addFuture(msg.getCmdId(), future);
            write0(msg);
            return future;
        } catch (Exception e) {
            logger.error("", msg, e);
        }
        return null;
    }
    private void write0(DefaultMessage msg){
        if(channel != null){
            channel.writeAndFlush(msg);
        }
    }

    private WriteFuture<?> addFuture(int cmd, WriteFuture<?> future) {
        List<WriteFuture<?>> list = requests.get(cmd);
        if (list == null) {
            list = new LinkedList<>();
            requests.put(cmd, list);
        }
        list.add(future);
        return future;
    }

    private WriteFuture<?> removeFuture(int cmd) {
        List<WriteFuture<?>> list = requests.get(cmd);
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.remove(0);
    }


    private class CytxHandler extends ChannelInboundHandlerAdapter{
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            logger.error("session created. " + ctx.channel().remoteAddress());
            connectFuture.setResult(true);
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            logger.error("session closed. " + ctx.channel().remoteAddress());
            connectFuture.setResult(false);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            logger.debug("message recieved. " + ctx.channel().remoteAddress());
            DefaultMessage message = (DefaultMessage) msg;

            WriteFuture<?> future = removeFuture(message.getCmdId());
            if (future != null) {
                future.setResponse(message);
            } else {
                //todo using protocol handler to process this message
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            logger.error("exception caught. " + ctx.channel().remoteAddress(), cause);
        }
    }


    private class ClearExpiredFuture implements Runnable{

        @Override
        public void run() {
            for (Map.Entry<Integer, List<WriteFuture<?>>> entry : requests.entrySet()) {
                List<WriteFuture<?>> list = entry.getValue();
                if (list != null) {
                    for (WriteFuture<?> f : list) {
                        if (f.isExpired(DEFAULT_TIME_OUT)) {
                            list.remove(f);
                        }
                    }
                }
            }
        }
    }
}
