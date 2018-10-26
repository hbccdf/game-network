package network.protocol.codec;

import io.netty.channel.socket.SocketChannel;
import network.protocol.InternalMessage;

public class InternalProtocolCodecFactory implements IProtocolCodecFactory<InternalMessage> {
    public static final InternalProtocolCodecFactory INSTANCE = new InternalProtocolCodecFactory();
    private InternalProtocolCodecFactory() {

    }

    @Override
    public IProtocolEncoder getEncoder(SocketChannel channel) {
        return new InternalProtocolEncoder();
    }

    @Override
    public IProtocolDecoder<InternalMessage> getDecoder(SocketChannel channel) {
        return new InternalProtocolDecoder();
    }
}
