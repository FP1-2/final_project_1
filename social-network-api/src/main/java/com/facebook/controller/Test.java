package com.facebook.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("ping")
@RestController
public class Test {
    @GetMapping
    public Object pong() {
        return "pong";
    }
}
