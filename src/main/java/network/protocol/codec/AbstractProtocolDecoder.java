package network.protocol.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

public abstract class AbstractProtocolDecoder<T> implements IProtocolDecoder<T> {
    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        T obj = decode(ctx, in);
        if (obj != null) {
            out.add(obj);
        }
    }

    public abstract T decode(ChannelHandlerContext ctx, ByteBuf in);
}
