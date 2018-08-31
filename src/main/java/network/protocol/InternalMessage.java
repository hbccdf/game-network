package network.protocol;

public class InternalMessage extends BaseMessage<InternalMessageHeader> {

    public InternalMessage(int cmdId, byte[] data) {
        super(new InternalMessageHeader(cmdId), data);
    }

    public int getConnId() {
        return this.header.getConnId();
    }

    public void setConnId(int connId) {
        this.header.setConnId(connId);
    }

    public short getFromId() {
        return this.header.getFromId();
    }

    public void setFormId(short formId) {
        this.header.setFromId(formId);
    }

    public short getToId() {
        return this.header.getToId();
    }

    public void setToId(short toId) {
        this.header.setToId(toId);
    }

    public int getSeqId() {
        return header.getSeqId();
    }

    public void setSeqId(int seqId) {
        this.header.setSeqId(seqId);
    }

    @Override
    public String toString() {
        return "InternalMessage [getFromId()=" + getFromId() + ", getToId()=" + getToId() + ", getConnId()="
                + getConnId() + ", getSeqId()=" + getSeqId() + ", getCommandId()=" + getCmdId() + "]";
    }
}
