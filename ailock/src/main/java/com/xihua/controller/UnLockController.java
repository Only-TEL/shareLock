package com.xihua.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lock/open")
public class UnLockController {

    @RequestMapping("/test")
    public String test(){
        System.out.println("test");
        return "test";
    }


}
