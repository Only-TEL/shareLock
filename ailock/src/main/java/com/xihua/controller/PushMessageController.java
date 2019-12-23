package com.xihua.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class PushMessageController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping("/index")
    public void toIndexPage(HttpServletResponse response) throws IOException {
        System.out.println("----");
        response.sendRedirect("index.html");
    }

    @PostMapping("/open/{code}")
    public void toRun(@PathVariable String code, HttpServletResponse response) throws IOException {
        System.out.println(code);
        System.out.println("准备开锁，验证操作");
    }

    @GetMapping("/toinfo")
    public void toInfo(HttpServletResponse response) throws IOException {
        System.out.println("to info page");
        // 在这里就是开锁的逻辑，会把手机号发送到小程序
        response.sendRedirect("info.html");
    }

    @GetMapping("/info")
    @ResponseBody
    public String info() throws IOException {
        System.out.println("查询出正在运行的数据，返回");
        return "success";
    }

    @GetMapping("/stop")
    public void stop(){
        // 向客户端推送消息
        simpMessagingTemplate.convertAndSend("/stop/back/14141321231", "success");
    }

}
