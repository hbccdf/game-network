package network.protocol.codec;

import io.netty.buffer.ByteBuf;

public interface IProtocolEncoder {
    void encode(Object msg, ByteBuf out);
}

