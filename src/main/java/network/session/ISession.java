package network.session;

import java.util.Date;

public interface ISession {
    int getId();

    int getUserId();

    Date getLoginTime();

    Date getCreateTime();

    Object getChannelId();

    int getRecvBytes();

    int getSendBytes();

    String getAddressInfo();

    void login(int userId);

    int logout();

    void onRecv(Object obj);

    void write(Object obj);
}
