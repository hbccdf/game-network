package network.client.future;

import network.client.IResultHandler;
import network.protocol.DefaultMessage;

public class WriteFuture<T> extends BaseWriteFuture<T> {
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
