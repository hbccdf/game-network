package network.client;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class BaseFuture<T> implements Future<T> {
    protected long createTime = System.currentTimeMillis();
    protected boolean isDone = false;

    protected final Object lock = new Object();

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return isDone;
    }

    public void wait(long timeout, TimeUnit unit)  throws InterruptedException, ExecutionException, TimeoutException {
        if (!isDone) {
            synchronized (lock) {
                try {
                    lock.wait(TimeUnit.MILLISECONDS.convert(timeout, unit));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        }

        if (!isDone) {
            throw new TimeoutException("request timeout.");
        }
    }

    public void complete() {
        synchronized (lock) {
            isDone = true;
            lock.notifyAll();
        }
    }

    public boolean isExpired(long timeout) {
        return System.currentTimeMillis() - createTime > timeout;
    }
}
