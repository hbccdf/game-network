package network.handler;

import io.netty.util.concurrent.EventExecutorGroup;

public class ThreadPerConnEventExecutorChooserFactory implements IEventExecutorChooserFactory {
    public static final ThreadPerConnEventExecutorChooserFactory INSTANCE = new ThreadPerConnEventExecutorChooserFactory();

    private ThreadPerConnEventExecutorChooserFactory() { }

    @Override
    public IEventExecutorChooser newChooser(EventExecutorGroup group) {
        return new ThreadPerConnEventExecutorChooser(group);
    }
}
