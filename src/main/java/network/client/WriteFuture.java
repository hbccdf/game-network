package network.client;

import network.protocol.DefaultMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WriteFuture<T> extends BaseWriteFuture<T> {
    private static final Logger logger = LoggerFactory.getLogger(WriteFuture.class);

    private IResultHandler<T> handler;

    public WriteFuture(IResultHandler<T> handler) {
        this.response = null;
        if(handler == null) {
            throw new IllegalArgumentException("result handler can't be null. ");
        }
        this.handler = handler;
    }

    @Override
    protected T handle(DefaultMessage response) {
        return handler.handle(response);
    }
}
