package network.client;

import network.handler.IProtocolHandler;
import network.protocol.codec.IProtocolCodecFactory;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class TcpConnectionFactory<T> extends BasePooledObjectFactory<TcpConnection<T>> {
    private String ip;
    private int port;
    private IProtocolCodecFactory<T> codecFactory;
    private IProtocolHandler<T> handler;

    public TcpConnectionFactory(String ip, int port, IProtocolCodecFactory<T> codecFactory, IProtocolHandler<T> handler) {
        this.ip = ip;
        this.port = port;
        this.codecFactory = codecFactory;
        this.handler = handler;
    }

    @Override
    public TcpConnection<T> create() throws Exception {
        TcpConnection<T> conn = new TcpConnection<T>(ip, port, codecFactory, handler);
        conn.connect();
        return conn;
    }

    @Override
    public PooledObject<TcpConnection<T>> wrap(TcpConnection<T> obj) {
        return new DefaultPooledObject<>(obj);
    }

    @Override
    public void destroyObject(PooledObject<TcpConnection<T>> p) throws Exception {
        TcpConnection<T> conn = p.getObject();
        conn.close();
    }

    @Override
    public boolean validateObject(PooledObject<TcpConnection<T>> p) {
        TcpConnection<T> conn = p.getObject();
        return conn.isValidate();
    }
}