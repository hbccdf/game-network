package network.client;

public interface IHandler<T> {
    void handle(T msg);
}
