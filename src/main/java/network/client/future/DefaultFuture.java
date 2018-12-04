package network.client.future;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class DefaultFuture extends BaseFuture<Boolean> {
    private static final int DEFAULT_TIME_OUT = 10000;

    private boolean result = false;
    @Override
    public Boolean get() throws InterruptedException {
        try {
            return get(DEFAULT_TIME_OUT, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {

        }
        return false;
    }

    @Override
    public Boolean get(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException {
        wait(timeout, unit);
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;

        complete();
    }
}
