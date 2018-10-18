package network.protocol.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import network.protocol.DefaultMessage;

public class DefaultMessageDecoder extends AbstractProtocolDecoder<DefaultMessage> {
    private static final int HEADER_LENGTH = 8;
    @Override
    public DefaultMessage decode(ChannelHandlerContext ctx, ByteBuf in) {
        if (in.readableBytes() < HEADER_LENGTH) {
            return null;
        }

        in.markReaderIndex();

        int cmdId = in.readInt();
        int len = in.readInt();

        if (in.readableBytes() < len) {
            in.resetReaderIndex();
            return null;
        }

        byte[] dst = new byte[len];
        in.readBytes(dst);
        DefaultMessage msg = new DefaultMessage(cmdId, dst);
        return msg;
    }
}
