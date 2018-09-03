package network.protocol.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import network.protocol.InternalMessage;
import network.util.NetworkUtil;

import java.nio.ByteOrder;

public class InternalProtocolDecoder extends AbstractProtocolDecoder<InternalMessage> {
    private static final int HEADER_LENGTH = 24;
    private ByteOrder order = ByteOrder.LITTLE_ENDIAN;

    @Override
    public InternalMessage decode(ChannelHandlerContext ctx, ByteBuf in) {
        if (in.readableBytes() < HEADER_LENGTH) {
            return null;
        }

        in.markReaderIndex();

        short fromId = in.readShort();
        short toId = in.readShort();
        int connId = in.readInt();
        int seqId = in.readInt();
        int cmdId = in.readInt();
        int len = in.readInt();

        if (order == ByteOrder.LITTLE_ENDIAN) {
            fromId = NetworkUtil.reverseEndian(fromId);
            toId = NetworkUtil.reverseEndian(toId);
            connId = NetworkUtil.reverseEndian(connId);
            seqId = NetworkUtil.reverseEndian(seqId);
            cmdId = NetworkUtil.reverseEndian(cmdId);
            len = NetworkUtil.reverseEndian(len);
        }

        if (in.readableBytes() < len) {
            in.resetReaderIndex();
            return null;
        }

        byte[] dst = new byte[len];
        in.readBytes(dst);

        InternalMessage msg = new InternalMessage(cmdId, dst);
        msg.setFormId(fromId);
        msg.setToId(toId);
        msg.setConnId(connId);
        msg.setSeqId(seqId);
        return msg;
    }
}
