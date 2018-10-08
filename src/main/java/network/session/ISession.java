package network.session;

import java.util.Date;

public interface ISession {
    int getId();

    int getUserId();

    Date getLoginTime();

    Object getChannelId();

    void login(int userId);

    int logout();

    void write(Object obj);
}
