package com.xihua.server.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class ServerCnxnFactory {

    public static final ByteBuffer CLOSE_CONN = ByteBuffer.allocate(0);

    protected final ConcurrentMap<Long, ServerCnxn> sessionMap = new ConcurrentHashMap<Long, ServerCnxn>();

    protected final HashSet<ServerCnxn> cnxns = new HashSet<ServerCnxn>();

    protected LockServer lockServer;

    public static ServerCnxnFactory createFactory() throws IOException {
        return new NIOServerCnxnFactory();
    }

    public static ServerCnxnFactory createFactory(int clientPort, int maxClientCnxns) throws IOException {
        return createFactory(new InetSocketAddress(clientPort), maxClientCnxns);
    }

    public static ServerCnxnFactory createFactory(InetSocketAddress addr, int maxClientCnxns) throws IOException {
        ServerCnxnFactory factory = createFactory();
        factory.configure(addr, maxClientCnxns);
        return factory;
    }

    public abstract void configure(InetSocketAddress addr, int maxClientCnxns) throws IOException;

    public abstract void join() throws InterruptedException;

    public abstract int getLocalPort();

    public abstract Iterable<ServerCnxn> getConnections();

    public abstract int getMaxClientCnxnsPerHost();

    public abstract InetSocketAddress getLocalAddress();

    public abstract void setMaxClientCnxnsPerHost(int max);

    public abstract void start();

    public abstract void startup(LockServer zks) throws IOException, InterruptedException;

    public abstract void shutdown();

    public abstract void closeSession(long sessionId);

    public abstract void closeAll();

    public final void setLockServer(LockServer server) {
        this.lockServer = server;
        if (server != null) {
            server.setServerCnxnFactory(this);
        }
    }

    public void addSession(long sessionId, ServerCnxn cnxn) {
        sessionMap.put(sessionId, cnxn);
    }

    public LockServer getLockServer() {
        return lockServer;
    }

    public int getNumAliveConnections() {
        synchronized (cnxns) {
            return cnxns.size();
        }
    }

}
