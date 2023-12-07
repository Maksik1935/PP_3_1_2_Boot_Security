package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewController {

    //А мы можем это прописать в конфиге, чтобы не плодить контроллеры ради 1 страницы?
    //Если добавить страницу в webSecurityConfig - скрипты js не подтягиваются, почему-то(
    @GetMapping ("/user")
    public String getUserPage() {
        return "user";
    }

    @GetMapping("/admin")
    public String getPage() {
        return "admin";
    }
}
