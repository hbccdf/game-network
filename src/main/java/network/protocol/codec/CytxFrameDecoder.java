package network.protocol.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class CytxFrameDecoder<T> extends ByteToMessageDecoder {
    private final IProtocolDecoder<T> decoder;

    public CytxFrameDecoder(IProtocolDecoder<T> decoder) {
        super();
        this.decoder = decoder;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (decoder != null) {
            decoder.decode(channelHandlerContext, byteBuf, list);
        }
    }
}
