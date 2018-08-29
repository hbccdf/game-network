package network.protocol.codec;

import io.netty.channel.socket.SocketChannel;

public interface IProtocolCodecFactory<T> {
    IProtocolEncoder GetEncoder(SocketChannel channel);

    IProtocolDecoder<T> GetDecoder(SocketChannel channel);
}
