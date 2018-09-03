package network.session;

public interface ISession {
    int getId();

    Object getChannelId();

    void write(Object obj);
}
