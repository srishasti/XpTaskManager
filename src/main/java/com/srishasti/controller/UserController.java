package com.srishasti.controller;

import com.srishasti.context.UserContext;
import com.srishasti.model.User;
import com.srishasti.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(origins = "http://localhost:5173",allowCredentials = "true")
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

        ResponseCookie cookie = ResponseCookie.from("jwt",string)
                .httpOnly(true)
                .secure(false)
                .sameSite("None")
                .path("/")
                .maxAge(60*60)
                .build();

        System.out.println("Cookie is sent in next line.");

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,cookie.toString())
                .body("Login Successful") ;

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
