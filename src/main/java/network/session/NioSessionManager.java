package network.session;

import io.netty.channel.ChannelId;

import java.util.concurrent.ConcurrentHashMap;

public class NioSessionManager implements ISessionManager {
    private ConcurrentHashMap<Integer, ISession> idToSessionMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<ChannelId, ISession> channelIdToSessionMap = new ConcurrentHashMap<>();

    @Override
    public ISession getSession(int sessionId) {
        return idToSessionMap.get(sessionId);
    }

    @Override
    public ISession getSessionByChannelId(Object channelId) {
        return channelIdToSessionMap.get(channelId);
    }

    @Override
    public void addSession(ISession session) {
        if (session != null) {
            idToSessionMap.put(session.getId(), session);
            channelIdToSessionMap.put((ChannelId) session.getChannelId(), session);
        }
    }

    @Override
    public void releaseSession(int sessionId) {
        ISession session = idToSessionMap.remove(sessionId);
        if (session != null) {
            channelIdToSessionMap.remove((ChannelId) session.getChannelId(), session);
        }
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

    @Override
    public void release() {
        idToSessionMap.clear();
        channelIdToSessionMap.clear();;
    }

    private ISession[] getSessions(int[] sessionIds) {
        ISession[] sessions = new ISession[sessionIds.length];
        for (int i = 0; i < sessionIds.length; ++i) {
            sessions[i] = idToSessionMap.get(sessionIds[i]);
        }
        return sessions;
    }
}
