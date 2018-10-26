package network.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import network.handler.CytxHandler;
import network.handler.IProtocolHandler;
import network.protocol.codec.CytxFrameDecoder;
import network.protocol.codec.CytxFrameEncoder;
import network.protocol.codec.IProtocolCodecFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TcpServer<T> {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private int port;
    private IProtocolCodecFactory<T> codecFactory;
    private IProtocolHandler<T> handler;

    private int nThreads;
    private Channel channel = null;

    public TcpServer(int port, IProtocolCodecFactory<T> codecFactory, IProtocolHandler<T> handler, int nThreads) {
        if (port <= 0) {
            throw new IllegalArgumentException("Illegal server port: " + port);
        }

        this.port = port;
        this.codecFactory = codecFactory;
        this.handler = handler;
        this.nThreads = nThreads;
    }

    public void start() throws Exception{
        new Thread(()->{
            EventLoopGroup bossGroup = new NioEventLoopGroup(1);
            EventLoopGroup workGroup = new NioEventLoopGroup(nThreads);
            try {
                ServerBootstrap b = new ServerBootstrap();
                b.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new CytxFrameEncoder(codecFactory.getEncoder(ch)));
                            p.addLast(new CytxFrameDecoder<T>(codecFactory.getDecoder(ch)));
                            p.addLast(new CytxHandler<T>(handler));
                        }
                    })
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true);

                ChannelFuture f = b.bind(port).sync();
                channel = f.channel();
                channel.closeFuture().sync();

            } catch (Exception e){
                logger.error("Fail listen port: {}, {}", port, e.toString());
            } finally {
                // Shut down all event loops to terminate all threads.
                bossGroup.shutdownGracefully();
                workGroup.shutdownGracefully();
            }
        }).start();
    }

    public boolean stop(){
        if (channel != null) {
            channel.close();
        }
        return true;
    }
}
