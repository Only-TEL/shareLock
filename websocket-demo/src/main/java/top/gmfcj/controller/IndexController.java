package top.gmfcj.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/index")
    public String toIndex() {
        return "/index.html";
    }

    @GetMapping("/send/text")
    public String sendText() {
        return "/index.html";
    }
}
