package network.session;

public interface ISessionManager {
    ISession getSession(int sessionId);
    void releaseSession(int sessionId);

    boolean sendMsg(int sessionId, Object obj);

    int broadcastMsg(int[] sessionsIds, Object obj);
}
