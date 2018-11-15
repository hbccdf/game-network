package network.client.future;

public abstract class BaseProtocolWriteFuture<T> extends BaseWriteFuture<T> {
    protected final Class<T> clz;

    public BaseProtocolWriteFuture(Class<T> clz) {
        this.clz = clz;
        if (clz == null) {
            throw new IllegalArgumentException("clz can't be null. ");
        }
    }
}
