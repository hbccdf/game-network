package network.protocol;

import java.util.Arrays;

public abstract class BaseMessage<T extends BaseMessageHeader> implements IMessage {
    protected T header;
    protected byte[] data;

    public BaseMessage(T header, byte[] data) {
        super();
        this.header = header;
        this.data = data;
    }

    @Override
    public int getCmdId() {
        return this.header.cmdId;
    }

    public void setCmdId(int id) {
        this.header.cmdId = id;
    }

    @Override
    public byte[] getData() {
        return this.data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return String.format("baseMsg[header=%s, data=%s]", header, Arrays.toString(data));
    }
}
