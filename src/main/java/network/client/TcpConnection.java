package network.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import network.core.BootstrapHelper;

public class TcpConnection {
    private final String ip;
    private final int port;
    private final ChannelInitializer<SocketChannel> initializer;

    private Bootstrap bootstrap;
    private Channel channel;

    public TcpConnection(String ip, int port, ChannelInitializer<SocketChannel> initializer){
        this.ip = ip;
        this.port = port;
        this.initializer = initializer;
    }

    public boolean connect() throws Exception{
        if (bootstrap != null) {
            return false;
        }

        Bootstrap b = new Bootstrap();
        b.group(BootstrapHelper.getClientGroup(1))
                .channel(BootstrapHelper.getClientChannelClass())
                .handler(initializer)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.SO_KEEPALIVE, true);

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

    public void write(Object msg){
        if(channel != null){
            channel.writeAndFlush(msg);
        }
    }
    @Override
    public String toString() {
        return "tcp:\\"+ip+":"+port;
    }
}
