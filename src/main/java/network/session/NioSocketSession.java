package network.session;

import io.netty.channel.Channel;

import java.util.Date;

public class NioSocketSession implements ISession {
    private int connId;
    private int userId;
    private Date loginTime;
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
    public Date getLoginTime() {
        return loginTime;
    }

    @Override
    public Object getChannelId() {
        return channel.id();
    }

    @Override
    public void login(int userId) {
        this.userId = userId;
        this.loginTime = new Date();
    }

    @Override
    public int logout() {
        int userId = this.userId;
        this.userId = 0;
        this.loginTime = null;
        return userId;
    }

    @Override
    public void write(Object obj) {
        channel.writeAndFlush(obj);
    }
}
