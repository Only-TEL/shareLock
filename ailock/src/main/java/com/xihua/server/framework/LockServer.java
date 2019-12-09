package com.xihua.server.framework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LockServer implements SessionTracker.SessionExpirer {
    protected static final Logger LOG = LoggerFactory.getLogger(LockServer.class);
    public static final int DEFAULT_TICK_TIME = 3000;

    protected volatile ServerState state = ServerState.INITIAL;

    private ServerCnxnFactory serverCnxnFactory;
    private SessionTracker sessionTracker;
    private LockServerShutdownHandler shutdownHandler;
    private SessionDatabase sessionDatabase;

    public LockServer(){
        sessionDatabase = new SessionDatabase();
    }

    public boolean canShutdown() {
        return state == ServerState.RUNNING || state == ServerState.ERROR;
    }

    public void shutdown() {
        shutdown(false);
    }

    public void shutdown(boolean b) {
        if(!canShutdown()){
            LOG.error("server is not running ,shutdown fail");
        }
        LOG.info("shutting down");
        setState(ServerState.SHUTDOWN);
        if (sessionTracker != null) {
            sessionTracker.shutdown();
        }
    }

    public void registerServerShutdownHandler(LockServerShutdownHandler handler) {
        this.shutdownHandler = handler;
    }

    @Override
    public void expire(SessionTracker.Session session) {
        long sessionId = session.getSessionId();
        close(sessionId);
    }

    private void close(long sessionId) {
        this.sessionTracker.setSessionClosing(sessionId);
        LOG.info("close session for sessionId : {}", sessionId);
    }

    @Override
    public long getServerId() {
        return 0;
    }

    public void startup() {
        if (sessionTracker == null) {
            createSessionTracker();
        }
        // start session tracker
        startSessionTracker();
        setState(ServerState.RUNNING);
    }

    public void createSessionTracker() {
        sessionTracker = new SessionTrackerImpl(this, sessionDatabase.getSessionWithTimeOuts(), DEFAULT_TICK_TIME, 1);
    }

    public void startSessionTracker() {
        ((SessionTrackerImpl) sessionTracker).start();
    }

    public ServerState getState() {
        return state;
    }

    public void setState(ServerState state) {
        this.state = state;
    }

    public SessionTracker getSessionTracker() {
        return sessionTracker;
    }

    public void setSessionTracker(SessionTracker sessionTracker) {
        this.sessionTracker = sessionTracker;
    }

    public LockServerShutdownHandler getShutdownHandler() {
        return shutdownHandler;
    }

    public ServerCnxnFactory getServerCnxnFactory() {
        return serverCnxnFactory;
    }

    public void setServerCnxnFactory(ServerCnxnFactory serverCnxnFactory) {
        this.serverCnxnFactory = serverCnxnFactory;
    }
}
