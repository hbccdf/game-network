package network.protocol;

public interface IMessage {
    int getCmdId();
    byte[] getData();
    int getTotalLength();
}
