package com.richardvinz.Book_Management_App.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/books")
    public String books() {
        return "index";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "index";
    }

    @GetMapping("/search")
    public String search() {
        return "index";
    }

    @GetMapping("/analytics")
    public String analytics() {
        return "index";
    }
}