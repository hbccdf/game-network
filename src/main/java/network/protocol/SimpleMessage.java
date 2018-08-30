package network.protocol;

public class SimpleMessage extends BaseMessage<SimpleMessageHeader>{

    public SimpleMessage(int cmdId, byte[] data) {
        super(new SimpleMessageHeader(cmdId), data);
    }

    @Override
    public String toString() {
        return String.format("simpleMsg[cmd=%d]", getCmdId());
    }
}
