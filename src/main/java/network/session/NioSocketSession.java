package network.session;

import io.netty.channel.Channel;

public class NioSocketSession implements ISession {
    private int connId;
    private Channel channel;
    public NioSocketSession(Channel channel, int connId) {
        this.channel = channel;
    }

    @Override
    public int getId() {
        return connId;
    }

    @Override
    public void write(Object obj) {
        channel.writeAndFlush(obj);
    }
}
