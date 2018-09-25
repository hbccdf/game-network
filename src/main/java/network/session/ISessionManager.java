package network.session;

public interface ISessionManager {
    ISession getSession(int sessionId);
    ISession getSessionByChannelId(Object channelId);

    ISession getSessionByUserId(int userId);

    void addSession(ISession session);
    void releaseSession(int sessionId);

    boolean setUserId(int sessionId, int userId);

    boolean sendMsg(int sessionId, Object obj);
    int broadcastMsg(int[] sessionsIds, Object obj);

    void release();
}
