package com.srishasti.controller;

import com.srishasti.context.UserContext;
import com.srishasti.model.User;
import com.srishasti.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user){
        userService.addUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody User user){
        String string = userService.verifyUser(user);
        if(string.isEmpty())
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(string, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<String> home(){
        return new ResponseEntity<>(" pls work. your user id is "+ UserContext.getUserId(), HttpStatus.OK);
    }

    @GetMapping("/profile")
    public User getProfile(){
        return userService.getCurrentUser();
    }

}
