package network.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import network.core.BootstrapHelper;
import network.handler.CytxHandler;
import network.handler.IProtocolHandler;
import network.protocol.codec.CytxFrameDecoder;
import network.protocol.codec.CytxFrameEncoder;
import network.protocol.codec.IProtocolCodecFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TcpServer<T> {
    private static final Logger logger = LoggerFactory.getLogger(TcpServer.class);
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
            EventLoopGroup bossGroup = BootstrapHelper.getBossGroup(1);
            EventLoopGroup workGroup = BootstrapHelper.getWorkerGroup(nThreads);
            try {
                ServerBootstrap b = new ServerBootstrap();
                b.group(bossGroup, workGroup)
                        .channel(BootstrapHelper.getServerChannelClass())
                        .option(ChannelOption.SO_BACKLOG, 128)
                        .handler(new LoggingHandler(LogLevel.INFO))
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) throws Exception {
                                ChannelPipeline p = ch.pipeline();
                                p.addLast(new CytxFrameEncoder(codecFactory.getEncoder(ch)));
                                p.addLast(new CytxFrameDecoder<>(codecFactory.getDecoder(ch)));
                                p.addLast(new CytxHandler<>(handler));
                            }
                        })
                        .option(ChannelOption.SO_REUSEADDR, true)
                        .childOption(ChannelOption.SO_KEEPALIVE, true)
                        .childOption(ChannelOption.TCP_NODELAY, true)
                        .childOption(ChannelOption.SO_RCVBUF, 128 * 1024)
                        .childOption(ChannelOption.SO_SNDBUF, 128 * 1024);


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
