package network.protocol.codec;

import io.netty.channel.socket.SocketChannel;

public interface IProtocolCodecFactory<T> {
    IProtocolEncoder getEncoder(SocketChannel channel);

    IProtocolDecoder<T> getDecoder(SocketChannel channel);
}
