package network.protocol;

public interface IMessage {
    int getCmdId();
    byte[] getData();
}
