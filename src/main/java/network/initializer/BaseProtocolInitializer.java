package network.initializer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import network.handler.CytxHandler;
import network.handler.IProtocolHandler;
import network.protocol.codec.CytxFrameDecoder;
import network.protocol.codec.CytxFrameEncoder;
import network.protocol.codec.IProtocolCodecFactory;

public class BaseProtocolInitializer<T> extends ChannelInitializer<SocketChannel> {
    private final IProtocolCodecFactory<T> codecFactory;
    private final IProtocolHandler<T> handler;

    public BaseProtocolInitializer(IProtocolCodecFactory<T> codecFactory, IProtocolHandler<T> handler) {
        this.codecFactory = codecFactory;
        this.handler = handler;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
        p.addLast(new CytxFrameEncoder(codecFactory.getEncoder(ch)));
        p.addLast(new CytxFrameDecoder<>(codecFactory.getDecoder(ch)));
        p.addLast(new CytxHandler<>(handler));
    }
}
