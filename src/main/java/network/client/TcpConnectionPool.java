package network.client;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class TcpConnectionPool extends GenericObjectPool<TcpConnection> {

    public TcpConnectionPool(PooledObjectFactory<TcpConnection> factory){
        this(factory, new GenericObjectPoolConfig());
    }

    public TcpConnectionPool(PooledObjectFactory<TcpConnection> factory,
                             GenericObjectPoolConfig config){
        super(factory, config);
    }

}