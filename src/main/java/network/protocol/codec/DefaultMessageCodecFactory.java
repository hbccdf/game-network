package network.protocol.codec;

import io.netty.channel.socket.SocketChannel;
import network.protocol.DefaultMessage;

public class DefaultMessageCodecFactory implements IProtocolCodecFactory<DefaultMessage> {

    public static final DefaultMessageCodecFactory INSTANCE = new DefaultMessageCodecFactory();
    private DefaultMessageCodecFactory() {

    }
    @Override
    public IProtocolEncoder GetEncoder(SocketChannel channel) {
        return new DefaultMessageEncoder();
    }

    @Override
    public IProtocolDecoder<DefaultMessage> GetDecoder(SocketChannel channel) {
        return new DefaultMessageDecoder();
    }
}
