package com.xihua.server.old;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

public class NIOServerFactory implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(NIOServerFactory.class);

    static {
        try {
            Selector.open().close();
        } catch (IOException ie) {
            LOG.error("Selector failed to open", ie);
        }
    }

    // 选择器
    final Selector selector = Selector.open();
    // 客户端缓存
    final HashMap<InetAddress, Set<NIOServer>> ipMap = new HashMap<>();

    public NIOServerFactory() throws IOException {
    }

    // 服务端信道
    ServerSocketChannel serverSocketChannel;
    // 最大的客户端数
    int maxClients = 60;
    // 处理channel线程
    Thread thread;

    // 配置服务器
    public void configure(InetSocketAddress addr, int maxcs) throws IOException {
        thread = new Thread(this, "NIOServerFactory:" + addr);
        thread.setDaemon(true);
        maxClients = maxcs;
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().setReuseAddress(true);
        LOG.info("binding to port " + addr);
        serverSocketChannel.socket().bind(addr);
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    // 启动服务器
    public void startup() {
        start();

    }

    // 开启处理线程
    public void start() {
        if (thread.getState() == Thread.State.NEW) {
            thread.start();
        }
    }

    @Override
    public void run() {
        while (!serverSocketChannel.socket().isClosed()) {
            try {
                selector.select(1000);
                Set<SelectionKey> selected;
                synchronized (this) {
                    selected = selector.selectedKeys();
                }
                for (SelectionKey k : selected) {
                    if ((k.readyOps() & SelectionKey.OP_ACCEPT) != 0) {
                        // ACCEPT 建立链接
                        SocketChannel sc = ((ServerSocketChannel) k.channel()).accept();
                        InetAddress inetAddress = sc.socket().getInetAddress();
                        int count = getClientCount(inetAddress);
                        // 检查客户端的数量
                        if (maxClients > 0 && count >= maxClients) {
                            LOG.warn("连接数量过多,最多允许" + maxClients);
                            sc.close();
                        } else {
                            LOG.info("接受来自" + sc.socket().getRemoteSocketAddress() + "的 socket 连接");
                            // 和Selector关联起来，将SocketChannel注册到selector上
                            sc.configureBlocking(false);
                            SelectionKey sk = sc.register(selector, SelectionKey.OP_READ);
                            // 为每一个channel和SelectionKey创建ServerCnxn
                            NIOServer cnxn = createConnection(sc, sk);
                            // 关联selectionKey与ServerCnxn
                            sk.attach(cnxn);
                            // 将cnxn加入到cnxns
                            addCnxn(cnxn);
                        }
                    } else if ((k.readyOps() & (SelectionKey.OP_READ | SelectionKey.OP_WRITE)) != 0) {
                        // 可读或者可写的操作类型
                        NIOServer c = (NIOServer) k.attachment();
                        //c.doIO(k);
                    } else {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Unexpected ops in select " + k.readyOps());
                        }
                    }
                }
                selected.clear();
            } catch (RuntimeException e) {
                LOG.warn("Ignoring unexpected runtime exception", e);
            } catch (Exception e) {
                LOG.warn("Ignoring exception", e);
            }
        }
        closeAll();
        LOG.info("NIOServerCnxn factory exited run method");
    }

    private int getClientCount(InetAddress cl) {
        synchronized (ipMap) {
            Set<NIOServer> set = ipMap.get(cl);
            if (set == null) return 0;
            return set.size();
        }
    }

    protected NIOServer createConnection(SocketChannel sock, SelectionKey sk) throws IOException {
        return new NIOServer(sock, sk, this);
    }

    protected final HashSet<NIOServer> cnxns = new HashSet<>();

    private void addCnxn(NIOServer server) {
        synchronized (server) {
            cnxns.add(server);
            synchronized (ipMap) {
                InetAddress addr = server.sock.socket().getInetAddress();
                Set<NIOServer> s = ipMap.get(addr);
                if (s == null) {
                    s = new HashSet<>(2);
                    s.add(server);
                    ipMap.put(addr, s);
                } else {
                    s.add(server);
                }
            }
        }
    }
    public synchronized void closeAll() {
        selector.wakeup();
        HashSet<NIOServer> servers;
        synchronized (this.cnxns) {
            servers = (HashSet<NIOServer>) this.cnxns.clone();
        }
        for (NIOServer server : servers) {
            try {
                server.close();
            } catch (Exception e) {
                LOG.warn("连接关闭异常");
            }
        }
    }
    public void removeCnxn(NIOServer cnxn) {

    }
}
