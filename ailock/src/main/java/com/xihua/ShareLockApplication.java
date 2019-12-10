package com.xihua;

import com.xihua.server.netty.LockServer;
import com.xihua.server.old.LockSocketServer;
import io.netty.channel.ChannelFuture;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.xihua.mapper")
public class ShareLockApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ShareLockApplication.class, args);
        System.out.println("====== HTTP server 启动完成 ======");
    }

    @Autowired
    private LockServer lockServer;

    @Override
    public void run(String... args) throws Exception {
        ChannelFuture future = lockServer.start("127.0.0.1", 9000);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            lockServer.destroy();
        }));
        // 服务端管道关闭的监听器并同步阻塞,直到channel关闭,线程才会往下执行,结束进程
        future.channel().closeFuture().syncUninterruptibly();
    }
}
