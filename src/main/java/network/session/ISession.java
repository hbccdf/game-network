package network.session;

public interface ISession {
    int getId();

    int getUserId();

    void setUserId(int userId);

    Object getChannelId();

    void write(Object obj);
}
