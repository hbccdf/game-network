package network.session;

import io.netty.channel.Channel;

import java.util.concurrent.atomic.AtomicInteger;

public class NioSessionFactory implements ISessionFactory {

    AtomicInteger ai = new AtomicInteger(1);

    private int generate() {
        return ai.getAndIncrement();
    }

    @Override
    public ISession newSession(Object ... args) {
        if (args.length < 1 && !(args[0] instanceof Channel)) {
            throw new IllegalArgumentException(String.format("not enough args {} , or the first arg is not netty nio channel", args.length));
        }

        Channel channel = (Channel)args[0];
        int sessionId = generate();

        ISession session = new NioSocketSession(channel, sessionId);
        return session;
    }

    @Override
    public void releaseSession(ISession session) {
        //do nothing
    }
}
