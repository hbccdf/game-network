package network.client;

import network.protocol.DefaultMessage;

public interface IResultHandler<T> {
    <T> T handle(DefaultMessage msg);
}
