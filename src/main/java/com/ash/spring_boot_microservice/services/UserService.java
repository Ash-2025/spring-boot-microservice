package com.ash.spring_boot_microservice.services;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }


    public String register(@RequestBody User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        System.out.println(user);
        user.setId(null);
        userRepository.save(user);
        return jwtService.generateToken(user);
    }
    public String login(@RequestBody User user){
        var u = userRepository.findByUserName(user.getUserName());
        if(u != null && passwordEncoder.matches(user.getPassword(), u.getPassword())){
            // generate jwt token here
            return jwtService.generateToken(user);

        } else {
            return null;
        }

        /*
         Authentication authenticaiton = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                user.getusername(), user.getPassword()
               )
           );
        */
    }
}
