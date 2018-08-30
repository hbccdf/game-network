package network.protocol.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import network.protocol.SimpleMessage;

public class SimpleProtocolDecoder extends AbstractProtocolDecoder<SimpleMessage> {
    private static final int HEADER_LENGTH = 12;
    @Override
    public SimpleMessage decode(ChannelHandlerContext ctx, ByteBuf in) {
        if (in.readableBytes() <= HEADER_LENGTH) {
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

        SimpleMessage msg = new SimpleMessage(cmdId, dst);
        return msg;

    }
}
