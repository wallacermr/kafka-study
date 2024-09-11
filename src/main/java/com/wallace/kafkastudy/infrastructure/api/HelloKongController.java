package com.wallace.kafkastudy.infrastructure.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/hello")
public class HelloKongController {

    @GetMapping
    public String say() {
        return "HELLO";
    }
}
