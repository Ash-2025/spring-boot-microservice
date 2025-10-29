package com.ash.spring_boot_microservice.controller;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository){
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user, HttpServletResponse response){
        String token = userService.register(user);

        Cookie cookie = new Cookie("jwt",token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // ✅ set true if using HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60); // 1h

        response.addCookie(cookie);

        return ResponseEntity.status(HttpStatus.OK).body(user.getUserName());
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user, HttpServletResponse response){
        String token = userService.login(user);

        if(token == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Details");
        }

        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60);

        response.addCookie(cookie);
        // set the token in the cookies
        return ResponseEntity.status(HttpStatus.OK).body("Login Successful");

    }
}
