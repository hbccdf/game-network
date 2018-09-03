package network.protocol.codec;

import io.netty.buffer.ByteBuf;
import network.protocol.InternalMessage;
import network.util.NetworkUtil;

import java.nio.ByteOrder;

public class InternalProtocolEncoder implements IProtocolEncoder {
    private ByteOrder order = ByteOrder.LITTLE_ENDIAN;
    @Override
    public void encode(Object obj, ByteBuf out) {
        if (obj == null || !(obj instanceof InternalMessage)) {
            return;
        }

        InternalMessage msg = (InternalMessage)obj;
        short fromId = msg.getFromId();
        short toId = msg.getToId();
        int connId = msg.getConnId();
        int seqId = msg.getSeqId();
        int cmdId = msg.getCmdId();
        int len = msg.getData().length;

        if (order == ByteOrder.LITTLE_ENDIAN) {
            fromId = NetworkUtil.reverseEndian(fromId);
            toId = NetworkUtil.reverseEndian(toId);
            connId = NetworkUtil.reverseEndian(connId);
            seqId = NetworkUtil.reverseEndian(seqId);
            cmdId = NetworkUtil.reverseEndian(cmdId);
            len = NetworkUtil.reverseEndian(len);
        }

        out.writeShort(fromId);
        out.writeShort(toId);
        out.writeInt(connId);
        out.writeInt(seqId);
        out.writeInt(cmdId);

        out.writeInt(len);
        out.writeBytes(msg.getData());
    }
}
