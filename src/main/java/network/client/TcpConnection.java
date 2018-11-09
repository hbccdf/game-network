package network.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import network.handler.CytxHandler;
import network.handler.IProtocolHandler;
import network.protocol.codec.CytxFrameDecoder;
import network.protocol.codec.CytxFrameEncoder;
import network.protocol.codec.IProtocolCodecFactory;

public class TcpConnection<T> {
    private final String ip;
    private final int port;
    private final IProtocolCodecFactory<T> codecFactory;
    private final IProtocolHandler<T> handler;

    private Bootstrap bootstrap;
    private Channel channel;

    public TcpConnection(String ip, int port, IProtocolCodecFactory<T> codecFactory, IProtocolHandler<T> handler){
        this.ip = ip;
        this.port = port;
        this.codecFactory = codecFactory;
        this.handler = handler;
    }

    public boolean connect() throws Exception{
        if (bootstrap != null) {
            return false;
        }

        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new CytxFrameEncoder(codecFactory.getEncoder(ch)));
                        p.addLast(new CytxFrameDecoder<>(codecFactory.getDecoder(ch)));
                        p.addLast(new CytxHandler<>(handler));
                    }
                })
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.SO_KEEPALIVE, true);

        // Start the connection attempt.
        channel = b.connect(ip, port).sync().channel();
        bootstrap = b;
        return true;
    }

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

    public void write(T msg){
        if(channel != null){
            channel.writeAndFlush(msg);
        }
    }
    @Override
    public String toString() {
        return "tcp:\\"+ip+":"+port;
    }
}
