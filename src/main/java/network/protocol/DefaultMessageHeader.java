package network.protocol;

public class DefaultMessageHeader extends BaseMessageHeader {
    public DefaultMessageHeader(int cmdId) {
        this.cmdId = cmdId;
    }

    @Override
    public int getLength() {
        return 4;
    }
}
