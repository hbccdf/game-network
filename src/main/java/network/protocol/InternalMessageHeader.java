package network.protocol;

public class InternalMessageHeader extends BaseMessageHeader{
    protected short fromId;
    protected short toId;
    protected int connId;
    protected int seqId;

    public InternalMessageHeader(int cmdId) {
        super();
        this.cmdId = cmdId;
    }

    public int getConnId() {
        return connId;
    }

    public void setConnId(int connId) {
        this.connId = connId;
    }

    public short getFromId() {
        return fromId;
    }

    public void setFromId(short fromId) {
        this.fromId = fromId;
    }

    public short getToId() {
        return toId;
    }

    public void setToId(short toId) {
        this.toId = toId;
    }

    public int getSeqId() {
        return seqId;
    }

    public void setSeqId(int seqId) {
        this.seqId = seqId;
    }
}
