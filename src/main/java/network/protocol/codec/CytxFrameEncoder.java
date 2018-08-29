package network.protocol.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class CytxFrameEncoder extends MessageToByteEncoder<Object> {
    private IProtocolEncoder encoder;

    public CytxFrameEncoder(IProtocolEncoder encoder) {
        super();
        this.encoder = encoder;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        if (encoder != null) {
            encoder.encode(o, byteBuf);
        }
    }
}
