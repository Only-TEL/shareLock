package com.xihua;

import com.xihua.server.netty.LockServer;
import com.xihua.server.old.LockSocketServer;
import com.xihua.utils.Global;
import io.netty.channel.ChannelFuture;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.xihua.mapper")
public class ShareLockApplication implements CommandLineRunner {

    @Value("${ailock.hostname}")
    private String hostname;

    @Value("${ailock.port}")
    private int port;

    @Autowired
    private LockServer lockServer;

    public static void main(String[] args) {
        SpringApplication.run(ShareLockApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        ChannelFuture future = lockServer.start(hostname, port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            lockServer.destroy();
        }));
        // 服务端管道关闭的监听器并同步阻塞,直到channel关闭,线程才会往下执行,结束进程
        future.channel().closeFuture().syncUninterruptibly();
    }
}
