package network.initializer;

import network.handler.IProtocolHandler;
import network.protocol.DefaultMessage;
import network.protocol.codec.DefaultMessageCodecFactory;

public class DefaultProtocolInitializer extends BaseProtocolInitializer<DefaultMessage> {

    public DefaultProtocolInitializer(IProtocolHandler<DefaultMessage> handler) {
        super(DefaultMessageCodecFactory.INSTANCE, handler);
    }
}
