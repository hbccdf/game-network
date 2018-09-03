package network.server;

import network.handler.IProtocolHandler;
import network.protocol.InternalMessage;
import network.protocol.codec.InternalProtocolCodecFactory;

public class InternalTcpServer extends TcpServer<InternalMessage> {

    public InternalTcpServer(int port, IProtocolHandler<InternalMessage> handler) {
        this(port, handler, 0);
    }

    public InternalTcpServer(int port, IProtocolHandler<InternalMessage> handler, int nThreads) {
        super(port, InternalProtocolCodecFactory.INSTANCE, handler, nThreads);
    }
}
