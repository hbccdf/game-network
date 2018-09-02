package network.handler;

import io.netty.util.concurrent.EventExecutorGroup;

public interface IEventExecutorChooserFactory {
    IEventExecutorChooser newChooser(EventExecutorGroup group);
}
