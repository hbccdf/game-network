package network.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import network.client.future.BaseWriteFuture;
import network.client.future.ConnectFuture;
import network.client.future.IProtocolWriteFutureFactory;
import network.client.future.ProtocolWriteFuture;
import network.core.BootstrapHelper;
import network.protocol.DefaultMessage;
import network.protocol.ProtocolManager;
import network.protocol.codec.CytxFrameDecoder;
import network.protocol.codec.CytxFrameEncoder;
import network.protocol.codec.DefaultMessageCodecFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class SyncTcpConnection {
    private static final Logger logger = LoggerFactory.getLogger(SyncTcpConnection.class);

    private final String ip;
    private final int port;
    private final IProtocolWriteFutureFactory factory;

    //ms
    private static final int DEFAULT_TIME_OUT = 60000;
    private final ConcurrentHashMap<Integer, List<BaseWriteFuture<?>>> requests;
    private Bootstrap bootstrap;
    private Channel channel;

    private ConnectFuture connectFuture;

    public SyncTcpConnection(String ip, int port, IProtocolWriteFutureFactory factory) {
        this.ip = ip;
        this.port = port;
        this.factory = factory;
        requests = new ConcurrentHashMap<>();
    }

    public boolean start() throws Exception{
        if (bootstrap != null) {
            return false;
        }

        Bootstrap b = new Bootstrap();
        b.group(BootstrapHelper.getClientGroup(1))
                .channel(BootstrapHelper.getClientChannelClass())
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
        bootstrap = b;
        ChannelFuture f = b.connect(ip, port);
        channel = f.channel();

        channel.eventLoop().scheduleAtFixedRate(new ClearExpiredFuture(), 0, 1, TimeUnit.MINUTES);

        f.sync();

        connectFuture = new ConnectFuture();
        return true;
    }

    public boolean connect() throws InterruptedException {
        return connectAsync().get();
    }

    public ConnectFuture connectAsync() {
        return connectFuture;
    }

    public void close(){
        try {
            if (connectFuture != null) {
                connectFuture.setResult(false);
            }
            if (channel != null) {
                channel.close().sync();
                channel = null;
            }
            if (bootstrap != null) {
                bootstrap.config().group().shutdownGracefully();
                bootstrap = null;
            }
        } catch (Exception e) {
            logger.error("close error", e);
        }
    }

    public String getAddress() {
        return ip + ":" + port;
    }

    public boolean isValidate() {
        if (this.channel != null) {
            return this.channel.isActive();
        }
        return false;
    }

    public void send(DefaultMessage msg) {
        try {
            write0(msg);
        } catch (Exception e) {
            logger.error("{}", msg, e);
        }
    }

    public <T> T write(DefaultMessage msg, Class<T> clz) {
        try {
            Future<T> future = writeAsync(msg, clz);
            return future.get();
        } catch (Exception e) {
            logger.error("", msg, e);
        }
        return null;
    }

    public <T> Future<T> writeAsync(DefaultMessage msg, Class<T> clz) {
        try {
            ProtocolWriteFuture<T> future = factory.newProtocolWriteFuture(clz);
            int cmd = ProtocolManager.getId(clz);
            addFuture(cmd, future);
            write0(msg);
            return future;
        } catch (Exception e) {
            logger.error("{}", msg, e);
        }
        return null;
    }

    private void write0(DefaultMessage msg){
        if(channel != null){
            channel.writeAndFlush(msg);
        }
    }

    private BaseWriteFuture<?> addFuture(int cmd, BaseWriteFuture<?> future) {
        List<BaseWriteFuture<?>> list = requests.computeIfAbsent(cmd, k -> new LinkedList<>());
        list.add(future);
        return future;
    }

    private BaseWriteFuture<?> removeFuture(int cmd) {
        List<BaseWriteFuture<?>> list = requests.get(cmd);
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.remove(0);
    }


    private class CytxHandler extends ChannelInboundHandlerAdapter{
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            logger.info("session created. " + ctx.channel().remoteAddress());
            connectFuture.setResult(true);
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            logger.error("session closed. " + ctx.channel().remoteAddress());
            connectFuture.setResult(false);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            logger.debug("message received. " + ctx.channel().remoteAddress());
            DefaultMessage message = (DefaultMessage) msg;

            BaseWriteFuture<?> future = removeFuture(message.getCmdId());
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
            for (Map.Entry<Integer, List<BaseWriteFuture<?>>> entry : requests.entrySet()) {
                List<BaseWriteFuture<?>> list = entry.getValue();
                if (list != null) {
                    for (BaseWriteFuture<?> f : list) {
                        if (f.isExpired(DEFAULT_TIME_OUT)) {
                            list.remove(f);
                        }
                    }
                }
            }
        }
    }
}
