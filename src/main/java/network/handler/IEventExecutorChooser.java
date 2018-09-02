package network.handler;

import io.netty.util.concurrent.EventExecutor;

public interface IEventExecutorChooser {
    EventExecutor choose(Object... args);
}
