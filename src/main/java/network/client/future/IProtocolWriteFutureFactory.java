package network.client.future;

public interface IProtocolWriteFutureFactory {
    <T> BaseProtocolWriteFuture<T> newProtocolWriteFuture(Class<T> clz);
}
