package com.xihua;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.xihua.mapper")
public class ShareLockApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShareLockApplication.class, args);
        System.out.println("启动完成");
    }
}
