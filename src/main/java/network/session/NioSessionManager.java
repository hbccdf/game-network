package network.session;

import java.util.concurrent.ConcurrentHashMap;

public class NioSessionManager implements ISessionManager {
    private ConcurrentHashMap<Integer, ISession> idToSessionMap;
    @Override
    public ISession getSession(int sessionId) {
        return idToSessionMap.get(sessionId);
    }

    @Override
    public void addSession(ISession session) {
        if (session != null) {
            idToSessionMap.put(session.getId(), session);
        }
    }

    @Override
    public void releaseSession(int sessionId) {
        idToSessionMap.remove(sessionId);
    }

    @Override
    public boolean sendMsg(int sessionId, Object obj) {
        ISession session = getSession(sessionId);
        if (session != null) {
            session.write(obj);
            return true;
        }
        return false;
    }

    @Override
    public int broadcastMsg(int[] sessionsIds, Object obj) {
        ISession[] sessions = getSessions(sessionsIds);
        int sessionsCount = 0;
        for (ISession session : sessions) {
            if (session != null) {
                session.write(obj);
                sessionsCount++;
            }
        }
        return sessionsCount;
    }


    private ISession[] getSessions(int[] sessionIds) {
        ISession[] sessions = new ISession[sessionIds.length];
        for (int i = 0; i < sessionIds.length; ++i) {
            sessions[i] = idToSessionMap.get(sessionIds[i]);
        }
        return sessions;
    }
}
