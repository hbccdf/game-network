package network.session;

import io.netty.channel.Channel;

public class NioSocketSession implements ISession {
    private int connId;
    private int userId;
    private Channel channel;
    public NioSocketSession(Channel channel, int connId) {
        this.channel = channel;
        this.connId = connId;
    }

    @Override
    public int getId() {
        return connId;
    }

    @Override
    public int getUserId() {
        return userId;
    }

    @Override
    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public Object getChannelId() {
        return channel.id();
    }

    @Override
    public void write(Object obj) {
        channel.writeAndFlush(obj);
    }
}
