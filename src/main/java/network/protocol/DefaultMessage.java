package network.protocol;

public class DefaultMessage extends BaseMessage<DefaultMessageHeader> {
    public DefaultMessage(int cmdId, byte[] data) {
        this(new DefaultMessageHeader(cmdId), data);
    }

    public DefaultMessage(DefaultMessageHeader header, byte[] data) {
        super(header, data);
    }

    @Override
    public String toString() {
        return String.format("defaultMsg[cmd=%d, len=%d]", getCmdId(), getData().length);
    }
}
