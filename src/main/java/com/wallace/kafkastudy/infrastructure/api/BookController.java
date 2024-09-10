package com.wallace.kafkastudy.infrastructure.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
public class BookController {

    @GetMapping
    ResponseEntity<Void> getBooks() {
        return ResponseEntity.ok().build();
    }
}
