package network.client;

public abstract class ProtocolWriteFuture<T> extends BaseWriteFuture<T> {
    protected Class<T> clz;

    public ProtocolWriteFuture(Class<T> clz) {
        this.clz = clz;
        if (clz == null) {
            throw new IllegalArgumentException("clz can't be null. ");
        }
    }
}
