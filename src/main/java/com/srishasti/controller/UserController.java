package com.srishasti.controller;

import com.srishasti.model.User;
import com.srishasti.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(origins = "http://localhost:5173",allowCredentials = "true")
@RestController
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user){
        String token = userService.addUser(user);
        if(token.isEmpty())
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        String cookie = generateCookie(token);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,cookie).body("Registration Successful");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody User user){
        String token = userService.verifyUser(user);
        if(token.isEmpty())
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        String cookie = generateCookie(token);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,cookie)
                .body("Login Successful") ;

    }

    public String generateCookie( String token){
        ResponseCookie cookie = ResponseCookie.from("jwt",token)
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(60*60)
                .build();
        return cookie.toString();
    }


    @GetMapping("/profile")
    public User getProfile(){
        return userService.getCurrentUser();
    }

}
