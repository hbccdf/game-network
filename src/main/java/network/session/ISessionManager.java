package network.session;

public interface ISessionManager {
    ISession getSession(int sessionId);

    void addSession(ISession session);
    void releaseSession(int sessionId);

    boolean sendMsg(int sessionId, Object obj);

    int broadcastMsg(int[] sessionsIds, Object obj);

    void release();
}
