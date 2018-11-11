package network.initializer;

import network.handler.IProtocolHandler;
import network.protocol.InternalMessage;
import network.protocol.codec.InternalProtocolCodecFactory;

public class InternalProtocolInitializer extends BaseProtocolInitializer<InternalMessage> {
    public InternalProtocolInitializer(IProtocolHandler<InternalMessage> handler) {
        super(InternalProtocolCodecFactory.INSTANCE, handler);
    }
}
