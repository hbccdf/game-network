package network.protocol.codec;

import io.netty.buffer.ByteBuf;
import network.protocol.SimpleMessage;

public class SimpleProtocolEncoder implements IProtocolEncoder {
    @Override
    public void encode(Object msg, ByteBuf out) {
        if (!(msg instanceof SimpleMessage)) {
            return;
        }

        SimpleMessage message = (SimpleMessage)msg;

        out.writeInt(message.getCmdId());

        out.writeInt(message.getData().length);
        out.writeBytes(message.getData());
    }
}
