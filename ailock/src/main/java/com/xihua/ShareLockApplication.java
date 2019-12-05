package com.xihua;

import com.xihua.server.LockSocketServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.xihua.mapper")
public class ShareLockApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShareLockApplication.class, args);
        System.out.println("HTTP server 启动完成");
        // 启动socket服务
        LockSocketServer server = new LockSocketServer();
        server.startSocketServer(8088);
    }
}
