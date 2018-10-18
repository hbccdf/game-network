package network.protocol.codec;

import io.netty.buffer.ByteBuf;
import network.protocol.DefaultMessage;

public class DefaultMessageEncoder implements IProtocolEncoder {
    @Override
    public void encode(Object obj, ByteBuf out) {
        if (obj == null || !(obj instanceof DefaultMessage)) {
            return;
        }

        DefaultMessage msg = ((DefaultMessage) obj);
        out.writeInt(msg.getCmdId());
        out.writeInt(msg.getData().length);
        out.writeBytes(msg.getData());
    }
}
