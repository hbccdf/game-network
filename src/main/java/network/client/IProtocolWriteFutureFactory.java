package network.client;

public interface IProtocolWriteFutureFactory {
    <T> ProtocolWriteFuture<T> newProtocolWriteFuture(Class<T> clz);
}
