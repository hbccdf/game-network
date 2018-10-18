package network.protocol;

public class DefaultMessage extends BaseMessage<DefaultMessageHeader> {
    public DefaultMessage(int cmdId, byte[] data) {
        this(new DefaultMessageHeader(cmdId), data);
    }

    public DefaultMessage(DefaultMessageHeader header, byte[] data) {
        super(header, data);
    }

    @Override
    public int getCmdId() {
        return super.getCmdId();
    }

    @Override
    public void setCmdId(int id) {
        super.setCmdId(id);
    }

    @Override
    public byte[] getData() {
        return super.getData();
    }

    @Override
    public void setData(byte[] data) {
        super.setData(data);
    }

    @Override
    public String toString() {
        return String.format("defaultMsg[cmd=%d, len=%d]", getCmdId(), getData().length);
    }
}
