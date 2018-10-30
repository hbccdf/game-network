package network.client.future;

public interface IProtocolWriteFutureFactory {
    <T> ProtocolWriteFuture<T> newProtocolWriteFuture(Class<T> clz);
}
