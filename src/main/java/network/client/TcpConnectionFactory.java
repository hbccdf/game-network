package network.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import network.handler.IProtocolHandler;
import network.protocol.codec.IProtocolCodecFactory;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class TcpConnectionFactory extends BasePooledObjectFactory<TcpConnection> {
    private final String ip;
    private final int port;
    private final ChannelInitializer<SocketChannel> initializer;

    public TcpConnectionFactory(String ip, int port, ChannelInitializer<SocketChannel> initializer) {
        this.ip = ip;
        this.port = port;
        this.initializer = initializer;
    }

    @Override
    public TcpConnection create() throws Exception {
        TcpConnection conn = new TcpConnection(ip, port, initializer);
        conn.connect();
        return conn;
    }

    @Override
    public PooledObject<TcpConnection> wrap(TcpConnection obj) {
        return new DefaultPooledObject<>(obj);
    }

    @Override
    public void destroyObject(PooledObject<TcpConnection> p) throws Exception {
        TcpConnection conn = p.getObject();
        conn.close();
    }

    @Override
    public boolean validateObject(PooledObject<TcpConnection> p) {
        TcpConnection conn = p.getObject();
        return conn.isValidate();
    }
}