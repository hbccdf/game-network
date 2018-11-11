package network.server;

import network.handler.IProtocolHandler;
import network.initializer.DefaultProtocolInitializer;
import network.protocol.DefaultMessage;
import network.protocol.codec.DefaultMessageCodecFactory;

public class DefaultTcpServer extends TcpServer {
    public DefaultTcpServer(int port, int nThreads, IProtocolHandler<DefaultMessage> handler) {
        super(port, nThreads, new DefaultProtocolInitializer(handler));
    }
}
