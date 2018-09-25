package network.session;

import io.netty.channel.ChannelId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class NioSessionManager implements ISessionManager {
    private static Logger logger = LoggerFactory.getLogger(NioSessionManager.class);

    private ConcurrentHashMap<Integer, ISession> idToSessionMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<ChannelId, ISession> channelIdToSessionMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, ISession> userIdToSessionMap = new ConcurrentHashMap<>();

    @Override
    public ISession getSession(int sessionId) {
        return idToSessionMap.get(sessionId);
    }

    @Override
    public ISession getSessionByChannelId(Object channelId) {
        return channelIdToSessionMap.get(channelId);
    }

    @Override
    public ISession getSessionByUserId(int userId) {
        return userIdToSessionMap.get(userId);
    }

    @Override
    public void addSession(ISession session) {
        if (session != null) {
            idToSessionMap.put(session.getId(), session);
            channelIdToSessionMap.put((ChannelId) session.getChannelId(), session);
            logger.info("add session {}, current session count {}", session.getId(), idToSessionMap.size());
        }
    }

    @Override
    public void releaseSession(int sessionId) {
        ISession session = idToSessionMap.remove(sessionId);
        if (session != null) {
            channelIdToSessionMap.remove((ChannelId) session.getChannelId(), session);
            int userId = session.getUserId();
            userIdToSessionMap.remove(userId);
        }
        logger.info("remove session {}, result {}, current session count {}", sessionId, session != null, idToSessionMap.size());
    }

    @Override
    public boolean setUserId(int sessionId, int userId) {
        ISession session = getSession(sessionId);
        if (session == null) {
            return false;
        }

        ISession userSession = getSessionByUserId(userId);
        if (userSession != null) {
            return false;
        }

        userIdToSessionMap.put(userId, session);
        return true;
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
        channelIdToSessionMap.clear();
        userIdToSessionMap.clear();
    }

    private ISession[] getSessions(int[] sessionIds) {
        ISession[] sessions = new ISession[sessionIds.length];
        for (int i = 0; i < sessionIds.length; ++i) {
            sessions[i] = idToSessionMap.get(sessionIds[i]);
        }
        return sessions;
    }
}
