package network.client;

import network.protocol.DefaultMessage;

public interface IResultHandler<T> {
    T handle(DefaultMessage msg);
}
