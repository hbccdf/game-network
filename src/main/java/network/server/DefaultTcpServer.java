package network.server;

import network.handler.IProtocolHandler;
import network.protocol.DefaultMessage;
import network.protocol.codec.DefaultMessageCodecFactory;

public class DefaultTcpServer extends TcpServer<DefaultMessage> {
    public DefaultTcpServer(int port, IProtocolHandler<DefaultMessage> handler, int nThreads) {
        super(port, DefaultMessageCodecFactory.INSTANCE, handler, nThreads);
    }
}
