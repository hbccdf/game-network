package network.client.future;

import network.protocol.DefaultMessage;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class BaseWriteFuture<T> extends BaseFuture<T> {
    //ms
    protected static final int DEFAULT_TIME_OUT = 60000;

    protected DefaultMessage response;

    @Override
    public T get() throws InterruptedException {
        try {
            return get(DEFAULT_TIME_OUT, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            // do nothing
        }
        return null;
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException {
        wait(timeout, unit);
        return handle(response);
    }

    public void setResponse(DefaultMessage response) {
        this.response = response;
        complete();
    }

    protected abstract T handle(DefaultMessage response);
}
