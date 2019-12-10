package com.xihua.server.nio;


import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;


public abstract class ServerCnxn {

    public final static Object me = new Object();

    public abstract InetAddress getSocketAddress();

    public abstract InetSocketAddress getRemoteSocketAddress();

    public abstract long getSessionId();

    public abstract int getSessionTimeout();

    public abstract void close();

    public abstract void sendResponse(String reply) throws IOException;

    public abstract void sendCloseSession();

    public abstract int getInterestOps();

    public abstract void setSessionId(long sessionId);

    public abstract void sendBuffer(ByteBuffer closeConn);

    public abstract void setSessionTimeout(int sessionTimeout);

}
