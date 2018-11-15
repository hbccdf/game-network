package network.core;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class BootstrapHelper {
    private static final boolean isLinux;
    private static EventLoopGroup defaultGroup;

    static {
        isLinux = "linux".equals(System.getProperty("os.name").toLowerCase());
    }

    public static EventLoopGroup getBossGroup(int threads) {
        return newGroup(threads);
    }

    public static EventLoopGroup getWorkerGroup(int threads) {
        return newGroup(threads);
    }

    public static EventLoopGroup getDefaultGroup() {
        initDefaultGroup();
        return defaultGroup;
    }

    public static EventLoopGroup getClientGroup(int threads) {
        return newGroup(threads);
    }

    public static Class<? extends ServerSocketChannel> getServerChannelClass() {
        if (isLinux) {
            return EpollServerSocketChannel.class;
        } else {
            return NioServerSocketChannel.class;
        }
    }

    public static Class<? extends SocketChannel> getClientChannelClass() {
        if (isLinux) {
            return EpollSocketChannel.class;
        } else {
            return NioSocketChannel.class;
        }
    }

    private static void initDefaultGroup() {
        if (defaultGroup == null) {
            defaultGroup = newGroup(0);
        }
    }

    private static EventLoopGroup newGroup(int threads) {
        if (isLinux) {
            return new EpollEventLoopGroup(threads);
        } else {
            return new NioEventLoopGroup();
        }
    }

}
