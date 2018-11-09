package network.handler;

import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.EventExecutorGroup;
import network.protocol.InternalMessage;

import java.util.ArrayList;

public class ThreadPerConnEventExecutorChooser implements IEventExecutorChooser{
    private final ArrayList<EventExecutor> executors;

    public ThreadPerConnEventExecutorChooser(EventExecutorGroup group) {
        this.executors = new ArrayList<>();
        for (EventExecutor executor : group) {
            this.executors.add(executor);
        }
    }

    @Override
    public EventExecutor choose(Object... args) {
        try {
            InternalMessage msg = (InternalMessage) args[0];
            return executors.get(Math.abs(msg.getConnId()) % executors.size());
        } catch (Exception e) {
            throw new IllegalArgumentException("ThreadPerConnEventExecutorChooser.choose only support type of InternalMessage. ");
        }
    }
}
