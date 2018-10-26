package network.protocol.codec;

import io.netty.channel.socket.SocketChannel;
import network.protocol.SimpleMessage;

public class SimpleProtocolCodecFactory implements IProtocolCodecFactory<SimpleMessage> {
    @Override
    public IProtocolEncoder getEncoder(SocketChannel channel) {
        return new SimpleProtocolEncoder();
    }

    @Override
    public IProtocolDecoder<SimpleMessage> getDecoder(SocketChannel channel) {
        return new SimpleProtocolDecoder();
    }
}
