package network.client.future;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class BaseFuture<T> implements Future<T> {
    private final long createTime = System.currentTimeMillis();
    private boolean isDone = false;

    private final Object lock = new Object();

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

    void wait(long timeout, TimeUnit unit)  throws InterruptedException, TimeoutException {
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

    void complete() {
        synchronized (lock) {
            isDone = true;
            lock.notifyAll();
        }
    }

    public boolean isExpired(long timeout) {
        return System.currentTimeMillis() - createTime > timeout;
    }
}
