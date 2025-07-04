package com.online_store.backend.api.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class DemoController {

    @GetMapping("/hello")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hello from /hello endpoint");
    }

    @GetMapping("/secured/hello")
    public ResponseEntity<String> saySecuredHello() {
        return ResponseEntity.ok("Hello from /secured/hello endpoint");
    }
}
