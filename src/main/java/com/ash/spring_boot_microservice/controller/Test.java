package com.ash.spring_boot_microservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/") // when you want to add url path to a class , like a prefix you should use requestMapping for ex for any route xyz or abc , /test will become a prefix and /xyz or /abc can be defined inside this class
public class Test {
    @GetMapping("/")
    public String def(){
        return "Default Route";
    }
}
