package network.client;

import network.protocol.DefaultMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class WriteFuture<T> extends BaseFuture<T> {
    private static final Logger logger = LoggerFactory.getLogger(WriteFuture.class);
    //ms
    private static final int DEFAULT_TIME_OUT = 60000;

    private DefaultMessage response;
    private IResultHandler<T> handler;

    public WriteFuture(IResultHandler<T> handler) {
        this.response = null;
        if(handler == null) {
            throw new IllegalArgumentException("result handler can't be null. ");
        }
        this.handler = handler;
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        try {
            return get(DEFAULT_TIME_OUT, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            // do nothing
        }
        return null;
    }

    @Override
    public T get(long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        wait(timeout, unit);
        return handler.handle(response);
    }

    public void setResponse(DefaultMessage msg){
        response = msg;

        complete();
    }
}
