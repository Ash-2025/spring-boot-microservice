package com.ash.spring_boot_microservice.controller;

import com.example.demo.entity.SoftwareEngineer;
import com.example.demo.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/sde")
public class SoftwareController {

    @GetMapping("/list")
    public List<SoftwareEngineer> getEngineers(Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return List.of(
                new SoftwareEngineer(1,"Ash", Arrays.asList("java","javascript")),
                new SoftwareEngineer(2,"Sanemi", Arrays.asList("Gleam","Erlang")),
                new SoftwareEngineer(3,user.getUserName(), Arrays.asList("Gleam","Erlang"))
        );
    }
}
