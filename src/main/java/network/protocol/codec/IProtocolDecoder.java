package network.protocol.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

public interface IProtocolDecoder<T> {
    void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out);
}
