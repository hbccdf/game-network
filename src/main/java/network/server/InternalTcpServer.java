package network.server;

import network.handler.IProtocolHandler;
import network.initializer.InternalProtocolInitializer;
import network.protocol.InternalMessage;
import network.protocol.codec.InternalProtocolCodecFactory;

public class InternalTcpServer extends TcpServer {

    public InternalTcpServer(int port, IProtocolHandler<InternalMessage> handler) {
        this(port, 0, handler);
    }

    public InternalTcpServer(int port, int nThreads, IProtocolHandler<InternalMessage> handler) {
        super(port, nThreads, new InternalProtocolInitializer(handler));
    }
}
