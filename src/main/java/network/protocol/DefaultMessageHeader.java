package network.protocol;

public class DefaultMessageHeader extends BaseMessageHeader {
    public DefaultMessageHeader(int cmdId) {
        this.cmdId = cmdId;
    }
}
