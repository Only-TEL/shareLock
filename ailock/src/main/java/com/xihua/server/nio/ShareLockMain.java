package com.xihua.server.nio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * start class
 */
public class ShareLockMain {

    private static final Logger LOG = LoggerFactory.getLogger(ShareLockMain.class);

    public static void main(String[] args) throws Exception {
        ShareLockMain main = new ShareLockMain();
        main.initializeAndRun(args);
    }

    private ServerCnxnFactory cnxnFactory;

    protected void initializeAndRun(String[] args) throws Exception {
        // 解析配置文件
        ServerConfig config = new ServerConfig();
        if (args.length == 1) {
            config.parse(args[0]);
        } else {
            config.parse(args);
        }
        runFromConfig(config);
    }

    public void runFromConfig(ServerConfig config) throws IOException, InterruptedException {
        LOG.info("Starting server");
        try {
            final LockServer lockServer = new LockServer();
            // 防止服务器出错
            final CountDownLatch shutdownLatch = new CountDownLatch(1);
            // 异常回调
            lockServer.registerServerShutdownHandler(new LockServerShutdownHandler(shutdownLatch));
            cnxnFactory = ServerCnxnFactory.createFactory();
            // 初始化cnxnFactory
            cnxnFactory.configure(config.getServerPortAddress(), config.getMaxClientCnxns());
            cnxnFactory.startup(lockServer);
            shutdownLatch.await();
            shutdown();
            cnxnFactory.join();
            if (lockServer.canShutdown()) {
                lockServer.shutdown(true);
            }
        } catch (InterruptedException e) {
            LOG.warn("Server interrupted", e);
        }
    }

    protected void shutdown() {
        if (cnxnFactory != null) {
            cnxnFactory.shutdown();
        }
    }
}
