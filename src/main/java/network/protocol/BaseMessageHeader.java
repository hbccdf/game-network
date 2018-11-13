package network.protocol;

public abstract class BaseMessageHeader {
    protected int cmdId;

    public abstract int getLength();

    @Override
    public String toString() {
        return String.format("baseMsgHeader[cmdId=%d]", cmdId);
    }
}
