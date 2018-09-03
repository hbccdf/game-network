package network.protocol.codec;

import io.netty.channel.socket.SocketChannel;
import network.protocol.InternalMessage;

public class InternalProtocolCodecFactory implements IProtocolCodecFactory<InternalMessage> {
    public static final InternalProtocolCodecFactory INSTANCE = new InternalProtocolCodecFactory();
    private InternalProtocolCodecFactory() {

    }

    @Override
    public IProtocolEncoder GetEncoder(SocketChannel channel) {
        return new InternalProtocolEncoder();
    }

    @Override
    public IProtocolDecoder<InternalMessage> GetDecoder(SocketChannel channel) {
        return new InternalProtocolDecoder();
    }
}
