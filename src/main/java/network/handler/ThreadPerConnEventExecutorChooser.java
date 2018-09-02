package network.handler;

import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.EventExecutorGroup;
import network.protocol.InternalMessage;

import java.util.ArrayList;
import java.util.Iterator;

public class ThreadPerConnEventExecutorChooser implements IEventExecutorChooser{
    private ArrayList<EventExecutor> executors;
    private EventExecutorGroup group;

    public ThreadPerConnEventExecutorChooser(EventExecutorGroup group) {
        this.group = group;
        this.executors = new ArrayList<EventExecutor>();
        Iterator<EventExecutor> it = this.group.iterator();
        while(it.hasNext()){
            this.executors.add(it.next());
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
