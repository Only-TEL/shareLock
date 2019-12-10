package com.xihua.server.nio;

import com.xihua.constants.Constants;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Writer;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.LinkedBlockingQueue;


public class NIOServerCnxn extends ServerCnxn {

    static final Logger LOG = LoggerFactory.getLogger(NIOServerCnxn.class);

    private static final String ZK_NOT_SERVING = "服务无法处理当前请求";

    NIOServerCnxnFactory factory;

    final SocketChannel socketChannel;

    protected final SelectionKey selectionKey;

    protected boolean initialized;

    ByteBuffer incomingBuffer = ByteBuffer.allocate(32);

    LinkedBlockingQueue<ByteBuffer> outgoingBuffers = new LinkedBlockingQueue<>();

    int sessionTimeout;

    protected final LockServer lockServer;

    long sessionId;

    public NIOServerCnxn(LockServer zk, SocketChannel sock, SelectionKey sk, NIOServerCnxnFactory factory) throws IOException {
        this.lockServer = zk;
        this.socketChannel = sock;
        this.selectionKey = sk;
        this.factory = factory;
        this.initialized = true;
        sock.socket().setTcpNoDelay(true);
        sock.socket().setSoLinger(false, -1);
        sk.interestOps(SelectionKey.OP_READ);
    }

    @Override
    public InetSocketAddress getRemoteSocketAddress() {
        if (socketChannel.isOpen() == false) {
            return null;
        }
        return (InetSocketAddress) socketChannel.socket().getRemoteSocketAddress();
    }

    @Override
    public InetAddress getSocketAddress() {
        if (socketChannel == null) {
            return null;
        }
        return socketChannel.socket().getInetAddress();
    }

    @Override
    public int getSessionTimeout() {
        return sessionTimeout;
    }

    @Override
    public long getSessionId() {
        return sessionId;
    }

    @Override
    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
        this.factory.addSession(sessionId, this);
    }

    @Override
    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    @Override
    public int getInterestOps() {
        return selectionKey.isValid() ? selectionKey.interestOps() : 0;
    }

    @Override
    public void sendBuffer(ByteBuffer bb) {
        if (bb != ServerCnxnFactory.CLOSE_CONN) {
            // 如果buffer是空，直接返回
            if (bb.remaining() == 0) {
                return;
            }
            if (selectionKey.isValid() && ((selectionKey.interestOps() & SelectionKey.OP_WRITE) == 0)) {
                try {
                    socketChannel.write(bb);
                } catch (IOException e) {
                    LOG.error("socketChannel write error", e);
                }
            }
        }
        synchronized (this.factory) {
            selectionKey.interestOps(SelectionKey.OP_WRITE);
            selectionKey.selector().wakeup();
            LOG.info("添加一个buffer 到 outgoingBuffers, selectionKey " + selectionKey);
            outgoingBuffers.add(bb);
        }
    }

    @Override
    public void close() {
        factory.removeCnxn(this);
        closeSock();
        if (selectionKey != null) {
            selectionKey.cancel();
        }
    }

    @Override
    public void sendCloseSession() {
        sendBuffer(ServerCnxnFactory.CLOSE_CONN);
    }


    public void doIO(SelectionKey key) throws InterruptedException {
        try {
            if (isSocketOpen() == false) {
                LOG.warn("打开 session:0x{} 的socket通道失败", Long.toHexString(sessionId));
                return;
            }
            // 读就绪
            if (key.isReadable()) {
                // 客户端发送的数据都在 incomingBuffer 中
                int rc = socketChannel.read(incomingBuffer);
                if (rc < 0) {
                    LOG.error("无法从客户端sessionId: 0x{} 读取数据", Long.toHexString(sessionId));
                    throw new RuntimeException("无法从客户端sessionId: 0x" + Long.toHexString(sessionId) + " 读取数据");
                }
                // 处理command
                incomingBuffer.flip();
                dealCommand(incomingBuffer);
                incomingBuffer.clear();
            }
            if (key.isWritable()) {
                if (outgoingBuffers.size() > 0) {
                    ByteBuffer directBuffer = factory.directBuffer;
                    directBuffer.clear();
                    for (ByteBuffer b : outgoingBuffers) {
                        if (directBuffer.remaining() < b.remaining()) {
                            b = (ByteBuffer) b.slice().limit(directBuffer.remaining());
                        }
                        int p = b.position();
                        directBuffer.put(b);
                        b.position(p);
                        if (directBuffer.remaining() == 0) {
                            break;
                        }
                    }
                    directBuffer.flip();
                    int sent = socketChannel.write(directBuffer);
                    ByteBuffer bb;
                    // 移除已发送的buffer
                    while (outgoingBuffers.size() > 0) {
                        bb = outgoingBuffers.peek();
                        if (bb == ServerCnxnFactory.CLOSE_CONN) {
                            throw new RuntimeException("close requested");
                        }
                        int left = bb.remaining() - sent;
                        if (left > 0) {
                            bb.position(bb.position() + sent);
                            break;
                        }
                        sent -= bb.remaining();
                        outgoingBuffers.remove();
                    }
                }
                synchronized (this.factory) {
                    if (outgoingBuffers.size() == 0) {
                        if (!initialized && (selectionKey.interestOps() & SelectionKey.OP_READ) == 0) {
                            throw new RuntimeException("请求已关闭");
                        }
                        selectionKey.interestOps(selectionKey.interestOps() & (~SelectionKey.OP_WRITE));
                    } else {
                        selectionKey.interestOps(selectionKey.interestOps() | SelectionKey.OP_WRITE);
                    }
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
            close();
        }
    }

    private void closeSock() {
        if (socketChannel.isOpen() == false) {
            return;
        }
        try {
            socketChannel.socket().shutdownOutput();
        } catch (IOException e) {
            LOG.debug("忽略 socket shutdown output exception", e);
        }
        try {
            socketChannel.socket().shutdownInput();
        } catch (IOException e) {
            LOG.debug("忽略 socket shutdown input exception", e);

        }
        try {
            socketChannel.socket().close();
        } catch (IOException e) {
            LOG.debug("忽略 socket close exception", e);

        }
        try {
            socketChannel.close();
        } catch (IOException e) {
            LOG.debug("忽略 socketchannel close exception", e);
        }
    }

    public boolean isZKServerRunning() {
        return lockServer != null;//&& lockServer.isRunning();
    }

    protected boolean isSocketOpen() {
        return socketChannel.isOpen();
    }

    private void dealCommand(ByteBuffer rBuffer) throws IOException {
        String requestMsg = String.valueOf(CharsetUtil.US_ASCII.decode(rBuffer).array());
        LOG.info("processing resive msg >>> {} msg from {}", requestMsg, socketChannel.socket().getRemoteSocketAddress());
        processMsg(requestMsg);
    }

    private boolean processMsg(String requestMsg) {
        // 开启线程处理cmd
        if (requestMsg.startsWith(Constants.OPEN_PREFIX)) {
            OpenCommand openCommand = new OpenCommand(requestMsg.substring(Constants.OPEN_PREFIX.length()));
            openCommand.start();
            return true;
        } else if (requestMsg.startsWith(Constants.STOP_PREFIX)) {
            StopCommand stopCommand = new StopCommand(requestMsg.substring(Constants.STOP_PREFIX.length()));
            stopCommand.start();
            return true;
        }

        return false;
    }

    private class OpenCommand extends CommandThread {

        public OpenCommand(String msg) {
            super(msg);
        }

        @Override
        public void commandRun() throws IOException {
            if (!isZKServerRunning()) {
                LOG.error(ZK_NOT_SERVING);
            } else {
                LOG.info("processing open command");
                LOG.info("open back ... backId = {}", msg);
                sendResponse("open success");
            }
        }
    }

    @Override
    public void sendResponse(String reply) throws IOException {
        byte[] bytes = reply.getBytes();
        ByteBuffer sendBuffer = ByteBuffer.wrap(bytes);
        sendBuffer(sendBuffer);
    }

    private class StopCommand extends CommandThread {

        public StopCommand(String msg) {
            super(msg);
        }

        @Override
        public void commandRun() throws IOException {
            if (!isZKServerRunning()) {
                LOG.error(ZK_NOT_SERVING);
            } else {
                LOG.info("processing stop command");
                LOG.info("stop back ... backId = {}", msg);
                sendResponse("stop success");
            }
        }
    }

    private abstract class CommandThread extends Thread {

        String msg;

        public CommandThread(String msg){
            this.msg = msg;
        }

        public void run() {
            try {
                commandRun();
            } catch (IOException ie) {
                LOG.error("Error in running command ", ie);
            }
        }

        public abstract void commandRun() throws IOException;
    }

    private class SendBufferWriter extends Writer {

        private StringBuffer sb = new StringBuffer();

        private void checkFlush(boolean force) {
            if ((force && sb.length() > 0) || sb.length() > 2048) {
                sendBufferSync(ByteBuffer.wrap(sb.toString().getBytes()));
                // clear our internal buffer
                sb.setLength(0);
            }
        }

        @Override
        public void close() throws IOException {
            if (sb == null) return;
            checkFlush(true);
            sb = null; // clear out the ref to ensure no reuse
        }

        @Override
        public void flush() throws IOException {
            checkFlush(true);
        }

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            sb.append(cbuf, off, len);
            checkFlush(false);
        }
    }

    void sendBufferSync(ByteBuffer bb) {
        try {
            socketChannel.configureBlocking(true);
            if (bb != ServerCnxnFactory.CLOSE_CONN) {
                if (socketChannel.isOpen()) {
                    socketChannel.write(bb);
                }
            }
        } catch (IOException ie) {
            LOG.error("Error sending data synchronously ", ie);
        }
    }
}
