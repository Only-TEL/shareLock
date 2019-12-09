package com.xihua.server.old;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class NIOServer {

    static final Logger LOG = LoggerFactory.getLogger(NIOServer.class);

    protected final SelectionKey selectionKey;
    final SocketChannel sock;
    NIOServerFactory factory;

    public NIOServer(SocketChannel sock, SelectionKey selectionKey, NIOServerFactory factory) {
        this.sock = sock;
        this.selectionKey = selectionKey;
        this.factory = factory;
    }


    public void close() {
        factory.removeCnxn(this);

        //closeSock();

        if (selectionKey != null) {
            try {
                selectionKey.cancel();
            } catch (Exception e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("selectionkey 取消出现异常", e);
                }
            }
        }
    }
}
