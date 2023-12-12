package com.example.demo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@SpringBootApplication
@RestController
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
    @GetMapping("/hello")
    public ResponseEntity<String> hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new ResponseEntity<>("Ok", HttpStatus.OK);
    }

    @PostMapping("/testtemplate")
    public ResponseEntity<String> testtemplate(@RequestBody Map<String, String> data) {
        String xml = data.get("template");

        return new ResponseEntity<String>("OK", HttpStatus.OK);
    }
}