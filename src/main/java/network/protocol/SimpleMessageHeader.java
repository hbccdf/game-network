package network.protocol;

public class SimpleMessageHeader extends BaseMessageHeader{
    public SimpleMessageHeader(int cmdId) {
        this.cmdId = cmdId;
    }
}
