package com.example.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Objects;

@RestController
public class UserController {
    private UserRepository userRepository;
    private JWTUtil jwtUtil;

    public UserController(UserRepository userRepository, JWTUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    void registerUser(@RequestBody User user){
        userRepository.save(user);
    }

    @PostMapping("/login")
    ResponseEntity<?> logUser(@RequestBody User user){
        for(User u : userRepository.findAll()){
            if(Objects.equals(u.getUsername(), user.getUsername()) && Objects.equals(u.getPassword(), user.getPassword())){
                String token = jwtUtil.generateToken(user.getUsername());
                return ResponseEntity.ok().body(Collections.singletonMap("token", token));
            }
        }
        throw new ResourceNotFoundException("User not found");
    }
}
